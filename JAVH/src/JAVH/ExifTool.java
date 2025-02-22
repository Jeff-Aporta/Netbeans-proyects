package JAVH;

import static JAVH.Constantes_Ejecutador.TMP_DIR;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Esta es una clase que se encarga de generarnos un objeto con el cual podemos
 * usar herramientas de ExifTool. ExifTool es una herramienta que funciona con
 * etiquetas que se le pasan por el simbolo del sistema y se especializa en
 * trabajar con los metadatos de un archivo, lo que tiene que ver con lectura y
 * modificación. con esta información podemos trabajar más fácilmente en
 * algoritmos relacionados con multimedia<br/><br/>
 * <p align="center">
 * <img src="https://jeffaporta01.000webhostapp.com/Librerias Java/JExifTool/ExifTool logo.png" width="200" height="200">
 * </p>
 */
public class ExifTool extends Ejecutador implements Constantes_ExiftTool {

    /**
     * Con esta variable podemos personalizar el idioma con el que queremos
     * exportar los metadatos<br/><br/>
     * <p align="center">
     * <img src="https://jeffaporta01.000webhostapp.com/Librerias Java/lenguaje.png" width="150" height="112">
     * </p>
     * <br/>
     * Available languages: cs - Czech , de - German (Deutsch), en - English ,
     * en-ca - Canadian English , en-gb - British English, es - Spanish
     * (Español), fi - Finnish (Suomi), fr - French, it - Italian (Italiano), ja
     * - Japanese , ko - Korean , nl - Dutch (Nederlands), pl - Polish (Polski),
     * ru - Russian , sv - Swedish (Svenska), tr - Turkish, zh-cn - Simplified
     * Chinese , zh-tw - Traditional Chinese<br/><br/>
     */
    public String LENGUAJE = LENGUAJE_INGLÉS__ENGLISH;
    public String MODO_EXPORTACIÓN = EXPORTAR_DATOS_TEXTO;
    /**
     * Esta variable contendrá los metadatos del archivo una vez que se hayan
     * cargado, los metadatos son una matriz de 2 columnas, donde la primer
     * columna es la etiqueta del metadato y la segunda el valor de ese
     * metadato, la cantidad de filas varia según el tipo de archivo que sea, ya
     * que todos tienen diferentes metadatos y diferente cantidad de los mismos,
     * los metadatos son bastante utiles cuando necesitamos datos como las
     * dimensiones de un video, la duración de un archivo multimedia o el
     * artista de una canción etc<br/><br/>
     * <p align="center">
     * <img src="https://jeffaporta01.000webhostapp.com/Librerias Java/JExifTool/tablaexiftool.png" width="350" height="325">
     * </p>
     */
    public String[][] METADATOS;

    public static void main(String[] args) throws Exception {
        JFrame alwayOnTop = new JFrame() {
            {
                setAlwaysOnTop(true);
            }
        };
        boolean ProbarExportaciónMetadatos = true;
        boolean ProbarLecturaMetadatos = true;

        String carpeta = "C:\\Users\\guillermo\\Documents\\Mega downloader\\";
        String archivo1 = "01 - Prelude.mp3";
        String archivo2 = "test.txt";

        String recurso = carpeta + archivo1;
        String salida = carpeta + archivo2;
        ExifTool exifTool = new ExifTool(recurso, salida);

        if (ProbarExportaciónMetadatos) {
//            exifTool.MODO_EXPORTACIÓN = Constantes_ExiftTool.EXPORTAR_DATOS_HTML;
            exifTool.GenerarArchivoMetadata();
            try {
                Desktop.getDesktop().open(new File(salida));
            } catch (Exception e) {
            }
            if (ProbarLecturaMetadatos) {
                exifTool.ImprimirMetadatos();
            }
        }
        {
            String[] options = new String[]{"Continuar"};
            JOptionPane.showOptionDialog(null,
                    "Aproveche esta pausa para revisar los cambios en el equipo", "Pausa...",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);
        }

        System.out.println("Segundos: " + exifTool.ObtenerDuraciónSegundos());
        System.out.println("Dimensión: " + exifTool.ObtenerDimensiones());
    }

    public ExifTool(String RECURSO) {
        this(RECURSO, "");
    }

    public ExifTool(String RECURSO, String SALIDA) {
        super(RECURSO, SALIDA);
        DIRECCIÓN_EJECUTABLE = DIRECCIÓN_EXIFTOOL;
    }

    public void ModificarDirección(String nuevaDirección) throws Exception {
        VerificarExistencia();
        {
            String[] caracteresNoPermitidosEnNombres = {
                "*", "?", "<", ">", "|"
            };
            for (String caracterNoPermitidoEnNombre : caracteresNoPermitidosEnNombres) {
                if (nuevaDirección.contains(caracterNoPermitidoEnNombre)) {
                    throw new RuntimeException(
                            "La ruta tiene el caracter " + caracterNoPermitidoEnNombre + " y no se puede asignar"
                    );
                }
            }
        }
        String ext = RECURSO.substring(RECURSO.lastIndexOf("."));
        if (!nuevaDirección.endsWith(ext)) {
            nuevaDirección += ext;
        }
        ModificarMetadatos("File Name", nuevaDirección);
        RECURSO = nuevaDirección;
    }

