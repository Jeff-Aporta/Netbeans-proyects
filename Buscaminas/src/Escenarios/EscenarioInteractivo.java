package Escenarios;

import GUI.GrupoDibujable;
import HerramientasMatemáticas.Dupla;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Desktop;
import java.net.URI;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.event.MouseWheelEvent;

public abstract class EscenarioInteractivo {

    static GrupoDibujable ElementosDibujables = new GrupoDibujable();

    public abstract void Cargar();

    public abstract void Deponer();

    public BufferedImage Fotograma() {
        try {
            BufferedImage fotograma = DimensiónFotograma().convBufferedImage();
            Graphics2D g = fotograma.createGraphics();
            ElementosDibujables.Dibujar(g);
            return fotograma;
        } catch (Exception e) {
            return null;
        }
    }

    public abstract Dupla DimensiónFotograma();

    public void buscarAcciónBotones_Presión(MouseEvent me) {
        ElementosDibujables.EscuchadorMouse(me);
    }

    public void buscarAcciónBotones_DeslizarScroll(MouseWheelEvent me) {
    }

    static final void AbrirPáginaWeb(String URL) {
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                Desktop dk = Desktop.getDesktop();
                dk.browse(new URI(URL));
            } catch (Exception e) {
                System.out.println("Error al abrir URL: " + URL + e.getMessage());
            }
        }
    }

    public static BufferedImage Fondo() {
        return DegradadoVertical(
                900,
                new Color(14, 17, 0),
                new Color(139, 173, 0)
        );
    }

    public static BufferedImage DegradadoVertical(int alto, Color colorSuperior, Color ColorInferior) {
        BufferedImage img = new Dupla(1, alto).convBufferedImage();
        Graphics2D g = img.createGraphics();
        g.setPaint(new GradientPaint(0, 0, colorSuperior, 0, alto, ColorInferior));
        g.fillRect(0, 0, 1, alto);
        return img;
    }

}
