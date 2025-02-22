//Última actualización 24/Septiembre/2017
package HerramientasMatemáticas;

import java.util.ArrayList;
import static java.lang.Math.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Matemática {

    public static final double PI = 3.141592653589793, e = 2.718281828459045, Phi = (1 + raiz2(5)) / 2,
            π = PI, phi = Phi, φ = phi, Φ = Phi;
    public static final byte GRADOS = 0, RADIANES = 1, PORCENTUAL = 2;

    public static void main(String[] args) {
        //Constantes, tipo double para que el calculo nos dé con decimales
        final double MS_POR_SEGUNDO = 1000;
        final double NS_POR_SEGUNDO = 1000000000;

        //Prueba del calculo de la duración en milisegundos
        long INICIO_MS = System.currentTimeMillis();
        AlgoritmoDePrueba();
        long DURACIÓN_MS = (System.currentTimeMillis() - INICIO_MS);

        //Prueba del calculo de la duración en nanosegundos
        long INICIO_NS = System.nanoTime();
        AlgoritmoDePrueba();
        long DURACIÓN_NS = (System.nanoTime() - INICIO_NS);

        double DURACIÓN_S1 = DURACIÓN_MS / MS_POR_SEGUNDO;
        double DURACIÓN_S2 = DURACIÓN_NS / NS_POR_SEGUNDO;

        System.out.println("TEST 1 - Resultados -  MS\n");
        System.out.println("El algoritmo demoró: " + DURACIÓN_MS + " Milisegundos");
        System.out.println("El algoritmo demoró: " + DURACIÓN_S1 + " Segundos");

        System.out.println("\nTEST 2 - Resultados -  NS\n");
        System.out.println("El algoritmo demoró: " + DURACIÓN_NS + " Nanosegundos");
        System.out.println("El algoritmo demoró: " + DURACIÓN_S2 + " Segundos");
    }

    static void AlgoritmoDePrueba() {
        for (int i = 0; i < 100000; i++) {
            System.out.println(i);
        }
    }

    public static ArrayList<Integer> ObtenerPrimos(Rango rango) {
        ArrayList<Integer> ListaDinamicaDePrimos = new ArrayList<>();
        for (int i = rango.IntVi(); i <= rango.Vf; i += rango.Sr) {
            if (esPrimo(i)) {
                ListaDinamicaDePrimos.add(i);
            }
        }
        return ListaDinamicaDePrimos;
    }

    public static boolean esPrimo(double Número) {
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
    }

    public static double Sumatoria(int Vi, int Vf, Sucesión S) {
        Rango r = new Rango(Vi, Vf);
        double Suma = 0;
        for (int n = r.IntVi(); r.Por_Dentro(n, true); n += r.Sr) {
            Suma += S.n_ésimo_Termino(n);
        }
        return Suma;
    }

    public static double ObtenerParteDecimal(double Número) {
        return Número - (int) Número;
    }

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

    public static double Grados(double radian) {
        return radian * 180 / π;
    }

    public static double Radianes(double Grado) {
        return Grado * π / 180;
    }

    public static double Conv_Radian(double θ, byte TIPO_ÁNGULO) {
        return TIPO_ÁNGULO == GRADOS ? Radianes(θ) : TIPO_ÁNGULO == PORCENTUAL ? θ * 2 * π : θ;
    }

    public static double Conv_Grados(double θ, byte TIPO_ÁNGULO) {
        return TIPO_ÁNGULO == PORCENTUAL ? θ * 360 : TIPO_ÁNGULO == RADIANES ? Grados(θ) : θ;
    }

    public static double ReducciónAngular(double θ) {
        double Rev = 2 * π;
        double p = ObtenerParteDecimal(θ / Rev);
        return θ < 0 ? Rev * (p + 1) : p * Rev;
    }

    public static double ReducciónAngular(double θ, byte TIPO_ÁNGULO) {
        double m = TIPO_ÁNGULO == GRADOS ? 180 / π : TIPO_ÁNGULO == PORCENTUAL ? 1 / (2 * π) : 1;
        return ReducciónAngular(Conv_Radian(θ, TIPO_ÁNGULO)) * m;
    }

    public static double Fracción(double numerador, double denominador) {
        return numerador / denominador;
    }

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
            return Γ(n + 1);
        }
        return -Γ(-n + 1); //=n*Γ(n)
    }

    public static double Gamma(double n) {
        return Γ(n);
    }

    public static double Gamma_DistribuciónDeProbabilidad(double α, double ß, double X) {
        return X <= 0 ? 0 : Apróx_Integral(0, X, (double X1) -> Fracción(poten(X1, α - 1) * poten(e, -X1 / ß), poten(ß, α) * Γ(α)));
    }

    public static double Gamma_DistribuciónDeProbabilidad(double α, double ß, double Xi, double Xf) {
        return Gamma_DistribuciónDeProbabilidad(α, ß, Xf) - Gamma_DistribuciónDeProbabilidad(α, ß, Xi);
    }

    public static double Γ(double z) {
        if (z < 0.5) {
            return π / (Sen(π * z) * Γ(1 - z));
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
    }

    public static int P(int n, int k) {
        return Permutación(n, k);
    }

    public static double P(double n, double k) {
        return Permutación(n, k);
    }

    public static int Permutación(int n, int k) {
        return (int) (Factorial(n) / Factorial(n - k));
    }

    public static double Permutación(double n, double k) {
        return Factorial(n) / Factorial(n - k);
    }

    public static double C(double n, double k) {
        return CombinatoriaSinRepetición(n, k);
    }

    public static int C(int n, int k) {
        return CombinatoriaSinRepetición(n, k);
    }

    public static double CombinatoriaSinRepetición(double n, double k) {
        return Factorial(n) / (Factorial(k) * Factorial(n - k));
    }

    public static int CombinatoriaSinRepetición(int n, int k) {
        return (int) (Factorial(n) / (Factorial(k) * Factorial(n - k)));
    }

    public static double CR(double n, double k) {
        return CombinatoriaConRepetición(n, k);
    }

    public static int CR(int n, int k) {
        return CombinatoriaConRepetición(n, k);
    }

    public static double CombinatoriaConRepetición(double n, double k) {
        return C(n + k - 1, k);
    }

    public static int CombinatoriaConRepetición(int n, int k) {
        return C(n + k - 1, k);
    }

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

    public static double AleatorioEntre(double vi, double vf) {
        return (vf - vi) * random() + vi;
    }

    public static int AleatorioEnteroEntre(double vi, double vf) {
        return Redondeo(AleatorioEntre(vi, vf));
    }

    public static double Ln(double Número) {
        return log(Número);
    }

    public static double raiz4(double Número) {
        return pow(Número, 1.0 / 4);
    }

    public static double raiz3(double Número) {
        return pow(Número, 1.0 / 3);
    }

    public static double raiz2(double Número) {
        return sqrt(Número);
    }

    public static double raiz(double Número, double Índice) {
        return pow(Número, 1.0 / Índice);
    }

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

    public static double Cot(double θ) {
        return 1 / Tan(θ);
    }

    public static double Coth(double x) {
        return 1 / Tanh(x);
    }

    public static double Csc(double θ) {
        return 1 / Sen(θ);
    }

    public static double Csch(double x) {
        return 1 / Senh(x);
    }

    public static double Sec(double θ) {
        return 1 / Cos(θ);
    }

    public static double Sech(double x) {
        return 1 / Cosh(x);
    }

    public static double Tan(double θ) {
        return tan(θ);
    }

    public static double Tanh(double x) {
        return tanh(x);
    }

    public static double Atanh(double x) {
        return (1 / 2.0) * (Ln(Fracción(1 + x, 1 - x)));
    }

    public static double Atan2(double Y, double X) {
        return atan2(Y, X);
    }

    public static double Atan(double m) {
        return atan(m);
    }

    public static double Cos(double θ) {
        return cos(θ);
    }

    public static double Acos(double x) {
        return acos(x);
    }

    public static double Cosh(double x) {
        return cosh(x);
    }

    public static double Acosh(double x) {
        return Ln(x + raiz2(poten2(x) - 1));
    }

    public static double Sen4(double θ) {
        return poten4(Sen(θ));
    }

    public static double Cos4(double θ) {
        return poten4(Cos(θ));
    }

    public static double Sen3(double θ) {
        return poten3(Sen(θ));
    }

    public static double Cos3(double θ) {
        return poten3(Cos(θ));
    }

    public static double Sen2(double θ) {
        return poten2(Sen(θ));
    }

    public static double Cos2(double θ) {
        return poten2(Cos(θ));
    }

    public static double Sen(double θ) {
        return sin(θ);
    }

    public static double Asen(double y) {
        return asin(y);
    }

    public static double Senh(double x) {
        return sinh(x);
    }

    public static double Asenh(double x) {
        return Ln(x + raiz2(poten2(x) + 1));
    }

    public static int Sgn(double x) {
        return x == 0 ? 0 : x > 0 ? 1 : -1;
    }

    public static float abs(float Número) {
        return Número >= 0 ? Número : -Número;
    }

    public static double abs(double Número) {
        return Número >= 0 ? Número : -Número;
    }

    public static long abs(long Número) {
        return Número >= 0 ? Número : -Número;
    }

    public static int abs(int Número) {
        return Número >= 0 ? Número : -Número;
    }

    public static int Redondeo(double Número) {
        return Piso(Número + 0.5);
    }

    public static int Piso(double Número) {
        return Número >= 0 || esEntero(Número) ? (int) Número : ((int) Número - 1);
    }

    public static int Suelo(double Número) {
        return Piso(Número);
    }

    public static int Techo(double Número) {
        return (int) (esEntero(Número) ? Número : Piso(Número) + 1);
    }

    public static int ParteEntera(double Número) {
        return (int) Número;
    }

    public static double ParteDecimal(double Número) {
        return abs(Número - ParteEntera(Número));
    }

    public static double ÁreaTriángulo(double[] U, double[] V) {
        return ÁreaParalelogramo(U, V) / 2;
    }

    public static double ÁreaParalelogramo(double[] U, double[] V) {
        return Distancia(ProductoCrúz(U, V));
    }

    public static double[] ProductoCrúz(double[] U, double[] V) {
        return new double[]{
            U[1] * V[2] - U[2] * V[1],
            U[2] * V[0] - U[0] * V[2],
            U[0] * V[1] - U[1] * V[0]
        };
    }

    public static double[] MultiplicarVectorMatriz(double[] Vector, double[][] Matriz) {
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
    }

    public static float[] MultiplicarVectorMatriz(float[] Vector, float[][] Matriz) {
        if (Vector.length != Matriz.length) {
            throw new Error("Longitudes diferentes, V: " + Vector.length + ", M: " + Matriz.length);
        }
        float[] R = new float[Matriz[0].length];
        for (int i = 0; i < Matriz[0].length; i++) {
            for (int j = 0; j < Vector.length; j++) {
                R[i] += Vector[j] * Matriz[j][i];
            }
        }
        return R;
    }

    public static int Distancia(int... componentes) {
        double Sumatoria = 0;
        for (double componente : componentes) {
            Sumatoria += poten2(componente);
        }
        return Redondeo(raiz2(Sumatoria));
    }

    public static double Distancia(double... componentes) {
        double Sumatoria = 0;
        for (double componente : componentes) {
            Sumatoria += poten2(componente);
        }
        return raiz2(Sumatoria);
    }

    public static double TeoremaDePitagoras(final double a, final double b) {
        return raiz2(poten2(a) + poten2(b));
    }

    public static float Mayor(final float... TérminosDeComparación) {
        float mayor = Integer.MIN_VALUE;
        for (float número : TérminosDeComparación) {
            mayor = número > mayor ? número : mayor;
        }
        return mayor;
    }

    public static float Menor(final float... TérminosDeComparación) {
        float menor = Integer.MAX_VALUE;
        for (float número : TérminosDeComparación) {
            menor = número < menor ? número : menor;
        }
        return menor;
    }

    public static int Mayor(final int... TérminosDeComparación) {
        int mayor = Integer.MIN_VALUE;
        for (int número : TérminosDeComparación) {
            mayor = número > mayor ? número : mayor;
        }
        return mayor;
    }

    public static int Menor(final int... TérminosDeComparación) {
        int menor = Integer.MAX_VALUE;
        for (int número : TérminosDeComparación) {
            menor = número < menor ? número : menor;
        }
        return menor;
    }

    public static double Mayor(final double... TérminosDeComparación) {
        double mayor = Integer.MIN_VALUE;
        for (double número : TérminosDeComparación) {
            mayor = número > mayor ? número : mayor;
        }
        return mayor;
    }

    public static double Menor(final double... TérminosDeComparación) {
        double menor = Integer.MAX_VALUE;
        for (double número : TérminosDeComparación) {
            menor = número < menor ? número : menor;
        }
        return menor;
    }

    public static double Sumatoria(final double... a) {
        double suma = 0;
        for (double n : a) {
            suma += n;
        }
        return suma;
    }

    public static double Promedio(final double... a) {
        return Sumatoria(a) / a.length;
    }

    public static int Máx(final int... a) {
        return Mayor(a);
    }

    public static int Mín(final int... a) {
        return Menor(a);
    }

    public static float Máx(final float... a) {
        return Mayor(a);
    }

    public static float Mín(final float... a) {
        return Menor(a);
    }

    public static double Máx(final double... a) {
        return Mayor(a);
    }

    public static double Mín(final double... a) {
        return Menor(a);
    }

    /*
    
    Tratamiento de texto matemático
    
     */
    public static double conv_String_Número(String textoNumerico) {
        return Double.parseDouble(textoNumerico);
    }

    public static double AjustarDecimales(final double Número, final int decimales) {
        return Double.parseDouble(String.format("%." + decimales + "f", Número));
    }

    public static String EliminarNotaciónCientifica(final double Número) {
        DecimalFormat num;
        num = new DecimalFormat("#.#########################");
        return num.format(Número);
    }

    public static String recortarDecimales(final double Número) {
        return recortarDecimales(Número, 4);
    }

    public static String recortarDecimales(final double Número, final int DecimalesMáximos) {
        if (Número == (int) Número) {
            return (int) Número + "";
        }
        for (int i = 1; i <= DecimalesMáximos; i++) {
            if (Número == AjustarDecimales(Número, i)) {
                return AjustarDecimales(Número, i) + "";
            }
        }
        String retorno = AjustarDecimales(Número, DecimalesMáximos) + "";
        return retorno.endsWith(".0") ? retorno.replace(".0", "") : retorno;
    }

    /*
    
    Sub Clases cortas
    
     */
    public static interface Función {

        public double Y(double x);
    }

    public static interface FunciónPolar {

        public double r(double θ);
    }

    public static FunciónPolar Polar_Cardioide() {
        return (double θ) -> 1 - Cos(θ);
    }

    public static FunciónPolar Polar_Corazón() {
        return (double θ) -> Sen(θ) * Fracción(raiz2(abs(Cos(θ))), Sen(θ) + 7f / 5) - 2 * Sen(θ) + 2;
    }

    public static FunciónPolar Polar_Corazón_Rizado() {
        return (double θ) -> 1.2 - Fracción(Sen(θ), 2 * raiz2(.2 + abs(Cos(θ)))) + (Sen(θ) + Cos(2 * θ)) / 5 + Sen(70 * θ) * 3 / 50.0;
    }

    public static FunciónPolar Polar_Mitad_Flor(float NúmeroDePetalos) {
        return (double θ) -> Cos(θ * NúmeroDePetalos / 2);
    }

    public static FunciónPolar Polar_Flor(float NúmeroDePetalos) {
        return (double θ) -> Cos(θ * NúmeroDePetalos);
    }

    public static FunciónPolar Polar_BrazosRadiales(double brazos, double anchuraDeBrazos, double estiramiento) {
        double c = 1 + (1 / estiramiento);
        return (double θ) -> anchuraDeBrazos / (c - Sen(brazos * θ));
    }

    public static double Apróx_Integral(final double a, final double b, final Función función) {
        int n = 130;
        double ΔX = (b - a) / n,
                Sumatoria = Sumatoria(0, n - 1, (int i) -> (esPar(i) ? 2 : 4) * función.Y(a + ΔX * i));
        return (ΔX / 3) * (función.Y(a) + Sumatoria + función.Y(b));
    }

    public static double Apróx_Derivada(double a1, final Función f) {
        try {
            final BigDecimal Δx;
            final double absa = abs(a1);
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
            final BigDecimal a = new BigDecimal(a1);
            final BigDecimal x_Δx = new BigDecimal(a1).add(Δx);
            final BigDecimal x1 = new BigDecimal(f.Y(x_Δx.doubleValue()) + "");
            final BigDecimal x2 = new BigDecimal(f.Y(a.doubleValue()));
            return new BigDecimal(x1 + "").subtract(x2).divide(Δx).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("No se pudo calcular la derivada");
    }

    public static interface Sucesión {

        public double n_ésimo_Termino(final int n);
    }

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

    public static final class Rango {

        public final static int POR_DETRAS = -1, POR_DENTRO = 0, POR_DELANTE = 1;

        private double Vi, Vf, Lr;
        private int Sr;

        public Rango(double Vi, double Vf) {
            ModificarPuntos(Vi, Vf);
        }

        public void ModificarPuntos(double Vi, double Vf) {
            this.Vi = Vi;
            this.Vf = Vf;
            Lr = Vf - Vi;
            Sr = Sgn(Lr);
        }

        public void ModificarPuntoInicial(double Vi) {
            this.Vi = Vi;
            Lr = Vf - Vi;
            Sr = Sgn(Lr);
        }

        public void ModificarPuntoFinal(double Vf) {
            this.Vf = Vf;
            Lr = Vf - Vi;
            Sr = Sgn(Lr);
        }

        public double Vi() {
            return Vi;
        }

        public double Vf() {
            return Vf;
        }

        public double ValMáx() {
            return Máx(Vi, Vf);
        }

        public double ValMín() {
            return Mín(Vi, Vf);
        }

        public double Lr() {
            return Lr;
        }

        public void ModificarLongitud(double Lr) {
            this.Lr = Lr;
            Vf = Vi + Sr * Lr;
            Sr = Lr <= 0 ? Sgn(Lr) : Sr;
        }

        public int ObtenerSentido() {
            return Sr;
        }

        public void ModificarSentido() {
            double temp = Vi;
            Vi = Vf;
            Vf = temp;
            Lr = Vf - Vi;
            Sr = Sgn(Lr);
        }

        public int Valoración(double n, boolean Incluyente) {
            if (n == Vf && Incluyente) {
                return POR_DENTRO;
            }
            if (Vi < Vf) {
                return n < Vi ? POR_DETRAS : n >= Vf ? POR_DELANTE : POR_DENTRO;
            } else {
                return n > Vi ? POR_DETRAS : n <= Vf ? POR_DELANTE : POR_DENTRO;
            }
//            if (Incluyente) {
//                return n == Pf ? 0 : Sgn(Piso(Vp(n)));
//            }
//            return Sgn(Piso(Vp(n)));
        }

        public double PuntoOpuesto(double n) {
            return Vf + Vi - n;
        }

        public double Vp(double n) {
            return Fracción(n - Vi, Vf - Vi);
        }

        public double Vpr(double n) {
            double Vp = Vp(n);
            double Vpr = Vp - Piso(Vp);
            double r = n > 0 ? Vpr : 1 + Vpr;
            if (r > 1) {
                r = ParteDecimal(r);
            }
            return r;
        }

        public double Dividir(double n) {
            return Lr / n;
        }

        public int AjusteReflejo(int núm) {
            return (int) AjusteReflejo((double) núm);
        }

        public double AjusteReflejo(double n) {
            double Vp = Vp(n);
            if (Piso(Vp) % 2 == 0 || Vpr(n) == 0) {
                return AjusteCiclico(n);
            } else {
                return PuntoOpuesto(AjusteCiclico(n));
            }
//            double sw1 = Fracción(poten(-1, Piso(Vp)), 2) + 1 / 2.0;
//            double sw2 = Fracción(poten(-1, Piso(Vp + 1)), 2) + 1 / 2.0;
//            return sw1 * AjusteCiclico(n) + sw2 * PuntoOpuesto(AjusteCiclico(n));
        }

        public int AjusteCiclico(int núm) {
            return (int) AjusteCiclico((double) núm);
        }

        public double AjusteCiclico(double n) {
            if (Valoración(n, true) == POR_DENTRO) {
                return n;
            }
            return Vpr(n) * Lr + Vi;
        }

        public int AjusteLineal(int núm) {
            return (int) AjusteLineal((double) núm);
        }

        public double AjusteLineal(double núm) {
            return núm > Vf ? Vf : núm < Vi ? Vi : núm;
        }

        public int IntVi() {
            return (int) Vi;
        }

        public int IntVf() {
            return (int) Vf;
        }

        public float floatVi() {
            return (float) Vi;
        }

        public float floatVf() {
            return (float) Vf;
        }

        public boolean Por_Debajo(double núm, boolean Incluyente) {
            return Valoración(núm, Incluyente) == POR_DETRAS;
        }

        public boolean Por_Dentro(double núm, boolean Incluyente) {
            return Valoración(núm, Incluyente) == POR_DENTRO;
        }

        public boolean Por_Fuera(double núm, boolean Incluyente) {
            return !Por_Dentro(núm, Incluyente);
        }

        public boolean Por_Encima(double núm, boolean Incluyente) {
            return Valoración(núm, Incluyente) == POR_DELANTE;
        }

        @Override
        public String toString() {
            return "Rango{" + "Pi=" + AjustarDecimales(Vi, 4)
                    + ", Pf=" + AjustarDecimales(Vf, 4)
                    + ", Lr=" + AjustarDecimales(Lr, 4)
                    + ", Sr=" + AjustarDecimales(Sr, 4) + '}';
        }
    }

    public static class Rango2D {

        public Rango rangoX;
        public Rango rangoY;

        public double[] Posición() {
            return new double[]{rangoX.Vi, rangoY.Vi};
        }

        public double[] Dimensión() {
            return new double[]{rangoX.Vf - rangoX.Vi, rangoY.Vf - rangoY.Vi};
        }

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

        public boolean estáDentro(double... punto2D) {
            return estáDentro(punto2D[0], punto2D[1]);
        }

        public boolean estáDentro(double x, double y) {
            return rangoX.Por_Dentro(x, true) && rangoY.Por_Dentro(y, true);
        }

        public double[] Ajustación_Dona(double x, double y) {
            return new double[]{rangoX.AjusteCiclico(x), rangoY.AjusteCiclico(y)};
        }
    }

}
