package JAVH;

import HerramientaArchivos.CarpetaDeRecursos;
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientasSistema.*;
import HerramientaDeImagen.Filtros_Lineales;
import HerramientasMatemáticas.Dupla;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Random;

public class FFmpeg extends Ejecutador implements Constantes_FFmpeg {

    public int FPS = -1;
    public int CRF = -1;
    public int ANCHO = -1;
    public int ALTO = -1;
    public int CONVOLUCIÓN_AFILAR_BORDES = -1;

    public static void main(String[] args) throws Exception {
        String name = "";
        FFmpeg ffmpeg = new FFmpeg();
        ffmpeg.SALIDA = "C:\\YAL7\\concat.mp4";
        String[] lst = new File("C:\\YAL7").list((dir, n) -> n.endsWith(".mp4"));
        for (int i = 0; i < lst.length; i++) {
            lst[i] = "C:\\YAL7\\" + lst[i];
        }
        ffmpeg.ConcatenarVideos_Opción2_CreandoLista(lst);
    }

    public void invertirColor() throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-vf");
                add("negate");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    public void velocidad(double multiplicador) throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-filter_complex");
                add("\"[0:v]setpts=" + 1 / multiplicador + "*PTS[v];[0:a]atempo=" + multiplicador + "[a]\"");
                add("-map");
                add("\"[v]\"");
                add("-map");
                add("\"[a]\"");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    public static int segundos(int h, int m, int s) {
        return h * 60 * 60 + m * 60 + s;
    }

    public FFmpeg() {
        super("", "");
    }

    public FFmpeg(String RECURSO) {
        super(RECURSO, "");
    }

    public FFmpeg(String RECURSO, String SALIDA) {
        super(RECURSO, SALIDA);
    }

    public int duración_s_con_fotogramas_$demorado$() throws Exception {
        FFmpeg ffmpeg = new FFmpeg(
                RECURSO
        );
        ffmpeg.FPS = 1;
        ffmpeg.ANCHO = 2;
        ffmpeg.ALTO = 2;
        CarpetaDeRecursos c = new CarpetaDeRecursos(
                CarpetaDeRecursos.TIPO_TMP,
                "calculo de duracion " + new Random().nextInt(100000)
        );
        for (File file : c.ListarArchivos()) {
            file.delete();
        }
        ffmpeg.SALIDA = c.DIRECCIÓN_CARPETA + "\\%07d.jpg";
        ffmpeg.ConvertirDeUnFormatoAOtro();
        int s = c.ListarArchivos().length;
        for (File file : c.ListarArchivos()) {
            file.delete();
        }
        c.ObtenerCarpeta().delete();
        return s;
    }

    public void SubirVolumen(double multiplicador) throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-af");
                add("volume=" + multiplicador);
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    public void recortarTiempoVideo_inicio_fin(int inicio_segundos, int fin_segundos) throws Exception {
        int duración = fin_segundos - inicio_segundos;
        recortarTiempoVideo_inicio_duración(inicio_segundos, duración);
    }

    public void recortarTiempoVideo_inicio_duración(int inicio_segundos, int duración_segundos) throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-ss");
                add(inicio_segundos);
                add("-i");
                add(RECURSO);
                add("-c");
                add("copy");
                add("-t");
                add(duración_segundos);
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    public void invertirTiempoVideo() throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-vf");
                add("reverse");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    public void Watermark(String img) throws Exception {
        Watermark(img, Dupla.MEDIO, Dupla.MEDIO);
    }

    public void Watermark(String img, byte Alin_H, byte Alin_V) throws Exception {
        Watermark(img, 0, Alin_H, Alin_V);
    }

    public void Watermark(String img, int margen, byte Alin_H, byte Alin_V) throws Exception {
        String X = margen + "";
        switch (Alin_H) {
            case Dupla.DERECHA:
                X = "main_w-overlay_w-" + margen;
                break;
            case Dupla.MEDIO:
                X = "(main_w-overlay_w)/2";
                break;
        }
        String Y = margen + "";
        switch (Alin_H) {
            case Dupla.ABAJO:
                Y = "main_h-overlay_h-" + margen;
                break;
            case Dupla.MEDIO:
                Y = "(main_h-overlay_h)/2";
                break;
        }
        Watermark(img, X, Y);
    }