    public void ModificarCarpeta(String nuevaCarpeta) throws Exception {
        VerificarExistencia();
        {
            String[] caracteresNoPermitidosEnNombres = {
                "*", "?", "<", ">", "|"
            };
            for (String caracterNoPermitidoEnNombre : caracteresNoPermitidosEnNombres) {
                if (nuevaCarpeta.contains(caracterNoPermitidoEnNombre)) {
                    throw new RuntimeException(
                            "El nombre tiene el caracter " + caracterNoPermitidoEnNombre + " y no se puede asignar"
                    );
                }
            }
        }
        String nombre = ConsultarMetadato("File Name", true);
        nuevaCarpeta = nuevaCarpeta.replace(nombre, "");
        ModificarMetadatos("Directory", nuevaCarpeta);
        RECURSO = (nuevaCarpeta + "\\" + nombre).replace("\\\\", "\\");
    }

    public void ModificarNombre(String nuevoNombre) throws Exception {
        VerificarExistencia();
        System.out.println("Modificar nombre de: " + RECURSO);
        System.out.println("Con: " + nuevoNombre);
        {
            char[] caracteresNoPermitidosEnNombres = {
                '*', '?', '<', '>', '|'
            };
            for (char caracterNoPermitidoEnNombre : caracteresNoPermitidosEnNombres) {
                if (nuevoNombre.contains(caracterNoPermitidoEnNombre + "")) {
                    throw new RuntimeException(
                            "El nuevo nombre tiene el caracter " + caracterNoPermitidoEnNombre
                            + " y no se puede asignar"
                    );
                }
            }
        }
        String ext = RECURSO.substring(RECURSO.lastIndexOf("."));
        if (!nuevoNombre.endsWith(ext)) {
            nuevoNombre += ext;
        }
        ModificarMetadatos("File Name", nuevoNombre);
        RECURSO = RECURSO.replace(RECURSO.substring(RECURSO.lastIndexOf("\\") + 1), nuevoNombre);
    }

    public Dimension ObtenerDimensiones() throws Exception {
        CargaDeMetadatos();
        String ancho = ConsultarMetadato("width", true);
        if (ancho == null) {
            return new Dimension();
        }
        String alto = ConsultarMetadato("height", true);
        if (alto == null) {
            return new Dimension();
        }
        try {
            return new Dimension(Integer.parseInt(ancho), Integer.parseInt(alto));
        } catch (Exception e) {
            return new Dimension();
        }
    }

    public String ConsultarMetadato(String etiqueta, boolean buscarAproximación) throws Exception {
        CargaDeMetadatos();
        for (int i = 0; i < METADATOS.length; i++) {
            if (METADATOS[i][0].toLowerCase().equals(etiqueta.toLowerCase())) {
                return METADATOS[i][1];
            }
        }
        if (buscarAproximación) {
            for (int i = 0; i < METADATOS.length; i++) {
                if (METADATOS[i][0].toLowerCase().contains(etiqueta.toLowerCase())) {
                    return METADATOS[i][1];
                }
            }
        }
        return "";
    }

    public double ObtenerDuraciónSegundos() throws Exception {
        String tiempo = ConsultarMetadato("duration", true);
        if (tiempo == null) {
            return 0;
        }
        tiempo = tiempo.replace("(approx)", "");
        tiempo = tiempo.replace("s", "");
        try {
            return Double.parseDouble(tiempo);
        } catch (Exception e) {
        }
        String[] valores = tiempo.split(":");
        System.out.println(tiempo);
        if (valores.length == 3) {
            double h = Double.parseDouble(valores[0]);
            double m = Double.parseDouble(valores[1]);
            double s = Double.parseDouble(valores[2]);
            double retorno = 60 * (60 * h + m) + s;
            return retorno;
        }
        if (valores.length == 2) {
            double m = Double.parseDouble(valores[0]);
            double s = Double.parseDouble(valores[1]);
            double retorno = 60 * m + s;
            return retorno;
        }
        try {
              return Double.parseDouble(valores[0]);
        } catch (Exception e) {
            return -1;
        }
      
    }

    public void RefrescarMetadatos() throws Exception {
        METADATOS = ObtenerMetadatos();
    }

    public void ImprimirMetadatos() throws Exception {
        CargaDeMetadatos();
        for (int i = 0; i < METADATOS.length; i++) {
            System.out.println(METADATOS[i][0] + "  :::  " + METADATOS[i][1]);
        }
    }

