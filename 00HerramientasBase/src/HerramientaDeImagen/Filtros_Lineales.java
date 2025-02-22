 //Última actualización 23/Julio/2017
package HerramientaDeImagen;

import HerramientaArchivos.LectoEscrituraArchivos;
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

    public static final byte AJUSTE_NINGUNO = 0;
    public static final byte AJUSTE_RELLENAR = 1;
    public static final byte AJUSTE_AJUSTAR = 2;
    public static final byte AJUSTE_EXPANDIR = 3;
    public static final byte AJUSTE_MOSAICO = 4;
    public static final byte AJUSTE_ICONO = AJUSTE_MOSAICO;
    public static final byte AJUSTE_CENTRADO = 6;
    public static final byte AJUSTE_CENTRADO_AJUSTAR = 7;

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
        BufferedImage img = LectoEscrituraArchivos.cargar_imagen(
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

    //<editor-fold defaultstate="collapsed" desc="Sobrecarga de ajuste personalizado »">
    public static PropiedadAjuste Ajuste_Personalizado(BufferedImage imagen, BufferedImage ImagenÁrea, int TIPO_AJUSTE) {
        return Ajuste_Personalizado(imagen, ImagenÁrea, TIPO_AJUSTE, false);
    }
    //</editor-fold>

    public static PropiedadAjuste Ajuste_Personalizado(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, int TIPO_AJUSTE, boolean ConservarPixeles) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        switch (TIPO_AJUSTE) {
            case AJUSTE_RELLENAR:
                return Ajuste_Rellenar(ImagenRelleno, ImagenÁrea, ConservarPixeles);
            case AJUSTE_AJUSTAR:
                return Ajuste_Ajustar(ImagenRelleno, ImagenÁrea, ConservarPixeles);
            case AJUSTE_EXPANDIR:
                return Ajuste_Expandir(ImagenRelleno, ImagenÁrea, ConservarPixeles);
            case AJUSTE_ICONO:
                return Ajuste_Mosaico(ImagenRelleno, ImagenÁrea);
            case AJUSTE_CENTRADO:
                return Ajuste_Centrado(ImagenRelleno, ImagenÁrea);
            case AJUSTE_CENTRADO_AJUSTAR:
                return Ajuste_Centrado_Ajustar(ImagenRelleno, ImagenÁrea, ConservarPixeles);
            default:
                return Ajuste_Ninguno(ImagenRelleno, ImagenÁrea);
        }
    }//</editor-fold>

    public static PropiedadAjuste Ajuste_Ninguno(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        ImagenÁrea.getGraphics().drawImage(ImagenRelleno, 0, 0, null);
        return PropiedadAjuste.NO_SUFRE_CAMBIOS;
    }//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Sobrecarga de ajuste rellenar »">
    public static PropiedadAjuste Ajuste_Rellenar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Ajuste_Rellenar(ImagenRelleno, ImagenÁrea, false);
    }//</editor-fold>

    public static PropiedadAjuste Ajuste_Rellenar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, boolean ConservarPixeles) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Ajuste_Rellenar(ImagenRelleno, ImagenÁrea, Dupla.MEDIO, Dupla.MEDIO, ConservarPixeles);
    }//</editor-fold>
    //</editor-fold>
    
    public static PropiedadAjuste Ajuste_Rellenar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, byte AlinH, byte AlinV, boolean ConservarPixeles) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
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
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sobrecarga de ajuste ajustar »">
    public static PropiedadAjuste Ajuste_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Ajuste_Ajustar(ImagenRelleno, ImagenÁrea, false);
    }//</editor-fold>

    public static PropiedadAjuste Ajuste_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, boolean ConservarPixeles) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Ajuste_Ajustar(ImagenRelleno, ImagenÁrea, Dupla.MEDIO, Dupla.MEDIO, ConservarPixeles);
    }//</editor-fold>

    public static PropiedadAjuste Ajuste_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, boolean ConservarPixeles, boolean alta_calidad) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Ajuste_Ajustar(ImagenRelleno, ImagenÁrea, Dupla.MEDIO, Dupla.MEDIO, ConservarPixeles, alta_calidad);
    }//</editor-fold>

    public static PropiedadAjuste Ajuste_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, byte AlinH, byte AlinV) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Ajuste_Ajustar(ImagenRelleno, ImagenÁrea, AlinH, AlinV, false);
    }//</editor-fold>

    public static PropiedadAjuste Ajuste_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, byte AlinH, byte AlinV, boolean ConservarPixeles) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Ajuste_Ajustar(ImagenRelleno, ImagenÁrea, AlinH, AlinV, ConservarPixeles, false);
    }//</editor-fold>
    //</editor-fold>
    
    public static PropiedadAjuste Ajuste_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, byte AlinH, byte AlinV, boolean ConservarPixeles, boolean alta_calidad) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
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
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
        }
        Dupla pos = Dupla.Alinear(
                ImagenÁrea, new Dupla(ImagenRelleno).Escalar(Proporción), AlinH, AlinV
        );
        if (alta_calidad) {
            ImagenRelleno = EscalarGranCalidad(
                    ImagenRelleno,
                    (int) (ImagenRelleno.getWidth() * Proporción),
                    (int) (ImagenRelleno.getHeight() * Proporción)
            );
            g.drawImage(
                    ImagenRelleno,
                    pos.intX(),
                    pos.intY(),
                    null
            );
        } else {
            g.drawImage(
                    ImagenRelleno,
                    new AffineTransform(
                            Proporción, 0,
                            0, Proporción,
                            pos.intX(), pos.intY()
                    ), null
            );
        }
        return new PropiedadAjuste(pos, new Dupla(Proporción));
    }//</editor-fold>

    public static PropiedadAjuste Ajuste_Alinear(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, byte Al_H, byte Al_V) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        if (ImagenRelleno == null || ImagenÁrea == null) {
            return null;
        }
        Dupla pos = Dupla.Alinear(ImagenÁrea, ImagenRelleno, Al_H, Al_V);
        ImagenÁrea.getGraphics().drawImage(ImagenRelleno, pos.intX(), pos.intY(), null);
        return new PropiedadAjuste(pos, Dupla.IDENTIDAD);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sobrecarga de ajuste centrado »">
    public static PropiedadAjuste Ajuste_Centrado(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {
        return Ajuste_Centrado(ImagenRelleno, ImagenÁrea, Dupla.MEDIO, Dupla.MEDIO);
    }
    //</editor-fold>
    
    public static PropiedadAjuste Ajuste_Centrado(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, byte AH, byte AV) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        if (ImagenRelleno == null || ImagenÁrea == null) {
            return null;
        }
        Dupla pos = Dupla.Alinear(ImagenÁrea, ImagenRelleno, AH, AV);
        ImagenÁrea.getGraphics().drawImage(ImagenRelleno, pos.intX(), pos.intY(), null);
        return new PropiedadAjuste(pos, Dupla.IDENTIDAD);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sobrecarga de ajuste centrado ajustar »">
    public static PropiedadAjuste Ajuste_Centrado_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Ajuste_Centrado_Ajustar(ImagenRelleno, ImagenÁrea, false);
    }//</editor-fold>

    public static PropiedadAjuste Ajuste_Centrado_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, boolean ConservarPixeles) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Ajuste_Centrado_Ajustar(ImagenRelleno, ImagenÁrea, Dupla.MEDIO, Dupla.MEDIO, ConservarPixeles);
    }//</editor-fold>

    public static PropiedadAjuste Ajuste_Centrado_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, byte AH, byte AV) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Ajuste_Centrado_Ajustar(ImagenRelleno, ImagenÁrea, AH, AV, false);
    }//</editor-fold>
    //</editor-fold>
    
    public static PropiedadAjuste Ajuste_Centrado_Ajustar(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, byte AH, byte AV, boolean ConservarPixeles) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        if (ImagenRelleno == null || ImagenÁrea == null) {
            return null;
        }
        if (ImagenRelleno.getWidth() > ImagenÁrea.getWidth() || ImagenRelleno.getHeight() > ImagenÁrea.getHeight()) {
            return Ajuste_Ajustar(ImagenRelleno, ImagenÁrea, AH, AV, ConservarPixeles);
        }
        return Ajuste_Centrado(ImagenRelleno, ImagenÁrea, AH, AV);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sobrecarga de ajuste expandir »">
    public static PropiedadAjuste Ajuste_Expandir(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {
        return Ajuste_Expandir(ImagenRelleno, ImagenÁrea, false);
    }
    //</editor-fold>
    
    public static PropiedadAjuste Ajuste_Expandir(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, boolean ConservarPixeles) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        if (ImagenRelleno == null || ImagenÁrea == null) {
            return null;
        } else if (new Dupla(ImagenRelleno).esIgual(ImagenÁrea)) {
            return Ajuste_Ninguno(ImagenRelleno, ImagenÁrea);
        }
        ImagenÁrea.getGraphics().drawImage(
                Escalar(ImagenRelleno, ImagenÁrea.getWidth(), ImagenÁrea.getHeight(), ConservarPixeles), 0, 0, null
        );
        return new PropiedadAjuste(Dupla.ORIGEN, new Dupla(ImagenÁrea).Dividir(ImagenRelleno));
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sobrecarga de ajuste mosaico »">
    public static PropiedadAjuste Ajuste_Mosaico(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea) {
        return Ajuste_Mosaico(ImagenRelleno, ImagenÁrea, 1);
    }
    //</editor-fold>
    
    public static PropiedadAjuste Ajuste_Mosaico(BufferedImage ImagenRelleno, BufferedImage ImagenÁrea, double escala) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        if (ImagenRelleno == null || ImagenÁrea == null) {
            return null;
        }
        Graphics2D g = ImagenÁrea.createGraphics();
        g.setPaint(
                new TexturePaint(
                        ImagenRelleno,
                        new Rectangle2D.Double(
                                0, 0,
                                ImagenRelleno.getWidth() * escala, ImagenRelleno.getHeight() * escala
                        )
                )
        );
        g.fillRect(0, 0, ImagenÁrea.getWidth(), ImagenÁrea.getHeight());
        return PropiedadAjuste.NO_SUFRE_CAMBIOS;
    }//</editor-fold>

    /*
    
    FIN DE LOS AJUSTES
    --------------------------------------------------------------------------------------------
     
     */
    public static BufferedImage ReflejarHorizontalVertical(BufferedImage Imagen) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        if (Imagen == null) {
            return null;
        }
        AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
        tx.translate(-Imagen.getWidth(), -Imagen.getHeight());
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(Imagen, null);
    }//</editor-fold>

    public static BufferedImage ReflejarHorizontal(BufferedImage Imagen) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        if (Imagen == null) {
            return null;
        }
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-Imagen.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(Imagen, null);
    }//</editor-fold>

    public static BufferedImage ReflejarVertical(BufferedImage Imagen) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        if (Imagen == null) {
            return null;
        }
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -Imagen.getHeight());
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(Imagen, null);
    }//</editor-fold>

    public static BufferedImage Clonar(BufferedImage Imagen) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        if (Imagen == null) {
            return null;
        }
        BufferedImage Clon = new BufferedImage(
                Imagen.getWidth(), Imagen.getHeight(), BufferedImage.TYPE_INT_ARGB
        );
        Clon.getGraphics().drawImage(Imagen, 0, 0, null);
        return Clon;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sobrecarga de Escalar »">
    public static BufferedImage Escalar(BufferedImage imagen, int AnchoNuevo, int AltoNuevo) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Escalar(imagen, AnchoNuevo, AltoNuevo, true);
    }//</editor-fold>
    
    public static BufferedImage Escalar(BufferedImage imagen, int AnchoNuevo, int AltoNuevo, boolean ConservarPixeles) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        if (AnchoNuevo <= 0 && AltoNuevo <= 0) {
            throw new Error("Debe al menos haber una dimensión de refererncia");
        }
        Dupla Proporcion = new Dupla(AnchoNuevo, AltoNuevo).Dividir(imagen);
        if (AnchoNuevo <= 0) {
            Proporcion.ReemplazarX(Proporcion.Y);
        }
        if (AltoNuevo <= 0) {
            Proporcion.ReemplazarY(Proporcion.X);
        }
        return Escalar(imagen, Proporcion.X, Proporcion.Y, ConservarPixeles);
    }//</editor-fold>
    
    public static BufferedImage Escalar(BufferedImage imagen, double porcentaje) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Escalar(imagen, porcentaje, true);
    }//</editor-fold>
    
    public static BufferedImage Escalar(BufferedImage imagen, double porcentaje, boolean ConservarPixeles) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Escalar(imagen, porcentaje, porcentaje, ConservarPixeles);
    }//</editor-fold>
    
    public static BufferedImage Escalar(BufferedImage Imagen, double pX, double pY) {//<editor-fold defaultstate="collapsed" desc="implementación de sobrecarga »">
        return Escalar(Imagen, pX, pY, true);
    }//</editor-fold>
    //</editor-fold>
    
    public static BufferedImage Escalar(BufferedImage Imagen, double pX, double pY, boolean ConservarPixeles) {
        if (Imagen == null) {
            return null;
        }
        return new AffineTransformOp(
                AffineTransform.getScaleInstance(pX, pY),
                ConservarPixeles ? AffineTransformOp.TYPE_NEAREST_NEIGHBOR : AffineTransformOp.TYPE_BILINEAR
        ).filter(Imagen, new Dupla(Imagen).Escalar(pX, pY).convBufferedImage());
    }

    public static BufferedImage EscalarGranCalidad(BufferedImage img, int w, int h) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        if (img == null) {
            return null;
        }
        Image i = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage Retorno = Dupla.convBufferedImage(i);
        Retorno.createGraphics().drawImage(i, 0, 0, null);
        return Retorno;
    }//</editor-fold>

    public static BufferedImage Rotar90D(BufferedImage Imagen) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
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
    }//</editor-fold>

    public static BufferedImage Rotar90I(BufferedImage Imagen) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
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
    }//</editor-fold>

}
