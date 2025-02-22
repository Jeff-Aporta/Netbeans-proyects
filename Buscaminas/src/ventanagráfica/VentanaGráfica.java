package ventanagráfica;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import HerramientasMatemáticas.Dupla;
import static HerramientaDeImagen.Filtros_Lineales.*;
import HerramientaDeImagen.GeneraciónDeTexto;
import static HerramientasMatemáticas.Matemática.*;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class VentanaGráfica extends JFrame implements MouseListener, MouseMotionListener, MouseWheelListener, WindowListener {

    public static final int //
            DIMENSIÓN_PEQUEÑA = 0,
            DIMENSIÓN_MEDIANA = 1,
            DIMENSIÓN_GRANDE = 2,
            DIMENSIÓN_PANTALLA_COMPLETA = 3;

    public final Mouse mouse = new Mouse();

    public final JLabel Presentador = new JLabel();
    public boolean Reposicionamiento = true;

    //Herramientas del dibujado de la imagen
    public int TIPO_AJUSTE = AJUSTE_CENTRADO_AJUSTAR;
    public boolean ConservarPíxeles = true;
    public PropiedadAjuste PropiedadAjusteFotograma;

    //Herramientas para el fondo
    public int TIPO_AJUSTE_FONDO = AJUSTE_RELLENAR;
    public BufferedImage ImagenFondo = null;
    public Color ColorFondo = null;

    //Variables auxiliares
    //Herramientas para la reposición y la redimensión
    private int x, y;//Cola del vector desplazamiento
    private static final int Marco_Redim = 10, Alt_Enc_Repos = 10;//Reguladores para las zonas de acción
    private boolean RedimIzquierda, RedimDerecha, RedimArriba, RedimAbajo, RedimTemp, ReposTemp;
    private Dupla PosiciónDerechaAbajo = new Dupla(), ÚltAct_PosCursor_Ventana = new Dupla();
    //Herramientas para la escalación de la aplicación
    private BufferedImage ÚltimaImagenCargada = null;
    private BufferedImage Fotograma;

    GeneraciónDeTexto Consola = new GeneraciónDeTexto();

    public static void main(String[] args) {
        VentanaGráfica ventana = new VentanaGráfica("Prueba") {
        };
        ventana.Consola.AlineaciónHorizontal = Dupla.MEDIO;
        ventana.Consola.ColorTexto = Color.WHITE;
        ventana.ImagenFondo = CargarImagen(
                "http://orig03.deviantart.net/f75e/f/2017/153/c/f/newton_copia_by_jeffreyagudelo-dbbcuo9.jpg"
        );
        ventana.Consola
                .Borde(Color.BLACK, 3.5f)
                .ModificarFuente(new Font("arial", Font.BOLD, 120));
        BufferedImage icono = CargarImagen("http://img07.deviantart.net/cc6c/i/2016/283/8/a/firma_jeff_aporta___copia_by_jeffreyagudelo-dakl4y3.png");
        ventana.MostrarTexto("%img% Jeff Aporta", icono);
    }

    public VentanaGráfica(String string) {
        super(string);
        setUndecorated(true);//Quita el marco

        setDefaultCloseOperation(EXIT_ON_CLOSE); //para que la aplicación termine cuando se cierre la ventana
        setMinimumSize(Dupla.TSD_320x180.convDimension());//Dimension minima
        CambiarTamaño(DIMENSIÓN_GRANDE);//Tamaño por defecto

        String icono = "http://img07.deviantart.net/cc6c/i/2016/283/8/a/firma_jeff_aporta___copia_by_jeffreyagudelo-dakl4y3.png";
        setIconImage(CargarImagen(icono));

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addWindowListener(this);

        setLocationRelativeTo(null);//Para que la ventana se centre en la pantalla
        add(Presentador);//Se añade el JLabel en 0,0 con las dimensiones de la ventana
        setVisible(true);//Para que se haga visible cuando se instancie
    }

    public void MostrarTexto(String texto) {
        ActualizarFotograma(Consola.GenerarTexto(texto));
    }

    public void MostrarTexto(String texto, BufferedImage... Iconos) {
        ActualizarFotograma(Consola.GenerarTexto(texto, Iconos));
    }

    public void ActualizarFotograma(BufferedImage imagen) {
        if (imagen == null | Fotograma == null) {
            imagen = ÚltimaImagenCargada;
            Fotograma = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        } else {
            ÚltimaImagenCargada = imagen;
        }
        DibujarFondo(Fotograma.getGraphics());
        PropiedadAjusteFotograma = AjustarImagenAlÁreaDeDibujado(imagen, Fotograma, TIPO_AJUSTE);
        Presentador.setIcon(new ImageIcon(Fotograma));
    }

    public void DibujarFondo(Graphics g) {
        if (ColorFondo != null) {
            if (ColorFondo.getAlpha() > 0) {
                g.setColor(ColorFondo);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
        if (ImagenFondo != null) {
            AjustarImagenAlÁreaDeDibujado(ImagenFondo, Fotograma, TIPO_AJUSTE_FONDO);
        }
    }

    private PropiedadAjuste AjustarImagenAlÁreaDeDibujado(BufferedImage ImagenDibujar, BufferedImage imagenÁrea, int TIPO_AJUSTE) {
        return Ajuste_Personalizado(
                ImagenDibujar, imagenÁrea, TIPO_AJUSTE, ConservarPíxeles
        );
    }

    private void RefrescarShape() {
        if (Dupla.DIMENSIÓN_PANTALLA.esIgual(getSize())) {
            //Shape de esquinas rectas
            setShape(new Rectangle2D.Double(0, 0, getWidth(), getHeight()));
        } else {
            //Shape de esquinas redondeadas
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
        }
    }

    private void ActualizarVentanaRedimensionada() {
        RefrescarShape();
        ActualizarFotograma(null);
    }

    public void setSizeAndUpdateFrame(BufferedImage imagen) {
        setSize(imagen);
        ActualizarFotograma(imagen);
    }

    public void setSize(Object o) {
        setSize(new Dupla(o).convDimension());
    }

    @Override
    public void setSize(Dimension nuevaDimensión) {
        super.setSize(nuevaDimensión);
        ActualizarVentanaRedimensionada();
    }

    @Override
    public void setSize(int ancho, int alto) {
        super.setSize(ancho, alto);
        ActualizarVentanaRedimensionada();
    }

    public void CambiarTamaño(final int NuevoEstado) {
        if (!isResizable()) {
            return;
        }
        switch (NuevoEstado) {
            case DIMENSIÓN_PEQUEÑA:
                setSize(Dupla.TSD_320x180.convDimension());
                break;
            case DIMENSIÓN_MEDIANA:
                setSize(Dupla.TM_640x360.convDimension());
                break;
            case DIMENSIÓN_GRANDE:
                setSize(Dupla.THD_1280x720.convDimension());
                break;
            case DIMENSIÓN_PANTALLA_COMPLETA:
                setSize(Dupla.DIMENSIÓN_PANTALLA.convDimension());
                break;
        }
        setLocationRelativeTo(null);
    }

    public Dupla ObtenerPosiciónDelCursor() {
        return Dupla.PosiciónCursorEnComponente(this);
    }

    public Dupla PosiciónCursorEnFotograma() {
        if (PropiedadAjusteFotograma == null) {
            return Dupla.ORIGEN;
        }
        return ObtenerPosiciónDelCursor()
                .Sustraer(PropiedadAjusteFotograma.Posición)
                .Dividir(PropiedadAjusteFotograma.Escalación);
    }

    private boolean CursorSobre_RedimIzquierda() {
        ÚltAct_PosCursor_Ventana = ObtenerPosiciónDelCursor();
        if (!isResizable()) {
            return false;
        }
        return ÚltAct_PosCursor_Ventana.X <= Marco_Redim && Reposicionamiento;
    }

    private boolean CursorSobre_RedimDerecha() {
        if (!isResizable()) {
            return false;
        }
        return ÚltAct_PosCursor_Ventana.X >= getWidth() - Marco_Redim;
    }

    private boolean CursorSobre_RedimArriba() {
        if (!isResizable()) {
            return false;
        }
        return ÚltAct_PosCursor_Ventana.Y <= Marco_Redim && Reposicionamiento;
    }

    private boolean CursorSobre_RedimAbajo() {
        if (!isResizable()) {
            return false;
        }
        return ÚltAct_PosCursor_Ventana.Y >= getHeight() - Marco_Redim;
    }

    private boolean CursorSobreReposicionar() {
        //Descartar los casos que por analisis desactivan inmediatamente la reposición
        if (!Reposicionamiento & !ReposTemp) {
            return false;
        } else if (CursorSobre_RedimIzquierda() | CursorSobre_RedimDerecha()) {
            return false;
        }
        double PosY_Cursor = ÚltAct_PosCursor_Ventana.Y;
        if (isResizable() || RedimTemp) {
            boolean DentroÁreaRepos = PosY_Cursor < Alt_Enc_Repos + Marco_Redim;
            boolean FueraMarcoRedim = PosY_Cursor > Marco_Redim;
            return DentroÁreaRepos && FueraMarcoRedim;
        }
        return PosY_Cursor < Alt_Enc_Repos;
    }

    private void ActualizarCursor_SegúnÁrea() {
        if (!(Reposicionamiento || ReposTemp) & !(isResizable() || RedimTemp)) {
            //Descartar el caso en el que no hayan cursores ni de redimensión ni de reposición
            setCursor(Cursor.DEFAULT_CURSOR);
            return;
        } else if (CursorSobreReposicionar()) {
            //Descartar el caso en el que no haya cursor de reposición
            setCursor(Cursor.MOVE_CURSOR);
            return;
        } else if (isResizable()) {
            //Descartar el caso en el que no hayan cursores de redimensión
            final boolean W = CursorSobre_RedimIzquierda(), E = CursorSobre_RedimDerecha();
            final boolean N = CursorSobre_RedimArriba(), S = CursorSobre_RedimAbajo();
            if (N) {
                setCursor(E ? Cursor.NE_RESIZE_CURSOR
                        : W ? Cursor.NW_RESIZE_CURSOR : Cursor.N_RESIZE_CURSOR);
                return;
            } else if (S) {
                setCursor(E ? Cursor.SE_RESIZE_CURSOR
                        : W ? Cursor.SW_RESIZE_CURSOR : Cursor.S_RESIZE_CURSOR);
                return;
            } else if (E) {
                setCursor(Cursor.E_RESIZE_CURSOR);
                return;
            } else if (W) {
                setCursor(Cursor.W_RESIZE_CURSOR);
                return;
            }
        }
        setCursor(Cursor.DEFAULT_CURSOR);
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        //Intercambiador de pantalla completa con doble clic
        if (isResizable()) {
            //Si no es redimensionable, no tiene sentido intentar intercambiar
            final boolean DobleClic = me.getClickCount() == 2;
            if (DobleClic) {
                //Solo si hubo doble clic, tiene sentido intentar intercambiar a pantalla completa
                final boolean EstáEnCabecera = CursorSobreReposicionar() || CursorSobre_RedimArriba();
                if (EstáEnCabecera) {
                    //Solo si el doble cilc fue en la cabecera tiene sentido intentar 
                    //intercambiar a pantalla completa
                    final boolean NoPantCompl = Dupla.DIMENSIÓN_PANTALLA.esDiferente(getSize());
                    if (NoPantCompl) {
                        CambiarTamaño(DIMENSIÓN_PANTALLA_COMPLETA);
                    } else {
                        CambiarTamaño(DIMENSIÓN_GRANDE);
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        mouse.PresiónClic(me);
        RedimIzquierda = CursorSobre_RedimIzquierda();
        RedimDerecha = CursorSobre_RedimDerecha();
        RedimArriba = CursorSobre_RedimArriba();
        RedimAbajo = CursorSobre_RedimAbajo();
        RedimTemp = isResizable();
        if (!RedimIzquierda && !RedimAbajo && !RedimArriba && !RedimDerecha) {
            setResizable(false);
        }
        PosiciónDerechaAbajo = new Dupla(getLocation()).Adicionar(getSize());
        ReposTemp = Reposicionamiento;
        if (CursorSobreReposicionar()) {
            x = me.getX();
            y = me.getY();
        } else {
            Reposicionamiento = false;
        }
        PostMousePressed(me);
        MouseInteracción(me);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        mouse.LiberaciónClic(me);
        Reposicionamiento = ReposTemp;
        setResizable(RedimTemp);
        PostMouseReleased(me);
        MouseInteracción(me);
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        Dupla PosCursor = Dupla.PosiciónCursorEnPantalla();
        if (isResizable()) {
            if (RedimDerecha) {
                setSize(PosCursor.intX() - getX(), getHeight());
            }
            if (RedimAbajo) {
                setSize(getWidth(), PosCursor.intY() - getY());
            }
            if (RedimIzquierda) {
                final Dupla NuevaDimensión = new Dupla(
                        PosiciónDerechaAbajo.Clon().Sustraer(PosCursor).rndX(),
                        getHeight()
                );
                if (NuevaDimensión.XesMenor(getMinimumSize())) {
                    setSize(getMinimumSize().width, getHeight());
                    final Dupla pos = PosiciónDerechaAbajo.Clon().Sustraer(getSize());
                    setLocation(pos.rndX(), getY());
                } else {
                    setSize(NuevaDimensión.convDimension());
                    setLocation(PosCursor.intX(), getY());
                }
            }
            if (RedimArriba) {
                final Dupla NuevaDimensión = new Dupla(
                        getWidth(),
                        PosiciónDerechaAbajo.Clon().Sustraer(PosCursor).rndY()
                );
                if (NuevaDimensión.YesMenor(getMinimumSize())) {
                    setSize(getWidth(), getMinimumSize().height);
                    final Dupla pos = PosiciónDerechaAbajo.Clon().Sustraer(getSize());
                    setLocation(getX(), pos.rndY());
                } else {
                    setSize(NuevaDimensión.convDimension());
                    setLocation(getX(), PosCursor.intY());
                }
            }
        } else if (Reposicionamiento) {
            setLocation(PosCursor.Sustraer(x, y).convPoint());
        }
        PostMouseDragged(me);
        MouseInteracción(me);
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        ActualizarCursor_SegúnÁrea();
        PostMouseMoved(me);
        MouseInteracción(me);
    }

    public void PostMouseDragged(MouseEvent me) {
    }

    public void PostMouseMoved(MouseEvent me) {
    }

    public void PostMousePressed(MouseEvent me) {
    }

    public void PostMouseReleased(MouseEvent me) {
    }

    public void MouseInteracción(MouseEvent me) {
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
        new Transparencia().ActivarVentana();
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
        new Transparencia().DesactivarVentana();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
    }

    private class Transparencia implements Runnable {

        static final double segundosTransición = .5;
        static final int fotogramas = 30;
        static final long Pausa = (long) ((1000 / fotogramas) * segundosTransición);

        Thread hilo = new Thread(this);
        Rango transparencia;

        public Transparencia DesactivarVentana() {
            transparencia = new Rango(getOpacity(), .5);
            hilo.start();
            return this;
        }

        public Transparencia ActivarVentana() {
            transparencia = new Rango(getOpacity(), 1);
            hilo.start();
            return this;
        }

        @Override
        public void run() {
            try {
                float Δt = (float) transparencia.Dividir(fotogramas);
                if (transparencia.Vf() == 1) {
                    for (float t = transparencia.floatVi(); t < transparencia.Vf(); t += Δt) {
                        setOpacity(t);
                        Thread.sleep(Pausa);
                    }
                    setOpacity(1);
                } else {
                    for (float t = transparencia.floatVi(); t > transparencia.Vf(); t += Δt) {
                        setOpacity(t);
                        Thread.sleep(Pausa);
                    }
                    setOpacity(.5f);
                }
            } catch (Exception e) {
            }
        }

    }

    public static class Mouse {

        public boolean ClicDerechoPresionado = false;
        public boolean ClicIzquierdoPresionado = false;
        public boolean ClicScrollPresionado = false;

        public void PresiónClic(MouseEvent me) {
            switch (me.getButton()) {
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

        public void LiberaciónClic(MouseEvent me) {
            switch (me.getButton()) {
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

    public static BufferedImage CargarImagen(final String dirección) {
        BufferedImage imagen = null;
        try {
            imagen = ImageIO.read(ClassLoader.class.getResource(dirección));
        } catch (Exception e) {
            try {
                ImageIO.read(ClassLoader.class.getResource("/" + dirección));
            } catch (Exception e2) {
                try {
                    imagen = ImageIO.read(new File(dirección));
                } catch (Exception e3) {
                    try {
                        imagen = ImageIO.read(new URL(dirección));
                    } catch (Exception e4) {
                        final String[] PosiblesCarpetas = {
                            "Imagenes", "Imágenes", "Images", "Assets", "Recursos"
                        };
                        for (final String PosibleCarpeta : PosiblesCarpetas) {
                            for (int i = 0; i < 3; i++) {
                                try {
                                    String cm = PosibleCarpeta;
                                    switch (i) {
                                        case 1:
                                            cm = cm.toLowerCase();
                                            break;
                                        case 2:
                                            cm = cm.toUpperCase();
                                            break;
                                    }
                                    imagen = ImageIO.read(ClassLoader.class.getResource("/" + cm + "/" + dirección));
                                    if (imagen != null) {
                                        return imagen;
                                    }
                                } catch (Exception e5) {
                                }
                            }
                            if (imagen != null) {
                                return imagen;
                            }
                        }
                    }
                }
            }
        }
        if (imagen == null) {
            System.err.println("La imagen no se encontró " + dirección);
        }
        return imagen;
    }
}