    public void Watermark(String img, int X, int Y) throws Exception {
        Watermark(img, X + "", Y + "");
    }

    public void Watermark(String img, String X, String Y) throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-i");
                add(img);
                add("-filter_complex");
                add("overlay=" + X + ":" + Y);//Abajo derecha
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    public void Reflejo_Horizontal_Arriba() throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-vf");
                add("\"split [main][tmp]; [tmp] crop=iw:ih/2:0:0, vflip [flip]; [main][flip] overlay=0:H/2\"");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        //Parece ser que el true puede ser cambiado por un false
        EjecutarCódigo_cmd(comandos, true);
    }

    public void Reflejo_Horizontal_Abajo() throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-vf");
                add("\"split [main][tmp]; [tmp] crop=iw:ih/2:0:ih/2, vflip [flip]; [main][flip] overlay=0:0\"");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        //Parece ser que el true puede ser cambiado por un false
        EjecutarCódigo_cmd(comandos, true);
    }

    /**
     * The color alice blue with an RGB value of #F0F8FF
     * <br/><br/>
     * <img src="https://trac.ffmpeg.org/raw-attachment/wiki/FancyFilteringExamples/ffplay-mirror.jpg" height="200" width="300">
     */
    public void Espejo_MitadIzquierda() throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-vf");
                add("\"crop=iw/2:ih:0:0,split[left][tmp];[tmp]hflip[right];[left][right] hstack\"");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        //Parece ser que el true puede ser cambiado por un false
        EjecutarCódigo_cmd(comandos, true);
    }

    public void Reflejo_Vertical_Derecha() throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-vf");
                add("\"crop=iw/2:ih:iw/2:0,split[left][tmp];[tmp]hflip[right];[right][left] hstack\"");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        //Parece ser que el true puede ser cambiado por un false
        EjecutarCódigo_cmd(comandos, true);
    }

    //Streams
    public void HacerDirecto_hx264(String SERVER, String PASS) throws Exception {
        HacerDirecto_hx264(SERVER, PASS, RECURSO);
    }

    public void HacerDirecto_hx264(String SERVER, String PASS, String Video) throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-re");
                add("-i");
                add(Video);
                add("-c:v");
                add("libx264");
                add("-crf");
                add(29);
                add("-preset");
                add("veryfast");
                add("-maxrate");
                add("3000k");
                add("-bufsize");
                add("6000k");
                add("-pix_fmt");
                add("yuv420p");
                add("-g");
                add(20);
                add("-c:a");
                add("aac");
                add("-b:a");
                add("160k");
                add("-ac");
                add(2);
                add("-ar");
                add("44100");
                add("-f");
                add("flv");
                add(SERVER + PASS);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    public void HacerDirecto_VideosDeCarpeta(String SERVER, String PASS, String Carpeta) throws Exception {
        File f = new File(Carpeta);
        for (File video : f.listFiles()) {
            HacerDirecto_hx264(SERVER, PASS, video.getPath());
        }
    }

    //Concatenadores
    public void CombinarAudioVideo(String AudioOVideo) throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                add("-i");
                add(AudioOVideo);
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
        System.out.println("Concatenado");
    }

    public void ConcatenarVideos_Opción1_Directo(String[] videos, String salida) throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                for (String video : videos) {
                    add("-i");
                    add("\"" + video + "\"");
                }
                add("-filter_complex");
                add("concat=n=" + videos.length + ":v=1:a=1");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    public void Generar_Loop(int repeticiones) throws Exception {
        Generar_Loop(RECURSO, repeticiones);
    }

    public void Generar_Loop(String video, int repeticiones) throws Exception {
        if (repeticiones < 1) {
            throw new RuntimeException("Error, el número de repeticiones debe ser mayor a 0");
        }
        String[] Repetidor = new String[repeticiones];
        for (int i = 0; i < repeticiones; i++) {
            Repetidor[i] = video;
        }
        ConcatenarVideos_Opción2_CreandoLista(Repetidor);
    }

    public void ConcatenarVideos_Opción2_CreandoLista(ArrayList<String> VideosConcatenar) throws Exception {
        String[] Listvideos = new String[VideosConcatenar.size()];
        for (int i = 0; i < VideosConcatenar.size(); i++) {
            Listvideos[i] = VideosConcatenar.get(i);
        }
        ConcatenarVideos_Opción2_CreandoLista(Listvideos);
    }

    public void ConcatenarVideos_Opción2_CreandoLista(String... VideosConcatenar) throws Exception {
        File file = File.createTempFile("list", ".txt");
        String lista = "";
        for (int i = 0; i < VideosConcatenar.length; i++) {
            String video = VideosConcatenar[i].replace("'", "'\\''");
            lista += "file \'" + video + "\'\n";
        }
        Auxiliar_LectoEscrituraArchivos.EscribirArchivo_ASCII(file, lista);
        ArrayList comandos = new ArrayList() {
            {
                add("-f");
                add("concat");
                add("-safe");
                add(0);
                add("-i");
                add(file.getAbsoluteFile());
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
        file.delete();
    }

    public void ConcatenarSoloAudio(String[] audios) throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                for (String audio : audios) {
                    add("-i");
                    add(audio);
                }
                add("-filter_complex");
                add("concat=n=" + audios.length + ":v=0:a=1");
                add("-vn");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    //Conversores
    public void HacerVideoConSecuenciaDeImagenes() throws Exception {
        HacerVideoConSecuenciaDeImagenes(FPS < 1 ? 25 : FPS);
    }

    public void HacerVideoConSecuenciaDeImagenes(int FPS) throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-framerate");
                add(FPS);
                add("-i");
                add(RECURSO);
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    public void HacerVideo_Imagenes(Object[] imagenes, double[] duraciones, Paint fondo, String Audio, int FPS, boolean inicioNegro, byte TIPO_AJUSTE) throws Exception {
        if (imagenes == null) {
            throw new RuntimeException(
                    "No hay imagenes"
            );
        }
        String secuencia_formato = "\\%07d.png";
        if (duraciones == null) {
            duraciones = new double[imagenes.length];
            for (int i = 0; i < imagenes.length; i++) {
                duraciones[i] = 3;
            }
        }
        if (imagenes.length != duraciones.length) {
            throw new RuntimeException(
                    "La cantidad de imagenes debe ser igual a la cantidad de duraciones"
            );
        }
        if (FPS < 1) {
            FPS = 25;
        }
        if (fondo == null) {
            fondo = Color.BLACK;
        }
        File folder = new File(TMP_DIR + "\\Fotogramas" + (int) (9999 * Math.random()));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        BufferedImage primera = null;
        if (imagenes instanceof BufferedImage[]) {
            primera = (BufferedImage) imagenes[0];
        } else if (imagenes instanceof String[]) {
            primera = LectoEscrituraArchivos.cargar_imagen((String) imagenes[0]);
        }
        FFmpeg FFmpeg = new FFmpeg(folder.getPath() + secuencia_formato, SALIDA);
        if (ANCHO < 0) {
            FFmpeg.ANCHO = primera.getWidth();
        } else {
            FFmpeg.ANCHO = ANCHO;
        }
        if (ALTO < 0) {
            FFmpeg.ALTO = primera.getHeight();
        } else {
            FFmpeg.ALTO = ALTO;
        }

        int n_fotograma = 0;
        BufferedImage salida = new BufferedImage(FFmpeg.ANCHO, FFmpeg.ALTO, 2);
        for (int i = 0; i < imagenes.length; i++) {
            Graphics2D g = salida.createGraphics();
            g.setPaint(fondo);
            g.fillRect(0, 0, salida.getWidth(), salida.getHeight());
            File f = new File(
                    folder.getPath() + "\\" + new Formatter().format(secuencia_formato, n_fotograma++)
            );
            if (i == 0) {
                Filtros_Lineales.Ajuste_Personalizado(primera, salida, TIPO_AJUSTE);
            } else {
                if (imagenes instanceof BufferedImage[]) {
                    Filtros_Lineales.Ajuste_Personalizado(
                            (BufferedImage) imagenes[i], salida, TIPO_AJUSTE
                    );
                } else if (imagenes instanceof String[]) {
                    Filtros_Lineales.Ajuste_Personalizado(LectoEscrituraArchivos.cargar_imagen((String) imagenes[i]), salida, TIPO_AJUSTE);
                }
            }
            LectoEscrituraArchivos.exportar_imagen(f, salida);
            for (int j = 0; j < duraciones[i] * FPS - 1; j++) {
                File copia = new File(
                        folder.getPath() + "\\" + new Formatter().format(secuencia_formato, n_fotograma++)
                );
                Sistema.DuplicarArchivo(f, copia);
            }
        }

        FFmpeg.HacerVideoConSecuenciaDeImagenes(FPS);

        try {
            for (int j = 0; j < 2; j++) {
                for (File listFile : folder.listFiles()) {
                    listFile.delete();
                }
            }
        } catch (Exception ex) {
        }
        folder.delete();
    }

    public void HacerVideoConSecuenciaDeImagenesYAudio(String Audio, int FPS, boolean inicioNegro) throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-framerate");
                add(FPS);
                add("-i");
                add(RECURSO);
                add("-i");
                add(Audio);
                if (inicioNegro) {
                    add("-vf");
                    add("fade");
                }
                add("-preset");
                add("veryfast");
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    public void ConvertirDeUnFormatoAOtro() throws Exception {
        ArrayList comandos = new ArrayList() {
            {
                add("-i");
                add(RECURSO);
                AñadirComandosDePropiedadesDeSalida(this);
            }
        };
        EjecutarCódigo_cmd(comandos, true);
    }

    /**
     * Con este metodo se agregan los comandos -r, -crf, -y para la salida del
     * archivo
     */
    public void AñadirComandosDePropiedadesDeSalida(ArrayList comandos) {
        if (ANCHO > 0 || ALTO > 0) {
            if (ANCHO % 2 != 0) {
                ANCHO--;
            }
            if (ALTO % 2 != 0) {
                ALTO--;
            }
            comandos.add("-vf");
            comandos.add("scale=" + (ANCHO <= 0 ? -1 : ANCHO) + ":" + (ALTO <= 0 ? -1 : ALTO));
        }
        FiltrosConvolución(comandos);
        if (FPS > 0) {
            comandos.add("-r");
            comandos.add(FPS);
        }
        if (CRF >= 0) {
            comandos.add("-crf");
            comandos.add(CRF);
        }
        if (SALIDA != null) {
            if (DIRECCIÓN_EJECUTABLE.equals(DIRECCIÓN_FFMPEG)) {
                comandos.add("-y");
                comandos.add(SALIDA);
                CrearCarpeta();
            }
        }
    }

    void FiltrosConvolución(ArrayList comandos) {
        if (CONVOLUCIÓN_AFILAR_BORDES > 0 && CONVOLUCIÓN_AFILAR_BORDES <= 3) {
            comandos.add("-vf");
            String Matriz = "";
            switch (CONVOLUCIÓN_AFILAR_BORDES) {
                case 1:
                    Matriz = ""
                            + "0 -1 0 "
                            + "-1 5 -1 "
                            + "0 -1 0";
                    break;
                case 2:
                    Matriz = ""
                            + "0 0 -1 0 0 "
                            + "0 0 -1 0 0 "
                            + "-1 -1 9 -1 -1 "
                            + "0 0 -1 0 0 "
                            + "0 0 -1 0 0";
                    break;
                case 3:
                    Matriz = ""
                            + "0 0 0 -1 0 0 0 "
                            + "0 0 0 -1 0 0 0 "
                            + "0 0 0 -1 0 0 0 "
                            + "-1 -1 -1 13 -1 -1 -1 "
                            + "0 0 0 -1 0 0 0 "
                            + "0 0 0 -1 0 0 0 "
                            + "0 0 0 -1 0 0 0";
                    break;
            }
            String R, G, B, A;
            R = G = B = A = Matriz;
            String exp = "'" + R + ":" + G + ":" + B + ":" + A + "'";
            comandos.add("convolution=" + exp);
        }
    }
}
