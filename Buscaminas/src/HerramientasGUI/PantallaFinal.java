package HerramientasGUI;

import java.awt.AlphaComposite;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public final class PantallaFinal extends JDialog {

    final static double TIEMPO_IMAGEN = 1.4f;
    final static float TIEMPO_TRANSICIÓN = 0.5f;
    final static float TIEMPO_OPACIDAD = 0.5f;

    boolean LinksActivados = false;

    final static double FPS = 30;
    final static float PROPORCIONALIDAD_FOTOGRAMA = (float) (1 / FPS);
    final static int MS_FOTOGRAMA = (int) (1000 * PROPORCIONALIDAD_FOTOGRAMA);

    static BufferedImage img0;
    static BufferedImage img1;

    public static void main(String args[]) {
        CerrarPrograma();
    }

    public static void CerrarPrograma() {
        new Thread(() -> {
            new PantallaFinal();
            System.exit(0);
        }).start();
    }

    private PantallaFinal() {
        super();
        initComponents();
        CargaImagenes();
        setSize(900, 506);
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 60, 60));
        SalirDelSistema();
    }

    void SalirDelSistema() {
        setOpacity(0);
        setVisible(true);
        {//Desaparecer la ventana
            int f = 0;
            while (f++ < FPS * TIEMPO_OPACIDAD) {
                setOpacity((PROPORCIONALIDAD_FOTOGRAMA / TIEMPO_OPACIDAD) * f);
                try {
                    Thread.sleep(MS_FOTOGRAMA);
                } catch (InterruptedException ex) {
                    System.exit(0);
                }
            }
        }
        //Mostrar la primer imagen
        try {
            Thread.sleep((int) (1000 * TIEMPO_IMAGEN));
        } catch (InterruptedException ex) {
            System.exit(0);
        }
        LinksActivados = true;
        jLabel2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        {//hacer transición entre imagen 0 e imagen 1
            int f = 0;
            while (f++ < FPS * TIEMPO_TRANSICIÓN) {
                BufferedImage fotograma = new BufferedImage(img0.getWidth(), img0.getHeight(), 2);
                Graphics2D g = fotograma.createGraphics();
                g.drawImage(img0, 0, 0, null);
                float op = (PROPORCIONALIDAD_FOTOGRAMA / TIEMPO_TRANSICIÓN) * f;
                op = op > 1 ? 1 : op;
                g.setComposite(
                        AlphaComposite.getInstance(
                                AlphaComposite.SRC_OVER,
                                op
                        )
                );
                g.drawImage(img1, 0, 0, null);
                jLabel1.setIcon(new ImageIcon(fotograma));
                try {
                    Thread.sleep(MS_FOTOGRAMA);
                } catch (InterruptedException ex) {
                    System.exit(0);
                }
            }
        }
        //Mostrar la segunda imagen
        try {
            Thread.sleep((int) (1000 * TIEMPO_IMAGEN));
        } catch (InterruptedException ex) {
            System.exit(0);
        }
        {//Desaparecer la ventana
            int f = 0;
            while (f++ < FPS * TIEMPO_OPACIDAD) {
                setOpacity(1 - (PROPORCIONALIDAD_FOTOGRAMA / TIEMPO_OPACIDAD) * f);
                try {
                    Thread.sleep(MS_FOTOGRAMA);
                } catch (InterruptedException ex) {
                    System.exit(0);
                }
            }
        }
    }

    final static void CargaImagenes() {
        try {
            img0 = ImageIO.read(ClassLoader.class.getResource("/HerramientasGUI/firma0.png"));
            jLabel1.setIcon(new ImageIcon(img0));
            img1 = ImageIO.read(ClassLoader.class.getResource("/HerramientasGUI/firma.png"));
        } catch (IOException ex) {
            System.exit(0);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(900, 506));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(900, 506));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(null);

        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("X");
        jLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel6MouseReleased(evt);
            }
        });
        getContentPane().add(jLabel6);
        jLabel6.setBounds(864, 10, 30, 20);

        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel2MousePressed(evt);
            }
        });
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 210, 190, 90);

        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel3MousePressed(evt);
            }
        });
        getContentPane().add(jLabel3);
        jLabel3.setBounds(660, 220, 230, 80);

        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel4MousePressed(evt);
            }
        });
        getContentPane().add(jLabel4);
        jLabel4.setBounds(270, 10, 370, 370);

        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel5MousePressed(evt);
            }
        });
        getContentPane().add(jLabel5);
        jLabel5.setBounds(200, 410, 500, 90);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, -2, 900, 510);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void jLabel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MousePressed
        if (!LinksActivados) {
            return;
        }
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                Desktop dk = Desktop.getDesktop();
                dk.browse(new URI("http://www.youtube.com/c/JeffAporta"));
                dk.browse(new URI("https://www.youtube.com/channel/UC69yHagTkWMJvrTTtIVSrnQ"));
            } catch (Exception e) {
                System.out.println("Error al abrir URL: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jLabel2MousePressed

    private void jLabel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MousePressed
        if (!LinksActivados) {
            return;
        }
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop dk = Desktop.getDesktop();
                dk.browse(new URI("https://www.facebook.com/Jeff.Aporta"));
            } catch (Exception e) {
                System.out.println("Error al abrir URL: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jLabel3MousePressed

    private void jLabel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MousePressed
        if (!LinksActivados) {
            return;
        }
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                Desktop dk = Desktop.getDesktop();
                dk.browse(new URI("http://www.youtube.com/c/JeffAporta"));
            } catch (Exception e) {
                System.out.println("Error al abrir URL: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jLabel4MousePressed

    private void jLabel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MousePressed
        if (!LinksActivados) {
            return;
        }
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                Desktop dk = Desktop.getDesktop();
                dk.browse(new URI("http://www.youtube.com/c/JeffAporta"));
            } catch (Exception e) {
                System.out.println("Error al abrir URL: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jLabel5MousePressed

    private void jLabel6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseReleased
        System.exit(0);
    }//GEN-LAST:event_jLabel6MouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    // End of variables declaration//GEN-END:variables
}
