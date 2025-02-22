package HerramientasMatemáticas;

import static HerramientaDeImagen.Filtros_Lineales.*;
import HerramientaDeImagen.GeneradorDeTexto;
import java.util.ArrayList;
import static HerramientasMatemáticas.Matemática.*;
import static java.lang.Math.*;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import static java.lang.Math.random;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public final class InterpretadorDeTextoMatemático {

    InterpretadorDeTextoMatemático padre;

    private boolean GuardarTextoDeAnalisis = false;
    public String TextoDeAnalisis = null;

    boolean Simplificación_Automatica = true;
    boolean Admitir_MultiplicacionesImplicitas = true;

    ArrayList<Variable> VariablesDeclaradas = new ArrayList<>();
    ArrayList<FunciónDeRetonoNúmerico> funcionesDeclaradas = new ArrayList<>();
    public ArrayList<Expresiones> Expresiones = new ArrayList<>();

    //Elementos de dibujado del árbol binario
    public GeneradorDeTexto ÁrbolBinario_GeneradorDeTexto;
    public Color ÁrbolBinario_colorBordes;
    public BasicStroke ÁrbolBinario_BasicStroke;
    public int ÁrbolBinario_SeparaciónHorizontal_Ramas = 20;
    public int ÁrbolBinario_SeparaciónVertical_Ramas = 20;
    public boolean ÁrbolBinario_DibujarUnión_Ramas = true;
    public boolean ÁrbolBinario_DibujarTextura_Nodos = true;
    public boolean ÁrbolBinario_MostrarNombreVariables = true;
    public boolean ÁrbolBinario_Bordear_Nodos = true;
    public BufferedImage ÁrbolBinario_Textura_Nodos;

    public static void main(String[] args) {
        InterpretadorDeTextoMatemático c;
        try {
            long t = System.nanoTime();
            c = new InterpretadorDeTextoMatemático("x=4;"
                    + "x(x)", true, true);
            System.out.println(c.Expresiones.get(0).TextoGenerador);
//            c.ObtenerValorDeExpresión();
//            c.ObtenerValorDeExpresión();
            System.out.println(c.TextoDeAnalisis);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n\nError al generar el analizador");
        }
    }

    public InterpretadorDeTextoMatemático(String Código, boolean ObligarPuntoYComa, boolean guardTxt,boolean simplificarAutomáticamente) throws Exception {
        try {
            Simplificación_Automatica = simplificarAutomáticamente;
            GuardarTextoDeAnalisis(guardTxt);
            CargarCódigo(Código, ObligarPuntoYComa);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    public InterpretadorDeTextoMatemático(String Código, boolean ObligarPuntoYComa, boolean guardTxt) throws Exception {
        this(Código, ObligarPuntoYComa, guardTxt, true);
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
        {//Reemplazos básicos para la interpretación
            if (ObligarUsoDePuntoYComa) {
                AñadirTextoDeAnalisis("Eliminación de saltos de línea");
                Código = Código.replace("\n", "");
            } else {
                AñadirTextoDeAnalisis("Saltos de línea reemplazados por punto y coma (;)");
                Código = Código.replace("\n", ";");
            }
            Código = Código.replace("$$", ";");
            Código = Código.replace(" es variable", " es var");
            Código = Código.replace(" is var", " es var");
        }

        String sentencias[] = Código.split(";");//Se rompen todas las sentencias

        for (int i = 0; i < sentencias.length; i++) {//Se recorre sentencia por sentencia para generar el objeto

            sentencias[i] = sentencias[i].trim();//Se eliminan espacios al princiio y al final

            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("\nAnalizando la sentencia: " + sentencias[i] + "\n");
            }

            {//Declaración de variables
                if (sentencias[i].contains("=")) {
                    if (GuardarTextoDeAnalisis) {
                        AñadirTextoDeAnalisis("Se encontró un igual (=) esta sentencia es de asignación");
                    }
                    String sentencia[] = sentencias[i].split("=");
                    String nombre = sentencia[0];
                    nombre = nombre.trim();
                    if (nombre.contains(" ")) {
                        throw new Exception("En el nombre las variables no pueden tener espacios");
                    }
                    NúmeroAbstracto valor = new Expresiones(sentencia[1].replace(" ", ""));
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
                    continue;
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
                    continue;
                }
            }
            {//Expresión de retorno númerico
                try {
                    añadirExpresión(sentencias[i]);
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
        Expresiones.add(new Expresiones(txtExpresión));
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
            System.out.println(t);
        }
    }

    public BufferedImage[] ImagenesÁrbolesBinarios() {
        boolean deponerColor = ÁrbolBinario_colorBordes == null;
        boolean deponerStroke = ÁrbolBinario_BasicStroke == null;
        boolean deponerTextura = ÁrbolBinario_Textura_Nodos == null;
        boolean deponerGenerador = ÁrbolBinario_GeneradorDeTexto == null;
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
        BufferedImage[] árboles = new BufferedImage[Expresiones.size()];
        for (int i = 0; i < Expresiones.size(); i++) {
            árboles[i] = Expresiones.get(i).ImagenlÁrbolBinario();
        }
        if (deponerColor) {
            ÁrbolBinario_colorBordes = null;
        }
        if (deponerStroke) {
            ÁrbolBinario_BasicStroke = null;
        }
        if (deponerTextura) {
            ÁrbolBinario_Textura_Nodos = null;
        }
        if (deponerGenerador) {
            ÁrbolBinario_GeneradorDeTexto = null;
        }
        return árboles;
    }

    public void DeponerElementosDeDibujoDeÁrbol() {
        ÁrbolBinario_colorBordes = null;
        ÁrbolBinario_BasicStroke = null;
        ÁrbolBinario_Textura_Nodos = null;
        ÁrbolBinario_GeneradorDeTexto = null;
    }

    private void CargarColorPorDefectoDeDibujoDeÁrbol() {
        if (ÁrbolBinario_colorBordes == null) {
            ÁrbolBinario_colorBordes = new Color(0, 150, 250);
        }
    }

    private void CargarStrokePorDefectoDeDibujoDeÁrbol() {
        if (ÁrbolBinario_BasicStroke == null) {
            ÁrbolBinario_BasicStroke = new BasicStroke(4);
        }
    }

    private void CargarGeneradorTextoPorDefectoDeDibujoDeÁrbol() {
        if (ÁrbolBinario_GeneradorDeTexto == null) {
            ÁrbolBinario_GeneradorDeTexto = new GeneradorDeTexto().ModificarTamañoFuente(20);
        }
    }

    private void CargarTexturaPorDefectoDeDibujoDeÁrbol() {
        if (ÁrbolBinario_Textura_Nodos == null) {
            ÁrbolBinario_Textura_Nodos = new BufferedImage(60, 60, 2) {
                {
                    Graphics2D g = createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setColor(Color.WHITE);
                    g.fillOval(0, 0, getWidth(), getHeight());
                    if (ÁrbolBinario_Bordear_Nodos) {
                        g.setColor(ÁrbolBinario_colorBordes);
                        g.setStroke(ÁrbolBinario_BasicStroke);
                        g.setComposite(AlphaComposite.SrcAtop);
                        g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                    }
                }
            };
        }
    }

    public double[] ObtenerValorDeExpresión() throws Exception {
        if (Expresiones.isEmpty()) {
            throw new Exception("Faltan expresiones de retorno");
        }
        double retorno[] = new double[Expresiones.size()];
        for (int i = 0; i < retorno.length; i++) {
            try {
                long tiempo = System.nanoTime();
                retorno[i] = Expresiones.get(i).ObtenerValor();
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("\nResultado encontrado: "
                            + retorno[i] + "\nde: " + Expresiones.get(i).TextoGenerador
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
        if (!Admitir_MultiplicacionesImplicitas) {
            return txtExpresión;
        }
        txtExpresión.replace(")(", ")*(");
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
        { //Unificar signos de agrupación
            txtExpresión = txtExpresión.replace("[", "(");
            txtExpresión = txtExpresión.replace("]", ")");
            txtExpresión = txtExpresión.replace("{", "(");
            txtExpresión = txtExpresión.replace("}", ")");
        }
        {//Eliminar espacios, tabulaciones y saltos de línea
            txtExpresión = txtExpresión.replace(" ", "");
            txtExpresión = txtExpresión.replace("\n", "");
            txtExpresión = txtExpresión.replace("\t", "");
        }

        txtExpresión = MultiplicacionesImplicitas(txtExpresión);

        {//Simplificar operación de signos
            int intentos = 0;
            boolean ambiguedad;
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
        {//Normalización de Ascii matemático
            txtExpresión = txtExpresión.replace("½", "1/2");
            txtExpresión = txtExpresión.replace("¼", "1/4");
            txtExpresión = txtExpresión.replace("¾", "3/4");
            txtExpresión = txtExpresión.replace("¹", "");
            txtExpresión = txtExpresión.replace("²", "^2");
            txtExpresión = txtExpresión.replace("³", "^3");

            txtExpresión = txtExpresión.replace("×", "*");
            txtExpresión = txtExpresión.replace("÷", "/");
            txtExpresión = txtExpresión.replace("—", "-");

            txtExpresión = txtExpresión.replace("√", "raiz2");
            txtExpresión = txtExpresión.replace("⌊", "piso(");
            txtExpresión = txtExpresión.replace("⌋", ")");
            txtExpresión = txtExpresión.replace("⌈", "techo(");
            txtExpresión = txtExpresión.replace("⌉", ")");
        }
        return txtExpresión;
    }

    public interface ExpresiónDeRetorno {

        public double ObtenerValor() throws Exception;
    }

    public abstract class NúmeroAbstracto extends Number implements ExpresiónDeRetorno {

        final String Descartado = "Descartado";

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
            return TextoGenerador.equals(Descartado);
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
            e.TextoGenerador = Descartado;
            return e;
        }

        Dupla calcularPosImg(BufferedImage img, Dupla pos) {
            return pos.Clon().Sustraer(new Dupla(img).Mitad());
        }

        @Override
        public int intValue() {
            try {
                return (int) round(ObtenerValor());
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

    public static NúmeroAbstracto NúmeroAbstracto(Number n) {
        return ((NúmeroAbstracto) n);
    }

    public class Expresiones extends NúmeroAbstracto {

        public Expresiones(double n) throws Exception {
            this(n + "");
        }

        public Expresiones(String s) throws Exception {
            TextoGenerador = s;
            if (s == null || "".equals(s)) {//No ha entrado expresión
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
                            if (NúmeroAbstracto(op.Cuerpo).Descartado()) {
                                TextoGenerador = NúmeroAbstracto(op.Número).TextoGenerador + "";
                                if (!s.equals(NúmeroAbstracto(op.Número).TextoGenerador)) {
                                    Número = new Expresiones(NúmeroAbstracto(op.Número).TextoGenerador);
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
                            Número = new Expresiones(op.TextoGenerador);
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
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("\nSe comprobará si " + s + " tiene un prefijo\n");
                }
                Prefijos p = new Prefijos(s);
                Número = p.Número;
                TextoGenerador = p.TextoGenerador;
                if (!s.equals(p.TextoGenerador)) {
                    Número = new Expresiones(p.TextoGenerador);
                    return;
                }
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Se encontró un operador como prefijo");
                }
                return;
            } catch (Exception e) {
            }
            try {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("\nSe comprobará si " + s + " tiene un sufijo\n");
                }
                Sufijos su = new Sufijos(s);
                Número = su.Número;
                TextoGenerador = su.TextoGenerador;
                if (!s.equals(su.TextoGenerador)) {
                    Número = new Expresiones(su.TextoGenerador);
                    return;
                }
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
                            AñadirTextoDeAnalisis("de: " + s + " con: " + TextoGenerador);
                        }
                        Número = new Expresiones(f.TextoGenerador);
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
            try {//Es un número
                double n = Double.parseDouble(TextoGenerador);
                img = ÁrbolBinario_GeneradorDeTexto.GenerarTexto(recortarDecimales(n) + "");
            } catch (Exception e) {
            }
            if (Número instanceof Variable) {//Es una variable
                if (ÁrbolBinario_MostrarNombreVariables) {
                    img = ÁrbolBinario_GeneradorDeTexto.GenerarTexto(((Variable) Número).nombre);
                } else {
                    img = ÁrbolBinario_GeneradorDeTexto.GenerarTexto(recortarDecimales(((Variable) Número).ObtenerValor()) + "");
                }
            }
            if (img == null) {
                //Es cualquier otra operación
                return ((NúmeroAbstracto) Número).ImagenlÁrbolBinario();
            }
            BufferedImage retorno = Dupla.convBufferedImage(ÁrbolBinario_Textura_Nodos);
            if (ÁrbolBinario_DibujarTextura_Nodos) {
                retorno = Clonar(ÁrbolBinario_Textura_Nodos);
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
                    núm = new Expresiones(cabeza);
                    break;
                case '-':
                    núm = new Expresiones("-1*" + cabeza);
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
                    núm = new Expresiones(cuerpo + "/100");
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

        Comparator<String> comparadorFactores() {
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
            if (Simplificación_Automatica && (simboloOperador == '+' || simboloOperador == '*' || simboloOperador == '-')) {
                if (simboloOperador == '-') {
                    String ordenables[] = new String[partes.length - 1];
                    for (int i = 1; i < partes.length; i++) {
                        ordenables[i - 1] = partes[i];
                    }
                    Arrays.sort(ordenables, comparadorFactores());
                    for (int i = 1; i < partes.length; i++) {
                        partes[i] = ordenables[i - 1];
                    }
                }
            } else if (simboloOperador == '+') {
                Arrays.sort(partes, comparadorFactores());
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
                        NúmeroAbstracto n1 = new Expresiones(partes[0]);
                        NúmeroAbstracto n2 = new Expresiones(partes[1]);
                        Número = n1;
                        Cuerpo = n2;
                        String r = IdentidadesBinarias(n1, n2, true);
                        if (!r.isEmpty()) {
                            n1 = new Expresiones(r);
                            TextoGenerador = n1.TextoGenerador;
                            if (GuardarTextoDeAnalisis) {
                                AñadirTextoDeAnalisis("Simplificado a: " + TextoGenerador);
                            }
                            Número = n1;
                            Cuerpo = Descartar();
                        } else {
                            TextoGenerador = NúmeroAbstracto(Número).TextoGenerador + simboloOperador + NúmeroAbstracto(Cuerpo).TextoGenerador;
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
                            NúmeroAbstracto n1 = new Expresiones(partes[i]);
                            try {
                                NúmeroAbstracto n2 = new Expresiones(partes[i + 1]);
                                r = IdentidadesBinarias(
                                        n1, n2, i == 0
                                );
                            } catch (Exception e) {
                                AñadirTextoDeAnalisis("Operación impar (número de términos)");
                            }
                            if (!r.isEmpty()) {
                                i++;
                                reducciones.add(new Expresiones(r).TextoGenerador);
                            } else {
                                reducciones.add(n1.TextoGenerador);
                            }
                        }
                        if (partes.length > reducciones.size()) {
                            TextoGenerador = "";
                            if (Simplificación_Automatica && (simboloOperador == '+' || simboloOperador == '*' || simboloOperador == '-')) {
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
                                    Collections.sort(reducciones, comparadorFactores());
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
                    Número = new Expresiones(partes[0]);
                    Cuerpo = Descartar();
                    for (int i = 1; i < partes.length; i++) {
                        if (i == partes.length - 1) {
                            Cuerpo = new Expresiones(partes[i]);
                            String r = IdentidadesBinarias(NúmeroAbstracto(Número),
                                    NúmeroAbstracto(Cuerpo), i == 0
                            );
                            if (!r.isEmpty()) {
                                Número = new Expresiones(r);
                                Cuerpo = Descartar();
                            }
                        } else {
                            try {
                                Número = new Expresiones(NúmeroAbstracto(Número).TextoGenerador + simboloOperador + new Expresiones(partes[i]).TextoGenerador);
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
                    return pow(a, b);
                default:
                    throw new RuntimeException("No se reconoce el simbolo operador");
            }
        }

        @Override
        public double ObtenerValor() throws Exception {
            return operar(Número.doubleValue(), Cuerpo.doubleValue());
        }

        @Override
        BufferedImage ImagenlÁrbolBinario() {
            BufferedImage op = Clonar(ÁrbolBinario_Textura_Nodos);
            Ajuste_Centrado_Ajustar(ÁrbolBinario_GeneradorDeTexto.GenerarTexto(simboloOperador + ""), op);
            BufferedImage arg1 = ((NúmeroAbstracto) Número).ImagenlÁrbolBinario();
            BufferedImage arg2 = null;
            arg2 = ((NúmeroAbstracto) Cuerpo).ImagenlÁrbolBinario();
            BufferedImage retorno = new BufferedImage(
                    arg1.getWidth() + arg2.getWidth() + ÁrbolBinario_SeparaciónHorizontal_Ramas,
                    Máx(arg1.getHeight(), arg2.getHeight()) + op.getHeight() + ÁrbolBinario_SeparaciónVertical_Ramas,
                    2
            );
            Graphics2D g = retorno.createGraphics();
            Dupla PosOP = Dupla.Alinear(retorno, op, Dupla.MEDIO, Dupla.ARRIBA);
            Dupla PosArg1 = Dupla.Alinear(retorno, arg1, Dupla.IZQUIERDA, Dupla.ABAJO);
            Dupla PosArg2 = Dupla.Alinear(retorno, arg2, Dupla.DERECHA, Dupla.ABAJO);
            if (ÁrbolBinario_DibujarUnión_Ramas) {
                g.setStroke(ÁrbolBinario_BasicStroke);
                g.setColor(ÁrbolBinario_colorBordes);
                g.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );
                int y = new Dupla(ÁrbolBinario_Textura_Nodos).Mitad().intY();
                Dupla pt1 = PosOP.Clon().Adicionar(y);
                Dupla pt2 = PosArg1.Clon().Adicionar(new Dupla(arg1).Mitad().X, y);
                Dupla pt3 = PosArg2.Clon().Adicionar(new Dupla(arg2).Mitad().X, y);
                g.drawLine(pt1.intX(), pt1.intY(), pt2.intX(), pt2.intY());
                g.drawLine(pt1.intX(), pt1.intY(), pt3.intX(), pt3.intY());
            }
            if (ÁrbolBinario_DibujarTextura_Nodos) {
                g.drawImage(op, PosOP.intX(), PosOP.intY(), null);
            }
            g.drawImage(arg1, PosArg1.intX(), PosArg1.intY(), null);
            g.drawImage(arg2, PosArg2.intX(), PosArg2.intY(), null);
            return retorno;
        }

        String IdentidadesBinarias(NúmeroAbstracto n1, NúmeroAbstracto n2, boolean primerOrden) throws Exception {
            if (!Simplificación_Automatica) {
                return "";
            }
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
                            return new Expresiones(T1[0] + "^" + new Expresiones("(" + e1 + "+" + e2 + ")").TextoGenerador).TextoGenerador;
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
                            return new Expresiones("(" + es1 + simboloOperador + es2 + ")").TextoGenerador + "*" + T1[T1.length - 1];
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
                        return new Expresiones("(" + es1 + "-" + es2 + ")").TextoGenerador + "*" + T1[T1.length - 1];
                    }
                    // 0-x = -1*x
                    if (n1.esNúmero() && n1.doubleValue() == 0) {
                        return "-1*" + new Expresiones("(" + n2.TextoGenerador + ")").TextoGenerador;
                    }
                    // x-0 = x
                    if (n2.esNúmero() && n2.doubleValue() == 0) {
                        return n1.TextoGenerador;
                    }
                    break;
            }
            return "";
        }
    }

    public final class FunciónAnalizadora extends NúmeroAbstracto {

        Expresiones[] Argumentos;
        FunciónDeRetonoNúmerico Función;

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
                Función = BibliotecaDeFunciones(nombreFunción);
                AñadirFunción(Función);
                if (!nombreFunción.equals(Función.nombre)) {
                    TextoGenerador = Función.nombre + "(" + argumentos + ")";
                    return;
                }
            } catch (Exception e) {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis(e.getMessage());
                }
                throw new Exception(e.getMessage());
            }
            String[] Args = argumentos.split(",");
            Argumentos = new Expresiones[Args.length];
            if (Args.length == 1 && nombreFunción.isEmpty()) {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Es una agrupación simple");
                }
                Argumentos[0] = new Expresiones(Args[0]);
                if ((Argumentos[0].esNúmero() || Argumentos[0].esVariable()) && Simplificación_Automatica) {
                    if (GuardarTextoDeAnalisis) {
                        AñadirTextoDeAnalisis("Es un número o una variable, por tanto se puede romper");
                    }
                    TextoGenerador = Argumentos[0].TextoGenerador;
                    return;
                }
            }
            for (int j = 0; j < Args.length; j++) {
                Args[j] = new Expresiones(Args[j]).TextoGenerador;
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Argumento dentro de función: " + Args[j]);
                }
            }
            boolean EsConstante = true;
            try {
                for (int j = 0; j < Args.length; j++) {
                    Argumentos[j] = new Expresiones(Args[j]);
                    EsConstante &= Argumentos[j].esNúmero();
                }
                if (EsConstante && Simplificación_Automatica) {
                    if (GuardarTextoDeAnalisis) {
                        AñadirTextoDeAnalisis("Todos los parámetros dentro de la función son números");
                        AñadirTextoDeAnalisis("Es una constante y se puede convertir");
                    }
                    TextoGenerador = ObtenerValor() + "";
                    return;
                } else if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("La función no se puede convertir a constante");
                }

                String NuevoTexto = nombreFunción + "(";
                for (int j = 0; j < Argumentos.length; j++) {
                    NuevoTexto += Argumentos[j].TextoGenerador + (j == Argumentos.length - 1 ? "" : ",");
                }
                NuevoTexto += ")";
                TextoGenerador = NuevoTexto;
                System.out.println("texto: " + TextoGenerador);
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
                return (double) Función.Proceso_Retorno(Argumentos);
            } catch (Exception e) {
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("ERROR: " + e.getMessage());
                }
                throw new Exception(e.getMessage());
            }
        }

        @Override
        BufferedImage ImagenlÁrbolBinario() {
            BufferedImage fx = Clonar(ÁrbolBinario_Textura_Nodos);
            Ajuste_Centrado_Ajustar(ÁrbolBinario_GeneradorDeTexto.GenerarTexto(Función.nombre + "()"), fx);
            BufferedImage[] expns = new BufferedImage[Argumentos.length];

            double[] ancho = new double[expns.length];
            double[] alto = new double[expns.length];
            for (int i = 0; i < expns.length; i++) {
                System.out.println(Argumentos[i]);
                expns[i] = Argumentos[i].ImagenlÁrbolBinario();
                ancho[i] = expns[i].getWidth();
                alto[i] = expns[i].getHeight();
            }
            int w = (int) Sumatoria(ancho);
            int h = (int) Máx(alto);
            BufferedImage Expresiones = new BufferedImage(w + ÁrbolBinario_SeparaciónHorizontal_Ramas * (expns.length - 1), h, 2);
            Graphics2D g = Expresiones.createGraphics();
            int px = 0;
            Dupla[] pts = new Dupla[expns.length];
            int i = 0;
            for (BufferedImage expn : expns) {
                Dupla p = Dupla.Alinear(Expresiones, expn, Dupla.POR_DEFECTO, Dupla.ABAJO);
                g.drawImage(expn, px, p.intY(), null);
                pts[i++] = new Dupla(px + expn.getWidth() / 2, p.Y + ÁrbolBinario_Textura_Nodos.getHeight() / 2);
                px += expn.getWidth() + ÁrbolBinario_SeparaciónHorizontal_Ramas;
            }
            BufferedImage retorno = new BufferedImage(
                    Máx(Expresiones.getWidth(), fx.getWidth()),
                    fx.getHeight() + Expresiones.getHeight() + ÁrbolBinario_SeparaciónVertical_Ramas,
                    2
            );
            g = retorno.createGraphics();
            Dupla posFx = Dupla.Alinear(retorno, fx, Dupla.MEDIO, Dupla.ARRIBA);
            Dupla posExps = Dupla.Alinear(retorno, Expresiones, Dupla.MEDIO, Dupla.ABAJO);
            if (ÁrbolBinario_DibujarUnión_Ramas) {
                g.setStroke(ÁrbolBinario_BasicStroke);
                g.setColor(ÁrbolBinario_colorBordes);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Dupla p1 = posFx.Clon().Adicionar(new Dupla(ÁrbolBinario_Textura_Nodos).Mitad());
                for (Dupla pt : pts) {
                    g.drawLine(p1.intX(), p1.intY(), pt.intX(), pt.intY() + posExps.intY());
                }
            }
            if (ÁrbolBinario_DibujarTextura_Nodos) {
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

        abstract Object Proceso_Retorno(Expresiones... e) throws Exception;
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
        if (GuardarTextoDeAnalisis) {
            if (!retorno.equals(nombre)) {
                AñadirTextoDeAnalisis("Se han eliminado los acentos para normalizar");
                AñadirTextoDeAnalisis(retorno);
            }
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
        if (GuardarTextoDeAnalisis) {
            AñadirTextoDeAnalisis("Se ha normalizado " + nombre + "\n");
        }
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
        if (GuardarTextoDeAnalisis) {
            AñadirTextoDeAnalisis("La variable no se encontró");
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
                Object Proceso_Retorno(Expresiones[] e) throws Exception {
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

        if (Admitir_MultiplicacionesImplicitas) {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
        nombre = nombre.replace("cosinus", "cos");
        nombre = nombre.replace("cosin", "cos");
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

        {//Eliminar acentos de la función
            String ConAcento = "ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ"
                    + "ĀāĂăĄąĆćĈĉĊċČčĎďĐđĒēĔĕĖėĘęĚěĜĝĞğĠġȑȒȓȔȕȗȘș";
            String SinAcento = "AAAAAAACEEEEIIIIDNOOOOOOUUUUYBaaaaaaaceeeeiiiionoooooouuuuyby"
                    + "AaAaAaCcCcCcCcDdDdEeEeEeEeEeGgGgGgrRrUuuSs";
            if (ConAcento.length() != SinAcento.length()) {
                throw new RuntimeException("Revise las cadenas para la sustitución de acentos, longitudes diferentes");
            }
            int ejemplares = ConAcento.length();
            for (int i = 0; i < ejemplares; i++) {
                nombre = nombre.replace(ConAcento.charAt(i), SinAcento.charAt(i));
            }
        }
        switch (nombre) {
            case "aleatorio":
            case "rand":
            case "random":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función aleatorio consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        if (e != null || e.length > 0) {
                            throw new Exception("La función aleatorio no recibe parámetros");
                        }
                        return Math.random();
                    }
                };
            case "aleatorioentre":
            case "randombetween":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función aleatorio entre consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double vi = e[0].doubleValue();
                        double vf = e[1].doubleValue();
                        return (vf - vi) * random() + vi;
                    }
                };
            case "aleatorioenteroentre":
            case "randomintegerbetween":
            case "randintbetween":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función aleatorio entero entre consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double vi = e[0].doubleValue();
                        double vf = e[1].doubleValue();
                        return Math.round((vf - vi) * random() + vi);
                    }
                };
            case "0"://Funcionará cuando la simplificación automatica esté desactivada
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función nula consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return 0;
                    }
                };
            case "-"://Funcionará cuando la simplificación automatica esté desactivada
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función de negación consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Math.toRadians(valor);
                    }
                };
            case "sen":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función seno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double θ = e[0].doubleValue();
                        return Math.sin(θ);
                    }
                };
            case "cos":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double θ = e[0].doubleValue();
                        return Math.cos(θ);
                    }
                };
            case "tan":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double θ = e[0].doubleValue();
                        return Math.tan(θ);
                    }
                };
            case "sec":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double θ = e[0].doubleValue();
                        return 1 / Math.cos(θ);
                    }
                };
            case "csc":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double θ = e[0].doubleValue();
                        return 1 / Math.sin(θ);
                    }
                };
            case "cot":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double θ = e[0].doubleValue();
                        return 1 / Math.tan(θ);
                    }
                };
            case "asen":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función seno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Math.asin(valor);
                    }
                };
            case "acos":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Math.acos(valor);
                    }
                };
            case "atan":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Math.atan(valor);
                    }
                };
            case "atan2":
            case "angulo":
            case "θ":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double Y = e[0].doubleValue();
                        double X = e[0].doubleValue();
                        return Math.atan2(Y, X);
                    }
                };
            case "senh":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función seno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Math.sinh(valor);
                    }
                };
            case "cosh":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Math.cosh(valor);
                    }
                };
            case "tanh":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Math.tanh(valor);
                    }
                };
            case "asenh":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función seno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double x = e[0].doubleValue();
                        return Math.log(x + Math.sqrt(x * x + 1));
                    }
                };
            case "acosh":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double x = e[0].doubleValue();
                        return Math.log(x + Math.sqrt(x * x - 1));
                    }
                };
            case "atanh":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double x = e[0].doubleValue();
                        return .5 * Math.log((1 + x) / (1 - x));
                    }
                };
            case "sech":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return 1 / Math.cosh(valor);
                    }
                };
            case "csch":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return 1 / Math.sinh(valor);
                    }
                };
            case "coth":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función coseno consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return 1 / Math.tanh(valor);
                    }
                };
            case "raiz":
            case "root":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        switch (e.length) {
                            case 1:
                                return Math.sqrt(valor);
                            case 2:
                                double valor2 = e[1].doubleValue();
                                return Math.pow(valor, 1.0 / valor2);
                            default:
                                throw new RuntimeException("Argumentos inválidos para la raíz");
                        }
                    }
                };
            case "rcuadrada":
            case "raizcuadrada":
            case "sqrt":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz cuadrada consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Math.pow(valor, 1 / 2f);
                    }
                };
            case "rcubica":
            case "raizcubica":
            case "cubicroot":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz cuadrada consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Math.pow(valor, 1 / 3f);
                    }
                };
            case "rcuarta":
            case "raizcuarta":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz cuarta consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return raiz4(valor);
                    }
                };
            case "potencia":
            case "pot":
            case "poten":
            case "pow":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función potencia consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return Math.pow(valor, valor2);
                    }
                };
            case "pcuadrada":
            case "potencuadrada":
            case "potenciacuadrada":
            case "cuadrado":
            case "sqr":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función potencia cuadrada consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return valor * valor;
                    }
                };
            case "pcubica":
            case "potencubica":
            case "potenciacubica":
            case "cubico":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función potencia cubica consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return valor * valor * valor;
                    }
                };
            case "pcuarta":
            case "potencuarta":
            case "potenciacuarta":
            case "cuarta":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función potencia cuarta consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return valor * valor * valor;
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Factorial(valor);
                    }
                };
            case "gamma":
            case "Γ":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función gamma consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Math.log(valor);
                    }
                };
            case "log":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        if (e.length == 1) {
                            return Math.log(valor);
                        } else {
                            double valor2 = e[1].doubleValue();
                            return Math.log(valor) / Math.log(valor2);
                        }
                    }
                };
            case "sgn":
            case "signo":
            case "signum":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función signo consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Sgn(valor);
                    }
                };
            case "abs":
            case "norma":
            case "modulo":
            case "magnitud":
            case "longitud":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función valor absoluto consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        switch (e.length) {
                            case 0:
                                throw new RuntimeException("No hay argumentos para evaluar en la función abs");
                            case 1:
                                double n = e[0].doubleValue();
                                return Math.abs(n);
                            default:
                                return Norma(ConvArreglo_double(e));
                        }
                    }
                };
            case "pitagoras":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función pitagoras consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return Pitagoras(valor, valor2);
                    }
                };
            case "redondeo":
            case "round":
            case "rnd":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return Math.round(valor);
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return floor(valor);
                    }
                };
            case "parteentera":
            case "pe":
            case "entero":
            case "int":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return (int) valor;
                    }
                };
            case "partedecimal":
            case "pd":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        return floor(valor);
                    }
                };
            case "permut":
            case "permutacion":
            case "p":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return CR(valor, valor2);
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valor = e[0].doubleValue();
                        double valor2 = e[1].doubleValue();
                        return fracción(valor, valor2);
                    }
                };
            case "mod":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función raiz consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        if (!e[1].esVariable()) {
                            throw new RuntimeException("El segundo parámetro debe ser una variable");
                        }
                        String var = e[1].TextoGenerador;
                        if (auxDx.Expresiones.isEmpty()) {
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
                            return f.Apróx_Derivada(e[2].ObtenerValor());
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        if (!e[1].esVariable()) {
                            throw new RuntimeException("El segundo parámetro debe ser una variable");
                        }
                        String var = e[1].TextoGenerador;
                        if (auxDx.Expresiones.isEmpty()) {
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
                            return f.Apróx_Integral(e[2].ObtenerValor(), e[3].ObtenerValor());
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valores[] = new double[e.length];
                        for (int i = 0; i < valores.length; i++) {
                            valores[i] = e[i].doubleValue();
                        }
                        return Norma(valores);
                    }
                };
            case "mayor":
            case "max":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valores[] = new double[e.length];
                        for (int i = 0; i < valores.length; i++) {
                            valores[i] = e[i].doubleValue();
                        }
                        return Máx(valores);
                    }
                };
            case "menor":
            case "min":
                if (GuardarTextoDeAnalisis) {
                    AñadirTextoDeAnalisis("Función factorial consultada");
                }
                return new FunciónDeRetonoNúmerico(nombre) {
                    @Override
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valores[] = new double[e.length];
                        for (int i = 0; i < valores.length; i++) {
                            valores[i] = e[i].doubleValue();
                        }
                        return Mín(valores);
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
                    Object Proceso_Retorno(Expresiones[] e) throws Exception {
                        double valores[] = new double[e.length];
                        for (int i = 0; i < valores.length; i++) {
                            valores[i] = e[i].doubleValue();
                        }
                        return Promedio(valores);
                    }
                };
        }
        FunciónDeRetonoNúmerico f = funciónInteractiva(nombre);
        if (f != null) {
            return f;
        }
        throw new FunciónNoReconocida("Función no reconocida");
    }

    FunciónDeRetonoNúmerico funciónInteractiva(String nombre) throws Exception {
        System.out.println("Interactiva........" + nombre);
        int op = -1;
        String funcionesInteractivas[] = {
            "raiz", "root", 
            "poten", "potencia", "pot", "pow", 
            "log", 
            "sen", "asen", "senh", "asenh", "cos", "acos", "cosh", "acosh", "tan", "atan", "tanh", 
            "atanh", "sec", "asec", "sech", "asech", "csc","acsc", "csch", "cot", "acot", "coth", 
            "acsch", "acoth"
        };
        for (int i = 0; i < funcionesInteractivas.length; i++) {
            if (nombre.startsWith(funcionesInteractivas[i])) {
                op = i;
                System.out.println("Se encontró " + op);
                break;
            }
        }
        if (op != -1) {
            String TxtIndice = nombre.substring(
                    funcionesInteractivas[op].length(), nombre.length()
            );
            Expresiones índice = new Expresiones(TxtIndice);
            if (GuardarTextoDeAnalisis) {
                AñadirTextoDeAnalisis("interacción encontrada");
                AñadirTextoDeAnalisis("El indice es: " + TxtIndice);
            }

            FunciónDeRetonoNúmerico f = BibliotecaDeFunciones(
                    funcionesInteractivas[op]
            );
            final int operación = op;
            return new FunciónDeRetonoNúmerico(nombre) {
                @Override
                Object Proceso_Retorno(Expresiones[] e) throws Exception {
                    if (operación < 7) {
                        return f.Proceso_Retorno(e[0], índice);
                    }
                    return Math.pow((double) f.Proceso_Retorno(e), índice.ObtenerValor());
                }
            };
        }
        System.out.println("No se encontró " + op);
        return null;
    }

    public class FunciónNoReconocida extends Exception {

        public FunciónNoReconocida(String message) {
            super(message);
        }
    }

}
