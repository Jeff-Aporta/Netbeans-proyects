/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.jhlabs.image;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;

public class ShadowFilter extends AbstractBufferedImageOp {

    private float radius = 5;
    private float angle = (float) Math.PI * 6 / 4;
    private float distance = 5;
    private float opacity = 0.5f;
    private boolean addMargins = false;
    private boolean shadowOnly = false;
    private int shadowColor = 0xff000000;
    
    public static void main(String[] args) throws Exception {
                JLabel presentador = new JLabel();
        ShadowFilter filtro = new ShadowFilter(30, 20, -20, .75f);
        BufferedImage carga = ImageIO.read(new URL(
                "https://cdn141.picsart.com/258219768022212.png?r1024x1024"
        ));
        BufferedImage imagen = filtro.filter(carga ,null);
        presentador.setIcon(new ImageIcon(imagen));
        new JFrame() {
            {
                add(presentador);
                setSize(imagen.getWidth(), imagen.getHeight());
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }
        };
    }
    
    public ShadowFilter() {
    }

    public ShadowFilter(float radius, float xOffset, float yOffset, float opacity) {
        this.radius = radius;
        this.angle = (float) Math.atan2(yOffset, xOffset);
        this.distance = (float) Math.sqrt(xOffset * xOffset + yOffset * yOffset);
        this.opacity = opacity;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public void setAddMargins(boolean addMargins) {
        this.addMargins = addMargins;
    }

    public boolean getAddMargins() {
        return addMargins;
    }

    public void setShadowOnly(boolean shadowOnly) {
        this.shadowOnly = shadowOnly;
    }

    public boolean getShadowOnly() {
        return shadowOnly;
    }

    public Rectangle2D getBounds2D(BufferedImage src) {
        Rectangle r = new Rectangle(0, 0, src.getWidth(), src.getHeight());
        if (addMargins) {
            float xOffset = distance * (float) Math.cos(angle);
            float yOffset = -distance * (float) Math.sin(angle);
            r.width += (int) (Math.abs(xOffset) + 2 * radius);
            r.height += (int) (Math.abs(yOffset) + 2 * radius);
        }
        return r;
    }

    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        if (dstPt == null) {
            dstPt = new Point2D.Double();
        }

        if (addMargins) {
            float xOffset = distance * (float) Math.cos(angle);
            float yOffset = -distance * (float) Math.sin(angle);
            float topShadow = Math.max(0, radius - yOffset);
            float leftShadow = Math.max(0, radius - xOffset);
            dstPt.setLocation(srcPt.getX() + leftShadow, srcPt.getY() + topShadow);
        } else {
            dstPt.setLocation(srcPt.getX(), srcPt.getY());
        }

        return dstPt;
    }

    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();

        float xOffset = distance * (float) Math.cos(angle);
        float yOffset = -distance * (float) Math.sin(angle);

        if (dst == null) {
            if (addMargins) {
                ColorModel cm = src.getColorModel();
                dst = new BufferedImage(cm, cm.createCompatibleWritableRaster(src.getWidth() + (int) (Math.abs(xOffset) + radius), src.getHeight() + (int) (Math.abs(yOffset) + radius)), cm.isAlphaPremultiplied(), null);
            } else {
                dst = createCompatibleDestImage(src, null);
            }
        }

        float shadowR = ((shadowColor >> 16) & 0xff) / 255f;
        float shadowG = ((shadowColor >> 8) & 0xff) / 255f;
        float shadowB = (shadowColor & 0xff) / 255f;

        // Make a black mask from the image's alpha channel 
        float[][] extractAlpha = {
            {0, 0, 0, shadowR},
            {0, 0, 0, shadowG},
            {0, 0, 0, shadowB},
            {0, 0, 0, opacity}
        };
        BufferedImage shadow = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        new BandCombineOp(extractAlpha, null).filter(src.getRaster(), shadow.getRaster());
        shadow = new GaussianFilter(radius).filter(shadow, null);

        Graphics2D g = dst.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        if (addMargins) {
            float radius2 = radius / 2;
            float topShadow = Math.max(0, radius - yOffset);
            float leftShadow = Math.max(0, radius - xOffset);
            g.translate(leftShadow, topShadow);
        }
        g.drawRenderedImage(shadow, AffineTransform.getTranslateInstance(xOffset, yOffset));
        if (!shadowOnly) {
            g.setComposite(AlphaComposite.SrcOver);
            g.drawRenderedImage(src, null);
        }
        g.dispose();

        return dst;
    }

    public String toString() {
        return "Stylize/Drop Shadow...";
    }
}
