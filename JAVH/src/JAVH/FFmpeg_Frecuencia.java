package JAVH;

import java.awt.Dimension;
import java.util.ArrayList;

public class FFmpeg_Frecuencia extends FFmpeg_Espectro implements Constantes_Frecuencia {

    protected String MODO_CANALES = "";
    protected String ESCALA_VALORES_AUDIO = "";
    protected String ESCALA_FRECUENCIA = "";
    protected String MODO_DIBUJO = "";

    public FFmpeg_Frecuencia(String RECURSO, String SALIDA) {
        super(RECURSO, SALIDA);
    }

    public FFmpeg_Frecuencia(String RECURSO, String SALIDA, int W, int H) {
        super(RECURSO, SALIDA, W, H);
    }

    public FFmpeg_Frecuencia(String RECURSO, String SALIDA, Dimension dimension) {
        super(RECURSO, SALIDA, dimension);
    }

    public void Modificar_ElModoDeCanales(String ModoCanal) {
        switch (ModoCanal) {
            case MODO_CANALES_SEPARADOS:
            case MODO_CANALES_COMBINADOS:
                MODO_CANALES = ModoCanal;
            default:
                throw new RuntimeException("El modo para los canales no se reconoce");
        }
    }

    public void Modificar_ElModoDeDibujoDeLaFrecuencia(String ModoDibujo) {
        switch (ModoDibujo) {
            case MODO_DIBUJO_BAR:
            case MODO_DIBUJO_DOT:
            case MODO_DIBUJO_LINE:
                MODO_DIBUJO = ModoDibujo;
            default:
                throw new RuntimeException("El modo de dibujo no se reconoce");
        }
    }

    public void Modificar_ElModoDeLaEscalaDeLaFrecuencia(String ModoEscala) {
        switch (ModoEscala) {
            case ESCALA_FRECUENCIA_LINEAR:
            case ESCALA_FRECUENCIA_LOGARITHMIC:
            case ESCALA_FRECUENCIA_REVERSE_LOGARITHMIC:
                ESCALA_FRECUENCIA = ModoEscala;
            default:
                throw new RuntimeException("El valor para la escala no se reconoce");
        }
    }

    public void Modificar_LaEscalaDeLosValoresDelAudio(String ModoEscala) {
        switch (ModoEscala) {
            case ESCALA_CÁLCULO_VAL_AUDIO_CUBICROOT:
            case ESCALA_CÁLCULO_VAL_AUDIO_LINEAR:
            case ESCALA_CÁLCULO_VAL_AUDIO_LOGARITHMIC:
            case ESCALA_CÁLCULO_VAL_AUDIO_SQUAREROOT:
                ESCALA_VALORES_AUDIO = ModoEscala;
            default:
                throw new RuntimeException("El valor para la escala no se reconoce");
        }
    }

    public void GenerarVideoDeLaFrecuencia() throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-filter_complex");
                add("\"");
                if (COMPAND) {
                    add("compand,");
                }
                add("aformat=channel_layouts=mono,");
                add("showfreqs=");
                AñadirComandoDeDimensión(this);
                AñadirComandoDeColor(this);
                add(ESCALA_VALORES_AUDIO);
                add(ESCALA_FRECUENCIA);
                add(MODO_DIBUJO);
                add(MODO_CANALES);
                add("\"");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }
}
