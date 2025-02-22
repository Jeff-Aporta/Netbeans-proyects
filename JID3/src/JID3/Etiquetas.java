package JID3;

import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientaDeImagen.Filtros_Lineales;
import JAVH.ExifTool;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.blinkenlights.jid3.*;
import org.blinkenlights.jid3.v1.ID3V1Tag;
import org.blinkenlights.jid3.v1.ID3V1_0Tag;
import org.blinkenlights.jid3.v1.ID3V1_1Tag;
import org.blinkenlights.jid3.v2.*;
import org.blinkenlights.jid3.v2.APICID3V2Frame.PictureType;

public class Etiquetas {

    public int LadoMáximoCover = 300;

    static String[] ExtensionesImagen_Cover = {".jpeg", ".jpg", ".png", ".gif"};
    static String[] PosiblesNombres_cover = {"cover", "portada", "delantera", "caratula", "front", "folder"};

    protected String PATH;
    public String TÍTULO, GÉNERO, ARTISTA, ALBÚM, COMENTARIO, AÑO, TRACK;
    public double SEGUNDOS;

    public static void main(String[] args) throws Exception {
        String rutaCarpeta = "C:\\Users\\57310\\Documents\\MEGAsync Downloads\\Ska P";
        File carpeta = new File(rutaCarpeta);
        etiquetasACDC(carpeta);
    }

    static void etiquetasACDC(File carpeta) throws Exception {
        for (File archivo : carpeta.listFiles()) {
            if (archivo.isDirectory()) {
                etiquetasACDC(archivo);
                continue;
            }
            if (!archivo.getPath().endsWith(".mp3")) {
                continue;
            }
            String audio = archivo.getPath();
            Etiquetas etiqueta = new Etiquetas(audio, false);
            etiqueta.TÍTULO = archivo.getName().substring(5);
            etiqueta.ModificarTags_EnArchivo(ImagenDeCaratulaEnCarpeta(archivo));
        }
    }

    static void ModificarEtiquetasCarpeta(File carpeta) throws Exception {
        String últimoGénero = "";
        for (File archivo : carpeta.listFiles()) {
            if (archivo.isDirectory()) {
                ModificarEtiquetasCarpeta(archivo);
                continue;
            }
            if (!archivo.getPath().endsWith(".mp3")) {
                continue;
            }
            String audio = archivo.getPath();
            Etiquetas etiqueta = new Etiquetas(audio, false);
            System.out.println("Albúm: " + etiqueta.ALBÚM);
            System.out.println("Artista: " + etiqueta.ARTISTA);
            if (etiqueta.TÍTULO.isEmpty()) {
                String titulo = archivo.getName();
                titulo = titulo.substring(0, titulo.lastIndexOf("."));
                titulo = titulo.replace("_", " ");
                etiqueta.TÍTULO = titulo;
                System.out.println("Titulo renombrado");
            }
            System.out.println("Título: " + etiqueta.TÍTULO);
            System.out.println("Año: " + etiqueta.AÑO);
            System.out.println("Comentario: " + etiqueta.COMENTARIO);
            if (etiqueta.ARTISTA.isEmpty()) {
                etiqueta.ARTISTA = "Authorless";
                System.out.println("Artista renombrado");
            }
            if (etiqueta.GÉNERO.isEmpty() || etiqueta.GÉNERO.equals("None")) {
                etiqueta.GÉNERO = últimoGénero;
                System.out.println("Género renombrado");
            } else {
                últimoGénero = etiqueta.GÉNERO;
            }
            System.out.println("Género: " + etiqueta.GÉNERO);
            System.out.println("Segundos: " + etiqueta.SEGUNDOS);
            System.out.println("Track: " + etiqueta.TRACK);
            etiqueta.ModificarTags_EnArchivo(null);
        }
    }

    public Etiquetas(String RutaArchivoMp3, boolean CargarSegundos) throws Exception {
        this(new File(RutaArchivoMp3), CargarSegundos);
    }

    public Etiquetas() {
        PATH = TRACK = TÍTULO = GÉNERO = ARTISTA = ALBÚM = COMENTARIO = AÑO = "";
    }

