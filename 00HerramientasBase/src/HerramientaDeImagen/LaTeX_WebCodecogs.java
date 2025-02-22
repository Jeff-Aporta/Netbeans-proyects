package HerramientaDeImagen;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Este objeto trabaja haciendo uso de un servicio de la página Codecogs<br><br>
 * <p style="text-align: center;">
 *
 * <img src="http://www.codecogs.com/images/logo3.png"><br><br></p>
 * aunque con este servicio contamos con muchas expresiones en LaTeX hay algunas
 * expresiones que no estan,<br><br>
 * <p style="text-align: center;">
 * <img src="https://latex.codecogs.com/png.latex?\dpi{300}\int_{a}^{b}f(x)dx" alt=""><br>
 */
public class LaTeX_WebCodecogs {

    /**
     * Esta es la constante para personalizar el tipo de fuente en Latin
     * Modern<br><br><p style="text-align: center;">
     * <img src="https://latex.codecogs.com/png.latex?\dpi{300}\int_{a}^{b}f(x)dx" alt=""><br>
     */
    public static final String Fuente_LatinModern = "";
    /**
     * Esta es la constante para personalizar el tipo de fuente en
     * Verdana<br><br><p style="text-align: center;">
     * <img src="https://latex.codecogs.com/png.latex?\dpi{300}\fn_jvn&space;\int_{a}^{b}f(x)dx" alt=""><br>
     */
    public static final String Fuente_Verdana = "\\fn_jvn";
    /**
     * Esta es la constante para personalizar el tipo de fuente en Comic
     * sans<br><br><p style="text-align: center;">
     * <img src="https://latex.codecogs.com/png.latex?\dpi{300}\fn_cs&space;\int_{a}^{b}f(x)dx" alt=""><br>
     */
    public static final String Fuente_ComicSans = "\\fn_cs";
    /**
     * Esta es la constante para personalizar el tipo de fuente en Computer
     * Modern<br><br><p style="text-align: center;">
     * <img src="https://latex.codecogs.com/png.latex?\dpi{300}\fn_cm&space;\int_{a}^{b}f(x)dx" alt=""><br>
     */
    public static final String Fuente_ComputerModern = "\\fn_cm";
    /**
     * Esta es la constante para personalizar el tipo de fuente en
     * Verdana<br><br><p style="text-align: center;">
     * <img src="https://latex.codecogs.com/png.latex?\dpi{300}\fn_phv&space;\int_{a}^{b}f(x)dx" alt=""><br>
     */
    public static final String Fuente_Helvetica = "\\fn_phv";
    /**
     * <p style="text-align: center;">
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQZr2llO3fNO6lBxLXnGi-wv7eNcUpWe-gBZdzsxabtWU7TqgEFRQCvoM9zIDSiKX1BWKSMHgXK6LUa/pub?w=400&h=518" alt="">
     */
    public Paint colorFondo;
    /**
     * <p style="text-align: center;">
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRE8ZOcmfJy2eJ1QFR74OI9eIfrUFereOJFvuQ-YHmdrtSvbO8ws37JaOn2wgEx5ljtVuRW5USKhHwO/pub?w=400&h=518" alt="">
     */
    public Paint colorTexto = Color.BLACK;
    /**
     * Esta variable sirve para personalizar el tipo de fuente con la que
     * generaremos la expresión, esta variable es de caracter protegido por lo
     * que para poder cambiar su valor debemos hacer referencia a la función
     * {@link #ModificarFuente(String Fuente)}.<br><br>
     * Los valores que puede tomar son:
     * <ul>
     * <li>Latin Modern</li>
     * <li>Verdana</li>
     * <li>Comic Sans</li>
     * <li>Computer Modern</li>
     * <li>Helvetica</li>
     * </ul>
     */
    protected String Fuente = Fuente_LatinModern;
    /**
     * Esta variable sirve para personalizar el tamaño de la letra, puede tomar
     * un valor númerico entre 1 y 999, esta variable por ser de caracter
     * protegido solo puede ser modificada por el método
     * {@link #ModificarPuntosPorPulgada(int PuntosPorPulgada)}.<br><br>
     */
    protected int PuntosPorPulgada;

    public static void main(String[] args) {
        LaTeX_WebCodecogs GeneradorLaTeX = new LaTeX_WebCodecogs();

        GeneradorLaTeX.colorFondo = new GradientPaint(0, 0, Color.GREEN, 0, 250, Color.BLUE);
        GeneradorLaTeX.colorTexto = new GradientPaint(0, 0, Color.BLUE, 0, 250, Color.GREEN);

        BufferedImage imagen = GeneradorLaTeX.ImagenLaTeX(
                "\\int_{a}^{b}f(x)dx"
        //                "\\frac{cos\\big(\\frac{\\pi}{n}\\big)}"
        //                + "{cos\\bigg(\\frac{2\\cdot asen\\big(sen\\big(\\frac{n\\theta+3\\pi}{2}\\big)\\big)}{n}\\bigg)}"
        );
        JLabel presentador = new JLabel(new ImageIcon(imagen));
        new JFrame("Prueba de la carga de imagenes matemáticas desde internet") {
            {
                add(presentador);
                setMinimumSize(new Dimension(640, 360));
                setSize(imagen.getWidth() + 30, imagen.getHeight() + 30);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }
        };
    }

