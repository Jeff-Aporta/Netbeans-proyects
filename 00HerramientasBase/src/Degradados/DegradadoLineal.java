package Degradados;

import HerramientasGUI.VentanaGráfica;
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

public class DegradadoLineal implements Paint {

    public Point puntoPartida;
    public Point puntoLlegada;
    public MapaDegradado mapaDegradado;
    public AjusteRango ajusteRango;
    private double Ux, Uy;

    public DegradadoLineal(Point puntoLlegada, MapaDegradado mapaDegradado) {
        this(new Point(), puntoLlegada, mapaDegradado);
    }

    public DegradadoLineal(Point puntoLlegada, MapaDegradado mapaDegradado, AjusteRango ajusteRango) {
        this(new Point(), puntoLlegada, mapaDegradado, ajusteRango);
    }

    public DegradadoLineal(Point puntoPartida, Point puntoLlegada, MapaDegradado mapaDegradado) {
        this(puntoPartida, puntoLlegada, mapaDegradado, AjusteRango.LINEAL);
    }

    public DegradadoLineal(Point puntoPartida, Point puntoLlegada, MapaDegradado mapaDegradado, AjusteRango ajusteRango) {
        this.puntoPartida = puntoPartida;
        this.puntoLlegada = puntoLlegada;
        this.mapaDegradado = mapaDegradado;
        this.ajusteRango = ajusteRango;
    }

    public static void main(String[] args) {
        int ancho = 1280;
        int alto = 720;
        MapaDegradado mapaDegradado = new MapaDegradado(
                Color.BLACK, Color.ORANGE, Color.PINK, Color.RED
        );
        Point p1 = new Point(200, 100);
        Point p2 = new Point(600, 400);
        DegradadoLineal degradadoLineal = new DegradadoLineal(
                p1, p2, mapaDegradado, AjusteRango.LINEAL
        );
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
        new VentanaGráfica(fotograma);
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
        public Raster getRaster(final int x, int y, int ancho, int alto) {
            Ux = puntoLlegada.x - puntoPartida.x;
            Uy = puntoLlegada.y - puntoPartida.y;
            final WritableRaster Raster = getColorModel().createCompatibleWritableRaster(ancho, alto);
            int cantidadPixeles = ancho * alto * 4;
            float[] PixelesSalida = new float[cantidadPixeles];
            int PosComponentePixel = 0;
            for (int fila = 0; fila < alto; fila++) {
                double PosyPixel = y + fila;
                for (int columna = 0; columna < ancho; columna++) {
                    double PosxPixel = x + columna;
                    double porcentaje = PorcentajeProyecciónOrtogonal(PosxPixel, PosyPixel);
                    float[] componentes = {
                        1, 1, 0, 1
                    };
                    for (float f : componentes) {
                        PixelesSalida[PosComponentePixel++] = f * 255;
                    }
                }
            }
            Raster.setPixels(0, 0, ancho, alto, PixelesSalida);
            return Raster;
        }
    }

    public double PorcentajeProyecciónOrtogonal(double x, double y) {
        double Vx = x - puntoPartida.x;
        double Vy = y - puntoPartida.y;
        return (Ux * Vx + Uy * Vy) / (Ux * Ux + Uy * Uy);
    }
}
