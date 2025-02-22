package Test;

import JAVH.FFmpeg;

public class recortarVideos {
    public static void main(String[] args) throws Exception {
        String ruta = "C:\\Users\\57310\\Downloads\\SER POBRE ES NO TENER PAZ (2).mp4";
        FFmpeg ff = new FFmpeg(ruta,ruta.replace(".mp4", "out.mp4"));
        ff.recortarTiempoVideo_inicio_fin(43, 51);
    }
}
