package JAVH;

import java.awt.Dimension;
import java.util.ArrayList;

public class FFmpeg_FrecuenciaCQT extends FFmpeg_Espectro {

    public static void main(String[] args) throws Exception {
        FFmpeg_FrecuenciaCQT freq = new FFmpeg_FrecuenciaCQT(
                "C:\\Users\\guillermo\\Downloads\\Midis\\suicide_note_part_1.mp3",
                "C:\\Users\\guillermo\\Downloads\\Midis\\suicide_note_part_1.mp4"
        );
        freq.FrequencySpectrum_CQT();
    }

    public FFmpeg_FrecuenciaCQT(String RECURSO, String SALIDA) {
        super(RECURSO, SALIDA);
    }

    public FFmpeg_FrecuenciaCQT(String RECURSO, String SALIDA, int W, int H) {
        super(RECURSO, SALIDA, W, H);
    }

    public FFmpeg_FrecuenciaCQT(String RECURSO, String SALIDA, Dimension dimension) {
        super(RECURSO, SALIDA, dimension);
    }

    public void FrequencySpectrum_CQT() throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-filter_complex");
                {
                    add("\"");
                    {
                        if (COMPAND) {
                            add("compand,");
                        }
                        add("showcqt=");
                        AñadirComandoDeDimensión(this);
                        AñadirComandoDeColor(this);
                    }
                    add("\"");
                }
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    @Override
    protected void AñadirComandoDeColor(ArrayList commands) {
        if (COLOR != null) {
            commands.add(":fontcolor="
                    + "0x" + Integer.toHexString(COLOR.getRGB()).substring(2).toUpperCase()
            );
        }
    }
}
