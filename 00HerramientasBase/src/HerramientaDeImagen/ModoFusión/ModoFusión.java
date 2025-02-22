package HerramientaDeImagen.ModoFusión;

import HerramientasColor.ConversorModelosColor;
import HerramientasMatemáticas.Dupla;
import static HerramientasMatemáticas.Matemática.*;
import static java.lang.Math.*;
import java.awt.Color;

public final class ModoFusión {

    public static void main(String[] args) {
        GUI_PruebaModoFusión.main(args);
    }

    public final static class Conmutar {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

        public static class Normal extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return (OperaciónBinaria) new OperaciónBinaria() {
                    @Override
                    public void Operar(int P, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelSalida = Píxeles[2];
                        System.arraycopy(PixelPintar, P, PixelSalida, P, 3);
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DisolverRuido extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            if (Math.round(Math.random()) == 0) {
                                PixelSalida[P] = PixelPintado[P];
                            } else {
                                PixelSalida[P] = PixelPintar[P];
                            }
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Máximo extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            PixelSalida[P] = Math.max(
                                    PixelPintado[P],
                                    PixelPintar[P]
                            );
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Mínimo extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            PixelSalida[P] = Math.min(
                                    PixelPintado[P],
                                    PixelPintar[P]
                            );
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static abstract class Enmallado extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

            @Override
            public void ModificarPixelesPintar(int[] PíxelesPintar, int Elemento) {
            }

            abstract boolean Regla(int[] Pintar, int P);

            @Override
            public OperaciónBinaria OperaciónPíxeles() {//<editor-fold defaultstate="collapsed" desc="Implementación del Código">
                return new OperaciónBinaria() {
                    @Override
                    public void AplicaciónSegúnAlphas(int[] p2, int[] p1, int[] r, int P) {
                        return;
                    }

                    @Override
                    public void NoIntersección_HayColorPintar(int[] Pintar, int[] Pintado, int[] Salida, int P, int a2, int a1) {
                        if (Regla(Pintar, P)) {
                            Salida[P + A] = 0;
                            Pintar[P + A] = 0;
                        } else {
                            System.arraycopy(Pintar, P, Salida, P, 3);
                        }
                    }

                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PíxelPintar = Píxeles[0];
                        int[] PíxelPintado = Píxeles[1];
                        int[] PíxelSalida = Píxeles[2];
                        boolean Regla = Regla(PíxelPintar, PosPíxel);
                        if (Regla) {
                            PíxelPintar[PosPíxel + A] = 0;
                        }
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            if (Regla) {
                                PíxelSalida[P] = PíxelPintado[P];
                            } else {
                                PíxelSalida[P] = PíxelPintar[P];
                            }
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }//</editor-fold>

            public static class Disolver extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                @Override
                boolean Regla(int[] Pintar, int P) {
                    double t = Opacidad * Pintar[P + A] / 255;
                    return Math.random() > t;
                }
            }//</editor-fold>

            public static class Cuadricula extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int c = p % AnchoÁrea;
                    int f = p / AnchoÁrea;
                    int Escala = (int) (Math.max(AnchoÁrea, AltoÁrea) * (1 - Opacidad) * (Pintar[P + A] / 255d));
                    if (Escala == 0) {
                        Escala = 1;
                    }
                    return (c / Escala + f / Escala) % 2 == 0;
                }
            }//</editor-fold>

