package HerramientaDeImagen;

import static HerramientasMatemáticas.Matemática.*;
import java.awt.Composite;

public abstract class ModoComposicionesPersonalizadas implements Composite {

    public final static byte PINTAR_ENCIMA = 0, PINTAR_DENTRO = 1;
    public final static byte //Ajustes
            AJUSTE_LINEAL = 0,
            AJUSTE_CICLICO = 1,
            AJUSTE_REFLEJO = 2,
            AJUSTE_INSERCIÓN_LINEAL_POR_SALIDA = 3,
            AJUSTE_INSERCIÓN_CICLICA_POR_SALIDA = 4,
            AJUSTE_INSERCIÓN_REFLEJO_POR_SALIDA = 5,
            AJUSTE_ELIMINACIÓN_POR_SALIDA = 6;

    final static byte R = 0, G = 1, B = 2, A = 3, RGBA = 4, RGB = 3;
    final static Rango EspacioRGB = new Rango(0, 255);

    protected byte AJUSTE_ESPACIO_RGB;

    final static float Porcentaje(float Canal) {
        return Canal / 255.0f;
    }

    static boolean HayIntersección(int A1, int A2) {
        return A1 > 0 && A2 > 0;
    }

    static void AjustarAlEspacio(int[] Colores, byte AJUSTE_ESPACIO_RGB) {
        if (AJUSTE_ESPACIO_RGB <= AJUSTE_REFLEJO) {
            for (int e = 0; e < Colores.length / RGBA; e++) {
                for (int Ch = R; Ch <= B && Colores[A + e * RGBA] > 0; Ch++) {
                    int P = Ch + e * RGBA;
                    switch (AJUSTE_ESPACIO_RGB) {
                        case AJUSTE_LINEAL:
                            Colores[P] = EspacioRGB.AjusteLineal(Colores[P]);
                            break;
                        case AJUSTE_CICLICO:
                            Colores[P] = EspacioRGB.AjusteCiclico(Colores[P]);
                            break;
                        case AJUSTE_REFLEJO:
                            Colores[P] = (int) EspacioRGB.AjusteReflejo((double) Colores[P]);
                            break;
                    }
                }
            }
            return;
        }
        for (int e = 0; e < Colores.length / RGBA; e++) {
            final int T = e * RGBA, α = A + T;
            if (Colores[α] > 0) {
                boolean HuboSalida = false;
                for (int i = R; i <= B; i++) {
                    HuboSalida |= EspacioRGB.Por_Fuera(Colores[i + T], true);
                    if (HuboSalida) {
                        break;
                    }
                }
                if (AJUSTE_ESPACIO_RGB == AJUSTE_ELIMINACIÓN_POR_SALIDA) {
                    if (HuboSalida) {
                        Colores[α] = 0;
                    }
                } else if (!HuboSalida) {
                    Colores[α] = 0;
                } else {
                    switch (AJUSTE_ESPACIO_RGB) {
                        case AJUSTE_INSERCIÓN_LINEAL_POR_SALIDA:
                            for (int i = 0; i < RGB; i++) {
                                int P = i + T;
                                Colores[P] = EspacioRGB.AjusteLineal(Colores[P]);
                            }
                            break;
                        case AJUSTE_INSERCIÓN_CICLICA_POR_SALIDA:
                            for (int i = 0; i < RGB; i++) {
                                int P = i + T;
                                Colores[P] = EspacioRGB.AjusteCiclico(Colores[P]);
                            }
                            break;
                        case AJUSTE_INSERCIÓN_REFLEJO_POR_SALIDA:
                            for (int i = 0; i < RGB; i++) {
                                int P = i + T;
                                Colores[P] = EspacioRGB.AjusteReflejo(Colores[P]);
                            }
                    }
                }
            }
        }
    }

    static double MóduloRGB(int... Color) {
        return Distancia(Color);
    }

    static double DistanciaRGB(int[] ColorPintar, int[] ColorPintado) {
        double r = ColorPintar[R] - ColorPintado[R];
        double g = ColorPintar[G] - ColorPintado[G];
        double b = ColorPintar[B] - ColorPintado[B];
        return Distancia(r, g, b);
    }
}
