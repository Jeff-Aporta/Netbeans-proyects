package HerramientasAudio.Midi;

import HerramientasSistema.BloqueDeCódigo.*;
import HerramientasSistema.Cronómetro;
import java.util.ArrayList;
import java.util.Collections;
import javax.sound.midi.*;

public class InterpretadorMIDI {

    public static final int MIDIEVENT_NOTE_ON = 0x90;
    public static final int MIDIEVENT_NOTE_OFF = 0x80;
    public static final int MIDIEVENT_CHANGE_INSTRUMENT = 0xc0;

    public int Duración_ms = -1;

    private float velocidadReproducción = 1;
    public float Volúmen = 1;

    public Canales Canales;

    public Cronómetro controlTiempo = new Cronómetro();

    public ReproductorNotas reproductorNotas = new ReproductorNotas();

    int[] PosiciónNotalCanal;

    float DesvanecimientoExtremos = 0;

    Simple EventoReproducciónTérminada;
    Simple_Strings EventoCargaArchivo;
    Simple_Longs EventoCargaSecuenciaLong;
    EventoNota EventoNota;

    public static void main(String[] args) throws Exception {
        InterpretadorMIDI lectorMIDI = new InterpretadorMIDI();
//        lectorMIDI.CargarSecuenciaLong(Efectos.Megaman.Leading());
//        lectorMIDI.CambiarInstrumento_TodosLosCanales(0, 10);
//        lectorMIDI.Reproducir();
        System.out.println(SecuenciaLong.Valor(0, 0, 11));
        SecuenciaLong.Volumen = 2;
//        String g = SecuenciaLong.GenerarSecuencia("https://bitmidi.com/uploads/41250.mid");
//        System.out.println(g);
    }

    public InterpretadorMIDI() {
    }

    public InterpretadorMIDI(Object o) throws Exception {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        CargarArchivo(o);
    }//</editor-fold>

