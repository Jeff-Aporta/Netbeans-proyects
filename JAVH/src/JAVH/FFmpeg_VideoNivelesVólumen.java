package JAVH;

import java.awt.Dimension;
import java.util.ArrayList;

/**
 * Genera una salida visualmente similar a la siguiente imagen<br/><br/>
 * <p align="center">
 * <img src="https://jeffaporta01.000webhostapp.com/Librerias Java/JFFMPEG/NOMono_NivelesVolumen.jpg" height="180" width="320">
 * </p>
 * <br/><br/>
 */
public class FFmpeg_VideoNivelesVólumen extends FFmpeg_Dimensionable {

    /**
     * Si AUDIO_MONO = true<br/>
     * <img src="https://jeffaporta01.000webhostapp.com/Librerias Java/JFFMPEG/Mono_NivelesVolumen.jpg" height="90" width="320">
     * <br/><br/>
     * Si AUDIO_MONO = false<br/>
     * <img src="https://jeffaporta01.000webhostapp.com/Librerias Java/JFFMPEG/NOMono_NivelesVolumen.jpg" height="180" width="320">
     * <br/><br/>
     * Por defecto es true
     */
    public boolean AUDIO_MONO = true;
    /**
     * Si MOSTRAR_ETIQUETA_NOMBRE_CANAL = true<br/>
     * <img src="https://jeffaporta01.000webhostapp.com/Librerias Java/JFFMPEG/Etiqueta nombre del canal.jpg" height="117" width="416">
     * <br/><br/>
     * Por defecto es true
     */
    public boolean MOSTRAR_ETIQUETA_NOMBRE_CANAL = true;
    /**
     * Si MOSTRAR_ETIQUETA_NOMBRE_CANAL = true<br/>
     * <img src="https://jeffaporta01.000webhostapp.com/Librerias Java/JFFMPEG/Etiqueta valor del canal.jpg" height="117" width="416">
     * <br/><br/>
     * Por defecto es true
     */
    public boolean MOSTRAR_ETIQUETA_VALOR_CANAL = true;

    public FFmpeg_VideoNivelesVólumen(String RECURSO, String SALIDA, int W, int H) {
        super(RECURSO, SALIDA, W, H);
    }

    public FFmpeg_VideoNivelesVólumen(String RECURSO, String SALIDA) {
        super(RECURSO, SALIDA);
    }

    public FFmpeg_VideoNivelesVólumen(String RECURSO, String SALIDA, Dimension dimension) {
        super(RECURSO, SALIDA, dimension);
    }

    public static void main(String[] args) throws Exception {
        String recurso = "C:\\Users\\guillermo\\Documents\\Mega downloader\\01 - Prelude.mp3";
        String salida = "C:\\Users\\guillermo\\Documents\\Mega downloader\\01 - Prelude.mp4";
        FFmpeg_VideoNivelesVólumen nivelesVólumen = new FFmpeg_VideoNivelesVólumen(recurso, salida);
        nivelesVólumen.GenerarVideo_EspectroDeNivelesVolumen();
    }

    public void GenerarVideo_EspectroDeNivelesVolumen() throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-filter_complex");
                add("\"");
                if (AUDIO_MONO) {
                    add("aformat=channel_layouts=mono,");
                }
                add("showvolume=");
                add("t=" + (MOSTRAR_ETIQUETA_NOMBRE_CANAL ? "enabled" : "disabled"));
                add(":v=" + (MOSTRAR_ETIQUETA_VALOR_CANAL ? "enabled" : "disabled"));
                if (DIMENSIÓN == null) {
                    add(":w=640:h=180");
                } else {
                    add(":w=" + (DIMENSIÓN.width <= 0 ? 640 : DIMENSIÓN.width));
                    add(":h=" + (DIMENSIÓN.height <= 0 ? 180 : DIMENSIÓN.height));
                }
                add("\"");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }
}
