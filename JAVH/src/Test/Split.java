package Test;

import JAVH.ExifTool;
import JAVH.FFmpeg;
import static JAVH.FFmpeg.segundos;

public class Split {

    public static void main(String[] args) throws Exception {
        String fuente = "C:\\Users\\USUARIO\\Downloads\\EXCEL AVANZADO ► JUEGO DE NAVES ► INVASIÓN.mp4";
        split_por_duración_máxima(
                fuente,
                segundos(0, 3, 50),
                segundos(0, 1, 0)
        );
    }

    public static void split_por_duración_máxima(String fuente, int duracion_fuente, int duración_máxima_segundos) throws Exception {
        if (duracion_fuente < 1) {
            try {
                ExifTool exifTool = new ExifTool(fuente);
                duracion_fuente = (int) exifTool.ObtenerDuraciónSegundos();
            } catch (Exception e) {
                FFmpeg fFmpeg = new FFmpeg(fuente);
                duracion_fuente = fFmpeg.duración_s_con_fotogramas_$demorado$();
            }
        }
        System.out.println("duración: " + duracion_fuente);
        int partes = (int) Math.ceil(1.0 * duracion_fuente / duración_máxima_segundos);
        System.out.println("cantidad de partes: " + partes);
        int duracion_pp = duracion_fuente / partes;
        System.out.println("duración por parte: " + duracion_pp);
        String ext = fuente.substring(fuente.lastIndexOf("."));
        System.out.println("extensión: " + ext);
        Object[] datos = new Object[partes * 2];
        int c = 1;
        for (int i = 0; i < datos.length; i++) {
            datos[i] = fuente.replace(ext, " - Parte " + c + " de " + partes + ext);
            i++;
            datos[i] = duracion_pp * c;
            c++;
        }
        split_por_marcas(fuente, datos);
    }

    public static void split_por_marcas(String fuente, Object... datos) throws Exception {
        if (datos.length % 2 != 0) {
            throw new RuntimeException("Los datos deben estar emparejados");
        }
        int testigo = 0;
        FFmpeg ffmpeg = new FFmpeg(fuente);
        for (int i = 0; i < datos.length; i++) {
            String salida = (String) datos[i];
            i++;
            int segundo = (int) datos[i];
            ffmpeg.SALIDA = salida;
            ffmpeg.recortarTiempoVideo_inicio_fin(testigo, testigo = segundo);
        }
    }
}
