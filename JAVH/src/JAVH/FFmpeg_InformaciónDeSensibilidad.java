package JAVH;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FFmpeg_InformaciónDeSensibilidad extends FFmpeg_Espectro {

    public int SENSIBILIDAD = 300;

    public FFmpeg_InformaciónDeSensibilidad(String RECURSO) {
        super(RECURSO, "");
    }

    public float[][] Valores_NodosDeAudio(int NúmeroDeNodos, int FPS) throws Exception {
        return Valores_NodosDeAudio(NúmeroDeNodos, FPS, -1);
    }

    public float[][] Valores_NodosDeAudio(int NúmeroDeNodos, int FPS, int CantidadDeFotogramas) throws Exception {
        Dimension SIZE = new Dimension(NúmeroDeNodos, SENSIBILIDAD);
        this.FPS = FPS;

        if (CantidadDeFotogramas <= 0) {
            FFmpeg_Información información = new FFmpeg_Información(RECURSO);
            CantidadDeFotogramas = información.Apróx_FotogramasQueCabenEnElRecurso(FPS);
        }

        float[][] valNodesWave = new float[CantidadDeFotogramas][NúmeroDeNodos];

        File folder = new File(TMP_DIR + "\\valwave" + (int) (9999 * Math.random()));
        if (!folder.exists()) {
            System.out.println("crear directorio: " + folder.getPath());
            folder.mkdirs();
            System.out.println("directorio creado: " + folder.getPath());
        }
        String Fotogramas = folder.getAbsolutePath() + "\\%07d.png";

        boolean CalcularConFrecuencias = MODO_NODOS == CALCULAR_USANDO_FRECUENCIAS;
        if (CalcularConFrecuencias) {
            FFmpeg_Frecuencia frecuencia = new FFmpeg_Frecuencia(RECURSO, Fotogramas);
            frecuencia.FPS = FPS;
            frecuencia.DIMENSIÓN = SIZE;
            frecuencia.ESCALA_VALORES_AUDIO = Constantes_Frecuencia.ESCALA_CÁLCULO_VAL_AUDIO_SQUAREROOT;
            frecuencia.ESCALA_FRECUENCIA = Constantes_Frecuencia.ESCALA_FRECUENCIA_LOGARITHMIC;
            frecuencia.GenerarVideoDeLaFrecuencia();
        } else {
            FFmpeg_OndasAudio ondasAudio = new FFmpeg_OndasAudio(RECURSO, Fotogramas);
            ondasAudio.FPS = FPS;
            ondasAudio.DIMENSIÓN = SIZE;
            ondasAudio.EspectroOndasAudio_Mono();
        }
        int nframe = 0;
        for (File listFile : folder.listFiles()) {
            BufferedImage frame = ImageIO.read(listFile);
            for (int node = 0; node < NúmeroDeNodos; node++) {
                float val = 0;
                for (int i = 0; i < SENSIBILIDAD; i++) {
                    int alpha = frame.getRGB(node, i) >>> 24;
                    if (alpha > 0) {
                        if (CalcularConFrecuencias) {
                            val = (float) (SENSIBILIDAD - i) / SENSIBILIDAD;
                        } else {
                            float sm = SENSIBILIDAD / 2f;
                            val = (sm - i) / sm;
                        }
                        valNodesWave[nframe][node] = val;
                        break;
                    }
                }
            }
            nframe++;
            if (nframe % 23 == 0) {
                System.out.println("read wave values: Frame " + nframe + " of " + CantidadDeFotogramas);
            }
            if (nframe >= CantidadDeFotogramas) {
                break;
            }
        }

        new Thread() {
            {
                start();
            }

            @Override
            public void run() {
                for (File listFile : folder.listFiles()) {
                    listFile.delete();
                }
                folder.delete();
            }
        };
        return valNodesWave;
    }

    public float[] VolumenValor_Fotogramas(int FPS) throws Exception {
        return VolumenValor_Fotogramas(FPS, -1);
    }

    public BufferedImage obtenerImagen_EspectroVolumen(int ancho, int alto) throws Exception {
        File file = File.createTempFile("EspectroDeVolumen", ".png");
        FFmpeg_OndasAudio ondasAudio = new FFmpeg_OndasAudio(RECURSO, file.getPath());
        ondasAudio.DIMENSIÓN = new Dimension(ancho, alto);
        ondasAudio.FPS = FPS;
        ondasAudio.COMPAND = true;
        ondasAudio.ImagenEspectroVolumen_Mono();
        BufferedImage espectroVolumen = ImageIO.read(file);
        file.delete();
        return espectroVolumen;
    }

    public float[] VolumenValor_Fotogramas(int FPS, int CantidadFotogramas) throws Exception {
        return VolumenValor_Fotogramas(FPS, CantidadFotogramas, false);
    }

    public float[] VolumenValor_Fotogramas(int FPS, int CantidadFotogramas, boolean compand) throws Exception {
        File file = File.createTempFile("EspectroDeVolumen", ".png");
        int ancho;
        if (CantidadFotogramas <= 0) {
            FFmpeg_Información información = new FFmpeg_Información(RECURSO);
            ancho = información.Apróx_FotogramasQueCabenEnElRecurso(FPS);
        } else {
            ancho = CantidadFotogramas;
        }

        FFmpeg_OndasAudio ondasAudio = new FFmpeg_OndasAudio(RECURSO, file.getPath());
        ondasAudio.DIMENSIÓN = new Dimension(ancho, SENSIBILIDAD);
        ondasAudio.FPS = FPS;
        if (compand) {
            ondasAudio.COMPAND = true;
        }
        ondasAudio.ImagenEspectroVolumen_Mono();

        BufferedImage espectroVolumen = ImageIO.read(file);
        float[] Frames = new float[ancho];

        for (int col = 0; col < espectroVolumen.getWidth(); col++) {
            float val = 0;
            for (int fil = 0; fil < espectroVolumen.getHeight(); fil++) {
                int alpha = espectroVolumen.getRGB(col, fil) >>> 24;
                if (alpha > 0) {
                    float sm = (SENSIBILIDAD / 2f);
                    val = (sm - fil) / sm;
                    Frames[col] = val;
                    break;
                }
            }
        }
        file.delete();
        return Frames;
    }
}
