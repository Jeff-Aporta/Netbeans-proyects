package HerramientaArchivos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ListarDocumentos {

    public static void main(String[] args) throws IOException {
        File f = File.createTempFile("prueba", ".txt");
        System.out.println(f);
        String dir = System.getProperty("user.home");
        System.out.println(dir);
        ArrayList<File> array = CargarArchivos(new File(dir), new String[]{"jpg", "png", "jpeg", "gif"}, new String[]{"AppData"});
        String dirs = "";
        for (File file : array) {
            System.out.println(file.getPath());
            dirs += file.getPath() + "\n";
            System.out.println(file.length() / 1024f);
        }
        LectoEscrituraArchivos.EscribirArchivo_ASCII(f, dirs);
    }

    public static ArrayList<File> CargarArchivos(File CarpetaRaíz, String[] Extensiones) {
        ArrayList<File> array = new ArrayList<>();
        return CargarArchivos(CarpetaRaíz, Extensiones, null, array);
    }

    public static ArrayList<File> CargarArchivos(File CarpetaRaíz, String[] Extensiones, boolean IncluirSubCarpetas) {
        ArrayList<File> array = new ArrayList<>();
        return CargarArchivos(CarpetaRaíz, Extensiones, null, IncluirSubCarpetas, array);
    }

    public static ArrayList<File> CargarArchivos(File CarpetaRaíz, String[] Extensiones, String[] CarpetasExcluidas) {
        ArrayList<File> array = new ArrayList<>();
        return CargarArchivos(CarpetaRaíz, Extensiones, CarpetasExcluidas, array);
    }

    public static ArrayList<File> CargarArchivos(File CarpetaRaíz, String[] Extensiones, String[] CarpetasExcluidas, ArrayList<File> array) {
        return CargarArchivos(CarpetaRaíz, Extensiones, CarpetasExcluidas, false, array);
    }

    public static ArrayList<File> CargarArchivos(File CarpetaRaíz, String[] Extensiones, String[] CarpetasExcluidas, boolean IncluirSubCarpetas, ArrayList<File> array) {
        if (Extensiones != null && Extensiones.length > 0) {
            for (String Extension : Extensiones) {
                File[] archivos = CarpetaRaíz.listFiles((File file) -> {
                    return file.getName().endsWith(Extension) && !file.isDirectory();
                });
                try {
                    for (File file : archivos) {
                        array.add(file);
                    }
                } catch (Exception e) {
                }

            }
        } else {
            File[] archivos = CarpetaRaíz.listFiles((File file) -> !file.isDirectory());
            try {
                for (File file : archivos) {
                    array.add(file);
                }
            } catch (Exception e) {
            }
        }
        if (IncluirSubCarpetas) {
            File[] carpetas = CarpetaRaíz.listFiles((File file) -> file.isDirectory());
            try {
                for (File carpeta : carpetas) {
                    if (CarpetasExcluidas != null) {
                        boolean admitible = true;
                        for (String ExcluirCarpeta : CarpetasExcluidas) {
                            if (carpeta.getPath().contains(ExcluirCarpeta)) {
                                admitible = false;
                            }
                        }
                        if (admitible) {
                            CargarArchivos(carpeta, Extensiones, CarpetasExcluidas, array);
                        }
                    } else {
                        CargarArchivos(carpeta, Extensiones, CarpetasExcluidas, array);
                    }
                }
            } catch (Exception e) {
            }
        }
        return array;
    }

}
