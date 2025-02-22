package Ejecutables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UbicadorEjecutables {

    /**
     * The ffmpeg executable file path.
     */
    public static String Dirección_FFmpeg = CopiarEjecutables();

    public static boolean isWindows;

    public static void main(String[] args) {
        System.out.println(UbicadorEjecutables.Dirección_FFmpeg);
    }

    public static String CopiarDelProyectoEjecutables(File CarpetaEjecutables) {
        try {
            // Windows?
            String NombreDelSistemaOperativo = System.getProperty("os.name").toLowerCase();
            isWindows = NombreDelSistemaOperativo.contains("windows");
            //Creación de la carpeta
            if (!CarpetaEjecutables.exists()) {
                CarpetaEjecutables.mkdirs();
            }
            // ffmpeg executable export on disk.
            String[] NombresDeLosEjecutables = {
                "ffmpeg" + (isWindows ? ".exe" : ""), "ffplay.exe", "exiftool.exe", "pthreadGC2.dll"
            };
            for (String NombreDelEjecutable : NombresDeLosEjecutables) {
                File ejecutable = new File(CarpetaEjecutables, NombreDelEjecutable);
                if (!ejecutable.exists()) {
                    new UbicadorEjecutables().copyFile(ejecutable.getName(), ejecutable);
                }
            }
            // Need a chmod?
            File Ejecutable_FFmpeg = new File(CarpetaEjecutables, NombresDeLosEjecutables[0]);
            if (!isWindows) {
                Runtime runtime = Runtime.getRuntime();
                runtime.exec(new String[]{"/bin/chmod", "755", Ejecutable_FFmpeg.getAbsolutePath()});
            }
            return Ejecutable_FFmpeg.getAbsolutePath();
        } catch (Exception e) {
        }
        return null;
    }

    public static String CopiarEjecutables() {
        File CarpetaEjecutables = new File(System.getenv("APPDATA"), "Jeff Aporta\\JFFmpeg Ejecutables");
        String retorno = CopiarDelProyectoEjecutables(CarpetaEjecutables);
        if (retorno != null) {
            return retorno;
        }
        return null;
    }

    private void copyFile(String path, File dest) throws Exception {
        InputStream input = null;
        OutputStream output = null;
        input = getClass().getResourceAsStream(path);
        output = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        for (int i = 0; (i = input.read(buffer)) != -1;) {
            output.write(buffer, 0, i);
        }
        output.close();
        input.close();
    }

}
