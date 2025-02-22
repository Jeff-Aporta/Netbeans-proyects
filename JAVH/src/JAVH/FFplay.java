package JAVH;

import java.awt.Dimension;
import java.util.ArrayList;

public class FFplay extends FFmpeg_Dimensionable {

    /**
     * Bucles de reproducción de películas número veces.
     * <br\><br\>
     * 0 significa para siempre, valor por defecto
     */
    public int FFplay_Repeticiones = 0;

    public boolean FFplay_IniciarEnPantallaCompleta = false;
    public boolean FFplay_DeshabilitarVideo = false;
    public boolean FFplay_DeshabilitarAudio = false;
    public boolean FFplay_DeshabilitarBordeDeLaVentana = false;

    public static void main(String[] args) throws Exception {
        String Recurso = "C:\\Users\\guillermo\\Documents\\Mega downloader\\test.mp4";
        FFplay FFplay = new FFplay(Recurso);
        FFplay.PlayFile();
    }

    public FFplay(String recurso) {
        super(recurso, null);
        DIRECCIÓN_EJECUTABLE = DIRECCIÓN_FFPLAY;
        RECURSO = recurso;
    }

    public void PlayFile() throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    @Override
    public void AñadirComandosDePropiedadesDeSalida(ArrayList comandos) {
        super.AñadirComandosDePropiedadesDeSalida(comandos);
        AddFFplay(comandos);
    }

    public void AddFFplay(ArrayList comandos) {
        comandos.add("-x");
        comandos.add(DIMENSIÓN.width);
        comandos.add("-y");
        comandos.add(DIMENSIÓN.height);
        comandos.add("-loop");
        comandos.add(FFplay_Repeticiones);
        if (FFplay_IniciarEnPantallaCompleta) {
            comandos.add("-fs");
        }
        if (FFplay_DeshabilitarVideo) {
            comandos.add("-vn");
        }
        if (FFplay_DeshabilitarAudio) {
            comandos.add("-an");
        }
        if (FFplay_DeshabilitarBordeDeLaVentana) {
            comandos.add("-noborder");
        }
        System.out.println(FFplay_GetKeysControls());
    }

    public static String FFplay_GetKeysControls() {
        return "q, ESC\n"
                + "Quit.\n"
                + "\n"
                + "f\n"
                + "Toggle full screen.\n"
                + "\n"
                + "p\n"
                + "Pause.\n"
                + "\n"
                + "m\n"
                + "Toggle mute.\n"
                + "\n"
                + "9, 0\n"
                + "Decrease and increase volume respectively.\n"
                + "\n"
                + "/, *\n"
                + "Decrease and increase volume respectively.\n"
                + "\n"
                + "a\n"
                + "Cycle audio channel in the current program.\n"
                + "\n"
                + "v\n"
                + "Cycle video channel.\n"
                + "\n"
                + "t\n"
                + "Cycle subtitle channel in the current program.\n"
                + "\n"
                + "c\n"
                + "Cycle program.\n"
                + "\n"
                + "w\n"
                + "Cycle video filters or show modes.\n"
                + "\n"
                + "s\n"
                + "Step to the next frame.\n"
                + "\n"
                + "Pause if the stream is not already paused, step to the next video frame, and pause.\n"
                + "\n"
                + "left/right\n"
                + "Seek backward/forward 10 seconds.\n"
                + "\n"
                + "down/up\n"
                + "Seek backward/forward 1 minute.\n"
                + "\n"
                + "page down/page up\n"
                + "Seek to the previous/next chapter. or if there are no chapters Seek backward/forward 10 minutes.\n"
                + "\n"
                + "right mouse click\n"
                + "Seek to percentage in file corresponding to fraction of width.\n"
                + "\n"
                + "left mouse double-click\n"
                + "Toggle full screen.";
    }
}
