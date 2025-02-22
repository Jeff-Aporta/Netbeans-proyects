package HerramientaDeImagen;

import static HerramientaDeImagen.LectoEscritor_Imagenes.*;
import HerramientasMatemáticas.Dupla;
import static HerramientasMatemáticas.Matemática.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ModoFusión extends ModoComposicionesPersonalizadas {

    OperaciónBinariaRGB OperaciónIntersección;
    byte ALPHA_DISCRIMINADOR;

    public final static byte //
            CÁLCULO_EXTENSIÓN_ADICIONAR = 0,
            CÁLCULO_EXTENSIÓN_RESTA = -128,
            CÁLCULO_EXTENSIÓN_MÓDULO = 1;

    public final static byte //
            NO_EXTENDER = 0,
            EXTENDER = 1,
            EXTENDER_INFERIOR = 2,
            EXTENDER_SUPERIOR = 3,
            EXTENDER_ADICIÓN = 4,
            EXTENDER_INTERSECCIÓN = 5,
            EXTENDER_UNIÓN = 6,
            EXTENDER_DIFERENCIA_XOR = 7;

    public final static byte// 
            //Operaciones elementales
            ModoFusión_ADICIÓN = 0,
            ModoFusión_DIFERENCIA_ABSOLUTA = 1,
            ModoFusión_DIFERENCIA = 2,
            ModoFusión_SUSTRAER = ModoFusión_DIFERENCIA,
            ModoFusión_DIFERENCIA_LATERALIZADA = 3,
            ModoFusión_PROMEDIO = 4,
            ModoFusión_DIVISIÓN = 5,
            ModoFusión_DIVIDE = ModoFusión_DIVISIÓN,
            ModoFusión_DIVISIÓN_LATERALIZADA = 6,
            ModoFusión_MÓDULO = 7,
            ModoFusión_MÓDULO_LATERALIZADO = 8,
            //Grises
            ModoFusión_GRIS_DISTANCIA = 9,
            //Nivel Binario
            ModoFusión_NIVEL_BINARIO_UNIÓN = 10,
            ModoFusión_NIVEL_BINARIO_INTERSECCIÓN = 11,
            ModoFusión_NIVEL_BINARIO_XOR = 12,
            ModoFusión_NIVEL_BINARIO_EXCLUSIÓN = ModoFusión_NIVEL_BINARIO_XOR,
            //Porcentual
            ModoFusión_PORCENTUAL_ADICIÓN_NULA = 13,
            ModoFusión_PORCENTUAL_PRODUCTO_VALORES_INVERTIDOS = 14,
            //Porcentuales, Pivote Base
            ModoFusión_PORCENTUAL_SOLAPAR_BASE = 15,
            ModoFusión_PORCENTUAL_ADICIÓN_BASE = 16,
            ModoFusión_PORCENTUAL_INCREMENTO_BASE = 17,
            ModoFusión_PORCENTUAL_DECREMENTO_BASE = 18,
            ModoFusión_PORCENTUAL_INCREMENTO_INVERTIDO_BASE = 19,
            ModoFusión_PORCENTUAL_PRODUCTO_BASE = 20,
            ModoFusión_PORCENTUAL_COCIENTE_BASE = 21,
            ModoFusión_PORCENTUAL_COCIENTE_MÁXIMO_BASE = 22,
            ModoFusión_PORCENTUAL_SUSTRACCIÓN_BASE = 23,
            ModoFusión_PORCENTUAL_SUSTRACCIÓN_LATERALIZADA_BASE = 24,
            ModoFusión_PORCENTUAL_SUSTRACCIÓN_ABSOLUTA_BASE = 25,
            ModoFusión_PORCENTUAL_POTENCIA_BASE = 26,
            //Porcentuales, Pivote superior
            ModoFusión_PORCENTUAL_SOLAPAR_SUPERIOR = 28,
            ModoFusión_PORCENTUAL_ADICIÓN_SUPERIOR = 29,
            ModoFusión_PORCENTUAL_INCREMENTO_SUPERIOR = 30,
            ModoFusión_PORCENTUAL_DECREMENTO_SUPERIOR = 31,
            ModoFusión_PORCENTUAL_INCREMENTO_INVERTIDO_SUPERIOR = 32,
            ModoFusión_PORCENTUAL_PRODUCTO_SUPERIOR = 33,
            ModoFusión_PORCENTUAL_COCIENTE_SUPERIOR = 34,
            ModoFusión_PORCENTUAL_COCIENTE_MÁXIMO_SUPERIOR = 35,
            ModoFusión_PORCENTUAL_SUSTRACCIÓN_SUPERIOR = 36,
            ModoFusión_PORCENTUAL_SUSTRACCIÓN_LATERALIZADA_SUPERIOR = 37,
            ModoFusión_PORCENTUAL_SUSTRACCIÓN_ABSOLUTA_SUPERIOR = 38,
            ModoFusión_PORCENTUAL_POTENCIA_SUPERIOR = 27,
            //Intercambio de canales, pivote Base
            ModoFusión_CONMUTAR_ROJO = 39,
            ModoFusión_CONMUTAR_ROJO_VERDE = 40,
            ModoFusión_CONMUTAR_ROJO_AZUL = 41,
            ModoFusión_CONMUTAR_VERDE = 42,
            ModoFusión_CONMUTAR_VERDE_AZUL = 43,
            ModoFusión_CONMUTAR_AZUL = 44,
            //Luces
            ModoFusión_LUZ_ROJA = 45,
            //Maximos y mínimos
            ModoFusión_MÁXIMO = 46,
            ModoFusión_MÍNIMO = 47,
            //Glitches
            ModoFusión_GLITCH1 = 48,
            ModoFusión_GLITCH2 = 49,
            ModoFusión_GLITCH3 = 50,
            //HSB
            COLOR = 51,
            //Paralelos en Photoshop
            ModoFusión_MULTIPLICAR = ModoFusión_PORCENTUAL_ADICIÓN_NULA,
            ModoFusión_COLOR_MÁS_OSCURO = ModoFusión_MÍNIMO,
            ModoFusión_TRAMA = ModoFusión_PORCENTUAL_PRODUCTO_VALORES_INVERTIDOS,
            ModoFusión_PANTALLA = ModoFusión_TRAMA,
            ModoFusión_COLOR_MÁS_CLARO = ModoFusión_MÁXIMO,
            ModoFusión_SOLAPAR = ModoFusión_PORCENTUAL_SOLAPAR_BASE,
            ModoFusión_LUZ_SUAVE = ModoFusión_SOLAPAR,
            ModoFusión_SOBREEXPOSICIÓN_LINEAL = ModoFusión_ADICIÓN,
            ModoFusión_AÑADIR = ModoFusión_ADICIÓN,
            ModoFusión_ADITIVO = ModoFusión_ADICIÓN,
            ModoFusión_LUZ_LINEAL = ModoFusión_PORCENTUAL_ADICIÓN_BASE,
            ModoFusión_SUBSTRACT = ModoFusión_SUSTRAER;

    public static void main(String[] args) {
//        int a = 200 << 24;
//        int r = 128 << 16;
//        int g = 30 << 8;
//        int b = 12 << 0;
//        int argb = a | r | g | b;
//        System.out.println(argb);
//        System.out.println("a: " + (argb << 0 >>> 24));
//        System.out.println("r: " + (argb << 8 >>> 24));
//        System.out.println("g: " + (argb << 16 >>> 24));
//        System.out.println("b: " + (argb << 24 >>> 24));
        JLabel Presentador = new JLabel();
        new JFrame() {
            {
                add(Presentador);
                setSize(900, 720);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setResizable(false);
                setVisible(true);
            }
        };
        BufferedImage img1 = cargarImagen(
                "/HerramientaDeImagen/ZZimgPrueba2.jpg"
        );
        BufferedImage img2 = cargarImagen(
                "/HerramientaDeImagen/ZZimgPrueba1.jpg"
        );
        BufferedImage b = Dupla.convBufferedImage(Presentador);
        BufferedImage m = Dupla.convBufferedImage(Presentador);
        {
            Graphics2D g = m.createGraphics();
            Dupla pos;
            pos = Dupla.Alinear(b, img1, Dupla.MEDIO, Dupla.MEDIO).SustraerX(20);
            g.drawImage(img1, pos.intX(), pos.intY(), null);
            pos = Dupla.Alinear(b, img2, Dupla.MEDIO, Dupla.MEDIO).AdicionarX(20);
            g.setComposite(CargarModoFusión(ModoFusión_LUZ_LINEAL));
            g.drawImage(img2, pos.intX(), pos.intY(), null);
            BufferedImage textura = new BufferedImage(20, 20, 2);
            g = textura.createGraphics();
            g.setColor(new Color(220, 220, 220));
            g.fillRect(0, 0, textura.getWidth(), textura.getHeight());
            g.setColor(Color.WHITE);
            int w = textura.getWidth() / 2;
            int h = textura.getHeight() / 2;
            g.fillOval(0, 0, w, h);
            g.fillOval(w, h, w, h);
            g = b.createGraphics();
            g.setPaint(new TexturePaint(textura, new Rectangle(0, 0, w * 2, h * 2)));
            g.fillRect(0, 0, b.getWidth(), b.getHeight());
            g.drawImage(m, 0, 0, null);
        }
        Presentador.setIcon(new ImageIcon(b));
    }

    public static ModoFusión CargarModoFusión(byte ModoFusión, byte AJUSTE_ESPACIO) {
        return CargarModoFusión(
                ModoFusión,
                true,
                false,
                NO_EXTENDER,
                AJUSTE_ESPACIO
        );
    }

    public static ModoFusión CargarModoFusión(byte ModoFusión) {
        return CargarModoFusión(
                ModoFusión,
                true,
                false,
                NO_EXTENDER,
                AJUSTE_LINEAL
        );
    }

    public final static ModoFusión CargarModoFusión(
            byte ModoFusión, boolean Normal, boolean PintarSoloDentro,
            byte EXTENSIÓN, byte AJUSTE_ESPACIO
    ) {
        OperaciónBinariaRGB operación = CargarOperación(ModoFusión);
//        operación = new OperaciónBinariaRGB() {
//            @Override
//            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
//                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
//                    for (int Ch = R; Ch <= B; Ch++) {
//                        int P = Ch + e * RGBA;
//                        RGBA_Salida[P] = (int) (255.0f*RGBA_Pintar[P]/(RGBA_Pintar[P]-RGBA_Pintado[P]));
//                    }
//                }
//            }
//        };
//        operación = CombinaciónDe6Dimensiones(
//                new double[][]{
//                    {0, 0, 0},
//                    {0, 1, 0},
//                    {0, 0, 1},
//                    {1, 0, 0},
//                    {0, 1, 0},
//                    {0, 0, 1}
//                }
//        );
        return new ModoFusión(operación, PintarSoloDentro)
                .TipoDeAjuste(AJUSTE_ESPACIO)
                .ModoExtensión(EXTENSIÓN);
    }

    static OperaciónBinariaRGB CargarOperación(byte ModoFusión) {
        OperaciónBinariaRGB operación = null;
        switch (ModoFusión) {
            case ModoFusión_SOBREEXPOSICIÓN_LINEAL:
                operación = Aditivo();
                break;
            case ModoFusión_DIFERENCIA_ABSOLUTA:
                operación = DiferenciaAbsoluta();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_DIFERENCIA:
                operación = Diferencia();
                break;
            case ModoFusión_DIFERENCIA_LATERALIZADA:
                operación = DiferenciaLateralizada();
                break;
            case ModoFusión_PROMEDIO:
                operación = Promedio();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_GRIS_DISTANCIA:
                operación = GrisDistancia();
                break;
            case ModoFusión_NIVEL_BINARIO_UNIÓN:
                operación = Unión();
                break;
            case ModoFusión_NIVEL_BINARIO_INTERSECCIÓN:
                operación = Intersección();
                break;
            case ModoFusión_NIVEL_BINARIO_XOR:
                operación = ExclusiónXOR();
                break;
            case ModoFusión_DIVISIÓN:
                operación = División();
                break;
            case ModoFusión_DIVISIÓN_LATERALIZADA:
                operación = DivisiónReciproca();
                break;
            case ModoFusión_MÓDULO:
                operación = Módulo();
                break;
            case ModoFusión_MÓDULO_LATERALIZADO:
                operación = MóduloLateralizado();
                break;
            case ModoFusión_PORCENTUAL_ADICIÓN_NULA:
                operación = PorcentualAdiciónNula();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_PORCENTUAL_INCREMENTO_BASE:
                operación = PorcentualIncrementoBase();
                break;
            case ModoFusión_PORCENTUAL_SOLAPAR_BASE:
                operación = PorcentualSolaparBase();
                break;
            case ModoFusión_PORCENTUAL_DECREMENTO_BASE:
                operación = PorcentualDecrementoBase();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_PORCENTUAL_INCREMENTO_INVERTIDO_BASE:
                operación = PorcentualIncrementoBaseInvertido();
                break;
            case ModoFusión_PORCENTUAL_PRODUCTO_BASE:
                operación = PorcentualProductoBase();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_PORCENTUAL_COCIENTE_BASE:
                operación = PorcentualCocienteBase();
                break;
            case ModoFusión_PORCENTUAL_COCIENTE_MÁXIMO_BASE:
                operación = PorcentualCocienteMáximoBase();
                break;
            case ModoFusión_PORCENTUAL_ADICIÓN_BASE:
                operación = PorcentualAdiciónBase();
                break;
            case ModoFusión_PORCENTUAL_SUSTRACCIÓN_BASE:
                operación = PorcentualSustracciónBase();
                break;
            case ModoFusión_PORCENTUAL_SUSTRACCIÓN_LATERALIZADA_BASE:
                operación = PorcentualSustracciónLateralizadaBase();
                break;
            case ModoFusión_PORCENTUAL_SUSTRACCIÓN_ABSOLUTA_BASE:
                operación = PorcentualSustracciónAbsolutaBase();
                break;
            case ModoFusión_PORCENTUAL_POTENCIA_BASE:
                operación = PorcentualPotenciaBase();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_PORCENTUAL_SOLAPAR_SUPERIOR:
                operación = PorcentualSolaparSuperior();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_PORCENTUAL_INCREMENTO_SUPERIOR:
                operación = PorcentualIncrementoSuperior();
                break;
            case ModoFusión_PORCENTUAL_DECREMENTO_SUPERIOR:
                operación = PorcentualDecrementoSuperior();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_PORCENTUAL_INCREMENTO_INVERTIDO_SUPERIOR:
                operación = PorcentualIncrementoSuperiorInvertido();
                break;
            case ModoFusión_PORCENTUAL_PRODUCTO_SUPERIOR:
                operación = PorcentualProductoSuperior();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_PORCENTUAL_COCIENTE_SUPERIOR:
                operación = PorcentualCocienteSuperior();
                break;
            case ModoFusión_PORCENTUAL_COCIENTE_MÁXIMO_SUPERIOR:
                operación = PorcentualCocienteMáximoSuperior();
                break;
            case ModoFusión_PORCENTUAL_ADICIÓN_SUPERIOR:
                operación = PorcentualAdiciónSuperior();
                break;
            case ModoFusión_PORCENTUAL_SUSTRACCIÓN_SUPERIOR:
                operación = PorcentualSustracciónSuperior();
                break;
            case ModoFusión_PORCENTUAL_SUSTRACCIÓN_LATERALIZADA_SUPERIOR:
                operación = PorcentualSustracciónLateralizadaSuperior();
                break;
            case ModoFusión_PORCENTUAL_POTENCIA_SUPERIOR:
                operación = PorcentualPotenciasSuperior();
                operación.RiesgoSalida = true;
                break;
            case ModoFusión_PORCENTUAL_SUSTRACCIÓN_ABSOLUTA_SUPERIOR:
                operación = PorcentualSustracciónAbsolutaSuperior();
                break;
            case ModoFusión_CONMUTAR_ROJO:
                operación = ConmutarRojoBase();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_CONMUTAR_ROJO_VERDE:
                operación = ConmutarRojoVerdeBase();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_CONMUTAR_ROJO_AZUL:
                operación = ConmutarRojoAzulBase();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_CONMUTAR_VERDE:
                operación = ConmutarVerdeBase();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_CONMUTAR_VERDE_AZUL:
                operación = ConmutarVerdeAzulBase();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_CONMUTAR_AZUL:
                operación = ConmutarAzulBase();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_MÁXIMO:
                operación = Máximo();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_MÍNIMO:
                operación = Mínimo();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_TRAMA:
                operación = PorcentualProductoValoresInvertidos();
                operación.RiesgoSalida = false;
                break;
            case ModoFusión_GLITCH1:
                operación = Glitch1();
                break;
            case ModoFusión_GLITCH2:
                operación = Glitch2();
                break;
            case ModoFusión_GLITCH3:
                operación = Glitch3();
                break;
            case COLOR:
                operación = Color();
        }
        if (operación == null) {
            throw new Error("El modo de operación no se ha reconocido");
        }
        return operación;
    }

    public ModoFusión(OperaciónBinariaRGB operación) {
        OperaciónIntersección = operación;
        ALPHA_DISCRIMINADOR = PINTAR_ENCIMA;
    }

    public ModoFusión(OperaciónBinariaRGB operación, boolean ALPHA_PINTAR_DENTRO) {
        OperaciónIntersección = operación;
        ALPHA_DISCRIMINADOR = ALPHA_PINTAR_DENTRO ? PINTAR_DENTRO : PINTAR_ENCIMA;
    }

    public ModoFusión ModoExtensión(byte ModoExtensión) {
        OperaciónIntersección.ModoFusión_EXTENSIÓN = ModoExtensión;
        return this;
    }

    public ModoFusión CálculoExtensión(byte Calculo) {
        OperaciónIntersección.CÁLCULO_EXTENSIÓN = Calculo;
        return this;
    }

    public ModoFusión Transparencia(float Transparencia) {
        if (Transparencia < 0 || Transparencia > 1) {
            throw new Error("Transparencia fuera de rango, entre 0 y 1");
        }
        OperaciónIntersección.Transparencia = Transparencia;
        return this;
    }

    public ModoFusión TenerEnCuentaElAlpha(boolean Alpha) {
        OperaciónIntersección.TenerEnCuentaElAlpha = Alpha;
        return this;
    }

    public ModoFusión AlphaDiscriminador(byte AlphaDiscriminador) {
        ALPHA_DISCRIMINADOR = AlphaDiscriminador;
        return this;
    }

    public ModoFusión PorcentajeAplicación(double... p) {
        boolean Identidad = true;
        OperaciónIntersección.PorcentajeAplicación = new double[RGB];
        for (int i = 0; i < RGB; i++) {
            OperaciónIntersección.PorcentajeAplicación[i] = p[i];
            Identidad &= p[i] == 1;
        }
        if (Identidad) {
            OperaciónIntersección.PorcentajeAplicación = null;
        }
        OperaciónIntersección.AplicarPorcentajesAplicación = !Identidad;
        return this;
    }

    public ModoFusión TipoDeAjuste(byte TipoAjuste) {
        AJUSTE_ESPACIO_RGB = OperaciónIntersección.AJUSTE_ESPACIO_RGB = TipoAjuste;
        return this;
    }

    @Override
    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return new CompositeContext() {
            @Override
            public void compose(Raster ÁreaPintar, Raster ÁreaPintada, WritableRaster RGBA_Salida) {
                try {
                    int w = Mín(ÁreaPintar.getWidth(), ÁreaPintada.getWidth());
                    int h = Mín(ÁreaPintar.getHeight(), ÁreaPintada.getHeight());
                    int e = w * h;
                    int[] RGBASalida = new int[RGBA * e];
                    boolean InserciónSalida = AJUSTE_ESPACIO_RGB > AJUSTE_REFLEJO && AJUSTE_ESPACIO_RGB < AJUSTE_ELIMINACIÓN_POR_SALIDA;
                    {
                        if (OperaciónIntersección.RiesgoSalida || !InserciónSalida) {
                            OperaciónIntersección.Calcular(
                                    ÁreaPintar.getPixels(0, 0, w, h, new int[RGBA * e]),
                                    ÁreaPintada.getPixels(0, 0, w, h, new int[RGBA * e]),
                                    RGBASalida, ALPHA_DISCRIMINADOR
                            );
                        }
                    }
                    RGBA_Salida.setPixels(0, 0, w, h, RGBASalida);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void dispose() {
            }
        };
    }

    interface Op {

        void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida);
    }

    final static OperaciónBinariaRGB Color() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    int r1, g1, b1;
                    r1 = RGBA_Pintado[R + e * RGBA];
                    g1 = RGBA_Pintado[G + e * RGBA];
                    b1 = RGBA_Pintado[B + e * RGBA];
                    float[] HSB1 = Color.RGBtoHSB(r1, g1, b1, null);
                    int r2, g2, b2;
                    r2 = RGBA_Pintar[R + e * RGBA];
                    g2 = RGBA_Pintar[G + e * RGBA];
                    b2 = RGBA_Pintar[B + e * RGBA];
                    float[] HSB2 = Color.RGBtoHSB(r2, g2, b2, null);
                    int rgb = Color.HSBtoRGB(HSB2[0], HSB2[1], HSB2[2]);
                    RGBA_Salida[R + e * RGBA] = rgb << 8 >>> 24;
                    RGBA_Salida[G + e * RGBA] = rgb << 16 >>> 24;
                    RGBA_Salida[B + e * RGBA] = rgb << 24 >>> 24;
                }
            }
        };
    }

    final static OperaciónBinariaRGB Glitch3() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) poten(RGBA_Pintado[P], RGBA_Pintar[P] / 255.0f);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB Glitch2() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (255.0f * 255.0f / (RGBA_Pintar[P] - RGBA_Pintado[P]));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB Glitch1() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (255.0f * RGBA_Pintar[P] / (RGBA_Pintar[P] - RGBA_Pintado[P]));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB CombinaciónDe6Dimensiones(float[]... RMatriz) {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    float[] V6 = new float[2 * RGB];
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        V6[Ch] = RGBA_Pintado[P];
                        V6[Ch + RGB] = RGBA_Pintar[P];
                    }
                    float NuevoColor[] = MultiplicarVectorMatriz(V6, RMatriz);
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) NuevoColor[Ch];
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB Mínimo() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = Mín(RGBA_Pintado[P], RGBA_Pintar[P]);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB Máximo() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = Máx(RGBA_Pintado[P], RGBA_Pintar[P]);
                    }
                }

            }
        };
    }

    final static OperaciónBinariaRGB ConmutarRojoAzulBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    int T = e * RGBA;
                    RGBA_Salida[R + T] = RGBA_Pintar[R + T];
                    RGBA_Salida[G + T] = RGBA_Pintado[G + T];
                    RGBA_Salida[B + T] = RGBA_Pintar[B + T];
                }
            }
        };
    }

    final static OperaciónBinariaRGB ConmutarAzulBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    int T = e * RGBA;
                    RGBA_Salida[R + T] = RGBA_Pintado[R + T];
                    RGBA_Salida[G + T] = RGBA_Pintado[G + T];
                    RGBA_Salida[B + T] = RGBA_Pintar[B + T];
                }
            }
        };
    }

    final static OperaciónBinariaRGB ConmutarVerdeAzulBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    int T = e * RGBA;
                    RGBA_Salida[R + T] = RGBA_Pintado[R + T];
                    RGBA_Salida[G + T] = RGBA_Pintar[G + T];
                    RGBA_Salida[B + T] = RGBA_Pintar[B + T];
                }
            }
        };
    }

    final static OperaciónBinariaRGB ConmutarVerdeBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    int T = e * RGBA;
                    RGBA_Salida[R + T] = RGBA_Pintado[R + T];
                    RGBA_Salida[G + T] = RGBA_Pintar[G + T];
                    RGBA_Salida[B + T] = RGBA_Pintado[B + T];
                }
            }
        };
    }

    final static OperaciónBinariaRGB ConmutarRojoVerdeBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    final int T = e * RGBA;
                    RGBA_Salida[R + T] = RGBA_Pintar[R + T];
                    RGBA_Salida[G + T] = RGBA_Pintar[G + T];
                    RGBA_Salida[B + T] = RGBA_Pintado[B + T];
                }
            }
        };
    }

    final static OperaciónBinariaRGB ConmutarRojoBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    final int T = e * RGBA;
                    RGBA_Salida[R + T] = RGBA_Pintar[R + T];
                    RGBA_Salida[G + T] = RGBA_Pintado[G + T];
                    RGBA_Salida[B + T] = RGBA_Pintado[B + T];
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualAdiciónNula() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) Porcentaje(RGBA_Pintar[P] * RGBA_Pintado[P]);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualSolaparSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (Porcentaje(RGBA_Pintar[P]) * (RGBA_Pintar[P] + Porcentaje(2 * RGBA_Pintado[P]) * (255 - RGBA_Pintar[P])));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualSustracciónAbsolutaSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (Porcentaje(RGBA_Pintar[P]) * abs(RGBA_Pintado[P] - RGBA_Pintar[P]));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB Porcentual4PotenciasSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) poten(RGBA_Pintar[P], 4 * RGBA_Pintado[P] / 255.0f);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB Porcentual2PotenciasSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) poten(RGBA_Pintar[P], 2 * RGBA_Pintado[P] / 255.0f);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualPotenciasSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) poten(RGBA_Pintar[P], RGBA_Pintado[P] / 255.0f);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualSustracciónLateralizadaSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (Porcentaje(RGBA_Pintar[P]) * (RGBA_Pintar[P] - RGBA_Pintado[P]));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualSustracciónSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (Porcentaje(RGBA_Pintar[P]) * (RGBA_Pintado[P] - RGBA_Pintar[P]));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualAdiciónSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (Porcentaje(RGBA_Pintar[P]) * (RGBA_Pintado[P] + RGBA_Pintar[P]));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualCocienteMáximoSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    final int T = e * RGBA;
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + T;
                        RGBA_Salida[P] = (int) (RGBA_Pintar[P] * 255.0 / RGBA_Pintado[P]);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualCocienteSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (poten2(RGBA_Pintar[P]) / RGBA_Pintado[P]);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualProductoSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (poten2(Porcentaje(RGBA_Pintar[P])) * RGBA_Pintado[P]);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualIncrementoSuperiorInvertido() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (RGBA_Pintar[P] * (2 - Porcentaje(RGBA_Pintado[P])));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualDecrementoSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (RGBA_Pintar[P] * (1 - Porcentaje(RGBA_Pintado[P])));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualIncrementoSuperior() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (RGBA_Pintar[P] * (1 + Porcentaje(RGBA_Pintado[P])));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualProductoValoresInvertidos() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (255 - Porcentaje((255 - RGBA_Pintar[P]) * (255 - RGBA_Pintado[P])));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualSolaparBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (Porcentaje(RGBA_Pintado[P]) * (RGBA_Pintado[P] + Porcentaje(2 * RGBA_Pintar[P]) * (255 - RGBA_Pintado[P])));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualSustracciónAbsolutaBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (Porcentaje(RGBA_Pintado[P]) * abs(RGBA_Pintado[P] - RGBA_Pintar[P]));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualPotenciaBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) poten(RGBA_Pintado[P], RGBA_Pintar[P] / 255.0f);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualSustracciónLateralizadaBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (Porcentaje(RGBA_Pintado[P]) * (RGBA_Pintar[P] - RGBA_Pintado[P]));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualSustracciónBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (Porcentaje(RGBA_Pintado[P]) * (RGBA_Pintado[P] - RGBA_Pintar[P]));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualAdiciónBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (Porcentaje(RGBA_Pintado[P]) * (RGBA_Pintado[P] + RGBA_Pintar[P]));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualCocienteMáximoBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (RGBA_Pintado[P] * 255.0 / RGBA_Pintar[P]);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualCocienteBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (poten2(RGBA_Pintado[P]) / RGBA_Pintar[P]);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualProductoBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (poten2(Porcentaje(RGBA_Pintado[P])) * RGBA_Pintar[P]);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualIncrementoBaseInvertido() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (RGBA_Pintado[P] * (2 - Porcentaje(RGBA_Pintar[P])));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualDecrementoBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (RGBA_Pintado[P] * (1 - Porcentaje(RGBA_Pintar[P])));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB PorcentualIncrementoBase() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (RGBA_Pintado[P] * (1 + Porcentaje(RGBA_Pintar[P])));
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB MóduloLateralizado() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = RGBA_Pintado[P] % RGBA_Pintar[P];
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB Módulo() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = RGBA_Pintar[P] % RGBA_Pintado[P];
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB DivisiónReciproca() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (RGBA_Pintar[P] * EspacioRGB.ValMáx() / RGBA_Pintado[P]);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB División() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) (RGBA_Pintado[P] * EspacioRGB.ValMáx() / RGBA_Pintar[P]);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB ExclusiónXOR() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) RGBA_Pintado[P] ^ (int) RGBA_Pintar[P];
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB Intersección() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) RGBA_Pintado[P] & (int) RGBA_Pintar[P];
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB Unión() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (int) RGBA_Pintado[P] | (int) RGBA_Pintar[P];
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB GrisDistancia() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    int d = Distancia(
                            RGBA_Pintar[R + e * RGBA] - RGBA_Pintado[R + e * RGBA],
                            RGBA_Pintar[G + e * RGBA] - RGBA_Pintado[G + e * RGBA],
                            RGBA_Pintar[B + e * RGBA] - RGBA_Pintado[B + e * RGBA]
                    );
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = d;
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB Promedio() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = (RGBA_Pintado[P] + RGBA_Pintar[P]) / 2;
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB DiferenciaLateralizada() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = RGBA_Pintar[P] - RGBA_Pintado[P];
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB Diferencia() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = RGBA_Pintado[P] - RGBA_Pintar[P];
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB DiferenciaAbsoluta() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = abs(RGBA_Pintado[P] - RGBA_Pintar[P]);
                    }
                }
            }
        };
    }

    final static OperaciónBinariaRGB Aditivo() {
        return new OperaciónBinariaRGB() {
            @Override
            public void Operar(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBASalida) {
                for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBASalida[P] = RGBA_Pintado[P] + RGBA_Pintar[P];
                    }
                }
            }
        };
    }

    public abstract static class OperaciónBinariaRGB implements Op {

        byte ModoFusión_EXTENSIÓN = NO_EXTENDER;
        byte CÁLCULO_EXTENSIÓN = CÁLCULO_EXTENSIÓN_ADICIONAR;
        byte AJUSTE_ESPACIO_RGB = AJUSTE_LINEAL;

        boolean AplicarPorcentajesAplicación = false;
        boolean TenerEnCuentaElAlpha = true;
        boolean RiesgoSalida = true;

        double[] PorcentajeAplicación;
        float Transparencia = 1;

        void Calcular(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
            Calcular(RGBA_Pintar, RGBA_Pintado, RGBA_Salida, PINTAR_ENCIMA);
        }

        void Calcular(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida, int ALPHA_DISCRIMINADOR) {
            if (ALPHA_DISCRIMINADOR < PINTAR_ENCIMA || ALPHA_DISCRIMINADOR > PINTAR_DENTRO) {
                throw new Error("Alpha discriminador irreconocible, valores admisibles [0,1]");
            }
            if (ALPHA_DISCRIMINADOR == PINTAR_DENTRO) {
                for (int e = 0; e < RGBA_Pintado.length / RGBA; e++) {
                    int PA = A + e * 4;
                    RGBA_Salida[PA] = RGBA_Pintado[PA];
                }
            } else {
                for (int e = 0; e < RGBA_Pintado.length / RGBA; e++) {
                    int PA = A + e * 4;
                    RGBA_Salida[PA] = EspacioRGB.AjusteLineal(RGBA_Pintado[PA] + RGBA_Pintar[PA]);
                }
            }
            Operar(RGBA_Pintar, RGBA_Pintado, RGBA_Salida);
            if (ModoFusión_EXTENSIÓN != NO_EXTENDER) {
                Extender(RGBA_Pintar, RGBA_Pintado, RGBA_Salida);
            }
            if (AplicarPorcentajesAplicación) {
                CalcularPorcentajeDeAplicación(RGBA_Pintar);
            }
            if (TenerEnCuentaElAlpha) {
                DiscriminarRGBSegúnAlpha(RGBA_Pintar, RGBA_Pintado, RGBA_Salida);
            }
            if (RiesgoSalida) {
                AjustarAlEspacio(RGBA_Salida, AJUSTE_ESPACIO_RGB);
            }
        }

        void CalcularPorcentajeDeAplicación(int[] RGBA_Pintar) {
            for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                for (int Ch = R; Ch < A; Ch++) {
                    int P = Ch + e * RGBA;
                    RGBA_Pintar[P] *= PorcentajeAplicación[Ch];
                }
            }
        }

        double CálcularExtención(int[] RGBA_Pintado, int[] RGBA_Pintar) {
            double ext = EspacioRGB.ValMáx();
            double r = 0, g = 0, b = 0;
            switch (ModoFusión_EXTENSIÓN) {
                case EXTENDER:
                    ext = DistanciaRGB(RGBA_Pintar, RGBA_Pintado);
                    break;
                case EXTENDER_INFERIOR:
                    ext = MóduloRGB(RGBA_Pintado);
                    break;
                case EXTENDER_SUPERIOR:
                    ext = MóduloRGB(RGBA_Pintar);
                    break;
                case EXTENDER_ADICIÓN:
                    r = RGBA_Pintado[R] + RGBA_Pintar[R];
                    g = RGBA_Pintado[G] + RGBA_Pintar[G];
                    b = RGBA_Pintado[B] + RGBA_Pintar[B];
                    break;
                case EXTENDER_INTERSECCIÓN:
                    r = (int) RGBA_Pintado[R] & (int) RGBA_Pintar[R];
                    g = (int) RGBA_Pintado[G] & (int) RGBA_Pintar[G];
                    b = (int) RGBA_Pintado[B] & (int) RGBA_Pintar[B];
                    break;
                case EXTENDER_UNIÓN:
                    r = (int) RGBA_Pintado[R] | (int) RGBA_Pintar[R];
                    g = (int) RGBA_Pintado[G] | (int) RGBA_Pintar[G];
                    b = (int) RGBA_Pintado[B] | (int) RGBA_Pintar[B];
                    break;
                case EXTENDER_DIFERENCIA_XOR:
                    r = (int) RGBA_Pintado[R] ^ (int) RGBA_Pintar[R];
                    g = (int) RGBA_Pintado[G] ^ (int) RGBA_Pintar[G];
                    b = (int) RGBA_Pintado[B] ^ (int) RGBA_Pintar[B];
                    break;
            }
            if (ModoFusión_EXTENSIÓN > EXTENDER_SUPERIOR) {
                switch (CÁLCULO_EXTENSIÓN) {
                    case CÁLCULO_EXTENSIÓN_ADICIONAR:
                        ext = r + g + b;
                        break;
                    case CÁLCULO_EXTENSIÓN_RESTA:
                        ext = -r - g - b;
                        break;
                    case CÁLCULO_EXTENSIÓN_MÓDULO:
                        ext = Distancia(r, g, b);
                        break;
                }
            }
            return ext;
        }

        void Extender(int[] ComponentesPintar, int[] ComponentesPintados, int[] ComponentesSalida) {
            for (int e = 0; e < ComponentesPintar.length / RGBA; e++) {
                int A1 = ComponentesPintar[A + e * RGBA];
                int A2 = ComponentesPintados[A + e * RGBA];
                boolean HayIntersección = HayIntersección(A1, A2);
                if (HayIntersección) {
                    int[] RGB1 = new int[RGB];
                    int[] RGB2 = new int[RGB];
                    for (int Ch = R; Ch < A; Ch++) {
                        int P = Ch + e * RGBA;
                        RGB1[Ch] = ComponentesPintados[P];
                        RGB2[Ch] = ComponentesPintar[P];
                    }
                    for (int Ch = R; Ch < A; Ch++) {
                        int P = Ch + e * RGBA;
                        double ext = CálcularExtención(RGB1, RGB2);
                        ComponentesSalida[P] = (int) (ComponentesSalida[P] * EspacioRGB.ValMáx() / ext);
                    }
                }
            }
        }

        void DiscriminarRGBSegúnAlpha(int[] RGBA_Pintar, int[] RGBA_Pintado, int[] RGBA_Salida) {
            for (int e = 0; e < RGBA_Pintar.length / RGBA; e++) {
                final int Ch_α = A + e * RGBA;
                float α1 = Porcentaje(RGBA_Pintado[Ch_α]), α2 = Porcentaje(RGBA_Pintar[Ch_α]);
                boolean A1 = α1 > 0, A2 = α2 > 0;
                if (A1 && A2) {
                    float t = α2 * Transparencia;
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        float //
                                pi = RGBA_Pintado[P] * α1,
                                pf = RGBA_Salida[P] * α2;
                        RGBA_Salida[P] = (int) ((pf - pi) * t + pi);
                    }
                } else if (A1) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = RGBA_Pintado[P];
                    }
                } else if (A2) {
                    for (int Ch = R; Ch <= B; Ch++) {
                        int P = Ch + e * RGBA;
                        RGBA_Salida[P] = RGBA_Pintar[P];
                    }
                    RGBA_Salida[Ch_α] *= Transparencia;
                }

            }
        }
    }
}
