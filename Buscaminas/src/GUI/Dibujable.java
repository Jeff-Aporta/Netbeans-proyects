package GUI;

import static Escenarios.EscenarioInteractivo.DegradadoVertical;
import static HerramientaDeImagen.Filtros_Lineales.AJUSTE_CENTRADO_AJUSTAR;
import static HerramientaDeImagen.Filtros_Lineales.AJUSTE_EXPANDIR;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.Filtros_Lineales.Clonar;
import HerramientaDeImagen.GeneraciónDeTexto;
import HerramientasMatemáticas.Dupla;
import HerramientasMatemáticas.Matemática;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import HerramientaDeImagen.LectoEscritor_Imagenes;
import static HerramientaDeImagen.LectoEscritor_Imagenes.cargarImagen;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.LectoEscritor_Imagenes.cargarImagen;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.LectoEscritor_Imagenes.cargarImagen;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.LectoEscritor_Imagenes.cargarImagen;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.LectoEscritor_Imagenes.cargarImagen;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.LectoEscritor_Imagenes.cargarImagen;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.LectoEscritor_Imagenes.cargarImagen;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.Filtros_Lineales.Ajuste_Personalizado;
import static HerramientaDeImagen.LectoEscritor_Imagenes.cargarImagen;

public interface Dibujable {

    void Dibujar(Graphics2D g);

    /*
     * Este método es únicamente algo auxiliar que en este proyecto (Buscaminas)
     * se usa para dibujar las alertas de sistemas de marcas inconsistentes
     */
    default void DibujadoExtra(Graphics2D g) {
    }

    static BufferedImage Icono(String texto, int TamañoFuente, Dupla Tamaño, Color color, boolean Redondear, int valRedondeo) {
        float HSB[] = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        Color colorSombra = new Color(Color.HSBtoRGB(HSB[0], HSB[1], HSB[2] - .3f));

        BufferedImage texto_Img = new GeneraciónDeTexto()
                .ModificarTamañoFuente(TamañoFuente)
                .Borde(new Color(14,64,39), 8)
                .ColorFuente(Color.WHITE)
                .AtributoNegrita()
                .AjustarEscala_ImagenTextura_AlturaDelTexto()
                .GenerarTexto(texto);
        BufferedImage botón = Tamaño.convBufferedImage();
        Graphics2D g = botón.createGraphics();
        if (Redondear) {
            g.fillRoundRect(0, 0, botón.getWidth(), botón.getHeight(), 70, 70);
        } else {
            g.fillRect(0, 0, botón.getWidth(), botón.getHeight());
        }
        BufferedImage Degradado = Tamaño.convBufferedImage();
        Ajuste_Personalizado(
                DegradadoVertical(
                        botón.getHeight(),
                        color,
                        colorSombra
                ),
                Degradado, AJUSTE_EXPANDIR
        );
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN));
        g.drawImage(Degradado, 0, 0, null);
        Ajuste_Personalizado(
                texto_Img, botón, AJUSTE_CENTRADO_AJUSTAR
        );
        return botón;
    }

    static BufferedImage Icono(String texto, Dupla Dimensión) {
        BufferedImage botón = Dimensión.convBufferedImage();
        Ajuste_Personalizado(
                DegradadoVertical(botón.getHeight(), new Color(0, 91, 141), new Color(0, 51, 101)), botón, AJUSTE_EXPANDIR);
        Ajuste_Personalizado(
                new GeneraciónDeTexto()
                .ColorFuente(Color.WHITE)
                .GenerarTexto(texto),
                botón, AJUSTE_CENTRADO_AJUSTAR
        );
        return botón;
    }

    //Usado para el botón de reiniciar tablero, y los + y - de los incrementadores
    static BufferedImage[] MapaBotónIcono_Sombra(BufferedImage icono) {
        return new BufferedImage[]{
            IconoInclinadoEnColor(Color.WHITE, icono),
            IconoInclinadoEnColor(new Color(0, 0, 0, 0), icono),
            IconoInclinadoEnColor(Color.GRAY, icono)
        };
    }

    static BufferedImage IconoInclinadoEnColor(Color sombra, BufferedImage icono) {
        BufferedImage imagen = Clonar(icono);
        Graphics2D g = imagen.createGraphics();
        g.setColor(sombra);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, .3f));
        g.fillRect(0, 0, imagen.getWidth(), imagen.getHeight());
        return imagen;
    }

    static BufferedImage ImagenTextualConIcono(
            String Texto, Color color, int TamañoFuente, int separación_TextoIcono, BufferedImage icono
    ) {
        return new GeneraciónDeTexto()
                .ModificarFuente(new Font("calibri", Font.BOLD, TamañoFuente))
                .DesfaseVertical(0)
                .ColorFuente(color)
                .IconoIzquierdaTexto(
                        Texto, separación_TextoIcono,
                        icono
                );
    }

    static BufferedImage ImagenRedSocial(String Texto, Color color, BufferedImage icono) {
        return ImagenTextualConIcono(Texto, color, 14, 10, icono);
    }

    static BufferedImage[] MapaImagenesInteractivas_RedesSociales(String Texto, BufferedImage icono) {
        return new BufferedImage[]{
            ImagenRedSocial(Texto, Color.WHITE, icono),
            ImagenRedSocial(Texto, new Color(1, 1, 1, .5f), icono),
            ImagenRedSocial(Texto, Color.YELLOW, icono)
        };
    }

    /*
     * Utilizados para los botones del menú:
     * nuevo juego,
     * mejores puntajes
     * configuración
     */
    static BufferedImage ImagenTextual(String Texto, Color color) {
        return new GeneraciónDeTexto().ColorFuente(color).GenerarTexto(Texto);
    }

    static BufferedImage[] MapaImagenesInteractivas_Textuales(String Texto) {
        return new BufferedImage[]{
            ImagenTextual(Texto, Color.WHITE),
            ImagenTextual(Texto, new Color(1, 1, 1, .5f)),
            ImagenTextual(Texto, Color.YELLOW)
        };
    }

    static interface EscuchadorMouse_DeslizamientoScroll {

        public void EscuchadorDeDeslizamientoDeScroll(MouseWheelEvent me);
    }

    static interface EscuchadorMouse_PresiónBotones {

        void EscuchadorDePresiónDeBotones(MouseEvent me);
    }

    static interface EscuchadorMouse extends EscuchadorMouse_DeslizamientoScroll, EscuchadorMouse_PresiónBotones {

        default void EscuchadorMouse(Object me) {
            if (me instanceof MouseWheelEvent) {
                EscuchadorDeDeslizamientoDeScroll((MouseWheelEvent) me);
            } else if (me instanceof MouseEvent) {
                EscuchadorDePresiónDeBotones((MouseEvent) me);
            }
        }
    }

    static abstract class ObjetoDibujable implements Dibujable, EscuchadorMouse {

        public Dupla Posición = Dupla.ORIGEN;
        public Dupla Dimensión = Dupla.IDENTIDAD;

        public ObjetoDibujable CambiarPosición(double x, double y) {
            return CambiarPosición(new Dupla(x, y));
        }

        public ObjetoDibujable CambiarPosición(Dupla d) {
            Posición = d;
            return this;
        }

        public boolean PuntoDentro(Dupla punto) {
            return new Matemática.Rango2D(
                    Posición.X, Posición.X + Dimensión.X,
                    Posición.Y, Posición.Y + Dimensión.Y
            ).estáDentro(punto.convVector());
        }
    }

}
