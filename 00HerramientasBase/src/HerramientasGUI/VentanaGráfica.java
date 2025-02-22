package HerramientasGUI;

import HerramientasGUI.AccesoriosVentanaGráfica.MenúCabeceraVentanaGráfica;
import static HerramientaArchivos.BibliotecaArchivos.Imagenes.Logos.*;
import HerramientasSistema.Sistema;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

import static HerramientaDeImagen.Filtros_Lineales.*;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientasMatemáticas.Dupla;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class VentanaGráfica extends JFrame implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, WindowListener {

    public static final int DIMENSIÓN_MUY_PEQUEÑA = 0;
    public static final int DIMENSIÓN_PEQUEÑA = 1;
    public static final int DIMENSIÓN_MEDIANA = 2;
    public static final int DIMENSIÓN_MEDIANA_GRANDE = 3;
    public static final int DIMENSIÓN_GRANDE = 4;
    public static final int DIMENSIÓN_PANTALLA_COMPLETA = 5;
    public static final int DIMENSIÓN_FOTOGRAMA = 6;

    public static final int FONDO_NORMAL = 0;
    public static final int FONDO_TRANSPARENTE_SUAVE = 1;
    public static final int FONDO_TRANSPARENTE = 2;
    public static final int FONDO_INVISIBLE = 3;

    public boolean RedondearEsquinas = true;
    public boolean esReposicionable = true;
    protected boolean SiempreVisible = false;
    public boolean MostrarPopMenúEncabezado = true;
    public boolean ÁreasInteractivas_ReposiciónYRedimensión = true;

    public static String[] EvitarGeneraciónDeOpcionesEnELMenúEspontaneo = {""};

    //Reguladores para las zonas de acción
    public int Anchura_Marco_Redimensión = 10;
    public int Anchura_Encabezado_Reposición = 10;

    //Variables auxiliares
    //Herramientas para la reposición y la redimensión
    private int x, y;//Cola del vector desplazamiento
    private Rectangle2D Límites_AntesFullScreen;
    private boolean AlwaysOnTop_AntesFullScreen;
    protected boolean RedimIzquierda, RedimDerecha, RedimArriba, RedimAbajo, RedimTemp, ReposTemp;
    protected Dupla PosiciónDerechaAbajo = new Dupla(), ÚltimaPosCursor_Presión = new Dupla();
    protected Dupla Proporción_Redimencionar;

    public final Mouse mouse_Info = new Mouse();

    public final Presentador Presentador = new Presentador(this);

    public static void main(String[] args) throws Exception {
        try {
            NimbusModificado.CargarTemaGrisNimbus();
            NimbusModificado.CargarNimbus();
        } catch (Exception e) {
        }
        VentanaGráfica ventana = new VentanaGráfica(JeffAporta_Texto());
//        ventana.ActualizarFotograma(JeffAporta_Texto());
    }

    public VentanaGráfica(String Titulo, boolean visible, boolean Undecorated) {
        super(Titulo);

        if (Sistema.is_windows) {
            setUndecorated(Undecorated);
        }

        if (!Undecorated) {
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent evt) {
                    setSize(getWidth(), getHeight());
                }
            });
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE); //para que la aplicación termine cuando se cierre la ventana

        setMinimumSize(dimensionesPorDefecto[DIMENSIÓN_MUY_PEQUEÑA].convDimension());//Dimension minima
        CambiarTamaño(DIMENSIÓN_MEDIANA_GRANDE);//Tamaño por defecto
        ColorFondo(Color.BLACK);

        try {
            setIconImage(EscalarGranCalidad(JeffAporta(), 64, -1));
        } catch (Exception e) {
        }

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addWindowListener(this);
        addKeyListener(this);

        add(Presentador);//Se añade el JLabel en 0,0 con las dimensiones de la ventana
        setContentPane(Presentador);
        setVisible(visible);//Para que se haga visible cuando se instancie
        CentrarEnPantalla();
    }

    public VentanaGráfica() {
        this("");
    }

    public VentanaGráfica(BufferedImage img) {
        this(true, false);
        ActualizarFotograma(img);
        ActualizarFotograma(img);
    }

    public VentanaGráfica(boolean visible) {
        this("", visible, false);
    }

    public VentanaGráfica(boolean visible, boolean undecorated) {
        this("", visible, undecorated);
    }

    public VentanaGráfica(String string) {
        this(string, true, false);
    }

    public VentanaGráfica AlCerrar_SalirAplicación() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        return this;
    }

    public VentanaGráfica AlCerrar_Deponer() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        return this;
    }

    public VentanaGráfica AlCerrar_NoHacerNada() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        return this;
    }

    public void ActualizarFotograma() {
        Presentador.ActualizarFotograma();
    }

    public void ActualizarFotograma(BufferedImage imagen, String texto) {
        ActualizarFotograma(imagen, texto, true);
    }

    public void ActualizarFotograma(BufferedImage imagen, String texto, boolean out) {
        ActualizarFotograma(imagen);
        if (out) {
            System.out.println(texto);
        } else {
            System.err.println(texto);
        }
    }

    public void ActualizarFotograma(BufferedImage imagen) {
        Presentador.ActualizarFotograma(imagen);
    }

    public VentanaGráfica ColorFondo(Paint c) {
        Presentador.ColorFondo(c);
        if (c instanceof Color) {
            Color c1 = ((Color) c);
            setBackground(c1);
        }
        ActualizarFotograma();
        return this;
    }

    public boolean ConservarPíxelesFotograma() {
        return Presentador.ConservarPíxeles;
    }

    public void ConservarPíxelesFotograma(boolean conservación) {
        Presentador.ConservarPíxeles = conservación;
    }

    public GeneradorDeTexto Consola() {
        return Presentador.Consola;
    }

    public VentanaGráfica Borde(Paint color, float grosor) {
        Presentador.Borde_Presentador(color, grosor);
        return this;
    }

    public VentanaGráfica GrosorBorde(float grosor) {
        Presentador.GrosorBorde_Presentador(grosor);
        return this;
    }

    public void MostrarTexto(String texto) {
        Presentador.MostrarTexto(texto);
    }

    public void Consola(GeneradorDeTexto consola) {
        Presentador.Consola = consola;
    }

    public Dupla PosiciónCursorEnFotograma() {
        return Presentador.PosiciónCursorEnFotograma();
    }

    public void TipoAjusteFotograma(byte Ajuste) {
        Presentador.TIPO_AJUSTE_FOTOGRAMA = Ajuste;
    }

    protected void RefrescarShape() {
        if (isUndecorated()) {
            boolean esPantallaCompleta = Dupla.DIMENSIÓN_PANTALLA.esIgual(getSize());
            if (!RedondearEsquinas || esPantallaCompleta) {
                setShape(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
            } else {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 25, 25));
            }
        }
    }

    private void ActualizarVentanaRedimensionada() {
        RefrescarShape();
    }

    /**
     * <p align="center">
     * Se encarga de mantener las proporciones al escalar la ventana
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQVN2IawLuJCv0uw48VYdoQhbMG1TipleAjHhBpLLyM-e6gnB02UQxpPmhpEUf32YsN3rG4cqfu2smW/pub?w=391&h=301">
     * <br><br>
     * no se aplicará la proporción si es null o (0,0)
     */
    public void ProporciónRedimensionar(int w, int h) {
        ProporciónRedimensionar(new Dupla(w, h));
    }

    /**
     * <p align="center">
     * Se encarga de mantener las proporciones al escalar la ventana
     * <br><br>
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQVN2IawLuJCv0uw48VYdoQhbMG1TipleAjHhBpLLyM-e6gnB02UQxpPmhpEUf32YsN3rG4cqfu2smW/pub?w=391&h=301">
     * <br><br>
     * no se aplicará la proporción si es null o (0,0)
     */
    public void ProporciónRedimensionar(Dupla proporción) {
        Proporción_Redimencionar = proporción;
    }

    public void AjustarProporcionalidadRedimensionamiento_FotogramaActual() {
        try {
            ProporciónRedimensionar(new Dupla(Presentador.ÚltimaImagenCargada));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("No se ha ajustado la proporcionalidad en la ventana, ocurrio un error");
        }
    }

    public void setSize(Object o) {
        setSize(new Dupla(o).convDimension());
    }

    @Override
    public void setSize(Dimension nuevaDimensión) {
        setSize(nuevaDimensión.width, nuevaDimensión.height);
    }
    int alturaEncabezado = 0;

    @Override
    public void setSize(int w, int h) {
        if (Proporción_Redimencionar != null && Proporción_Redimencionar.esDiferente(Dupla.ORIGEN)) {
            Dupla dim = new Dupla(w, h)
                    .ProyectarOrtogonalmenteEn(Proporción_Redimencionar);
            w = dim.Ancho();
            h = dim.Alto();
        }
//        if (alturaEncabezado == 0) {
//            float ancho_Presentador = Presentador.getWidth();
//            float alto_Presentador = Presentador.getHeight();
//            float ancho_Proporción = Proporción_Redimencionar.Ancho();
//            float alto_Proporción = Proporción_Redimencionar.Alto();
//            float razón_wh_proporción = ancho_Proporción / alto_Proporción;
//            float razón_wh_presentador = ancho_Presentador / alto_Presentador;
//            if (razón_wh_presentador != razón_wh_proporción) {
//                float Diferencia = ancho_Presentador / razón_wh_proporción;
//                alturaEncabezado = (int) (alto_Presentador - Diferencia);
//            }
//        }
        super.setSize(w, h);
        ActualizarVentanaRedimensionada();
    }

    @Override
    public void setBounds(Rectangle rctngl) {
        setBounds(rctngl.x, rctngl.y, rctngl.width, rctngl.height);
    }

    public void setBounds(double x, double y, double w, double h) {
        setBounds((int) x, (int) y, (int) w, (int) h);
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {
//        if (Proporción_Redimencionar != null && Proporción_Redimencionar.esDiferente(Dupla.ORIGEN)) {
//            Dupla dim = new Dupla(w, h)
//                    .ProyectarOrtogonalmenteEn(Proporción_Redimencionar);
//            w = dim.Ancho();
//            h = dim.Alto();
//        }
        super.setBounds(x, y, w, h);
        ActualizarVentanaRedimensionada();
    }

    public boolean ObtenerSi_EstáCentradaEnPantalla() {
        return Dupla.Alinear(
                DIMENSIÓN_PANTALLA_COMPLETA, getSize(), Dupla.MEDIO, Dupla.MEDIO
        ).esIgual(getLocation());
    }

    public boolean ObtenerSi_EsSiempreVisible() {
        return SiempreVisible;
    }

    public void Alternar_SiempreVisible() {
        setAlwaysOnTop(SiempreVisible = !SiempreVisible);
    }

    public void CambiarModoFondo(int NuevoEstado) {
        switch (NuevoEstado) {
            case FONDO_NORMAL:
                Presentador.setBackground(
                        new Color(Presentador.getBackground().getRGB())
                );
                break;
            case FONDO_TRANSPARENTE_SUAVE:
                Presentador.setBackground(
                        new Color((Presentador.getBackground().getRGB() | 0xff000000) & 0x35ffffff, true)
                );
                break;
            case FONDO_TRANSPARENTE:
                Presentador.setBackground(
                        new Color((Presentador.getBackground().getRGB() | 0xff000000) & 0x01ffffff, true)
                );
                break;
            case FONDO_INVISIBLE:
                Presentador.setBackground(
                        new Color(Presentador.getBackground().getRGB() & 0xffffff, true)
                );
                break;
        }
        try {
            setBackground(Presentador.getBackground());
        } catch (Exception e) {
        }
        ActualizarFotograma();
    }

    protected Dupla[] dimensionesPorDefecto = {
        Dupla.TSD2_160x90, Dupla.TSD_320x180, Dupla.TM_640x360, Dupla.TM_905x509,
        Dupla.THD_1280x720
    };

    public void CambiarTamaño(int NuevoEstado) {
        CambiarTamaño(NuevoEstado, false);
    }

    public void CambiarTamaño(int NuevoEstado, boolean centrar) {
        Dupla dim;
        switch (NuevoEstado) {
            case DIMENSIÓN_MUY_PEQUEÑA:
            case DIMENSIÓN_PEQUEÑA:
            case DIMENSIÓN_MEDIANA:
            case DIMENSIÓN_MEDIANA_GRANDE:
            case DIMENSIÓN_GRANDE:
                dim = dimensionesPorDefecto[NuevoEstado];
                break;
            case DIMENSIÓN_PANTALLA_COMPLETA:
                Límites_AntesFullScreen = new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
                AlwaysOnTop_AntesFullScreen = SiempreVisible;
                if (!SiempreVisible) {
                    Alternar_SiempreVisible();
                }
                dim = Dupla.DIMENSIÓN_PANTALLA;
                break;
            case DIMENSIÓN_FOTOGRAMA:
                dim = new Dupla(Presentador.ÚltimaImagenCargada);
                if (Dupla.DIMENSIÓN_PANTALLA.esMenorOIgual(dim)) {
                    CambiarTamaño(DIMENSIÓN_PANTALLA_COMPLETA);
                    return;
                }
                break;
            default:
                throw new RuntimeException("No se reconoce el identificador para el cambio de dimensión");
        }
        Dupla posCentrado;
        if (centrar || NuevoEstado == DIMENSIÓN_PANTALLA_COMPLETA) {
            posCentrado = Dupla.Alinear(
                    Dupla.DIMENSIÓN_PANTALLA,
                    dim,
                    Dupla.MEDIO, Dupla.MEDIO
            );
        } else {
            posCentrado = new Dupla(getLocation());
        }
        setBounds(
                posCentrado.X, posCentrado.Y,
                dim.Ancho(), dim.Alto()
        );
    }

    public void CentrarEnPantalla() {
        setLocationRelativeTo(null);
    }

    private boolean ÁreasInteractivas_ReposiciónYRedimensión() {
        return ÁreasInteractivas_ReposiciónYRedimensión && Dupla.DIMENSIÓN_PANTALLA.esDiferente(getSize());
    }

    private boolean CursorSobre_RedimIzquierda() {
        ÚltimaPosCursor_Presión = Presentador.ObtenerPosiciónDelCursor();
        if (!isResizable() || !ÁreasInteractivas_ReposiciónYRedimensión()) {
            return false;
        }
        return ÚltimaPosCursor_Presión.X <= Anchura_Marco_Redimensión && esReposicionable;
    }

    private boolean CursorSobre_RedimDerecha() {
        if (!isResizable() || !ÁreasInteractivas_ReposiciónYRedimensión()) {
            return false;
        }
        return ÚltimaPosCursor_Presión.X >= getWidth() - Anchura_Marco_Redimensión;
    }

    private boolean CursorSobre_RedimArriba() {
        if (!isResizable() || !ÁreasInteractivas_ReposiciónYRedimensión()) {
            return false;
        }
        return ÚltimaPosCursor_Presión.Y <= Anchura_Marco_Redimensión && esReposicionable;
    }

    private boolean CursorSobre_RedimAbajo() {
        if (!isResizable() || !ÁreasInteractivas_ReposiciónYRedimensión()) {
            return false;
        }
        return ÚltimaPosCursor_Presión.Y >= getHeight() - Anchura_Marco_Redimensión;
    }

    private boolean CursorSobreEncabezado() {
        double PosY_Cursor = Presentador.ObtenerPosiciónDelCursor().Y;
        int encabezado = Anchura_Encabezado_Reposición + Anchura_Marco_Redimensión;
        boolean DentroÁreaRepos = PosY_Cursor < encabezado;
        return DentroÁreaRepos;
    }

    private boolean CursorSobreReposicionar() {
        //Descartar los casos que por analisis desactivan inmediatamente la reposición
        if (!ÁreasInteractivas_ReposiciónYRedimensión()) {
            return false;
        }
        if (!esReposicionable & !ReposTemp) {
            return false;
        } else if (CursorSobre_RedimIzquierda() | CursorSobre_RedimDerecha()) {
            return false;
        }
        double PosY_Cursor = ÚltimaPosCursor_Presión.Y;
        if (isResizable() || RedimTemp) {
            boolean DentroÁreaRepos = PosY_Cursor < Anchura_Encabezado_Reposición + Anchura_Marco_Redimensión;
            boolean FueraMarcoRedim = PosY_Cursor > Anchura_Marco_Redimensión;
            return DentroÁreaRepos && FueraMarcoRedim;
        }
        return PosY_Cursor < Anchura_Encabezado_Reposición;
    }

    private void ActualizarCursor_SegúnÁreaInteracción_ReposORedim() {
        if (!(esReposicionable || ReposTemp) & !(isResizable() || RedimTemp)) {
            //Descartar el caso en el que no hayan cursores ni de redimensión ni de reposición
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        } else if (CursorSobreReposicionar()) {
            //Descartar el caso en el que no haya cursor de reposición
            setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            return;
        } else if (isResizable()) {
            //Descartar el caso en el que no hayan cursores de redimensión
            final boolean W = CursorSobre_RedimIzquierda(), E = CursorSobre_RedimDerecha();
            final boolean N = CursorSobre_RedimArriba(), S = CursorSobre_RedimAbajo();
            if (N) {
                setCursor(Cursor.getPredefinedCursor(E ? Cursor.NE_RESIZE_CURSOR
                        : W ? Cursor.NW_RESIZE_CURSOR : Cursor.N_RESIZE_CURSOR));
                return;
            } else if (S) {
                setCursor(Cursor.getPredefinedCursor(E ? Cursor.SE_RESIZE_CURSOR
                        : W ? Cursor.SW_RESIZE_CURSOR : Cursor.S_RESIZE_CURSOR));
                return;
            } else if (E) {
                setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                return;
            } else if (W) {
                setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                return;
            }
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (isUndecorated()) {
            boolean clicIzquierdo = me.getButton() == MouseEvent.BUTTON1;
            boolean clicDerecho = me.getButton() == MouseEvent.BUTTON3;
            //Intercambiador de pantalla completa con doble clic
            if (isResizable() && clicIzquierdo) {
                //Si no es redimensionable, no tiene sentido intentar intercambiar
                final boolean DobleClic = me.getClickCount() == 2;
                if (DobleClic) {
                    //Solo si hubo doble clic, tiene sentido intentar intercambiar a pantalla completa
                    final boolean EstáEnCabecera = CursorSobreEncabezado();
                    if (EstáEnCabecera) {
                        //Solo si el doble cilc fue en la cabecera tiene sentido intentar 
                        //intercambiar a pantalla completa
                        final boolean NoPantCompl = Dupla.DIMENSIÓN_PANTALLA.esDiferente(getSize());
                        if (NoPantCompl) {
                            CambiarTamaño(DIMENSIÓN_PANTALLA_COMPLETA);
                        } else {
                            if (Límites_AntesFullScreen != null) {
                                setBounds(
                                        Límites_AntesFullScreen.getX(), Límites_AntesFullScreen.getY(),
                                        Límites_AntesFullScreen.getWidth(), Límites_AntesFullScreen.getHeight()
                                );
                                if (!AlwaysOnTop_AntesFullScreen) {
                                    Alternar_SiempreVisible();
                                }
                            } else {
                                CambiarTamaño(DIMENSIÓN_GRANDE, true);
                            }
                        }
                    }
                    return;
                }
            }
            if (MostrarPopMenúEncabezado && CursorSobreEncabezado() && clicDerecho) {
                GenerarMenúCabecera(me);
            }
        }
    }

    public void GenerarMenúCabecera(MouseEvent e) {
        MenúCabeceraVentanaGráfica menu = new MenúCabeceraVentanaGráfica(this);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (isUndecorated()) {
            mouse_Info.PresiónClic(me);
            RedimIzquierda = CursorSobre_RedimIzquierda();
            RedimDerecha = CursorSobre_RedimDerecha();
            RedimArriba = CursorSobre_RedimArriba();
            RedimAbajo = CursorSobre_RedimAbajo();
            RedimTemp = isResizable();
            if (!RedimIzquierda && !RedimAbajo && !RedimArriba && !RedimDerecha) {
                setResizable(false);
            }
            PosiciónDerechaAbajo = new Dupla(getLocation()).Adicionar(getSize());
            ReposTemp = esReposicionable;
            if (CursorSobreReposicionar()) {
                x = me.getX();
                y = me.getY();
            } else {
                esReposicionable = false;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if (isUndecorated()) {
            mouse_Info.LiberaciónClic(me);
            esReposicionable = ReposTemp;
            setResizable(RedimTemp);
        }
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (isUndecorated()) {
            Dupla PosCursor = Dupla.PosiciónCursorEnPantalla();
            if (ÁreasInteractivas_ReposiciónYRedimensión()) {
                double ancho = getWidth(), alto = getHeight();
                double PosX = getX(), PosY = getY();
                if (isResizable()) {
                    if (RedimDerecha) {
                        ancho = PosCursor.X - PosX;
                    }
                    if (RedimAbajo) {
                        alto = PosCursor.Y - PosY;
                    }
                    if (RedimIzquierda) {
                        final Dupla NuevaDimensión = new Dupla(
                                PosiciónDerechaAbajo.Clon().Sustraer(PosCursor).rndX(),
                                alto
                        );
                        if (NuevaDimensión.XesMenor(getMinimumSize())) {
                            ancho = getMinimumSize().width;
                            Dupla pos = PosiciónDerechaAbajo.Clon().Sustraer(ancho, alto);
                            PosX = pos.X;
                        } else {
                            ancho = NuevaDimensión.Ancho();
                            alto = NuevaDimensión.Alto();
                            PosX = PosCursor.X;
                        }
                    }
                    if (RedimArriba) {
                        final Dupla NuevaDimensión = new Dupla(
                                ancho,
                                PosiciónDerechaAbajo.Clon().Sustraer(PosCursor).rndY()
                        );
                        if (NuevaDimensión.YesMenor(getMinimumSize())) {
                            alto = getMinimumSize().height;
                            Dupla pos = PosiciónDerechaAbajo.Clon().Sustraer(ancho, alto);
                            PosY = pos.Y;
                        } else {
                            ancho = NuevaDimensión.Ancho();
                            alto = NuevaDimensión.Alto();
                            PosY = PosCursor.intY();
                        }
                    }
                } else if (esReposicionable) {
                    PosX = PosCursor.X - x;
                    PosY = PosCursor.Y - y;
                }
                setBounds(PosX, PosY, ancho, alto);
            }
        }

    }

    @Override
    public void mouseMoved(MouseEvent me) {
        if (isUndecorated()) {
            ActualizarCursor_SegúnÁreaInteracción_ReposORedim();
        }
    }

    @Override
    public void windowOpened(WindowEvent we) {
    }

    @Override
    public void windowClosing(WindowEvent we) {
    }

    @Override
    public void windowClosed(WindowEvent we) {
    }

    @Override
    public void windowIconified(WindowEvent we) {
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
    }

    @Override
    public void windowActivated(WindowEvent we) {
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    public void SalirAplicación() {
        System.exit(0);
    }

    public class Mouse {

        public boolean ClicDerechoPresionado = false;
        public boolean ClicIzquierdoPresionado = false;
        public boolean ClicScrollPresionado = false;

        public MouseEvent event;

        private void PresiónClic(MouseEvent me) {
            event = me;
            PresiónClic();
        }

        private void PresiónClic() {
            if (event == null) {
                return;
            }
            switch (event.getButton()) {
                case MouseEvent.BUTTON1:
                    ClicIzquierdoPresionado = true;
                    break;
                case MouseEvent.BUTTON2:
                    ClicScrollPresionado = true;
                    break;
                case MouseEvent.BUTTON3:
                    ClicDerechoPresionado = true;
                    break;
            }
        }

        private void LiberaciónClic(MouseEvent me) {
            event = me;
            LiberaciónClic();
        }

        private void LiberaciónClic() {
            if (event == null) {
                return;
            }
            switch (event.getButton()) {
                case MouseEvent.BUTTON1:
                    ClicIzquierdoPresionado = false;
                    break;
                case MouseEvent.BUTTON2:
                    ClicScrollPresionado = false;
                    break;
                case MouseEvent.BUTTON3:
                    ClicDerechoPresionado = false;
                    break;
            }
        }
    }
}
