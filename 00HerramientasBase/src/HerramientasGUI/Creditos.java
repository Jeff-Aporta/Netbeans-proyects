package HerramientasGUI;

import HerramientaArchivos.BibliotecaArchivos;
import HerramientasSistema.Sistema;
import HerramientaDeImagen.Filtros_Lineales;
import static HerramientaDeImagen.Filtros_Lineales.EscalarGranCalidad;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import static HerramientaArchivos.BibliotecaArchivos.añadir_biblioteca_appdata_img;
import static HerramientaArchivos.BibliotecaArchivos.añadir_biblioteca_appdata_img;
import static HerramientaArchivos.BibliotecaArchivos.añadir_biblioteca_appdata_img;
import static HerramientaArchivos.BibliotecaArchivos.añadir_biblioteca_appdata_img;

public class Creditos extends JDialog {

    public static Creditos singleton;

    BufferedImage LogoApp = null;

    public static void main(String args[]) throws Exception {
        try {
            NimbusModificado.CargarNimbus();
        } catch (Exception ex) {
        }
        Generar();
    }

    public static void Generar() {
        Generar(null);
    }

    public static void Generar(BufferedImage img) {
        if (singleton != null && singleton.isVisible()) {
            return;
        }
        if (img == null) {
            singleton = new Creditos();
        } else {
            singleton = new Creditos(img);
        }
    }

    protected static BufferedImage LogoApp() throws Exception {
        return añadir_biblioteca_appdata_img(
                "Logo\\Programas",
                "https://docs.google.com/drawings/d/e/2PACX-1vTX-mcCopryMSM8XGZ6gvBxQJKdK"
                + "wVZY5_JYoClTjyqx0hu2UaDJ5isnajjmP5j7rjeD6vAcKyE9xJt/pub?w=720&h=720",
                "coverador.png"
        );
    }

    private Creditos() {
        this(null);
    }

    private Creditos(BufferedImage logo) {
        super();
        LogoApp = logo;
        initComponents();
        try {
            setIconImage(EscalarGranCalidad(BibliotecaArchivos.Imagenes.Logos.JeffAporta(),
                    32, -1
            ));
        } catch (Exception e) {
        }
        setVisible(true);
        setLocationRelativeTo(null);
        if (LogoApp == null) {
            mostrarLogosSinLogoApp();
        } else {
            mostrarLogosConLogoApp();
        }
    }

    public void mostrarLogosConLogoApp() {
        try {
            BufferedImage logoApp = LogoApp;
            BufferedImage Icono = new BufferedImage(jLabel1.getWidth(), jLabel1.getHeight(), 2);
            Filtros_Lineales.Ajuste_Centrado_Ajustar(logoApp, Icono);
            jLabel1.setIcon(new ImageIcon(Icono));
        } catch (Exception ex) {
        }
        try {
            BufferedImage logoDonar = BibliotecaArchivos.Imagenes.Logos.DonacionesPaypal();
            BufferedImage Icono = new BufferedImage(jLabel4.getWidth(), jLabel4.getHeight(), 2);
            Filtros_Lineales.Ajuste_Centrado_Ajustar(logoDonar, Icono);
            jLabel4.setIcon(new ImageIcon(Icono));
        } catch (Exception ex) {
        }
        try {
            BufferedImage logoAporta = BibliotecaArchivos.Imagenes.Logos.JeffAporta_Texto();
            BufferedImage Icono = new BufferedImage(jLabel2.getWidth(), jLabel2.getHeight(), 2);
            Filtros_Lineales.Ajuste_Centrado_Ajustar(logoAporta, Icono);
            jLabel2.setIcon(new ImageIcon(Icono));
        } catch (Exception ex) {
        }
        try {
            BufferedImage logoFBJeffrey = EscalarGranCalidad(BibliotecaArchivos.Imagenes.Logos.FBJeffrey(), -1, 162);
            BufferedImage Icono = new BufferedImage(jLabel3.getWidth(), jLabel3.getHeight(), 2);
            Filtros_Lineales.Ajuste_Centrado_Ajustar(logoFBJeffrey, Icono);
            jLabel3.setIcon(new ImageIcon(Icono));
        } catch (Exception ex) {
        }
    }

