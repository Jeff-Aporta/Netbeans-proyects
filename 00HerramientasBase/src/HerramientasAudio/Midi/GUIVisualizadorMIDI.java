package HerramientasAudio.Midi;

import HerramientaArchivos.ArrastrarArchivos.ArrastrarArchivos;
import HerramientaArchivos.BibliotecaArchivos;
import HerramientaDeImagen.Filtros_Lineales;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientaDeImagen.ModoFusión.ModoFusión;
import static HerramientasAudio.Midi.BibliotecaSonidos.Efectos.SecuenciasLong.*;
import HerramientasAudio.Midi.InterpretadorMIDI.Canal;
import HerramientasAudio.Midi.InterpretadorMIDI.RenglónTransformado;
import HerramientasAudio.Midi.ReproductorNotas.HerramientasMIDI;
import HerramientasColor.ConversorModelosColor;
import HerramientasGUI.Maquillaje_SwingGUI;
import HerramientasGUI.NimbusModificado;
import HerramientasGUI.Personalizador_JList;
import HerramientasGUI.Presentador;
import HerramientasMatemáticas.Matemática;
import HerramientasSistema.BloqueDeCódigo;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.border.TitledBorder;
import HerramientasSistema.Cronómetro;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import static HerramientasAudio.Midi.InterpretadorMIDI.GenerarRenglones;
import static HerramientasAudio.Midi.ReproductorNotas.Constantes.*;
import HerramientasMatemáticas.Dupla;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUIVisualizadorMIDI extends javax.swing.JFrame {

    InterpretadorMIDI interpretadorMIDI;

    Color colorPrincipal = new Color(0x00D0FF);

    BufferedImage BotónIniciar = BibliotecaArchivos.Imagenes.Botones.Algorítmicos
            .BaseIniciar(colorPrincipal);

    BufferedImage BotónDetener = BibliotecaArchivos.Imagenes.Botones.Algorítmicos
            .BaseDetener(colorPrincipal);

    BufferedImage BotónPausa = BibliotecaArchivos.Imagenes.Botones.Algorítmicos
            .BasePausa(colorPrincipal);

    Color[] colorCanal = new Color[32];
    int CanalMostrar = 0;

    boolean Regulador_ExpandirHerramientas = false;
    long reguladorActualización = System.currentTimeMillis();

    public static void main(String args[]) {
        try {
            NimbusModificado.CargarTemaBlancoNimbus();
            NimbusModificado.CargarNimbus();
        } catch (Exception ex) {
        }
        new GUIVisualizadorMIDI();
    }

    public GUIVisualizadorMIDI() {//<editor-fold defaultstate="collapsed" desc="Código del constructor »">
        initComponents();

        jScrollPane1.getVerticalScrollBar().setUnitIncrement(30);

        //<editor-fold defaultstate="collapsed" desc="Aplicación de la transparencia en los componentes">
        Maquillaje_SwingGUI.Transparencia_JButtons(new JButton[]{jButton2, jButton3});

        Maquillaje_SwingGUI.Transparencia_JPanels(this);
        jPanel2.setOpaque(true);

        Maquillaje_SwingGUI.Transparencia_JScrollPanes(this);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Personalización de los JSliders triangulares">
        ((Maquillaje_SwingGUI.JSlider_ColaTriangular) jSlider1).colorSinProgreso = Color.GRAY;

        ((Maquillaje_SwingGUI.JSlider_ColaTriangular) jSlider3).colorSinProgreso = Color.GRAY;
        ((Maquillaje_SwingGUI.JSlider_ColaTriangular) jSlider4).colorSinProgreso = Color.GRAY;

        ((Maquillaje_SwingGUI.JSlider_ColaTriangular) jSlider3).colorProgreso = new Color(0x009FDD);
        ((Maquillaje_SwingGUI.JSlider_ColaTriangular) jSlider4).colorProgreso = new Color(0x009FDD);
        //</editor-fold>

        {
            //<editor-fold defaultstate="collapsed" desc="Personalización de Jlist de instrumentos">
            String[] instrumentos = new String[HerramientasMIDI.INSTRUMENTOS_SOPORTADOS];
            for (int i = 0; i < instrumentos.length; i++) {
                try {
                    instrumentos[i] = i + ") " + (String) ReproductorNotas.TablaInstrumentos_Obtener1(i)[0];
                } catch (Exception ex) {
                }
            }
            Maquillaje_SwingGUI.AsignarLista_JList(jList1, instrumentos);

            jList1.setCellRenderer(new Personalizador_JList() {
                {
                    Selección_ColorFondo = new Color(0x6EE4FF);
                    Selección_ColorFuente = Color.BLACK;
                    NoSelección_ColorFondo = new Color(63, 75, 87);
                    NoSelección_ColorFuente = Color.GRAY;
                }
            });
            //</editor-fold>
        }

        //<editor-fold defaultstate="collapsed" desc="Personalización del interpretador MIDI">
        try {
            interpretadorMIDI = new InterpretadorMIDI();

            interpretadorMIDI.EventoReproducciónTérminada = () -> {
                //<editor-fold defaultstate="collapsed" desc="Implementación de evento »">
                Maquillaje_SwingGUI.AñadirImgBotón_Estilo2(
                        jButton2, BotónIniciar
                );
                jLabel2.setText("");
                jProgressBar1.setValue(0);
                //</editor-fold>
            };

            long regulador[] = new long[1];
            interpretadorMIDI.EventoNota = (canal, ms_activación, ActivarNota, nota, volumen) -> {
                //<editor-fold defaultstate="collapsed" desc="Implementación de evento »">
                if (CanalMostrar == canal) {
                    if (ActivarNota) {
                        jLabel2.setText(HerramientasMIDI.ConvertirNotaString(nota));
                        jProgressBar1.setValue((int) (100 * volumen / 128f));
                        regulador[0] = System.currentTimeMillis();
                    } else {
                        if (System.currentTimeMillis() - regulador[0] < 250) {
                            return;
                        }
                        jLabel2.setText("");
                        jProgressBar1.setValue(0);
                    }
                }
                //</editor-fold>
            };

            BloqueDeCódigo.Simple NuevaMelodia = () -> {
                //<editor-fold defaultstate="collapsed" desc="Implementación de evento »">
                reguladorActualización = System.currentTimeMillis();
                CargarColoresCaneles();
                interpretadorMIDI.Detener();
                jLabel2.setText("");
                jProgressBar1.setValue(0);
                GenerarBotonesCanales();
                ActualizarInformaciónCanales();
                jScrollPane1.repaint();
                Maquillaje_SwingGUI.AñadirImgBotón_Estilo2(
                        jButton2, BotónIniciar
                );
                //</editor-fold>
            };

            interpretadorMIDI.EventoCargaArchivo = (s) -> {
                //<editor-fold defaultstate="collapsed" desc="Implementación de evento »">
                jTextField1.setText(s[0]);
                NuevaMelodia.Ejecutar();
                ((MostrarNotas) jLabel1).CargarArchivo(s[0]);
                GeneradorMelodia = s[0];
                //</editor-fold>
            };

            interpretadorMIDI.EventoCargaSecuenciaLong = (l) -> {
                //<editor-fold defaultstate="collapsed" desc="Implementación de evento »">
                jTextField1.setText("Secuencia long");
                NuevaMelodia.Ejecutar();
                ((MostrarNotas) jLabel1).CargarSecuenciaLong(l);
                GeneradorMelodia = l;
                //</editor-fold>
            };

            interpretadorMIDI.CargarSecuenciaLong(BibliotecaSonidos.Melodias.MoonlightSonata());

        } catch (Exception ex) {
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Carga de las imagenes iniciales en los botones">
        Maquillaje_SwingGUI.AñadirImgBotón_Estilo2(jButton2,
                BotónIniciar
        );
        Maquillaje_SwingGUI.AñadirImgBotón_Estilo2(
                jButton3, BotónDetener
        );
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Escuchador de archivos arrastrados">
        ArrastrarArchivos.ColorBorde = new Color(0, true);
        ArrastrarArchivos.Añadir((s) -> {
            new Thread() {
                @Override
                public void run() {
                    jLabel1.setText("Leyendo archivo");
                    CargarArchivo(s[0]);
                    jLabel1.setText("");
                    reguladorActualización = System.currentTimeMillis();
                }
            }.start();
        },
                jLabel1
        );
        //</editor-fold>

        jLabel1.setText("");

        new Thread() {//<editor-fold defaultstate="collapsed" desc="Actualizador de fotogramas">
            @Override
            public void run() {
                while (true) {
                    Actualizar();
                    try {
                        sleep(1000 / 40);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }.start();//</editor-fold>

        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }//</editor-fold>

    Object GeneradorMelodia;

    void ImprimirSecuenciaLong() {
        String g = "";
        if (GeneradorMelodia instanceof String) {
            g = InterpretadorMIDI.SecuenciaLong.GenerarSecuencia(
                    (String) GeneradorMelodia
            );
        } else {
            System.out.println("Error con " + GeneradorMelodia);
        }
        System.out.println(g);
    }

    void CargarSecuenciaLong(long[] l) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        interpretadorMIDI.CargarSecuenciaLong(l);
    }//</editor-fold>

    void CargarArchivo(String s) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            interpretadorMIDI.CargarArchivo(s);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "El archivo no se puede leer\n" + s,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//</editor-fold>

    void CargarColoresCaneles() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (int i = 0; i < colorCanal.length; i++) {
            colorCanal[i] = ConversorModelosColor.Aleatorio();
        }
    }//</editor-fold>

    void CambiarCanalMostrar(int c) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Canal canal = interpretadorMIDI.Canales.get(c);
        jSlider1.setValue((int) (canal.volúmen * 100));
        jList1.setSelectedIndex(HerramientasMIDI.ObtenerIndice(canal.banco, canal.programa));
    }//</editor-fold>

    void ActualizarInformaciónCanales() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        String info = "<html>"
                + "<body style=\"background-color: #ffffff;\">";
        for (int canal = 0; canal < interpretadorMIDI.Canales.size(); canal++) {
            int llave = HerramientasMIDI.GenerarLlaveInstrumento(
                    interpretadorMIDI.Canales.get(canal).banco,
                    interpretadorMIDI.Canales.get(canal).programa
            );
            info += "<span style=\"color: #" + ConversorModelosColor.RGBtoHex(colorCanal[canal]) + ";\">"
                    + "Canal " + canal + ": "
                    + HerramientasMIDI.ObtenerIndice(llave) + ") "
                    + HerramientasMIDI.ConvertirInstrumentoAString(llave)
                    + "</span>";
            if (canal < interpretadorMIDI.Canales.size()) {
                info += "<br>";
            }
        }
        info += "</body>";
        info += "</html>";
        jLabel4.setText(info);
    }//</editor-fold>

    void Actualizar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">

        Dupla p = Dupla.PosiciónCursorEnComponente(this);
        jMenuBar1.setVisible(p.YestáEntre(0, 60) && p.XestáEntre(0, 200));

        if (!interpretadorMIDI.enPausa()) {
            reguladorActualización = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - reguladorActualización < 2000) {
            if (actualizarBarraTiempo) {
                jSlider2.setValue((int) (jSlider2.getMaximum() * interpretadorMIDI.porcentajeRecorrido()));
            }
            ((TitledBorder) jSlider2.getBorder()).setTitle(
                    Cronómetro.convertir_HHMMSS_3_9_decimales(
                            (long) interpretadorMIDI.ObtenerTiempoReproducción(),
                            Cronómetro.UNIDAD_MEDIDA_MILISEGUNDOS
                    )
                    + " | "
                    + Cronómetro.convertir_HHMMSS_3_9_decimales(
                            interpretadorMIDI.Duración_ms,
                            Cronómetro.UNIDAD_MEDIDA_MILISEGUNDOS
                    )
                    + " | "
                    + ((MostrarNotas) jLabel1).notas.size() + " notas"
            );
            jSlider2.repaint();
            ((MostrarNotas) jLabel1).actualizar();
        }
    }//</editor-fold>

    void GenerarBotonesCanales() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (interpretadorMIDI == null) {
            return;
        }
        jPanel1.removeAll();
        Enumeration<AbstractButton> e = buttonGroup1.getElements();
        while (e.hasMoreElements()) {
            buttonGroup1.remove(e.nextElement());
        }
        int s = interpretadorMIDI.Canales.size();
        jPanel1.setLayout(new GridLayout(s < 5 ? 5 : s, 0));
        for (int i = 0; i < interpretadorMIDI.Canales.size(); i++) {
            final int c = i;
            jPanel1.add(new Maquillaje_SwingGUI.JRadioButtonColorSolido("Canal " + i) {
                {
                    Selección_Fondo = ConversorModelosColor.Aleatorio();
                    NoSelección_Fondo = Color.BLACK;
                    NoSelección_Fuente = Color.WHITE;
                    Selección_Fondo = new Color(0x009FDD);
                    Selección_Fuente = Color.WHITE;
                    buttonGroup1.add(this);
                    setFont(getFont().deriveFont(18f));
                    addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ae) {
                            CanalMostrar = c;
                            CambiarCanalMostrar(c);
                            ((Maquillaje_SwingGUI.JSlider_ColaTriangular) jSlider1).colorProgreso = Selección_Fondo;
                            jSlider1.repaint();
                        }
                    });
                }
            });
        }
        ((JRadioButton) jPanel1.getComponent(0)).setSelected(true);
        ((JRadioButton) jPanel1.getComponent(0)).getActionListeners()[0].actionPerformed(null);
    }//</editor-fold>

    class MostrarNotas extends Presentador {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

        final ArrayList<graficoNota> notas = new ArrayList<>();
        final double cantidadMáxNotas = Byte.MAX_VALUE;

        final int anchura = 1400;
        double altura;

        int notamin;
        int notamáx;

        {
            TIPO_AJUSTE_FOTOGRAMA = Filtros_Lineales.AJUSTE_EXPANDIR;
        }

        public void CargarSecuenciaLong(long[] l) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            notas.clear();

            ArrayList<RenglónTransformado> rt = InterpretadorMIDI.GenerarRenglonesTransformados(l);

            generarShapesDeLasNotas(rt);
        }//</editor-fold>

        public void CargarArchivo(String s) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            try {
                ArrayList<RenglónTransformado> rt = interpretadorMIDI.GenerarRenglonesTransformados(GenerarRenglones(s));
                System.out.println("Notas: " + rt.size() + " - " + s);
                generarShapesDeLasNotas(rt);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }//</editor-fold>

        void generarShapesDeLasNotas(ArrayList<RenglónTransformado> rt) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            notas.clear();

            altura = interpretadorMIDI.Duración_ms * .2;

            for (RenglónTransformado r : rt) {
                //<editor-fold defaultstate="collapsed" desc="Generación de las formas de las notas">
                if (r.esInstrumento || r.esDuración) {
                    continue;
                }
                double duraciónMelodia_ms = interpretadorMIDI.Duración_ms;
                double posY = altura * r.milisegundoEvento / duraciónMelodia_ms;
                double LargorNota = altura * r.sostener / duraciónMelodia_ms;
                notas.add(new graficoNota(new RoundRectangle2D.Double(
                        r.nota * anchura / cantidadMáxNotas,
                        -(posY + LargorNota),
                        anchura / cantidadMáxNotas,
                        LargorNota,
                        20,
                        20
                ),
                        r.canal
                ));
                //</editor-fold>
            }

            notamin = Integer.MAX_VALUE;
            notamáx = Integer.MIN_VALUE;

            for (int i = 0; i < rt.size(); i++) {
                //<editor-fold defaultstate="collapsed" desc="Cálculo de las notas mínima y máxima »">
                if (rt.get(i).esInstrumento || rt.get(i).esDuración) {
                    continue;
                }
                notamin = Integer.min(notamin, rt.get(i).nota);
                notamáx = Integer.max(notamáx, rt.get(i).nota);
                //</editor-fold>
            }

            //<editor-fold defaultstate="collapsed" desc="Generación de las magenes de las notas con el borde del fotograma">
            notamin -= 2;
            notamáx += 2;
            //</editor-fold>

            System.out.println();
            System.out.println("min " + notamin);
            System.out.println("max " + notamáx);
        }//</editor-fold>

        public void actualizar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            ActualizarFotograma(fotograma());
        }//</editor-fold>

        BufferedImage fotograma() {//<editor-fold defaultstate="collapsed" desc="Generación del fotograma »">
            try {
                BufferedImage retorno = new BufferedImage(anchura, 600, 2);
                Graphics2D g = retorno.createGraphics();

                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (jRadioButton1.isSelected()) {
                    //<editor-fold defaultstate="collapsed" desc="Dibujado del borde del fotograma">
                    g.setColor(Color.BLACK);
                    g.drawRect(0, 0, retorno.getWidth() - 1, retorno.getHeight() - 1);
                    //</editor-fold>
                }

                if (jRadioButton2.isSelected()) {
                    //<editor-fold defaultstate="collapsed" desc="Dibujado de los señaladores númericos inferiores">
                    int h = 20;
                    boolean NotasReducidas = !jRadioButton4.isSelected();
                    int rango = NotasReducidas ? (notamáx - notamin) : Byte.MAX_VALUE;
                    double ancho = 1d * anchura / rango;
                    int w = (int) Math.round(ancho);
                    Color color = new Color(0x007B99);

                    for (int i = 0; i < rango; i++) {
                        String texto = (NotasReducidas ? (notamin + i) : i) + "";
                        int PosX = (int) (i * ancho);
                        BufferedImage nota = new BufferedImage(w, h, 2);
                        Graphics2D gt = nota.createGraphics();
                        gt.setStroke(new BasicStroke(2));
                        gt.setColor(color);
                        gt.drawRect(0, 0, w, h);
                        new GeneradorDeTexto()
                                .ModificarTamañoFuente(12)
                                .ColorFuente(color)
                                .DibujarTextoCentradoEnImagen(nota, texto);
                        g.drawImage(nota, PosX, retorno.getHeight() - h - 2, null);
                    }
                    //</editor-fold>
                }

                g.setTransform(new AffineTransform() {
                    {
                        //<editor-fold defaultstate="collapsed" desc="Ajuste de la transformación">
                        double Vi = retorno.getHeight();
                        double Vf = altura + retorno.getHeight();
                        double t = interpretadorMIDI.porcentajeRecorrido();
                        if (!jRadioButton4.isSelected()) {
                            scale(anchura / ((notamáx - notamin) * (anchura / cantidadMáxNotas)), 1);
                            translate(-notamin * (anchura / cantidadMáxNotas), (Vf - Vi) * t + Vi - 22);
                        } else {
                            translate(0, (Vf - Vi) * t + Vi - 22);
                        }
                        //</editor-fold>
                    }
                });

                for (graficoNota nota : notas) {
                    //<editor-fold defaultstate="collapsed" desc="Dibujado de las notas »">
                    g.setComposite(new ModoFusión.Porcentual.LuzSuave()); //Para cuando una nota se dibuje encima de otra
                    if (jRadioButton3.isSelected()) {
                        g.setColor(colorCanal[nota.canal]);
                    } else {
                        g.setColor(colorPrincipal);
                    }
                    g.fill(nota.forma);
                    g.setComposite(AlphaComposite.SrcOver);//Para volver al modo de pintura normal
                    g.setColor(Color.BLACK);//Color del borde de las notas
                    g.draw(nota.forma);
                    //</editor-fold>
                }

                return retorno;
            } catch (Exception e) {
                return null;
            }
        }//</editor-fold>

        class graficoNota {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase »">

            RoundRectangle2D forma;
            int canal;

            public graficoNota(RoundRectangle2D forma, int canal) {
                this.forma = forma;
                this.canal = canal;
            }
        }//</editor-fold>

    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Código generado con ayuda de Netbeans »">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new MostrarNotas();
        jPanel2 = new javax.swing.JPanel();
        jSlider1 = new Maquillaje_SwingGUI.JSlider_ColaTriangular();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane3 = new javax.swing.JScrollPane();
        jLabel4 = new javax.swing.JLabel();
        jSlider3 = new Maquillaje_SwingGUI.JSlider_ColaTriangular();
        jSlider4 = new Maquillaje_SwingGUI.JSlider_ColaTriangular();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSlider2 = new Maquillaje_SwingGUI.JSlider_ColaLineal();
        jButton4 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenuItem37 = new javax.swing.JMenuItem();
        jMenuItem38 = new javax.swing.JMenuItem();
        jMenuItem39 = new javax.swing.JMenuItem();
        jMenuItem40 = new javax.swing.JMenuItem();
        jMenuItem41 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Visualizador");

        jPanel2.setBackground(new java.awt.Color(63, 75, 87));
        jPanel2.setBorder(null);

        jSlider1.setMaximum(200);
        jSlider1.setValue(100);
        jSlider1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Volumen canal 100%", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        jSlider1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jSlider1MouseClicked(evt);
            }
        });

        jScrollPane2.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jList1.setBorder(null);
        jList1.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        jScrollPane1.setBorder(null);
        jScrollPane1.setViewportBorder(null);

        jPanel1.setBackground(new java.awt.Color(63, 63, 63));
        jPanel1.setBorder(null);
        jPanel1.setLayout(new java.awt.GridLayout(32, 0));
        jScrollPane1.setViewportView(jPanel1);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("3F#");

        jProgressBar1.setMaximum(101);
        jProgressBar1.setStringPainted(true);

        jScrollPane3.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

        jLabel4.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Información");
        jLabel4.setBorder(null);
        jScrollPane3.setViewportView(jLabel4);

        jSlider3.setMaximum(200);
        jSlider3.setValue(100);
        jSlider3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Volumen 100%", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jSlider3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider3StateChanged(evt);
            }
        });
        jSlider3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jSlider3MouseClicked(evt);
            }
        });

        jSlider4.setMaximum(600);
        jSlider4.setValue(100);
        jSlider4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Velocidad x1.0", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jSlider4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider4StateChanged(evt);
            }
        });
        jSlider4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jSlider4MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jSlider4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jSlider4MouseReleased(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 62, 76));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Cargar archivo");
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(63, 75, 87));
        jTextField1.setForeground(new java.awt.Color(255, 255, 255));
        jTextField1.setText("jTextField1");
        jTextField1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jTextField1.setRequestFocusEnabled(false);

        jButton5.setBackground(new java.awt.Color(0, 62, 76));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Colores");
        jButton5.setFocusPainted(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jRadioButton1.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Borde");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Indicadores de notas");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jRadioButton3.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton3.setSelected(true);
        jRadioButton3.setText("Colores de canales");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        jRadioButton4.setForeground(new java.awt.Color(255, 255, 255));
        jRadioButton4.setText("Mostrar todas las notas");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(0, 62, 76));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Secuencia");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSlider4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jSlider3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRadioButton3)
                                    .addComponent(jRadioButton4)
                                    .addComponent(jButton6))
                                .addGap(0, 5, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jRadioButton1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSlider4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jRadioButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(3, 3, 3))
        );

        jPanel4.setLayout(new java.awt.GridLayout(1, 0, 5, 5));

        jButton2.setText("R");
        jButton2.setFocusPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton2);

        jButton3.setText("D");
        jButton3.setFocusPainted(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton3);

        jSlider2.setMaximum(200);
        jSlider2.setValue(100);
        jSlider2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1), "00:00:00 | 00:00:00", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(0, 208, 255))); // NOI18N
        jSlider2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jSlider2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jSlider2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton4.setBackground(new java.awt.Color(0, 62, 76));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("▼");
        jButton4.setBorder(null);
        jButton4.setFocusPainted(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jMenu1.setText("<html> <strong>♪</strong> Melodias </html>");

        jMenuItem1.setText("Los Simpsons");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Carol of the bells");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Para elisa | Beethoven");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Harry Potter");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setText("Davy Jones | Piratas del caribe");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem6.setText("Tapión | Dragon ball Z");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem7.setText("Lillium | Elfen lied");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem8.setText("Great fairy fountain | Zelda");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuItem9.setText("Moonlight sonata | Beethoven");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem9);

        jMenuItem10.setText("Nocturne | Chopin");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem10);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("<html> <strong>♪</strong> Efectos </html>");

        jMenu3.setText("Megaman");

        jMenuItem11.setText("Enemy Hit");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem11);

        jMenuItem13.setText("Hyper Bomb");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem13);

        jMenuItem14.setText("Ice Slasher");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem14);

        jMenuItem15.setText("Landing");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem15);

        jMenuItem16.setText("Shooting Lemons");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem16);

        jMenuItem17.setText("Magnet Beam");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem17);

        jMenuItem18.setText("One Up");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem18);

        jMenuItem19.setText("Pause");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem19);

        jMenuItem20.setText("Rolling Cutter");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem20);

        jMenuItem21.setText("Select");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem21);

        jMenuItem22.setText("Spark Shock");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem22);

        jMenuItem23.setText("Super Arm");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem23);

        jMenuItem24.setText("Thunder Beam");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem24);

        jMenu2.add(jMenu3);

        jMenu4.setText("Castlevania");

        jMenuItem12.setText("Enemy Cry");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem12);

        jMenuItem25.setText("Enemy Kill");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem25);

        jMenuItem26.setText("Gold Knife");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem26);

        jMenuItem27.setText("Heart");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem27);

        jMenuItem28.setText("Holy Water");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem28);

        jMenu2.add(jMenu4);

        jMenu5.setText("Sonic");

        jMenuItem29.setText("Skid");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem29);

        jMenuItem30.setText("Ring Pickup");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem30);

        jMenuItem31.setText("Ring Spill");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem31);

        jMenu2.add(jMenu5);

        jMenu6.setText("Pacman");

        jMenuItem32.setText("Eating Dots");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem32);

        jMenuItem33.setText("Start");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem33);

        jMenuItem34.setText("Death Bloop");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem34);

        jMenu2.add(jMenu6);

        jMenu7.setText("Zelda");

        jMenuItem35.setText("Treasure 1");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem35);

        jMenuItem36.setText("Treasure 2");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem36);

        jMenuItem37.setText("Puzzle Solved");
        jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem37ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem37);

        jMenuItem38.setText("Hit Enemy");
        jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem38ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem38);

        jMenuItem39.setText("Kill Enemy");
        jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem39ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem39);

        jMenuItem40.setText("Block");
        jMenuItem40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem40ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem40);

        jMenuItem41.setText("Death");
        jMenuItem41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem41ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem41);

        jMenu2.add(jMenu7);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (interpretadorMIDI.enPausa()) {
            if (interpretadorMIDI.tieneCargadoUnArchivo()) {
                interpretadorMIDI.Reproducir();
                Maquillaje_SwingGUI.AñadirImgBotón_Estilo2(
                        jButton2, BotónPausa
                );
            }
        } else {
            interpretadorMIDI.Pausar();
            Maquillaje_SwingGUI.AñadirImgBotón_Estilo2(
                    jButton2, BotónIniciar
            );
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        interpretadorMIDI.Detener();
        Maquillaje_SwingGUI.AñadirImgBotón_Estilo2(
                jButton2, BotónIniciar
        );
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jSlider3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider3StateChanged
        interpretadorMIDI.Volúmen = (float) jSlider3.getValue() / 100;
        float porcentaje = jSlider3.getValue() / 100f;
        ((TitledBorder) jSlider3.getBorder()).setTitle("Volumen " + (int) (porcentaje * 100) + "%");
    }//GEN-LAST:event_jSlider3StateChanged

    private void jSlider4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider4StateChanged
        float valor = jSlider4.getValue() / 100f;
        ((TitledBorder) jSlider4.getBorder()).setTitle("Velocidad x" + Matemática.Truncar(valor, 2));
        interpretadorMIDI.velocidadReproducción(valor);
    }//GEN-LAST:event_jSlider4StateChanged

    boolean actualizarBarraTiempo = true;

    private void jSlider4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider4MousePressed
        actualizarBarraTiempo = false;
    }//GEN-LAST:event_jSlider4MousePressed

    private void jSlider4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider4MouseReleased
        actualizarBarraTiempo = true;
    }//GEN-LAST:event_jSlider4MouseReleased

    private void jSlider2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider2MousePressed
        actualizarBarraTiempo = false;
    }//GEN-LAST:event_jSlider2MousePressed

    private void jSlider2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider2MouseReleased
        float valor = (float) jSlider2.getValue() / jSlider2.getMaximum();
        interpretadorMIDI.CambiarMomentoReproducción((int) (valor * interpretadorMIDI.Duración_ms));
        reguladorActualización = System.currentTimeMillis();
        actualizarBarraTiempo = true;
    }//GEN-LAST:event_jSlider2MouseReleased

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        float porcentaje = jSlider1.getValue() / 100f;
        interpretadorMIDI.Canales.get(CanalMostrar).volúmen = porcentaje;
        ((TitledBorder) jSlider1.getBorder()).setTitle("Volumen canal " + (int) (porcentaje * 100) + "%");
    }//GEN-LAST:event_jSlider1StateChanged

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
        interpretadorMIDI.Canales.get(CanalMostrar).CambiarInstrumento(jList1.getSelectedIndex());
        ActualizarInformaciónCanales();
    }//GEN-LAST:event_jList1ValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String s = JOptionPane.showInputDialog(this, "Ingrese una dirección", "ruta", JOptionPane.QUESTION_MESSAGE);
        if (s != null) {
            CargarArchivo(s);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jSlider4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider4MouseClicked
        if (evt.getClickCount() == 2) {
            jSlider4.setValue(100);
            jSlider4StateChanged(null);
        }
    }//GEN-LAST:event_jSlider4MouseClicked

    private void jSlider3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider3MouseClicked
        if (evt.getClickCount() == 2) {
            jSlider3.setValue(100);
            jSlider3StateChanged(null);
        }
    }//GEN-LAST:event_jSlider3MouseClicked

    private void jSlider1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseClicked
        if (evt.getClickCount() == 2) {
            jSlider1.setValue(100);
            jSlider1StateChanged(null);
        }
    }//GEN-LAST:event_jSlider1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (Regulador_ExpandirHerramientas) {
            jPanel2.setVisible(true);
            jButton4.setText("▼");
        } else {
            jPanel2.setVisible(false);
            jButton4.setText("▲");
        }
        Regulador_ExpandirHerramientas = !Regulador_ExpandirHerramientas;
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        reguladorActualización = System.currentTimeMillis();
        CargarColoresCaneles();
        ActualizarInformaciónCanales();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        ((MostrarNotas) jLabel1).actualizar();
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        ((MostrarNotas) jLabel1).actualizar();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        ((MostrarNotas) jLabel1).actualizar();
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        ((MostrarNotas) jLabel1).actualizar();
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        ImprimirSecuenciaLong();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        interpretadorMIDI.CargarSecuenciaLong(BibliotecaSonidos.Melodias.Simpsons());
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        interpretadorMIDI.CargarSecuenciaLong(BibliotecaSonidos.Melodias.HarryPotter());
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        interpretadorMIDI.CargarSecuenciaLong(BibliotecaSonidos.Melodias.CarolOfTheBells());
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        interpretadorMIDI.CargarSecuenciaLong(BibliotecaSonidos.Melodias.ParaElisa());
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        interpretadorMIDI.CargarSecuenciaLong(BibliotecaSonidos.Melodias.DavyJones());
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        interpretadorMIDI.CargarSecuenciaLong(BibliotecaSonidos.Melodias.DragonBallZ_Tapion_s_theme());
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        interpretadorMIDI.CargarSecuenciaLong(BibliotecaSonidos.Melodias.ElfenLied_Lilium());
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        interpretadorMIDI.CargarSecuenciaLong(BibliotecaSonidos.Melodias.Zelda_GreatFairyFountain());
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        interpretadorMIDI.CargarSecuenciaLong(BibliotecaSonidos.Melodias.MoonlightSonata());
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        interpretadorMIDI.CargarSecuenciaLong(BibliotecaSonidos.Melodias.Nocturne());
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    void AsignarEfecto(long[] Secuencia, float Velocidad, int Instrumento) {
        interpretadorMIDI.CargarSecuenciaLong(Secuencia);
        jSlider4.setValue((int) (100 * Velocidad));
        interpretadorMIDI.CambiarInstrumento_TodosLosCanales(Instrumento);
        ActualizarInformaciónCanales();
    }

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        AsignarEfecto(Megaman.HitEnemy(), 3.6f, GUN_SHOT);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        AsignarEfecto(Megaman.HyperBomb(), 4.5f, SQUARE);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        AsignarEfecto(Megaman.IceSlasher(), 4.5f, SAW);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        AsignarEfecto(Megaman.Landing(), 4.5f, SQUARE);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        AsignarEfecto(Megaman.ShootingLemons(), 4f, SAW);
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        AsignarEfecto(Megaman.MagnetBeam(), 3.7f, SYNTH_BASS_3);
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        AsignarEfecto(Megaman.OneUp(), 1.5f, SOLO_VOX);
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        AsignarEfecto(Megaman.Pause(), 2.4f, SQUARE_WAVE);
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        AsignarEfecto(Megaman.RollingCutter(), 4.5f, GUN_SHOT);
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        AsignarEfecto(Megaman.Select(), 3.6f, SAW);
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        AsignarEfecto(Megaman.SparkShock(), 2.4f, SAW);
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        AsignarEfecto(Megaman.SuperArm(), 4f, SQUARE);
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        AsignarEfecto(Megaman.ThunderBeam(), 4.5f, SAW);
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        AsignarEfecto(Castlevania.EnemyCry(), 3f, SAW);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        AsignarEfecto(Castlevania.EnemyKill(), 2.25f, MACHINE_GUN);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        AsignarEfecto(Castlevania.GoldKnife(), 3.65f, FEEDBACK_GT);
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        AsignarEfecto(Castlevania.Heart(), 3.65f, SINE_WAVE);
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        AsignarEfecto(Castlevania.HolyWater(), 3f, SQUARE);
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        AsignarEfecto(Sonic.Skid(), 3.04f, SYNTH_BASS_3);
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        AsignarEfecto(Sonic.RingPickup(), 2.04f, ATMOSPHERE);
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        AsignarEfecto(Sonic.RingSpill(), 3.63f, MUSIC_BOX);
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        AsignarEfecto(Pacman.EatingDots(), 2f, SYNTH_BASS_1);
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        AsignarEfecto(Pacman.Start(), 2.25f, SYNTH_BASS_1);
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        AsignarEfecto(Pacman.DeathBloop(), 1.5f, OBOE);
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        AsignarEfecto(Zelda.Treasure1(), 1.5f, SQUARE);
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        AsignarEfecto(Zelda.Treasure2(), 1.5f, SQUARE);
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed
        AsignarEfecto(Zelda.PuzzleSolved(), .75f, SQUARE);
    }//GEN-LAST:event_jMenuItem37ActionPerformed

    private void jMenuItem38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem38ActionPerformed
        AsignarEfecto(Zelda.HitEnemy(), 2.3f, SQUARE);
    }//GEN-LAST:event_jMenuItem38ActionPerformed

    private void jMenuItem39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem39ActionPerformed
        AsignarEfecto(Zelda.KillEnemy(), 2.3f, SQUARE);
    }//GEN-LAST:event_jMenuItem39ActionPerformed

    private void jMenuItem40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem40ActionPerformed
        AsignarEfecto(Zelda.Block(), 4.5f, SQUARE);
    }//GEN-LAST:event_jMenuItem40ActionPerformed

    private void jMenuItem41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem41ActionPerformed
        AsignarEfecto(Zelda.Death(), .9f, SQUARE);
    }//GEN-LAST:event_jMenuItem41ActionPerformed

    //<editor-fold defaultstate="collapsed" desc="Declaración de las variables de la GUI »">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem38;
    private javax.swing.JMenuItem jMenuItem39;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem41;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JSlider jSlider4;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
    //</editor-fold>
}
