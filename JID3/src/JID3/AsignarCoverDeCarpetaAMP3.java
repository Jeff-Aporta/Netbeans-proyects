package JID3;

import HerramientaDeImagen.Filtros_Lineales;
import HerramientasGUI.NimbusModificado;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class AsignarCoverDeCarpetaAMP3 {

    static ArrayList<File> ArchivosMp3 = new ArrayList<>();
    static ArrayList<File> Archivos_tmp = new ArrayList<>();
    static boolean Trabajando;
    static VentanaAsignarCover ventana;

    public static void main(String[] args) throws Exception {
        String carpeta = "";
        if (carpeta == null || carpeta.isEmpty()) {
            NimbusModificado.CargarTemaGrisNimbus();
            NimbusModificado.CargarNimbus();
            ventana = new VentanaAsignarCover();
        } else {
            EmpezarCoverización(carpeta);
        }
    }

    public static void EmpezarCoverización(String ruta) throws Exception {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            throw new RuntimeException("La dirección no se encuentra");
        }
        if (archivo.isDirectory()) {
            CargarArchivosAudio(archivo);
        } else {
            ArchivosMp3.add(archivo);
        }
        int c = ArchivosMp3.size();
        Trabajando = true;
        for (int i = 0; i < 1; i++) {
            new Thread() {
                @Override
                public void run() {
                    while (!ArchivosMp3.isEmpty()) {
                        try {
                            File archivo = ArchivosMp3.get(0);
                            ArchivosMp3.remove(0);
                            Etiquetas e = new Etiquetas(archivo, false);
                            BufferedImage cover = e.ImagenDeCaratulaEnCarpeta();
                            if (ventana != null) {
                                ventana.jLabel1.setIcon(new ImageIcon(Filtros_Lineales.EscalarGranCalidad(cover, 300, 300)));
                                ventana.jLabel3.setText(
                                        "<html>\n"
                                        + "<p>Título: " + e.TÍTULO + "</p>\n"
                                        + "<p>Álbum: " + e.ALBÚM + "</p>\n"
                                        + "<p>Artista: " + e.ARTISTA + "</p>\n"
                                        + "<p>Año: " + e.AÑO + "</p>\n"
                                        + "<p>Género: " + e.GÉNERO + "</p>\n\n"
                                        + "<p>Dimensión: " + cover.getWidth() + "x" + cover.getHeight() + "</p>"
                                );
                                ventana.jLabel4.setText((c - ArchivosMp3.size()) + "/" + c);
                                ventana.jList1.setModel(new javax.swing.AbstractListModel<String>() {

                                    public int getSize() {
                                        return ArchivosMp3.size();
                                    }

                                    public String getElementAt(int i) {
                                        try {
                                            return ArchivosMp3.get(i).getName();
                                        } catch (Exception e) {
                                            return "";
                                        }
                                    }
                                });
                            }
                            e.ModificarTags_EnArchivo(
                                    cover,
                                    e.TÍTULO,
                                    e.ARTISTA,
                                    e.ALBÚM,
                                    e.COMENTARIO,
                                    e.GÉNERO,
                                    e.TRACK,
                                    e.AÑO
                            );
                        } catch (Exception e) {
                        }
                    }
                    Trabajando = false;
                }
            }.start();
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    while (Trabajando) {
                        sleep(3000);
                    }
                    Eliminar_tmp(archivo);
                    System.out.println("FIN");
                    System.out.println(c);
                    ventana.jLabel1.setIcon(null);
                    ventana.jLabel3.setText("");
                    ventana.jButton1.setEnabled(true);
                    ventana.jButton3.setEnabled(true);
                    ventana.jTextField1.setEnabled(true);
                    ventana.MostrarLogo();
                    ventana.jTextField1.setText("");
                    if (ventana.jRadioButton1.isSelected()) {
                        System.exit(0);
                    }
                    ventana.jRadioButton1.setEnabled(false);
                } catch (InterruptedException ex) {
                }
            }
        }.start();
    }

    public static void Eliminar_tmp(File CarpetaRaíz) {
        Archivos_tmp.clear();
        CargarArchivos_tmp(CarpetaRaíz);
        for (File file : Archivos_tmp) {
            try {
                file.delete();
            } catch (Exception e) {
            }
        }
        JOptionPane.showMessageDialog(
                ventana,
                "Se han borrado los tmp fallidos\nTotal: " + Archivos_tmp.size(),
                "Archivos borrados",
                JOptionPane.PLAIN_MESSAGE
        );
        Archivos_tmp.clear();
    }

    public static ArrayList<File> CargarArchivos_tmp(File CarpetaRaíz) {
        if (!CarpetaRaíz.isDirectory()) {
            return Archivos_tmp;
        }
        if (!CarpetaRaíz.exists()) {
            throw new RuntimeException("El directorio no existe");
        }
        File[] carpetas = CarpetaRaíz.listFiles(FiltroCarpetas());
        for (File file : CarpetaRaíz.listFiles(FiltroArchivos_Extención("tmp"))) {
            Archivos_tmp.add(file);
        }
        for (File carpeta : carpetas) {
            CargarArchivos_tmp(carpeta);
        }
        return Archivos_tmp;
    }

    public static ArrayList<File> CargarArchivosAudio(File CarpetaRaíz) {
        if (!CarpetaRaíz.exists()) {
            throw new RuntimeException("El directorio no existe");
        }
        File[] carpetas = CarpetaRaíz.listFiles(FiltroCarpetas());
        for (File file : CarpetaRaíz.listFiles(FiltroArchivosAudio())) {
            ArchivosMp3.add(file);
        }
        for (File carpeta : carpetas) {
            CargarArchivosAudio(carpeta);
        }
        return ArchivosMp3;
    }

    static FileFilter FiltroCarpetas() {
        return (File file) -> {
            if (file.isDirectory() && !file.getPath().toLowerCase().endsWith("- full")) {
                return true;
            }
            return false;
        };
    }

    static FileFilter FiltroArchivosAudio() {
        return FiltroArchivos_Extención("mp3");
    }

    static FileFilter FiltroArchivos_Extención(String... formatosPermitidos) {
        for (int i = 0; i < formatosPermitidos.length; i++) {
            formatosPermitidos[i] = formatosPermitidos[i].toLowerCase();
        }
        return (File file) -> {
            if (file.isDirectory()) {
                return false;
            }
            for (String formatoPermitido : formatosPermitidos) {
                if (file.getPath().toLowerCase().endsWith(formatoPermitido)) {
                    return true;
                }
            }
            return false;
        };
    }
}
