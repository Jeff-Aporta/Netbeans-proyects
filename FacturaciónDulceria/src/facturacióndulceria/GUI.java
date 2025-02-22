package facturacióndulceria;

import HerramientaArchivos.CarpetaDeRecursos;
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientaDeImagen.Filtros_Lineales;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientasGUI.NimbusModificado;
import HerramientasGUI.Presentador;
import HerramientasGUI.VentanaGráfica;
import HerramientasMatemáticas.Dupla;
import HerramientasMatemáticas.Matemática;
import HerramientasSistema.Sistema;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class GUI extends javax.swing.JFrame {

    static String URL_ListadoPreciosProductos = "https://docs.google.com/spreadsheets/d/e/2PACX-1vTzSHP_HuiwCmpR6I56jACiS577zm3EEluoIIEna6mQOp05kYuo49CgzkFICGvwLfBsC4JIwWjXfEwj/pub?gid=0&single=true&output=tsv";

    static CarpetaDeRecursos carpetaRaíz = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación");
    static CarpetaDeRecursos carpetaFacturas = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/facturas");
    static CarpetaDeRecursos carpetaInventario = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/inventario");
    static CarpetaDeRecursos carpetaGastos = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/gastos");
    static CarpetaDeRecursos carpetaImagenesProductos = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/Imagenes productos");
    static CarpetaDeRecursos carpetaCreditos = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/creditos");

    static ArrayList<Producto> productos = new ArrayList<>();
    static String[] nombresProductos;

    static BufferedImage imgDefault;

    static GUI ventanaPrincipal = null;
    static VentanaGráfica ventanaCarga = new VentanaGráfica("Carga", false, true);

    static void mostrarTextoVentanaCarga(String txt) {
        BufferedImage img = Dupla.convBufferedImage(Dupla.DIMENSIÓN_PANTALLA);
        Graphics2D g = img.createGraphics();
        Dupla xy = Dupla.Alinear(img, new Dupla(350), Dupla.MEDIO, Dupla.MEDIO);
        g.setColor(new Color(128, 128, 128, 100));
        g.fillOval(xy.intX(), xy.intY(), 350, 350);
        GeneradorDeTexto gdt = new GeneradorDeTexto()
                .ModificarTamañoFuente(50)
                .ColorFuente(Color.WHITE)
                .Borde(new Color(0, 0, 0), 6)
                .AlineaciónHorizontal(Dupla.MEDIO)
                .AtributoNormal_Plano();
        gdt.DibujarTextoCentradoEnImagen(img, txt);
        ventanaCarga.ActualizarFotograma(img);
    }

    public static void main(String args[]) {
        ventanaCarga.CambiarModoFondo(VentanaGráfica.FONDO_INVISIBLE);
        ventanaCarga.CambiarTamaño(VentanaGráfica.DIMENSIÓN_PANTALLA_COMPLETA);
        ventanaCarga.setUndecorated(true);
        ventanaCarga.setVisible(true);
        ventanaCarga.setAlwaysOnTop(true);
        carpetaRaíz.diasActualizarArchivos(1);
        carpetaImagenesProductos.diasActualizarArchivos(1000);
        mostrarTextoVentanaCarga("Cargando Imagen por defecto");
        CarpetaDeRecursos.Recurso def = carpetaImagenesProductos.GenerarRecurso("https://media.istockphoto.com/vectors/sweet-shop-vector-id1201448484?k=20&m=1201448484&s=612x612&w=0&h=LswVfmGex7UzgHFEvGAoEP-mCMj_t1S-zC6J4XIzzHc=", "default.png");
        imgDefault = LectoEscrituraArchivos.cargar_imagen(def.ObtenerArchivo());
        mostrarTextoVentanaCarga("Cargando Lista de precios");
        carpetaRaíz.GenerarRecurso(URL_ListadoPreciosProductos, "Lista de precios.txt");
        cargarListaPrecios();
        NimbusModificado.CargarTemaBlancoNimbus();
        NimbusModificado.CargarNimbus();
        ventanaPrincipal = new GUI();
        ((Presentador) ventanaPrincipal.jLabel16).ActualizarFotograma(imgDefault);
        jList1.requestFocus();
        ventanaCarga.hide();
    }

    public GUI() {
        initComponents();
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = nombresProductos;

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
        {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            tcr.setHorizontalAlignment(SwingConstants.RIGHT);
            for (int i = 0; i < jTable1.getColumnCount(); i++) {
                jTable1.getColumnModel().getColumn(i).setCellRenderer(tcr);
            }
        }
        {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
            tcr.setHorizontalAlignment(SwingConstants.LEFT);
            for (int i = 0; i < jTable3.getColumnCount(); i++) {
                jTable3.getColumnModel().getColumn(i).setCellRenderer(tcr);
            }
            jTable3.getModel().addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (row < 0 || column < 0) {
                        return;
                    }
                    TableModel model = (TableModel) e.getSource();
                    Object cantidad = model.getValueAt(row, column);
                    Object nombreProducto = model.getValueAt(row, 0);
                    for (Producto producto : productos) {
                        if (producto.Nombre.equals(nombreProducto)) {
                            producto.modificarCantidad(Integer.parseInt(cantidad.toString()));
                            break;
                        }
                    }
                    cargarInventario();
                }
            });
            jTable1.getModel().addTableModelListener(new TableModelListener() {
                int r = 0;

                @Override
                public void tableChanged(TableModelEvent e) {
                    refrescarValoresTabla_venta();
                }
            });
        }
        BordeTitulo(jLabel4, Color.LIGHT_GRAY, "Unidad");
        BordeTitulo(jLabel6, Color.LIGHT_GRAY, "Subtotal");
        BordeTitulo(jLabel18, Color.LIGHT_GRAY, "Cliente");
        BordeTitulo(jLabel19, Color.LIGHT_GRAY, "Saldo");
        BordeTitulo(jLabel10, Color.LIGHT_GRAY, "Nota");
        actualizarTodo();
        actualizarCreditados();
        setVisible(true);
    }

    void actualizarTodo() {
        cargarInventario();
        actualizarResumen();
        cargarListaDeGastos();
        creditos_actualizarListaRecibos();
        creditos_ponerSumaCliente();
        ActualizarCantidadProductosVendidos();
    }

    void BordeTitulo(JComponent j, Color c, String t) {
        j.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(c), t));
    }

    void ReiniciarImagenes() {
        File[] f = carpetaImagenesProductos.ListarArchivos();
        for (int i = 0; i < f.length; i++) {
            f[i].delete();
        }
    }

    static void cargarListaPrecios() {
        String textoListadoPrecios = "";
        try {
            textoListadoPrecios = LectoEscrituraArchivos.LeerArchivo_ASCII(carpetaRaíz.BuscarRecurso("Lista de precios.txt").ObtenerArchivo()
            );
        } catch (Exception ex) {
        }
        String[] renglones = textoListadoPrecios.split("\n");
        nombresProductos = new String[renglones.length - 1];
        {
            int i = 0;
            for (String string : renglones) {
                if (i == 0) {
                    i++;
                    continue;
                }
                if (string.isEmpty()) {
                    continue;
                }
                String[] arr = string.split("\t");
                Producto p = new Producto(arr[0], Integer.parseInt(arr[7]), arr.length < 9 ? "" : arr[8], arr.length < 10 ? "" : arr[9]);
                if (arr.length >= 11) {
                    p.establecerImagen(arr[10]);
                }
                productos.add(p);
                nombresProductos[(i++) - 1] = arr[0];
            }
            Arrays.sort(nombresProductos);
            Collections.sort(productos, (t, t1) -> {
                return t.Nombre.compareTo(t1.Nombre);
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel16 = new Presentador();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jButton4 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jButton13 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList<>();
        jScrollPane13 = new javax.swing.JScrollPane();
        jList5 = new javax.swing.JList<>();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel11 = new Presentador();
        jButton7 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jButton19 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("$0");

        jList1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jList1KeyReleased(evt);
            }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel1.setText("Producto");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Valor unidad", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(300);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(80);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(80);
        }

        jButton1.setBackground(new java.awt.Color(204, 0, 0));
        jButton1.setText("Eliminar fila");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton8.setText("Reiniciar factura");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jPanel7.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        jLabel16.setForeground(new java.awt.Color(204, 204, 204));
        jLabel16.setBorder(null);
        jPanel7.add(jLabel16);

        jPanel8.setLayout(new java.awt.GridLayout(4, 0));

        jLabel2.setText("Cantidad");
        jPanel8.add(jLabel2);

        jTextField1.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        jTextField1.setText("1");
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jPanel8.add(jTextField1);

        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel8.add(jLabel4);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel8.add(jLabel6);

        jPanel7.add(jPanel8);

        jPanel10.setLayout(new java.awt.GridLayout(3, 0));

        jPanel11.setLayout(new java.awt.GridLayout(1, 0, 5, 0));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Venta");
        jRadioButton1.setFocusable(false);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jPanel11.add(jRadioButton1);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Compra");
        jRadioButton2.setFocusable(false);
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jPanel11.add(jRadioButton2);

        jPanel10.add(jPanel11);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton2.setText("Agregar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton2KeyReleased(evt);
            }
        });
        jPanel10.add(jButton2);

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton3.setText("Guardar Factura");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel10.add(jButton3);

        jPanel7.add(jPanel10);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 109, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton8)
                                .addComponent(jButton1)))
                        .addGap(5, 5, 5))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ventas y Compras", jPanel1);

        jList3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jList3KeyReleased(evt);
            }
        });
        jScrollPane7.setViewportView(jList3);

        jButton9.setText("Crear tipo de gasto");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Guardar");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Razón", "Valor"
            }
        ));
        jScrollPane8.setViewportView(jTable5);

        jButton11.setText("Registrar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jButton11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jButton11KeyReleased(evt);
            }
        });

        jButton12.setText("Actualizar");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9)
                    .addComponent(jButton12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10)
                    .addComponent(jButton12))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Gastos", jPanel5);

        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jList2);

        jButton4.setText("Refrescar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Valor unidad", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable2);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("$0");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4)
                    .addComponent(jLabel9))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Historial facturas", jPanel2);

        jButton13.setText("Crear cliente");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel14.setText("Clientes");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel15.setText("Recibos");

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Valor unidad", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane11.setViewportView(jTable6);

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor transacción"));

        jButton14.setText("Abonar");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText("Actualizar");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jList4.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList4ValueChanged(evt);
            }
        });
        jScrollPane12.setViewportView(jList4);

        jList5.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList5ValueChanged(evt);
            }
        });
        jScrollPane13.setViewportView(jList5);

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel18.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Cliente"));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Saldo"));

        jButton16.setText("Crédito");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setText("Saldar");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Nota"));

        jButton18.setText("Gasto");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton20.setText("Renombrar");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jButton15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton20))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jButton14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton18)
                                .addGap(8, 8, 8)
                                .addComponent(jButton17)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton14)
                            .addComponent(jButton16)
                            .addComponent(jButton17)
                            .addComponent(jButton18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                            .addComponent(jScrollPane13)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13)
                    .addComponent(jButton15)
                    .addComponent(jButton20))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Créditos", jPanel6);

        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Dìa", "Producido"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTable4);

        jButton7.setText("Actualizar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("jLabel12");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 846, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                        .addGap(37, 37, 37)
                        .addComponent(jButton7)))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Estado del dìa", jPanel4);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Cantidad", "Distribuidor", "Telefóno"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable3);

        jButton5.setText("Aumentar cantidad");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Establecer cantidad");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 891, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Inventario", jPanel3);

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Ventas", "P. Ventas", "Dinero", "P. Dinero"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(jTable7);

        jButton19.setText("Actualizar");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jButton19)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 1037, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton19)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Lo más vendido", jPanel12);

        jPanel9.add(jTabbedPane2);

        jTabbedPane1.addTab("Resumenes", jPanel9);

        getContentPane().add(jTabbedPane1);

        jMenu1.setText("Herramientas");

        jMenu2.setText("Productos");

        jMenuItem1.setText("Abrir BD Productos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem2.setText("Recargar BD");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setText("Reiniciar Imágenes");
        jMenu2.add(jMenuItem3);

        jMenu1.add(jMenu2);

        jMenuItem4.setText("Generar PDF");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String producto = jList1.getSelectedValue();
        int cantidadIngresar = (int) Double.parseDouble(jTextField1.getText());
        boolean esCompra = jRadioButton2.isSelected();
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            String clave = jTable1.getValueAt(i, 0).toString();
            if (producto.equals(clave)) {
                jTable1.setValueAt(cantidadIngresar + Integer.parseInt(jTable1.getValueAt(i, 1).toString()), i, 1);
                refrescarValoresTabla_venta();
                jList1.requestFocus();
                return;
            }
        }
        if (esCompra) {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.addRow(new Object[]{producto, cantidadIngresar, "", ""});
            jList1.requestFocus();
            sumarTotales();
        } else {
            int p = (int) productos.get(jList1.getSelectedIndex()).precio;
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.addRow(new Object[]{producto, cantidadIngresar, p, p * cantidadIngresar});
            jList1.requestFocus();
            sumarTotales();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    void refrescarValoresTabla_venta() {
        if (jRadioButton2.isSelected()) {
            return;
        }
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            String clave = jTable1.getValueAt(i, 0).toString();
            for (Producto producto : productos) {
                if (producto.Nombre.equals(clave)) {
                    int cantidad = Integer.parseInt(jTable1.getValueAt(i, 1).toString());
                    {
                        String v = ((int) producto.precio) + "";
                        if (!jTable1.getValueAt(i, 2).toString().equals(v)) {
                            jTable1.setValueAt(v, i, 2);
                        }
                    }
                    {
                        String v = (int) (producto.precio * cantidad) + "";
                        boolean b = !jTable1.getValueAt(i, 3).toString().equals(v);
                        if (b) {
                            jTable1.setValueAt(v, i, 3);
                            sumarTotales();
                        }
                    }

                }
            }
        }
    }

    void cargarInventario() {
        eliminarFilasDeUnJTable(jTable3);
        ArrayList<Producto> copy = new ArrayList();
        for (Producto producto : productos) {
            copy.add(producto);
        }
        Collections.sort(copy, (t, t1) -> {
            return ((Integer) t1.obtenerCantidad()).compareTo(t.obtenerCantidad());
        });
        for (Producto producto : copy) {
            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            model.addRow(new Object[]{
                producto.Nombre, producto.obtenerCantidad(), producto.distribuidor, producto.telefóno
            });
        }
    }

    void sumarTotales() {
        boolean esCompra = jRadioButton2.isSelected();
        if (esCompra) {
            return;
        }
        int t = 0;
        if (jTable1.getRowCount() > 0) {
            for (int i = 0; i < jTable1.getRowCount(); i++) {
                t += Double.parseDouble(jTable1.getValueAt(i, 3).toString());
            }
        } else {
            t = 0;
        }
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        String l1 = defaultFormat.format(t);
        l1 = l1.substring(0, l1.indexOf(","));
        jLabel8.setText(l1);
    }

    void actualizarProductoIngresar() {
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        Producto producto = productos.get(jList1.getSelectedIndex());
        int p = (int) producto.precio;
        int c = (int) Double.parseDouble(jTextField1.getText());
        String l1 = defaultFormat.format(p);
        l1 = l1.substring(0, l1.indexOf(","));
        String l2 = defaultFormat.format(p * c);
        l2 = l2.substring(0, l2.indexOf(","));
        jLabel4.setText(l1);
        jLabel6.setText(l2);
        ((Presentador) jLabel16).ActualizarFotograma(producto.imagen);
    }

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        actualizarProductoIngresar();
    }//GEN-LAST:event_jList1ValueChanged

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        jTextField1.setSelectionEnd(1000);
        jTextField1.setSelectionStart(0);
    }//GEN-LAST:event_jTextField1FocusGained

    private void jButton2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton2KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButton2.doClick();
        }
        jTextField1.setText("1");
    }//GEN-LAST:event_jButton2KeyReleased

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        actualizarProductoIngresar();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButton2.requestFocus();
        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jList1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jTextField1.requestFocus();
        }
    }//GEN-LAST:event_jList1KeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (jTable1.getRowCount() <= 0) {
            JOptionPane.showMessageDialog(this, "No hay datos para facturar");
            return;
        }

        int PAGO_CONTADO = 1;
        int PAGO_CREDITO = 2;
        int VENTA = 3;
        String[] options = {"Cancelar", "Contado", "Crédito"};
        int tipoDePago;
        if (jRadioButton1.isSelected()) {
            tipoDePago = JOptionPane.showOptionDialog(this, "Tipo de pago",
                    "Selección",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]
            );
        } else {
            tipoDePago = VENTA;
        }

        if (tipoDePago <= 0) {
            JOptionPane.showMessageDialog(this, "Se ha cancelado la acción", "Cancelado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreCreditado = "";
        int montoAbonado = 0;

        if (tipoDePago == PAGO_CREDITO) {

            JList list = new JList(ObtenerListaDeCreditarios());
            ListDialog dialog = new ListDialog("Clientes con credito", list);
            dialog.show();
            if (dialog.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Se ha cancelado la acción", "Cancelado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            nombreCreditado = dialog.getSelectedItem().toString();

            while (true) {
                Object textoMontoAbonado = JOptionPane.showInputDialog(
                        null,
                        "Monto abonado",
                        "Entrada",
                        JOptionPane.QUESTION_MESSAGE,
                        null, null,
                        "0"
                );
                try {
                    montoAbonado = Integer.parseInt(textoMontoAbonado.toString());
                    break;
                } catch (Exception e) {
                }
                if (textoMontoAbonado == null) {
                    JOptionPane.showMessageDialog(this, "Se ha cancelado");
                    return;
                }
            }

        }

        boolean esCompra = jRadioButton2.isSelected();

        Calendar fecha = Calendar.getInstance();

        String nombreArchivo = fecha.get(Calendar.YEAR)
                + "-" + (fecha.get(Calendar.MONTH) + 1)
                + "-" + fecha.get(Calendar.DAY_OF_MONTH)
                + " " + fecha.get(Calendar.HOUR_OF_DAY) + "-"
                + fecha.get(Calendar.MINUTE) + "-"
                + fecha.get(Calendar.SECOND) + ".txt";

        CarpetaDeRecursos.Recurso recursoFactura = carpetaFacturas.GenerarRecurso(nombreArchivo);
        CarpetaDeRecursos carpetaCliente = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/creditos/" + nombreCreditado);
        CarpetaDeRecursos.Recurso recursoCreditado = carpetaCliente.GenerarRecurso(nombreArchivo);

        String textoGuardar = "";
        if (esCompra) {
            String titulo = JOptionPane.showInputDialog("Tìtulo");
            textoGuardar = "Titulo: " + (titulo != null ? titulo : "") + "\n";
        }
        if (tipoDePago == PAGO_CREDITO) {
            String titulo = JOptionPane.showInputDialog("Nota:");
            if (titulo == null || titulo.trim().isEmpty()) {
                titulo = "CREDITO A " + nombreCreditado.toUpperCase();
            }
            textoGuardar = "Titulo: " + titulo + "\n";
        }
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            String renglón = "";
            for (int j = 0; j < jTable1.getColumnCount(); j++) {
                renglón += jTable1.getValueAt(i, j).toString() + "\t";
            }
            textoGuardar += renglón + "\n";
            for (int j = 0; j < productos.size(); j++) {
                if (productos.get(j).Nombre.equals(jTable1.getValueAt(i, 0).toString())) {
                    Producto producto = productos.get(j);
                    producto.modificarCantidad(
                            producto.obtenerCantidad() - (int) Double.parseDouble(jTable1.getValueAt(i, 1).toString()) * (esCompra ? -1 : 1)
                    );
                    break;
                }
            }
        }
        textoGuardar += "\n";
        String textoValor = "";
        if (esCompra) {
            int valor = 0;
            while (valor <= 0) {
                try {
                    valor = Integer.parseInt(JOptionPane.showInputDialog("Valor"));
                } catch (Exception e) {
                }
            }
            textoValor = "valor total: " + -valor;
        } else {
            if (tipoDePago == PAGO_CREDITO) {
                textoValor = "valor total: " + montoAbonado;
            } else {
                textoValor = "valor total: " + jLabel8.getText().replace("$", "").replace(".", "");
            }
        }
        textoGuardar += textoValor;
        LectoEscrituraArchivos.EscribirArchivo_ASCII(recursoFactura.ObtenerArchivo(), textoGuardar);
        if (tipoDePago == PAGO_CREDITO) {
            LectoEscrituraArchivos.EscribirArchivo_ASCII(
                    recursoCreditado.ObtenerArchivo(),
                    textoGuardar.replace(textoValor, "valor total: -" + (Integer.parseInt(jLabel8.getText().replace("$", "").replace(".", "")) - montoAbonado))
            );
        }
        eliminarFilasDeUnJTable(jTable1);
        JOptionPane.showMessageDialog(this, "Factura generada");
        actualizarTodo();
        jRadioButton1.setSelected(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        refrescarFacturas();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        refrescarFacturas();
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jList2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList2ValueChanged
        if (jList2.getSelectedValue() == null) {
            return;
        }
        eliminarFilasDeUnJTable(jTable2);
        jLabel7.setText("");
        try {
            String ruta = carpetaFacturas.DIRECCIÓN_CARPETA() + jList2.getSelectedValue();
            String txt = LectoEscrituraArchivos.LeerArchivo_ASCII(
                    ruta
            );
            String[] renglones = txt.split("\n");
            boolean esGasto = false;
            for (int i = 0; i < renglones.length; i++) {
                if (renglones[i].isEmpty()) {
                    continue;
                }
                if (renglones[i].contains("valor total")) {
                    break;
                }
                if (renglones[i].contains("Titulo:")) {
                    String[] items = renglones[i].split(":");
                    jLabel7.setText(items[1].trim().toUpperCase());
                    if (jLabel7.getText().equals("GASTOS")) {
                        esGasto = true;
                    }
                } else {
                    String[] items = renglones[i].split("\t");
                    DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                    if (items.length == 4) {
                        model.addRow(new Object[]{items[0], items[1], items[2], items[3]});
                    }
                    if (items.length == 2) {
                        model.addRow(new Object[]{items[0], "", items[1], ""});
                    }
                }

            }
            String valorTotal = renglones[renglones.length - 1].replace("valor total: ", "");
            jLabel9.setText(valorTotal);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jList2ValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int r = jTable1.getSelectedRow();
        if (r >= 0) {
            ((DefaultTableModel) jTable1.getModel()).removeRow(r);
        }
        sumarTotales();
    }//GEN-LAST:event_jButton1ActionPerformed

    void establecerNumeroInventario(int n) {
        int[] r = jTable3.getSelectedRows();
        String nombre = jTable3.getValueAt(r[0], 0).toString();
        try {
            for (int i = 0; i < r.length; i++) {
                jTable3.setValueAt(n, r[i], 1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El valor ingresado, no se puede interpretar", "Error", JOptionPane.ERROR_MESSAGE);
        }
        for (int i = 0; i < jTable3.getRowCount(); i++) {
            if (nombre.equals(jTable3.getValueAt(i, 0).toString())) {
                jTable3.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int r = jTable3.getSelectedRow();
        String nombre = jTable3.getValueAt(r, 0).toString();
        try {
            int n = (int) Double.parseDouble(JOptionPane.showInputDialog("Ingresa el aumento"));
            jTable3.setValueAt(Integer.parseInt(jTable3.getValueAt(r, 1).toString()) + n, r, 1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El valor ingresado, no se puede interpretar", "Error", JOptionPane.ERROR_MESSAGE);
        }
        for (int i = 0; i < jTable3.getRowCount(); i++) {
            if (nombre.equals(jTable3.getValueAt(i, 0).toString())) {
                jTable3.setRowSelectionInterval(i, i);
                break;
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int r = jTable3.getSelectedRow();
        String nombre = jTable3.getValueAt(r, 0).toString();
        try {
            int n = (int) Double.parseDouble(JOptionPane.showInputDialog("Establece el valor"));
            jTable3.setValueAt(n, r, 1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El valor ingresado, no se puede interpretar", "Error", JOptionPane.ERROR_MESSAGE);
        }
        for (int i = 0; i < jTable3.getRowCount(); i++) {
            if (nombre.equals(jTable3.getValueAt(i, 0).toString())) {
                jTable3.setRowSelectionInterval(i, i);
                break;
            }
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    int DIAS_RESUMEN = 30;
    String[][] resumenTabla = new String[2][DIAS_RESUMEN];

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        actualizarResumen();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        if (jTable1.getRowCount() > 0) {
            refrescarValoresTabla_venta();
        }
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            model.setValueAt("", i, 2);
            model.setValueAt("", i, 3);
        }
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        eliminarFilasDeUnJTable(jTable1);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        String gastoRegistrar = JOptionPane.showInputDialog("Gasto a registrar");
        if (gastoRegistrar == null) {
            return;
        }

        String nombreGastos = "GASTOS DISPONIBLES.txt";
        CarpetaDeRecursos.Recurso archivoGastos = carpetaGastos.GenerarRecurso(nombreGastos);

        if (archivoGastos.ObtenerArchivo().exists()) {
            try {
                String texto = LectoEscrituraArchivos.LeerArchivo_ASCII(archivoGastos.ObtenerArchivo());
                String[] renglones = texto.split("\n");
                for (String renglon : renglones) {
                    if (renglon.toLowerCase().equals(gastoRegistrar.toLowerCase())) {
                        JOptionPane.showMessageDialog(this, "Gasto ya está resgistrado", "error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                texto += "\n" + gastoRegistrar;
                LectoEscrituraArchivos.EscribirArchivo_ASCII(archivoGastos.ObtenerArchivo(), texto);
            } catch (Exception ex) {
            }
        } else {
            LectoEscrituraArchivos.EscribirArchivo_ASCII(archivoGastos.ObtenerArchivo(), gastoRegistrar);
        }
        cargarListaDeGastos();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        cargarListaDeGastos();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jList3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jList3KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButton11.requestFocus();
        }
    }//GEN-LAST:event_jList3KeyReleased

    void eliminarFilasDeUnJTable(JTable jt) {
        while (jt.getRowCount() > 0) {
            ((DefaultTableModel) jt.getModel()).removeRow(0);
        }
    }

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        try {
            int valor = Integer.parseInt(JOptionPane.showInputDialog("Valor del gasto"));
            DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
            model.addRow(new Object[]{jList3.getSelectedValue(), valor});
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
        }
        jLabel13.setText(-(int) sumarColumna(jTable5, 1) + "");
        jList3.requestFocus();
    }//GEN-LAST:event_jButton11ActionPerformed

    double sumarColumna(JTable jt, int c) {
        double t = 0;
        if (jt.getRowCount() > 0) {
            for (int i = 0; i < jt.getRowCount(); i++) {
                t += Double.parseDouble(jt.getValueAt(i, c).toString());
            }
        } else {
            t = 0;
        }
        return t;
    }


    private void jButton11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton11KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jButton11.doClick();
            jList3.requestFocus();
        }
    }//GEN-LAST:event_jButton11KeyReleased

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        Calendar fecha = Calendar.getInstance();

        String nombre = fecha.get(Calendar.YEAR)
                + "-" + (fecha.get(Calendar.MONTH) + 1)
                + "-" + fecha.get(Calendar.DAY_OF_MONTH)
                + " " + fecha.get(Calendar.HOUR_OF_DAY) + "-"
                + fecha.get(Calendar.MINUTE) + "-"
                + fecha.get(Calendar.SECOND) + ".txt";

        CarpetaDeRecursos.Recurso r = carpetaFacturas.GenerarRecurso(nombre);

        String textoGuardar = "";
        textoGuardar = "Titulo: GASTOS\n";
        for (int i = 0; i < jTable5.getRowCount(); i++) {
            String renglón = "";
            for (int j = 0; j < jTable5.getColumnCount(); j++) {
                renglón += jTable5.getValueAt(i, j).toString() + "\t";
            }
            textoGuardar += renglón + "\n";
        }
        textoGuardar += "\n";
        textoGuardar += "valor total: " + jLabel13.getText().replace("$", "").replace(".", "");

        LectoEscrituraArchivos.EscribirArchivo_ASCII(r.ObtenerArchivo(), textoGuardar);
        eliminarFilasDeUnJTable(jTable5);
        JOptionPane.showMessageDialog(this, "Factura generada");
        cargarInventario();
        actualizarResumen();
        jRadioButton1.setSelected(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        actualizarCreditados();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        String nombre = JOptionPane.showInputDialog("Nombre");
        if (nombre == null) {
            return;
        }
        CarpetaDeRecursos carpetaCliente = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/creditos/" + nombre);
        actualizarCreditados();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jList4ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList4ValueChanged
        if (jList4.getSelectedIndex() < 0) {
            jLabel18.setText("");
            jList5.setModel(new javax.swing.AbstractListModel<String>() {
                String[] strings = {};

                public int getSize() {
                    return strings.length;
                }

                public String getElementAt(int i) {
                    return strings[i];
                }
            });
        } else {
            creditos_actualizarListaRecibos();
            creditos_ponerSumaCliente();
        }
    }//GEN-LAST:event_jList4ValueChanged

    void creditos_actualizarListaRecibos() {
        String nombre = jList4.getSelectedValue();
        if (nombre == null) {
            return;
        }
        jLabel18.setText(nombre);
        CarpetaDeRecursos carpetaCliente = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/creditos/" + nombre);
        List<File> f = Arrays.asList(carpetaCliente.ObtenerCarpeta().listFiles());
        Collections.sort(f, (t, t1) -> {
            try {
                long d1 = Files.readAttributes(
                        t.toPath(),
                        BasicFileAttributes.class
                ).creationTime().to(TimeUnit.MILLISECONDS);
                long d2 = Files.readAttributes(
                        t1.toPath(),
                        BasicFileAttributes.class
                ).creationTime().to(TimeUnit.MILLISECONDS);
                return new Long(d2).compareTo(d1);
            } catch (Exception e) {
                return 0;
            }
        });

        String nombresCreditos[] = new String[f.size()];
        for (int i = 0; i < f.size(); i++) {
            File file = f.get(i);
            nombresCreditos[i] = file.getName();
        }
        jList5.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = nombresCreditos;

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
        jList5.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object value, int index, boolean isSelected, boolean isFocus) {
                Component c = super.getListCellRendererComponent(jlist, value, index, isSelected, isFocus);
                String ruta = carpetaCliente.DIRECCIÓN_CARPETA() + value.toString();
                try {
                    String texto = LectoEscrituraArchivos.LeerArchivo_ASCII(ruta);
                    if (texto.contains("DEUDA")) {
                        setForeground(Color.RED);
                    } else if (texto.contains("Abono\t")) {
                        setForeground(new Color(0, 128, 0));
                    } else if (texto.contains("Saldado\t")) {
                        setForeground(new Color(200, 128, 0));
                    }
                } catch (Exception ex) {
                }
                return c;
            }

        });
    }

    void creditos_ponerSumaCliente() {
        String nombre = jList4.getSelectedValue();
        if (nombre == null) {

        }
        CarpetaDeRecursos carpetaCliente = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/creditos/" + nombre);
        File[] f = carpetaCliente.ObtenerCarpeta().listFiles();
        jLabel19.setText(sumarRecibos(f) + "");
    }

    int sumarRecibos(File... recibos) {
        int suma = 0;
        for (File recibo : recibos) {
            suma += valorRecibo(recibo);
        }
        return suma;
    }

    int valorRecibo(File recibo) {
        try {
            String texto = LectoEscrituraArchivos.LeerArchivo_ASCII(recibo);
            String[] renglones = texto.split("\n");
            String renglonValor = renglones[renglones.length - 1]
                    .replace("valor total: ", "")
                    .replace("$", "")
                    .replace(".", "");
            return Integer.parseInt(renglonValor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private void jList5ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList5ValueChanged
        if (jList5.getSelectedValue() == null) {
            return;
        }
        String nombre = jList4.getSelectedValue();
        if (nombre == null) {
            return;
        }
        CarpetaDeRecursos carpetaCliente = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/creditos/" + nombre);
        CarpetaDeRecursos.Recurso r = carpetaCliente.GenerarRecurso(jList5.getSelectedValue());
        eliminarFilasDeUnJTable(jTable6);
        try {
            String texto = LectoEscrituraArchivos.LeerArchivo_ASCII(r.ObtenerArchivo());
            String[] renglones = texto.split("\n");
            for (int i = 0; i < renglones.length; i++) {
                String renglon = renglones[i];
                if (renglon.isEmpty()) {
                    continue;
                }
                if (renglon.contains("Titulo: ")) {
                    jLabel10.setText(renglon.replace("Titulo: ", ""));
                    continue;
                }
                if (renglon.contains("valor total: ")) {
                    jLabel17.setText(renglon.replace("valor total: ", ""));
                    continue;
                }
                String[] arr = renglon.split("\t");
                DefaultTableModel model = (DefaultTableModel) jTable6.getModel();
                if (arr.length == 4) {
                    model.addRow(new Object[]{arr[0], arr[1], arr[2], arr[3]});
                }
                if (arr.length == 2) {
                    model.addRow(new Object[]{arr[0], "", arr[1], ""});
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jList5ValueChanged

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        String nombre = jList4.getSelectedValue();
        if (nombre == null) {
            JOptionPane.showMessageDialog(this, "Seleccione a alguién", "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Object textoAbono = JOptionPane.showInputDialog(
                null,
                "Monto abonado",
                "Entrada",
                JOptionPane.QUESTION_MESSAGE,
                null, null,
                "0"
        );
        if (textoAbono == null) {
            JOptionPane.showMessageDialog(this, "Se ha cancelado", "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int abono = 0;
        try {
            abono = Integer.parseInt(textoAbono.toString());
            if (abono <= 0) {
                JOptionPane.showMessageDialog(this, "No hacer nada", "", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se puede interpretar el número ingresado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String textoGuardar = "Titulo: ABONO " + jList4.getSelectedValue().toUpperCase() + "\nAbono\t" + abono + "\n\nvalor total: " + abono;
        CarpetaDeRecursos carpetaCliente = new CarpetaDeRecursos(
                CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/creditos/" + nombre
        );
        Calendar fecha = Calendar.getInstance();
        String nombreArchivo = fecha.get(Calendar.YEAR)
                + "-" + (fecha.get(Calendar.MONTH) + 1)
                + "-" + fecha.get(Calendar.DAY_OF_MONTH)
                + " " + fecha.get(Calendar.HOUR_OF_DAY) + "-"
                + fecha.get(Calendar.MINUTE) + "-"
                + fecha.get(Calendar.SECOND) + ".txt";
        CarpetaDeRecursos.Recurso r = carpetaFacturas.GenerarRecurso(nombreArchivo);
        CarpetaDeRecursos.Recurso r2 = carpetaCliente.GenerarRecurso(nombreArchivo);
        LectoEscrituraArchivos.EscribirArchivo_ASCII(r.ObtenerArchivo(), textoGuardar);
        LectoEscrituraArchivos.EscribirArchivo_ASCII(r2.ObtenerArchivo(), textoGuardar);
        actualizarTodo();
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        generarCredito(false);
    }//GEN-LAST:event_jButton16ActionPerformed

    void generarCredito(boolean esGasto) {
        if (jList4.getSelectedValue() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione al cliente", "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Object textoAbono = JOptionPane.showInputDialog(
                null,
                "Valor",
                "Entrada",
                JOptionPane.QUESTION_MESSAGE,
                null, null,
                "0"
        );
        if (textoAbono == null) {
            JOptionPane.showMessageDialog(this, "Se ha cancelado", "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int credito = 0;
        try {
            credito = Integer.parseInt(textoAbono.toString());
            if (credito <= 0) {
                JOptionPane.showMessageDialog(this, "No hacer nada", "", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se puede interpretar el número ingresado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Object razon = JOptionPane.showInputDialog(
                null,
                "Razón",
                "Entrada",
                JOptionPane.QUESTION_MESSAGE,
                null, null,
                "Sin especificar"
        );
        if (razon == null || razon.toString().isEmpty()) {
            razon = "Sin especificar";
        }
        String textoGuardar = "Titulo: DEUDA " + jList4.getSelectedValue().toUpperCase() + "\n" + razon.toString() + "\t" + -credito + "\n\nvalor total: " + -credito;
        CarpetaDeRecursos carpetaCliente = new CarpetaDeRecursos(
                CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/creditos/" + jList4.getSelectedValue()
        );
        Calendar fecha = Calendar.getInstance();
        String nombreArchivo = fecha.get(Calendar.YEAR)
                + "-" + (fecha.get(Calendar.MONTH) + 1)
                + "-" + fecha.get(Calendar.DAY_OF_MONTH)
                + " " + fecha.get(Calendar.HOUR_OF_DAY) + "-"
                + fecha.get(Calendar.MINUTE) + "-"
                + fecha.get(Calendar.SECOND) + ".txt";

        CarpetaDeRecursos.Recurso r2 = carpetaCliente.GenerarRecurso(nombreArchivo);
        if (esGasto) {
            CarpetaDeRecursos.Recurso r = carpetaFacturas.GenerarRecurso(nombreArchivo);
            LectoEscrituraArchivos.EscribirArchivo_ASCII(r.ObtenerArchivo(), textoGuardar);
        }
        LectoEscrituraArchivos.EscribirArchivo_ASCII(r2.ObtenerArchivo(), textoGuardar);
        actualizarTodo();
    }


    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            jButton1.doClick();
        }
    }//GEN-LAST:event_jTable1KeyReleased

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        String nombre = jList4.getSelectedValue();
        if (nombre == null) {
            JOptionPane.showMessageDialog(this, "Seleccione a alguién", "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int input = JOptionPane.showConfirmDialog(
                null,
                "¿Seguro que desea saldar a " + nombre + "?",
                "Cuestión",
                JOptionPane.YES_NO_OPTION
        );
        if (input == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(this, "No hacer nada", "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int saldar = 0;

        try {
            saldar = Integer.parseInt(jLabel19.getText().replace("-", ""));
            if (saldar <= 0) {
                JOptionPane.showMessageDialog(this, "No hacer nada", "", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se puede interpretar el número ingresado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String textoGuardar = "Titulo:  EQUILIBRIO\nSaldado\t" + saldar + "\n\nvalor total: " + saldar;
        CarpetaDeRecursos carpetaCliente = new CarpetaDeRecursos(
                CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/creditos/" + jList4.getSelectedValue()
        );
        Calendar fecha = Calendar.getInstance();
        String nombreArchivo = fecha.get(Calendar.YEAR)
                + "-" + (fecha.get(Calendar.MONTH) + 1)
                + "-" + fecha.get(Calendar.DAY_OF_MONTH)
                + " " + fecha.get(Calendar.HOUR_OF_DAY) + "-"
                + fecha.get(Calendar.MINUTE) + "-"
                + fecha.get(Calendar.SECOND) + ".txt";
        CarpetaDeRecursos.Recurso r2 = carpetaCliente.GenerarRecurso(nombreArchivo);
        LectoEscrituraArchivos.EscribirArchivo_ASCII(r2.ObtenerArchivo(), textoGuardar);
        creditos_actualizarListaRecibos();
        creditos_ponerSumaCliente();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Sistema.Abrir_URL_EnNavegador("https://docs.google.com/spreadsheets/d/1lUOMcJ8Ujr6mp_f8Ca6RCW4SjcMOR6HC00erMDDaleE/edit?usp=sharing");
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        cargarListaPrecios();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        try {
            JOptionPane.showMessageDialog(this, "Empezar a generar PDF");
            GeneradorDePDF.main(null);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        generarCredito(true);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        ActualizarCantidadProductosVendidos();
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        String nombre = jList4.getSelectedValue();
        if (nombre == null) {
            return;
        }
        CarpetaDeRecursos carpetaCliente = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/creditos/" + nombre);
        String nuevoNombre = JOptionPane.showInputDialog("Nuevo nombre");
        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Acción cancelada", "", JOptionPane.ERROR_MESSAGE);
            return;
        }
        carpetaCliente.ObtenerCarpeta().renameTo(
                new File(Sistema.ruta_jeffAporta_appdata + "\\Dulceria facturación\\creditos\\" + nuevoNombre + "\\")
        );
        actualizarCreditados();
    }//GEN-LAST:event_jButton20ActionPerformed

    void ActualizarCantidadProductosVendidos() {
        new Thread() {
            @Override
            public void run() {
                ArrayList<comodinProducto_cantidad> contadorProductos = new ArrayList<>();
                for (Producto producto : productos) {
                    contadorProductos.add(new comodinProducto_cantidad(producto.Nombre));
                }
                File[] archivos = carpetaFacturas.ListarArchivos();
                for (File archivo : archivos) {
                    if (valorRecibo(archivo) >= 0) {
                        try {
                            String texto = LectoEscrituraArchivos.LeerArchivo_ASCII(archivo);
                            String[] renglones = texto.split("\n");
                            for (String renglon : renglones) {
                                String[] items = renglon.split("\t");
                                if (items.length == 4) {
                                    for (comodinProducto_cantidad contadorProducto : contadorProductos) {
                                        if (contadorProducto.nombre.equals(items[0])) {
                                            contadorProducto.cantidad += Integer.parseInt(items[1]);
                                            contadorProducto.valor += Integer.parseInt(items[3]);
                                        }
                                    }
                                }
                            }
                        } catch (Exception ex) {
                        }
                    }
                }
                eliminarFilasDeUnJTable(jTable7);
                Collections.sort(contadorProductos, (t, t1) -> {
                    return new Integer(t1.valor).compareTo(t.valor);
                });
                int totalProductosVendidos = 0;
                int totalDineroVendido = 0;
                for (comodinProducto_cantidad contadorProducto : contadorProductos) {
                    totalProductosVendidos += contadorProducto.cantidad;
                    totalDineroVendido += contadorProducto.valor;
                }
                for (comodinProducto_cantidad contadorProducto : contadorProductos) {
                    DefaultTableModel model = (DefaultTableModel) jTable7.getModel();
                    model.addRow(new Object[]{
                        contadorProducto.nombre,
                        contadorProducto.cantidad,
                        Matemática.recortarDecimales(100.0 * contadorProducto.cantidad / totalProductosVendidos, 2) + "%",
                        contadorProducto.valor,
                        Matemática.recortarDecimales(100.0 * contadorProducto.valor / totalDineroVendido, 2) + "%"
                    });
                }
            }
        }.start();
    }

    class comodinProducto_cantidad {

        int cantidad = 0;
        int valor = 0;
        String nombre;

        public comodinProducto_cantidad(String nombre) {
            this.nombre = nombre;
        }
    }

    String[] ObtenerListaDeCreditarios() {
        File[] f = carpetaCreditos.ObtenerCarpeta().listFiles((file, string) -> {
            return !"null".equals(string);
        });
        String nombres[] = new String[f.length];
        for (int i = 0; i < f.length; i++) {
            File file = f[i];
            nombres[i] = file.getName();
        }
        Arrays.sort(nombres);
        return nombres;
    }

    void actualizarCreditados() {
        String[] nombres = ObtenerListaDeCreditarios();
        jList4.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = nombres;

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
    }

    void cargarListaDeGastos() {
        String nombreGastos = "GASTOS DISPONIBLES.txt";
        CarpetaDeRecursos.Recurso archivoGastos = carpetaGastos.GenerarRecurso(nombreGastos);

        try {
            String texto = LectoEscrituraArchivos.LeerArchivo_ASCII(archivoGastos.ObtenerArchivo());
            String[] renglones = texto.split("\n");
            jList3.setModel(new javax.swing.AbstractListModel<String>() {
                String[] strings = renglones;

                public int getSize() {
                    return strings.length;
                }

                public String getElementAt(int i) {
                    return strings[i];
                }
            });
        } catch (Exception ex) {
        }
    }

    void actualizarResumen() {
        eliminarFilasDeUnJTable(jTable4);
        Calendar c = Calendar.getInstance();
        int total = 0;
        int max = 0;
        for (int i = 0; i < DIAS_RESUMEN; i++) {
            String nombreDía = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH);
            File[] facturas = carpetaFacturas.ObtenerCarpeta().listFiles((file, name) -> {
                return name.split(" ")[0].equals(nombreDía);
            });
            resumenTabla[0][i] = nombreDía;
            int valor = 0;
            for (File factura : facturas) {
                try {
                    String texto = LectoEscrituraArchivos.LeerArchivo_ASCII(factura);
                    String[] renglones = texto.split("\n");
                    valor += Integer.parseInt(
                            renglones[renglones.length - 1].replace("valor total: ", "").replace(".", "").replace("$", "").trim()
                    );
                } catch (Exception ex) {
                }
            }
            if (max < valor) {
                max = valor;
            }
            total += valor;
            resumenTabla[1][i] = valor + "";
            DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
            model.addRow(new Object[]{resumenTabla[0][i], resumenTabla[1][i]});
            c.add(Calendar.DAY_OF_MONTH, -1);
        }
        jLabel12.setText("Ventas + Compras + Gastos = " + total);

        BufferedImage img = new BufferedImage(DIAS_RESUMEN * 35 + 30, 400 + 30 + 30, 2);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.drawLine(30, img.getHeight() - 30, img.getWidth(), img.getHeight() - 30);
        GeneradorDeTexto gdt = new GeneradorDeTexto()
                .ModificarTamañoFuente(10)
                .ColorFuente(Color.BLACK)
                .AtributoNormal_Plano();
        int[] xs = new int[DIAS_RESUMEN + 2];
        int[] ys = new int[DIAS_RESUMEN + 2];
        for (int i = 0; i < DIAS_RESUMEN; i++) {
            int x = 35 * i + 30 - 5;
            int h;
            if (max == 0) {
                h = 0;
            } else {
                h = (int) (Long.parseLong(resumenTabla[1][DIAS_RESUMEN - i - 1]) * 400 / max);
            }
            if (h < 0) {
                h = 0;
                g.setColor(Color.RED);
            }
            int y = img.getHeight() - 30 - h - 5;
            xs[i] = x + 5;
            ys[i] = y + 5;
            g.fillOval(x, y, 10, 10);
            g.setColor(Color.BLACK);
            BufferedImage txtsaldo = gdt.GenerarTexto(resumenTabla[1][DIAS_RESUMEN - i - 1]);
            g.drawImage(
                    txtsaldo,
                    x - txtsaldo.getWidth() / 2,
                    y - txtsaldo.getHeight(),
                    null
            );
            int p = resumenTabla[0].length - i - 1;
            if (p < 0) {
                p = 0;
            }
            BufferedImage txt = gdt.GenerarTexto(
                    resumenTabla[0][p]
                            .replace(c.get(Calendar.YEAR) + "-", "")
                            .replace((c.get(Calendar.YEAR) - 1) + "-", "")
                            .replace("-", "/")
            );
            g.drawImage(
                    txt,
                    35 * i + 30 - txt.getWidth() / 2,
                    img.getHeight() - 15,
                    null
            );
        }
        xs[30] = 35 * 29 + 30;
        ys[30] = img.getHeight() - 30;
        xs[31] = 30;
        ys[31] = img.getHeight() - 30;
        g.setColor(new Color(0, 128, 255, 60));
        g.fillPolygon(xs, ys, 32);
        for (int i = 1; i < 5; i++) {
            int y = (int) (400 * ((double) (i - 1) / 4)) + 30;
            g.drawLine(30, y, img.getWidth(), y);
            BufferedImage txt = gdt.GenerarTexto(max * (5 - i) / 4 + "");
            g.drawImage(
                    txt,
                    0,
                    y - txt.getHeight() / 2,
                    null
            );
        }
        ((Presentador) jLabel11).ActualizarFotograma(img);
    }

    void refrescarFacturas() {
        List<File> a = Arrays.asList(carpetaFacturas.ListarArchivos());
        Collections.sort(a, (t, t1) -> {
            try {
                long d1 = Files.readAttributes(
                        t.toPath(),
                        BasicFileAttributes.class
                ).creationTime().to(TimeUnit.MILLISECONDS);
                long d2 = Files.readAttributes(
                        t1.toPath(),
                        BasicFileAttributes.class
                ).creationTime().to(TimeUnit.MILLISECONDS);
                return new Long(d1).compareTo(d2);
            } catch (Exception e) {
                return 0;
            }
        });
        File[] archivos = new File[a.size()];
        for (int i = 0; i < archivos.length; i++) {
            archivos[i] = a.get(i);
        }

        String[] nombres = new String[archivos.length];
        for (int i = 0; i < archivos.length; i++) {
            nombres[i] = archivos[archivos.length - 1 - i].getName()
                    .replaceFirst("-", "/")
                    .replaceFirst("-", "/")
                    .replaceFirst("-", ":")
                    .replaceFirst("-", ":")
                    .replace(".txt", "");
        }
        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = nombres;

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i].replace("/", "-").replace(":", "-") + ".txt";
            }
        });
        jList2.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object value, int index, boolean isSelected, boolean isFocus) {
                Component c = super.getListCellRendererComponent(jlist, value, index, isSelected, isFocus);
                String ruta = carpetaFacturas.DIRECCIÓN_CARPETA() + jList2.getModel().getElementAt(index);
                try {
                    String texto = LectoEscrituraArchivos.LeerArchivo_ASCII(ruta);
                    if (texto.contains("CREDITO A")) {
                        setForeground(Color.BLUE);
                    } else if (texto.contains("Abono\t")) {
                        setForeground(new Color(0, 128, 0));
                    } else {
                        int valor = valorRecibo(new File(ruta));
                        if (valor < 0) {
                            setForeground(Color.RED);
                        }
                    }
                } catch (Exception ex) {
                }
                return c;
            }

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private static javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jList3;
    private javax.swing.JList<String> jList4;
    private javax.swing.JList<String> jList5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    static final class Producto {

        String Nombre;
        String distribuidor;
        String telefóno;
        BufferedImage imagen = imgDefault;
        CarpetaDeRecursos.Recurso archivoCantidad;
        double precio;

        public Producto(String Nombre, double precio, String distribuidor, String telefóno) {
            this.Nombre = Nombre;
            this.precio = precio;
            this.distribuidor = distribuidor;
            this.telefóno = telefóno;
            archivoCantidad = carpetaInventario.GenerarRecurso(Nombre.replace("&", "y").replace(":", "-") + ".txt");
            establecerCantidad();
        }

        void establecerImagen(String url) {
            File archivoPNG = carpetaImagenesProductos.GenerarRecurso(Nombre + ".png").ObtenerArchivo();
            File archivoJPG = carpetaImagenesProductos.GenerarRecurso(Nombre + ".jpg").ObtenerArchivo();
            if (archivoPNG.exists()) {
                mostrarTextoVentanaCarga("Imágen\n" + "Cargando:\n" + archivoPNG.getName());
                imagen = LectoEscrituraArchivos.cargar_imagen(archivoPNG);
                return;
            }
            mostrarTextoVentanaCarga("Imágen\n" + "Descargando:\n" + Nombre);
            if (archivoJPG.exists()) {
                imagen = LectoEscrituraArchivos.cargar_imagen(archivoJPG);
            } else {
                carpetaImagenesProductos.GenerarRecurso(
                        url, Nombre + ".png"
                );
                imagen = LectoEscrituraArchivos.cargar_imagen(archivoPNG);
            }
            if (imagen == imgDefault) {
                return;
            }
            BufferedImage img = GeneradorDePDF.prepararImagenProducto(imagen);
            LectoEscrituraArchivos.exportar_imagen(
                    Filtros_Lineales.EscalarGranCalidad(img, -1, 600),
                    archivoPNG
            );
            imagen = LectoEscrituraArchivos.cargar_imagen(archivoPNG);
        }

        int obtenerCantidad() {
            try {
                return Integer.parseInt(LectoEscrituraArchivos.LeerArchivo_ASCII(archivoCantidad.ObtenerArchivo()));
            } catch (Exception ex) {
                return 0;
            }
        }

        void modificarCantidad(int cantidad) {
            LectoEscrituraArchivos.EscribirArchivo_ASCII(archivoCantidad.ObtenerArchivo(), cantidad + "");
        }

        void establecerCantidad() {
            if (!archivoCantidad.ObtenerArchivo().exists()) {
                LectoEscrituraArchivos.EscribirArchivo_ASCII(archivoCantidad.ObtenerArchivo(), 0 + "");
            }
        }
    }
}
