package com.jhlabs.composite;

import java.awt.*;
import java.awt.image.*;

public final class HardLightComposite extends RGBComposite {

    public static void main(String[] args) throws Exception {
        test(new HardLightComposite(1));
    }

    public HardLightComposite(float alpha) {
        super(alpha);
    }

    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return new Context(extraAlpha, srcColorModel, dstColorModel);
    }

    static class Context extends RGBCompositeContext {

        public Context(float alpha, ColorModel srcColorModel, ColorModel dstColorModel) {
            super(alpha, srcColorModel, dstColorModel);
        }

        public void composeRGB(int[] src, int[] dst, float alpha) {
            int w = src.length;

            for (int i = 0; i < w; i += 4) {
                int sr = src[i];
                int dir = dst[i];
                int sg = src[i + 1];
                int dig = dst[i + 1];
                int sb = src[i + 2];
                int dib = dst[i + 2];
                int sa = src[i + 3];
                int dia = dst[i + 3];
                int dor, dog, dob;

                if (sr > 127) {
                    dor = 255 - 2 * multiply255(255 - sr, 255 - dir);
                } else {
                    dor = 2 * multiply255(sr, dir);
                }
                if (sg > 127) {
                    dog = 255 - 2 * multiply255(255 - sg, 255 - dig);
                } else {
                    dog = 2 * multiply255(sg, dig);
                }
                if (sb > 127) {
                    dob = 255 - 2 * multiply255(255 - sb, 255 - dib);
                } else {
                    dob = 2 * multiply255(sb, dib);
                }

                float a = alpha * sa / 255f;
                float ac = 1 - a;

                dst[i] = (int) (a * dor + ac * dir);
                dst[i + 1] = (int) (a * dog + ac * dig);
                dst[i + 2] = (int) (a * dob + ac * dib);
                dst[i + 3] = (int) (sa * alpha + dia * ac);
            }
        }
    }

}
