package Animacion;

import HerramientasMatemáticas.Dupla;
import HerramientasMatemáticas.Dupla.Curva;

import static HerramientasMatemáticas.Matemática.*;
import static java.lang.Math.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class EspectroSonido extends ObjetoDibujable {

    public float[] frecuencias;
    public Color color = new Color(200, 200, 255, 150);
    public Color colorBorde = new Color(255, 255, 255, 255);

    public float anchorBanda = (float) (toRadians(3));
    public float radioInterno = 160;
    public float radio = 220;
    public float radiofrontera = 350;
    public float rotación = 0;

    @Override
    public BufferedImage getSkin() {
        if (frecuencias == null) {
            return null;
        }
        rotación+=toRadians(.4);
        BufferedImage fotograma = Escenario.convBufferedImage();
        Graphics2D g = fotograma.createGraphics();
        for (int i = 0; i < frecuencias.length; i++) {
            double theta = i * 2 * π / frecuencias.length;
            float ri = (radioInterno - radio) * abs(frecuencias[i]) + radio;
            Curva C_base = Curva.Circulo(posición, ri, rotación);
            float r = (radiofrontera - radio) * abs(frecuencias[i]) + radio;
            Curva C_frontera = Curva.Circulo(posición, r, rotación);
            Dupla p1 = C_base.XY(theta);
            Dupla p2 = C_base.XY(theta + anchorBanda);
            Dupla p3 = C_frontera.XY(theta + anchorBanda);
            Dupla p4 = C_frontera.XY(theta);
            int[] X = {p1.intX(), p2.intX(), p3.intX(), p4.intX()};
            int[] Y = {p1.intY(), p2.intY(), p3.intY(), p4.intY()};
            g.setColor(color);
            g.fillPolygon(X, Y, 4);
            g.setColor(colorBorde);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
            g.drawPolygon(X, Y, 4);
        }
        return fotograma;
    }
}
