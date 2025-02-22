
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientasMatemáticas.Dupla;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

public class bot_tor_adfly {

    static Robot robot;

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
                    lbl.setText(d.X + " - " + d.Y);
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
                setVisible(true);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        };
        while (true) {
            adfly();
            Thread.sleep((long) (0.5*60*1000));
        }
    }

    static void adfly() throws Exception {
        Runtime runtime = Runtime.getRuntime();
        Process theProcess = runtime.exec(
                new String[]{"C:\\Users\\57310\\Desktop\\Tor Browser\\Browser\\firefox.exe"}
        );
        Thread.sleep(2000);
        mouseMove(Dupla.DIMENSIÓN_PANTALLA.Ancho() / 2, Dupla.DIMENSIÓN_PANTALLA.Alto() / 2);
        makeClickLeft();
        makeWindowsUp();
        mouseMove(0, 0);
        Thread.sleep(100);
        mouseMove(766, 106);
        makeClickLeft();
        Thread.sleep(6000);
        String texto = LectoEscrituraArchivos.LeerArchivo_ASCII("C:\\Users\\57310\\Downloads\\adf.ly-bot-main\\adf.ly-bot-main\\links.txt");
        String[] renglones = texto.split("\n");
        String link = renglones[(int) (Math.random() * renglones.length)];
        System.out.println(link);
        copy(link);
        paste();
        makeENTER();
        Thread.sleep(15000);
        mouseMove(573, 254);
        makeClickLeft();
        Thread.sleep(20000);
        makeCtrlW();
        Thread.sleep(10000);
        mouseMove(1820, 170);
        robot.delay(100);
        makeClickLeft();
        Thread.sleep(10000);
        makeCtrlW();
    }

    static void makeClickLeft() {
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(100);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    static void paste() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(100);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_V);
        robot.delay(100);
    }

    static void copy(String string) {
        StringSelection stringSelection = new StringSelection(string);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    static void makeAltF4() {
        robot.keyPress(KeyEvent.VK_ALT);
        robot.delay(100);
        robot.keyPress(KeyEvent.VK_F4);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_F4);
        robot.delay(100);
    }
    
    static void makeCtrlW() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.delay(100);
        robot.keyPress(KeyEvent.VK_W);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_W);
        robot.delay(100);
    }

    static void makeWindowsUp() {
        robot.keyPress(KeyEvent.VK_WINDOWS);
        robot.delay(100);
        robot.keyPress(KeyEvent.VK_UP);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_WINDOWS);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_UP);
        robot.delay(100);
    }

    static void makeENTER() {
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(100);
    }

    static void sendKeys(Robot robot, String keys) {
        for (char c : keys.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED == keyCode) {
                continue;
            }
            robot.keyPress(keyCode);
            robot.delay(100);
            robot.keyRelease(keyCode);
            robot.delay(100);
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
