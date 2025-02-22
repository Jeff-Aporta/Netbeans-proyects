package GeneradorDeEspectroSimple082019;

import HerramientaDeImagen.Filtro_Convolución;
import HerramientaDeImagen.Filtros_Lineales;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientasGUI.VentanaGráfica;
import HerramientasMatemáticas.Dupla;
import HerramientasMatemáticas.Matemática;
import HerramientasSistema.Sistema;
import JAVH.FFmpeg;
import JAVH.FFmpeg_InformaciónDeSensibilidad;
import JID3.Etiquetas;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Random;
import javax.imageio.ImageIO;

public class Videos2YoutubeAudioLibrary {

    public static final String outFramesFormat = "\\%07d.jpg";
    public static final String outFramesExtensión = outFramesFormat.substring(
            outFramesFormat.lastIndexOf('.') + 1
    );

    static final int FPS = 18;
    static int alturaCentralDibujo = 0;
    static int desplazamientoVerticalDibujo = 0;

    static VentanaGráfica ventana = new VentanaGráfica();

    static BufferedImage Logo = null;

    public static void main(String[] args) throws Exception {
        String carpeta = "C:\\YAL";
        RepetirEnTodosLosMp3(new File(carpeta));
        System.exit(0);
    }

    static void RepetirEnTodosLosMp3(File carpeta) throws Exception {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (File archivo : carpeta.listFiles()) {
            if (archivo.isDirectory()) {//<editor-fold defaultstate="collapsed" desc="Generar busqueda recursiva »">
                RepetirEnTodosLosMp3(archivo);
                continue;
            }//</editor-fold>

            if (!archivo.getPath().endsWith(".mp3")) {//<editor-fold defaultstate="collapsed" desc="Comprobar si la extensión es .mp3 »">
                continue;
            }//</editor-fold>

            String rutaSalida;
            {
                //<editor-fold defaultstate="collapsed" desc="Generar y comprobar ruta de salida »">
                Etiquetas etiquetas = new Etiquetas(archivo.getPath(), false);
                rutaSalida = etiquetas.TÍTULO + " - " + etiquetas.ARTISTA + " - " + etiquetas.GÉNERO;
                if (rutaSalida.length() >= 100) {
                    rutaSalida = rutaSalida.substring(0, 100);
                }
                rutaSalida = LimpiarNombreDeCaracteresNoVálidos(rutaSalida);
                rutaSalida = carpeta.getPath() + "\\" + rutaSalida + ".mp4";
                if (new File(rutaSalida).exists()) {
                    System.err.println("el archivo ya existe: " + rutaSalida);
                    continue;
                }
                String nombreParaSubida = rutaSalida.replace("-", "—").replace("(", "{").replace(")", "}").replace("[", "{").replace("]", "}");
                if (new File(nombreParaSubida).exists()) {
                    System.err.println("el archivo ya existe: " + nombreParaSubida);
                    continue;
                }
                //</editor-fold>
            }
            GenerarSencillo(archivo.getPath(), rutaSalida);
        }
    }//</editor-fold>

