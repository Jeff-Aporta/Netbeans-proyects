
import HerramientasMatemáticas.Dupla;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import static java.lang.Thread.sleep;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import static javax.swing.SwingConstants.CENTER;

public class bot_eliminar_videos_youtube {

    static Robot robot;
    static int ciclos = 0;
    static int cantidadVideos = 1249;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        JLabel lbl = new JLabel() {
            {
                setHorizontalAlignment(CENTER);
                setFont(getFont().deriveFont(20f));
            }
        };
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    Dupla d = Dupla.PosiciónCursorEnPantalla();
                    lbl.setText(d.X + " - " + d.Y + " (" + ciclos + "/" + (1 + cantidadVideos / 50) + ")");
                    try {
                        sleep(100);
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
        JFrame frame = new JFrame() {
            {
                add(lbl);
                setSize(250, 100);
                Dupla d = Dupla.Alinear(Dupla.DIMENSIÓN_PANTALLA, getSize(), Dupla.DERECHA, Dupla.ARRIBA);
                setLocation(d.intX(), d.intY());
                setAlwaysOnTop(true);
                setVisible(true);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        };
        sleep(10000);
        for (int j = 0; j < (1 + (cantidadVideos-3*30-7*50) / 50) ; j++) {
            ciclos = j;
            System.out.println(j);
            mouseMove(450, 406);
            makeClickLeft();
            sleep(1000);
            mouseMove(1280, 421);
            makeClickLeft();
            sleep(1000);
            mouseMove(1280, 473);
            makeClickLeft();
            sleep(1000);
            mouseMove(586, 648);
            makeClickLeft();
            sleep(1000);
            mouseMove(1280, 730);
            makeClickLeft();
            sleep(6000);
            while (true) {
                Color c = new Color(31, 31, 31);
                Point p = new Point(1187, 429);
                BufferedImage pantallazo = robot.createScreenCapture(new Rectangle(Dupla.DIMENSIÓN_PANTALLA.convDimension()));
                if (pantallazo.getRGB(p.x, p.y) == c.getRGB()) {
                    sleep(1000);
                } else {
                    break;
                }
            }
            sleep(10000);
        }
        System.exit(0);
    }

    static void makeClickLeft() throws InterruptedException {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        sleep(100);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    static void makeClickRight() throws InterruptedException {
        robot.mousePress(InputEvent.BUTTON3_MASK);
        sleep(100);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
    }

    static void mouseMove(int x, int y) throws Exception {
        int intento = 0;
        while ((MouseInfo.getPointerInfo().getLocation().getX() != x || MouseInfo.getPointerInfo().getLocation().getY() != y) && intento <= 100) {
            new Robot().mouseMove(x, y);
            intento++;
        }
    }
}
