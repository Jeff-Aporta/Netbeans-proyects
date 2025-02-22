package HerramientaDeImagen;

import HerramientasMatemáticas.Dupla;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import static HerramientasMatemáticas.Matemática.*;

public final class DegradadoCónico implements Paint {

    final static int R = 0, G = 1, B = 2, A = 3, RGBA = 4;

    Dupla Centro;
    RectaColores[] colores;
    float Δθ = 0;

    public static void main(String[] args) {
        JLabel Presentador = new JLabel();
        new JFrame() {
            {
                setSize(600, 600);
                setLocationRelativeTo(null);
                add(Presentador);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }
        };
        Dupla PosiciónCentro = new Dupla(Presentador).Mitad();
        Color[] ColoresPintar;
        float[] PorcentajesAparición;
        int Divisiones = 16;
        ColoresPintar = new Color[Divisiones];
        PorcentajesAparición = new float[Divisiones];
        for (int i = 0; i < Divisiones; i++) {
            ColoresPintar[i] = Color.getHSBColor(i * (1f / Divisiones), 1, 1);
            PorcentajesAparición[i] = i * (1f / Divisiones);
        }
        ColoresPintar = new Color[]{new Color(255,228,0), new Color(250,0,3), new Color(68,0,0),new Color(250,0,3)};
        PorcentajesAparición = new float[]{0, .2f, .5f,0.8f};
        DegradadoCónico DegradadoCónico = new DegradadoCónico(
                PosiciónCentro,
                PorcentajesAparición,
                ColoresPintar,
                (float) Radianes(0)
        );
        BufferedImage Fotograma = new BufferedImage(Presentador.getWidth(), Presentador.getHeight(), 2) {
            {
                Graphics2D g = createGraphics();
                g.setPaint(DegradadoCónico);
                g.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        Presentador.setIcon(new ImageIcon(Fotograma));
    }

    public DegradadoCónico(Dupla Centro, float[] PorcentajesDeAparición, Color[] ColoresPintar) {
        this(Centro, PorcentajesDeAparición, ColoresPintar, 0);
    }

    public DegradadoCónico(Dupla Centro, float[] PorcentajesDeAparición, Color[] ColoresPintar, float Δθ) {
        this.Δθ = Δθ;
        if (PorcentajesDeAparición.length != ColoresPintar.length) {
            throw new Error("El tamaño de los arreglos no coincide, Son diferentes");
        }
        int Elementos = ColoresPintar.length;
        if (Elementos < 2) {
            throw new Error("Se necesitan como mínimo 2 elementos de color para el degradado cónico");
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
            final WritableRaster RASTER = getColorModel().createCompatibleWritableRaster(w, h);
            int[] RGBA_Salida = new int[w * h * RGBA];
            Rango Circunferencia = new Rango(0, 2 * π);
            for (int Fila = 0; Fila < h; Fila++) {
                for (int Columna = 0; Columna < w; Columna++) {
                    double ángulo = new Dupla(x + Columna, y + Fila).Sustraer(Centro).CambiarSentidoY().Ángulo();
                    ángulo -= Δθ;
                    ángulo = Circunferencia.AjusteCiclico(ángulo);
                    ángulo /= 2 * π;
                    for (RectaColores rectaColor : colores) {
                        if (rectaColor.Rango.Por_Dentro(ángulo, true)) {
                            final int[] Color = rectaColor.CalcularColor(ángulo);
                            final int T = (Fila * w + Columna) * RGBA;
                            for (int Ch = R; Ch <= A; Ch++) {
                                int P = Ch + T;
                                RGBA_Salida[P] = Color[Ch];
                            }
                            break;
                        }
                    }
                }
            }
            RASTER.setPixels(0, 0, w, h, RGBA_Salida);
            return RASTER;
        }
    }

    class RectaColores {

        Rango Rango;
        int[] Colores = new int[2 * RGBA];

        public RectaColores(float ValorInicio, float ValorFin, Color ColorCola, Color ColorCabeza) {
            Rango = new Rango(ValorInicio, ValorFin);
            for (int i = 0; i < Colores.length / RGBA; i++) {
                for (int j = R; j <= A; j++) {
                    int bs = 8 * ((j + 1) % RGBA);
                    Colores[j + i * RGBA] = (i == 0 ? ColorCola : ColorCabeza).getRGB() << bs >>> 24;
                }
            }
        }

        int[] CalcularColor(double v) {
            double t = Rango.Vp(v);
            int Retorno[] = new int[RGBA];
            for (int Ch = R; Ch <= A; Ch++) {
                double Pi = Colores[Ch];
                double Pf = Colores[Ch + RGBA];
                Retorno[Ch] = (int) ((Pf - Pi) * t + Pi);
            }
            return Retorno;
        }
    }
}
