package GeneradorDeEspectroSimple082019;

import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientasMatemáticas.Dupla;
import HerramientasSistema.Sistema;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DescargarYoutubeAudioLibrary {

    public static void main(String[] args) throws Exception {
        JLabel lbl = new JLabel() {
            {
                setFont(new Font("verdana", Font.PLAIN, 40));
                setHorizontalAlignment(CENTER);
            }
        };
        JFrame ventana = new JFrame() {
            {
                add(lbl);
                setAlwaysOnTop(true);
                setSize(300, 150);
                setLocation(
                        Dupla.DIMENSIÓN_PANTALLA.Ancho() - getWidth() - 20,
                        Dupla.DIMENSIÓN_PANTALLA.Alto() - getHeight() - 20
                );
                setVisible(true);
            }
        };
        String carpeta = "C:\\Users\\josel\\Downloads\\Nueva carpeta\\";
        String nombre = "alt.html";
        ArrayList<String> rutas = new ArrayList<>();
        {
            lbl.setText("Leyendo");
            String texto = LectoEscrituraArchivos.LeerArchivo_ASCII(carpeta + nombre);
            lbl.setText("Protegiendo los links");
            texto = texto.replace("<a href=\"https://www.youtube.com/audiolibrary_download?", "https://www.youtube.com/audiolibrary_download?");
            lbl.setText("Eliminando el HTML innecesario");
            texto = LectoEscrituraArchivos.EliminarCódigoHTML_TextoPlano(texto);
            lbl.setText("Separando los renglones");
            String renglones[] = texto.split("\n");
            for (String renglón : renglones) {
                lbl.setText("Analizando el renglón y ajustando");
                renglón = renglón.trim();
                lbl.setText(renglón);
                if (renglón.isEmpty()) {
                    lbl.setText("Este renglón está vacío");
                    continue;
                }
                if (!renglón.startsWith("https://www.youtube.com/audiolibrary")) {
                    lbl.setText("no hay el link necesitado");
                    continue;
                }
                String r = renglón.substring(0, renglón.indexOf("\""));
                rutas.add(r);
                lbl.setText("link extraido");
            }
        }
        for (int i = 0; i < rutas.size(); i++) {
            lbl.setText((i + 1) + "/" + rutas.size());
            System.out.println(rutas.get(i));
            Sistema.Abrir_URL_EnNavegador(rutas.get(i));
            Thread.sleep(3000);
        }
        Toolkit.getDefaultToolkit().beep();
        Thread.sleep(1000);
        Toolkit.getDefaultToolkit().beep();
        System.exit(0);
    }

}
