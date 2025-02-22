package _Laboratorio;

import HerramientaArchivos.ArrastrarArchivos.ArrastrarArchivos;
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientaDeImagen.Filtro_Convolución;
import HerramientaDeImagen.Filtros_Lineales;
import HerramientasGUI.NimbusModificado;
import HerramientasGUI.Presentador;
import java.awt.image.BufferedImage;

public class imagen_miniatura_youtube extends javax.swing.JFrame {

    public static void main(String[] args) {
        NimbusModificado.CargarTemaOscuroNimbus();
        NimbusModificado.CargarNimbus();
        imagen_miniatura_youtube imagen = new imagen_miniatura_youtube();
    }

    BufferedImage imagen;
    String ruta;
    Presentador presentador;

    public imagen_miniatura_youtube() {
        initComponents();
        presentador = (Presentador) jLabel1;
        presentador.setText("");
        setSize(600, 400);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setVisible(true);
        ArrastrarArchivos.Añadir((rutas) -> {
            ruta = rutas[0];
            imagen = LectoEscrituraArchivos.cargar_imagen(ruta);
            presentador.ActualizarFotograma(generarImagenMiniatura());
        }, this);
    }

    BufferedImage generarImagenMiniatura() {
        BufferedImage retorno = new BufferedImage(1280, 720, 2);
        Filtros_Lineales.Ajuste_Rellenar(imagen, retorno);
        for (int i = 0; i < 15; i++) {
            retorno = Filtro_Convolución.Desenfoque_Vertical(retorno, 10);
            retorno = Filtro_Convolución.Desenfoque_Horizontal(retorno, 10);
        }
        Filtros_Lineales.Ajuste_Ajustar(imagen, retorno);
        return retorno;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new Presentador();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Presentador");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton1.setText("Guardar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 494, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        LectoEscrituraArchivos.exportar_imagen(presentador.ÚltimaImagenCargada, ruta);
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

}
