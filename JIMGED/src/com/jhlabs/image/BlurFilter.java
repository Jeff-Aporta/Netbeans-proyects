package com.jhlabs.image;

import static com.jhlabs.image.PlasmaFilter.randomize;
import java.awt.image.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;

public class BlurFilter extends ConvolveFilter {
    
    public static void main(String[] args) throws Exception {
        JLabel presentador = new JLabel();
        BlurFilter p = new BlurFilter();
        BufferedImage carga = ImageIO.read(new URL(
                "https://img1.imagenesgratis.com/ig/tigres/tigres_039.jpg"
        ));
        BufferedImage imagen = p.filter(carga ,null);
        presentador.setIcon(new ImageIcon(imagen));
        new JFrame() {
            {
                add(presentador);
                setSize(imagen.getWidth(), imagen.getHeight());
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }
        };
    }

    protected static float[] blurMatrix = {
        1 / 14f, 2 / 14f, 1 / 14f,
        2 / 14f, 2 / 14f, 2 / 14f,
        1 / 14f, 2 / 14f, 1 / 14f
    };

    public BlurFilter() {
        super(blurMatrix);
    }

    public String toString() {
        return "Blur/Simple Blur";
    }
}
