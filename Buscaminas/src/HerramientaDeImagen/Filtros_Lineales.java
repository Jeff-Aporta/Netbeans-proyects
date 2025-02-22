//Última actualización 23/Julio/2017
package HerramientaDeImagen;

import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import HerramientasMatemáticas.Dupla; //Ajuste a la carpeta correspondiente de ser necesario.
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Filtros_Lineales {

    public static class PropiedadAjuste {

        public Dupla Posición;
        public Dupla Escalación;
        public final static PropiedadAjuste NO_SUFRE_CAMBIOS = new PropiedadAjuste(Dupla.ORIGEN, Dupla.IDENTIDAD);

        public PropiedadAjuste(Dupla Posición, Dupla Escalación) {
            this.Posición = Posición;
            this.Escalación = Escalación;
        }
    }

    public static final int //Identificadores para el ajuste del fotograma en la ventana
            AJUSTE_NINGUNO = 0,
            AJUSTE_RELLENAR = 1, AJUSTE_AJUSTAR = 2, AJUSTE_EXPANDIR = 3,
            AJUSTE_MOSAICO = 4, AJUSTE_ICONO = 5, AJUSTE_CENTRADO = 6,
            AJUSTE_CENTRADO_AJUSTAR = 7;

    public static void main(String[] args) {
        JLabel presentador = new JLabel();
        new JFrame() {
            {
                add(presentador);
                setSize(900, 600);
                setLocationRelativeTo(null);
                setResizable(false);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }
        };
        BufferedImage img = LectoEscritor_Imagenes.cargarImagen(
                "/HerramientaDeImagen/ZZimgPrueba1.jpg"
        );
        BufferedImage fotograma = Dupla.convBufferedImage(presentador);
        Ajuste_Personalizado(Escalar(img, -1, presentador.getHeight(), true), fotograma, AJUSTE_CENTRADO);
        presentador.setIcon(new ImageIcon(fotograma));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
        }
        fotograma = Dupla.convBufferedImage(presentador);
        Ajuste_Personalizado(EscalarGranCalidad(img, -1, presentador.getHeight()), fotograma, AJUSTE_CENTRADO);
        presentador.setIcon(new ImageIcon(fotograma));
    }

    public static PropiedadAjuste Ajuste_Personalizado(BufferedImage imagen, BufferedImage ImagenÁrea, int TIPO_AJUSTE) {
        return Ajuste_Personalizado(imagen, ImagenÁrea, TIPO_AJUSTE, false);
    }

    public static PropiedadAjuste Ajuste_Personalizado(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, int TIPO_AJUSTE, boolean ConservarPixeles) {
        switch (TIPO_AJUSTE) {
            case AJUSTE_RELLENAR:
                return Ajuste_Rellenar(ImagenRelleno, ImagenÁrea, ConservarPixeles);
            case AJUSTE_AJUSTAR:
                return Ajuste_Ajustar(ImagenRelleno, ImagenÁrea, ConservarPixeles);
            case AJUSTE_EXPANDIR:
                return Ajuste_Expandir(ImagenRelleno, ImagenÁrea, ConservarPixeles);
            case AJUSTE_MOSAICO:
            case AJUSTE_ICONO:
                return Ajuste_Mosaico(ImagenRelleno, ImagenÁrea);
            case AJUSTE_CENTRADO:
                return Ajuste_Centrado(ImagenRelleno, ImagenÁrea);
            case AJUSTE_CENTRADO_AJUSTAR:
                return Ajuste_Centrado_Ajustar(ImagenRelleno, ImagenÁrea);
            default:
                return Ajuste_Ninguno(ImagenRelleno, ImagenÁrea);
        }
    }

    public static PropiedadAjuste Ajuste_Ninguno(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {
        ImagenÁrea.getGraphics().drawImage(ImagenRelleno, 0, 0, null);
        return PropiedadAjuste.NO_SUFRE_CAMBIOS;
    }

    public static PropiedadAjuste Ajuste_Rellenar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {
        return Ajuste_Rellenar(ImagenRelleno, ImagenÁrea, false);
    }

    public static PropiedadAjuste Ajuste_Rellenar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, boolean ConservarPixeles) {
        return Ajuste_Rellenar(ImagenRelleno, ImagenÁrea, Dupla.MEDIO, Dupla.MEDIO, ConservarPixeles);
    }

    public static PropiedadAjuste Ajuste_Rellenar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, int AlinH, int AlinV, boolean ConservarPixeles) {
        if (ImagenRelleno == null || ImagenÁrea == null) {
            return null;
        } else if (new Dupla(ImagenRelleno).esIgual(ImagenÁrea)) {
            return Ajuste_Ninguno(ImagenRelleno, ImagenÁrea);
        }
        Dupla ProporcionesEscalación = new Dupla(ImagenÁrea).Dividir(ImagenRelleno);
        double Proporción;
        if (ImagenÁrea.getWidth() > ImagenÁrea.getHeight()) {
            Proporción = ProporcionesEscalación.X;
            if (ImagenRelleno.getHeight() * Proporción < ImagenÁrea.getHeight()) {
                Proporción = ProporcionesEscalación.Y;
            }
        } else {
            Proporción = ProporcionesEscalación.Y;
            if (ImagenRelleno.getWidth() * Proporción < ImagenÁrea.getWidth()) {
                Proporción = ProporcionesEscalación.X;
            }
        }
        Dupla pos = Dupla.Alinear(
                ImagenÁrea, new Dupla(ImagenRelleno).Escalar(Proporción), AlinH, AlinV
        );
        Graphics2D g = ImagenÁrea.createGraphics();
        if (!ConservarPixeles) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }
        g.drawImage(
                ImagenRelleno,
                new AffineTransform(
                        Proporción, 0,
                        0, Proporción,
                        pos.X, pos.Y
                ), null
        );
        return new PropiedadAjuste(pos, new Dupla(Proporción));
    }

    public static PropiedadAjuste Ajuste_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {
        return Ajuste_Ajustar(ImagenRelleno, ImagenÁrea, false);
    }

    public static PropiedadAjuste Ajuste_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, boolean ConservarPixeles) {
        return Ajuste_Ajustar(ImagenRelleno, ImagenÁrea, Dupla.MEDIO, Dupla.MEDIO, ConservarPixeles);
    }

    public static PropiedadAjuste Ajuste_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, int AlinH, int AlinV, boolean ConservarPixeles) {
        if (ImagenRelleno == null || ImagenÁrea == null) {
            return null;
        } else if (new Dupla(ImagenRelleno).esIgual(ImagenÁrea)) {
            return Ajuste_Ninguno(ImagenRelleno, ImagenÁrea);
        }
        Dupla ProporcionEscalación = new Dupla(ImagenÁrea).Dividir(ImagenRelleno);
        double Proporción;
        if (ImagenÁrea.getWidth() < ImagenÁrea.getHeight()) {
            Proporción = ProporcionEscalación.X;
            if (ImagenRelleno.getHeight() * Proporción > ImagenÁrea.getHeight()) {
                Proporción = ProporcionEscalación.Y;
            }
        } else {
            Proporción = ProporcionEscalación.Y;
            if (ImagenRelleno.getWidth() * Proporción > ImagenÁrea.getWidth()) {
                Proporción = ProporcionEscalación.X;
            }
        }
        Graphics2D g = ImagenÁrea.createGraphics();
        if (!ConservarPixeles) {
            g.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );
        }
        Dupla pos = Dupla.Alinear(
                ImagenÁrea, new Dupla(ImagenRelleno).Escalar(Proporción), AlinH, AlinV
        );
        g.drawImage(
                ImagenRelleno,
                new AffineTransform(
                        Proporción, 0,
                        0, Proporción,
                        pos.intX(), pos.intY()
                ), null
        );
        return new PropiedadAjuste(pos, new Dupla(Proporción));
    }

    public static PropiedadAjuste Ajuste_Centrado(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {
        if (ImagenRelleno == null || ImagenÁrea == null) {
            return null;
        }
        Dupla pos = Dupla.Alinear(ImagenÁrea, ImagenRelleno, Dupla.MEDIO, Dupla.MEDIO);
        ImagenÁrea.getGraphics().drawImage(ImagenRelleno, pos.intX(), pos.intY(), null);
        return new PropiedadAjuste(pos, Dupla.IDENTIDAD);
    }

    public static PropiedadAjuste Ajuste_Centrado_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {
        if (ImagenRelleno == null || ImagenÁrea == null) {
            return null;
        }
        if (ImagenRelleno.getWidth() > ImagenÁrea.getWidth() || ImagenRelleno.getHeight() > ImagenÁrea.getHeight()) {
            return Ajuste_Ajustar(ImagenRelleno, ImagenÁrea);
        }
        return Ajuste_Centrado(ImagenRelleno, ImagenÁrea);
    }

    public static PropiedadAjuste Ajuste_Expandir(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {
        return Ajuste_Expandir(ImagenRelleno, ImagenÁrea, false);
    }

    public static PropiedadAjuste Ajuste_Expandir(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, boolean ConservarPixeles) {
        if (ImagenRelleno == null || ImagenÁrea == null) {
            return null;
        } else if (new Dupla(ImagenRelleno).esIgual(ImagenÁrea)) {
            return Ajuste_Ninguno(ImagenRelleno, ImagenÁrea);
        }
        ImagenÁrea.getGraphics().drawImage(
                Escalar(ImagenRelleno, ImagenÁrea.getWidth(), ImagenÁrea.getHeight(), ConservarPixeles), 0, 0, null
        );
        return new PropiedadAjuste(Dupla.ORIGEN, new Dupla(ImagenÁrea).Dividir(ImagenRelleno));
    }

    public static PropiedadAjuste Ajuste_Mosaico(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {
        return Ajuste_Mosaico(ImagenRelleno, ImagenÁrea, 1);
    }

    public static PropiedadAjuste Ajuste_Mosaico(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, double escala) {
        if (ImagenRelleno == null || ImagenÁrea == null) {
            return null;
        }
        Graphics2D g = ImagenÁrea.createGraphics();
        g.setPaint(
                new TexturePaint(
                        ImagenRelleno,
                        new Rectangle2D.Double(0, 0, ImagenRelleno.getWidth() * escala, ImagenRelleno.getHeight() * escala)
                )
        );
        g.fillRect(0, 0, ImagenÁrea.getWidth(), ImagenÁrea.getHeight());
        return PropiedadAjuste.NO_SUFRE_CAMBIOS;
    }

    /*
    
    FIN DE LOS AJUSTES
    --------------------------------------------------------------------------------------------
     
     */
    public static BufferedImage ReflejarHorizontalVertical(BufferedImage Imagen) {
        if (Imagen == null) {
            return null;
        }
        AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
        tx.translate(-Imagen.getWidth(), -Imagen.getHeight());
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(Imagen, null);
    }

    public static BufferedImage ReflejarHorizontal(BufferedImage Imagen) {
        if (Imagen == null) {
            return null;
        }
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-Imagen.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(Imagen, null);
    }

    public static BufferedImage ReflejarVertical(BufferedImage Imagen) {
        if (Imagen == null) {
            return null;
        }
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -Imagen.getHeight());
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(Imagen, null);
    }

    public static BufferedImage Clonar(BufferedImage Imagen) {
        if (Imagen == null) {
            return null;
        }
        BufferedImage Clon = new BufferedImage(
                Imagen.getWidth(), Imagen.getHeight(), BufferedImage.TYPE_INT_ARGB
        );
        Clon.getGraphics().drawImage(Imagen, 0, 0, null);
        return Clon;
    }

    public static BufferedImage Escalar(BufferedImage imagen, int AnchoNuevo, int AltoNuevo, boolean ConservarPixeles) {
        if (AnchoNuevo <= 0 && AltoNuevo <= 0 ) {
            throw new Error("Debe al menos haber una dimensión de refererncia");
        }
        Dupla Proporcion = new Dupla(AnchoNuevo, AltoNuevo).Dividir(imagen);
        if (AnchoNuevo <= 0) {
            Proporcion.ReemplazarX(Proporcion.Y);
        }
        if (AltoNuevo <= 0) {
            Proporcion.ReemplazarX(Proporcion.X);
        }
        return Escalar(imagen, Proporcion.X, Proporcion.Y, ConservarPixeles);
    }

    public static BufferedImage Escalar(BufferedImage imagen, double porcentaje, boolean ConservarPixeles) {
        return Escalar(imagen, porcentaje, porcentaje, ConservarPixeles);
    }

    public static BufferedImage Escalar(BufferedImage Imagen, double pX, double pY, boolean ConservarPixeles) {
        if (Imagen == null) {
            return null;
        }
        return new AffineTransformOp(
                AffineTransform.getScaleInstance(pX, pY),
                ConservarPixeles ? AffineTransformOp.TYPE_NEAREST_NEIGHBOR : AffineTransformOp.TYPE_BILINEAR
        ).filter(Imagen, null);
    }

    public static BufferedImage EscalarGranCalidad(BufferedImage img, int w, int h) {
        if (img == null) {
            return null;
        }
        Image i = new ImageIcon(img).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage Retorno = Dupla.convBufferedImage(i);
        Retorno.createGraphics().drawImage(i, 0, 0, null);
        return Retorno;
    }

    public static BufferedImage Rotar90D(BufferedImage Imagen) {
        if (Imagen == null) {
            return null;
        }
        return new AffineTransformOp(
                new AffineTransform(
                        0, 1,
                        -1, 0,
                        Imagen.getHeight(), 0
                ),
                AffineTransformOp.TYPE_BILINEAR
        ).filter(Imagen, new Dupla(Imagen).Invertir_XYaYX().convBufferedImage());
    }

    public static BufferedImage Rotar90I(BufferedImage Imagen) {
        if (Imagen == null) {
            return null;
        }
        return new AffineTransformOp(
                new AffineTransform(
                        0, -1,
                        1, 0,
                        0, Imagen.getWidth()
                ),
                AffineTransformOp.TYPE_BILINEAR
        ).filter(Imagen, new Dupla(Imagen).Invertir_XYaYX().convBufferedImage());
    }

}
