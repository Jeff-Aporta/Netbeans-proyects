package jna;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Experimental {

    public static void cmd(String strProcessName, boolean abrirConsola) throws Exception {
        ArrayList<String> comandos_Ejecutar = new ArrayList();
        comandos_Ejecutar.add(strProcessName);
        if (abrirConsola) {
            comandos_Ejecutar.add(0, "cmd");
            comandos_Ejecutar.add(1, "/c");
            comandos_Ejecutar.add(2, "start");
            comandos_Ejecutar.add(3, "cmd.exe");
            comandos_Ejecutar.add(4, "/K");
//            comandos_Ejecutar.add(5, "\"");
            //El código inicial se ubica aquí
//            comandos_Ejecutar.add("&&");
//            comandos_Ejecutar.add("exit");
//            comandos_Ejecutar.add("\"");
        }

        String[] cmd = new String[comandos_Ejecutar.size()];
        for (int i = 0; i < cmd.length; i++) {
            cmd[i] = comandos_Ejecutar.get(i).toString();
        }
        Runtime runtime = Runtime.getRuntime();
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
