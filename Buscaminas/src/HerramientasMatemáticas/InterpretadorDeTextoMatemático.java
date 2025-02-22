package HerramientasMatemáticas;

import static HerramientaDeImagen.Filtros_Lineales.*;
import HerramientaDeImagen.GeneraciónDeTexto;
import java.util.ArrayList;
import static HerramientasMatemáticas.Matemática.*;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public final class InterpretadorDeTextoMatemático {

    InterpretadorDeTextoMatemático padre;
    ArrayList<Variable> VariablesDeclaradas = new ArrayList<>();
    ArrayList<FunciónDeRetonoNúmerico> funcionesDeclaradas = new ArrayList<>();

    private boolean GuardarTextoDeAnalisis = false;
    public String TextoDeAnalisis = null;

    boolean Simplificación = true;
    boolean MultiplicacionesImplicitas = true;

    public ArrayList<Expresiónes> Expresión = new ArrayList<>();

    //Elementos de dibujado del árbol binario
    public GeneraciónDeTexto generador;
    public Color colorBorde;
    public BasicStroke basicStroke;
    public int SeparaciónHorizontal = 20;
    public int SeparaciónVertical = 20;
    public boolean dibujarRamas = true;
    public boolean dibujarVertices = true;
    public boolean MostrarVariables = true;
    public boolean Bordear = true;
    public BufferedImage texturaVertices;

    public static void main(String[] args) {
        InterpretadorDeTextoMatemático c;
        try {
            long t = System.nanoTime();
            c = new InterpretadorDeTextoMatemático("x=4\n"
                    + "y=-2\n"
                    + "0-(-x-x)", true);
//            System.out.println(c.prueba());
////            c.AñadirVariable("x", 32);
            double demora = (System.nanoTime() - t) / 1000000000.0;
            System.out.println(c.ObtenerValorDeExpresión()[0] + "   " + demora);
            t = System.nanoTime();
            System.out.println(c.ObtenerValorDeExpresión()[0]);
            demora = (System.nanoTime() - t) / 1000000000.0;
            System.out.println(demora);
            t = System.nanoTime();
            System.out.println(c.ObtenerValorDeExpresión()[0]);
            demora = (System.nanoTime() - t) / 1000000000.0;
            System.out.println(demora);
            t = System.nanoTime();
            System.out.println(c.ObtenerValorDeExpresión()[0]);
            demora = (System.nanoTime() - t) / 1000000000.0;
            System.out.println(demora);
            t = System.nanoTime();
            System.out.println(c.ObtenerValorDeExpresión()[0]);
            demora = (System.nanoTime() - t) / 1000000000.0;
            System.out.println(demora);
//            c.ObtenerValorDeExpresión();
//            c.ObtenerValorDeExpresión();
//            System.out.println(c.TextoDeAnalisis);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n\nError al generar el analizador");
        }
    }

    public InterpretadorDeTextoMatemático(String Código, boolean ObligarPuntoYComa, boolean guardTxt) throws Exception {
        try {
            GuardarTextoDeAnalisis(guardTxt);
            CargarCódigo(Código, ObligarPuntoYComa);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public InterpretadorDeTextoMatemático(String Código, boolean ObligarPuntoYComa) throws Exception {
        this(Código, ObligarPuntoYComa, false);
    }

    public InterpretadorDeTextoMatemático(String Código) throws Exception {
        this(Código, true, false);
    }

    public InterpretadorDeTextoMatemático() {
    }

    public InterpretadorDeTextoMatemático CargarCódigo(String Código) throws Exception {
        return CargarCódigo(Código, false);
    }

    public InterpretadorDeTextoMatemático CargarCódigo(String Código, boolean ObligarUsoDePuntoYComa) throws Exception {
        if (GuardarTextoDeAnalisis) {
            AñadirTextoDeAnalisis("Se empezará con el analisis de:\n");
            AñadirTextoDeAnalisis(Código);
            AñadirTextoDeAnalisis("\n");
        }
        if (ObligarUsoDePuntoYComa) {
            AñadirTextoDeAnalisis("Eliminando todos los saltos de línea");
            Código = Código.replace("\n", "");
        } else {
            AñadirTextoDeAnalisis("Todos los saltos de línea serán reemplazados por punto y coma (;)");
            Código = Código.replace("\n", ";");
        }
        Código = Código.replace("$$", ";");
        Código = Código.replace(" es variable", " es var");
        Código = Código.replace(" is var", " es var");
        String sentencias[] = Código.split(";");
        for (int i = 0; i < sentencias.length; i++) {

            sentencias[i] = sentencias[i].trim();
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("\nAnalizando la sentencia: " + sentencias[i] + "\n");
            }
            if (sentencias[i].contains("=")) {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Se encontró un igual (=) esta sentencia es de asignación");
                }
                String sentencia[] = sentencias[i].split("=");
                String nombre = sentencia[0];
                nombre = nombre.trim();
                if (nombre.contains(" ")) {
                    throw new Exception("Las variables no pueden tener espacios");
                }
                NúmeroAbstracto valor = new Expresiónes(sentencia[1].replace(" ", ""));
                AñadirVariable(
                        nombre,
                        valor
                );
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis(
                            "\nVariable generada\nNombre: " + nombre
                            + "\nValor: " + valor.TextoGenerador + "\n"
                    );
                }
            } else if (sentencias[i].endsWith("es var")) {
                String nombre = sentencias[i].substring(0, sentencias[i].length() - 6);
                nombre = nombre.trim();
                if (nombre.contains(" ")) {
                    throw new Exception("Las variables no pueden tener espacios");
                }
                AñadirVariable(nombre, 0);
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("\nSe ha declarado una variable con el nombre: " + nombre + "\n");
                }
            } else {
                try {
                    añadirExpresión(LimpiarExpresiónDeExpresionesImplicitas(sentencias[i]));
                    if (GuardarTextoDeAnalisis) {
                        AñadirTextoDeAnalisis("\nSe ha generado una expresión de retorno con: " + sentencias[i] + "\n");
                    }
                } catch (Exception e) {
                    throw new Exception(e.getMessage());
                }
            }
        }
        return this;
    }

    public InterpretadorDeTextoMatemático añadirExpresión(String txtExpresión) throws Exception {
        txtExpresión = LimpiarExpresiónDeExpresionesImplicitas(txtExpresión);
        Expresión.add(new Expresiónes(txtExpresión));
        return this;
    }

    public InterpretadorDeTextoMatemático GuardarTextoDeAnalisis(boolean g) {
        GuardarTextoDeAnalisis = g;
        return this;
    }

    private void AñadirTextoDeAnalisis(String t) {
        if (GuardarTextoDeAnalisis) {
            if (TextoDeAnalisis == null) {
                TextoDeAnalisis = t;
                return;
            }
            TextoDeAnalisis += "\n" + t;
        }
    }

    public BufferedImage[] ImagenesÁrbolesBinarios() {
        boolean deponerColor = colorBorde == null;
        boolean deponerStroke = basicStroke == null;
        boolean deponerTextura = texturaVertices == null;
        boolean deponerGenerador = generador == null;
        if (deponerColor) {
            CargarColorPorDefectoDeDibujoDeÁrbol();
        }
        if (deponerStroke) {
            CargarStrokePorDefectoDeDibujoDeÁrbol();
        }
        if (deponerTextura) {
            CargarTexturaPorDefectoDeDibujoDeÁrbol();
        }
        if (deponerGenerador) {
            CargarGeneradorTextoPorDefectoDeDibujoDeÁrbol();
        }
        BufferedImage[] árboles = new BufferedImage[Expresión.size()];
        for (int i = 0; i < Expresión.size(); i++) {
            árboles[i] = Expresión.get(i).ImagenlÁrbolBinario();
        }
        if (deponerColor) {
            colorBorde = null;
        }
        if (deponerStroke) {
            basicStroke = null;
        }
        if (deponerTextura) {
            texturaVertices = null;
        }
        if (deponerGenerador) {
            generador = null;
        }
        return árboles;
    }

    public void DeponerElementosDeDibujoDeÁrbol() {
        colorBorde = null;
        basicStroke = null;
        texturaVertices = null;
        generador = null;
    }

    private void CargarColorPorDefectoDeDibujoDeÁrbol() {
        if (colorBorde == null) {
            colorBorde = new Color(0, 150, 250);
        }
    }

    private void CargarStrokePorDefectoDeDibujoDeÁrbol() {
        if (basicStroke == null) {
            basicStroke = new BasicStroke(4);
        }
    }

    private void CargarGeneradorTextoPorDefectoDeDibujoDeÁrbol() {
        if (generador == null) {
            generador = new GeneraciónDeTexto();
        }
    }

    private void CargarTexturaPorDefectoDeDibujoDeÁrbol() {
        if (texturaVertices == null) {
            texturaVertices = new BufferedImage(90, 90, 2) {
                {
                    Graphics2D g = createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setColor(Color.WHITE);
                    g.fillOval(0, 0, getWidth(), getHeight());
                    if (Bordear) {
                        g.setColor(colorBorde);
                        g.setStroke(basicStroke);
                        g.setComposite(AlphaComposite.SrcAtop);
                        g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                    }
                }
            };
        }
    }

    public double[] ObtenerValorDeExpresión() throws Exception {
        if (Expresión.isEmpty()) {
            throw new Exception("Faltan expresiones de retorno");
        }
        double retorno[] = new double[Expresión.size()];
        for (int i = 0; i < retorno.length; i++) {
            try {
                long tiempo = System.nanoTime();
                retorno[i] = Expresión.get(i).ObtenerValor();
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("\nResultado encontrado: "
                            + retorno[i] + "\nde: " + Expresión.get(i).TextoGenerador
                    );
                }
                double t = (System.nanoTime() - tiempo) / 1000000000.0;
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis(t + " Segundos\n");
                }
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Tiempo en interpretar: " + EliminarNotaciónCientifica(t) + " Segundos");
                }
            } catch (Exception e) {
                throw new Exception("No se pudo interpretar la expresión \n" + e.getMessage());
            }
        }
        return retorno;
    }

    public void EliminarVariable(String nombre) {
        try {
            VariablesDeclaradas.remove(ObtenerVariable(nombre));
        } catch (Exception ex) {
        }
    }

    public void AñadirVariable(String nombre, double Valor) {
        for (Variable variable1 : VariablesDeclaradas) {
            if (variable1.nombre.equals(nombre)) {
                variable1.valor = Valor;
                return;
            }
        }
        VariablesDeclaradas.add(new Variable(nombre, Valor));
    }

    public void AñadirVariable(String nombre, NúmeroAbstracto Valor) {
        for (Variable variable1 : VariablesDeclaradas) {
            if (variable1.nombre.equals(nombre)) {
                variable1.valor = Valor;
                return;
            }
        }
        VariablesDeclaradas.add(new Variable(nombre, Valor));
    }

    public boolean ExisteVariable(String nombre) {
        try {
            ObtenerVariable(nombre);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Variable ObtenerVariable(String nombre) throws Exception {
        for (Variable VariableDeclarada : VariablesDeclaradas) {
            if (VariableDeclarada.nombre.equals(nombre)) {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("\nLa variable se encontró en las variables anteriormente añadidas\n");
                }
                return VariableDeclarada;
            }
        }
        if (padre != null) {
            try {
                return padre.ObtenerVariable(nombre);
            } catch (Exception e) {
            }
        }
        if (GuardarTextoDeAnalisis) {
            AñadirTextoDeAnalisis("No hay ninguna variable cargada que tenga el nombre: " + nombre);
            AñadirTextoDeAnalisis("Se buscará en la biblioteca\n");
        }
        try {
            Variable var = BibliotecaDeVariables(nombre);
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("\nLa variable se encontró en la biblioteca de variables\n");
            }
            return var;
        } catch (Exception e) {
        }
        throw new Exception("La variable " + nombre + " no ha sido encontrada\n");
    }

    public void AñadirFunción(FunciónDeRetonoNúmerico f) throws Exception {
        for (FunciónDeRetonoNúmerico funcionDeclarada : funcionesDeclaradas) {
            if (funcionDeclarada.nombre.equals(f.nombre)) {
                return;
            }
        }
        if (f.nombre.contains("+") || f.nombre.contains("-") || f.nombre.contains("/") || f.nombre.contains("^")) {
            throw new Exception("La función añadida tiene en el nombre operadores binarios");
        }
        funcionesDeclaradas.add(f);
        if (GuardarTextoDeAnalisis) {
            AñadirTextoDeAnalisis("\nFunción cargada: " + f.nombre);
        }
    }

    public FunciónDeRetonoNúmerico ObtenerFunción(String nombre) throws Exception {
        for (FunciónDeRetonoNúmerico funcionDeclarada : funcionesDeclaradas) {
            if (funcionDeclarada.nombre.equals(nombre)) {
                return funcionDeclarada;
            }
        }
        throw new Exception("La función " + nombre + " no ha sido encontrada");
    }

    public String MultiplicacionesImplicitas(String txtExpresión) {
        if (!MultiplicacionesImplicitas) {
            return txtExpresión;
        }
        for (Variable VariablesDeclarada : VariablesDeclaradas) {
            String n = VariablesDeclarada.nombre;
            txtExpresión = txtExpresión.replace(")" + n, ")*" + n);
        }
        for (int i = 0; i < 10; i++) {
            txtExpresión = txtExpresión.replace(")" + i, ")*" + i);
        }
        return txtExpresión;
    }

    public String LimpiarExpresiónDeExpresionesImplicitas(String txtExpresión) {
        //Unificación de signos de agrupación
        txtExpresión = txtExpresión.replace("[", "(");
        txtExpresión = txtExpresión.replace("]", ")");
        txtExpresión = txtExpresión.replace(" ", "");//Eliminar espacios
        txtExpresión = txtExpresión.replace("\n", "");//Eliminar saltos de linea
        txtExpresión = MultiplicacionesImplicitas(txtExpresión);
        //Para las multiplicaciones implicitas
        txtExpresión = txtExpresión.replace(")(", ")*(");
        {
            //Eliminar posible ambiguedad de multiplicación de signos
            int intentos = 0;
            boolean ambiguedad = false;
            do {
                txtExpresión = txtExpresión.replace("--", "+");
                txtExpresión = txtExpresión.replace("++", "+");
                txtExpresión = txtExpresión.replace("+-", "-");
                txtExpresión = txtExpresión.replace("-+", "-");
                ambiguedad = false;
                ambiguedad |= txtExpresión.contains("++");
                ambiguedad |= txtExpresión.contains("--");
                ambiguedad |= txtExpresión.contains("-+");
                ambiguedad |= txtExpresión.contains("+-");
                intentos++;
            } while (ambiguedad && intentos < 15);
        }
        txtExpresión = txtExpresión.replace("½", "0.5");//Reemplaza el caracter ½ por su valor decimal
        txtExpresión = txtExpresión.replace("¼", "0.25");//Reemplaza el caracter ¼ por su valor decimal
        txtExpresión = txtExpresión.replace("¾", "0.75");//Reemplaza el caracter ¾ por su valor decimal
        txtExpresión = txtExpresión.replace("¹", "");//Elevar un numero a la 1 da como resultado el mismo numero
        txtExpresión = txtExpresión.replace("²", "^2");//Reemplaza el caracter ² por su operación correspondiente
        txtExpresión = txtExpresión.replace("³", "^3");//Reemplaza el caracter ³ por su operación correspondiente
        txtExpresión = txtExpresión.replace("×", "*");//Reemplaza el caracter × por su operación correspondiente
        txtExpresión = txtExpresión.replace("÷", "/");//Reemplaza el caracter ÷ por su operación correspondiente
        txtExpresión = txtExpresión.replace("√", "raiz2");//Reemplaza el caracter ÷ por su operación correspondiente
        return txtExpresión;
    }

    public interface ExpresiónDeRetorno {

        public double ObtenerValor() throws Exception;
    }

    public abstract class NúmeroAbstracto extends Number implements ExpresiónDeRetorno {

        Number Número;
        String TextoGenerador;

        abstract BufferedImage ImagenlÁrbolBinario();

        boolean esVariable() {
            try {
                ObtenerVariable(TextoGenerador);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        boolean esNúmero() {
            try {
                Double.parseDouble(TextoGenerador);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public boolean Descartado() {
            return TextoGenerador.equals("inutil");
        }

        public NúmeroAbstracto Descartar() {
            NúmeroAbstracto e;
            try {
                e = new NúmeroAbstracto() {
                    @Override
                    BufferedImage ImagenlÁrbolBinario() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public double ObtenerValor() throws Exception {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                };
            } catch (Exception ex) {
                throw new RuntimeException("No se pudo generar el termino inutil");
            }
            e.TextoGenerador = "inutil";
            return e;
        }

        Dupla calcularPosImg(BufferedImage img, Dupla pos) {
            return pos.Clon().Sustraer(new Dupla(img).Mitad());
        }

        @Override
        public int intValue() {
            try {
                return Redondeo(ObtenerValor());
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }

        @Override
        public long longValue() {
            try {
                return (long) ObtenerValor();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }

        @Override
        public float floatValue() {
            try {
                return (float) ObtenerValor();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }

        @Override
        public double doubleValue() {
            try {
                return ObtenerValor();
            } catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    public static NúmeroAbstracto ConvNA(Number n) {
        return ((NúmeroAbstracto) n);
    }

    public class Expresiónes extends NúmeroAbstracto {

        public Expresiónes(double n) throws Exception {
            this(n + "");
        }

        public Expresiónes(String s) throws Exception {
            TextoGenerador = s;
            if ("".equals(s)) {
                throw new Exception("No se ha recibido ninguna expresión");
            }
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Generando expresión algebraica con: " + s);
            }
            try {
                Número = Double.parseDouble(s);
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("\n" + s + " es un número\n");
                }
                return;
            } catch (Exception e) {
                try {
                    if (GuardarTextoDeAnalisis) {
                        AñadirTextoDeAnalisis("\nSe comprobará si " + s + " es una variable\n");
                    }
                    Número = ObtenerVariable(s);
                    if (GuardarTextoDeAnalisis) {
                        AñadirTextoDeAnalisis("\n" + s + " es una variable\n");
                    }
                    return;
                } catch (Exception e2) {
                    if (s.length() <= 2) {
                        throw new Exception("No se reconoce: " + s);
                    }
                    if (GuardarTextoDeAnalisis) {
                        AñadirTextoDeAnalisis("\n" + s + " NO es una variable\n");
                    }
                }
            }

            char[] operadores = {'+', '-', '/', '*', '^', '&', '|'};
            for (char operador : operadores) {
                if (s.contains(operador + "")) {
                    try {
                        if (GuardarTextoDeAnalisis) {
                            AñadirTextoDeAnalisis("\nSe comprobará si " + s + " es operación binaria tipo (" + operador + ")\n");
                        }
                        OperaciónBinaria op = new OperaciónBinaria(s, operador);
                        try {
                            if (ConvNA(op.Cuerpo).Descartado()) {
                                TextoGenerador = ConvNA(op.Número).TextoGenerador + "";
                                if (!s.equals(ConvNA(op.Número).TextoGenerador)) {
                                    Número = new Expresiónes(ConvNA(op.Número).TextoGenerador);
                                    return;
                                }
                                Número = op.Número;
                                if (GuardarTextoDeAnalisis) {
                                    AñadirTextoDeAnalisis("Se simplificó a: " + Número.doubleValue());
                                    AñadirTextoDeAnalisis("Se descompuso " + s + " en operaciones binarias " + TextoGenerador);
                                }
                                return;
                            }
                        } catch (Exception e) {
                            if (ElementosOperaciónBinaria(s, operador) > 1) {
                                throw new Exception("Problema separando " + s + " en operación binaria tipo (" + operador + ")");
                            }
                        }
                        TextoGenerador = op.TextoGenerador;
                        if (!s.equals(op.TextoGenerador)) {
                            Número = new Expresiónes(op.TextoGenerador);
                            return;
                        }
                        Número = op;
                        if (GuardarTextoDeAnalisis) {
                            AñadirTextoDeAnalisis("Se descompuso " + s + " en operaciones binarias " + TextoGenerador);
                        }
                        return;
                    } catch (FunciónNoReconocida e) {
                        throw new Exception(e.getMessage());
                    } catch (Exception e) {
                        if (GuardarTextoDeAnalisis) {
                            AñadirTextoDeAnalisis("\n" + s + " NO es operación binaria tipo (" + operador + ")\n");
                        }
                    }
                }
            }
            try {
                Prefijos p = new Prefijos(s);
                if (!s.equals(p.TextoGenerador)) {
                    Número = new Expresiónes(p.TextoGenerador);
                    return;
                }
                Número = p.Número;
                TextoGenerador = p.TextoGenerador;
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Se encontró un operador como prefijo");
                }
                return;
            } catch (Exception e) {
            }
            try {
                Sufijos su = new Sufijos(s);
                Número = su.Número;
                TextoGenerador = su.TextoGenerador;
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Se encontró un operador como sufijo");
                }
                return;
            } catch (Exception e) {
            }
            if (s.contains("(") && s.endsWith(")")) {
                try {
                    FunciónAnalizadora f = new FunciónAnalizadora(s);
                    if (GuardarTextoDeAnalisis) {
                        AñadirTextoDeAnalisis("Se determinó que " + s + " es una función");
                    }
                    TextoGenerador = f.TextoGenerador;
                    if (!s.equals(f.TextoGenerador)) {
                        if (GuardarTextoDeAnalisis) {
                            AñadirTextoDeAnalisis("La función indica que hay que redefinir el número");
                            AñadirTextoDeAnalisis("de: " + s + " con: " + f.TextoGenerador);
                        }
                        Número = new Expresiónes(f.TextoGenerador);
                    } else {
                        Número = f;
                    }
                    return;
                } catch (Exception e2) {
                    throw new Exception(e2.getMessage() + " Problema con: " + s);
                }
            }
            throw new Exception("La Expresión no se puede interpretar " + s);
        }

        @Override
        public double ObtenerValor() throws Exception {
            try {
                return Número.doubleValue();
            } catch (Exception e) {
                throw new Exception("No se pudo determinar el valor de la expresión\n" + e.getMessage());
            }
        }

        @Override
        BufferedImage ImagenlÁrbolBinario() {
            BufferedImage img = null;
            try {
                double n = Double.parseDouble(TextoGenerador);
                img = generador.GenerarTexto(recortarDecimales(n) + "");
            } catch (Exception e) {
            }
            if (Número instanceof Variable) {
                if (MostrarVariables) {
                    img = generador.GenerarTexto(((Variable) Número).nombre);
                } else {
                    img = generador.GenerarTexto(recortarDecimales(((Variable) Número).ObtenerValor()) + "");
                }
            }
            if (img == null) {
                return ((NúmeroAbstracto) Número).ImagenlÁrbolBinario();
            }
            BufferedImage retorno = Dupla.convBufferedImage(texturaVertices);
            if (dibujarVertices) {
                retorno = Clonar(texturaVertices);
                Graphics2D g = retorno.createGraphics();
                Ajuste_Personalizado(img, retorno, AJUSTE_CENTRADO_AJUSTAR);
            }
            return retorno;
        }
    }

    int ElementosOperaciónBinaria(String s, char simbolo) {
        return ExtraerSegúnSimboloRespetandoAgrupaciones(s, simbolo).length;
    }

    boolean TieneOperaciónBinaria(String s, char simbolo) {
        return ElementosOperaciónBinaria(s, simbolo) > 1;
    }

    String[] ExtraerSegúnSimboloRespetandoAgrupaciones(String s, char Simbolo) {
        //Expresiones con agrupaciones
        if (!s.contains(Simbolo + "")) {
            return new String[]{s};
        }
        ArrayList<String> tms = new ArrayList<>();
        int ObstaculosEncontrados = 1;
        int BuscaExt = 0;
        int últimaExt = 0;
        //extraer sumas
        while (BuscaExt < s.length()) {
            char c = s.charAt(BuscaExt++);
            boolean simboloEncontrado = c == Simbolo;
            if (simboloEncontrado && ObstaculosEncontrados == 1 && BuscaExt > 1) {
                //momento de extraer término
                String Sub = s.substring(últimaExt, BuscaExt - 1);
                últimaExt = BuscaExt;
                tms.add(Sub);
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Término extraido " + Sub + " operación: (" + Simbolo + ")");
                }
            } else if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis(c + "");
            }
            if (c == '(') {
                ObstaculosEncontrados++;
            } else if (c == ')') {
                ObstaculosEncontrados--;
            }
        }
        {
            String Sub = s.substring(últimaExt, BuscaExt);
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Término extraido " + Sub + " operación: (" + Simbolo + ")");
            }
            tms.add(Sub);
        }
        String c[] = new String[tms.size()];
        for (int i = 0; i < c.length; i++) {
            c[i] = tms.get(i);
        }
        return c;
    }

    public class Prefijos extends NúmeroAbstracto {

        public Prefijos(String n) throws Exception {
            TextoGenerador = n;
            char operador = n.charAt(0);
            String cabeza = n.substring(1, n.length());
            NúmeroAbstracto núm;
            switch (operador) {
                case '+':
                    núm = new Expresiónes(cabeza);
                    break;
                case '-':
                    núm = new Expresiónes("-1*" + cabeza);
                    break;
                case '~':
                case '!':
                    núm = new FunciónAnalizadora("no(" + cabeza + ")");
                    break;
                default:
                    throw new Exception("No se encontró prefijo");
            }
            Número = núm;
            TextoGenerador = núm.TextoGenerador;
        }

        @Override
        public double ObtenerValor() throws Exception {
            return Número.doubleValue();
        }

        @Override
        BufferedImage ImagenlÁrbolBinario() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    public class Sufijos extends NúmeroAbstracto {

        public Sufijos(String n) throws Exception {
            TextoGenerador = n;
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Buscar sufijo en: " + n);
            }
            char operador = n.charAt(n.length() - 1);
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Sufijo: " + operador);
            }
            String cuerpo = n.substring(0, n.length() - 1);
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Cuerpo: " + cuerpo);
            }
            NúmeroAbstracto núm;
            switch (operador) {
                case '!':
                    núm = new FunciónAnalizadora("fact(" + cuerpo + ")");
                    break;
                case '%':
                    núm = new Expresiónes(cuerpo + "/100");
                    break;
                default:
                    throw new Exception("No se encontró prefijo");
            }
            Número = núm;
            TextoGenerador = núm.TextoGenerador;
        }

        @Override
        public double ObtenerValor() throws Exception {
            return Número.doubleValue();
        }

        @Override
        BufferedImage ImagenlÁrbolBinario() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    public final class OperaciónBinaria extends NúmeroAbstracto {

        char simboloOperador;
        Number Cuerpo;

        public OperaciónBinaria(Number numerador, Number denominador, char op) {
            this.Número = numerador;
            this.Cuerpo = denominador;
            simboloOperador = op;
        }

        Comparator<String> comparadorTérminos() {
            return (String o1, String o2) -> {
                String[] e1 = ExtraerSegúnSimboloRespetandoAgrupaciones(o1, '*');
                String[] e2 = ExtraerSegúnSimboloRespetandoAgrupaciones(o2, '*');
                String a;
                try {
                    Double.parseDouble(e1[0]);
                    if (e1.length == 1) {
                        return 1;
                    } else {
                        a = e1[1];
                    }
                } catch (Exception e) {
                    a = e1[0];
                }
                String a2;
                try {
                    Double.parseDouble(e2[0]);
                    if (e2.length == 1) {
                        return -1;
                    } else {
                        a2 = e2[1];
                    }
                } catch (Exception e) {
                    a2 = e2[0];
                }
                return a.compareTo(a2);
            };
        }

        void Organizar(String[] partes) {
            if (Simplificación && (simboloOperador == '+' || simboloOperador == '*' || simboloOperador == '-')) {
                if (simboloOperador == '-') {
                    String ordenables[] = new String[partes.length - 1];
                    for (int i = 1; i < partes.length; i++) {
                        ordenables[i - 1] = partes[i];
                    }
                    Arrays.sort(ordenables, comparadorTérminos());
                    for (int i = 1; i < partes.length; i++) {
                        partes[i] = ordenables[i - 1];
                    }
                }
            } else if (simboloOperador == '+') {
                Arrays.sort(partes, comparadorTérminos());
            }
        }

        public OperaciónBinaria(String n, char operador) throws Exception {
            TextoGenerador = n;
            simboloOperador = operador;
            if (!n.contains(operador + "")) {
                throw new Exception("No existe esta operación (" + operador + ") en la expresión: " + n);
            }
            String[] partes = ExtraerSegúnSimboloRespetandoAgrupaciones(n, operador);
            {
                String añadir = "";
                for (int i = 0; i < partes.length; i++) {
                    if (i == partes.length - 1) {
                        añadir += partes[i];
                    } else {
                        añadir += partes[i] + ", ";
                    }
                }
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Se extrajeron los elementos: " + añadir);
                }
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Hay: " + partes.length + " Elementos");
                }
            }
            Organizar(partes);
            switch (partes.length) {
                case 0:
                case 1:
                    throw new Exception("Se necesitan al menos 2 términos para efectuar la operación");
                case 2:
                    try {
                        NúmeroAbstracto n1 = new Expresiónes(partes[0]);
                        NúmeroAbstracto n2 = new Expresiónes(partes[1]);
                        Número = n1;
                        Cuerpo = n2;
                        String r = IdentidadesBinarias(n1, n2, true);
                        if (!r.isEmpty()) {
                            n1 = new Expresiónes(r);
                            TextoGenerador = n1.TextoGenerador;
                            if (GuardarTextoDeAnalisis) {
                                AñadirTextoDeAnalisis("Simplificado a: " + TextoGenerador);
                            }
                            Número = n1;
                            Cuerpo = Descartar();
                        } else {
                            TextoGenerador = ConvNA(Número).TextoGenerador + simboloOperador + ConvNA(Cuerpo).TextoGenerador;
                            if (GuardarTextoDeAnalisis) {
                                AñadirTextoDeAnalisis(TextoGenerador + " Analizado");
                            }
                        }
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                    break;
                default:
                    ArrayList<String> reducciones = new ArrayList<>();
                    while (true) {
                        for (int i = 0; i < partes.length; i++) {
                            String r = "";
                            NúmeroAbstracto n1 = new Expresiónes(partes[i]);
                            try {
                                NúmeroAbstracto n2 = new Expresiónes(partes[i + 1]);
                                r = IdentidadesBinarias(
                                        n1, n2, i == 0
                                );
                            } catch (Exception e) {
                                AñadirTextoDeAnalisis("Operación impar (número de términos)");
                            }
                            if (!r.isEmpty()) {
                                i++;
                                reducciones.add(new Expresiónes(r).TextoGenerador);
                            } else {
                                reducciones.add(n1.TextoGenerador);
                            }
                        }
                        if (partes.length > reducciones.size()) {
                            TextoGenerador = "";
                            if (Simplificación && (simboloOperador == '+' || simboloOperador == '*' || simboloOperador == '-')) {
                                if (simboloOperador == '-') {
                                    ArrayList<String> ordenables = new ArrayList();
                                    for (int i = 1; i < reducciones.size(); i++) {
                                        ordenables.add(reducciones.get(i));
                                    }
                                    Collections.sort(ordenables);
                                    for (int i = 1; i < reducciones.size(); i++) {
                                        reducciones.set(i, ordenables.get(i - 1));
                                    }
                                } else {
                                    Collections.sort(reducciones, comparadorTérminos());
                                }
                            }
                            partes = new String[reducciones.size()];
                            for (int i = 0; i < reducciones.size(); i++) {
                                partes[i] = reducciones.get(i);
                                TextoGenerador += partes[i] + (i == reducciones.size() - 1 ? "" : simboloOperador);
                            }
                            if (GuardarTextoDeAnalisis) {
                                AñadirTextoDeAnalisis("Simplificado a: " + TextoGenerador);
                            }
                            reducciones.clear();
                        } else {
                            break;
                        }
                    }
                    Número = new Expresiónes(partes[0]);
                    Cuerpo = Descartar();
                    for (int i = 1; i < partes.length; i++) {
                        if (i == partes.length - 1) {
                            Cuerpo = new Expresiónes(partes[i]);
                            String r = IdentidadesBinarias(
                                    ConvNA(Número),
                                    ConvNA(Cuerpo), i == 0
                            );
                            if (!r.isEmpty()) {
                                Número = new Expresiónes(r);
                                Cuerpo = Descartar();
                            }
                        } else {
                            try {
                                Número = new Expresiónes(ConvNA(Número).TextoGenerador + simboloOperador + new Expresiónes(partes[i]).TextoGenerador);
                            } catch (Exception e) {
                                throw new Exception(e.getMessage());
                            }
                        }
                    }
                    break;
            }
        }

        double operar(double a, double b) {
            switch (simboloOperador) {
                case '+':
                    return a + b;
                case '-':
                    return a - b;
                case '*':
                    return a * b;
                case '/':
                    return a / b;
                case '|':
                    return (int) a | (int) b;
                case '&':
                    return (int) a & (int) b;
                case '^':
                    return poten(a, b);
            }
            return -1;
        }

        @Override
        public double ObtenerValor() throws Exception {
            return operar(Número.doubleValue(), Cuerpo.doubleValue());
        }

        @Override
        BufferedImage ImagenlÁrbolBinario() {
            BufferedImage op = Clonar(texturaVertices);
            Ajuste_Centrado_Ajustar(generador.GenerarTexto(simboloOperador + ""), op);
            BufferedImage arg1 = ((NúmeroAbstracto) Número).ImagenlÁrbolBinario();
            BufferedImage arg2 = ((NúmeroAbstracto) Cuerpo).ImagenlÁrbolBinario();
            BufferedImage retorno = new BufferedImage(
                    arg1.getWidth() + arg2.getWidth() + SeparaciónHorizontal,
                    Máx(arg1.getHeight(), arg2.getHeight()) + op.getHeight() + SeparaciónVertical,
                    2
            );
            Graphics2D g = retorno.createGraphics();
            Dupla PosOP = Dupla.Alinear(retorno, op, Dupla.MEDIO, Dupla.ARRIBA);
            Dupla PosArg1 = Dupla.Alinear(retorno, arg1, Dupla.IZQUIERDA, Dupla.ABAJO);
            Dupla PosArg2 = Dupla.Alinear(retorno, arg2, Dupla.DERECHA, Dupla.ABAJO);
            if (dibujarRamas) {
                g.setStroke(basicStroke);
                g.setColor(colorBorde);
                g.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );
                int y = new Dupla(texturaVertices).Mitad().intY();
                Dupla pt1 = PosOP.Clon().Adicionar(y);
                Dupla pt2 = PosArg1.Clon().Adicionar(new Dupla(arg1).Mitad().X, y);
                Dupla pt3 = PosArg2.Clon().Adicionar(new Dupla(arg2).Mitad().X, y);
                g.drawLine(pt1.intX(), pt1.intY(), pt2.intX(), pt2.intY());
                g.drawLine(pt1.intX(), pt1.intY(), pt3.intX(), pt3.intY());
            }
            if (dibujarVertices) {
                g.drawImage(op, PosOP.intX(), PosOP.intY(), null);
            }
            g.drawImage(arg1, PosArg1.intX(), PosArg1.intY(), null);
            g.drawImage(arg2, PosArg2.intX(), PosArg2.intY(), null);
            return retorno;
        }

        boolean ParentesisDestruidos(NúmeroAbstracto n1) {
            String txt = n1.TextoGenerador;
            if (txt.startsWith("(") && txt.endsWith(")")) {
                int ObstaculoEncontrado = 0;
                for (char c : txt.toCharArray()) {
                    if (ObstaculoEncontrado < 0) {
                        return false;
                    }
                    if (c == '(') {
                        ObstaculoEncontrado++;
                    }
                    if (c == ')') {
                        ObstaculoEncontrado--;
                    }
                }
                switch (simboloOperador) {
                    case '+':
                        n1.TextoGenerador = txt.substring(1, txt.length() - 1);
                        return true;
                    case '-':
                        n1.TextoGenerador = (txt.startsWith("(-") ? "" : "+") + txt.substring(1, txt.length() - 1);
                        txt = n1.TextoGenerador;
                        ObstaculoEncontrado = 0;
                        for (int i = 0; i < txt.length(); i++) {
                            char c = txt.charAt(i);
                            if (ObstaculoEncontrado == 0) {
                                if (c == '+') {
                                    char[] copia = txt.toCharArray();
                                    copia[i] = '-';
                                    n1.TextoGenerador = new String(copia);
                                }
                                if (c == '-') {
                                    char[] copia = txt.toCharArray();
                                    copia[i] = '+';
                                    n1.TextoGenerador = new String(copia);
                                }
                                txt = n1.TextoGenerador;
                            }
                            if (c == '(') {
                                ObstaculoEncontrado++;
                            }
                            if (c == ')') {
                                ObstaculoEncontrado--;
                            }
                        }
                        return true;
                }
            }
            return false;
        }

        String IdentidadesBinarias(NúmeroAbstracto n1, NúmeroAbstracto n2, boolean primerOrden) throws Exception {
            if (!Simplificación) {
                return "";
            }
            boolean b1 = ParentesisDestruidos(n1);
            boolean b2 = ParentesisDestruidos(n2);
            if (n1.esNúmero() && n2.esNúmero()) {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Retorno de operación númerica");
                }
                return operar(n1.doubleValue(), n2.doubleValue()) + "";
            } else if (n2.esNúmero()) {
                switch (simboloOperador) {
                    case '+':
                    case '*':
                        //x+1->1+x & x*2 -> 2*x número de primero
                        NúmeroAbstracto ntemp = n1;
                        n1 = n2;
                        n2 = ntemp;
                        break;
                }
            }
            switch (simboloOperador) {
                case '^':
                    //x^1=x
                    if (n2.esNúmero() && n2.ObtenerValor() == 1) {
                        return n1.TextoGenerador;
                    }
                    //x^0=1, x difrente 0
                    if (n2.esNúmero() && n2.ObtenerValor() == 0) {
                        if (n1.ObtenerValor() == 0) {
                            return Double.NaN + "";
                        } else {
                            return "1";
                        }
                    }
                    break;
                case '*':
                    //x*x = x^2
                    if (n1.TextoGenerador.equals(n2.TextoGenerador)) {
                        return n1.TextoGenerador + '^' + 2;
                    } else if (ElementosOperaciónBinaria(n2.TextoGenerador, '^') <= 2 || ElementosOperaciónBinaria(n1.TextoGenerador, '^') <= 2) {
                        //x^n*x^m = x^(m+n)
                        String T1[] = ExtraerSegúnSimboloRespetandoAgrupaciones(n1.TextoGenerador, '^');
                        String T2[] = ExtraerSegúnSimboloRespetandoAgrupaciones(n2.TextoGenerador, '^');
                        if (T1[0].equals(T2[0])) {
                            String e1 = T1.length == 1 ? 1 + "" : n1.TextoGenerador.substring(n1.TextoGenerador.indexOf('^') + 1);
                            String e2 = T2.length == 1 ? 1 + "" : n2.TextoGenerador.substring(n2.TextoGenerador.indexOf('^') + 1);
                            return new Expresiónes(T1[0] + "^" + new Expresiónes("(" + e1 + "+" + e2 + ")").TextoGenerador).TextoGenerador;
                        }
                    }
                    // 0*x = 0
                    if (n1.esNúmero() && n1.doubleValue() == 0) {
                        return "0";
                    }
                    // 1*x = x
                    if (n1.esNúmero() && n1.doubleValue() == 1) {
                        return n2.TextoGenerador;
                    }
                    break;
                case '/': //eliminar convertir divisiones
                    if (n2.esNúmero() && n2.doubleValue() == 0) {// x/0 = ∞, si x = 0 entonces NaN
                        if (n1.doubleValue() == 0) {
                            return Double.NaN + "";
                        } else {
                            return Double.POSITIVE_INFINITY + "";
                        }
                    } else if (n2.esNúmero() && n2.doubleValue() == 1) {// x/1=x
                        return n1.TextoGenerador;
                    } else if (n1.esNúmero() && n1.doubleValue() == 0) { // 0/x = 0
                        if (n2.ObtenerValor() == 0) {
                            return Double.NaN + "";
                        }
                        return "0";
                    } else if (n1.TextoGenerador.equals(n2.TextoGenerador)) {//x/x = 1
                        if (n2.ObtenerValor() == 0) {
                            return Double.NaN + "";
                        }
                        return "1";
                    }
                    int e1 = ElementosOperaciónBinaria(n1.TextoGenerador, '+');
                    e1 += ElementosOperaciónBinaria(n1.TextoGenerador, '-');
                    e1 += ElementosOperaciónBinaria(n1.TextoGenerador, '/');
                    int e2 = ElementosOperaciónBinaria(n2.TextoGenerador, '+');
                    e2 += ElementosOperaciónBinaria(n2.TextoGenerador, '-');
                    e2 += ElementosOperaciónBinaria(n2.TextoGenerador, '/');
                    if (e1 == 3 && e2 == 3) {
                        String[] m1 = ExtraerSegúnSimboloRespetandoAgrupaciones(n1.TextoGenerador, '*');
                        ArrayList<String> a1 = new ArrayList();
                        for (String string : m1) {
                            a1.add(string);
                        }
                        String[] m2 = ExtraerSegúnSimboloRespetandoAgrupaciones(n2.TextoGenerador, '*');
                        ArrayList<String> a2 = new ArrayList();
                        for (String string : m2) {
                            a2.add(string);
                        }
                        ArrayList<String> nuevos = new ArrayList();
                        for (int i = 0; i < a1.size(); i++) {
                            String string = a1.get(i);
                            for (int j = 0; j < a2.size(); j++) {
                                String string1 = a2.get(j);
                                if (string.equals(string1)) {
                                    a1.remove(string);
                                    a2.remove(string1);
                                    break;
                                } else {
                                    String[] p1 = ExtraerSegúnSimboloRespetandoAgrupaciones(string, '^');
                                    String[] p2 = ExtraerSegúnSimboloRespetandoAgrupaciones(string1, '^');
                                    if (p1.length <= 2 && p2.length <= 2) {
                                        if (p1[0].equals(p2[0])) {
                                            a1.remove(string);
                                            a2.remove(string1);
                                            String T1 = p1.length == 1 ? 1 + "" : p1[1];
                                            String T2 = p2.length == 1 ? 1 + "" : p2[1];
                                            String exp = new Expresiónes("(" + T1 + "-" + T2 + ")").TextoGenerador;
                                            nuevos.add(p1[0] + "^" + exp);
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        for (String nuevo : nuevos) {
                            a1.add(nuevo);
                        }
                        Collections.sort(a1);
                        String n = "";
                        for (int i = 0; i < a1.size(); i++) {
                            n += a1.get(i) + (i == a1.size() - 1 ? "" : "*");
                        }
                        if (a2.isEmpty()) {
                            return n;
                        } else {
                            String d = "";
                            for (int i = 0; i < a2.size(); i++) {
                                d += a2.get(i) + (i == a2.size() - 1 ? "" : "*");
                            }
                            System.out.println(n + "/" + d);
                            return n + "/" + d;
                        }
                    }
                    break;
                case '+':
                    //x+x = 2*x
                    if (n1.TextoGenerador.equals(n2.TextoGenerador) && n1.esVariable()) {
                        return 2 + "*" + n1.TextoGenerador;
                    } else if (ElementosOperaciónBinaria(n2.TextoGenerador, '*') <= 2 || ElementosOperaciónBinaria(n1.TextoGenerador, '*') <= 2) {
                        //m*x+n*x = (m+n)*x
                        String T1[] = ExtraerSegúnSimboloRespetandoAgrupaciones(n1.TextoGenerador, '*');
                        String T2[] = ExtraerSegúnSimboloRespetandoAgrupaciones(n2.TextoGenerador, '*');
                        if (T1[T1.length - 1].equals(T2[T2.length - 1])) {
                            String es1 = T1.length == 1 ? 1 + "" : n1.TextoGenerador.substring(0, n1.TextoGenerador.lastIndexOf("*"));
                            String es2 = T2.length == 1 ? 1 + "" : n2.TextoGenerador.substring(0, n2.TextoGenerador.lastIndexOf("*"));
                            return new Expresiónes("(" + es1 + simboloOperador + es2 + ")").TextoGenerador + "*" + T1[T1.length - 1];
                        }
                    }
                    // 0+x = x
                    if (n1.esNúmero() && n1.doubleValue() == 0) {
                        return n2.TextoGenerador;
                    }
                    break;
                case '-':
                    //x-x = 0
                    //m*x-n*x = (m-n)*x
                    String T1[] = ExtraerSegúnSimboloRespetandoAgrupaciones(n1.TextoGenerador, '*');
                    String T2[] = ExtraerSegúnSimboloRespetandoAgrupaciones(n2.TextoGenerador, '*');
                    String v1 = T1[T1.length - 1];
                    if (v1.equals(T2[T2.length - 1])) {
                        String es1 = T1.length == 1 ? (primerOrden ? 1 : -1) + "" : n1.TextoGenerador.substring(0, n1.TextoGenerador.lastIndexOf("*"));
                        String es2 = T2.length == 1 ? -1 + "" : n2.TextoGenerador.substring(0, n2.TextoGenerador.lastIndexOf("*"));
                        try {
                            double d1 = Double.parseDouble(es1);
                            double d2 = Double.parseDouble(es2);
                            return (d1 + d2) + "*" + T1[T1.length - 1];
                        } catch (Exception e) {
                        }
                        return new Expresiónes("(" + es1 + "-" + es2 + ")").TextoGenerador + "*" + T1[T1.length - 1];
                    }
                    // 0-x = -1*x
                    if (n1.esNúmero() && n1.doubleValue() == 0) {
                        return "-1*" + new Expresiónes("(" + n2.TextoGenerador + ")").TextoGenerador;
                    }
                    // x-0 = x
                    if (n2.esNúmero() && n2.doubleValue() == 0) {
                        return n1.TextoGenerador;
                    }
                    break;
            }
            if (b1 || b2) {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("No se llevó a cabo una operación binaria pero si se sufrieron cambios en la expresión");
                }
                return new Expresiónes(n1.TextoGenerador + simboloOperador + n2.TextoGenerador).TextoGenerador;
            }
            return "";
        }
    }

    public final class FunciónAnalizadora extends NúmeroAbstracto {

        Expresiónes[] expresiónes;
        FunciónDeRetonoNúmerico función;

        public FunciónAnalizadora(String Expresión) throws Exception {
            TextoGenerador = Expresión;
            int i = Expresión.indexOf("(");
            String argumentos = Expresión.substring(i + 1, Expresión.length() - 1);
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Los argumentos: " + argumentos);
            }
            String nombreFunción = Expresión.substring(0, i);
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("De: " + nombreFunción);
            }
            try {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("\nSe buscará en la biblioteca");
                }
                función = BibliotecaDeFunciones(nombreFunción);
                AñadirFunción(función);
                if (!nombreFunción.equals(función.nombre)) {
                    TextoGenerador = función.nombre + "(" + argumentos + ")";
                    return;
                }
            } catch (Exception e) {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis(e.getMessage());
                }
                throw new Exception(e.getMessage());
            }
            String[] Expre = argumentos.split(",");
            expresiónes = new Expresiónes[Expre.length];
            if (Expre.length == 1 && nombreFunción.isEmpty() && Simplificación) {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Es una agrupación simple");
                }
                expresiónes[0] = new Expresiónes(Expre[0]);
                if (expresiónes[0].esNúmero() || expresiónes[0].esVariable()) {
                    if (GuardarTextoDeAnalisis) {
                        AñadirTextoDeAnalisis("Es un número o una variable, por tanto se puede romper");
                    }
                    TextoGenerador = expresiónes[0].TextoGenerador;
                    return;
                }
            }
            for (int j = 0; j < Expre.length; j++) {
                Expre[j] = new Expresiónes(Expre[j]).TextoGenerador;
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Argumento dentro de función: " + Expre[j]);
                }
            }
            boolean EsConstante = true;
            try {
                for (int j = 0; j < Expre.length; j++) {
                    expresiónes[j] = new Expresiónes(Expre[j]);
                    EsConstante &= expresiónes[j].esNúmero();
                }
                if (EsConstante && Simplificación) {
                    if (GuardarTextoDeAnalisis) {
                        AñadirTextoDeAnalisis("Todos los parámetros dentro de la función son números");
                        AñadirTextoDeAnalisis("Es una constante y se puede convertir");
                    }
                    TextoGenerador = ObtenerValor() + "";
                } else if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("La función tiene variables dentro, por ende no se puede convertir a constante");
                }
                if (expresiónes.length == 1 && nombreFunción.isEmpty()) {
                    int s = ExtraerSegúnSimboloRespetandoAgrupaciones(expresiónes[0].TextoGenerador, '+').length;
                    int r = ExtraerSegúnSimboloRespetandoAgrupaciones(expresiónes[0].TextoGenerador, '-').length;
                    if (s == 1 && r == 1) {
                        TextoGenerador = expresiónes[0].TextoGenerador;
                    }
                } else {
                    String args = nombreFunción + "(";
                    for (int j = 0; j < expresiónes.length; j++) {
                        args += expresiónes[j].TextoGenerador + (j == expresiónes.length - 1 ? "" : ",");
                    }
                    args += ")";
                    TextoGenerador = args;
                    System.out.println("texto: " + args);
                }
            } catch (Exception e) {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("ERROR: " + e.getMessage());
                }
                throw new Exception(e.getMessage());
            }
        }

        @Override
        public double ObtenerValor() throws Exception {
            try {
                return (double) función.F(expresiónes);
            } catch (Exception e) {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("ERROR: " + e.getMessage());
                }
                throw new Exception(e.getMessage());
            }
        }

        @Override
        BufferedImage ImagenlÁrbolBinario() {
            BufferedImage fx = Clonar(texturaVertices);
            Ajuste_Centrado_Ajustar(generador.GenerarTexto(función.nombre), fx);
            BufferedImage[] expns = new BufferedImage[expresiónes.length];
            if (función.nombre.equals("") && expns.length == 1) {
                return expresiónes[0].ImagenlÁrbolBinario();
            }
            double[] ancho = new double[expns.length];
            double[] alto = new double[expns.length];
            for (int i = 0; i < expns.length; i++) {
                expns[i] = expresiónes[i].ImagenlÁrbolBinario();
                ancho[i] = expns[i].getWidth();
                alto[i] = expns[i].getHeight();
            }
            int w = (int) Sumatoria(ancho);
            int h = (int) Mayor(alto);
            BufferedImage Expresiones = new BufferedImage(w + SeparaciónHorizontal * (expns.length - 1), h, 2);
            Graphics2D g = Expresiones.createGraphics();
            int px = 0;
            Dupla[] pts = new Dupla[expns.length];
            int i = 0;
            for (BufferedImage expn : expns) {
                Dupla p = Dupla.Alinear(Expresiones, expn, -1, Dupla.ABAJO);
                g.drawImage(expn, px, p.intY(), null);
                pts[i++] = new Dupla(px + expn.getWidth() / 2, p.Y + texturaVertices.getHeight() / 2);
                px += expn.getWidth() + SeparaciónHorizontal;
            }
            BufferedImage retorno = new BufferedImage(
                    Mayor(Expresiones.getWidth(), fx.getWidth()),
                    fx.getHeight() + Expresiones.getHeight() + SeparaciónVertical,
                    2
            );
            g = retorno.createGraphics();
            Dupla posFx = Dupla.Alinear(retorno, fx, Dupla.MEDIO, Dupla.ARRIBA);
            Dupla posExps = Dupla.Alinear(retorno, Expresiones, Dupla.MEDIO, Dupla.ABAJO);
            if (dibujarRamas) {
                g.setStroke(basicStroke);
                g.setColor(colorBorde);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Dupla p1 = posFx.Clon().Adicionar(new Dupla(texturaVertices).Mitad());
                for (Dupla pt : pts) {
                    g.drawLine(p1.intX(), p1.intY(), pt.intX(), pt.intY() + posExps.intY());
                }
            }
            if (dibujarVertices) {
                g.drawImage(fx, posFx.intX(), posFx.intY(), null);
            }
            g.drawImage(Expresiones, posExps.intX(), posExps.intY(), null);
            return retorno;
        }
    }

    public abstract class FunciónDeRetonoNúmerico {

        String nombre;

        public FunciónDeRetonoNúmerico(String nombre) {
            this.nombre = nombre;
        }

        abstract Object F(Expresiónes[] e) throws Exception;
    }

    public class Variable extends NúmeroAbstracto {

        public String nombre;
        private Number valor;

        public Variable(String nombre, Number valor) {
            this.nombre = nombre;
            this.valor = valor;
            if (GuardarTextoDeAnalisis) {
                if (valor instanceof NúmeroAbstracto) {
                    AñadirTextoDeAnalisis("Variable cargada con nombre: " + nombre
                            + " y valor: " + ((NúmeroAbstracto) valor).TextoGenerador);
                } else {
                    AñadirTextoDeAnalisis("Variable cargada con nombre: " + nombre + " y valor: " + valor);
                }
            }
        }

        @Override
        public String toString() {
            return nombre + "=" + valor;
        }

        @Override
        public double ObtenerValor() {
            return valor.doubleValue();
        }

        @Override
        BufferedImage ImagenlÁrbolBinario() {
            throw new UnsupportedOperationException("Not supported yet. Error en la clase variable");
        }
    }

    String NormalizarCadena(String nombre) {
        AñadirTextoDeAnalisis("Normalizando: " + nombre);
        String retorno = nombre.toLowerCase();
        if (GuardarTextoDeAnalisis) {
            if (!retorno.equals(nombre)) {
                AñadirTextoDeAnalisis("Se ha convertido la cadena a solo minúsculas para normalizar");
                AñadirTextoDeAnalisis(retorno);
                nombre = retorno;
            }
        }
        retorno = RemoverAcentos(retorno);
        if (GuardarTextoDeAnalisis) {
            if (!retorno.equals(nombre)) {
                AñadirTextoDeAnalisis("Se han eliminado los acentos para normalizar");
                AñadirTextoDeAnalisis(retorno);
            }
        }
        return retorno;
    }

    String RemoverAcentos(String origen) {
        String ConAcento = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýÿ";
        String SinAcento = "AAAAAAACEEEEIIIIDNOOOOOOUUUUYBaaaaaaaceeeeiiiionoooooouuuuyy";
        if (ConAcento.length() != SinAcento.length()) {
            throw new RuntimeException("Revise las cadenas usadas para la eliminación de acentos");
        }
        int ejemplares = ConAcento.length();
        String retorno = origen;
        for (int i = 0; i < ejemplares; i++) {
            retorno = retorno.replace(ConAcento.charAt(i), SinAcento.charAt(i));
        }
        return retorno;
    }

    public Variable BibliotecaDeVariables(String nombre) throws Exception {
        if (nombre.length() > 8) {
            String msg = "La expesión excede la longitud máxima como para estar en la biblioteca";
            AñadirTextoDeAnalisis(msg);
            throw new Exception(msg);
        }
        switch (nombre) {
            case "G":
                return new Variable("G", Double.parseDouble("6.67392E-11"));
        }
        nombre = NormalizarCadena(nombre);
        switch (nombre) {
            case "π":
            case "pi":
            case "pi()":
                return new Variable("π", PI);
            case "φ":
            case "Φ":
                return new Variable("Φ", Φ);
            case "e":
            case "euler":
            case "napier":
                return new Variable("e", e);
            case "∞":
            case "infinito":
            case "infinite":
            case "inf":
                return new Variable("∞", Double.POSITIVE_INFINITY);
            case "g":
            case "grav":
            case "gravedad":
            case "gravity":
                return new Variable("g", 9.80665);
            case "c":
            case "luz":
                return new Variable("c", 299792458);
        }
        throw new Exception("La variable no se encontró");
    }

    FunciónDeRetonoNúmerico BibliotecaDeFunciones(String nombre) throws Exception, FunciónNoReconocida {
        try {
            FunciónDeRetonoNúmerico f = ObtenerFunción(nombre);
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("La función fué encontrada en las funciones previamente añadidas");
            }
            return f;
        } catch (Exception e) {
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("La función no fué encontrada en las funciones anteriormente usadas");
                AñadirTextoDeAnalisis("Se procedera a buscar en la biblioteca de funciones por defecto");
            }
        }
        if (nombre.isEmpty()) {
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Función agrupación consultada");
            }
            return new FunciónDeRetonoNúmerico(nombre) {
                @Override
                Object F(Expresiónes[] e) throws Exception {
                    if (e.length != 1) {
                        throw new Exception("Las agrupaciones solo reciben 1 argumento"
                                + "\nlas comas separan argumentos\nlos puntos son para separar la parte decimal");
                    }
                    double valor = e[0].doubleValue();
                    return valor;
                }
            };
        }
        if (nombre.startsWith("+")) {
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Se ha encontrado un + en el inicio del nombre de una función: retirandolo");
            }
            return BibliotecaDeFunciones(nombre.substring(1, nombre.length()));
        }

        if (MultiplicacionesImplicitas) {
            AñadirTextoDeAnalisis("Se descartará que la función sea una multiplicación implicita");
            try {
                Object valorNúmero;
                try {
                    valorNúmero = Double.parseDouble(nombre);
                } catch (Exception e) {
                    valorNúmero = ObtenerVariable(nombre);
                }
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función de multiplicación implicita encontrada");
                }
                Number CNúmero = ((Number) valorNúmero);
                return new FunciónDeRetonoNúmerico(nombre + "*") {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return CNúmero.doubleValue() * valor;
                    }
                };
            } catch (Exception e) {
                AñadirTextoDeAnalisis("El nombre de la función no es ni una variable ni un número");
            }
        }

        nombre = NormalizarCadena(nombre);

        if (nombre.startsWith("arc")) {
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Se encontró el prefijo '\"arc\" se procederá a reemplazarlo por \"a\"");
                AñadirTextoDeAnalisis("para descartar la busqueda de funciones trigonometricas inversas");
            }
            nombre = nombre.replace("arco", "a");
            nombre = nombre.replace("arc", "a");
        }
        if (nombre.endsWith("hiperbolico")) {
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Se encontró el sufijo '\"hiperbolico\" se procederá a reemplazarlo por \"h\"");
                AñadirTextoDeAnalisis("para descartar la busqueda de funciones trigonometricas hiperbolicas");
            }
            nombre = nombre.replace("hiperbolico", "h");

        }
        nombre = nombre.replace("seno", "sen");
        nombre = nombre.replace("sinus", "sen");
        nombre = nombre.replace("sin", "sen");
        nombre = nombre.replace("coseno", "cos");
        nombre = nombre.replace("tangente", "tan");
        nombre = nombre.replace("secante", "sec");
        nombre = nombre.replace("cosecante", "csc");
        nombre = nombre.replace("cosec", "csc");
        nombre = nombre.replace("cotangente", "cot");
        try {
            AñadirTextoDeAnalisis("Ahora que se ha normalizado la cadena, se procederá nuevamente a buscar en "
                    + "las funciones previamente añadidas");
            FunciónDeRetonoNúmerico f = ObtenerFunción(nombre);
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("La función fué encontrada en las funciones previamente añadidas");
            }
            return f;
        } catch (Exception e) {
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("La función no fué encontrada en las funciones anteriormente usadas");
                AñadirTextoDeAnalisis("Se procedera a buscar en la biblioteca de funciones por defecto");
            }
        }

        if ((nombre.startsWith("raiz") || nombre.startsWith("root")) && nombre.length() > 4) {
            String TxtIndice = nombre.substring(4, nombre.length());
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Raiz indicada encontrada: indice " + TxtIndice);
            }
            Object ObjIndice;
            try {
                ObjIndice = Double.parseDouble(TxtIndice);
            } catch (Exception e) {
                try {
                    ObjIndice = ObtenerVariable(TxtIndice);
                } catch (Exception e2) {
                    throw new Exception("No se reconoce el índice " + TxtIndice + " para " + nombre.replace(TxtIndice, ""));
                }
            }
            Number NúmIndice = ((Number) ObjIndice);
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("El indice es númerico: " + NúmIndice);
            }
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Raíz indicada retornada");
            }
            return new FunciónDeRetonoNúmerico("raiz" + NúmIndice) {
                @Override
                Object F(Expresiónes[] e) throws Exception {
                    if (e.length != 1) {
                        throw new Exception("Las raices indicadas solo reciben 1 argumento"
                                + "\nlas comas separan argumentos\nlos puntos son para separar la parte decimal");
                    }
                    double valor = e[0].doubleValue();
                    return raiz(valor, NúmIndice.doubleValue());
                }
            };
        }
        boolean Pot = (nombre.startsWith("poten") && nombre.length() > 5);
        if (nombre.length() > 3) {
            Pot |= nombre.startsWith("pow");
            Pot |= nombre.startsWith("sen");
            Pot |= nombre.startsWith("cos");
            Pot |= nombre.startsWith("tan");
            Pot |= nombre.startsWith("sec");
            Pot |= nombre.startsWith("csc");
            Pot |= nombre.startsWith("cot");
        }
        if (Pot) {
            String TxtIndice;
            if (nombre.startsWith("poten")) {
                TxtIndice = nombre.substring(5, nombre.length());
            } else {
                TxtIndice = nombre.substring(3, nombre.length());
            }
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("Potencia indicada encontrada: exponente " + TxtIndice + " Para: " + nombre.replace(TxtIndice, ""));
            }
            Object ObjPot;
            try {
                ObjPot = Double.parseDouble(TxtIndice);
            } catch (Exception e) {
                try {
                    ObjPot = ObtenerVariable(TxtIndice);
                } catch (Exception e2) {
                    throw new Exception("No se reconoce el exponente " + TxtIndice + " Para: " + nombre.replace(TxtIndice, ""));
                }
            }
            Number NúmExponente = ((Number) ObjPot);
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("El indice es númerico: " + NúmExponente);
            }
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("potencia indicada retornada");
            }
            return new FunciónDeRetonoNúmerico(nombre) {
                @Override
                Object F(Expresiónes[] e) throws Exception {
                    double valor = e[0].doubleValue();
                    if (GuardarTextoDeAnalisis) {
                        AñadirTextoDeAnalisis(nombre);
                    }
                    if (!nombre.startsWith("poten") && !nombre.startsWith("pow")) {
                        if (nombre.startsWith("sin") || nombre.startsWith("sen")) {
                            valor = Sen(valor);
                        } else if (nombre.startsWith("cos")) {
                            valor = Cos(valor);
                        } else if (nombre.startsWith("tan")) {
                            valor = Tan(valor);
                        } else if (nombre.startsWith("sec")) {
                            valor = Sec(valor);
                        } else if (nombre.startsWith("csc")) {
                            valor = Csc(valor);
                        } else if (nombre.startsWith("cot")) {
                            valor = Cot(valor);
                        }
                    }
                    return poten(valor, NúmExponente.doubleValue());
                }
            };
        }

        switch (nombre) {
            case "aleatorio"://Agrupador
            case "random"://Agrupador
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función aleatorio consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        if (e != null || e.length > 0) {
                            throw new Exception("La función aleatorio no recibe parámetros");
                        }
                        return Math.random();
                    }
                };
            case "0"://Agrupador
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función nula consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return 0;
                    }
                };
            case "-"://Agrupador
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función de negación consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return -valor;
                    }
                };
            case "radianes":
            case "rad":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función seno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Radianes(valor);
                    }
                };
            case "sen":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función seno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Sen(valor);
                    }
                };
            case "cos":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Cos(valor);
                    }
                };
            case "tan":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Tan(valor);
                    }
                };
            case "sec":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Sec(valor);
                    }
                };
            case "csc":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Csc(valor);
                    }
                };
            case "cot":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Cot(valor);
                    }
                };
            case "asen":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función seno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Asen(valor);
                    }
                };
            case "acos":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Acos(valor);
                    }
                };
            case "atan":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Atan(valor);
                    }
                };
            case "senh":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función seno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Senh(valor);
                    }
                };
            case "cosh":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Cosh(valor);
                    }
                };
            case "tanh":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Tanh(valor);
                    }
                };
            case "asenh":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función seno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Asenh(valor);
                    }
                };
            case "acosh":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Acosh(valor);
                    }
                };
            case "atanh":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Atanh(valor);
                    }
                };
            case "sech":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Sech(valor);
                    }
                };
            case "csch":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Csch(valor);
                    }
                };
            case "coth":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Cot(valor);
                    }
                };
            case "raizcuadrada":
            case "raizc":
            case "sqrt":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz cuadrada consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return raiz2(valor);
                    }
                };
            case "raizcubica":
            case "cubicroot":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz cuadrada consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return raiz3(valor);
                    }
                };
            case "raizcuarta":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz cuarta consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return raiz4(valor);
                    }
                };
            case "potencuadrada":
            case "potenciacuadrada":
            case "cuadrado":
            case "sqr":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función potencia cuadrada consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return poten2(valor);
                    }
                };
            case "potencubica":
            case "potenciacubica":
            case "cubico":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función potencia cubica consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return poten3(valor);
                    }
                };
            case "cuarta":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función potencia cuarta consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return poten3(valor);
                    }
                };
            case "fact":
            case "facto":
            case "factorial":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Factorial(valor);
                    }
                };
            case "gamma":
            case "Γ":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Gamma(valor);
                    }
                };
            case "ln":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Ln(valor);
                    }
                };
            case "sgn":
            case "signo":
            case "signum":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Sgn(valor);
                    }
                };
            case "abs":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return abs(valor);
                    }
                };
            case "redondeo":
            case "rdnd":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Redondeo(valor);
                    }
                };
            case "piso":
            case "suelo":
            case "floor":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Piso(valor);
                    }
                };
            case "parteentera":
            case "pe":
            case "int":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return ParteEntera(valor);
                    }
                };
            case "partedecimal":
            case "pd":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return ParteDecimal(valor);
                    }
                };
            case "techo":
            case "ceil":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Piso(valor);
                    }
                };
            case "atan2":
            case "angulo":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return Atan2(valor, valor2);
                    }
                };
            case "pitagoras":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return TeoremaDePitagoras(valor, valor2);
                    }
                };
            case "raiz":
            case "root":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return raiz(valor, valor2);
                    }
                };
            case "permutacion":
            case "p":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return P(valor, valor2);
                    }
                };
            case "combinatoriasinrepeticion":
            case "c":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return C(valor, valor2);
                    }
                };
            case "combinatoriaconrepeticion":
            case "cr":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return CR(valor, valor2);
                    }
                };
            case "aleatorioenteroentre":
            case "randomint":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función aleatorio entero entre consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return AleatorioEnteroEntre(valor, valor2);
                    }
                };
            case "aleatorioentre":
            case "randombetween":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función aleatorio entre consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return AleatorioEntre(valor, valor2);
                    }
                };
            case "poten":
            case "pow":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return poten(valor, valor2);
                    }
                };
            case "frac":
            case "dividir":
            case "division":
            case "fraction":
            case "fraccion":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return Fracción(valor, valor2);
                    }
                };
            case "mod":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return valor % valor2;
                    }
                };
            case "|":
            case "union":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        int valor = e[0].intValue();
                        int valor2 = e[1].intValue();
                        return (double) (valor | valor2);
                    }
                };
            case "&":
            case "interseccion":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        int valor = e[0].intValue();
                        int valor2 = e[1].intValue();
                        return (double) (valor & valor2);
                    }
                };
            case "xor":
            case "^":
            case "exclusion":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función exclusión consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        if (e.length != 2) {
                            throw new Exception("Exclusión recibe 2 argumentos");
                        }
                        int valor = e[0].intValue();
                        int valor2 = e[1].intValue();
                        return (double) (valor ^ valor2);
                    }
                };
            case "~":
            case "negbin":
            case "no":
            case "not":
            case "negar":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        int valor = e[0].intValue();
                        return (double) (~valor);
                    }
                };
            case "<<":
            case "desplIzquierda":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        int valor = e[0].intValue();
                        int valor2 = e[1].intValue();
                        return (double) (valor << valor2);
                    }
                };
            case ">>":
            case "desplderechaconsigno":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        int valor = e[0].intValue();
                        int valor2 = e[1].intValue();
                        return (double) (valor >> valor2);
                    }
                };
            case ">>>":
            case "desplderecha":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        return (double) (e[0].intValue() >>> e[1].intValue());
                    }
                };
            case "distribuciondeprobabilidad":
            case "gammadist":
            case "gamma.dist":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        double valor3 = e[2].doubleValue();
                        if (e.length == 3) {
                            return Gamma_DistribuciónDeProbabilidad(valor, valor2, valor3);
                        }
                        double valor4 = e[3].doubleValue();
                        return Gamma_DistribuciónDeProbabilidad(valor, valor2, valor3, valor4);
                    }
                };
            case "aproxderiv":
            case "adf":
            case "af'":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                InterpretadorDeTextoMatemático auxDx = new InterpretadorDeTextoMatemático();
                auxDx.padre = this;
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        if (!e[1].esVariable()) {
                            throw new RuntimeException("El segundo parámetro debe ser una variable");
                        }
                        String var = e[1].TextoGenerador;
                        if (auxDx.Expresión.isEmpty()) {
                            auxDx.AñadirVariable(var, 0);
                            auxDx.añadirExpresión(e[0].TextoGenerador);
                        }
                        Función f = new Función() {
                            @Override
                            public double Y(double x) {
                                try {
                                    auxDx.AñadirVariable(var, x);
                                    return auxDx.ObtenerValorDeExpresión()[0];
                                } catch (Exception ex) {
                                    throw new RuntimeException("Problema con la derivada");
                                }
                            }
                        };
                        try {
                            return Apróx_Derivada(e[2].ObtenerValor(), f);
                        } catch (Exception e1) {
                            throw new RuntimeException("Problema con la derivada");
                        }
                    }
                };
            case "aproxintegral":
            case "aintegral":
            case "a∫":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                auxDx = new InterpretadorDeTextoMatemático();
                auxDx.padre = this;
                return new FunciónDeRetonoNúmerico("∫") {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        if (!e[1].esVariable()) {
                            throw new RuntimeException("El segundo parámetro debe ser una variable");
                        }
                        String var = e[1].TextoGenerador;
                        if (auxDx.Expresión.isEmpty()) {
                            auxDx.AñadirVariable(var, 0);
                            auxDx.añadirExpresión(e[0].TextoGenerador);
                        }
                        Función f = new Función() {
                            @Override
                            public double Y(double x) {
                                try {
                                    auxDx.AñadirVariable(var, x);
                                    return auxDx.ObtenerValorDeExpresión()[0];
                                } catch (Exception ex) {
                                    throw new RuntimeException("Problema con la derivada");
                                }
                            }
                        };
                        try {
                            return Apróx_Integral(e[2].ObtenerValor(), e[3].ObtenerValor(), f);
                        } catch (Exception e1) {
                            throw new RuntimeException("Problema con la derivada");
                        }
                    }
                };
            case "distancia":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valores[] = new double[e.length];
                        for (int i = 0; i < valores.length; i++) {
                            valores[i] = e[i].doubleValue();
                        }
                        return Distancia(valores);
                    }
                };
            case "mayor":
            case "max":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valores[] = new double[e.length];
                        for (int i = 0; i < valores.length; i++) {
                            valores[i] = e[i].doubleValue();
                        }
                        return Mayor(valores);
                    }
                };
            case "menor":
            case "min":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valores[] = new double[e.length];
                        for (int i = 0; i < valores.length; i++) {
                            valores[i] = e[i].doubleValue();
                        }
                        return Menor(valores);
                    }
                };
            case "promedio":
            case "prom":
            case "average":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object F(Expresiónes[] e) throws Exception {
                        double valores[] = new double[e.length];
                        for (int i = 0; i < valores.length; i++) {
                            valores[i] = e[i].doubleValue();
                        }
                        return Promedio(valores);
                    }
                };
        }
        throw new FunciónNoReconocida("Función no reconocida");
    }

    public class FunciónNoReconocida extends Exception {

        public FunciónNoReconocida(String message) {
            super(message);
        }
    }

}
