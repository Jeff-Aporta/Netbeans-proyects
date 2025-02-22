
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientaDeImagen.Filtros_Lineales;
import HerramientasGUI.VentanaGráfica;
import HerramientasMatemáticas.Dupla;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class redimensión_masiva_imagenes {

    public static void main(String[] args) {
        String ruta = "C:\\Users\\USUARIO\\Downloads\\Mile";
        String[] extensionsSupport = {".png", ".jpeg", ".jpg"};
        File[] imgs = new File(ruta).listFiles((file, name) -> {
            for (String extension : extensionsSupport) {
                if (name.toLowerCase().endsWith(extension)) {
                    return true;
                }
            }
            return false;
        });
        VentanaGráfica vg = new VentanaGráfica();
        long heapSize = Runtime.getRuntime().totalMemory();
        for (File img : imgs) {
            long sz = (img.length() / 1024 / 1024);
            System.out.println(sz);
            if (sz > (heapSize / 1024 / 1024) - 30) {
                img.delete();
                continue;
            }
            try {
                File newf = new File(ruta + "\\moderate-size\\" + img.getName());
                if (!new File(ruta + "\\moderate-size").exists()) {
                    new File(ruta + "\\moderate-size").mkdirs();
                }
                if (newf.exists()) {
                    img.delete();
                    continue;
                }
//                dimensiónMáxima(2500, img, newf, vg);
                TamañosFijosConFiltroLineal(512, 512, img, newf, vg);
            } catch (Exception e) {
                System.exit(0);
            }
        }
        System.exit(0);
    }

    public static void dimensiónMáxima(int dimmax, File img, File newf, VentanaGráfica vg) {
        BufferedImage Imagen = LectoEscrituraArchivos.cargar_imagen(img);
        if (Imagen.getWidth() > Imagen.getHeight()) {
            Imagen = Filtros_Lineales.EscalarGranCalidad(Imagen, dimmax, -1);
        } else {
            Imagen = Filtros_Lineales.EscalarGranCalidad(Imagen, -1, dimmax);
        }
        vg.ActualizarFotograma(Imagen);
        LectoEscrituraArchivos.exportar_imagen(Imagen, newf);
        img.delete();
    }

    public static void TamañosFijosConFiltroLineal(int w, int h, File img, File newf, VentanaGráfica vg) {
        BufferedImage Imagen = LectoEscrituraArchivos.cargar_imagen(img);
        BufferedImage area = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Filtros_Lineales.Ajuste_Rellenar(Imagen, area);
        vg.ActualizarFotograma(area);
        LectoEscrituraArchivos.exportar_imagen(area, newf);
        img.delete();
    }
}
