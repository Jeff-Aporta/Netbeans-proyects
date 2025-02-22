//Última actualización: 3/Octubre/2017
package HerramientasMatemáticas;

import java.awt.Component;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.awt.geom.Dimension2D;

import static HerramientasMatemáticas.Matemática.*;//Ajustar a la carpeta correspondiente de ser necesario.
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

public final class Dupla {

    public double X, Y;
    public boolean Protegido = false;

    public static final Dupla /**
             * Preferiblemente no usarlos para generar instancias que sean
             * susceptibles a sufrir modificaciones con reemplazamientos
             */
            ORIGEN = new Dupla().Proteger(),
            IDENTIDAD = new Dupla(1).Proteger(),
            DIMENSIÓN_PANTALLA = new Dupla(Toolkit.getDefaultToolkit().getScreenSize()).Proteger(),
            THD_1280x720 = new Dupla(1280, 720).Proteger(),
            TM_640x360 = THD_1280x720.Mitad().Proteger(),
            TSD_320x180 = THD_1280x720.Cuarto().Proteger();

    //Identificadores de alineación.
    public final static byte ARRIBA = 0, MEDIO = 1, ABAJO = 2, IZQUIERDA = 0, DERECHA = 2;


    /*
    ---------------------------------------------------------------------------
    
    Constructores
    
    
     */
    public Dupla() {
        X = 0;
        Y = 0;
    }

    public Dupla(double X, double Y) {
        this.X = X;
        this.Y = Y;
    }

    public Dupla(double n) {
        this.X = n;
        this.Y = n;
    }

    public Dupla(double... d) {
        X = d[0];
        Y = d[1];
    }

    public Dupla(Object obj) {
        //Siempre separar por comas, para evitar bucle infinito
        if (obj instanceof Dupla) {
            ReemplazarXY(((Dupla) obj).X, ((Dupla) obj).Y);
        } else if (obj instanceof Number) {
            ReemplazarXY(((Number) obj).doubleValue(), ((Number) obj).doubleValue());
        } else if (obj instanceof Dimension2D) {
            ReemplazarXY(((Dimension2D) obj).getWidth(), ((Dimension2D) obj).getHeight());
        } else if (obj instanceof Image) {
            ReemplazarXY(((Image) obj).getWidth(null), ((Image) obj).getHeight(null));
        } else if (obj instanceof Point) {
            ReemplazarXY(((Point) obj).x, ((Point) obj).y);
        } else if (obj instanceof Component) {
            ReemplazarXY(((Component) obj).getWidth(), ((Component) obj).getHeight());
        } else if (obj instanceof Color) {
            int rgb = ((Color) obj).getRGB();
            int x = rgb << 8 >>> 24;
            int y = rgb << 16 >>> 24;
            ReemplazarXY(x, y);
        }
    }

    public Dupla(double Radio, double θ, byte TIPO_ÁNGULO) {
        X = Radio * Cos(Conv_Radian(θ, TIPO_ÁNGULO));
        Y = Radio * Sen(Conv_Radian(θ, TIPO_ÁNGULO));
    }

    /*
    
    Fin de los Constructores
    
    ----------------------------------------------------------------------------------
    
    Inicio de los métodos que modifican a la dupla
    
     */
    //Operaciones básicas
    public Dupla Proteger() {
        Protegido = true;
        return this;
    }

    public Dupla Desproteger() {
        Protegido = false;
        return this;
    }

