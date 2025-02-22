package facturacióndulceria;

import HerramientaArchivos.CarpetaDeRecursos;
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientaDeImagen.Filtros_Lineales;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientasMatemáticas.Dupla;
import HerramientasSistema.Sistema;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GeneradorDePDF {

    static int ANCHO_HOJA = (int) PDRectangle.LETTER.getWidth();
    static int ALTO_HOJA = (int) PDRectangle.LETTER.getHeight();

    static int ANCHO_CELDA = (int) (ANCHO_HOJA * 0.5 - 20);
    static int ALTO_CELDA = 180;
    static int COLUMNAS = 2;
    static int FILAS = 4;

    static CarpetaDeRecursos carpetaPDF = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/PDF");

    static CarpetaDeRecursos carpetaImagenesProductos = new CarpetaDeRecursos(CarpetaDeRecursos.TIPO_APPDATA, "Dulceria facturación/Imagenes productos");
    static File[] archivosImagenes = carpetaImagenesProductos.ListarArchivos();

    static int contadorImg = 0;

    static boolean Trabajando = false;

    public static void main(String[] args) throws Exception {
        if (Trabajando) {
            return;
        }
        Trabajando = true;
        carpetaPDF.diasActualizarArchivos(1);
        try (PDDocument document = new PDDocument()) {
            for (int i = 0; i < archivosImagenes.length / 6.0; i++) {
                if (contadorImg>=archivosImagenes.length) {
                    continue;
                }
                if (archivosImagenes[contadorImg].length() == 0) {
                    contadorImg++;
                    continue;
                }
                BufferedImage img = new BufferedImage(ANCHO_HOJA, ALTO_HOJA, 2) {
                    {
                        Graphics2D g = createGraphics();
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 0, getWidth(), getHeight());
                        g.setColor(new Color(255, 255, 255, 60));
                        g.fillRect(0, 0, ANCHO_HOJA, ALTO_HOJA);
                        BufferedImage cuadricula = new BufferedImage(COLUMNAS * ANCHO_CELDA, FILAS * ALTO_CELDA, 2) {
                            {
                                Graphics2D g = createGraphics();
                                g.setColor(Color.black);
                                for (int j = 0; j < FILAS; j++) {
                                    for (int i = 0; i < COLUMNAS; i++) {
                                        int x = i * ANCHO_CELDA;
                                        int y = j * ALTO_CELDA;
                                        if (contadorImg >= archivosImagenes.length) {
                                            continue;
                                        }
                                        String ruta = archivosImagenes[contadorImg].getPath();
                                        String titulo = archivosImagenes[contadorImg].getName();
                                        if (titulo.contains("default")) {
                                            contadorImg++;
                                            i--;
                                            continue;
                                        }
                                        BufferedImage img = LectoEscrituraArchivos.cargar_imagen(ruta);
                                        g.drawImage(imagenProducto(titulo, img), x, y, null);
                                    }
                                }
                            }
                        };
                        Dupla c = Dupla.Alinear(this, cuadricula, Dupla.MEDIO, Dupla.MEDIO);
                        g.drawImage(cuadricula, c.intX(), c.intY(), null);
                    }

                    BufferedImage imagenProducto(String titulo, BufferedImage img) {
                        if (img == null) {
                            img = new BufferedImage(1, 1, 2);
                        }
                        img = Filtros_Lineales.EscalarGranCalidad(
                                img, -1, 150
                        );
                        if (img.getWidth() > 150) {
                            img = Filtros_Lineales.EscalarGranCalidad(
                                    img, 150, -1
                            );
                        }
                        final BufferedImage imgProducto = img;

                        BufferedImage producto = new BufferedImage(ANCHO_CELDA, ALTO_CELDA, 2) {
                            {
                                BufferedImage imgcolorPrincipal = Filtros_Lineales.EscalarGranCalidad(imgProducto, 1, 1);
                                Color colorPrincipal = new Color(imgcolorPrincipal.getRGB(0, 0));
                                {
                                    int r = colorPrincipal.getRed();
                                    int g = colorPrincipal.getGreen();
                                    int b = colorPrincipal.getBlue();
                                    float[] hsb = Color.RGBtoHSB(r, g, b, null);
                                    colorPrincipal = new Color(Color.HSBtoRGB(hsb[0], 1, hsb[2] * 0.8f));
                                }
                                GeneradorDeTexto gdt = new GeneradorDeTexto()
                                        .ModificarTamañoFuente(18)
                                        .AtributoNormal_Plano()
                                        .ColorFuente(Color.WHITE)
                                        .Borde(colorPrincipal, 5)
                                        .AlineaciónHorizontal(Dupla.DERECHA)
                                        .DistanciaEntreRenglones(0);
                                Graphics2D g = createGraphics();
                                Dupla c = Dupla.Alinear(this, imgProducto, Dupla.MEDIO, Dupla.MEDIO);
                                g.drawImage(imgProducto, 0, c.intY(), null);
                                BufferedImage txt = gdt.GenerarTexto(
                                        gdt.transformarTextoConRenglonesAuto(
                                                titulo.replace(".jpg", "").replace(".png", ""), ANCHO_CELDA - imgProducto.getWidth()
                                        ).replace("×", "\n×").replace("\n\n", "\n")
                                );
                                g.drawImage(txt, ANCHO_CELDA - txt.getWidth(), c.intY(), null);
                            }
                        };
                        contadorImg++;
                        mostrarEstado("Se ingresó al PDF el producto: " + contadorImg);
                        return producto;
                    }

                };
                mostrarEstado("Agregando Hoja al PDF: " + (int) Math.ceil(contadorImg / 6.0) + "/" + (int) Math.ceil(archivosImagenes.length / 6.0));
                añadirHojaImagen(document, img);
            }
            String ruta = System.getProperty("user.home") + File.separatorChar + "OneDrive\\Documentos" + "\\Catalogo Dulceria.pdf";
            System.out.println(ruta);
            document.save(ruta);
            Sistema.AbrirRecursoDelComputador(ruta);
        }
        mostrarEstado("Nueva Hoja agregada al PDF: " + (int) Math.ceil(contadorImg / 6.0));
        contadorImg = 0;
        Trabajando = false;
    }

    static boolean tieneTransparencia(BufferedImage img) {
        try {
            boolean tieneTransparencia = false;
            for (int j = 0; j < img.getWidth(); j++) {
                for (int k = 0; k < img.getHeight(); k++) {
                    Color color = new Color(img.getRGB(j, k), true);
                    if (color.getAlpha() < 255) {
                        tieneTransparencia = true;
                        break;
                    }
                }
                if (tieneTransparencia) {
                    break;
                }
            }
            return tieneTransparencia;
        } catch (Exception e) {
        }
        return false;
    }

    static BufferedImage prepararImagenProducto(BufferedImage img) {
        if (img == null) {
            return null;
        }
        BufferedImage sinFondo = new BufferedImage(img.getWidth(), img.getHeight(), 2);
        Filtros_Lineales.Ajuste_Ajustar(trim(img), sinFondo);
        if (!tieneTransparencia(img)) {
            eliminarBlanco(sinFondo);
        }
        BufferedImage recortada = new BufferedImage(sinFondo.getWidth(), sinFondo.getHeight(), 2);
        Filtros_Lineales.Ajuste_Ajustar(trim(sinFondo), recortada);
        return trim(recortada);
    }

    static BufferedImage trim(BufferedImage entrada) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (int i = 0; i < entrada.getWidth(); i++) {
            for (int j = 0; j < entrada.getHeight(); j++) {
                Color c = new Color(entrada.getRGB(i, j), true);
                if (c.getAlpha() < 255) {
                    continue;
                }
                minX = Math.min(i, minX);
                minY = Math.min(j, minY);
                maxX = Math.max(i, maxX);
                maxY = Math.max(j, maxY);
            }
        }
        final int x = minX;
        final int y = minY;
        final int w = maxX - minX;
        final int h = maxY - minY;

        return new BufferedImage(w, h, 2) {
            {
                BufferedImage crop = entrada.getSubimage(x, y, w, h);
                Graphics2D g = createGraphics();
                g.drawImage(crop, 0, 0, null);
            }
        };
    }

    static void eliminarBlanco(BufferedImage img) {
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color c = new Color(img.getRGB(i, j));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                int d = (int) Math.sqrt(Math.pow(255 - r, 2) + Math.pow(255 - g, 2) + Math.pow(255 - b, 2));
                double radio = 20;
                if (d <= radio) {
                    int t = (int) (255 * d / radio);
                    img.setRGB(i, j, new Color(255, 255, 255, t).getRGB());
                }
                img.setRGB(i, j, img.getRGB(i, j));
            }
        }
    }

    private static void mostrarEstado(String txt) {
        if (GUI.ventanaPrincipal != null) {
            GUI.ventanaPrincipal.setTitle(txt);
        }
        System.out.println(txt);
    }

    static void añadirHojaImagen(PDDocument document, BufferedImage img) throws Exception {
        PDPage page = new PDPage(PDRectangle.LETTER);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Image
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", bos);
        byte[] data = bos.toByteArray();
        PDImageXObject image = PDImageXObject.createFromByteArray(document, data, Math.random() + "");
        contentStream.drawImage(image, 0, ALTO_HOJA - image.getHeight(), image.getWidth(), image.getHeight());

        contentStream.close();
    }

}
