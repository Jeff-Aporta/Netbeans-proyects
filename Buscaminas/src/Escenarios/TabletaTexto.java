/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Escenarios;

import GUI.BotónMina;
import GUI.Dibujable;
import GUI.Imagen;
import static HerramientaDeImagen.Filtros_Lineales.Clonar;
import HerramientaDeImagen.GeneraciónDeTexto;
import HerramientasMatemáticas.Dupla;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

public class TabletaTexto extends Imagen {

    private BufferedImage imagenFondo;
    private String Texto = "";

    private Color colorTexto;

    public TabletaTexto(Color colorFondo, Color colorMarco, Color colorTexto, String TextoInicial) {
        super(null);
        Dimensión = new Dupla(3 * BotónMina.LADO, 1.25 * BotónMina.LADO);
        imagenFondo = ImagenFondo(colorFondo, colorMarco);
        this.colorTexto = colorTexto;
        Texto = TextoInicial;
        CalcularImagenDibujar();
    }

    void CambiarTexto(String texto) {
        Texto = texto;
        CalcularImagenDibujar();
    }

    void CambiarColorTexto(Color color) {
        colorTexto = color;
        CalcularImagenDibujar();
    }

    void CalcularImagenDibujar() {
        CambiarImagen(new GeneraciónDeTexto()
                .ColorFuente(colorTexto)
                .DibujarTextoCentradoEnImagen(
                        Texto, Clonar(imagenFondo)
                )
        );
    }

    void CambiarColoresFondo(Color colorFondo, Color colorMarco) {
        imagenFondo = ImagenFondo(colorFondo, colorMarco);
        CalcularImagenDibujar();
    }

    BufferedImage ImagenFondo(Color colorFondo, Color colorMarco) {
        return new BufferedImage(Dimensión.Ancho(), Dimensión.Alto(), 2) {
            {
                Graphics2D g = createGraphics();
                g.setColor(colorFondo);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(colorMarco);
                g.setStroke(new BasicStroke(2));
                g.drawRect(1, 0, Dimensión.Ancho() - 1, Dimensión.Alto() - 1);
            }
        };
    }

    @Override
    public void EscuchadorDeDeslizamientoDeScroll(MouseWheelEvent me) {
    }

    @Override
    public void EscuchadorDePresiónDeBotones(MouseEvent me) {
    }
}
