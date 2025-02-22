package HerramientaDeImagen.ModoFusión;

import HerramientaDeImagen.ModoFusión.ModoFusión.Conmutar.Normal;
import HerramientasColor.ConversorModelosColor;
import HerramientasMatemáticas.Matemática;
import static HerramientasMatemáticas.Matemática.*;
import java.awt.Color;

public final class ModoPintura {

    public static void main(String[] args) {
        GUI_PruebaModoPintura.main(args);
    }

    public abstract static class BaseModoPintura extends Normal {//<editor-fold defaultstate="collapsed" desc="Cuerpo del modelo base »">

        public double Aplicación = 1;
        public double Aplicación2 = 1;
        public double Aplicación3 = 1;
        public double Aplicación4 = 1;
        public double AplicaciónRojo = 1;
        public double AplicaciónVerde = 1;
        public double AplicaciónAzul = 1;

        {
            RiesgoSalida = false;
        }

        @Override
        public void ModificarPixelesPintar(int[] Pintar, int P) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            super.ModificarPixelesPintar(Pintar, P);
            int[] color = new int[]{Pintar[P + R], Pintar[P + G], Pintar[P + B], Pintar[P + A]};
            Efecto(Pintar, color, P);
            AjustarAlEspacio(color);
            Aplicación(Pintar, color, P);
        }//</editor-fold>

        abstract public void Efecto(int[] Pintar, int[] color, int P);

