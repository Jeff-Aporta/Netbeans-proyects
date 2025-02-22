package _Laboratorio;

import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientasGUI.NimbusModificado;

public class inf_usr_youtube extends javax.swing.JFrame {

    static String[] canales = {
        "https://www.youtube.com/channel/UC1zgh-6xm_a-PI49zb6e5HA", //Jeff Aporta
        "https://www.youtube.com/channel/UCz5wDL89bFU_-1y4bkO3WVA", //Audio libro - Audio lectura
        "https://www.youtube.com/channel/UC-Zj0Eg4ZNUf6bVuXc9ntug", //1 Free Music for Commercial Use - YouTube Library
        "https://www.youtube.com/channel/UCWQ_K6KdjkvaTktVVGqXHdQ", //2 Free Music for Commercial Use - YouTube Library
        "https://www.youtube.com/channel/UC0Bdcs3h-gBjA5Fwj6qWeGg", //3 Free Music for Commercial Use - YouTube Library
        "https://www.youtube.com/channel/UC2SDRHQPXxC5Hn13Rl0yyZQ", //4 Free Music for Commercial Use - YouTube Library
        "https://www.youtube.com/channel/UCzizgdzAAI6DXGRK8PQ57SQ", //Joshua
        "https://www.youtube.com/channel/UChbgp4v5VDtySNOOU0JinSA" //Java devone
    };

    static public inf_usr_youtube singleton;

    public static void main(String args[]) {
        try {
            NimbusModificado.CargarNimbus();
        } catch (Exception ex) {
        }
        singleton = new inf_usr_youtube();
    }

    public inf_usr_youtube() {
        initComponents();
        setSize(650, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    static void actualizar() {
        new Thread() {
            @Override
            public void run() {
                try {
                    jLabel1.setText(generar_tabla(canales));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    static String generar_tabla(String... canales) throws Exception {
        String tarjetas = "";
        for (String canal : canales) {
            tarjetas += generar_tarjeta_canal(canal) + "<br>";
        }
        String html = "<html>\n"
                + tabla_fondo(tarjetas)
                + "</html>";
        return html;
    }

    static String tabla_fondo(String tarjetas) {
        String html = "<table width=\"" + (int) (singleton.getWidth() * .9) + "\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td align=\"center\">";
        html += tarjetas;
        html += "</td>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>";
        return html;
    }

    static String generar_tarjeta_canal(String canal) throws Exception {
        String[] renglones = LectoEscrituraArchivos.LeerArchivo_ASCII(
                canal + "/about"
        ).split("\n");
        String nombre = null;
        String imagen = null;
        String vistas = null;
        String fecha_ingreso = null;
        String país = null;
        String suscriptores = null;
        int control_país = -1;
        for (String renglon : renglones) {
            if (nombre == null && renglon.contains("og:title")) {
                nombre = renglon;
                nombre = nombre.substring(0, nombre.lastIndexOf("\""));
                nombre = nombre.substring(nombre.lastIndexOf("\"") + 1);
                nombre = nombre.trim();
            }
            if (imagen == null && renglon.contains("og:image")) {
                imagen = renglon;
                imagen = imagen.substring(0, imagen.lastIndexOf("\""));
                imagen = imagen.substring(imagen.lastIndexOf("\"") + 1);
                imagen = imagen.trim();
            }
            if (vistas == null && renglon.contains("about-stat") && renglon.contains("views")) {
                vistas = LectoEscrituraArchivos.EliminarCódigoHTML_TextoPlano(renglon).trim();
            }
            if (fecha_ingreso == null && renglon.contains("about-stat") && renglon.contains("Joined")) {
                fecha_ingreso = LectoEscrituraArchivos.EliminarCódigoHTML_TextoPlano(renglon).trim();
            }
            if (país == null) {
                switch (control_país) {
                    case -1:
                        if (renglon.contains("country-inline")) {
                            control_país++;
                        }
                        break;
                    case 0:
                        país = renglon.trim();
                        break;
                }
            }
            if (suscriptores == null && renglon.contains("subscribers")) {
                suscriptores = LectoEscrituraArchivos
                        .EliminarCódigoHTML_TextoPlano(renglon)
                        .replace("SubscribeSubscribedUnsubscribe", "")
                        .trim();
                suscriptores += " subscribers";
            }
        }
        if (país==null) {
            país="";
        }
        return "<table cellspacing=\"5\" cellpadding=\"0\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td width=\"150\" height=\"150\"><img src=\"" + imagen + "\" width=\"150\" height=\"150\" /></td>\n"
                + "<td width=\"400\">\n"
                + "<h2>" + nombre + "</h2>" + país + "<br><br>" + suscriptores + "<br>" + vistas + "<br>" + fecha_ingreso + "\n"
                + "</td>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>";
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Visualizador");

        jButton1.setText("Actualizar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Actualizar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton1))
                            .addComponent(jButton2))
                        .addGap(0, 497, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane1.setViewportView(jPanel1);

        getContentPane().add(jScrollPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        actualizar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        actualizar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        actualizar();
    }//GEN-LAST:event_formComponentResized

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private static javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
