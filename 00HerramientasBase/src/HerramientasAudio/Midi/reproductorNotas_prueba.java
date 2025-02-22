package HerramientasAudio.Midi;

import javax.sound.midi.*;

public class reproductorNotas_prueba {

    Synthesizer synth;

    public float volúmen = 1;

    public static final int DO = 0;
    public static final int DO_ = 1;
    public static final int RE = 2;
    public static final int RE_ = 3;
    public static final int MI = 4;
    public static final int FA = 5;
    public static final int FA_ = 6;
    public static final int SOL = 7;
    public static final int SOL_ = 8;
    public static final int LA = 9;
    public static final int LA_ = 10;
    public static final int SI = 11;

    public static void main(String[] args) throws Exception {
        reproductorNotas_prueba notas_prueba = new reproductorNotas_prueba();
        notas_prueba.cambiarInstrumento(0, 0, 10);
        notas_prueba.melodia();
    }

    interface bloque {

        void ejecutar();
    }

    void melodia() {
        int octava = 5;
        float v = 0.7f;
        bloque p1 = () -> {
            tocar(octava + 1, MI, 500 * v);
            tocar(octava + 1, RE_, 500 * v);
            tocar(octava + 1, MI, 500 * v);
            tocar(octava + 1, RE_, 500 * v);
            tocar(octava + 1, MI, 500 * v);
            tocar(octava, SI, 500 * v);
            tocar(octava + 1, RE, 500 * v);
            tocar(octava + 1, DO, 500 * v);
            tocar(octava, LA, 1100 * v);
            tocar(octava, DO, 500 * v);
            tocar(octava, FA, 500 * v);
            tocar(octava, LA, 500 * v);
            tocar(octava, SI, 1100 * v);
            tocar(octava, MI, 500 * v);
            tocar(octava, FA_, 500 * v);
            tocar(octava, SI, 500 * v);
        };
        for (int i = 1; i <= 4; i++) {
            p1.ejecutar();
            if (i == 4) {
                tocar(octava, LA, 1100 * v);
            } else {
                tocar(octava + 1, DO, 1100 * v);
            }
        }
        tocar(octava , SI, 500 * v);
        tocar(octava + 1, DO, 500 * v);
        tocar(octava + 1, RE, 500 * v);
        tocar(octava + 1, MI, 1100 * v);
        tocar(octava , SOL, 500 * v);
        tocar(octava + 1, FA, 500 * v);
        tocar(octava + 1, MI, 500 * v);
        tocar(octava + 1, RE, 1100 * v);
        tocar(octava , FA, 500 * v);
        tocar(octava + 1, MI, 500 * v);
        tocar(octava + 1, RE, 500 * v);
        tocar(octava + 1, DO, 1100 * v);
        for (int i = 0; i < 2; i++) {
            p1.ejecutar();
            tocar(octava + 1, DO, 1100 * v);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
    }

    void tocar(int octava, int nota, float sostener) {
        try {
            sonar(0, notaByte(octava, nota));
            Thread.sleep((long) sostener);
            apagar(0, notaByte(octava, nota));
        } catch (Exception e) {
        }
    }

    static int notaByte(int octava, int nota) {
        return 12 * octava + nota;
    }

    public reproductorNotas_prueba() {
        open();
    }

    public void open() {
        try {
            if (synth != null) {
                return;
            }
            synth = MidiSystem.getSynthesizer();
            synth.open();
        } catch (MidiUnavailableException ex) {
        }
    }

    public void close() {
        synth.close();
        synth = null;
    }

    public void cambiarInstrumento(int canal, int banco, int programa) {
        synth.getChannels()[canal].programChange(banco, programa);
    }

    public void sonar(int canal, int nota) {
        synth.getChannels()[canal].noteOn(nota, (int) (volúmen * 128));
    }

    public void apagar(int canal, int nota) {
        synth.getChannels()[canal].noteOff(nota, 0);
    }

}
