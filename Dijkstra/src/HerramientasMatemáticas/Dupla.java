//Última actualización: 19/Febrero/2019
package HerramientasMatemáticas;

//<editor-fold defaultstate="collapsed" desc="Importe de librerias">
import java.awt.Component;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import java.awt.Toolkit;
import java.awt.geom.Dimension2D;

import static HerramientasMatemáticas.Matemática.*;
import static java.lang.Math.*;

import java.awt.Image;
import java.awt.geom.Point2D;
//</editor-fold>

public final class Dupla {

    //<editor-fold defaultstate="collapsed" desc="Constantes y Variables">
    //<editor-fold defaultstate="collapsed" desc="Variables del objeto">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Representa una de las componentes de la Dupla.
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQB0-qL8MPHoSFtfG5VIa3Zl5jFQ1aUoevZE0MH47C8_8ajHzXcwRr6l-ny1SaiNY9fDvckniIWoEaF/pub?w=200&h=200">
     */
    //</editor-fold>
    public double X, Y;
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Esta variable determina si las variables X y Y se pueden modificar, si es
     * false es porque no está protegido y por tanto X y Y se pueden modificar,
     * si es true entonces X y Y no se pueden modificar y por tanto cada vez que
     * se intente modificar la dupla no se podrá y en su lugar se crearán clones
     * de la Dupla, y los clones son los que serán modificados para
     * posteriormente ser retornados.
     * <br> <br>
     * todas las constantes estáticas de tipo Dupla en esta clase por defecto
     * vienen protegidas para poder operar con ellas libremente y que no pierdan
     * su valor original
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQA-1QvrA6DqEplVHvknQ2-fPvESX9YF_oL69FfUA3Lin9ajz9Y2RsAHob6SXXBwMvcjiIBGnVgRIOR/pub?w=380&h=380">
     */
    //</editor-fold>
    public boolean Protegido = false;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constantes estáticas">
    //<editor-fold defaultstate="collapsed" desc="Vectores constantes carácteristicos">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Esta variable representa el cero o origen, el punto
     * (<code>0</code>,<code>0</code>), es el Elemento neutro de la suma.
     * <br> <br>
     * <code>Protegido = true</code>
     * <br> <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vT0aiXu2CEgpZx9M5SFUA-HGcDl9QDrDJRn9Gk__-VPyvryJfD34zixi4AFR-jabFZ6zs_aZPXYVrix/pub?w=250&h=250">
     * <br> <br>
     * Decir que existe un vector cero (elemento neutro) tal que u+0=u, equivale
     * a exigir que exista un vector incapaz de efectuar, mediante la suma,
     * modificación alguna a todos los vectores.
     * <br> <br>
     * <img src="https://upload.wikimedia.org/wikipedia/commons/7/74/Vectorial_space_P_3.GIF">
     */
    //</editor-fold>
    public static final Dupla ORIGEN = new Dupla().Proteger();
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Esta variable representa el origen, el punto (1,1), es el Elemento neutro
     * de la multiplicación.
     * <br>
     * <br>
     * <code>Protegido = true</code>
     * <br> <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vS-wZXEf7OS3_RamgHO8fnYb8F4ZTJovPfWNLnj16dt1k7FdZIX2mMaWPNWDYYLS1r8knPPzrLlNeYE/pub?w=200&h=200">
     * <br> <br>
     * Decir que existe el escalar 1 tal que 1u=u, equivale a decir exista un
     * escalar incapaz de efectuar, mediante producto, modificación alguna a
     * todos los vectores
     * <br> <br>
     * <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/4/49/Vectorial_space_P_b.GIF/270px-Vectorial_space_P_b.GIF">
     */
    //</editor-fold>
    public static final Dupla IDENTIDAD = new Dupla(1).Proteger();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constante de dimensión de la pantalla de la maquina en la que se está ejecutando">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Esta variable representa el tamaño de la pantalla del computador donde el
     * programa se esté ejecutando.
     * <br>
     * <br>
     * <code>Protegido = true</code>
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vT1lZIQQXttpVYU7nviWsYMYsihBV-qmLkHgk4wLQPTkrTDcsI2wog5uGVjnjLwjzIUha_rtXcF-k14/pub?w=380&h=285">
     */
    //</editor-fold>
    public static final Dupla DIMENSIÓN_PANTALLA = new Dupla(
            Toolkit.getDefaultToolkit().getScreenSize()
    ).Proteger();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constantes para las dimensiones de video">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Esta variable representa el tamaño de 1280x720, a partir de aquí un video
     * se considera HD, tiene proporción de 16:9.
     * <br>
     * <br>
     * <code>Protegido = true</code>
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRyxRw-sVlCkyWd7Rol9CIbuYJCnSWrrB9WTysdTldqJAIZX0V6Z3-bw3y31jhVRghCe8Eh5IW4fjfJ/pub?w=380&h=380">
     */
    //</editor-fold>
    public static final Dupla THD_1280x720 = new Dupla(1280, 720).Proteger();
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Esta variable representa el tamaño de 905x509, es una resolución que se
     * considera de calidad media, tiene proporción de 16:9.
     * <br>
     * <br>
     * esta proporción resulta de tomar la resolución de 1280x720 y reducir su
     * área a la mitad, lo que es equivalente a dividir la dimensión entre raíz
     * de 2.
     * <br>
     * <br>
     * <code>Protegido = true</code>
     * <br>
     * <br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}&space;905\times&space;509&space;\approx&space;\tfrac{1}{\sqrt{2}}\bigg(1280\times&space;720\bigg)" title="905\times 509 \approx \tfrac{1}{\sqrt{2}}\bigg(1280\times 720\bigg)" />
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRyxRw-sVlCkyWd7Rol9CIbuYJCnSWrrB9WTysdTldqJAIZX0V6Z3-bw3y31jhVRghCe8Eh5IW4fjfJ/pub?w=380&h=380">
     */
    //</editor-fold>
    public static final Dupla TM_905x509 = THD_1280x720.Multiplicar(raiz2(1 / 2f)).Proteger();
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Esta variable representa el tamaño de 640x360, es una resolución que se
     * considera de calidad media, tiene proporción de 16:9.
     * <br>
     * <br>
     * esta proporción resulta de tomar la resolución de 1280x720 y reducir su
     * tamaño a la mitad
     * <br>
     * <br>
     * <code>Protegido = true</code>
     * <br>
     * <br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}&space;640\times&space;360&space;=&space;\tfrac{1}{2}\bigg(1280\times&space;720\bigg)"  />
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRyxRw-sVlCkyWd7Rol9CIbuYJCnSWrrB9WTysdTldqJAIZX0V6Z3-bw3y31jhVRghCe8Eh5IW4fjfJ/pub?w=380&h=380">
     */
    //</editor-fold>
    public static final Dupla TM_640x360 = THD_1280x720.Mitad().Proteger();
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Esta variable representa el tamaño de 320x180, es una resolución que se
     * considera de calidad baja, tiene proporción de 16:9, es usada en algunas
     * pantallas para celular.
     * <br>
     * <br>
     * esta proporción resulta de tomar la resolución de 1280x720 y reducir su
     * tamaño 4 veces
     * <br>
     * <br>
     * <code>Protegido = true</code>
     * <br>
     * <br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}&space;320\times&space;180&space;=&space;\tfrac{1}{4}\bigg(1280\times&space;720\bigg)" />
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRyxRw-sVlCkyWd7Rol9CIbuYJCnSWrrB9WTysdTldqJAIZX0V6Z3-bw3y31jhVRghCe8Eh5IW4fjfJ/pub?w=380&h=380">
     */
    //</editor-fold>
    public static final Dupla TSD_320x180 = THD_1280x720.Cuarto().Proteger();
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Esta variable representa el tamaño de 160x90, es una resolución que se
     * considera de calidad muy baja, tiene proporción de 16:9, es usada en
     * algunas pantallas para celular.
     * <br>
     * <br>
     * esta proporción resulta de tomar la resolución de 1280x720 y reducir su
     * tamaño 8 veces
     * <br>
     * <br>
     * <code>Protegido = true</code>
     * <br>
     * <br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}&space;16\times&space;90&space;=&space;\tfrac{1}{8}\bigg(1280\times&space;720\bigg)" />
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRyxRw-sVlCkyWd7Rol9CIbuYJCnSWrrB9WTysdTldqJAIZX0V6Z3-bw3y31jhVRghCe8Eh5IW4fjfJ/pub?w=380&h=380">
     */
    //</editor-fold>
    public static final Dupla TSD2_160x90 = THD_1280x720.Dividir(8).Proteger();
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constantes para la alineación entre dimensiones">
    //<editor-fold defaultstate="collapsed" desc="Carácteristicos de la alineación Vertical">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Identificador para la alineación vertical.
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vR_ECehg8ChvdibPKWDjp6FNB9suIU0kkkpmxUpMSPv6jkBEr1-CbKEolhbX2w2DI43KXJt7LzjVhGt/pub?w=382&h=320">
     */
    //</editor-fold>
    public final static byte ARRIBA = 1;

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Identificador para la alineación vertical.
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQF9AOA8n_1dX-myif2SWCj-2tXTD1zERCi5uDGtkZOIMOM7VVjEgjOd78XCcNRCR79Ui9I9-l_fh5j/pub?w=382&h=320">
     */
    //</editor-fold>
    public final static byte ABAJO = 2;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Alineación Centrada, válido para Horizontal y Vertical">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Identificador para la alineación vertical u horizontal.
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vTRQDm-IsGVecOv0qXkm6y_tdyaLvD2mY4-h1Pxq_sWT53nCc2RgTnu38uEPVddGp5NGe0qllnoMRZX/pub?w=382&h=320">
     */
    //</editor-fold>
    public final static byte MEDIO = 3;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Carácteristicos de la alineación Horizontal">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Identificador para la alineación Horizontal.
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vT2I-OMSD-LB0QTBxqcY8KDfaxoTWS0_MbF90lgti3h4OJbrhnPRVb6qKvclo3DYnw_DSak6YnqiBGL/pub?w=382&h=320">
     */
    //</editor-fold>
    public final static byte IZQUIERDA = 4;
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Identificador para la alineación Horizontal.
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vSyfDjNpM4OsUfQp38Ym_9wnVLvTqODHYgr1FMQ3Jt7NrmyXxYTbwCEsznhQR2cWcTbrR6gDwAjQ-ih/pub?w=382&h=320">
     */
    //</editor-fold>
    public final static byte DERECHA = 5;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="No se desea Especificar una alineación">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Identificador para NO especificar la alineación vertical u horizontal,
     * por defecto horizontalmente suele valer izquierda y verticalmente arriba,
     * ya que en el sistema coordenado matricial la columna de la izquierda es
     * <code>0</code> al igual que la fila de arriba.
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vShcrlQT9A79SsSKFqEU7TdcXjFrWvFsjoCluJkt0a6HF4eO83YJ0FAKgEU5mC9Y5ifRJYiEIn5ID_x/pub?w=382&h=320">
     */
    //</editor-fold>
    public final static byte POR_DEFECTO = -1;
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructores">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * Inicializa la dupla con el valor de (<code>0</code>,<code>0</code>)
     */
    //</editor-fold>
    public Dupla() {
        X = 0;
        Y = 0;
    }

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * Inicializa la dupla con los valores especificados
     */
    //</editor-fold>
    public Dupla(double X, double Y) {
        this.X = X;
        this.Y = Y;
    }

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Inicializa la dupla con un mismo valor para las componentes, algo
     * particular de este constructor es que cualquier vector inicializado por
     * aquí tendrá una inclinación de 45° o 225°.
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQEGh5ZooH6_aPUadCxRv2s9YCn7lFJyAZDMYnPPtwdGwh9A4gShg3XTcbwgCxLlflovPQJILc9wLph/pub?w=200&amp;h=200">
     */
    //</editor-fold>
    public Dupla(double n) {
        this.X = n;
        this.Y = n;
    }

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * recibe un arreglo de tamaño n y coge sus 2 primeras componentes para
     * inicializar la dupla, geométricamente es la proyección sobre el plano XY.
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRqwnjip7Is8hqJc7BQU7Dp3XinSrP-ZMlivUYXdsKMVyTQ18d_t91M5drRaMhLrC1e2TvakWmVirff/pub?w=300&h=225">
     */
    //</editor-fold>
    public Dupla(double... d) {
        X = d[0];
        Y = d[1];
    }

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * Hay diversos tipos de objetos que pueden ser interpretados como una
     * dupla, este constructor pretende tomar de difetentes tipos de objetos su
     * interpretación como dupla y así poder inicializar una dupla
     */
    //</editor-fold>
    public Dupla(Object obj) {//<editor-fold defaultstate="collapsed" desc="Generaliza a varios tipos de objetos que pueden ser interpretados como una Dupla">
        if (obj instanceof Dupla) {
            ReemplazarXY(((Dupla) obj).X, ((Dupla) obj).Y);
        } else if (obj instanceof Number) {
            ReemplazarXY(((Number) obj).doubleValue(), ((Number) obj).doubleValue());
        } else if (obj instanceof Dimension) {
            ReemplazarXY(((Dimension) obj).width, ((Dimension) obj).height);
        } else if (obj instanceof Dimension2D) {
            ReemplazarXY(((Dimension2D) obj).getWidth(), ((Dimension2D) obj).getHeight());
        } else if (obj instanceof Image) {
            ReemplazarXY(((Image) obj).getWidth(null), ((Image) obj).getHeight(null));
        } else if (obj instanceof Point) {
            ReemplazarXY(((Point) obj).x, ((Point) obj).y);
        } else if (obj instanceof Component) {
            ReemplazarXY(((Component) obj).getWidth(), ((Component) obj).getHeight());
        } else {
            throw new RuntimeException("El objeto usado para inicializar la dupla no es válido");
        }
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Inicializa la Dupla a partir de una coordenada en polar, el tercer
     * parametro es una especificación para el tipo de ángulo, este
     * identificador se encontrará en la clase Matemática
     * <br><br>
     * <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/13/Polar_graph_paper.svg/250px-Polar_graph_paper.svg.png">.
     * <br><br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}&space;\\x=&space;r&space;\cdot&space;cos&space;\theta&space;\\&space;y=&space;r&space;\cdot&space;sen&space;\theta">
     */
    //</editor-fold>
    public Dupla(double Radio, double θ, byte TIPO_ÁNGULO) {
        X = Radio * cos(Radianes(θ, TIPO_ÁNGULO));
        Y = Radio * sen(Radianes(θ, TIPO_ÁNGULO));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas de modificación y obtención (Getters y Setters)">
    //<editor-fold defaultstate="collapsed" desc="Herramientas de modificación (Setters)">
    //<editor-fold defaultstate="collapsed" desc="Protección para evitar la modificación de las  variables X,Y">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Protege la dupla poniendo la variable Protegido en true, evitando que X y
     * Y puedan ser modificadas, y hará que en cada intento se retornen clones.
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRRlX3i0eoIfsEsW9mWf729PHGhdH7Ft0YiGMAJf_mA41hNHUscqZWoiXy5GM9CDbnK0-lQPmdf5MZv/pub?w=200&h=200">
     */
    //</editor-fold>
    public Dupla Proteger() {
        Protegido = true;
        return this;
    }

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Desprotege la dupla poniendo la variable Protegido en false, permitiendo
     * que X y Y puedan ser modificadas.
     * <br>
     * <br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vSX46yln4kwXt3puUri6ciUPGPGB3srnXvS9HgrG9A7x1BXMbbxhQDLXrCr7owcOk-0kRkspAXvH58w/pub?w=200&h=200">
     */
    //</editor-fold>
    public Dupla Desproteger() {
        Protegido = false;
        return this;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Operaciones elementales">
    //<editor-fold defaultstate="collapsed" desc="Herramientas basadas en la Suma (+)">
    //<editor-fold defaultstate="collapsed" desc="Adición">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Adiciona un objeto que se pueda interpretar en dupla a la dupla
     * <br> <br>
     * <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Vectoren_optellen.svg/320px-Vectoren_optellen.svg.png">.
     * <br> <br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}A(\vec{a})=(x,y)&plus;\vec{a}=(x&plus;\vec{a_x}\text{&space;},\text{&space;}y&plus;\vec{a_y})" />
     */
    //</editor-fold>
    public Dupla Adicionar(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Adicionar(dupla.X, dupla.Y);
        }
        return Adicionar(dupla.X, dupla.Y);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Adiciona un mismo número a ambas componentes.
     * <br> <br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}A(n)=(x&plus;n\text{&space;},\text{&space;}y&plus;n)" />
     */
    //</editor-fold>
    public Dupla Adicionar(double n) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Adicionar(n, n);
        }
        return Adicionar(n, n);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Adiciona 2 números a la dupla en sus componentes respectivos.
     * <br> <br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}A(n_1,n_2)=(x&plus;n_1\text{&space;},\text{&space;}y&plus;n_2)" />
     */
    //</editor-fold>
    public Dupla Adicionar(double x, double y) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Adicionar(x, y);
        }
        X += x;
        Y += y;
        return this;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Adición únicamente para X">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Adiciona la componente X de un elemento que se pueda interpretar como
     * dupla.
     * <br>
     * <br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}A_x(\vec{a})=(x&plus;\vec{a_x}\text{&space;},\text{&space;}y)" />
     */
    //</editor-fold>
    public Dupla AdicionarX(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().AdicionarX(obj);
        }
        X += new Dupla(obj).X;
        return this;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Adiciona un número a la componente X.
     * <br>
     * <br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}A_x(n)=(x&plus;n\text{&space;},\text{&space;}y)" />
     */
    //</editor-fold>
    public Dupla AdicionarX(double x) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().AdicionarX(x);
        }
        X += x;
        return this;
    }//</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Adición únicamente para Y">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Adiciona la componente Y de un elemento que se pueda interpretar como
     * dupla.
     * <br>
     * <br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}A_y(\vec{a})=(x\text{&space;},\text{&space;}y&plus;\vec{a_y})" />
     */
    //</editor-fold>
    public Dupla AdicionarY(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().AdicionarY(obj);
        }
        Y += new Dupla(obj).Y;
        return this;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Adiciona un número a la componente X.
     * <br>
     * <br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}A_y(n)=(x\text{&space;},\text{&space;}y+n)" />
     */
    //</editor-fold>
    public Dupla AdicionarY(double y) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Adicionar(0, y);
        }
        return Adicionar(0, y);
    }//</editor-fold>
    //</editor-fold>
    //</editor-fold>

    /*      */ //<editor-fold defaultstate="collapsed" desc="Traslación">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Adiciona un objeto que se pueda interpretar en dupla a la dupla
     * <br><br>
     * <code> Es sinónimo de Adicionar</code>
     * <br><br>
     * <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Vectoren_optellen.svg/320px-Vectoren_optellen.svg.png">.
     * <br><br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}T(\vec{a})=(x,y)&plus;\vec{a}=(x&plus;\vec{a_x}\text{&space;},\text{&space;}y&plus;\vec{a_y})" />
     */
    //</editor-fold>
    public Dupla Trasladar(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Adicionar(dupla);
        }
        return Adicionar(dupla);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Adiciona un mismo número a ambas componentes.
     * <br><br>
     * <code> Es sinónimo de Adicionar</code>
     * <br><br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}T(n)=(x&plus;n\text{&space;},\text{&space;}y&plus;n)" />
     */
    //</editor-fold>
    public Dupla Trasladar(double n) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">

        if (Protegido) {
            return Clon().Adicionar(n);
        }
        return Adicionar(n);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Adiciona 2 números a la dupla en sus componentes respectivos.
     * <br><br>
     * <code> Es sinónimo de Adicionar</code>
     * <br><br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}T(n_1,n_2)=(x&plus;n_1\text{&space;},\text{&space;}y&plus;n_2)" />
     */
    //</editor-fold>
    public Dupla Trasladar(double x, double y) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">

        if (Protegido) {
            return Clon().Trasladar(x, y);
        }
        return Adicionar(x, y);
    }//</editor-fold>
    //</editor-fold>

    /*      */ //<editor-fold defaultstate="collapsed" desc="Desplazamiento cruz en una matriz">
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * En el sistema coordenado matricial, al ser un sistema discreto la
     * desplazación mínima es de 1 unidad.
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQkQW1IcHRvf_hjocPhoIVOC1iAMWgvWemfWGr_8-utsqWaPrJwxynZPdPrbJMDbSbIRiJwGGNMk-a5/pub?w=350&amp;h=250">
     * <br><br>
     * Es un movimiento lateral que dezplaza una posición a la derecha la dupla
     * <br><br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}D()=(x+1\text{&space;},\text{&space;}y)" />
     */
    //</editor-fold>
    public Dupla Desplazar1_Derecha() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Desplazar1_Derecha();
        }
        X++;
        return this;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * En el sistema coordenado matricial, al ser un sistema discreto la
     * desplazación mínima es de 1 unidad.
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQkQW1IcHRvf_hjocPhoIVOC1iAMWgvWemfWGr_8-utsqWaPrJwxynZPdPrbJMDbSbIRiJwGGNMk-a5/pub?w=350&amp;h=250">
     * <br><br>
     * Es un movimiento lateral que dezplaza una posición a la izquierda la
     * dupla
     * <br><br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}D()=(x-1\text{&space;},\text{&space;}y)" />
     */
    //</editor-fold>
    public Dupla Desplazar1_Izquierda() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Desplazar1_Izquierda();
        }
        X--;
        return this;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * En el sistema coordenado matricial, al ser un sistema discreto la
     * desplazación mínima es de 1 unidad.
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQkQW1IcHRvf_hjocPhoIVOC1iAMWgvWemfWGr_8-utsqWaPrJwxynZPdPrbJMDbSbIRiJwGGNMk-a5/pub?w=350&amp;h=250">
     * <br><br>
     * Es un movimiento lateral que dezplaza una posición abajo la dupla
     * <br><br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}D()=(x\text{&space;},\text{&space;}y+1)" />
     */
    //</editor-fold>
    public Dupla Desplazar1_Abajo() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">

        if (Protegido) {
            return Clon().Desplazar1_Abajo();
        }
        Y++;
        return this;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * En el sistema coordenado matricial, al ser un sistema discreto la
     * desplazación mínima es de 1 unidad.
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQkQW1IcHRvf_hjocPhoIVOC1iAMWgvWemfWGr_8-utsqWaPrJwxynZPdPrbJMDbSbIRiJwGGNMk-a5/pub?w=350&amp;h=250">
     * <br><br>
     * Es un movimiento lateral que dezplaza una posición arriba la dupla
     * <br><br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}D()=(x\text{&space;},\text{&space;}y-1)" />
     */
    //</editor-fold>
    public Dupla Desplazar1_Arriba() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">

        if (Protegido) {
            return Clon().Desplazar1_Arriba();
        }
        Y--;
        return this;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * En el sistema coordenado matricial, al ser un sistema discreto la
     * desplazación mínima es de 1 unidad.
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQkQW1IcHRvf_hjocPhoIVOC1iAMWgvWemfWGr_8-utsqWaPrJwxynZPdPrbJMDbSbIRiJwGGNMk-a5/pub?w=350&amp;h=250">
     * <br><br>
     * Es un movimiento diagonal que dezplaza la dupla una posición a la derecha
     * y una arriba
     * <br><br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}D()=(x+1\text{&space;},\text{&space;}y-1)" />
     */
    //</editor-fold>
    public Dupla Desplazar1_DerechaArriba() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Desplazar1_Derecha();
        }
        Y--;
        X++;
        return this;
    }//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">

    /**
     * <p align="center">
     * En el sistema coordenado matricial, al ser un sistema discreto la
     * desplazación mínima es de 1 unidad.
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQkQW1IcHRvf_hjocPhoIVOC1iAMWgvWemfWGr_8-utsqWaPrJwxynZPdPrbJMDbSbIRiJwGGNMk-a5/pub?w=350&amp;h=250">
     * <br><br>
     * Es un movimiento diagonal que dezplaza la dupla una posición a la
     * izquierda y una abajo
     * <br><br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}D()=(x-1\text{&space;},\text{&space;}y+1)" />
     */
    //</editor-fold>
    public Dupla Desplazar1_IzquierdaAbajo() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Desplazar1_IzquierdaAbajo();
        }
        X--;
        Y++;
        return this;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * En el sistema coordenado matricial, al ser un sistema discreto la
     * desplazación mínima es de 1 unidad.
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQkQW1IcHRvf_hjocPhoIVOC1iAMWgvWemfWGr_8-utsqWaPrJwxynZPdPrbJMDbSbIRiJwGGNMk-a5/pub?w=350&amp;h=250">
     * <br><br>
     * Es un movimiento diagonal que dezplaza la dupla una posición a la derecha
     * y una abajo
     * <br><br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}D()=(x+1\text{&space;},\text{&space;}y+1)" />
     */
    //</editor-fold>
    public Dupla Desplazar1_DerechaAbajo() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Desplazar1_DerechaAbajo();
        }
        Y++;
        X++;
        return this;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * En el sistema coordenado matricial, al ser un sistema discreto la
     * desplazación mínima es de 1 unidad.
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQkQW1IcHRvf_hjocPhoIVOC1iAMWgvWemfWGr_8-utsqWaPrJwxynZPdPrbJMDbSbIRiJwGGNMk-a5/pub?w=350&amp;h=250">
     * <br><br>
     * Es un movimiento diagonal que dezplaza la dupla una posición a la
     * izquierda y una arriba
     * <br><br>
     * <img src="https://latex.codecogs.com/gif.latex?\dpi{150}D()=(x-1\text{&space;},\text{&space;}y-1)" />
     */
    //</editor-fold>
    public Dupla Desplazar1_IzquierdaArriba() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Desplazar1_IzquierdaArriba();
        }
        Y--;
        X--;
        return this;
    }//</editor-fold>
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sustracción">
    public Dupla Sustraer(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Adicionar(-dupla.X, -dupla.Y);
        }
        return Adicionar(-dupla.X, -dupla.Y);
    }//</editor-fold>

    public Dupla Sustraer(double n) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Adicionar(-n, -n);
        }
        return Adicionar(-n, -n);
    }//</editor-fold>

    public Dupla Sustraer(double x, double y) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Sustraer(x, y);
        }
        X -= x;
        Y -= y;
        return this;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sustracción Únicamenta para X">
    public Dupla SustraerX(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Adicionar(-dupla.X, 0);
        }
        return Adicionar(-dupla.X, 0);
    }//</editor-fold>

    public Dupla SustraerX(double x) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Adicionar(-x, 0);
        }
        return Adicionar(-x, 0);
    }//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Sustracción únicamente para Y">
    public Dupla SustraerY(Object obj) { //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Adicionar(0, -dupla.Y);
        }
        return Adicionar(0, -dupla.Y);
    }//</editor-fold>

    public Dupla SustraerY(double y) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Adicionar(0, -y);
        }
        return Adicionar(0, -y);
    }//</editor-fold>
    //</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas basadas en la Multiplicación (*)">
