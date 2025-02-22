package HerramientaDeImagen.ModoFusión;

//<editor-fold defaultstate="collapsed" desc="Importe de librerias ●">
import HerramientaArchivos.ArrastrarArchivos.ArrastrarArchivos;
import HerramientaArchivos.BibliotecaArchivos;
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientaDeImagen.Filtros_Lineales;
import HerramientaDeImagen.ModoFusión.ModoPintura.BaseModoPintura;
import HerramientasGUI.Maquillaje_SwingGUI;
import HerramientasGUI.Presentador;
import HerramientasGUI.Personalizador_JList;
import HerramientasMatemáticas.Matemática;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Paint;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
//</editor-fold>

public class GUI_PruebaModoPinturaCompuesto extends GUI_Aux {

    JTextField[][] textfields;

    boolean cambiandoModo = true;

    ArrayList<String> ListaCompuesta = new ArrayList<>();
    ListaID ListaID = new ListaID();

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc="Selección del Look and feel">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
        }
        //</editor-fold>
        new GUI_PruebaModoPinturaCompuesto();
    }

    public GUI_PruebaModoPinturaCompuesto() {//<editor-fold defaultstate="collapsed" desc="Implementación de código del constructor »">
        //<editor-fold defaultstate="collapsed" desc="inicialización de la GUI">
        initComponents();
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        //</editor-fold>

        jLabel5.setText("");

        textfields = ObtenerMatrizGUI_CL();

        ActualizarJtext();

        ListenersJText();
        ListenersArrastreDeArchivos();

        InicializaciónDeLosPresentadores();

        Escenario = new Fotograma();
        Escenario.centrarCapas();

        jList2.setCellRenderer(new Personalizador_JList());
        GUI_PruebaModoPintura.LlenarTablaDeModeloPintura(ListaID);
        Maquillaje_SwingGUI.AsignarLista_JList(jList2, ListaID.obtenerListaNombres());

        Maquillaje_SwingGUI.AsignarLista_JList(jList3, ListaCompuesta);
        jList3.setSelectedIndex(0);

        ModoFusión = new ModoPinturaCompuesto();

        SincronizarGUIConModeloPintura();
        setVisible(true);
        cambiandoModo = false;
    }//</editor-fold>

    void SincronizarGUIConModeloPintura() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            //<editor-fold defaultstate="collapsed" desc="Se avisará si el modo de fusión genera salidas RGB">
            Presentador p = (Presentador) jLabel5;
            if (ModoFusión.RiesgoSalida) {
                p.ActualizarFotograma(BibliotecaArchivos.Imagenes.Iconos.Alerta("  Hay riesgo\n  de salida\n  RGB")
                );
            } else {
                p.ActualizarFotograma(BibliotecaArchivos.Imagenes.Iconos.Correcto("  No Hay\n  riesgo de\n  salida RGB")
                );
            }
            //</editor-fold>
            if (jList2.getSelectedValue() != null) {
                PresentadorEscenario.setText("  " + jList2.getSelectedValue().replace("/t/", ""));
            } else {
                PresentadorEscenario.setText("  Normal");
            }
            jSlider8.setEnabled(false);
            jSlider7.setEnabled(false);
            jSlider6.setEnabled(false);
            jSlider2.setEnabled(false);
            BaseModoPintura ModPint = (BaseModoPintura) ((ModoPinturaCompuesto) ModoFusión).ModosPintura.get(
                    jList3.getSelectedIndex()
            );
            switch (ModPint.AplicacionesUsadas()) {
                case 4:
                    jSlider8.setEnabled(true);
                    jSlider8.setValue((int) (ModPint.Aplicación4 * jSlider8.getMaximum()));
                case 3:
                    jSlider7.setEnabled(true);
                    jSlider7.setValue((int) (ModPint.Aplicación3 * jSlider7.getMaximum()));
                case 2:
                    jSlider6.setEnabled(true);
                    jSlider6.setValue((int) (ModPint.Aplicación2 * jSlider6.getMaximum()));
                case 1:
                    jSlider2.setEnabled(true);
                    jSlider2.setValue((int) (ModPint.Aplicación * jSlider2.getMaximum()));
            }
            jSlider3.setValue((int) (ModPint.AplicaciónRojo * jSlider3.getMaximum()));
            jSlider4.setValue((int) (ModPint.AplicaciónVerde * jSlider4.getMaximum()));
            jSlider5.setValue((int) (ModPint.AplicaciónAzul * jSlider5.getMaximum()));
            ActualizarJtext();
        } catch (Exception e) {
        }
    }//</editor-fold>

    void SincronizarModoPinturaConGUI() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            if (!cambiandoModo) {
                ModoFusión.Opacidad = 1d * jSlider1.getValue() / jSlider1.getMaximum();
                BaseModoPintura ModPint = (BaseModoPintura) ((ModoPinturaCompuesto) ModoFusión).ModosPintura.get(
                        jList3.getSelectedIndex()
                );
                ModPint.AplicaciónRojo = 1d * jSlider3.getValue() / jSlider3.getMaximum();
                ModPint.AplicaciónVerde = 1d * jSlider4.getValue() / jSlider4.getMaximum();
                ModPint.AplicaciónAzul = 1d * jSlider5.getValue() / jSlider5.getMaximum();
                switch (ModPint.AplicacionesUsadas()) {
                    case 4:
                        ModPint.Aplicación4 = 1d * jSlider8.getValue() / jSlider8.getMaximum();
                    case 3:
                        ModPint.Aplicación3 = 1d * jSlider7.getValue() / jSlider7.getMaximum();
                    case 2:
                        ModPint.Aplicación2 = 1d * jSlider6.getValue() / jSlider6.getMaximum();
                    case 1:
                        ModPint.Aplicación = 1d * jSlider2.getValue() / jSlider2.getMaximum();
                }
            }
            ModoFusión.Modificar_AlphaDiscriminador((byte) jComboBox1.getSelectedIndex());
            switch (jComboBox2.getSelectedItem().toString()) {
                case "Extremos":
                    ModoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_A_EXTREMOS);
                    break;
                case "Extremos abs":
                    ModoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_A_EXTREMOS_ABS);
                    break;
                case "Circular":
                    ModoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_CIRCULAR);
                    break;
                case "Circular abs":
                    ModoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_CIRCULAR_ABS);
                    break;
                case "Reflejo":
                    ModoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_REFLEJO);
                    break;
                case "Reflejo Inv":
                    ModoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_REFLEJO_INV);
                    break;
                case "Mascara de bits":
                    ModoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_MASCARA_BITS);
                    break;
                case "Nulidad por salida":
                    ModoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_NULIDAD_POR_SALIDA);
                    break;
                case "Sin definir":
                    ModoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_SIN_DEFINIR);
                    break;
            }
        } catch (Exception e) {
        }
        ActualizarEscenario();
    }//</editor-fold>

    void InicializaciónDeLosPresentadores() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        PresentadorEscenario = (Presentador) jLabel1;
        PresentadorEscenario.setText("");
        PresentadorEscenario.TIPO_AJUSTE_FONDO = Filtros_Lineales.AJUSTE_MOSAICO;

        PresentadorPatrónFondo = (Presentador) jLabel4;
        PresentadorPatrónFondo.TIPO_AJUSTE_FONDO = Filtros_Lineales.AJUSTE_MOSAICO;

        PresentadorCapaSuperior = (Presentador) jLabel2;
        PresentadorCapaSuperior.Borde_Fotograma(Color.GRAY, 2).setText("");
        PresentadorCapaSuperior.ColorFondo(new Color(0x13000000, true));

        PresentadorCapaInferior = (Presentador) jLabel3;
        PresentadorCapaInferior.Borde_Fotograma(Color.LIGHT_GRAY, 2).setText("");

        jLabel4MouseReleased(null);
    }//</editor-fold>

    void ActualizarJtext() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        boolean b = true;
        int n = 0;
        try {
            BaseModoPintura ModPint = (BaseModoPintura) ((ModoPinturaCompuesto) ModoFusión).ModosPintura.get(
                    jList3.getSelectedIndex()
            );
            if (ModPint instanceof ModoPintura.Simple.Matriz.TL.BaseMatrizTL) {
                n = 9;
            }
            if (ModPint instanceof ModoPintura.Simple.Matriz.CL.BaseMatrizCL) {
                n = 18;
            }
        } catch (Exception e) {
        }
        for (JTextField[] textfield : textfields) {
            for (JTextField jTextField : textfield) {
                b = n-- > 0;
                if (b) {
                    jTextField.setBackground(Color.WHITE);
                } else {
                    jTextField.setBackground(new Color(0xcccccc));
                }
                jTextField.setEditable(b);
            }
        }
        ActualizarModeloBasePintura_Matriz();

    }//</editor-fold>

    void ListenersArrastreDeArchivos() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        ArrastrarArchivos.Añadir((rutas) -> {
                    BufferedImage NuevaImagen = LectoEscrituraArchivos.cargar_imagen(rutas[0]);
                    if (NuevaImagen == null) {
                        return;
                    }
                    Escenario.ModificarCapas(
                            NuevaImagen, null
                    );
                    Escenario.centrarCapas();
                },
                jLabel1, jLabel2
        );
        ArrastrarArchivos.Añadir(jLabel3, (rutas) -> {
            BufferedImage NuevaImagen = LectoEscrituraArchivos.cargar_imagen(rutas[0]);
            if (NuevaImagen == null) {
                return;
            }
            Escenario.ModificarCapas(
                    null, NuevaImagen
            );
            Escenario.centrarCapas();
        });
    }//</editor-fold>

    void ListenersJText() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (JTextField[] textfield : textfields) {
            for (JTextField jTextField : textfield) {
                jTextField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent evt) {
                        if (!jTextField.isEditable()) {
                            return;
                        }
                        try {
                            if (!jTextField.getText().equals("")) {
                                double a = Double.parseDouble(jTextField.getText());
                            }
                            jTextField.setBackground(Color.WHITE);
                            ActualizarModeloBasePintura_Matriz();
                        } catch (Exception e) {
                            jTextField.setBackground(Color.PINK);
                        }
                    }
                });
            }
        }
    }//</editor-fold>

    void ActualizarModeloBasePintura_Matriz() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            BaseModoPintura ModPint = (BaseModoPintura) ((ModoPinturaCompuesto) ModoFusión).ModosPintura.get(
                    jList3.getSelectedIndex()
            );
            if (ModPint instanceof ModoPintura.Simple.Matriz.BaseMatriz) {
                ModoPintura.Simple.Matriz.BaseMatriz CL = (ModoPintura.Simple.Matriz.BaseMatriz) ModPint;
                CL.actualizarMatriz(ObtenerMatriz());
                ActualizarEscenario();
            }
        } catch (Exception e) {
        }
    }//</editor-fold>

    float[][] ObtenerMatriz() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        float[][] retorno = new float[6][3];
        for (int c = 0; c < retorno[0].length; c++) {
            for (int f = 0; f < retorno.length; f++) {
                try {
                    retorno[f][c] = Float.parseFloat(textfields[f][c].getText());
                } catch (Exception e) {
                    retorno[f][c] = 0;
                }
            }
        }
        return retorno;
    }//</editor-fold>

    JTextField[][] ObtenerMatrizGUI_CL() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        JTextField[][] retorno = new JTextField[6][3];
        int f = 0;
        int c = 0;
        for (Component component : jPanel2.getComponents()) {
            retorno[f][c] = (JTextField) component;
            c++;
            if (c == 3) {
                c = 0;
                f++;
            }
        }
        return retorno;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Código generado para la GUI con ayuda de Netbeans">
    //<editor-fold defaultstate="collapsed" desc="Inicialización de las variables de la GUI">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new Presentador();
        jLabel3 = new Presentador();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new Presentador();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new Presentador();
        jSlider1 = new javax.swing.JSlider();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jSlider3 = new javax.swing.JSlider();
        jSlider4 = new javax.swing.JSlider();
        jSlider5 = new javax.swing.JSlider();
        jLabel5 = new Presentador();
        jPanel6 = new javax.swing.JPanel();
        jSlider2 = new javax.swing.JSlider();
        jSlider6 = new javax.swing.JSlider();
        jSlider7 = new javax.swing.JSlider();
        jSlider8 = new javax.swing.JSlider();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jPanel9 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Matriz de transformación"));
        jPanel2.setLayout(new java.awt.GridLayout(6, 3));

        jTextField1.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("1");
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField1);

        jTextField2.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField2);

        jTextField3.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField3);

        jTextField4.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField4);

        jTextField5.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setText("1");
        jTextField5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField5);

        jTextField6.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField6);

        jTextField7.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField7);

        jTextField8.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField8);

        jTextField9.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField9.setText("1");
        jTextField9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField9);

        jTextField10.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField10);

        jTextField11.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField11);

        jTextField12.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField12);

        jTextField13.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField13);

        jTextField14.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField14);

        jTextField15.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField15);

        jTextField16.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField16.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField16);

        jTextField17.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField17);

        jTextField18.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField18.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField18);

        jPanel3.setLayout(new java.awt.GridLayout(2, 1));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Capa superior");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setBorder(null);
        jPanel3.add(jLabel2);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Capa Inferior");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel3.setBorder(null);
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel3MouseReleased(evt);
            }
        });
        jPanel3.add(jLabel3);

        jButton1.setBackground(new java.awt.Color(151, 176, 177));
        jButton1.setText("Cambiar imagenes");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(151, 176, 177));
        jButton2.setText("Randomizar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(151, 176, 177));
        jButton3.setText("C");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(151, 176, 177));
        jButton4.setText("Limpiar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)))))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 102, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Presentador Imagenes");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jLabel1MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel1MouseMoved(evt);
            }
        });
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel1MouseReleased(evt);
            }
        });
        jLabel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jLabel1ComponentResized(evt);
            }
        });

        jPanel5.setLayout(new java.awt.GridLayout(4, 1));

        jLabel4.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Patrón de fondo");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel4MouseReleased(evt);
            }
        });
        jPanel5.add(jLabel4);

        jSlider1.setMaximum(300);
        jSlider1.setValue(jSlider1.getMaximum());
        jSlider1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Opacidad 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        jPanel5.add(jSlider1);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Adición", "Binarizar", "Dentro", "Mascara", "Borrado", "Borrado y aplicado", "Borrado fuerte", "Borrado rectángular", "Distancia", "Intersección", "Disolver", "Unión por bits", "Intersección por bits", "XOR por bits", "Sustracción & masc. 16-bits", "Sustracción & masc. 16-bits lat.", "const. opacidad", "const. opacidad mascara", "const. opacidad salida", "const. opacidad intersección", "const. opacidad escalonar", "const. opacidad rectificación lineal" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createTitledBorder("Discriminación Alpha"));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel5.add(jComboBox1);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Extremos", "Extremos abs", "Circular", "Circular abs", "Reflejo", "Reflejo Inv", "Mascara de bits", "Nulidad por salida", "Sin definir" }));
        jComboBox2.setBorder(javax.swing.BorderFactory.createTitledBorder("Ajuste RGB"));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jPanel5.add(jComboBox2);

        jPanel7.setLayout(new java.awt.GridLayout(4, 1));

        jSlider3.setMaximum(300);
        jSlider3.setValue(jSlider3.getMaximum());
        jSlider3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación Rojo 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(153, 0, 0))); // NOI18N
        jSlider3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider3StateChanged(evt);
            }
        });
        jPanel7.add(jSlider3);

        jSlider4.setMaximum(300);
        jSlider4.setValue(jSlider4.getMaximum());
        jSlider4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación Verde 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(0, 153, 0))); // NOI18N
        jSlider4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider4StateChanged(evt);
            }
        });
        jPanel7.add(jSlider4);

        jSlider5.setMaximum(300);
        jSlider5.setValue(jSlider5.getMaximum());
        jSlider5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación Azul 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(0, 0, 204))); // NOI18N
        jSlider5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider5StateChanged(evt);
            }
        });
        jPanel7.add(jSlider5);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Riesgo de salida");
        jPanel7.add(jLabel5);

        jPanel6.setLayout(new java.awt.GridLayout(4, 0));

        jSlider2.setMaximum(300);
        jSlider2.setValue(jSlider2.getMaximum());
        jSlider2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación 1) 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });
        jPanel6.add(jSlider2);

        jSlider6.setMaximum(400);
        jSlider6.setValue(jSlider6.getMaximum());
        jSlider6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación 2) 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));
        jSlider6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider6StateChanged(evt);
            }
        });
        jPanel6.add(jSlider6);

        jSlider7.setMaximum(400);
        jSlider7.setValue(jSlider7.getMaximum());
        jSlider7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación 3) 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));
        jSlider7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider7StateChanged(evt);
            }
        });
        jPanel6.add(jSlider7);

        jSlider8.setMaximum(400);
        jSlider8.setValue(jSlider7.getMaximum());
        jSlider8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación 4) 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));
        jSlider8.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider8StateChanged(evt);
            }
        });
        jPanel6.add(jSlider8);

        jPanel8.setLayout(new java.awt.GridLayout(1, 2));

        jList3.setBorder(javax.swing.BorderFactory.createTitledBorder("Modo Compuesto"));
        jList3.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jList3.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Lista compuesta" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList3.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList3ValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jList3);

        jPanel8.add(jScrollPane3);

        jList2.setBorder(javax.swing.BorderFactory.createTitledBorder("Modos de fusión"));
        jList2.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Lista de Modos de pintura" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList2);

        jPanel8.add(jScrollPane2);

        jPanel9.setLayout(new java.awt.GridLayout(1, 10));

        jButton5.setBackground(new java.awt.Color(151, 176, 177));
        jButton5.setText("Agregar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton5);

        jButton6.setBackground(new java.awt.Color(151, 176, 177));
        jButton6.setText("Eliminar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton6);

        jButton7.setBackground(new java.awt.Color(151, 176, 177));
        jButton7.setText("Cambiar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton7);

        jButton8.setText("Subir");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton8);

        jButton9.setText("Bajar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton9);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //</editor-fold>

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        String s = JOptionPane.showInputDialog(
                null,
                "Inserte una ruta para la carga de la imagen 1",
                "Lectura de la ruta",
                JOptionPane.PLAIN_MESSAGE
        );
        String s2 = JOptionPane.showInputDialog(
                null,
                "Inserte una ruta para la carga de la imagen 2",
                "Lectura de la ruta",
                JOptionPane.PLAIN_MESSAGE
        );
        BufferedImage Imagen1 = LectoEscrituraArchivos.cargar_imagen(s);
        BufferedImage Imagen2 = LectoEscrituraArchivos.cargar_imagen(s2);
        Escenario.ModificarCapas(Imagen1, Imagen2);
        Escenario.centrarCapas();
        //</editor-fold>
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        SincronizarModoPinturaConGUI();
        jSlider1.setBorder(BorderFactory.createTitledBorder(null,
                "Opacidad " + Matemática.Truncar(ModoFusión.Opacidad * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP
        ));
        //</editor-fold>
    }//GEN-LAST:event_jSlider1StateChanged

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        Escenario.BuscarActivación();
    }//GEN-LAST:event_jLabel1MousePressed

    private void jLabel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseMoved

    }//GEN-LAST:event_jLabel1MouseMoved

    private void jLabel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseDragged
        Escenario.Arrastrar();
    }//GEN-LAST:event_jLabel1MouseDragged

    private void jLabel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseReleased
        Escenario.Liberar();
    }//GEN-LAST:event_jLabel1MouseReleased

    private void jLabel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jLabel1ComponentResized
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            Escenario.AjustarCapasAlEscenario();
        } catch (Exception e) {
        }
        //</editor-fold>
    }//GEN-LAST:event_jLabel1ComponentResized

    private void jLabel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseReleased
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            Escenario.IntercambiarCapas();
        } catch (Exception e) {
        }
        //</editor-fold>
    }//GEN-LAST:event_jLabel3MouseReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (JTextField[] textfield : textfields) {
            for (JTextField jtxt : textfield) {
                if (!jtxt.isEditable()) {
                    continue;
                }
                int a = -2;
                int b = 2;
                float f = (float) Matemática.Truncar(((b - a) * Math.random() + a), 4, true);
                jtxt.setText(f + "");
            }
        }
        ActualizarModeloBasePintura_Matriz();
        //</editor-fold>
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        SincronizarModoPinturaConGUI();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    int Swiche1 = 0;

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        switch (Swiche1 = (Swiche1 + 1) % 4) {
            case 0:
                Escenario.ModificarCapas(BibliotecaArchivos.Imagenes.Test.IMG1(),
                        BibliotecaArchivos.Imagenes.Test.IMG2()
                );
                break;
            case 1:
                Escenario.ModificarCapas(BibliotecaArchivos.Imagenes.Test.PNG_2(),
                        BibliotecaArchivos.Imagenes.Test.Wallpaper_3()
                );
                break;
            case 2:
                Escenario.ModificarCapas(BibliotecaArchivos.Imagenes.Test.IMG3(),
                        BibliotecaArchivos.Imagenes.Test.IMG4()
                );
                break;
            case 3:
                Escenario.ModificarCapas(BibliotecaArchivos.Imagenes.Test.Wallpaper_1(),
                        BibliotecaArchivos.Imagenes.Test.Wallpaper_2()
                );
                break;
        }
        Escenario.centrarCapas();
        //</editor-fold>
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (JTextField[] textfield : textfields) {
            for (JTextField jTextField : textfield) {
                jTextField.setText("");
            }
        }
        ActualizarModeloBasePintura_Matriz();
        //</editor-fold>
    }//GEN-LAST:event_jButton4ActionPerformed

    int Swiche2 = 0;

    private void jLabel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseReleased
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Paint c = null;
        BufferedImage img = null;
        switch ((Swiche1++) % 5) {
            case 0:
                img = BibliotecaArchivos.Imagenes.Texturas.PatrónCuadricula(
                        Color.LIGHT_GRAY, Color.WHITE, 26, 26
                );
                break;
            case 1:
                img = BibliotecaArchivos.Imagenes.Texturas.PatrónCuadricula(
                        Color.DARK_GRAY, Color.BLACK, 26, 26
                );
                break;
            case 3:
                c = Color.WHITE;
                break;
            case 4:
                c = Color.BLACK;
                break;
        }
        PresentadorEscenario.ImagenFondo(img);
        PresentadorEscenario.ColorFondo(c);
        PresentadorEscenario.ActualizarFotograma();

        PresentadorPatrónFondo.ImagenFondo(img);
        PresentadorPatrónFondo.ColorFondo(c);
        PresentadorPatrónFondo.ActualizarFotograma();
        //</editor-fold>
    }//GEN-LAST:event_jLabel4MouseReleased

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        SincronizarModoPinturaConGUI();
        BaseModoPintura ModPint = (BaseModoPintura) ((ModoPinturaCompuesto) ModoFusión).ModosPintura.get(
                jList3.getSelectedIndex()
        );
        jSlider2.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación 1) " + Matemática.Truncar(ModPint.Aplicación * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP
        )
        );
        //</editor-fold>
    }//GEN-LAST:event_jSlider2StateChanged

    private void jSlider3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider3StateChanged
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        SincronizarModoPinturaConGUI();
        BaseModoPintura ModPint = (BaseModoPintura) ((ModoPinturaCompuesto) ModoFusión).ModosPintura.get(
                jList3.getSelectedIndex()
        );
        jSlider3.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación Rojo " + Matemática.Truncar(ModPint.AplicaciónRojo * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP,
                new Font("sansserif", Font.BOLD, 12),
                new Color(153, 0, 0))
        );
        //</editor-fold>
    }//GEN-LAST:event_jSlider3StateChanged

    private void jSlider4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider4StateChanged
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        SincronizarModoPinturaConGUI();
        BaseModoPintura ModPint = (BaseModoPintura) ((ModoPinturaCompuesto) ModoFusión).ModosPintura.get(
                jList3.getSelectedIndex()
        );
        jSlider4.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación Verde " + Matemática.Truncar(ModPint.AplicaciónVerde * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP,
                new Font("sansserif", Font.BOLD, 12),
                new Color(0, 153, 0))
        );
        //</editor-fold>
    }//GEN-LAST:event_jSlider4StateChanged

    private void jSlider5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider5StateChanged
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        SincronizarModoPinturaConGUI();
        BaseModoPintura ModPint = (BaseModoPintura) ((ModoPinturaCompuesto) ModoFusión).ModosPintura.get(
                jList3.getSelectedIndex()
        );
        jSlider5.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación Azul " + Matemática.Truncar(ModPint.AplicaciónAzul * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP,
                new Font("sansserif", Font.BOLD, 12),
                new Color(0, 0, 153))
        );
        //</editor-fold>
    }//GEN-LAST:event_jSlider5StateChanged

    private void jSlider6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider6StateChanged
        SincronizarModoPinturaConGUI();
        float p = 0;
        BaseModoPintura ModPint = (BaseModoPintura) ((ModoPinturaCompuesto) ModoFusión).ModosPintura.get(
                jList3.getSelectedIndex()
        );
        if (ModPint.AplicacionesUsadas() == 2) {
            p = (float) ModPint.Aplicación2;
        } else {
            p = 1f * jSlider6.getValue() / jSlider6.getMaximum();
        }
        jSlider6.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación 2) " + Matemática.Truncar(p * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP)
        );
    }//GEN-LAST:event_jSlider6StateChanged

    private void jSlider7StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider7StateChanged
        SincronizarModoPinturaConGUI();
        float p = 0;
        BaseModoPintura ModPint;
        ModPint = (BaseModoPintura) ((ModoPinturaCompuesto) ModoFusión).ModosPintura.get(
                jList3.getSelectedIndex()
        );
        if (ModPint.AplicacionesUsadas() == 3) {
            p = (float) ModPint.Aplicación3;
        } else {
            p = 1f * jSlider7.getValue() / jSlider7.getMaximum();
        }
        jSlider7.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación 3) " + Matemática.Truncar(p * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP)
        );
    }//GEN-LAST:event_jSlider7StateChanged

    private void jSlider8StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider8StateChanged
        SincronizarModoPinturaConGUI();
        float p = 0;
        BaseModoPintura ModPint;
        ModPint = (BaseModoPintura) ((ModoPinturaCompuesto) ModoFusión).ModosPintura.get(
                jList3.getSelectedIndex()
        );
        if (ModPint.AplicacionesUsadas() == 4) {
            p = (float) ModPint.Aplicación4;
        } else {
            p = 1f * jSlider8.getValue() / jSlider8.getMaximum();
        }
        jSlider8.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación 4) " + Matemática.Truncar(p * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP)
        );
    }//GEN-LAST:event_jSlider8StateChanged

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        SincronizarModoPinturaConGUI();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jList3ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList3ValueChanged
        SincronizarGUIConModeloPintura();
    }//GEN-LAST:event_jList3ValueChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            String Nombre = jList2.getSelectedValue();
            byte Modo = (byte) ListaID.ObtenerID(Nombre);
            ((ModoPinturaCompuesto) ModoFusión).ModosPintura.add(GUI_PruebaModoPintura.ConsultarModoPintura(
                    Modo, ObtenerMatriz()
            ));
            ListaCompuesta.add(Nombre.replace("/t/", ""));
            Maquillaje_SwingGUI.AsignarLista_JList(jList3, ListaCompuesta);
            jList3.setSelectedIndex(ListaCompuesta.size() - 1);
            ActualizarEscenario();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            int in = jList3.getSelectedIndex();
            if (in > 0) {
                ModoPinturaCompuesto m = (ModoPinturaCompuesto) ModoFusión;
                m.ModosPintura.add(in - 1, m.ModosPintura.get(in));
                m.ModosPintura.remove(in + 1);
                ListaCompuesta.add(in - 1, ListaCompuesta.get(in));
                ListaCompuesta.remove(in + 1);
                Maquillaje_SwingGUI.AsignarLista_JList(jList3, ListaCompuesta);
                jList3.setSelectedIndex(in - 1);
            }
            ActualizarEscenario();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {
            int in = jList3.getSelectedIndex() + 1;
            if (in < ListaCompuesta.size()) {
                ModoPinturaCompuesto m = (ModoPinturaCompuesto) ModoFusión;
                m.ModosPintura.add(in - 1, m.ModosPintura.get(in));
                m.ModosPintura.remove(in + 1);
                ListaCompuesta.add(in - 1, ListaCompuesta.get(in));
                ListaCompuesta.remove(in + 1);
                Maquillaje_SwingGUI.AsignarLista_JList(jList3, ListaCompuesta);
                jList3.setSelectedIndex(in);
            }
            ActualizarEscenario();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            int in = jList3.getSelectedIndex();
            ModoPinturaCompuesto m = (ModoPinturaCompuesto) ModoFusión;
            m.ModosPintura.remove(in);
            ListaCompuesta.remove(in);
            Maquillaje_SwingGUI.AsignarLista_JList(jList3, ListaCompuesta);
            jList3.setSelectedIndex(in - 1);
            ActualizarEscenario();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            String Nombre = jList2.getSelectedValue();
            byte Modo = (byte) ListaID.ObtenerID(Nombre);
            int in = jList3.getSelectedIndex();
            BaseModoPintura mp = GUI_PruebaModoPintura.ConsultarModoPintura(
                    Modo, ObtenerMatriz()
            );
            ((ModoPinturaCompuesto) ModoFusión).ModosPintura.set(in, mp);
            ListaCompuesta.set(in, Nombre.replace("/t/", ""));
            Maquillaje_SwingGUI.AsignarLista_JList(jList3, ListaCompuesta);
            jList3.setSelectedIndex(in);
            ActualizarEscenario();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    //<editor-fold defaultstate="collapsed" desc="Declaración de variables de la GUI">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jList3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JSlider jSlider4;
    private javax.swing.JSlider jSlider5;
    private javax.swing.JSlider jSlider6;
    private javax.swing.JSlider jSlider7;
    private javax.swing.JSlider jSlider8;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
    //</editor-fold>

}
