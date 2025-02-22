package JAVH;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;

public class Auxiliar_LectoEscrituraArchivos implements Constantes_Ejecutador {

    public static void main(String[] args) throws Exception {
        boolean probarEscritura = true;
        boolean probarLectura_Ascii = true;
        File archivo = new File(TMP_DIR + "\\prueba.txt");
        if (probarEscritura) {
            String texto = "\"El mundo era tan reciente, que muchas cosas carecían de nombre,\n"
                    + "y para mencionarlas había que señalarlas con el dedo."
                    + "Todos los años, por el mes de marzo,\n"
                    + "una familia de gitanos desarrapados plantaba su carpa cerca de la aldea,\n"
                    + "y con un grande alboroto de pitos y timbales daban a conocer los\n"
                    + "nuevos inventos.\"";
            EscribirArchivo_ASCII(archivo, texto);
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(new File(TMP_DIR));
                desktop.open(archivo);
            } catch (Exception e) {
            }
        }
        if (probarLectura_Ascii) {
            try {
                System.out.println(Auxiliar_LectoEscrituraArchivos.LeerArchivo_ASCII(archivo));
            } catch (Exception e) {
            }
        }
    }

    public static String LeerArchivo_ASCII(File archivo) throws Exception {
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

    public static void EscribirArchivo_ASCII(File archivo, String txt) {
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
        }
    }
}