//<editor-fold defaultstate="collapsed" desc="Multiplicación">
    public Dupla Multiplicar(double n) { //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Multiplicar(n);
        }
        X *= n;
        Y *= n;
        return this;
    }//</editor-fold>

    public Dupla Multiplicar(double x, double y) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Multiplicar(x, y);
        }
        X *= x;
        Y *= y;
        return this;
    }//</editor-fold>

    public Dupla Multiplicar(Object obj) { //<editor-fold defaultstate="collapsed" desc="Implementación de código">

        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Multiplicar(obj);
        }
        X *= dupla.X;
        Y *= dupla.Y;
        return this;
    }//</editor-fold>

    public Dupla Multiplicar(double a, double b, double c, double d) { //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().ReemplazarXY(a * X + b * Y, c * X + d * Y);
        }
        return ReemplazarXY(a * X + b * Y, c * X + d * Y);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Multiplicar Únicamente para X">
    public Dupla MultiplicarX(double Numero) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().MultiplicarX(Numero);
        }
        X *= Numero;
        return this;
    }//</editor-fold>

    public Dupla MultiplicarX(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().MultiplicarX(obj);
        }
        X *= new Dupla(obj).X;
        return this;
    }//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Multiplicar únicamente para Y">
    public Dupla MultiplicarY(double Numero) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().MultiplicarY(Numero);
        }
        Y *= Numero;
        return this;
    }//</editor-fold>

    public Dupla MultiplicarY(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">

        if (Protegido) {
            return Clon().MultiplicarY(obj);
        }
        Y *= new Dupla(obj).Y;
        return this;
    }//</editor-fold>
