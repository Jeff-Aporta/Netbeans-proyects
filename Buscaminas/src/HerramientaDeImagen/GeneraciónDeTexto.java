//Última actualización 5/Octubre/2017
package HerramientaDeImagen;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

//Clases forasteras necesarias para el funcionamiento
//Ajustar a las carpetas correspondientes de ser necesario.
import HerramientasMatemáticas.Dupla;
import static HerramientasMatemáticas.Matemática.*;
import static HerramientaDeImagen.Filtros_Lineales.*;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

//Solo usadas en el main, para la prueba
//Opcional eliminar junto con el main
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

public class GeneraciónDeTexto {

    final static String EtiquetaInserciónIcono = "%img%";

    public static void main(String[] args) {
        //Instancia de la clase
        GeneraciónDeTexto texto = new GeneraciónDeTexto()
                //Configuración de las propiedades para la prueba
                .Borde(new Color(100, 0, 20), 5)
                .ColorFuente(Color.BLACK)
                .ImagenTextura(cargarImagen("https://us.123rf.com/450wm/roystudio/roystudio1210/roystudio121000074/16041055-red-metal-texture-background-may-use-as-christmas-background.jpg?ver=6"))
                .AjustarEscala_ImagenTextura_AlturaDelTexto()
                .AlineaciónHorizontal(Dupla.MEDIO)
                .DesfaseVertical(0)
                .ModificarFuente(new Font("arial", Font.PLAIN, 60));

        String Texto_Prueba = "Quisiera ser un %img%"
                + "\nPara tocar mi %img% en tu %img%"
                + "\nY hacer  %img% de  %img% por donde quiera"
                + "\nPasar la  %img% en  %img%"
                + "\n%img% en ti";

        BufferedImage Pez = cargarImagen(
                "https://orig00.deviantart.net/4230/f/2017/277/8/1/pez_by_jeffreyagudelo-dbpjxzh.png"
        );
        BufferedImage Nariz = cargarImagen(
                "https://orig00.deviantart.net/a5c8/f/2017/277/f/6/nariz_by_jeffreyagudelo-dbpjxz8.png"
        );
        BufferedImage Pecera = cargarImagen(
                "https://orig00.deviantart.net/5637/f/2017/277/f/8/pecera_by_jeffreyagudelo-dbpjxzf.png"
        );
        BufferedImage Burbujas = cargarImagen(
                "https://orig00.deviantart.net/491d/f/2017/277/7/c/burbujas_by_jeffreyagudelo-dbpjxz4.png"
        );
        BufferedImage Amor = cargarImagen(
                "https://pre00.deviantart.net/b21c/th/pre/f/2017/277/e/2/amor_by_jeffreyagudelo-dbpjxz0.png"
        );
        BufferedImage Noche = cargarImagen(
                "https://orig00.deviantart.net/59e5/f/2017/277/f/c/noche_by_jeffreyagudelo-dbpjxza.png"
        );
        BufferedImage Vela = cargarImagen(
                "https://orig00.deviantart.net/98e2/f/2017/277/1/9/vela_by_jeffreyagudelo-dbpjxzj.png"
        );
        BufferedImage Mojado = cargarImagen(
                "https://orig00.deviantart.net/7a2e/f/2017/277/a/5/mojado_by_jeffreyagudelo-dbpjxz6.png"
        );

        //Conversión del texto a imagen.
        BufferedImage TextoAImagen = texto.GenerarTexto(
                Texto_Prueba,
                Pez, Nariz, Pecera, Burbujas, Amor, Noche, Vela, Mojado
        );
        JFrame ventana = new JFrame("Jeff Aporta, Prueba de la conversión de texto a imagen");
        try {
            String Icono = "http://img07.deviantart.net/cc6c/i/2016/283/8/a/firma_jeff_aporta___copia_by_jeffreyagudelo-dakl4y3.png";
            ventana.setIconImage(ImageIO.read(new URL(Icono)));
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono, no hay acceso a la imagen en internet");
        }
        ventana.add(new JLabel(new ImageIcon(TextoAImagen)));
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(new Dupla(TextoAImagen).Adicionar(50).convDimension());
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private Font fuente = new Font("arial", Font.PLAIN, 72);
    private FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(fuente);

    public int AlineaciónHorizontal = Dupla.IZQUIERDA;
    public boolean ConservarPixeles = false;
    public Color ColorTexto = Color.BLACK;

    public final static byte //
            BORDE_FUERA = 0,
            BORDE_DENTRO = 1,
            BORDE_CENTRO = 2,
            BORDE_DEBORADOR = 3,
            BORDE_LIMPIADOR = 4;

    public Color ColorBorde;
    public byte ESTILO_BORDE = BORDE_FUERA;
    public float GrosorBorde = 1.5f;

    public BufferedImage ImagenTextura;
    public float TransparenciaImagenTextura = 1;
    public Dupla EscalaDePatrón_ImagenTextura = Dupla.IDENTIDAD;

    public int DesfaseVertical = 25;

    public GeneraciónDeTexto ModificarTamañoFuente(int Tamaño) {
        ModificarFuente(new Font(fuente.getFontName(), fuente.getStyle(), Tamaño));
        return this;
    }

    public GeneraciónDeTexto AtributoNormal_Plano() {
        ModificarFuente(new Font(fuente.getFontName(), Font.PLAIN, fuente.getSize()));
        return this;
    }

    public GeneraciónDeTexto AtributoNegrita() {
        ModificarFuente(new Font(fuente.getFontName(), Font.BOLD, fuente.getSize()));
        return this;
    }

    public GeneraciónDeTexto AtributoCursiva() {
        ModificarFuente(new Font(fuente.getFontName(), Font.ITALIC, fuente.getSize()));
        return this;
    }

    public GeneraciónDeTexto AtributoNegritaCursiva() {
        ModificarFuente(new Font(fuente.getFontName(), Font.BOLD | Font.ITALIC, fuente.getSize()));
        return this;
    }

    public GeneraciónDeTexto TransparenciaImagenTextura(float transparencia) {
        TransparenciaImagenTextura = transparencia;
        return this;
    }

    public GeneraciónDeTexto EscalaDePatrón_ImagenTextura(Dupla escala) {
        EscalaDePatrón_ImagenTextura = escala;
        return this;
    }

    public GeneraciónDeTexto ImagenTextura(BufferedImage imagen) {
        ImagenTextura = imagen;
        return this;
    }

    public GeneraciónDeTexto AjustarEscala_ImagenTextura_AlturaDelTexto() {
        if (ImagenTextura != null) {
            double alto = 1.06 * fm.getHeight();
            EscalaDePatrón_ImagenTextura = new Dupla(alto / ImagenTextura.getHeight());
        }
        return this;
    }

    public GeneraciónDeTexto Borde(Color color, float grosor, byte EstiloBorde) {
        ColorBorde = color;
        GrosorBorde = grosor;
        ESTILO_BORDE = EstiloBorde;
        return this;
    }

    public GeneraciónDeTexto Borde(Color color, float grosor) {
        ColorBorde = color;
        GrosorBorde = grosor;
        return this;
    }

    public GeneraciónDeTexto ColorBorde(Color color) {
        ColorBorde = color;
        return this;
    }

    public GeneraciónDeTexto GrosorBorde(float grosor) {
        GrosorBorde = grosor;
        return this;
    }

    public GeneraciónDeTexto ModificarFuente(Font fuente) {
        this.fuente = fuente;
        fm = Toolkit.getDefaultToolkit().getFontMetrics(fuente);
        return this;
    }

    public FontMetrics ObtenerMetricasDeFuente() {
        return fm;
    }

    public GeneraciónDeTexto AlineaciónHorizontal(int Alineación) {
        AlineaciónHorizontal = Alineación;
        return this;
    }

    public GeneraciónDeTexto DesfaseVertical(int desfase) {
        DesfaseVertical = desfase;
        return this;
    }

    public GeneraciónDeTexto ColorFuente(Color c) {
        ColorTexto = c;
        return this;
    }

    public BufferedImage DibujarTextoCentradoEnImagen(String Texto, BufferedImage imagen) {
        Ajuste_Centrado_Ajustar(GenerarTexto(Texto), imagen);
        return imagen;
    }

    public BufferedImage GenerarImagenConTexto(String Texto, Dupla TamañoImg) {
        BufferedImage ImgÁrea = TamañoImg.convBufferedImage();
        Ajuste_Ajustar(GenerarTexto(Texto), ImgÁrea);
        return ImgÁrea;
    }

    public BufferedImage IconoIzquierdaTexto(String texto, int Separación, BufferedImage... iconos) {
        return ImagenIzquierdaTexto(texto, Separación, false, iconos);
    }

    public BufferedImage ImagenIzquierdaTexto(String texto, int Separación, boolean conservarPixelesImg, BufferedImage... iconos) {
        BufferedImage ImagenPrincipal = iconos[0];
        BufferedImage[] iconosTexto = new BufferedImage[iconos.length - 1];
        for (int i = 1; i < iconos.length; i++) {
            iconosTexto[i - 1] = iconos[i];
        }
        BufferedImage textoImg = GenerarTexto(texto, iconosTexto);
        ImagenPrincipal = Escalar(
                ImagenPrincipal, (double) textoImg.getHeight() / ImagenPrincipal.getHeight(), conservarPixelesImg
        );
        BufferedImage imagenRetorno = new BufferedImage(
                textoImg.getWidth() + ImagenPrincipal.getWidth() + Separación,
                textoImg.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics g = imagenRetorno.getGraphics();
        g.drawImage(ImagenPrincipal, 0, 0, null);
        g.drawImage(textoImg, ImagenPrincipal.getWidth() + Separación, 0, null);
        return imagenRetorno;
    }

    public BufferedImage GenerarTexto(String texto, BufferedImage... iconos) {
        return GenerarTexto(texto, 0, iconos);
    }

    public BufferedImage GenerarTexto(String texto, double ReducciónImagenes, BufferedImage... iconos) {
        String[] Renglones = texto.split("\n");
        BufferedImage[] Renglón = new BufferedImage[Renglones.length];
        int[] Cantidad_imgRenglón = new int[Renglón.length];
        {
            int aux = 0;
            for (String renglón : Renglones) {
                Cantidad_imgRenglón[aux++] = (renglón.length() - renglón.replaceAll(EtiquetaInserciónIcono, "").length()) / EtiquetaInserciónIcono.length();
            }
        }
        Dupla Dimensiones = new Dupla();
        int imagenesUsadas = 0;
        for (int i = 0; i < Renglones.length; i++) {
            BufferedImage imgs_renglón[] = new BufferedImage[Cantidad_imgRenglón[i]];
            for (int j = 0; j < Cantidad_imgRenglón[i]; j++) {
                imgs_renglón[j] = iconos[imagenesUsadas++];
            }
            Renglón[i] = TextoLineal(Renglones[i], ReducciónImagenes, imgs_renglón);
            Dimensiones.X = Máx(Dimensiones.X, Renglón[i].getWidth());
            Dimensiones.Y += Renglón[i].getHeight() + DesfaseVertical;
        }
        Dimensiones.Y -= DesfaseVertical;
        BufferedImage ImagenFinal = Dimensiones.convBufferedImage();
        Graphics g = ImagenFinal.getGraphics();
        int alturaPorRenglón = Renglón[0].getHeight() + DesfaseVertical;
        for (int i = 0; i < Renglones.length; i++) {
            g.drawImage(
                    Renglón[i],
                    Dupla.Alinear(
                            new Dupla(ImagenFinal), new Dupla(Renglón[i]), AlineaciónHorizontal, -1
                    ).intX(), i * alturaPorRenglón, null
            );
        }
        return ImagenFinal;
    }

    public BufferedImage GenerarTexto(String texto) {
        String[] Renglones = texto.split("\n");
        BufferedImage[] Renglón = new BufferedImage[Renglones.length];
        Dupla Dimensiones = new Dupla();
        for (int i = 0; i < Renglones.length; i++) {
            Renglón[i] = TextoLineal(Renglones[i]);
            Dimensiones.X = Máx(Dimensiones.X, Renglón[i].getWidth());
            Dimensiones.Y += Renglón[i].getHeight() + DesfaseVertical;
        }
        Dimensiones.Y -= DesfaseVertical;
        BufferedImage ImagenFinal = Dimensiones.convBufferedImage();
        Graphics g = ImagenFinal.getGraphics();
        int alturaPorRenglón = Renglón[0].getHeight() + DesfaseVertical;
        for (int i = 0; i < Renglones.length; i++) {
            g.drawImage(
                    Renglón[i],
                    Dupla.Alinear(
                            new Dupla(ImagenFinal), new Dupla(Renglón[i]), AlineaciónHorizontal, Dupla.MEDIO
                    ).intX(), i * alturaPorRenglón, null
            );
        }
        return ImagenFinal;
    }

    private BufferedImage TextoLineal(String texto, BufferedImage... iconos) {
        return TextoLineal(texto, 0, iconos);
    }

    private BufferedImage TextoLineal(String texto, double ReducciónImagenes, BufferedImage... iconos) {
        String partes[] = texto.split(EtiquetaInserciónIcono);
        int CantidadImagenes = partes.length * 2 - 1 + (texto.endsWith(EtiquetaInserciónIcono) ? 1 : 0);
        BufferedImage imagenes[] = new BufferedImage[CantidadImagenes];

        int //
                img = 0,
                ancho = 0,
                alto = 0;

        for (int i = 0; i < partes.length; i++) {
            imagenes[img] = TextoLineal(partes[i]);
            if (alto == 0) {
                alto = imagenes[img].getHeight();
            }
            ancho += imagenes[img++].getWidth();
            if (i < iconos.length) {
                if (iconos[i] != null) {
                    double escala = (double) alto / iconos[i].getHeight();
                    imagenes[img] = new Dupla(iconos[i]).Escalar(escala).convBufferedImage();
                    Ajuste_Personalizado(iconos[i], imagenes[img], AJUSTE_AJUSTAR, ConservarPixeles);
                    ancho += imagenes[img++].getWidth();
                }
            }
        }

        BufferedImage retorno = Dupla.convBufferedImage(ancho, alto);
        {
            int x = 0;
            for (BufferedImage imagen : imagenes) {
                if (imagen != null) {
                    int y = (retorno.getHeight() - imagen.getHeight()) / 2;
                    retorno.getGraphics().drawImage(imagen, x, y, null);
                    x += imagen.getWidth();
                }
            }
        }
        return retorno;
    }

    private BufferedImage TextoLineal(String texto) {

        boolean HayQueDibujarBorde = ColorBorde != null;

        final int x, y, desfaseBorde, ancho, alto;
        desfaseBorde = (int) (HayQueDibujarBorde ? GrosorBorde : 0);
        ancho = fm.stringWidth(texto) + desfaseBorde;
        alto = (int) (1.06 * fm.getHeight() + desfaseBorde);
        x = desfaseBorde / 2;
        y = fm.getAscent() + fm.getLeading() + desfaseBorde / 4;

        if (texto == null) {
            texto = "null";
        }

        if (texto.equals("")) {
            /* Si no hay texto podemos de una vez retornar una imagen con la
             * altura correspondiente a las métricas de fuente pero sólo con un
             * pixel de ancho        */
            return Dupla.convBufferedImage(1, alto);
        }

        BufferedImage ImgTexto = Dupla.convBufferedImage(ancho, alto);
        Graphics2D g = ImgTexto.createGraphics();

        if (!ConservarPixeles) {
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
        }
        g.setFont(fuente);
        TextLayout SiluetaDelTexto = new TextLayout(texto, fuente, g.getFontRenderContext());
        Shape Silueta = SiluetaDelTexto.getOutline(AffineTransform.getTranslateInstance(x, y));

        if (ImagenTextura == null) {
            g.setColor(ColorTexto);
            g.fill(Silueta);
        } else {
            double w = ImagenTextura.getWidth() * EscalaDePatrón_ImagenTextura.X;
            double h = ImagenTextura.getHeight() * EscalaDePatrón_ImagenTextura.Y;
            g.setPaint(
                    new TexturePaint(ImagenTextura, new Rectangle2D.Double(0, 0, w, h))
            );
            g.fill(Silueta);
        }

        if (HayQueDibujarBorde) {
            DibujarBorde(g, Silueta);
        }

        return ImgTexto;
    }

    void DibujarBorde(Graphics2D g, Shape shape) {
        g.setStroke(new BasicStroke(GrosorBorde,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
        g.setColor(ColorBorde);
        switch (ESTILO_BORDE) {
            case BORDE_FUERA:
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP));
                break;
            case BORDE_CENTRO:
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                break;
            case BORDE_DENTRO:
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP));
                break;
            case BORDE_DEBORADOR:
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OUT));
                break;
            case BORDE_LIMPIADOR:
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
                break;
        }
        if (!ConservarPixeles) {
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
        }
        g.draw(shape);
    }

    public static BufferedImage cargarImagen(final String dirección) {
        BufferedImage imagen = null;
        try {
            imagen = ImageIO.read(ClassLoader.class.getResource(dirección));
        } catch (Exception e) {
            try {
                ImageIO.read(ClassLoader.class.getResource("/" + dirección));
            } catch (Exception e2) {
                try {
                    imagen = ImageIO.read(new File(dirección));
                } catch (Exception e3) {
                    try {
                        imagen = ImageIO.read(new URL(dirección));
                    } catch (Exception e4) {
                        final String[] PosiblesCarpetas = {
                            "Imagenes", "Imágenes", "Images", "Assets", "Recursos"
                        };
                        for (final String PosibleCarpeta : PosiblesCarpetas) {
                            for (int i = 0; i < 3; i++) {
                                try {
                                    String cm = PosibleCarpeta;
                                    switch (i) {
                                        case 1:
                                            cm = cm.toLowerCase();
                                            break;
                                        case 2:
                                            cm = cm.toUpperCase();
                                            break;
                                    }
                                    imagen = ImageIO.read(ClassLoader.class.getResource("/" + cm + "/" + dirección));
                                    if (imagen != null) {
                                        return imagen;
                                    }
                                } catch (Exception e5) {
                                }
                            }
                            if (imagen != null) {
                                return imagen;
                            }
                        }
                    }
                }
            }
        }
        if (imagen == null) {
            System.err.println("La imagen no se encontró " + dirección);
        }
        return imagen;
    }

    public BufferedImage GenerarTexto(double AjustarDecimales) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
