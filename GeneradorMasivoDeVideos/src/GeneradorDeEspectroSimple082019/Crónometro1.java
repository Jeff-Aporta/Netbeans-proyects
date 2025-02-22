package GeneradorDeEspectroSimple082019;

import HerramientaDeImagen.GeneradorDeTexto;
import HerramientasGUI.VentanaGráfica;
import HerramientasMatemáticas.Dupla;
import HerramientasMatemáticas.Matemática;
import HerramientasSistema.Sistema;
import JAVH.FFmpeg;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import javax.imageio.ImageIO;

public class Crónometro1 {

    public static final String outFramesFormat = "\\%07d.jpg";
    public static final String outFramesExtensión = outFramesFormat.substring(
            outFramesFormat.lastIndexOf('.') + 1
    );

    static final int FPS = 30;

    static VentanaGráfica ventana = new VentanaGráfica();

    public static void main(String[] args) throws Exception {
        String carpeta = "C:\\YAL";

        for (int i = 1; i < 2; i++) {
            String rutaSalida = carpeta + "\\" + i + (i == 1 ? " minute" : " minutes") + " stopwatch - Cronómetro de " + i + (i == 1 ? " minuto" : " minutos") + " .mp4";
            GenerarSencillo(rutaSalida, i * 60);
        }

        System.exit(0);
    }

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

    static void GenerarSencillo(final String rutaVideoSalida, final double segundos) throws Exception {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        File CarpetaFotogramas = new File(Sistema.ruta_jeffAporta_tmp + "\\Fotogramas " + Matemática.AleatorioEnteroEntre(1, 9999));
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

        final int FotogramasTotales = (int) (segundos * FPS) + 1;
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
                            Color colorFondo = new Color(0x444444);
                            g.setColor(colorFondo);
                            g.fillRect(0, 0, getWidth(), getHeight());
                            //</editor-fold>
                        }
                        {
                            //<editor-fold defaultstate="collapsed" desc="Texturizado del fondo »">
                            {
                                //<editor-fold defaultstate="collapsed" desc="Cuadricula de fondo »">
                                g.setColor(new Color(0x33000000, true));
                                int w = 20;
                                int h = 20;
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

                }
            };//</editor-fold>

            ventana.ActualizarFotograma(imagenFondo);

            GeneradorDeTexto renderizadorDeTexto = new GeneradorDeTexto()
                    .AlineaciónHorizontal(Dupla.MEDIO)
                    .Borde(new Color(0x66ffffff, true), 8)
                    .DistanciaEntreRenglones(0)
                    .ModificarTamañoFuente(100)
                    .ColorFuente(Color.WHITE);

