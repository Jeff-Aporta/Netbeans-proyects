//Última actualización 5/Octubre/2017
package HerramientaDeImagen;

import HerramientaArchivos.LectoEscritor_Imagenes;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import HerramientasMatemáticas.Dupla;
import static HerramientasMatemáticas.Matemática.*;
import static HerramientaDeImagen.Filtros_Lineales.*;
import static HerramientaArchivos.LectoEscritor_Imagenes.*;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class GeneradorDeTexto {

    public final static String Etiqueta_InserciónIcono = "%img%";

    public final static byte BORDE_FUERA = 0;
    public final static byte BORDE_DENTRO = 1;
    public final static byte BORDE_CENTRO = 2;
    public final static byte BORDE_DEBORADOR = 3;
    public final static byte BORDE_LIMPIADOR = 4;

    protected Font fuente = new Font("arial", Font.BOLD, 72);
    protected FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(fuente);

    protected byte AlineaciónHorizontal = Dupla.IZQUIERDA;
    public boolean ConservarPixeles = false;
    public Paint Pintura_Texto = Color.GRAY;

    public Paint Pintura_Borde;
    public byte ESTILO_BORDE = BORDE_FUERA;
    public float GrosorBorde = 1.5f;

    public BufferedImage ImagenTextura;
    public float ImagenTextura_Transparencia = 1;
    public Dupla ImagenTextura_EscalaDePatrón = Dupla.IDENTIDAD;

    public int DistanciaEntreRenglones = 25;

    public GeneradorDeTexto ModificarTamañoFuente(int Tamaño) {
        ModificarFuente(new Font(fuente.getFontName(), fuente.getStyle(), Tamaño));
        return this;
    }

    public GeneradorDeTexto AtributoNormal_Plano() {
        ModificarFuente(new Font(fuente.getFontName(), Font.PLAIN, fuente.getSize()));
        return this;
    }

    public GeneradorDeTexto AtributoNegrita() {
        ModificarFuente(new Font(fuente.getFontName(), Font.BOLD, fuente.getSize()));
        return this;
    }

    public GeneradorDeTexto ConvertirFuente_Cursiva() {
        ModificarFuente(new Font(fuente.getFontName(), Font.ITALIC, fuente.getSize()));
        return this;
    }

    public GeneradorDeTexto ConvertirFuente_NegritaCursiva() {
        ModificarFuente(new Font(fuente.getFontName(), Font.BOLD | Font.ITALIC, fuente.getSize()));
        return this;
    }

    public GeneradorDeTexto ImagenTextura_Transparencia(float transparencia) {
        ImagenTextura_Transparencia = transparencia;
        return this;
    }

    public GeneradorDeTexto ImagenTextura_EscalaDePatrón(Dupla escala) {
        ImagenTextura_EscalaDePatrón = escala;
        return this;
    }

    public GeneradorDeTexto ImagenTextura_Cambiar(BufferedImage imagen) {
        ImagenTextura = imagen;
        return this;
    }

    public GeneradorDeTexto AjustarEscala_ImagenTextura_AlturaDelTexto() {
        if (ImagenTextura != null) {
            double alto = 1.06 * fm.getHeight();
            ImagenTextura_EscalaDePatrón = new Dupla(alto / ImagenTextura.getHeight());
        }
        return this;
    }

    public GeneradorDeTexto Borde(Paint color, float grosor, byte EstiloBorde) {
        Pintura_Borde = color;
        GrosorBorde = grosor;
        ESTILO_BORDE = EstiloBorde;
        return this;
    }

    public GeneradorDeTexto Borde(Paint color, float grosor) {
        Pintura_Borde = color;
        GrosorBorde = grosor;
        return this;
    }

    public GeneradorDeTexto ColorBorde(Paint color) {
        Pintura_Borde = color;
        return this;
    }

    public GeneradorDeTexto GrosorBorde(float grosor) {
        GrosorBorde = grosor;
        return this;
    }

    public GeneradorDeTexto ModificarTipoFuente(String Nombrefuente) {
        this.fuente = new Font(Nombrefuente, this.fuente.getStyle(), this.fuente.getSize());
        fm = Toolkit.getDefaultToolkit().getFontMetrics(this.fuente);
        return this;
    }

    public GeneradorDeTexto ModificarFuente(Font fuente) {
        this.fuente = fuente;
        fm = Toolkit.getDefaultToolkit().getFontMetrics(fuente);
        return this;
    }

    public FontMetrics ObtenerMetricasDeFuente() {
        return fm;
    }

    public GeneradorDeTexto AlineaciónHorizontal(byte Alineación) {
        AlineaciónHorizontal = Alineación;
        return this;
    }

    public GeneradorDeTexto DistanciaEntreRenglones(int desfase) {
        DistanciaEntreRenglones = desfase;
        return this;
    }

    public GeneradorDeTexto ColorFuente(Paint c) {
        Pintura_Texto = c;
        return this;
    }

    public BufferedImage DibujarTextoCentradoEnImagen(BufferedImage imagen, Texto_Iconos texto_Iconos) {
        return DibujarTextoCentradoEnImagen(imagen, texto_Iconos.texto, texto_Iconos.iconos);
    }

    public BufferedImage DibujarTextoCentradoEnImagen(BufferedImage imagen, String Texto, BufferedImage... iconos) {
        Ajuste_Centrado_Ajustar(GenerarTexto(Texto, iconos), imagen);
        return imagen;
    }

    public BufferedImage DibujarTextoCentradoEnImagen(BufferedImage imagen, String Texto) {
        return DibujarTextoCentradoEnImagen(Texto, imagen);
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

    public BufferedImage GenerarTexto(Texto_Iconos texto_Iconos) {
        return GenerarTexto(texto_Iconos.texto, texto_Iconos.iconos);
    }

    public BufferedImage GenerarTexto(String texto, Object... Iconos) {
        BufferedImage[] iconos = new BufferedImage[Iconos.length];
        for (int i = 0; i < iconos.length; i++) {
            if (Iconos[i] instanceof String) {
                String s = (String) Iconos[i];
                iconos[i] = LectoEscritor_Imagenes.cargarImagen(s);
            } else if (Iconos[i] instanceof BufferedImage) {
                iconos[i] = (BufferedImage) Iconos[i];
            } else {
                throw new RuntimeException(
                        "Sólamente se reciben objetos que representen la ruta de una imagen o una imagen como "
                        + "bufferedimage"
                );
            }
        }
        return GenerarTexto(texto, iconos);
    }

    public BufferedImage GenerarTexto(String texto, String... Direccióniconos) {
        BufferedImage[] iconos = new BufferedImage[Direccióniconos.length];
        for (int i = 0; i < iconos.length; i++) {
            iconos[i] = LectoEscritor_Imagenes.cargarImagen(Direccióniconos[i]);
        }
        return GenerarTexto(texto, iconos);
    }

    public BufferedImage GenerarTexto(String texto, BufferedImage... iconos) {
        String[] Renglones = texto.split("\n");
        BufferedImage[] Renglón = new BufferedImage[Renglones.length];
        int[] Cantidad_imgRenglón = new int[Renglón.length];
        {
            int aux = 0;
            for (String renglón : Renglones) {
                Cantidad_imgRenglón[aux++] = (renglón.length() - renglón.replaceAll(Etiqueta_InserciónIcono, "").length()) / Etiqueta_InserciónIcono.length();
            }
        }
        Dupla Dimensiones = new Dupla();
        int imagenesUsadas = 0;
        for (int i = 0; i < Renglones.length; i++) {
            BufferedImage imgs_renglón[] = new BufferedImage[Cantidad_imgRenglón[i]];
            for (int j = 0; j < Cantidad_imgRenglón[i]; j++) {
                imgs_renglón[j] = iconos[imagenesUsadas++];
            }
            Renglón[i] = TextoLineal(Renglones[i], imgs_renglón);
            Dimensiones.X = Máx(Dimensiones.X, Renglón[i].getWidth());
            Dimensiones.Y += Renglón[i].getHeight() + DistanciaEntreRenglones;
        }
        Dimensiones.Y -= DistanciaEntreRenglones;
        BufferedImage ImagenFinal = Dimensiones.convBufferedImage();
        Graphics g = ImagenFinal.getGraphics();
        int alturaPorRenglón = Renglón[0].getHeight() + DistanciaEntreRenglones;
        for (int i = 0; i < Renglones.length; i++) {
            g.drawImage(
                    Renglón[i],
                    Dupla.Alinear(
                            new Dupla(ImagenFinal), new Dupla(Renglón[i]), AlineaciónHorizontal, Dupla.POR_DEFECTO
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
            Dimensiones.Y += Renglón[i].getHeight() + DistanciaEntreRenglones;
        }
        Dimensiones.Y -= DistanciaEntreRenglones;
        BufferedImage ImagenFinal = Dimensiones.convBufferedImage();
        Graphics g = ImagenFinal.getGraphics();
        int alturaPorRenglón = Renglón[0].getHeight() + DistanciaEntreRenglones;
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
        if (texto.startsWith(Etiqueta_InserciónIcono)) {
            texto = " " + texto;
        }
        String partes[] = texto.split(Etiqueta_InserciónIcono);
        int CantidadImagenes = partes.length * 2 - 1 + (texto.endsWith(Etiqueta_InserciónIcono) ? 1 : 0);
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

        boolean HayQueDibujarBorde = Pintura_Borde != null;

        final int x, y, desfaseBorde, ancho, alto;
        desfaseBorde = (int) (HayQueDibujarBorde ? GrosorBorde : 0);
        ancho = fm.stringWidth(texto) + desfaseBorde;
        alto = (int) (fm.getHeight() + desfaseBorde);
        x = desfaseBorde / 2;
        y = fm.getAscent() + fm.getLeading() + desfaseBorde / 2;

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
            g.setPaint(Pintura_Texto);
            g.fill(Silueta);
        } else {
            double w = ImagenTextura.getWidth() * ImagenTextura_EscalaDePatrón.X;
            double h = ImagenTextura.getHeight() * ImagenTextura_EscalaDePatrón.Y;
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
        g.setStroke(new BasicStroke(GrosorBorde, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        g.setPaint(Pintura_Borde);
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

    public static class Texto_Iconos {

        public String texto;
        public BufferedImage[] iconos;

        public Texto_Iconos(String texto, BufferedImage... iconos) {
            this.texto = texto;
            this.iconos = iconos;
        }
    }
}
