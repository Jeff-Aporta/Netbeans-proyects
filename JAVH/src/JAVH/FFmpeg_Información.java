package JAVH;

import static JAVH.Ejecutador.*;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FFmpeg_Información extends FFmpeg implements Constantes_FFmpeg, Constantes_Frecuencia {

    public static void main(String[] args) throws Exception {
        String recurso = "C:\\Users\\guillermo\\Documents\\Mega downloader\\01 - Prelude.mp3";
        String salida = "C:\\Users\\guillermo\\Documents\\Mega downloader\\01 - Prelude.txt";
        FFmpeg_Información información = new FFmpeg_Información(recurso);
        String[][] metadatos = información.Obtener_Metadatos();
        for (int i = 0; i < metadatos.length; i++) {
            System.out.println(metadatos[i][0] + " : " + metadatos[i][1]);
        }
    }

    public void Exportar_Metadatos() throws Exception {
        if (SALIDA == null || SALIDA.isEmpty()) {
            throw new RuntimeException("No se ha especificado un archivo de salida");
        }
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-f");
                add("ffmetadata");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, false);
    }

    public String[][] Obtener_Metadatos() throws Exception {
        File salida = File.createTempFile("MetadatosFFmpeg", ".txt");
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-f");
                add("ffmetadata");
                add("-y");
                add(salida.getPath());
            }
        };
        EjecutarCódigo_cmd(comandos, false);
        String lectura = Auxiliar_LectoEscrituraArchivos.LeerArchivo_ASCII(salida);
        salida.delete();
        lectura = lectura.replace(";FFMETADATA1\n", "");
        String[] filas = lectura.split("\n");
        String[][] retorno = new String[filas.length][2];
        for (int i = 0; i < filas.length; i++) {
            String fila = filas[i];
            String etiqueta = fila.substring(0, fila.indexOf("="));
            String valor = fila.substring(fila.indexOf("=") + 1);
            retorno[i][0] = etiqueta;
            retorno[i][1] = valor;
        }
        return retorno;
    }

    public FFmpeg_Información(String RECURSO) {
        super(RECURSO, null);
    }

    public FFmpeg_Información(String RECURSO, String SALIDA) {
        super(RECURSO, SALIDA);
    }

    public static FileFilter ObtenerFiltroPara_ExtensionesSoportadas() throws Exception {
        final String[] ext = Obtener_Extensiones_CodificaciónYDecodificación_Soportados();
        return (File file) -> {
            if (file.isDirectory()) {
                return false;
            }
            for (String string : ext) {
                if (file.getName().toLowerCase().endsWith("." + string)) {
                    return true;
                }
            }
            return false;
        };
    }

    public static String Obtener_Formatos_Codificación_Soportados() throws Exception {
        String retorno = "";
        String cmd = DIRECCIÓN_FFMPEG + " -formats";
        BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream()));
        String linea;
        int i = 0;
        while ((linea = br.readLine()) != null) {
            if (i++ <= 3) {
                continue;
            }
            linea = linea.trim();
            if (linea.startsWith("E") || linea.startsWith("DE")) {
                retorno += linea.substring(linea.indexOf(" ") + 1) + "\n";
            }
        }
        return retorno.substring(0, retorno.length() - 1);
    }

    public static String[] Obtener_Extensiones_CodificaciónYDecodificación_Soportados() throws Exception {
        Set<String> hs = new HashSet<>();
        for (String Extensiones_Codificación : Obtener_Extensiones_Codificación_Soportados()) {
            hs.add(Extensiones_Codificación);
        }
        for (String Extensiones_Decodificación : Obtener_Extensiones_Decodificación_Soportados()) {
            hs.add(Extensiones_Decodificación);
        }
        String[] extensiones = new String[hs.size()];
        int i = 0;
        for (String h : hs) {
            extensiones[i++] = h;
        }
        return extensiones;
    }

    public static String[] Obtener_Extensiones_Codificación_Soportados() throws Exception {
        String D = Obtener_Formatos_Codificación_Soportados().replace(", as", "").replaceAll(",", "\n");
        String[] filtro1 = D.split("\n");
        for (int i = 0; i < filtro1.length; i++) {
            if (filtro1[i].contains(" ")) {
                filtro1[i] = filtro1[i].substring(0, filtro1[i].indexOf(" "));
            }
        }
        return filtro1;
    }

    public static String[] Obtener_Extensiones_Decodificación_Soportados() throws Exception {
        String D = Obtener_Formatos_Decodificación_Soportados().replaceAll(",", "\n");
        String[] filtro1 = D.split("\n");
        for (int i = 0; i < filtro1.length; i++) {
            if (filtro1[i].contains(" ")) {
                filtro1[i] = filtro1[i].substring(0, filtro1[i].indexOf(" "));
            }
        }
        return filtro1;
    }

    public static String Obtener_Formatos_Decodificación_Soportados() throws Exception {
        String retorno = "";
        String cmd = DIRECCIÓN_FFMPEG + " -formats";
        BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream()));
        String linea;
        int i = 0;
        while ((linea = br.readLine()) != null) {
            if (i++ <= 3) {
                continue;
            }
            linea = linea.trim();
            if (linea.startsWith("D")) {
                retorno += linea.substring(linea.indexOf(" ") + 1).trim() + "\n";
            }
        }
        return retorno;
    }

    public static String Obtener_Formatos_CodificaciónYDecodificación_Soportados() throws Exception {
        String retorno = "";
        String cmd = DIRECCIÓN_FFMPEG + " -formats";
        BufferedReader br = new BufferedReader(
                new InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream())
        );
        String linea;
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            retorno += linea + "\n";
        }
        return retorno.substring(0, retorno.length() - 1);
    }

    public int Apróx_SegundosEnElRecurso(int FPS) throws Exception {
        return Apróx_FotogramasQueCabenEnElRecurso(1);
    }

    public int Apróx_FotogramasQueCabenEnElRecurso(int FPS) throws Exception {
        File dir = new File(TMP_DIR + "\\FPS SOURCE" + (int) (Math.random() * 9999999));
        try {
            if (dir.exists()) {
                for (File listFile : dir.listFiles()) {
                    listFile.delete();
                }
                dir.delete();
            }
        } catch (Exception e) {
        }
        dir.mkdirs();
        String Fotogramas = dir.getPath() + "\\%07d.png";
        FFmpeg_Frecuencia frecuencia = new FFmpeg_Frecuencia(RECURSO, Fotogramas);
        frecuencia.FPS = FPS;
        frecuencia.CRF = 51;
        frecuencia.DIMENSIÓN = new Dimension(1, 1);
        frecuencia.GenerarVideoDeLaFrecuencia();
        int Frames = dir.listFiles().length;
        new Thread() {
            {
                start();
            }

            @Override
            public void run() {
                try {
                    for (File listFile : dir.listFiles()) {
                        listFile.delete();
                    }
                } catch (Exception e) {
                }
                dir.delete();
            }
        };
        return Frames;
    }
}