    static String LimpiarNombreDeCaracteresNoVálidos(String Nombre) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Nombre = Nombre.replace("'", "");
        Nombre = Nombre.replace("\"", "");
        Nombre = Nombre.replace("/", " ");
        Nombre = Nombre.replace("\\", " ");
        Nombre = Nombre.replace("?", " ");
        Nombre = Nombre.replace("&", "and");
        Nombre = Nombre.replace(":", " ");
        Nombre = Nombre.replace("*", " ");
        Nombre = Nombre.replace("<", " ");
        Nombre = Nombre.replace(">", " ");
        Nombre = Nombre.replace("|", " ");
        return Nombre;
    }//</editor-fold>

    static void GenerarSencillo(final String rutaAudio, final String rutaVideoSalida) throws Exception {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        File CarpetaFotogramas = new File(Sistema.ruta_jeffAporta_appdata + "\\Fotogramas " + Matemática.AleatorioEnteroEntre(1, 9999));
        //<editor-fold defaultstate="collapsed" desc="Eliminación de posible duplicado de la carpeta temporal »">
        try {
            if (CarpetaFotogramas.exists()) {
                for (File listFile : CarpetaFotogramas.listFiles()) {
                    listFile.delete();
                }
                CarpetaFotogramas.delete();
            }
        } catch (Exception e) {
        }
        //</editor-fold>
        CarpetaFotogramas.mkdirs();

        Etiquetas etiquetas = new Etiquetas(rutaAudio, true);

        final int FotogramasTotales = (int) (etiquetas.SEGUNDOS * FPS);
        ArrayList<BufferedImage> FotogramasGuardar = new ArrayList<>();

        new Thread() {//<editor-fold defaultstate="collapsed" desc="Implementación del hilo para guardar los fotogramas generados »">
            int ContadorDeFotogramasGuardados = 1;

            @Override
            public void run() {//<editor-fold defaultstate="collapsed" desc="Hilo para guardar los fotogramas generados »">
                while (ContadorDeFotogramasGuardados <= FotogramasTotales) {
                    try {
                        if (!FotogramasGuardar.isEmpty()) {
                            BufferedImage img = FotogramasGuardar.get(0);
                            try {
                                FotogramasGuardar.remove(0);
                            } catch (Exception e) {
                            }
                            File archivo = new File(
                                    CarpetaFotogramas.getPath()
                                    + new Formatter().format(
                                            outFramesFormat,
                                            ContadorDeFotogramasGuardados++
                                    )
                            );
                            ImageIO.write(img, outFramesExtensión, archivo);
                        } else {
                            Thread.sleep(2000);
                        }
                    } catch (Exception ex) {
                    }
                }
            }//</editor-fold>
        }.start();//</editor-fold>

        {
            //<editor-fold defaultstate="collapsed" desc="Generar secuencia animada para el video »">

            BufferedImage imagenFondo = new BufferedImage(
                    1280,
                    720,
                    BufferedImage.TYPE_INT_ARGB
            ) {//<editor-fold defaultstate="collapsed" desc="Dibujado de la imagen de fondo »">
                {
                    Graphics2D g = createGraphics();
                    {
                        //<editor-fold defaultstate="collapsed" desc="Fondo »">
                        BufferedImage imagen = null;
                        while (imagen == null) {
                            System.err.println("Intentando cargar la imagen de fondo");
                            try {
                                switch (new Random().nextInt(2)) {
                                    case 0:
                                        imagen = ImageIO.read(new URL("http://placeimg.com/1280/720"));
                                        System.err.println("imagen generada con place img");
                                        break;
                                    case 1:
                                        imagen = ImageIO.read(new URL("https://picsum.photos/1280/720?random"));
                                        System.err.println("imagen generada con picsum");
                                        break;
                                    case 2:
                                        imagen = ImageIO.read(new URL("http://lorempixel.com/1280/720/"));
                                        System.err.println("imagen generada con lorem pixel");
                                        break;
                                }
                                if (imagen != null) {
                                    break;
                                }
                            } catch (Exception ex) {
                            }
                            System.err.println("reintentar cargar la imagen de fondo");
                        }
                        ventana.ActualizarFotograma(imagen, "imagen de fondo cargada exitosamente", false);
                        g.drawImage(imagen, 0, 0, null);
                        g.setPaint(
                                new GradientPaint(
                                        0, 0,
                                        new Color(0x99000000, true),
                                        0, getHeight() / 2,
                                        new Color(0x55000000, true),
                                        true
                                )
                        );
                        g.fillRect(0, 0, getWidth(), getHeight());
                        System.err.println("imagen de fondo dibujada");
                        //</editor-fold>
                    }

                    GeneradorDeTexto renderizadorDeTexto = new GeneradorDeTexto()
                            .AlineaciónHorizontal(Dupla.MEDIO)
                            .Borde(Color.BLACK, 18)
                            .DistanciaEntreRenglones(0)
                            .ModificarTamañoFuente(70)
                            .ColorFuente(Color.WHITE);

                    System.out.println(etiquetas);
                    BufferedImage textoSuperior = renderizadorDeTexto.GenerarTexto(
                            etiquetas.TÍTULO + "\n" + etiquetas.ARTISTA
                    );
                    if (textoSuperior.getWidth() > getWidth() * .9) {
                        textoSuperior = Filtros_Lineales.Escalar(textoSuperior, (int) (getWidth() * .9), -1);
                    }

                    renderizadorDeTexto.ModificarTamañoFuente(50);
                    BufferedImage textoInferior = renderizadorDeTexto.GenerarTexto(
                            etiquetas.GÉNERO
                    );

                    //<editor-fold defaultstate="collapsed" desc="Dibujado de los textos »">
                    g.drawImage(
                            textoSuperior,
                            (getWidth() - textoSuperior.getWidth()) / 2,
                            0,
                            null
                    );
                    g.drawImage(
                            textoInferior,
                            (getWidth() - textoInferior.getWidth()) / 2,
                            getHeight() - textoInferior.getHeight(),
                            null
                    );
                    //</editor-fold>

                    desplazamientoVerticalDibujo = textoSuperior.getHeight();
                    alturaCentralDibujo = (getHeight() - textoSuperior.getHeight() - textoInferior.getHeight()) / 2;
                }
            };//</editor-fold>

            ventana.ActualizarFotograma(imagenFondo, "imagen de fondo generada", false);

            float volumen[];
            {
                FFmpeg_InformaciónDeSensibilidad sensibilidad = new FFmpeg_InformaciónDeSensibilidad(rutaAudio);
                volumen = sensibilidad.VolumenValor_Fotogramas(FPS, FotogramasTotales, true);
            }

            while (Logo == null) {//<editor-fold defaultstate="collapsed" desc="Si no hay logo cargado, se carga por primera vez »">
                System.err.println("Intentando cargar la imagen del logo");
                try {
                    Logo = ImageIO.read(new URL("https://yt3.ggpht.com/a/AGF-l79_qi1DGV49BYaixluDwCgkVoZuYX_8xgxaDQ=s900-c-k-c0xffffffff-no-rj-mo"));
                    System.err.println("imagen de logo cargada exitosamente");
                    break;
                } catch (Exception e) {
                }
                System.err.println("reintentar cargar la imagen de logo");
            }//</editor-fold>

            BufferedImage LogoConMascara = new BufferedImage(
                    600, 600, 2
            ) {//<editor-fold defaultstate="collapsed" desc="Logo que crece con el sonido »">
                {
                    Graphics2D g = createGraphics();
                    g.setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON
                    );
                    g.setColor(Color.WHITE);
                    g.fillOval(0, 0, getWidth(), getHeight());
                    g.setComposite(AlphaComposite.SrcAtop);
                    g.drawImage(Logo,
                            AffineTransform.getScaleInstance((double) getWidth() / Logo.getWidth(),
                                    (double) getHeight() / Logo.getHeight()
                            ),
                            null
                    );
                }
            };//</editor-fold>

            ventana.ActualizarFotograma(LogoConMascara, "imagen de logo generada", false);

            for (int nFotograma = 0; nFotograma < FotogramasTotales; nFotograma++) {
                //<editor-fold defaultstate="collapsed" desc="Generación de los fotogramas »">
                final int n = nFotograma;

                BufferedImage fotograma = new BufferedImage(
                        imagenFondo.getWidth(), imagenFondo.getHeight(), BufferedImage.TYPE_INT_RGB
                ) {//<editor-fold defaultstate="collapsed" desc="Dibujado del fotograma »">
                    {

                        BufferedImage imagenFondoTransformada = new BufferedImage(imagenFondo.getWidth(), imagenFondo.getHeight(), 2) {
                            {
                                double i = volumen[n] * .02;
                                double p = 1 + i;
                                AffineTransform at = new AffineTransform();
                                at.translate(-getWidth() * i / 2, -getHeight() * i / 2);
                                at.scale(p, p);
                                int r = (int) (volumen[n] * 15);
                                BufferedImage tmp = Filtro_Convolución.Desenfoque_Vertical(imagenFondo, r);
                                Graphics2D g = createGraphics();
                                g.setRenderingHint(
                                        RenderingHints.KEY_INTERPOLATION,
                                        RenderingHints.VALUE_INTERPOLATION_BILINEAR
                                );
                                g.drawImage(tmp, at, null);
                            }
                        };
                        Graphics2D g = createGraphics();
                        g.setRenderingHint(
                                RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR
                        );
                        g.drawImage(imagenFondoTransformada, 0, 0, null);

                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - volumen[n] * .5f));
                        double escala = .5 * volumen[n] + .5;
                        double wlogo = LogoConMascara.getWidth() * escala;
                        double hlogo = LogoConMascara.getHeight() * escala;
                        double x = (imagenFondo.getWidth() - wlogo) / 2;
                        double y = alturaCentralDibujo - hlogo / 2 + desplazamientoVerticalDibujo;
                        g.drawImage(
                                LogoConMascara,
                                new AffineTransform(
                                        escala, 0,
                                        0, escala,
                                        x, y
                                ),
                                null
                        );
                    }
                };//</editor-fold>

                FotogramasGuardar.add(fotograma);
                ventana.ActualizarFotograma(fotograma);
                ventana.setTitle(nFotograma + "/" + FotogramasTotales);

                while (FotogramasGuardar.size() > 30) {
                    //<editor-fold defaultstate="collapsed" desc="Generar espera cuando se sature de fotogramas a guardar »">
                    Thread.sleep(10);
                    //</editor-fold>
                }

                //</editor-fold>
            }

            while (!FotogramasGuardar.isEmpty()) {
                //<editor-fold defaultstate="collapsed" desc="Generar espera cuando queden fotogramas a guardar »">
                Thread.sleep(10);
                //</editor-fold>
            }
            //</editor-fold>
        }
        System.err.println("inicia la rasterización");
        rasterizar(CarpetaFotogramas, rutaAudio, rutaVideoSalida);
        System.err.println("---------------------------------FINALIZADO");
    }//</editor-fold>

    public static void rasterizar(final File rutaFotogramas, final String rutaAudio, final String videoSalida) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            FFmpeg FFmpeg = new FFmpeg(rutaFotogramas.getPath() + outFramesFormat, videoSalida);
            FFmpeg.HacerVideoConSecuenciaDeImagenesYAudio(rutaAudio, FPS, FPS >= 5);
            //<editor-fold defaultstate="collapsed" desc="Borrar carpeta de fotogramas »">
            try {
                for (int j = 0; j < 2; j++) {
                    for (File listFile : rutaFotogramas.listFiles()) {
                        listFile.delete();
                    }
                }
            } catch (Exception ex) {
            }
            rutaFotogramas.delete();
            //</editor-fold>
        } catch (Exception ex) {
        }
    }//</editor-fold>

}
