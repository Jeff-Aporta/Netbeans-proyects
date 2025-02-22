package JAVH;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

public class FFmpeg_Espectro extends FFmpeg_Dimensionable implements Constantes_DeOnda {

    public Color COLOR = null;
    public boolean COMPAND = false;
    public byte MODO_NODOS = CALCULAR_USANDO_ONDAS;

    public FFmpeg_Espectro(String RECURSO, String SALIDA, int W, int H) {
        super(RECURSO, SALIDA, new Dimension(W, H));
    }

    public FFmpeg_Espectro(String RECURSO, String SALIDA) {
        super(RECURSO, SALIDA, null);
    }

    public FFmpeg_Espectro(String RECURSO, String SALIDA, Dimension dimension) {
        super(RECURSO, SALIDA, dimension);
    }

    protected void AñadirComandoDeColor(ArrayList commands) {
        if (COLOR != null) {
            commands.add(":colors="
                    + "0x" + Integer.toHexString(COLOR.getRGB()).substring(2).toUpperCase()
            );
        }
    }
}
