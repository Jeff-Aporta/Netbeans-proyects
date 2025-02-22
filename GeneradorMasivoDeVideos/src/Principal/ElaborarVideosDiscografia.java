package Principal;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

public class ElaborarVideosDiscografia {

    public static void main(String[] args) throws Exception {
        iniciarTrabajo("C:\\Users\\57310\\Music\\renderizar");
    }

    public static void iniciarTrabajo(String dir) throws Exception {
        File CarpetaRaíz = new File(dir);
        File[] carpetas = CarpetaRaíz.listFiles((File file, String name) -> new File(file, name).isDirectory());
//        carpetas = invertirOrden(carpetas);
        for (File carpeta : carpetas) {
            try {
                CarpetaBanda(carpeta.getPath());
            } catch (Exception e) {
            }
        }
    }

    public static File[] invertirOrden(File[] array) {
        File[] farr = new File[array.length];
        for (int i = 0; i < farr.length; i++) {
            farr[array.length - i - 1] = array[i];
        }
        return farr;
    }

    public static void CarpetaBanda(String dir) throws Exception {
//        String FullDiscography = dir + dir.substring(dir.lastIndexOf("\\")) + " {FULL DISCOGRAPHY}.mp4";
//        if (new File(FullDiscography).exists()) {
//            return;
//        } else {
//            String FullDiscography2 = FullDiscography.replace(".mp4", " Part. 1.mp4");
//            if (new File(FullDiscography2).exists()) {
//                return;
//            }
//        }
        try {
            GeneradorDeSencillos.PROYECTO = ConstantesGeneradoresVideo.SIMPLE_SIN_CRONOMETRO;
            GeneradorDeSencillos.GenerarSencillos(dir);
        } catch (Exception e) {
        }
        ArrayList<File> rutasAlbumes = GeneradorAlbumes.GenerarAlbumesMusica(dir);
//        System.out.println("Albumes encontrados " + rutasAlbumes.size());
//        for (File rutaAlbum : rutasAlbumes) {
//            System.out.println("->" + rutaAlbum);
//        }
//        try {
//            ConcatenarTodosLosVideos.Concatenar_segúnDuraciones(
//                    rutasAlbumes,
//                    FullDiscography
//            );
//        } catch (Exception ex) {
//            System.err.println("Error al crear la discografía completa");
//        }
    }
}
