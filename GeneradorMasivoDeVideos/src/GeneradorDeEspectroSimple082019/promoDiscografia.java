package GeneradorDeEspectroSimple082019;

import HerramientaArchivos.ArrastrarArchivos.ArrastrarArchivos;
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientaDeImagen.Filtros_Lineales;
import HerramientasGUI.NimbusModificado;
import HerramientasGUI.Presentador;
import HerramientasGUI.VentanaGráfica;
import HerramientasMatemáticas.Matemática;
import HerramientasSistema.Sistema;
import JAVH.FFmpeg;
import JID3.Etiquetas;
import static Principal.ProyectoVideo.outFramesFormat;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class promoDiscografia extends javax.swing.JFrame {

    public static ArrayList<BufferedImage> imagenes = new ArrayList<>();

    public static final String outFramesFormat = "\\%07d.jpg";
    public static final String outFramesExtensión = outFramesFormat.substring(
            outFramesFormat.lastIndexOf('.') + 1
    );

    static VentanaGráfica ventana;

    static int duracion;
    static Etiquetas etiquetas;

    public static void main(String[] args) throws Exception {
        NimbusModificado.CargarNimbus();
        promoDiscografia p = new promoDiscografia();
        while (p.isVisible()) {
            Thread.sleep(100);
        }
        ventana = new VentanaGráfica();
        String ruta = "A:\\YAL\\Generos\\I_Don_t_See_the_Branches_I_See_the_Leaves.mp3";
        duracion = 616 / imagenes.size();
        etiquetas = new Etiquetas(ruta, false);
        etiquetas.SEGUNDOS = 616;
        GenerarSencillo(
                ruta,
                "A:\\YAL\\discografia.mp4"
        );
        System.exit(0);
    }

    public promoDiscografia() {//<editor-fold defaultstate="collapsed" desc="Constructor »">
        initComponents();
        setSize(600, 600);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        ArrastrarArchivos.Añadir((String[] rutas) -> {
            for (String ruta : rutas) {
                BufferedImage img = LectoEscrituraArchivos.cargar_imagen(ruta);
                if (img == null) {
                    continue;
                }
                JLabel lbl = new JLabel() {
                    @Override
                    public void paint(Graphics grphcs) {
                        super.paint(grphcs);
                        BufferedImage fnd = new BufferedImage(getWidth(), getHeight(), 2);
                        Filtros_Lineales.Ajuste_Ajustar(img, fnd);
                        grphcs.drawImage(fnd, 0, 0, null);
                    }
                };
                jPanel1.add(lbl);
                SwingUtilities.updateComponentTreeUI(this);
                imagenes.add(img);
                System.out.println(ruta);
            }
        }, this);
        setVisible(true);
    }//</editor-fold>

    static String LimpiarNombreDeCaracteresNoVálidos(String Nombre) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Nombre = Nombre.replace("'", "");
        Nombre = Nombre.replace("\"", "");
        Nombre = Nombre.replace("/", " ");
        Nombre = Nombre.replace("\\", " ");
        Nombre = Nombre.replace("?", " ");
        Nombre = Nombre.replace("&", "and");
        Nombre = Nombre.replace(":", " ");
        Nombre = Nombre.replace("*", " ");
        Nombre = Nombre.replace("<", " ");
        Nombre = Nombre.replace(">", " ");
        Nombre = Nombre.replace("|", " ");
        return Nombre;
    }//</editor-fold>

    static void GenerarSencillo(final String rutaAudio, final String rutaVideoSalida) throws Exception {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        File CarpetaFotogramas = new File(Sistema.ruta_jeffAporta_tmp + "\\Fotogramas " + Matemática.AleatorioEnteroEntre(1, 9999));
        //<editor-fold defaultstate="collapsed" desc="Eliminación de posible duplicado de la carpeta temporal »">
        try {
            if (CarpetaFotogramas.exists()) {
                for (File listFile : CarpetaFotogramas.listFiles()) {
                    listFile.delete();
                }
                CarpetaFotogramas.delete();
            }
        } catch (Exception e) {
        }
        //</editor-fold>
        CarpetaFotogramas.mkdirs();

        {
            //<editor-fold defaultstate="collapsed" desc="Generar secuencia animada para el video »">

            BufferedImage imagenFondo = new BufferedImage(
                    1280,
                    720,
                    BufferedImage.TYPE_INT_ARGB
            ) {//<editor-fold defaultstate="collapsed" desc="Dibujado de la imagen de fondo »">
                {
                    Graphics2D g = createGraphics();
                    //<editor-fold defaultstate="collapsed" desc="Fondo »">
                    {
                        //<editor-fold defaultstate="collapsed" desc="Color de fondo">
                        Color colorFondo = new Color(0x444444);
                        g.setColor(colorFondo);
                        g.fillRect(0, 0, getWidth(), getHeight());
                        //</editor-fold>
                    }
                    {
                        //<editor-fold defaultstate="collapsed" desc="Texturizado del fondo »">
                        {
                            //<editor-fold defaultstate="collapsed" desc="Cuadricula de fondo »">
                            g.setColor(new Color(0x33000000, true));
                            int w = 20;
                            int h = 20;
                            for (int i = 0; i < getWidth() / w; i++) {
                                for (int j = 0; j < getHeight() / h; j++) {
                                    if ((i + j) % 2 == 0) {
                                        g.fillRect(i * w, j * h, w, h);
                                    }
                                }
                            }
                            //</editor-fold>
                        }
                        {
                            //<editor-fold defaultstate="collapsed" desc="Sombra vertical »">
                            g.setPaint(
                                    new GradientPaint(
                                            0,
                                            0,
                                            new Color(0, true),
                                            0,
                                            getHeight(),
                                            new Color(0x99000000, true)
                                    )
                            );
                            g.fillRect(0, 0, getWidth(), getHeight());
                            //</editor-fold>
                        }
                        //</editor-fold>
                    }
                    //</editor-fold>
                }
            };//</editor-fold>

            ventana.ActualizarFotograma(imagenFondo, "imagen de fondo generada", true);
            int ContadorDeFotogramasGuardados = 0;

            for (int i = 0; i < imagenes.size(); i++) {
                BufferedImage fotograma = Filtros_Lineales.Clonar(imagenFondo);
                Filtros_Lineales.Ajuste_Ajustar(imagenes.get(i), fotograma);
                File archivo = new File(
                        CarpetaFotogramas.getPath()
                        + new Formatter().format(
                                outFramesFormat,
                                ContadorDeFotogramasGuardados++
                        )
                );
                LectoEscrituraArchivos.exportar_imagen(fotograma, archivo);
                ventana.ActualizarFotograma(fotograma);
                ventana.setTitle((i + 1) + "/" + imagenes.size());

                for (int j = 1; j < duracion; j++) {
                    File copia = new File(
                            CarpetaFotogramas.getPath() + new Formatter().format(outFramesFormat, ContadorDeFotogramasGuardados++)
                    );
                    Sistema.DuplicarArchivo(archivo, copia);
                }
            }
            //</editor-fold>
        }

        System.err.println(
                "inicia la rasterización");
        rasterizar(CarpetaFotogramas, rutaAudio, rutaVideoSalida);

        System.err.println(
                "---------------------------------FINALIZADO");
    }//</editor-fold>

    public static void rasterizar(final File rutaFotogramas, final String rutaAudio, final String videoSalida) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            FFmpeg FFmpeg = new FFmpeg(rutaFotogramas.getPath() + outFramesFormat, videoSalida);
            FFmpeg.HacerVideoConSecuenciaDeImagenesYAudio(rutaAudio, 1, false);
            //<editor-fold defaultstate="collapsed" desc="Borrar carpeta de fotogramas »">
            try {
                for (int j = 0; j < 2; j++) {
                    for (File listFile : rutaFotogramas.listFiles()) {
                        listFile.delete();
                    }
                }
            } catch (Exception ex) {
            }
            rutaFotogramas.delete();
            //</editor-fold>
        } catch (Exception ex) {
        }
    }//</editor-fold>

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setLayout(new java.awt.GridLayout(3, 0));

        jButton1.setText("Empezar");
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
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 517, Short.MAX_VALUE)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        hide();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
