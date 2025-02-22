package _Laboratorio;

public class Eliminar_saltos_de_linea_innecesario extends javax.swing.JFrame {

    public Eliminar_saltos_de_linea_innecesario() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Procesar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String texto_original = jTextArea1.getText();
        String[] renglones = texto_original.split("\n");
        for (int i = 0; i < renglones.length; i++) {
            String renglon = renglones[i];
            if (renglon.startsWith("(") && renglon.endsWith(")")) {
                renglones[i] = "";
            }
            if (renglon.contains("Génesis") && renglon.contains(":")) {
                renglones[i] = "";
            }
            renglones[i] = renglones[i].trim();
            if (i % 20 == 0) {
                System.out.println("1) se han analizado " + i + " renglones de " + renglones.length);
            }
        }
        String texto_modificado = String.join("\n", renglones);
        String[] libros = {
            "Génesis", "ÉXODO", "LEVÍTICO", "NÚMEROS", "DEUTERONOMIO", "JOSUÉ", "JUECES", "RUT",
            "SAMUEL", "REYES", "CRÓNICAS", "ESDRAS", "NEHEMÍAS", "ESTER", "JOB", "SALMO", "PROVERBIOS",
            "ECLESTIASTÉS", "CANTARES", "ISAÍAS", "JEREMÍAS", "LAMENTACIONES", "EZEQUIEL", "DANIEL",
            "OSEAS", "JOEL", "AMÓS", "ABDÍAS", "JONÁS", "MIQUEAS", "NAHUM", "HABACUC", "SOFONÍAS",
            "HAGEO", "ZACARÍAS", "MALAQUÍAS", "MATEO", "MARCOS", "LUCAS", "JUAN", "HECHOS",
            "ROMANOS", "CORINTIOS", "GÁLATAS", "EFESIOS", "FILIPENSES", "COLOSENSES", "TESALONICENSES",
            "TIMOTEO", "TITO", "FILEMÓN", "HEBREOS", "SANTIAGO", "PEDRO", "JUAN", "JUDAS", "APOCALIPSIS"
        };
        for (int i = 0; i < libros.length; i++) {
            for (int j = 151; j > 0; j--) {
                String tmp = texto_modificado;
                texto_modificado = texto_modificado.replace(libros[i] + " " + j, "");
//                if (!texto_modificado.equals(tmp)) {
//                    System.out.println("se ha borrado: " + libros[i] + " " + j);
//                }
                System.out.println("se ha analizado: " + libros[i] + " " + j);
            }
        }

        for (int i = 0; i < 10; i++) {
            String tmp = texto_modificado;
            texto_modificado = texto_modificado.replace(i + "", "");
            if (!texto_modificado.equals(tmp)) {
                System.out.println("el número " + i + " ha sido reemplazado");
            }
        }
        texto_modificado = texto_modificado.replace("\n\n ", "\n");
//        s = s.replace("%separacion%", "\n\n");
        jTextArea2.setText(texto_modificado);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Eliminar_saltos_de_linea_innecesario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Eliminar_saltos_de_linea_innecesario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Eliminar_saltos_de_linea_innecesario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Eliminar_saltos_de_linea_innecesario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Eliminar_saltos_de_linea_innecesario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}
