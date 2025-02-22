package _Laboratorio;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class NoiseTV {

    final static double FPS = 30;
    final static double MS_SEGUNDO = 1000;
    final static double Pausa = MS_SEGUNDO / FPS;
    final static int CantidadMuestras = 10;
    final static int ancho = 1280;
    final static int alto = 720;
    final static BufferedImage[] FotogramasNoiseTV = new BufferedImage[CantidadMuestras];

    public static void main(String[] args) {
        JLabel presentador = new JLabel();
        JFrame ventana = new JFrame() {
            {
                setSize(ancho, alto);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                add(presentador);
                setVisible(true);
            }
        };
        for (int i = 0; i < CantidadMuestras; i++) {
            FotogramasNoiseTV[i] = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
            RellenarCon_Textura_TVNoiseStatic(FotogramasNoiseTV[i], 1);
        }
        int Muestra = 0;
        while (true) {//Ciclo que dà la sensaciòn de vìdeo
            presentador.setIcon(new ImageIcon(
                    Escalar(
                            FotogramasNoiseTV[Muestra = ++Muestra % CantidadMuestras],
                            ventana.getWidth(), ventana.getHeight()
                    )
            )
            );
            try {
                Thread.sleep((long) Pausa);
            } catch (Exception ex) {
            }
        }
    }

    public static BufferedImage Escalar(BufferedImage imagen, int AnchoNuevo, int AltoNuevo) {
        double pX = (double) AnchoNuevo / imagen.getWidth();
        double pY = (double) AltoNuevo / imagen.getHeight();
        return new AffineTransformOp(
                AffineTransform.getScaleInstance(pX, pY), AffineTransformOp.TYPE_BILINEAR
        ).filter(imagen, new BufferedImage((int) (imagen.getWidth() * pX), (int) (imagen.getHeight() * pY), 2));
    }

    public static void RellenarCon_Textura_TVNoiseStatic(BufferedImage imagen, int lado_celda) {
        if (lado_celda <= 0) {
            throw new RuntimeException("El lado debe ser como mínimo de 1 px");
        }
        Graphics2D g = imagen.createGraphics();
        g.fillRect(0, 0, imagen.getWidth(), imagen.getHeight());
        for (int col = 0; col < imagen.getWidth() / lado_celda; col++) {
            for (int fil = 0; fil < imagen.getHeight() / lado_celda; fil++) {
                int valor = (int) (255 * Math.random());
                g.setColor(new Color(valor, valor, valor));
                g.fillRect(col * lado_celda, fil * lado_celda, lado_celda, lado_celda);
            }
        }
    }
}
