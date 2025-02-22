package GeneradorDeEspectroSimple082019;

import HerramientasGUI.VentanaGráfica;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;

public class CargaAleatoriaDeImagen {

    public static void main(String[] args) throws Exception {
        VentanaGráfica ventana = new VentanaGráfica();
        BufferedImage imagen = null;
        while (true) {
            while (imagen == null) {
                System.err.println("cargando imagen");
                try {
                    switch (new Random().nextInt(2)) {
                        case 0:
                            imagen = ImageIO.read(new URL("http://placeimg.com/1280/720"));
                            System.err.println("imagen generada con place img");
                            break;
                        case 1:
                            imagen = ImageIO.read(new URL("https://picsum.photos/1280/720?random"));
                            System.err.println("imagen generada con picsum");
                            break;
                        case 2:
                            imagen = ImageIO.read(new URL("http://lorempixel.com/1280/720/"));
                            System.err.println("imagen generada con lorem pixel");
                            break;
                    }
                } catch (Exception ex) {
                }
                if (imagen != null) {
                    break;
                }
                System.err.println("reintentar la carga de la imagen");
            }
            ventana.ActualizarFotograma(imagen);
            Thread.sleep(1000);
            imagen = null;
        }
    }

}