//</editor-fold>
//</editor-fold>

    /*      */ //<editor-fold defaultstate="collapsed" desc="Escalación">
    public Dupla Escalar(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Escalar(dupla);
        }
        return Multiplicar(dupla);
    }//</editor-fold>

    public Dupla Escalar(double x, double y) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Multiplicar(x, y);
        }
        return Multiplicar(x, y);
    }//</editor-fold>

    public Dupla Escalar(double escala) { //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Multiplicar(escala);
        }
        return Multiplicar(escala);
    }//</editor-fold>
//</editor-fold>

    /*       *///<editor-fold defaultstate="collapsed" desc="Cambio de sentido">
    public Dupla CambiarSentido() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Multiplicar(-1);
        }
        return Multiplicar(-1);
    } //</editor-fold>

    public Dupla CambiarSentidoX() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">

        if (Protegido) {
            return Clon().MultiplicarX(-1);
        }
        return MultiplicarX(-1);
    }
//</editor-fold>

    public Dupla CambiarSentidoY() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">

        if (Protegido) {
            return Clon().MultiplicarY(-1);
        }
        return MultiplicarY(-1);
    }
//</editor-fold>
//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas basadas en la División (/)">
//<editor-fold defaultstate="collapsed" desc="División">
    public Dupla Dividir(double x, double y) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Dividir(x, y);
        }
        X /= x;
        Y /= y;
        return this;
    }//</editor-fold>

    public Dupla Dividir(double n) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Dividir(n);
        }
        X /= n;
        Y /= n;
        return this;
    }//</editor-fold>

    public Dupla Dividir(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().Dividir(dupla);
        }
        X /= dupla.X;
        Y /= dupla.Y;
        return this;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Dividir únicamente para X">
    public Dupla DividirX(double Numero) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().DividirX(Numero);
        }
        X /= Numero;
        return this;
    }//</editor-fold>

    public Dupla DividirX(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().DividirX(dupla);
        }
        X /= dupla.X;
        return this;
    }//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Dividir Únicamente para Y">
    public Dupla DividirY(double Numero) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().DividirY(Numero);
        }
        Y /= Numero;
        return this;
    }//</editor-fold>

    public Dupla DividirY(Object obj) { //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().DividirY(dupla);
        }
        Y /= dupla.Y;
        return this;
    }//</editor-fold>
