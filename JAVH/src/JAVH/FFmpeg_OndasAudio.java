package JAVH;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

public class FFmpeg_OndasAudio extends FFmpeg_Espectro implements Constantes_DeOnda {

    private String MODO_DIBUJO = "";
    private String ESCALA = "";
    public int BANDAS = 2;

    public static void main(String[] args) throws Exception {
        String archivo = "C:\\Users\\guillermo\\Documents\\League of Legends\\zapateo.mp4";
        String Salida = "C:\\Users\\guillermo\\Documents\\League of Legends\\zapateo ondas.mp4";
        FFmpeg_OndasAudio ondasAudio = new FFmpeg_OndasAudio(
                archivo, Salida, 1280, 720
        );
        ondasAudio.COLOR = new Color(0x10A0FF);
        ondasAudio.FPS = 15;
        ondasAudio.MODO_DIBUJO = MODO_DIBUJO_ONDA_LINE;
        ondasAudio.EspectroOndasAudio_Mono();
    }

    public FFmpeg_OndasAudio(String RECURSO, String SALIDA, int W, int H) {
        super(RECURSO, SALIDA, W, H);
    }

    public FFmpeg_OndasAudio(String RECURSO, String SALIDA) {
        super(RECURSO, SALIDA);
    }

    public FFmpeg_OndasAudio(String RECURSO, String SALIDA, Dimension dimension) {
        super(RECURSO, SALIDA, dimension);
    }

    public void ModificarModoDibujo(String Modo) {
        switch (Modo) {
            case MODO_DIBUJO_ONDA_CLINE:
            case MODO_DIBUJO_ONDA_LINE:
            case MODO_DIBUJO_ONDA_P2P:
            case MODO_DIBUJO_ONDA_POINT:
                MODO_DIBUJO = Modo;
                break;
            default:
                throw new RuntimeException("Modo de onda no válido");
        }
    }

    public void ModificarEscala(String Escala) {
        switch (Escala) {
            case ESCALA_ONDA_LINEAR:
            case ESCALA_ONDA_LOGARITHMIC:
            case ESCALA_ONDA_SQUAREROOT:
            case ESCALA_ONDA_CUBICROOT:
                ESCALA = Escala;
                break;
            default:
                throw new RuntimeException("Escala de onda no válido");
        }
    }

    public void EspectroOndasAudio_Mono() throws Exception {
        EspectroOndasAudio(true);
    }

    public void EspectroOndasAudio_Stereo() throws Exception {
        EspectroOndasAudio(false);
    }

    public void EspectroOndasAudio(boolean mono) throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-filter_complex");
                add("\"");
                if (COMPAND) {
                    add("compand,");
                }
                if (mono) {
                    add("aformat=channel_layouts=mono,");
                }
                add("showwaves=");

                AñadirComandoDeDimensión(this);
                AñadirComandoDeColor(this);
                add(MODO_DIBUJO);
                BandasDeAudio(this);
                add(ESCALA);
                add("\"");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    public void ImagenEspectroVolumen_Estereo() throws Exception {
        ImagenEspectroVolumen(false);
    }

    public void ImagenEspectroVolumen_Mono() throws Exception {
        ImagenEspectroVolumen(true);
    }

    public void ImagenEspectroVolumen(boolean mono) throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-filter_complex");
                add("\"");
                if (COMPAND) {
                    add("compand,");
                }
                if (mono) {
                    add("aformat=channel_layouts=mono,");
                }
                add("showwavespic=");
                AñadirComandoDeDimensión(this);
                AñadirComandoDeColor(this);
                if (!mono) {
                    BandasDeAudio(this);
                }
                add(ESCALA);
                add("\"");
                add("-frames:v");
                add(1);
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, false);
    }

    protected void BandasDeAudio(ArrayList commands) {
        switch (BANDAS) {
            case 1:
                commands.add(":split_channels=0");
                break;
            case 2:
                commands.add(":split_channels=1");
                break;
            default:
                throw new RuntimeException("Número de bandas erroneo: " + BANDAS);
        }
    }
}
