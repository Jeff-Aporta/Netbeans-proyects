/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import static HerramientaDeImagen.LectoEscritor_Imagenes.*;
import static HerramientaDeImagen.Filtros_Lineales.*;
import static Escenarios.Buscaminas.*;
import static Principal.Principal.*;
import static HerramientasMatemáticas.Matemática.*;
import static GUI.Dibujable.*;

import HerramientaDeImagen.GeneraciónDeTexto;
import HerramientasMatemáticas.Dupla;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class BotónMina extends Botón {

    //Imágenes estaticas, sólo se necesita una instancia
    public static BufferedImage ImgCubierto,
            ImgCubierto_Sobre,
            ImgCubierto_Pres,
            ImgVacio,
            ImgMarcado, ImgMarcado_Sobre,
            ImgCuestión,
            Números[],
            MinaActivada,
            MinaDesactivada,
            MinaDesactivadaErronea,
            Inconsistencia;

    public static byte LADO;

    public static final byte //Identificadores de estado
            ESTADO_CUBIERTO = 0,
            ESTADO_MARCADO = 1,
            ESTADO_CUESTIÓN = 2;

    public byte ESTADO_MINA = ESTADO_CUBIERTO;

    public byte ValorEnTablero = 0;
    public Dupla PosiciónEnTablero;

    public boolean HAY_INCONSISTENCIA_SISTEMA_MARCAS = false;

    @Override
    public void DibujadoExtra(Graphics2D g) {
        if (HAY_INCONSISTENCIA_SISTEMA_MARCAS) {
            Dupla pos = new Dupla(Posición).Sustraer(new Dupla(Inconsistencia).Cuarto());
            g.drawImage(Inconsistencia, pos.intX(), pos.intY(), null);
        }
    }

    @Override
    public BotónMina CambiarPosición(double x, double y) {
        super.CambiarPosición(x, y);
        return this;
    }

    @Override
    protected void Acción_Interactivo(MouseEvent me) {
        if (me.getButton() == MouseEvent.BUTTON3) {
            switch (ESTADO_MINA) {
                case ESTADO_CUBIERTO:
                    ESTADO_MINA = ESTADO_MARCADO;
                    MinasMarcadas.Aumentar();
                    CambiarIcono(ImgMarcado);
                    break;
                case ESTADO_MARCADO:
                    ESTADO_MINA = ESTADO_CUESTIÓN;
                    MinasMarcadas.Decrementar();
                    CambiarIcono(ImgCuestión);
                    break;
                case ESTADO_CUESTIÓN:
                    ESTADO_MINA = ESTADO_CUBIERTO;
                    CambiarIcono(ImgCubierto);
                    break;
            }
            BuscarSistemaDeMarcasInconsistentes();
        }
        if (me.getButton() == MouseEvent.BUTTON1) {
            ProcesoParalelo.Descubrir(this);
        }
        CalcularFotograma();
    }

    @Override
    protected void Acción_No_Interactivo(MouseEvent me) {
        if (me.getClickCount() == 2) {
            ProcesoParalelo.DescubrirPeriféricos(this);
        }
    }

    void BuscarSistemaDeMarcasInconsistentes() {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int c = PosiciónEnTablero.Columna() + i;
                int f = PosiciónEnTablero.Fila() + j;
                try {
                    botonesMina[c][f].BuscarSistemaInconsistente();
                } catch (Exception e) {
                }
            }
        }
    }

    void BuscarSistemaInconsistente() {
        if (INTERACTIVO || ValorEnTablero == 0) {
            HAY_INCONSISTENCIA_SISTEMA_MARCAS = false;
            return;
        }
        int MarcasPerifericas = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int c = PosiciónEnTablero.Columna() + i;
                int f = PosiciónEnTablero.Fila() + j;
                try {
                    if (botonesMina[c][f].ESTADO_MINA == ESTADO_MARCADO) {
                        MarcasPerifericas++;
                    }
                } catch (Exception e) {
                }
            }
        }
        HAY_INCONSISTENCIA_SISTEMA_MARCAS = MarcasPerifericas > ValorEnTablero;
    }

    public BotónMina() {
        super(ImgCubierto_Sobre, ImgCubierto, ImgCubierto_Pres);
    }

    public BotónMina AsignaPistaNúmerica(byte n) {
        ValorEnTablero = n;
        return this;
    }

    public BotónMina AsignarPosiciónTablero(int c, int f) {
        PosiciónEnTablero = new Dupla(c, f);
        return this;
    }

    static BufferedImage EscalarLado(BufferedImage img) {
        return Escalar(img, LADO, LADO, false);
    }

    public void CambiarIcono(BufferedImage Icono) {
        boolean Inactivar;
        Inactivar = Icono == MinaActivada || Icono == MinaDesactivada || Icono == MinaDesactivadaErronea;
        for (BufferedImage Número : Números) {
            if (Icono == Número || Inactivar) {
                INTERACTIVO = false;
                CambiarImagenes(null, Icono, null);
                return;
            }
        }
        if (Icono == ImgCubierto) {
            CambiarImagenes(ImgCubierto_Sobre, ImgCubierto, ImgCubierto_Pres);
            return;
        }
        if (Icono == ImgMarcado) {
            CambiarImagenes(ImgMarcado_Sobre, ImgMarcado, ImgMarcado);
            return;
        }
        CambiarImagenes(Sombreado(Icono, Color.BLUE), Icono, Sombreado(Icono, Color.BLACK));
        CalcularFotograma();
    }

    boolean BuscarDuda() {
        int c = PosiciónEnTablero.Columna();
        int f = PosiciónEnTablero.Fila();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                try {
                    if (botonesMina[c + i][f + j].INTERACTIVO) {
                        if (botonesMina[c + i][f + j].ESTADO_MINA == ESTADO_CUESTIÓN) {
                            return true;
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    void DescubrirPerifericos() {
        if (BuscarDuda()) {
            return;
        }
        int c = PosiciónEnTablero.Columna();
        int f = PosiciónEnTablero.Fila();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                try {
                    BotónMina botónMina = botonesMina[c + i][f + j];
                    if (botónMina.INTERACTIVO) {
                        ProcesoParalelo.Descubrir(botónMina);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    void Descubrir() {
        if (ESTADO_MINA == ESTADO_MARCADO || !INTERACTIVO) {
            return;
        }
        if (ValorEnTablero == -1) {
            if (!PrimeraPistaDetectada) {
                ReDepositarMina(PosiciónEnTablero);
                Descubrir();
                return;
            } else {
                CambiarIcono(MinaActivada);
                TerminarJuego();
            }
        } else if (ValorEnTablero == 0) {
            if (!PrimeraPistaDetectada) {
                PrimeraPistaDetectada = true;
                tablero = null;
            }
            CambiarIcono(ImgVacio);
            ProcesoParalelo.DescubrirPeriféricos(this);
        } else if (ValorEnTablero > 0) {
            CambiarIcono(Números[ValorEnTablero]);
        }
        ImgDibujar = MouseFuera_Img;
        ConcluirJuego();
    }

    public static void CargarElementosEstaticos() {
        double lado_Aux = Mín(1280 / Columnas, 720 / (Filas + 2));
        LADO = (byte) (lado_Aux > 50 ? 50 : lado_Aux);
        System.out.println("LADO: " + LADO);
        if (ImgCubierto == null) {
            ImgCubierto = EscalarLado(cargarImagen("Baldosa Base.jpg"));
            ImgCubierto_Sobre = Sombreado(ImgCubierto, Color.BLUE);
            ImgCubierto_Pres = Sombreado(ImgCubierto, Color.BLACK);
            ImgVacio = EscalarLado(cargarImagen("Baldosa Vacia.jpg"));
            ImgMarcado = Marcado();
            ImgMarcado_Sobre = Sombreado(ImgMarcado, Color.WHITE);
            ImgCuestión = Cuestión();
            Números = new BufferedImage[]{
                ImgVacio,
                EscalarLado(Número(1)),
                EscalarLado(Número(2)),
                EscalarLado(Número(3)),
                EscalarLado(Número(4)),
                EscalarLado(Número(5)),
                EscalarLado(Número(6)),
                EscalarLado(Número(7)),
                EscalarLado(Número(8))
            };
            MinaActivada = MinaActivada();
            MinaDesactivada = MinaDesactivada();
            MinaDesactivadaErronea = MinaDesactivadaErronea();
            Inconsistencia = Escalar(EscalarLado(cargarImagen("Inconsistencia.png")), .7, false);
        }
    }

    public static void DeponerElementosEstaticos() {
        ImgCubierto = null;
        ImgCubierto_Sobre = null;
        ImgCubierto_Pres = null;
        ImgVacio = null;
        ImgMarcado = null;
        ImgMarcado_Sobre = null;
        ImgCuestión = null;
        Números = null;
        MinaActivada = null;
        MinaDesactivada = null;
        MinaDesactivadaErronea = null;
        Inconsistencia = null;
    }

    static BufferedImage Sombreado(BufferedImage imagen, Color color) {
        return IconoInclinadoEnColor(color, Escalar(imagen, LADO, LADO, false));
    }

    static BufferedImage Marcado() {
        BufferedImage Retorno = Clonar(ImgCubierto);
        Retorno.getGraphics().drawImage(EscalarLado(cargarImagen("Marcado.png")), 0, 0, null);
        return Retorno;
    }

    static BufferedImage Cuestión() {
        BufferedImage Cuestión = Clonar(ImgCubierto);
        Cuestión.getGraphics().drawImage(EscalarLado(cargarImagen("Cuestión.png")), 0, 0, null);
        return Cuestión;
    }

    static BufferedImage MinaActivada() {
        BufferedImage Retorno = IconoInclinadoEnColor(Color.RED, ImgVacio);
        Retorno.getGraphics().drawImage(EscalarLado(cargarImagen("Mina Activada.png")), 0, 0, null);
        return Retorno;
    }

    static BufferedImage MinaDesactivada() {
        BufferedImage Retorno = Clonar(ImgVacio);
        Retorno.getGraphics().drawImage(EscalarLado(cargarImagen("Mina Desactivada.png")), 0, 0, null);
        return Retorno;
    }

    static BufferedImage MinaDesactivadaErronea() {
        BufferedImage Retorno = Clonar(ImgVacio);
        Graphics2D g = Retorno.createGraphics();
        g.drawImage(EscalarLado(cargarImagen("Mina Desactivada.png")), 0, 0, null);
        g.drawImage(EscalarLado(cargarImagen("Error.png")), 0, 0, null);
        return Retorno;
    }

    static BufferedImage Número(int n) {
        Color colores[] = {
            null,//0
            Color.BLUE, new Color(0, 128, 0), Color.RED, new Color(0, 0, 128), new Color(128, 0, 0),
            new Color(128, 0, 128), new Color(128, 128, 0), new Color(0, 128, 128)
        };
        BufferedImage Retorno = Clonar(ImgVacio);
        Ajuste_Personalizado(new GeneraciónDeTexto().ModificarFuente(new Font("arial", Font.BOLD, 40))
                .ColorFuente(colores[n]).GenerarTexto(n + ""), Retorno, AJUSTE_CENTRADO_AJUSTAR);
        return Retorno;
    }

    private static class ProcesoParalelo implements Runnable {

        Thread hilo = new Thread(this);
        BotónMina botónMinaAux;

        static final byte DESCUBRIR = 0, DESCUBRIR_PERIFERICOS = 1;
        byte INSTRUCCIÓN;

        private ProcesoParalelo(BotónMina botónMina, byte instrucción) {
            INSTRUCCIÓN = instrucción;
            botónMinaAux = botónMina;
            hilo.start();
        }

        static void Descubrir(BotónMina botónMina) {
            new ProcesoParalelo(botónMina, DESCUBRIR);
        }

        static void DescubrirPeriféricos(BotónMina botónMina) {
            new ProcesoParalelo(botónMina, DESCUBRIR_PERIFERICOS);
        }

        @Override
        public void run() {
            switch (INSTRUCCIÓN) {
                case DESCUBRIR:
                    botónMinaAux.Descubrir();
                    break;
                case DESCUBRIR_PERIFERICOS:
                    botónMinaAux.DescubrirPerifericos();
                    break;
            }
        }
    }

}
