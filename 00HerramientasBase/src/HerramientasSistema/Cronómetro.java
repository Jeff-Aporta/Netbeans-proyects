package HerramientasSistema;

import HerramientasMatemáticas.Matemática;

public class Cronómetro {

    public static final byte UNIDAD_MEDIDA_NANOSEGUNDOS = 0;
    public static final byte UNIDAD_MEDIDA_MILISEGUNDOS = 1;

    private byte UNIDAD_MEDIDA;

    long TiempoReferencia = -1;
    long Acumulado = 0;

    public static void main(String[] args) throws Exception {
        Cronómetro cronometro = new Cronómetro(UNIDAD_MEDIDA_NANOSEGUNDOS);
        cronometro.Iniciar();
        for (int i = 0; i < 1000000; i++) {
            System.out.println(i);
        }
        System.out.println(cronometro.SegundosTranscurridos());
    }

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * La unidad de medida será en milisegundos.
     */
    //</editor-fold>
    public Cronómetro() {//<editor-fold defaultstate="collapsed" desc="Constructor »">
        this(UNIDAD_MEDIDA_MILISEGUNDOS);
    }//</editor-fold>

    public Cronómetro(byte UNIDAD_MEDIDA) {//<editor-fold defaultstate="collapsed" desc="Constructor »">
        switch (UNIDAD_MEDIDA) {
            case UNIDAD_MEDIDA_MILISEGUNDOS:
            case UNIDAD_MEDIDA_NANOSEGUNDOS:
                this.UNIDAD_MEDIDA = UNIDAD_MEDIDA;
                break;
            default:
                throw new RuntimeException("No se reconoce la unidad de medida");
        }
    }//</editor-fold>

