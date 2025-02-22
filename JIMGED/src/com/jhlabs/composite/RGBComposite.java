package com.jhlabs.composite;

import java.awt.*;
import java.awt.image.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;

public abstract class RGBComposite implements Composite {

    protected float extraAlpha;

    static void test(Composite composite) throws Exception {
        BufferedImage ImagenDebajo;
        {
            String URL_ImagenDebajo = "https://i1.wp.com/www.jordankranda.com/wp-content/uploads/2013/12/"
                    + "6935001-beautiful-girl-images.jpg?resize=640%2C360";
            ImagenDebajo = ImageIO.read(new URL(URL_ImagenDebajo));
            ImagenDebajo.createGraphics().drawImage(ImagenDebajo, 0, 0, null);
        }
        BufferedImage ImagenEncima;
        {
            String URL_ImagenArriba = "http://3.bp.blogspot.com/-NjUGCxHTg1U/VespAW_doBI/AAAAAAAAQsk/KGBFnQJk1-E/"
                    + "s640/Basil%2Bis%2Bapproximately%2B40%2Bkms%2Bfrom%2BNaran.jpg";
            ImagenEncima = new BufferedImage(ImagenDebajo.getWidth(), ImagenDebajo.getHeight(), 2);
            ImagenEncima.createGraphics().drawImage(ImageIO.read(new URL(URL_ImagenArriba)), 0, 0, null);
        }
        BufferedImage ImagenCombinada = new BufferedImage(ImagenDebajo.getWidth(), ImagenDebajo.getHeight(), 2);
        {
            Graphics2D g = ImagenCombinada.createGraphics();
            g.drawImage(ImagenDebajo, 0, 0, null);
            g.setComposite(composite);
            g.drawImage(ImagenEncima, 0, 0, null);
        }
        JLabel presentador = new JLabel();
        new JFrame() {
            {
                add(presentador);
                setSize(ImagenCombinada.getWidth(), ImagenCombinada.getHeight());
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }
        };
        presentador.setIcon(new ImageIcon(ImagenDebajo));
        Thread.sleep(1000);
        presentador.setIcon(new ImageIcon(ImagenEncima));
        Thread.sleep(1000);
        presentador.setIcon(new ImageIcon(ImagenCombinada));
    }

    public RGBComposite() {
        this(1.0f);
    }

    public RGBComposite(float alpha) {
        if (alpha < 0.0f || alpha > 1.0f) {
            throw new IllegalArgumentException("RGBComposite: alpha must be between 0 and 1");
        }
        this.extraAlpha = alpha;
    }

    public float getAlpha() {
        return extraAlpha;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(extraAlpha);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RGBComposite)) {
            return false;
        }
        RGBComposite c = (RGBComposite) o;

        return extraAlpha == c.extraAlpha;
    }

    public abstract static class RGBCompositeContext implements CompositeContext {

        private float alpha;
        private ColorModel srcColorModel;
        private ColorModel dstColorModel;

        public RGBCompositeContext(float alpha, ColorModel srcColorModel, ColorModel dstColorModel) {
            this.alpha = alpha;
            this.srcColorModel = srcColorModel;
            this.dstColorModel = dstColorModel;
        }

        public void dispose() {
        }

        // Multiply two numbers in the range 0..255 such that 255*255=255
        static int multiply255(int a, int b) {
            int t = a * b + 0x80;
            return ((t >> 8) + t) >> 8;
        }

        static int clamp(int a) {
            return a < 0 ? 0 : a > 255 ? 255 : a;
        }

        public abstract void composeRGB(int[] src, int[] dst, float alpha);

        public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
            float alpha = this.alpha;

            int[] srcPix = null;
            int[] dstPix = null;

            int x = dstOut.getMinX();
            int w = dstOut.getWidth();
            int y0 = dstOut.getMinY();
            int y1 = y0 + dstOut.getHeight();

            for (int y = y0; y < y1; y++) {
                srcPix = src.getPixels(x, y, w, 1, srcPix);
                dstPix = dstIn.getPixels(x, y, w, 1, dstPix);
                composeRGB(srcPix, dstPix, alpha);
                dstOut.setPixels(x, y, w, 1, dstPix);
            }
        }

    }
}
