package Principal;

import JAVH.ExifTool;
import JAVH.FFmpeg;
import java.io.File;
import java.util.ArrayList;

import static Principal.ConstantesGeneradoresVideo.*;

public class ConcatenarTodosLosVideos implements ConstantesGeneradoresVideo {

    final static ArrayList<File> Video_Mp4 = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        String dir = "C:\\Users\\guillermo\\Documents\\Mega downloader\\Bring Me The Horizon";
        File CarpetaRaíz = new File(dir);
        CargarFullAlbums(CarpetaRaíz);
        String[] Aux = CarpetaRaíz.getPath().split("\\\\");
        String nombre = CarpetaRaíz.getPath() + "\\" + Aux[Aux.length - 1] + " {FULL DISCOGRAPHY}.mp4";
        System.out.println(nombre);
        String[] lista = new String[Video_Mp4.size()];
        for (int i = 0; i < lista.length; i++) {
            lista[i] = Video_Mp4.get(i).getPath();
        }
        FFmpeg FFmpeg = new FFmpeg("", nombre);
        FFmpeg.ConcatenarVideos_Opción2_CreandoLista(lista);
    }

    public static void Concatenar(ArrayList<File> rutas, String nombre) throws Exception {
        double CantidadDeVideos;
        {
            double duraciónTotal = 0;
            for (int i = 0; i < rutas.size(); i++) {
                ExifTool exifTool = new ExifTool(rutas.get(i).getPath());
                duraciónTotal += exifTool.ObtenerDuraciónSegundos();
            }
            CantidadDeVideos = (int) Math.ceil(duraciónTotal / Duración_Máxima_Segundos);
        }

        if (CantidadDeVideos > 1) {
            int MáxVideosPorParte = (int) Math.ceil(rutas.size() / CantidadDeVideos);
            System.out.println("Cantidad de videos: " + CantidadDeVideos);
            System.out.println("Máximo videos por parte: " + MáxVideosPorParte);
            System.out.println("Videos:");
            for (File ruta : rutas) {
                System.out.println(ruta.getPath());
            }
            for (int i = 0; i < CantidadDeVideos; i++) {
                String[] lista;
                {
                    ArrayList<String> video = new ArrayList<>();
                    for (int j = 0; j < MáxVideosPorParte; j++) {
                        if (rutas.isEmpty()) {
                            break;
                        }
                        video.add(rutas.get(0).getPath());
                        rutas.remove(0);
                    }
                    lista = new String[video.size()];
                    for (int j = 0; j < lista.length; j++) {
                        lista[j] = video.get(j);
                    }
                }
                System.out.println("Videos para la parte " + (i + 1));
                for (String string : lista) {
                    System.out.println(string);
                }
                System.out.println("Videos restantes:");
                for (File ruta : rutas) {
                    System.out.println(ruta.getPath());
                }
                FFmpeg FFmpeg = new FFmpeg("", nombre.replace(".mp4", "") + " Part. " + (i + 1) + ".mp4");
                FFmpeg.ConcatenarVideos_Opción2_CreandoLista(lista);
            }
        } else {
            String[] lista = new String[rutas.size()];
            for (int i = 0; i < lista.length; i++) {
                lista[i] = rutas.get(i).getPath();
            }
            FFmpeg FFmpeg = new FFmpeg("", nombre);
            FFmpeg.ConcatenarVideos_Opción2_CreandoLista(lista);
        }
    }

    static class Mapa_VideoInfo extends ArrayList<VideoInfo> {

        public Mapa_VideoInfo(ArrayList<File> rutas) {
            try {
                for (int i = 0; i < rutas.size(); i++) {
                    String ruta = rutas.get(i).getPath();
                    ExifTool exifTool = new ExifTool(ruta);
                    double d = exifTool.ObtenerDuraciónSegundos();
                    add(new VideoInfo(ruta, d));
                    System.out.println(ConvertirTiempo_Cadena(d) + " - " + ruta);
                }
            } catch (Exception e) {
            }
        }

        public String Ruta_Cabeza() {
            return get(0).ruta;
        }

        public double Duración_Cabeza() {
            return get(0).duración;
        }

        public void Eliminar_Cabeza() {
            remove(0);
        }

        public double duraciónTotal() {
            double d = 0;
            for (VideoInfo video : this) {
                d += video.duración;
            }
            return d;
        }

    }

    static class VideoInfo {

        String ruta;
        double duración;

        public VideoInfo(String ruta, double duración) {
            this.ruta = ruta;
            this.duración = duración;
        }

    }

    public static void Concatenar_segúnDuraciones(ArrayList<File> rutas, String nombre) throws Exception {
        Mapa_VideoInfo videoInfo = new Mapa_VideoInfo(rutas);
        rutas.clear();
        double MínimoDeVideos;
        {
            double razón = videoInfo.duraciónTotal() / Duración_Máxima_Segundos;
            MínimoDeVideos = (int) Math.ceil(razón);
            System.out.println("Mínimo de videos: " + MínimoDeVideos);
        }
        double duraciónMedia = videoInfo.duraciónTotal() / MínimoDeVideos;
        System.out.println("Total videos: " + videoInfo.size());
        System.out.println("Duración total: " + ConvertirTiempo_Cadena(videoInfo.duraciónTotal()));
        System.out.println("Duración media: " + ConvertirTiempo_Cadena(duraciónMedia));
        if (MínimoDeVideos > 1) {
            int MáxVideosPorParte = (int) Math.ceil(videoInfo.size() / MínimoDeVideos);
            System.out.println("Cantidad mínima de videos: " + MínimoDeVideos);
            System.out.println("Máximo videos por parte: " + MáxVideosPorParte);
            int i = 0;
            while (!videoInfo.isEmpty()) {
                int indice = i + 1;
                String[] lista;
                {
                    ArrayList<String> video = new ArrayList<>();
                    {
                        double duraciónVideo = 0;
                        while (true) {
                            if (videoInfo.isEmpty()) {
                                break;
                            }
                            duraciónVideo += videoInfo.Duración_Cabeza();
                            if (duraciónVideo >= duraciónMedia) {
                                if (videoInfo.size() <= 2 && duraciónVideo < Duración_Máxima_Segundos) {
                                    System.out.println("Se agregó un video extra a la duración media: " + videoInfo.Ruta_Cabeza());
                                } else if (!video.isEmpty()) {
                                    duraciónVideo -= videoInfo.Duración_Cabeza();
                                    break;
                                }
                            }
                            video.add(videoInfo.Ruta_Cabeza());
                            videoInfo.Eliminar_Cabeza();
                        }
                        System.out.println("Duración de la parte " + indice + ": " + ConvertirTiempo_Cadena(duraciónVideo));
                        System.out.println("número de videos: " + video.size());
                    }
                    lista = new String[video.size()];
                    for (int j = 0; j < lista.length; j++) {
                        lista[j] = video.get(j);
                    }
                }
                System.out.println("Videos para la parte " + indice);
                for (String string : lista) {
                    System.out.println(string);
                }
                FFmpeg FFmpeg = new FFmpeg(null, nombre.replace(".mp4", "") + " Part. " + indice + ".mp4");
                FFmpeg.ConcatenarVideos_Opción2_CreandoLista(lista);
                i++;
            }
        } else {
            String[] lista = new String[videoInfo.size()];
            for (int i = 0; i < lista.length; i++) {
                lista[i] = videoInfo.Ruta_Cabeza();
                videoInfo.Eliminar_Cabeza();
            }
            FFmpeg FFmpeg = new FFmpeg("", nombre);
            FFmpeg.ConcatenarVideos_Opción2_CreandoLista(lista);
        }
//        System.exit(0);
    }

    public static String ConvertirTiempo_Cadena(double segundos) {
        int horas = (int) (segundos / 60 / 60);
        int minutos = (int) (segundos / 60) % 60;
        segundos %= 60;
        return (horas < 10 ? "0" : "") + horas
                + ":" + (minutos < 10 ? "0" : "") + minutos
                + ":" + (segundos < 10 ? "0" : "")
                + String.format("%.1f", segundos).replace(",", ".");
    }

    public static void CargarFullAlbums(File CarpetaRaíz) {
        File[] carpetas = CarpetaRaíz.listFiles(FiltroCarpetas_S());
        ArrayList<File> ArchivosVideo = new ArrayList<>();
        if (CarpetaRaíz.getPath().toLowerCase().endsWith("- full")) {
            for (File file : CarpetaRaíz.listFiles(FiltroArchivosVideo())) {
                ArchivosVideo.add(file);
            }
        }
        if (!ArchivosVideo.isEmpty()) {
            Video_Mp4.addAll(ArchivosVideo);
        }
        for (File carpeta : carpetas) {
            CargarFullAlbums(carpeta);
        }
    }
}
