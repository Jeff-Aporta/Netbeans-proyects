package JID3;

import HerramientaArchivos.ArrastrarArchivos.ArrastrarArchivos;
import HerramientaArchivos.BibliotecaArchivos;
import static HerramientaArchivos.BibliotecaArchivos.*;
import static HerramientaDeImagen.Filtros_Lineales.EscalarGranCalidad;
import HerramientasGUI.Creditos;
import static JID3.AsignarCoverDeCarpetaAMP3.Eliminar_tmp;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class VentanaAsignarCover extends javax.swing.JFrame {

    public VentanaAsignarCover() {
        initComponents();
        setTitle("Coverador");
        try {
            setIconImage(EscalarGranCalidad(BibliotecaArchivos.Imagenes.Logos.JeffAporta(), 32, -1));
        } catch (Exception e) {
        }
        setSize(721, 351);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        MostrarLogo();
        ArrastrarArchivos.Añadir(this, (String[] rutas) -> {
            if (rutas.length > 1) {
                return;
            }
            File Elemento = new File(rutas[0]);
            if (!Elemento.exists()) {
                return;
            }
            if (!Elemento.isDirectory()) {
                if (!Elemento.getName().toLowerCase().endsWith(".mp3")) {
                    return;
                }
            }
            jTextField1.setText(Elemento.getPath());
        });
    }

    public void MostrarLogo() {
        try {
            BufferedImage logo = LogoApp();
            jLabel1.setIcon(new ImageIcon(EscalarGranCalidad(logo, 300, 300)));
        } catch (Exception ex) {
        }
    }

    public static BufferedImage LogoApp() throws Exception {
        return BibliotecaArchivos.añadir_biblioteca_appdata_img(
                "Logo\\Programas",
                "https://docs.google.com/drawings/d/e/2PACX-1vTX-mcCopryMSM8XGZ6gvBxQJKdK"
                + "wVZY5_JYoClTjyqx0hu2UaDJ5isnajjmP5j7rjeD6vAcKyE9xJt/pub?w=720&h=720",
                "coverador.png"
        );
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jButton3 = new javax.swing.JButton();
        jRadioButton2 = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(721, 351));
        getContentPane().setLayout(null);

        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 11, 300, 300);
        getContentPane().add(jTextField1);
        jTextField1.setBounds(316, 25, 395, 28);

        jLabel2.setText("Directorio");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(316, 11, 150, 14);

        jButton1.setText("Empezar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(316, 58, 80, 23);

        jList1.setEnabled(false);
        jScrollPane1.setViewportView(jList1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(504, 58, 207, 225);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(jLabel3);
        jLabel3.setBounds(316, 87, 184, 195);

        jButton2.setText("Creditos");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(316, 288, 120, 23);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("0/0");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(544, 290, 130, 20);

        jRadioButton1.setText("Salir al finalizar");
        jRadioButton1.setEnabled(false);
        getContentPane().add(jRadioButton1);
        jRadioButton1.setBounds(450, 290, 110, 23);

        jButton3.setText("Borrar tmp");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(400, 58, 100, 23);

        jRadioButton2.setText("Siempre visible");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jRadioButton2);
        jRadioButton2.setBounds(600, 5, 110, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            AsignarCoverDeCarpetaAMP3.EmpezarCoverización(jTextField1.getText());
            jButton1.setEnabled(false);
            jButton3.setEnabled(false);
            jTextField1.setEnabled(false);
            jRadioButton1.setEnabled(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFrame frame = this;
        new Thread() {
            @Override
            public void run() {
                try {
                    Eliminar_tmp(new File(jTextField1.getText()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }.start();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        setAlwaysOnTop(jRadioButton2.isSelected());
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    public javax.swing.JButton jButton3;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JList<String> jList1;
    public javax.swing.JRadioButton jRadioButton1;
    public javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
