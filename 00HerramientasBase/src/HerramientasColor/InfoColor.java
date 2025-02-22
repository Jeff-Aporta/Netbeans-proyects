package HerramientasColor;

import HerramientaDeImagen.Filtros_Lineales;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class InfoColor {

    public static void main(String[] args) {
//            Color color = Color.
    }

    public static float Tono(Color c) {
        float[] HSB = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        return HSB[0];
    }

    public static float Saturación_HSB(Color c) {
        float[] HSB = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        return HSB[1];
    }

    public static float Brillo(Color c) {
        float[] HSB = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        return HSB[2];
    }

    public static float Saturación_HSL(Color c) {
        float[] HSL = ConversorModelosColor.RGBtoHSL(c.getRed(), c.getGreen(), c.getBlue());
        return HSL[1];
    }

    public static float Luminosidad(Color c) {
        float[] HSL = ConversorModelosColor.RGBtoHSL(c.getRed(), c.getGreen(), c.getBlue());
        return HSL[2];
    }

    public static Color ColorPrincipal(BufferedImage imagen) {
        return new Color(Filtros_Lineales.EscalarGranCalidad(imagen, 1, 1).getRGB(0, 0), true);
    }

}
