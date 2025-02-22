package com.jhlabs.composite;

import static com.jhlabs.composite.RGBComposite.test;
import java.awt.*;
import java.awt.image.*;
import java.awt.color.*;

public final class ContourComposite implements Composite {

    private int offset;

    public static void main(String[] args) throws Exception {
        test(new ContourComposite(1));
    }

    public ContourComposite(int offset) {
        this.offset = offset;
    }

    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return new ContourCompositeContext(offset, srcColorModel, dstColorModel);
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ContourComposite)) {
            return false;
        }
        return true;
    }

}

class ContourCompositeContext implements CompositeContext {

    private int offset;

    public ContourCompositeContext(int offset, ColorModel srcColorModel, ColorModel dstColorModel) {
        this.offset = offset;
    }

    public void dispose() {
    }

    public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
        int x = src.getMinX();
        int y = src.getMinY();
        int w = src.getWidth();
        int h = src.getHeight();

        int[] srcPix = null;
        int[] srcPix2 = null;
        int[] dstInPix = null;
        int[] dstOutPix = new int[w * 4];

        for (int i = 0; i < h; i++) {
            srcPix = src.getPixels(x, y, w, 1, srcPix);
            dstInPix = dstIn.getPixels(x, y, w, 1, dstInPix);

            int lastAlpha = 0;
            int k = 0;
            for (int j = 0; j < w; j++) {
                int alpha = srcPix[k + 3];
                int alphaAbove = i != 0 ? srcPix2[k + 3] : alpha;

                if (i != 0 && j != 0 && ((alpha ^ lastAlpha) & 0x80) != 0 || ((alpha ^ alphaAbove) & 0x80) != 0) {
                    if ((offset + i + j) % 10 > 4) {
                        dstOutPix[k] = 0x00;
                        dstOutPix[k + 1] = 0x00;
                        dstOutPix[k + 2] = 0x00;
                    } else {
                        dstOutPix[k] = 0xff;
                        dstOutPix[k + 1] = 0xff;
                        dstOutPix[k + 2] = 0x7f;
                    }
                    dstOutPix[k + 3] = 0xff;
                } else {
                    dstOutPix[k] = dstInPix[k];
                    dstOutPix[k + 1] = dstInPix[k + 1];
                    dstOutPix[k + 2] = dstInPix[k + 2];
//					if ( dstOut == dstIn )
                    dstOutPix[k] = 0xff;
                    dstOutPix[k + 1] = 0;
                    dstOutPix[k + 2] = 0;
                    dstOutPix[k + 3] = 0;
//					else
//						dstOutPix[k+3] = dstInPix[k+3];
                }

                lastAlpha = alpha;
                k += 4;
            }

            dstOut.setPixels(x, y, w, 1, dstOutPix);

            int[] t = srcPix;
            srcPix = srcPix2;
            srcPix2 = t;
            y++;
        }
    }

}
