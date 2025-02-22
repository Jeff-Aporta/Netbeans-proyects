package Animacion;

import HerramientasMatemáticas.Dupla;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class ObjetoDibujable {

    public static float FPS = 15;
    public static Dupla Escenario = new Dupla(1280, 720).Proteger();

    public Dupla posición = new Dupla();
    public Dupla Escala = new Dupla(1);

    public void Dibujar(Graphics2D g) {
        g.setTransform(ObtenerAffineTransform());
        g.drawImage(getSkin(), 0, 0, null);
    }

    public abstract BufferedImage getSkin();

    public AffineTransform ObtenerAffineTransform() {
        AffineTransform at = new AffineTransform();
        at.translate(posición.X, posición.Y);
        at.scale(Escala.X, Escala.Y);
        return at;
    }
}