    public LaTeX_WebCodecogs() {
        PuntosPorPulgada = 500;
    }

    public LaTeX_WebCodecogs(int PuntosPorPulgada) {
        this.PuntosPorPulgada = PuntosPorPulgada;
    }

    /**
     * Esta función sirve para cambiar el tipo de fuente con el que se generará
     * la imagen LaTeX, Los valores que puede tomar son:
     * <ul>
     * <li>Latin Modern
     * <br><br><img src="https://latex.codecogs.com/png.latex?\dpi{170}\int_{a}^{b}f(x)dx" alt=""><br></li>
     * <li>Verdana<br><br><img src="https://latex.codecogs.com/png.latex?\dpi{170}\fn_jvn&space;\int_{a}^{b}f(x)dx" alt=""></li>
     * <li>Comic
     * Sans<br><br><img src="https://latex.codecogs.com/png.latex?\dpi{150}\fn_cs&space;\int_{a}^{b}f(x)dx" alt=""></li>
     * <li>Computer
     * Modern<br><br><img src="https://latex.codecogs.com/png.latex?\dpi{170}\fn_cm&space;\int_{a}^{b}f(x)dx" alt=""></li>
     * <li>Helvetica<br><br><img src="https://latex.codecogs.com/png.latex?\dpi{170}\fn_phv&space;\int_{a}^{b}f(x)dx" alt=""></li>
     * </ul>
     */
    public LaTeX_WebCodecogs ModificarFuente(String Fuente) {
        switch (Fuente) {
            case Fuente_LatinModern:
            case Fuente_Verdana:
            case Fuente_ComicSans:
            case Fuente_ComputerModern:
            case Fuente_Helvetica:
                this.Fuente = Fuente;
                break;
            default:
                throw new RuntimeException("El tipo de fuente no se reconoce");
        }
        return this;
    }

    /**
     * Este método se encarga de modificar los puntos por pulgada de la imagen,
     * que dicho de otra forma ese encarga de modificar el tamaño de la fuente,
     * los valores que admite son entre 0 y
     * 999<br><br><p style="text-align: center;">
     * valor = 50<br><br>
     * <img src="https://latex.codecogs.com/png.latex?\dpi{50}\fn_phv&space;\int_{a}^{b}f(x)dx" alt=""><br><br>
     * valor = 175<br><br>
     * <img src="https://latex.codecogs.com/png.latex?\dpi{175}\fn_phv&space;\int_{a}^{b}f(x)dx" alt=""><br><br>
     * valor = 300<br><br>
     * <img src="https://latex.codecogs.com/png.latex?\dpi{300}\fn_phv&space;\int_{a}^{b}f(x)dx" alt=""><br><br>
     */
    public LaTeX_WebCodecogs ModificarPuntosPorPulgada(int PuntosPorPulgada) {
        this.PuntosPorPulgada = PuntosPorPulgada;
        if (PuntosPorPulgada >= 1000) {
            this.PuntosPorPulgada = 999;
        }
        return this;
    }

    /**
     * Esta es una función auxiliar con el proposito de leer la imagen de
     * internet
     */
    private BufferedImage CargarImagenDeInternet(String ruta) {
        try {
            return ImageIO.read(new URL(ruta));
        } catch (Exception e) {
            System.err.println("La imagen no se pudo cargar " + ruta);
        }
        /**
         * Código para generar una imagen en caso de que no se haya podido
         * cargar de internet, algo como un mensaje de no hay conexión o
         * ecuación mal formulada
         */
        return null;
    }

    /**
     * Este método se encarga del protocolo de la generación de la imagen,
     * generación del código para la imagen, carga web de la imagen y además el
     * tratamiento del color del fondo y del texto.
     */
    public BufferedImage ImagenLaTeX(String expresiónLátex) {
        BufferedImage Expresión = CargarImagenDeInternet(GenerarCódigo(expresiónLátex));
        {
            BufferedImage ColorTexto = new BufferedImage(Expresión.getWidth(), Expresión.getHeight(), 2);
            Graphics2D g = ColorTexto.createGraphics();
            g.drawImage(Expresión, 0, 0, null);
            g.setComposite(AlphaComposite.SrcIn);
            g.setPaint(colorTexto);
            g.fillRect(0, 0, ColorTexto.getWidth(), ColorTexto.getHeight());
            Expresión = ColorTexto;
        }
        BufferedImage retorno = new BufferedImage(Expresión.getWidth(), Expresión.getHeight(), 2);
        {
            Graphics2D g = retorno.createGraphics();
            if (colorFondo != null) {
                g.setPaint(colorFondo);
                g.fillRect(0, 0, retorno.getWidth(), retorno.getHeight());
            }
            g.drawImage(Expresión, 0, 0, null);
        }
        return retorno;
    }

    /**
     * Este método se encarga de administrar la sintaxis de la URL de la imagen
     * que se va a generar
     */
    public String GenerarCódigo(String etiquetaLátex) {
        String código = "https://latex.codecogs.com/png.latex?\\dpi{" + PuntosPorPulgada + "} " + Fuente + " " + etiquetaLátex;
        código = código.replace(" ", "&space;");
        código = código.replace("\n", "\\\\");
        return código;
    }

}