//</editor-fold>
//</editor-fold>

    /*       *///<editor-fold defaultstate="collapsed" desc="Normalización">
    public Dupla Normalizar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().Dividir(Magnitud());
        }
        return this.Dividir(Magnitud());
    }//</editor-fold>
//</editor-fold>
//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas de Reemplazación">
    //<editor-fold defaultstate="collapsed" desc="Reemplazamientos rectángulares">
    public Dupla ReemplazarXY(double nuevoX, double nuevoY) { //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().ReemplazarXY(nuevoX, nuevoY);
        }
        X = nuevoX;
        Y = nuevoY;
        return this;
    }//</editor-fold>

    public Dupla ReemplazarXY(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().ReemplazarXY(obj);
        }
        X = dupla.X;
        Y = dupla.Y;
        return this;
    }//</editor-fold>

    public Dupla ReemplazarX(double nuevoX) { //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().ReemplazarX(nuevoX);
        }
        X = nuevoX;
        return this;
    }//</editor-fold>

    public Dupla ReemplazarX(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().ReemplazarX(obj);
        }
        X = dupla.X;
        return this;
    }//</editor-fold>

    public Dupla ReemplazarY(double nuevoY) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().ReemplazarY(nuevoY);
        }
        Y = nuevoY;
        return this;
    }//</editor-fold>

    public Dupla ReemplazarY(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla dupla = new Dupla(obj);
        if (Protegido) {
            return Clon().ReemplazarY(obj);
        }
        Y = dupla.Y;
        return this;
    }//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Reemplazamientos polares">
    public Dupla ReemplazarRθ(double nuevoRadio, double Nuevoθ) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().ReemplazarXY(nuevoRadio * cos(Nuevoθ), nuevoRadio * sen(Nuevoθ));
        }
        return ReemplazarXY(nuevoRadio * cos(Nuevoθ), nuevoRadio * sen(Nuevoθ));
    }//</editor-fold>

    public Dupla ReemplazarRadio(double NuevoRadio) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        double θ = θ();
        if (Protegido) {
            return Clon().ReemplazarXY(NuevoRadio * cos(θ), NuevoRadio * sen(θ));
        }
        return ReemplazarXY(NuevoRadio * cos(θ), NuevoRadio * sen(θ));
    }//</editor-fold>

    public Dupla Reemplazarθ(double θ) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        double r = Radio();
        if (Protegido) {
            return Clon().ReemplazarXY(r * cos(θ), r * sen(θ));
        }
        return ReemplazarXY(r * cos(θ), r * sen(θ));
    }//</editor-fold>
