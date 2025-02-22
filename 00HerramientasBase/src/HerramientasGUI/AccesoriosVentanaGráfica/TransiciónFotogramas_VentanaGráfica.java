package HerramientasGUI.AccesoriosVentanaGráfica;

import HerramientaArchivos.BibliotecaArchivos;
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientaDeImagen.Filtro_Convolución;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientasGUI.VentanaGráfica;
import HerramientasMatemáticas.Dupla;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class TransiciónFotogramas_VentanaGráfica {

    public static void main(String[] args) throws Exception {
        VentanaGráfica ventana = new VentanaGráfica();
        ventana.CambiarTamaño(VentanaGráfica.DIMENSIÓN_GRANDE, true);
        ventana.ProporciónRedimensionar(16, 9);
        ventana.Alternar_SiempreVisible();
        ventana.Consola(new GeneradorDeTexto()
                .ModificarTamañoFuente(40)
                .AlineaciónHorizontal(Dupla.MEDIO)
                .ColorFuente(Color.WHITE));
        int FPS = 20;
        BufferedImage imgPartida = BibliotecaArchivos.Imagenes.Test.Wallpaper_1();
        BufferedImage imgLlegada = BibliotecaArchivos.Imagenes.Test.Wallpaper_2();
//        {
//            ventana.MostrarTexto("Transición: Fundido");
//            Thread.sleep(1000);
//            Fundido(imgPartida, imgLlegada, FPS, 1, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            Color color = new Color(
//                    (int) ((2 * Math.random() - 1) * Integer.MAX_VALUE)
//            );
//            BufferedImage ImgColor = new BufferedImage(200, 200, 2) {
//                {
//                    Graphics2D g = createGraphics();
//                    g.setColor(color);
//                    g.fillOval(0, 0, getWidth(), getHeight());
//                }
//            };
//            ventana.MostrarTexto("Transición: Fundido por color %img%", ImgColor);
//            Thread.sleep(1000);
//            FundidoPorColor(color, imgPartida, imgLlegada, FPS, 1, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Empuje desde Arriba");
//            Thread.sleep(2000);
//            EmpujarDesde_ARRIBA(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Empuje desde Derecha");
//            Thread.sleep(2000);
//            EmpujarDesde_DERECHA(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Empuje desde Abajo");
//            Thread.sleep(2000);
//            EmpujarDesde_ABAJO(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Empuje desde Izquierda");
//            Thread.sleep(2000);
//            EmpujarDesde_IZQUIERDA(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            Dupla d = new Dupla(
//                    Math.random() * imgPartida.getWidth(),
//                    Math.random() * imgPartida.getHeight()
//            );
//            ventana.MostrarTexto("Transición:\nAparecer Desde un punto ej\n" + d.toString());
//            Thread.sleep(2000);
//            AparecerDesde(d, imgPartida, imgLlegada, FPS, 1, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Aparecer Desde Centro");
//            Thread.sleep(2000);
//            AparecerDesdeCentro(imgPartida, imgLlegada, FPS, 1, ventana);
//            Thread.sleep(1000);
//        }
//        ventana.MostrarTexto("Transiciones \"Revelar\"");
//        Thread.sleep(2000);
//        {
//            ventana.MostrarTexto("Transición: Revelar desde DERECHA");
//            Thread.sleep(2000);
//            RevelarDesde_DERECHA(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Revelar desde IZQUIERDA");
//            Thread.sleep(2000);
//            RevelarDesde_IZQUIERDA(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Revelar desde ABAJO");
//            Thread.sleep(2000);
//            RevelarDesde_ABAJO(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Revelar desde DERECHA-ABAJO");
//            Thread.sleep(2000);
//            RevelarDesde_DERECHA_ABAJO(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Revelar desde DERECHA-ARRIBA");
//            Thread.sleep(2000);
//            RevelarDesde_DERECHA_ARRIBA(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Revelar desde IZQUIERDA-ABAJO");
//            Thread.sleep(2000);
//            RevelarDesde_IZQUIERDA_ABAJO(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Revelar desde IZQUIERDA-ARRIBA");
//            Thread.sleep(2000);
//            RevelarDesde_IZQUIERDA_ARRIBA(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Revelar desde ARRIBA");
//            Thread.sleep(2000);
//            RevelarDesde_ARRIBA(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        ventana.MostrarTexto("Transiciones \"Cubrir\"");
//        Thread.sleep(2000);
//        {
//            ventana.MostrarTexto("Transición: Cubrir desde DERECHA");
//            Thread.sleep(2000);
//            CubrirDesde_DERECHA(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Cubrir desde IZQUIERDA");
//            Thread.sleep(2000);
//            CubrirDesde_IZQUIERDA(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Cubrir desde ABAJO");
//            Thread.sleep(2000);
//            CubrirDesde_ABAJO(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Cubrir desde DERECHA-ABAJO");
//            Thread.sleep(2000);
//            CubrirDesde_DERECHA_ABAJO(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Cubrir desde DERECHA-ARIBA");
//            Thread.sleep(2000);
//            CubrirDesde_DERECHA_ARRIBA(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Cubrir desde IZQUIERDA-ABAJO");
//            Thread.sleep(2000);
//            CubrirDesde_IZQUIERDA_ABAJO(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
//        {
//            ventana.MostrarTexto("Transición: Cubrir desde IZQUIERDA-ARRIBA");
//            Thread.sleep(2000);
//            CubrirDesde_IZQUIERDA_ARRIBA(imgPartida, imgLlegada, FPS, .6, ventana);
//            Thread.sleep(1000);
//        }
        {
            ventana.MostrarTexto("Transición: Cubrir desde ARRIBA");
            Thread.sleep(2000);
            CubrirDesde_ARRIBA(imgPartida, imgLlegada, FPS, .6, ventana);
            Thread.sleep(1000);
        }
//        {
//            for (int i = 1; i <= 55; i++) {
//                ventana.MostrarTexto("Transición: Cortina " + i);
//                Thread.sleep(1000);
//                Cortina(
//                        BibliotecaTransiciones.Transición_Cortina_30(),
//                        imgPartida, imgLlegada, ventana
//                );
//                Thread.sleep(1000);
//            }
//        }
        Thread.sleep(1000);
        ventana.MostrarTexto("FIN");
        Thread.sleep(2000);
        System.exit(0);
    }

    //<editor-fold defaultstate="collapsed" desc="Transición cortina">
    public static void Cortina(File[] Cortina, BufferedImage imgPartida, BufferedImage imgLlegada, VentanaGráfica ventana) {
        Cortina(Cortina, imgPartida, imgLlegada, ventana, false);
    }
    
    public static void Cortina(File[] Cortina, BufferedImage imgPartida, BufferedImage imgLlegada, VentanaGráfica ventana, boolean PresentarAlReves) {
        Cortina(Cortina, imgPartida, imgLlegada, ventana, PresentarAlReves, null);
    }
    
    public static void Cortina(File[] Cortina, BufferedImage imgPartida, BufferedImage imgLlegada, VentanaGráfica ventana, Paint color) {
        Cortina(Cortina, imgPartida, imgLlegada, ventana, false, color);
    }
    
    public static void Cortina(File[] Cortina, BufferedImage imgPartida, BufferedImage imgLlegada, VentanaGráfica ventana, boolean PresentarAlReves, Paint color) {
        Cortina(Cortina, imgPartida, imgLlegada, ventana, PresentarAlReves, color, PresentarAlReves);
    }
    
    public static void Cortina(File[] Cortina, BufferedImage imgPartida, BufferedImage imgLlegada, VentanaGráfica ventana, boolean PresentarAlReves, Paint color, boolean PintarCortinaGris) {
        Cortina(Cortina, imgPartida, imgLlegada, 22, ventana, PresentarAlReves, color, PintarCortinaGris);
    }
    
    public static void Cortina(File[] Cortina, BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, VentanaGráfica ventana, boolean PresentarAlReves, Paint Color, boolean PintarCortinaGris) {
        //<editor-fold defaultstate="collapsed" desc="Implementación del código">
        ventana.ActualizarFotograma(imgPartida);
        if (Cortina != null) {
            ArrayList<File> Fotogramas = new ArrayList<>();
            for (File file : Cortina) {
                if (!file.exists()) {
                    throw new RuntimeException("Todas las imagenes para la transición deben existir");
                }
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    throw new RuntimeException("Todas las imagenes para la transición deben ser formato PNG");
                }
                Fotogramas.add(file);
            }
            double ex = 0;
            double ey = 0;
            AffineTransform ajusteEscala = new AffineTransform();
            
            double duración = Fotogramas.size() / (double) FPS;
            double t = 0;
            long ms_inicio = System.currentTimeMillis();
            while (t < 1) {
                try {
                    t = (System.currentTimeMillis() - ms_inicio) / (1000 * duración);
                    if (t > 1) {
                        break;
                    }
                    int p = (int) Math.round(Fotogramas.size() * t);
                    if (p == Fotogramas.size()) {
                        break;
                    }
                    if (PresentarAlReves) {
                        p = Fotogramas.size() - p;
                    }
                    BufferedImage ImgPersiana = LectoEscrituraArchivos.cargar_imagen(
                            Fotogramas.get(p)
                    );
                    if (PintarCortinaGris) {
                        BufferedImage im = ImgPersiana;
                        BufferedImage gris = new BufferedImage(
                                ImgPersiana.getWidth(), ImgPersiana.getHeight(), BufferedImage.TYPE_BYTE_GRAY
                        ) {
                            {
                                Graphics2D g = createGraphics();
                                g.drawImage(im, 0, 0, null);
                            }
                        };
                        Graphics2D g = ImgPersiana.createGraphics();
                        g.setComposite(AlphaComposite.SrcAtop);
                        g.drawImage(gris, 0, 0, null);
                    }
                    if (Color != null) {
                        Graphics2D g = ImgPersiana.createGraphics();
                        g.setPaint(Color);
                        g.setComposite(AlphaComposite.SrcAtop);
                        g.fillRect(0, 0, ImgPersiana.getWidth(), ImgPersiana.getHeight());
                    }
                    BufferedImage fotograma = new BufferedImage(imgPartida.getWidth(), imgPartida.getHeight(), 2);
                    Graphics2D g = fotograma.createGraphics();
                    if (t < .5) {
                        g.drawImage(imgPartida, 0, 0, null);
                    } else {
                        g.drawImage(imgLlegada, 0, 0, null);
                    }
                    if (ex == 0 && ey == 0) {
                        ex = imgPartida.getWidth() / (double) ImgPersiana.getWidth();
                        ey = imgPartida.getHeight() / (double) ImgPersiana.getHeight();
                        ajusteEscala = AffineTransform.getScaleInstance(ex, ey);
                    }
                    g.drawImage(ImgPersiana, ajusteEscala, null);
                    ventana.ActualizarFotograma(fotograma);
                    Thread.sleep(1000 / FPS / 4);
                } catch (Exception e) {
                }
            }
        }
        ventana.ActualizarFotograma(imgLlegada);
//</editor-fold>
    }
