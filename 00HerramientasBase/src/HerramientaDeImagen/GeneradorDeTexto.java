//Última actualización 5/Octubre/2017
package HerramientaDeImagen;

import HerramientaArchivos.BibliotecaArchivos;
import HerramientaArchivos.CarpetaDeRecursos;
import HerramientaArchivos.LectoEscrituraArchivos;
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
import static HerramientaArchivos.LectoEscrituraArchivos.*;
import HerramientasGUI.VentanaGráfica;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    public GeneradorDeTexto AtributoCursiva() {
        ModificarFuente(new Font(fuente.getFontName(), Font.ITALIC, fuente.getSize()));
        return this;
    }

    public GeneradorDeTexto AtributoNegritaCursiva() {
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

    public static void main(String[] args) {
        GeneradorDeTexto gdt = new GeneradorDeTexto();
        System.out.println(
                gdt.transformarTextoConRenglonesAuto("Hola mundo, soy Jeffrey Agudelo", 400)
        );
    }

    public String transformarTextoConRenglonesAuto(String texto, int anchoMáximo) {
        texto = texto.replace("\n", " ");
        String[] palabrasArr = texto.split(" ");
        ArrayList<String> palabras = new ArrayList<>();
        for (int i = 0; i < palabrasArr.length; i++) {
            palabras.add(palabrasArr[i]);
        }
        ArrayList<String> renglones = new ArrayList<>();
        ArrayList<String> renglónAcumulado = new ArrayList<>();
        while (!palabras.isEmpty()) {
            renglónAcumulado.add(palabras.get(0));
            palabras.remove(0);
            String textoAcumulado = join(renglónAcumulado, " ");
            if (TextoLineal(textoAcumulado).getWidth() > anchoMáximo) {
                palabras.add(0, renglónAcumulado.get(renglónAcumulado.size() - 1));
                renglónAcumulado.remove(renglónAcumulado.size() - 1);
                renglones.add(join(renglónAcumulado, " "));
                renglónAcumulado.clear();
            }
        }
        renglones.add(join(renglónAcumulado, " "));
        return join(renglones, "\n");
    }

    public static String join(ArrayList<String> r, String separador) {
        String retorno = "";
        for (int i = 0; i < r.size(); i++) {
            retorno += r.get(i) + separador;
        }
        return retorno;
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

    public int alto_texto() {
        boolean HayQueDibujarBorde = Pintura_Borde != null;
        int desfaseBorde = (int) (HayQueDibujarBorde ? GrosorBorde : 0);
        return (int) (fm.getHeight() + desfaseBorde);
    }

    private BufferedImage TextoLineal(String texto) {

        boolean HayQueDibujarBorde = Pintura_Borde != null;

        final int x, y, desfaseBorde, ancho, alto;
        desfaseBorde = (int) (HayQueDibujarBorde ? GrosorBorde : 0);
        ancho = fm.stringWidth(texto) + desfaseBorde;
        alto = alto_texto();
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
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER));
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

    public static GeneradorDeTexto RedNight() throws Exception {
        GeneradorDeTexto texto = new GeneradorDeTexto()
                .Borde(new Color(100, 0, 20), 5)
                .ColorFuente(Color.BLACK)
                .ImagenTextura_Cambiar(BibliotecaArchivos.Imagenes.Texturas.DegradadoRedNight())
                .AjustarEscala_ImagenTextura_AlturaDelTexto()
                .AlineaciónHorizontal(Dupla.MEDIO)
                .DistanciaEntreRenglones(0);
        return texto;
    }

    public static void TestBurbujasDeAmorJuanLuisGuerra() throws Exception {
        GeneradorDeTexto texto = RedNight();

        final CarpetaDeRecursos carpeta = new CarpetaDeRecursos(
                CarpetaDeRecursos.TIPO_APPDATA, "Acertijo"
        );
        carpeta.TiempoDias_Actualizar = 30;

        String Texto_Prueba = "Quisiera ser un %img%"
                + "\nPara tocar mi %img% en tu %img%"
                + "\nY hacer %img% de %img% por donde quiera"
                + "\nPasar la %img% en %img%"
                + "\n%img% en ti";
        String[] nombreImg = {
            //<editor-fold defaultstate="collapsed" desc="Nombres para la prueba">
            "pez.png",
            "nariz.png",
            "pecera.png",
            "burbujas.png",
            "amor.png",
            "noche.png",
            "vela.png",
            "mojado.png"
        //</editor-fold>
        };
        String[] rutaImg = {
            //<editor-fold defaultstate="collapsed" desc="Rutas para la prueba">
            "https://docs.google.com/drawings/d/e/2PACX-1vSS_vjZBIjxZAqgJBF6xNZiLlhLQTFIfkXKKI_MrEcRkv-z2SvJwJzK6nkqNzoDW37vu8r5w-B8ZJL6/pub?w=494&h=355",
            "https://docs.google.com/drawings/d/e/2PACX-1vSSmD-Pjr79SQxBukrC6oGBRN7sP6nwkkmnRhs7Rs5Bvej2hZRPNLXSqq57cbG_e_TxzDp7vG0Yui56/pub?w=770&h=770",
            "https://docs.google.com/drawings/d/e/2PACX-1vTew8G5gKN8honWC16iUmqPQ67PymqwZObAvBmaUUM6Mw81KaC0vRkR-w64SDuhLANIBFUf6jfRpGcY/pub?w=674&h=660",
            "https://docs.google.com/drawings/d/e/2PACX-1vQ9ZD524iC8c5sVIilq6eBqGHFI4FWXNRZYjLcK1-mfwqPPl_QuD11GA6N0ebv0efgzEUL0KKqHo82v/pub?w=782&h=870",
            "https://docs.google.com/drawings/d/e/2PACX-1vRY3bIgF26haagGqRmnOrn5jqV7BzfZnUDEd4tOepNtI7CVTNB3do8BF9NviZq6H-QKm9fn3baqnyLC/pub?w=319&amp;h=302",
            "https://docs.google.com/drawings/d/e/2PACX-1vSY8Lw8yb6Z4PtPnIaaiTuL8bs26cal36V2bJ3jQ-7_oF8KAQwZmIu2Pvgd1UdZymM55qHRwdhmOnZ1/pub?w=908&h=898",
            "https://docs.google.com/drawings/d/e/2PACX-1vSTZBhGCy8pbw6sYqe3TtmhKM0UUOOjcX4_fBbhfPQJ4qkuTVAeykV3DocKsvXPupNp6UctaccykEXY/pub?w=208&h=515",
            "https://docs.google.com/drawings/d/e/2PACX-1vSnfegM6Xrtge283a7iOWT2dgVhzI39flIgkP_bLmxqLvOmJAU4yNX88d5QHz2PkZKzF21I14N4SM1q/pub?w=1391&h=1035"
        //</editor-fold>
        };
        BufferedImage[] imgs = new BufferedImage[rutaImg.length];
        for (int i = 0; i < imgs.length; i++) {
            CarpetaDeRecursos.Recurso rec = carpeta.GenerarRecurso(rutaImg[i], nombreImg[i]);
            imgs[i] = cargar_imagen(rec.DIRECCIÓN);
        }
        BufferedImage TextoAImagen = texto.GenerarTexto(Texto_Prueba, imgs);
        CarpetaDeRecursos.Recurso rec = carpeta.GenerarRecurso("Recultado.png");
        LectoEscrituraArchivos.exportar_imagen(TextoAImagen, rec.DIRECCIÓN);

        VentanaGráfica ventanaGráfica = new VentanaGráfica("Prueba de la generación de texto");
        ventanaGráfica.ActualizarFotograma(TextoAImagen);
    }
}
