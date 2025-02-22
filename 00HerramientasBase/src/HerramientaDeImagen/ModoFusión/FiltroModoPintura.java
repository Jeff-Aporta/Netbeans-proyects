package HerramientaDeImagen.ModoFusión;

import HerramientaDeImagen.ModoFusión.ModoPintura.BaseModoPintura;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public final class FiltroModoPintura {

    public static BaseModoPintura Brillo(double porcentaje) {
        return new ModoPintura.ConVariableDeAplicación.EquilibrarHSB() {
            {
                Aplicación3 += (porcentaje * .5);
            }
        };
    }

    public static BaseModoPintura Monocromo(Color color) {
        return new ModoPintura.ConVariableDeAplicación.Monocromatizar(color);
    }

    public static BufferedImage Aplicar(BufferedImage img, ModoPintura.BaseModoPintura... p) {
        BufferedImage retorno = new BufferedImage(img.getWidth(), img.getHeight(), 2);
        Graphics2D g = retorno.createGraphics();
        g.setComposite(new ModoPinturaCompuesto(p));
        g.drawImage(img, 0, 0, null);
        return retorno;
    }

}
