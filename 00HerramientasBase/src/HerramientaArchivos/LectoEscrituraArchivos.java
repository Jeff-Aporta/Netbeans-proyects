package HerramientaArchivos;

import HerramientaDeImagen.Filtros_Lineales;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;

public class LectoEscrituraArchivos {

    public static void main(String[] args) throws Exception {
        String ruta = "https://www.youtube.com/";
//        System.out.println(LectoEscrituraArchivos.LeerArchivo_ASCII(ruta));
        System.out.println(System.getProperty("user.home"));
        String myDocuments = null;

        try {
            Process p = Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
            p.waitFor();

            InputStream in = p.getInputStream();
            byte[] b = new byte[in.available()];
            in.read(b);
            in.close();

            myDocuments = new String(b);
            myDocuments = myDocuments.split("\\s\\s+")[4];

        } catch (Throwable t) {
            t.printStackTrace();
        }

        System.out.println(myDocuments);
//        int[] bits = LeerArchivo_BIN(url);
//        for (int i = 0; i < bits.length; i++) {
//            System.out.print(bits[i]);
//            if (i < bits.length - 1) {
//                System.out.print(",");
//            } else {
//                System.out.println();
//            }
//        }
    }

    public static void DescargarArchivo(String archivo_remoto, String archivo_local) throws Exception {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            BufferedImage img = cargar_imagen(archivo_remoto);
            if (img != null) {
                exportar_imagen(img, archivo_local);
                return;
            }
        } catch (Exception e) {
        }
        int[] arr = LectoEscrituraArchivos.LeerArchivo_BIN(archivo_remoto);
        LectoEscrituraArchivos.EscribirArchivo_BIN(new File(archivo_local), arr);
    }//</editor-fold>

    public static String EliminarCódigoHTML_TextoPlano(String inputString) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        String entrada[] = {"&lt;", "&gt;", "&amp;", "&nbsp;", "&bull;"};
        String salida[] = {"<", ">;", "&", " ", ""};
        for (int i = 0; i < entrada.length; i++) {
            inputString = inputString.replace(entrada[i], salida[i]);
        }
        inputString = inputString.replaceAll("<[^>]*>", "");
        return inputString;
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cargar imagen »">
    public static BufferedImage cargar_imagen(URL archivo) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        BufferedImage imagen;
        try {
            imagen = ImageIO.read(archivo);
            return convertir_tipo_ARGB(imagen);
        } catch (IOException ex) {
            return null;
        }
    }//</editor-fold>

    public static BufferedImage cargar_imagen(File archivo) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        BufferedImage imagen;
        try {
            imagen = ImageIO.read(archivo);
            return convertir_tipo_ARGB(imagen);
        } catch (IOException ex) {
            return null;
        }
    }//</editor-fold>

    public static BufferedImage cargar_imagen(String ruta) {
        BufferedImage imagen = null;

        {//<editor-fold defaultstate="collapsed" desc="Descarta la posibilida de que la ruta sea un archivo remoto »">
            try {
                imagen = ImageIO.read(new URL(ruta));
                return convertir_tipo_ARGB(imagen);
            } catch (Exception e3) {
            }
        }//</editor-fold>

        {//<editor-fold defaultstate="collapsed" desc="Descarta la posibilidad de que la ruta sea un archivo local »">
            try {
                imagen = ImageIO.read(new File(ruta));
                return convertir_tipo_ARGB(imagen);
            } catch (Exception e4) {
            }
        }//</editor-fold>

        {//<editor-fold defaultstate="collapsed" desc="Descarta la posibilidad de que la ruta sea un recurso explicito del proyecto »">
            try {
                imagen = ImageIO.read(ClassLoader.class.getResource(ruta));
                return convertir_tipo_ARGB(imagen);
            } catch (Exception e) {
            }
        }//</editor-fold>

        {//<editor-fold defaultstate="collapsed" desc="Descarta la posibilidad de que la ruta sea un recurso implicito del proyecto »">
            try {
                imagen = ImageIO.read(ClassLoader.class.getResource("/" + ruta));
                return convertir_tipo_ARGB(imagen);
            } catch (Exception e2) {
            }
        }//</editor-fold>

        {//<editor-fold defaultstate="collapsed" desc="Descarta la posibilidad que la ruta sea un recurso implicito en una carpeta no especificada »">
            String PosiblesCarpetasDelProyecto[] = {
                "Imagenes", "Imágenes", "Recursos",
                "HerramientaDeImagen", "Assets", "Images"
            };
            for (String PosibleCarpeta : PosiblesCarpetasDelProyecto) {
                for (int i = 0; i < 3; i++) {
                    String cm = PosibleCarpeta;
                    cm = i == 1 ? cm.toLowerCase() : i == 2 ? cm.toUpperCase() : cm;
                    try {
                        imagen = ImageIO.read(ClassLoader.class.getResource("/" + cm + "/" + ruta));
                    } catch (Exception e5) {
                    }
                    if (imagen != null) {//Romper el for de las posibles varianzas
                        return convertir_tipo_ARGB(imagen);
                    }
                }
            }
        }//</editor-fold>

        if (imagen == null) {
            System.err.println("La imagen no se encontró; " + ruta);
        }

        return convertir_tipo_ARGB(imagen);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Exportar imagen »">
    public static void exportar_imagen(BufferedImage imagen, String Archivo) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        exportar_imagen(new File(Archivo), imagen);
    }//</editor-fold>

    public static void exportar_imagen(String Archivo, BufferedImage imagen) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        exportar_imagen(new File(Archivo), imagen);
    }//</editor-fold>

    public static void exportar_imagen(BufferedImage imagen, File Archivo) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        exportar_imagen(Archivo, imagen);
    }//</editor-fold>

    public static void exportar_imagen(File Archivo, BufferedImage imagen) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (imagen==null) {
            return;
        }
        try {
            if (Archivo.getName().toLowerCase().endsWith(".jpg") || Archivo.getName().toLowerCase().endsWith(".jpeg")) {
                BufferedImage img = new BufferedImage(
                        imagen.getWidth(),
                        imagen.getHeight(),
                        BufferedImage.TYPE_INT_RGB
                ) {
                    {
                        Graphics2D g = createGraphics();
                        g.drawImage(imagen, 0, 0, null);
                    }
                };
                ImageIO.write(img, "jpg", Archivo);
                return;
            }
            Archivo = agregar_extensión(Archivo, ".png");
            ImageIO.write(imagen, "png", Archivo);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al guardar el archivo " + Archivo.getPath());
        }
    }//</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Cargar-exportar imagen con JFileChooser »">
    public static BufferedImage cargar_imagen_con_JFileChooser() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        JFileChooser selector = new JFileChooser();
        selector.addChoosableFileFilter(FiltroSoloImagenes());
        selector.setAcceptAllFileFilterUsed(false);
        selector.setFileView(MostrarMiniaturas());
        selector.setDialogTitle("Cargar imagen");
        selector.showOpenDialog(null);
        return cargar_imagen(selector.getSelectedFile());
    }//</editor-fold>

    public static void exportar_imagen_con_JFileChooser(BufferedImage imagen) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        exportar_imagen_con_JFileChooser(imagen, new Component() {
        });
    }//</editor-fold>

    public static void exportar_imagen_con_JFileChooser(BufferedImage imagen, Component c) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            JFileChooser selector = new JFileChooser();
            selector.addChoosableFileFilter(FiltroSoloImagenes());
            selector.setAcceptAllFileFilterUsed(false);
            selector.setFileView(MostrarMiniaturas());
            selector.setDialogTitle("Exportar imagen");
            selector.showOpenDialog(null);
            exportar_imagen(selector.getSelectedFile(), imagen);
        } catch (Exception e) {
            System.out.println("Error al guardar el archivo ");
        }
    }//</editor-fold>

    public static FileView MostrarMiniaturas() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return new FileView() {
            @Override
            public Icon getIcon(File archivo) {
                String extension = obtener_extensión(archivo);
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
    }//</editor-fold>

    public static FileFilter FiltroSoloImagenes() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return new FileFilter() {
            @Override
            public boolean accept(File archivo) {
                if (archivo.isDirectory()) {
                    return true;
                }
                String ext = obtener_extensión(archivo);
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
    }//</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Leer archivo binario »">
    public static int[] LeerArchivo_BIN(String ruta) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            return LeerArchivo_BIN(new File(ruta));
        } catch (Exception e) {
            try {
                return LeerArchivo_BIN(new URL(ruta));
            } catch (Exception e2) {
                System.err.println("No se encontró el archivo en: " + ruta);
                return null;
            }
        }
    }//</editor-fold>

    public static int[] LeerArchivo_BIN(File archivo) throws Exception {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (archivo == null) {
            throw new RuntimeException("Error, el archivo de lectura no puede ser nulo");
        }
        FileReader fr = new FileReader(archivo);
        return LeerArchivo_BIN(fr);
    }//</editor-fold>

    public static int[] LeerArchivo_BIN(URL url) throws Exception {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        URLConnection conexión = url.openConnection();
        InputStreamReader isr = new InputStreamReader(conexión.getInputStream());
        return LeerArchivo_BIN(isr);
    }//</editor-fold>

    public static int[] LeerArchivo_BIN(Reader reader) throws Exception {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        BufferedReader br = new BufferedReader(reader);
        ArrayList<Integer> Bits = new ArrayList<>();
        int Número = 0;
        while ((Número = br.read()) != -1) {
            Bits.add(Número);
        }
        if (reader != null) {
            reader.close();
        }
        int[] retorno = new int[Bits.size()];
        for (int i = 0; i < retorno.length; i++) {
            retorno[i] = Bits.get(i);
        }
        return retorno;
    }//</editor-fold>
    //</editor-fold>

    public static String LeerArchivo_ASCII(String ruta) throws Exception {
        try {
            return LectoEscrituraArchivos.LeerArchivo_ASCII(new File(ruta));
        } catch (Exception e) {
            try {
                return LectoEscrituraArchivos.LeerArchivo_ASCII(new URL(ruta));
            } catch (Exception e2) {
                System.out.println("Error al cargar el archivo " + ruta);
                e.printStackTrace();
                return null;
            }
        }
    }

    public static String LeerArchivo_ASCII(URL url) throws Exception {
        URLConnection conexión = url.openConnection();
        InputStreamReader isr = new InputStreamReader(conexión.getInputStream());
        return LeerArchivo_ASCII(isr);
    }

    public static String LeerArchivo_ASCII(File archivo) throws Exception {
        if (archivo == null) {
            throw new RuntimeException("Error, el archivo de lectura no puede ser nulo");
        }
        FileReader fr = new FileReader(archivo);
        return LeerArchivo_ASCII(fr);
    }

    public static String LeerArchivo_ASCII(Reader reader) throws Exception {
        BufferedReader br = null;
        br = new BufferedReader(reader);
        String texto = "";
        String linea;
        boolean PrimerRenglón = true;
        while ((linea = br.readLine()) != null) {
            if (PrimerRenglón) {
                PrimerRenglón = false;
            } else {
                texto += "\n";
            }
            texto += linea;
        }
        reader.close();
        br.close();
        return texto;
    }

    //<editor-fold defaultstate="collapsed" desc="Escribir archivo ascii-bin">
    public static void EscribirArchivo_ASCII(File archivo, String txt) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (archivo == null) {
            throw new RuntimeException("Error, el archivo de escrutura no puede ser nulo");
        }
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(archivo);
            pw = new PrintWriter(fichero);
            if (txt.contains("\n")) {
                String[] renglones = txt.split("\n");
                for (int i = 0; i < renglones.length; i++) {
                    if (i != renglones.length - 1) {
                        pw.println(renglones[i]);
                    } else {
                        pw.print(renglones[i]);
                    }
                }
            } else {
                pw.write(txt);
            }
            fichero.close();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//</editor-fold>

    public static void EscribirArchivo_BIN(File archivo, int... números) throws Exception {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (archivo == null) {
            throw new RuntimeException("Error, el archivo de escrutura no puede ser nulo");
        }
        FileWriter fichero = null;
        PrintWriter pw = null;
        fichero = new FileWriter(archivo);
        pw = new PrintWriter(fichero);
        for (int i = 0; i < números.length; i++) {
            pw.write(números[i]);
        }
        fichero.close();
        pw.close();
    }//</editor-fold>
    //</editor-fold>

    public static BufferedImage convertir_tipo_ARGB(BufferedImage imagen) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (imagen == null || imagen.getType() == BufferedImage.TYPE_INT_ARGB) {
            return imagen;
        }
        BufferedImage retorno = new BufferedImage(
                imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_ARGB
        );
        retorno.getGraphics().drawImage(imagen, 0, 0, null);
        return retorno;
    }//</editor-fold>

    public static String obtener_extensión(File archivo) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        String ext = null;
        String s = archivo.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }//</editor-fold>

    public static File agregar_extensión(File Archivo, String Extensión) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        String ruta = Archivo.getAbsolutePath();
        if (Extensión.charAt(0) != '.') {
            Extensión = "." + Extensión;
        }
        if (ruta.endsWith(Extensión)) {
            return new File(ruta);
        } else {
            return new File(ruta + Extensión);
        }
    }//</editor-fold>

}
