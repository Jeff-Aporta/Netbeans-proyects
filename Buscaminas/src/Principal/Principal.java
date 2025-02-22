package Principal;

import Escenarios.Buscaminas;
import Escenarios.Menú;
import Escenarios.AjusteJuego;
import Escenarios.Presentación;
import Escenarios.EscenarioInteractivo;
import java.awt.Color;
import java.awt.event.MouseEvent;
import ventanagráfica.VentanaGráfica;

import static HerramientaDeImagen.Filtros_Lineales.*;
import java.awt.event.MouseWheelEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import static ventanagráfica.VentanaGráfica.*;

public class Principal {

    public final static int PRESENTACIÓN = 0, MENÚ = 1, BUSCAMINAS = 2, REINICIAR = 3, AJUSTE_JUEGO = 4;
    static int ESCENARIO;

    public static Menú menú;
    public static Buscaminas buscaminas;
    public static AjusteJuego ajusteJuego;

    public final static VentanaGráfica ventana = new VentanaGráfica(
            "Buscaminas - [Jeff Aporta] Jeffrey Alexander Agudelo Espitia"
    ) {
        @Override
        public void MouseInteracción(MouseEvent me) {
            if (isActive()) {
                CalcularFotograma();
            }
        }

        @Override
        public void PostMousePressed(MouseEvent me) {
        }

        @Override
        public void PostMouseReleased(MouseEvent me) {
            EscenarioInteractivo escenarioInteractivo = null;
            switch (ESCENARIO) {
                case PRESENTACIÓN:
                    Presentación.Detener();
                    CambiarEscenario(MENÚ);
                    break;
                case MENÚ:
                    escenarioInteractivo = menú;
                    break;
                case BUSCAMINAS:
                    escenarioInteractivo = buscaminas;
                    break;
                case AJUSTE_JUEGO:
                    escenarioInteractivo = ajusteJuego;
                    break;
            }
            if (escenarioInteractivo != null) {
                escenarioInteractivo.buscarAcciónBotones_Presión(me);
            }
        }

        @Override
        public void PostMouseMoved(MouseEvent me) {
        }

        @Override
        public void PostMouseDragged(MouseEvent me) {
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent me) {
            switch (ESCENARIO) {
                case AJUSTE_JUEGO:
                    ajusteJuego.buscarAcciónBotones_DeslizarScroll(me);
                    break;
            }
        }
    };

    public static void main(String[] args) {
        ventana.ActualizarFotograma(null);
        ventana.TIPO_AJUSTE_FONDO = AJUSTE_MOSAICO;
        ventana.CambiarTamaño(DIMENSIÓN_PANTALLA_COMPLETA);
        CambiarEscenario(PRESENTACIÓN);
//        CambiarEscenario(MENÚ);
//        CambiarEscenario(AJUSTE_JUEGO);
//        CambiarEscenario(BUSCAMINAS);
        ventana.setVisible(true);
        f fil = new f();
    }

    public static void CambiarEscenario(int NUEVO_ESCENARIO) {

        ESCENARIO = NUEVO_ESCENARIO;

        if (ESCENARIO != MENÚ && menú != null) {
            menú.Deponer();
            menú = null;
        }
        if (ESCENARIO != BUSCAMINAS && buscaminas != null) {
            buscaminas.Deponer();
            buscaminas = null;
        }
        if (ESCENARIO != AJUSTE_JUEGO && ajusteJuego != null) {
            ajusteJuego.Deponer();
            ajusteJuego = null;
        }

        switch (ESCENARIO) {
            case MENÚ:
            case AJUSTE_JUEGO:
            case BUSCAMINAS:
            case REINICIAR:
                ventana.ImagenFondo = EscenarioInteractivo.Fondo();
                break;
        }

        switch (ESCENARIO) {
            case PRESENTACIÓN:
                new Presentación().presentarFirmas();
                break;
            case MENÚ:
                menú = new Menú();
                break;
            case BUSCAMINAS:
                buscaminas = new Buscaminas();
                break;
            case AJUSTE_JUEGO:
                ajusteJuego = new AjusteJuego();
                break;
        }
        for (int i = 0; i < 2; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
            CalcularFotograma();
        }
    }

    static long ÚltimaActualización;
    static final long NS = 1000000000;
    static final double FPS_Máximo = 8;
    static final long TiempoMinimoActualización = (long) (NS / FPS_Máximo);

    public static void CalcularFotograma() {
        {
            long TiempoTranscurrido_ÚltimaActualización = System.nanoTime() - ÚltimaActualización;
            if (TiempoTranscurrido_ÚltimaActualización < TiempoMinimoActualización) {
                return;
            }
            ÚltimaActualización = System.nanoTime();
        }
        EscenarioInteractivo Aux = null;
        switch (ESCENARIO) {
            case MENÚ:
                Aux = menú;
                break;
            case BUSCAMINAS:
                Aux = buscaminas;
                break;
            case AJUSTE_JUEGO:
                Aux = ajusteJuego;
                break;
        }
        if (Aux == null) {
            return;
        }
        ventana.ActualizarFotograma(Aux.Fotograma());
    }

    private static class f implements Runnable {

        Thread hilo = new Thread(this);

        public f() {
            hilo.start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                }
                CalcularFotograma();
            }
        }

    }

}