            for (int nFotograma = 0; nFotograma < FotogramasTotales; nFotograma++) {
                //<editor-fold defaultstate="collapsed" desc="Generación de los fotogramas »">
                final int n = nFotograma;
                final double segundosTranscurridos = (double) nFotograma / FPS;

                BufferedImage fotograma = new BufferedImage(
                        imagenFondo.getWidth(), imagenFondo.getHeight(), BufferedImage.TYPE_INT_RGB
                ) {//<editor-fold defaultstate="collapsed" desc="Dibujado del fotograma »">
                    {
                        Graphics2D g = createGraphics();
                        g.drawImage(imagenFondo, 0, 0, null);
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        {
                            //<editor-fold defaultstate="collapsed" desc="Líneas punteadas externas »">
                            int diametro = (int) (getHeight() * .9);
                            g.setStroke(new BasicStroke(
                                    4.0f, // Width
                                    BasicStroke.CAP_SQUARE, // End cap
                                    BasicStroke.JOIN_MITER, // Join style
                                    10.0f, // Miter limit
                                    new float[]{10.0f, 10.0f}, // Dash pattern
                                    1000000 - n// Dash phase
                            ));
                            g.setColor(Color.WHITE);
                            g.drawOval((getWidth() - diametro) / 2, (getHeight() - diametro) / 2, diametro, diametro);
                            //</editor-fold>
                        }
                        {
                            //<editor-fold defaultstate="collapsed" desc="Cuerpo circular central »">
                            int diametro = (int) (getHeight() * .85);

                            g.setPaint(new GradientPaint(
                                    0,
                                    (getHeight() - diametro) / 2,
                                    new Color(0x500055),
                                    0,
                                    (getHeight() + diametro) / 2,
                                    new Color(0x300BBA)
                            ));
                            g.fillOval((getWidth() - diametro) / 2, (getHeight() - diametro) / 2, diametro, diametro);

                            g.setStroke(new BasicStroke((float) (20 + 5 * Math.sin(segundosTranscurridos * (Math.PI / 2)))));
                            g.setColor(Color.WHITE);
                            g.drawOval((getWidth() - diametro) / 2, (getHeight() - diametro) / 2, diametro, diametro);
                            //</editor-fold>
                        }
                        {
                            //<editor-fold defaultstate="collapsed" desc="Manecilla larga de cada 10 segundos »">
                            double s = segundosTranscurridos / 10;
                            int diametro = (int) (getHeight() * .76);

                            int largorAngularCola = 60;//grados

                            double t1 = s * 2 * Math.PI;
                            double t2 = s * 2 * Math.PI - largorAngularCola * Math.PI / 180;
                            int x = (int) (diametro / 2 * Math.cos(t1) + getWidth() / 2);
                            int y = (int) (diametro / 2 * Math.sin(t1) + getHeight() / 2);
                            int x2 = (int) (diametro / 2 * Math.cos(t2) + getWidth() / 2);
                            int y2 = (int) (diametro / 2 * Math.sin(t2) + getHeight() / 2);

                            g.setPaint(new GradientPaint(x, y, new Color(0x00ffff), x2, y2, new Color(0x0000ffff, true)));
                            g.setStroke(new BasicStroke(
                                    20, // Width
                                    BasicStroke.CAP_ROUND, // End cap
                                    BasicStroke.JOIN_MITER // Join style
                            ));
                            g.drawArc(
                                    (getWidth() - diametro) / 2,
                                    (getHeight() - diametro) / 2,
                                    diametro,
                                    diametro,
                                    -(int) (s * 360),
                                    largorAngularCola
                            );
                            //</editor-fold>
                        }
                        {
                            //<editor-fold defaultstate="collapsed" desc="Manecilla corta de cada 1 segundo »">
                            double s = segundosTranscurridos;
                            int diametro = (int) (getHeight() * .68);

                            int largorAngularCola = 60;//grados

                            double t1 = s * 2 * Math.PI;
                            double t2 = s * 2 * Math.PI - largorAngularCola * Math.PI / 180;
                            int x = (int) (diametro / 2 * Math.cos(t1) + getWidth() / 2);
                            int y = (int) (diametro / 2 * Math.sin(t1) + getHeight() / 2);
                            int x2 = (int) (diametro / 2 * Math.cos(t2) + getWidth() / 2);
                            int y2 = (int) (diametro / 2 * Math.sin(t2) + getHeight() / 2);

                            g.setPaint(new GradientPaint(x, y, new Color(0x00AC9D), x2, y2, new Color(0x0000AC9D, true)));
                            g.setStroke(new BasicStroke(5));
                            g.drawArc(
                                    (getWidth() - diametro) / 2,
                                    (getHeight() - diametro) / 2,
                                    diametro,
                                    diametro,
                                    -(int) (s * 360),
                                    largorAngularCola
                            );
                            g.fillOval(x - 6, y - 6, 13, 13);
                            //</editor-fold>
                        }
                        {
                            //<editor-fold defaultstate="collapsed" desc="Texto tiempo crónometro »">
                            String tiempo = Matemática.ConvertirSegundosATiempo(segundosTranscurridos, 2);
                            renderizadorDeTexto.DibujarTextoCentradoEnImagen(this, tiempo);
                            //</editor-fold>
                        }
                        {
                            //<editor-fold defaultstate="collapsed" desc="Rombos laterales giratorios »">
                            int r = 30;
                            int[] x = new int[4];
                            int[] y = new int[4];
                            double rot = (segundosTranscurridos / 20) * (2 * Math.PI);
                            {
                                //<editor-fold defaultstate="collapsed" desc="Rombo giratorio derecha »">
                                for (int i = 0; i < 4; i++) {
                                    double a = 2 * Math.PI / 4;
                                    x[i] = (int) (r * Math.cos(i * a + rot));
                                    y[i] = (int) (r * Math.sin(i * a + rot));
                                }
                                Graphics2D g2 = createGraphics();
                                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                g2.translate((getWidth() + getHeight() * .9) / 2 + 2 * r, getHeight() / 2);
                                g2.fillPolygon(x, y, 4);
                                //</editor-fold>
                            }
                            {
                                //<editor-fold defaultstate="collapsed" desc="Rombo giratorio izquierda »">
                                for (int i = 0; i < 4; i++) {
                                    double a = 2 * Math.PI / 4;
                                    x[i] = (int) (r * Math.cos(i * a - rot));
                                    y[i] = (int) (r * Math.sin(i * a - rot));
                                }
                                Graphics2D g2 = createGraphics();
                                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                g2.translate((getWidth() - getHeight() * .9) / 2 - 2 * r, getHeight() / 2);
                                g2.fillPolygon(x, y, 4);
                                //</editor-fold>
                            }
                            //</editor-fold>
                        }
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

        Renderizar(CarpetaFotogramas, rutaVideoSalida);
    }//</editor-fold>

    public static void Renderizar(final File carpetaFotogramas, final String videoSalida) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            FFmpeg FFmpeg = new FFmpeg(carpetaFotogramas.getPath() + outFramesFormat, videoSalida);
            FFmpeg.HacerVideoConSecuenciaDeImagenes(FPS);
            //<editor-fold defaultstate="collapsed" desc="Borrar carpeta de fotogramas »">
            try {
                for (int j = 0; j < 2; j++) {
                    for (File listFile : carpetaFotogramas.listFiles()) {
                        listFile.delete();
                    }
                }
            } catch (Exception ex) {
            }
            carpetaFotogramas.delete();
            //</editor-fold>
        } catch (Exception ex) {
        }
    }//</editor-fold>

}
