package Escenarios;

import static HerramientaDeImagen.LectoEscritor_Imagenes.*;
import HerramientasMatemáticas.Dupla;
import static Principal.Principal.*;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Presentación {

    static boolean EnEjecición;

    public void presentarFirmas() {
        EnEjecición = true;
        presentar(cargarImagen("Logo Jeff Aporta.png"));
        presentar(cargarImagen("Logo.png"));
        CambiarEscenario(MENÚ);
    }

    public static void Detener() {
        EnEjecición = false;
    }

    private static void presentar(BufferedImage imagen) {
        if (!EnEjecición) {
            return;
        }
        try {
            ventana.ImagenFondo = null;
            ventana.ColorFondo = Color.BLACK;
            
            float FotogramasDesvanesimiento = 15; 
            float Δdesvanesimiento = 1 / FotogramasDesvanesimiento;
            float Transparencia = 0;
            for (int i = 0; i < FotogramasDesvanesimiento; i++) {
                if (!EnEjecición) {
                    return;
                }
                BufferedImage fotograma = Dupla.convBufferedImage(imagen);
                Graphics2D g = fotograma.createGraphics();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Transparencia += Δdesvanesimiento));
                g.drawImage(imagen, 0, 0, null);
                ventana.ActualizarFotograma(fotograma);
                Thread.sleep((long) (1000 / (FotogramasDesvanesimiento * 2)));
            }
            if (!EnEjecición) {
                return;
            }
            Thread.sleep(1500);
            if (!EnEjecición) {
                return;
            }
            for (int i = 0; i < FotogramasDesvanesimiento; i++) {
                if (!EnEjecición) {
                    return;
                }
                BufferedImage fotograma = Dupla.convBufferedImage(imagen);
                Graphics2D g = fotograma.createGraphics();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Transparencia -= Δdesvanesimiento));
                g.drawImage(imagen, 0, 0, null);
                ventana.ActualizarFotograma(fotograma);
                Thread.sleep((long) (1000 / (FotogramasDesvanesimiento * 2)));
            }
            if (!EnEjecición) {
                return;
            }
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
    }

}
