package Principal;

import java.io.File;
import java.io.FileFilter;

public interface ConstantesGeneradoresVideo {

    int Duración_Máxima_Segundos = (int) (10 * 60 * 60);
    byte YOUTUBE = 0, ESPECTRO_CIRCULAR = 1, SIMPLE_CRONOMETRO = 2,SIMPLE_SIN_CRONOMETRO=3;

    static FileFilter FiltroCarpetas_S() {
        return (File file) -> {
            if (file.isDirectory()) {
                return true;
            }
            return false;
        };
    }

    static FileFilter FiltroCarpetas() {
        return (File file) -> {
            if (file.isDirectory() && !file.getPath().toLowerCase().endsWith("- full")) {
                return true;
            }
            return false;
        };
    }

    static String LimpiarNombreDeCaracteresNoVálidos(String Nombre) {
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
    }

    static FileFilter FiltroArchivosVideo() {
        return FiltroArchivos_Extención("mp4");
    }

    static FileFilter FiltroArchivosAudio() {
        return FiltroArchivos_Extención("mp3", "m4a", "wma");
    }

    static FileFilter FiltroArchivos_Extención(String... formatosPermitidos) {
        for (int i = 0; i < formatosPermitidos.length; i++) {
            formatosPermitidos[i] = formatosPermitidos[i].toLowerCase();
        }
        return (File file) -> {
            if (file.isDirectory()) {
                return false;
            }
            for (String formatoPermitido : formatosPermitidos) {
                if (file.getPath().toLowerCase().endsWith(formatoPermitido)) {
                    return true;
                }
            }
            return false;
        };
    }
}