            public static class CuadriculaAgujerosCirculares extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int c = p % AnchoÁrea;
                    int f = p / AnchoÁrea;
                    int Escala = (int) (Math.max(AnchoÁrea, AltoÁrea) * (1 - Opacidad) * (Pintar[P + A] / 255d));
                    if (Escala <= 1) {
                        Escala = 1;
                    }
                    int cr = c % Escala;
                    int fr = f % Escala;
                    int cc = Escala / 2;
                    return (c / Escala + f / Escala) % 2 == 0 && Pitagoras(cr - cc, fr - cc) <= cc;
                }
            }//</editor-fold>

            public static class CuadriculaPuntos extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int c = p % AnchoÁrea;
                    int f = p / AnchoÁrea;
                    int Escala = (int) (Math.max(AnchoÁrea, AltoÁrea) * (1 - Opacidad) * (Pintar[P + A] / 255d));
                    if (Escala <= 2) {
                        return (c + f) % 2 == 0;
                    }
                    int cr = c % Escala;
                    int fr = f % Escala;
                    float cc = Escala / 2f;
                    return (c / Escala + f / Escala) % 2 == 0 || Pitagoras(cr - cc, fr - cc) >= cc;
                }
            }//</editor-fold>

            public static class AgujerosPuntosYCuadros extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int c = p % AnchoÁrea;
                    int f = p / AnchoÁrea;
                    int Escala = (int) (Math.max(AnchoÁrea, AltoÁrea) * (1 - Opacidad) * (Pintar[P + A] / 255d));
                    if (Escala <= 1) {
                        Escala = 1;
                    }
                    int cr = c % Escala;
                    int fr = f % Escala;
                    int cc = Escala / 2;
                    return (c / Escala + f / Escala) % 2 == 0 || Pitagoras(cr - cc, fr - cc) <= cc;
                }
            }//</editor-fold>

            public static class PuntosYCuadros extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int c = p % AnchoÁrea;
                    int f = p / AnchoÁrea;
                    int Escala = (int) (Math.max(AnchoÁrea, AltoÁrea) * (1 - Opacidad) * (Pintar[P + A] / 255d));
                    if (Escala <= 2) {
                        return (c + f) % 2 == 0;
                    }
                    int cr = c % Escala;
                    int fr = f % Escala;
                    float cc = Escala / 2f;
                    return (c / Escala + f / Escala) % 2 == 0 && Pitagoras(cr - cc + 1, fr - cc + 1) >= cc;
                }
            }//</editor-fold>

            public static class Columnas extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int c = p % AnchoÁrea;
                    int Escala = (int) (AnchoÁrea * (1 - Opacidad) * (Pintar[P + A] / 255d));
                    if (Escala == 0) {
                        Escala = 1;
                    }
                    return (c / Escala) % 2 == 0;
                }
            }//</editor-fold>

            public static class Filas extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int Escala = (int) (AltoÁrea * (1 - Opacidad) * (Pintar[P + A] / 255d));
                    if (Escala == 0) {
                        Escala = 1;
                    }
                    return (f / Escala) % 2 == 0;
                }
            }//</editor-fold>

            public static class Bloques extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int c = p % AnchoÁrea;
                    int f = p / AnchoÁrea;
                    int Escala = (int) (Math.min(AnchoÁrea, AltoÁrea) * (1 - Opacidad) * (Pintar[P + A] / 255d));
                    if (Escala == 0) {
                        Escala = 1;
                    }
                    return (f / Escala) % 2 == 0 || (c / Escala) % 2 == 0;
                }
            }//</editor-fold>

            public static class Pasillos extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int c = p % AnchoÁrea;
                    int f = p / AnchoÁrea;
                    int Escala = (int) (Math.max(AnchoÁrea, AltoÁrea) * (1 - Opacidad) * (Pintar[P + A] / 255d));
                    if (Escala == 0) {
                        Escala = 1;
                    }
                    return (f / Escala) % 2 == 0 && (c / Escala) % 2 == 0;
                }
            }//</editor-fold>

            public static class Arcos extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    int d = (int) Pitagoras(c, f);
                    int Escala = (int) (Pitagoras(AnchoÁrea, AltoÁrea) * (1 - Opacidad) * (Pintar[P + A] / 255d));
                    if (Escala == 0) {
                        Escala = 1;
                    }
                    return (d / Escala) % 2 == 0;
                }
            }//</editor-fold>

            public static class Aros extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    int d = (int) Pitagoras(c - AnchoÁrea / 2, f - AltoÁrea / 2);
                    int Escala = (int) (.5 * Pitagoras(AnchoÁrea, AltoÁrea) * (1 - Opacidad) * (Pintar[P + A] / 255d));
                    if (Escala == 0) {
                        Escala = 1;
                    }
                    return (d / Escala) % 2 == 0;
                }
            }//</editor-fold>

            public static class Fuga extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    int d = (int) Math.toDegrees(new Dupla(c, f).Ángulo() * 3);
                    double Escala = (1 - Opacidad) * (Pintar[P + A]) / (.3 * pi);
                    if (Escala == 0) {
                        Escala = 1;
                    }
                    return (int) (d / Escala) % 2 == 0;
                }
            }//</editor-fold>

            public static class Radial extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    double d = Math.toDegrees(new Dupla(c - AnchoÁrea / 2, f - AltoÁrea / 2).Ángulo()) * 10;
                    int Escala = (int) (14.1 * (1 - Opacidad) * (Pintar[P + A]));
                    if (Escala == 0) {
                        Escala = 1;
                    }
                    return (int) (d / Escala) % 2 == 0;
                }
            }//</editor-fold>

            public static class Espiral extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    Dupla a = new Dupla(c - AnchoÁrea / 2, f - AltoÁrea / 2);
                    int Escala = (int) (2 * π * (1 - Opacidad) * (Pintar[P + A]));
                    if (Escala == 0) {
                        Escala = 1;
                    }
                    double d = Escala * (a.Ángulo());
                    return (int) (a.Radio() / d) % 2 == 0;
                }
            }//</editor-fold>

            public static class Diafragma extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    Dupla a = new Dupla(c - AnchoÁrea / 2, f - AltoÁrea / 2);
                    int Escala = (int) (20 * (Opacidad) * (Pintar[P + A]));
                    if (Escala == 0) {
                        Escala = 1;
                    }
                    double R1 = Escala;
                    double R2 = (a.Radio());
                    return (int) (R1 / R2) % 2 == 0;
                }
            }//</editor-fold>

            public static class CorazónRizado extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    Dupla k = new Dupla(c - AnchoÁrea / 2, -f + AltoÁrea / 2);
                    double d = k.Radio();
                    double x = k.Ángulo();
                    double e = 100 * (1 - Opacidad) * (Pintar[P + A] / 255d);
                    if (e <= 4) {
                        e = 4;
                    }
                    double r = 5 - frac(2.5 * sen(x), sqrt(.2 + abs(cos(x)))) + sen(x) + cos(2 * x) + .3 * sen(70 * x);
                    r *= e;
                    return (int) (d / r) % 2 == 0;
                }
            }//</editor-fold>

            public static class Corazón extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    Dupla k = new Dupla(c - AnchoÁrea / 2, -f + AltoÁrea / 2);
                    double d = k.Radio();
                    double x = k.Ángulo();
                    double e = 100 * (1 - Opacidad) * (Pintar[P + A] / 255d);
                    if (e <= 4) {
                        e = 4;
                    }
                    double r = sen(x) * frac(sqrt(abs(cos(x))), sen(x) + 7 / 5f) - 2 * sen(x) + 2;
                    r *= e;
                    return (int) (d / r) % 2 == 0;
                }
            }//</editor-fold>

            public static class Cardioide extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    Dupla k = new Dupla(c - AnchoÁrea / 2, -f + AltoÁrea / 2);
                    double d = k.Radio();
                    double x = k.Ángulo();
                    double e = 100 * (1 - Opacidad) * (Pintar[P + A] / 255d);
                    if (e <= 4) {
                        e = 4;
                    }
                    double r = 1 - sen(x);
                    r *= e;
                    return (int) (d / r) % 2 == 0;
                }
            }//</editor-fold>

            public static class Flor2Petalos extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    Dupla k = new Dupla(c - AnchoÁrea / 2, -f + AltoÁrea / 2);
                    double d = k.Radio();
                    double x = k.Ángulo();
                    double e = 100 * (1 - Opacidad) * (Pintar[P + A] / 255d);
                    if (e <= 4) {
                        e = 4;
                    }
                    double r = sen(x);
                    r *= e;
                    return (int) (d / r) % 2 == 0;
                }
            }//</editor-fold>

            public static class Flor3Petalos extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    Dupla k = new Dupla(c - AnchoÁrea / 2, -f + AltoÁrea / 2);
                    double d = k.Radio();
                    double x = k.Ángulo();
                    double e = 100 * (1 - Opacidad) * (Pintar[P + A] / 255d);
                    if (e <= 4) {
                        e = 4;
                    }
                    double r = sen(1.5 * x);
                    r *= e;
                    return (int) (d / r) % 2 == 0;
                }
            }//</editor-fold>

            public static class Flor4Petalos extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    Dupla k = new Dupla(c - AnchoÁrea / 2, -f + AltoÁrea / 2);
                    double d = k.Radio();
                    double x = k.Ángulo();
                    double e = 100 * (1 - Opacidad) * (Pintar[P + A] / 255d);
                    if (e <= 4) {
                        e = 4;
                    }
                    double r = sen(2 * x);
                    r *= e;
                    return (int) (d / r) % 2 == 0;
                }
            }//</editor-fold>

            public static class Flor5Petalos extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    Dupla k = new Dupla(c - AnchoÁrea / 2, -f + AltoÁrea / 2);
                    double d = k.Radio();
                    double x = k.Ángulo();
                    double e = 100 * (1 - Opacidad) * (Pintar[P + A] / 255d);
                    if (e <= 4) {
                        e = 4;
                    }
                    double r = sen(2.5 * x);
                    r *= e;
                    return (int) (d / r) % 2 == 0;
                }
            }//</editor-fold>

            public static class Flor6Petalos extends Enmallado {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

                boolean Regla(int[] Pintar, int P) {
                    int p = P / 4;
                    int f = p / AnchoÁrea;
                    int c = p % AnchoÁrea;
                    Dupla k = new Dupla(c - AnchoÁrea / 2, -f + AltoÁrea / 2);
                    double d = k.Radio();
                    double x = k.Ángulo();
                    double e = 100 * (1 - Opacidad) * (Pintar[P + A] / 255d);
                    if (e <= 4) {
                        e = 4;
                    }
                    double r = sen(3 * x);
                    r *= e;
                    return (int) (d / r) % 2 == 0;
                }
            }//</editor-fold>
        }//</editor-fold>
    }//</editor-fold>

    public final static class CombinaciónLineal {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

        public static class General extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            float[][] Matriz;

            //<editor-fold defaultstate="collapsed" desc="Constructores">
            public General() {
                super();
                this.Matriz = MatrizAleatoria();
            }//</editor-fold>

            public General(float[][] Matriz) {
                super();
                this.Matriz = Matriz;
            }

            void ActualizarMatriz(float[][] m) {
                Matriz = m;
            }

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        float[] V6 = new float[2 * RGB];
                        for (int Caminante = R; Caminante <= B; Caminante++) {//<editor-fold defaultstate="collapsed" desc="Llenado del vector a operar">
                            int P = Caminante + PosPíxel;
                            V6[Caminante] = PixelPintado[P];
                            V6[Caminante + RGB] = PixelPintar[P];
                        }//</editor-fold>
                        float ResultadoVectorMatriz[] = MultiplicarVectorMatriz(V6, Matriz);
                        for (int Caminante = R; Caminante <= B; Caminante++) {//<editor-fold defaultstate="collapsed" desc="Poner el resultado en los pixeles de salida">
                            int P = Caminante + PosPíxel;
                            PixelSalida[P] = (int) ResultadoVectorMatriz[Caminante];
                        }//</editor-fold>
                    }
                };
            }

            public static float[][] MatrizAleatoria() {
                float[][] matriz = new float[6][3];
                //<editor-fold defaultstate="collapsed" desc="Llenado de la matriz con datos aleatorios">
                for (int i = 0; i < matriz.length; i++) {
                    for (int j = 0; j < matriz[i].length; j++) {//<editor-fold defaultstate="collapsed" desc="Poner un número aleatorio en la posición ij de la matriz">
                        int Vi = -2;
                        int Vf = 2;
                        matriz[i][j] = (float) ((Vf - Vi) * Math.random() + Vi);
                    }
                    //</editor-fold>
                }
                //</editor-fold>
                return matriz;
            }
        }//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Conmutaciones de canales »">
        //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
        /**
         * Este es un modo de fusión donde se conmuta el canal azul del Píxel
         * pintado por el canal azul del píxel a pintar.
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}PixPintado=(R_1,G_1,B_1)">
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}PixPintar=(R_2,G_2,B_2)">
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}M=\begin{pmatrix}1&0&0\\0&1&0\\0&0&0\\0&0&0\\0&0&0\\0&0&1\end{pmatrix}">
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}V=PixPintado\;\cup\;PixPintar">
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}V=(R_1,G_1,B_1,R_2,G_2,B_2)">
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}Pixel=V\cdot&space;M">
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}Pixel=(R_1,G_1,B_2)">
         * <br><br>
         * Por la naturaleza de la esta fusión no tiene riesgo de salida del
         * espacio RGB
         */
        //</editor-fold>
        public static class Conmutar_Azul extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPixel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPixel] = RGBA_Pintado[R + PosPixel];
                        PixelSalida[G + PosPixel] = RGBA_Pintado[G + PosPixel];
                        PixelSalida[B + PosPixel] = PixelPintar[B + PosPixel];
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Conmutar_Rojo extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPixel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPixel] = PixelPintar[R + PosPixel];
                        PixelSalida[G + PosPixel] = RGBA_Pintado[G + PosPixel];
                        PixelSalida[B + PosPixel] = RGBA_Pintado[B + PosPixel];
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Conmutar_RojoAzul extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPixel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPixel] = PixelPintar[R + PosPixel];
                        PixelSalida[G + PosPixel] = RGBA_Pintado[G + PosPixel];
                        PixelSalida[B + PosPixel] = PixelPintar[B + PosPixel];
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Conmutar_RojoVerde extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPixel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPixel] = PixelPintar[R + PosPixel];
                        PixelSalida[G + PosPixel] = PixelPintar[G + PosPixel];
                        PixelSalida[B + PosPixel] = RGBA_Pintado[B + PosPixel];
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Conmutar_Verde extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = RGBA_Pintado[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintar[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = RGBA_Pintado[B + PosPíxel];
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Conmutar_VerdeAzul extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = RGBA_Pintado[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintar[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintar[B + PosPíxel];
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Aditivos »">
        public static class RGB_Aditivo extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPixel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPixel;
                            PixelSalida[P] = RGBA_Pintado[P] + PixelPintar[P];
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Aditivo_LuzRoja extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] + PixelPintar[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel];
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Aditivo_LuzVerde extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] + PixelPintar[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel];
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Aditivo_LuzAzul extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] + PixelPintar[B + PosPíxel];
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Aditivo_LuzAmarilla extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] + PixelPintar[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] + PixelPintar[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel];
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Aditivo_LuzMagenta extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] + PixelPintar[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] + PixelPintar[B + PosPíxel];
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Aditivo_LuzCyan extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] + PixelPintar[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] + PixelPintar[B + PosPíxel];
                    }
                };
            }
        }//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
        /**
         * Este modo de fusión toma el porcentaje de Brillo de la imagen a
         * pintar y lo sobreexpone en la imagen pintada, creando un mapa de
         * brillos.
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}PixPintado=(R_1,G_1,B_1)">
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}PixPintar=(R_2,G_2,B_2)">
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}k=\tfrac{1}{3}"><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}M=\begin{pmatrix}1&0&0\\0&1&0\\0&0&1\\k&k&k\\k&k&k\\k&k&k\end{pmatrix}">
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}V=PixPintado\;\cup\;PixPintar">
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}V=(R_1,G_1,B_1,R_2,G_2,B_2)">
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}i=\tfrac{R_2+G_2+B_2}{3}">
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}Pixel=V\cdot&space;M">
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{160}Pixel=(R_1+i,G_1+i,B_1+i)">
         * <br><br><p align="center">
         * {@linkplain HerramientaDeImagen.ModelosModosRGBA.ModoRGBA.Operación#RiesgoSalida
         * <img src="https://docs.google.com/drawings/d/e/2PACX-1vTy_Xt-l0k-HOZgwy0nTeber0ESnbmWr5BcoXGK8xL5ehaCooqLjlgxcgsc7NiCUDZ03Xy4aOJSdMqJ/pub?w=150&h=90">
         * }<
         * /p>
         */
        //</editor-fold>
        public static class RGB_Aditivo_LuzGris extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        int i = (PixelPintar[R + PosPíxel] + PixelPintar[G + PosPíxel] + PixelPintar[B + PosPíxel]) / 3;
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] + i;
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] + i;
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] + i;
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Aditivo_LuzGrisRojo extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        int i = PixelPintar[R + PosPíxel];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] + i;
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] + i;
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] + i;
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Aditivo_LuzGrisVerde extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        int i = PixelPintar[G + PosPíxel];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] + i;
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] + i;
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] + i;
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Aditivo_LuzGrisAzul extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        int i = PixelPintar[B + PosPíxel];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] + i;
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] + i;
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] + i;
                    }
                };
            }
        }//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
        /**
         * Este modo de fusión toma el porcentaje de amarillo de la imagen a
         * pintar y lo sobreexpone en la imagen pintada, creando un mapa de
         * brillos equivalente al porcentaje de amarillo.
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}PixPintado=(R_1,G_1,B_1)">
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}PixPintar=(R_2,G_2,B_2)">
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}M=\begin{pmatrix}1&0&0\\0&1&0\\0&0&1\\\tfrac{G_2}{255}&\tfrac{G_2}{255}&\tfrac{G_2}{255}\\0&0&0\\0&0&0\end{pmatrix}=\begin{pmatrix}1&0&0\\0&1&0\\0&0&1\\0&0&0\\\tfrac{R_2}{255}&\tfrac{R_2}{255}&\tfrac{R_2}{255}\\0&0&0\end{pmatrix}">
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}V=PixPintado\;\cup\;PixPintar">
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}V=(R_1,G_1,B_1,R_2,G_2,B_2)">
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}Pixel=V\cdot&space;M">
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}i=\tfrac{1}{255}\,R_2\,G_2">
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\dpi{120}Pixel=(R_1+i,\;G_1+i,\;B_1+i)">
         * <br><br>
         * Por la naturaleza de la esta fusión no tiene riesgo de salida del
         * espacio RGB
         */
        //</editor-fold>
        public static class RGB_Aditivo_LuzGrisAmarillo extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        int i = PixelPintar[R + PosPíxel] * PixelPintar[G + PosPíxel] / 255;
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] + i;
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] + i;
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] + i;
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Aditivo_LuzGrisMagenta extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        int i = PixelPintar[R + PosPíxel] * PixelPintar[B + PosPíxel] / 255;
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] + i;
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] + i;
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] + i;
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Aditivo_LuzGrisCyan extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        int i = PixelPintar[B + PosPíxel] * PixelPintar[G + PosPíxel] / 255;
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] + i;
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] + i;
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] + i;
                    }
                };
            }
        }//</editor-fold>
        //</editor-fold>

        public static class RGB_Diferencia extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            PixelSalida[P] = PixelPintado[P] - PixelPintar[P];
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Diferencia_Lat extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            PixelSalida[P] = PixelPintar[P] - PixelPintado[P];
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Diferencia_TintaCyan extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] - PixelPintar[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel];
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Diferencia_TintaMagenta extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] - PixelPintar[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel];
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Diferencia_TintaAmarilla extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] - PixelPintar[B + PosPíxel];
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Diferencia_TintaAzul extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] - PixelPintar[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] - PixelPintar[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel];
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Diferencia_TintaVerde extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel] - PixelPintar[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] - PixelPintar[B + PosPíxel];
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Diferencia_TintaRoja extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        PixelSalida[R + PosPíxel] = PixelPintado[R + PosPíxel];
                        PixelSalida[G + PosPíxel] = PixelPintado[G + PosPíxel] - PixelPintar[G + PosPíxel];
                        PixelSalida[B + PosPíxel] = PixelPintado[B + PosPíxel] - PixelPintar[B + PosPíxel];
                    }
                };
            }
        }//</editor-fold>

        public static class RGB_Promedio extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            PixelSalida[P] = (PixelPintado[P] + PixelPintar[P]) / 2;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Conmutaciones HSB »">
        public static class HSB_Conmutar_Brillo extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        int r1, g1, b1;
                        int r2, g2, b2;
                        //<editor-fold defaultstate="collapsed" desc="Carga de las componentes de los colores a operar">
                        r1 = PixelPintado[R + PosPíxel];
                        g1 = PixelPintado[G + PosPíxel];
                        b1 = PixelPintado[B + PosPíxel];
                        r2 = PixelPintar[R + PosPíxel];
                        g2 = PixelPintar[G + PosPíxel];
                        b2 = PixelPintar[B + PosPíxel];
                        //</editor-fold>
                        if (r1 == r2 && g1 == g2 && b1 == b2) {//<editor-fold defaultstate="collapsed" desc="Si el color es un gris, la conmutación es simple">
                            for (int caminante = R; caminante <= B; caminante++) {
                                int P = caminante + PosPíxel;
                                PixelSalida[P] = PixelPintado[P];
                            }
                            return;
                        }//</editor-fold>
                        float[] HSB1 = Color.RGBtoHSB(r1, g1, b1, null);
                        float[] HSB2 = Color.RGBtoHSB(r2, g2, b2, null);
                        int rgb = Color.HSBtoRGB(HSB1[0], HSB1[1], HSB2[2]);
                        for (int caminante = R; caminante <= B; caminante++) {
                            PixelSalida[caminante + PosPíxel] = rgb << 8 * (caminante + 1) >>> 24;
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class HSB_Conmutar_Saturación extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        int r1, g1, b1;
                        r1 = PixelPintado[R + PosPíxel];
                        g1 = PixelPintado[G + PosPíxel];
                        b1 = PixelPintado[B + PosPíxel];
                        int r2, g2, b2;
                        r2 = PixelPintar[R + PosPíxel];
                        g2 = PixelPintar[G + PosPíxel];
                        b2 = PixelPintar[B + PosPíxel];
                        if (r1 == r2 && g1 == g2 && b1 == b2) {
                            for (int caminante = R; caminante <= B; caminante++) {
                                int P = caminante + PosPíxel;
                                PixelSalida[P] = PixelPintado[P];
                            }
                            return;
                        }
                        float[] HSB1 = Color.RGBtoHSB(r1, g1, b1, null);
                        float[] HSB2 = Color.RGBtoHSB(r2, g2, b2, null);
                        int rgb = Color.HSBtoRGB(HSB1[0], HSB2[1], HSB1[2]);
                        for (int caminante = R; caminante <= B; caminante++) {
                            PixelSalida[caminante + PosPíxel] = rgb << 8 * (caminante + 1) >>> 24;
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class HSB_Conmutar_SaturaciónBrillo extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PíxelPintar = Píxeles[0];
                        int[] PíxelPintado = Píxeles[1];
                        int[] PíxelSalida = Píxeles[2];
                        int r1, g1, b1;
                        r1 = PíxelPintado[R + PosPíxel];
                        g1 = PíxelPintado[G + PosPíxel];
                        b1 = PíxelPintado[B + PosPíxel];
                        int r2, g2, b2;
                        r2 = PíxelPintar[R + PosPíxel];
                        g2 = PíxelPintar[G + PosPíxel];
                        b2 = PíxelPintar[B + PosPíxel];
                        if (r1 == r2 && g1 == g2 && b1 == b2) {
                            for (int caminante = R; caminante <= B; caminante++) {
                                int P = caminante + PosPíxel;
                                PíxelSalida[P] = PíxelPintado[P];
                            }
                            return;
                        }
                        float[] HSB1 = Color.RGBtoHSB(r1, g1, b1, null);
                        float[] HSB2 = Color.RGBtoHSB(r2, g2, b2, null);
                        int rgb = Color.HSBtoRGB(HSB1[0], HSB2[1], HSB2[2]);
                        for (int caminante = R; caminante <= B; caminante++) {
                            PíxelSalida[caminante + PosPíxel] = rgb << 8 * (caminante + 1) >>> 24;
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class HSB_Conmutar_Tono extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PíxelPintar = Píxeles[0];
                        int[] PíxelPintado = Píxeles[1];
                        int[] PíxelSalida = Píxeles[2];
                        int r1, g1, b1;
                        r1 = PíxelPintado[R + PosPíxel];
                        g1 = PíxelPintado[G + PosPíxel];
                        b1 = PíxelPintado[B + PosPíxel];
                        int r2, g2, b2;
                        r2 = PíxelPintar[R + PosPíxel];
                        g2 = PíxelPintar[G + PosPíxel];
                        b2 = PíxelPintar[B + PosPíxel];
                        if (r1 == r2 && g1 == g2 && b1 == b2) {
                            for (int caminante = R; caminante <= B; caminante++) {
                                int P = caminante + PosPíxel;
                                PíxelSalida[P] = PíxelPintado[P];
                            }
                            return;
                        }
                        float Tono = ConversorModelosColor.ObtenerTono(r2, g2, b2);
                        float[] HSB1 = Color.RGBtoHSB(r1, g1, b1, null);
                        int rgb = Color.HSBtoRGB(Tono, HSB1[1], HSB1[2]);
                        for (int caminante = R; caminante <= B; caminante++) {
                            PíxelSalida[caminante + PosPíxel] = rgb << 8 * (caminante + 1) >>> 24;
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class HSB_Conmutar_TonoBrillo extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PíxelPintar = Píxeles[0];
                        int[] PíxelPintado = Píxeles[1];
                        int[] PíxelSalida = Píxeles[2];
                        int r1, g1, b1;
                        r1 = PíxelPintado[R + PosPíxel];
                        g1 = PíxelPintado[G + PosPíxel];
                        b1 = PíxelPintado[B + PosPíxel];
                        int r2, g2, b2;
                        r2 = PíxelPintar[R + PosPíxel];
                        g2 = PíxelPintar[G + PosPíxel];
                        b2 = PíxelPintar[B + PosPíxel];
                        if (r1 == r2 && g1 == g2 && b1 == b2) {
                            for (int caminante = R; caminante <= B; caminante++) {
                                int P = caminante + PosPíxel;
                                PíxelSalida[P] = PíxelPintado[P];
                            }
                            return;
                        }
                        float[] HSB1 = Color.RGBtoHSB(r1, g1, b1, null);
                        float[] HSB2 = Color.RGBtoHSB(r2, g2, b2, null);
                        int rgb = Color.HSBtoRGB(HSB2[0], HSB1[1], HSB2[2]);
                        for (int caminante = R; caminante <= B; caminante++) {
                            PíxelSalida[caminante + PosPíxel] = rgb << 8 * (caminante + 1) >>> 24;
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class HSB_Conmutar_TonoSaturación extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación de la clase »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PíxelPintar = Píxeles[0];
                        int[] PíxelPintado = Píxeles[1];
                        int[] PíxelSalida = Píxeles[2];
                        int r1, g1, b1;
                        r1 = PíxelPintado[R + PosPíxel];
                        g1 = PíxelPintado[G + PosPíxel];
                        b1 = PíxelPintado[B + PosPíxel];
                        int r2, g2, b2;
                        r2 = PíxelPintar[R + PosPíxel];
                        g2 = PíxelPintar[G + PosPíxel];
                        b2 = PíxelPintar[B + PosPíxel];
                        if (r1 == r2 && g1 == g2 && b1 == b2) {
                            for (int caminante = R; caminante <= B; caminante++) {
                                int P = caminante + PosPíxel;
                                PíxelSalida[P] = PíxelPintado[P];
                            }
                            return;
                        }
                        float[] HSB1 = Color.RGBtoHSB(r1, g1, b1, null);
                        float[] HSB2 = Color.RGBtoHSB(r2, g2, b2, null);
                        int rgb = Color.HSBtoRGB(HSB2[0], HSB2[1], HSB1[2]);
                        for (int caminante = R; caminante <= B; caminante++) {
                            PíxelSalida[caminante + PosPíxel] = rgb << 8 * (caminante + 1) >>> 24;
                        }
                    }
                };
            }
        }//</editor-fold>
        //</editor-fold>
    }//</editor-fold>

    public abstract class Interferencia extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

        double interferencia = 1;

        public class Glitch_1 extends Interferencia {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PíxelPintar = Píxeles[0];
                        int[] PíxelPintado = Píxeles[1];
                        int[] PíxelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PíxelSalida[P] = (int) (interferencia * PíxelPintar[P] / (PíxelPintar[P] - PíxelPintado[P]));
                        }
                    }
                };
            }
        }//</editor-fold>

        public class Glitch_2 extends Interferencia {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PíxelPintar = Píxeles[0];
                        int[] PíxelPintado = Píxeles[1];
                        int[] PíxelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PíxelSalida[P] = (int) Math.pow((Porcentaje(PíxelPintado[P])) * PíxelPintar[P], interferencia);
                        }
                    }
                };
            }
        }//</editor-fold>
    }//</editor-fold>

    public final static class NivelBinario {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

        public static class Intersección extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintado[P] & PixelPintar[P];
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Unión extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintado[P] | PixelPintar[P];
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class UniónExcluyente extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintado[P] ^ PixelPintar[P];
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Intersección_Negación extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = ~PixelPintado[P] & ~PixelPintar[P];
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Unión_Negación extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = ~PixelPintado[P] | ~PixelPintar[P];
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Intersección_NegaciónBase extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = ~PixelPintado[P] & PixelPintar[P];
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Unión_NegaciónBase extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = ~PixelPintado[P] | PixelPintar[P];
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Intersección_NegaciónSuperior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintado[P] & ~PixelPintar[P];
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Unión_NegaciónSuperior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintado[P] | ~PixelPintar[P];
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class UniónExcluyente_Negación extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintado[P] ^ ~PixelPintar[P];
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarDerecha extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintado[P] >> (8 * PixelPintar[P] / 255);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarIzquierda extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintado[P] << (8 * PixelPintar[P] / 255);
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarDerechaLat extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintar[P] >> (8 * PixelPintado[P] / 255);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarIzquierdaLat extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintar[P] << (8 * PixelPintado[P] / 255);
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarDerecha_Negación extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = ~PixelPintado[P] >> (8 * (~PixelPintar[P] & 0xff) / 255);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarIzquierda_Negación extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = ~PixelPintado[P] << (8 * (~PixelPintar[P] & 0xff) / 255);
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarDerechaLat_Negación extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = ~PixelPintar[P] >> (8 * (~PixelPintado[P] & 0xff) / 255);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarIzquierdaLat_Negación extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = ~PixelPintar[P] << (8 * (~PixelPintado[P] & 0xff) / 255);
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarDerecha_NegaciónBase extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = ~PixelPintado[P] >> (8 * PixelPintar[P] / 255);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarIzquierda_NegaciónBase extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = ~PixelPintado[P] << (8 * PixelPintar[P] / 255);
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarDerechaLat_NegaciónSuperior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = ~PixelPintar[P] >> (8 * PixelPintado[P] / 255);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarIzquierdaLat_NegaciónSuperior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = ~PixelPintar[P] << (8 * PixelPintado[P] / 255);
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarDerecha_NegaciónSuperior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintado[P] >> (8 * (~PixelPintar[P] & 0xff) / 255);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarIzquierda_NegaciónSuperior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintado[P] << (8 * (~PixelPintar[P] & 0xff) / 255);
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarDerechaLat_NegaciónBase extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintar[P] >> (8 * (~PixelPintado[P] & 0xff) / 255);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class DesplazarIzquierdaLat_NegaciónBase extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = PixelPintar[P] << (8 * (~PixelPintado[P] & 0xff) / 255);
                            PixelSalida[P] &= 0xff;
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

    }//</editor-fold>

    public final static class Porcentual {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

        //<editor-fold defaultstate="collapsed" desc="Adición »">
        public static class Adición_Base extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (Porcentaje(PixelPintar[P]) * (PixelPintado[P] + PixelPintar[P]));
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class Adición_Superior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (Porcentaje(PixelPintado[P]) * (PixelPintado[P] + PixelPintar[P]));
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class Adición_Nula extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (Porcentaje(PixelPintado[P] * PixelPintar[P]));
                        }
                    }
                };
            }
        }//</editor-fold>
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Sustracción »">
        public static class SustracciónAbsoluta_Superior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (Porcentaje(PixelPintar[P]) * Math.abs(PixelPintado[P] - PixelPintar[P]));
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Sustracción_Base extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            PixelSalida[P] = (int) (Porcentaje(PixelPintado[P]) * (PixelPintado[P] - PixelPintar[P]));
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class SustracciónLateralizada_Superior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (Porcentaje(PixelPintar[P]) * (PixelPintar[P] - PixelPintado[P]));
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class Sustracción_Superior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            PixelSalida[P] = (int) (Porcentaje(PixelPintar[P]) * (PixelPintado[P] - PixelPintar[P]));
                        }
                    }
                };
            }
        }//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
        /**
         * <img src="https://latex.codecogs.com/png.latex?\large&space;Pixel=\frac{PixelPintado}{MaxRGB}\bigg|PixelPintar-PixelPintado\bigg|"/>
         * <br>
         * Por la naturaleza de la operación esta fusión no tiene riesgo de
         * salida del espacio RGB
         *
         *
         */
        //</editor-fold>
        public static class SustracciónAbsoluta_Base extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            PixelSalida[P] = (int) (Porcentaje(PixelPintado[P]) * Math.abs(PixelPintado[P] - PixelPintar[P]));
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
        /**
         * <img src="https://latex.codecogs.com/png.latex?\large&space;Pixel=\frac{PixelPintado}{MaxRGB}\bigg(PixelPintar-PixelPintado\bigg)"/>
         * <br>
         */
        //</editor-fold>
        public static class SustracciónLateralizada_Base extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (Porcentaje(PixelPintado[P]) * (PixelPintar[P] - PixelPintado[P]));
                        }
                    }
                };
            }
        }//</editor-fold>
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Producto »">
        //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
        /**
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\large&space;Pixel=PixPintar(\tfrac{PixPintado}{255}\cdot\tfrac{PixPintar}{255})"/>
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\large&space;Pixel=PixPintado(\tfrac{PixPintar}{255})^{2}"/>
         * <br><br><p align="center">
         * {@linkplain HerramientaDeImagen.ModelosModosRGBA.ModoRGBA.Operación#RiesgoSalida
         * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQqtGcX1AmmHSe-SnMPVlv1XKUA8xVRALoXdQijY7IDD9FUhcuq0FRRlEnKTQdIrQWx_AyGkjKbf41Q/pub?w=150&h=90">}
         * </p>
         */
        //</editor-fold>
        public static class Producto_Base extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            int p2 = PixelPintar[P];
                            PixelSalida[P] = PixelPintado[P] * (p2 * p2) / (255 * 255);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
        /**
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\large&space;Pixel=PixPintado(\tfrac{PixPintado}{255}\cdot\tfrac{PixPintar}{255})"/>
         * <br><br>
         * <img src="https://latex.codecogs.com/png.latex?\large&space;Pixel=PixPintar(\tfrac{PixPintado}{255})^{2}"/>
         * <br><br><p align="center">
         * {@linkplain HerramientaDeImagen.ModelosModosRGBA.ModoRGBA.Operación#RiesgoSalida
         * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQqtGcX1AmmHSe-SnMPVlv1XKUA8xVRALoXdQijY7IDD9FUhcuq0FRRlEnKTQdIrQWx_AyGkjKbf41Q/pub?w=150&h=90">}
         * </p>
         */
        //</editor-fold>
        public static class Producto_Superior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            int p2 = PixelPintado[P];
                            PixelSalida[P] = PixelPintar[P] * (p2 * p2) / (255 * 255);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Producto_Inversos extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            PixelSalida[P] = (int) (255 - 255 * (1 - PixelPintado[P] / 255f) * (1 - PixelPintar[P] / 255f));
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Cociente »">
        public static class Cociente_Base extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (PixelPintado[P] * ((float) PixelPintado[P] / PixelPintar[P]));
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class Cociente_Extremo_Base extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (255 * ((float) PixelPintado[P] / PixelPintar[P]));
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class Cociente_Superior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (PixelPintar[P] * ((float) PixelPintar[P] / PixelPintado[P]));
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class Cociente_Extremo_Superior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (255 * ((float) PixelPintar[P] / PixelPintado[P]));
                        }
                    }
                };
            }
        }//</editor-fold>
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Potencia »">
        //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
        /**
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\large&space;Pixel=\bigg(PixPintado\bigg)^\frac{PixPintar}{255}"/>
         * <br><br>
         * Por la naturaleza de la operación esta fusión no tiene riesgo de
         * salida del espacio RGB
         */
        //</editor-fold>
        public static class Potencia_Base extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PíxelPintar = Píxeles[0];
                        int[] PíxelPintado = Píxeles[1];
                        int[] PíxelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            PíxelSalida[P] = (int) Math.pow(PíxelPintado[P], PíxelPintar[P] / 255f);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
        /**
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\large&space;Pixel=\bigg(PixPintar\bigg)^\frac{PixPintado}{255}"/>
         * <br><br>
         * Por la naturaleza de la operación esta fusión no tiene riesgo de
         * salida del espacio RGB
         */
        //</editor-fold>
        public static class Potencia_Superior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PíxelPintar = Píxeles[0];
                        int[] PíxelPintado = Píxeles[1];
                        int[] PíxelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            PíxelSalida[P] = (int) Math.pow(
                                    PíxelPintar[P], PíxelPintado[P] / 255f
                            );
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Decremento »">
        public static class Decremento_Base extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (PixelPintado[P] * (1 - Porcentaje(PixelPintar[P])));
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class Decremento_Superior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (PixelPintar[P] * (1 - Porcentaje(PixelPintado[P])));
                        }
                    }
                };
            }
        }//</editor-fold>
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Incremento »">
        public static class Incremento_Base extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (PixelPintado[P] * (1 + Porcentaje(PixelPintar[P])));
                        }
                    }
                };
            }
        }//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
        /**
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\large&space;Pixel=PixPintar\bigg(1+\frac{PixPintado}{255}\bigg)"/>
         * <br>
         * <br>
         */
        //</editor-fold>
        public static class Incremento_Superior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (PixelPintar[P] * (1 + Porcentaje(PixelPintado[P])));
                        }
                    }
                };
            }
        }//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
        /**
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\large&space;Pixel=PixPintar\bigg(1+\frac{PixPintado}{255}\bigg)"/>
         * <br>
         * <br>
         */
        //</editor-fold>
        public static class IncrementoInvertido_Superior extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (PixelPintar[P] * (2 - Porcentaje(PixelPintado[P])));
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class IncrementoInvertido_Base extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PixelSalida[P] = (int) (PixelPintado[P] * (2 - Porcentaje(PixelPintar[P])));
                        }
                    }
                };
            }
        }//</editor-fold>
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
        /**
         * <br>
         * <img src="https://latex.codecogs.com/png.latex?\large&space;Pixel=255-\frac{(255-PixPintar)(255-PixPintado)}{255}"/>
         * <br> <br>
         * Por la naturaleza de la operación esta fusión no tiene riesgo de
         * salida del espacio RGB
         *
         *
         */
        //</editor-fold>
        public static class Trama extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int e = 0; e < PixelPintar.length / RGBA; e++) {
                            for (int Ch = R; Ch <= B; Ch++) {
                                int P = Ch + e * RGBA;
                                PixelSalida[P] = (int) (255 - Porcentaje((255 - PixelPintar[P]) * (255 - PixelPintado[P])));
                            }
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class LuzSuave extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PíxelPintar = Píxeles[0];
                        int[] PíxelPintado = Píxeles[1];
                        int[] PíxelSalida = Píxeles[2];
                        for (int componente = R; componente <= B; componente++) {
                            int P = componente + PosPíxel;
                            PíxelSalida[P] = (int) (Porcentaje(PíxelPintado[P])
                                    * (PíxelPintado[P] + Porcentaje(2 * PíxelPintar[P]) * (255 - PíxelPintado[P])));
                        }
                    }
                };
            }
        }//</editor-fold>
    }//</editor-fold>
    
    public final static class photoshop{
        
        public static class divide extends Porcentual.Cociente_Extremo_Base {
        }
        
        public static class aditivo extends CombinaciónLineal.RGB_Aditivo {
        }
        
        public static class oscurecer extends Conmutar.Mínimo {
        }
        
        public static class multiplicar extends Porcentual.Adición_Nula {
        }
        
        public static class Trama extends Porcentual.Trama {
        }

        public static class LuzSuave extends Porcentual.LuzSuave  {
        }
    }

    public final static class SinGrupo {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

        public static class Distancia extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            PixelSalida[P] = Math.abs(RGBA_Pintado[P] - PixelPintar[P]);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class LuzNivelFuerte extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            int A = RGBA_Pintado[P], B = PixelPintar[P];
                            if (B <= 2 * A - 255) {
                                PixelSalida[P] = 2 * A - 255;
                            } else if (B >= 2 * A) {
                                PixelSalida[P] = 2 * A;
                            } else {
                                PixelSalida[P] = B + 2 * A;
                            }
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class LuzNivel extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            int A = RGBA_Pintado[P], B = PixelPintar[P];
                            if (B <= A - 255) {
                                PixelSalida[P] = A - 255;
                            } else if (B >= A) {
                                PixelSalida[P] = A;
                            } else {
                                PixelSalida[P] = B + A;
                            }
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class AditivoBinarizado extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            int A = RGBA_Pintado[P], B = PixelPintar[P];
                            if (A + B >= 255) {
                                PixelSalida[P] = 255;
                            } else {
                                PixelSalida[P] = 0;
                            }
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class SubexposicionLineal extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            int A = RGBA_Pintado[P], B = PixelPintar[P];
                            PixelSalida[P] = A + B - 255;
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class ColorMasOscuro extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        int A = 0;
                        A += RGBA_Pintado[PosPíxel + R];
                        A += RGBA_Pintado[PosPíxel + G];
                        A += RGBA_Pintado[PosPíxel + B];
                        A /= 3;
                        int C = 0;
                        C += PixelPintar[PosPíxel + R];
                        C += PixelPintar[PosPíxel + G];
                        C += PixelPintar[PosPíxel + B];
                        C /= 3;
                        if (A < C) {
                            System.arraycopy(RGBA_Pintado, PosPíxel, PixelSalida, PosPíxel, 3);
                        } else {
                            System.arraycopy(PixelPintar, PosPíxel, PixelSalida, PosPíxel, 3);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class ColorMasClaro extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        int A = 0;
                        A += RGBA_Pintado[PosPíxel + R];
                        A += RGBA_Pintado[PosPíxel + G];
                        A += RGBA_Pintado[PosPíxel + B];
                        A /= 3;
                        int C = 0;
                        C += PixelPintar[PosPíxel + R];
                        C += PixelPintar[PosPíxel + G];
                        C += PixelPintar[PosPíxel + B];
                        C /= 3;
                        if (A > C) {
                            System.arraycopy(RGBA_Pintado, PosPíxel, PixelSalida, PosPíxel, 3);
                        } else {
                            System.arraycopy(PixelPintar, PosPíxel, PixelSalida, PosPíxel, 3);
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Sobreexponer extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            int A = RGBA_Pintado[P], B = PixelPintar[P];
                            PixelSalida[P] = (int) (B / (1 - A / 255f));
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class Superponer extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            int A = RGBA_Pintado[P], B = PixelPintar[P];
                            PixelSalida[P] = (int) (255 * (1 - 2 * (1 - A / 255f) * (1 - B / 255f)));
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class LuzFuerte extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            int A = RGBA_Pintado[P], B = PixelPintar[P];
                            if (B <= 128) {
                                PixelSalida[P] = (int) (255 * (2 * (A / 255f) * (B / 255f)));
                            } else {
                                PixelSalida[P] = (int) (255 * (1 - 2 * (1 - A / 255f) * (1 - B / 255f)));
                            }
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class E extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] RGBA_Pintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            int A = RGBA_Pintado[P], B = PixelPintar[P];
                            PixelSalida[P] = (int) (255 * (1 - 2 * (1 - A / 255f) * (1 - B / 255f)));
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class División extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PíxelPintar = Píxeles[0];
                        int[] PíxelPintado = Píxeles[1];
                        int[] PíxelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            PíxelSalida[P] = (int) (PíxelPintado[P] * Rango_RGB.ObtenerExtremoMayor() / PíxelPintar[P]);
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class DivisiónLateralizada extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PíxelPintar = Píxeles[0];
                        int[] PíxelPintado = Píxeles[1];
                        int[] PíxelSalida = Píxeles[2];
                        for (int Ch = R; Ch <= B; Ch++) {
                            int P = Ch + PosPíxel;
                            PíxelSalida[P] = (int) (PíxelPintar[P] * 255f / PíxelPintado[P]);
                        }
                    }
                };
            }
        }//</editor-fold>

        public static class Módulo extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            try {
                                PixelSalida[P] = PixelPintar[P] % PixelPintado[P];
                            } catch (Exception ex) {
                            }
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class MóduloLateralizado extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        for (int caminante = R; caminante <= B; caminante++) {
                            int P = caminante + PosPíxel;
                            try {
                                PixelSalida[P] = PixelPintado[P] % PixelPintar[P];
                            } catch (Exception ex) {
                            }
                        }
                    }
                }.NoHayRiesgoDeSalidaRGB();
            }
        }//</editor-fold>

        public static class Gris_Distancia extends BaseModoFusión {//<editor-fold defaultstate="collapsed" desc="Implementación del modo de fusión »">

            @Override
            public OperaciónBinaria OperaciónPíxeles() {
                return new OperaciónBinaria() {
                    @Override
                    public void Operar(int PosPíxel, int[]... Píxeles) {
                        int[] PixelPintar = Píxeles[0];
                        int[] PixelPintado = Píxeles[1];
                        int[] PixelSalida = Píxeles[2];
                        int d = (int) Norma(new double[]{
                            PixelPintar[R + PosPíxel] - PixelPintado[R + PosPíxel],
                            PixelPintar[G + PosPíxel] - PixelPintado[G + PosPíxel],
                            PixelPintar[B + PosPíxel] - PixelPintado[B + PosPíxel]
                        });
                        for (int Canal = R; Canal <= B; Canal++) {
                            int P = Canal + PosPíxel;
                            PixelSalida[P] = d;
                        }
                    }
                };
            }
        }//</editor-fold>
    }//</editor-fold>
}
