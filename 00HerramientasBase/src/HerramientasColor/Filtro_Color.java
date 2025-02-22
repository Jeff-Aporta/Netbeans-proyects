package HerramientasColor;

import HerramientaArchivos.BibliotecaArchivos;
import HerramientasGUI.VentanaGráfica;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;

public class Filtro_Color {

    public static void main(String[] args) {
        VentanaGráfica v = new VentanaGráfica();
        BufferedImage img = BibliotecaArchivos.Imagenes.Test.PNG_1();
        img = Brillo(img, -.5);
        v.CambiarModoFondo(VentanaGráfica.FONDO_TRANSPARENTE);
        v.ActualizarFotograma(img);
    }

    public static BufferedImage Binarizar(BufferedImage imagen) {
        return Binarizar(imagen, null);
    }

    public static BufferedImage Binarizar(BufferedImage imagen, double... aplicarCanal) {
        return Discretizar(imagen, 2, aplicarCanal);
    }

    public static BufferedImage Discretizar(BufferedImage imagen, double Niveles) {
        return Discretizar(imagen, Niveles, null);
    }

    public static BufferedImage Discretizar(BufferedImage imagen, double Niveles, double... aplicarCanal) {
        short[] ValsOp = new short[256];
        for (short i = 0; i < ValsOp.length; i++) {
            if (Niveles > 0) {
                double dn = 255 / (Niveles - 1);
                double t = Math.round(i / dn);
                ValsOp[i] = (short) (t * dn);
                if (ValsOp[i] < 0) {
                    ValsOp[i] = 0;
                }
                if (ValsOp[i] > 255) {
                    ValsOp[i] = 255;
                }
            } else {
                ValsOp[i] = i;
            }
        }
        LookupTable table = calcularTabla(imagen, ValsOp, aplicarCanal);
        LookupOp op = new LookupOp(table, null);
        return op.filter(imagen, null);
    }

    public static BufferedImage Invertir(BufferedImage imagen) {
        return Invertir(imagen, null);
    }

    public static BufferedImage Invertir(BufferedImage imagen, double... aplicarCanal) {
        short[] ValOp = new short[256];
        for (short i = 0; i < ValOp.length; i++) {
            ValOp[i] = (short) (255 - i);
        }
        LookupTable table = calcularTabla(imagen, ValOp, aplicarCanal);
        LookupOp op = new LookupOp(table, null);
        return op.filter(imagen, null);
    }

    public static BufferedImage Brillo(BufferedImage imagen, double brillo, double... aplicarCanal) {
        short[] ValOp = new short[256];
        for (short i = 0; i < ValOp.length; i++) {
            ValOp[i] = (short) (i + brillo * 255);
            if (ValOp[i] > 255) {
                ValOp[i] = 255;
            }
            if (ValOp[i] < 0) {
                ValOp[i] = 0;
            }
        }
        LookupTable table = calcularTabla(imagen, ValOp, aplicarCanal);
        LookupOp op = new LookupOp(table, null);
        return op.filter(imagen, null);
    }

    protected static LookupTable calcularTabla(BufferedImage imagen, short[] Op, double... a) {
        double[] ap = {1, 1, 1};
        for (int i = 0; i < ap.length && a != null; i++) {
            try {
                ap[i] = a[i];
            } catch (Exception e) {
            }
        }
        a = ap;
        int máx = 256;
        short[] A = new short[máx];
        short[] R = new short[máx];
        short[] G = new short[máx];
        short[] B = new short[máx];
        for (short i = 0; i < máx; i++) {
            A[i] = i;
            R[i] = (short) ((Op[i] - i) * a[0] + i);
            G[i] = (short) ((Op[i] - i) * a[1] + i);
            B[i] = (short) ((Op[i] - i) * a[2] + i);
        }
        if (imagen.getType() == BufferedImage.TYPE_INT_ARGB) {
            short[][] rgba = {R, G, B, A};
            return new ShortLookupTable(0, rgba);
        } else {
            short[][] rgb = {R, G, B};
            return new ShortLookupTable(0, rgb);
        }
    }

    public static BufferedImage EscalaDeGrises(BufferedImage imagen, float porcentaje) {
        BufferedImage gris = new BufferedImage(
                imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_BYTE_GRAY
        ) {
            {
                Graphics2D g = createGraphics();
                g.drawImage(imagen, 0, 0, null);
            }
        };
        BufferedImage retorno = new BufferedImage(
                imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_ARGB
        ) {
            {
                Graphics2D g = createGraphics();
                g.drawImage(imagen, 0, 0, null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, porcentaje));
                g.drawImage(gris, 0, 0, null);
            }
        };
        return retorno;
    }

    public static BufferedImage Monocromatizar(BufferedImage imagen, float Tono) {
        Tono = Tono % 1;
        BufferedImage retorno = new BufferedImage(
                imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_ARGB
        );
        for (int col = 0; col < imagen.getWidth(); col++) {
            for (int fil = 0; fil < imagen.getHeight(); fil++) {
                int argb = imagen.getRGB(col, fil);
                int a = argb >>> 24;
                int r = (argb >>> 16) & 0xff;
                int g = (argb >>> 8) & 0xff;
                int b = argb & 0xff;
                float[] HSB = Color.RGBtoHSB(r, g, b, null);
                retorno.setRGB(
                        col, fil,
                        (Color.HSBtoRGB(Tono, HSB[1], HSB[2]) & 0xffffff) | a << 24
                );
            }
        }
        return retorno;
    }

    public static BufferedImage Monocromatizar(BufferedImage imagen, Color color) {
        float[] HSB = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return Monocromatizar(imagen, HSB[0]);
    }

    public static BufferedImage Monocolorizar_HSB(BufferedImage imagen, float Tono, float saturación) {
        Tono = Tono % 1;
        saturación = saturación < 0 ? 0 : saturación > 1 ? 1 : saturación;
        BufferedImage retorno = new BufferedImage(
                imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_ARGB
        );
        for (int col = 0; col < imagen.getWidth(); col++) {
            for (int fil = 0; fil < imagen.getHeight(); fil++) {
                int argb = imagen.getRGB(col, fil);
                int a = argb >>> 24;
                int r = (argb >>> 16) & 0xff;
                int g = (argb >>> 8) & 0xff;
                int b = argb & 0xff;
                float[] HSB = Color.RGBtoHSB(r, g, b, null);
                retorno.setRGB(
                        col, fil,
                        (Color.HSBtoRGB(Tono, saturación, HSB[2]) & 0xffffff) | a << 24
                );
            }
        }
        return retorno;
    }

    public static BufferedImage Monocolorizar_HSL(BufferedImage imagen, float Tono, float saturación) {
        Tono = Tono % 1;
        saturación = saturación < 0 ? 0 : saturación > 1 ? 1 : saturación;
        BufferedImage retorno = new BufferedImage(
                imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_ARGB
        );
        for (int col = 0; col < imagen.getWidth(); col++) {
            for (int fil = 0; fil < imagen.getHeight(); fil++) {
                int argb = imagen.getRGB(col, fil);
                int a = argb >>> 24;
                int r = (argb >>> 16) & 0xff;
                int g = (argb >>> 8) & 0xff;
                int b = argb & 0xff;
                float[] HSL = ConversorModelosColor.RGBtoHSL(r, g, b, a);
                HSL[0] = Tono;
                HSL[1] = saturación;
                retorno.setRGB(
                        col, fil,
                        ConversorModelosColor.HSLtoRGB(HSL)
                );
            }
        }
        return retorno;
    }
}
