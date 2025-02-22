package HerramientasMatemáticas;

import java.util.ArrayList;

public class InterpreteExprecionesAlgebraicas {

    public static void main(String[] args) {
        String expresión = "(((1+2)+(3+4)-5+6+7))";
        InterpreteExprecionesAlgebraicas iea = new InterpreteExprecionesAlgebraicas(expresión);
        System.out.println(iea.texto());
        System.out.println(iea.valor());
        System.out.println(iea.valorTextual());
    }

    final ArrayList<Variable> variables = new ArrayList<>();
    final ArrayList<Función> funciones = new ArrayList<>();

    Elemento e;

    public InterpreteExprecionesAlgebraicas(String Expresión) {
        e = new Elemento(Expresión);
        while (e.númeroAbstracto instanceof Función) {
            Función f = (Función) e.númeroAbstracto;
            if (f.nombre.isEmpty()) {
                e = f.elemento[0];
            } else {
                break;
            }
        }
    }

    double valor() {
        return e.ValorNumérico();
    }

    String valorTextual() {
        return new Constante(e.ValorNumérico() + "").toString();
    }

    String texto() {
        return e.toString();
    }

    static abstract class númeroAbstracto extends Number {

        boolean positivo = true;

        abstract double ValorNumérico();

        void cambiarSigno() {
            positivo = !positivo;
        }

        @Override
        abstract public String toString();

        @Override
        public int intValue() {
            return (int) ValorNumérico();
        }

        @Override
        public long longValue() {
            return (long) ValorNumérico();
        }

        @Override
        public float floatValue() {
            return (float) ValorNumérico();
        }

        @Override
        public double doubleValue() {
            return ValorNumérico();
        }
    }

    Variable consultarVariable(String nombre) {
        for (Variable variable : variables) {
            if (variable.nombre.equals(nombre)) {
                return variable;
            }
        }
        return null;
    }

    FunciónTransformadora consultarFunción(String nombre) {
        for (Función función : funciones) {
            if (función.nombre.equals(nombre)) {
                return función.transformadora;
            }
        }
        return null;
    }

    FunciónTransformadora bibliotecaFuncionesTransformadoras(String nombre) {
        System.out.println("Se consulto: " + nombre);
        switch (nombre) {
            case "":
                return (Elemento... e) -> {
                    return e[0].ValorNumérico();
                };
        }
        System.out.println("No se encontro: " + nombre);
        return null;
    }

    class Elemento extends númeroAbstracto {

        númeroAbstracto númeroAbstracto;

        public Elemento(String expresión) {
            if (expresión == null || expresión.isEmpty()) {
                throw new RuntimeException("La expresión está vacía");
            }
            try {
                númeroAbstracto = new Constante(expresión);
                return;
            } catch (Exception e) {
                System.out.println("No es constante: " + e.getMessage());
            }
            númeroAbstracto = consultarVariable(expresión);
            if (númeroAbstracto != null) {
                return;
            }
            try {
                númeroAbstracto = new OperaciónBinaria(expresión);
                return;
            } catch (Exception e) {
                System.out.println("No es operación binaria: " + e.getMessage());
            }
            try {
                númeroAbstracto = new Función(expresión);
                return;
            } catch (Exception e) {
                System.out.println("No es función: " + e.getMessage());
            }
            if (númeroAbstracto == null) {
                throw new RuntimeException("No se pudo reconocer: " + expresión);
            }
        }

        @Override
        double ValorNumérico() {
            return númeroAbstracto.ValorNumérico();
        }

        @Override
        public String toString() {
            return númeroAbstracto.toString();
        }

    }

    class Constante extends númeroAbstracto {

        double valor;

        public Constante(String valor) {
            this.valor = Double.parseDouble(valor);
        }

        @Override
        double ValorNumérico() {
            return valor;
        }

        @Override
        public String toString() {
            double d = ValorNumérico();
            for (int i = 5; i >= 0; i--) {
                String n = String.format("%." + i + "f", d);
                if (n.charAt(n.length() - 1) == '0') {
                    continue;
                }
                return n;
            }
            return (long) d + "";
        }
    }

    class Variable extends númeroAbstracto {

        String nombre;
        double valor;

        public Variable(String nombre, double valor) {
            this.nombre = nombre;
            this.valor = valor;
        }

