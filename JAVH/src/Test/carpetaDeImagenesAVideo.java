package Test;

import JAVH.Ejecutador;
import JAVH.FFmpeg;
import java.io.File;
import java.util.ArrayList;

public class carpetaDeImagenesAVideo {

    public static void main(String[] args) {
        String[] nombres = {
             "Echoes of Eternity", "Wandering Souls", "Fragments of Time", "Silent Universe", "Ephemeral Dreams", "Infinite Questions", "Shadows of Reality", "Cosmic Reflections", "Beyond the Veil", "Illusions of Self", "Raging Storm", "Unleashed Fury", "Breaking Chains", "Silent Screams", "Inferno Within", "Unchained Anger", "Fury of the Heart", "Shattered Control", "Burning Rage", "Violent Whispers", "Weeping Skies", "Broken Wings", "Echoes of Despair", "Fading Hope", "Eternal Sorrow", "Lonesome Paths", "Tears in Silence", "Heartache's Lullaby", "Shadows of Grief", "Lost in Melancholy", "Bleeding Shadows", "Crying Stars", "Torn Reflections", "Whispers of Anguish", "Bitter Remembrance", "Endless Echoes", "Fractured Dreams", "Sorrow's Wrath", "Twisted Realities", "Darkened Horizons"
        };
        File[] dir = new File("C:\\YAL8").listFiles((d, name) -> name.endsWith(".mp3"));
        FFmpeg ffmpeg = new FFmpeg();
        int i = 0;
        for (File file : dir) {
            try {
                ffmpeg.SALIDA = file.getPath().replace(file.getName(), "Shadows of Reality  ~ " + nombres[i++] + ".mp4");
                ArrayList comandos = new ArrayList() {
                    {
                        add("-i");
                        add("C:\\YAL8\\phonk.mp4");
                        add("-i");
                        add(file.getPath());
                        add("-map");
                        add(" 0:v");
                        add("-map");
                        add("1:a");
                        add("-c:v");
                        add("copy");
                        add("-c:a");
                        add("copy");
//                        add("-t");
//                        add("00:00:59");
                        ffmpeg.AñadirComandosDePropiedadesDeSalida(this);
                    }
                };
                ffmpeg.EjecutarCódigo_cmd(comandos, true);
                System.out.println("Concatenado");
            } catch (Exception e) {
            }
        }
    }
}