    public byte UNIDAD_MEDIDA() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return UNIDAD_MEDIDA;
    }//</editor-fold>

    public void Modificar_UNIDAD_MEDIDA(byte UnidadMedida) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (TiempoReferencia != -1 && Acumulado != 0) {
            return;
        }
        UNIDAD_MEDIDA = UnidadMedida;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Sirve para cambiar arbitrariamente el tiempo del crónometro.
     */
    //</editor-fold>
    public void CambiarTiempo(long nuevoTiempo) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Acumulado = nuevoTiempo;
        if (!estaEnPausa()) {
            CalcularTiempoReferencia_Actual();
        }
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Pone el crónometro en 0 y lo pausa.
     */
    //</editor-fold>
    public void Reestablecer() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        TiempoReferencia = -1;
        Acumulado = 0;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Pone el crónometro en 0 pero no lo pausa.
     */
    //</editor-fold>
    public void Reiniciar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        CalcularTiempoReferencia_Actual();
        Acumulado = 0;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Es casi lo mismo que reiniciar, solo que no se ejecuta si está en pausa.
     */
    //</editor-fold>
    public Cronómetro Iniciar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (!estaEnPausa()) {
            return this;
        }
        Reiniciar();
        return this;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Se encarga de pausar el crónometro, acumula el tiempo transcurrido y
     * elimina el tiempo de referencia.
     */
    //</editor-fold>
    public void Pausar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (estaEnPausa()) {
            return;
        }
        Acumulado = TiempoTranscurrido();
        TiempoReferencia = -1;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Es la función inversa a la pausa, calcula un nuevo tiempo de referencia.
     */
    //</editor-fold>
    public void Reanudar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (!estaEnPausa()) {
            return;
        }
        CalcularTiempoReferencia_Actual();
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Calcula el tiempo transcurrido basandose en el momento actual, el último
     * tiempo de referencia y el tiempo acumulado.
     */
    //</editor-fold>
    public long TiempoTranscurrido() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (estaEnPausa()) {
            return Acumulado;
        }
        switch (UNIDAD_MEDIDA) {
            case UNIDAD_MEDIDA_MILISEGUNDOS:
                return System.currentTimeMillis() - TiempoReferencia + Acumulado;
            case UNIDAD_MEDIDA_NANOSEGUNDOS:
                return System.nanoTime() - TiempoReferencia + Acumulado;
            default:
                throw new RuntimeException("No se reconoce la unidad de medida");
        }
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Se basa en el último tiempo de referencia para determinar si el
     * crónometro está en pausa o no.
     * <br><br>
     * Si hay tiempo de referencia entonces no está en pausa -> retorna
     * false<br>
     * Si no hay tiempo de referencia entonces está en pausa -> retorna true<br>
     */
    //</editor-fold>
    public boolean estaEnPausa() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return TiempoReferencia == -1;
    }//</editor-fold>

    public double SegundosTranscurridos() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return TiempoTranscurrido() / UnidadesPorSegundo();
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Carga una nueva unidad para el tiempo de referencia.
     */
    //</editor-fold>
    private void CalcularTiempoReferencia_Actual() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        switch (UNIDAD_MEDIDA) {
            case UNIDAD_MEDIDA_MILISEGUNDOS:
                TiempoReferencia = System.currentTimeMillis();
                break;
            case UNIDAD_MEDIDA_NANOSEGUNDOS:
                TiempoReferencia = System.nanoTime();
                break;
        }
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Retorna la unidad que tiene 1 segundo, según la unidad de medida del
     * crónometro.<br><br>
     * si mide en milisegundos -> retorna 1.000<br>
     * si mide en nanosegundos -> retorna 1.000.000.000
     */
    //</editor-fold>
    private double UnidadesPorSegundo() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        switch (UNIDAD_MEDIDA) {
            case UNIDAD_MEDIDA_MILISEGUNDOS:
                return 1000.0;
            case UNIDAD_MEDIDA_NANOSEGUNDOS:
                return 1000000000.0;
            default:
                throw new RuntimeException("No se reconoce la unidad de medida");
        }
    }//</editor-fold>

    @Override
    public String toString() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return convertir_HHMMSS_3_9_decimales(TiempoTranscurrido(), UNIDAD_MEDIDA);
    }//</editor-fold>

    public String Cadena_SegundosEnteros() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return convertir_HHMMSS_entero(TiempoTranscurrido());
    }//</editor-fold>

    public static String convertir_HHMMSS(int segundos) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return convertir_HHMMSS_entero(segundos * 1000);
    }//</editor-fold>

    public static String convertir_HHMMSS_entero(long tiempo) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return convertir_HHMMSS_decimales_adaptables(tiempo, UNIDAD_MEDIDA_MILISEGUNDOS, 0);
    }//</editor-fold>

    public static String convertir_HHMMSS_3_9_decimales(long tiempo, byte UnidadMedida) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return Cronómetro.convertir_HHMMSS_decimales_adaptables(
                tiempo,
                UnidadMedida,
                (UnidadMedida == UNIDAD_MEDIDA_MILISEGUNDOS ? 3 : 9)
        );
    }//</editor-fold>

    public static String convertir_HHMMSS_decimales_adaptables(long tiempo, byte UnidadMedida, int decimales) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        double factorConversión;
        switch (UnidadMedida) {
            case UNIDAD_MEDIDA_MILISEGUNDOS:
                factorConversión = 1 / 1000.0;
                break;
            case UNIDAD_MEDIDA_NANOSEGUNDOS:
                factorConversión = 1 / 1000000000.0;
                break;
            default:
                throw new RuntimeException("No se reconoce la unidad de medida");
        }
        double segundos = (tiempo * factorConversión);
        if (decimales == 0) {
            segundos = (int) segundos;
        }
        int horas = (int) (segundos / 60 / 60);
        int minutos = (int) (segundos / 60) % 60;
        segundos %= 60;
        return Matemática.CerosIzquierda(horas, 2)
                + ":" + Matemática.CerosIzquierda(minutos, 2)
                + ":" + (segundos < 10 ? "0" : "")
                + String.format(
                        "%." + decimales + "f", segundos
                ).replace(",", ".");
    }//</editor-fold>

}
