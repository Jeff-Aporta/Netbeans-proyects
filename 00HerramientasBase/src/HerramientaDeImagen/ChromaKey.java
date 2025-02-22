package HerramientaDeImagen;

import HerramientaArchivos.LectoEscrituraArchivos;
import static HerramientaArchivos.ListarDocumentos.CargarArchivos;
import HerramientasMatemáticas.Dupla;
import HerramientasMatemáticas.Matemática;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class ChromaKey {

    public static void main(String[] args) {
        ArrayList<File> imagenes = new ArrayList<>();
        String dir = "C:\\Users\\guillermo\\Documents\\MEGAsync Downloads\\Material\\Mega Pack De Transiciones By Angel Roid Y Alexius Tv\\Estilo # (1)\\Fotogramas Estilo 1 - 0";
        imagenes = CargarArchivos(new File(dir), new String[]{"png"});
        for (File imagen : imagenes) {
            System.out.println(imagen.getPath());
        }
//        for (File imgFile : imagenes) {
//            BufferedImage img = LectoEscritor_Imagenes.cargarImagen(imgFile);
//            Color key = new Color(4, 11, 245);
//            LectoEscritor_Imagenes.ExportarImagen(
//                    AplicarChromaKey(img, key, new Color(key.getRGB() & 0xffffff, true), 255),
//                    imgFile
//            );
//        }
        BufferedImage ej = LectoEscrituraArchivos.cargar_imagen(imagenes.get(0));
        BufferedImage Sprite = new BufferedImage(ej.getWidth() * imagenes.size(), ej.getHeight(), 2);
        for (int i = 0; i < imagenes.size(); i++) {
            BufferedImage img = LectoEscrituraArchivos.cargar_imagen(imagenes.get(i));
            Sprite.createGraphics().drawImage(img, i * img.getWidth(), 0, null);
        }
//        Sprite = Filtros_Lineales.EscalarGranCalidad(Sprite, (int) (Sprite.getWidth()*Matemática.raiz2(1/2f)), -1);
        LectoEscrituraArchivos.exportar_imagen(Sprite, dir + "\\Sprite.png");
    }

    public static BufferedImage AplicarChromaKey(BufferedImage img, Color colorClave, int radioProximidad) {
        return AplicarChromaKey(img, colorClave, new Color(0, true), radioProximidad);
    }

    public static BufferedImage AplicarChromaKey(BufferedImage img, Color colorClave, Color reemplazar, int radioProximidad) {
        if (radioProximidad < 1) {
            throw new RuntimeException("El radio de proximidad no puede ser menor a 1");
        }
        BufferedImage retorno = Dupla.convBufferedImage(img);
        for (int col = 0; col < img.getWidth(); col++) {
            for (int fil = 0; fil < img.getHeight(); fil++) {
                Color píxel = new Color(img.getRGB(col, fil), true);
                double d = Matemática.Norma(new double[]{
                    píxel.getRed() - colorClave.getRed(),
                    píxel.getGreen() - colorClave.getGreen(),
                    píxel.getBlue() - colorClave.getBlue()
                });
                double p;
                p = d / radioProximidad;
                if (p > 1) {
                    p = 1;
                }
                int r = (int) ((píxel.getRed() - reemplazar.getRed()) * p + reemplazar.getRed());
                int g = (int) ((píxel.getGreen() - reemplazar.getGreen()) * p + reemplazar.getGreen());
                int b = (int) ((píxel.getBlue() - reemplazar.getBlue()) * p + reemplazar.getBlue());
                int a = (int) ((píxel.getAlpha() - reemplazar.getAlpha()) * p + reemplazar.getAlpha());
                retorno.setRGB(col, fil, new Color(r, g, b, a).getRGB());
            }
        }
        return retorno;
    }
}