    public void ModificarMetadatos(String... nuevosMetadatos) throws Exception {
        if (nuevosMetadatos.length % 2 != 0) {
            throw new RuntimeException("Los metadatos estan incompletos, tienen que quedar en duplas");
        }
        String[][] reestructura = new String[nuevosMetadatos.length / 2][2];
        for (int i = 0; i < nuevosMetadatos.length / 2; i++) {
            reestructura[i][0] = nuevosMetadatos[i * 2];
            reestructura[i][1] = nuevosMetadatos[i * 2 + 1];
        }
        ModificarMetadatos(reestructura);
    }

    public void ModificarMetadatos(String[][] nuevosMetadatos) throws Exception {
        VerificarExistencia();
        ArrayList comandos = new ArrayList() {
            {
                for (int i = 0; i < nuevosMetadatos.length; i++) {
                    String etiqueta = nuevosMetadatos[i][0].replace(" ", "").trim();
                    String valor = nuevosMetadatos[i][1].trim();
                    add("-" + etiqueta + "=\"" + valor + "\"");
                }
                add("\"" + RECURSO + "\"");
            }
        };
        EjecutarCódigo_cmd(comandos, true);
        try {
            RefrescarMetadatos();
        } catch (Exception e) {
        }
    }

    /**
     * Esta función se encarga de leer lo metadatos, organizarlos en una matriz
     * de 2 columnas donde la primer columna son las etiquetas de los metadatos
     * y la segunda los valores de los mismos respectivamente<br/><br/>
     * <p align="center">
     * <img src="https://jeffaporta01.000webhostapp.com/Librerias Java/JExifTool/tablaexiftool.png" width="350" height="325">
     * </p>
     */
    public String[][] ObtenerMetadatos() throws Exception {
        VerificarExistencia();
        String Dir_metadatos = TMP_DIR + "metadata " + (int) (Math.random() * 999999) + ".txt";
        ExifTool exifTool = new ExifTool(RECURSO, Dir_metadatos);
        exifTool.GenerarArchivoMetadata();
        File archivoMetadatos = new File(Dir_metadatos);
        String LecturaMetadatos = Auxiliar_LectoEscrituraArchivos.LeerArchivo_ASCII(archivoMetadatos);
        archivoMetadatos.delete();
        String[] Filas = LecturaMetadatos.split("\n");
        String[][] Metadatos = new String[Filas.length][2];
        for (int i = 0; i < Filas.length; i++) {
            String Fila = Filas[i];
            try {
                String etiqueta = Fila.substring(0, Fila.indexOf("\t")).trim();
                String valor = Fila.substring(Fila.indexOf("\t")).trim();
                Metadatos[i][0] = etiqueta;
                Metadatos[i][1] = valor;
            } catch (Exception e) {
                Metadatos[i][0] = Fila.trim();
            }
        }
        return Metadatos;
    }

    private void VerificarExistencia() throws Exception {
        if (!new File(RECURSO).exists()) {
            throw new RuntimeException("El archivo no se encuentra... " + RECURSO);
        }
    }

    private void CargaDeMetadatos() throws Exception {
        if (METADATOS == null) {
            RefrescarMetadatos();
        }
    }

    public void GenerarArchivoMetadata() throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-lang");
                add(LENGUAJE);
                add(MODO_EXPORTACIÓN);
                add(RECURSO);
                add(">");
                add(SALIDA);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    public void ModificarModoExportación(String ModoExportación) {
        switch (ModoExportación) {
            case EXPORTAR_DATOS_TEXTO:
            case EXPORTAR_DATOS_TABULADOS:
            case EXPORTAR_DATOS_HTML:
                MODO_EXPORTACIÓN = ModoExportación;
                break;
            default:
                System.err.println("El modo de exportación no se reconoce");
        }
    }

    public void ModificarLenguaje(String Lenguaje) {
        switch (Lenguaje) {
            case LENGUAJE_CHECO__CZECH:
            case LENGUAJE_ALEMÁN__GERMAN:
            case LENGUAJE_INGLÉS__ENGLISH:
            case LENGUAJE_INGLÉS_CANADIENSE__ENGLISH_CANADIAN:
            case LENGUAJE_INGLÉS_BRITÁNICO__ENGLISH_BRITISH:
            case LENGUAJE_ESPAÑOL__SPANISH:
            case LENGUAJE_FINLANDÉS__FINNISH:
            case LENGUAJE_FRANCÉS__FRENCH:
            case LENGUAJE_ITALIANO__ITALIAN:
            case LENGUAJE_JAPONES__JAPANESE:
            case LENGUAJE_KOREANO__KOREAN:
            case LENGUAJE_HOLANDÉS__DUTCH:
            case LENGUAJE_POLACO__POLISH:
            case LENGUAJE_RUSO__RUSSIAN:
            case LENGUAJE_SUECO__SWEDISH:
            case LENGUAJE_TURCO__TURKISH:
            case LENGUAJE_CHINO_SIMPLIFICADO__SIMPLIFIED_CHINESE:
            case LENGUAJE_CHINO_TRADICIONAL__TRADITIONAL_CHINESE:
                LENGUAJE = Lenguaje;
                break;
            default:
                System.err.println("El Lenguaje especificado no se reconoce");
        }
    }

}
