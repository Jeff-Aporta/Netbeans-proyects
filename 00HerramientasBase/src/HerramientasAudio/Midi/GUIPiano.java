package HerramientasAudio.Midi;

import HerramientaDeImagen.Filtros_Lineales;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientaDeImagen.ModoFusión.ModoFusión;
import static HerramientasAudio.Midi.BibliotecaSonidos.*;
import HerramientasAudio.Midi.ReproductorNotas.HerramientasMIDI;
import HerramientasGUI.Maquillaje_SwingGUI;
import HerramientasGUI.NimbusModificado;
import HerramientasGUI.Presentador;
import HerramientasMatemáticas.Dupla;
import HerramientasMatemáticas.Matemática;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.*;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GUIPiano extends JFrame {

    ReproductorNotas reproductorNotas = new ReproductorNotas();

    Tecla[] TeclasBlancas;
    Tecla[] TeclasNegras;

    public static void main(String args[]) {
        try {
            NimbusModificado.CargarTemaOscuroNimbus();
            NimbusModificado.CargarNimbus();
        } catch (Exception ex) {
        }
        new GUIPiano();
    }

    public GUIPiano() {//<editor-fold defaultstate="collapsed" desc="Constructor »">
        initComponents();
        jLabel1.setText("");
        jLabel2.setText("");
        jLabel3.setBorder(null);
        jLabel1.setBorder(null);
        jTabbedPane1.setBorder(null);
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(30);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        Maquillaje_SwingGUI.OcultarPestañas_JTabbedPane(jTabbedPane1);
        {
            String[] instrumentos = new String[ReproductorNotas.HerramientasMIDI.INSTRUMENTOS_SOPORTADOS];
            for (int i = 0; i < instrumentos.length; i++) {
                try {
                    instrumentos[i] = i + ") " + (String) ReproductorNotas.TablaInstrumentos_Obtener1(i)[0];
                } catch (MidiUnavailableException ex) {
                }
            }
            Maquillaje_SwingGUI.AsignarLista_JList(jList2, instrumentos);
        }
        jList2.setSelectedIndex(0);
        setVisible(true);
    }//</editor-fold>

    class MostrarNotas extends JLabel {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        long últimaPresión;
        float Eco;

        Tecla tecla;

        @Override
        public void paint(Graphics grphcs) {
            Graphics2D g = (Graphics2D) grphcs;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (tecla != null && tecla.PresionadaMouse) {
                Mostrar();
            }
            float p = (System.currentTimeMillis() - últimaPresión) / ((1 + Eco) * 1000f);
            if (p < 0) {
                p = 0;
            } else if (p > 1) {
                return;
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - p));
            Point2D center = new Point2D.Float(getWidth() / 2, getHeight() / 2);
            float radius = (getWidth() + getHeight()) / 2;
            float[] dist = {0.0f, 1.0f};
            Color[] colors = {new Color(0, true), Color.LIGHT_GRAY};
            RadialGradientPaint gradientPaint = new RadialGradientPaint(center, radius, dist, colors);
            g.setPaint(gradientPaint);
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paint(grphcs);
        }

        void Mostrar() {
            últimaPresión = System.currentTimeMillis();
            Eco = jSlider1.getValue() / 100f;
            ((Teclado) jLabel1).TomarMedida();
        }

        public void setText(String string, Tecla tecla) {
            super.setText("<html>"
                    + "<p align=\"center\">"
                    + string + "<br>"
                    + "<h1 style=\"color: #888888;\" align=\"center\">"
                    + "(" + HerramientasMIDI.CalcularByte_Nota(string) + ")"
                    + "</h2>"
            );
            this.tecla = tecla;
        }
    }//</editor-fold>

    class Teclado extends Presentador {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        int nOctavaInicio;
        int nOctavaFin;
        int RangoOctavas;

        final int anchoTeclado = 1400;
        final int altoTeclado = 440;

        boolean clicPresionado = false;

        Dupla DimTecla;

        {
            TIPO_AJUSTE_FOTOGRAMA = Filtros_Lineales.AJUSTE_AJUSTAR;
            ModificarRangoOctavas(2, 6);
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            sleep(1000 / 25);
                            actualizarConRegulador();
                        } catch (Exception ex) {
                        }
                    }
                }
            }.start();
            setFocusable(true);
        }

        void ModificarRangoOctavas(int i, int f) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            nOctavaInicio = Integer.min(i, f);
            nOctavaFin = Integer.max(i, f);
            RangoOctavas = nOctavaFin - nOctavaInicio + 1;

            float blancas = RangoOctavas * 7;
            float negras = RangoOctavas * 5;

            this.DimTecla = new Dupla(anchoTeclado / blancas, altoTeclado);

            TeclasBlancas = new Tecla[(int) blancas];
            {
                int nota = nOctavaInicio * 12;
                for (int j = 0; j < blancas; j++) {
                    TeclasBlancas[j] = new Tecla(
                            new RoundRectangle2D.Double(
                                    j * DimTecla.X, 0,
                                    DimTecla.X, DimTecla.Y, 15, 15
                            ),
                            nota
                    );
                    if (j % 7 == 2 || j % 7 == 6) {
                        nota++;
                    } else {
                        nota += 2;
                    }
                }
            }
            TeclasNegras = new Tecla[(int) negras];
            {
                int desfacePos = 0;
                int nota = nOctavaInicio * 12 + 1;
                for (int j = 0; j < negras; j++) {
                    TeclasNegras[j] = new Tecla(
                            new RoundRectangle2D.Double(
                                    DimTecla.X * (j + desfacePos) + (DimTecla.X * .5 + DimTecla.X * .7) / 2, 0,
                                    DimTecla.X * .7, DimTecla.Y * .6, 15, 15
                            ),
                            nota
                    );
                    if (j % 5 == 1 || j % 5 == 4) {
                        nota += 3;
                    } else {
                        nota += 2;
                    }
                    if (j % 5 == 1 || j % 5 == 4) {
                        desfacePos++;
                    }
                }
            }
        }//</editor-fold>

        Dupla PosCursor;
        long últimaActualización;

        void actualizar() {
            PosCursor = PosiciónCursorEnFotograma();
            ActualizarFotograma(renderizaciónTeclas());
        }

        void actualizarConRegulador() {
            PosCursor = PosiciónCursorEnFotograma();
            long reg = System.currentTimeMillis() - últimaActualización;
            if (!new Rectangle2D.Double(0, 0, anchoTeclado, altoTeclado).intersects(puntoCursor())) {
                float s = jSlider1.getValue() / 100f;
                if (reg > (2 + s) * 1000) {
                    return;
                }
            } else {
                TomarMedida();
            }
            jLabel2.repaint();
            ActualizarFotograma(renderizaciónTeclas());
        }

        void TomarMedida() {
            últimaActualización = System.currentTimeMillis();
        }

        BufferedImage renderizaciónTeclas() {
            BufferedImage retorno = new BufferedImage(anchoTeclado, altoTeclado, 2);
            Graphics2D g = retorno.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(3.5f));
            Dupla dimNegras = new Dupla(
                    TeclasNegras[0].área.getWidth(),
                    TeclasNegras[0].área.getHeight()
            );

            for (int i = 0; i < TeclasBlancas.length; i++) {
                Tecla tecla = TeclasBlancas[i];
                boolean MouseSobreTecla = tecla.área.intersects(puntoCursor());

                if (MouseSobreTecla) {//<editor-fold defaultstate="collapsed" desc="regla para evitar la colisión doble de blancas y negras">
                    int indice = i % 7;
                    boolean izquierda[] = new boolean[]{false, true, true, false, true, true, true};
                    boolean derecha[] = new boolean[]{true, true, false, true, true, true, false};
                    if (izquierda[indice]) {
                        if (puntoCursor().getY() < dimNegras.Alto()) {
                            if (puntoCursor().getX() - tecla.área.getX() <= dimNegras.Ancho() / 2) {
                                MouseSobreTecla = false;
                            }
                        }
                    }
                    if (derecha[indice]) {
                        if (puntoCursor().getY() < dimNegras.Alto()) {
                            if (puntoCursor().getX() - tecla.área.getX() >= tecla.área.getWidth() - dimNegras.Ancho() / 2) {
                                MouseSobreTecla = false;
                            }
                        }
                    }
                }//</editor-fold>

                g.setPaint(new GradientPaint(0, 0, Color.LIGHT_GRAY, 0, altoTeclado / 2, Color.WHITE, true));
                g.setComposite(AlphaComposite.SrcOver);
                g.fill(tecla.área);
                g.setColor(Color.GRAY);
                g.setComposite(AlphaComposite.SrcAtop);
                g.draw(tecla.área);
                if (jRadioButton1.isSelected()) {
                    if (i < AsciiTeclasBlancas.length) {
                        BufferedImage ascii = new GeneradorDeTexto()
                                .ModificarTamañoFuente(25)
                                .ColorFuente(Color.RED)
                                .GenerarTexto((char) AsciiTeclasBlancas[i] + "");
                        Dupla p = Dupla.Alinear(tecla.área, ascii, Dupla.MEDIO, Dupla.ABAJO);
                        g.drawImage(ascii, (int) (p.X + tecla.área.getX()), p.intY(), null);
                    }
                }
                if (MouseSobreTecla || tecla.PresionadaTeclado) {
                    if (clicPresionado || tecla.PresionadaTeclado) {
                        g.setColor(new Color(Color.HSBtoRGB((299 / 360f) * (tecla.nota / 12) / 10, 1, 1)));
                        tecla.presionarMouse();
                    } else {
                        tecla.soltarMouse();
                        g.setColor(Color.LIGHT_GRAY);
                    }
                    g.setComposite(new ModoFusión.Porcentual.Adición_Nula());
                    g.fill(tecla.área);
                } else {
                    tecla.soltarMouse();
                }
            }
            for (int i = 0; i < TeclasNegras.length; i++) {
                Tecla tecla = TeclasNegras[i];
                boolean MouseSobreTecla = tecla.área.intersects(puntoCursor());
                g.setPaint(new GradientPaint(0, 0, Color.DARK_GRAY, 0, (float) (tecla.área.getHeight() / 2), Color.BLACK, true));
                g.setComposite(AlphaComposite.SrcOver);
                g.fill(tecla.área);
                if (jRadioButton1.isSelected()) {
                    if (i < AsciiTeclasNegras.length) {
                        BufferedImage ascii = new GeneradorDeTexto()
                                .ModificarTamañoFuente(20)
                                .ColorFuente(Color.RED)
                                .GenerarTexto((char) AsciiTeclasNegras[i] + "");
                        Dupla p = Dupla.Alinear(tecla.área, ascii, Dupla.MEDIO, Dupla.ABAJO);
                        g.drawImage(ascii, (int) (p.X + tecla.área.getX()), p.intY(), null);
                    }
                }
                if (MouseSobreTecla || tecla.PresionadaTeclado) {
                    if (clicPresionado || tecla.PresionadaTeclado) {
                        g.setColor(new Color(Color.HSBtoRGB((299 / 360f) * (tecla.nota / 12) / 10, 1, 1)));
                        tecla.presionarMouse();
                    } else {
                        tecla.soltarMouse();
                        g.setColor(Color.LIGHT_GRAY);
                    }
                    g.setComposite(new ModoFusión.Porcentual.Incremento_Base());
                    g.fill(tecla.área);
                } else {
                    tecla.soltarMouse();
                }
            }
            return retorno;
        }

        Rectangle2D puntoCursor() {
            return new Rectangle2D.Double(PosCursor.X, PosCursor.Y, 1, 1);
        }
    }//</editor-fold>

    class Tecla {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        boolean blanca = true;

        RoundRectangle2D área;
        int nota;
        boolean PresionadaMouse = false;
        boolean PresionadaTeclado = false;

        public Tecla(RoundRectangle2D área, int nota) {
            this.área = área;
            this.nota = nota;
        }

        void presionarMouse() {
            if (PresionadaMouse) {
                return;
            }
            PresionadaMouse = true;
            reproductorNotas.Reproducir(nota);
            System.out.println(nota);
            ((MostrarNotas) jLabel2).setText(HerramientasMIDI.ConvertirNotaString(nota), this);
        }

        void soltarMouse() {
            if (!PresionadaMouse) {
                return;
            }
            PresionadaMouse = false;
            reproductorNotas.Suspender(nota);
        }

        void presionarTeclado() {
            if (PresionadaTeclado) {
                return;
            }
            PresionadaTeclado = true;
            reproductorNotas.Reproducir(nota);
            System.out.println(nota);
            ((MostrarNotas) jLabel2).setText(HerramientasMIDI.ConvertirNotaString(nota), this);
        }

        void soltarTeclado() {
            if (!PresionadaTeclado) {
                return;
            }
            PresionadaTeclado = false;
            reproductorNotas.Suspender(nota);
        }
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Código generado con ayuda de Netbeans »">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new Teclado();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jLabel2 = new MostrarNotas();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jSlider2 = new javax.swing.JSlider();
        jSlider1 = new javax.swing.JSlider();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jButton14 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Teclas del piano");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jLabel1MouseDragged(evt);
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
        jLabel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jLabel1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jLabel1KeyReleased(evt);
            }
        });

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("F#");
        jTabbedPane1.addTab("Mostrar Notas", jLabel2);

        jList2.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

        jTabbedPane1.addTab("Selección de instrumento", jScrollPane2);

        jLabel3.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Información del instrumento");
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton1.setBackground(new java.awt.Color(153, 102, 0));
        jButton1.setText("Seleccionar instrumento");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Melodias de prueba"));

        jPanel2.setLayout(new java.awt.GridLayout(12, 0));

        jButton3.setText("Para Elisa | Beethoven ");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);

        jButton4.setText("Lilium | Elfen Lied");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);

        jButton9.setText("Tapión");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton9);

        jButton5.setText("El puente de Londres");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);

        jButton6.setText("Canción de Cuna | Lullaby");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6);

        jButton7.setText("La Mañana | Morning");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton7);

        jButton8.setText("Adornemos esta Navidad ");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton8);

        jButton10.setText("Piratas");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton10);

        jButton11.setText("Brilla Brilla estrellita");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton11);

        jButton12.setText("El chavo del 8");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton12);

        jButton13.setText("Mario Bross | Final ");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton13);

        jButton2.setText("Can - Can | Garlop Infernal");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        jScrollPane1.setViewportView(jPanel2);

        jPanel3.setLayout(new java.awt.GridLayout(4, 0));

        jSlider2.setMaximum(200);
        jSlider2.setValue(200);
        jSlider2.setBorder(javax.swing.BorderFactory.createTitledBorder("Volumen 100%"));
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });
        jPanel3.add(jSlider2);

        jSlider1.setMaximum(1500);
        jSlider1.setValue(0);
        jSlider1.setBorder(javax.swing.BorderFactory.createTitledBorder("Eco 0.0 s"));
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        jPanel3.add(jSlider1);

        jSpinner1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(2, 0, 10, 1));
        jSpinner1.setBorder(javax.swing.BorderFactory.createTitledBorder("Octava inicial"));
        jSpinner1.setFocusable(false);
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });
        jPanel3.add(jSpinner1);

        jSpinner2.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(6, 0, 10, 1));
        jSpinner2.setBorder(javax.swing.BorderFactory.createTitledBorder("Octava final"));
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner2StateChanged(evt);
            }
        });
        jPanel3.add(jSpinner2);

        jButton14.setBackground(new java.awt.Color(51, 0, 0));
        jButton14.setText("Detener");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jRadioButton1.setText("Dibujar ascii");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14)
                        .addGap(89, 89, 89)))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jRadioButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton14)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addGap(79, 79, 79))
        );

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseDragged
        ((Teclado) jLabel1).clicPresionado = true;
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jLabel1MouseDragged

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        ((Teclado) jLabel1).clicPresionado = true;
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jLabel1MousePressed

    private void jLabel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseReleased
        ((Teclado) jLabel1).clicPresionado = false;
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jLabel1MouseReleased

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        ((Teclado) jLabel1).ModificarRangoOctavas(
                (int) jSpinner1.getValue(),
                (int) jSpinner2.getValue()
        );
        ((Teclado) jLabel1).actualizar();
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner2StateChanged
        ((Teclado) jLabel1).ModificarRangoOctavas(
                (int) jSpinner1.getValue(),
                (int) jSpinner2.getValue()
        );
        ((Teclado) jLabel1).actualizar();
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jSpinner2StateChanged

    boolean MostrarListaInstrumentos = true;

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (MostrarListaInstrumentos) {
            jTabbedPane1.setSelectedIndex(1);
            jButton1.setText("Mostrar notas");
        } else {
            jTabbedPane1.setSelectedIndex(0);
            jButton1.setText("Seleccionar instrumento");
        }
        MostrarListaInstrumentos = !MostrarListaInstrumentos;
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jList2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList2ValueChanged
        jLabel2.setText(
                "<html>"
                + "<p align=\"center\">"
                + "Toca una tecla <br>y escucha el <br>elemento seleccionado</td>\n"
        );
        try {
            Instrument[] orchestra = MidiSystem.getSynthesizer().getAvailableInstruments();
            String renglón = orchestra[jList2.getSelectedIndex()].toString();
            jLabel3.setText(renglón);
        } catch (Exception e) {
        }
        reproductorNotas.CambiarInstrumento(ReproductorNotas.HerramientasMIDI.ObtenerLlave(jList2.getSelectedIndex()));
        if (isVisible()) {
            ((MostrarNotas) jLabel2).Mostrar();
        }
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jList2ValueChanged

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        float eco = (float) Matemática.Truncar(jSlider1.getValue() / 100f, 2);
        reproductorNotas.Eco = eco;
        jSlider1.setBorder(
                BorderFactory.createTitledBorder("Eco " + eco + " s")
        );
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jSlider1StateChanged

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged
        float volumen = (float) (float) jSlider2.getValue() / jSlider2.getMaximum();
        reproductorNotas.Volúmen = volumen;
        jSlider2.setBorder(
                BorderFactory.createTitledBorder(
                        "Volumen " + Matemática.Truncar(volumen * 100, 2) + "%"
                )
        );
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jSlider2StateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
//        DetenerHiloMelodiaPrueba();
//        HiloMelodiasPrueba = new Thread() {
//            @Override
//            public void run() {
//                reproductorNotas.Probar.CanCan_Garlop_Infernal();
//            }
//        };
//        HiloMelodiasPrueba.start();
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jButton2ActionPerformed

    ArrayList<InterpretadorMIDI> reproductores = new ArrayList<>();

    void EliminarReproductores() {
        new Thread() {
            @Override
            public void run() {
                while (!reproductores.isEmpty()) {
                    try {
                        for (InterpretadorMIDI reproductor : reproductores) {
                            if (reproductor.ReproducciónIniciada()) {
                                reproductor.CambiarInstrumento_TodosLosCanales(
                                        HerramientasMIDI.ObtenerLlave(
                                                jList2.getSelectedIndex()
                                        )
                                );
                                continue;
                            } else {
                                reproductores.remove(reproductor);
                                System.out.println("reproductor removido");
                                break;
                            }
                        }
                        sleep(500);
                    } catch (Exception ex) {
                    }
                }
                System.out.println("fin de la eliminación");
            }
        }.start();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            InterpretadorMIDI lectorMIDI = new InterpretadorMIDI();
            reproductores.add(lectorMIDI);
            lectorMIDI.CargarSecuenciaLong(Melodias.Simpsons());
            lectorMIDI.Reproducir(.5f);
            EliminarReproductores();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
//        DetenerHiloMelodiaPrueba();
        jLabel1.requestFocusInWindow();
        for (Tecla tecla : TeclasBlancas) {
            tecla.soltarMouse();
            tecla.soltarTeclado();
        }
        for (Tecla tecla : TeclasNegras) {
            tecla.soltarMouse();
            tecla.soltarTeclado();
        }
        for (int i = 0; i < Byte.MAX_VALUE; i++) {
            reproductorNotas.Suspender(i);
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
//        DetenerHiloMelodiaPrueba();
//        HiloMelodiasPrueba = new Thread() {
//            @Override
//            public void run() {
//                reproductorNotas.Probar.Lillium();
//            }
//        };
//        HiloMelodiasPrueba.start();
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
//        DetenerHiloMelodiaPrueba();
//        HiloMelodiasPrueba = new Thread() {
//            @Override
//            public void run() {
//                reproductorNotas.Probar.Tapión();
//            }
//        };
//        HiloMelodiasPrueba.start();
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
//        DetenerHiloMelodiaPrueba();
//        HiloMelodiasPrueba = new Thread() {
//            @Override
//            public void run() {
//                reproductorNotas.Probar.ElPuenteDeLondres();
//            }
//        };
//        HiloMelodiasPrueba.start();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
//        DetenerHiloMelodiaPrueba();
//        HiloMelodiasPrueba = new Thread() {
//            @Override
//            public void run() {
//                reproductorNotas.Probar.CanciónDeCuna_Lullaby();
//            }
//        };
//        HiloMelodiasPrueba.start();
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
//        DetenerHiloMelodiaPrueba();
//        HiloMelodiasPrueba = new Thread() {
//            @Override
//            public void run() {
//                reproductorNotas.Probar.LaMañana();
//            }
//        };
//        HiloMelodiasPrueba.start();
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
//        DetenerHiloMelodiaPrueba();
//        HiloMelodiasPrueba = new Thread() {
//            @Override
//            public void run() {
//                reproductorNotas.Probar.AdornemosEstaNavidad();
//            }
//        };
//        HiloMelodiasPrueba.start();
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
//        DetenerHiloMelodiaPrueba();
//        HiloMelodiasPrueba = new Thread() {
//            @Override
//            public void run() {
//                reproductorNotas.Probar.Piratas();
//            }
//        };
//        HiloMelodiasPrueba.start();
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
//        DetenerHiloMelodiaPrueba();
//        HiloMelodiasPrueba = new Thread() {
//            @Override
//            public void run() {
//                reproductorNotas.Probar.BrillaBrillaEstrellita();
//            }
//        };
//        HiloMelodiasPrueba.start();
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
//        DetenerHiloMelodiaPrueba();
//        HiloMelodiasPrueba = new Thread() {
//            @Override
//            public void run() {
//                reproductorNotas.Probar.ChavoDel8();
//            }
//        };
//        HiloMelodiasPrueba.start();
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
//        DetenerHiloMelodiaPrueba();
//        HiloMelodiasPrueba = new Thread() {
//            @Override
//            public void run() {
//                reproductorNotas.Probar.MarioBrossFinal();
//            }
//        };
//        HiloMelodiasPrueba.start();
        jLabel1.requestFocusInWindow();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jLabel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel1KeyPressed
        eventoTeclado(evt.getKeyCode(), true);
    }//GEN-LAST:event_jLabel1KeyPressed

    private void jLabel1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabel1KeyReleased
        eventoTeclado(evt.getKeyCode(), false);
    }//GEN-LAST:event_jLabel1KeyReleased

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        jLabel1.requestFocusInWindow();
        ((Teclado) jLabel1).TomarMedida();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    int[] AsciiTeclasBlancas = new int[]{
        KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_T, KeyEvent.VK_Y,
        KeyEvent.VK_U, KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_P, KeyEvent.VK_DEAD_ACUTE,
        KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C,
        KeyEvent.VK_V, KeyEvent.VK_B, KeyEvent.VK_N, KeyEvent.VK_M, KeyEvent.VK_COMMA,
        KeyEvent.VK_PERIOD, KeyEvent.VK_MINUS
    };
    int[] AsciiTeclasNegras = new int[]{
        KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_9,
        KeyEvent.VK_0, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_G,
        KeyEvent.VK_H, KeyEvent.VK_K, KeyEvent.VK_L
    };

    void eventoTeclado(int ascii, boolean presión) {
        ((Teclado) jLabel1).TomarMedida();
        System.out.println(ascii);
        for (int i = 0; i < AsciiTeclasBlancas.length && i < TeclasBlancas.length; i++) {
            if (AsciiTeclasBlancas[i] == ascii) {
                TeclasBlancas[i].PresionadaTeclado = presión;
                break;
            }
        }
        for (int i = 0; i < AsciiTeclasNegras.length && i < TeclasNegras.length; i++) {
            if (AsciiTeclasNegras[i] == ascii) {
                TeclasNegras[i].PresionadaTeclado = presión;
                break;
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}