        @Override
        double ValorNumérico() {
            return valor;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    interface FunciónTransformadora {

        double transformar(Elemento... e);

    }

    class Función extends númeroAbstracto {

        String nombre;
        FunciónTransformadora transformadora;
        Elemento[] elemento;

        public Función(String Expresión) {
            if (!Expresión.endsWith(")")) {
                throw new RuntimeException("Las funciones deben terminar en  \")\"");
            }
            int primerParentesis = Expresión.indexOf("(");
            nombre = Expresión.substring(0, primerParentesis);
            transformadora = consultarFunción(nombre);
            if (transformadora == null) {
                transformadora = bibliotecaFuncionesTransformadoras(nombre);
                if (transformadora != null) {
                    funciones.add(this);
                }
            }
            if (transformadora == null) {
                throw new RuntimeException("No se pudo generar la función: " + nombre);
            }
            String[] argumentos = Expresión.substring(
                    primerParentesis + 1, Expresión.length() - 1
            ).split(",");
            elemento = new Elemento[argumentos.length];
            for (int i = 0; i < argumentos.length; i++) {
                elemento[i] = new Elemento(argumentos[i]);
            }
        }

        @Override
        double ValorNumérico() {
            return transformadora.transformar(elemento);
        }

        @Override
        public String toString() {
            String retorno = nombre + "(" + elemento[0].toString();
            for (int i = 1; i < elemento.length; i++) {
                retorno += "," + elemento[1];
            }
            retorno += ")";
            return retorno;
        }
    }
    final char[][] operadores = {{'+', '-'}, {'*'}, {'/'}, {'^'}};

    class OperaciónBinaria extends númeroAbstracto {

        char operador;
        ArrayList<Elemento> Expresión = new ArrayList<>();

        public OperaciónBinaria(String expresión) {
            ArrayList<String> e = ExtraerElementos(expresión);
            int i = 0;
            for (String string : e) {
                Elemento element = new Elemento(string);
                char c = expresión.charAt(i);
                System.out.println("s: " + c + " : " + expresión);
                if (c == '-') {
                    element.cambiarSigno();
                }
                i += string.length();
                this.Expresión.add(element);
            }
            operador = expresión.charAt(e.get(0).length());
        }

        public OperaciónBinaria(Elemento[] expresión, char op) {
            for (Elemento e : expresión) {
                this.Expresión.add(e);
            }
            operador = op;
        }

        void RomperParentesis() {
            if (operador == '+') {

            }
            for (int i = 0; i < Expresión.size(); i++) {
                Elemento elemento = Expresión.get(i);
                Función f;
                if (elemento.númeroAbstracto instanceof Función) {
                    f = (Función) elemento.númeroAbstracto;
                    if (f.nombre.isEmpty()) {
                        Expresión.remove(i);
                        Expresión.add(i, f.elemento[0]);
                    }
                } else {
                    continue;
                }
            }

        }

        @Override
        double ValorNumérico() {
            double resultado = Expresión.get(0).ValorNumérico();
            for (int i = 1; i < Expresión.size(); i++) {
                double e = Expresión.get(i).ValorNumérico();
                resultado = Operar(resultado, e);
            }
            return resultado;
        }

        @Override
        public String toString() {
            String retorno = Expresión.get(0).toString();
            for (int i = 1; i < Expresión.size(); i++) {
                retorno += operador + Expresión.get(i).toString();
            }
            return retorno;
        }

        double Operar(double a, double b) {
            switch (operador) {
                case '+':
                case '-':
                    return a + b;
                case '*':
                    return a * b;
                case '/':
                    return a / b;
                case '^':
                    return Math.pow(a, b);
                default:
                    throw new RuntimeException(
                            "La operación binaria con este operador no está definida " + operador
                    );
            }
        }

        ArrayList<String> ExtraerElementos(String expresión) {
            ArrayList<String> e = new ArrayList<>();
            int posOp = PosiciónPrimerOperadorPrincipal(expresión);
            if (posOp == -1) {
                throw new RuntimeException(
                        "La expresión: " + expresión + " No tiene al menos un operador principal"
                );
            }
            char op = expresión.charAt(posOp);
            String e1 = expresión.substring(0, posOp);
            String e2 = expresión.substring(posOp + 1, expresión.length());
            e.add(e1);
            return ExtraerElementos(e2, op, e);
        }

        ArrayList<String> ExtraerElementos(String expresión, char op, ArrayList<String> e) {
            int posOp = PosiciónPrimerOperadorPrincipal(expresión);
            if (posOp == -1 || op != expresión.charAt(posOp)) {
                e.add(expresión);
                return e;
            }
            String e1 = expresión.substring(0, posOp);
            String e2 = expresión.substring(posOp + 1, expresión.length());
            e.add(e1);
            return ExtraerElementos(e2, op, e);
        }

        boolean esOperadorBinario(char op) {
            return valorOperadorBinario(op) != -1;
        }

        int valorOperadorBinario(char op) {
            for (int i = 0; i < operadores.length; i++) {
                for (int j = 0; j < operadores[i].length; j++) {
                    if (operadores[i][j] == op) {
                        return i;
                    }
                }
            }
            return -1;
        }

        int PosiciónPrimerOperadorPrincipal(String expresión) {
            if (esOperadorBinario(expresión.charAt(expresión.length() - 1))) {
                throw new RuntimeException(
                        "Las expresiones no pueden terminar en un oprador binario"
                );
            }
            int pos = -1;
            int val = -1;
            int agrupación = 0;
            for (int i = 0; i < expresión.length(); i++) {
                if (agrupación < 0) {
                    throw new RuntimeException("Hay un parentesis de cierre sin apertura");
                }
                char c = expresión.charAt(i);
                if (i == 0 && (c == '+' || c == '-')) {
                    continue;//No importa si inicia por un operador de signo
                }
                if (c == '(') {
                    agrupación++;
                    continue;
                }
                if (c == ')') {
                    agrupación--;
                    continue;
                }
                if (agrupación == 0 && esOperadorBinario(c)) {
                    int candidato = valorOperadorBinario(c);
                    if (val == -1 || candidato < val) {
                        val = candidato;
                        pos = i;
                    }
                }
            }
            if (agrupación != 0) {
                throw new RuntimeException("Faltan parentesis en la expresión " + expresión);
            }
            return pos;
        }

    }

}
