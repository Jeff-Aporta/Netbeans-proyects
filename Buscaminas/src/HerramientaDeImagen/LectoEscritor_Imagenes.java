//Última actualización 25/Septiembre/2017
package HerramientaDeImagen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;

public class LectoEscritor_Imagenes {

    public static void main(String[] args) {
        BufferedImage imagen = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagen.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, imagen.getWidth(), imagen.getHeight());
        g.setColor(Color.YELLOW);
        g.fillOval(0, 0, imagen.getWidth(), imagen.getHeight());

        BufferedImage imagenPrueba = cargarImagen();
        JFrame ventana = new JFrame("Ventana prueba");
        ventana.setSize(imagenPrueba.getWidth(), imagenPrueba.getHeight());
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel presentador = new JLabel();
        presentador.setIcon(new ImageIcon(imagenPrueba));
        ventana.add(presentador);
        ventana.setVisible(true);
        ventana.setLocationRelativeTo(null);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }
        imagenPrueba = cargarImagen(
                "http://red-crucero2.com/news/wp-content/uploads/2017/04/AGUJEO.jpg"
        );
        presentador.setIcon(new ImageIcon(imagenPrueba));
        ventana.setSize(imagenPrueba.getWidth(), imagenPrueba.getHeight());
        ventana.setLocationRelativeTo(null);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
        }
        imagenPrueba = cargarImagen(
                "http://3.bp.blogspot.com/_RjFF6Un9L68/TU3Jb1l_VOI/AAAAAAAAAZU/OKmNKQnP49k/s1600/%25D8%"
                + "25AE%25D9%2584%25D9%2581%25D9%258A%25D8%25A7%25D8%25AA+2.jpg"
                + "");
        presentador.setIcon(new ImageIcon(imagenPrueba));
        ventana.setSize(imagenPrueba.getWidth(), imagenPrueba.getHeight());
        ventana.setLocationRelativeTo(null);
    }

    public static BufferedImage cargarImagen() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Seleccione una imagen - Jeff Aporta");
        selector.showOpenDialog(null);
        File Archivo = AgregarExtensión(selector.getSelectedFile(), ".png");
        BufferedImage imagen = null;
        try {
            imagen = ImageIO.read(Archivo);
        } catch (IOException ex) {
        }
        return imagen;
    }

    public static BufferedImage cargarImagen(Object o) {
        if (o instanceof File) {
            return cargarImagen(((File) o).getPath());
        } else if (o instanceof URL) {
            return cargarImagen(((URL) o).getPath());
        } else {
            System.err.println("No se reconoce el objeto para la carga de la imagen");
        }
        return null;
    }

    public static BufferedImage cargarImagen(String dirección) {
        BufferedImage imagen = null;
        try {//intentar con lo más frecuente, buscar de manera directa en los recursos
            imagen = ImageIO.read(ClassLoader.class.getResource(dirección));
            return ConvImgARGB(imagen);
        } catch (Exception e) {
        }
        try {//buscar de manera directa en los recursos, en una carpeta agregada como recurso
            imagen = ImageIO.read(ClassLoader.class.getResource("/" + dirección));
            return ConvImgARGB(imagen);
        } catch (Exception e2) {
        }
        try {
            //Si no funciona, entonces hay que suponer que la dirección es una URL directa
            imagen = ImageIO.read(new URL(dirección));
            return ConvImgARGB(imagen);
        } catch (Exception e3) {
        }
        try {
            //Aunque con la URL debería funcionar, también descartamos que el File sea capaz de cargar la imagen
            imagen = ImageIO.read(new File(dirección));
            return ConvImgARGB(imagen);
        } catch (Exception e4) {
        }
        String PosiblesCarpetasDelProyecto[] = {
            "Imagenes", "Imágenes", "Recursos", "HerramientasGUI",
            "HerramientaDeImagen", "Assets", "Images"
        };
        for (String PosibleCarpeta : PosiblesCarpetasDelProyecto) {
            for (int i = 0; i < 3; i++) {
                String cm = PosibleCarpeta;
                cm = i == 1 ? cm.toLowerCase() : i == 2 ? cm.toUpperCase() : cm;
                try {
                    imagen = ImageIO.read(ClassLoader.class.getResource("/" + cm + "/" + dirección));
                } catch (Exception e5) {
                }
                if (imagen != null) {//Romper el for de las posibles varianzas
                    return ConvImgARGB(imagen);
                }
            }
        }
        if (imagen == null) {
            System.err.println("La imagen no se encontró; " + dirección);
        }
        return ConvImgARGB(imagen);
    }

    private static BufferedImage ConvImgARGB(BufferedImage imagen) {
        BufferedImage retorno = new BufferedImage(
                imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_ARGB
        );
        retorno.getGraphics().drawImage(imagen, 0, 0, null);
        return retorno;
    }

    public static String ObtenerExtensión(File archivo) {
        String ext = null;
        String s = archivo.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static File AgregarExtensión(File Archivo, String Extensión) {
        String ruta = Archivo.getAbsolutePath();
        if (Extensión.charAt(0) != '.') {
            Extensión = "." + Extensión;
        }
        if (ruta.endsWith(Extensión)) {
            return new File(ruta);
        } else {
            return new File(ruta + Extensión);
        }
    }

    public static File EvitarSobrescrituraSinConfirmar(File Archivo) {
        if (!Archivo.exists()) {
            return Archivo;
        }
        int Pregunta = JOptionPane.showConfirmDialog(
                null,
                "El archivo Existe\n¿desea reemplazarlo?",
                "Archivo ya existente",
                JOptionPane.YES_NO_CANCEL_OPTION
        );
        switch (Pregunta) {
            case JOptionPane.CANCEL_OPTION:
                return new File("Cancelar");
            case JOptionPane.NO_OPTION:
                File ArchivoViejo = Archivo.getAbsoluteFile();
                int indice = 0;
                while (Archivo.exists()) {
                    Archivo = new File(
                            ArchivoViejo.getPath().replace(
                                    ArchivoViejo.getName(),
                                    ArchivoViejo.getName().replace(".png", " (" + (indice++) + ").png")
                            )
                    );
                }
        }
        return Archivo;
    }

    public static void ExportarImagen(BufferedImage imagen, File Archivo, boolean EvitarSobrescritura) {
        try {
            Archivo = AgregarExtensión(Archivo, ".png");
            if (EvitarSobrescritura) {
                Archivo = EvitarSobrescrituraSinConfirmar(Archivo);
            }
            if (Archivo.getPath().equals("Cancelar")) {
                return;
            }
            ImageIO.write(imagen, "png", Archivo);
        } catch (Exception e) {
            System.out.println("Error al guardar el archivo " + Archivo.getPath());
        }
    }

    public static void ExportarImagen(BufferedImage imagen, Component c, boolean EvitarSobrescritura) {
        try {
            JFileChooser selector = new JFileChooser();
            selector.addChoosableFileFilter(FiltroSoloImagenes());
            selector.setAcceptAllFileFilterUsed(false);
            selector.setFileView(MostrarMiniaturas());
            selector.setDialogTitle("Guardar como imagen png - Jeff Aporta");
            selector.showSaveDialog(c);
            ExportarImagen(imagen, selector.getSelectedFile(), EvitarSobrescritura);
        } catch (Exception e) {
            System.out.println("Error al guardar el archivo ");
        }
    }

    public static void ExportarImagen(BufferedImage imagen) {
        ExportarImagen(imagen, new Component() {
        }, true);
    }

    public static void ExportarImagen(BufferedImage imagen, Component c) {
        ExportarImagen(imagen, c, true);
    }

    public static void ExportarImagen(BufferedImage imagen, boolean EvitarSobrescritura) {
        ExportarImagen(imagen, new Component() {
        }, EvitarSobrescritura);
    }

    public static void ExportarImagen(BufferedImage imagen, String ruta, boolean EvitarSobrescritura) {
        ExportarImagen(imagen, new File(ruta), EvitarSobrescritura);
    }

    public static void ExportarImagen(BufferedImage imagen, String ruta) {
        ExportarImagen(imagen, ruta, true);
    }

    public static FileView MostrarMiniaturas() {
        return new FileView() {
            @Override
            public Icon getIcon(File archivo) {
                String extension = ObtenerExtensión(archivo);
                Icon icon = null;
                if (extension != null) {
                    try {
                        BufferedImage img = ImageIO.read(archivo);
                        Image i = new ImageIcon(archivo.getPath()).getImage().getScaledInstance(-1, 16, Image.SCALE_FAST);
                        BufferedImage icono = new BufferedImage(32, 20, 2);
                        Filtros_Lineales.Ajuste_Centrado_Ajustar(img, icono);
                        icon = new ImageIcon(i);
                    } catch (Exception ex) {
                    }
                }
                return icon;
            }
        };
    }

    public static FileFilter FiltroSoloImagenes() {
        return new FileFilter() {
            @Override
            public boolean accept(File archivo) {
                if (archivo.isDirectory()) {
                    return true;
                }
                String ext = ObtenerExtensión(archivo);
                if (ext != null) {
                    if (ext.equals("jpeg") || ext.equals("jpg") || ext.equals("gif") || ext.equals("png")) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "Solo imagenes";
            }
        };
    }
}
