package Escenarios;

import GUI.Botón;
import GUI.BotónMina;
import static HerramientaDeImagen.Filtros_Lineales.*;
import HerramientaDeImagen.GeneraciónDeTexto;
import static HerramientaDeImagen.GeneraciónDeTexto.*;
import static GUI.Dibujable.*;
import HerramientasMatemáticas.Dupla;
import static HerramientasMatemáticas.Matemática.*;
import static Principal.Principal.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

public class Buscaminas extends EscenarioInteractivo {

    public static final byte MARCO = 15;

    public static final byte //Identificadores de la dificultad
            DIFICULTAD_FÁCIL = 1, DIFICULTAD_MEDIA = 2,
            DIFICULTAD_DIFICIL = 4, DIFICULTAD_EXPERTO = 5;

    public static byte DIFICULTAD = 1;

    public static Rango ColumnasFilas_Permitidas;
    public static byte Columnas = 40, Filas = 30;

    public static boolean GameOver, Finalizado, PrimeraPistaDetectada;

    public static int CantidadMinas, Número_MinasMarcadas;

    public static byte[][] tablero;
    public static BotónMina[][] botonesMina;
    public static Reloj reloj;
    public static MinasMarcadas MinasMarcadas;

    private static Buscaminas Auxiliar;
    private static Botón Reiniciar;

    public Buscaminas() {
        Auxiliar = this;
        IniciarNuevoJuego();
    }

    @Override
    public void buscarAcciónBotones_Presión(MouseEvent me) {
        Reiniciar.EscuchadorMouse(me);
        for (BotónMina[] Columna : botonesMina) {
            for (BotónMina botónMina : Columna) {
                botónMina.EscuchadorMouse(me);
            }
        }
    }

    @Override
    public BufferedImage Fotograma() {
        BufferedImage fotograma = DimensiónFotograma().convBufferedImage();
        try {
            for (BotónMina[] Columna : botonesMina) {
                for (BotónMina botónMina : Columna) {
                    botónMina.Dibujar(fotograma.createGraphics());
                }
            }
            reloj.Dibujar(fotograma.createGraphics());
            MinasMarcadas.Dibujar(fotograma.createGraphics());
            Reiniciar.Dibujar(fotograma.createGraphics());
        } catch (Exception e) {
        }
        return fotograma;
    }

    @Override
    public void Deponer() {
        reloj = null;
        MinasMarcadas = null;
        botonesMina = null;
        Reiniciar = null;
        BotónMina.DeponerElementosEstaticos();
    }

    @Override
    public void Cargar() {
        if (BotónMina.ImgCubierto == null) {
            BotónMina.CargarElementosEstaticos();
        }
        if (ColumnasFilas_Permitidas == null) {
            ColumnasFilas_Permitidas = new Rango(10, 40);
        }
        CantidadMinas = (int) (Columnas * Filas * DIFICULTAD * 0.05);
        GameOver = false;
        Finalizado = false;
        Número_MinasMarcadas = 0;
        PrimeraPistaDetectada = false;
        reloj = new Reloj();
        MinasMarcadas = new MinasMarcadas();
    }

    public void IniciarNuevoJuego() {
        Cargar();
        GenerarTablero();
        CargarTablero();

        if (Reiniciar == null) {
            BufferedImage icono = cargarImagen("refrescar.png");
            BufferedImage[] iconos = MapaBotónIcono_Sombra(
                    Escalar(icono, 1.25 * BotónMina.LADO / icono.getHeight(), false)
            );
            Reiniciar = new Botón(iconos) {
                @Override
                protected void Acción_Interactivo(MouseEvent me) {
                    System.out.println("reiniciar");
                    Reiniciar();
                }
            };
            Reiniciar.CambiarPosición(
                    DimensiónFotograma().Sustraer(Reiniciar.Dimensión).Mitad().X,
                    reloj.Posición.Y
            );
        }
    }

