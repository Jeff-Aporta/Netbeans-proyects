package APP_customFolderIcon;

import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientasSistema.Sistema;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;

public class ModificarIconosCarpetas {

    public static void main(String[] args) {
        test();
        //EliminarTodosLos_ini("C:\\Users\\57310\\Documents");
    }

    static void test() {
//        asignarIconoCarpetas(
//                "C:\\Users\\57310\\Pictures\\Iconos\\camtasia-folder.ico",
//                "C:\\Users\\57310\\Documents\\Camtasia",
//                1
//        );
        asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\folder-css.ico",
                "C:\\Users\\57310\\Documents\\CSS",
                1
        );
        asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\folder-javascript.ico",
                "C:\\Users\\57310\\Documents\\Javascript",
                1
        );
         asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\folder-javascript-css.ico",
                "C:\\Users\\57310\\Documents\\Javascript\\00CSS",
                1
        );
         asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\folder-javascript-p5-js.ico",
                "C:\\Users\\57310\\Documents\\Javascript\\00p5.js",
                1
        );
         asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\folder-three-js.ico",
                "C:\\Users\\57310\\Documents\\Javascript\\00Three.js",
                1
        );
         asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\folder-libs.ico",
                "C:\\Users\\57310\\Documents\\Javascript\\00Libs",
                1
        );
        asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\folder-java.ico",
                "C:\\Users\\57310\\Documents\\NetBeansProjects",
                1
        );
        asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\folder-ISeeCI.ico",
                "C:\\Users\\57310\\Documents\\ISeeCI",
                1
        );
        asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\nodejs-folder.ico",
                "C:\\Users\\57310\\Documents\\Node js",
                1
        );
        asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\public-folder.ico",
                "C:\\Users\\57310\\Documents\\Node js",
                "public"
        );
         asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\folder-git.ico",
                "C:\\Users\\57310\\Documents\\Node js",
                ".git"
        );
        asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\node_modules-folder.ico",
                "C:\\Users\\57310\\Documents\\Node js",
                "node_modules"
        );
        asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\pseint-folder.ico",
                "C:\\Users\\57310\\Documents\\PSeInt",
                1
        );
        asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\folder-python.ico",
                "C:\\Users\\57310\\Documents\\Python",
                1
        );
        asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\folder-pycache.ico",
                "C:\\Users\\57310\\Documents\\Python",
                "__pycache__"
        );
         asignarIconoCarpetas(
                "C:\\Users\\57310\\Pictures\\Iconos\\public-folder.ico",
                "C:\\Users\\57310\\Documents\\Python",
                "static"
        );
    }

    static void asignarIconoCarpetas(String icono, String raiz, int niveles) {
        asignarIconoCarpetas(icono, raiz, niveles, 0);
    }

    static void asignarIconoCarpetas(String icono, String raiz, String mascara) {
        if (new File(raiz).getName().equals(mascara)) {
            asignarIconoCarpeta(icono, raiz);
        }
        File[] f = new File(raiz).listFiles((file) -> {
            return file.isDirectory();
        });
        for (File file : f) {
            asignarIconoCarpetas(icono, file.getPath(), mascara);
        }
    }

    static void asignarIconoCarpetas(String icono, String raiz, int niveles, int nivel) {
        if (nivel > niveles) {
            return;
        }
        asignarIconoCarpeta(icono, raiz);
        File[] f = new File(raiz).listFiles((file) -> {
            return file.isDirectory();
        });
        for (File file : f) {
            asignarIconoCarpetas(icono, file.getPath(), niveles, nivel + 1);
        }
    }

    static void asignarIconoCarpeta(String icono, String carpeta) {
        String path_icon = carpeta + "\\icon.ico";
        String path_ini = carpeta + "\\desktop.ini";

        new File(path_ini).delete();
        new File(path_icon).delete();

        Sistema.DuplicarArchivo(icono, path_icon);
        LectoEscrituraArchivos.EscribirArchivo_ASCII(
                new File(path_ini),
                "[.ShellClassInfo]\n"
                + "IconResource=icon.ico,0\n"
                + "[ViewState]\n"
                + "Mode=\n"
                + "Vid=\n"
                + "FolderType=Pictures"
        );
        try {
            Files.setAttribute(Paths.get(path_icon), "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        } catch (Exception e) {
        }
        try {
            Files.setAttribute(Paths.get(path_ini), "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        } catch (Exception e) {
        }
    }

    static void EliminarTodosLos_ini(String folder) {
        File carpeta = new File(folder);
        File[] archivos = carpeta.listFiles();
        try {
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    EliminarTodosLos_ini(archivo.getPath());
                    continue;
                }
                if (archivo.getName().endsWith(".ini")) {
                    archivo.delete();
                }
                if (archivo.getName().endsWith(".ico")) {
                    archivo.delete();
                }
            }
        } catch (Exception e) {
        }
    }
}
