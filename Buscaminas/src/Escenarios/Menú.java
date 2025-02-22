package Escenarios;

import GUI.Botón;
import GUI.GrupoDibujable;
import HerramientasMatemáticas.Dupla;

import static Principal.Principal.*;
import static HerramientaDeImagen.LectoEscritor_Imagenes.*;
import static HerramientaDeImagen.Filtros_Lineales.*;
import static GUI.Dibujable.*;
import GUI.Imagen;
import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class Menú extends EscenarioInteractivo {

    public Menú() {
        Cargar();
    }

    @Override
    public void Deponer() {
        ElementosDibujables = null;
    }

    @Override
    public void Cargar() {
        Dupla dimensiónBotón = new Dupla(550,80);
        Botón NuevoJuego = new Botón(
                MapaBotónIcono_Sombra(
                        Icono("Nuevo Juego", 50, dimensiónBotón, new Color(80, 141, 40), true, 100)
                )
        ) {
            @Override
            protected void Acción_Interactivo(MouseEvent me) {
                CambiarEscenario(BUSCAMINAS);
            }
        };

        Botón MejoresPuntajes = new Botón(
                MapaBotónIcono_Sombra(
                        Icono("Mejores Puntajes", 50, dimensiónBotón, new Color(80, 141, 40), true, 100)
                )
        ) {
            @Override
            protected void Acción_Interactivo(MouseEvent me) {
                JOptionPane.showMessageDialog(ventana, "Mejores Puntajes");
            }
        };

        Botón Configuración = new Botón(
                MapaBotónIcono_Sombra(
                        Icono("Configuración", 50, dimensiónBotón, new Color(80, 141, 40), true, 100)
                )
        ) {
            @Override
            protected void Acción_Interactivo(MouseEvent me) {
                JOptionPane.showMessageDialog(ventana, "Configuración");
            }
        };

        Botón Facebook = new Botón(MapaImagenesInteractivas_RedesSociales(
                "Jeffrey Alexander\nAgudelo Espitia", cargarImagen("facebook.png")
        )) {
            @Override
            protected void Acción_Interactivo(MouseEvent me) {
                AbrirPáginaWeb("https://www.facebook.com/Jeff.Aporta");
            }
        };

        Botón YT_JeffAporta = new Botón(MapaImagenesInteractivas_RedesSociales("Jeff\nAporta", cargarImagen("youtube.png")
        )) {
            @Override
            protected void Acción_Interactivo(MouseEvent me) {
                AbrirPáginaWeb("http://www.youtube.com/c/JeffAporta?sub_confirmation=1");
            }
        };

        Botón YT_JeffDibuja = new Botón(MapaImagenesInteractivas_RedesSociales("Jeff\nDibuja", cargarImagen("youtube.png")
        )) {
            @Override
            protected void Acción_Interactivo(MouseEvent me) {
                AbrirPáginaWeb(
                        "https://www.youtube.com/channel/UC69yHagTkWMJvrTTtIVSrnQ?sub_confirmation=1"
                );
            }
        };
        GrupoDibujable botonesRedes;
        GrupoDibujable botonesMenú;

        Imagen Logo = new Imagen(Escalar(cargarImagen("Logo.png"), .6, true));

        botonesMenú = new GrupoDibujable(
                Logo,
                NuevoJuego,
                MejoresPuntajes,
                Configuración
        );

        botonesRedes = new GrupoDibujable(
                Facebook,
                YT_JeffAporta,
                YT_JeffDibuja
        ).OrientaciónVertical(false);

        botonesMenú.ReposicionarObjetosDibujables(
                Dupla.Alinear(
                        Dupla.THD_1280x720, botonesMenú.Dimensión(), Dupla.MEDIO, Dupla.MEDIO
                )
        );
        botonesRedes.ReposicionarObjetosDibujables(
                Dupla.Alinear(
                        Dupla.THD_1280x720, botonesRedes.Dimensión(), -1, Dupla.ABAJO
                )
        );
        ElementosDibujables = new GrupoDibujable(botonesMenú, botonesRedes);
    }

    @Override
    public Dupla DimensiónFotograma() {
        return Dupla.THD_1280x720;
    }

}
