package JAVH;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Ejecutador implements Constantes_Ejecutador {

    public byte CONSOLE_SHOW = CONSOLE_SHOW_ALWAYS;

    public String RECURSO;
    public String SALIDA;

    protected String DIRECCIÓN_EJECUTABLE = DIRECCIÓN_FFMPEG;

    public static char[] Problematic = {'&'};

    public Ejecutador(String RECURSO, String SALIDA) {
        this.RECURSO = RECURSO;
        this.SALIDA = SALIDA;
    }

    //TOOLS
    protected void CrearCarpeta() {
        File f = new File(SALIDA);
        f = new File(f.getPath().replace("\\" + f.getName(), ""));
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public void EjecutarCódigo_cmd(ArrayList comandos_Ejecutar, boolean AbrirConsola) throws Exception {//experimental
        comandos_Ejecutar.add(0, "\""+DIRECCIÓN_EJECUTABLE+"\"");
        switch (CONSOLE_SHOW) {
            case CONSOLE_SHOW_ALWAYS:
                AbrirConsola = true;
                break;
            case CONSOLE_SHOW_NEVER:
                AbrirConsola = false;
                break;
            case CONSOLE_SHOW_AS_DEV_DET:
                break;
            default:
                throw new RuntimeException("No se reconoce console show " + CONSOLE_SHOW);
        }
        boolean contieneProblematicChar = false;
        String a = "";
        for (Object comando : comandos_Ejecutar) {
            a += comando + " ";
            String coString = comando.toString();
            for (char c : Problematic) {
                if (coString.contains(c + "")) {
                    contieneProblematicChar = true;
                    break;
                }
            }
            if (contieneProblematicChar) {
                break;
            }
        }
        if (AbrirConsola) {
            if (a.length() > 8191) {
                JOptionPane.showMessageDialog(null, "La instrucción contiene más de 8191 caracteres\n"
                        + "El cmd no puede ejecutar instrucciones tan largas\n"
                        + "intente solucionarlo reduciendo el nombre de las rutas\n"
                );
                String info = "https://support.microsoft.com/en-us/help/830473/command-prompt-cmd--exe-command-line-string-limitation";
                System.out.println(info);
                JOptionPane.showMessageDialog(null, "En la consola se ha impreso la dirección\n"
                        + info
                        + "para que se informe de esta limintación\n"
                );
                throw new RuntimeException("Instrucción demasiado larga");
            }
            comandos_Ejecutar.add(0, "cmd");
            comandos_Ejecutar.add(1, "/c");
            comandos_Ejecutar.add(2, "start");
            comandos_Ejecutar.add(3, "cmd.exe");
            comandos_Ejecutar.add(4, "/K");

            if (!contieneProblematicChar) {
                comandos_Ejecutar.add(5, "\"");
            }
            comandos_Ejecutar.add("&&");
            comandos_Ejecutar.add("exit");
            if (!contieneProblematicChar) {
                comandos_Ejecutar.add("\"");
            }
        }
        String[] cmd = new String[comandos_Ejecutar.size()];
        String s = "";
        for (int i = 0; i < cmd.length; i++) {
            cmd[i] = comandos_Ejecutar.get(i).toString();
            s += cmd[i];
            s+= " ";
        }
        Runtime runtime = Runtime.getRuntime();
        System.out.println(s);
        Process ffmpeg = runtime.exec(cmd);
        InputStreamReader in = new InputStreamReader(ffmpeg.getInputStream());
        InputStreamReader in2 = new InputStreamReader(ffmpeg.getErrorStream());
        BufferedReader read = new BufferedReader(in);
        BufferedReader read2 = new BufferedReader(in2);
        String str;
        while ((str = read.readLine()) != null) {
            String str2 = read2.readLine();
            System.out.println(str2);
            System.out.println(str);
        }
    }

}
