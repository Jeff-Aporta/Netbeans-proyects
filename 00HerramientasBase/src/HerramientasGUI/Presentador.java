package HerramientasGUI;

//<editor-fold defaultstate="collapsed" desc="Importación de librerias ●">
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientaDeImagen.Filtros_Lineales;
import static HerramientaDeImagen.Filtros_Lineales.AJUSTE_CENTRADO_AJUSTAR;
import static HerramientaDeImagen.Filtros_Lineales.AJUSTE_RELLENAR;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientasMatemáticas.Dupla;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
//</editor-fold>

public class Presentador extends javax.swing.JLabel {

    //<editor-fold defaultstate="collapsed" desc="Variables para personalizar el dibujado del fotograma ●">
    public boolean ConservarPíxeles = false;
    public int TIPO_AJUSTE_FOTOGRAMA = AJUSTE_CENTRADO_AJUSTAR;
    public Filtros_Lineales.PropiedadAjuste PropiedadAjusteFotograma;
    //</editor-fold>

    public int TIPO_AJUSTE_FONDO = AJUSTE_RELLENAR;
    protected Paint ColorFondo;
    private BufferedImage ImagenFondo = null;

    public BufferedImage ÚltimaImagenCargada = null;
    public BufferedImage Fotograma;
    public BufferedImage UFotograma;

    public float grosorBorde_Presentador;
    public Paint ColorBorde_Presentador;

    public float grosorBorde_Fotograma;
    public Paint ColorBorde_Fotograma;

    public GeneradorDeTexto Consola;

    final Component ventana;

    public Presentador() {
        this(null);
    }

    public Presentador(Component v) {
        ventana = v;
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                ActualizarFotograma();
            }
        });
    }

    public Presentador ActualizarFotograma() {
        BufferedImage a = null;
        ActualizarFotograma(a);
        return this;
    }

    public Presentador ActualizarFotograma(String imagen) {
        BufferedImage img = LectoEscrituraArchivos.cargar_imagen(imagen);
        ActualizarFotograma(img);
        return this;
    }

    public Presentador ActualizarFotograma(BufferedImage imagen) {
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
            PropiedadAjusteFotograma = AjustarImagenAlÁreaDeDibujado(imagen, Fotograma, TIPO_AJUSTE_FOTOGRAMA);
            DibujarBorde_Fotograma(Fotograma.createGraphics());
            DibujarBorde_Presentador(Fotograma.createGraphics());
            UFotograma = Fotograma;
        } catch (Exception e) {
        }
        repaint();
        return this;
    }

    public void DibujarBorde_Fotograma(Graphics2D g) {
        if (ColorBorde_Fotograma != null && grosorBorde_Fotograma > 0) {
            g.setStroke(new BasicStroke(grosorBorde_Fotograma));
            g.setPaint(ColorBorde_Fotograma);
            g.drawRect(
                    PropiedadAjusteFotograma.Posición.intX(),
                    PropiedadAjusteFotograma.Posición.intY(),
                    (int) (PropiedadAjusteFotograma.Escalación.X * ÚltimaImagenCargada.getWidth()),
                    (int) (PropiedadAjusteFotograma.Escalación.Y * ÚltimaImagenCargada.getHeight())
            );
        }
    }

    public void DibujarFondo(Graphics2D g) {
        float alpha = getBackground().getAlpha() / 255f;
        if (alpha < 0.05) {
            return;
        } 
        if (ColorFondo != null) {
            g.setPaint(ColorFondo);
            if (alpha < 1) {
                g.setComposite(
                        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
                );
            }
            g.fillRect(0, 0, getWidth(), getHeight());
        } 
        if (ImagenFondo != null) {
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

    public void DibujarBorde_Presentador(Graphics2D g) {
        if (grosorBorde_Presentador > 0) {
            if (ventana != null && ventana instanceof Frame && ((Frame) ventana).isUndecorated()) {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(new BasicStroke(ObtenerGrosorBorde_Presentador()));
                g.setPaint(ObtenerColorBorde_Presentador());
                g.draw(((Frame) ventana).getShape());
            } else {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(new BasicStroke(ObtenerGrosorBorde_Presentador()));
                g.setPaint(ObtenerColorBorde_Presentador());
                g.drawRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    @Override
    public void paint(Graphics grphcs) {
        grphcs.drawImage(Fotograma, 0, 0, null);
        super.paint(grphcs);
    }

    //<editor-fold defaultstate="collapsed" desc="Getters y Setters del Borde »">
    //<editor-fold defaultstate="collapsed" desc="Getters del Borde »">
    public float ObtenerGrosorBorde_Presentador() {
        return grosorBorde_Presentador;
    }

    public Paint ObtenerColorBorde_Presentador() {
        return ColorBorde_Presentador;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Modificadores de los bordes »">
    //<editor-fold defaultstate="collapsed" desc="Borde del Presentador »">
    public Presentador Borde_Presentador(Paint color, float grosor) {
        grosorBorde_Presentador = grosor;
        ColorBorde_Presentador = color;
        ActualizarFotograma();
        return this;
    }

    public void GrosorBorde_Presentador(float grosor) {
        grosorBorde_Presentador = grosor;
        ActualizarFotograma();
    }

    public void ColorBorde_Presentador(Paint color) {
        ColorBorde_Presentador = color;
        ActualizarFotograma();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Borde del fotograma »">
    public Presentador Borde_Fotograma(Paint color, float grosor) {
        grosorBorde_Fotograma = grosor;
        ColorBorde_Fotograma = color;
        ActualizarFotograma();
        return this;
    }

    public void GrosorBorde_Fotograma(float grosor) {
        grosorBorde_Fotograma = grosor;
        ActualizarFotograma();
    }

    public void ColorBorde_Fotograma(Paint color) {
        ColorBorde_Fotograma = color;
        ActualizarFotograma();
    }
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setters del fondo »">
    public Presentador ColorFondo(Paint color) {
        ColorFondo = color;
        return this;
    }

    public void ImagenFondo(BufferedImage img) {
        ImagenFondo = img;
        ActualizarFotograma();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas para mostrar texto rápidamente »">
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas para el tratamiento de la posición del cursor »">
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
    //</editor-fold>
}
