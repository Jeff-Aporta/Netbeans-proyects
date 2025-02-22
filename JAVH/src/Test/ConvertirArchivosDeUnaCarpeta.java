package Test;

import JAVH.FFmpeg;
import JAVH.FFmpeg_Información;
import JAVH.FFmpeg_OndasAudio;
import java.io.File;

public class ConvertirArchivosDeUnaCarpeta {

    public static void main(String[] args) throws Exception {
        String CarpetaEntrada = "C:\\Users\\josel\\Music\\Crystal Castles\\[2016] Amnesty (I)";
        String CarpetaMp4 = CarpetaEntrada + "\\Videos mp4";
        String CarpetaMp3 = CarpetaEntrada + "\\Audios videos";
        ConvertirCarpetaDeArchivos(CarpetaEntrada, CarpetaMp3, "mp3");
//        ConvertirCarpetaDeArchivos(CarpetaEntrada, CarpetaMp4, "mp4");
//        GenerarImagenesDeAudio(CarpetaMp3, CarpetaMp3, "png", true);
    }

    public static void ConvertirCarpetaDeArchivos(String CarpetaEntrada, String CarpetaSalida, String extensión) throws Exception {
        ConvertirCarpetaDeArchivos(CarpetaEntrada, CarpetaSalida, extensión, false);
    }

    public static void ConvertirCarpetaDeArchivos(String CarpetaEntrada, String CarpetaSalida, String extensión, boolean Sobreescribir) throws Exception {
        if (!extensión.startsWith(".")) {
            extensión = "." + extensión;
        }
        if (!CarpetaSalida.endsWith("\\") && !CarpetaSalida.endsWith("/")) {
            CarpetaSalida += "\\";
        }
        File f1 = new File(CarpetaEntrada);
        File[] entradas = f1.listFiles(FFmpeg_Información.ObtenerFiltroPara_ExtensionesSoportadas());
        File[] salidas = new File[entradas.length];
        for (int i = 0; i < salidas.length; i++) {
            String nombre = entradas[i].getName().substring(0, entradas[i].getName().lastIndexOf("."));
            nombre += extensión;
            salidas[i] = new File(CarpetaSalida + nombre);
            if (entradas[i].getPath().equals(salidas[i].getPath())) {
                throw new RuntimeException(
                        "Uno de los archivos de salida concuerda con uno de los de entrada\n"
                        + "Entrada: " + entradas[i].getPath() + "\n"
                        + "Salida: " + salidas[i].getPath()
                );
            }
        }
        for (int i = 0; i < entradas.length; i++) {
            if (!Sobreescribir) {
                if (salidas[i].exists()) {
                    continue;
                }
            }
            FFmpeg FFmpeg = new FFmpeg(entradas[i].getPath(), salidas[i].getPath());
            FFmpeg.ConvertirDeUnFormatoAOtro();
        }
    }

    public static void GenerarImagenesDeAudio(String CarpetaEntrada, String CarpetaSalida, String extensión, boolean Sobreescribir) throws Exception {
        if (!extensión.startsWith(".")) {
            extensión = "." + extensión;
        }
        if (!CarpetaSalida.endsWith("\\") && !CarpetaSalida.endsWith("/")) {
            CarpetaSalida += "\\";
        }
        File f1 = new File(CarpetaEntrada);
        File[] entradas = f1.listFiles(FFmpeg_Información.ObtenerFiltroPara_ExtensionesSoportadas());
        File[] salidas = new File[entradas.length];
        for (int i = 0; i < salidas.length; i++) {
            String nombre = entradas[i].getName().substring(0, entradas[i].getName().lastIndexOf("."));
            nombre += "img";
            nombre += extensión;
            salidas[i] = new File(CarpetaSalida + nombre);
            if (entradas[i].getPath().equals(salidas[i].getPath())) {
                throw new RuntimeException(
                        "Uno de los archivos de salida concuerda con uno de los de entrada\n"
                        + "Entrada: " + entradas[i].getPath() + "\n"
                        + "Salida: " + salidas[i].getPath()
                );
            }
        }
        for (int i = 0; i < entradas.length; i++) {
            if (!Sobreescribir) {
                if (salidas[i].exists()) {
                    continue;
                }
            }
            FFmpeg_OndasAudio FFmpeg = new FFmpeg_OndasAudio(entradas[i].getPath(), salidas[i].getPath());
            FFmpeg.ImagenEspectroVolumen_Estereo();
        }
    }

}
