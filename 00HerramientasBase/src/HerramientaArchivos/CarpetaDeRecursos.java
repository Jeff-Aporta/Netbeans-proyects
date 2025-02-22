package HerramientaArchivos;

import HerramientasSistema.Sistema;
import HerramientasSistema.Fecha;
import java.io.File;
import java.net.URL;

public class CarpetaDeRecursos {

    public static final String URL_NO_APLICA = "NO APLICA";

    public static final byte TIPO_TMP = 0;
    public static final byte TIPO_APPDATA = 1;

    public final Byte TIPO;
    public final String DIRECCIÓN_CARPETA;
    public final String NOMBRE;

    public int TiempoDias_Actualizar = 15;

    public static void main(String[] args) throws Exception {
        CarpetaDeRecursos recursos = new CarpetaDeRecursos(
                CarpetaDeRecursos.TIPO_APPDATA,
                "Carpeta de prueba"
        );
        Recurso rec = recursos.GenerarRecurso("Archivo.txt");
        System.out.println(DiasDeModificaciónDeUnRecurso(rec));
        LectoEscrituraArchivos.EscribirArchivo_ASCII(
                rec.ObtenerArchivo(), "Prueba de escritura"
        );
        int[] datos = LectoEscrituraArchivos.LeerArchivo_BIN(rec.ObtenerArchivo());
        for (int dato : datos) {
            System.out.println(dato);
        }
        System.out.println(LectoEscrituraArchivos.LeerArchivo_ASCII(rec.ObtenerArchivo()));
    }

    public CarpetaDeRecursos(byte TIPO, String Nombre) {
        this.TIPO = TIPO;
        this.NOMBRE = Nombre;
        DIRECCIÓN_CARPETA = DIRECCIÓN_CARPETA();
        File carpeta = new File(DIRECCIÓN_CARPETA);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    public File ObtenerCarpeta() {
        return new File(DIRECCIÓN_CARPETA);
    }

    public CarpetaDeRecursos diasActualizarArchivos(int dias) {
        TiempoDias_Actualizar = dias;
        return this;
    }

    public File[] ListarArchivos() {
        return new File(DIRECCIÓN_CARPETA).listFiles();
    }

    public File[] ListarArchivos(String extensión) {
        return new File(DIRECCIÓN_CARPETA).listFiles((file) -> {
            return file.getName().toLowerCase().endsWith(extensión);
        });
    }

    public void AbrirCarpeta() {
        Sistema.AbrirRecursoDelComputador(DIRECCIÓN_CARPETA);
    }

    public Recurso GenerarRecurso(String nombre) {
        return GenerarRecurso(null, nombre);
    }

    public Recurso GenerarRecurso(String URL, String nombre) {
        try {
            return BuscarRecurso(URL, nombre);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static double DiasDeModificaciónDeUnRecurso(Recurso recurso) {
        return Fecha.DiasDeModificaciónDeUnArchivo(recurso.ObtenerArchivo());
    }

    public String DIRECCIÓN_CARPETA() {
        if (DIRECCIÓN_CARPETA != null) {
            return DIRECCIÓN_CARPETA;
        }
        switch (TIPO) {
            case TIPO_APPDATA:
                return Sistema.ruta_jeffAporta_appdata + "\\" + NOMBRE + "\\";
            case TIPO_TMP:
                return Sistema.ruta_jeffAporta_tmp + "\\" + NOMBRE + "\\";
            default:
                throw new RuntimeException("No se reconoce el caracter de la carpeta");
        }
    }

    public Recurso BuscarRecurso(String nombre) throws Exception {
        return BuscarRecurso(null, nombre);
    }

    public Recurso BuscarRecurso(String dirección, String nombre) throws Exception {
        if (dirección == null) {
            return new Recurso(nombre);
        }
        return new Recurso(new URL(dirección), nombre);
    }

    public class Recurso {

        public final String URL;
        public final String DIRECCIÓN;
        public final String NOMBRE;

        public Recurso(File file) throws Exception {
            URL = URL_NO_APLICA;
            DIRECCIÓN = file.getPath();
            NOMBRE = file.getName();
            if (!file.exists()) {
                throw new RuntimeException("El archivo especificado no existe: " + URL);
            }
        }

        public Recurso(String Nombre) throws Exception {
            URL = URL_NO_APLICA;
            NOMBRE = Nombre;
            DIRECCIÓN = DIRECCIÓN_CARPETA() + NOMBRE;
        }

        public Recurso(URL URL, String Nombre) throws Exception {
            this.URL = URL.toString();
            if (Nombre == null) {
                String nombre = URL.getFile();
                Nombre = nombre.substring(nombre.lastIndexOf("/") + 1);
            }
            NOMBRE = Nombre;
            DIRECCIÓN = DIRECCIÓN_CARPETA() + NOMBRE;
            if (ObtenerArchivo().exists()) {
                if (DiasDeModificaciónDeUnRecurso(this) > TiempoDias_Actualizar) {
                    try {
                        Descargar_ArchivoDeWeb();
                    } catch (Exception e) {
                    }
                }
            } else {
                try {
                    Descargar_ArchivoDeWeb();
                } catch (Exception e) {
                }
            }
        }

        public File ObtenerArchivo() {
            return new File(DIRECCIÓN);
        }

        public void Descargar_ArchivoDeWeb() throws Exception {
            if (Sistema.VerificarConexiónWeb()) {
                LectoEscrituraArchivos.DescargarArchivo(URL, DIRECCIÓN);
                if (!ObtenerArchivo().exists()) {
                    throw new RuntimeException("imposible conseguir el recurso de: " + URL);
                } else {
                    System.out.println("El archivo ha sido descargado: " + NOMBRE + "  " + URL);
                }
            } else {
                throw new RuntimeException("imposible conseguir el recurso de: " + URL + "\nNo hay conexión web");
            }
        }

        public void AbrirArchivo() {
            Sistema.AbrirRecursoDelComputador(DIRECCIÓN);
        }

        public void AbrirArchivoWeb_EnNavegador() {
            Sistema.AbrirRecursoDelComputador(URL);
        }

    }
}
