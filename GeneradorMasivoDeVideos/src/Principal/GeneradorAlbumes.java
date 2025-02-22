package Principal;

import HerramientasGUI.VentanaGráfica;
import JAVH.FFmpeg;
import JID3.Etiquetas;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import static Principal.ConstantesGeneradoresVideo.*;
import javax.swing.JOptionPane;

public class GeneradorAlbumes implements ConstantesGeneradoresVideo {

    final static byte PROYECTO = ESPECTRO_CIRCULAR;

    final static ArrayList<ArrayList<File>> Carpetas_Mp3 = new ArrayList<>();

    public static void main(String[] args) {
        switch (PROYECTO) {
            case YOUTUBE:
                CargarCarpetasMp3(
                        new File(
                                "C:\\Users\\Àngel\\Documents\\Musica\\Youtube Audio Library"
                        )
                );
                for (int i = 0; !Carpetas_Mp3.isEmpty(); i++) {
                    ArrayList<File> arrayList = Carpetas_Mp3.get(0);
                    Carpetas_Mp3.remove(0);
                    for (int j = 0; j <= 20; j++) {
                        try {
                            IniciarGenerador(arrayList, j);
                            Collections.shuffle(arrayList);
                        } catch (Exception e) {
                            break;
                        }
                    }
                }
                break;
            case ESPECTRO_CIRCULAR:
            case SIMPLE_CRONOMETRO:
                GenerarAlbumesMusica("C:\\Users\\guillermo\\Documents\\Mega downloader\\Bring Me The Horizon\\2006 - Count Your Blessings");
                break;
        }
    }

