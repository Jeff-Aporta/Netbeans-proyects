package HerramientaArchivos;

import java.awt.Font;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class LectorMixto {

    public static Font cargar_Fuente(Object o, float tamaño) {
        Font retorno = null;
        if (o instanceof File) {
            try {
                retorno = Font.createFont(Font.TRUETYPE_FONT, ((File) o));
            } catch (Exception e) {
                try {
                    retorno = Font.createFont(Font.TYPE1_FONT, ((File) o));
                } catch (Exception e2) {
                }
            }
        } else if (o instanceof InputStream) {
            try {
                retorno = Font.createFont(Font.TRUETYPE_FONT, ((InputStream) o));
            } catch (Exception e) {
                try {
                    retorno = Font.createFont(Font.TYPE1_FONT, ((InputStream) o));
                } catch (Exception e2) {
                }
            }
        } else if (o instanceof String) {
            String s = (String) o;
            try {
                URL url = new URL(s);
                retorno = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            } catch (Exception e) {
                try {
                    retorno = Font.createFont(Font.TYPE1_FONT, ((InputStream) o));
                } catch (Exception e2) {
                    return cargar_Fuente(new File(s), tamaño);
                }
            }
        } else {
            throw new RuntimeException("No se reconoce el objeto");
        }
        if (retorno != null) {
            //retorno = retorno.deriveFont(tamaño);
        }
        return retorno;
    }

}
