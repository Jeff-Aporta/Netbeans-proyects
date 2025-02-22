package _Laboratorio;

import HerramientaArchivos.BibliotecaArchivos;
import HerramientaArchivos.LectoEscrituraArchivos;
import static HerramientaDeImagen.Filtros_Lineales.*;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientaDeImagen.ModoFusión.ModoFusión;
import HerramientasMatemáticas.Dupla;
import static HerramientasMatemáticas.Matemática.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class SamplePuzzle extends javax.swing.JFrame {

    static byte[][] PartidaInicial = {
        {3, 1, 1, 0, 2, 1, 2, 0, 1, 0, 2, 1, 3, 1, 2, 1},//M
        {1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0},//A
        {1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0},//B
        {0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0}//C
    };
    final static ArrayList<Byte> //
            M = new ArrayList(),
            A = new ArrayList(),
            B = new ArrayList(),
            C = new ArrayList(),
            R = new ArrayList();

    final static ArrayList<ArrayList<Byte>> Bandas = new ArrayList() {
        {
            add(M);
            add(A);
            add(B);
            add(C);
        }
    };
    static double Columnas, Filas;

    Controlador ControlM;
    Controlador ControlA;
    Controlador ControlB;
    Controlador ControlC;
    Controlador ControlR;

    final static int LIBERADO = 0, SOBRE = 1, PRESS = 2;
    BufferedImage Correcto, Incorrecto, Apagado;
    BufferedImage BtnDerecha[] = new BufferedImage[3];
    BufferedImage BtnIzquierda[] = new BufferedImage[3];
    BufferedImage Resultado[] = new BufferedImage[3];

    ArrayList<Controlador> Controladores;
    ArrayList<JLabel> BotonesIzquierda;
    ArrayList<JLabel> BotonesDerecha;

    void GenerarPartidaAleatoria() {
        int nB = AleatorioEnteroEntre(16, 22);
        int aB = AleatorioEnteroEntre(3, 6);
        byte[] M = new byte[nB];
        byte[] A = new byte[nB];
        byte[] B = new byte[nB];
        byte[] C = new byte[nB];
        for (int i = 0; i < nB; i++) {
            A[i] = (byte) AleatorioEnteroEntre(0, aB - 2);
            B[i] = (byte) AleatorioEnteroEntre(0, aB - 2);
            C[i] = (byte) AleatorioEnteroEntre(0, aB - 2);
            byte S = (byte) (A[i] + B[i] + C[i]);
            if (S > aB) {
                i--;
            } else {
                M[i] = S;
            }
        }
        byte[][] retorno = {M, A, B, C};
        int a;
        for (int i = 1; i < retorno.length; i++) {
            for (int j = 0; j < retorno[i].length; j++) {
                System.out.print(retorno[i][j] + ",");
            }
            System.out.println("");
        }
        System.out.println("");

        for (int i = 1; i < retorno.length; i++) {
            a = AleatorioEnteroEntre(nB / 2, nB);
            if (AleatorioEnteroEntre(0, 1) == 1) {
                for (int c = 0; c < a; c++) {
                    Controlador.DesplazarArregloDerecha(retorno[i]);
                }
            } else {
                for (int c = 0; c < a; c++) {
                    Controlador.DesplazarArregloIzquierda(retorno[i]);
                }
            }
        }
        CargarPartida(retorno);
    }

    void CargarPartida(byte[]... mapa) {
        for (int Banda = 0; Banda < mapa.length; Banda++) {
            Bandas.get(Banda).clear();
            for (int Muestra = 0; Muestra < mapa[Banda].length; Muestra++) {
                Bandas.get(Banda).add(mapa[Banda][Muestra]);
            }
        }
        R.clear();
        for (int i = 0; i < M.size(); i++) {
            R.add((byte) 0);
        }
        Columnas = M.size();
        Filas = Alto();
        Controlador.ActualizarFondo();
        for (Controlador Controlador : Controladores) {
            Controlador.RefrescarDibujo();
        }
        Correct_lbl.setIcon(new ImageIcon(Apagado));
        Incorrect_lbl.setIcon(new ImageIcon(Apagado));
    }

    public SamplePuzzle() {
        initComponents();
        setSize(900, 630);
        setLocationRelativeTo(null);
        BotonesDerecha = new ArrayList() {
            {
                add(jLabel15);
                add(jLabel12);
                add(jLabel17);
            }
        };
        this.BotonesIzquierda = new ArrayList() {
            {
                add(jLabel14);
                add(jLabel13);
                add(jLabel16);
            }
        };
        ControlM = new Controlador(M, jLabel4, new Color(243, 242, 178));
        ControlA = new Controlador(A, jLabel5, new Color(255, 141, 133));
        ControlB = new Controlador(B, jLabel6, new Color(250, 250, 100));
        ControlC = new Controlador(C, jLabel8, new Color(1, 235, 255));
        ControlR = new Controlador(R, jLabel11, new Color(243, 242, 178));
        Controladores = new ArrayList() {
            {
                add(ControlA);
                add(ControlB);
                add(ControlC);
                add(ControlM);
                add(ControlR);
            }
        };
        BufferedImage textura = new BufferedImage(getWidth(), getHeight(), 2) {
            {
                Color c1 = Color.DARK_GRAY;
                Color c2 = Color.BLACK;
                Graphics2D g = createGraphics();
                g.setPaint(
                        new RadialGradientPaint(
                                new Point2D.Double(getWidth() / 2, getHeight() / 2),
                                getWidth() / 2,
                                new float[]{0, 1},
                                new Color[]{c1, c2}
                        )
                );
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        BufferedImage Textura = Escalar(textura, jLabel1.getWidth(), jLabel1.getHeight(), false);
        Correcto = EscalarGranCalidad(BibliotecaArchivos.Imagenes.Iconos.Correcto_estilo1(), Correct_lbl.getWidth(), Correct_lbl.getHeight());
        Incorrecto = EscalarGranCalidad(BibliotecaArchivos.Imagenes.Iconos.Incorrecto_estilo1(), Incorrect_lbl.getWidth(), Incorrect_lbl.getHeight());
        {
            Graphics2D g = Correcto.createGraphics();
            g.setComposite(new ModoFusión.photoshop.LuzSuave());
            g.drawImage(Correcto, 0, 0, null);
        }
        {
            Graphics2D g = Incorrecto.createGraphics();
            g.setComposite(new ModoFusión.photoshop.LuzSuave());
            g.drawImage(Incorrecto, 0, 0, null);
        }
        {
            int w = jLabel12.getWidth(), h = jLabel12.getHeight();
            BtnDerecha[0] = EscalarGranCalidad(BibliotecaArchivos.Imagenes.Botones.Inicio(), w, h);
            BtnIzquierda[LIBERADO] = ReflejarHorizontal(BtnDerecha[LIBERADO]);
            GenerarMapaBotones(BtnDerecha);
            GenerarMapaBotones(BtnIzquierda);
            {
                final int[] i = new int[1];
                for (JLabel jLabel : BotonesIzquierda) {
                    jLabel.setText(null);
                    jLabel.setIcon(new ImageIcon(BtnIzquierda[LIBERADO]));
                    final int n = i[0];
                    jLabel.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            BotonesIzquierda.get(n).setIcon(new ImageIcon(BtnIzquierda[PRESS]));
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            Controladores.get(n).MoverIzquierda();
                            BotonesIzquierda.get(n).setIcon(new ImageIcon(BtnIzquierda[LIBERADO]));
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            BotonesIzquierda.get(n).setIcon(new ImageIcon(BtnIzquierda[SOBRE]));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            BotonesIzquierda.get(n).setIcon(new ImageIcon(BtnIzquierda[LIBERADO]));
                        }
                    });
                    i[0]++;
                }
            }
            {
                final int[] i = new int[1];
                for (JLabel jLabel : BotonesDerecha) {
                    jLabel.setText(null);
                    jLabel.setIcon(new ImageIcon(BtnDerecha[LIBERADO]));
                    final int n = i[0];
                    jLabel.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            BotonesDerecha.get(n).setIcon(new ImageIcon(BtnDerecha[PRESS]));
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            Controladores.get(n).MoverDerecha();
                            BotonesDerecha.get(n).setIcon(new ImageIcon(BtnDerecha[LIBERADO]));
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                            BotonesDerecha.get(n).setIcon(new ImageIcon(BtnDerecha[SOBRE]));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            BotonesDerecha.get(n).setIcon(new ImageIcon(BtnDerecha[LIBERADO]));
                        }
                    });
                    i[0]++;
                }
            }
        }
        jLabel1.setIcon(new ImageIcon(Textura));
        Apagado = new BufferedImage(Correcto.getWidth(), Correcto.getHeight(), BufferedImage.TYPE_INT_ARGB);
        {
            Graphics2D g = Apagado.createGraphics();
            g.setColor(Color.BLACK);
            g.fillOval(0, 0, Correcto.getWidth(), Correcto.getHeight());
        }
        CargarPartida(PartidaInicial);

        GenerarEtiqueta(new Color(243, 242, 178), jLabel2);
        GenerarEtiqueta(new Color(255, 0, 0), jLabel3);
        GenerarEtiqueta(new Color(255, 200, 0), jLabel7);
        GenerarEtiqueta(new Color(0, 200, 255), jLabel9);

        //JLabel10
        Dupla Dim = new Dupla(jLabel10);
        Resultado[LIBERADO] = Dim.convBufferedImage();
        {
            Graphics2D g = Resultado[LIBERADO].createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setPaint(
                    new RadialGradientPaint(
                            Dim.Mitad().floatX(),
                            Dim.Mitad().floatY(),
                            Dim.Mitad().floatX(),
                            new float[]{0, .5f},
                            new Color[]{new Color(0, 112, 213), new Color(0, 76, 145)}
                    )
            );
            g.fillRoundRect(0, 0, Dim.intX(), Dim.intY(), 60, 60);
            Dim.Sustraer(10);
            Dupla pos = Dupla.Alinear(jLabel10, Dim, Dupla.MEDIO, Dupla.MEDIO);
            g.setStroke(new BasicStroke(2.3f));
            g.setColor(Color.WHITE);
            g.drawRoundRect(pos.intX(), pos.intY(), Dim.Ancho(), Dim.Alto(), 50, 50);
            new GeneradorDeTexto()
                    .ColorFuente(Color.WHITE)
                    .Borde(new Color(0, 59, 113), 3f)
                    .ModificarTamañoFuente(30)
                    .DibujarTextoCentradoEnImagen(jLabel10.getText(), Resultado[LIBERADO]);
            Dim.Sustraer(10).SustraerX(5);
            pos = Dupla.Alinear(jLabel10, Dim, Dupla.MEDIO, Dupla.MEDIO).Proteger();
            g.setPaint(
                    new GradientPaint(
                            pos.intX(), pos.intY(), new Color(1f, 1f, 1f, .5f),
                            pos.intX(), pos.Adicionar(Dim.Mitad()).intY(), new Color(0, 0, 0, 0)
                    )
            );
            g.fillRoundRect(pos.intX(), pos.intY(), Dim.Ancho(), Dim.Mitad().Alto(), 40, 40);
        }
        GenerarMapaBotones(Resultado);
        jLabel10.setText(null);
        jLabel10.setIcon(new ImageIcon(Resultado[LIBERADO]));
        setVisible(true);
    }

    void GenerarMapaBotones(BufferedImage[] imagenes) {
        {
            imagenes[PRESS] = Clonar(imagenes[LIBERADO]);
            Graphics2D g = imagenes[PRESS].createGraphics();
            g.setComposite(new ModoFusión.photoshop.multiplicar());
            g.drawImage(imagenes[PRESS], 0, 0, null);
        }
        {
            imagenes[SOBRE] = Clonar(imagenes[LIBERADO]);
            Graphics2D g = imagenes[SOBRE].createGraphics();
            g.setComposite(new ModoFusión.photoshop.Trama());
            g.drawImage(imagenes[SOBRE], 0, 0, null);
        }
    }

    void GenerarEtiqueta(Color Color, JLabel Lbl) {
        BufferedImage img = Dupla.convBufferedImage(Lbl);
        String txt = Lbl.getText();
        Lbl.setText(null);
        {
            Graphics2D g = img.createGraphics();
            g.setPaint(new GradientPaint(0, 15, new Color(50, 50, 55), 0, img.getHeight(), new Color(16, 16, 16), true));
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.fillRoundRect(0, 0, img.getWidth(), img.getHeight(), 40, 40);
        }
        GeneradorDeTexto gene = new GeneradorDeTexto().ColorFuente(Color).ModificarTamañoFuente(36);
        BufferedImage imgTxt = gene.GenerarTexto(txt);
        BufferedImage Flecha = gene.GenerarTexto("→");
        BufferedImage flechaAlargada = new Dupla(Lbl.getWidth() * .9, Flecha.getHeight()).convBufferedImage();
        {
            Graphics2D g = flechaAlargada.createGraphics();
            g.drawImage(Flecha, flechaAlargada.getWidth() - Flecha.getWidth(), 0, null);
            g.drawImage(
                    Escalar(
                            Flecha.getSubimage(0, 0, Flecha.getWidth() / 2, Flecha.getHeight()),
                            flechaAlargada.getWidth() - Flecha.getWidth() / 2,
                            flechaAlargada.getHeight(), false
                    ),
                    0, 0, null
            );
            int h = 0;
            for (int f = 0; f < flechaAlargada.getHeight(); f++) {
                for (int c = 0; c < flechaAlargada.getWidth(); c++) {
                    if (flechaAlargada.getRGB(c, f) >>> 24 > 0) {
                        h = f;
                        break;
                    }
                }
                if (h != 0) {
                    break;
                }
            }
            flechaAlargada = flechaAlargada.getSubimage(0, h, flechaAlargada.getWidth(), flechaAlargada.getHeight() - h - 1);
        }
        BufferedImage composición = new Dupla(flechaAlargada).AdicionarY(imgTxt).convBufferedImage();
        {
            Graphics2D g = composición.createGraphics();
            int h = 0;
            Dupla pos = Dupla.Alinear(composición, imgTxt, Dupla.MEDIO, Dupla.POR_DEFECTO);
            g.drawImage(imgTxt, pos.intX(), h, null);
            h += imgTxt.getHeight();
            pos = Dupla.Alinear(composición, flechaAlargada, Dupla.MEDIO, Dupla.POR_DEFECTO);
            g.drawImage(flechaAlargada, pos.intX(), h, null);
        }
//        g.setComposite(CargarModoFusión(PROMEDIO));
        Ajuste_Personalizado(composición, img, AJUSTE_CENTRADO_AJUSTAR);
        Lbl.setIcon(new ImageIcon(img));
    }

    void CalcularResultado() {
        Runnable runnable = new Runnable() {
            public void run() {
                for (int i = 0; i < R.size(); i++) {
                    R.set(i, (byte) 0);
                }
                ControlR.RefrescarDibujo();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
                for (int i = 0; i < Columnas; i++) {
                    R.set(i, (byte) (A.get(i) + B.get(i) + C.get(i)));
                    ControlR.color = Color.GREEN;
                    ControlR.RefrescarDibujo();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                    }
                    ControlR.color = new Color(243, 242, 178);
                    ControlR.RefrescarDibujo();
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException ex) {
                    }
                }
                CalcularSolución();
            }
        };
        new Thread(runnable).start();
    }

    void CalcularSolución() {
        boolean EsCorrecto = true;
        for (int i = 0; i < Columnas; i++) {
            EsCorrecto &= A.get(i) + B.get(i) + C.get(i) == M.get(i);
        }
        if (EsCorrecto) {
            Correct_lbl.setIcon(new ImageIcon(Correcto));
            Incorrect_lbl.setIcon(new ImageIcon(Apagado));
            ControlR.color = new Color(0, 120, 0);
            ControlR.RefrescarDibujo();
        } else {
            Correct_lbl.setIcon(new ImageIcon(Apagado));
            Incorrect_lbl.setIcon(new ImageIcon(Incorrecto));
            ControlR.color = new Color(120, 0, 0);
            ControlR.RefrescarDibujo();
        }
    }

    public static final int Alto() {
        int mayor = Integer.MIN_VALUE;
        for (int número : M) {
            mayor = número > mayor ? número : mayor;
        }
        return mayor;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        Incorrect_lbl = new javax.swing.JLabel();
        Correct_lbl = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Muestra");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 30, 240, 60);

        jLabel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        getContentPane().add(jLabel4);
        jLabel4.setBounds(280, 20, 590, 90);

        jLabel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        getContentPane().add(jLabel5);
        jLabel5.setBounds(280, 140, 590, 90);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("A");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 150, 150, 60);

        jLabel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        getContentPane().add(jLabel6);
        jLabel6.setBounds(280, 260, 590, 90);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("B");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(20, 270, 150, 60);

        jLabel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        getContentPane().add(jLabel8);
        jLabel8.setBounds(280, 380, 590, 90);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("C");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(20, 390, 150, 60);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Resultado");
        jLabel10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel10MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel10MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel10MouseReleased(evt);
            }
        });
        getContentPane().add(jLabel10);
        jLabel10.setBounds(20, 510, 180, 60);

        jLabel11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        getContentPane().add(jLabel11);
        jLabel11.setBounds(280, 500, 590, 90);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText(">");
        jLabel12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(jLabel12);
        jLabel12.setBounds(230, 280, 40, 40);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("<");
        jLabel13.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(jLabel13);
        jLabel13.setBounds(180, 280, 40, 40);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("<");
        jLabel14.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(jLabel14);
        jLabel14.setBounds(180, 160, 40, 40);

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText(">");
        jLabel15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(jLabel15);
        jLabel15.setBounds(230, 160, 40, 40);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("<");
        jLabel16.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(jLabel16);
        jLabel16.setBounds(180, 400, 40, 40);

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText(">");
        jLabel17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(jLabel17);
        jLabel17.setBounds(230, 400, 40, 40);
        getContentPane().add(Incorrect_lbl);
        Incorrect_lbl.setBounds(220, 550, 40, 40);
        getContentPane().add(Correct_lbl);
        Correct_lbl.setBounds(220, 500, 40, 40);

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("C");
        jLabel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel18MouseReleased(evt);
            }
        });
        getContentPane().add(jLabel18);
        jLabel18.setBounds(200, 100, 30, 30);
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 900, 610);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel10MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseReleased
        CalcularResultado();
        jLabel10.setIcon(new ImageIcon(Resultado[LIBERADO]));
    }//GEN-LAST:event_jLabel10MouseReleased

    private void jLabel18MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseReleased
        GenerarPartidaAleatoria();
    }//GEN-LAST:event_jLabel18MouseReleased

    private void jLabel10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MousePressed
        jLabel10.setIcon(new ImageIcon(Resultado[PRESS]));
    }//GEN-LAST:event_jLabel10MousePressed

    private void jLabel10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseExited
        jLabel10.setIcon(new ImageIcon(Resultado[LIBERADO]));
    }//GEN-LAST:event_jLabel10MouseExited

    private void jLabel10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseEntered
        jLabel10.setIcon(new ImageIcon(Resultado[SOBRE]));
    }//GEN-LAST:event_jLabel10MouseEntered

    public static void main(String args[]) {
        new SamplePuzzle();
    }
    static boolean Desplazando = false;

    public static class Controlador {

        Composite modoFusión = new ModoFusión.photoshop.aditivo();
        static BufferedImage Fondo;
        static double AnchoCasilla, AltoCasilla;
        Color color;
        final ArrayList<Byte> Arreglo;
        JLabel Label;

        static final BufferedImage Fondo() {
            Dupla Dimensiones = new Dupla(jLabel4).SustraerY(1);
            BufferedImage Retorno = Dimensiones.convBufferedImage();
            Graphics2D g = Retorno.createGraphics();
            g.setPaint(new GradientPaint(0, 0, Color.BLACK, 0, Dimensiones.Alto() / 2, new Color(0, 24, 44), true));
            g.fillRect(0, 0, Dimensiones.Ancho(), Dimensiones.Alto());
            BufferedImage Cuadricula = Dimensiones.convBufferedImage();
            g = Cuadricula.createGraphics();
            g.setColor(new Color(30, 50, 30));
            g.setStroke(new BasicStroke(1.5f));
            AnchoCasilla = Dimensiones.Ancho() / Columnas;
            AltoCasilla = Dimensiones.Alto() / Filas;
            for (int C = 0; C < Columnas; C++) {
                for (int F = 0; F < Filas; F++) {
                    g.drawRect((int) (C * AnchoCasilla), (int) (F * AltoCasilla), (int) AnchoCasilla, (int) AltoCasilla);
                }
            }
            g = Retorno.createGraphics();
            g.setComposite(new ModoFusión.photoshop.divide().ModificarOpacidad(.16));
            g.drawImage(Cuadricula, 0, 0, null);
            return Retorno;
        }

        static void ActualizarFondo() {
            Fondo = Fondo();
        }

        public Controlador(ArrayList<Byte> Arreglo, JLabel Label, Color c) {
            if (Fondo == null) {
                ActualizarFondo();
            }
            this.Arreglo = Arreglo;
            this.Label = Label;
            color = c;
            RefrescarDibujo();
        }

        void MoverIzquierda() {
            if (Desplazando) {
                return;
            }
            Desplazando = true;
            new Thread(() -> {
                for (int i = 0; i <= 30; i++) {
                    BufferedImage Ondas = Dupla.convBufferedImage(Fondo);
                    Graphics2D g = Ondas.createGraphics();
                    g.setColor(color);
                    {
                        int Columna = -1;
                        final int w = (int) (AnchoCasilla + 1), h = (int) (AltoCasilla + 1);
                        for (byte b : Arreglo) {
                            final int x = (int) (++Columna * AnchoCasilla);
                            for (int Fila = 0; Fila < b; Fila++) {
                                int y = (int) ((Filas - Fila - 1) * AltoCasilla);
                                g.fillRect(x, y, w, h);
                            }
                        }
                    }
                    BufferedImage Panel = Clonar(Fondo);
                    g = Panel.createGraphics();
                    g.setComposite(modoFusión);
                    AffineTransform a = AffineTransform.getTranslateInstance(-AnchoCasilla * i / 30.0, 0);
                    g.setTransform(a);
                    g.drawImage(Ondas, 0, 0, null);
                    g.drawImage(Ondas, Ondas.getWidth(), 0, null);
                    Label.setIcon(new ImageIcon(Panel));
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                    }
                }
                DesplazarArregloIzquierda(Arreglo);
                RefrescarDibujo();
                Desplazando = false;
            }
            ).start();
        }

        static void DesplazarArregloIzquierda(byte[] Arreglo) {
            byte[] Nuevo = new byte[Arreglo.length];
            byte U = Arreglo[0];
            for (int i = 0; i < Arreglo.length - 1; i++) {
                Nuevo[i] = Arreglo[i + 1];
            }
            Nuevo[Arreglo.length - 1] = U;
            for (int i = 0; i < Arreglo.length; i++) {
                Arreglo[i] = Nuevo[i];
            }
        }

        static void DesplazarArregloIzquierda(ArrayList<Byte> Arreglo) {
            byte[] Nuevo = new byte[Arreglo.size()];
            byte U = Arreglo.get(0);
            for (int i = 0; i < Arreglo.size() - 1; i++) {
                Nuevo[i] = Arreglo.get(i + 1);
            }
            Nuevo[Arreglo.size() - 1] = U;
            for (int i = 0; i < Arreglo.size(); i++) {
                Arreglo.set(i, Nuevo[i]);
            }
        }

        void MoverDerecha() {
            if (Desplazando) {
                return;
            }
            Desplazando = true;
            new Thread(() -> {
                for (int i = 0; i <= 30; i++) {
                    BufferedImage Ondas = Dupla.convBufferedImage(Fondo);
                    Graphics2D g = Ondas.createGraphics();
                    g.setColor(color);
                    {
                        int Columna = -1;
                        final int w = (int) (AnchoCasilla + 1), h = (int) (AltoCasilla + 1);
                        for (byte b : Arreglo) {
                            final int x = (int) (++Columna * AnchoCasilla);
                            for (int Fila = 0; Fila < b; Fila++) {
                                int y = (int) ((Filas - Fila - 1) * AltoCasilla);
                                g.fillRect(x, y, w, h);
                            }
                        }
                    }
                    BufferedImage Panel = Clonar(Fondo);
                    g = Panel.createGraphics();
                    g.setComposite(modoFusión);
                    AffineTransform a = AffineTransform.getTranslateInstance(AnchoCasilla * i / 30.0, 0);
                    g.setTransform(a);
                    g.drawImage(Ondas, 0, 0, null);
                    g.drawImage(Ondas, -Ondas.getWidth(), 0, null);
                    Label.setIcon(new ImageIcon(Panel));
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                    }
                }
                DesplazarArregloDerecha(Arreglo);
                RefrescarDibujo();
                Desplazando = false;
            }).start();
        }

        static void DesplazarArregloDerecha(byte[] Arreglo) {
            byte[] Nuevo = new byte[Arreglo.length];
            byte U = Arreglo[Arreglo.length - 1];
            for (int i = 1; i < Arreglo.length; i++) {
                Nuevo[i] = Arreglo[i - 1];
            }
            Nuevo[0] = U;
            for (int i = 0; i < Arreglo.length; i++) {
                Arreglo[i] = Nuevo[i];
            }
        }

        static void DesplazarArregloDerecha(ArrayList<Byte> Arreglo) {
            byte[] Nuevo = new byte[Arreglo.size()];
            byte U = Arreglo.get(Arreglo.size() - 1);
            for (int i = 1; i < Arreglo.size(); i++) {
                Nuevo[i] = Arreglo.get(i - 1);
            }
            Nuevo[0] = U;
            for (int i = 0; i < Arreglo.size(); i++) {
                Arreglo.set(i, Nuevo[i]);
            }
        }

        void RefrescarDibujo() {
            BufferedImage Ondas = Dupla.convBufferedImage(Fondo);
            Graphics2D g = Ondas.createGraphics();
            g.setColor(color);
            int c = 0;
            for (byte b : Arreglo) {
                for (int f = 0; f < b; f++) {
                    g.fillRect((int) (c * AnchoCasilla), (int) ((Filas - f - 1) * AltoCasilla), (int) AnchoCasilla + 1, (int) AltoCasilla + 1);
                }
                c++;
            }
            BufferedImage Panel = Clonar(Fondo);
            g = Panel.createGraphics();
            g.setComposite(modoFusión);
            g.drawImage(Ondas, 0, 0, null);
            Label.setIcon(new ImageIcon(Panel));
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Correct_lbl;
    private javax.swing.JLabel Incorrect_lbl;
    private static javax.swing.JLabel jLabel1;
    private static javax.swing.JLabel jLabel10;
    private static javax.swing.JLabel jLabel11;
    private static javax.swing.JLabel jLabel12;
    private static javax.swing.JLabel jLabel13;
    private static javax.swing.JLabel jLabel14;
    private static javax.swing.JLabel jLabel15;
    private static javax.swing.JLabel jLabel16;
    private static javax.swing.JLabel jLabel17;
    private static javax.swing.JLabel jLabel18;
    private static javax.swing.JLabel jLabel2;
    private static javax.swing.JLabel jLabel3;
    private static javax.swing.JLabel jLabel4;
    private static javax.swing.JLabel jLabel5;
    private static javax.swing.JLabel jLabel6;
    private static javax.swing.JLabel jLabel7;
    private static javax.swing.JLabel jLabel8;
    private static javax.swing.JLabel jLabel9;
    // End of variables declaration//GEN-END:variables
}