//</editor-fold>

    public static void AparecerDesdeCentro(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        AparecerDesde(new Dupla(imgPartida).Centro(), imgPartida, imgLlegada, FPS, duración_S, ventana);
    }

    public static void AparecerDesde(Dupla puntoAparición, BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        ventana.ActualizarFotograma(imgPartida);
        double t = 0;
        long ms_inicio = System.currentTimeMillis();
        Dupla.Curva TransiciónPosición = Dupla.Curva.RectaVectorial2Puntos(
                puntoAparición, Dupla.ORIGEN
        );
        while (t < 1) {
            try {
                t = (System.currentTimeMillis() - ms_inicio) / (1000 * duración_S);
                if (t >= 1) {
                    break;
                }
                Dupla pos = TransiciónPosición.XY(t);
                AffineTransform at = new AffineTransform();
                at.translate(pos.intX(), pos.intY());
                at.scale(t, t);
                BufferedImage fotograma = new BufferedImage(imgPartida.getWidth(), imgPartida.getHeight(), 2);
                Graphics2D g = fotograma.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(imgPartida, 0, 0, null);
                g.fillOval(pos.intX(), pos.intY(), 5, 5);
                g.drawImage(imgLlegada, at, null);
                ventana.ActualizarFotograma(fotograma);
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
            }
        }
        ventana.ActualizarFotograma(imgLlegada);
    }

    public static void RevelarDesde_DERECHA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        RevelarDesde(Dupla.DERECHA, Dupla.MEDIO, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void RevelarDesde_DERECHA_ABAJO(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        RevelarDesde(Dupla.DERECHA, Dupla.ABAJO, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void RevelarDesde_DERECHA_ARRIBA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        RevelarDesde(Dupla.DERECHA, Dupla.ARRIBA, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void RevelarDesde_IZQUIERDA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        RevelarDesde(Dupla.IZQUIERDA, Dupla.MEDIO, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void RevelarDesde_IZQUIERDA_ABAJO(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        RevelarDesde(Dupla.IZQUIERDA, Dupla.ABAJO, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void RevelarDesde_IZQUIERDA_ARRIBA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        RevelarDesde(Dupla.IZQUIERDA, Dupla.ARRIBA, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void RevelarDesde_ARRIBA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        RevelarDesde(Dupla.MEDIO, Dupla.ARRIBA, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void RevelarDesde_ABAJO(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        RevelarDesde(Dupla.MEDIO, Dupla.ABAJO, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void RevelarDesde(byte LateralHorizontal, byte LateralVertical, BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        if (LateralHorizontal != Dupla.DERECHA && LateralHorizontal != Dupla.IZQUIERDA) {
            if (LateralVertical != Dupla.ARRIBA && LateralVertical != Dupla.ABAJO) {
                throw new RuntimeException("Al menos unos de los laterales debe indicar un inicio");
            }
        }
        ventana.ActualizarFotograma(imgPartida);
        Dupla posFin_imgPartida;
        switch (LateralHorizontal) {
            case Dupla.DERECHA:
                posFin_imgPartida = new Dupla(-imgPartida.getWidth(), 0);
                break;
            case Dupla.IZQUIERDA:
                posFin_imgPartida = new Dupla(imgPartida.getWidth(), 0);
                break;
            default:
                posFin_imgPartida = new Dupla();
        }
        switch (LateralVertical) {
            case Dupla.ARRIBA:
                posFin_imgPartida.ReemplazarY(imgPartida.getHeight());
                break;
            case Dupla.ABAJO:
                posFin_imgPartida.ReemplazarY(-imgPartida.getHeight());
                break;
        }
        Dupla.Curva recorridoimgPartida = Dupla.Curva.RectaVectorial2Puntos(
                Dupla.ORIGEN, posFin_imgPartida
        );
        int radioDesenfoque = (int) (1 / duración_S) * 15 + 1;

        BufferedImage imgDesenfoque = null;
        switch (LateralHorizontal) {
            case Dupla.DERECHA:
            case Dupla.IZQUIERDA:
                imgDesenfoque = Filtro_Convolución.Desenfoque_Horizontal(imgPartida, radioDesenfoque);
        }
        switch (LateralVertical) {
            case Dupla.ARRIBA:
            case Dupla.ABAJO:
                imgDesenfoque = Filtro_Convolución.Desenfoque_Horizontal(imgPartida, radioDesenfoque);
        }

        double t = 0;
        long ms_inicio = System.currentTimeMillis();
        while (t < 1) {
            try {
                t = (System.currentTimeMillis() - ms_inicio) / (1000 * duración_S);
                if (t >= 1) {
                    break;
                }
                BufferedImage fotograma = new BufferedImage(imgPartida.getWidth(), imgPartida.getHeight(), 2);
                Graphics2D g = fotograma.createGraphics();
                g.drawImage(imgLlegada, 0, 0, null);

                Dupla posimgPartida = recorridoimgPartida.XY(t);
                g.drawImage(imgDesenfoque, posimgPartida.intX(), posimgPartida.intY(), null);

                ventana.ActualizarFotograma(fotograma);
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
            }
        }
        ventana.ActualizarFotograma(imgLlegada);
    }

    public static void CubrirDesde_DERECHA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        CubrirDesde(Dupla.DERECHA, Dupla.MEDIO, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void CubrirDesde_DERECHA_ABAJO(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        CubrirDesde(Dupla.DERECHA, Dupla.ABAJO, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void CubrirDesde_DERECHA_ARRIBA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        CubrirDesde(Dupla.DERECHA, Dupla.ARRIBA, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void CubrirDesde_IZQUIERDA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        CubrirDesde(Dupla.IZQUIERDA, Dupla.MEDIO, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void CubrirDesde_IZQUIERDA_ABAJO(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        CubrirDesde(Dupla.IZQUIERDA, Dupla.ABAJO, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void CubrirDesde_IZQUIERDA_ARRIBA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        CubrirDesde(Dupla.IZQUIERDA, Dupla.ARRIBA, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void CubrirDesde_ARRIBA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        CubrirDesde(Dupla.MEDIO, Dupla.ARRIBA, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void CubrirDesde_ABAJO(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        CubrirDesde(Dupla.MEDIO, Dupla.ABAJO, imgPartida, imgLlegada, FPS, .75, ventana);
    }

    public static void CubrirDesde(byte LateralHorizontal, byte LateralVertical, BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        if (LateralHorizontal != Dupla.DERECHA && LateralHorizontal != Dupla.IZQUIERDA) {
            if (LateralVertical != Dupla.ARRIBA && LateralVertical != Dupla.ABAJO) {
                throw new RuntimeException("Al menos unos de los laterales debe indicar un inicio");
            }
        }
        ventana.ActualizarFotograma(imgPartida);
        Dupla posInicio_imgLlegada;
        switch (LateralHorizontal) {
            case Dupla.DERECHA:
                posInicio_imgLlegada = new Dupla(imgPartida.getWidth(), 0);
                break;
            case Dupla.IZQUIERDA:
                posInicio_imgLlegada = new Dupla(-imgPartida.getWidth(), 0);
                break;
            default:
                posInicio_imgLlegada = new Dupla();
        }
        switch (LateralVertical) {
            case Dupla.ARRIBA:
                posInicio_imgLlegada.ReemplazarY(-imgPartida.getHeight());
                break;
            case Dupla.ABAJO:
                posInicio_imgLlegada.ReemplazarY(imgPartida.getHeight());
                break;
        }
        Dupla.Curva recorridoimgPartida = Dupla.Curva.RectaVectorial2Puntos(
                posInicio_imgLlegada, Dupla.ORIGEN
        );
        int radioDesenfoque = (int) (1 / duración_S) * 15 + 1;

        BufferedImage imgDesenfoque = null;
        switch (LateralHorizontal) {
            case Dupla.DERECHA:
            case Dupla.IZQUIERDA:
                imgDesenfoque = Filtro_Convolución.Desenfoque_Horizontal(imgLlegada, radioDesenfoque);
        }
        switch (LateralVertical) {
            case Dupla.ARRIBA:
            case Dupla.ABAJO:
                imgDesenfoque = Filtro_Convolución.Desenfoque_Horizontal(imgLlegada, radioDesenfoque);
        }

        double t = 0;
        long ms_inicio = System.currentTimeMillis();
        while (t < 1) {
            try {
                t = (System.currentTimeMillis() - ms_inicio) / (1000 * duración_S);
                if (t >= 1) {
                    break;
                }
                BufferedImage fotograma = new BufferedImage(imgPartida.getWidth(), imgPartida.getHeight(), 2);
                Graphics2D g = fotograma.createGraphics();
                g.drawImage(imgPartida, 0, 0, null);

                Dupla posimgLlegada = recorridoimgPartida.XY(t);
                g.drawImage(imgDesenfoque, posimgLlegada.intX(), posimgLlegada.intY(), null);

                ventana.ActualizarFotograma(fotograma);
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
            }
        }
        ventana.ActualizarFotograma(imgLlegada);
    }

    public static void EmpujarDesde_ARRIBA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        EmpujarDesde(Dupla.ARRIBA, imgPartida, imgLlegada, FPS, duración_S, ventana);
    }

    public static void EmpujarDesde_DERECHA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        EmpujarDesde(Dupla.DERECHA, imgPartida, imgLlegada, FPS, duración_S, ventana);
    }

    public static void EmpujarDesde_ABAJO(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        EmpujarDesde(Dupla.ABAJO, imgPartida, imgLlegada, FPS, duración_S, ventana);
    }

    public static void EmpujarDesde_IZQUIERDA(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        EmpujarDesde(Dupla.IZQUIERDA, imgPartida, imgLlegada, FPS, duración_S, ventana);
    }

    public static void EmpujarDesde(byte Lateral, BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        ventana.ActualizarFotograma(imgPartida);
        double t = 0;
        long ms_inicio = System.currentTimeMillis();
        Dupla posFin_imgPartida;
        Dupla posInicio_ImgLlegada;
        switch (Lateral) {
            case Dupla.ARRIBA:
                posFin_imgPartida = new Dupla(0, imgPartida.getHeight());
                posInicio_ImgLlegada = new Dupla(0, -imgPartida.getHeight());
                break;
            case Dupla.DERECHA:
                posFin_imgPartida = new Dupla(-imgPartida.getWidth(), 0);
                posInicio_ImgLlegada = new Dupla(imgPartida.getWidth(), 0);
                break;
            case Dupla.ABAJO:
                posFin_imgPartida = new Dupla(0, -imgPartida.getHeight());
                posInicio_ImgLlegada = new Dupla(0, imgPartida.getHeight());
                break;
            case Dupla.IZQUIERDA:
                posFin_imgPartida = new Dupla(imgPartida.getWidth(), 0);
                posInicio_ImgLlegada = new Dupla(-imgPartida.getWidth(), 0);
                break;
            default:
                throw new RuntimeException("Use los laterales byte de la clase Dupla, no use el MEDIO");
        }
        Dupla.Curva recorridoimgPartida = Dupla.Curva.RectaVectorial2Puntos(
                Dupla.ORIGEN, posFin_imgPartida
        );
        Dupla.Curva recorridoImgLlegada = Dupla.Curva.RectaVectorial2Puntos(
                posInicio_ImgLlegada, Dupla.ORIGEN
        );
        int radioDesenfoque = (int) (1 / duración_S) * 10 + 1;
        while (t < 1) {
            try {
                t = (System.currentTimeMillis() - ms_inicio) / (1000 * duración_S);
                if (t >= 1) {
                    break;
                }
                BufferedImage fotograma = new BufferedImage(imgPartida.getWidth(), imgPartida.getHeight(), 2);
                Graphics2D g = fotograma.createGraphics();
                Dupla posimgPartida = recorridoimgPartida.XY(t);
                Dupla posImgLlegada = recorridoImgLlegada.XY(t);
                g.drawImage(imgPartida, posimgPartida.intX(), posimgPartida.intY(), null);
                g.drawImage(imgLlegada, posImgLlegada.intX(), posImgLlegada.intY(), null);
                switch (Lateral) {
                    case Dupla.ARRIBA:
                    case Dupla.ABAJO:
                        fotograma = Filtro_Convolución.Desenfoque_Vertical(fotograma, radioDesenfoque);
                        break;
                    case Dupla.DERECHA:
                    case Dupla.IZQUIERDA:
                        fotograma = Filtro_Convolución.Desenfoque_Horizontal(fotograma, radioDesenfoque);
                        break;
                }
                ventana.ActualizarFotograma(fotograma);
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
            }
        }
        ventana.ActualizarFotograma(imgLlegada);
    }

    public static void Fundido(BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        ventana.ActualizarFotograma(imgPartida);
        double t = 0;
        long ms_inicio = System.currentTimeMillis();
        while (t < 1) {
            try {
                t = (System.currentTimeMillis() - ms_inicio) / (1000 * duración_S);
                if (t >= 1) {
                    break;
                }
                BufferedImage fotograma = new BufferedImage(imgPartida.getWidth(), imgPartida.getHeight(), 2);
                Graphics2D g = fotograma.createGraphics();
                AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - (float) t);
                g.setComposite(a);
                g.drawImage(imgPartida, 0, 0, null);
                a = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) t);
                g.setComposite(a);
                g.drawImage(imgLlegada, 0, 0, null);
                ventana.ActualizarFotograma(fotograma);
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
            }
        }
        ventana.ActualizarFotograma(imgLlegada);
    }

    public static void FundidoPorColor(Color color, BufferedImage imgPartida, BufferedImage imgLlegada, int FPS, double duración_S, VentanaGráfica ventana) {
        ventana.ActualizarFotograma(imgPartida);
        double t = 0;
        long ms_inicio = System.currentTimeMillis();
        while (t < 2) {
            try {
                t = (System.currentTimeMillis() - ms_inicio) / (1000 * duración_S / 2);
                if (t >= 2) {
                    break;
                }
                BufferedImage fotograma = new BufferedImage(imgPartida.getWidth(), imgPartida.getHeight(), 2);
                Graphics2D g = fotograma.createGraphics();
                if (t <= 1) {
                    g.drawImage(imgPartida, 0, 0, null);
                    g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (t * 255)));
                    g.fillRect(0, 0, fotograma.getWidth(), fotograma.getHeight());
                } else {
                    g.drawImage(imgLlegada, 0, 0, null);
                    g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) ((1 - t % 1) * 255)));
                    g.fillRect(0, 0, fotograma.getWidth(), fotograma.getHeight());
                }
                ventana.ActualizarFotograma(fotograma);
                Thread.sleep(1000 / FPS);
            } catch (Exception e) {
            }
        }
        ventana.ActualizarFotograma(imgLlegada);
    }
}