    public void mostrarLogosSinLogoApp() {
        try {
            BufferedImage logoJeffAporta = BibliotecaArchivos.Imagenes.Logos.JeffAporta_Texto();
            BufferedImage Icono = new BufferedImage(jLabel1.getWidth(), jLabel1.getHeight(), 2);
            Filtros_Lineales.Ajuste_Centrado_Ajustar(logoJeffAporta, Icono);
            jLabel1.setIcon(new ImageIcon(Icono));
        } catch (Exception ex) {
        }
        try {
            BufferedImage logoDonar = BibliotecaArchivos.Imagenes.Logos.DonacionesPaypal();
            BufferedImage Icono = new BufferedImage(jLabel4.getWidth(), jLabel4.getHeight(), 2);
            Filtros_Lineales.Ajuste_Centrado_Ajustar(logoDonar, Icono);
            jLabel4.setIcon(new ImageIcon(Icono));
        } catch (Exception ex) {
        }
        try {
            BufferedImage logoJeffDibuja = BibliotecaArchivos.Imagenes.Logos.JeffDibuja();
            BufferedImage Icono = new BufferedImage(jLabel2.getWidth(), jLabel2.getHeight(), 2);
            Filtros_Lineales.Ajuste_Centrado_Ajustar(logoJeffDibuja, Icono);
            jLabel2.setIcon(new ImageIcon(Icono));
        } catch (Exception ex) {
        }
        try {
            BufferedImage logoFBJeffrey = EscalarGranCalidad(BibliotecaArchivos.Imagenes.Logos.FBJeffrey(), -1, 162);
            BufferedImage Icono = new BufferedImage(jLabel3.getWidth(), jLabel3.getHeight(), 2);
            Filtros_Lineales.Ajuste_Centrado_Ajustar(logoFBJeffrey, Icono);
            jLabel3.setIcon(new ImageIcon(Icono));
        } catch (Exception ex) {
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowDeactivated(java.awt.event.WindowEvent evt) {
                formWindowDeactivated(evt);
            }
        });

        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel1MouseReleased(evt);
            }
        });

        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel2MouseReleased(evt);
            }
        });

        jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel3MouseReleased(evt);
            }
        });

        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel4MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseReleased
        if (LogoApp == null) {
            Sistema.Abrir_URL_EnNavegador(Sistema.url_jeffAporta);
        } else {
            new VentanaGráfica() {
                {
                    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    ColorFondo(new Color(255, 255, 255, 128));
                    ActualizarFotograma(LogoApp);
                    RedondearEsquinas = false;
                    MostrarPopMenúEncabezado = false;
                    CambiarTamaño(DIMENSIÓN_FOTOGRAMA, true);
                }

                @Override
                public void windowDeactivated(WindowEvent we) {
                    dispose();
                }
            };
        }
        dispose();
    }//GEN-LAST:event_jLabel1MouseReleased

    private void jLabel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseReleased
        Sistema.Abrir_URL_EnNavegador(Sistema.url_donar_paypal);
        dispose();
    }//GEN-LAST:event_jLabel4MouseReleased

    private void jLabel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseReleased
        if (LogoApp != null) {
            Sistema.Abrir_URL_EnNavegador(Sistema.url_jeffAporta);
        } else {
            Sistema.Abrir_URL_EnNavegador(Sistema.url_jeffDibuja);
        }
        dispose();
    }//GEN-LAST:event_jLabel2MouseReleased

    private void jLabel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseReleased
        Sistema.Abrir_URL_EnNavegador(Sistema.url_facebook);
        dispose();
    }//GEN-LAST:event_jLabel3MouseReleased

    private void formWindowDeactivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeactivated
        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
        }
        dispose();
    }//GEN-LAST:event_formWindowDeactivated

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