    public Etiquetas(File ArchivoMp3, boolean CargarSegundos) throws Exception {
        PATH = ArchivoMp3.getPath();
        TRACK = TÍTULO = GÉNERO = ARTISTA = ALBÚM = COMENTARIO = AÑO = "";
        if (CargarSegundos || !ArchivoMp3.getPath().toLowerCase().endsWith("mp3")) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        CargarEtiquetasExifTool(ArchivoMp3);
                    } catch (Exception ex) {
                    }
                }
            };
            t.start();
            long tref = System.currentTimeMillis();
            while (t.isAlive()) {
                if (System.currentTimeMillis() - tref > 5000) {
                    break;
                }
                Thread.sleep(500);
                System.out.println("Exiftool (Cargando etiquetas): " + ((System.currentTimeMillis() - tref) / 1000.0));
            }
        } else {
            try {
                CargarEtiquetasJID3(ArchivoMp3, CargarSegundos);
            } catch (Exception ex) {
                Thread t = new Thread() {
                    @Override
                    public void run() {
                        try {
                            CargarEtiquetasExifTool(ArchivoMp3);
                        } catch (Exception ex) {
                        }
                    }
                };
                t.start();
                long tref = System.currentTimeMillis();
                while (t.isAlive()) {
                    if (System.currentTimeMillis() - tref > 5000) {
                        break;
                    }
                    Thread.sleep(500);
                    System.out.println("Exiftool (Cargando etiquetas): " + ((System.currentTimeMillis() - tref) / 1000.0));
                }
            }
        }
        TÍTULO = EliminarCaracteresEspeciales(TÍTULO);
        GÉNERO = EliminarCaracteresEspeciales(GÉNERO);
        ARTISTA = EliminarCaracteresEspeciales(ARTISTA);
        ALBÚM = EliminarCaracteresEspeciales(ALBÚM);
        AÑO = EliminarCaracteresEspeciales(AÑO);
        COMENTARIO = EliminarCaracteresEspeciales(COMENTARIO);
    }

    public Etiquetas(String TITULO, String GENERO, String ARTISTA, String ALBUM, String AÑO, String COMENTARIO, String TRACK, int SEGUNDOS) {
        this.TÍTULO = TITULO;
        this.GÉNERO = GENERO;
        this.ARTISTA = ARTISTA;
        this.ALBÚM = ALBUM;
        this.SEGUNDOS = SEGUNDOS;
        this.COMENTARIO = COMENTARIO;
        this.AÑO = AÑO;
        this.TRACK = TRACK;
    }

    @Override
    public String toString() {
        return "Etiquetas{" + "TITULO = " + TÍTULO + ", GENERO = " + GÉNERO + ", ARTISTA = " + ARTISTA + ", ALBUM = " + ALBÚM + ", DURACI\u00d3N_S = " + SEGUNDOS + ", A\u00d1O = " + AÑO + ", TRACK_No = " + TRACK + '}';
    }

    public void CargarSegundos() {
        try {
            ExifTool exifTool = new ExifTool(PATH);
            SEGUNDOS = exifTool.ObtenerDuraciónSegundos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Problema al leer los segundos");
        }
    }

    public void CargarEtiquetasExifTool(File archivoAudio) throws Exception {
        ExifTool exifTool = new ExifTool(PATH);
        SEGUNDOS = exifTool.ObtenerDuraciónSegundos();
        TÍTULO = exifTool.ConsultarMetadato("Title", true);
        GÉNERO = exifTool.ConsultarMetadato("Genre", true);
        ARTISTA = exifTool.ConsultarMetadato("Artist", true);
        ALBÚM = exifTool.ConsultarMetadato("Album", true);
        COMENTARIO = exifTool.ConsultarMetadato("Comment", true);
        AÑO = exifTool.ConsultarMetadato("Year", true);
        TRACK = exifTool.ConsultarMetadato("Track", true);
        if (TÍTULO.isEmpty()) {
            CargarEtiquetasJID3(archivoAudio, false);
        }
    }

    public void CargarEtiquetasJID3(File archivoAudio, boolean CargarSegundos) throws Exception {
        MediaFile mediaFile = new MP3File(archivoAudio);
        for (Object obj : mediaFile.getTags()) {
            if (obj instanceof ID3V1_0Tag) {
                ID3V1_0Tag Tag = (ID3V1_0Tag) obj;
                try {
                    TÍTULO = Tag.getTitle().trim();
                } catch (Exception e) {
                }
                try {
                    GÉNERO = Tag.getGenre().toString().trim();
                } catch (Exception e) {
                }
                try {
                    ARTISTA = Tag.getArtist().trim();
                } catch (Exception e) {
                }
                try {
                    ALBÚM = Tag.getAlbum().trim();
                } catch (Exception e) {
                }
                try {
                    COMENTARIO = Tag.getComment().trim();
                } catch (Exception e) {
                }
                try {
                    AÑO = Tag.getYear();
                } catch (Exception e) {
                }
            } else if (obj instanceof ID3V2_3_0Tag) {
                ID3V2_3_0Tag Tag = (ID3V2_3_0Tag) obj;
                try {
                    TÍTULO = Tag.getTitle().trim();
                } catch (Exception e) {
                }
                try {
                    GÉNERO = Tag.getGenre().trim();
                } catch (Exception e) {
                }
                try {
                    ARTISTA = Tag.getArtist().trim();
                } catch (Exception e) {
                }
                try {
                    ALBÚM = Tag.getAlbum().trim();
                } catch (Exception e) {
                }
                try {
                    COMENTARIO = Tag.getComment().trim();
                } catch (Exception e) {
                }
                try {
                    AÑO = Tag.getYear() + "";
                } catch (Exception e) {
                }
                try {
                    TRACK = Tag.getTrackNumber() + "";
                } catch (Exception e) {
                }
            }
        }
        if (CargarSegundos) {
            CargarSegundos();
        }
    }

    public static String EliminarCaracteresEspeciales(String Nombre) {
        try {
            ArrayList<Character> nombre = new ArrayList<>();
            for (char c : Nombre.toCharArray()) {
                if (c <= 255 && c > 0) {
                    nombre.add(c);
                }
            }
            char[] c = new char[nombre.size()];
            for (int i = 0; i < c.length; i++) {
                c[i] = nombre.get(i);
            }
            return new String(c);
        } catch (Exception e) {
            return Nombre;
        }
    }

    public void ModificarTags_EnArchivo() throws Exception {
        ModificarTags_EnArchivo(ImagenDeCaratulaEnCarpeta(), this);
    }

    public void ModificarTags_EnArchivo(BufferedImage cover) throws Exception {
        ModificarTags_EnArchivo(cover, this);
    }

    public void ModificarTags_EnArchivo(BufferedImage cover, Etiquetas e) throws Exception {
        ModificarTags_EnArchivo(cover, e.TÍTULO, e.ARTISTA, e.ALBÚM, e.COMENTARIO, e.GÉNERO, e.TRACK, e.AÑO);
    }

    public void ModificarTags_EnArchivo(BufferedImage cover, String titulo, String artista, String album, String comentario, String genero, String track, String año) throws Exception {
        if (artista != null) {
            ARTISTA = artista;
        }
        if (album != null) {
            ALBÚM = album;
        }
        if (titulo != null) {
            TÍTULO = titulo;
        }
        if (comentario != null) {
            COMENTARIO = comentario;
        }
        if (genero != null) {
            GÉNERO = genero;
        }
        if (track != null) {
            TRACK = track;
        }
        if (año != null) {
            AÑO = año;
        }
        ModificarTagsEnArchivo(cover);
    }

    public void ModificarTagsEnArchivo(BufferedImage cover) throws Exception {
        try {
            ID3V1Tag id3V1Tag = new ID3V1_1Tag();
            ID3V2_3_0Tag id3V2Tag = new ID3V2_3_0Tag();
            if (ARTISTA != null) {
                id3V1Tag.setArtist(ARTISTA);
                id3V2Tag.setArtist(ARTISTA);
            }
            if (ALBÚM != null) {
                id3V1Tag.setAlbum(ALBÚM);
                id3V2Tag.setAlbum(ALBÚM);
            }
            if (TÍTULO != null) {
                id3V1Tag.setTitle(TÍTULO);
                id3V2Tag.setTitle(TÍTULO);
            }
            if (COMENTARIO != null) {
                id3V1Tag.setComment(COMENTARIO);
                id3V2Tag.setComment(COMENTARIO);
            }
            if (GÉNERO != null) {
                id3V2Tag.setGenre(GÉNERO);
            }
            if (TRACK != null) {
                try {
                    id3V2Tag.setTrackNumber(Integer.parseInt(TRACK));
                } catch (Exception e) {
                }
            }
            if (AÑO != null) {
                id3V1Tag.setYear(AÑO);
                try {
                    id3V2Tag.setYear(Integer.parseInt(AÑO));
                } catch (Exception e) {
                }
            }
            if (cover != null) {
                {
                    int lado = Math.min(cover.getWidth(), cover.getHeight());
                    if (lado > LadoMáximoCover) {
                        lado = LadoMáximoCover;
                    }
                    cover = Filtros_Lineales.EscalarGranCalidad(cover, lado, lado);
                }
                APICID3V2Frame myAPIC = new APICID3V2Frame(
                        "image/jpg", PictureType.FrontCover, "", ImageToByteArray(cover)
                );
                id3V2Tag.addAPICFrame(myAPIC);
            }

            MP3File mp3File = new MP3File(new File(PATH));
            mp3File.setID3Tag(id3V1Tag);
            mp3File.setID3Tag(id3V2Tag);
            mp3File.sync();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("cannot write ID3 tags to file " + PATH + "; track: " + TRACK + "; reason: " + ex, ex);
        }
    }

    public static byte[] ImageToByteArray(BufferedImage imagen) throws Exception {
        BufferedImage cover = new BufferedImage(
                imagen.getWidth(), imagen.getHeight(), BufferedImage.TYPE_INT_RGB
        );
        cover.getGraphics().drawImage(imagen, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(cover, "jpg", baos);
        byte[] b = baos.toByteArray();
        return b;
    }

    public static byte[] ImageToByteArray(File imageFile) throws Exception {
        return ImageToByteArray(ImageIO.read(imageFile));
    }

    public BufferedImage ImagenDeCaratulaEnCarpeta() throws Exception {
        File f = LocalizarCaratula();
        BufferedImage caratula = ImageIO.read(f);
        int lado = Math.min(caratula.getHeight(), caratula.getWidth());
        BufferedImage retorno = new BufferedImage(lado, lado, 2);
        Filtros_Lineales.Ajuste_Rellenar(caratula, retorno);
        if ((double) caratula.getWidth() / caratula.getHeight() != 1) {
            LectoEscrituraArchivos.exportar_imagen(retorno, f);
        }
        return retorno;
    }

    public File LocalizarCaratula() throws Exception {
        return LocalizarCaratula(new File(PATH));
    }

    public static BufferedImage ImagenDeCaratulaEnCarpeta(File ArchivoAudio) throws Exception {
        return ImageIO.read(LocalizarCaratula(ArchivoAudio));
    }

    public static File LocalizarCaratula(File ArchivoAudio) throws Exception {
        ArrayList<File> Postulados = new ArrayList();
        {
            File folder = new File(ArchivoAudio.getPath().replace("\\" + ArchivoAudio.getName(), ""));
            System.out.println(folder);
            for (String ext : ExtensionesImagen_Cover) {
                Postulados.addAll(
                        Arrays.asList(
                                folder.listFiles(
                                        (File file) -> file.getName().toLowerCase().endsWith(ext)
                                )
                        )
                );
            }
        }
        {
            if (Postulados.isEmpty()) {
                throw new RuntimeException("No se pudo encontrar la caratula de:\n" + ArchivoAudio.getPath());
            }
            for (String PosibleNombre : PosiblesNombres_cover) {
                for (File Postulado : Postulados) {
                    if (Postulado.getName().toLowerCase().contains(PosibleNombre)) {
                        return Postulado;
                    }
                }
            }
            return Postulados.get(0);
        }
    }
}
