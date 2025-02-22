package HerramientaArchivos.ArrastrarArchivos;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PruebaArrastrarArchivos {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Arrastrar Archivos") {
            {
                setAlwaysOnTop(true);
                setSize(600, 400);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setVisible(true);
            }
        };
        final JTextArea jTextArea = new JTextArea();
        frame.getContentPane().add(new JScrollPane(jTextArea), BorderLayout.CENTER);

        ArrastrarArchivos.Añadir(
                jTextArea, (String[] cadenas) -> {
                    for (int i = 0; i < cadenas.length; i++) {
                        try {
                            jTextArea.append(cadenas[i] + "\n");
                        } catch (Exception e) {
                        }
                    }
                }
        );
    }

}
