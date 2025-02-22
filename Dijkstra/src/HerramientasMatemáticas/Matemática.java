//Última actualización 19/Febrero/2018
package HerramientasMatemáticas;

import java.util.ArrayList;
import static java.lang.Math.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Matemática {

    //<editor-fold defaultstate="collapsed" desc="Constantes e identificadores">
//<editor-fold defaultstate="collapsed" desc="Constantes matemáticas">
    public static final double PI = 3.141592653589793;
    public static final double e = 2.718281828459045;
    public static final double Phi = (1 + raiz2(5)) / 2;
    public static final double π = PI;
    public static final double phi = Phi;
    public static final double φ = phi;
    public static final double Φ = Phi;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Identificadores para especificar el tipo de ángulo">
    public static final byte ÁNGULO_GRADOS = 0;
    public static final byte ÁNGULO_RADIANES = 1;
    public static final byte ÁNGULO_PORCENTUAL = 2;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dentificadores para especificar el tipo de promedio">
    public static final byte PROMEDIO_ARITMÉTICO = 0;
    public static final byte PROMEDIO_ARMÓNICO = 1;
    public static final byte PROMEDIO_GEOMÉTRICO = 2;
//</editor-fold>
//</editor-fold>

    public static void main(String[] args) {
        double a = π;
        System.out.println(Truncar_Decimales(a, 6));
    }

    public static ArrayList<Integer> ObtenerPrimosEntre(int partida, int llegada) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Rango rango = new Rango(partida, llegada);
        ArrayList<Integer> ListaDinamicaDePrimos = new ArrayList<>();
        for (int i = rango.IntExtremoInicial(); i <= rango.ExtremoFinal; i += rango.Sentido) {
            if (esPrimo(i)) {
                ListaDinamicaDePrimos.add(i);
            }
        }
        return ListaDinamicaDePrimos;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones booleanas">
    public static boolean esEntero(double Número) {
        return Número == (int) Número;
    }

    public static boolean tieneParteDecimal(double Número) {
        return !esEntero(Número);
    }

    public static boolean esNatural(double Número) {
        return esEntero(Número) && esPositivo(Número);
    }

    public static boolean esNegativo(double Número) {
        return Número <= 0;
    }

    public static boolean esPositivo(double Número) {
        return Número >= 0;
    }

    public static boolean esImpar(double Número) {
        return !esPar(Número);
    }

    public static boolean esPar(double Número) {
        return Número % 2 == 0;
    }

    public static boolean EsDivisorDe(double a, double b) {
        return esEntero(b / a);
    }

    public static boolean EsMultiploDe(double a, double b) {
        return esEntero(a / b);
    }

    public static boolean esPrimo(double Número) {
        //<editor-fold defaultstate="collapsed" desc="Identifica si un número es primo o no">
        if (Número <= 1 || !esEntero(Número)) {
            return false;
        }
        int contador = 2;
        while (contador <= Número / 2) {
            if (Número % contador == 0) {
                return false;
            }
            contador += contador >= 3 ? 2 : 1;
        }
        return true;
//</editor-fold>
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones para conversión de ángulos">
    public static double Grados(double radian) {
        return radian * 180 / π;
    }

    public static double Radianes(double Grado) {
        return Grado * π / 180;
    }

    public static double Radianes(double θ, byte TIPO_ÁNGULO) {
        return TIPO_ÁNGULO == ÁNGULO_GRADOS ? Radianes(θ) : TIPO_ÁNGULO == ÁNGULO_PORCENTUAL ? θ * 2 * π : θ;
    }

    public static double Grados(double θ, byte TIPO_ÁNGULO) {
        return TIPO_ÁNGULO == ÁNGULO_PORCENTUAL ? θ * 360 : TIPO_ÁNGULO == ÁNGULO_RADIANES ? Matemática.Grados(θ) : θ;
    }

    public static double ReducciónAngular(double θ) {
        double Rev = 2 * π;
        double p = ParteDecimal(θ / Rev);
        return θ < 0 ? Rev * (p + 1) : p * Rev;
    }

    public static double ReducciónAngular(double θ, byte TIPO_ÁNGULO) {
        double m = TIPO_ÁNGULO == ÁNGULO_GRADOS ? 180 / π : TIPO_ÁNGULO == ÁNGULO_PORCENTUAL ? 1 / (2 * π) : 1;
        return ReducciónAngular(Radianes(θ, TIPO_ÁNGULO)) * m;
    }
//</editor-fold>

    public static double Fracción(double numerador, double denominador) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        return numerador / denominador;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones que usan a la función Gamma Γ(x)">
    public static double Γ(double z) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de la función Gamma  Γ(x)">
        if (z < 0.5) {
            return π / (sen(π * z) * Γ(1 - z));
        }
        z--;
        double C[] = {
            676.5203681218851, -1259.1392167224028, 771.32342877765313, -176.61502916214059,
            12.507343278686905, -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7
        };
        int C_ejemplares = C.length;
        double Ag = 0.99999999999980993, t = z + (C_ejemplares - 1) + 0.5;
        for (int k = 0; k < C_ejemplares; k++) {
            Ag += C[k] / (z + k + 1);
        }
        return raiz2(2 * π) * poten(t, z + 0.5) * poten(e, -t) * Ag;
//</editor-fold>
    }

    public static double Gamma(double n) {
        return Γ(n);
    }

    //<editor-fold defaultstate="collapsed" desc="Distribución de probabilidad">
    public static double Gamma_DistribuciónDeProbabilidad(double α, double ß, double X) {
        Función GammaDist = (double x) -> Fracción(poten(x, α - 1) * poten(e, -x / ß), poten(ß, α) * Γ(α));
        return X <= 0 ? 0 : GammaDist.Apróx_Integral(0, X);
    }

    public static double Gamma_DistribuciónDeProbabilidad(double α, double ß, double Xi, double Xf) {
        return Gamma_DistribuciónDeProbabilidad(α, ß, Xf) - Gamma_DistribuciónDeProbabilidad(α, ß, Xi);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Fuonción factorial de un número">
    public static long Factorial(long n) {
        if (n == 0) {
            return 1;
        } else if (n < 0) {
            long k = abs(n);
            return -(k * Factorial(k - 1));
        } else {
            return n * Factorial(n - 1);
        }
    }

    public static double Factorial(double n) {
        if (n <= 12 && n >= -12 && esEntero(n)) {
            return Factorial((long) n);
        }
        if (esPositivo(n)) {
            return Gamma(n + 1);
        }
        return -Gamma(-n + 1);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="funciones de combinatoria y permutación">
    //<editor-fold defaultstate="collapsed" desc="Permutación P(x)">
    //<editor-fold defaultstate="collapsed" desc="Implementación para números enteros long">
    public static long Permutación(long n, long k) {
        return (long) (Factorial(n) / Factorial(n - k));
    }

    public static long P(long n, long k) {
        return Permutación(n, k);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Implementación para números double">
    public static double Permutación(double n, double k) {
        return Factorial(n) / Factorial(n - k);
    }

    public static double P(double n, double k) {
        return Permutación(n, k);
    }
//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Combinatoria sin repetición">
    //<editor-fold defaultstate="collapsed" desc="Implementación para double">
    public static double CombinatoriaSinRepetición(double n, double k) {
        return Factorial(n) / (Factorial(k) * Factorial(n - k));
    }

    public static double C(double n, double k) {
        return CombinatoriaSinRepetición(n, k);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Implementación para enteros de tipo long">
    public static long CombinatoriaSinRepetición(long n, long k) {
        return (long) (Factorial(n) / (Factorial(k) * Factorial(n - k)));
    }

    public static long C(long n, long k) {
        return CombinatoriaSinRepetición(n, k);
    }
//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Combinatoria con repetición">
//<editor-fold defaultstate="collapsed" desc="Implementación para números double">
    public static double CombinatoriaConRepetición(double n, double k) {
        return C(n + k - 1, k);
    }

    public static double CR(double n, double k) {
        return CombinatoriaConRepetición(n, k);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Implementación para números enteros long">
    public static long CombinatoriaConRepetición(long n, long k) {
        return C(n + k - 1, k);
    }

    public static long CR(int n, long k) {
        return CombinatoriaConRepetición(n, k);
    }
//</editor-fold>
//</editor-fold>
//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Factorización de un número">
    public static int[] FACT(int n) {
        return Factorizar(n);
    }

    public static int[] Factorizar(int n) {
        if (!esEntero(n) || n < 2) {
            return new int[]{0};
        }
        ArrayList<Integer> FACT = new ArrayList<>();
        int k = n;
        for (int i = 2; i <= k; i++) {
            if (EsDivisorDe(i, k)) {
                FACT.add(i);
                k /= i;
                i = 1;
            }
        }
        int[] fact = new int[FACT.size()];
        int c = 0;
        for (int i : FACT) {
            fact[c++] = i;
        }
        return fact;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones de aleatorio">
    public static double AleatorioEntre(double vi, double vf) {
        return (vf - vi) * random() + vi;
    }

    public static int AleatorioEnteroEntre(double vi, double vf) {
        return Redondeo(AleatorioEntre(vi, vf));
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones para logaritmos">
    public static double Log(double Número, double Base) {
        return log(Número) / log(Base);
    }

    public static double Ln(double Número) {
        return log(Número);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones para raices">
    public static double raiz4(double Número) {
        return pow(Número, 1 / 4f);
    }

    public static double raiz3(double Número) {
        return pow(Número, 1 / 3f);
    }

    public static double raiz2(double Número) {
        return sqrt(Número);
    }

    public static double raiz(double Número, double Índice) {
        return pow(Número, 1.0 / Índice);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="funciones para potencias">
    public static double poten4(double base) {
        return poten(base, 4);
    }

    public static double poten3(double base) {
        return base * base * base;
    }

    public static double poten2(double base) {
        return base * base;
    }

    public static double poten(double base, double Exponente) {
        return pow(base, Exponente);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones de trigónometria hiperbólica">
    //<editor-fold defaultstate="collapsed" desc="Funciones hiperbólicas">
    public static double Senh(double x) {
        return sinh(x);
    }

    public static double Tanh(double x) {
        return tanh(x);
    }

    public static double Sech(double x) {
        return 1 / cosh(x);
    }

    public static double Csch(double x) {
        return 1 / Senh(x);
    }

    public static double Coth(double x) {
        return 1 / Tanh(x);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones Inversas Hiperbólicas">
    public static double Atanh(double x) {
        return (1 / 2.0) * (Ln(Fracción(1 + x, 1 - x)));
    }

    public static double Asenh(double x) {
        return Ln(x + raiz2(x * x + 1));
    }

    public static double Acosh(double x) {
        return Ln(x + raiz2(x * x - 1));
    }
//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones Trigónometricas">
    //<editor-fold defaultstate="collapsed" desc="Funciones de coseno">
    public static double Cos2(double θ) {
        return poten2(cos(θ));
    }

    public static double Cos3(double θ) {
        return poten3(cos(θ));
    }

    public static double Cos4(double θ) {
        return poten4(cos(θ));
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones de Seno">
    public static double sen(double θ) {
        return sin(θ);
    }

    public static double sen2(double θ) {
        return poten2(sen(θ));
    }

    public static double sen3(double θ) {
        return poten3(sen(θ));
    }

    public static double sen4(double θ) {
        return poten4(sen(θ));
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Demás funciones trigónometricas">
    public static double Cot(double θ) {
        return 1 / Tan(θ);
    }

    public static double Csc(double θ) {
        return 1 / sen(θ);
    }

    public static double Sec(double θ) {
        return 1 / cos(θ);
    }

    public static double Tan(double θ) {
        return tan(θ);
    }

    public static double ASen(double y) {
        return asin(y);
    }
//</editor-fold>

    public static double Atan2(double Y, double X) {
        return atan2(Y, X);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones especiales">
    public static int Sgn(double x) {
        return (int) signum(x);
    }

    public static int Redondeo(double Número) {
//        return Piso(Número + 0.5);
        return (int) round(Número);
    }

    public static long Piso(double Número) {
        return (long) floor(Número);
        //<editor-fold defaultstate="collapsed" desc="Código alternativo por definición matemática">
//        return Número >= 0 || esEntero(Número) ? (int) Número : ((int) Número - 1);
//</editor-fold>
    }

    public static int Techo(double Número) {
//        return (int) (esEntero(Número) ? Número : Piso(Número) + 1);
        return (int) ceil(Número);
    }

    public static int ParteEntera(double Número) {
        return (int) Número;
    }

    public static double ParteDecimal(double Número) {
        return abs(Número - ParteEntera(Número));
    }

    public static double Truncar_Decimales(double x, final int n) {
        //<editor-fold defaultstate="collapsed" desc="Implementación del código">
        int a = (int) pow(10, n);
        if (x > 0) {
            return floor(a * x) / a;
        } else {
            return ceil(a * x) / a;
        }
//</editor-fold>
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones para vectores, Algebra Lineal">
//<editor-fold defaultstate="collapsed" desc="Funciones que usan el producto cruz entre 2 vectores">
    public static double[] ProductoCrúz(double[] U, double[] V) {
        return new double[]{
            U[1] * V[2] - U[2] * V[1],
            U[2] * V[0] - U[0] * V[2],
            U[0] * V[1] - U[1] * V[0]
        };
    }

    public static double ÁreaTriángulo(double[] U, double[] V) {
        return ÁreaParalelogramo(U, V) / 2;
    }

    public static double ÁreaParalelogramo(double[] U, double[] V) {
        return Norma(ProductoCrúz(U, V));
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Multiplicación vector Matriz">
    public static double[] MultiplicarVectorMatriz(double[] Vector, double[][] Matriz) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Vector.length != Matriz.length) {
            throw new Error("Longitudes diferentes, V: " + Vector.length + ", M: " + Matriz.length);
        }
        double[] R = new double[Matriz[0].length];
        for (int i = 0; i < Matriz[0].length; i++) {
            for (int j = 0; j < Vector.length; j++) {
                R[i] += Vector[j] * Matriz[j][i];
            }
        }
        return R;
//</editor-fold>
    }

    public static float[] MultiplicarVectorMatriz(float[] Vector, float[][] Matriz) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Vector.length != Matriz.length) {
            throw new Error("Longitudes diferentes, V: " + Vector.length + ", M: " + Matriz.length);
        }
        float[] R = new float[Matriz[0].length];
        for (int i = 0; i < Matriz[0].length; i++) {
            for (int j = 0; j < Vector.length; j++) {
                if (Matriz[j][i] == 0) {
                    continue;
                }
                R[i] += Vector[j] * Matriz[j][i];
            }
        }
        return R;
//</editor-fold>
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Norma de un vector">
    public static double Norma(Number... componentes) {
        return Norma(ConvVectDouble(componentes));
    }

    public static double Norma(double... componentes) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        double Sumatoria = 0;
        for (double d : componentes) {
            Sumatoria += d * d;
        }
        return Math.sqrt(Sumatoria);
//</editor-fold>
    }
//</editor-fold>

    public static double[] ConvVectDouble(Object vect) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        double[] retorno;
        if (vect instanceof float[]) {
            float[] nvect = ((float[]) vect);
            retorno = new double[nvect.length];
            for (int i = 0; i < retorno.length; i++) {
                retorno[i] = nvect[i];
            }
        } else if (vect instanceof int[]) {
            int[] nvect = ((int[]) vect);
            retorno = new double[nvect.length];
            for (int i = 0; i < retorno.length; i++) {
                retorno[i] = nvect[i];
            }
        } else if (vect instanceof long[]) {
            long[] nvect = ((long[]) vect);
            retorno = new double[nvect.length];
            for (int i = 0; i < retorno.length; i++) {
                retorno[i] = nvect[i];
            }
        } else if (vect instanceof byte[]) {
            byte[] nvect = ((byte[]) vect);
            retorno = new double[nvect.length];
            for (int i = 0; i < retorno.length; i++) {
                retorno[i] = nvect[i];
            }
        } else if (vect instanceof short[]) {
            short[] nvect = ((short[]) vect);
            retorno = new double[nvect.length];
            for (int i = 0; i < retorno.length; i++) {
                retorno[i] = nvect[i];
            }
        } else if (vect instanceof Number[]) {
            Number[] nvect = ((Number[]) vect);
            retorno = new double[nvect.length];
            for (int i = 0; i < retorno.length; i++) {
                retorno[i] = nvect[i].doubleValue();
            }
        } else {
            throw new RuntimeException("No se reconocen los números para realizar la operación");
        }
        return retorno;
//</editor-fold>
    }
//</editor-fold>

    public static double TeoremaDePitagoras(double a, double b) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        return raiz2(poten2(a) + poten2(b));
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones para conjuntos de números">
    //<editor-fold defaultstate="collapsed" desc="Funciones máximo y mínimo de un conjunto de números">
    //Realización: https://youtu.be/CkYKGlr0Tu4
    public static double Máx(double... TérminosDeComparación) {
        //<editor-fold defaultstate="collapsed" desc="Escoge el mayor de todos los números">
        double NúmeroMayor = TérminosDeComparación[0];
        for (int i = 1; i < TérminosDeComparación.length; i++) {
            double Número = TérminosDeComparación[i];
            if (Número > NúmeroMayor) {
                NúmeroMayor = Número;
            }
        }
        return NúmeroMayor;
//</editor-fold>
    }

    //Realización: https://youtu.be/CkYKGlr0Tu4
    public static double Mín(double... TérminosDeComparación) {
        //<editor-fold defaultstate="collapsed" desc="Escoge el menor de todos los números">
        double NúmeroMenor = TérminosDeComparación[0];
        for (int i = 1; i < TérminosDeComparación.length; i++) {
            double Número = TérminosDeComparación[i];
            if (Número < NúmeroMenor) {
                NúmeroMenor = Número;
            }
        }
        return NúmeroMenor;
//</editor-fold>
    }

    //Realización: https://youtu.be/CkYKGlr0Tu4
    public static float Máx(float... TérminosDeComparación) {
        return (float) Máx(ConvVectDouble(TérminosDeComparación));
    }

    //Realización: https://youtu.be/CkYKGlr0Tu4
    public static int Máx(int... TérminosDeComparación) {
        return (int) Máx(ConvVectDouble(TérminosDeComparación));
    }

    //Realización: https://youtu.be/CkYKGlr0Tu4
    public static long Máx(long... TérminosDeComparación) {
        return (long) Máx(ConvVectDouble(TérminosDeComparación));
    }

    //Realización: https://youtu.be/CkYKGlr0Tu4
    public static double Máx(Object TérminosDeComparación) {
        return Máx(ConvVectDouble(TérminosDeComparación));
    }

    //Realización: https://youtu.be/CkYKGlr0Tu4
    public static float Mín(float... TérminosDeComparación) {
        return (float) Mín(ConvVectDouble(TérminosDeComparación));
    }

    //Realización: https://youtu.be/CkYKGlr0Tu4
    public static int Mín(int... TérminosDeComparación) {
        return (int) Mín(ConvVectDouble(TérminosDeComparación));
    }

    //Realización: https://youtu.be/CkYKGlr0Tu4
    public static long Mín(long... TérminosDeComparación) {
        return (long) Mín(ConvVectDouble(TérminosDeComparación));
    }

    //Realización: https://youtu.be/CkYKGlr0Tu4
    public static double Mín(Object TérminosDeComparación) {
        return Mín(ConvVectDouble(TérminosDeComparación));
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones de Sumatoria para un conjunto de números">
    public static int Sumatoria(int... Números) {
        return (int) Sumatoria(ConvVectDouble(Números));
    }

    public static long Sumatoria(long... Números) {
        return (long) Sumatoria(ConvVectDouble(Números));
    }

    public static float Sumatoria(float... Números) {
        return (float) Sumatoria(ConvVectDouble(Números));
    }

    public static double Sumatoria(Object Números) {
        return Sumatoria(ConvVectDouble(Números));
    }

    public static double Sumatoria(double... Números) {
        //<editor-fold defaultstate="collapsed" desc="Calcula la suma de una lista de números">
        double suma = Números[0];
        for (int i = 1; i < Números.length; i++) {
            suma += Números[i];
        }
        return suma;
//</editor-fold>
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones de Promedio para un conjunto de números">
//Realización: https://youtu.be/Q5dEztC42e8
    public static double Promedio(Object Datos) {
        return Promedio(ConvVectDouble(Datos));
    }

    //Realización: https://youtu.be/Q5dEztC42e8
    public static double Promedio(double... Datos) {
        return PromedioAritmético(Datos);
    }

    //Realización: https://youtu.be/Q5dEztC42e8
    public static double PromedioAritmético(Object Datos) {
        return PromedioAritmético(ConvVectDouble(Datos));
    }

    //Realización: https://youtu.be/Q5dEztC42e8
    public static double PromedioAritmético(double... Datos) {
        return Promedio(PROMEDIO_ARITMÉTICO, Datos);
    }

    //Realización: https://youtu.be/Q5dEztC42e8
    public static double PromedioArmónico(Object Datos) {
        return PromedioArmónico(ConvVectDouble(Datos));
    }

    //Realización: https://youtu.be/Q5dEztC42e8
    public static double PromedioArmónico(double... Datos) {
        return Promedio(PROMEDIO_ARMÓNICO, Datos);
    }

    //Realización: https://youtu.be/Q5dEztC42e8
    public static double PromedioGeométrico(Object Datos) {
        return PromedioGeométrico(ConvVectDouble(Datos));
    }

    //Realización: https://youtu.be/Q5dEztC42e8
    public static double PromedioGeométrico(double... Datos) {
        return Promedio(PROMEDIO_GEOMÉTRICO, Datos);
    }

    //Realización: https://youtu.be/Q5dEztC42e8
    public static double Promedio(byte TIPO_PROMEDIO, Object Datos) {
        return Promedio(TIPO_PROMEDIO, ConvVectDouble(Datos));
    }

    //Realización: https://youtu.be/Q5dEztC42e8
    public static double Promedio(byte TIPO_PROMEDIO, double... Datos) {
        //<editor-fold defaultstate="collapsed" desc="Calcula el promedio especificado de un conjunto de números">
        int CantidadDatos = Datos.length;
        switch (TIPO_PROMEDIO) {
            case PROMEDIO_ARITMÉTICO:
                //(x1+x2+x3+...+xn)/n
                double suma = Datos[0];
                for (int i = 1; i < CantidadDatos; i++) {
                    suma += Datos[i];
                }
                return suma / CantidadDatos;
            case PROMEDIO_ARMÓNICO:
                //n/(1/x1+1/x2+1/x3+...+1/xn)
                double sumaReciproca = 1 / Datos[0];
                for (int i = 1; i < CantidadDatos; i++) {
                    sumaReciproca += 1 / Datos[i];
                }
                return CantidadDatos / sumaReciproca;
            case PROMEDIO_GEOMÉTRICO:
                //raiz(x1*x2*x3*...*xn,n)
                double producto = Datos[0];
                for (int i = 1; i < CantidadDatos; i++) {
                    producto *= Datos[i];
                }
                return Math.pow(producto, 1.0 / CantidadDatos);
            default:
                throw new RuntimeException("No se reconoce el tipo de promedio que se quiere calcular");
        }
//</editor-fold>
    }
//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones para el tratamiento de texto matemático">
    //Realización: https://youtu.be/-NiRj8NnqL8
    public static String EliminarNotaciónCientifica(final double Número) {
        String d = "####################################";
        return new DecimalFormat("#." + d + d + d).format(Número);
    }

    public static String recortarDecimales(final double Número) {
        return recortarDecimales(Número, 4);
    }

    public static String recortarDecimales(final double Número, final int DecimalesMáximos) {
        if (Número == (int) Número) {
            return (int) Número + "";
        }
        String retorno = Truncar_Decimales(Número, DecimalesMáximos) + "";
        return retorno.endsWith(".0") ? retorno.replace(".0", "") : retorno;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Subclases simples resultantes de conceptos matemáticos">
    //<editor-fold defaultstate="collapsed" desc="Clases para representar Funciones f(x)">
    public interface Función {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase">

        public double Y(double x); //Realización: https://youtu.be/cdRsU9qY_Zg

        //<editor-fold defaultstate="collapsed" desc="Apróximación de la derivada y la integral">
        public default double Apróx_Integral(final double a, final double b) {//<editor-fold defaultstate="collapsed" desc="Implementación del Código">
            int iteraciones = 120;
            double ΔX = (b - a) / iteraciones;
            double Sumatoria = ((Sucesión) (int n) -> (esPar(n) ? 2 : 4) * Y(a + ΔX * n)).Sumatoria(iteraciones - 1);
            return (ΔX / 3) * (Y(a) + Sumatoria + Y(b));
        }
//</editor-fold>

        public default double Apróx_Derivada(double a1) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
            try {
                final BigDecimal Δx;
                final double absa = abs(a1);
                //<editor-fold defaultstate="collapsed" desc="Ajuste del delta">
                if (absa > 1000000000) {
                    Δx = new BigDecimal("0.00001");
                } else if (absa > 500000000) {
                    Δx = new BigDecimal("0.000001");
                } else if (absa > 20000000) {
                    Δx = new BigDecimal("0.0000001");
                } else if (absa > 7000000) {
                    Δx = new BigDecimal("0.00000001");
                } else if (absa > 1000000) {
                    Δx = new BigDecimal("0.000000001");
                } else if (absa > 10000) {
                    Δx = new BigDecimal("0.0000000001");
                } else {
                    Δx = new BigDecimal("0.00000000001");
                }
//</editor-fold>
                final BigDecimal a = new BigDecimal(a1);
                final BigDecimal x_Δx = new BigDecimal(a1).add(Δx);
                final BigDecimal x1 = new BigDecimal(Y(x_Δx.doubleValue()) + "");
                final BigDecimal x2 = new BigDecimal(Y(a.doubleValue()));
                return new BigDecimal(x1 + "").subtract(x2).divide(Δx).doubleValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new RuntimeException("No se pudo calcular la derivada");
        }//</editor-fold>
//</editor-fold>
    }//</editor-fold>

    public static interface FunciónPolar {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase">

        public double r(double θ);

        //<editor-fold defaultstate="collapsed" desc="Funciones polares carácteristicas">
        public static FunciónPolar Polar_Cardioide() {
            return (double θ) -> 1 - cos(θ);
        }

        public static FunciónPolar Polar_Corazón() {
            return (double θ) -> sen(θ) * Fracción(raiz2(abs(cos(θ))), sen(θ) + 7f / 5) - 2 * sen(θ) + 2;
        }

        public static FunciónPolar Polar_Corazón_Rizado() {
            return (double θ) -> 1.2 - Fracción(sen(θ), 2 * raiz2(.2 + abs(cos(θ)))) + (sen(θ) + cos(2 * θ)) / 5 + sen(70 * θ) * 3 / 50.0;
        }

        public static FunciónPolar Polar_Mitad_Flor(float NúmeroDePetalos) {
            return (double θ) -> cos(θ * NúmeroDePetalos / 2);
        }

        public static FunciónPolar Polar_Flor(float NúmeroDePetalos) {
            return (double θ) -> cos(θ * NúmeroDePetalos);
        }

        public static FunciónPolar Polar_BrazosRadiales(double brazos, double anchuraDeBrazos, double estiramiento) {
            double c = 1 + (1 / estiramiento);
            return (double θ) -> anchuraDeBrazos / (c - sen(brazos * θ));
        }
        //</editor-fold>

    }//</editor-fold>
    //</editor-fold>

    public static interface Sucesión { //<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase">

        public double n_ésimo_Termino(final int n);

        //<editor-fold defaultstate="collapsed" desc="Sumatoria">
        default public double Sumatoria(int llegada) {//<editor-fold defaultstate="collapsed" desc="Implementación del código">
            return Sumatoria(1, llegada);
        }//</editor-fold>

        default public double Sumatoria(int partida, int llegada) {//<editor-fold defaultstate="collapsed" desc="Implementación del código">
            Rango r = new Rango(partida, llegada);
            double Suma = 0;
            for (int n = r.IntExtremoInicial(); n <= r.ExtremoFinal(); n += r.Sentido) {
                Suma += n_ésimo_Termino(n);
            }
            return Suma;
        } //</editor-fold>
        //</editor-fold> 

        //<editor-fold defaultstate="collapsed" desc="Sucesiones carácteristicas">
        public static Sucesión Sucesión_Fibonacci() {
            return (int n) -> Redondeo(raiz2(5) / 5 * poten(φ, n));
        }

        public static Sucesión Sucesión_Seno(final double θ) {
            return (int n) -> (poten(-1, n) * poten(θ, 2 * n + 1)) / Factorial(2 * n + 1);
        }

        public static Sucesión Sucesión_Coseno(final double θ) {
            return (int n) -> (poten(-1, n) * poten(θ, 2 * n)) / Factorial(2 * n);
        }

        public static Sucesión Sucesión_SenoHiperbolico(final double X) {
            return (int n) -> (poten(X, 2 * n + 1)) / Factorial(2 * n + 1);
        }

        public static Sucesión Sucesión_CosenoHiperbolico(final double X) {
            return (int n) -> (poten(X, 2 * n)) / Factorial(2 * n);
        }
//</editor-fold>
    }//</editor-fold>

    public static final class Rango {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase">

        //<editor-fold defaultstate="collapsed" desc="Variables e identificadores">
        //<editor-fold defaultstate="collapsed" desc="Identificadores para ubicación">
        public final static int POR_DETRAS = -1;
        public final static int POR_DENTRO = 0;
        public final static int POR_DELANTE = 1;
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Variables">
        private double ExtremoInicial;
        private double ExtremoFinal;
        private double Magnitud;
        private int Sentido;
//</editor-fold>
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Contructores">
        public Rango(double ExtremoInicial, double ExtremoFinal) {
            ModificarExtremos(ExtremoInicial, ExtremoFinal);
        }

        public Rango(double ExtremoFinal) {
            ModificarExtremos(0, ExtremoFinal);
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Herramientas para la obtención de datos">
        //<editor-fold defaultstate="collapsed" desc="Obtener Extremo Inicio">
        public double ExtremoInicial() {
            return ExtremoInicial;
        }

        public int IntExtremoInicial() {
            return (int) ExtremoInicial;
        }

        public float floatExtremoInicial() {
            return (float) ExtremoInicial;
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Obtener Extremo Fin">
        public double ExtremoFinal() {
            return ExtremoFinal;
        }

        public int IntExtremoFinal() {
            return (int) ExtremoFinal;
        }

        public float floatExtremoFinal() {
            return (float) ExtremoFinal;
        }
//</editor-fold>

        public double ObtenerExtremoMayor() {
            return Máx(ExtremoInicial, ExtremoFinal);
        }

        public double ObtenerExtremoMenor() {
            return Mín(ExtremoInicial, ExtremoFinal);
        }

        public double LongitudDelRango() {
            return Magnitud;
        }

        public int ObtenerSentido() {
            return Sentido;
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Herramientas para la modificación de la estructura">
        public void ModificarExtremos(double NuevoExtremoInicial, double NuevoExtremoFinal) {
            this.ExtremoInicial = NuevoExtremoInicial;
            this.ExtremoFinal = NuevoExtremoFinal;
            Magnitud = NuevoExtremoFinal - NuevoExtremoInicial;
            Sentido = Sgn(Magnitud);
        }

        public void ModificarExtremoInicial(double NuevoExtremoInicial) {
            this.ExtremoInicial = NuevoExtremoInicial;
            Magnitud = ExtremoFinal - NuevoExtremoInicial;
            Sentido = Sgn(Magnitud);
        }

        public void ModificarExtremoFinal(double NuevoExtremoFinal) {
            this.ExtremoFinal = NuevoExtremoFinal;
            Magnitud = NuevoExtremoFinal - ExtremoInicial;
            Sentido = Sgn(Magnitud);
        }

        public void ModificarSentido() {
            ModificarExtremos(ExtremoFinal, ExtremoInicial);
        }

        public void ModificarLongitud(double Lr) {
            this.Magnitud = Lr;
            ExtremoFinal = ExtremoInicial + Sentido * Lr;
            Sentido = Lr <= 0 ? Sgn(Lr) : Sentido;
        }
//</editor-fold>

        public int Valoración(double n, boolean Incluyente) {//<editor-fold defaultstate="collapsed" desc="Implementación del código">

            if (n == ExtremoFinal && Incluyente) {
                return POR_DENTRO;
            }
            if (ExtremoInicial < ExtremoFinal) {
                return n < ExtremoInicial ? POR_DETRAS : n >= ExtremoFinal ? POR_DELANTE : POR_DENTRO;
            } else {
                return n > ExtremoInicial ? POR_DETRAS : n <= ExtremoFinal ? POR_DELANTE : POR_DENTRO;
            }
            //<editor-fold defaultstate="collapsed" desc="Código alternativo usando deinición matemática">
//            if (Incluyente) {
//                return n == Pf ? 0 : Sgn(Piso(Vp(n)));
//            }
//            return Sgn(Piso(Vp(n)));
//</editor-fold>
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Funciónes del valor porcentual">
        public double ValorPorcentual(double n) {
            return (n - ExtremoInicial) / (ExtremoFinal - ExtremoInicial);
        }

        public double ValorPorcentualReducido(double n) {
            double Vp = ValorPorcentual(n);
            double Vpr = Vp - Piso(Vp);
            double Retorno = n > 0 ? Vpr : 1 + Vpr;
            if (Retorno > 1) {
                Retorno = ParteDecimal(Retorno);
            }
            return Retorno;
        }
//</editor-fold>

        public double PuntoOpuesto(double n) {
            return ExtremoFinal + ExtremoInicial - n;
        }

        //<editor-fold defaultstate="collapsed" desc="Ajuste reflejo">
        public float AjusteReflejo(float núm) {
            return (float) AjusteReflejo((double) núm);
        }

        public int AjusteReflejo(int núm) {
            return (int) AjusteReflejo((double) núm);
        }

        public double AjusteReflejo(double n) {
            double Vp = ValorPorcentual(n);
            if (Piso(Vp) % 2 == 0 || ValorPorcentualReducido(n) == 0) {
                return AjusteCircular(n);
            } else {
                return PuntoOpuesto(AjusteCircular(n));
            }
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Ajuste reflejo invertido">
        public int AjusteReflejoInv(int núm) {
            return (int) AjusteReflejoInv((double) núm);
        }

        public double AjusteReflejoInv(double n) {
            double Vp = ValorPorcentual(n);
            if (Piso(Vp) % 2 == 0 || ValorPorcentualReducido(n) == 0) {
                return PuntoOpuesto(AjusteCircular(n));
            } else {
                return AjusteCircular(n);
            }
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Ajuste Circular">
        public float AjusteCircular(float núm) {
            return (float) AjusteCircular((double) núm);
        }

        public int AjusteCircular(int núm) {
            return (int) AjusteCircular((double) núm);
        }

        public double AjusteCircular(double n) {
            if (Valoración(n, true) == POR_DENTRO) {
                return n;
            }
            return ValorPorcentualReducido(n) * Magnitud + ExtremoInicial;
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Ajuste de extremos">
        public float AjusteDeExtremos(float núm) {
            return (float) AjusteDeExtremos((double) núm);
        }

        public int AjusteDeExtremos(int núm) {
            return (int) AjusteDeExtremos((double) núm);
        }

        public double AjusteDeExtremos(double núm) {
            return núm > ExtremoFinal ? ExtremoFinal : núm < ExtremoInicial ? ExtremoInicial : núm;
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Herramientas booleanas para determinar la ubicación de un elemento">
        //<editor-fold defaultstate="collapsed" desc="Por debajo">
        public boolean Por_Debajo(double núm, boolean IncluirExtremoFinal) {
            return Valoración(núm, IncluirExtremoFinal) == POR_DETRAS;
        }

        public boolean Por_Debajo(double núm) {
            return Por_Debajo(núm, false);
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Por dentro">
        public boolean Por_Dentro(double núm, boolean IncluirExtremoFinal) {
            return Valoración(núm, IncluirExtremoFinal) == POR_DENTRO;
        }

        public boolean Por_Dentro(double núm) {
            return Por_Dentro(núm, false);
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Por fuera">
        public boolean Por_Fuera(double núm, boolean IncluirExtremoFinal) {
            return !Por_Dentro(núm, IncluirExtremoFinal);
        }

        public boolean Por_Fuera(double núm) {
            return Por_Fuera(núm, false);
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Por encima">
        public boolean Por_Encima(double núm, boolean IncluirExtremoFinal) {
            return Valoración(núm, IncluirExtremoFinal) == POR_DELANTE;
        }

        public boolean Por_Encima(double núm) {
            return Por_Encima(núm, false);
        }
//</editor-fold>
//</editor-fold>

        @Override
        public String toString() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
            return "Rango{" + "Pi=" + Truncar_Decimales(ExtremoInicial, 4)
                    + ", Pf=" + Truncar_Decimales(ExtremoFinal, 4)
                    + ", Lr=" + Truncar_Decimales(Magnitud, 4)
                    + ", Sr=" + Truncar_Decimales(Sentido, 4) + '}';
        }//</editor-fold>
    } //</editor-fold>

    public static class Rango2D {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase">

        public Rango rangoX;
        public Rango rangoY;

        public double[] Posición() {
            return new double[]{rangoX.ExtremoInicial, rangoY.ExtremoInicial};
        }

        public double[] Dimensión() {
            return new double[]{rangoX.ExtremoFinal - rangoX.ExtremoInicial, rangoY.ExtremoFinal - rangoY.ExtremoInicial};
        }

        //<editor-fold defaultstate="collapsed" desc="Constructores">
        public Rango2D(Rango X, Rango Y) {
            rangoX = X;
            rangoY = Y;
        }

        public Rango2D(double Xf, double Yf) {
            rangoX = new Rango(0, Xf);
            rangoY = new Rango(0, Yf);
        }

        public Rango2D(double Xi, double Xf, double Yi, double Yf) {
            rangoX = new Rango(Xi, Xf);
            rangoY = new Rango(Yi, Yf);
        }
//</editor-fold>

        public boolean estáDentro(double... punto2D) {
            return estáDentro(punto2D[0], punto2D[1]);
        }

        public boolean estáDentro(double x, double y) {
            return rangoX.Por_Dentro(x, true) && rangoY.Por_Dentro(y, true);
        }

        public double[] Ajustación_Dona(double x, double y) {
            return new double[]{rangoX.AjusteCircular(x), rangoY.AjusteCircular(y)};
        }
    }//</editor-fold>
    //</editor-fold>

}
