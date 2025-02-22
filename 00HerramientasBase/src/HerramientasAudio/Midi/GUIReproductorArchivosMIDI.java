package HerramientasAudio.Midi;

import HerramientaArchivos.ArrastrarArchivos.ArrastrarArchivos;
import HerramientaArchivos.*;
import HerramientasColor.Filtro_Color;
import HerramientasGUI.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import javax.swing.*;

public final class GUIReproductorArchivosMIDI extends javax.swing.JFrame {

    boolean ModificandoPosición = false;
    boolean reproduciendo = false;
    int ArchivoEnCurso = -1;

    float tonoBotones = .57f;

    ReproductorArchivosMIDI ReproductorMidi = new ReproductorArchivosMIDI();
    ArrayList<String> RutasMidi = new ArrayList<>();
    Thread refrescador = new Thread() {
        @Override
        public void run() {
            while (true) {
                try {
                    if (ReproductorMidi.EstaReproduciendo()) {
                        if (!ModificandoPosición) {
                            jSlider1.setValue((int) (jSlider1.getMaximum() * ReproductorMidi.ObtenerUbicación_Porcentaje()));
                        }
                        jLabel1.setText(ReproductorMidi.tiempoUbicación());
                    } else {
                        if (ReproductorMidi.ObtenerUbicación_Porcentaje() == 1 && reproduciendo) {
                            if (ArchivoEnCurso + 1 > jList1.getModel().getSize() - 1) {
                                reproduciendo = !reproduciendo;
                                ActualizarBotónInicio();
                            } else {
                                if (jSlider1.getValue() > 0) {
                                    jButton2ActionPerformed(null);
                                }
                            }
                        }
                        sleep(1000);
                    }
                    sleep(1000 / 20);
                } catch (Exception ex) {
                }
            }
        }
    };

    JSlider barra() {
        return new JSlider();
    }

    JPanel fondo() {
        return new JPanel() {
            @Override
            public void paint(Graphics grphcs) {
                Graphics2D g = (Graphics2D) grphcs;
                GradientPaint paint = new GradientPaint(
                        new Point2D.Double(0, 0),
                        Color.GRAY,
                        new Point2D.Double(0, getHeight() / 2.3),
                        new Color(178, 219, 255),
                        true
                );
                g.setPaint(paint);
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paint(grphcs);
            }
        };
    }

    public static void main(String args[]) {
        try {
            NimbusModificado.CargarNimbus();
        } catch (Exception ex) {
        }
        new GUIReproductorArchivosMIDI().setVisible(true);
    }

    public GUIReproductorArchivosMIDI() {
        initComponents();
        setTitle("Reproductor MIDI");
        cargarIcono();
        CargarImagenesBotones();
        refrescador.start();
        Maquillaje_SwingGUI.Transparencia_JButtons(jPanel1);
        Maquillaje_SwingGUI.Transparencia_JPanels(jPanel1);
        setLocationRelativeTo(null);
        ArrastrarArchivos.Añadir(this, (files) -> {
            extraerMidis(files);
        });
    }

    void cargarIcono() {
        BufferedImage icono = BibliotecaArchivos.Imagenes.Botones.Inicio();
        Image escala = icono.getScaledInstance(64, -1, Image.SCALE_AREA_AVERAGING);
        setIconImage(escala);
    }

    void CargarImagenesBotones() {
        ActualizarBotónInicio();
        asignarIconoBotón(BibliotecaArchivos.Imagenes.Botones.Adelante(), jButton2);
        asignarIconoBotón(BibliotecaArchivos.Imagenes.Botones.Atras(), jButton4);
        asignarIconoBotón(BibliotecaArchivos.Imagenes.Botones.Reiniciar(), jButton5);
        asignarIconoBotón(BibliotecaArchivos.Imagenes.Botones.Basura(), jButton3);
        asignarIconoBotón(BibliotecaArchivos.Imagenes.Botones.Añadir(), jButton6);
    }

    void asignarIconoBotón(BufferedImage icono, JButton btn) {
        {
            Graphics2D g = icono.createGraphics();
            g.setComposite(AlphaComposite.Clear);
            g.setStroke(new BasicStroke(23));
            g.drawOval(0, 0, icono.getWidth(), icono.getHeight());
        }
        Image escala = icono.getScaledInstance(70, -1, Image.SCALE_AREA_AVERAGING);
        icono = new BufferedImage(escala.getWidth(null), escala.getHeight(null), 2);
        icono.getGraphics().drawImage(escala, 0, 0, null);
        icono = Filtro_Color.Monocromatizar(icono, tonoBotones);
        Maquillaje_SwingGUI.AñadirImgBotón_Estilo2(btn, icono);
    }