//</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas de giro">
    public Dupla girar(double θ) { //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        double a = cos(θ), b = sen(θ);
        if (Protegido) {
            return Clon().ReemplazarXY(a * X - b * Y, b * X + a * Y);
        }
        return ReemplazarXY(a * X - b * Y, b * X + a * Y);
    }//</editor-fold>

    public Dupla girar(double Δθ, byte TIPO_ÁNGULO) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().girar(Radianes(Δθ, TIPO_ÁNGULO));
        }
        return girar(Radianes(Δθ, TIPO_ÁNGULO));
    }//</editor-fold>

    public Dupla g90Derecha() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().ReemplazarXY(Y, -X);
        }
        return ReemplazarXY(Y, -X);
    }//</editor-fold>

    public Dupla g90Izquierda() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().ReemplazarXY(-Y, X);
        }
        return ReemplazarXY(-Y, X);
    }//</editor-fold>
//</editor-fold>

    public Dupla Invertir_XYaYX() { //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().ReemplazarXY(Y, X);
        }
        return ReemplazarXY(Y, X);
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas experimentales">
    public Dupla Atracción(Dupla puntoPrueba, double r) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (Protegido) {
            return Clon().PuntoAtractor(puntoPrueba, r);
        }
        if (puntoPrueba.distanciaPunto(this) == 0) {
            return this;
        }
        Dupla Atracción = new Dupla(r / (puntoPrueba.distanciaPunto(this)), puntoPrueba.Ángulo(this), ÁNGULO_RADIANES);
        return Atracción;
    }//</editor-fold>

    public Dupla PuntoAtractor(Dupla puntoPrueba, double r) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">

        Dupla Atracción = Atracción(puntoPrueba, r);
        return Atracción.Radio() > distanciaPunto(puntoPrueba) ? ReemplazarXY(puntoPrueba) : Trasladar(Atracción);
    }//</editor-fold>
    //</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas de obtención (Getters)">
    //<editor-fold defaultstate="collapsed" desc="Herramientas de retorno númerico">
    //<editor-fold defaultstate="collapsed" desc="Obtención del ángulo">
    //<editor-fold defaultstate="collapsed" desc="Con respecto a un punto">
    public double Ángulo(Object Cola) {
        Dupla dupla = new Dupla(Cola);
        return Ángulo(dupla.X, dupla.Y);
    }

    public double Ángulo(Dupla Cola) {
        return Ángulo(Cola.X, Cola.Y);
    }

    public double Ángulo(double Cola_X, double Cola_Y) {//Vector
        double θ = Atan2(Y - Cola_Y, X - Cola_X);
        return θ < 0 ? θ + 2 * π : θ;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Con respecto al origen">
    public double Ángulo() {
        double θ = Atan2(Y, X);
        return θ < 0 ? θ + 2 * π : θ;
    }

    public double θ() {
        return Ángulo();
    }
//</editor-fold>
//</editor-fold>

    public double ProductoPunto(Dupla p) {
        return X * p.X + Y * p.Y;
    }

    //<editor-fold defaultstate="collapsed" desc="Area XY">
    public double Área() {
        return absX() * absY();
    }

    public int intÁrea() {
        return (int) Área();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Formas e interpretaciones de X">
    //<editor-fold defaultstate="collapsed" desc="Enteros">
    public int intX() {
        return (int) X;
    }

    public int Columna() {
        return intX();
    }

    public int Ancho() {
        return rndX(); //Probablemente haya que cambiarlo por absX()
    }

    public int rndX() {
        return Redondeo(X);
    }
    //</editor-fold>

    public float floatX() {
        return (float) X;
    }

    public double absX() {
        return abs(X);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Formas e interpretaciones de Y">
    //<editor-fold defaultstate="collapsed" desc="Enteros">
    public int intY() {
        return (int) Y;
    }

    public int Fila() {
        return intY();
    }

    public int rndY() {
        return Redondeo(Y);
    }

    public int Alto() {
        return rndY(); //Probablemente haya que cambiarlo por absY()
    }
    //</editor-fold>

    public float floatY() {
        return (float) Y;
    }

    public double absY() {
        return abs(Y);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones Basadas en el Teorema de Pitagoras">
    //<editor-fold defaultstate="collapsed" desc="Distancia con respecto a otro punto">
    public double distanciaPunto(Dupla punto) {
        return TeoremaDePitagoras(X - punto.X, Y - punto.Y);
    }

    public double distanciaPunto(double x, double y) {
        return TeoremaDePitagoras(X - x, Y - y);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Radio, Distancia al origen">
    public double Radio(Dupla Cola) {//Vector
        return distanciaPunto(Cola);
    }

    public double Radio2() {
        double r = Radio();
        return r * r;
    }

    public double Radio() {
        return TeoremaDePitagoras(X, Y);
    }

    public double Magnitud() {
        return Radio();
    }
//</editor-fold>
    //</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas de comparación de retorno booleano">
    //<editor-fold defaultstate="collapsed" desc="Es igual (=)">
    @Override
    public boolean equals(Object obj) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
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
    }//</editor-fold>

    public boolean esIgual(Object obj) {
        return equals(obj);
    }

    public boolean esIgual(double x, double y) {
        return X == x && Y == y;
    }
    //</editor-fold>

    public boolean esMayor(Object obj) {
        Dupla d = new Dupla(obj);
        return X > d.X || Y > d.Y;
    }

    public boolean esMayorOIgual(Object obj) {
        Dupla d = new Dupla(obj);
        return X >= d.X || Y >= d.Y;
    }

    public boolean esMenor(Object obj) {
        Dupla d = new Dupla(obj);
        return X < d.X || Y < d.Y;
    }

    public boolean esMenorOIgual(Object obj) {
        Dupla d = new Dupla(obj);
        return X <= d.X || Y <= d.Y;
    }

    public boolean esDiferente(Object obj) {
        return !esIgual(obj);
    }

    //<editor-fold defaultstate="collapsed" desc="Comparacion unicamente con respecto a X">
    public boolean XesMayor(Object obj) {
        return X > new Dupla(obj).X;
    }

    public boolean XesMenor(Object obj) {
        return X < new Dupla(obj).X;
    }

    public boolean XesMayor_Igual(Object obj) {
        return X >= new Dupla(obj).X;
    }

    public boolean XesMenor_Igual(Object obj) {
        return X <= new Dupla(obj).X;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Comparacion unicamente con respecto a Y">
    public boolean YesMenor(Object obj) {
        return Y < new Dupla(obj).Y;
    }

    public boolean YesMayor(Object obj) {
        return Y > new Dupla(obj).Y;
    }

    public boolean YesMenor_Igual(Object obj) {
        return Y <= new Dupla(obj).Y;
    }

    public boolean YesMayor_Igual(Object obj) {
        return Y >= new Dupla(obj).Y;
    }
//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas para obtener información clonando y no modificando la original">
    public Dupla Clon() {
        return new Dupla(X, Y);
    }

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * <p align="center">
     * Decir que u+(-u)=0, es exigir la existencia de un elemento opuesto, -u,
     * que sumado a u simplifique en un vector cero.
     * <br>
     * <br>
     * <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/3c/Vectorial_space_P_4.GIF/180px-Vectorial_space_P_4.GIF">
     */
    //</editor-fold>
    public Dupla ElementoOpuesto() {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        return Clon().CambiarSentido();
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Distancia dirigida">
    public Dupla distanciaDirigida(Dupla punto) {
        return Clon().Sustraer(punto);
    }

    public Dupla distanciaDirigida(double x, double y) {
        return Clon().Sustraer(x, y);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Fracciones de la Dupla">
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
//</editor-fold>

    public Dupla PuntoMedio(Dupla punto) {
        return Clon().Adicionar(punto).Dividir(2);
    }

    public Dupla Convertir_Polar() {
        return new Dupla(Radio(), θ());
    }

    //<editor-fold defaultstate="collapsed" desc="Proyección ortogonal">
    public Dupla ProyectarOrtogonalmenteEn(Object u) {
        Dupla dupla = new Dupla(u);
        return dupla.Escalar(ProductoPunto(dupla) / dupla.Radio2());
    }

    public Dupla ProyectarOrtogonalmenteEn(Object a, Object b) {
        Dupla u = new Dupla(b).Sustraer(a);
        Dupla v = Clon().Sustraer(a);
        return v.ProyectarOrtogonalmenteEn(u).Adicionar(a);
    }
//</editor-fold>
//</editor-fold>
//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas de visualización textual">
    //<editor-fold defaultstate="collapsed" desc="Herramientas para imprimir en la consola">
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
//</editor-fold>

    @Override
    public String toString() {//<editor-fold defaultstate="collapsed" desc="Implementacion del codigo">
        return "<" + "X=" + String.format("%.4f", X) + ", Y=" + String.format("%.4f", Y) + '>';
    }//</editor-fold>
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas de conversión de tipo">
    public double[] convVector() {
        return new double[]{X, Y};
    }

    public Dimension convDimension() {
        return new Dimension(rndX(), rndY());
    }

    public Point2D convPoint2D() {
        return new Point2D.Double(X, Y);
    }

    public Point convPoint() {
        return new Point(rndX(), rndY());
    }

    public BufferedImage convBufferedImage() {
        return new BufferedImage(X < 1 ? 1 : rndX(), Y < 1 ? 1 : rndY(), BufferedImage.TYPE_INT_ARGB);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones independientes, métodos estáticos">
    public static BufferedImage convBufferedImage(double x, double y) {
        return new Dupla(x, y).convBufferedImage();
    }

    public static BufferedImage convBufferedImage(Object obj) {
        return new Dupla(obj).convBufferedImage();
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

    public static Dupla PosiciónCursorEnComponente(Component c) { //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Point posicionActual = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(posicionActual, c);
        return new Dupla(posicionActual);
    }//</editor-fold>

    public static Dupla Alinear(Object DimEnvolvente, Object DimMenor, byte AlineaciónH, byte AlineaciónV) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código">
        Dupla DuplaEnvolvente = new Dupla(DimEnvolvente).Sustraer(new Dupla(DimMenor));
        return new Dupla(
                AlineaciónH == DERECHA ? DuplaEnvolvente.X : AlineaciónH == MEDIO ? DuplaEnvolvente.X / 2 : 0,
                AlineaciónV == ABAJO ? DuplaEnvolvente.Y : AlineaciónV == MEDIO ? DuplaEnvolvente.Y / 2 : 0
        );
    }//</editor-fold>

    public static Dupla CorteEntre2Rectas(Dupla a, Dupla b, Dupla c, Dupla d) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        double k1 = (b.X - a.X);
        double k2 = (b.Y - a.Y);
        double w = -Fracción(
                k1 * (c.Y - a.Y) - k2 * (c.X - a.X),
                k1 * (d.Y - c.Y) - k2 * (d.X - c.X)
        );
        double x = (d.X - c.X) * w + c.X;
        double y = (d.Y - c.Y) * w + c.Y;
        return new Dupla(x, y);
    }//</editor-fold>

    public static Dupla CorteEntre2Rectas(double[] a, double[] b, double[] c, double[] d) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        double k1 = (b[0] - a[0]);
        double k2 = (b[1] - a[1]);
        double w = -Fracción(
                k1 * (c[1] - a[1]) - k2 * (c[0] - a[0]),
                k1 * (d[1] - c[1]) - k2 * (d[0] - c[0])
        );
        double x = (d[0] - c[0]) * w + c[0];
        double y = (d[1] - c[1]) * w + c[1];
        return new Dupla(x, y);
    }//</editor-fold>
//</editor-fold>

    public static interface Curva {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la clase">

        Dupla XY(double t);

        //<editor-fold defaultstate="collapsed" desc="Curvas caracteristicas">
        static Curva RectaVectorial2Puntos(Dupla a, Dupla b) {
            return (t) -> new Dupla((b.X - a.X) * t + a.X, (b.Y - a.Y) * t + a.Y);
        }

        static Curva Circulo(Dupla centro, double radio) {
            return Circulo(centro, radio, 0);
        }

        static Curva Circulo(Dupla centro, double radio, double Δθ) {
            return (t) -> new Dupla(radio * Math.cos(t + Δθ) + centro.X, radio * Math.sin(t + Δθ) + centro.Y);
        }

        static Curva Funcion(Función f) {
            return (t) -> new Dupla(t, f.Y(t));
        }
        //</editor-fold>
    }
//</editor-fold>

}
