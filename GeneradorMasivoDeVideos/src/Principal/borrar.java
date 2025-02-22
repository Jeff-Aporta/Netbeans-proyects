package Principal;

import java.io.File;

public class borrar {

    public static void main(String[] args) {
        String ruta = "C:\\Users\\57310\\Documents\\Javascript\\_p5.js\\p5js a mp4\\PCSX2";
        File f = new File(ruta);
        for (File listFile : f.listFiles((file, name) -> name.endsWith(".jpg"))) {
            System.out.println("\"" + listFile.getName() + "\",");
        }
    }

}
