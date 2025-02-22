package HerramientasGUI;

import HerramientaDeImagen.Filtros_Lineales;
import static HerramientaDeImagen.Filtros_Lineales.AJUSTE_CENTRADO_AJUSTAR;
import static HerramientaDeImagen.Filtros_Lineales.AJUSTE_RELLENAR;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientasMatemáticas.Dupla;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class Presentador extends javax.swing.JLabel {

    //Herramientas del dibujado de la imagen
    public boolean ConservarPíxeles = false;
    public int TIPO_AJUSTE = AJUSTE_CENTRADO_AJUSTAR;
    public Filtros_Lineales.PropiedadAjuste PropiedadAjusteFotograma;

    //Herramientas para el fondo
    public int TIPO_AJUSTE_FONDO = AJUSTE_RELLENAR;
    protected Paint ColorFondo;
    private BufferedImage ImagenFondo = null;

    //Herramientas para la escalación de la aplicación
    BufferedImage ÚltimaImagenCargada = null;
    BufferedImage Fotograma;

    //Herramientas para el borde
    protected float grosorBorde;
    protected Paint ColorBorde;

    public GeneradorDeTexto Consola;

    final Component ventana;

    public Presentador() {
        this(null);
    }

    public Presentador(Component v) {
        ventana = v;
        setVerticalAlignment(TOP);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                ActualizarFotograma();
            }
        });
    }

    public void ActualizarFotograma() {
        ActualizarFotograma(null);
    }

    public void ActualizarFotograma(BufferedImage imagen) {
        if (imagen == null || Fotograma == null) {
            imagen = ÚltimaImagenCargada;
        } else {
            ÚltimaImagenCargada = imagen;
        }
        try {
            Fotograma = new BufferedImage(
                    getWidth(), getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            DibujarFondo(Fotograma.createGraphics());
            PropiedadAjusteFotograma = AjustarImagenAlÁreaDeDibujado(imagen, Fotograma, TIPO_AJUSTE);
            DibujarBorde(Fotograma.createGraphics());
            setText("");
//            setIcon(new ImageIcon(Fotograma));
            repaint();
        } catch (Exception e) {
        }
    }

    @Override
    public void paint(Graphics grphcs) {
        super.paint(grphcs);
        grphcs.drawImage(Fotograma, 0, 0, null);
    }

    public void DibujarFondo(Graphics2D g) {
        float alpha = getBackground().getAlpha() / 255f;
        if (ColorFondo != null) {
            g.setPaint(ColorFondo);
            g.setComposite(
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
            );
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setComposite(AlphaComposite.SrcOver);
        }
        if (ImagenFondo != null && alpha > 0.03) {
            if (alpha < 1) {
                BufferedImage fondo = new BufferedImage(ImagenFondo.getWidth(), ImagenFondo.getHeight(), 2);
                Graphics2D g2 = fondo.createGraphics();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.drawImage(ImagenFondo, 0, 0, null);
                AjustarImagenAlÁreaDeDibujado(fondo, Fotograma, TIPO_AJUSTE_FONDO);
            } else {
                AjustarImagenAlÁreaDeDibujado(ImagenFondo, Fotograma, TIPO_AJUSTE_FONDO);
            }
        }
    }

    private Filtros_Lineales.PropiedadAjuste AjustarImagenAlÁreaDeDibujado(BufferedImage ImagenDibujar, BufferedImage imagenÁrea, int TIPO_AJUSTE) {
        return Ajuste_Personalizado(
                ImagenDibujar, imagenÁrea, TIPO_AJUSTE, ConservarPíxeles
        );
    }

    public void DibujarBorde(Graphics2D g) {
        if (grosorBorde > 0) {
            if (ventana != null && ventana instanceof Frame && ((Frame) ventana).isUndecorated()) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(new BasicStroke(ObtenerGrosorBorde()));
                g.setPaint(ObtenerPaintBorde());
                g.draw(((Frame) ventana).getShape());
            } else {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(new BasicStroke(ObtenerGrosorBorde()));
                g.setPaint(ObtenerPaintBorde());
                g.drawRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    public float ObtenerGrosorBorde() {
        return grosorBorde;
    }

    public Paint ObtenerPaintBorde() {
        return ColorBorde;
    }

    public Color ObtenerColorBorde() {
        if (ColorBorde instanceof Color) {
            return (Color) ColorBorde;
        }
        return null;
    }

    public Presentador ColorFondo(Paint color) {
        ColorFondo = color;
        return this;
    }

    public Presentador Borde(Paint color, float grosor) {
        grosorBorde = grosor;
        ColorBorde = color;
        ActualizarFotograma();
        return this;
    }

    public void GrosorBorde(float grosor) {
        grosorBorde = grosor;
        ActualizarFotograma();
    }

    public void ColorBorde(Paint color) {
        ColorBorde = color;
        ActualizarFotograma();
    }

    public void ImagenFondo(BufferedImage img) {
        ImagenFondo = img;
        ActualizarFotograma();
    }

    public void MostrarTexto(String texto) {
        if (Consola == null) {
            this.Consola = new GeneradorDeTexto();
        }
        ActualizarFotograma(Consola.GenerarTexto(texto));
    }

    public void MostrarTexto(String texto, BufferedImage... Iconos) {
        if (Consola == null) {
            this.Consola = new GeneradorDeTexto();
        }
        ActualizarFotograma(Consola.GenerarTexto(texto, Iconos));
    }

    public Dupla ObtenerPosiciónDelCursor() {
        return Dupla.PosiciónCursorEnComponente(this);
    }

    public Dupla PosiciónCursorEnFotograma() {
        if (PropiedadAjusteFotograma == null) {
            return Dupla.ORIGEN;
        }
        return ObtenerPosiciónDelCursor()
                .Sustraer(PropiedadAjusteFotograma.Posición)
                .Dividir(PropiedadAjusteFotograma.Escalación);
    }
}
