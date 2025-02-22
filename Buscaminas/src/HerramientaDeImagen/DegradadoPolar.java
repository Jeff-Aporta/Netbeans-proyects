package HerramientaDeImagen;

import HerramientasMatemáticas.Dupla;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import static HerramientasMatemáticas.Matemática.*;
import static HerramientaDeImagen.ModoFusión.*;
import static HerramientasMatemáticas.Dupla.*;

public final class DegradadoPolar implements Paint {

    public final static byte R = 0, G = 1, B = 2, A = 3, RGBA = 4;
    public final static byte AJUSTE_LINEAL = 0, AJUSTE_CICLICO = 1, AJUSTE_REFLEJO = 2;
    public final static byte //
            INTERPRETACIÓN_DEG_ALCANCE_CORTO = 0,
            INTERPRETACIÓN_DEG_ALCANCE_LARGO = 1,
            INTERPRETACIÓN_DEG_TEXTURIZADO = 2,
            INTERPRETACIÓN_DEG_SERPENTINA = 4,
            INTERPRETACIÓN_DEG_APERTURA = 5,
            INTERPRETACIÓN_DEG_VISLUMBRAR = 6,
            INTERPRETACIÓN_DEG_FOCAL = 7,
            INTERPRETACIÓN_DEG_LOG_DIVIDIR = 8,
            INTERPRETACIÓN_DEG_LOG_MULTIPLICAR = 9,
            INTERPRETACIÓN_DEG_LOG_ADICIONAR = 10,
            INTERPRETACIÓN_DEG_LOG_MODULO1 = 11,
            INTERPRETACIÓN_DEG_LOG_MODULO2 = 12,
            INTERPRETACIÓN_DEG_LOG_EXCLUSIÓN_BITS = 13,
            INTERPRETACIÓN_DEG_TEXTURIZADO2 = 14,
            INTERPRETACIÓN_DEG_INTERSECCIÓN_BITS_SIERPINSKI = 15,
            INTERPRETACIÓN_DEG_LOG_UNIÓN_BITS_SIERPINSKI = 16,
            INTERPRETACIÓN_DEG_LOG_UNIÓN_BITS_SIERPINSKI_N2 = 28,
            INTERPRETACIÓN_DEG_RAIZ2_BINARIO_LAZOS = 17,
            INTERPRETACIÓN_DEG_RAIZ2_BINARIO_LAZOS_CON_MOÑO = 18,
            INTERPRETACIÓN_DEG_12 = 19,
            INTERPRETACIÓN_DEG_13 = 20,
            INTERPRETACIÓN_DEG_14 = 21,
            INTERPRETACIÓN_DEG_15 = 22,
            INTERPRETACIÓN_DEG_16 = 23,
            INTERPRETACIÓN_DEG_ATRACTOR = 3;

    private byte //
            AJUSTE_PORCENTUAL = AJUSTE_LINEAL,
            INTERPRETAR_DEG = INTERPRETACIÓN_DEG_ALCANCE_CORTO;

    public float Δθ = 0;
    public float Escala;

    private OperaciónBinariaRGB operaciónBinariaRGB;
    public InterferenciaBinaria interferenciaBinaria;
    private final byte[] GirosDeEvaluación = {0, 1};

    private boolean INVERTIR = false;

    Dupla Centro;
    RectaColores[] colores;
    Object CurvaRadial;

    static double da = 0.001;

