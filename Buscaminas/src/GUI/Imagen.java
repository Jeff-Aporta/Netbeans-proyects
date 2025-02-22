package GUI;

import static GUI.Dibujable.*;
import static HerramientaDeImagen.ModoFusión.*;
import HerramientasMatemáticas.Dupla;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

public class Imagen extends ObjetoDibujable {

    public BufferedImage imagenDibujar;

    public Imagen(BufferedImage imagenDibujar) {
        CambiarImagen(imagenDibujar);
        Dimensión = new Dupla(imagenDibujar);
    }

    public void CambiarImagen(BufferedImage imagen) {
        imagenDibujar = imagen;
    }

    @Override
    public void Dibujar(Graphics2D g) {
        g.drawImage(imagenDibujar, Posición.intX(), Posición.intY(), null);
    }

    @Override
    public void EscuchadorDeDeslizamientoDeScroll(MouseWheelEvent me) {
    }

    @Override
    public void EscuchadorDePresiónDeBotones(MouseEvent me) {
    }

}
