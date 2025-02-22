package HerramientasGUI;

import HerramientasMatemáticas.Dupla;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class CapaEco {

    BufferedImage Imagen;
    public Dimension Dimensión;
    float Difusión = .13f;

    public static void main(String[] args) throws Exception {
        VentanaGráfica ventana = new VentanaGráfica().ColorFondo(Color.WHITE);
        Dupla pos = new Dupla();
        Dupla a = new Dupla(5, 5);
        int w = 1280;
        int h = 720;
        int r = 30;
        CapaEco capaEco = new CapaEco(w, h);
        while (true) {
            pos.Adicionar(a);
            if (pos.XesMayor(w - r) || pos.XesMenor(0)) {
                a.CambiarSentidoX();
            }
            if (pos.YesMayor(h - r) || pos.YesMenor(0)) {
                a.CambiarSentidoY();
            }
            BufferedImage imagen = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB) {
                {
                    Graphics2D g = createGraphics();
                    g.setColor(Color.BLACK);
                    g.fillOval(pos.intX(), pos.intY(), r, r);
                }
            };
            ventana.ActualizarFotograma(capaEco.Imagen(imagen));
            Thread.sleep(1000 / 60);
        }
    }

    public CapaEco(int w, int h) {
        this(new Dimension(w, h));
    }

    public CapaEco(Dimension dimensión) {
        this.Dimensión = dimensión;
    }

    public BufferedImage Imagen() {
        return Imagen(null);
    }

    public BufferedImage Imagen(BufferedImage fotograma) {
        if (fotograma == null) {
            return Imagen;
        }
        BufferedImage eco = new BufferedImage(
                Dimensión.width, Dimensión.height, BufferedImage.TYPE_INT_ARGB
        ) {
            {
                {
                    Graphics2D g = createGraphics();
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - Difusión));
                    g.drawImage(Imagen, 0, 0, null);
                }
                {
                    Graphics2D g = createGraphics();
                    g.drawImage(fotograma, 0, 0, null);
                }
            }
        };
        Imagen = eco;
        return Imagen;
    }

}
