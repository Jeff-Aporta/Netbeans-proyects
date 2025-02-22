package Principal;

import HerramientaArchivos.BibliotecaArchivos.Imagenes.*;
import HerramientaArchivos.CarpetaDeRecursos;
import HerramientaDeImagen.Filtros_Lineales;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Centrado_Ajustar;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientasGUI.VentanaGráfica;
import HerramientasMatemáticas.Dupla;
import HerramientasMatemáticas.Matemática;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Creditos {

    public static final int w = 1296, h = 729;
    public static final int DuraciónCreditos = 10;

    final static String Credito1 = "Video made with software developed by Jeff Aporta in Java\n(Jeffrey Alexander Agudelo Espitia)"
            + "\nPereira (Risaralda) - Colombia";
    final static String Credito2 = "http://www.youtube.com/c/JeffAporta";

    public static void main(String[] args) {
        BufferedImage[] Creditos = GenerarCreditos();
        int i = 0;
        int w = 1280 / 2;
        int h = 720 / 2;
        int d = 30;
        Dupla pos = Dupla.Alinear(
                Dupla.DIMENSIÓN_PANTALLA,
                new Dupla(w * 2 + d, h),
                Dupla.MEDIO,
                Dupla.MEDIO
        );
        for (BufferedImage img : Creditos) {
            JLabel presentador = new JLabel();
            presentador.setIcon(new ImageIcon(Filtros_Lineales.Escalar(img, w, h, true)));
            VentanaGráfica ventana = new VentanaGráfica();
            ventana.ActualizarFotograma(img);
            ventana.CambiarModoFondo(VentanaGráfica.DIMENSIÓN_MEDIANA);
            ventana.Alternar_SiempreVisible();
            ventana.setLocation(pos.intX() + (i++) * (w + d), pos.intY());
        }
    }

    public static BufferedImage[] GenerarCreditos() {
        return GenerarCreditos(Color.getHSBColor((float) Math.random(), 1, .7f));
    }

    public static BufferedImage[] GenerarCreditos(Color colorBordeLetra) {
        BufferedImage fotograma1 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        BufferedImage fotograma2 = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        GeneradorDeTexto generaciónDeTexto = new GeneradorDeTexto()
                .ColorFuente(Color.WHITE)
                .ModificarTamañoFuente(70)
                .Borde(colorBordeLetra, 7)
                .AlineaciónHorizontal(Dupla.MEDIO);
        CarpetaDeRecursos creditos = new CarpetaDeRecursos(
                CarpetaDeRecursos.TIPO_APPDATA,
                "Creditos para el fin de los videos"
        );
        try {
            BufferedImage cred1 = generaciónDeTexto.GenerarTexto(
                    Credito1
            );
            BufferedImage cred2 = generaciónDeTexto.GenerarTexto(
                    Credito2
            );
            BufferedImage LogoJava = Filtros_Lineales.Escalar(Logos.Java_MarcoYSombra(), .5);
            int d = 40;
            BufferedImage cred = new BufferedImage(
                    Matemática.Máx(cred1.getWidth(), cred2.getWidth(), LogoJava.getWidth()),
                    cred1.getHeight() + cred2.getHeight() + LogoJava.getHeight() + 2 * d, 2
            );
            BufferedImage[] contenido = {cred1, LogoJava, cred2};
            Graphics2D g = cred.createGraphics();
            int h = 0;
            for (BufferedImage img : contenido) {
                Dupla pos = Dupla.Alinear(cred, img, Dupla.MEDIO, Dupla.POR_DEFECTO);
                g.drawImage(img, pos.intX(), h, null);
                h += img.getHeight() + d;
            }
            Ajuste_Centrado_Ajustar(Filtros_Lineales.Escalar(cred, .9), fotograma1);
        } catch (Exception e) {
        }
        try {
            Ajuste_Centrado_Ajustar(
                    Logos.JeffAporta_Texto(),
                    fotograma2
            );
        } catch (Exception e) {
        }
        return new BufferedImage[]{fotograma1, fotograma2};
    }

}
