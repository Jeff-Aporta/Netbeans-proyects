package Principal;

import Animacion.*;
import static Animacion.ObjetoDibujable.Escenario;
import static HerramientaArchivos.BibliotecaArchivos.*;
import static HerramientaArchivos.LectoEscrituraArchivos.*;
import HerramientasSistema.Sistema;
import HerramientaDeImagen.Filtro_Convolución;
import HerramientaDeImagen.Filtros_Lineales;
import HerramientasColor.InfoColor;
import static HerramientaDeImagen.Filtros_Lineales.*;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientaDeImagen.ModoFusión.ModoFusión.CombinaciónLineal.RGB_Aditivo;
import HerramientasColor.ConversorModelosColor;
import HerramientasGUI.VentanaGráfica;
import HerramientasMatemáticas.*;
import JAVH.*;
import static JAVH.Constantes_Ejecutador.*;
import JID3.Etiquetas;
import static Principal.GeneradorDeSencillos.actual;
import static Principal.GeneradorDeSencillos.total;
import com.jhlabs.image.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ProyectoVideo {

    public static final String outFramesFormat = "\\%07d.jpg";
    public static final String outFramesExtensión = outFramesFormat.substring(
            outFramesFormat.lastIndexOf('.') + 1
    );

    public static void main(String[] args) throws Exception {
        String Audio = "C:\\Me\\Mago de Oz\\1994 - Mägo de Oz\\04 Gerdundula.mp3";
        String Video = Audio.replace(".mp3", ".mp4");
//        Proyecto_SimpleConCronometro(Audio, Video, "Aquí va el album", null);
        Proyecto_Ondas_Caratula(Audio, Video);
    }

    public static void Proyecto_Album(final String audio, final String videoSalida, String extra, VentanaGráfica ventana) throws Exception {
        BufferedImage cover = null;
        try {
            cover = Etiquetas.ImagenDeCaratulaEnCarpeta(new File(audio));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Caratula no encontrada\n" + audio);
            return;
        }
        BufferedImage fondo1;
        BufferedImage fondo2 = fondo1 = new BufferedImage(1280, 720, 2);
        Ajuste_Rellenar(cover, fondo1);
        Ajuste_Rellenar(cover, fondo2);
        {
            PlasmaFilter p = new PlasmaFilter();
            p.useImageColors = true;
            p.seed = Matemática.AleatorioEnteroEntre(Integer.MIN_VALUE, Integer.MAX_VALUE);
            fondo1 = p.filter(fondo1, null);
            fondo2 = p.filter(fondo2, null);
            {
                Graphics2D g = fondo2.createGraphics();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
                BufferedImage fondotemp = new BufferedImage(1280, 720, 2);
                Ajuste_Rellenar(cover, fondotemp);
                g.drawImage(fondotemp, 0, 0, null);
            }
        }
        BufferedImage LogoCentral = Dupla.convBufferedImage(165 * 2);
        {
            Graphics2D g = LogoCentral.createGraphics();
            g.fillOval(0, 0, LogoCentral.getWidth(), LogoCentral.getHeight());
            g.setComposite(AlphaComposite.SrcIn);

            BufferedImage LogoCentraltemp = Dupla.convBufferedImage(165 * 2);
            Ajuste_Rellenar(cover, LogoCentraltemp);
            g.drawImage(LogoCentraltemp, 0, 0, null);
        }
        float[] HSL = ConversorModelosColor.RGBtoHSL(InfoColor.ColorPrincipal(cover));
        if (HSL[2] > .7) {
            HSL[2] = .7f;
        }
        Color cp = new Color(ConversorModelosColor.HSLtoRGB(HSL));
        EspectroCircular(
                audio,
                videoSalida,
                extra,
                ventana,
                cp,
                new BufferedImage[]{fondo1, fondo2},
                LogoCentral
        );
    }

    public static void Proyecto_Ondas_Caratula(final String audio, final String videoSalida) throws Exception {
        BufferedImage cover = Etiquetas.ImagenDeCaratulaEnCarpeta(new File(audio));

        float[] HSL = ConversorModelosColor.RGBtoHSL(InfoColor.ColorPrincipal(cover));
        HSL[1] = HSL[1] * (1 + 3 * HSL[2]);
        if (HSL[2] < .2f) {
            HSL[2] = .2f;
        }
        Color Color_Cover = new Color(ConversorModelosColor.HSLtoRGB(HSL));

        String Centro = TMP_DIR + "\\Centro" + (int) (9999 * Math.random()) + ".png";
        BufferedImage CentroImg = new BufferedImage(300, 300, 2) {
            {
                Graphics2D g = createGraphics();
                g.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
                );
                g.fillOval(0, 0, getWidth(), getHeight());
                g.setComposite(AlphaComposite.SrcAtop);
                BufferedImage escala = new BufferedImage(getWidth(), getHeight(), 2);
                Filtros_Lineales.Ajuste_Ajustar(cover, escala);
                g.drawImage(escala, 0, 0, null);
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(3));
                g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        BufferedImage Portada = new BufferedImage(1260, 700, 2);
        Filtros_Lineales.Ajuste_Centrado_Ajustar(CentroImg, Portada);
        Etiquetas etiquetas = new Etiquetas(audio, false);
        Filtros_Lineales.Ajuste_Centrado_Ajustar(
                new GeneradorDeTexto()
                        .GrosorBorde(5)
                        .AlineaciónHorizontal(Dupla.MEDIO)
                        .ModificarTamañoFuente(40)
                        .ColorBorde(Color_Cover)
                        .ModificarTipoFuente("times new roman")
                        .ColorFuente(Color.WHITE)
                        .GenerarTexto(
                                etiquetas.TÍTULO + " - " + etiquetas.ARTISTA + " - " + etiquetas.ALBÚM
                                + "\n" + etiquetas.AÑO
                        ),
                Portada,
                Dupla.MEDIO, Dupla.ABAJO
        );

        exportar_imagen(Portada, Centro);

        String videoOndas = TMP_DIR + "\\Espectro" + (int) (9999 * Math.random()) + ".mp4";
        FFmpeg_OndasAudio ondasAudio = new FFmpeg_OndasAudio(
                audio, videoOndas, 1280, 720
        );
        ondasAudio.COLOR = Color_Cover;
        ondasAudio.FPS = 12;
        ondasAudio.ModificarModoDibujo(
                FFmpeg_OndasAudio.MODO_DIBUJO_ONDA_LINE
        );
        ondasAudio.EspectroOndasAudio_Mono();

        FFmpeg fmpeg = new FFmpeg(videoOndas, videoSalida);
        fmpeg.Watermark(Centro);

        new File(videoOndas).delete();
        new File(Centro).delete();
    }

    public static void Proyecto_SimpleSinCronometro(final String audio, final String videoSalida, String extra, VentanaGráfica ventana) throws Exception {
        BufferedImage cover = Etiquetas.ImagenDeCaratulaEnCarpeta(new File(audio));
        BufferedImage fondo = new BufferedImage(1280, 720, 2);
        Ajuste_Rellenar(cover, fondo);
        {
            Graphics2D g = fondo.createGraphics();
            g.setColor(new Color(0, 0, 0, (int) (255 * .10f)));
            g.fillRect(0, 0, fondo.getWidth(), fondo.getHeight());
            {
                GradientPaint degradado = new GradientPaint(
                        0, fondo.getHeight() / 2, new Color(0, true),
                        0, 0, new Color(0, 0, 0, (int) (255 * .75f)),
                        true
                );
                g.setPaint(degradado);
                g.fillRect(0, 0, fondo.getWidth(), fondo.getHeight());
            }
            fondo = Filtro_Convolución.Desenfoque_Radial(fondo, 40);
        }
        int anchoCover = 450;
        BufferedImage LogoCentral = Dupla.convBufferedImage(anchoCover);
        {
            Graphics2D g = LogoCentral.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.fillRoundRect(0, 0, LogoCentral.getWidth(), LogoCentral.getHeight(), 50, 50);

            BufferedImage LogoCentraltemp = Dupla.convBufferedImage(anchoCover);
            Ajuste_Rellenar(cover, LogoCentraltemp);

            g.setComposite(AlphaComposite.SrcIn);
            g.drawImage(LogoCentraltemp, 0, 0, null);
        }
        float[] HSL = ConversorModelosColor.RGBtoHSL(InfoColor.ColorPrincipal(cover));
        HSL[1] = HSL[1] * (1 + HSL[2]);
        if (HSL[1] > 1) {
            HSL[1] = 1;
        }
        if (HSL[2] > .55) {
            HSL[2] = .55f;
        }
        Color cp = new Color(ConversorModelosColor.HSLtoRGB(HSL));
        SimpleSinCronometro(
                audio,
                videoSalida,
                extra,
                ventana,
                cp,
                fondo,
                LogoCentral
        );
    }

    public static void Proyecto_SimpleConCronometro(final String audio, final String videoSalida, String extra, VentanaGráfica ventana) throws Exception {
        BufferedImage cover = Etiquetas.ImagenDeCaratulaEnCarpeta(new File(audio));
        BufferedImage fondo = new BufferedImage(1280, 720, 2);
        Ajuste_Rellenar(cover, fondo);
        {
            Graphics2D g = fondo.createGraphics();
            g.setColor(new Color(0, 0, 0, (int) (255 * .10f)));
            g.fillRect(0, 0, fondo.getWidth(), fondo.getHeight());
            {
                GradientPaint degradado = new GradientPaint(
                        0, fondo.getHeight() / 2, new Color(0, true),
                        0, 0, new Color(0, 0, 0, (int) (255 * .75f)),
                        true
                );
                g.setPaint(degradado);
                g.fillRect(0, 0, fondo.getWidth(), fondo.getHeight());
            }
            fondo = Filtro_Convolución.Desenfoque_Radial(fondo, 40);
        }
        int anchoCover = 450;
        BufferedImage LogoCentral = Dupla.convBufferedImage(anchoCover);
        {
            Graphics2D g = LogoCentral.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.fillRoundRect(0, 0, LogoCentral.getWidth(), LogoCentral.getHeight(), 50, 50);

            BufferedImage LogoCentraltemp = Dupla.convBufferedImage(anchoCover);
            Ajuste_Rellenar(cover, LogoCentraltemp);

            g.setComposite(AlphaComposite.SrcIn);
            g.drawImage(LogoCentraltemp, 0, 0, null);
        }
        float[] HSL = ConversorModelosColor.RGBtoHSL(InfoColor.ColorPrincipal(cover));
        HSL[1] = HSL[1] * (1 + HSL[2]);
        if (HSL[1] > 1) {
            HSL[1] = 1;
        }
        if (HSL[2] > .55) {
            HSL[2] = .55f;
        }
        Color cp = new Color(ConversorModelosColor.HSLtoRGB(HSL));
        SimpleConCronometro(
                audio,
                videoSalida,
                extra,
                ventana,
                cp,
                fondo,
                LogoCentral
        );
    }

    public static void Proyecto_Youtube_Plasma(final String audio, final String videoSalida, String extra, VentanaGráfica ventana) throws Exception {
        BufferedImage fondo1;
        BufferedImage fondo2;
        {
            PlasmaFilter p = new PlasmaFilter();
            p.seed = Matemática.AleatorioEnteroEntre(Integer.MIN_VALUE, Integer.MAX_VALUE);
            fondo1 = p.filter(Dupla.THD_1280x720.convBufferedImage(), null);
            p.turbulence = 2.3f;
            fondo2 = p.filter(Dupla.THD_1280x720.convBufferedImage(), null);
        }
        BufferedImage LogoCentral = Dupla.convBufferedImage(165 * 2);
        {
            Graphics2D g = LogoCentral.createGraphics();
            g.setColor(Color.WHITE);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.fillOval(0, 0, LogoCentral.getWidth(), LogoCentral.getHeight());
            BufferedImage logo = cargar_imagen("C:\\Users\\Àngel\\Documents\\Musica\\Youtube Audio Library\\logo.png");
            logo = Escalar(logo, .7, false);
            Ajuste_Rellenar(logo, LogoCentral);
        }
        EspectroCircular(
                audio,
                videoSalida,
                extra,
                ventana,
                null,
                new BufferedImage[]{fondo1, fondo2},
                LogoCentral
        );
    }

    public static void EspectroCircular(final String audio, final String videoSalida, String extra, VentanaGráfica ventana, Color colorbarra, BufferedImage[] fondo, BufferedImage LogoCentral) throws Exception {
        boolean FinishAndExit = false;
        int FPS = 9;
        if (ventana == null) {
            FPS = 1;
            FinishAndExit = true;
            ventana = new VentanaGráfica("ventana");
        }
        ObjetoDibujable.FPS = FPS;
        File CarpetaOndas = new File(Sistema.ruta_jeffAporta_tmp + "\\Espectro puntos " + Matemática.AleatorioEnteroEntre(0, 9999));
        CarpetaOndas.delete();
        CarpetaOndas.mkdirs();
        FFmpeg_OndasAudio ondasAudio = new FFmpeg_OndasAudio(audio, CarpetaOndas.getPath() + "\\%07d.png");
        ondasAudio.FPS = FPS;
        ondasAudio.CRF = 28;
        ondasAudio.ModificarModoDibujo(Constantes_DeOnda.MODO_DIBUJO_ONDA_POINT);
        ondasAudio.COLOR = Color.WHITE;
        ondasAudio.DIMENSIÓN = Dupla.THD_1280x720.convDimension();
        ondasAudio.EspectroOndasAudio_Mono();

        File[] WaveFrames = CarpetaOndas.listFiles();
        final int FotogramasTOT = WaveFrames.length;
        FFmpeg_InformaciónDeSensibilidad sensibilidad = new FFmpeg_InformaciónDeSensibilidad(audio);
        float[] volume = sensibilidad.VolumenValor_Fotogramas(FPS, FotogramasTOT);
        sensibilidad.MODO_NODOS = Constantes_DeOnda.CALCULAR_USANDO_ONDAS;
        float[][] wave = sensibilidad.Valores_NodosDeAudio(50, FPS, FotogramasTOT);

        File dir;
        {
            dir = new File(Sistema.ruta_jeffAporta_tmp + "\\Fotogramas " + Matemática.AleatorioEnteroEntre(1, 9999));
        }
        try {
            if (dir.exists()) {
                for (File listFile : dir.listFiles()) {
                    listFile.delete();
                }
                dir.delete();
            }
        } catch (Exception e) {
        }
        dir.mkdirs();

        Etiquetas etiquetas = new Etiquetas(audio, true);
        BufferedImage[] FinalCreditos = Creditos.GenerarCreditos();

        ArrayList<BufferedImage> imagenesGuardar = new ArrayList<>();

        Thread[] GuardarImagenes = new Thread[1];
        int[] c = {1};

        for (int i = 0; i < GuardarImagenes.length; i++) {
            GuardarImagenes[i] = new Thread() {
                {
                    start();
                }

                @Override
                public void run() {
                    while (c[0] < FotogramasTOT) {
                        try {
                            if (!imagenesGuardar.isEmpty()) {
                                File f = new File(
                                        dir.getPath() + new Formatter().format(outFramesFormat, c[0]++)
                                );
                                BufferedImage img = imagenesGuardar.get(0);
                                try {
                                    imagenesGuardar.remove(0);
                                } catch (Exception e) {
                                }
                                ImageIO.write(img, outFramesExtensión, f);
                            } else {
                                Thread.sleep(2000);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            };
        }

//        Particulas particulas = new Particulas(Color.WHITE);
//        particulas.posición = Escenario.Mitad();
        EspectroSonido espectroCircular = new EspectroSonido();
        espectroCircular.posición = Escenario.Mitad();

        BoxBlurFilter boxDesenfoque = new BoxBlurFilter();

        BarraPorcentaje barraPorcentaje = new BarraPorcentaje();

        BufferedImage info;
        {
            GeneradorDeTexto gene = new GeneradorDeTexto()
                    .DistanciaEntreRenglones(0)
                    .ModificarTamañoFuente(20)
                    .ColorFuente(Color.WHITE);
            if (colorbarra != null) {
                gene.Borde(colorbarra, 10);
                Color colorsombra = new Color(
                        colorbarra.getRed() / 3, colorbarra.getGreen() / 3, colorbarra.getBlue() / 3
                );
                barraPorcentaje.coloresRecorrido = new Color[]{colorbarra, colorsombra};
                espectroCircular.colorBorde = colorbarra;
            }
            info = gene.GenerarTexto(
                    etiquetas.TÍTULO.trim().replace("[", "\n[").replace("(", "\n(")
                    + "\n" + etiquetas.ARTISTA.trim()
                    + (extra.equals("") ? "" : "\n" + extra.trim())
            );
        }

        int i = 0;
        for (float frameVolume : volume) {
            BufferedImage img = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB) {
                {
                    getGraphics().setColor(Color.BLACK);
                    getGraphics().fillRect(0, 0, getWidth(), getHeight());
                }
            };
            if (i >= (etiquetas.SEGUNDOS - Creditos.DuraciónCreditos) * FPS && etiquetas.SEGUNDOS > 20) {
                if (i < (etiquetas.SEGUNDOS - Creditos.DuraciónCreditos / 2) * FPS) {
                    img = FinalCreditos[0];
                } else {
                    img = FinalCreditos[1];
                }
            } else {
                try {
                    Graphics2D g = img.createGraphics();
                    {
                        BufferedImage fondoframe = Dupla.THD_1280x720.convBufferedImage();
                        Graphics2D g2 = fondoframe.createGraphics();
                        g2.drawImage(fondo[0], 0, 0, null);
                        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, frameVolume));
                        g2.drawImage(fondo[1], 0, 0, null);
                        g.drawImage(fondoframe, 0, 0, null);
                    }

//                    particulas.incrementador_VelocidadNacimientoParticulas = frameVolume * 10;
//                    g.drawImage(particulas.getSkin(), 0, 0, null);
                    g.drawImage(info, 10, img.getHeight() - info.getHeight() - 10, null);

                    espectroCircular.frecuencias = wave[i];
                    {
                        BufferedImage espectro = Escenario.convBufferedImage();

                        barraPorcentaje.grosor = (int) (10 * frameVolume + 3);
                        barraPorcentaje.porcentaje = (float) i / FotogramasTOT;
                        {
                            BufferedImage waveframe = ImageIO.read(WaveFrames[i]);
                            espectro.getGraphics().drawImage(waveframe, 0, 0, null);
                        }
                        g.drawImage(barraPorcentaje.getSkin(), 0, 0, null);
                        espectro.getGraphics().drawImage(barraPorcentaje.getSkin(), 0, 0, null);
                        espectro.getGraphics().drawImage(espectroCircular.getSkin(), 0, 0, null);

                        boxDesenfoque.setVRadius(frameVolume * 90);
                        boxDesenfoque.setHRadius(frameVolume * 15);
                        BufferedImage desenfoque = boxDesenfoque.filter(espectro, null);
                        g.setComposite(new RGB_Aditivo());
                        g.drawImage(espectro, 0, 0, null);
                        g.drawImage(desenfoque, 0, 0, null);

                        desenfoque = boxDesenfoque.filter(desenfoque, null);
                        g.drawImage(desenfoque, 0, 0, null);

                        g.setComposite(AlphaComposite.SrcOver);
                    }

                    {
                        float escala = 1 + frameVolume * .4f;
                        AffineTransform at = new AffineTransform();
                        Dupla pos = Dupla.Alinear(img, new Dupla(LogoCentral).Escalar(escala), Dupla.MEDIO, Dupla.MEDIO);
                        at.translate(pos.X, pos.Y);
                        at.scale(escala, escala);
                        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g.drawImage(LogoCentral, at, null);
                    }
                } catch (Exception e) {
                    continue;
                }
            }

            ventana.ActualizarFotograma(img);
            BufferedImage guardar = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
            guardar.getGraphics().drawImage(img, 0, 0, null);
            imagenesGuardar.add(guardar);
            while (imagenesGuardar.size() > 100) {
                System.out.println("Demasiados fotogramas en cola: " + imagenesGuardar.size());
                Thread.sleep(1000);
            }

            if (i++ % (FPS * 2) == 0) {
                System.out.println("Fotogramas generados: " + (i - 1) + " de " + FotogramasTOT
                        + "\nen cola: " + imagenesGuardar.size() + " " + videoSalida);
            }
        }
        try {
            if (CarpetaOndas.exists()) {
                for (File listFile : CarpetaOndas.listFiles()) {
                    listFile.delete();
                }
                CarpetaOndas.delete();
            }
        } catch (Exception e) {
        }
        boolean esperar = true;
        while (esperar) {
            esperar = false;
            for (Thread GuardarImagene : GuardarImagenes) {
                esperar |= GuardarImagene.isAlive();
            }
            System.out.println("Esperando la finalización del guardado " + c[0]);
            if (imagenesGuardar.isEmpty()) {
                for (Thread GuardarImagene : GuardarImagenes) {
                    GuardarImagene.stop();
                }
            }
            Thread.sleep(1000);
        }

        Renderizar(dir, audio, videoSalida, FPS, FinishAndExit, ventana);
    }

    public static void SimpleConCronometro(final String audio, final String videoSalida, String extra, VentanaGráfica ventana, Color colorbarra, BufferedImage fondo, BufferedImage LogoCentral) throws Exception {
//        {
//            Graphics2D g = fondo.createGraphics();
//            g.drawImage(
//                    new GeneradorDeTexto()
//                            .ModificarTamañoFuente(30)
//                            .ColorFuente(new Color(255, 255, 255, 200))
//                            .Borde(Color.black, 5)
//                            .DistanciaEntreRenglones(0)
//                            .GenerarTexto("Poner el video en velocidad x2 | Put the video up to speed x2\nPara mejor experiencia link de descarga en la descripción\nSuscribete"),
//                    0,
//                    0,
//                    null
//            );
//        }
        boolean FinishAndExit = false;
        int FPS = 3;
        if (ventana == null) {
            FinishAndExit = true;
            ventana = new VentanaGráfica("ventana");
        }
        ObjetoDibujable.FPS = FPS;
        Etiquetas etiquetas = new Etiquetas(audio, true);
        if (etiquetas.SEGUNDOS > 240) {
            etiquetas.SEGUNDOS = 240;
        }
        final int FotogramasTOT = (int) (etiquetas.SEGUNDOS * FPS);
        File dir = new File(Sistema.ruta_jeffAporta_tmp + "\\Fotogramas " + Matemática.AleatorioEnteroEntre(1, 9999));
        try {
            if (dir.exists()) {
                for (File listFile : dir.listFiles()) {
                    listFile.delete();
                }
                dir.delete();
            }
        } catch (Exception e) {
        }
        dir.mkdirs();

        BufferedImage[] FinalCreditos = Creditos.GenerarCreditos();

        ArrayList<BufferedImage> imagenesGuardar = new ArrayList<>();

        Thread[] GuardarImagenes = new Thread[1];
        int[] c = {1};

        for (int i = 0; i < GuardarImagenes.length; i++) {
            GuardarImagenes[i] = new Thread() {
                {
                    start();
                }

                @Override
                public void run() {
                    while (c[0] <= FotogramasTOT) {
                        try {
                            if (!imagenesGuardar.isEmpty()) {
                                BufferedImage img = imagenesGuardar.get(0);
                                try {
                                    imagenesGuardar.remove(0);
                                } catch (Exception e) {
                                }
                                File archivo = new File(
                                        dir.getPath() + new Formatter().format(outFramesFormat, c[0]++)
                                );
                                ImageIO.write(img, outFramesExtensión, archivo);
                                while (img == imagenesGuardar.get(0)) {
                                    imagenesGuardar.remove(0);
                                    File copia = new File(
                                            dir.getPath() + new Formatter().format(outFramesFormat, c[0]++)
                                    );
                                    Sistema.DuplicarArchivo(archivo, copia);
                                }
                            } else {
                                Thread.sleep(2000);
                            }
                        } catch (Exception ex) {
                        }
                    }
                }
            };
        }
        GeneradorDeTexto gene = new GeneradorDeTexto()
                .DistanciaEntreRenglones(0)
                .ModificarTamañoFuente(40)
                .ColorFuente(Color.WHITE);
        if (colorbarra != null) {
            gene.Borde(colorbarra, 10);
        }

        int máxFotogramasEnCola;
        {
            int h = GeneradorDeSencillos.hilos;
            máxFotogramasEnCola = 200 * FPS / h;
        }
        BufferedImage DescargaEnLaDescripción = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB) {
            {
                Graphics2D g = createGraphics();
                BufferedImage txt = gene.GenerarTexto(
                        "%img%"
                        + "\n\n"
                        + "Descarga el álbum en la descripción"
                        + "\n"
                        + "Download the album in the description",
                        añadir_biblioteca_appdata_img(
                                "Siluetas",
                                "https://docs.google.com/drawings/d/e/2PACX-1vTRBPY-A3vwe1dHPh8xd5bodfLgA9UbHnm1_dXL0i-dJ_10DvNDdIL_xi9W9k0SpmW-BSZD2MP5w_QL/pub?w=512&h=513",
                                "Descarga Sonido.png"
                        )
                );
                Filtros_Lineales.Ajuste_Centrado_Ajustar(txt, this);
            }
        };
        BufferedImage info;
        for (int segundo = 0; segundo < etiquetas.SEGUNDOS; segundo++) {
            BufferedImage img = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB) {
                {
                    getGraphics().setColor(Color.BLACK);
                    getGraphics().fillRect(0, 0, getWidth(), getHeight());
                }
            };
            if (segundo < 10) {
                img = DescargaEnLaDescripción;
//            } else if (segundo >= (etiquetas.SEGUNDOS - Creditos.DuraciónCreditos) && etiquetas.SEGUNDOS > 30) {
//                if (segundo < (etiquetas.SEGUNDOS - Creditos.DuraciónCreditos / 2)) {
//                    img = FinalCreditos[0];
//                } else {
//                    img = FinalCreditos[1];
//                }
            } else {
                try {
                    Ajuste_Rellenar(fondo, img);
                    BufferedImage banner = new BufferedImage((int) (img.getWidth() * .85), LogoCentral.getHeight(), 2);
                    info = gene.GenerarTexto(
                            etiquetas.TÍTULO.trim()
                                    .replace("[", "\n[")
                                    .replace("(", "\n(")
                                    .replace("{", "\n{")
                            + "\n\n" + etiquetas.ARTISTA.trim()
                                    .replace("[", "\n[")
                                    .replace("(", "\n(")
                                    .replace("{", "\n{")
                            + (extra.equals("") ? "" : "\n" + extra.trim())
                                    .replace("[", "\n[")
                                    .replace("(", "\n(")
                                    .replace("{", "\n{")
                            + "\n\n" + ConvertirFotogramasATiempo(segundo * FPS, FPS)
                            + "  /  " + ConvertirFotogramasATiempo(FotogramasTOT, FPS)
                    );
                    {
                        int w = banner.getWidth() - LogoCentral.getWidth() - 10;
                        int h = LogoCentral.getHeight();
                        BufferedImage infoAj = new BufferedImage(w, h, 2);
                        Ajuste_Centrado_Ajustar(info, infoAj);
                        info = infoAj;
                    }
                    {
                        Graphics2D g = banner.createGraphics();
                        g.drawImage(LogoCentral, 0, 0, null);
                        g.drawImage(info, banner.getWidth() - info.getWidth(), 0, null);
                    }
                    Ajuste_Centrado_Ajustar(banner, img);
                } catch (Exception e) {
                    continue;
                }
            }

            ventana.ActualizarFotograma(img);
            BufferedImage guardar = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
            guardar.getGraphics().drawImage(img, 0, 0, null);
            for (int j = 0; j < FPS; j++) {
                imagenesGuardar.add(guardar);
            }
            while (imagenesGuardar.size() > máxFotogramasEnCola) {
                float n1 = imagenesGuardar.size();
                System.out.println("Demasiados fotogramas en cola: " + imagenesGuardar.size());
                Thread.sleep(1000);
                float n2 = imagenesGuardar.size();
                System.out.println("Velocidad: " + ((n1 - n2)) + " por segundo");
            }
            if (segundo % (100) == 0) {
                System.out.println("Fotogramas generados: " + (segundo) + " de " + FotogramasTOT
                        + "\nen cola: " + imagenesGuardar.size() + " " + videoSalida);
            }
        }
        boolean esperar = true;
        while (esperar) {
            esperar = false;
            for (Thread GuardarImagene : GuardarImagenes) {
                esperar |= GuardarImagene.isAlive();
            }
            float n1 = imagenesGuardar.size();
            System.out.println("Quedan " + imagenesGuardar.size() + " fotogramas por guardar");
            ventana.MostrarTexto("Quedan " + imagenesGuardar.size() + " fotogramas");
            if (imagenesGuardar.isEmpty()) {
                for (Thread GuardarImagene : GuardarImagenes) {
                    GuardarImagene.stop();
                }
            }
            Thread.sleep(1000);
            float n2 = imagenesGuardar.size();
            System.out.println("Velocidad: " + ((n1 - n2)) + " por segundo");
        }

        ventana.MostrarTexto(
                (total - GeneradorDeSencillos.ArchivosMp3.size()) + " de " + total
                + "\n" + (100f * actual / total) + "%"
        );
        Renderizar(dir, audio, videoSalida, FPS, FinishAndExit, ventana);
    }

    public static void SimpleSinCronometro(final String audio, final String videoSalida, String extra, VentanaGráfica ventana, Color colorbarra, BufferedImage fondo, BufferedImage LogoCentral) throws Exception {
        boolean FinishAndExit = false;
        int FPS = 3;
        if (ventana == null) {
            FinishAndExit = true;
            ventana = new VentanaGráfica("ventana");
        }
        ObjetoDibujable.FPS = FPS;
        Etiquetas etiquetas = new Etiquetas(audio, true);
//        if (etiquetas.SEGUNDOS > 240) {
//            etiquetas.SEGUNDOS = 240;
//        }
        final int FotogramasTOT = (int) (etiquetas.SEGUNDOS * FPS);
        File dir = new File(Sistema.ruta_jeffAporta_tmp + "\\Fotogramas " + Matemática.AleatorioEnteroEntre(1, 9999));
        try {
            if (dir.exists()) {
                for (File listFile : dir.listFiles()) {
                    listFile.delete();
                }
                dir.delete();
            }
        } catch (Exception e) {
        }
        dir.mkdirs();

        BufferedImage[] FinalCreditos = Creditos.GenerarCreditos();

        ArrayList<BufferedImage> imagenesGuardar = new ArrayList<>();

        Thread[] GuardarImagenes = new Thread[1];
        int[] c = {1};

        for (int i = 0; i < GuardarImagenes.length; i++) {
            GuardarImagenes[i] = new Thread() {
                {
                    start();
                }

                @Override
                public void run() {
                    while (c[0] <= FotogramasTOT) {
                        try {
                            if (!imagenesGuardar.isEmpty()) {
                                BufferedImage img = imagenesGuardar.get(0);
                                try {
                                    imagenesGuardar.remove(0);
                                } catch (Exception e) {
                                }
                                File archivo = new File(
                                        dir.getPath() + new Formatter().format(outFramesFormat, c[0]++)
                                );
                                ImageIO.write(img, outFramesExtensión, archivo);
                                while (img == imagenesGuardar.get(0)) {
                                    imagenesGuardar.remove(0);
                                    File copia = new File(
                                            dir.getPath() + new Formatter().format(outFramesFormat, c[0]++)
                                    );
                                    Sistema.DuplicarArchivo(archivo, copia);
                                }
                            } else {
                                Thread.sleep(2000);
                            }
                        } catch (Exception ex) {
                        }
                    }
                }
            };
        }
        GeneradorDeTexto gene = new GeneradorDeTexto()
                .DistanciaEntreRenglones(0)
                .ModificarTamañoFuente(40)
                .ColorFuente(Color.WHITE);
        if (colorbarra != null) {
            gene.Borde(colorbarra, 10);
        }

        int máxFotogramasEnCola;
        {
            int h = GeneradorDeSencillos.hilos;
            máxFotogramasEnCola = 200 * FPS / h;
        }
        BufferedImage DescargaEnLaDescripción = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB) {
            {
                Graphics2D g = createGraphics();
                BufferedImage txt = gene.GenerarTexto(
                        "%img%"
                        + "\n\n"
                        + "Descarga el álbum en la descripción"
                        + "\n"
                        + "Download the album in the description",
                        añadir_biblioteca_appdata_img(
                                "Siluetas",
                                "https://docs.google.com/drawings/d/e/2PACX-1vTRBPY-A3vwe1dHPh8xd5bodfLgA9UbHnm1_dXL0i-dJ_10DvNDdIL_xi9W9k0SpmW-BSZD2MP5w_QL/pub?w=512&h=513",
                                "Descarga Sonido.png"
                        )
                );
                Filtros_Lineales.Ajuste_Centrado_Ajustar(txt, this);
            }
        };

        BufferedImage  cover = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB) {
            {
                getGraphics().setColor(Color.BLACK);
                getGraphics().fillRect(0, 0, getWidth(), getHeight());
            }
        };
        Ajuste_Rellenar(fondo,  cover);
        BufferedImage banner = new BufferedImage((int) ( cover.getWidth() * .85), LogoCentral.getHeight(), 2);
        BufferedImage info = gene.GenerarTexto(
                etiquetas.TÍTULO.trim()
                        .replace("[", "\n[")
                        .replace("(", "\n(")
                        .replace("{", "\n{")
                + "\n\n" + etiquetas.ARTISTA.trim()
                        .replace("[", "\n[")
                        .replace("(", "\n(")
                        .replace("{", "\n{")
                + (extra.equals("") ? "" : "\n" + extra.trim())
                        .replace("[", "\n[")
                        .replace("(", "\n(")
                        .replace("{", "\n{")
        );
        {
            int w = banner.getWidth() - LogoCentral.getWidth() - 10;
            int h = LogoCentral.getHeight();
            BufferedImage infoAj = new BufferedImage(w, h, 2);
            Ajuste_Centrado_Ajustar(info, infoAj);
            info = infoAj;
        }
        {
            Graphics2D g = banner.createGraphics();
            g.drawImage(LogoCentral, 0, 0, null);
            g.drawImage(info, banner.getWidth() - info.getWidth(), 0, null);
        }
        Ajuste_Centrado_Ajustar(banner,  cover);
        for (int segundo = 0; segundo < etiquetas.SEGUNDOS; segundo++) {
            BufferedImage img = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB) {
                {
                    getGraphics().setColor(Color.BLACK);
                    getGraphics().fillRect(0, 0, getWidth(), getHeight());
                }
            };
            if (segundo < 10) {
                img = DescargaEnLaDescripción;
            } else {
                img = cover;
            }

            ventana.ActualizarFotograma(img);
            for (int j = 0; j < FPS; j++) {
                imagenesGuardar.add(img);
            }
        }
        boolean esperar = true;
        while (esperar) {
            esperar = false;
            for (Thread GuardarImagene : GuardarImagenes) {
                esperar |= GuardarImagene.isAlive();
            }
            float n1 = imagenesGuardar.size();
            System.out.println("Quedan " + imagenesGuardar.size() + " fotogramas por guardar");
            ventana.MostrarTexto("Quedan " + imagenesGuardar.size() + " fotogramas");
            if (imagenesGuardar.isEmpty()) {
                for (Thread GuardarImagene : GuardarImagenes) {
                    GuardarImagene.stop();
                }
            }
            Thread.sleep(1000);
            float n2 = imagenesGuardar.size();
            System.out.println("Velocidad: " + ((n1 - n2)) + " por segundo");
        }

        ventana.MostrarTexto(
                (total - GeneradorDeSencillos.ArchivosMp3.size()) + " de " + total
                + "\n" + (100f * actual / total) + "%"
        );
        Renderizar(dir, audio, videoSalida, FPS, FinishAndExit, ventana);
    }

    public static void Renderizar(File dir, String audio, String videoSalida, int FPS, boolean finish, VentanaGráfica ventana) {
        if (dir.listFiles().length == 0) {
            System.out.println("No hay fotogramas para renderizar");
            dir.delete();
            return;
        }
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    FFmpeg FFmpeg = new FFmpeg(dir.getPath() + outFramesFormat, videoSalida);
                    FFmpeg.HacerVideoConSecuenciaDeImagenesYAudio(audio, FPS, FPS >= 5);
                    try {
                        for (int j = 0; j < 2; j++) {
                            for (File listFile : dir.listFiles()) {
                                listFile.delete();
                            }
                        }
                    } catch (Exception ex) {
                    }
                    dir.delete();
                    if (finish) {
                        ventana.dispose();
                    }
                } catch (Exception ex) {
                }
            }
        };
        t.start();
        long tiempo_inicio = System.currentTimeMillis();
        while (t.isAlive()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            double s_transcurrido = (System.currentTimeMillis() - tiempo_inicio) / 1000;
            System.out.println("Esperando el proceso combinación " + s_transcurrido);
            if (s_transcurrido > 120) {
                break;
            }
        }
    }

    static String ConvertirFotogramasATiempo(int fotograma, int FPS) {
        int s = (fotograma / FPS) % 60;
        int m = (fotograma / FPS / 60) % 60;
        int h = (fotograma / FPS / 60 / 60) % 60;
        return (h < 10 ? "0" : "") + h + ":" + (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s;
    }
}
