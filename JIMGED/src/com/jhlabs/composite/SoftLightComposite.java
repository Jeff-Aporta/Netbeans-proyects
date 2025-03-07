package com.jhlabs.composite;

import java.awt.*;
import java.awt.image.*;

public final class SoftLightComposite extends RGBComposite {

	public SoftLightComposite( float alpha ) {
        super( alpha );
	}

	public CompositeContext createContext( ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints ) {
		return new Context( extraAlpha, srcColorModel, dstColorModel );
	}

    static class Context extends RGBCompositeContext {
        public Context( float alpha, ColorModel srcColorModel, ColorModel dstColorModel ) {
            super( alpha, srcColorModel, dstColorModel );
        }

        public void composeRGB( int[] src, int[] dst, float alpha ) {
            int w = src.length;

            for ( int i = 0; i < w; i += 4 ) {
                int sr = src[i];
                int dir = dst[i];
                int sg = src[i+1];
                int dig = dst[i+1];
                int sb = src[i+2];
                int dib = dst[i+2];
                int sa = src[i+3];
                int dia = dst[i+3];
                int dor, dog, dob;

                int d;
                d = multiply255(sr, dir);
                dor = d + multiply255(dir, 255 - multiply255(255-dir, 255-sr)-d);
                d = multiply255(sg, dig);
                dog = d + multiply255(dig, 255 - multiply255(255-dig, 255-sg)-d);
                d = multiply255(sb, dib);
                dob = d + multiply255(dib, 255 - multiply255(255-dib, 255-sb)-d);

                float a = alpha*sa/255f;
                float ac = 1-a;

                dst[i] = (int)(a*dor + ac*dir);
                dst[i+1] = (int)(a*dog + ac*dig);
                dst[i+2] = (int)(a*dob + ac*dib);
                dst[i+3] = (int)(sa*alpha + dia*ac);
            }
        }
    }

}
