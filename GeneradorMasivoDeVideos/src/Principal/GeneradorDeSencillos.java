package Principal;

import HerramientaArchivos.BibliotecaArchivos;
import HerramientasGUI.VentanaGráfica;
import JID3.Etiquetas;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import static Principal.ConstantesGeneradoresVideo.*;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneradorDeSencillos implements ConstantesGeneradoresVideo {

    static ArrayList<File> ArchivosMp3 = new ArrayList<>();

    static byte PROYECTO = ESPECTRO_CIRCULAR;
    public static int actual = 0, total = 0;
    public static int hilos = 1;

    public static void main(String[] args) throws Exception {
        String dir;
        switch (PROYECTO) {
            case YOUTUBE:
                dir = "C:\\Users\\Àngel\\Documents\\Musica\\Youtube Audio Library";
                break;
            case ESPECTRO_CIRCULAR:
                dir = "C:\\YAL";
                break;
            default:
                throw new RuntimeException("No se reconoce el tipo de proyecto");
        }
        GenerarSencillos(dir);
    }

    public static void GenerarSencillos(String dir) throws Exception {
        CargarArchivosAudio(new File(dir));
        total = ArchivosMp3.size();
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < hilos; i++) {
            threads.add(new Thread() {
                {
                    start();
                }

                @Override
                public void run() {
                    try {
                        IniciarGenerador();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Thread.sleep(1000);
        }
        while (true) {
            boolean esperar = false;
            for (Thread thread : threads) {
                esperar |= thread.isAlive();
            }
            if (!esperar) {
                break;
            }
            Thread.sleep(2 * 1000);
        }
    }

    public static String[] GenerarRutasVideo(File archivoMp3) throws Exception {
        return GenerarRutasVideo(GenerarNombreCarpeta(archivoMp3), archivoMp3);
    }

    public static String[] GenerarRutasVideo(String[] path, File archivoMp3) {
        String Nombre = path[0];
        String Carpeta = path[1];
        String rutaVideoBase = archivoMp3.getPath().replace(archivoMp3.getName(), Nombre);
        String rutaVideoFinal;
        switch (PROYECTO) {
            case YOUTUBE:
            case ESPECTRO_CIRCULAR:
            case SIMPLE_CRONOMETRO:
            case SIMPLE_SIN_CRONOMETRO:
                rutaVideoFinal = archivoMp3.getPath().replace(archivoMp3.getName(), Carpeta + "\\" + Nombre);
                break;
            default:
                throw new RuntimeException("Proyecto no reconocido");
        }
        return new String[]{rutaVideoBase, rutaVideoFinal};
    }

    public static String[] GenerarNombreCarpeta(File archivoMp3) throws Exception {
        return GenerarNombreCarpeta(archivoMp3, new Etiquetas(archivoMp3, false));
    }

    /**
     * Se encarga de organizar el nombre del archivo de video.
     */
    public static String[] GenerarNombreCarpeta(File archivoMp3, Etiquetas etiquetas) {
        String Nombre;
        String Carpeta;
        Carpeta = archivoMp3.getPath().replace("\\" + archivoMp3.getName(), "");
        String[] Aux = Carpeta.split("\\\\");
        Carpeta = Aux[Aux.length - 1];
        switch (PROYECTO) {
            case YOUTUBE:
                Nombre = etiquetas.TÍTULO + " - " + etiquetas.ARTISTA + " - " + Carpeta;
                etiquetas.COMENTARIO = Carpeta.trim();
                break;
            case ESPECTRO_CIRCULAR:
            case SIMPLE_CRONOMETRO:
            case SIMPLE_SIN_CRONOMETRO:
                String extra = etiquetas.ALBÚM;
                Nombre = etiquetas.TÍTULO + " - " + etiquetas.ARTISTA + " - " + extra;
                if (!etiquetas.AÑO.isEmpty()) {
                    extra += "\n(" + etiquetas.AÑO + ")";
                }
                etiquetas.COMENTARIO = extra;
                break;
            default:
                throw new RuntimeException("Proyecto no reconocido");
        }
        Nombre = LimpiarNombreDeCaracteresNoVálidos(Nombre);
        if (Nombre.length() > 92) {
            Nombre = Nombre.substring(0, 91 - 4);
        }
        Nombre += ".mp4";
        return new String[]{Nombre, Carpeta};
    }

    /**
     * Creación de un hilo que genera videos hasta que la lista de archivos en
     * cola este vacía.
     */
    public static void IniciarGenerador() {
        VentanaGráfica ventana = new VentanaGráfica("Generador de videos", true, false) {
            {
                ColorFondo(Color.BLACK);
                ProporciónRedimensionar(16, 9);
            }

            @Override
            public void mouseClicked(MouseEvent me) {
                super.mouseClicked(me);
                boolean clicDerecho = me.getButton() == MouseEvent.BUTTON3;
                if (clicDerecho) {
                    GenerarMenúCabecera(me);
                }
            }
        };
        while (!ArchivosMp3.isEmpty()) {
            try {
                File archivoMp3 = ArchivosMp3.get(0);
                ArchivosMp3.remove(0);
                String progreso = "Archivo " + (++actual) + " de " + total + " - " + (100f * actual / total) + "%";
                System.out.println(progreso);
                System.out.println("Archivo cargado: " + archivoMp3.getPath());

                Etiquetas etiquetas = new Etiquetas(archivoMp3, false);
                if (etiquetas.TÍTULO.isEmpty()) {
                    Etiquetas etiquetastemp = new Etiquetas(archivoMp3, true);
                    etiquetas.TÍTULO = etiquetastemp.TÍTULO;
                    etiquetas.ALBÚM = etiquetastemp.ALBÚM;
                    etiquetas.ARTISTA = etiquetastemp.ARTISTA;
                    etiquetas.AÑO = etiquetastemp.AÑO;
                    etiquetas.SEGUNDOS = etiquetastemp.SEGUNDOS;
                }
                String[] path = GenerarNombreCarpeta(archivoMp3, etiquetas);
                String[] rutasVideo = GenerarRutasVideo(path, archivoMp3);

                String rutaVideoFinal = rutasVideo[1];

                File videofinal = new File(
                        rutaVideoFinal
                                .replace("[", "(")
                                .replace("]", ")")
                );
                File videofinalRenombrado = new File(
                        videofinal.getPath().replace(
                                videofinal.getName(),
                                videofinal.getName()
                                        .replace("-", "—")
                                        .replace("(", "{")
                                        .replace(")", "}")
                                        .replace("[", "{")
                                        .replace("]", "}")
                        )
                );
                {//Creación de la carpeta si es inexistente
                    String Nombre = path[0];
                    if (videofinal.exists() || videofinalRenombrado.exists()) {
                        System.err.println("El video ya existe: " + videofinal.getPath());
                        continue;
                    } else if (!new File(rutaVideoFinal.replace("\\" + Nombre, "")).exists()) {
                        new File(rutaVideoFinal.replace("\\" + Nombre, "")).mkdirs();
                    }
                    System.out.println(videofinal.getPath());
                    System.out.println(videofinalRenombrado.getPath());
                }

                if (!ventana.isVisible()) {
                    ventana.setVisible(true);
                }
                ventana.setTitle(videofinal.getName() + " | " + progreso);
                System.out.println("Se procederá a generar el video base");
                switch (PROYECTO) {
                    case YOUTUBE:
                        ProyectoVideo.Proyecto_Youtube_Plasma(
                                archivoMp3.getPath(), videofinal.getPath(), etiquetas.COMENTARIO, ventana
                        );
                        break;
                    case ESPECTRO_CIRCULAR:
                        ProyectoVideo.Proyecto_Album(
                                archivoMp3.getPath(), videofinal.getPath(), etiquetas.COMENTARIO, ventana
                        );
                        break;
                    case SIMPLE_CRONOMETRO:
                        ProyectoVideo.Proyecto_SimpleConCronometro(
                                archivoMp3.getPath(), videofinal.getPath(), etiquetas.COMENTARIO, ventana
                        );
                        break;
                    case SIMPLE_SIN_CRONOMETRO:
                        ProyectoVideo.Proyecto_SimpleSinCronometro(
                                archivoMp3.getPath(), videofinal.getPath(), etiquetas.COMENTARIO, ventana
                        );
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        actual = 0;
        ventana.dispose();
    }

    public static void CargarArchivosAudio(File CarpetaRaíz) {
        File[] carpetas = CarpetaRaíz.listFiles(FiltroCarpetas());
        for (File file : CarpetaRaíz.listFiles(FiltroArchivosAudio())) {
            ArchivosMp3.add(file);
        }
        for (File carpeta : carpetas) {
            CargarArchivosAudio(carpeta);
        }
    }

}