    void ActualizarLista() {
        jLabel4.setText(RutasMidi.size() + "");
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            public int getSize() {
                return RutasMidi.size();
            }

            public String getElementAt(int i) {
                return RutasMidi.get(i);
            }
        });
        if (ArchivoEnCurso >= 0 && !RutasMidi.isEmpty()) {
            jList1.setSelectedIndex(ArchivoEnCurso);
        }
    }

    void extraerMidis(String[] rutas) {
        new Thread() {
            @Override
            public void run() {
                RutasMidi = extraerMidis(rutas, RutasMidi);
                ActualizarLista();
            }
        }.start();
    }

    ArrayList<String> extraerMidis(String[] rutas, ArrayList<String> Midis) {
        for (String ruta : rutas) {
            try {
                File f = new File(ruta);
                if (f.isDirectory()) {
                    File[] archivos = f.listFiles();
                    String[] rutasDir = new String[archivos.length];
                    for (int i = 0; i < archivos.length; i++) {
                        rutasDir[i] = archivos[i].getPath();
                    }
                    extraerMidis(rutasDir, Midis);
                    continue;
                }
                if (ruta.toLowerCase().endsWith(".mid")) {
                    Midis.add(ruta);
                    jLabel4.setText(RutasMidi.size() + "");
                }
            } catch (Exception e) {
            }
        }
        return Midis;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = fondo();
        jSlider1 = new javax.swing.JSlider();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jSlider1.setMaximum(1000);
        jSlider1.setValue(0);
        jSlider1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jSlider1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jSlider1MouseReleased(evt);
            }
        });

        jButton1.setText("Reproducir/pausar");
        jButton1.setToolTipText("Iniciar/Pausar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Siguiente");
        jButton2.setToolTipText("Siguiente");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("00:00:00.0");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("00:00:00.0");

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jList1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jList1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jButton3.setText("Vaciar");
        jButton3.setToolTipText("Eliminar lista");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Anterior");
        jButton4.setToolTipText("Anterior");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Reiniciar");
        jButton5.setToolTipText("Reiniciar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Agregar");
        jButton6.setToolTipText("Agregar ruta a la lista");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jRadioButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jRadioButton1.setText("Siempre visible");
        jRadioButton1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jRadioButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Creditos");
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel3MouseReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (jList1.getModel().getSize() < 1) {
            return;
        }
        if (ReproductorMidi.EstaReproduciendo()) {
            jButton1.doClick();
        }
        jSlider1.setValue(0);
        reproduciendo = false;
        ActualizarBotónInicio();
        ReproductorMidi.Cerrar();
        RutasMidi.clear();
        ActualizarLista();
    }//GEN-LAST:event_jButton3ActionPerformed

    void ActualizarBotónInicio() {
        if (reproduciendo) {
            asignarIconoBotón(BibliotecaArchivos.Imagenes.Botones.Pausa(), jButton1);
            refrescador.resume();
        } else {
            asignarIconoBotón(BibliotecaArchivos.Imagenes.Botones.Inicio(), jButton1);
            refrescador.suspend();
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jList1.getModel().getSize() < 1) {
            return;
        }
        reproduciendo = !reproduciendo;
        ActualizarBotónInicio();
        if (reproduciendo) {
            if (ReproductorMidi.TieneCargadoUnArchivo()) {
                if (ReproductorMidi.ObtenerUbicación_Porcentaje() >= .99) {
                    jButton5.doClick();
                }
                ReproductorMidi.Reproducir();
            } else {
                try {
                    if (jList1.getSelectedIndex() == -1) {
                        jList1.setSelectedIndex(0);
                    }
                    ArchivoEnCurso = jList1.getSelectedIndex();
                    AsignarMidi();
                    ReproductorMidi.Reproducir();
                } catch (Exception ex) {
                }
            }
        } else {
            ReproductorMidi.Pausar();
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            if (jList1.getModel().getSize() < 1) {
                return;
            }
            if (!ReproductorMidi.TieneCargadoUnArchivo()) {
                return;
            }
            if (ArchivoEnCurso + 1 > jList1.getModel().getSize() - 1) {
                return;
            }
            jSlider1.setValue(0);
            jList1.setSelectedIndex(++ArchivoEnCurso);
            AsignarMidi();
            reproduciendo = true;
            if (!ReproductorMidi.EstaReproduciendo()) {
                ReproductorMidi.Reproducir();
            }
            ActualizarBotónInicio();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            if (jList1.getModel().getSize() < 1) {
                return;
            }
            if (!ReproductorMidi.TieneCargadoUnArchivo()) {
                return;
            }
            if (ArchivoEnCurso - 1 < 0) {
                return;
            }
            jSlider1.setValue(0);
            jList1.setSelectedIndex(--ArchivoEnCurso);
            AsignarMidi();
            reproduciendo = true;
            if (!ReproductorMidi.EstaReproduciendo()) {
                ReproductorMidi.Reproducir();
            }
            ActualizarBotónInicio();
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    void AsignarMidi() throws Exception {
        ReproductorMidi.Asignar_Midi(jList1.getSelectedValue());
        jLabel2.setText(ReproductorMidi.CadenaTiempoDuración());
    }

    private void jSlider1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseReleased
        ModificandoPosición = false;
        if (ReproductorMidi.EstaReproduciendo()) {
            float p = (float) jSlider1.getValue() / jSlider1.getMaximum();
            ReproductorMidi.ModificarUbicación_Porcentaje(p);
        }
    }//GEN-LAST:event_jSlider1MouseReleased

    private void jSlider1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MousePressed
        ModificandoPosición = true;
    }//GEN-LAST:event_jSlider1MousePressed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        ReproductorMidi.Reiniciar();
        jSlider1.setValue(0);
        if (!ReproductorMidi.EstaReproduciendo() && !reproduciendo) {
            jButton1.doClick();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String entrada = JOptionPane.showInputDialog(this, "Ingrese las rutas, separelas con punto y coma (;)");
        if (entrada == null) {
            return;
        }
        entrada = entrada.trim();
        if (entrada.isEmpty()) {
            return;
        }
        String[] rutas = entrada.split(";");
        extraerMidis(rutas);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (evt.getClickCount() < 2) {
            return;
        }
        IniciarReproducciónEvento();
    }//GEN-LAST:event_jList1MouseClicked

    void IniciarReproducciónEvento() {
        float p = ReproductorMidi.ObtenerUbicación_Porcentaje();
        if (ArchivoEnCurso == jList1.getSelectedIndex() && p > 0) {
            return;
        }
        try {
            AsignarMidi();
            ArchivoEnCurso = jList1.getSelectedIndex();
            reproduciendo = true;
            if (!ReproductorMidi.EstaReproduciendo()) {
                ReproductorMidi.Reproducir();
            }
            ActualizarBotónInicio();
        } catch (Exception ex) {
        }
    }

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        setAlwaysOnTop(jRadioButton1.isSelected());
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jLabel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseReleased
        Creditos.Generar();
    }//GEN-LAST:event_jLabel3MouseReleased

    private void jList1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList1KeyReleased
        int ascii = evt.getKeyCode();
        switch (ascii) {
            case KeyEvent.VK_ENTER:
                IniciarReproducciónEvento();
                return;
            case KeyEvent.VK_DELETE:
                if (jList1.getSelectedIndex() == -1) {
                    return;
                }
                if (ArchivoEnCurso == jList1.getSelectedIndex()) {
                    ReproductorMidi.Cerrar();
                    reproduciendo = false;
                    ActualizarBotónInicio();
                    String cero = "00:00:00.0";
                    jLabel1.setText(cero);
                    jLabel2.setText(cero);
                    jSlider1.setValue(0);
                }
                RutasMidi.remove(jList1.getSelectedIndex());
                ActualizarLista();
                if (!RutasMidi.isEmpty()) {
                    int size = RutasMidi.size();
                    ArchivoEnCurso = ArchivoEnCurso >= size ? size - 1 : ArchivoEnCurso;
                    jList1.setSelectedIndex(ArchivoEnCurso);
                }
                return;
        }
    }//GEN-LAST:event_jList1KeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private static javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private static javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JSlider jSlider1;
    // End of variables declaration//GEN-END:variables
}
