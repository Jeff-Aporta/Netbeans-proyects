package HerramientaDeImagen.ModoFusión;

import HerramientaArchivos.BibliotecaArchivos;
import HerramientaDeImagen.Filtros_Lineales;
import HerramientasGUI.Presentador;
import HerramientasMatemáticas.Dupla;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;

public class GUI_Aux extends JFrame {

    Presentador PresentadorEscenario;
    Presentador PresentadorPatrónFondo;
    Presentador PresentadorCapaSuperior;
    Presentador PresentadorCapaInferior;
    Fotograma Escenario;
    BaseModoFusión ModoFusión;
    
    ListaID ListaID = new ListaID();

    void ActualizarEscenario() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            PresentadorEscenario.ActualizarFotograma(Escenario.fotograma());
        } catch (Exception e) {
        }
    }//</editor-fold>

    public static class ListaID extends ArrayList<ID> {//<editor-fold defaultstate="collapsed" desc="Código de la clase">

        public void add(String nombre, byte ID) {
            add(new ID(nombre, ID));
        }

        public void add(String nombre) {
            add(new ID(nombre));
        }

        String[] obtenerListaNombres() {
            String[] retorno = new String[size()];
            for (int j = 0; j < this.size(); j++) {
                retorno[j] = get(j).nombre;
            }
            return retorno;
        }

        byte ObtenerID(String n) {
            for (ID id : this) {
                if (id.nombre.equals(n)) {
                    return id.ID;
                }
            }
            throw new RuntimeException("No se encontró el identificador con nombre: " + n);
        }
    }//</editor-fold>

    public static class ID {//<editor-fold defaultstate="collapsed" desc="Código de la clase">

        String nombre;
        byte ID;

        public ID(String nombre) {
            this.nombre = nombre;
        }

        public ID(String nombre, byte ID) {
            this.nombre = nombre;
            this.ID = ID;
        }
    }//</editor-fold>

    public class Fotograma {//<editor-fold defaultstate="collapsed" desc="Código de la clase">

        //<editor-fold defaultstate="collapsed" desc="Variables ●">
        Capa capaSuperior;
        Capa capaInferior;
        //</editor-fold>

        public Fotograma() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            PresentadorEscenario.setText("Cargando imagenes");
            PresentadorCapaSuperior.setText("Cargando imagenes");
            PresentadorCapaInferior.setText("Cargando imagenes");
            ModificarCapas(BibliotecaArchivos.Imagenes.Test.IMG1(),
                    BibliotecaArchivos.Imagenes.Test.IMG2()
            );
            PresentadorEscenario.setText("");
            PresentadorCapaSuperior.setText("");
            PresentadorCapaInferior.setText("");
        }//</editor-fold>

        void ModificarCapas(BufferedImage Superior, BufferedImage Inferior) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            if (Superior != null) {
                capaSuperior = new Capa(Superior);
                PresentadorCapaSuperior.ActualizarFotograma(Superior);
            }
            if (Inferior != null) {
                capaInferior = new Capa(Inferior);
                PresentadorCapaInferior.ActualizarFotograma(Inferior);
            }
        }//</editor-fold>

        void IntercambiarCapas() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            ModificarCapas(capaInferior.imagenDibujar, capaSuperior.imagenDibujar);
            centrarCapas();
            ActualizarEscenario();
        }//</editor-fold>

        void centrarCapas() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            capaSuperior.Centrar();
            capaInferior.Centrar();
            ActualizarEscenario();
        }//</editor-fold>

        void AjustarCapasAlEscenario() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            capaSuperior.AjustarAlEscenario();
            capaInferior.AjustarAlEscenario();
            centrarCapas();
        }//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Herramientas para el movimiento con el mouse »">
        void BuscarActivación() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            capaSuperior.BuscarActivación();
            if (capaSuperior.mover) {
                return;
            }
            capaInferior.BuscarActivación();
        }//</editor-fold>

        void Liberar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            capaSuperior.mover = false;
            capaInferior.mover = false;
        }//</editor-fold>

        void Arrastrar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            capaSuperior.Arrastrar();
            capaInferior.Arrastrar();
            ActualizarEscenario();
        }//</editor-fold>
        //</editor-fold>

        BufferedImage fotograma() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            BufferedImage retorno = Dupla.convBufferedImage(PresentadorEscenario);
            Graphics2D g = retorno.createGraphics();
            //<editor-fold defaultstate="collapsed" desc="Instrucciones de dibujado">
            capaInferior.Dibujar(g);
            //<editor-fold defaultstate="collapsed" desc="Modificación del composite">
            if (ModoFusión != null) {
                g.setComposite(ModoFusión);
            }
            //</editor-fold>
            capaSuperior.Dibujar(g);
            //</editor-fold>
            return retorno;
        }//</editor-fold>

        public class Capa {//<editor-fold defaultstate="collapsed" desc="Código de la clase  »">

            //<editor-fold defaultstate="collapsed" desc="Variables »">
            //<editor-fold defaultstate="collapsed" desc="Para la interacción con el Mouse ●">
            boolean mover = false;
            Dupla XY;
            Rectangle2D zonaActividad;
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Para el dibujado ●">
            BufferedImage imagenEntrada;
            BufferedImage imagenDibujar;
            AffineTransform at;
            //</editor-fold>
            //</editor-fold>

            public Capa(BufferedImage imagen) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
                imagenEntrada = imagen;
                AjustarAlEscenario();
            }//</editor-fold>

            void AjustarAlEscenario() {
                {//<editor-fold defaultstate="collapsed" desc="Ajuste de redimensión para el escenarios">
                    int Lado = Math.min(PresentadorEscenario.getWidth(), PresentadorEscenario.getHeight()) - 20;
                    if (Lado < 1) {
                        Lado = 1;
                    }
                    imagenDibujar = Filtros_Lineales.Escalar(imagenEntrada, Lado, -1);
                    if (imagenDibujar.getHeight() > Lado) {
                        imagenDibujar = Filtros_Lineales.Escalar(imagenEntrada, -1, Lado);
                    }
                }//</editor-fold>
                zonaActividad = new Rectangle.Double(0, 0, imagenDibujar.getWidth(), imagenDibujar.getHeight());
                at = AffineTransform.getTranslateInstance(zonaActividad.getX(), zonaActividad.getY());
            }

            public void Centrar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
                Dupla p = Dupla.Alinear(PresentadorEscenario, imagenDibujar, Dupla.MEDIO, Dupla.MEDIO);
                ModificarPosición(p.X, p.Y);
            }//</editor-fold>

            public void Dibujar(Graphics2D g) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
                g.drawImage(imagenDibujar, at, null);
                g.setComposite(AlphaComposite.SrcOver);
                g.setColor(Color.WHITE);
                g.draw(zonaActividad);
            }//</editor-fold>

            public void BuscarActivación() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
                if (zonaActividad.contains(PresentadorEscenario.PosiciónCursorEnFotograma().convPoint2D())) {
                    mover = true;
                }
                XY = PresentadorEscenario.PosiciónCursorEnFotograma().Sustraer(zonaActividad);
            }//</editor-fold>

            public void Arrastrar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
                if (!mover) {
                    return;
                }
                Dupla pos = PresentadorEscenario.PosiciónCursorEnFotograma().Sustraer(XY);
                ModificarPosición(pos.X, pos.Y);
            }//</editor-fold>

            public void ModificarPosición(double x, double y) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
                zonaActividad.setRect(x, y, zonaActividad.getWidth(), zonaActividad.getHeight());
                at = AffineTransform.getTranslateInstance(zonaActividad.getX(), zonaActividad.getY());
            }//</editor-fold>
        }//</editor-fold>
    }//</editor-fold>
}
