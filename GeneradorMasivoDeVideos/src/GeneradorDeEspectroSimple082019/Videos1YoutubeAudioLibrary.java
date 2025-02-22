package GeneradorDeEspectroSimple082019;

import HerramientaDeImagen.Filtros_Lineales;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientasGUI.VentanaGráfica;
import HerramientasMatemáticas.Dupla;
import HerramientasMatemáticas.Matemática;
import HerramientasSistema.Sistema;
import JAVH.Constantes_DeOnda;
import JAVH.FFmpeg;
import JAVH.FFmpeg_InformaciónDeSensibilidad;
import JID3.Etiquetas;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import javax.imageio.ImageIO;

public class Videos1YoutubeAudioLibrary {

    public static final String outFramesFormat = "\\%07d.jpg";
    public static final String outFramesExtensión = outFramesFormat.substring(
            outFramesFormat.lastIndexOf('.') + 1
    );

    static final int FPS = 12;
    static int alturaCentralDibujo = 0;
    static int desplazamientoVerticalDibujo = 0;

    static VentanaGráfica ventana = new VentanaGráfica();

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
                    continue;
                }
                if (new File(
                        rutaSalida
                                .replace("-", "—")
                                .replace("(", "{")
                                .replace(")", "}")
                                .replace("[", "{")
                                .replace("]", "}")
                ).exists()) {
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
            int[] ContadorDeFotogramasGuardados = {1};

            {
                start();
            }

            @Override
            public void run() {//<editor-fold defaultstate="collapsed" desc="Hilo para guardar los fotogramas generados »">
                while (ContadorDeFotogramasGuardados[0] <= FotogramasTotales) {
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
                                            ContadorDeFotogramasGuardados[0]++
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
        };//</editor-fold>

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
                        {
                            //<editor-fold defaultstate="collapsed" desc="Color de fondo">
                            Color colorFondo = new Color(Color.HSBtoRGB((float) Math.random(), .57f, .79f));
                            g.setColor(colorFondo);
                            g.fillRect(0, 0, getWidth(), getHeight());
                            //</editor-fold>
                        }
                        {
                            //<editor-fold defaultstate="collapsed" desc="Texturizado del fondo »">
                            {
                                //<editor-fold defaultstate="collapsed" desc="Cuadricula de fondo »">
                                g.setColor(new Color(0x33000000, true));
                                int w = 10;
                                int h = 10;
                                for (int i = 0; i < getWidth() / w; i++) {
                                    for (int j = 0; j < getHeight() / h; j++) {
                                        if ((i + j) % 2 == 0) {
                                            g.fillRect(i * w, j * h, w, h);
                                        }
                                    }
                                }
                                //</editor-fold>
                            }
                            {
                                //<editor-fold defaultstate="collapsed" desc="Sombra vertical »">
                                g.setPaint(
                                        new GradientPaint(
                                                0,
                                                0,
                                                new Color(0, true),
                                                0,
                                                getHeight(),
                                                new Color(0x99000000, true)
                                        )
                                );
                                g.fillRect(0, 0, getWidth(), getHeight());
                                //</editor-fold>
                            }
                            //</editor-fold>
                        }
                        //</editor-fold>
                    }

                    GeneradorDeTexto renderizadorDeTexto = new GeneradorDeTexto()
                            .AlineaciónHorizontal(Dupla.MEDIO)
                            .Borde(Color.WHITE, 6)
                            .DistanciaEntreRenglones(0)
                            .ModificarTamañoFuente(70)
                            .ColorFuente(Color.BLACK);

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

                    g.setColor(Color.BLACK);
                    int diametro = 400;
                    desplazamientoVerticalDibujo = textoSuperior.getHeight();
                    alturaCentralDibujo = (getHeight() - textoSuperior.getHeight() - textoInferior.getHeight()) / 2;
                    int h = alturaCentralDibujo - diametro / 2 + desplazamientoVerticalDibujo;
                    g.fillOval(
                            (getWidth() - diametro) / 2,
                            h,
                            diametro,
                            diametro
                    );

                }
            };//</editor-fold>

            ventana.ActualizarFotograma(imagenFondo);

            float[][] nodosOndas;
            {
                //<editor-fold defaultstate="collapsed" desc="Extracción de los nodos de ondas »">
                FFmpeg_InformaciónDeSensibilidad sensibilidad = new FFmpeg_InformaciónDeSensibilidad(rutaAudio);
                sensibilidad.MODO_NODOS = Constantes_DeOnda.CALCULAR_USANDO_ONDAS;
                nodosOndas = sensibilidad.Valores_NodosDeAudio(10, FPS, FotogramasTotales);
                //</editor-fold>
            }

            for (int nFotograma = 0; nFotograma < FotogramasTotales; nFotograma++) {
                //<editor-fold defaultstate="collapsed" desc="Generación de los fotogramas »">
                final int n = nFotograma;

                BufferedImage Espectro = new BufferedImage(
                        300, 200, 2
                ) {//<editor-fold defaultstate="collapsed" desc="Dibujado de las barras nodales de onda para el espectro »">
                    {
                        Graphics2D g = createGraphics();
                        g.setColor(Color.WHITE);
                        int anchoBarraNodalOnda = 8;
                        int distanciaNodal = getWidth() / 10;
                        for (int i = 0; i < 10; i++) {
                            int altoBarraNodalOnda = (int) (getHeight() * nodosOndas[n][i]);
                            int x = i * distanciaNodal + (distanciaNodal - anchoBarraNodalOnda) / 2;
                            int y = (getHeight() - altoBarraNodalOnda) / 2;
                            g.fillRect(
                                    x,
                                    y,
                                    anchoBarraNodalOnda,
                                    altoBarraNodalOnda
                            );
                        }
                    }
                };//</editor-fold>

                BufferedImage fotograma = new BufferedImage(
                        imagenFondo.getWidth(), imagenFondo.getHeight(), BufferedImage.TYPE_INT_RGB
                ) {//<editor-fold defaultstate="collapsed" desc="Dibujado del fotograma »">
                    {
                        Graphics2D g = createGraphics();
                        g.drawImage(imagenFondo, 0, 0, null);
                        g.drawImage(
                                Espectro,
                                (imagenFondo.getWidth() - Espectro.getWidth()) / 2,
                                alturaCentralDibujo - Espectro.getHeight() / 2 + desplazamientoVerticalDibujo,
                                null
                        );
                    }
                };//</editor-fold>

                FotogramasGuardar.add(fotograma);
                ventana.ActualizarFotograma(fotograma);
                ventana.setTitle(nFotograma + "/" + FotogramasTotales);

                while (FotogramasGuardar.size() > 20) {
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

        Renderizar(CarpetaFotogramas, rutaAudio, rutaVideoSalida);
    }//</editor-fold>

    public static void Renderizar(final File rutaFotogramas, final String rutaAudio, final String videoSalida) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
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
