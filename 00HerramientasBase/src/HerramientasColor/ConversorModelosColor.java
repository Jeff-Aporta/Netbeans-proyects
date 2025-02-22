package HerramientasColor;

import HerramientasMatemáticas.Matemática.Rango;
import java.awt.Color;

public class ConversorModelosColor {

    public static final Rango rangoPorcentual = new Rango(0, 1);

    public static void main(String[] args) {
        Color color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        System.out.println(RGBtoHex(new Color(r, g, b)));
        System.out.println("");
        System.out.println("R: " + r);
        System.out.println("G: " + g);
        System.out.println("B: " + b);
        System.out.println("RGB a HSL");
        float[] HSL = RGBtoHSL(r, g, b);
        System.out.println("H: " + HSL[0]);
        System.out.println("S: " + HSL[1]);
        System.out.println("L: " + HSL[2]);
        System.out.println("HSL a RGB");
        int RGBfromHSL = HSLtoRGB(HSL);
        System.out.println("R: " + ((RGBfromHSL >> 16) & 0xff));
        System.out.println("G: " + ((RGBfromHSL >> 8) & 0xff));
        System.out.println("B: " + ((RGBfromHSL & 0xff)));
        System.out.println();
        System.out.println("RGB a HSB propio");
        float[] HSB = RGBtoHSB(r, g, b, 255);
        System.out.println("H: " + HSB[0]);
        System.out.println("S: " + HSB[1]);
        System.out.println("B: " + HSB[2]);
        System.out.println("RGB a HSB clase Color");
        float[] HSB2 = Color.RGBtoHSB(r, g, b, null);
        System.out.println("H: " + HSB2[0]);
        System.out.println("S: " + HSB2[1]);
        System.out.println("B: " + HSB2[2]);
        System.out.println();
        System.out.println("HSB a RGB propio");
        int RGBfromHSB = HSBtoRGB(HSB);
        System.out.println("R: " + ((RGBfromHSB >> 16) & 0xff));
        System.out.println("G: " + ((RGBfromHSB >> 8) & 0xff));
        System.out.println("B: " + (RGBfromHSB & 0xff));
        System.out.println("RGB a HSB clase Color");
        int RGBfromHSB2 = Color.HSBtoRGB(HSB2[0], HSB2[1], HSB2[2]);
        System.out.println("R: " + ((RGBfromHSB2 >> 16) & 0xff));
        System.out.println("G: " + ((RGBfromHSB2 >> 8) & 0xff));
        System.out.println("B: " + (RGBfromHSB2 & 0xff));
        System.out.println();
        System.out.println("RGB a CMYK");
        float CMYK[] = RGBtoCMYK(r, g, b);
        System.out.println("C: " + CMYK[0]);
        System.out.println("M: " + CMYK[1]);
        System.out.println("Y: " + CMYK[2]);
        System.out.println("K: " + CMYK[3]);
        System.out.println();
        System.out.println("CMYK a RGB");
        int RGBdeCMYK = CMYKtoRGB(CMYK);
        System.out.println("R: " + ((RGBdeCMYK >> 16) & 0xff));
        System.out.println("G: " + ((RGBdeCMYK >> 8) & 0xff));
        System.out.println("B: " + (RGBdeCMYK & 0xff));
    }

    public static String RGBtoHex(Color c) {
        return RGBtoHex(c.getRed(), c.getGreen(), c.getBlue());
    }

    public static String RGBtoHex(int r, int g, int b) {
        return Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
    }

