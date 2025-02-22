package HerramientaDeImagen;

import HerramientasMatemáticas.Dupla;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import static HerramientasGUI.NimbusModificado.*;

public class PruebaDegradadoPolar extends JFrame {

    public final static byte TEMA_OSCURO = 0, TEMA_CLARO = 1, TEMA_PERSONALIZADO = 2;

    public static void main(String args[]) {
        try {
            Memoria.Inicializar();
            switch (Memoria.TEMA_ENTORNO) {
                case TEMA_OSCURO:
                    CargarTemaOscuroNimbus();
                    break;
                case TEMA_CLARO:
                    CargarTemaClaroNimbus();
                    break;
                case TEMA_PERSONALIZADO:
                    CargarTemaColorPersonalizadoNimbus(
                            Memoria.ColorPrincipal,
                            Memoria.ColorDeAtención,
                            1
                    );
                    break;
                default:
                    System.out.println("Tema no encontrado");
            }
            CargarNimbus();
        } catch (Exception ex) {
            System.out.println("Error al cargar Nimbus");
        }
        PruebaDegradadoPolar p = new PruebaDegradadoPolar();
    }

    public PruebaDegradadoPolar() {
        initComponents();
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(20);
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jMenuBar1.setVisible(false);
        setSize(1150, 650);
        setLocationRelativeTo(null);
        Memoria.tam.Imprimir_Consola("tam");
        Memoria.pos.Imprimir_Consola("pos");
//        setSize(Memoria.tam.convDimension());
//        setLocation(Memoria.pos.convPoint());
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Forma = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jSpinner6 = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jSpinner10 = new javax.swing.JSpinner();
        jSpinner11 = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jSpinner8 = new javax.swing.JSpinner();
        jPanel8 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jSpinner9 = new javax.swing.JSpinner();
        jPanel6 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jSpinner7 = new javax.swing.JSpinner();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jSpinner5 = new javax.swing.JSpinner();
        jTextField5 = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        jPanel38 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jRadioButton26 = new javax.swing.JRadioButton();
        jRadioButton27 = new javax.swing.JRadioButton();
        jPanel40 = new javax.swing.JPanel();
        jRadioButton28 = new javax.swing.JRadioButton();
        jRadioButton29 = new javax.swing.JRadioButton();
        jLabel27 = new javax.swing.JLabel();
        jPanel42 = new javax.swing.JPanel();
        jComboBox13 = new javax.swing.JComboBox<>();
        jComboBox14 = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel43 = new javax.swing.JPanel();
        jSpinner15 = new javax.swing.JSpinner();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jSpinner16 = new javax.swing.JSpinner();
        jComboBox15 = new javax.swing.JComboBox<>();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseMoved(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Paleta"));
        jPanel3.setLayout(null);

        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.add(jLabel2);
        jLabel2.setBounds(10, 20, 350, 70);

        jButton1.setText("Editar Textualmente");
        jPanel3.add(jButton1);
        jButton1.setBounds(10, 100, 129, 23);

        jButton3.setText("Añadir Color");
        jPanel3.add(jButton3);
        jButton3.setBounds(150, 100, 91, 23);

        jButton2.setText("Eliminar");
        jPanel3.add(jButton2);
        jButton2.setBounds(250, 100, 80, 23);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Centro"));
        jPanel4.setLayout(null);

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel());
        jPanel4.add(jSpinner2);
        jSpinner2.setBounds(50, 30, 60, 20);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("X");
        jPanel4.add(jLabel3);
        jLabel3.setBounds(10, 30, 30, 20);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Y");
        jPanel4.add(jLabel4);
        jLabel4.setBounds(10, 60, 30, 20);

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel());
        jPanel4.add(jSpinner3);
        jSpinner3.setBounds(50, 60, 60, 20);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Escala");
        jPanel4.add(jLabel5);
        jLabel5.setBounds(133, 65, 70, 20);

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(1.0f, null, null, 1.0f));
        jPanel4.add(jSpinner4);
        jSpinner4.setBounds(130, 85, 80, 20);

        jSpinner6.setModel(new javax.swing.SpinnerNumberModel(0, 0, 359, 1));
        jPanel4.add(jSpinner6);
        jSpinner6.setBounds(130, 40, 80, 20);

        jLabel6.setText("Rotación");
        jPanel4.add(jLabel6);
        jLabel6.setBounds(133, 20, 80, 14);

        jButton7.setText("Centrar");
        jPanel4.add(jButton7);
        jButton7.setBounds(30, 90, 69, 23);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Escala y rotación"));
        jPanel11.setLayout(null);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel14.setText("Escala");
        jPanel11.add(jLabel14);
        jLabel14.setBounds(10, 60, 50, 20);

        jSpinner10.setModel(new javax.swing.SpinnerNumberModel(1.0f, null, null, 1.0f));
        jPanel11.add(jSpinner10);
        jSpinner10.setBounds(60, 60, 60, 20);

        jSpinner11.setModel(new javax.swing.SpinnerNumberModel(0, 0, 359, 1));
        jPanel11.add(jSpinner11);
        jSpinner11.setBounds(60, 30, 60, 20);

        jLabel15.setText("Rotación");
        jPanel11.add(jLabel15);
        jLabel15.setBounds(10, 30, 50, 14);

        jButton4.setText("Restaurar");
        jPanel11.add(jButton4);
        jButton4.setBounds(30, 90, 81, 23);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Interferencias Binarias"));
        jPanel12.setLayout(null);

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.X_AXIS));

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("A");
        jPanel7.add(jLabel19);

        jSpinner8.setModel(new javax.swing.SpinnerNumberModel());
        jPanel7.add(jSpinner8);

        jPanel5.add(jPanel7);

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.X_AXIS));

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("B");
        jPanel8.add(jLabel20);

        jSpinner9.setModel(new javax.swing.SpinnerNumberModel());
        jPanel8.add(jSpinner9);

        jPanel5.add(jPanel8);

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.X_AXIS));

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("C");
        jPanel6.add(jLabel18);

        jSpinner7.setModel(new javax.swing.SpinnerNumberModel());
        jPanel6.add(jSpinner7);

        jPanel5.add(jPanel6);

        jPanel12.add(jPanel5);
        jPanel5.setBounds(10, 30, 110, 90);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "|", "&", "^" }));
        jPanel12.add(jComboBox2);
        jComboBox2.setBounds(125, 90, 33, 20);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "|", "&", "^" }));
        jPanel12.add(jComboBox3);
        jComboBox3.setBounds(125, 30, 33, 20);

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "|", "&", "^" }));
        jPanel12.add(jComboBox4);
        jComboBox4.setBounds(125, 60, 33, 20);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("◼");
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.add(jLabel7);
        jLabel7.setBounds(165, 35, 20, 20);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("◼");
        jLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.add(jLabel8);
        jLabel8.setBounds(165, 65, 20, 20);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("▶");
        jLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.add(jLabel9);
        jLabel9.setBounds(165, 95, 20, 20);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Negar"));
        jPanel9.setLayout(null);

        jRadioButton1.setText("B");
        jRadioButton1.setAlignmentX(0.5F);
        jRadioButton1.setBorder(null);
        jPanel9.add(jRadioButton1);
        jRadioButton1.setBounds(20, 40, 40, 15);

        jRadioButton2.setText("A");
        jRadioButton2.setAlignmentX(0.5F);
        jRadioButton2.setBorder(null);
        jPanel9.add(jRadioButton2);
        jRadioButton2.setBounds(20, 20, 40, 15);

        jRadioButton3.setText("N");
        jRadioButton3.setAlignmentX(0.5F);
        jRadioButton3.setBorder(null);
        jPanel9.add(jRadioButton3);
        jRadioButton3.setBounds(20, 80, 40, 15);

        jRadioButton4.setText("C");
        jRadioButton4.setAlignmentX(0.5F);
        jRadioButton4.setBorder(null);
        jPanel9.add(jRadioButton4);
        jRadioButton4.setBounds(20, 60, 40, 15);

        jPanel12.add(jPanel9);
        jPanel9.setBounds(190, 10, 70, 110);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        jPanel36.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder("Forma"));
        jPanel37.setLayout(null);

        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f)));
        jPanel37.add(jSpinner5);
        jSpinner5.setBounds(100, 140, 70, 20);
        jPanel37.add(jTextField5);
        jTextField5.setBounds(10, 75, 200, 20);

        jButton17.setText("Cargar mapa PNG");
        jPanel37.add(jButton17);
        jButton17.setBounds(10, 105, 200, 23);

        jPanel38.setLayout(null);

        jPanel39.setLayout(new javax.swing.BoxLayout(jPanel39, javax.swing.BoxLayout.Y_AXIS));

        Forma.add(jRadioButton26);
        jRadioButton26.setText("Elipse");
        jPanel39.add(jRadioButton26);

        Forma.add(jRadioButton27);
        jRadioButton27.setSelected(true);
        jRadioButton27.setText("Rectángulo");
        jPanel39.add(jRadioButton27);

        jPanel38.add(jPanel39);
        jPanel39.setBounds(0, 0, 79, 46);

        jPanel40.setLayout(new javax.swing.BoxLayout(jPanel40, javax.swing.BoxLayout.Y_AXIS));

        Forma.add(jRadioButton28);
        jRadioButton28.setText("Círculo");
        jPanel40.add(jRadioButton28);

        Forma.add(jRadioButton29);
        jRadioButton29.setText("PNG");
        jPanel40.add(jRadioButton29);

        jPanel38.add(jPanel40);
        jPanel40.setBounds(100, 0, 100, 46);

        jPanel37.add(jPanel38);
        jPanel38.setBounds(10, 20, 200, 50);

        jLabel27.setText("Escalar");
        jPanel37.add(jLabel27);
        jLabel27.setBounds(10, 140, 90, 20);

        jPanel42.setBorder(javax.swing.BorderFactory.createTitledBorder("Controles"));
        jPanel42.setLayout(null);

        jComboBox13.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alcance Corto", "Alcance Largo", "Atractor", "Serpentina", "Apertura", "Vislumbrar", "Focal", "Dividir", "Multiplicar", "Adicionar", "Log modulo 1", "Log modulo 2", "Log exclusión Bin.", "Intersec. Bin. sierpinski", "Unión Bin. Sierpinski", "Unión Bin. Sierpinski 2", "Lazos Bin.", "Lazos Bin. con moño", "Texturizado", "Texturizado 2" }));
        jPanel42.add(jComboBox13);
        jComboBox13.setBounds(10, 85, 200, 20);

        jComboBox14.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LINEAL", "CICLICO", "REFLEJO" }));
        jPanel42.add(jComboBox14);
        jComboBox14.setBounds(10, 40, 200, 20);

        jLabel28.setText("Ajuste RGB");
        jPanel42.add(jLabel28);
        jLabel28.setBounds(15, 20, 90, 20);

        jLabel29.setText("Interpretación");
        jPanel42.add(jLabel29);
        jLabel29.setBounds(15, 65, 69, 14);

        jPanel43.setBorder(javax.swing.BorderFactory.createTitledBorder("Revoluciones y fusión"));
        jPanel43.setLayout(null);

        jSpinner15.setModel(new javax.swing.SpinnerNumberModel(0, -9, 9, 1));
        jPanel43.add(jSpinner15);
        jSpinner15.setBounds(140, 20, 39, 20);

        jLabel30.setText("Inicio");
        jPanel43.add(jLabel30);
        jLabel30.setBounds(10, 20, 90, 20);

        jLabel31.setText("Fin");
        jPanel43.add(jLabel31);
        jLabel31.setBounds(10, 45, 90, 20);

        jSpinner16.setModel(new javax.swing.SpinnerNumberModel(1, -9, 9, 1));
        jPanel43.add(jSpinner16);
        jSpinner16.setBounds(140, 50, 39, 20);

        jComboBox15.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aditivo", "Diferencia", "Diferencia Abs.", "Diferencia Lat.", "Promedio", "División", "División Lat.", "Mod.", "Mod. Lat.", "Distancia.", "Bin. Unión.", "Bin. Intersec.", "Bin. Exclusión", "Color más oscuro", "Multiplicar", "Color más claro", "Trama (Pantalla)", "Luz Suave (Solapar)", "Luz Lineal", "(%) Incremento BASE", "(%) Incremento Inv. BASE", "(%) Decremento BASE", "(%) Producto BASE", "(%) Cociente BASE", "(%) Cociente Máx. BASE", "(%) Sustracción BASE", "(%) Sustracción Lat. BASE", "(%) Sustracción Abs. BASE", "(%) Potencia BASE", "(%) Incremento SUP.", "(%) Incremento Inv. SUP.", "(%) Decremento SUP.", "(%) Producto SUP.", "(%) Cociente SUP.", "(%) Cociente Máx. SUP.", "(%) Sustracción SUP.", "(%) Sustracción Lat. SUP.", "(%) Sustracción Abs. SUP.", "(%) Potencia SUP.", "Conmutar Rojo (R)", "Conmutar Rojo y Verde (RG)", "Conmutar Rojo y Azul (RB)", "Conmutar Verde (G)", "Conmutar Verde y Azul (GB)", "Conmutar Azul (B)", "Glitch 1", "Glitch 2", "Glitch 3" }));
        jPanel43.add(jComboBox15);
        jComboBox15.setBounds(10, 80, 180, 20);

        jPanel42.add(jPanel43);
        jPanel43.setBounds(10, 120, 200, 120);

        jButton18.setText("Ver Código");

        jButton19.setText("Biblioteca");

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanel42, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel42, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 170, Short.MAX_VALUE)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton18)
                    .addComponent(jButton19))
                .addContainerGap())
        );

        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        jScrollPane1.setViewportView(jPanel1);

        getContentPane().add(jScrollPane1);

        jMenu1.setText("Configuración");

        jMenuItem1.setText("Tema del entorno");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("Guardar configuración al salir");
        jMenu1.add(jRadioButtonMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Creditos");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jScrollPane1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseMoved
        Dupla pos = Dupla.PosiciónCursorEnComponente(this);
        if (pos.YesMenor(50) && pos.XesMenor(getWidth() / 2)) {
            if (!jMenuBar1.isVisible()) {
                jMenuBar1.setVisible(true);
            }
        } else if (jMenuBar1.isVisible()) {
            jMenuBar1.setVisible(false);
        }
    }//GEN-LAST:event_jScrollPane1MouseMoved


    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        BufferedImage ic = LectoEscritor_Imagenes.cargarImagen("ZZIconPaint.png");
        String[] options = new String[]{"Tema Oscuro", "Tema Claro", "Elegir Color Tema"};
        int response = JOptionPane.showOptionDialog(
                null,
                "Elige tema para la aplicación", "Color",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                new ImageIcon(ic),
                options,
                options[0]
        );
        switch (response) {
            case 0:
                CargarTemaOscuroNimbus();
                Memoria.TEMA_ENTORNO = TEMA_OSCURO;
                break;
            case 1:
                CargarTemaClaroNimbus();
                Memoria.TEMA_ENTORNO = TEMA_CLARO;
                break;
            case 2:
                Color ColorPrincipal = JColorChooser.showDialog(null, "Color principal del tema", new Color(55, 55, 55));
                Color ColorDeAtención = JColorChooser.showDialog(null, "Color de atención", new Color(255, 255, 0));
                CargarTemaColorPersonalizadoNimbus(
                        ColorPrincipal,
                        ColorDeAtención,
                        1
                );
                Memoria.TEMA_ENTORNO = TEMA_PERSONALIZADO;
                Memoria.SAT = (100);
                Memoria.ColorPrincipal = ColorPrincipal;
                Memoria.ColorDeAtención = ColorDeAtención;
                break;
            default:
                CargarTemaOscuroNimbus();
                break;
        }
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "Para que el cambio haga bien el efecto, debe reiniciar la aplicación\n¿Desea cerrar el programa?", "Color",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        new Thread(
                () -> {
                    if (jRadioButtonMenuItem1.isSelected()) {
                        Memoria.pos = new Dupla(getLocation());
                        Memoria.tam = new Dupla(getSize());
                        Memoria.Guardar();
                    }
//                    PantallaFinal.CerrarPrograma();
                    System.exit(0);
                }
        ).start();
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup Forma;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox13;
    private javax.swing.JComboBox<String> jComboBox14;
    private javax.swing.JComboBox<String> jComboBox15;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton26;
    private javax.swing.JRadioButton jRadioButton27;
    private javax.swing.JRadioButton jRadioButton28;
    private javax.swing.JRadioButton jRadioButton29;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner10;
    private javax.swing.JSpinner jSpinner11;
    private javax.swing.JSpinner jSpinner15;
    private javax.swing.JSpinner jSpinner16;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JSpinner jSpinner7;
    private javax.swing.JSpinner jSpinner8;
    private javax.swing.JSpinner jSpinner9;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables

    static class Memoria {

        private static final String ARCHIVO_NOMBRE = "tema.png";
        private static final String CARPETA_PROYECTO = "Degradado Polar";
        private static final String APPDATA = System.getenv("APPDATA");
        private static final String CARPETA = nombreSubCarpeta();
        private static final File ARCHIVO = Archivo();

        public static int TEMA_ENTORNO;
        public static int SAT;
        public static Color ColorPrincipal;
        public static Color ColorDeAtención;
        public static Dupla pos;
        public static Dupla tam;

        public static void Inicializar() {
            if (!ARCHIVO.exists()) {
                ColorPrincipal = new Color(0);
                ColorDeAtención = new Color(0);
                TEMA_ENTORNO = TEMA_OSCURO;
                SAT = 0;
                tam = new Dupla(1150, 650);
                pos = Dupla.Alinear(
                        Dupla.DIMENSIÓN_PANTALLA,
                        tam,
                        Dupla.MEDIO, Dupla.MEDIO
                );
                GenerarDirectorio();
                Guardar();
            } else {
                Cargar();
            }
        }

        public static void Cargar() {
            try {
                BufferedImage img = ImageIO.read(ARCHIVO);
                TEMA_ENTORNO = img.getRGB(0, 0);
                SAT = img.getRGB(1, 0);
                ColorPrincipal = new Color(img.getRGB(2, 0), true);
                ColorDeAtención = new Color(img.getRGB(3, 0), true);
                pos = new Dupla(new Color(img.getRGB(4, 0), true));
                tam = new Dupla(new Color(img.getRGB(5, 0), true));
            } catch (IOException ex) {
                System.out.println("No se ha podido cargar el archivo");
            }
        }

        public static void Guardar() {
            try {
                BufferedImage im = new BufferedImage(6, 1, 2);
                im.setRGB(0, 0, TEMA_ENTORNO);
                im.setRGB(1, 0, SAT);
                im.setRGB(2, 0, ColorPrincipal.getRGB());
                im.setRGB(3, 0, ColorDeAtención.getRGB());
//                im.setRGB(4, 0, new Color(pos.intX(), pos.intY(), 0).getRGB());
//                im.setRGB(5, 0, new Color(tam.intX(), tam.intY(), 0).getRGB());
                ImageIO.write(im, "png", ARCHIVO);
            } catch (Exception e) {
                System.out.println("Imposible Escribir en el archivo");
            }
        }

        private static File Archivo() {
            return new File(APPDATA + "\\" + CARPETA + ARCHIVO_NOMBRE);
        }

        private static String nombreSubCarpeta() {
            return "Jeff Aporta\\" + CARPETA_PROYECTO + "\\";
        }

        private static void GenerarDirectorio() {
            String Ruta = ARCHIVO.getPath().replace(ARCHIVO.getName(), "");
            if (!new File(Ruta).exists()) {
                new File(Ruta).mkdirs();
            }
        }
    }

}
