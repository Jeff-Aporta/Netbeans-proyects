package HerramientaDeImagen;

import HerramientaArchivos.ArrastrarArchivos.ArrastrarArchivos;
import HerramientaArchivos.BibliotecaArchivos;
import HerramientaArchivos.LectoEscrituraArchivos;
import static HerramientaDeImagen.Filtros_Lineales.EscalarGranCalidad;
import HerramientasGUI.NimbusModificado;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static HerramientaArchivos.BibliotecaArchivos.Imagenes.Logos.JeffAporta;
import javax.swing.JFrame;

public class Prueba_Filtro_Convolución extends JFrame {

    static final byte MASCARA_CUADRADA = 0;
    static final byte MASCARA_CIRCULAR = 1;

    static byte MASCARA;

    static BufferedImage imgPrueba = Filtros_Lineales.EscalarGranCalidad(BibliotecaArchivos.Imagenes.Test.Wallpaper_2(), 640, -1
    );

    Filtro DesenfoqueAritmético = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Desenfoque_aritmético(img, (int) jSpinner1.getValue());
        }

        @Override
        public String Nombre() {
            return "Desenfoque Aritmético";
        }
    };

    Filtro DesenfoqueRadial = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Desenfoque_Radial(img, (int) jSpinner1.getValue());
        }

        @Override
        public String Nombre() {
            return "Desenfoque Radial";
        }
    };

    Filtro DesenfoqueHorizontal = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Desenfoque_Horizontal(img, (int) jSpinner1.getValue());
        }

        @Override
        public String Nombre() {
            return "Desenfoque Horizontal";
        }
    };
    Filtro DesenfoqueVertical = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Desenfoque_Vertical(img, (int) jSpinner1.getValue());
        }

        @Override
        public String Nombre() {
            return "Desenfoque Vertical";
        }
    };

    Filtro EndurecerBordes = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.EndurecerBordes(img, (int) jSpinner1.getValue());
        }

        @Override
        public String Nombre() {
            return "Endurecer Bordes";
        }
    };
    Filtro PerfilarBordes = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.PerfilarBordes(img, (int) jSpinner1.getValue());
        }

        @Override
        public String Nombre() {
            return "Perfilar Bordes";
        }
    };
    Filtro DetectarBordesNeón = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.DetectarBordes_Neón(img, (int) jSpinner1.getValue());
        }

        @Override
        public String Nombre() {
            return "Detectar Bordes Neón";
        }
    };
    Filtro DetectarBordesRadio = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.DetectarBordes_Radio(img, (int) jSpinner1.getValue());
        }

        @Override
        public String Nombre() {
            return "Detectar Bordes Radio";
        }
    };
    Filtro DetectarBordesContraste = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Borde_Contraste(img, (int) jSpinner1.getValue());
        }

        @Override
        public String Nombre() {
            return "Detectar Borde Contraste";
        }
    };

    Filtro Sobel_0 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel_0(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 0°";
        }
    };
    Filtro Sobel_45 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel_45(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 45°";
        }
    };
    Filtro Sobel_90 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel_90(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 90°";
        }
    };
    Filtro Sobel_135 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel_135(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 135°";
        }
    };
    Filtro Sobel_180 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel_180(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 180°";
        }
    };
    Filtro Sobel_225 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel_225(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 225°";
        }
    };
    Filtro Sobel_270 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel_270(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 270°";
        }
    };
    Filtro Sobel_315 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel_315(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 315°";
        }
    };
    Filtro Sobel2_0 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel2_0(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 2 0°";
        }
    };
    Filtro Sobel2_45 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel2_45(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 2 45°";
        }
    };
    Filtro Sobel2_90 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel2_90(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 2 90°";
        }
    };
    Filtro Sobel2_135 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel2_135(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 2 135°";
        }
    };
    Filtro Sobel2_180 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel2_180(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 2 180°";
        }
    };
    Filtro Sobel2_225 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel2_225(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 2 225°";
        }
    };
    Filtro Sobel2_270 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel2_270(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 2 270°";
        }
    };
    Filtro Sobel2_315 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Sobel2_315(img);
        }

        @Override
        public String Nombre() {
            return "Sobel 2 315°";
        }
    };
    Filtro Relieve_0 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve_0(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 0°";
        }
    };
    Filtro Relieve_45 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve_45(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 45°";
        }
    };
    Filtro Relieve_90 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve_90(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 90°";
        }
    };
    Filtro Relieve_135 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve_135(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 135°";
        }
    };
    Filtro Relieve_180 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve_180(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 180°";
        }
    };
    Filtro Relieve_225 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve_225(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 225°";
        }
    };
    Filtro Relieve_270 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve_270(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 270°";
        }
    };
    Filtro Relieve_315 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve_315(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 315°";
        }
    };
    Filtro Relieve2_0 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve2_0(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 2 0°";
        }
    };
    Filtro Relieve2_45 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve2_45(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 2 45°";
        }
    };
    Filtro Relieve2_90 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve2_90(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 2 90°";
        }
    };
    Filtro Relieve2_135 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve2_135(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 2 135°";
        }
    };
    Filtro Relieve2_180 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve2_180(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 2 180°";
        }
    };
    Filtro Relieve2_225 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve2_225(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 2 225°";
        }
    };
    Filtro Relieve2_270 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve2_270(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 2 270°";
        }
    };
    Filtro Relieve2_315 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.Relieve2_315(img);
        }

        @Override
        public String Nombre() {
            return "Relieve 2 315°";
        }
    };

    Filtro DetectarBorde1 = new Filtro() {
        @Override
        public BufferedImage Aplicar(BufferedImage img) {
            return Filtro_Convolución.DetectarBorde_1(img);
        }

        @Override
        public String Nombre() {
            return "Detectar Borde 1";
        }
    };

    Filtro[] Filtros = {
        DesenfoqueRadial, DesenfoqueAritmético, DesenfoqueHorizontal, DesenfoqueVertical,
        EndurecerBordes, PerfilarBordes, DetectarBordesNeón, DetectarBordesRadio,
        DetectarBordesContraste,
        Sobel_0, Sobel_45, Sobel_90, Sobel_135, Sobel_180, Sobel_225, Sobel_270, Sobel_315,
        Sobel2_0, Sobel2_45, Sobel2_90, Sobel2_135, Sobel2_180, Sobel2_225, Sobel2_270, Sobel2_315,
        Relieve_0, Relieve_45, Relieve_90, Relieve_135, Relieve_180, Relieve_225, Relieve_270, Relieve_315,
        Relieve2_0, Relieve2_45, Relieve2_90, Relieve2_135, Relieve2_180, Relieve2_225, Relieve2_270, Relieve2_315,
        DetectarBorde1
    };

    public static void main(String args[]) {
        try {
            NimbusModificado.CargarTemaOscuroNimbus();
            NimbusModificado.CargarNimbus();
        } catch (Exception e) {
        }
        Prueba_Filtro_Convolución p = new Prueba_Filtro_Convolución();
        ArrastrarArchivos.Añadir(p, (String[] cadenas) -> {
            p.jTextField1.setText(cadenas[0]);
        });
    }

    public Prueba_Filtro_Convolución() {
        initComponents();
        CargarListaFiltros();
        CargarIconosAlpha();
        actualizar();

        try {
            setIconImage(EscalarGranCalidad(JeffAporta(), 32, -1));
        } catch (Exception e) {
        }

        setSize(662, 548);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void CargarIconosAlpha() {
        Color color = Color.WHITE;
        BufferedImage cuadrado = new BufferedImage(70, 70, 2) {
            {
                Graphics2D g = createGraphics();
                g.setColor(color);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        jLabel5.setIcon(new ImageIcon(cuadrado));
        BufferedImage círculo = new BufferedImage(70, 70, 2) {
            {
                Graphics2D g = createGraphics();
                g.setColor(color);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        jLabel4.setIcon(new ImageIcon(círculo));
    }

    void BordeRojo(JLabel lbl) {
        lbl.setBorder(BorderFactory.createLineBorder(new Color(255, 0, 0), 5));
    }

    void BordeNegro(JLabel lbl) {
        lbl.setBorder(BorderFactory.createLineBorder(new Color(0)));
    }

    public void CargarListaFiltros() {
        jList1.setModel(new javax.swing.AbstractListModel<String>() {

            @Override
            public int getSize() {
                return Filtros.length;
            }

            @Override
            public String getElementAt(int i) {
                try {
                    return Filtros[i].Nombre();
                } catch (Exception e) {
                    return "";
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 11, 640, 359);

        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(210, 380, 280, 130);

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });
        getContentPane().add(jSpinner1);
        jSpinner1.setBounds(500, 400, 60, 30);

        jLabel2.setText("Radio");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(500, 380, 140, 20);

        jLabel3.setText("Prueba de conservación de alpha");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 390, 190, 14);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel4MouseReleased(evt);
            }
        });
        getContentPane().add(jLabel4);
        jLabel4.setBounds(110, 414, 90, 90);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0), 5));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel5MouseReleased(evt);
            }
        });
        getContentPane().add(jLabel5);
        jLabel5.setBounds(10, 414, 90, 90);

        jButton1.setText("Cargar imagen");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(500, 490, 120, 23);
        getContentPane().add(jTextField1);
        jTextField1.setBounds(500, 455, 150, 30);

        jLabel6.setText("Ruta de la imagen");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(500, 440, 150, 14);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        actualizar();
    }//GEN-LAST:event_jList1ValueChanged

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        actualizar();
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jLabel5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseReleased
        BordeRojo(jLabel5);
        BordeNegro(jLabel4);
        MASCARA = MASCARA_CUADRADA;
        actualizar();
    }//GEN-LAST:event_jLabel5MouseReleased

    private void jLabel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseReleased
        BordeRojo(jLabel4);
        BordeNegro(jLabel5);
        MASCARA = MASCARA_CIRCULAR;
        actualizar();
    }//GEN-LAST:event_jLabel4MouseReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String ruta = jTextField1.getText();
        BufferedImage cargada = LectoEscrituraArchivos.cargar_imagen(ruta);
        jTextField1.setText("");
        if (cargada == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró la imagen en la ruta"
                    + "\nUse una imagen ubicada en una dirección del equipo o una ruta de internet"
                    + "\n\nTambién puede arrastrar una imagen y soltarla,\nautomaticamente se cargará su ruta en el equipo",
                    "Error", JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        BufferedImage img = new BufferedImage(640, 360, 2);
        Filtros_Lineales.Ajuste_Centrado_Ajustar(cargada, img);
        imgPrueba = img;
        actualizar();
    }//GEN-LAST:event_jButton1ActionPerformed

    void actualizar() {
        try {
            Filtros[jList1.getSelectedIndex()].ActualizarPresentador();
        } catch (Exception e) {
            if (jList1.getSelectedIndex() != -1) {
                e.printStackTrace();
                System.err.println("Error al calcular el filtro");
            }
            Filtros[0].ActualizarPresentador();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    public static javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables

    interface Filtro {

        BufferedImage Aplicar(BufferedImage img);

        default void ActualizarPresentador() {
            try {
                switch (MASCARA) {
                    case MASCARA_CUADRADA:
                        jLabel1.setIcon(new ImageIcon(Aplicar(imgPrueba)));
                        break;
                    case MASCARA_CIRCULAR:
                        BufferedImage mascara = new BufferedImage(imgPrueba.getWidth(), imgPrueba.getHeight(), 2) {
                            {
                                BufferedImage circulo = new BufferedImage(getHeight(), getHeight(), 2) {
                                    {
                                        Graphics2D g = createGraphics();
                                        g.fillOval(0, 0, getWidth(), getHeight());
                                    }
                                };
                                Filtros_Lineales.Ajuste_Ajustar(circulo, this);
                                Graphics2D g = createGraphics();
//                                g.setComposite(new ModoFusión_Normal().Modificar_AlphaDiscriminador(Constantes_ModoFusión.OP_ALPHA_DENTRO));
                                g.drawImage(imgPrueba, 0, 0, null);
                            }
                        };
                        jLabel1.setIcon(new ImageIcon(Aplicar(mascara)));
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String Nombre();
    }

}