    public static ArrayList<File> GenerarAlbumesMusica(String dir) {
        ArrayList<File> rutas = new ArrayList<>();
        CargarCarpetasMp3(new File(dir));
        for (int i = 0; !Carpetas_Mp3.isEmpty(); i++) {
            ArrayList<File> arrayList = Carpetas_Mp3.get(0);
            Carpetas_Mp3.remove(0);
            try {
                File ruta = IniciarGenerador(arrayList, 0);
                System.err.println("El generador de albumes retornó: " + ruta);
                if (ruta.exists()) {
                    rutas.add(ruta);
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        for (int i = 0; i < rutas.size(); i++) {
            System.out.println(rutas.get(i).getPath());
        }
        return rutas;
    }

    public static File IniciarGenerador(ArrayList<File> ArchivosMp3, int indice) throws Exception {
        String Carpeta;
        Carpeta = ArchivosMp3.get(0).getPath().replace("\\" + ArchivosMp3.get(0).getName(), "");
        String[] Aux = Carpeta.split("\\\\");
        Carpeta = Carpeta + "\\" + Aux[Aux.length - 1] + " - Full";
        if (!new File(Carpeta).exists()) {
            System.err.println("se creó la carpeta " + Carpeta);
            new File(Carpeta).mkdirs();
        }
        Etiquetas etiquetas = new Etiquetas(ArchivosMp3.get(0), false);
        if (etiquetas.TÍTULO.isEmpty()) {
            etiquetas = new Etiquetas(ArchivosMp3.get(0), true);
        }
        String Ruta_VideoSalida;
        String nombre = "";
        {
            switch (PROYECTO) {
                case YOUTUBE:
                    nombre += Aux[Aux.length - 1];
                    break;
                case ESPECTRO_CIRCULAR:
                case SIMPLE_CRONOMETRO:
                    nombre += etiquetas.ARTISTA + " - " + etiquetas.ALBÚM;
                    break;
                default:
                    throw new RuntimeException("El tipo de proyecto de generación no se reconoce");
            }
            nombre += " - ";
            if (indice > 0) {
                nombre += "Mix #" + indice + " ";
            }
            switch (PROYECTO) {
                case YOUTUBE:
                    nombre += "Youtube Audio Library {Music for streaming and creators}";
                    break;
                case ESPECTRO_CIRCULAR:
                case SIMPLE_CRONOMETRO:
                    nombre += "FULL ALBUM {COMPLETO}";
                    break;
                default:
                    throw new RuntimeException("El tipo de proyecto de generación no se reconoce");
            }
            nombre += ".mp4";
            nombre = nombre
                    .replace("[", "(")
                    .replace("]", ")");
            nombre = LimpiarNombreDeCaracteresNoVálidos(nombre);
        }
        Ruta_VideoSalida = Carpeta + "\\" + nombre;
        File ArchivoVideoSalida = new File(Ruta_VideoSalida);
        {
            if (ArchivoVideoSalida.exists()) {
                return ArchivoVideoSalida;
            }
            if (PROYECTO == YOUTUBE) {
                File ArchivoVideoSalida_Alt = new File(
                        Ruta_VideoSalida.replace("{Music}", "{Music for streaming and creators}")
                );
                if (ArchivoVideoSalida_Alt.exists()) {
                    return ArchivoVideoSalida_Alt;
                }
            }
        }
        if (PROYECTO == YOUTUBE) {
            ArchivosMp3 = ListaSegúnDuración(ArchivosMp3);
        }

        ArrayList<String> videos = new ArrayList<>();
        GeneradorDeSencillos.PROYECTO = PROYECTO;
        for (File archivoMp3 : ArchivosMp3) {
            String rutavideo = GeneradorDeSencillos.GenerarRutasVideo(archivoMp3)[1];
            rutavideo = rutavideo
                    .replace("[", "(")
                    .replace("]", ")");
            if (new File(rutavideo).exists()) {
                videos.add(rutavideo);
            }
        }
        //Si los videos no existen
        if (!videos.isEmpty()) {
            String[] rvideos = new String[videos.size()];
            for (int i = 0; i < rvideos.length; i++) {
                rvideos[i] = videos.get(i);
            }
            System.out.println("Se generará el video concatenado");
            Thread t = new Thread() {
                @Override
                public void run() {
                    String temp = Ruta_VideoSalida.replace(".mp4", "temp.mp4");
                    FFmpeg FFmpeg = new FFmpeg("", temp);
                    try {
                        FFmpeg.ConcatenarVideos_Opción2_CreandoLista(rvideos);
                        FFmpeg = new FFmpeg(temp, Ruta_VideoSalida);
                        FFmpeg.velocidad(1);
                        if (!new File(temp).delete()) {
                            JOptionPane.showMessageDialog(null, "no se pudo eliminar el video: " + Ruta_VideoSalida);
                        }
                    } catch (Exception ex) {
                        System.out.println("Concatenador de videos falló");
                        ex.printStackTrace();
                    }
                }
            };
            t.start();
            long tiempo_inicio = System.currentTimeMillis();
            while (t.isAlive()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                }
                double s_transcurrido = (System.currentTimeMillis() - tiempo_inicio) / 1000;
                System.out.println(s_transcurrido);
                if (s_transcurrido > 300) {
                    break;
                }
            }
        }
        return ArchivoVideoSalida;
    }

    public static ArrayList<File> ListaSegúnDuración(ArrayList<File> ArchivosMp3) throws Exception {
        int duración = 0;
        for (int i = 0; i < ArchivosMp3.size(); i++) {
            File file = ArchivosMp3.get(i);
            Etiquetas etiquetas = new Etiquetas(file, true);
            duración += etiquetas.SEGUNDOS;
            if (duración > Duración_Máxima_Segundos) {
                ArrayList<File> temp = new ArrayList<>();
                temp.addAll(ArchivosMp3);
                for (int j = i; j < ArchivosMp3.size(); j++) {
                    temp.remove(i);
                }
                System.out.println("Duración: " + duración + " S = " + duración / 60f + " M = " + duración / (60 * 60f) + " H");
                return temp;
            }
        }
        System.out.println("Duración: " + duración + " S = " + duración / 60f + " M = " + duración / (60 * 60f) + " H");
        return ArchivosMp3;
    }

    public static void CargarCarpetasMp3(File CarpetaRaíz) {
        ArrayList<File> ArchivosMp3 = new ArrayList<>();
        for (File file : CarpetaRaíz.listFiles(FiltroArchivosAudio())) {
            ArchivosMp3.add(file);
        }
        if (!ArchivosMp3.isEmpty()) {
            Carpetas_Mp3.add(ArchivosMp3);
        }
        File[] carpetas = CarpetaRaíz.listFiles(FiltroCarpetas());
        for (File carpeta : carpetas) {
            if (!carpeta.getPath().toLowerCase().contains("- full")) {
                CargarCarpetasMp3(carpeta);
            }
        }
    }

}
