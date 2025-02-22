package Degradados;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Point;
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

public class DegradadoCónico implements Paint {

    public Point Centro;
    public double ángulo;
    public MapaDegradado mapaDegradado;
    public AjusteRango ajusteRango;

    public static void main(String[] args) {
        int ancho = 1280;
        int alto = 720;
        MapaDegradado mapaDegradado = new MapaDegradado(
                new Color(100, 200, 240),
                new Color(20, 120, 255),
                new Color(60, 0, 90),
                new Color(60, 0, 0)
        );
        Point p1 = new Point(300, 400);
        Point p2 = new Point(400, 400);
        DegradadoCónico degradadoLineal = new DegradadoCónico(
                p1, p2, mapaDegradado, AjusteRango.LINEAL_ABS
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
            g.setPaint(degradadoLineal);
            g.fillRect(0, 0, ancho, alto);
            {
                g.setColor(Color.WHITE);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
        presentador.setIcon(new ImageIcon(fotograma));
    }

    public DegradadoCónico(Point puntoLlegada, MapaDegradado mapaDegradado) {
        this(new Point(), puntoLlegada, mapaDegradado);
    }

    public DegradadoCónico(Point puntoLlegada, MapaDegradado mapaDegradado, AjusteRango ajusteRango) {
        this(new Point(), puntoLlegada, mapaDegradado, ajusteRango);
    }

    public DegradadoCónico(Point puntoPartida, Point puntoLlegada, MapaDegradado mapaDegradado) {
        this(puntoPartida, puntoLlegada, mapaDegradado, AjusteRango.LINEAL);
    }

    public DegradadoCónico(Point puntoPartida, Point puntoLlegada, MapaDegradado mapaDegradado, AjusteRango ajusteRango) {
        this.Centro = puntoPartida;
        ángulo = Math.atan2(puntoLlegada.y - puntoPartida.y, puntoLlegada.x - puntoPartida.x);
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
                double PosyPixel = y + fila - Centro.y;
                for (int columna = 0; columna < ancho; columna++) {
                    double PosxPixel = x + columna - Centro.x;
                    double theta = Math.atan2(-PosyPixel, PosxPixel);
                    theta -= ángulo;
                    if (theta < 0) {
                        theta += 2 * Math.PI;
                    }
                    theta %= (2 * Math.PI);
                    double porcentaje = theta / (2 * Math.PI);
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
