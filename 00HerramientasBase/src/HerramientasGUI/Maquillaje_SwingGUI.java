package HerramientasGUI;

import HerramientaDeImagen.Filtros_Lineales;
import HerramientaDeImagen.ModoFusión.FiltroModoPintura;
import HerramientasColor.Filtro_Color;
import HerramientasMatemáticas.Dupla;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.plaf.basic.*;

public class Maquillaje_SwingGUI {

    public static void repaint(Component c) {//<editor-fold defaultstate="collapsed" desc="implementación de código »">
        SwingUtilities.updateComponentTreeUI(c);
    }//</editor-fold>

    public static void Transparencia_JPanels(Container container) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Component[] componentes = container.getComponents();
        Component[] c = new Component[componentes.length + 1];
        for (int i = 0; i < componentes.length; i++) {
            c[i] = componentes[i];
        }
        c[componentes.length - 1] = container;
        Transparencia_JPanel(c);
    }//</editor-fold>

    public static void Transparencia_JPanel(Component[] c) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (Component component : c) {
            if (component instanceof Container) {
                Container container = (Container) component;
                Transparencia_JPanel(container.getComponents());
            }
            if (component instanceof JPanel) {
                ((JPanel) component).setOpaque(false);
            }
        }
    }//</editor-fold>

    public static void Transparencia_JScrollPanes(Container c) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Transparencia_JScrollPanes(c.getComponents());
    }//</editor-fold>

    public static void Transparencia_JScrollPanes(Component[] c) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (Component component : c) {
            if (component instanceof Container) {
                Container container = (Container) component;
                Transparencia_JScrollPanes(container.getComponents());
            }
            if (component instanceof JScrollPane) {
                JScrollPane scrollPane = ((JScrollPane) component);
                scrollPane.getViewport().setOpaque(false);
                scrollPane.setOpaque(false);
                scrollPane.setBorder(null);
            }
        }
    }//</editor-fold>

    public static void Transparencia_JButton(JButton button) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
    }//</editor-fold>

    public static void Transparencia_JButtons(Container container) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Transparencia_JButtons(container.getComponents());
    }//</editor-fold>

    public static void TransparenciaJButtons(JButton[] JButtons) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (JButton jButton : JButtons) {
            Transparencia_JButton(jButton);
        }
    }//</editor-fold>

    public static void Transparencia_JButtons(Component[] componentes) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (Component component : componentes) {
            if (component instanceof Container) {
                Maquillaje_SwingGUI.Transparencia_JButtons((Container) component);
            }
            if (component instanceof JButton) {
                Transparencia_JButton((JButton) component);
            }
        }
    }//</editor-fold>

    public static void ColorLetraBotones(Container container, Color MouseFuera, Color MouseEncima, Color MousePresionando) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        ColorLetraBotones(container.getComponents(), MouseFuera, MouseEncima, MousePresionando);
    }//</editor-fold>

    public static void ColorLetraBotones(Component[] componentes, Color MouseFuera, Color MouseEncima, Color MousePresionando) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (Component component : componentes) {
            if (component instanceof Container) {
                Container container = (Container) component;
                ColorLetraBotones(container, MouseFuera, MouseEncima, MousePresionando);
            }
            if (component instanceof JButton) {
                ColorLetraBotón((JButton) component, MouseFuera, MouseEncima, MousePresionando);
            }
        }
    }//</editor-fold>

    public static void ColorLetraBotones(JButton[] JButtons, Color MouseFuera, Color MouseEncima, Color MousePresionando) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (JButton button : JButtons) {
            ColorLetraBotón(button, MouseFuera, MouseEncima, MousePresionando);
        }
    }//</editor-fold>

    public static void ColorLetraBotón(JButton button, Color MouseFuera, Color MouseEncima, Color MousePresionando) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        button.setForeground(MouseFuera);
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                button.setForeground(MousePresionando);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                if (me.getX() > button.getWidth() || me.getY() > button.getHeight()) {
                    button.setForeground(MouseFuera);
                } else if (me.getX() < 0 || me.getY() < 0) {
                    button.setForeground(MouseFuera);
                } else {
                    button.setForeground(MouseEncima);
                }
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                button.setForeground(MouseEncima);
            }

            @Override
            public void mouseExited(MouseEvent me) {
                button.setForeground(MouseFuera);
            }
        });
    }//</editor-fold>

    public static void OcultarPestañas_JTabbedPane(JTabbedPane jTabbedPane) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            jTabbedPane.setUI(new BasicTabbedPaneUI() {
                @Override
                public void paint(Graphics g, JComponent jc) {
                }

                @Override
                protected int calculateTabHeight(int i, int i1, int i2) {
                    return 0;
                }

                @Override
                protected int calculateTabWidth(int i, int i1, FontMetrics fm) {
                    return 0;
                }
            });
        } catch (Exception e) {
        }
    }//</editor-fold>

    public static JLabel SeparadorHorizontal(Paint color, float grosor, float anchura) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return new JLabel() {
            @Override
            public void paint(Graphics grphcs) {
                int na = (int) (getWidth() * anchura);
                int e = (getWidth() - na) / 2;
                Graphics2D g = (Graphics2D) grphcs;
                g.setPaint(color);
                g.setStroke(new BasicStroke(grosor));
                g.drawLine(e, getHeight() / 2, e + na, getHeight() / 2);
            }
        };
    }//</editor-fold>

    public static JLabel SeparadorHorizontal_Degradado(float p1, Color c1, float p2, Color c2, float grosor, float anchura) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return new JLabel() {
            @Override
            public void paint(Graphics grphcs) {
                int na = (int) (getWidth() * anchura);
                int e = (getWidth() - na) / 2;
                Graphics2D g = (Graphics2D) grphcs;
                g.setPaint(
                        new GradientPaint(
                                e + getWidth() * p1, 0, c1,
                                e + na - getWidth() * p2, 0, c2,
                                true
                        )
                );
                g.setStroke(new BasicStroke(grosor));
                g.drawLine(e, getHeight() / 2, getWidth() - e, getHeight() / 2);
            }
        };
    }//</editor-fold>

    public static void BarraSimple(JScrollPane scrollPane, Color colorBarra) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        BarraSimple(scrollPane, colorBarra, null);
    }//</editor-fold>

    public static void BarraSimple(JScrollPane scrollPane, Color colorBarra, Color colorFondo) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            Color c1 = colorBarra == null ? new Color(0, true) : colorBarra;
            Color c2 = colorFondo == null ? new Color(0, true) : colorFondo;

            protected JButton BotónNoVisible() {
                return new JButton("z") {
                    {
                        Dimension Dim0 = new Dimension(0, 0);
                        setPreferredSize(Dim0);
                        setMinimumSize(Dim0);
                        setMaximumSize(Dim0);
                    }
                };
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return BotónNoVisible();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return BotónNoVisible();
            }

            @Override
            protected void paintThumb(Graphics g1, JComponent c, Rectangle fondo) {
                Graphics2D g = (Graphics2D) g1;
                g.setColor(c1);
                g.fill(fondo);
            }

            @Override
            protected void paintTrack(Graphics g1, JComponent c, Rectangle barra) {
                Graphics2D g = (Graphics2D) g1;
                g.setColor(c2);
                g.fill(barra);
            }
        });
    }//</editor-fold>

    public static void AñadirImgBotón_Estilo1(JButton botón, BufferedImage Img) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            BufferedImage área = new BufferedImage(
                    botón.getWidth(), botón.getHeight(), BufferedImage.TYPE_INT_ARGB
            );
            Filtros_Lineales.Ajuste_Ajustar(Img, área, false, true);
            double cambio = .15;
            BufferedImage ImgPresionado = Filtro_Color.Brillo(área, -cambio);
            BufferedImage ImgEncima = Filtro_Color.Brillo(área, cambio);
            botón.setText("");
            botón.setIcon(new ImageIcon(área));
            botón.setPressedIcon(new ImageIcon(ImgPresionado));
            botón.setRolloverIcon(new ImageIcon(ImgEncima));
        } catch (Exception e) {
        }
    }//</editor-fold>

    public static void AñadirImgBotón_Estilo2(JButton botón, BufferedImage Img) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            BufferedImage área = new BufferedImage(
                    botón.getWidth(), botón.getHeight(), BufferedImage.TYPE_INT_ARGB
            );
            Filtros_Lineales.Ajuste_Ajustar(Img, área);
            double cambio = .20;
            BufferedImage ImgPresionado = FiltroModoPintura.Aplicar(
                    área, FiltroModoPintura.Brillo(-cambio)
            );
            BufferedImage ImgEncima = FiltroModoPintura.Aplicar(
                    área, FiltroModoPintura.Brillo(cambio)
            );
            botón.setText("");
            botón.setIcon(new ImageIcon(new BufferedImage(
                    botón.getWidth(), botón.getHeight(), BufferedImage.TYPE_INT_ARGB
            ) {
                {
                    Image im = área.getScaledInstance((int) (.9 * área.getWidth()), -1, Image.SCALE_SMOOTH);
                    BufferedImage imge = new BufferedImage(im.getWidth(null), im.getHeight(null), 2);
                    imge.getGraphics().drawImage(im, 0, 0, null);
                    Filtros_Lineales.Ajuste_Centrado(imge, this);
                }
            }));
            botón.setPressedIcon(new ImageIcon(ImgPresionado));
            botón.setRolloverIcon(new ImageIcon(ImgEncima));
        } catch (Exception e) {
        }
    }//</editor-fold>

    public static void AsignarImg_Label(JLabel lbl, BufferedImage Img) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            BufferedImage área = new BufferedImage(
                    lbl.getWidth(), lbl.getHeight(), BufferedImage.TYPE_INT_ARGB
            );
            Filtros_Lineales.Ajuste_Ajustar(Img, área, false, true);
            lbl.setText("");
            lbl.setIcon(new ImageIcon(área));
        } catch (Exception e) {
        }
    }//</editor-fold>

    public static void AsignarLista_JList(JList jList2, ArrayList<String> strings) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        jList2.setModel(new AbstractListModel<String>() {
            @Override
            public int getSize() {
                return strings.size();
            }

            @Override
            public String getElementAt(int i) {
                return strings.get(i);
            }
        });
    }//</editor-fold>

    public static void AsignarLista_JList(JList jList2, String[] strings) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        jList2.setModel(new AbstractListModel<String>() {
            @Override
            public int getSize() {
                return strings.length;
            }

            @Override
            public String getElementAt(int i) {
                return strings[i];
            }
        });
    }//</editor-fold>

    public static class JSlider_ColaTriangular extends JSlider {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        public int MargenLateral = 20;
        public float GrosorLíneas = 4;

        public Color colorProgreso = new Color(0x00D0FF);
        public Color colorSinProgreso = Color.DARK_GRAY;
        public Color colorIcono = Color.BLACK;

        public byte AlineaciónVerticalCola = Dupla.ABAJO;

        public boolean DibujarIcono = true;

        @Override
        public void paint(Graphics grphcs) {
            paintBorder(grphcs);
            Graphics2D g = (Graphics2D) grphcs;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            float p = (float) getValue() / getMaximum();
            int Vf = getWidth() - MargenLateral;
            int d = 18;
            int x = (int) Math.round((Vf - MargenLateral) * p + MargenLateral);
            int y = getHeight() / 2;
            g.setColor(colorSinProgreso);
            g.fillPolygon(
                    new int[]{Vf, x, x},
                    new int[]{
                        AlineaciónVerticalCola == Dupla.MEDIO ? y
                                : AlineaciónVerticalCola == Dupla.ARRIBA ? y - d / 2
                                        : y + d / 2 - (int) GrosorLíneas / 2,
                        y - d / 2 + (int) GrosorLíneas / 2, y + d / 2
                    },
                    3
            );
            g.setColor(colorProgreso);
            g.fillPolygon(
                    new int[]{MargenLateral, x, x},
                    new int[]{
                        AlineaciónVerticalCola == Dupla.MEDIO ? y
                                : AlineaciónVerticalCola == Dupla.ARRIBA ? y - d / 2
                                        : y + d / 2 - (int) GrosorLíneas / 2,
                        y - d / 2 + (int) GrosorLíneas / 2, y + d / 2
                    },
                    3
            );
            g.setColor(colorIcono);
            g.fillOval(x - d / 2, y - d / 2, d, d);
            g.setColor(colorProgreso);
            g.setStroke(new BasicStroke(GrosorLíneas));
            g.drawOval(x - d / 2, y - d / 2, d, d);
        }

        @Override
        public void paintImmediately(Rectangle rctngl) {
            super.paintImmediately(new Rectangle(0, 0, getWidth(), getHeight()));
        }

        @Override
        public void paintImmediately(int i, int i1, int i2, int i3) {
            super.paintImmediately(0, 0, getWidth(), getHeight());
        }
    }//</editor-fold>

    public static class JSlider_ColaLineal extends JSlider {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        public int MargenLateral = 20;
        public float GrosorLíneas = 4;

        public Color colorProgreso = new Color(0x00D0FF);
        public Color colorSinProgreso = Color.GRAY;
        public Color colorIcono = Color.BLACK;

        boolean DibujarIcono = true;

        @Override
        public void paint(Graphics grphcs) {
            paintBorder(grphcs);
            Graphics2D g = (Graphics2D) grphcs;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            float p = (float) getValue() / getMaximum();
            int Vf = getWidth() - MargenLateral;
            int d = 18;
            int x = (int) Math.round((Vf - MargenLateral) * p + MargenLateral);
            int y = getHeight() / 2;
            g.setColor(colorSinProgreso);
            g.setStroke(new BasicStroke(GrosorLíneas));
            g.drawLine(MargenLateral, y, Vf, y);
            g.setColor(colorProgreso);
            g.drawLine(MargenLateral, y, x, y);
            if (DibujarIcono) {
                g.setColor(colorIcono);
                g.fillOval(x - d / 2, y - d / 2, d, d);
                g.setColor(colorProgreso);
                g.drawOval(x - d / 2, y - d / 2, d, d);
            }
        }

        @Override
        public void paintImmediately(Rectangle rctngl) {
            super.paintImmediately(new Rectangle(0, 0, getWidth(), getHeight()));
        }

        @Override
        public void paintImmediately(int i, int i1, int i2, int i3) {
            super.paintImmediately(0, 0, getWidth(), getHeight());
        }
    }//</editor-fold>

    public static class JRadioButtonColorSolido extends JRadioButton {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        public Color Selección_Fondo = new Color(0x00FF8B);
        public Color NoSelección_Fondo = new Color(0xcccccc);
        public Color Selección_Fuente = Color.BLACK;
        public Color NoSelección_Fuente = Color.BLACK;

        public JRadioButtonColorSolido(String txt) {
            super(txt);
            setBorder(null);
            setIconTextGap(0);
            setDisabledIcon(new ImageIcon(new BufferedImage(1, 1, 2)));
            setDisabledSelectedIcon(new ImageIcon(new BufferedImage(1, 1, 2)));
            setSelectedIcon(new ImageIcon(new BufferedImage(1, 1, 2)));
            setIcon(new ImageIcon(new BufferedImage(1, 1, 2)));
            setHorizontalAlignment(CENTER);
        }

        @Override
        public void paint(Graphics grphcs) {
            Graphics2D g = (Graphics2D) grphcs;
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
            );
            if (isSelected()) {
                g.setColor(Selección_Fondo);
                setForeground(Selección_Fuente);
            } else {
                g.setColor(NoSelección_Fondo);
                setForeground(NoSelección_Fuente);
            }
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            Dupla d = Dupla.PosiciónCursorEnComponente(this);
            if (d.estáDentro(2, getWidth() - 2, 2, getHeight() - 2)) {
                g.setColor(new Color(0x30ffffff, true));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
            super.paint(g);
        }
    }
//</editor-fold>
}
