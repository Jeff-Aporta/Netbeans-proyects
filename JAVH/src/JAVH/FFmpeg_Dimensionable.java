package JAVH;

import java.awt.Dimension;
import java.util.ArrayList;

public class FFmpeg_Dimensionable extends FFmpeg {

    /**
     * Indica el tamaño que va a tener la salida<br/>
     * <img src="https://jeffaporta01.000webhostapp.com/Librerias Java/Dimension.png" height="200" width="200">
     * por defecto es null
     */
    public Dimension DIMENSIÓN = null;

    public FFmpeg_Dimensionable(String RECURSO, String SALIDA) {
        this(RECURSO, SALIDA, null);
    }

    public FFmpeg_Dimensionable(String RECURSO, String SALIDA, int W, int H) {
        this(RECURSO, SALIDA, new Dimension(W, H));
    }

    public FFmpeg_Dimensionable(String RECURSO, String SALIDA, Dimension dimension) {
        super(RECURSO, SALIDA);
        if (dimension == null) {
            DIMENSIÓN = new Dimension(640, 360);
        } else {
            DIMENSIÓN = dimension;
        }
    }

    protected void AñadirComandoDeDimensión(ArrayList commands) {
        if (DIMENSIÓN == null) {
            commands.add("s=640x360");
        } else {
            commands.add("s=" + DIMENSIÓN.width + "x" + DIMENSIÓN.height);
        }
    }
}
