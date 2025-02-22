
import HerramientasSistema.Sistema;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class escogerFotosAlAzar {

    static public final String[] extensiones = new String[]{".png", ".jpeg", ".jpg"};

    public static void main(String[] args) {
        String path = "C:\\Users\\USUARIO\\Downloads\\nathycastrillon\\moderate-size";
        int ejemplares = 200;
        String azar = path + "\\azar" + ejemplares;
        if (!new File(azar).exists()) {
            new File(azar).mkdir();
        }
        final File[] archivos = new File(path).listFiles((dir, name) -> {
            for (String extension : extensiones) {
                if (name.endsWith(extension)) {
                    return true;
                }
            }
            return false;
        });
        ArrayList<File> ArrayListFiles = new ArrayList<File>() {
            {
                for (int i = 0; i < archivos.length; i++) {
                    add(archivos[i]);
                }
            }
        };
        Collections.shuffle(ArrayListFiles);
        for (int i = 0; i < ejemplares; i++) {
            Sistema.DuplicarArchivo(ArrayListFiles.get(i), new File(azar + "\\" + ArrayListFiles.get(i).getName()));
        }
    }

}