    public Dupla Adicionar(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Adicionar(dupla.X, dupla.Y);
        }
        return Adicionar(dupla.X, dupla.Y);
    }

    public Dupla AdicionarX(Object obj) {
        if (Protegido) {
            return Clon().AdicionarX(obj);
        }
        X += new Dupla(obj).X;
        return this;
    }

    public Dupla AdicionarY(Object obj) {
        if (Protegido) {
            return Clon().AdicionarY(obj);
        }
        Y += new Dupla(obj).Y;
        return this;
    }

    public Dupla AdicionarX(double x) {
        if (Protegido) {
            return Clon().AdicionarX(x);
        }
        X += x;
        return this;
    }

    public Dupla AdicionarY(double y) {
        if (Protegido) {
            return Clon().Adicionar(0, y);
        }
        return Adicionar(0, y);
    }

    public Dupla Adicionar(double n) {
        if (Protegido) {
            return Clon().Adicionar(n, n);
        }
        return Adicionar(n, n);
    }

    public Dupla Adicionar(double x, double y) {
        if (Protegido) {
            return Clon().Adicionar(x, y);
        }
        X += x;
        Y += y;
        return this;
    }

    public Dupla Sustraer(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Adicionar(-dupla.X, -dupla.Y);
        }
        return Adicionar(-dupla.X, -dupla.Y);
    }

    public Dupla SustraerX(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Adicionar(-dupla.X, 0);
        }
        return Adicionar(-dupla.X, 0);
    }

    public Dupla SustraerY(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Adicionar(0, -dupla.Y);
        }
        return Adicionar(0, -dupla.Y);
    }

    public Dupla SustraerX(double x) {
        if (Protegido) {
            return Clon().Adicionar(-x, 0);
        }
        return Adicionar(-x, 0);
    }

    public Dupla SustraerY(double y) {
        if (Protegido) {
            return Clon().Adicionar(0, -y);
        }
        return Adicionar(0, -y);
    }

    public Dupla Sustraer(double n) {
        if (Protegido) {
            return Clon().Adicionar(-n, -n);
        }
        return Adicionar(-n, -n);
    }

    public Dupla Sustraer(double x, double y) {
        if (Protegido) {
            return Clon().Sustraer(x, y);
        }
        X -= x;
        Y -= y;
        return this;
    }

    public Dupla Dividir(double x, double y) {
        if (Protegido) {
            return Clon().Dividir(x, y);
        }
        X /= x;
        Y /= y;
        return this;
    }

    public Dupla Dividir(double n) {
        if (Protegido) {
            return Clon().Dividir(n);
        }
        X /= n;
        Y /= n;
        return this;
    }

    public Dupla Dividir(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Dividir(dupla);
        }
        X /= dupla.X;
        Y /= dupla.Y;
        return this;
    }

    public Dupla DividirX(double Numero) {
        if (Protegido) {
            return Clon().DividirX(Numero);
        }
        X /= Numero;
        return this;
    }

    public Dupla DividirX(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().DividirX(dupla);
        }
        X /= dupla.X;
        return this;
    }

    public Dupla DividirY(double Numero) {
        if (Protegido) {
            return Clon().DividirY(Numero);
        }
        Y /= Numero;
        return this;
    }

    public Dupla DividirY(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().DividirY(dupla);
        }
        Y /= dupla.Y;
        return this;
    }

    public Dupla Multiplicar(double n) {
        if (Protegido) {
            return Clon().Multiplicar(n);
        }
        X *= n;
        Y *= n;
        return this;
    }

    public Dupla Multiplicar(double x, double y) {
        if (Protegido) {
            return Clon().Multiplicar(x, y);
        }
        X *= x;
        Y *= y;
        return this;
    }

    public Dupla Multiplicar(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Multiplicar(obj);
        }
        X *= dupla.X;
        Y *= dupla.Y;
        return this;
    }

    public Dupla Multiplicar(double a, double b, double c, double d) {
        if (Protegido) {
            return Clon().ReemplazarXY(a * X + b * Y, c * X + d * Y);
        }
        return ReemplazarXY(a * X + b * Y, c * X + d * Y);
    }

    public Dupla MultiplicarX(double Numero) {
        if (Protegido) {
            return Clon().MultiplicarX(Numero);
        }
        X *= Numero;
        return this;
    }

    public Dupla MultiplicarX(Object obj) {
        if (Protegido) {
            return Clon().MultiplicarX(obj);
        }
        X *= new Dupla(obj).X;
        return this;
    }

    public Dupla MultiplicarY(double Numero) {
        if (Protegido) {
            return Clon().MultiplicarY(Numero);
        }
        Y *= Numero;
        return this;
    }

    public Dupla MultiplicarY(Object obj) {
        if (Protegido) {
            return Clon().MultiplicarY(obj);
        }
        Y *= new Dupla(obj).Y;
        return this;
    }

    public Dupla Normalizar() {
        if (Protegido) {
            return Clon().Dividir(Magnitud());
        }
        return this.Dividir(Magnitud());
    }

    //Transformaciones rigidas y elasticas 
    public Dupla Trasladar(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Adicionar(dupla);
        }
        return Adicionar(dupla);
    }

    public Dupla Trasladar(double n) {
        if (Protegido) {
            return Clon().Adicionar(n);
        }
        return Adicionar(n);
    }

    public Dupla Trasladar(double x, double y) {
        if (Protegido) {
            return Clon().Trasladar(x, y);
        }
        return Adicionar(x, y);
    }

    //Métodos de desplazamiento en una matriz
    public Dupla Desplazar1Derecha() {
        if (Protegido) {
            return Clon().Desplazar1Derecha();
        }
        X++;
        return this;
    }

    public Dupla Desplazar1Izquierda() {
        if (Protegido) {
            return Clon().Desplazar1Izquierda();
        }
        X--;
        return this;
    }

    public Dupla Desplazar1Abajo() {
        if (Protegido) {
            return Clon().Desplazar1Abajo();
        }
        Y++;
        return this;
    }

    public Dupla Desplazar1Arriba() {
        if (Protegido) {
            return Clon().Desplazar1Arriba();
        }
        Y--;
        return this;
    }

    //Escalar
    public Dupla Escalar(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Escalar(dupla);
        }
        return Multiplicar(dupla);
    }

    public Dupla Escalar(double x, double y) {
        if (Protegido) {
            return Clon().Multiplicar(x, y);
        }
        return Multiplicar(x, y);
    }

    public Dupla Escalar(double escala) {
        if (Protegido) {
            return Clon().Multiplicar(escala);
        }
        return Multiplicar(escala);
    }

    //Métodos de cambio de sentido
    public Dupla CambiarSentido() {
        if (Protegido) {
            return Clon().Multiplicar(-1);
        }
        return Multiplicar(-1);
    }

    public Dupla CambiarSentidoX() {
        if (Protegido) {
            return Clon().MultiplicarX(-1);
        }
        return MultiplicarX(-1);
    }

    public Dupla CambiarSentidoY() {
        if (Protegido) {
            return Clon().MultiplicarY(-1);
        }
        return MultiplicarY(-1);
    }

    //Métodos de giro
    public Dupla girar(double θ) {
        double a = Cos(θ), b = Sen(θ);
        if (Protegido) {
            return Clon().ReemplazarXY(a * X - b * Y, b * X + a * Y);
        }
        return ReemplazarXY(a * X - b * Y, b * X + a * Y);
    }

    public Dupla girar(double Δθ, byte TIPO_ÁNGULO) {
        if (Protegido) {
            return Clon().girar(Conv_Radian(Δθ, TIPO_ÁNGULO));
        }
        return girar(Conv_Radian(Δθ, TIPO_ÁNGULO));
    }

    public Dupla g90Derecha() {
        if (Protegido) {
            return Clon().ReemplazarXY(Y, -X);
        }
        return ReemplazarXY(Y, -X);
    }

    public Dupla g90Izquierda() {
        if (Protegido) {
            return Clon().ReemplazarXY(-Y, X);
        }
        return ReemplazarXY(-Y, X);
    }

    public Dupla Atracción(Dupla puntoPrueba, double r) {
        if (Protegido) {
            return Clon().PuntoAtractor(puntoPrueba, r);
        }
        if (puntoPrueba.distanciaPunto(this) == 0) {
            return this;
        }
        Dupla Atracción = new Dupla(r / (puntoPrueba.distanciaPunto(this)), puntoPrueba.Ángulo(this), RADIANES);
        return Atracción;
    }

    public Dupla PuntoAtractor(Dupla puntoPrueba, double r) {
        Dupla Atracción = Atracción(puntoPrueba, r);
        return Atracción.Radio() > distanciaPunto(puntoPrueba) ? ReemplazarXY(puntoPrueba) : Trasladar(Atracción);
    }

    public Dupla FunciónAtractora(Curva_nativa función, double r) {
        if (Protegido) {
            return Clon().FunciónAtractora(función, r);
        }
        Dupla puntoPrueba = función.XY(X);
        return PuntoAtractor(puntoPrueba, r);
    }

    public Dupla FunciónPolarAtractora(Curva_nativa funciónPolar, double r, Dupla centro, double r_atracción) {
        if (Protegido) {
            return Clon().FunciónPolarAtractora(funciónPolar, r, centro, r_atracción);
        }
        return PuntoAtractor(
                funciónPolar.XY(Ángulo(centro)).Escalar(new Dupla(r)).Trasladar(centro),
                r_atracción
        );
    }

    //Métodos de sustitución o reemplazamiento.
    public Dupla ReemplazarXY(double nuevoX, double nuevoY) {
        if (Protegido) {
            return Clon().ReemplazarXY(nuevoX, nuevoY);
        }
        X = nuevoX;
        Y = nuevoY;
        return this;
    }

    public Dupla ReemplazarXY(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().ReemplazarXY(obj);
        }
        X = dupla.X;
        Y = dupla.Y;
        return this;
    }

    public Dupla ReemplazarX(double nuevoX) {
        if (Protegido) {
            return Clon().ReemplazarX(nuevoX);
        }
        X = nuevoX;
        return this;
    }

    public Dupla ReemplazarX(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().ReemplazarX(obj);
        }
        X = dupla.X;
        return this;
    }

    public Dupla ReemplazarY(double nuevoY) {
        if (Protegido) {
            return Clon().ReemplazarY(nuevoY);
        }
        Y = nuevoY;
        return this;
    }

    public Dupla ReemplazarY(Object obj) {
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().ReemplazarY(obj);
        }
        Y = dupla.Y;
        return this;
    }

    public Dupla ReemplazarRθ(double nuevoRadio, double Nuevoθ) {
        if (Protegido) {
            return Clon().ReemplazarXY(nuevoRadio * Cos(Nuevoθ), nuevoRadio * Sen(Nuevoθ));
        }
        return ReemplazarXY(nuevoRadio * Cos(Nuevoθ), nuevoRadio * Sen(Nuevoθ));
    }

    public Dupla ReemplazarRadio(double NuevoRadio) {
        double θ = θ();
        if (Protegido) {
            return Clon().ReemplazarXY(NuevoRadio * Cos(θ), NuevoRadio * Sen(θ));
        }
        return ReemplazarXY(NuevoRadio * Cos(θ), NuevoRadio * Sen(θ));
    }

    public Dupla Reemplazarθ(double θ) {
        double r = Radio();
        if (Protegido) {
            return Clon().ReemplazarXY(r * Cos(θ), r * Sen(θ));
        }
        return ReemplazarXY(r * Cos(θ), r * Sen(θ));
    }

    public Dupla Invertir_XYaYX() {
        if (Protegido) {
            return Clon().ReemplazarXY(Y, X);
        }
        return ReemplazarXY(Y, X);
    }

    /*
    -----------------------------------------------------------------------------------------------------------
    
    Métodos que retornan duplas, pero sin modificar a la dupla.
    
     */
    public Dupla Clon() {
        return new Dupla(X, Y);
    }

    public Dupla Copia() {
        return new Dupla(X, Y);
    }

    public Dupla distanciaDirigida(Dupla punto) {
        return Clon().Sustraer(punto);
    }

    public Dupla distanciaDirigida(double x, double y) {
        return Clon().Sustraer(x, y);
    }

    public Dupla Centro() {
        return Clon().Dividir(2);
    }

    public Dupla Mitad() {
        return Clon().Dividir(2);
    }

    public Dupla Tercio() {
        return Clon().Dividir(3);
    }

    public Dupla Cuarto() {
        return Clon().Dividir(4);
    }

    public Dupla PuntoMedio(Dupla punto) {
        return Clon().Adicionar(punto).Dividir(2);
    }

    public Dupla ImagenPolar() {
        return Circulo().XY(Y).Escalar(X);
    }

    public Dupla convPolar() {
        return new Dupla(Radio(), θ());
    }

    /*
    -----------------------------------------------------------------------------------------------------------
    
    Métodos que retornan números, deducciones de los valores de X y Y
    
     */
    public double Ángulo(Object Cola) { //Vector
        Dupla dupla = new Dupla(Cola);
        return Ángulo(dupla.X, dupla.Y);
    }

    public double Ángulo(Dupla Cola) { //Vector
        return Ángulo(Cola.X, Cola.Y);
    }

    public double Ángulo(double Cola_X, double Cola_Y) {//Vector
        double θ = Atan2(Y - Cola_Y, X - Cola_X);
        return θ < 0 ? θ + 2 * π : θ;
    }

    public double Ángulo() {
        double θ = Atan2(Y, X);
        return θ < 0 ? θ + 2 * π : θ;
    }

    public double θ() {
        return Ángulo();
    }

    public float floatX() {
        return (float) X;
    }

    public float floatY() {
        return (float) Y;
    }

    public int intX() {
        return (int) X;
    }

    public int intY() {
        return (int) Y;
    }

    public int rndX() {
        return Redondeo(X);
    }

    public int rndY() {
        return Redondeo(Y);
    }

    public double absX() {
        return abs(X);
    }

    public double absY() {
        return abs(Y);
    }

    public double Área() {
        return absX() * absY();
    }

    public int Columna() {
        return intX();
    }

    public int Fila() {
        return intY();
    }

    public int Ancho() {
        return rndX(); //Probablemente haya que cambiarlo por absX()
    }

    public int Alto() {
        return rndY(); //Probablemente haya que cambiarlo por absY()
    }

    public int Width() {
        return Ancho();
    }

    public int Height() {
        return Alto();
    }

    public int Column() {
        return Columna();
    }

    public int Row() {
        return Fila();
    }

    public double distanciaPunto(Dupla punto) {
        return TeoremaDePitagoras(X - punto.X, Y - punto.Y);
    }

    public double distanciaPunto(double x, double y) {
        return TeoremaDePitagoras(X - x, Y - y);
    }

    public double Radio(Dupla Cola) {//Vector
        return distanciaPunto(Cola);
    }

    public double Radio() {
        return TeoremaDePitagoras(X, Y);
    }

    public double Magnitud() {
        return Radio();
    }

    /*
    ---------------------------------------------------------------------------------------------
    
    Métodos void
    
     */
    public Dupla Imprimir_Consola(String PreTexto) {
        System.out.println(PreTexto + " " + this);
        return this;
    }

    public void Imprimir_Consola() {
        System.out.println(this);
    }

    public void Imprimir_Ángulo_Consola(Dupla Cola) {
        System.out.println(Grados(Ángulo(Cola)) + "°");
    }

    public void Imprimir_Ángulo_Consola() {
        System.out.println(Grados(θ()) + "°");
    }

    /*
    -------------------------------------------------------------------------------------------
    
    Métodos sobreescritos de Object
    
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        }
        final Dupla other = new Dupla(obj);
        if (Double.doubleToLongBits(this.X) != Double.doubleToLongBits(other.X)) {
            return false;
        }
        if (Double.doubleToLongBits(this.Y) != Double.doubleToLongBits(other.Y)) {
            return false;
        }
        return true;
    }

    public boolean XesMenor(Object obj) {
        return X < new Dupla(obj).X;
    }

    public boolean YesMenor(Object obj) {
        return Y < new Dupla(obj).Y;
    }

    public boolean esIgual(Object obj) {
        return equals(obj);
    }

    public boolean esDiferente(Object obj) {
        return !esIgual(obj);
    }

    public boolean esIgual(double x, double y) {
        return X == x && Y == y;
    }

    @Override
    public String toString() {
        return "<" + "X=" + recortarDecimales(X) + ", Y=" + recortarDecimales(Y) + '>';
    }

    /*
    
    Métodos de conversión de tipo
    
    
     */
    public double[] convVector() {
        return new double[]{X, Y};
    }

    public Dimension convDimension() {
        return new Dimension(rndX(), rndY());
    }

    public Point convPoint() {
        return new Point(rndX(), rndY());
    }

    public BufferedImage convBufferedImage() {
        return new BufferedImage(intX(), intY(), BufferedImage.TYPE_INT_ARGB);
    }

    /*

      Métodos estáticos
    
     */
    public static BufferedImage convBufferedImage(int x, int y) {
        return new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
    }

    public static BufferedImage convBufferedImage(Object obj) {
        Dupla dim = new Dupla(obj);
        return new BufferedImage(dim.Ancho(), dim.Alto(), BufferedImage.TYPE_INT_ARGB);
    }

    public static double DeterminarÁngulo(Object vector) {
        return new Dupla(vector).θ();
    }

    public static double DeterminarÁngulo(Object Cola, Object Cabeza) {
        return new Dupla(Cabeza).Ángulo(Cola);
    }

    public static Dupla PosiciónCursorEnPantalla() {
        return new Dupla(MouseInfo.getPointerInfo().getLocation());
    }

    public static Dupla PosiciónCursorEnComponente(Component c) {
        Point posicionActual = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(posicionActual, c);
        return new Dupla(posicionActual);
    }

    public static Dupla Alinear(Object DimEnvolvente, Object DimMenor, int AlineaciónH, int AlineaciónV) {
        Dupla DuplaEnvolvente = new Dupla(DimEnvolvente).Sustraer(new Dupla(DimMenor));
        return new Dupla(
                AlineaciónH == DERECHA ? DuplaEnvolvente.X : AlineaciónH == MEDIO ? DuplaEnvolvente.X / 2 : 0,
                AlineaciónV == ABAJO ? DuplaEnvolvente.Y : AlineaciónV == MEDIO ? DuplaEnvolvente.Y / 2 : 0
        );
    }

    //Curvas especiales
    public static boolean EsTransformaciónLineal(Transformación_Nativa T) {
        Dupla a = new Dupla(2, 3);
        Dupla b = new Dupla(5, 7);
        double c = 11;
        a.Imprimir_Consola("a");
        b.Imprimir_Consola("b");
        System.out.println("c = " + c);
        System.out.println("\nT(a) + T(b) = T(a+b)\n");
        String al = T.T(a).Adicionar(T.T(b)).toString();
        String bl = T.T(a.Clon().Adicionar(b)).toString();
        boolean primeraCondicion = al.equals(bl); //T(a) + T(b) = T(a+b); 
        System.out.println(T.T(a) + " + " + T.T(b) + " = " + bl);
        System.out.println(al + " = " + bl + " : " + primeraCondicion);
        System.out.println("\n" + (primeraCondicion ? "Paso la primera condicion" : "NO paso la primera condicion"));

        al = T.T(a.Clon().Multiplicar(c)).toString();
        bl = T.T(a).Multiplicar(c).toString();
        boolean SegundaCondicion = al.equals(bl);
        System.out.println("\nc T(a) = T(c a)\n");//T(c a) = c T(a)
        System.out.println(recortarDecimales(c) + " * " + T.T(a) + " = " + bl);
        System.out.println(al + " = " + bl + " : " + SegundaCondicion);
        System.out.println("\n" + (SegundaCondicion ? "Paso la Segunda Condicion" : "NO paso la Segunda Condicion\n"));
        return primeraCondicion
                && SegundaCondicion;
    }

    //Fin de Curvas Especiales.
    public static interface Curva_nativa {

        public Dupla XY(double t);
    }

    public static abstract class Curva implements Curva_nativa {

        @Override
        public abstract Dupla XY(double t);

        public void Dibujar(Graphics2D g, double i, double f, int puntos) {
            Dibujar(g, i, f, puntos, Plano.PlanoNatural);
        }

        public void Dibujar(Graphics2D g, double i, double f, int puntos, Plano plano) {
            int[][] xy = TabularXY(i, f, puntos, plano);
            g.drawPolygon(xy[0], xy[1], puntos);
        }

        public void Pintar(Graphics2D g, double i, double f, int puntos) {
            Pintar(g, i, f, puntos, Plano.PlanoNatural);
        }

        public void Pintar(Graphics2D g, double i, double f, int puntos, Plano plano) {
            int[][] xy = TabularXY(i, f, puntos, plano);
            g.fillPolygon(xy[0], xy[1], puntos);
        }

        public int[][] TabularXY(double i, double f, int puntos) {
            return TabularXY(i, f, puntos, Plano.PlanoNatural);
        }

        public int[][] TabularXY(double i, double f, int puntos, Plano plano) {
            int[] x = new int[puntos];
            int[] y = new int[puntos];
            double Dt = (f - i) / puntos;
            for (int t = 0; t < puntos; t++) {
                Dupla punto = XY(Dt * t);
                x[t] = plano.UbicaciónEnElPlano(punto).rndX();
                y[t] = plano.UbicaciónEnElPlano(XY(Dt * t)).rndY();
            }
            return new int[][]{x, y};
        }

        public Curva Derivada() {
            return convCurva(Dupla.Derivada(this));
        }

        public Curva Rotar(double θ) {
            return convCurva(Dupla.RotarCurva(this, θ));
        }

        public Curva Trasladar(double Tx, double Ty) {
            return convCurva(Dupla.TrasladarCurva(this, Tx, Ty));
        }

        public Curva Trasladar(Dupla Txy) {
            return convCurva(Dupla.TrasladarCurva(this, Txy));
        }

        public Curva Escalar(double Ex, double Ey) {
            return convCurva(Dupla.EscalarCurva(this, Ex, Ey));
        }

        public Curva Escalar(double E) {
            return convCurva(Dupla.EscalarCurva(this, E));
        }

        public Curva Escalar(Dupla escala) {
            return convCurva(Dupla.EscalarCurva(this, escala));
        }

        public final Curva CambiarSentidoY() {
            return convCurva(Dupla.CambiarSentidoY(this));
        }

        public Curva funciónPolarAtractora(Curva_nativa funciónPolar, double radio, Dupla centro, double radioAtracción) {
            return convCurva(Dupla.funciónPolarAtractora(this, funciónPolar, radio, centro, radioAtracción));
        }

        public Curva Contra(double k) {
            return convCurva(Dupla.Contra(this, k));
        }

        public Curva PuntoAtractor(Dupla puntoPrueba, double radio) {
            return convCurva(Dupla.PuntoAtractor(this, puntoPrueba, radio));
        }

        public Curva PuntoMedio(Curva_nativa F2) {
            return convCurva(Dupla.PuntoMedio(this, F2));
        }

        public Curva ImagenPolar() {
            return convCurva(Dupla.ImagenPolar(this));
        }

        public Curva funciónAtractora(Curva_nativa funciónPrueba, double radio) {
            return convCurva(Dupla.funciónAtractora(this, funciónPrueba, radio));
        }

        public Curva ImagenEspacioParamétrico(Curva_nativa FunciónTransformadora) {
            return convCurva(Dupla.ImagenEspacioParamétrico(this, FunciónTransformadora));
        }

        public static Curva Circulo(double radio, Dupla centro) {
            return convCurva(Dupla.Circulo(radio, centro));
        }

        public final static Curva Circulo() {
            return convCurva(Dupla.Circulo());
        }

        public final static Curva CorazónPolar() {
            return convCurva(Dupla.CorazónPolar());
        }

        public final static Curva Poligonoide(double k, double θ) {
            return convCurva(Dupla.Poligonoide(k, θ));
        }

        public final static Curva Nubleré(double a, double b, double c, double d) {
            return convCurva(Dupla.Nubleré(a, b, c, d));
        }

        public static Curva Epicicloide(double k) {
            return convCurva(Dupla.Epicicloide(k));
        }

        public static Curva Astroide() {
            return convCurva(Dupla.Astroide());
        }

        public static Curva Astroide(double k) {
            return Epicicloide(-k);
        }

        public static Curva Cardioide(double k) {
            return Epicicloide(k);
        }

        public static Curva RectaVectorial2Puntos(Dupla punto, Dupla punto2) {
            return convCurva(Dupla.RectaVectorial2Puntos(punto, punto2));
        }

        public static Curva PuntoMedio(Curva_nativa F, Curva_nativa F2) {
            return convCurva(Dupla.PuntoMedio(F, F2));
        }

        public static Curva Poligono(double k) {
            return convCurva(Dupla.Poligono(k));
        }

        public static Curva ImagenPolar(Curva_nativa Función) {
            return convCurva(Dupla.ImagenPolar(Función));
        }

        public static Curva ImagenEspacioParamétrico(Curva_nativa Función, Curva_nativa FunciónTransformadora) {
            return convCurva(Dupla.ImagenEspacioParamétrico(Función, FunciónTransformadora));
        }

        public static Curva PuntoAtractor(Curva_nativa función, Dupla puntoPrueba, double radio) {
            return convCurva(Dupla.PuntoAtractor(función, puntoPrueba, radio));
        }

        public static Curva funciónAtractora(Curva_nativa función, Curva_nativa funciónPrueba, double radio) {
            return convCurva(Dupla.funciónAtractora(función, funciónPrueba, radio));
        }

        public static Curva funciónPolarAtractora(Curva_nativa función, Curva_nativa funciónPolar, double radio, Dupla centro, double radioAtracción) {
            return convCurva(Dupla.funciónPolarAtractora(función, funciónPolar, radio, centro, radioAtracción));
        }

        public static Curva Contra(Curva_nativa curva, double k) {
            return convCurva(Dupla.Contra(curva, k));
        }

        public static final Curva convCurva(Curva_nativa c) {
            return new Curva() {
                @Override
                public Dupla XY(double t) {
                    return c.XY(t);
                }
            };
        }
    }

    public final static Curva_nativa Derivada(Curva_nativa c) {
        final double Δt = 0.00000000001;
        return (double t) -> c.XY(t + Δt).Sustraer(c.XY(t)).Dividir(Δt).Normalizar();
    }

    public final static Curva_nativa Poligonoide(double k, double θ) {
        return (double t) -> new Dupla(
                (1.0 / (4 * k)) * ((4 * k - 2) * Cos(t + θ) - Cos((k + 1) * t + θ) + 3 * Cos((k - 1) * t - θ)),
                (1.0 / (4 * k)) * ((5 * k - 4) * Sen(t + θ) - Sen((k + 1) * t + θ) - 3 * Sen((k - 1) * t - θ))
        );
    }

    public final static Curva_nativa Nubleré(double a, double b, double c, double d) {
        return (double t) -> new Dupla(Cos(a * t) - poten3(Cos(b * t)), Sen(c * t) - poten3(Sen(d * t)));
    }

    public final static Curva_nativa Epicicloide(double k) {
        return (double t) -> {
            double c = k + 1;
            return new Dupla(c * Cos(t) - Cos(c * t), c * Sen(t) - Sen(c * t)).Multiplicar(1 / k);
        };
    }

    public final static Curva_nativa Astroide() {
        return (double t) -> new Dupla(Cos3(t), Sen3(t));
    }

    public final static Curva_nativa Astroide(double k) {
        return Epicicloide(-k);
    }

    public final static Curva_nativa Cardioide(double k) {
        return Epicicloide(k);
    }

    public final static Curva_nativa RotarCurva(Curva_nativa curva, double θ) {
        return (double t) -> curva.XY(t).girar(θ);
    }

    public final static Curva_nativa TrasladarCurva(Curva_nativa curva, double Tx, double Ty) {
        return (double t) -> curva.XY(t).Adicionar(Tx, Ty);
    }

    public final static Curva_nativa TrasladarCurva(Curva_nativa curva, Dupla Txy) {
        return (double t) -> curva.XY(t).Adicionar(Txy);
    }

    public final static Curva_nativa CambiarSentidoY(Curva_nativa curva) {
        return (double t) -> curva.XY(t).CambiarSentidoY();
    }

    public final static Curva_nativa EscalarCurva(Curva_nativa curva, double Ex, double Ey) {
        return (double t) -> curva.XY(t).Escalar(Ex, Ey);
    }

    public final static Curva_nativa EscalarCurva(Curva_nativa curva, double E) {
        return (double t) -> curva.XY(t).Escalar(E);
    }

    public final static Curva_nativa EscalarCurva(Curva_nativa c, Object escala) {
        if (escala instanceof Number) {
            return (double t) -> c.XY(t).Escalar(((Number) escala).floatValue());
        }
        return (double t) -> c.XY(t).Escalar(escala);
    }

    public final static Curva_nativa Circulo() {
        return (double t) -> new Dupla(Cos(t), Sen(t));
    }

    public final static Curva_nativa CorazónPolar() {
        return (double t) -> {
            double r = Sen(t) * Fracción(raiz2(abs(Cos(t))), Sen(t) + 7f / 5) - 2 * Sen(t) + 2;
            return Circulo().XY(t).Escalar(r / 2);
        };
    }

    public final static Curva_nativa Circulo(double radio, Dupla centro) {
        return (double t) -> new Dupla(radio * Cos(t), radio * Sen(t)).Trasladar(centro);
    }

    public final static Curva_nativa RectaVectorial2Puntos(Dupla punto, Dupla punto2) {
        return (double t) -> punto2.Clon().Sustraer(punto).Multiplicar(t).Adicionar(punto);
    }

    static Dupla CorteEntre2Rectas(Curva_nativa R1, Curva_nativa R2) {
        return CorteEntre2Rectas(R1.XY(0), R1.XY(1), R2.XY(0), R2.XY(1));
    }

    static Dupla CorteEntre2Rectas(Dupla a, Dupla b, Dupla c, Dupla d) {
        double k1 = (b.X - a.X);
        double k2 = (b.Y - a.Y);
        double w = -Fracción(
                k1 * (c.Y - a.Y) - k2 * (c.X - a.X),
                k1 * (d.Y - c.Y) - k2 * (d.X - c.X)
        );
        double x = (d.X - c.X) * w + c.X;
        double y = (d.Y - c.Y) * w + c.Y;
        return new Dupla(x, y);
    }

    static Dupla CorteEntre2Rectas(double[] a, double[] b, double[] c, double[] d) {
        double k1 = (b[0] - a[0]);
        double k2 = (b[1] - a[1]);
        double w = -Fracción(
                k1 * (c[1] - a[1]) - k2 * (c[0] - a[0]),
                k1 * (d[1] - c[1]) - k2 * (d[0] - c[0])
        );
        double x = (d[0] - c[0]) * w + c[0];
        double y = (d[1] - c[1]) * w + c[1];
        return new Dupla(x, y);
    }

    public final static Curva_nativa PuntoMedio(Curva_nativa F, Curva_nativa F2) {
        return (double t) -> F.XY(t).PuntoMedio(F2.XY(t));
    }

    public final static Función SenoPoligonal(double v) {
        return (double x) -> Poligono(v).XY(x).Y;
    }

    public final static Función CosenoPoligonal(double v) {
        return (double x) -> Poligono(v).XY(x).X;
    }

    public final static Curva_nativa Poligono(double v) {
        final double k = 2 * π / v;
        return new Curva_nativa() {
            @Override
            public Dupla XY(double t) {
                Dupla.Curva_nativa curva = Dupla.Circulo();
                double Piv = Sgn(t) * Piso(abs(t) / k) * k, Pfv = Piv + Sgn(t) * k;
                if (Pfv == Piv) {
                    Pfv = Piv + k;
                }
                return CorteEntre2Rectas(
                        Dupla.ORIGEN.convVector(), curva.XY(t).convVector(),
                        curva.XY(Piv).convVector(), curva.XY(Pfv).convVector()
                );
            }
        };
    }

    public final static Curva_nativa ImagenPolar(Curva_nativa Función) {
        return ImagenEspacioParamétrico(Función, Circulo());
    }

    public final static Curva_nativa ImagenEspacioParamétrico(Curva_nativa Función, Curva_nativa FunciónTransformadora) {
        return (double t) -> FunciónTransformadora.XY(Función.XY(t).X).Escalar(Función.XY(t).Y);
    }

    public final static Curva_nativa PuntoAtractor(Curva_nativa función, Dupla puntoPrueba, double radio) {
        return (double t) -> función.XY(t).PuntoAtractor(puntoPrueba, radio);
    }

    public final static Curva_nativa funciónAtractora(Curva_nativa función, Curva_nativa funciónPrueba, double radio) {
        return (double t) -> función.XY(t).PuntoAtractor(funciónPrueba.XY(t), radio);
    }

    public final static Curva_nativa funciónPolarAtractora(Curva_nativa función, Curva_nativa funciónPolar, double radio, Dupla centro, double radioAtracción) {
        return (double t) -> función.XY(t).FunciónPolarAtractora(funciónPolar, radio, centro, radioAtracción);
    }

    public final static Curva_nativa Contra(Curva_nativa curva, double k) {
        return (double t) -> Contra(t, k, curva).T(curva.XY(t)); //Se le aplica una Transformación 
    }

    public static interface Transformación_Nativa {

        public Dupla T(Dupla d);
    }

    //Falta transformación de transformaciones
    public static Transformación_Nativa Translación(Dupla dt) {
        return (Dupla d) -> d.Adicionar(dt);
    }

    public static Transformación_Nativa Translación(double Txy) {
        return (Dupla d) -> d.Adicionar(Txy);
    }

    public static Transformación_Nativa Translación(double Tx, double Ty) {
        return (Dupla d) -> d.Adicionar(Tx, Ty);
    }

    public static Transformación_Nativa Escalación(double Ex, double Ey) {
        return (Dupla d) -> d.Escalar(Ex, Ey);
    }

    public static Transformación_Nativa Rotación(double θ) {
        return (Dupla d) -> d.girar(θ);
    }

    public static Transformación_Nativa PuntoAtractorT(Dupla puntoAtractor, double r) {
        return (Dupla d) -> d.PuntoAtractor(puntoAtractor, r);
    }

    public static Transformación_Nativa ImagenPolarT() {
        return (Dupla d) -> d.ImagenPolar();
    }

    public static Transformación_Nativa Contra(double t, double k, Curva_nativa curva) {
        return (Dupla d) -> {
            double CVR = Piso(abs(t) / k);
            double Pi = Sgn(t) * CVR * k;
            double Pf = Pi + Sgn(t) * k;
            double ti = Pf - t + Pi;
            Dupla T = curva.XY(Pi).Adicionar(curva.XY(Pf));
            return curva.XY(ti).Multiplicar(-1).Adicionar(T);
        };
    }

    public static class Vector {

        Dupla cola;
        Dupla cabeza;

        public Vector(Object cola, Object cabeza) {
            this.cola = new Dupla(cola);
            this.cabeza = new Dupla(cabeza);
        }

        public double Magnitud() {
            return cola.distanciaPunto(cabeza);
        }

        public double Ángulo() {
            return cabeza.Ángulo(cola);
        }

    }

    public static class Plano { //Para dibujado vectorial

        public Dupla Centro, Escala;
        public double Rotación = 0;
        public boolean CambioSentidoY = true, CambioSentidoX = false;
        public ArrayList<Transformación_Nativa> transformaciónesAdicionales = new ArrayList<>();

        final static Plano PlanoNatural = new Plano(ORIGEN, IDENTIDAD) {
            {
                CambioSentidoY = false;
            }
        };

        public Plano(Dupla Centro, Dupla Escala) {
            this.Centro = Centro;
            this.Escala = Escala;
        }

        public Dupla UbicaciónEnElPlano(Dupla puntoUbicar) {
            Dupla nuevoPunto = puntoUbicar.Clon();
            for (Transformación_Nativa transformaciónAdicional : transformaciónesAdicionales) {
                nuevoPunto = transformaciónAdicional.T(nuevoPunto);
            }
            if (Escala.esDiferente(new Dupla(1))) {
                nuevoPunto.Escalar(Escala);
            }
            if (Rotación > 0) {
                nuevoPunto.girar(Rotación);
            }
            if (CambioSentidoY) {
                nuevoPunto.CambiarSentidoY();
            }
            if (CambioSentidoX) {
                nuevoPunto.CambiarSentidoX();
            }
            if (Centro.esDiferente(ORIGEN)) {
                nuevoPunto.Trasladar(Centro);
            }
            return nuevoPunto;
        }

        public Dupla[] UbicaciónEnElPlano(Dupla[] puntosUbicar) {
            Dupla[] nuevosPuntos = new Dupla[puntosUbicar.length];
            for (int i = 0; i < puntosUbicar.length; i++) {
                nuevosPuntos[i] = UbicaciónEnElPlano(puntosUbicar[i]);
            }
            return nuevosPuntos;
        }

        public Dupla UbicaciónEnElPlano(Dupla puntoUbicar, Dupla TraslaciónAdicional) {
            return UbicaciónEnElPlano(puntoUbicar).Trasladar(TraslaciónAdicional);
        }

        public Curva_nativa AjustarAlPlano(Curva_nativa f) {
            return (double t) -> UbicaciónEnElPlano(f.XY(t));
        }
    }

    public static class PoligonoRadial extends ArrayList<Dupla> {

        public final static Rango RangoTabulación = new Rango(0, 2 * π);

        public Dupla posición;
        public double θ;

        public PoligonoRadial(Dupla... d) {
            AñadirNuevoVertice(d);
        }

        public PoligonoRadial(PoligonoRadial d) {
            Dupla[] d2 = new Dupla[d.size()];
            for (int i = 0; i < d.size(); i++) {
                d2[i] = d.get(i).Copia();
            }
            AñadirNuevoVertice(d2);
        }

        public ArrayList<Dupla> ObtenerImagen(Plano p) {
            ArrayList<Dupla> arr = new ArrayList<>();
            for (int i = 0; i < size(); i++) {
                arr.add(p.UbicaciónEnElPlano(get(i)));
            }
            return arr;
        }

        public void AñadirNuevoVertice(Dupla... d) {
            for (Dupla vertice : d) {
                if (!RangoTabulación.Por_Dentro(vertice.Y, true)) {
                    throw new Error(
                            "La generación del poligono debe estar entre 0 y 2π " + vertice.toString()
                    );
                } else {
                    add(vertice);
                }
            }
            sort((Object t, Object t1) -> {
                return new Double(((Dupla) t).Y).compareTo(((Dupla) t1).Y);
            });
            if (get(0).Y != 0) {
                throw new Error("Debe al menos haber un punto de ángulo el 0");
            }
            if (get(0).Copia().ReemplazarY(2 * π).esDiferente(get(size() - 1))) {
                add(get(0).Copia().ReemplazarY(2 * π));
            }
        }

        public Dupla Tabular(final double θ) {
            double θt = RangoTabulación.AjusteCiclico(θ);

            for (int i = 0; i < size() - 1; i++) {
                double θi = get(i).Y;
                double θf = get(i + 1).Y;

                Rango Detθ = new Rango(θi, θf);
                if (Detθ.Por_Dentro(θt, true)) {
                    return RectaVectorial2Puntos(
                            get(i).ImagenPolar(), get(i + 1).ImagenPolar()
                    ).XY(Detθ.Vp(θt))
                            .girar(this.θ)
                            .Trasladar(posición);
                }

            }
            return ORIGEN;
        }

        public Dupla TabularEquilibrio(double θ) {
            θ = RangoTabulación.AjusteCiclico(θ);

            for (int i = 0; i < size() - 1; i++) {
                Rango Detθ = new Rango(get(i).Y, get(i + 1).Y);
                if (Detθ.Por_Dentro(θ, true)) {
                    Rango DetR = new Rango(get(i).X, get(i + 1).X);
                    return new Dupla(DetR.Lr() * Detθ.Vp(θ) + DetR.Vi(), θ, RADIANES);
                }

            }
            return ORIGEN;
        }

        @Override
        public String toString() {
            String t = "";
            int i = 0;
            for (Dupla dupla : this) {
                t += i++ + dupla.toString() + "\n";
            }
            return t;
        }

    }

}
