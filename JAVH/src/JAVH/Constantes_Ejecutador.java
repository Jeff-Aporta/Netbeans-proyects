package JAVH;

import Ejecutables.UbicadorEjecutables;

public interface Constantes_Ejecutador {

    byte CONSOLE_SHOW_ALWAYS = 0;
    byte CONSOLE_SHOW_NEVER = 1;
    byte CONSOLE_SHOW_AS_DEV_DET = 2;

    String TMP_DIR = System.getProperty("java.io.tmpdir");
    String DIRECCIÓN_FFMPEG = UbicadorEjecutables.Dirección_FFmpeg;
    String DIRECCIÓN_FFPLAY = DIRECCIÓN_FFMPEG.replace("ffmpeg", "ffplay");
    String DIRECCIÓN_EXIFTOOL = DIRECCIÓN_FFMPEG.replace("ffmpeg", "exiftool");
}
