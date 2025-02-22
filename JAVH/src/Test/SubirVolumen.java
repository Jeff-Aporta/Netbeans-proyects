package Test;

import JAVH.FFmpeg;
import JAVH.FFmpeg_Información;
import JAVH.FFmpeg_InformaciónDeSensibilidad;
import java.io.File;

public class SubirVolumen {

    public static void main(String[] args) throws Exception {
        File carpeta = new File("C:\\Users\\57310\\Documents\\MEGAsync Downloads");
        for (File carpetaEntrada : carpeta.listFiles((file) -> file.isDirectory())) {
            String carpetaSalida = carpetaEntrada + "\\Volumen alto";
            SubirAudioCarpetaDeArchivos(carpetaEntrada.getPath(), carpetaSalida, true);
        }
//        String entrada = "C:\\YAL\\Generos\\Clasica\\Moonlight_Sonata_by_Beethoven.mp3";
//        String salida = "C:\\YAL\\Generos\\Clasica\\a\\Moonlight_Sonata_by_Beethoven.mp3";
//        SubirAudio1Archivo(entrada, salida);
    }

    public static void SubirAudio1Archivo(String archivoEntrada, String archivoSalida) throws Exception {
//        {
//            FFmpeg_OndasAudio FFmpeg = new FFmpeg_OndasAudio(archivoEntrada, archivoSalida.replace(".mp3", " prev.png"));
//            FFmpeg.ImagenEspectroVolumen_Estereo();
//        }
        if(archivoEntrada.contains("{volumen ajustado}")){
            System.out.println(archivoEntrada);
            System.out.println("ya estaba ajustado el volumen");
            return;
        }
        FFmpeg_InformaciónDeSensibilidad sensibilidad = new FFmpeg_InformaciónDeSensibilidad(archivoEntrada);
        float[] volumen = sensibilidad.VolumenValor_Fotogramas(3, 30);
        float max = -1;
        for (float f : volumen) {
            max = Math.max(f, max);
        }
        max = 1 / max;
        max *= 0.95;
        System.out.println(archivoEntrada);
        System.out.println("Incrementar: " + max);
        FFmpeg ffmpeg = new FFmpeg(archivoEntrada, archivoSalida);
        ffmpeg.SubirVolumen(max);
//        {
//            FFmpeg_OndasAudio FFmpeg = new FFmpeg_OndasAudio(archivoSalida, archivoSalida.replace(".mp3", " post.png"));
//            FFmpeg.ImagenEspectroVolumen_Estereo();
//        }
    }

    public static void SubirAudioCarpetaDeArchivos(String CarpetaEntrada, String CarpetaSalida, boolean Sobreescribir) throws Exception {
        File carpeta = new File(CarpetaEntrada);
        for (File carpetaEntrada : carpeta.listFiles((file) -> file.isDirectory())) {
            SubirAudioCarpetaDeArchivos(carpetaEntrada.getPath(), carpetaEntrada.getPath(), true);
        }
        if (!CarpetaSalida.endsWith("\\") && !CarpetaSalida.endsWith("/")) {
            CarpetaSalida += "\\";
        }
        File f1 = new File(CarpetaEntrada);
        File[] entradas = f1.listFiles(
                (file) -> {
                    return file.getName().toLowerCase().endsWith(".mp3");
                }
        );
        File[] salidas = new File[entradas.length];
        for (int i = 0; i < salidas.length; i++) {
            String nombre = entradas[i].getName();
            salidas[i] = new File(CarpetaSalida + nombre.replace(".mp3", " {volumen ajustado}.mp3"));
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
            SubirAudio1Archivo(entradas[i].getPath(), salidas[i].getPath());
            entradas[i].delete();
        }
    }

}