    public static void main(String[] args) {
        JLabel Presentador = new JLabel();
        JFrame v = new JFrame() {
            {
                setSize(1200, 700);
                setLocationRelativeTo(null);
                add(Presentador);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }
        };
        Dupla PosiciónCentro = new Dupla(Presentador).Mitad();
        Color[] ColoresPintar;
        float[] PorcentajesAparición;
        ColoresPintar = new Color[]{
            new Color(100, 200, 240),
            new Color(20, 120, 255),
            new Color(60, 0, 90),
            new Color(60, 0, 0)
        };
        PorcentajesAparición = new float[]{0, .2f, .5f, 1};
        Curva_nativa generadora = Nubleré(2, 7, 2, 7);
        DegradadoPolar degradadoParamétrico = new DegradadoPolar(
                PosiciónCentro,
                PorcentajesAparición, ColoresPintar,
                Polar_Mitad_Flor(6),
                90
        );
        degradadoParamétrico.AjustePorcentual(AJUSTE_REFLEJO);
        degradadoParamétrico.ModoCurva(INTERPRETACIÓN_DEG_RAIZ2_BINARIO_LAZOS_CON_MOÑO);
        degradadoParamétrico.Invertir();
//        degradadoParamétrico.ModificarRevolucionesDeEvaluación(CargarOperación(MODO_COLOR_MÁS_CLARO), -2, 2);
        InterferenciaBinaria interferenciaBinaria = new InterferenciaBinaria(
                new int[]{0, 0, 0},
                new byte[]{
                    InterferenciaBinaria.OP_BINARIO_UNIÓN,
                    InterferenciaBinaria.OP_BINARIO_UNIÓN,
                    InterferenciaBinaria.OP_BINARIO_UNIÓN
                },
                new boolean[]{false, true, true},
                new boolean[]{false, false, false, false}
        );
//        degradadoParamétrico.interferenciaBinaria = interferenciaBinaria;
        while (true) {
            interferenciaBinaria.InterferenciaBinaria[0]++;
            interferenciaBinaria.InterferenciaBinaria[1]++;
            interferenciaBinaria.InterferenciaBinaria[2]++;
            degradadoParamétrico.Centro = new Dupla(v).Mitad();
            BufferedImage Fotograma = new BufferedImage(Presentador.getWidth(), Presentador.getHeight(), 2) {
                {
                    Graphics2D g = createGraphics();
                    g.setPaint(degradadoParamétrico);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            Presentador.setIcon(new ImageIcon(Fotograma));
        }
    }

    public DegradadoPolar(Dupla Centro, float[] PorcentajesDeAparición, Color[] ColoresPintar, Object curva, float escala) {
        Escala = escala;
        if (curva instanceof Number && ((Number) curva).floatValue() == 0) {
            throw new Error("No se puede generar degradado con radio 0");
        }
        CambiarCurvaRadial(curva, escala);
        if (PorcentajesDeAparición.length != ColoresPintar.length) {
            throw new Error("El tamaño de los arreglos no coincide, Son diferentes");
        }
        int Elementos = ColoresPintar.length;
        if (Elementos < 2) {
            throw new Error("Se necesitan como mínimo 2 elementos de color para el degradado cónico");
        }
        for (float f : PorcentajesDeAparición) {
            if (f < 0 || f > 1) {
                throw new Error("Los porcentajes deben estar en notación decimal con valores entre 0 y 1");
            }
        }
        this.Centro = Centro.Proteger();
        if (PorcentajesDeAparición[0] != 0) {
            Elementos++;
            float pTemp[] = PorcentajesDeAparición;
            Color cTemp[] = ColoresPintar;
            PorcentajesDeAparición = new float[Elementos];
            ColoresPintar = new Color[Elementos];
            PorcentajesDeAparición[0] = 0;
            ColoresPintar[0] = cTemp[0];
            for (int i = 0; i < Elementos - 1; i++) {
                PorcentajesDeAparición[i + 1] = pTemp[i];
                ColoresPintar[i + 1] = cTemp[i];
            }
        }
        if (PorcentajesDeAparición[Elementos - 1] != 1) {
            Elementos++;
            float pTemp[] = PorcentajesDeAparición;
            Color cTemp[] = ColoresPintar;
            PorcentajesDeAparición = new float[Elementos];
            ColoresPintar = new Color[Elementos];
            for (int i = 0; i < Elementos; i++) {
                PorcentajesDeAparición[i] = pTemp[i % pTemp.length];
                ColoresPintar[i] = cTemp[i % cTemp.length];
            }
            PorcentajesDeAparición[Elementos - 1] = 1;
        }
        colores = new RectaColores[Elementos - 1];
        for (int i = 0; i < colores.length; i++) {
            colores[i] = new RectaColores(
                    PorcentajesDeAparición[i], PorcentajesDeAparición[i + 1],
                    ColoresPintar[i], ColoresPintar[i + 1]
            );
        }
    }

    public DegradadoPolar Rotar(float Δθ, byte TIPO_ÁNGULO) {
        return DesfaceΔθ(Δθ, TIPO_ÁNGULO);
    }

    public DegradadoPolar DesfaceΔθ(float Δθ, byte TIPO_ÁNGULO) {
        return DesfaceΔθ((float) Conv_Radian(Δθ, TIPO_ÁNGULO));
    }

    public DegradadoPolar Rotar(float Δθ) {
        return DesfaceΔθ(Δθ);
    }

    public DegradadoPolar DesfaceΔθ(float nΔθ) {
        Δθ = nΔθ;
        return this;
    }

    public DegradadoPolar InterferenciaBinaria(InterferenciaBinaria interferencia) {
        interferenciaBinaria = interferencia;
        return this;
    }

    public DegradadoPolar Escala(float escala) {
        Escala = escala;
        return this;
    }

    public DegradadoPolar MoverElCentro(Dupla centro) {
        Centro = centro;
        return this;
    }

    public DegradadoPolar Invertir() {
        INVERTIR = !INVERTIR;
        return this;
    }

    public DegradadoPolar AjustePorcentual(byte Ajuste) {
        if (Ajuste < AJUSTE_LINEAL || Ajuste > AJUSTE_REFLEJO) {
            throw new Error("No se reconoce el tipo de ajuste");
        }
        AJUSTE_PORCENTUAL = Ajuste;
        return this;
    }

    public DegradadoPolar ModoCurva(byte Modo) {
        if (Modo < INTERPRETACIÓN_DEG_ALCANCE_CORTO || Modo > 50) {
            throw new Error("No se reconoce el modo de curva");
        }
        INTERPRETAR_DEG = Modo;
        return this;
    }

    public DegradadoPolar ModificarRevolucionesDeEvaluación(int... b) {
        return ModificarRevolucionesDeEvaluación(null, b);
    }

    public DegradadoPolar ModificarRevolucionesDeEvaluación(byte... b) {
        return ModificarRevolucionesDeEvaluación(null, b);
    }

    public DegradadoPolar ModificarRevolucionesDeEvaluación(OperaciónBinariaRGB op, int... b) {
        byte[] b2 = new byte[b.length];
        for (int i = 0; i < b.length; i++) {
            b2[i] = (byte) b[i];
        }
        return ModificarRevolucionesDeEvaluación(op, b2);
    }

    public DegradadoPolar ModificarRevolucionesDeEvaluación(OperaciónBinariaRGB op, byte... b) {
        if (b == null) {
            throw new Error("Los límites no pueden ser nulos");
        }
        if (b.length != 2) {
            throw new Error("Especifique una revolución de inicio y una de fin");
        }
        if (b[0] > b[1]) {
            throw new Error("La revolución de inicio es mayor a la de fin");
        }
        for (int i = 0; i < GirosDeEvaluación.length; i++) {
            GirosDeEvaluación[i] = b[i];
        }
        operaciónBinariaRGB = op;
        return this;
    }

    public DegradadoPolar CambiarCurvaRadial(Object curva) {
        return CambiarCurvaRadial(curva, Escala);
    }

    public DegradadoPolar CambiarCurvaRadial(Object curva, float escala) {
        if (curva instanceof Curva) {
            CurvaRadial = ((Curva) curva).Escalar(escala);
        } else if (curva instanceof Curva_nativa) {
            CurvaRadial = EscalarCurva((Curva_nativa) curva, escala);
        } else if (curva instanceof Number) {
            CurvaRadial = ((Number) curva).doubleValue() * escala;
        } else if (curva instanceof Función || curva instanceof FunciónPolar) {
            CurvaRadial = curva;
        } else {
            throw new Error("El objeto para la función radial es de instancia conocida");
        }
        return this;
    }

    @Override
    public PaintContext createContext(ColorModel cm, Rectangle r, Rectangle2D usr, AffineTransform at, RenderingHints rh) {
        return new DegradadoCónico_PaintContext();
    }

    @Override
    public int getTransparency() {
        return Transparency.TRANSLUCENT;
    }

    private final class DegradadoCónico_PaintContext implements PaintContext {

        @Override
        public void dispose() {
        }

        @Override
        public ColorModel getColorModel() {
            return ColorModel.getRGBdefault();
        }

        @Override
        public Raster getRaster(int x, int y, int w, int h) {
            int[] RGBA_Salida;
            final int Revoluciones = GirosDeEvaluación[1] - GirosDeEvaluación[0];
            int[][] Capas;
            final boolean SinOP = operaciónBinariaRGB == null;
            Capas = new int[SinOP ? 1 : Revoluciones][w * h * RGBA];
            int imgRev = GirosDeEvaluación[0];
            final Rango Circunferencia = new Rango(0, 2 * π), Porcentual = new Rango(0, 1);
            int INSTANCIA = 0;
            final int INSTANCIA_NÚMERO = 0,
                    INSTANCIA_CURVA = 1,
                    INSTANCIA_FUNCIÓN = 2,
                    INSTANCIA_FUNCIÓN_POLAR = 3;
            if (CurvaRadial instanceof Number) {
                INSTANCIA = INSTANCIA_NÚMERO;
            } else if (CurvaRadial instanceof Curva_nativa) {
                INSTANCIA = INSTANCIA_CURVA;
            } else if (CurvaRadial instanceof Función) {
                INSTANCIA = INSTANCIA_FUNCIÓN;
            } else {
                INSTANCIA = INSTANCIA_FUNCIÓN_POLAR;
            }
            boolean CalcularÁnguloPunto;
            CalcularÁnguloPunto = INTERPRETAR_DEG == INTERPRETACIÓN_DEG_ATRACTOR;
            CalcularÁnguloPunto |= INSTANCIA > INSTANCIA_NÚMERO;
            double ÁnguloPunto = 0;
            for (int Fila = 0; Fila < h; Fila++) {
                for (int Columna = 0; Columna < w; Columna++) {
                    double t = 0;
                    Dupla Punto = new Dupla(x + Columna, y + Fila).Sustraer(Centro).CambiarSentidoY();
                    double RadioPunto = Punto.Radio();
                    double RadioCurva = 1;
                    if (CalcularÁnguloPunto) {
                        ÁnguloPunto = Punto.Ángulo();
                        if (Δθ != 0) {
                            ÁnguloPunto -= Δθ;
                            ÁnguloPunto = Circunferencia.AjusteCiclico(ÁnguloPunto);
                        }
                    }
                    int it = Revoluciones;
                    if (SinOP) {
                        it = 1;
                    }
                    for (int i = 0; i < it; i++) {
                        double θ;
                        if (operaciónBinariaRGB == null && Revoluciones > 1) {
                            if (imgRev >= GirosDeEvaluación[1]) {
                                imgRev = GirosDeEvaluación[0];
                            }
                            θ = ÁnguloPunto + imgRev++ * 2 * π;
                        } else {
                            θ = ÁnguloPunto + (i + GirosDeEvaluación[0]) * 2 * π;
                        }
                        switch (INSTANCIA) {
                            case INSTANCIA_NÚMERO:
                                RadioCurva = ((Number) CurvaRadial).doubleValue();
                                break;
                            case INSTANCIA_CURVA:
                                RadioCurva = ((Curva_nativa) CurvaRadial).XY(θ).Radio();
                                break;
                            case INSTANCIA_FUNCIÓN:
                                RadioCurva = ((Función) CurvaRadial).Y(θ) * Escala;
                                break;
                            case INSTANCIA_FUNCIÓN_POLAR:
                                RadioCurva = ((FunciónPolar) CurvaRadial).r(θ) * Escala;
                                break;
                        }
                        if (interferenciaBinaria != null) {
                            RadioPunto = interferenciaBinaria.operar(RadioPunto, 1);
                            RadioCurva = interferenciaBinaria.operar(RadioCurva, 2);
                        }
                        double OP;
                        switch (INTERPRETAR_DEG) {
                            case INTERPRETACIÓN_DEG_ALCANCE_LARGO:
                                OP = RadioPunto / RadioCurva;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = OP;
                                break;
                            case INTERPRETACIÓN_DEG_ALCANCE_CORTO:
                                OP = RadioCurva / RadioPunto;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = OP;
                                break;
                            case INTERPRETACIÓN_DEG_SERPENTINA:
                                OP = RadioPunto - RadioCurva;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = OP / (Escala);
                                break;
                            case INTERPRETACIÓN_DEG_APERTURA:
                                OP = RadioPunto * RadioCurva;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = OP / (Escala * Escala);
                                break;
                            case INTERPRETACIÓN_DEG_VISLUMBRAR:
                                OP = RadioPunto % RadioCurva;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = OP / Escala;
                                break;
                            case INTERPRETACIÓN_DEG_FOCAL:
                                OP = RadioCurva % RadioPunto;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = OP / Escala;
                                break;
                            case INTERPRETACIÓN_DEG_LOG_DIVIDIR:
                                OP = RadioCurva / RadioPunto;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = Ln(abs(OP) + .0001);
                                break;
                            case INTERPRETACIÓN_DEG_LOG_MULTIPLICAR:
                                OP = RadioPunto * RadioCurva;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = Ln(abs(OP) + .0001);
                                break;
                            case INTERPRETACIÓN_DEG_LOG_ADICIONAR:
                                OP = RadioPunto + RadioCurva;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = Ln(abs(OP) + .0001);
                                break;
                            case INTERPRETACIÓN_DEG_LOG_MODULO1:
                                OP = RadioCurva % RadioPunto;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = Ln(abs(OP) + .0001);
                                break;
                            case INTERPRETACIÓN_DEG_LOG_MODULO2:
                                OP = RadioPunto % RadioCurva;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = Ln(abs(OP) + .0001);
                                break;
                            case INTERPRETACIÓN_DEG_LOG_EXCLUSIÓN_BITS:
                                OP = (int) RadioPunto ^ (int) RadioCurva;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = Ln(abs(OP) + .0001);
                                break;
                            case INTERPRETACIÓN_DEG_LOG_UNIÓN_BITS_SIERPINSKI:
                                OP = (int) RadioPunto | (int) RadioCurva;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = Ln(abs(OP) + .0001);
                                break;
                            case INTERPRETACIÓN_DEG_LOG_UNIÓN_BITS_SIERPINSKI_N2:
                                OP = (int) RadioPunto | (int) RadioCurva;
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = Ln(abs(OP) + .0001);
                                break;
                            case INTERPRETACIÓN_DEG_INTERSECCIÓN_BITS_SIERPINSKI:
                                t = Ln(abs((int) RadioPunto & (int) RadioCurva) + .0001);
                                break;
                            case INTERPRETACIÓN_DEG_ATRACTOR:
                                Dupla Atracción = Punto.Atracción(new Dupla(RadioPunto, θ, RADIANES), Escala);
                                OP = Atracción.Radio();
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = OP;
                                break;
                            case INTERPRETACIÓN_DEG_TEXTURIZADO:
                                Atracción = Punto.Atracción(new Dupla(RadioCurva, θ, RADIANES), Escala);
                                OP = (RadioPunto - RadioCurva) / (Atracción.Escalar(Escala * .03).Radio());
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = OP;
                                break;
                            case INTERPRETACIÓN_DEG_TEXTURIZADO2:
                                Atracción = Punto.Atracción(new Dupla(RadioCurva, θ, RADIANES), Escala);
                                OP = RadioPunto / (Atracción.Radio());
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = OP;
                                break;
                            case INTERPRETACIÓN_DEG_RAIZ2_BINARIO_LAZOS:
                            case INTERPRETACIÓN_DEG_RAIZ2_BINARIO_LAZOS_CON_MOÑO:
                                double C = 1;
                                if (INTERPRETAR_DEG==INTERPRETACIÓN_DEG_RAIZ2_BINARIO_LAZOS_CON_MOÑO) {
                                    C=π;
                                }
                                Atracción = Punto.Atracción(new Dupla(RadioCurva, θ, RADIANES), Escala);
                                OP = (((int) RadioPunto & (int) RadioCurva) ^ ((int) RadioPunto | (int) RadioCurva));
                                OP += Atracción.Radio();
                                if (interferenciaBinaria != null) {
                                    OP = interferenciaBinaria.operar(OP, 0);
                                }
                                t = raiz2(abs(OP * C));
                                break;
                        }   
                        switch (AJUSTE_PORCENTUAL) {
                            case AJUSTE_LINEAL:
                                t = Porcentual.AjusteLineal(t);
                                break;
                            case AJUSTE_CICLICO:
                                t = Porcentual.AjusteCiclico(t);
                                break;
                            case AJUSTE_REFLEJO:
                                t = Porcentual.AjusteReflejo(t);
                                break;
                        }
                        if (INVERTIR) {
                            t = 1 - t;
                        }
                        for (RectaColores rectaColor : colores) {
                            if (rectaColor.RangoDeAplicación.Por_Dentro(t, true)) {
                                final int[] Color = rectaColor.CalcularColor(t);
                                final int T = (Fila * w + Columna) * RGBA;
                                for (int Ch = R; Ch <= A; Ch++) {
                                    int P = Ch + T;
                                    Capas[i][P] = Color[Ch];
                                }
                                break;
                            }
                        }
                    }

                }
            }
            RGBA_Salida = Capas[0];
            if (Revoluciones > 1 && operaciónBinariaRGB != null) {
                for (int i = 1; i < Capas.length; i++) {
                    operaciónBinariaRGB.Calcular(RGBA_Salida, Capas[i], RGBA_Salida);
                }
            }
            final WritableRaster Salida = getColorModel().createCompatibleWritableRaster(w, h);
            Salida.setPixels(0, 0, w, h, RGBA_Salida);
            return Salida;
        }
    }

    public static class InterferenciaBinaria {

        public final static byte OP_BINARIO_UNIÓN = 0, OP_BINARIO_EXCLUSIÓN = 1, OP_BINARIO_INTERSECCIÓN = 2;
        public final boolean[] Negación = new boolean[4];
        public final byte[] InterferenciaBinariaOP = new byte[3];
        public final boolean[] HacerInterferencia = new boolean[3];
        public final int[] InterferenciaBinaria = new int[3];

        public InterferenciaBinaria(int[] ValoresInterferencia, byte[] OpsInterferencia, boolean[] HacerInterferencias, boolean[] Negaciones) {
            if (ValoresInterferencia.length != 3 || OpsInterferencia.length != 3 || Negaciones.length != 4) {
                throw new Error("La cantidad de canales en los parámetros no coinciden");
            }
            for (int i = 0; i < 4; i++) {
                if (i < 3) {
                    InterferenciaBinaria[i] = ValoresInterferencia[i];
                    InterferenciaBinariaOP[i] = OpsInterferencia[i];
                    HacerInterferencia[i] = HacerInterferencias[i];
                }
                Negación[i] = Negaciones[i];
            }
        }

        public double operar(double número, int Canal) {
            if (Canal < 0 || Canal > 2) {
                throw new Error("Fuera de los canales admitidos, intente con valores entre 0 y 2");
            }
            int n = (int) número;
            if (!HacerInterferencia[Canal]) {
                if (Negación[3]) {
                    return ~n;
                } else {
                    return número;
                }
            }
            int ValInterferencia = InterferenciaBinaria[Canal];
            if (Negación[Canal]) {
                ValInterferencia = ~ValInterferencia;
            }
            if (Negación[3]) {
                n = ~n;
            }
            switch (InterferenciaBinariaOP[Canal]) {
                case OP_BINARIO_UNIÓN:
                    n |= ValInterferencia;
                    break;
                case OP_BINARIO_INTERSECCIÓN:
                    n &= ValInterferencia;
                    break;
                case OP_BINARIO_EXCLUSIÓN:
                    n ^= ValInterferencia;
                    break;
                default:
                    throw new Error("El indicador de operación binaria no se reconoce");
            }
            return n;
        }
    }

    public class RectaColores {

        Rango RangoDeAplicación;
        int[] Colores = new int[2 * RGBA];

        public RectaColores(float ValorInicio, float ValorFin, Color ColorCola, Color ColorCabeza) {
            RangoDeAplicación = new Rango(ValorInicio, ValorFin);
            for (int i = 0; i < Colores.length / RGBA; i++) {
                for (int j = R; j <= A; j++) {
                    int bs = 8 * ((j + 1) % RGBA);
                    Colores[j + i * RGBA] = (i == 0 ? ColorCola : ColorCabeza).getRGB() << bs >>> 24;
                }
            }
        }

        int[] CalcularColor(double v) {
            double t = RangoDeAplicación.Vp(v);
            int Retorno[] = new int[RGBA];
            for (int Ch = R; Ch <= A; Ch++) {
                double Pi = Colores[Ch], Pf = Colores[Ch + RGBA];
                Retorno[Ch] = (int) ((Pf - Pi) * t + Pi);
            }
            return Retorno;
        }
    }
}
