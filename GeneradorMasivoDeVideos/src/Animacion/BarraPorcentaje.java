package Animacion;

import HerramientasMatemáticas.Dupla;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class BarraPorcentaje extends ObjetoDibujable {

    public Color[] coloresFondo = {Color.WHITE, Color.LIGHT_GRAY};
    public Color[] coloresRecorrido = {Color.RED, new Color(150, 0, 0)};

    public float porcentaje = 0;
    public int grosor = 6;
    public Dupla puntoInicial = Escenario.Mitad().ReemplazarX(0);
    public Dupla puntoFinal = Escenario.Mitad().ReemplazarX(Escenario.X);

    public static void main(String[] args) throws InterruptedException {
        BarraPorcentaje b = new BarraPorcentaje();
        JLabel presentador = new JLabel();
        new JFrame() {
            {
                add(presentador);
                setSize(1280, 720);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }
        };
        for (float i = 0; i < 1; i += 0.01) {
            b.porcentaje = i;
            presentador.setIcon(new ImageIcon(b.getSkin()));
            Thread.sleep(100);
        }
    }

    @Override
    public BufferedImage getSkin() {
        int ancho = (int) puntoInicial.distanciaAlPunto(puntoFinal);
        BufferedImage barra = new BufferedImage(ancho, grosor, 2);
        {
            Graphics2D g = barra.createGraphics();
            g.setPaint(new GradientPaint(0, 0, coloresFondo[0], 0, barra.getHeight(), coloresFondo[1]));
            g.fillRect(0, 0, ancho, barra.getHeight());
            g.setPaint(new GradientPaint(0, 0, coloresRecorrido[0], 0, barra.getHeight(), coloresRecorrido[1]));
            g.fillRect(0, 0, (int) (ancho * porcentaje), barra.getHeight());
        }
        BufferedImage fotograma = Escenario.convBufferedImage();
        {
            Graphics2D g = fotograma.createGraphics();
            g.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );
            AffineTransform at = new AffineTransform();
            at.translate(puntoInicial.intX(), puntoInicial.intY());
            at.rotate(puntoFinal.Ángulo(puntoInicial));
            g.drawImage(barra, at, null);
        }
        return fotograma;
    }
}
