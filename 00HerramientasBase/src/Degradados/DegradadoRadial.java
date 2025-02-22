package Degradados;

import HerramientasGUI.VentanaGráfica;
import HerramientasMatemáticas.Dupla;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class DegradadoRadial extends DegradadoPolar {

    public static void main(String[] args) {
        int ancho = 1280;
        int alto = 720;
        MapaDegradado mapaDegradado = new MapaDegradado(
                new Color(100, 200, 240),
                new Color(20, 120, 255),
                new Color(60, 0, 90),
                new Color(60, 0, 0)
        );
        Dupla p1 = new Dupla(ancho / 2, alto / 2);
        Dupla p2 = new Dupla(p1.X + 100, p1.Y);
        DegradadoRadial degradadoRadial = new DegradadoRadial(
                p1, p2, mapaDegradado, AjusteRango.CÍCLICO
        );
        BufferedImage fotograma = new BufferedImage(ancho, alto, 2);
        {
            Graphics2D g = fotograma.createGraphics();
            g.setPaint(degradadoRadial);
            g.fillRect(0, 0, ancho, alto);
            g.setColor(Color.WHITE);
            g.drawLine(p1.intX(), p1.intY(), p2.intX(), p2.intY());
        }
        VentanaGráfica v = new VentanaGráfica("Prueba");
        v.ActualizarFotograma(fotograma);
        v.CambiarTamaño(VentanaGráfica.DIMENSIÓN_FOTOGRAMA, true);
        v.AjustarProporcionalidadRedimensionamiento_FotogramaActual();
    }

    public DegradadoRadial(Dupla puntoLlegada, MapaDegradado mapaDegradado) {
        super(1, new Dupla(), puntoLlegada, mapaDegradado);
    }

    public DegradadoRadial(Dupla puntoLlegada, MapaDegradado mapaDegradado, AjusteRango ajusteRango) {
        super(1, new Dupla(), puntoLlegada, mapaDegradado, ajusteRango);
    }

    public DegradadoRadial(Dupla puntoPartida, Dupla puntoLlegada, MapaDegradado mapaDegradado) {
        super(1, puntoPartida, puntoLlegada, mapaDegradado, AjusteRango.LINEAL);
    }

    public DegradadoRadial(Dupla puntoPartida, Dupla puntoLlegada, MapaDegradado mapaDegradado, AjusteRango ajusteRango) {
        super(1, puntoPartida, puntoLlegada, mapaDegradado, ajusteRango);
    }
}
