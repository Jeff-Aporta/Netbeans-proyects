package Principal;

import HerramientaArchivos.LectoEscrituraArchivos;
import static HerramientaDeImagen.Filtros_Lineales.EscalarGranCalidad;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientasMatemáticas.Dupla;
import JID3.Etiquetas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class CompositorDeImagenes {

    public static void main(String[] args) throws Exception {
        File ArchivoMp3 = new File("C:\\Users\\Ángel\\Documents\\Youtube Audio Library\\Country y Folk\\12_Mornings.mp3");
        BufferedImage f = Youtube(new Etiquetas(ArchivoMp3, true));
        JLabel presentador = new JLabel();
        presentador.setIcon(new ImageIcon(f));
        new JFrame() {
            {
                add(presentador);
                setSize(f.getWidth(), f.getHeight());
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }
        };
    }

    public static BufferedImage Youtube(Etiquetas etiquetas) {
        BufferedImage retorno = LectoEscrituraArchivos.cargar_imagen("Fondo.jpg");
        float tono = (float) Math.random();
        Monocromatizar(retorno, tono, LectoEscrituraArchivos.cargar_imagen("mascara.jpg"));
        String str = etiquetas.TÍTULO;
        str = str.replace("[", "(");
        str = str.replace("]", ")");
        str = str.replace("{", "(");
        str = str.replace("}", ")");
        str = str.replace("(", " - ");
        str = str.replace(")", "");
        String título[] = str.split(" - ");
        for (int i = 0; i < título.length; i++) {
            título[i] = título[i].trim();
        }

        char[] títuloPrincipal = título[0].toCharArray();
        int sp = 0;
        for (int i = 0; i < títuloPrincipal.length; i++) {
            if (títuloPrincipal[i] == ' ') {
                sp++;
                if (sp == 4) {
                    sp = 0;
                    títuloPrincipal[i] = '\n';
                }
            }
        }
        título[0] = new String(títuloPrincipal);

        int size = 150;
        GeneradorDeTexto fuente = new GeneradorDeTexto()
                .ModificarTamañoFuente(size)
                .AlineaciónHorizontal(Dupla.DERECHA)
                .DistanciaEntreRenglones(0)
                .ColorFuente(Color.WHITE)
                .Borde(Color.getHSBColor(tono, 1, .5f), 10);
        BufferedImage[] imagenesTítulo = new BufferedImage[título.length];
        int w = 0, h = 0;
        for (int i = 0; i < título.length; i++) {
            imagenesTítulo[i] = fuente.GenerarTexto(título[i]);
            h += imagenesTítulo[i].getHeight();
            w = Math.max(imagenesTítulo[i].getWidth(), w);
            if (i == 0) {
                fuente.ModificarTamañoFuente(size / 3);
                fuente.GrosorBorde(fuente.GrosorBorde / 2);
            }
        }
        BufferedImage imagenTítulo = new BufferedImage(w, h, 2);
        h = 0;
        for (BufferedImage img : imagenesTítulo) {
            imagenTítulo.getGraphics().drawImage(img, imagenTítulo.getWidth() - img.getWidth(), h, null);
            h += img.getHeight();
        }
        int anchoMáx = 850;
        int altoMáx = 250;
        if (imagenTítulo.getWidth() > anchoMáx) {
            imagenTítulo = EscalarGranCalidad(imagenTítulo, anchoMáx, -1);
        }
        if (imagenTítulo.getHeight() > altoMáx) {
            imagenTítulo = EscalarGranCalidad(imagenTítulo, -1, altoMáx);
        }
        Graphics2D g = retorno.createGraphics();
        Dupla posTítulo = Dupla.Alinear(retorno, imagenTítulo, Dupla.DERECHA, Dupla.MEDIO);
        g.drawImage(imagenTítulo, posTítulo.intX(), posTítulo.intY(), null);
        BufferedImage imgDescripción = fuente.GenerarTexto(etiquetas.ARTISTA + "\n" + etiquetas.COMENTARIO);
        altoMáx = 235;
        if (imgDescripción.getWidth() > anchoMáx) {
            imgDescripción = EscalarGranCalidad(imgDescripción, anchoMáx, -1);
        }
        if (imgDescripción.getHeight() > altoMáx) {
            imgDescripción = EscalarGranCalidad(imgDescripción, -1, altoMáx);
        }
        Dupla pos2 = Dupla.Alinear(retorno, imgDescripción, Dupla.DERECHA, Dupla.ABAJO);
        g.drawImage(imgDescripción, pos2.intX(), pos2.intY(), null);
        return retorno;
    }

    public static void Monocromatizar(BufferedImage imagen, float h, BufferedImage mascara) {
        for (int col = 0; col < imagen.getWidth(); col++) {
            for (int fil = 0; fil < imagen.getHeight(); fil++) {
                Color cm = new Color(mascara.getRGB(col, fil), true);
                float HSBm[] = Color.RGBtoHSB(cm.getRed(), cm.getGreen(), cm.getBlue(), null);
                if (HSBm[2] < 20f / 100) {
                    Color c = new Color(imagen.getRGB(col, fil), true);
                    float HSB[] = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                    Color nc = Color.getHSBColor(h, HSB[1], HSB[2]);
                    imagen.setRGB(col, fil, nc.getRGB());
                }
            }
        }
    }
}