    //<editor-fold defaultstate="collapsed" desc="Herramientas HSL »">
    public static int HSLtoRGB(float... HSL) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (HSL.length < 3 || HSL.length > 4) {
            throw new RuntimeException("Cantidad de argumentos inválidos");
        }
        float H = rangoPorcentual.AjusteCircular(HSL[0]);
        float S = rangoPorcentual.AjusteAExtremos(HSL[1]);
        float L = rangoPorcentual.AjusteAExtremos(HSL[2]);
        float C = (1 - Math.abs(2 * L - 1)) * S;
        float ΔH = H * 360 / 60f;
        float X = C * (1 - Math.abs(ΔH % 2 - 1));
        float ΔR = 0, ΔG = 0, ΔB = 0;
        switch ((int) ΔH) {
            case 0:
                ΔR = C;
                ΔG = X;
                ΔB = 0;
                break;
            case 1:
                ΔR = X;
                ΔG = C;
                ΔB = 0;
                break;
            case 2:
                ΔR = 0;
                ΔG = C;
                ΔB = X;
                break;
            case 3:
                ΔR = 0;
                ΔG = X;
                ΔB = C;
                break;
            case 4:
                ΔR = X;
                ΔG = 0;
                ΔB = C;
                break;
            case 5:
                ΔR = C;
                ΔG = 0;
                ΔB = X;
                break;
        }
        float m = L - C / 2;
        int A = 255;
        if (HSL.length == 4) {
            A = (int) (rangoPorcentual.AjusteAExtremos(HSL[3]) * 255);
        }
        A <<= 24;
        int R = Math.round((ΔR + m) * 255);
        R <<= 16;
        int G = Math.round((ΔG + m) * 255);
        G <<= 8;
        int B = Math.round((ΔB + m) * 255);
        return A | R | G | B;
    }//</editor-fold>

    public static float[] RGBtoHSL(Color c) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return RGBtoHSL(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }//</editor-fold>

    public static float[] RGBtoHSL(int r, int g, int b, boolean ignorarAlpha) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return RGBtoHSL(r, g, b, ignorarAlpha ? -1 : 255);
    }//</editor-fold>

    public static float[] RGBtoHSL(int r, int g, int b) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return RGBtoHSL(r, g, b, 255);
    }//</editor-fold>

    public static float[] RGBtoHSL(int r, int g, int b, int a) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        float R = r / 255f;
        float G = g / 255f;
        float B = b / 255f;
        float Máx = Math.max(Math.max(R, G), B);
        float Mín = Math.min(Math.min(R, G), B);
        float D = Máx - Mín;
        float θ = 60 / 360f;
        float H;
        if (D == 0) {
            H = 0;
        } else if (Máx == R) {
            H = θ * ((G - B) / D) + 1;
        } else if (Máx == G) {
            H = θ * ((B - R) / D) + 120 / 360f;
        } else {
            H = θ * ((R - G) / D) + 240 / 360f;
        }
        float L = (Máx + Mín) / 2;
        float S;
        if (D == 0) {
            S = 0;
        } else {
            S = D / (1 - Math.abs(2 * L - 1));
        }
        H = rangoPorcentual.AjusteCircular(H);
        S = rangoPorcentual.AjusteAExtremos(S);
        L = rangoPorcentual.AjusteAExtremos(L);
        if (a > 0) {
            return new float[]{H, S, L, a / 255f};
        }
        return new float[]{H, S, L};
    }//</editor-fold>

    public static float Obtener_S_deHSL(int r, int g, int b) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        float R = r / 255f, G = g / 255f, B = b / 255f;
        float Máx = Math.max(Math.max(R, G), B), Mín = Math.min(Math.min(R, G), B);
        float D = Máx - Mín;
        float L = (Máx + Mín) / 2;
        float S = D == 0 ? 0 : D / (1 - Math.abs(2 * L - 1));
        return S;
    }//</editor-fold>

    public static float Obtener_L_deHSL(Color c) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return Obtener_L_deHSL(c.getRed(), c.getGreen(), c.getBlue());
    }//</editor-fold>

    public static float Obtener_L_deHSL(int r, int g, int b) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        float R = r / 255f, G = g / 255f, B = b / 255f;
        float Máx = Math.max(Math.max(R, G), B), Mín = Math.min(Math.min(R, G), B);
        float L = (Máx + Mín) / 2;
        return rangoPorcentual.AjusteAExtremos(L);
    }//</editor-fold>
    //</editor-fold>

    public static float ObtenerTono(Color c) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return ObtenerTono(c.getRed(), c.getGreen(), c.getBlue());
    }//</editor-fold>

    public static float ObtenerTono(int r, int g, int b) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        float R = r / 255f;
        float G = g / 255f;
        float B = b / 255f;
        float Máx = Math.max(Math.max(R, G), B);
        float Mín = Math.min(Math.min(R, G), B);
        float D = Máx - Mín;
        if (D == 0) {
            return 0;
        }
        float θ = 60 / 360f;
        if (Máx == R) {
            return θ * ((G - B) / D) + 1;
        } else if (Máx == G) {
            return θ * ((B - R) / D) + 120 / 360f;
        } else {
            return θ * ((R - G) / D) + 240 / 360f;
        }
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas HSB »">
    public static int HSBtoRGB(float... HSB) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (HSB.length < 3 || HSB.length > 4) {
            throw new RuntimeException("Cantidad de argumentos inválidos");
        }
        float H = rangoPorcentual.AjusteCircular(HSB[0]);
        float S = rangoPorcentual.AjusteAExtremos(HSB[1]);
        float V = rangoPorcentual.AjusteAExtremos(HSB[2]);
        float C = V * S;
        float ΔH = H * 360 / 60f;
        float X = C * (1 - Math.abs(ΔH % 2 - 1));
        float ΔR = 0, ΔG = 0, ΔB = 0;
        switch ((int) ΔH) {
            case 0:
                ΔR = C;
                ΔG = X;
                ΔB = 0;
                break;
            case 1:
                ΔR = X;
                ΔG = C;
                ΔB = 0;
                break;
            case 2:
                ΔR = 0;
                ΔG = C;
                ΔB = X;
                break;
            case 3:
                ΔR = 0;
                ΔG = X;
                ΔB = C;
                break;
            case 4:
                ΔR = X;
                ΔG = 0;
                ΔB = C;
                break;
            case 5:
                ΔR = C;
                ΔG = 0;
                ΔB = X;
                break;
        }
        float m = V - C;
        int A = 255;
        if (HSB.length == 4) {
            A = (int) (rangoPorcentual.AjusteAExtremos(HSB[3]) * 255);
        }
        A <<= 24;
        int R = Math.round((ΔR + m) * 255);
        R <<= 16;
        int G = Math.round((ΔG + m) * 255);
        G <<= 8;
        int B = Math.round((ΔB + m) * 255);
        return A | R | G | B;
    }//</editor-fold>

    public static float[] RGBtoHSB(Color c) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }//</editor-fold>

    public static float[] RGBtoHSB(int r, int g, int b) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return RGBtoHSB(r, g, b, 255);
    }//</editor-fold>

    public static float[] RGBtoHSB(int r, int g, int b, int a) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        float R = r / 255f;
        float G = g / 255f;
        float B = b / 255f;
        float Máx = Math.max(Math.max(R, G), B);
        float Mín = Math.min(Math.min(R, G), B);
        float D = Máx - Mín;
        float θ = 60 / 360f;
        float H;
        if (D == 0) {
            H = 0;
        } else if (Máx == R) {
            H = θ * ((G - B) / D) + 1;
        } else if (Máx == G) {
            H = θ * ((B - R) / D) + 120 / 360f;
        } else {
            H = θ * ((R - G) / D) + 240 / 360f;
        }
        float S;
        if (Máx == 0) {
            S = 0;
        } else {
            S = D / Máx;
        }
        float V = Máx;
        H = rangoPorcentual.AjusteCircular(H);
        S = rangoPorcentual.AjusteAExtremos(S);
        V = rangoPorcentual.AjusteAExtremos(V);
        return new float[]{H, S, V, a / 255f};
    }//</editor-fold>

    public static float Obtener_B_deHSB(Color c) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return Obtener_B_deHSB(c.getRed(), c.getGreen(), c.getBlue());
    }//</editor-fold>

    public static float Obtener_B_deHSB(int r, int g, int b) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        float R = r / 255f;
        float G = g / 255f;
        float B = b / 255f;
        float Máx = Math.max(Math.max(R, G), B);
        float Brillo = Máx;
        return rangoPorcentual.AjusteAExtremos(Brillo);
    }//</editor-fold>

    public static float Obtener_S_deHSB(int r, int g, int b) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        float R = r / 255f;
        float G = g / 255f;
        float B = b / 255f;
        float Máx = Math.max(Math.max(R, G), B);
        float Mín = Math.min(Math.min(R, G), B);
        float D = Máx - Mín;
        float S;
        if (Máx == 0) {
            return 0;
        } else {
            return D / Máx;
        }
    }//</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Herramientas CMYK »">
    public static float[] RGBtoCMYK(Color c) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return RGBtoCMYK(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }//</editor-fold>

    public static float[] RGBtoCMYK(int r, int g, int b, boolean ignorarAlpha) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return RGBtoCMYK(r, g, b, ignorarAlpha ? -1 : 255);
    }//</editor-fold>

    public static float[] RGBtoCMYK(int r, int g, int b) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return RGBtoCMYK(r, g, b, 255);
    }//</editor-fold>

    public static float[] RGBtoCMYK(int r, int g, int b, int a) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        float R = r / 255f;
        float G = g / 255f;
        float B = b / 255f;
        float K = 1 - Math.max(Math.max(R, G), B);
        float C = (1 - R - K) / (1 - K);
        float M = (1 - G - K) / (1 - K);
        float Y = (1 - B - K) / (1 - K);
        C = rangoPorcentual.AjusteAExtremos(C);
        M = rangoPorcentual.AjusteAExtremos(M);
        Y = rangoPorcentual.AjusteAExtremos(Y);
        K = rangoPorcentual.AjusteAExtremos(K);
        if (a > 0) {
            return new float[]{C, M, Y, K, a / 255f};
        } else {
            return new float[]{C, M, Y, K};
        }
    }//</editor-fold>

    public static int CMYKtoRGB(float... CMYK) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (CMYK.length < 4 || CMYK.length > 5) {
            throw new RuntimeException("Cantidad de argumentos inválidos");
        }
        float pK = (1 - CMYK[3]);
        int R = Math.round(255 * (1 - CMYK[0]) * pK);
        int G = Math.round(255 * (1 - CMYK[1]) * pK);
        int B = Math.round(255 * (1 - CMYK[2]) * pK);
        int A = 255;
        if (CMYK.length == 5) {
            A = (int) (CMYK[4] * 255);
        }
        A <<= 24;
        R <<= 16;
        G <<= 8;
        return A | R | G | B;
    }//</editor-fold>
    //</editor-fold>

    public static Color Aleatorio() {
        return new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
    }

}
