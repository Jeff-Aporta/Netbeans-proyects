package dijkstra;

//<editor-fold defaultstate="collapsed" desc="Importación de librerias">
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientasGUI.Presentador;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
//</editor-fold>

public class GUI_Dijkstra extends JFrame {

    JComponent[][] matrizAdyacencia_GUI = new JComponent[11][11];

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
        }
        new GUI_Dijkstra()
                .CargarElementosMatrizAdyacencia()
                .ListenersParaEfectoReflejo()
                .ActualizarGráfica();
//</editor-fold>
    }

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public GUI_Dijkstra() {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        initComponents();
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(30);
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(30);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
//</editor-fold>
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos auxiliares para el constructor">
    GUI_Dijkstra ListenersParaEfectoReflejo() {
        //<editor-fold defaultstate="collapsed" desc="Actualización automatica de los labels relacionados y cambio de color">
        for (int c = 1; c < 11; c++) {
            for (int f = 1; f < 11; f++) {
                if (matrizAdyacencia_GUI[c][f] instanceof JTextField) {
                    JTextField Txtfld = (JTextField) matrizAdyacencia_GUI[c][f];
                    if (matrizAdyacencia_GUI[f][c] instanceof JLabel) {
                        JLabel lbl = (JLabel) matrizAdyacencia_GUI[f][c];
                        Txtfld.addKeyListener(new KeyAdapter() {
                            @Override
                            public void keyReleased(KeyEvent evt) {
                                lbl.setText(Txtfld.getText());
                                try {
                                    if (!Txtfld.getText().equals("")) {
                                        double a = Double.parseDouble(Txtfld.getText());
                                    }
                                    Txtfld.setBackground(Color.WHITE);
                                    ActualizarGráfica();
                                    //<editor-fold defaultstate="collapsed" desc="Impresión de la matriz en código">
                                    {
                                        double[][] a = MatrizAdyacenciaAjustada();
                                        String r = "{\n";
                                        for (int f = 0; f < a.length; f++) {
                                            r += "\t{";
                                            for (int c = 0; c < a[0].length; c++) {
                                                r += a[f][c] == Double.POSITIVE_INFINITY ? 0 + "" : a[f][c] == (int) a[f][c] ? (int) a[f][c] + "" : a[f][c];
                                                if (c != a[0].length - 1) {
                                                    r += ",";
                                                }
                                            }
                                            r += "}";
                                            r += (f != a[0].length - 1 ? "," : "") + "\n";
                                        }
                                        r += "}";
                                        System.out.println(r);
                                    }
                                    //</editor-fold>
                                } catch (Exception e) {
                                    Txtfld.setBackground(Color.PINK);
                                }
                            }
                        });
                    }
                }
            }
        }
        return this;
        //</editor-fold>
    }

    GUI_Dijkstra CargarElementosMatrizAdyacencia() {
        //<editor-fold defaultstate="collapsed" desc="Se toman los JTextFields y  los JLabels y se relacionan en una matriz de filas y columnas">
        for (int c = 0; c < 11; c++) {
            for (int f = 0; f < 11; f++) {
                matrizAdyacencia_GUI[c][f] = (JComponent) jPanel1.getComponent(c * 11 + f);
                JComponent component = matrizAdyacencia_GUI[c][f];
                component.setBorder(BorderFactory.createLineBorder(new Color(0xaaaaaa)));
                if (matrizAdyacencia_GUI[c][f] instanceof JLabel) {
                    JLabel lbl = (JLabel) matrizAdyacencia_GUI[c][f];
                    lbl.setHorizontalAlignment(JLabel.CENTER);
                    if (c == 0) {
                        if (f != 0) {
                            lbl.setText(f + "");
                            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
                        }
                    } else if (f == 0) {
                        lbl.setText(c + "");
                        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
                    }
                }
                if (matrizAdyacencia_GUI[c][f] instanceof JTextField) {
                    JTextField Txtfld = (JTextField) matrizAdyacencia_GUI[c][f];
                    Txtfld.setHorizontalAlignment(JTextField.CENTER);
                    if (c == f) {
                        Txtfld.setText("0");
                        Txtfld.setEditable(false);
                    }
                }
            }
        }
        return this;
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas para relacionar la matriz de la GUI en matriz númerica">
    double[][] MatrizAdyacenciaAjustada() {
        //<editor-fold defaultstate="collapsed" desc="Generación de la matriz omitiendo los nodos que no tienen conexión">
        int dim = 0;
        double[][] M = MatrizAdyacencia();
        for (int c = 0; c < 10; c++) {
            for (int f = 0; f < 10; f++) {
                if (c == f) {
                    continue;
                }
                if (M[c][f] != Double.POSITIVE_INFINITY) {
                    dim = c + 1;
                    break;
                }
            }
        }
        double[][] MR = new double[dim][dim];
        for (int c = 0; c < dim; c++) {
            for (int f = 0; f < dim; f++) {
                MR[c][f] = M[c][f];
            }
        }
        return MR;
//</editor-fold>
    }

    double[][] MatrizAdyacencia() {
        //<editor-fold defaultstate="collapsed" desc="Generación de la matriz teniendo en cuenta los 10 nodos posibles">
        double[][] Matriz = new double[10][10];
        for (int c = 1; c < 11; c++) {
            for (int f = 1; f < 11; f++) {
                try {
                    JTextField Txtfld;
                    if (matrizAdyacencia_GUI[c][f] instanceof JLabel) {
                        Txtfld = (JTextField) matrizAdyacencia_GUI[f][c];
                    } else {
                        Txtfld = (JTextField) matrizAdyacencia_GUI[c][f];
                    }
                    Matriz[c - 1][f - 1] = Double.parseDouble(Txtfld.getText());
                    if (c == f) {
                        continue;
                    }
                    if (Matriz[c - 1][f - 1] == 0) {
                        throw new RuntimeException("El costo no puede ser 0");
                    }
                } catch (Exception e) {
                    Matriz[c - 1][f - 1] = Double.POSITIVE_INFINITY;
                }
            }
        }
        return Matriz;
//</editor-fold>
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos gráficos para el dibujado del grafo y de las tablas">
    GUI_Dijkstra ActualizarGráfica() {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        try {
            double[][] m = MatrizAdyacenciaAjustada();
            int cantidadNodos = m.length;

            BufferedImage fotograma = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);

            //<editor-fold defaultstate="collapsed" desc="Dibujado del grafo en la imagen">
            Graphics2D g = fotograma.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Nodo_AuxiliarParaDibujar[] nodos = new Nodo_AuxiliarParaDibujar[cantidadNodos];
            Arista_AuxiliarParaDibujar[] arista = new Arista_AuxiliarParaDibujar[CantidadDeAristas(m)];
            {
                //<editor-fold defaultstate="collapsed" desc="Calculo de la posición e inicialización de los nodos">
                Point centro = new Point(fotograma.getWidth() / 2, fotograma.getHeight() / 2);
                int radio = (int) (.5 * fotograma.getHeight() - 45);
                double separaciónAngular = 2 * Math.PI / cantidadNodos;

                for (int i = 0; i < cantidadNodos; i++) {
                    nodos[i] = new Nodo_AuxiliarParaDibujar(new Point(
                            (int) (centro.x + radio * Math.cos(separaciónAngular * i - separaciónAngular / 2)),
                            (int) (centro.y + radio * Math.sin(separaciónAngular * i - separaciónAngular / 2))
                    ));
                }
                //</editor-fold>
            }
            {
                //<editor-fold defaultstate="collapsed" desc="Inicialización de las aristas">
                int i = 0;
                for (int f = 0; f < cantidadNodos; f++) {
                    for (int c = 0; c < f; c++) {
                        if (m[f][c] != 0 && m[f][c] != Double.POSITIVE_INFINITY) {
                            arista[i++] = new Arista_AuxiliarParaDibujar(nodos[f], nodos[c], (int) m[f][c]);
                        }
                    }
                }
                //</editor-fold>
            }
            //<editor-fold defaultstate="collapsed" desc="dibujado de las aristas">
            {
                int grosor = 3;
                g.setStroke(new BasicStroke(grosor));
                for (Arista_AuxiliarParaDibujar aristaDibujar : arista) {
                    g.setColor(aristaDibujar.color);
                    g.drawLine(
                            aristaDibujar.nodo1.posición.x, aristaDibujar.nodo1.posición.y,
                            aristaDibujar.nodo2.posición.x, aristaDibujar.nodo2.posición.y
                    );
                    BufferedImage costo = new GeneradorDeTexto()
                            .ColorFuente(Color.BLACK)
                            .Borde(Color.GRAY, 8)
                            .DibujarTextoCentradoEnImagen(
                                    new BufferedImage(40, 40, 2) {
                                {
                                    Graphics2D g1 = createGraphics();
                                    g1.setRenderingHint(
                                            RenderingHints.KEY_ANTIALIASING,
                                            RenderingHints.VALUE_ANTIALIAS_ON
                                    );
                                    g1.setColor(aristaDibujar.color);
                                    g1.fillOval(0, 0, getWidth(), getHeight());
                                }
                            },
                                    aristaDibujar.costo + ""
                            );

                    g.drawImage(
                            costo,
                            (aristaDibujar.nodo1.posición.x + aristaDibujar.nodo2.posición.x - costo.getWidth()) / 2,
                            (aristaDibujar.nodo1.posición.y + aristaDibujar.nodo2.posición.y - costo.getHeight()) / 2,
                            null
                    );
                }
            }
            //</editor-fold>

            for (int i = 0; i < cantidadNodos; i++) {
                {//dibujado del Nodo
                    int diametro = 80;
                    int r = diametro / 2;
                    BufferedImage nodo = new BufferedImage(diametro, diametro, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g1 = nodo.createGraphics();
                    {//Suavizadoi de bordes
                        g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    }
                    g1.setColor(Color.ORANGE);
                    g1.fillOval(0, 0, diametro, diametro);
                    g1.setColor(Color.BLACK);
                    int grosor = 2;
                    g1.setStroke(new BasicStroke(grosor));
                    g1.drawOval(grosor / 2, grosor / 2, diametro - grosor, diametro - grosor);
                    nodo = new GeneradorDeTexto()
                            .ColorFuente(Color.BLACK)
                            .DibujarTextoCentradoEnImagen(nodo, (i + 1) + "");

                    g.drawImage(nodo, nodos[i].posición.x - r, nodos[i].posición.y - r, null);
                }
            }
            //</editor-fold>

            ((Presentador) jLabel1).ActualizarFotograma(fotograma);
        } catch (Exception e) {
            e.printStackTrace();
        }
        actualizarMatrices();
        return this;
        //</editor-fold>
    }

    void actualizarMatrices() {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        try {
            jLabel40.setText(HTMLMatrices_Costes_Recorrido(MatrizCostes_CalculadaConPotencia(MatrizAdyacenciaAjustada())));
            jLabel69.setText(HTMLMatriz_Floyd_Warshall());
        } catch (Exception e) {
            jLabel40.setText("");
            jLabel69.setText("");
        }
        //</editor-fold>
    }

    static class Nodo_AuxiliarParaDibujar {

        //<editor-fold defaultstate="collapsed" desc="Implementación de la clase">
        Point posición;

        public Nodo_AuxiliarParaDibujar(Point posición) {
            this.posición = posición;
        }
        //</editor-fold>
    }

    static class Arista_AuxiliarParaDibujar {

        //<editor-fold defaultstate="collapsed" desc="Implementacion de la clase">
        Nodo_AuxiliarParaDibujar nodo1;
        Nodo_AuxiliarParaDibujar nodo2;
        int costo;
        Color color;

        public Arista_AuxiliarParaDibujar(Nodo_AuxiliarParaDibujar nodo1, Nodo_AuxiliarParaDibujar nodo2, int costo) {
            this.nodo1 = nodo1;
            this.nodo2 = nodo2;
            this.costo = costo;
            color = new Color((int) (Math.random() * Integer.MAX_VALUE));
        }
        //</editor-fold>
    }

    String HTMLMatrices_Costes_Recorrido(double[][] m) {
        //<editor-fold defaultstate="collapsed" desc="Generación del código HTML">
        String[][][] Floyd_Worshall = Conclusiones_Floyd_Warshall();
        String HTML = "<html>"
                + "<p align=\"center\">"
                + "<table cellspacing=\"15\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td style=\"text-align: center; vertical-align: top;\">"
                + "Matriz de costos mínimos"
                + "<br>"
                + "<br>"
                + HTMLMatriz_Tabla(Floyd_Worshall[0], true)
                + "</td>\n"
                + "<td style=\"text-align: center; vertical-align: top;\">"
                + "Matriz de recorridos"
                + "<br>"
                + "<br>"
                + HTMLMatriz_Tabla(Floyd_Worshall[1], true)
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>"
                + "</html>";
        return HTML;
        //</editor-fold>
    }

    String HTMLMatriz_Floyd_Warshall() {
        //<editor-fold defaultstate="collapsed" desc="Generación del código HTML">
        String[][][] Floyd_Worshall = Conclusiones_Floyd_Warshall();
        String HTML = "<html>"
                + "<p align=\"center\">"
                + "<table cellspacing=\"15\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td style=\"text-align: center; vertical-align: top;\">"
                + "Conclusiones"
                + "<br>"
                + "<br>"
                + HTMLMatriz_Tabla(Floyd_Worshall[2])
                + "</td>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>"
                + "</html>";
        return HTML;
        //</editor-fold>
    }

    String HTMLMatriz_Tabla(double[][] m) {
        return HTMLMatriz_Tabla(ConvertirMatrizDoubleEnString(m), true);
    }

    static String HTMLMatriz_Tabla(String[][] m) {
        return HTMLMatriz_Tabla(m, false);
    }

    static String HTMLMatriz_Tabla(String[][] m, boolean enumerar) {
        //<editor-fold defaultstate="collapsed" desc="Código de la tabla">
        String HTML = "<table  style=\"margin-left: auto; margin-right: auto;\" border=\"1\" cellspacing=\"0\" cellpadding=\"4\">\n"
                + "<tbody>\n";
        if (enumerar) {
            //<editor-fold defaultstate="collapsed" desc="Encabezados superiores (Columnas)">
            HTML += "<tr>";
            HTML += "<td style=\"background-color: #005FA3;\">";
            HTML += " ";
            HTML += "</td>";
            for (int i = 0; i < m.length; i++) {
                HTML += "<td style=\"background-color: #005FA3;\">";
                HTML += "<span style=\"color: #ffffff;\">";
                HTML += "<strong>";
                HTML += (i + 1);
                HTML += "</strong>";
                HTML += "</span>";
                HTML += "</td>";
            }
            HTML += "</tr>";
            //</editor-fold>
        }

        for (int f = 0; f < m[0].length; f++) {
            //<editor-fold defaultstate="collapsed" desc="Inserción de las filas">
            HTML += "<tr>";
            if (enumerar) {
                //<editor-fold defaultstate="collapsed" desc="Encabezados laterales (Filas)">
                HTML += "<td style=\"background-color: #005FA3;\">";
                HTML += "<span style=\"color: #ffffff;\">";
                HTML += "<strong>";
                HTML += (f + 1);
                HTML += "</strong>";
                HTML += "</span>";
                HTML += "</td>";
                //</editor-fold>
            }
            for (int c = 0; c < m.length; c++) {
                //<editor-fold defaultstate="collapsed" desc="Cuerpo de la fila">
                if ((c == 0 || f == 0) && !enumerar) {
                    HTML += "<td style=\"background-color: #005FA3;\">";
                    HTML += "<span style=\"color: #ffffff;\">";
                    HTML += "<strong>";

                } else {
                    HTML += "<td style=\"background-color: #ffffff;\">";
                }

                boolean especial = false;
                if (m[c][f] != null) {
                    switch (m[c][f]) {
                        case "∞":
                            HTML += "<span style=\"color: #00aa00;\">";
                            especial = true;
                        case "×":
                            HTML += especial ? "" : "<span style=\"color: #aa0000;\">";
                            HTML += "<strong>";
                        default:
                            HTML += m[c][f];
                            if (especial) {
                                HTML += "</strong>";
                                HTML += "</span>";
                            }
                    }
                }
                if ((c == 0 || f == 0) && !enumerar) {
                    HTML += "</strong>";
                    HTML += "</span>";
                }
                HTML += "</td>";
                //</editor-fold>
            }
            HTML += "</tr>";
            //</editor-fold>
        }
        HTML += "</tbody>\n"
                + "</table>\n";
        HTML = HTML
                .replace("<td style=\"", "<td style=\"text-align: center;width: 20px;")
                .replace("<td>", "<td style=\"text-align: center;width: 20px;\">");
        return HTML;
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas de matriz de costes calculada con potencia">
    static double[][] MatrizCostes_CalculadaConPotencia(double[][] m) {
        return PotenciaLog(m, CantidadDeAristas(m));
    }

    static int CantidadDeAristas(double[][] m) {
        //<editor-fold defaultstate="collapsed" desc="Contar los elementos en una de las mitades tiangulares de la matriz">
        int a = 0;
        for (int f = 0; f < m.length; f++) {
            for (int c = 0; c < m[0].length; c++) {
                if (c == f) {
                    break;
                }
                if (m[c][f] != 0 && m[c][f] != Double.POSITIVE_INFINITY) {
                    a++;
                }
            }
        }
        return a;
        //</editor-fold>
    }

    static double[][] PotenciaLog(double[][] m, int p) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (p == 1) {//Toda matriz elevada a la 1 es igual a ella misma, este es el punto de parada
            return m;
        }
        if (p % 2 == 0) {
            return PotenciaLog(MultiplicaciónFeliz(m, m), p / 2);
        } else {
            return MultiplicaciónFeliz(m, PotenciaLog(MultiplicaciónFeliz(m, m), p / 2));
        }
        //</editor-fold>
    }

    static double[][] MultiplicaciónFeliz(double[][] matriz1, double[][] matriz2) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de Código">
        int filas_m1 = matriz1.length;
        int columnas_m1 = matriz1[0].length;
        int filas_m2 = matriz2.length;
        int columnas_m2 = matriz2[0].length;
        if (columnas_m1 != filas_m2) {
            throw new RuntimeException("Columnas no coinciden con filas");
        }
        double[][] retorno = new double[columnas_m2][filas_m1];
        for (int col_i = 0; col_i < filas_m1; col_i++) {
            for (int fil_i = 0; fil_i < columnas_m2; fil_i++) {
                double e = Double.POSITIVE_INFINITY;
                for (int k = 0; k < columnas_m1; k++) {
                    e = Math.min(matriz1[col_i][k] + matriz2[k][fil_i], e);
                }
                retorno[col_i][fil_i] = e;
            }
        }
        return retorno;
//</editor-fold>
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas Floyd-Warshall">
    String[][][] Conclusiones_Floyd_Warshall() {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        double[][][] M = Floyd_Warshall();
        int cantidadNodos = M[0].length;
        int repetidor = (cantidadNodos - 1);
        String[][] S = new String[4][cantidadNodos * repetidor + 1];
        //<editor-fold defaultstate="collapsed" desc="Encabezados">
        S[0][0] = "Nodo Origen";
        S[1][0] = "Nodo Destino";
        S[2][0] = "Distancia Mínima";
        S[3][0] = "Recorrido";
        //</editor-fold>
        for (int i = 0; i < cantidadNodos; i++) {
            int c = 1;
            for (int j = 0; j < repetidor; j++, c++) {
                //<editor-fold defaultstate="collapsed" desc="Llenado de la tabla">
                int fila = i * repetidor + j + 1;
                int NodoOrigen = i + 1;
                S[0][fila] = NodoOrigen + "";
                if (c == i + 1) {
                    c++;
                }
                int NodoDestino = c;
                S[1][fila] = NodoDestino + "";
                S[2][fila] = ConvertirDoubleEnString(M[0][i][c - 1]) + "";
                S[3][fila] = CaminoString(NodoOrigen - 1, NodoDestino - 1) + "";
                //</editor-fold>
            }
        }
        return new String[][][]{
            ConvertirMatrizDoubleEnString(M[0]),
            ConvertirMatrizDoubleEnString(M[1]),
            S
        };
        //</editor-fold>
    }

    String CaminoString(int NodoPartida, int NodoDestino) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        try {
            int[] camino = null;
            {
                int[] camino1 = Camino(NodoPartida, NodoDestino);
                if (camino1 != null) {
                    int[] camino2 = Camino(NodoDestino, NodoPartida);
                    if (camino1.length > camino2.length) {
                        camino = camino1;
                    } else {
                        camino = new int[camino2.length];
                        for (int i = 0; i < camino.length; i++) {
                            camino[i] = camino2[camino.length - 1 - i];
                        }
                    }
                }
            }
            String retorno = "";
            for (int i = 0; camino != null && i < camino.length; i++) {
                retorno += camino[i];
                if (i != camino.length - 1) {
                    retorno += ",";
                }
            }
            if (retorno.isEmpty()) {
                return "×";
            }
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
        //</editor-fold>
    }

    int[] Camino(final int NodoPartida, final int NodoDestino) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (NodoPartida == NodoDestino) {
            return new int[]{NodoPartida};
        }
        double[][][] F = Floyd_Warshall();
        {
            double[][] MatrizCostes = F[0];
            if (MatrizCostes[NodoPartida][NodoDestino] == Double.POSITIVE_INFINITY) {
                return null;
            }
        }
        double[][] MatrizRecorridos = F[1];
        ArrayList<Integer> n = new ArrayList<>();
        n.add(NodoDestino + 1);
        double i = MatrizRecorridos[NodoPartida][NodoDestino];

        int b = 0;
        while (!Double.isNaN(i)) {
            n.add((int) i);
            i = MatrizRecorridos[NodoPartida][(int) i - 1];
        }
        Collections.reverse(n);
        int[] retorno = new int[n.size()];
        for (int j = 0; j < n.size(); j++) {
            retorno[j] = n.get(j);
        }
        return retorno;

    } //</editor-fold>

    double[][][] Floyd_Warshall() { //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        double[][] MatrizCostos = MatrizAdyacenciaAjustada();
        int cantidadNodos = MatrizCostos.length;
        double[][] MatrizRecorridos = new double[cantidadNodos][cantidadNodos];
        //<editor-fold defaultstate="collapsed" desc="Llenado de la matriz de recorridos">
        for (int c = 0; c < cantidadNodos; c++) {
            for (int f = 0; f < cantidadNodos; f++) {
                if (c == f) {
                    MatrizRecorridos[c][f] = Double.NaN;
                } else {
                    MatrizRecorridos[c][f] = c + 1;
                }
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Ejecución del algoritmo de Floyd-Warshall">
        for (int Pivote = 0; Pivote < cantidadNodos; Pivote++) {
            for (int c = 0; c < cantidadNodos; c++) {
                for (int f = 0; f < cantidadNodos; f++) {
                    double suma = MatrizCostos[Pivote][f] + MatrizCostos[c][Pivote];
                    double e = MatrizCostos[c][f];
                    if (suma < e) {
                        MatrizCostos[c][f] = suma;
                        MatrizRecorridos[c][f] = Pivote + 1;
                    }
                }
            }
        }
        //</editor-fold>

        return new double[][][]{
            MatrizCostos,
            MatrizRecorridos
        };
    }//</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramienta para la verificación por consola">
    static void imprimirMatriz(double[][] m) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        for (int f = 0; f < m.length; f++) {
            for (int c = 0; c < m[0].length; c++) {
                if (m[f][c] == Double.POSITIVE_INFINITY) {
                    System.out.print("∞ ");
                } else if (Double.isNaN(m[f][c])) {
                    System.out.print("- ");
                } else if (m[f][c] == (int) m[f][c]) {
                    System.out.print((int) m[f][c] + " ");
                } else {
                    System.out.print(m[f][c] + " ");
                }
            }
            System.out.println();
        }
//</editor-fold>
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Auxiliares para convertir doubles en String">
    static String ConvertirDoubleEnString(double d) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (d == Double.POSITIVE_INFINITY) {
            return "∞";
        } else if (Double.isNaN(d)) {
            return "×";
        } else if (d == (int) d) {
            return (int) d + "";
        }
        return d + "";
        //</editor-fold>
    }

    static String[][] ConvertirMatrizDoubleEnString(double[][] m) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        String[][] s = new String[m.length][m[0].length];
        for (int c = 0; c < m.length; c++) {
            for (int f = 0; f < m[0].length; f++) {
                s[c][f] = ConvertirDoubleEnString(m[c][f]);
            }
        }
        return s;
//</editor-fold>
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas y eventos fabricados con la GUI de Netbeans">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new Presentador();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        jTextField27 = new javax.swing.JTextField();
        jTextField28 = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jTextField30 = new javax.swing.JTextField();
        jTextField31 = new javax.swing.JTextField();
        jTextField32 = new javax.swing.JTextField();
        jTextField33 = new javax.swing.JTextField();
        jTextField34 = new javax.swing.JTextField();
        jTextField35 = new javax.swing.JTextField();
        jTextField36 = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jTextField37 = new javax.swing.JTextField();
        jTextField38 = new javax.swing.JTextField();
        jTextField39 = new javax.swing.JTextField();
        jTextField40 = new javax.swing.JTextField();
        jTextField41 = new javax.swing.JTextField();
        jTextField42 = new javax.swing.JTextField();
        jTextField43 = new javax.swing.JTextField();
        jTextField44 = new javax.swing.JTextField();
        jTextField45 = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jTextField46 = new javax.swing.JTextField();
        jTextField47 = new javax.swing.JTextField();
        jTextField48 = new javax.swing.JTextField();
        jTextField49 = new javax.swing.JTextField();
        jTextField50 = new javax.swing.JTextField();
        jTextField51 = new javax.swing.JTextField();
        jTextField52 = new javax.swing.JTextField();
        jTextField53 = new javax.swing.JTextField();
        jTextField54 = new javax.swing.JTextField();
        jTextField55 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jLabel69 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel40 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Dibujador de grafo");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jPanel1.setLayout(new java.awt.GridLayout(11, 11));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel2);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel3);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel4);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel5);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel6);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel7);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel8);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel9);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel10);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel11);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel12);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel13);

        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField1);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("4");
        jPanel1.add(jLabel14);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel15);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel16);

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel17);

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel18);

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel19);

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel20);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel21);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel22);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel23);

        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("4");
        jPanel1.add(jTextField2);

        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField3);

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("6");
        jPanel1.add(jLabel24);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("5");
        jPanel1.add(jLabel25);

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel26);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel27);

        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel28);

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel29);

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel30);

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel31);

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel32);

        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField4);

        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setText("6");
        jPanel1.add(jTextField5);

        jTextField6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField6);

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("3");
        jPanel1.add(jLabel33);

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel34);

        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel35);

        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel36);

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel37);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel38);

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel39);

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel41);

        jTextField7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField7);

        jTextField8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField8.setText("5");
        jPanel1.add(jTextField8);

        jTextField9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField9.setText("3");
        jPanel1.add(jTextField9);

        jTextField10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField10);

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel42);

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel43);

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel44);

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel45);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel46);

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel47);

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel48);

        jTextField11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField11);

        jTextField12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField12);

        jTextField13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField13);

        jTextField14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField14);

        jTextField15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField15);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel49);

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel50);

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel51);

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel52);

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel53);

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel54);

        jTextField16.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField16);

        jTextField17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField17);

        jTextField18.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField18);

        jTextField19.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField19);

        jTextField20.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField20);

        jTextField21.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField21);

        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel55);

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel56);

        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel57);

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel58);

        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel59);

        jTextField22.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField22);

        jTextField23.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField23);

        jTextField24.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField24);

        jTextField25.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField25);

        jTextField26.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField26);

        jTextField27.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField27);

        jTextField28.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField28);

        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel60);

        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel61);

        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel62);

        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel63);

        jTextField29.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField29);

        jTextField30.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField30);

        jTextField31.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField31);

        jTextField32.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField32);

        jTextField33.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField33);

        jTextField34.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField34);

        jTextField35.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField35);

        jTextField36.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField36);

        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel64);

        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel65);

        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel66);

        jTextField37.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField37);

        jTextField38.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField38);

        jTextField39.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField39);

        jTextField40.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField40);

        jTextField41.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField41);

        jTextField42.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField42);

        jTextField43.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField43);

        jTextField44.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField44);

        jTextField45.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField45);

        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel67);

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(jLabel68);

        jTextField46.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField46);

        jTextField47.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField47);

        jTextField48.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField48);

        jTextField49.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField49);

        jTextField50.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField50);

        jTextField51.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField51);

        jTextField52.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField52);

        jTextField53.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField53);

        jTextField54.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField54);

        jTextField55.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField55);

        jButton3.setText("Generar conexiones aleatorias");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel69.setText("Floyd-Warshall");
        jScrollPane2.setViewportView(jLabel69);

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("Matriz de costos mínimos");
        jScrollPane1.setViewportView(jLabel40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2))
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)))
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        int cantidadNodosMin = 4;
        int cantidadNodos = (int) Math.round((11 - cantidadNodosMin) * Math.random() + cantidadNodosMin);

        //<editor-fold defaultstate="collapsed" desc="Limpieza de las conexiones previas">
        for (int c = 1; c < 11; c++) {
            for (int f = 1; f < 11; f++) {
                //<editor-fold defaultstate="collapsed" desc="Evita que se modifique la diagonal principal">
                if (c == f) {
                    continue;
                }
                //</editor-fold>
                if (matrizAdyacencia_GUI[c][f] instanceof JTextField) {
                    JTextField a = (JTextField) matrizAdyacencia_GUI[c][f];
                    JLabel b = (JLabel) matrizAdyacencia_GUI[f][c];
                    a.setText("");
                    b.setText("");
                }
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Inserción de las nuevas conexiones">
        for (int c = 1; c < cantidadNodos; c++) {
            for (int f = 1; f < cantidadNodos; f++) {
                if (c == f) {
                    continue;
                }
                if (matrizAdyacencia_GUI[c][f] instanceof JTextField) {
                    JTextField a = (JTextField) matrizAdyacencia_GUI[c][f];
                    JLabel b = (JLabel) matrizAdyacencia_GUI[f][c];
                    if ((int) (Math.random() * 4) == 0) {
                        int costo = (int) (Math.random() * 10 + 1);
                        a.setText(costo + "");
                        b.setText(costo + "");
                    } else {
                        a.setText("");
                        b.setText("");
                    }
                }
            }
        }

        //</editor-fold>
        ActualizarGráfica();
        //</editor-fold>
    }//GEN-LAST:event_jButton3ActionPerformed

    //<editor-fold defaultstate="collapsed" desc="Delcaración de las variables para la GUI, hecho por Netbeans">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
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
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField44;
    private javax.swing.JTextField jTextField45;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField47;
    private javax.swing.JTextField jTextField48;
    private javax.swing.JTextField jTextField49;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField50;
    private javax.swing.JTextField jTextField51;
    private javax.swing.JTextField jTextField52;
    private javax.swing.JTextField jTextField53;
    private javax.swing.JTextField jTextField54;
    private javax.swing.JTextField jTextField55;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
    //</editor-fold>

}