        public void Aplicación(int[] Pintar, int[] colorR, int P) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            Pintar[R + P] = (int) ((colorR[R] - Pintar[R + P]) * Aplicación * AplicaciónRojo + Pintar[R + P]);
            Pintar[G + P] = (int) ((colorR[G] - Pintar[G + P]) * Aplicación * AplicaciónVerde + Pintar[G + P]);
            Pintar[B + P] = (int) ((colorR[B] - Pintar[B + P]) * Aplicación * AplicaciónAzul + Pintar[B + P]);
            Pintar[A + P] = colorR[A];
        }//</editor-fold>

        public int AplicacionesUsadas() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            if (this instanceof CuadrupleAplicación) {
                return 4;
            }
            if (this instanceof TripleAplicación) {
                return 3;
            }
            if (this instanceof DobleAplicación) {
                return 2;
            }
            return 1;
        }//</editor-fold>

    }//</editor-fold>

    public static interface DobleAplicación {//<editor-fold defaultstate="collapsed" desc="Cuerpo del modelo base »">
    }//</editor-fold>

    public static interface TripleAplicación extends DobleAplicación {//<editor-fold defaultstate="collapsed" desc="Cuerpo del modelo base »">
    }//</editor-fold>

    public static interface CuadrupleAplicación extends TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Cuerpo del modelo base »">
    }//</editor-fold>

    public final static class ConVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

        public abstract static class BaseVariableDeAplicación extends BaseModoPintura {//<editor-fold defaultstate="collapsed" desc="Cuerpo del modelo base »">

            @Override
            public void Efecto(int[] Pintar, int[] color, int P) {
                Efecto(color);
            }

            abstract public void Efecto(int[] color);

            @Override
            public void Aplicación(int[] Pintar, int[] colorR, int P) {
                Pintar[R + P] = (int) ((colorR[R] - Pintar[R + P]) * AplicaciónRojo + Pintar[R + P]);
                Pintar[G + P] = (int) ((colorR[G] - Pintar[G + P]) * AplicaciónVerde + Pintar[G + P]);
                Pintar[B + P] = (int) ((colorR[B] - Pintar[B + P]) * AplicaciónAzul + Pintar[B + P]);
            }
        }//</editor-fold>

        public static class Discretizar extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .75;
            }

            @Override
            public void Efecto(int[] color) {
                int niveles = (int) ((1 - Aplicación) * 15 + 2);
                for (int c = 0; c <= B; c++) {
                    color[c] = (int) Rango_RGB.Discretizar(color[c], niveles);
                }
            }
        }//</editor-fold>

        public static class Monocromatizar extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            public Monocromatizar() {
                Aplicación = .71;
            }

            public Monocromatizar(Color c) {
                float[] HSB = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                Aplicación = HSB[0];
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float tono = (float) Aplicación;
                int rgb = Color.HSBtoRGB(tono, HSB[1], HSB[2]);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class Monosaturar_HSL extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            @Override
            public void Efecto(int[] color) {
                float[] HSB = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                float saturación = (float) Aplicación;
                int rgb = ConversorModelosColor.HSLtoRGB(HSB[0], saturación, HSB[2]);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class Monobrillar_HSL extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .75;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                float brillo = (float) Aplicación;
                int rgb = ConversorModelosColor.HSLtoRGB(HSB[0], HSB[1], brillo);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class Monosaturar_HSB extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            @Override
            public void Efecto(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float saturación = (float) Aplicación;
                int rgb = Color.HSBtoRGB(HSB[0], saturación, HSB[2]);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class Monobrillar_HSB extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            @Override
            public void Efecto(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float brillo = (float) Aplicación;
                int rgb = Color.HSBtoRGB(HSB[0], HSB[1], brillo);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class Monocanalizar_rojo extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .3;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float rojo = (float) Aplicación * 255;
                color[R] = (int) rojo;
            }
        }//</editor-fold>

        public static class Monocanalizar_verde extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .3;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float verde = (float) Aplicación * 255;
                color[G] = (int) verde;
            }
        }//</editor-fold>

        public static class Monocanalizar_azul extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .3;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float azul = (float) Aplicación * 255;
                color[B] = (int) azul;
            }
        }//</editor-fold>

        public static class Monocanalizar_cyan extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .3;
            }

            @Override
            public void Efecto(int[] color) {
                float[] CMYK = ConversorModelosColor.RGBtoCMYK(color[R], color[G], color[B]);
                CMYK[0] = (float) Aplicación;
                int rgb = ConversorModelosColor.CMYKtoRGB(CMYK);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class Monocanalizar_magenta extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .3;
            }

            @Override
            public void Efecto(int[] color) {
                float[] CMYK = ConversorModelosColor.RGBtoCMYK(color[R], color[G], color[B]);
                CMYK[1] = (float) Aplicación;
                int rgb = ConversorModelosColor.CMYKtoRGB(CMYK);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class Monocanalizar_amarillo extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .3;
            }

            @Override
            public void Efecto(int[] color) {
                float[] CMYK = ConversorModelosColor.RGBtoCMYK(color[R], color[G], color[B]);
                CMYK[2] = (float) Aplicación;
                int rgb = ConversorModelosColor.CMYKtoRGB(CMYK);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class Monocanalizar_negro extends BaseVariableDeAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .3;
            }

            @Override
            public void Efecto(int[] color) {
                float[] CMYK = ConversorModelosColor.RGBtoCMYK(color[R], color[G], color[B]);
                CMYK[3] = (float) Aplicación;
                int rgb = ConversorModelosColor.CMYKtoRGB(CMYK);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class Monocolorizar_HSL extends BaseVariableDeAplicación implements DobleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .58;
                Aplicación2 = .7;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                float tono = (float) Aplicación;
                float saturación = (float) Aplicación2;
                if (tono < 0) {
                    tono = 1 + tono;
                }
                if (tono > 1) {
                    tono = tono % 1;
                }
                if (saturación < 0) {
                    saturación = 1 + saturación;
                }
                if (saturación > 1) {
                    saturación = saturación % 1;
                }
                int rgb = ConversorModelosColor.HSLtoRGB(tono, saturación, HSB[2]);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class Monocolorizar_HSB extends BaseVariableDeAplicación implements DobleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .58;
                Aplicación2 = .7;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float tono = (float) Aplicación;
                float saturación = (float) Aplicación2;
                tono = ConversorModelosColor.rangoPorcentual.AjusteCircular(tono);
                saturación = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(saturación);
                int rgb = Color.HSBtoRGB(tono, saturación, HSB[2]);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class ConservarSaturaciónDeColorEnTonoEspecifico extends BaseVariableDeAplicación implements DobleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación2 = 0.065f;
            }

            void convertirEnGris(int[] color) {
                int g = (int) (255 * ConversorModelosColor.Obtener_B_deHSB(color[R], color[G], color[B]));
                for (int c = 0; c < color.length; c++) {
                    color[c] = g;
                }
            }

            @Override
            public void Efecto(int[] color) {
                float tono = Color.RGBtoHSB(color[R], color[G], color[B], null)[0];
                float ánguloColor = (float) Aplicación;
                float delta = (float) Aplicación2;
                float a = ánguloColor + delta;
                float b = ánguloColor - delta;
                if (a > 1 && (tono > b || tono < a % 1)) { //Hubo salida por delante
                    return;
                } else if (b < 0 && (tono < a || tono > 1 + b)) { //Hubo salida por detras
                    return;
                } else if ((tono <= a && tono >= b)) { //No hubo salida
                    return;
                }
                convertirEnGris(color);
            }
        }//</editor-fold>

        public static class EliminarSaturaciónDeColorEnTonoEspecifico extends BaseVariableDeAplicación implements DobleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación2 = 0.065f;
            }

            void convertirEnGris(int[] color) {
                int g = (int) (255 * ConversorModelosColor.Obtener_B_deHSB(color[R], color[G], color[B]));
                for (int c = 0; c < color.length; c++) {
                    color[c] = g;
                }
            }

            @Override
            public void Efecto(int[] color) {
                float tono = Color.RGBtoHSB(color[R], color[G], color[B], null)[0];
                float ánguloColor = (float) Aplicación;
                float delta = (float) Aplicación2;
                float a = ánguloColor + delta;
                float b = ánguloColor - delta;
                if (a > 1 && (tono > b || tono < a % 1)) { //Hubo salida por delante
                    convertirEnGris(color);
                } else if (b < 0 && (tono < a || tono > 1 + b)) { //Hubo salida por detras
                    convertirEnGris(color);
                } else if ((tono <= a && tono >= b)) { //No hubo salida
                    convertirEnGris(color);
                }
            }
        }//</editor-fold>

        public static class CambiarTonoDeColorEnTonoEspecifico extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación2 = 0.07f;
                Aplicación3 = 0.5f;
            }

            void cambiarTono(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float tono = (float) ConversorModelosColor.rangoPorcentual.AjusteCircular(Aplicación3);
                int rgb = Color.HSBtoRGB(tono, HSB[1], HSB[2]);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }

            @Override
            public void Efecto(int[] color) {
                float tono = Color.RGBtoHSB(color[R], color[G], color[B], null)[0];
                float ánguloColor = (float) Aplicación;
                float delta = (float) Aplicación2;
                float a = ánguloColor + delta;
                float b = ánguloColor - delta;
                if (a > 1 && (tono > b || tono < a % 1)) { //Hubo salida por delante
                    cambiarTono(color);
                } else if (b < 0 && (tono < a || tono > 1 + b)) { //Hubo salida por detras
                    cambiarTono(color);
                } else if ((tono <= a && tono >= b)) { //No hubo salida
                    cambiarTono(color);
                }
            }
        }//</editor-fold>

        public static class CambiarSaturaciónHSBColorEnTonoEspecifico extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación2 = 0.07f;
                Aplicación3 = 0.5f;
            }

            void Desaturar(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                int rgb = Color.HSBtoRGB(HSB[0], (float) (HSB[1] * Aplicación3), HSB[2]);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }

            @Override
            public void Efecto(int[] color) {
                float tono = Color.RGBtoHSB(color[R], color[G], color[B], null)[0];
                float ánguloColor = (float) Aplicación;
                float delta = (float) Aplicación2;
                float a = ánguloColor + delta;
                float b = ánguloColor - delta;
                if (a > 1 && (tono > b || tono < a % 1)) { //Hubo salida por delante
                    Desaturar(color);
                } else if (b < 0 && (tono < a || tono > 1 + b)) { //Hubo salida por detras
                    Desaturar(color);
                } else if ((tono <= a && tono >= b)) { //No hubo salida
                    Desaturar(color);
                }
            }
        }//</editor-fold>

        public static class CambiarBrilloHSBDeColorEnTonoEspecifico extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación2 = 0.07f;
                Aplicación3 = 0.5f;
            }

            void cambiarBrillo(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float brillo = (float) ConversorModelosColor.rangoPorcentual.AjusteCircular(Aplicación3);
                int rgb = Color.HSBtoRGB(HSB[0], HSB[1], HSB[2] * brillo);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }

            @Override
            public void Efecto(int[] color) {
                float tono = Color.RGBtoHSB(color[R], color[G], color[B], null)[0];
                float ánguloColor = (float) Aplicación;
                float delta = (float) Aplicación2;
                float a = ánguloColor + delta;
                float b = ánguloColor - delta;
                if (a > 1 && (tono > b || tono < a % 1)) { //Hubo salida por delante
                    cambiarBrillo(color);
                } else if (b < 0 && (tono < a || tono > 1 + b)) { //Hubo salida por detras
                    cambiarBrillo(color);
                } else if ((tono <= a && tono >= b)) { //No hubo salida
                    cambiarBrillo(color);
                }
            }
        }//</editor-fold>

        public static class CambiarSaturaciónHSLColorEnTonoEspecifico extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación2 = 0.07f;
                Aplicación3 = 0.5f;
            }

            void Desaturar(int[] color) {
                float[] HSB = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                int rgb = ConversorModelosColor.HSLtoRGB(HSB[0], (float) (HSB[1] * Aplicación3), HSB[2]);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }

            @Override
            public void Efecto(int[] color) {
                float tono = Color.RGBtoHSB(color[R], color[G], color[B], null)[0];
                float ánguloColor = (float) Aplicación;
                float delta = (float) Aplicación2;
                float a = ánguloColor + delta;
                float b = ánguloColor - delta;
                if (a > 1 && (tono > b || tono < a % 1)) { //Hubo salida por delante
                    Desaturar(color);
                } else if (b < 0 && (tono < a || tono > 1 + b)) { //Hubo salida por detras
                    Desaturar(color);
                } else if ((tono <= a && tono >= b)) { //No hubo salida
                    Desaturar(color);
                }
            }
        }//</editor-fold>

        public static class CambiarBrilloHSLDeColorEnTonoEspecifico extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación2 = 0.07f;
                Aplicación3 = 0.5f;
            }

            void cambiarBrillo(int[] color) {
                float[] HSB = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                float brillo = (float) ConversorModelosColor.rangoPorcentual.AjusteCircular(Aplicación3);
                int rgb = ConversorModelosColor.HSLtoRGB(HSB[0], HSB[1], HSB[2] * brillo);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }

            @Override
            public void Efecto(int[] color) {
                float tono = Color.RGBtoHSB(color[R], color[G], color[B], null)[0];
                float ánguloColor = (float) Aplicación;
                float delta = (float) Aplicación2;
                float a = ánguloColor + delta;
                float b = ánguloColor - delta;
                if (a > 1 && (tono > b || tono < a % 1)) { //Hubo salida por delante
                    cambiarBrillo(color);
                } else if (b < 0 && (tono < a || tono > 1 + b)) { //Hubo salida por detras
                    cambiarBrillo(color);
                } else if ((tono <= a && tono >= b)) { //No hubo salida
                    cambiarBrillo(color);
                }
            }
        }//</editor-fold>

        public static class EquilibrarHSB extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = 0;
                Aplicación2 = .5;
                Aplicación3 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float tono = (float) (HSB[0] + Aplicación);
                float saturación = (float) (HSB[1] + (Aplicación2 - .5) / .5);
                float brillo = (float) (HSB[2] + (Aplicación3 - .5) / .5);
                tono = ConversorModelosColor.rangoPorcentual.AjusteCircular(tono);
                saturación = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(saturación);
                brillo = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(brillo);
                int rgb = Color.HSBtoRGB(tono, saturación, brillo);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class EquilibrarHSL extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = 0;
                Aplicación2 = .5;
                Aplicación3 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                float tono = (float) (HSB[0] + Aplicación);
                float saturación = (float) (HSB[1] + (Aplicación2 - .5) / .5);
                float brillo = (float) (HSB[2] + (Aplicación3 - .5) / .5);
                tono = ConversorModelosColor.rangoPorcentual.AjusteCircular(tono);
                saturación = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(saturación);
                brillo = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(brillo);
                int rgb = ConversorModelosColor.HSLtoRGB(tono, saturación, brillo);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class EquilibrarBinarizandoHSB extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = 0;
                Aplicación2 = .5;
                Aplicación3 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float tono = (float) (HSB[0] + Aplicación);
                float saturación = Math.round(HSB[1] + (Aplicación2 - .5) / .5);
                float brillo = Math.round(HSB[2] + (Aplicación3 - .5) / .5);
                tono = ConversorModelosColor.rangoPorcentual.AjusteCircular(tono);
                saturación = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(saturación);
                brillo = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(brillo);
                int rgb = Color.HSBtoRGB(tono, saturación, brillo);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class EquilibrarBinarizandoHSL extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = 0;
                Aplicación2 = .5;
                Aplicación3 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                float tono = (float) (HSB[0] + Aplicación);
                float saturación = Math.round(HSB[1] + (Aplicación2 - .5) / .5);
                float brillo = Math.round(HSB[2] + (Aplicación3 - .5) / .5);
                tono = ConversorModelosColor.rangoPorcentual.AjusteCircular(tono);
                saturación = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(saturación);
                brillo = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(brillo);
                int rgb = ConversorModelosColor.HSLtoRGB(tono, saturación, brillo);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class ExtenderHSB extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .5;
                Aplicación2 = .5;
                Aplicación3 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float tono = (float) (2 * HSB[0] * Aplicación);
                float saturación = (float) (2 * HSB[1] * Aplicación2);
                float brillo = (float) (2 * HSB[2] * Aplicación3);
                tono = ConversorModelosColor.rangoPorcentual.AjusteCircular(tono);
                saturación = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(saturación);
                brillo = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(brillo);
                int rgb = Color.HSBtoRGB(tono, saturación, brillo);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class ExtenderHSL extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .5;
                Aplicación2 = .5;
                Aplicación3 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSL = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                float tono = (float) (2 * HSL[0] * Aplicación);
                float saturación = (float) (2 * HSL[1] * Aplicación2);
                float brillo = (float) (2 * HSL[2] * Aplicación3);
                tono = ConversorModelosColor.rangoPorcentual.AjusteCircular(tono);
                saturación = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(saturación);
                brillo = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(brillo);
                int rgb = ConversorModelosColor.HSLtoRGB(tono, saturación, brillo);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class ExtenderBinarizandoHSB extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .5;
                Aplicación2 = .5;
                Aplicación3 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                float tono = (float) (2 * HSB[0] * Aplicación);
                float saturación = Math.round(2 * HSB[1] * Aplicación2);
                float brillo = Math.round(2 * HSB[2] * Aplicación3);
                tono = ConversorModelosColor.rangoPorcentual.AjusteCircular(tono);
                saturación = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(saturación);
                brillo = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(brillo);
                int rgb = Color.HSBtoRGB(tono, saturación, brillo);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class EquilibrarRGB extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .5;
                Aplicación2 = .5;
                Aplicación3 = .5;
                RiesgoSalida = true;
            }

            @Override
            public void Efecto(int[] color) {
                int dR = (int) (color[R] + 255 * (Aplicación - .5) / .5);
                int dG = (int) (color[G] + 255 * (Aplicación2 - .5) / .5);
                int dB = (int) (color[B] + 255 * (Aplicación3 - .5) / .5);
                color[R] = dR;
                color[G] = dG;
                color[B] = dB;
            }
        }//</editor-fold>

        public static class ExtenderRGB extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .5;
                Aplicación2 = .5;
                Aplicación3 = .5;
                RiesgoSalida = true;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                int dR = (int) (2 * color[R] * Aplicación);
                int dG = (int) (2 * color[G] * Aplicación2);
                int dB = (int) (2 * color[B] * Aplicación3);
                color[R] = dR;
                color[G] = dG;
                color[B] = dB;
            }
        }//</editor-fold>

        public static class EquilibrarBinarizandoRGB extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .5;
                Aplicación2 = .5;
                Aplicación3 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                int dR = (int) (color[R] + 128 * (Aplicación - .5) / .5);
                int dG = (int) (color[G] + 128 * (Aplicación2 - .5) / .5);
                int dB = (int) (color[B] + 128 * (Aplicación3 - .5) / .5);
                dR = (int) Rango_RGB.Discretizar(dR, 2);
                dG = (int) Rango_RGB.Discretizar(dG, 2);
                dB = (int) Rango_RGB.Discretizar(dB, 2);
                color[R] = dR;
                color[G] = dG;
                color[B] = dB;
            }
        }//</editor-fold>

        public static class ExtenderBinarizandoRGB extends BaseVariableDeAplicación implements TripleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .5;
                Aplicación2 = .5;
                Aplicación3 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                float[] HSB = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                int dR = (int) (2 * color[R] * Aplicación);
                int dG = (int) (2 * color[G] * Aplicación2);
                int dB = (int) (2 * color[B] * Aplicación3);
                dR = (int) Rango_RGB.Discretizar(dR, 2);
                dG = (int) Rango_RGB.Discretizar(dG, 2);
                dB = (int) Rango_RGB.Discretizar(dB, 2);
                color[R] = dR;
                color[G] = dG;
                color[B] = dB;
            }
        }//</editor-fold>

        public static class EquilibrarCMYK extends BaseVariableDeAplicación implements CuadrupleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .5;
                Aplicación2 = .5;
                Aplicación3 = .5;
                Aplicación4 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                float[] CMYK = ConversorModelosColor.RGBtoCMYK(color[R], color[G], color[B]);
                float dC = (float) (CMYK[0] + (Aplicación - .5) / .5);
                float dM = (float) (CMYK[1] + (Aplicación2 - .5) / .5);
                float dY = (float) (CMYK[2] + (Aplicación3 - .5) / .5);
                float dK = (float) (CMYK[3] + (Aplicación4 - .5) / .5);
                dC = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dC);
                dM = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dM);
                dY = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dY);
                dK = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dK);
                int rgb = ConversorModelosColor.CMYKtoRGB(dC, dM, dY, dK);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class EquilibrarBinarizandoCMYK extends BaseVariableDeAplicación implements CuadrupleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .5;
                Aplicación2 = .5;
                Aplicación3 = .5;
                Aplicación4 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                float[] CMYK = ConversorModelosColor.RGBtoCMYK(color[R], color[G], color[B]);
                float dC = Math.round(CMYK[0] + (Aplicación - .5) / .5);
                float dM = Math.round(CMYK[1] + (Aplicación2 - .5) / .5);
                float dY = Math.round(CMYK[2] + (Aplicación3 - .5) / .5);
                float dK = Math.round(CMYK[3] + (Aplicación4 - .5) / .5);
                dC = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dC);
                dM = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dM);
                dY = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dY);
                dK = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dK);
                int rgb = ConversorModelosColor.CMYKtoRGB(dC, dM, dY, dK);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class ExtenderCMYK extends BaseVariableDeAplicación implements CuadrupleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .5;
                Aplicación2 = .5;
                Aplicación3 = .5;
                Aplicación4 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                float[] CMYK = ConversorModelosColor.RGBtoCMYK(color[R], color[G], color[B]);
                float dC = (float) (2 * CMYK[0] * Aplicación);
                float dM = (float) (2 * CMYK[1] * Aplicación2);
                float dY = (float) (2 * CMYK[2] * Aplicación3);
                float dK = (float) (2 * CMYK[3] * Aplicación4);
                dC = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dC);
                dM = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dM);
                dY = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dY);
                dK = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dK);
                int rgb = ConversorModelosColor.CMYKtoRGB(dC, dM, dY, dK);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

        public static class ExtenderBinarizandoCMYK extends BaseVariableDeAplicación implements CuadrupleAplicación {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            {
                Aplicación = .5;
                Aplicación2 = .5;
                Aplicación3 = .5;
                Aplicación4 = .5;
            }

            @Override
            public void Efecto(int[] color) {
                float[] CMYK = ConversorModelosColor.RGBtoCMYK(color[R], color[G], color[B]);
                float dC = Math.round(2 * CMYK[0] * Aplicación);
                float dM = Math.round(2 * CMYK[1] * Aplicación2);
                float dY = Math.round(2 * CMYK[2] * Aplicación3);
                float dK = Math.round(2 * CMYK[3] * Aplicación4);
                dC = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dC);
                dM = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dM);
                dY = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dY);
                dK = ConversorModelosColor.rangoPorcentual.AjusteAExtremos(dK);
                int rgb = ConversorModelosColor.CMYKtoRGB(dC, dM, dY, dK);
                color[R] = (rgb >>> 16) & 0xff;
                color[G] = (rgb >>> 8) & 0xff;
                color[B] = rgb & 0xff;
            }
        }//</editor-fold>

    }//</editor-fold>

    public final static class Simple {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

        public abstract static class ModeloBaseSimple extends BaseModoPintura {//<editor-fold defaultstate="collapsed" desc="Cuerpo del modelo base »">

            @Override
            public void Efecto(int[] Pintar, int[] color, int P) {
                Efecto(color);
            }

            abstract public void Efecto(int[] color);
        }//</editor-fold>

        public static class Normal extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            @Override
            public void Efecto(int[] color) {
            }
        }//</editor-fold>

        public static class Binarizar extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            @Override
            public void Efecto(int[] color) {
                for (int c = R; c <= B; c++) {
                    color[c] = color[c] > 128 ? 255 : 0;
                }
            }
        }//</editor-fold>

        public static class Posterizar extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            @Override
            public void Efecto(int[] color) {
                for (int c = R; c <= B; c++) {
                    color[c] = color[c] - color[c] % 32;
                }
            }
        }//</editor-fold>

        public static class Invertir extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            @Override
            public void Efecto(int[] color) {
                for (int c = 0; c <= B; c++) {
                    color[c] = 255 - color[c];
                }
            }
        }//</editor-fold>

        public static class RuidoGris extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

            @Override
            public void Efecto(int[] color) {
                int[] c = new int[3];
                for (int i = 0; i < 3; i++) {
                    c[AleatorioEnteroEntre(0, 2)] = color[AleatorioEnteroEntre(0, 2)];
                }
                System.arraycopy(c, 0, color, 0, 3);
            }
        }//</editor-fold>

        public final static class Gris {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

            public abstract static class ModeloBaseGris extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Cuerpo del modelo base »">

                @Override
                public void Efecto(int[] Pintar, int[] color, int P) {
                    Efecto(color);
                }

                abstract int ValorGris(int[] color);

                @Override
                public void Efecto(int[] color) {
                    int G = ValorGris(color);
                    System.arraycopy(new int[]{G, G, G}, 0, color, 0, 3);
                }
            }//</editor-fold>

            public static class PromedioRGB extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return (color[R] + color[G] + color[B]) / 3;
                }
            }//</editor-fold>

            public static class Distancia extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    int n = (int) Matemática.Norma(color[R], color[G], color[B]);
                    return n > 255 ? 255 : n;
                }
            }//</editor-fold>

            public static class Distancia_Invertido extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    int n = (int) Matemática.Norma(color[R], color[G], color[B]);
                    n = n > 255 ? 255 : n;
                    n = 255 - n;
                    return n;
                }
            }//</editor-fold>

            public static class CanalRojo extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return color[R];
                }
            }//</editor-fold>

            public static class CanalVerde extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return color[G];
                }
            }//</editor-fold>

            public static class CanalAzul extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return color[B];
                }
            }//</editor-fold>

            public static class CanalMagenta extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return (color[R] * color[B]) / 255;
                }
            }//</editor-fold>

            public static class CanalAmarillo extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return (color[R] * color[G]) / 255;
                }
            }//</editor-fold>

            public static class CanalCyan extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return (color[B] * color[G]) / 255;
                }
            }//</editor-fold>

            public static class Brillo_HSL extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return (int) (255 * ConversorModelosColor.Obtener_B_deHSB(color[R], color[G], color[B]));
                }
            }//</editor-fold>

            public static class Brillo_HSB extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return (int) (255 * ConversorModelosColor.Obtener_L_deHSL(color[R], color[G], color[B]));
                }
            }//</editor-fold>

            public static class Brillo_HSL_Binarizado extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    if (ConversorModelosColor.Obtener_B_deHSB(color[R], color[G], color[B]) > .5) {
                        return 255;
                    }
                    return 0;
                }
            }//</editor-fold>

            public static class Brillo_HSB_Binarizado extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    if (ConversorModelosColor.Obtener_L_deHSL(color[R], color[G], color[B]) > .5) {
                        return 255;
                    }
                    return 0;
                }
            }//</editor-fold>

            public static class Brillo_HSL_Invertido extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return (int) (255 * (1 - ConversorModelosColor.Obtener_B_deHSB(color[R], color[G], color[B])));
                }
            }//</editor-fold>

            public static class Brillo_HSB_Invertido extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return (int) (255 * (1 - ConversorModelosColor.Obtener_L_deHSL(color[R], color[G], color[B])));
                }
            }//</editor-fold>

            public static class Tono extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return (int) (255 * Color.RGBtoHSB(color[R], color[G], color[B], null)[0]);
                }
            }//</editor-fold>

            public static class Saturación_HSB extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return (int) (255 * ConversorModelosColor.Obtener_S_deHSB(color[R], color[G], color[B]));
                }
            }//</editor-fold>

            public static class Saturación_HSL extends ModeloBaseGris {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorGris(int[] color) {
                    return (int) (255 * ConversorModelosColor.Obtener_S_deHSL(color[R], color[G], color[B]));
                }
            }//</editor-fold>
        }//</editor-fold>

        public final static class Alpha {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

            public abstract static class ModeloBaseAlpha extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Cuerpo del modelo base »">

                @Override
                public void Efecto(int[] Pintar, int[] color, int P) {
                    Efecto(color);
                }

                abstract int ValorAlpha(int[] color);

                @Override
                public void Efecto(int[] color) {
                    int alpha = ValorAlpha(color);
                    color[A] = Math.min(alpha, color[A]);
                }
            }//</editor-fold>

            public static class PromedioRGB extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return (color[R] + color[G] + color[B]) / 3;
                }
            }//</editor-fold>

            public static class Distancia extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    int n = (int) Matemática.Norma(color[R], color[G], color[B]);
                    return n > 255 ? 255 : n;
                }
            }//</editor-fold>

            public final static class Distancia_Invertido extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    int n = (int) Matemática.Norma(color[R], color[G], color[B]);
                    n = n > 255 ? 255 : n;
                    n = 255 - n;
                    return n;
                }
            }//</editor-fold>

            public static class CanalRojo extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return color[R];
                }
            }//</editor-fold>

            public static class CanalVerde extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return color[G];
                }
            }//</editor-fold>

            public static class CanalAzul extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return color[B];
                }
            }//</editor-fold>

            public static class CanalMagenta extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return (color[R] * color[B]) / 255;
                }
            }//</editor-fold>

            public static class CanalAmarillo extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return (color[R] * color[G]) / 255;
                }
            }//</editor-fold>

            public static class CanalCyan extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return (color[B] * color[G]) / 255;
                }
            }//</editor-fold>

            public static class Brillo_HSL extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return (int) (255 * ConversorModelosColor.Obtener_B_deHSB(color[R], color[G], color[B]));
                }
            }//</editor-fold>

            public static class Brillo_HSB extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return (int) (255 * ConversorModelosColor.Obtener_L_deHSL(color[R], color[G], color[B]));
                }
            }//</editor-fold>

            public static class Brillo_HSL_Binarizado extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    if (ConversorModelosColor.Obtener_B_deHSB(color[R], color[G], color[B]) > .5) {
                        return 255;
                    }
                    return 0;
                }
            }//</editor-fold>

            public static class Brillo_HSB_Binarizado extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    if (ConversorModelosColor.Obtener_L_deHSL(color[R], color[G], color[B]) > .5) {
                        return 255;
                    }
                    return 0;
                }
            }//</editor-fold>

            public static class Brillo_HSL_Invertido extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return (int) (255 * (1 - ConversorModelosColor.Obtener_B_deHSB(color[R], color[G], color[B])));
                }
            }//</editor-fold>

            public static class Brillo_HSB_Invertido extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return (int) (255 * (1 - ConversorModelosColor.Obtener_L_deHSL(color[R], color[G], color[B])));
                }
            }//</editor-fold>

            public static class Tono extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return (int) (255 * Color.RGBtoHSB(color[R], color[G], color[B], null)[0]);
                }
            }//</editor-fold>

            public static class Saturación_HSB extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return (int) (255 * ConversorModelosColor.Obtener_S_deHSB(color[R], color[G], color[B]));
                }
            }//</editor-fold>

            public static class Saturación_HSL extends ModeloBaseAlpha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                @Override
                int ValorAlpha(int[] color) {
                    return (int) (255 * ConversorModelosColor.Obtener_S_deHSL(color[R], color[G], color[B]));
                }
            }//</editor-fold>
        }//</editor-fold>

        public final static class Matriz {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

            public abstract static class BaseMatriz extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Cuerpo del modelo base »">

                public float[][] matriz;

                public void actualizarMatriz(float[][] nuevaMatriz) {
                    for (int f = 0; f < matriz.length; f++) {
                        for (int c = 0; c < matriz[f].length; c++) {
                            matriz[f][c] = nuevaMatriz[f][c];
                        }
                    }
                }

                @Override
                public void Efecto(int[] color) {
                    CalcularColor(color);
                }

                abstract void CalcularColor(int[] color);
            }//</editor-fold>

            public final static class TL {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

                public abstract static class BaseMatrizTL extends BaseMatriz {//<editor-fold defaultstate="collapsed" desc="Cuerpo del modelo base »">

                    public BaseMatrizTL(float[][] M) {
                        matriz = new float[3][3];
                        actualizarMatriz(M);
                    }
                }//</editor-fold>

                public static class RGB extends BaseMatrizTL {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    {
                        RiesgoSalida = true;
                    }

                    public RGB(float[][] matriz) {
                        super(matriz);
                    }

                    @Override
                    void CalcularColor(int[] color) {
                        float[] transformado = Matemática.MultiplicarVectorMatriz(
                                new float[]{color[R], color[G], color[B]},
                                matriz
                        );
                        for (int i = R; i <= B; i++) {
                            color[i] = (int) transformado[i];
                        }
                    }
                }//</editor-fold>

                public static class HSB extends BaseMatrizTL {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    public HSB(float[][] matriz) {
                        super(matriz);
                    }

                    @Override
                    void CalcularColor(int[] color) {
                        float[] transformado = Matemática.MultiplicarVectorMatriz(
                                Color.RGBtoHSB(color[R], color[G], color[B], null),
                                matriz
                        );
                        transformado[0] = ConversorModelosColor.rangoPorcentual.AjusteCircular(transformado[0]);
                        for (int i = 1; i < 3; i++) {
                            transformado[i] = (float) ConversorModelosColor.rangoPorcentual
                                    .AjusteAlRango(transformado[i], AJUSTE_ESPACIO_RGB);
                        }
                        int rgb = Color.HSBtoRGB(transformado[0], transformado[1], transformado[2]);
                        color[R] = (rgb >>> 16) & 0xff;
                        color[G] = (rgb >>> 8) & 0xff;
                        color[B] = rgb & 0xff;
                    }
                }//</editor-fold>

                public static class HSL extends BaseMatrizTL {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    public HSL(float[][] matriz) {
                        super(matriz);
                    }

                    @Override
                    void CalcularColor(int[] color) {
                        float[] transformado = Matemática.MultiplicarVectorMatriz(
                                ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B], true),
                                matriz
                        );
                        transformado[0] = ConversorModelosColor.rangoPorcentual.AjusteCircular(transformado[0]);
                        for (int i = 1; i < 3; i++) {
                            transformado[i] = (float) ConversorModelosColor.rangoPorcentual
                                    .AjusteAlRango(transformado[i], AJUSTE_ESPACIO_RGB);
                        }
                        int rgb = ConversorModelosColor.HSLtoRGB(transformado[0], transformado[1], transformado[2]);
                        color[R] = (rgb >>> 16) & 0xff;
                        color[G] = (rgb >>> 8) & 0xff;
                        color[B] = rgb & 0xff;
                    }
                }//</editor-fold>

                public static class R extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        color[G] = 0;
                        color[B] = 0;
                    }
                }//</editor-fold>

                public static class G extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        color[R] = 0;
                        color[B] = 0;
                    }
                }//</editor-fold>

                public static class B extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        color[R] = 0;
                        color[G] = 0;
                    }
                }//</editor-fold>

                public static class RG extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        color[B] = 0;
                    }
                }//</editor-fold>

                public static class RB extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        color[G] = 0;
                    }
                }//</editor-fold>

                public static class GB extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        color[R] = 0;
                    }
                }//</editor-fold>

                public static class RBG extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        System.arraycopy(new int[]{color[R], color[B], color[G]}, 0, color, 0, 3);
                    }
                }//</editor-fold>

                public static class GRB extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        System.arraycopy(new int[]{color[G], color[R], color[B]}, 0, color, 0, 3);
                    }
                }//</editor-fold>

                public static class GBR extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        System.arraycopy(new int[]{color[G], color[B], color[R]}, 0, color, 0, 3);
                    }
                }//</editor-fold>

                public static class BRG extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        System.arraycopy(new int[]{color[B], color[R], color[G]}, 0, color, 0, 3);
                    }
                }//</editor-fold>

                public static class BGR extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        System.arraycopy(new int[]{color[B], color[G], color[R]}, 0, color, 0, 3);
                    }
                }//</editor-fold>

                public static class ApagarCanales_RGB extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        for (int i = 0; i < 3; i++) {
                            color[i] = 0;
                        }
                    }
                }//</editor-fold>

            }//</editor-fold>

            public final static class CL {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

                public abstract static class BaseMatrizCL extends BaseMatriz {//<editor-fold defaultstate="collapsed" desc="Cuerpo del modelo base »">

                    public BaseMatrizCL(float[][] M) {
                        matriz = new float[6][3];
                        actualizarMatriz(M);
                    }
                }//</editor-fold>

                public static class RGBHSB extends BaseMatrizCL {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    public RGBHSB(float[][] matriz) {
                        super(matriz);
                    }

                    {
                        RiesgoSalida = true;
                    }

                    @Override
                    void CalcularColor(int[] color) {
                        float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                        float[] transformado = Matemática.MultiplicarVectorMatriz(
                                new float[]{color[R], color[G], color[B], 255 * HSB[0], 255 * HSB[1], 255 * HSB[2]},
                                matriz
                        );
                        for (int i = 0; i < 3; i++) {
                            color[i] = (int) transformado[i];
                        }
                    }
                }//</editor-fold>

                public static class RGBHSL extends BaseMatrizCL {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    public RGBHSL(float[][] matriz) {
                        super(matriz);
                    }

                    {
                        RiesgoSalida = true;
                    }

                    @Override
                    void CalcularColor(int[] color) {
                        float[] HSL = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                        float[] transformado = Matemática.MultiplicarVectorMatriz(
                                new float[]{color[R], color[G], color[B], 255 * HSL[0], 255 * HSL[1], 255 * HSL[2]},
                                matriz
                        );
                        for (int i = 0; i < 3; i++) {
                            color[i] = (int) transformado[i];
                        }
                    }
                }//</editor-fold>

                public static class HSBRGB extends BaseMatrizCL {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    public HSBRGB(float[][] matriz) {
                        super(matriz);
                    }

                    @Override
                    void CalcularColor(int[] color) {
                        float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                        float[] transformado = Matemática.MultiplicarVectorMatriz(
                                new float[]{HSB[0], HSB[1], HSB[2], color[R] / 255f, color[G] / 255f, color[B] / 255f},
                                matriz
                        );
                        transformado[0] = ConversorModelosColor.rangoPorcentual.AjusteCircular(transformado[0]);
                        for (int i = 1; i < 3; i++) {
                            transformado[i] = (float) ConversorModelosColor.rangoPorcentual
                                    .AjusteAlRango(transformado[i], AJUSTE_ESPACIO_RGB);
                        }
                        int rgb = Color.HSBtoRGB(transformado[0], transformado[1], transformado[2]);
                        color[R] = (rgb >>> 16) & 0xff;
                        color[G] = (rgb >>> 8) & 0xff;
                        color[B] = rgb & 0xff;
                    }
                }//</editor-fold>

                public static class HSBHSL extends BaseMatrizCL {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    public HSBHSL(float[][] matriz) {
                        super(matriz);
                    }

                    @Override
                    void CalcularColor(int[] color) {
                        float[] HSL = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                        float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                        float[] transformado = Matemática.MultiplicarVectorMatriz(
                                new float[]{HSB[0], HSB[1], HSB[2], HSL[0], HSL[1], HSL[2]},
                                matriz
                        );
                        transformado[0] = ConversorModelosColor.rangoPorcentual.AjusteCircular(transformado[0]);
                        for (int i = 1; i < 3; i++) {
                            transformado[i] = (float) ConversorModelosColor.rangoPorcentual
                                    .AjusteAlRango(transformado[i], AJUSTE_ESPACIO_RGB);
                        }
                        int rgb = Color.HSBtoRGB(transformado[0], transformado[1], transformado[2]);
                        color[R] = (rgb >>> 16) & 0xff;
                        color[G] = (rgb >>> 8) & 0xff;
                        color[B] = rgb & 0xff;
                    }
                }//</editor-fold>

                public static class HSLRGB extends BaseMatrizCL {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    public HSLRGB(float[][] matriz) {
                        super(matriz);
                    }

                    @Override
                    void CalcularColor(int[] color) {
                        float[] HSL = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                        float[] transformado = Matemática.MultiplicarVectorMatriz(
                                new float[]{HSL[0], HSL[1], HSL[2], color[R] / 255f, color[G] / 255f, color[B] / 255f},
                                matriz
                        );
                        transformado[0] = ConversorModelosColor.rangoPorcentual.AjusteCircular(transformado[0]);
                        for (int i = 1; i < 3; i++) {
                            transformado[i] = (float) ConversorModelosColor.rangoPorcentual
                                    .AjusteAlRango(transformado[i], AJUSTE_ESPACIO_RGB);
                        }
                        int rgb = ConversorModelosColor.HSLtoRGB(transformado[0], transformado[1], transformado[2]);
                        color[R] = (rgb >>> 16) & 0xff;
                        color[G] = (rgb >>> 8) & 0xff;
                        color[B] = rgb & 0xff;
                    }
                }//</editor-fold>

                public static class HSLHSB extends BaseMatrizCL {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    public HSLHSB(float[][] matriz) {
                        super(matriz);
                    }

                    @Override
                    void CalcularColor(int[] color) {
                        float[] HSL = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                        float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                        float[] transformado = Matemática.MultiplicarVectorMatriz(
                                new float[]{HSL[0], HSL[1], HSL[2], HSB[0], HSB[1], HSB[2]},
                                matriz
                        );
                        transformado[0] = ConversorModelosColor.rangoPorcentual.AjusteCircular(transformado[0]);
                        for (int i = 1; i < 3; i++) {
                            transformado[i] = (float) ConversorModelosColor.rangoPorcentual
                                    .AjusteAlRango(transformado[i], AJUSTE_ESPACIO_RGB);
                        }
                        int rgb = ConversorModelosColor.HSLtoRGB(transformado[0], transformado[1], transformado[2]);
                        color[R] = (rgb >>> 16) & 0xff;
                        color[G] = (rgb >>> 8) & 0xff;
                        color[B] = rgb & 0xff;
                    }
                }//</editor-fold>

                public static class Temperatura extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        float[] HSB = Color.RGBtoHSB(color[R], color[G], color[B], null);
                        float k = HSB[2] + color[B] / 255f;
                        float[] transformado = new float[]{HSB[1], k, k};
                        transformado[0] = ConversorModelosColor.rangoPorcentual.AjusteCircular(transformado[0]);
                        for (int i = 1; i < 3; i++) {
                            transformado[i] = (float) ConversorModelosColor.rangoPorcentual
                                    .AjusteAlRango(transformado[i], AJUSTE_ESPACIO_RGB);
                        }
                        int rgb = Color.HSBtoRGB(transformado[0], transformado[1], transformado[2]);
                        color[R] = (rgb >>> 16) & 0xff;
                        color[G] = (rgb >>> 8) & 0xff;
                        color[B] = rgb & 0xff;
                    }
                }//</editor-fold>

                public static class InsertarRGBenHSB extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        float[] transformado = new float[]{color[R] / 255f, color[G] / 255f, color[B] / 255f};
                        int rgb = Color.HSBtoRGB(transformado[0], transformado[1], transformado[2]);
                        color[R] = (rgb >>> 16) & 0xff;
                        color[G] = (rgb >>> 8) & 0xff;
                        color[B] = rgb & 0xff;
                    }
                }//</editor-fold>

                public static class InsertarRGBenHSL extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        float[] transformado = new float[]{color[R] / 255f, color[G] / 255f, color[B] / 255f};
                        int rgb = ConversorModelosColor.HSLtoRGB(transformado[0], transformado[1], transformado[2]);
                        color[R] = (rgb >>> 16) & 0xff;
                        color[G] = (rgb >>> 8) & 0xff;
                        color[B] = rgb & 0xff;
                    }
                }//</editor-fold>

                public static class InsertarHSBenHSL extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        float[] transformado = Color.RGBtoHSB(color[R], color[G], color[B], null);
                        int rgb = ConversorModelosColor.HSLtoRGB(transformado[0], transformado[1], transformado[2]);
                        color[R] = (rgb >>> 16) & 0xff;
                        color[G] = (rgb >>> 8) & 0xff;
                        color[B] = rgb & 0xff;
                    }
                }//</editor-fold>

                public static class InsertarHSLenHSB extends ModeloBaseSimple {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de pintura »">

                    @Override
                    public void Efecto(int[] color) {
                        float[] transformado = ConversorModelosColor.RGBtoHSL(color[R], color[G], color[B]);
                        int rgb = Color.HSBtoRGB(transformado[0], transformado[1], transformado[2]);
                        color[R] = (rgb >>> 16) & 0xff;
                        color[G] = (rgb >>> 8) & 0xff;
                        color[B] = rgb & 0xff;
                    }
                }//</editor-fold>

            }//</editor-fold>

        }//</editor-fold>

    }//</editor-fold>

    public static class Puntillismo extends BaseModoPintura {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

        @Override
        public void Efecto(int[] Pintar, int[] color, int P) {
            int p1;
            {
                double radio = Aplicación * 100;
                int p = P / 4;
                int c = ((p % AnchoÁrea) + AleatorioEnteroEntre(-radio, radio)) % AnchoÁrea;
                int f = ((p / AnchoÁrea) + AleatorioEnteroEntre(-radio, radio)) % AltoÁrea;
                p1 = 4 * Math.abs((f * (AnchoÁrea) + c));
            }
            System.arraycopy(Pintar, p1, color, 0, 3);
        }

        public static class Fuerte extends Puntillismo {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public void Aplicación(int[] Pintar, int[] colorR, int P) {
                System.arraycopy(colorR, 0, Pintar, P, 3);
            }
        }//</editor-fold>
    }//</editor-fold>

    public static class PuntillismoHorizontal extends BaseModoPintura {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

        @Override
        public void Efecto(int[] Pintar, int[] color, int P) {
            int p1;
            {
                double radio = Aplicación * 100;
                int p = P / 4;
                int c = ((p % AnchoÁrea) + AleatorioEnteroEntre(-radio, radio)) % AnchoÁrea;
                int f = ((p / AnchoÁrea)) % AltoÁrea;
                p1 = 4 * Math.abs((f * (AnchoÁrea) + c));
            }
            System.arraycopy(Pintar, p1, color, 0, 3);
        }

        public static class Fuerte extends PuntillismoHorizontal {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public void Aplicación(int[] Pintar, int[] colorR, int P) {
                System.arraycopy(colorR, 0, Pintar, P, 3);
            }
        }//</editor-fold>
    }//</editor-fold>

    public static class PuntillismoVertical extends BaseModoPintura {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

        @Override
        public void Efecto(int[] Pintar, int[] color, int P) {
            int p1;
            {
                double radio = Aplicación * 100;
                int p = P / 4;
                int c = ((p % AnchoÁrea)) % AnchoÁrea;
                int f = ((p / AnchoÁrea) + AleatorioEnteroEntre(-radio, radio)) % AltoÁrea;
                p1 = 4 * Math.abs((f * (AnchoÁrea) + c));
            }
            System.arraycopy(Pintar, p1, color, 0, 3);
        }

        public static class Fuerte extends PuntillismoVertical {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public void Aplicación(int[] Pintar, int[] colorR, int P) {
                System.arraycopy(colorR, 0, Pintar, P, 3);
            }
        }//</editor-fold>
    }//</editor-fold>

    public static class PuntillismoDiagonalDerecha extends BaseModoPintura {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

        @Override
        public void Efecto(int[] Pintar, int[] color, int P) {
            int p1;
            {
                double radio = Aplicación * 100;
                int p = P / 4;
                int R = AleatorioEnteroEntre(-radio, radio);
                int c = ((p % AnchoÁrea) + R) % AnchoÁrea;
                int f = ((p / AnchoÁrea) - R) % AltoÁrea;
                p1 = 4 * Math.abs((f * (AnchoÁrea) + c));
            }
            System.arraycopy(Pintar, p1, color, 0, 3);
        }

        public static class Fuerte extends PuntillismoDiagonalDerecha {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public void Aplicación(int[] Pintar, int[] colorR, int P) {
                System.arraycopy(colorR, 0, Pintar, P, 3);
            }
        }//</editor-fold>
    }//</editor-fold>

    public static class PuntillismoDiagonalIzquierda extends BaseModoPintura {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

        @Override
        public void Efecto(int[] Pintar, int[] color, int P) {
            int p1;
            {
                double radio = Aplicación * 100;
                int p = P / 4;
                int R = AleatorioEnteroEntre(-radio, radio);
                int c = ((p % AnchoÁrea) + R) % AnchoÁrea;
                int f = ((p / AnchoÁrea) + R) % AltoÁrea;
                p1 = 4 * Math.abs((f * (AnchoÁrea) + c));
            }
            System.arraycopy(Pintar, p1, color, 0, 3);
        }

        public static class Fuerte extends PuntillismoDiagonalIzquierda {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public void Aplicación(int[] Pintar, int[] colorR, int P) {
                System.arraycopy(colorR, 0, Pintar, P, 3);
            }
        }//</editor-fold>
    }//</editor-fold>
}
