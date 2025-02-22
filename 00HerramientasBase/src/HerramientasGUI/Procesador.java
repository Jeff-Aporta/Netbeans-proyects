package HerramientasGUI;

import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientaArchivos.LectorMixto;
import HerramientaDeImagen.GeneradorDeTexto;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;

public abstract class Procesador {

    BufferedImage canvas;
    Graphics2D g;
    int width;
    int height;
    JFrame frame;
    JLabel lbl = new JLabel() {
        @Override
        public void paint(Graphics grphcs) {
            grphcs.drawImage(canvas, 0, 0, null);
            super.paint(grphcs);
        }
    };

    Thread hilo = new Thread() {
        @Override
        public void run() {
            while (true) {
                update();
                try {
                    sleep(1000 / 100);
                } catch (InterruptedException ex) {
                }
            }
        }
    };

    public static void main(String[] args) {
        Procesador p = new Procesador(800, 500) {
            BufferedImage bg;
            GeneradorDeTexto gdt = new GeneradorDeTexto();

            @Override
            public void setup() {
                bg = loadImage("https://i.ibb.co/Dk7rp41/k.jpg");
                gdt.ModificarFuente(getFont("C:\\Users\\57310\\Downloads\\lmroman.ttf"));
            }

            @Override
            public void draw() {
                image(bg, 0, 0, width, height);
                gdt.DibujarTextoCentradoEnImagen(canvas, "Jeff Aporta");
            }
        };
    }

    private static Font getFont(String name)  {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(name));
        } catch (Exception ex) {
        }
        return null;
    }

    public Procesador(int width, int height) {
        resizeCanvas(width, height);
        frame = new JFrame() {
            {
                setSize(width + 20, height + 60);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
                add(lbl);
            }
        };
        setup();
        hilo.start();
    }

    public abstract void setup();

    public abstract void draw();

    public void update() {
        draw();
        lbl.repaint();
    }

    void resizeCanvas(int width, int height) {
        this.width = width;
        this.height = height;
        canvas = new BufferedImage(width, height, 2);
        g = canvas.createGraphics();
    }

    BufferedImage loadImage(String s) {
        return LectoEscrituraArchivos.cargar_imagen(s);
    }

    void loadFont(String f, float sz) {
        g.setFont(LectorMixto.cargar_Fuente(f, sz));
    }

    void color(Color c) {
        g.setColor(c);
    }

    void background() {
        rect(0, 0, width, height);
    }

    void rect(double x, double y, double w, double h) {
        g.fillRect((int) x, (int) y, (int) w, (int) h);
    }

    void drawRect(double x, double y, double w, double h) {
        g.fillRect((int) x, (int) y, (int) w, (int) h);
    }

    void circle(double x, double y, double d) {
        g.fillOval((int) (x - d / 2), (int) (y - d / 2), (int) d, (int) d);
    }

    void drawCircle(double x, double y, double d) {
        g.fillOval((int) (x - d / 2), (int) (y - d / 2), (int) d, (int) d);
    }

    void image(BufferedImage img, double x, double y) {
        g.drawImage(img, (int) x, (int) y, null);
    }

    void image(BufferedImage img, double x, double y, double w, double h) {
        g.drawImage(img, (int) x, (int) y, (int) w, (int) h, null);
    }
}