    public static boolean BuscarConclusión() {
        for (BotónMina[] Columna : botonesMina) {
            for (BotónMina botónMina : Columna) {
                if (botónMina.INTERACTIVO) {
                    if (botónMina.ValorEnTablero != -1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void ConcluirJuego() {
        Finalizado = BuscarConclusión();
    }

    public void Reiniciar() {
        for (BotónMina[] Columna : botonesMina) {
            for (BotónMina botónMina : Columna) {
                botónMina.HAY_INCONSISTENCIA_SISTEMA_MARCAS = false;
                botónMina.ESTADO_MINA = BotónMina.ESTADO_CUBIERTO;
                botónMina.INTERACTIVO = true;
                botónMina.CambiarIcono(BotónMina.ImgCubierto);
            }
        }
        IniciarNuevoJuego();
    }

    public static void TerminarJuego() {
        GameOver = true;
        MostrarPosiciónMinas_TérminarJuego();
        for (BotónMina[] Columna : botonesMina) {
            for (BotónMina botónMina : Columna) {
                botónMina.INTERACTIVO = false;
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
    }

    public static void MostrarPosiciónMinas_TérminarJuego() {
        for (BotónMina[] Columna : botonesMina) {
            for (BotónMina botónMina : Columna) {
                CalcularFotograma();
                boolean TieneMarca = botónMina.ESTADO_MINA == BotónMina.ESTADO_MARCADO;
                if (botónMina.ValorEnTablero == -1 && botónMina.INTERACTIVO) {
                    if (!TieneMarca) {
                        botónMina.CambiarIcono(BotónMina.MinaDesactivada);
                    }
                } else if (TieneMarca) {
                    botónMina.CambiarIcono(BotónMina.MinaDesactivadaErronea);
                }
            }
        }
    }

    void GenerarTablero() {
        tablero = new byte[Columnas][Filas];
        DepositarMinas();
        GenerarPistasPerifericasALasMinas();
    }

    void DepositarMinas() {
        int contador = 0;
        while (contador < CantidadMinas) {
            int c = AleatorioEnteroEntre(0, Columnas - 1);
            int f = AleatorioEnteroEntre(0, Filas - 1);
            if (tablero[c][f] == 0) {
                tablero[c][f] = -1;
                contador++;
            }
        }
    }

    public static void ReDepositarMina(Dupla Posición) {
        System.out.println("Mina Redepositada");
        int c = Posición.intX(), f = Posición.intY();
        tablero[c][f] = 0;
        while (true) {
            int Nc = AleatorioEnteroEntre(0, Columnas - 1);
            int Nf = AleatorioEnteroEntre(0, Filas - 1);
            if (tablero[Nc][Nf] != -1 && c != Nc && f != Nf) {
                tablero[Nc][Nf] = -1;
                GenerarPistasPerifericasALasMinas();
                Auxiliar.CargarTablero();
                return;
            }
        }
    }

    public static void GenerarPistasPerifericasALasMinas() {
        for (int c = 0; c < Columnas; c++) {
            for (int f = 0; f < Filas; f++) {
                byte contadorMinasPerifericas = 0;
                if (tablero[c][f] != -1) {
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            int c_temp = c + i, f_temp = f + j;
                            try {
                                if (tablero[c_temp][f_temp] == -1) {
                                    contadorMinasPerifericas++;
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                    tablero[c][f] = contadorMinasPerifericas;
                }
            }
        }
    }

    public static Dupla PosiciónTablero() {
        return new Dupla(MARCO, 2 * MARCO + reloj.Dimensión.intY());
    }

    @Override
    public Dupla DimensiónFotograma() {
        return DimensiónTablero().Adicionar(MarcoExtraTablero());
    }

    public static Dupla DimensiónTablero() {
        return new Dupla(Columnas, Filas).Escalar(BotónMina.LADO);
    }

    public static Dupla MarcoExtraTablero() {
        return new Dupla(MARCO * 2, reloj.Dimensión.intY() + MARCO * 3);
    }

    private void CargarTablero() {
        if (botonesMina == null) {
            botonesMina = new BotónMina[Columnas][Filas];
        }
        Dupla PosiciónTablero = PosiciónTablero();
        for (int c = 0; c < Columnas; c++) {
            for (int f = 0; f < Filas; f++) {

                BotónMina Casilla = botonesMina[c][f];

                if (Casilla == null) {
                    Casilla = botonesMina[c][f] = new BotónMina().AsignaPistaNúmerica(tablero[c][f]);
                    Casilla.AsignarPosiciónTablero(c, f)
                            .CambiarPosición(
                                    c * BotónMina.LADO + PosiciónTablero.intX(),
                                    f * BotónMina.LADO + PosiciónTablero.intY()
                            );

                } else {

                    Casilla.AsignaPistaNúmerica(tablero[c][f]);
                    if (!Casilla.INTERACTIVO) {
                        Casilla.CambiarIcono(BotónMina.Números[Casilla.ValorEnTablero]);
                    }

                }
                System.out.print(tablero[c][f] + " ");
            }
            System.out.println("");
        }
    }

    public class MinasMarcadas extends TabletaTexto {

        public MinasMarcadas() {
            super(Color.BLACK, Color.DARK_GRAY, Color.RED, CantidadMinas + "");
            Posición = new Dupla(MARCO);
        }

        public void Aumentar() {
            CambiarTexto(CantidadMinas - ++Número_MinasMarcadas + "");
        }

        public void Decrementar() {
            CambiarTexto(CantidadMinas - (--Número_MinasMarcadas) + "");
        }
    }

    public class Reloj extends TabletaTexto implements Runnable {

        Thread hilo = new Thread(this);
        int SegundosJuego = 0;

        public Reloj() {
            super(Color.BLACK, Color.DARK_GRAY, Color.RED, 0 + "");
            Posición = new Dupla((Columnas - 3) * BotónMina.LADO + MARCO, MARCO);
            hilo.start();
        }

        @Override
        public void run() {
            while (!GameOver && this == reloj) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
                CambiarTexto(++SegundosJuego + "");
                if (ventana.isActive()) {
                    CalcularFotograma();
                }
            }
            hilo = null;
        }

    }

}