    public void velocidadReproducción(float FactorVelocidad) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        velocidadReproducción = FactorVelocidad;
        SuspenderTodasLasNotas();
        ReposicionarNotas((int) ObtenerTiempoReproducción());
    }//</editor-fold>

    public void CambiarVolumenCanal(int canal, float volúmen) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Canales.get(canal).volúmen = volúmen;
    }//</editor-fold>

    public void CambiarInstrumento(int canal, int banco, int programa) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Canales.get(canal).banco = banco;
        Canales.get(canal).programa = programa;
    }//</editor-fold>

    public void CambiarInstrumento_TodosLosCanales(int llave) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        CambiarInstrumento_TodosLosCanales((llave >>> 16) & 0xffff, llave & 0xffff);
    }//</editor-fold>

    public void CambiarInstrumento_TodosLosCanales(int banco, int programa) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (int canal = 0; canal < Canales.size(); canal++) {
            CambiarInstrumento(canal, banco, programa);
        }
    }//</editor-fold>

    public void CargarSecuenciaLong(long[] l) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        SuspenderTodasLasNotas();
        for (long n : l) {
            if (SecuenciaLong.esDuración(n)) {
                Duración_ms = SecuenciaLong.ObtenerDuración(n);
                break;
            }
        }
        Canales = new Canales(l);
        PosiciónNotalCanal = new int[Canales.size()];
        Detener();
        if (EventoCargaSecuenciaLong != null) {
            EventoCargaSecuenciaLong.Ejecutar(l);
        }
    }//</editor-fold>

    public void CargarArchivo(Object o) throws Exception {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            SuspenderTodasLasNotas();

            RenglonesEventos r = GenerarRenglones(o);

            for (RenglónEvento renglón : r) {
                if (renglón.esDuración) {
                    Duración_ms = renglón.Duración;
                    break;
                }
            }
            Canales = new Canales(r);
            PosiciónNotalCanal = new int[Canales.size()];
            Detener();
            if (EventoCargaArchivo != null) {
                EventoCargaArchivo.Ejecutar(o.toString());
            }
        } catch (Exception e) {
            Duración_ms = -1;
            if (EventoCargaArchivo != null) {
                EventoCargaArchivo.Ejecutar(o.toString());
            }
            throw new RuntimeException("Error al cargar el archivo");
        }
    }//</editor-fold>

    boolean tieneCargadoUnArchivo() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (Duración_ms < 0) {
            return false;
        }
        return true;
    }//</editor-fold>

    void deponer() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Canales.clear();
        Canales = null;
        controlTiempo.Reestablecer();
        controlTiempo = null;
        reproductorNotas.Deponer();
        reproductorNotas = null;
        PosiciónNotalCanal = null;
    }//</editor-fold>

    public double porcentajeRecorrido() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (!tieneCargadoUnArchivo()) {
            return 0;
        }
        return ObtenerTiempoReproducción() / Duración_ms;
    }//</editor-fold>

    public double ObtenerTiempoReproducción() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (!tieneCargadoUnArchivo()) {
            return 0;
        }
        return controlTiempo.TiempoTranscurrido() * velocidadReproducción;
    }//</editor-fold>

    void CambiarMomentoReproducción(int ms) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (!tieneCargadoUnArchivo()) {
            return;
        }
        controlTiempo.CambiarTiempo(ms);
        SuspenderTodasLasNotas();
        ReposicionarNotas(ms);
    }//</editor-fold>

    void ReposicionarNotas(int ms) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (!tieneCargadoUnArchivo()) {
            return;
        }
        for (int i = 0; i < Canales.size(); i++) {
            PosiciónNotalCanal[i] = 0;
            for (int j = 0; j < Canales.get(i).size() && Canales.get(i).get(j).ms_activación < ms; j++) {
                PosiciónNotalCanal[i]++;
            }
        }
    }//</editor-fold>

    void Detener() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (!tieneCargadoUnArchivo()) {
            return;
        }
        if (controlTiempo != null) {
            controlTiempo.Pausar();
            controlTiempo.Reestablecer();
            CambiarMomentoReproducción(0);
        }
        try {
            Thread.sleep(10);
        } catch (Exception ex) {
        }
        SuspenderTodasLasNotas();
    }//</editor-fold>

    void SuspenderTodasLasNotas() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (!tieneCargadoUnArchivo()) {
            return;
        }
        if (reproductorNotas != null) {
            for (MidiChannel CanalSonido : reproductorNotas.CanalSonido) {
                for (int j = 0; j < Byte.MAX_VALUE; j++) {
                    CanalSonido.noteOff(j);
                }
            }
        }
    }//</editor-fold>

    void Pausar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (!tieneCargadoUnArchivo()) {
            return;
        }
        controlTiempo.Pausar();
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
        }
        SuspenderTodasLasNotas();
    }//</editor-fold>

    void Reproducir(int repeticiones) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (int i = 0; i < repeticiones || repeticiones < 0; i++) {
            if (i == 0) {
                Reproducir(true, false);
            } else if (i == repeticiones - 1) {
                Reproducir(false, true);
            } else {
                Reproducir(false, false);
            }
        }
    }//</editor-fold>

    void Reproducir(float retardo) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Reproducir(true, true, retardo);
    }//</editor-fold>

    void Reproducir() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Reproducir(true, true);
    }//</editor-fold>

    boolean enPausa() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (!tieneCargadoUnArchivo()) {
            return true;
        }
        return controlTiempo.estaEnPausa();
    }//</editor-fold>

    boolean ReproducciónIniciada() {
        return hiloReproducción != null;
    }

    Thread hiloReproducción;

    void Reproducir(boolean DesvInicio, boolean DesvFin) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Reproducir(DesvInicio, DesvFin, 0);
    }//</editor-fold>

    void Reproducir(boolean DesvInicio, boolean DesvFin, float retardo) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (!tieneCargadoUnArchivo()) {
            return;
        }
        controlTiempo.Reanudar();
        if (!ReproducciónIniciada()) {
            hiloReproducción = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep((long) (1000 * retardo));
                    } catch (InterruptedException ex) {
                    }
                    ReproducirSinHilo(DesvInicio, DesvFin);
                }
            };
            hiloReproducción.start();
        }
    }//</editor-fold>

    void ReproducirSinHilo(boolean DesvInicio, boolean DesvFin) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (!tieneCargadoUnArchivo()) {
            return;
        }
        controlTiempo.Reanudar();
        double tiempo_ms = 0;
        while (tiempo_ms <= Duración_ms) {
            try {
                if (controlTiempo.estaEnPausa()) {
                    Thread.sleep(200);
                    continue;
                }
                tiempo_ms = ObtenerTiempoReproducción();
                for (int C = 0; C < Canales.size(); C++) {
                    Canal canal = Canales.get(C);
                    reproductorNotas.CambiarInstrumento(C, canal.banco, canal.programa);
                    if (PosiciónNotalCanal[C] < canal.size()) {

                        boolean NuevoEventoTecla = tiempo_ms > canal.get(PosiciónNotalCanal[C]).ms_activación;

                        if (NuevoEventoTecla) {
                            float factorVolumen = Volúmen;
                            {
                                factorVolumen *= canal.volúmen;
                                if (DesvanecimientoExtremos > 0) {
                                    float factorDesvanecer = 1;
                                    float tiempo_s = (float) (tiempo_ms / 1000f);
                                    float desvExt = DesvanecimientoExtremos;
                                    if (DesvInicio && tiempo_s <= desvExt) {
                                        factorDesvanecer = tiempo_s / desvExt;
                                    } else if (DesvFin && tiempo_s >= (Duración_ms / 1000f) - desvExt) {
                                        factorDesvanecer = 1 + (((Duración_ms) / 1000f - tiempo_s - desvExt) / desvExt);
                                    }
                                    factorVolumen *= factorDesvanecer;
                                }
                            }
                            if (canal.get(PosiciónNotalCanal[C]).ActivarNota) {
                                reproductorNotas.Reproducir(C,
                                        canal.get(PosiciónNotalCanal[C]).nota,
                                        factorVolumen * canal.get(PosiciónNotalCanal[C]).volumen / 128
                                );
                                if (EventoNota != null) {
                                    EventoNota.Ejecutar(
                                            C,
                                            canal.get(PosiciónNotalCanal[C]).ms_activación,
                                            true,
                                            canal.get(PosiciónNotalCanal[C]).nota,
                                            (int) (factorVolumen * canal.get(PosiciónNotalCanal[C]).volumen)
                                    );
                                }
                            } else {
                                reproductorNotas.Suspender(C, canal.get(PosiciónNotalCanal[C]).nota);
                                if (EventoNota != null) {
                                    EventoNota.Ejecutar(
                                            C,
                                            canal.get(PosiciónNotalCanal[C]).ms_activación,
                                            false,
                                            canal.get(PosiciónNotalCanal[C]).nota,
                                            (int) (factorVolumen * canal.get(PosiciónNotalCanal[C]).volumen)
                                    );
                                }
                            }
                            PosiciónNotalCanal[C]++;
                        }
                    }
                }
            } catch (Exception e) {
                break;
            }
        }
        Detener();
        if (EventoReproducciónTérminada != null) {
            EventoReproducciónTérminada.Ejecutar();
        }
        hiloReproducción = null;
    }//</editor-fold>

    static RenglonesEventos GenerarRenglones(Object o) throws Exception {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Sequence sequence = ReproductorArchivosMIDI.Generar_Sequence(o);
        RenglonesEventos renglones = new RenglonesEventos();

        renglones.add(new RenglónEvento((int) (1000 * sequence.getMicrosecondLength() / 1000000f)));

        for (Track track : sequence.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                int ms = (int) (1000 * sequence.getMicrosecondLength() * event.getTick() / (sequence.getTickLength() * 1000000f));
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    boolean activar = false;
                    switch (sm.getCommand()) {
                        case MIDIEVENT_NOTE_ON:
                            activar = true;
                        case MIDIEVENT_NOTE_OFF:
                            int key = sm.getData1();
                            int volumen = sm.getData2();
                            renglones.add(new RenglónEvento(sm.getChannel(), ms, key, volumen, activar));
                        case MIDIEVENT_CHANGE_INSTRUMENT:
                            renglones.add(new RenglónEvento(sm.getChannel(), sm.getData2(), sm.getData1()));
                            break;
                    }
                }
            }
        }
        renglones.Organizar();
        return renglones;
    }//</editor-fold>

    class Canales extends ArrayList<Canal> {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        public Canales(RenglonesEventos renglones) {
            for (RenglónEvento renglón : renglones) {
                while (size() <= renglón.canal) {
                    add(new Canal());
                }
                if (!renglón.esDuración) {
                    if (renglón.esInstrumento) {
                        get(renglón.canal).banco = renglón.banco;
                        get(renglón.canal).programa = renglón.programa;
                    } else {
                        get(renglón.canal).add(
                                new DatoEvento(
                                        renglón.milisegundoEvento,
                                        renglón.activar,
                                        renglón.nota,
                                        renglón.volumen
                                )
                        );
                    }
                }
            }
        }

        public Canales(long[] l) {
            for (long n : l) {
                if (SecuenciaLong.esDuración(n)) {
                    continue;
                }
                int[] a;
                if (SecuenciaLong.esInstrumento(n)) {
                    a = SecuenciaLong.ObtenerInstrumento(n);
                    while (size() <= a[0]) {
                        add(new Canal());
                    }
                    get(a[0]).banco = a[1];
                    get(a[0]).programa = a[2];
                    continue;
                } else if (SecuenciaLong.esSonido(n)) {
                    a = SecuenciaLong.ObtenerSonido(n);
                    while (size() <= a[1]) {
                        add(new Canal());
                    }
                    get(a[1]).add(
                            new DatoEvento(
                                    a[0],
                                    true,
                                    a[3],
                                    a[2]
                            )
                    );
                    get(a[1]).add(
                            new DatoEvento(
                                    a[0] + a[4],
                                    false,
                                    a[3],
                                    a[2]
                            )
                    );
                }
            }
            for (Canal c : this) {
                Collections.sort(c);
            }
        }
    }//</editor-fold>

    class Canal extends ArrayList<DatoEvento> {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        int banco;
        int programa;

        void CambiarInstrumento(int indice) {
            int llave = ReproductorNotas.HerramientasMIDI.ObtenerLlave(indice);
            banco = (llave >>> 16) & 0xffff;
            programa = (llave) & 0xffff;
        }

        float volúmen = 1;
    }//</editor-fold>

    class DatoEvento implements Comparable<DatoEvento> {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        long ms_activación;
        boolean ActivarNota;
        int nota;
        int volumen;

        public DatoEvento(long ms, boolean ActivarNota, int Tecla, int volumen) {
            this.ms_activación = ms;
            this.ActivarNota = ActivarNota;
            this.nota = Tecla;
            this.volumen = volumen;
        }

        @Override
        public int compareTo(DatoEvento t) {
            return new Long(ms_activación).compareTo(t.ms_activación);
        }
    }//</editor-fold>

    static class SecuenciaLong {

        public final static int ID_DURACIÓN = 0;
        public final static int ID_INSTRUMENTO = 1;
        public final static int ID_SONIDO = 2;

        public final static int bits_identificador = 2;
        public final static int bits_canal = 5;
        public final static int bits_ms_activación = 26;
        public final static int bits_nota = 7;
        public final static int bits_volumen = 8;
        public final static int bits_sostener = 16;

        public final static int mascara_identificador = 0x3;
        public final static int mascara_canal = 0x1f;
        public final static int mascara_ms_activación = 0x3ffffff;
        public final static int mascara_nota = 0x7f;
        public final static int mascara_volumen = 0xff;
        public final static int mascara_sostener = 0xffff;

        public static float Volumen = 1;

        static String GenerarSecuencia(String s) {
            try {
                ArrayList<RenglónTransformado> rt = GenerarRenglonesTransformados(GenerarRenglones(s));
                return GenerarSecuencia(rt);
            } catch (Exception ex) {
                return null;
            }
        }

        static String GenerarSecuencia(ArrayList<RenglónTransformado> T) {
            String retorno = "return new long[]{\n";
            for (int i = 0; i < T.size(); i++) {
                System.out.println(T.get(i) + " - " + Valor(T.get(i)));
                retorno += Valor(T.get(i)) + "L";
                if (i != T.size() - 1) {
                    retorno += ",";
                    if (i % 10 == 0) {
                        retorno += "\n";
                    }
                }
            }
            retorno += "\n};";
            return retorno;
        }

        static long Valor(int duración) {
            duración <<= bits_identificador;
            return duración | ID_DURACIÓN;
        }

        static long Valor(RenglónTransformado r) {
            if (r.esDuración) {
                return Valor(r.Duración);
            }
            if (r.esInstrumento) {
                return Valor(r.canal, r.banco, r.programa);
            }
            return Valor(
                    r.milisegundoEvento,
                    r.canal, (long) (r.volumen * Volumen),
                    r.nota,
                    r.sostener
            );
        }

        static long Valor(long canal, int banco, int programa) {
            long llave = ReproductorNotas.HerramientasMIDI.GenerarLlaveInstrumento(banco, programa);
            llave <<= bits_identificador;
            canal <<= 32 + bits_identificador;
            return canal | llave | ID_INSTRUMENTO;
        }

        static long Valor(long ms_activación, long canal, long volumen, long nota, long sostener) {
            ms_activación &= mascara_ms_activación;
            canal &= mascara_canal;
            volumen &= mascara_volumen;
            nota &= mascara_nota;
            sostener &= mascara_sostener;

            int a = bits_ms_activación;
            ms_activación <<= 64 - a;
            a += bits_canal;
            canal <<= 64 - a;
            a += bits_volumen;
            volumen <<= 64 - a;
            a += bits_nota;
            nota <<= 64 - a;
            a += bits_sostener;
            sostener <<= 64 - a;

            return ms_activación | canal | volumen | nota | sostener | ID_SONIDO;
        }

        static boolean esDuración(long valor) {
            return ObtenerIdentificador(valor) == ID_DURACIÓN;
        }

        static boolean esInstrumento(long valor) {
            return ObtenerIdentificador(valor) == ID_INSTRUMENTO;
        }

        static boolean esSonido(long valor) {
            return ObtenerIdentificador(valor) == ID_SONIDO;
        }

        static int ObtenerIdentificador(long valor) {
            return (int) (valor & mascara_identificador);
        }

        static int ObtenerDuración(long valor) {
            return (int) (valor >>> bits_identificador);
        }

        static int[] ObtenerInstrumento(long valor) {
            valor >>>= bits_identificador;
            int canal = (int) (valor >>> 32) & mascara_canal;
            int banco = (int) (valor >>> 16) & 0xffff;
            int programa = (int) (valor) & 0xffff;
            return new int[]{canal, banco, programa};
        }

        static int[] ObtenerSonido(long valor) {
            long ms_activación = valor;
            long canal = valor;
            long volumen = valor;
            long nota = valor;
            long sostener = valor;

            int a = bits_ms_activación;
            ms_activación >>>= 64 - a;
            a += bits_canal;
            canal >>>= 64 - a;
            a += bits_volumen;
            volumen >>>= 64 - a;
            a += bits_nota;
            nota >>>= 64 - a;
            a += bits_sostener;
            sostener >>>= 64 - a;

            ms_activación &= mascara_ms_activación;
            canal &= mascara_canal;
            volumen &= mascara_volumen;
            nota &= mascara_nota;
            sostener &= mascara_sostener;
            int[] retorno = new int[]{(int) ms_activación, (int) canal, (int) volumen, (int) nota, (int) sostener};
            return retorno;
        }

    }

    static ArrayList<RenglónTransformado> GenerarRenglonesTransformados(long[] l) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        ArrayList<RenglónTransformado> rt = new ArrayList<>();
        for (long m : l) {
            if (SecuenciaLong.esDuración(m)) {
                rt.add(new RenglónTransformado(SecuenciaLong.ObtenerDuración(m)));
                continue;
            }
            if (SecuenciaLong.esInstrumento(m)) {
                int[] a = SecuenciaLong.ObtenerInstrumento(m);
                rt.add(new RenglónTransformado(a[0], a[1], a[2]));
                continue;
            }
            if (SecuenciaLong.esSonido(m)) {
                int[] a = SecuenciaLong.ObtenerSonido(m);
                rt.add(new RenglónTransformado(
                        new RenglónEvento(a[1], a[0], a[3], a[2], true),
                        a[4]
                ));
            }
        }
        return rt;
    }

    static ArrayList<RenglónTransformado> GenerarRenglonesTransformados(RenglonesEventos entrada) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        RenglonesEventos r = new RenglonesEventos();
        int s = entrada.size();
        for (RenglónEvento renglónEvento : entrada) {
            r.add(renglónEvento);
        }
        ArrayList<RenglónTransformado> resultado = new ArrayList<>();

        while (!r.isEmpty()) {
            RenglónEvento renglónInicio = r.get(0);
            r.remove(0);
            if (renglónInicio.esDuración) {
                resultado.add(new RenglónTransformado(renglónInicio.Duración));
                continue;
            }
            if (!renglónInicio.activar) {
//                System.out.println("Error, el renglón pivote no es de activación " + renglónInicio);
            }
            RenglónTransformado renglónResultado = null;
            for (RenglónEvento renglónEvento : r) {
                if (renglónEvento.nota == renglónInicio.nota) {
                    if (renglónEvento.activar) {
//                        System.out.println("doble nota de activación encontrada");
                    }
                    if (renglónInicio.esInstrumento) {
                        renglónResultado = new RenglónTransformado(
                                renglónInicio.canal, renglónInicio.banco, renglónInicio.programa
                        );
                    } else {
                        renglónResultado = new RenglónTransformado(
                                renglónInicio,
                                renglónEvento.milisegundoEvento - renglónInicio.milisegundoEvento
                        );
                    }
                    r.remove(renglónEvento);
                    break;
                }
            }
            if (renglónResultado == null) {
//                System.out.println("No se encontró el renglón de fin");
            } else {
                resultado.add(renglónResultado);
            }
        }
        return resultado;
    }//</editor-fold>

    static class RenglónTransformado extends RenglónEvento {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        int sostener;

        public RenglónTransformado(int Duración) {
            super(Duración);
        }

        public RenglónTransformado(int canal, int banco, int programa) {
            super(canal, banco, programa);
        }

        public RenglónTransformado(RenglónEvento renglón, int sostener) {
            super(
                    renglón.canal,
                    renglón.milisegundoEvento,
                    renglón.nota,
                    renglón.volumen,
                    renglón.activar
            );
            this.sostener = sostener;
        }

        @Override
        public String toString() {
            if (esDuración) {
                return Duración + " ms";
            }
            if (esInstrumento) {
                return "C:" + canal
                        + ":B:" + banco
                        + ":P:" + programa;
            }
            return "C:" + canal
                    + ":ME:" + String.format("%08d", milisegundoEvento)
                    + ":N:" + nota
                    + ":V:" + volumen
                    + ":S:" + sostener;
        }
    }//</editor-fold>

    public static class RenglonesEventos extends ArrayList<RenglónEvento> {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        void Organizar() {
            Collections.sort(this);
            int canal = -1;
            ArrayList<RenglónEvento> eliminar = new ArrayList<>();
            for (RenglónEvento r : this) {
                if (!r.esInstrumento) {
                    continue;
                }
                if (r.canal != canal) {
                    canal = r.canal;
                } else {
                    eliminar.add(r);
                }
            }
            removeAll(eliminar);
        }
    }//</editor-fold>

    public static class RenglónEvento implements Comparable<RenglónEvento> {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        int canal;

        boolean esDuración = false;
        int Duración;

        boolean esInstrumento = false;
        int banco;
        int programa;

        int milisegundoEvento;
        int nota;
        int volumen;
        boolean activar = false;

        public RenglónEvento(int canal, int milisegundoEvento, int nota, int volumen, boolean activar) {
            this.canal = canal;
            this.milisegundoEvento = milisegundoEvento;
            this.nota = nota;
            this.volumen = volumen;
            this.activar = activar;
        }

        public RenglónEvento(int canal, int banco, int programa) {
            this.canal = canal;
            this.banco = banco;
            this.programa = programa;
            esInstrumento = true;
        }

        public RenglónEvento(int Duración) {
            this.Duración = Duración;
            esDuración = true;
        }

        @Override
        public boolean equals(Object o) {
            return super.equals(o);
        }

        @Override
        public String toString() {
            if (esDuración) {
                return Duración + " ms";
            }
            if (esInstrumento) {
                return "C:" + canal
                        + ":B:" + banco
                        + ":Programa:" + programa;
            }
            return "C:" + canal
                    + ":ME:" + String.format("%08d", milisegundoEvento)
                    + ":N:" + nota
                    + ":V:" + volumen
                    + ":A:" + (activar);
        }

        @Override
        public int compareTo(RenglónEvento t) {
            return toString().compareTo(t.toString());
        }
    }//</editor-fold>

    public static interface EventoNota {

        void Ejecutar(int Canal, long ms_activación, boolean ActivarNota, int nota, int volumen);
    }
}
