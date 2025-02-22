
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientasMatemáticas.Dupla;
import HerramientasSistema.Sistema;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import static java.lang.Thread.sleep;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import static javax.swing.SwingConstants.CENTER;

public class bot_cinecalidad {

    static Robot robot;
    static int ciclos = 277;

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
                    lbl.setText(d.X + " - " + d.Y + " (" + ciclos + "/285)");
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
        for (int j = 0; true; j++) {
            System.out.println(ciclos);
            if (ciclos > 285) {
                break;
            }
            Sistema.Abrir_URL_EnNavegador("https://www.cinecalidad.lat/page/" + (ciclos++) + "/");
            //sleep(1000);
            //makeCtrlT();
            sleep(3000);
            abrirPaginasDePeliculas();
            sleep(6000);
            for (int i = 0; i < 12; i++) {
                copy("");
                obtenerInfoPelicula();
                obtenerLinkDescarga();
            }
            sleep(1000);
            makeCtrlW();
        }
        System.exit(0);
    }

    static void obtenerLinkDescarga() throws Exception {
        copy("");
        {//Link por uTorrent
            sleep(2000);
            makeF12();
            sleep(1500);
            mouseMove(1149, 339);
            makeClickRight();
            mouseMove(1149 + 10, 339 + 10);
            makeClickLeft();
            sleep(100);
            mouseMove(333, 730);
            makeClickLeft();
            makeAltTAB();
            sleep(100);
            makeENTER();
            paste();
            sleep(2000);
            makeAltTAB();
            makeCtrlW();
        }
        copy("");
        {//Link por MEGA
            sleep(2000);
            makeF12();
            sleep(1500);
            mouseMove(1149, 339);
            makeClickRight();
            mouseMove(1149 + 10, 339 + 10);
            makeClickLeft();
            sleep(100);
            mouseMove(336, 576);
            makeClickLeft();
            makeAltTAB();
            sleep(100);
            makeENTER();
            paste();
            makeENTER();
            copy("},");
            paste();
            sleep(2000);
            makeAltTAB();
            makeCtrlW();
        }

        makeCtrlW();
    }

    static void obtenerInfoPelicula() throws Exception {
        sleep(2000);
        makeF12();
        sleep(1500);
        mouseMove(1149, 281);
        makeClickRight();
        mouseMove(1149 + 10, 281 + 10);
        makeClickLeft();
        sleep(100);
        mouseMove(348, 1030);
        makeClickLeft();
        sleep(100);
        makeAltTAB();
        sleep(100);
        makeENTER();
        makeENTER();
        paste();
        sleep(1000);
        makeAltTAB();
        sleep(100);
        {//Clic en link descarga
            mouseMove(1149, 308);
            makeClickRight();
            mouseMove(1149 + 10, 308 + 10);
            makeClickLeft();
        }
    }

    static void abrirPaginasDePeliculas() throws Exception {
//        mouseMove(636, 81);
//        copy("https://www.cinecalidad.lat/page/" + (ciclos++) + "/");
//        makeClickLeft();
//        makeDelete();
//        paste();
//        makeENTER();
//        sleep(3000);
        makeF12();
        sleep(2000);
        mouseMove(1149, 245);
        makeClickRight();
        mouseMove(1149 + 10, 245 + 10);
        makeClickLeft();
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

    static void makeAltTAB() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_ALT);
        sleep(200);
        robot.keyPress(KeyEvent.VK_TAB);
        sleep(200);
        robot.keyRelease(KeyEvent.VK_ALT);
        sleep(200);
        robot.keyRelease(KeyEvent.VK_TAB);
        sleep(200);
    }

    static void paste() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_CONTROL);
        sleep(100);
        robot.keyPress(KeyEvent.VK_V);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_V);
    }

    static void copy(String string) {
        StringSelection stringSelection = new StringSelection(string);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    static void makeAltF4() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_ALT);
        sleep(100);
        robot.keyPress(KeyEvent.VK_F4);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_ALT);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_F4);
    }

    static void makeCtrlW() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_CONTROL);
        sleep(100);
        robot.keyPress(KeyEvent.VK_W);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_W);
    }

    static void makeCtrlT() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_CONTROL);
        sleep(100);
        robot.keyPress(KeyEvent.VK_T);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_T);
    }

    static void makeWindowsUp() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_WINDOWS);
        sleep(100);
        robot.keyPress(KeyEvent.VK_UP);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_WINDOWS);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_UP);
    }

    static void makeDelete() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_DELETE);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_DELETE);
    }

    static void makeENTER() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_ENTER);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    static void makeF12() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_F12);
        sleep(100);
        robot.keyRelease(KeyEvent.VK_F12);
    }

    static void sendKeys(Robot robot, String keys) throws InterruptedException {
        for (char c : keys.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                continue;
            }
            robot.keyPress(keyCode);
            sleep(100);
            robot.keyRelease(keyCode);
            sleep(100);
        }
    }

    static void mouseMove(int x, int y) throws Exception {
        int intento = 0;
        while ((MouseInfo.getPointerInfo().getLocation().getX() != x || MouseInfo.getPointerInfo().getLocation().getY() != y) && intento <= 100) {
            new Robot().mouseMove(x, y);
            intento++;
        }
    }
}
