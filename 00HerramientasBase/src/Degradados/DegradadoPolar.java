package Degradados;

import HerramientasMatemáticas.Dupla;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;

public class DegradadoPolar implements Paint {

    public Dupla Centro;
    public double Escala;
    public double ángulo;

    public MapaDegradado mapaDegradado;
    public AjusteRango ajusteRango;

    Object funciónGeneradora;

    public static void main(String[] args) {
        int ancho = 1280;
        int alto = 720;
        MapaDegradado mapaDegradado = new MapaDegradado(
                new Color(100, 200, 240),
                new Color(20, 120, 255),
                new Color(60, 0, 90),
                new Color(60, 0, 0)
        );
        Dupla p1 = new Dupla(500, 400);
        Dupla p2 = new Dupla(530, 430);
        double n = 3;
        double d = Double.parseDouble(String.format("%.4f", Math.cos(Math.PI / n)).replace(",", "."));
        DegradadoPolar degradadoPolar = new DegradadoPolar(
                (FunciónPolar) (double θ) -> {
                    return d / Math.cos((2 / n) * Math.asin(Math.sin((n * θ + 3 * Math.PI) / 2)));
                }, p1, p2, mapaDegradado, AjusteRango.CÍCLICO
        );
        JLabel presentador = new JLabel();
        new JFrame() {
            {
                add(presentador);
                setSize(ancho, alto);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }
        };
        BufferedImage fotograma = new BufferedImage(ancho, alto, 2);
        {
            Graphics2D g = fotograma.createGraphics();
            g.setPaint(degradadoPolar);
            g.fillRect(0, 0, ancho, alto);
            {
                g.setColor(Color.WHITE);
                g.drawLine(p1.intX(), p1.intY(), p2.intX(), p2.intY());
            }
        }
        presentador.setIcon(new ImageIcon(fotograma));
    }

    public DegradadoPolar(Object funciónPolar, Dupla puntoLlegada, MapaDegradado mapaDegradado) {
        this(funciónPolar, new Dupla(), puntoLlegada, mapaDegradado);
    }

    public DegradadoPolar(Object funciónPolar, Dupla puntoLlegada, MapaDegradado mapaDegradado, AjusteRango ajusteRango) {
        this(funciónPolar, new Dupla(), puntoLlegada, mapaDegradado, ajusteRango);
    }

    public DegradadoPolar(Object funciónPolar, Dupla puntoPartida, Dupla puntoLlegada, MapaDegradado mapaDegradado) {
        this(funciónPolar, puntoPartida, puntoLlegada, mapaDegradado, AjusteRango.LINEAL);
    }

    public DegradadoPolar(Object funciónPolar, Dupla puntoPartida, Dupla puntoLlegada, MapaDegradado mapaDegradado, AjusteRango ajusteRango) {
        this.Centro = puntoPartida;
        double y = puntoLlegada.Y - puntoPartida.Y;
        double x = puntoLlegada.X - puntoPartida.X;
        ángulo = Math.atan2(y, x);
        Escala = Math.sqrt(y * y + x * x);
        funciónGeneradora = funciónPolar;
        this.mapaDegradado = mapaDegradado;
        this.ajusteRango = ajusteRango;
    }

    @Override
    public PaintContext createContext(ColorModel cm, Rectangle rctngl, Rectangle2D rd, AffineTransform at, RenderingHints rh) {
        return new DegradadoLinealPaintContext();
    }

    @Override
    public int getTransparency() {
        return Transparency.TRANSLUCENT;
    }

    private class DegradadoLinealPaintContext implements PaintContext {

        @Override
        public void dispose() {
        }

        @Override
        public ColorModel getColorModel() {
            return ColorModel.getRGBdefault();
        }

        @Override
        public Raster getRaster(int x, int y, int ancho, int alto) {
            final WritableRaster Raster = getColorModel().createCompatibleWritableRaster(ancho, alto);
            int cantidadPixeles = ancho * alto * 4;
            float[] PixelesSalida = new float[cantidadPixeles];
            int PosComponentePixel = 0;
            for (int fila = 0; fila < alto; fila++) {
                double PosyPixel = y + fila - Centro.Y;
                for (int columna = 0; columna < ancho; columna++) {
                    double PosxPixel = x + columna - Centro.X;
                    double θ = Math.atan2(PosyPixel, PosxPixel);
                    θ -= ángulo;
                    if (θ < 0) {
                        θ += 2 * Math.PI;
                    }
                    θ %= (2 * Math.PI);
                    double radioFunción;
                    if (funciónGeneradora instanceof Number) {
                        radioFunción = ((Number) funciónGeneradora).doubleValue();
                    } else if (funciónGeneradora instanceof FunciónPolar) {
                        radioFunción = ((FunciónPolar) funciónGeneradora).R(θ);
                    } else {
                        throw new RuntimeException("El calculo del radio no se pudo llevar a cabo");
                    }
                    radioFunción *= Escala;
                    double radioPunto = Math.sqrt(PosxPixel * PosxPixel + PosyPixel * PosyPixel);
                    double porcentaje = radioPunto / radioFunción;
                    float[] componentes = mapaDegradado.CalcularComponentesColor(
                            porcentaje, ajusteRango
                    );
                    for (float f : componentes) {
                        PixelesSalida[PosComponentePixel++] = f * 255;
                    }
                }
            }
            Raster.setPixels(0, 0, ancho, alto, PixelesSalida);
            return Raster;
        }
    }

}
