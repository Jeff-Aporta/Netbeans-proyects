package GUI;

import static HerramientaDeImagen.Filtros_Lineales.Clonar;
import HerramientaDeImagen.GeneraciónDeTexto;
import HerramientasMatemáticas.Dupla;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static HerramientasMatemáticas.Matemática.*;
import static GUI.Dibujable.*;
import static Principal.Principal.*;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.event.MouseWheelEvent;

public abstract class Botón extends ObjetoDibujable implements EscuchadorMouse {

    private static final byte //Identificadores
            EJECUTAR_ACCIÓN_INTERACTIVA = 0,
            EJECUTAR_ACCIÓN_NO_INTERACTIVA = 1,
            EJECUTAR_SCROLL_ACCIÓN_INTERACTIVA = 2,
            EJECUTAR_SCROLL_ACCIÓN_NO_INTERACTIVA = 3;

    private static MouseEvent PressEvent;
    private static MouseWheelEvent WheelEvent;

    final static byte MOUSE_ENCIMA = 0, INACTIVO = 1, PRESIONADO = 2;

    public byte ESTADO = INACTIVO;

    public boolean INTERACTIVO = true;
    protected BufferedImage MouseDentro_Img, MouseFuera_Img, Presionado_Img, ImgDibujar;

    public Botón(BufferedImage... imagenes) {
        Inicializar(imagenes);
    }

    public Botón(BufferedImage imagen) {
        Inicializar(new BufferedImage[]{imagen, imagen, imagen});
    }

    public Botón(BufferedImage MouseEncima, BufferedImage Desactivado, BufferedImage Activado) {
        Inicializar(new BufferedImage[]{MouseEncima, Desactivado, Activado});
    }

    public void Inicializar(BufferedImage... imagenes) {
        for (int i = 0; i < 3; i++) {
            if (imagenes[i] != null) {
                Dimensión = new Dupla(imagenes[i]);
                break;
            }
        }
        CambiarImagenes(imagenes[0], imagenes[1], imagenes[2]);
    }

    public void CambiarImagenes(BufferedImage imagen) {
        CambiarImagenes(imagen, imagen, imagen);
    }

    public void CambiarImagenes(BufferedImage MouseDentro, BufferedImage MouseFuera, BufferedImage MousePresionando) {
        MouseDentro_Img = MouseDentro;
        ImgDibujar = MouseFuera_Img = MouseFuera;
        Dimensión = new Dupla(ImgDibujar);
        Presionado_Img = MousePresionando;
        CalcularFotograma();
    }

    protected void EscuchadorParaCambioDeImágen(Dupla posiciónMouse) {
        if (!INTERACTIVO) {
            if (ESTADO != INACTIVO) {
                ESTADO = INACTIVO;
            } else {
                return;
            }
        } else if (PuntoDentro(ventana.PosiciónCursorEnFotograma())) {
            if (ventana.mouse.ClicIzquierdoPresionado) {
                if (ESTADO == PRESIONADO) {
                    return;
                }
                ESTADO = PRESIONADO;
            } else {
                if (ESTADO == MOUSE_ENCIMA) {
                    return;
                }
                ESTADO = MOUSE_ENCIMA;
            }
        } else {
            if (ESTADO == INACTIVO) {
                return;
            }
            ESTADO = INACTIVO;
        }
        switch (ESTADO) {
            case PRESIONADO:
                ImgDibujar = Presionado_Img;
                break;
            case INACTIVO:
                ImgDibujar = MouseFuera_Img;
                break;
            case MOUSE_ENCIMA:
                ImgDibujar = MouseDentro_Img;
                break;
        }
    }

    @Override
    public void Dibujar(Graphics2D g) {
        Dupla posiciónMouse = ventana.PosiciónCursorEnFotograma();
        EscuchadorParaCambioDeImágen(posiciónMouse);
        if (INTERACTIVO && PuntoDentro(posiciónMouse)) {
            ventana.setCursor(Cursor.HAND_CURSOR);
        }
        g.drawImage(ImgDibujar, Posición.intX(), Posición.intY(), null);
        DibujadoExtra(g);
    }

    @Override
    public void EscuchadorDeDeslizamientoDeScroll(MouseWheelEvent me) {
        if (PuntoDentro(ventana.PosiciónCursorEnFotograma())) {
            WheelEvent = me;
            new AcciónEjecutable().Ejecutar(
                    INTERACTIVO
                            ? EJECUTAR_SCROLL_ACCIÓN_INTERACTIVA
                            : EJECUTAR_SCROLL_ACCIÓN_NO_INTERACTIVA
            );
        }
    }

    @Override
    public void EscuchadorDePresiónDeBotones(MouseEvent me) {
        if (PuntoDentro(ventana.PosiciónCursorEnFotograma())) {
            PressEvent = me;
            new AcciónEjecutable().Ejecutar(
                    INTERACTIVO
                            ? EJECUTAR_ACCIÓN_INTERACTIVA
                            : EJECUTAR_ACCIÓN_NO_INTERACTIVA
            );
        }
    }

    /*
     * Por lo general los botones ejecutan algo al presionarlos, por eso este
     * método es de obligatoria implementación
     */
    protected abstract void Acción_Interactivo(MouseEvent me);

    protected void Acción_No_Interactivo(MouseEvent me) {
    }

    protected void ScrollAcción_Interactivo(MouseWheelEvent me) {
    }

    protected void ScrollAcción_No_Interactivo(MouseWheelEvent me) {
    }

    public class AcciónEjecutable implements Runnable {

        Thread hilo;
        byte INSTRUCCIÓN;

        private AcciónEjecutable() {
            hilo = new Thread(this);
        }

        void Ejecutar(byte Instrucción) {
            INSTRUCCIÓN = Instrucción;
            hilo.start();
            hilo = null;
        }

        @Override
        public void run() {
            switch (INSTRUCCIÓN) {
                case EJECUTAR_ACCIÓN_INTERACTIVA:
                    Acción_Interactivo(PressEvent);
                    break;
                case EJECUTAR_ACCIÓN_NO_INTERACTIVA:
                    Acción_No_Interactivo(PressEvent);
                    break;
                case EJECUTAR_SCROLL_ACCIÓN_INTERACTIVA:
                    ScrollAcción_Interactivo(WheelEvent);
                    break;
                case EJECUTAR_SCROLL_ACCIÓN_NO_INTERACTIVA:
                    ScrollAcción_No_Interactivo(WheelEvent);
                    break;
            }
        }
    }

}
