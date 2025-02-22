package HerramientaDeImagen.ModoFusión;

//<editor-fold defaultstate="collapsed" desc="Importe de librerias ●">
import HerramientasMatemáticas.Matemática;
import static HerramientasMatemáticas.Matemática.Mín;
import HerramientasMatemáticas.Matemática.Rango;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
//</editor-fold>

public abstract class BaseModoFusión implements Composite {

    //<editor-fold defaultstate="collapsed" desc="Constantes Modo fusión ●">
    public final static byte OP_ALPHA_ADICIÓN = 0;
    public final static byte OP_ALPHA_BINARIZAR = 1;
    public final static byte OP_ALPHA_DENTRO = 2;
    public final static byte OP_ALPHA_MASCARA = 3;
    public final static byte OP_ALPHA_BORRADO = 4;
    public final static byte OP_ALPHA_BORRADO_Y_APLICADO = 5;
    public final static byte OP_ALPHA_BORRADO_FUERTE = 6;
    public final static byte OP_ALPHA_BORRADO_RECTÁNGULAR = 7;
    public final static byte OP_ALPHA_DISTANCIA = 8;
    public final static byte OP_ALPHA_INTERSECCIÓN = 9;
    public final static byte OP_ALPHA_DISOLVER = 10;
    public final static byte OP_ALPHA_BITS_OP_UNIÓN = 11;
    public final static byte OP_ALPHA_BITS_OP_INTERSECCIÓN = 12;
    public final static byte OP_ALPHA_BITS_OP_XOR = 13;
    public final static byte OP_ALPHA_SUSTRACCIÓN_8BITS = 14;
    public final static byte OP_ALPHA_SUSTRACCIÓN_8BITS_LAT = 15;
    public final static byte OP_ALPHA_CONST_OPACIDAD = 16;
    public final static byte OP_ALPHA_CONST_OPACIDAD_MASCARA = 17;
    public final static byte OP_ALPHA_CONST_OPACIDAD_SALIDA = 18;
    public final static byte OP_ALPHA_CONST_OPACIDAD_INTERSECCIÓN = 19;
    public final static byte OP_ALPHA_CONST_OPACIDAD_ESCALONAR = 20;
    public final static byte OP_ALPHA_CONST_OPACIDAD_RECTIFICACIÓN_LINEAL = 21;

    public final static byte AJUSTE_A_EXTREMOS = Rango.AJUSTE_A_EXTREMOS;
    public final static byte AJUSTE_A_EXTREMOS_ABS = Rango.AJUSTE_A_EXTREMOS_ABS;
    public final static byte AJUSTE_CIRCULAR = Rango.AJUSTE_CIRCULAR;
    public final static byte AJUSTE_CIRCULAR_ABS = Rango.AJUSTE_CIRCULAR_ABS;
    public final static byte AJUSTE_REFLEJO = Rango.AJUSTE_REFLEJO;
    public final static byte AJUSTE_REFLEJO_INV = Rango.AJUSTE_REFLEJO_INV;
    public final static byte AJUSTE_MASCARA_BITS = 6;
    public final static byte AJUSTE_INSERCIÓN_EXTREMOS_POR_SALIDA = 7;
    public final static byte AJUSTE_INSERCIÓN_CIRCULAR_POR_SALIDA = 8;
    public final static byte AJUSTE_INSERCIÓN_REFLEJO_POR_SALIDA = 9;
    public final static byte AJUSTE_NULIDAD_POR_SALIDA = 10;
    public final static byte AJUSTE_SIN_DEFINIR = 11;

    public final static byte R = 0, G = 1, B = 2, A = 3, RGBA = 4, RGB = 3;

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Representa el rango de valores permitidos en los canales ARGB, a cada
     * canal le corresponden 8 bits qué es cualquier valor entero entre 0 y 255.
     * <br><br>
     * <p align="center">
     * <img src="https://i.ytimg.com/vi/RY8XcwVlwgE/hqdefault.jpg" width=350 height=270>
     */
    //</editor-fold>
    public final static Rango Rango_RGB = new Rango(0, 255);
    //</editor-fold>

    final public OperaciónBinaria OperadorBinario;

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * El riesgo de salida es una variable de valor booleano que se encarga de
     * indicar si después de efectuar las operaciones entre imagenes los valores
     * resultantes son suceptibles a salirse del
     * {@linkplain #Rango_RGB Rango RGB}.
     * <br><br><hr><p align="center">
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQqtGcX1AmmHSe-SnMPVlv1XKUA8xVRALoXdQijY7IDD9FUhcuq0FRRlEnKTQdIrQWx_AyGkjKbf41Q/pub?w=150&h=90">
     * </p><br>
     * Cuando no hay riesgo de salida, la complejidad al dibujar es menor y por
     * tanto el dibujado en una imagen se realizará de manera más rápida.
     * <br><br><hr><p align="center">
     * <img src="https://docs.google.com/drawings/d/e/2PACX-1vTy_Xt-l0k-HOZgwy0nTeber0ESnbmWr5BcoXGK8xL5ehaCooqLjlgxcgsc7NiCUDZ03Xy4aOJSdMqJ/pub?w=150&h=90">
     * </p><br>
     * Cuando hay riesgo de salida el algoritmo tendrá que recorrer cada uno de
     * los componentes realizandoles un
     * {@linkplain AjustarAlEspacio(int[]) ajuste al espacio RGB}.
     */
    //</editor-fold>
    public boolean RiesgoSalida = true;
    public byte AJUSTE_ESPACIO_RGB = AJUSTE_A_EXTREMOS;

    public double Opacidad = 1;

    public int AnchoÁrea;
    public int AltoÁrea;

    public BaseModoFusión() {
        OperadorBinario = (OperaciónBinaria) OperaciónPíxeles();
    }

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Esta función recibe todos los píxeles de la imagen que ya está dibujada,
     * los píxeles de la imagen a dibujar y una posición que representa al píxel
     * que se quiere modificar{@code .}
     * <ul style="list-style-type: circle;"><li>
     * La imagen a dibujar o imagen a pintar también es comúnmente es llamada
     * recurso, en inglés source o por su abreviación src{@code .}<br><br>
     * </li><li>
     * La imagen dibujada o imagen pintada también es comúnmente es llamada
     * Destino, en inglés destination o por su abreviación dst{@code .}
     * </li></ul>
     * toma estos datos y genera una nueva imagen a mostrar, la idea de la
     * operación entre píxeles es realizar una transformación que combine los
     * datos de ambas imagenes para generar un resultado específico.
     * <br><br>
     * La representación general de los píxeles de ambas imagenes es así:
     * <br><br>
     * <img src="https://latex.codecogs.com/png.latex?\dpi{120}PixPintado=(R_1,G_1,B_1)">
     * <br>
     * <img src="https://latex.codecogs.com/png.latex?\dpi{120}PixPintar=(R_2,G_2,B_2)">
     * <br><br>
     * PixPintado es la representación general de los píxeles de la imagen
     * pintada y PixPintar es la representación general de los píxeles de la
     * imagen a pintar, normalmente PixPintado y PixPintar corresponden al píxel
     * en la misma posición de su imagen respectiva.
     * <br><br>
     * Después de operar los píxeles de ambas imagenes se genera un nuevo píxel,
     * que sería el píxel resultado, llamado píxel de salida, salida o por su
     * traducción al inglés out, representado como:
     * <br><br>
     * <img src="https://latex.codecogs.com/png.latex?\dpi{120}Pixel=T(PixPintado\;,\;PixPintar)">
     * <br><br>
     * Siendo T(A,B) la operación entre los píxeles, es decir, esta función.
     */
    //</editor-fold>
    public abstract OperaciónBinaria OperaciónPíxeles();

    @Override
    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        //<editor-fold defaultstate="collapsed" desc="Implementación de código, especial para operar Imagenes »">
        return new CompositeContext() {
            @Override
            public void compose(Raster CapaPintar, Raster CapaPintada, WritableRaster CapaResultado) {

                AnchoÁrea = Mín(CapaPintar.getWidth(), CapaPintada.getWidth());
                AltoÁrea = Mín(CapaPintar.getHeight(), CapaPintada.getHeight());
                int área = AnchoÁrea * AltoÁrea;

                int[] PixCapaPintar = CapaPintar.getPixels(0, 0, AnchoÁrea, AltoÁrea, new int[4 * área]);
                int[] PixCapaPintada = CapaPintada.getPixels(0, 0, AnchoÁrea, AltoÁrea, new int[4 * área]);
                int[] PixCapaResultado = new int[4 * área];
                try {
                    Calcular(PixCapaPintar, PixCapaPintada, PixCapaResultado);
                    CapaResultado.setPixels(0, 0, AnchoÁrea, AltoÁrea, PixCapaResultado);
                } catch (Exception e) {
                    CapaResultado.setPixels(0, 0, AnchoÁrea, AltoÁrea, PixCapaPintada);
                }
            }

            @Override
            public void dispose() {
            }
        };
    }//</editor-fold>

    public BaseModoFusión ModificarOpacidad(double Opacidad) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (Opacidad < 0 || Opacidad > 1) {
            throw new Error("Transparencia fuera de rango, Solo valores entre 0 y 1");
        }
        OperadorBinario.ModificarOpacidad(Opacidad);
        return this;
    }//</editor-fold>

    public BaseModoFusión Modificar_AjusteRGB(byte AjusteRGB) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        AJUSTE_ESPACIO_RGB = AjusteRGB;
        return this;
    }//</editor-fold>

    public BaseModoFusión Modificar_AlphaDiscriminador(byte AlphaDiscriminador) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        OperadorBinario.ALPHA_DISCRIMINADOR = AlphaDiscriminador;
        return this;
    }//</editor-fold>

    public BaseModoFusión NoHayRiesgoDeSalidaRGB() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        OperadorBinario.NoHayRiesgoDeSalidaRGB();
        return this;
    }//</editor-fold>

    public void ModificarPixelesPintar(int[] PíxelesPintar, int Elemento) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (Opacidad < 1) {
            int p_Alpha = A + Elemento;
            int Alpha = PíxelesPintar[p_Alpha];
            PíxelesPintar[p_Alpha] = (int) (Alpha * Opacidad);
        }
    }//</editor-fold>

    public void Calcular(int[] PíxelesPintar, int[] PíxelesPintados, int[] Píxeles_Salida) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        OperadorBinario.Calcular(
                PíxelesPintar,
                PíxelesPintados,
                Píxeles_Salida
        );
    }//</editor-fold>

    public void AjustarAlEspacio(int[] Píxeles) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (AJUSTE_ESPACIO_RGB == AJUSTE_SIN_DEFINIR) {
            return;
        }
        if (AJUSTE_ESPACIO_RGB <= AJUSTE_MASCARA_BITS) {
            //<editor-fold defaultstate="collapsed" desc="Ajustes que no modifican el alpha">
            if (!RiesgoSalida) {//<editor-fold defaultstate="collapsed" desc="Si no existe riesgo de salida no hay que hacer nada">
                return;
            }//</editor-fold>

            for (int Píxel = 0; Píxel < Píxeles.length / RGBA; Píxel++) {
                for (int Componenete = R; Componenete <= B; Componenete++) {
                    //<editor-fold defaultstate="collapsed" desc="Se recorren los componentes para ajustarlos">
                    int Pos_Componente = Componenete + Píxel * RGBA;
                    int k = Píxeles[Pos_Componente];
                    switch (AJUSTE_ESPACIO_RGB) {//<editor-fold defaultstate="collapsed" desc="Selección de Ajuste">
                        case AJUSTE_A_EXTREMOS:
                        case AJUSTE_A_EXTREMOS_ABS:
                            k = Rango_RGB.AjusteAExtremosAbs(k);
                            break;
                        case AJUSTE_CIRCULAR:
                        case AJUSTE_CIRCULAR_ABS:
                        case AJUSTE_REFLEJO:
                        case AJUSTE_REFLEJO_INV:
                            k = (int) Rango_RGB.AjusteAlRango(k == 255 ? k - 1 : k, AJUSTE_ESPACIO_RGB);
                            break;
                        case AJUSTE_MASCARA_BITS://<editor-fold defaultstate="collapsed" desc="Operación de ajuste">
                            k &= 0xff;
                            break;//</editor-fold>
                        }//</editor-fold>
                    Píxeles[Pos_Componente] = k;
                }//</editor-fold>
            }
            return;
            //</editor-fold>
        }

        if (!RiesgoSalida && AJUSTE_ESPACIO_RGB == AJUSTE_NULIDAD_POR_SALIDA) {
            return;
        }
        if (!RiesgoSalida && AJUSTE_ESPACIO_RGB != AJUSTE_NULIDAD_POR_SALIDA) {
            //<editor-fold defaultstate="collapsed" desc="Si no existe riesgo de salida todos los alpha se nularan">
            for (int Píxel = 0; Píxel < Píxeles.length / RGBA; Píxel++) {//<editor-fold defaultstate="collapsed" desc="Poner todos los alpha en 0">
                final int Pos_Elemento = Píxel * RGBA;
                int Alpha = A + Pos_Elemento;
                Píxeles[Alpha] = 0;
            }//</editor-fold>
            return;
            //</editor-fold>
        }

        //<editor-fold defaultstate="collapsed" desc="Ajustes que modifican el alpha">
        for (int Píxel = 0; Píxel < Píxeles.length / RGBA; Píxel++) {
            //<editor-fold defaultstate="collapsed" desc="Se recorreran todas las componentes alpha para ajuste">
            final int Pos_Elemento = Píxel * RGBA;
            int Alpha = A + Pos_Elemento;
            if (Píxeles[Alpha] > 0) {//<editor-fold defaultstate="collapsed" desc="Si el alpha es mayor a 0 se buscará posible nulidad">
                boolean HuboSalida = false;
                for (int i = R; i <= B; i++) {//<editor-fold defaultstate="collapsed" desc="Se busca salida en alguna de las componentes">
                    HuboSalida |= Rango_RGB.Por_Fuera(Píxeles[i + Pos_Elemento], true);
                    if (HuboSalida) {
                        //<editor-fold defaultstate="collapsed" desc="Cuando encuentra un componente que salió, no revisa los demás">
                        break;
                    }//</editor-fold>
                }//</editor-fold>

                if (AJUSTE_ESPACIO_RGB == AJUSTE_NULIDAD_POR_SALIDA) {
                    //<editor-fold defaultstate="collapsed" desc="Operación de nulidad por salida, si entra continua con el siguiente píxel">
                    if (HuboSalida) {//<editor-fold defaultstate="collapsed" desc="Si hubo salida el alpha se nula">
                        Píxeles[Alpha] = 0;
                    }//</editor-fold>
                    continue;
                }//</editor-fold>

                if (HuboSalida) {
                    //<editor-fold defaultstate="collapsed" desc="Si hubo salida se realizará el ajuste correspondiente">
                    for (int ComponenteRGB = R; ComponenteRGB < RGB; ComponenteRGB++) {
                        int Pos_Componente = ComponenteRGB + Pos_Elemento;
                        switch (AJUSTE_ESPACIO_RGB) {
                            case AJUSTE_INSERCIÓN_EXTREMOS_POR_SALIDA:
                                Píxeles[Pos_Componente] = Rango_RGB.AjusteAExtremos(Píxeles[Pos_Componente]);
                                break;
                            case AJUSTE_INSERCIÓN_CIRCULAR_POR_SALIDA:
                                Píxeles[Pos_Componente] = Rango_RGB.AjusteCircular(Píxeles[Pos_Componente]);
                                break;
                            case AJUSTE_INSERCIÓN_REFLEJO_POR_SALIDA:
                                Píxeles[Pos_Componente] = Rango_RGB.AjusteReflejo(Píxeles[Pos_Componente]);
                        }
                    }
                    //</editor-fold>
                } else {//<editor-fold defaultstate="collapsed" desc="Si no hubo salida, el alpha se nula">
                    Píxeles[Alpha] = 0;
                }//</editor-fold>

            }//</editor-fold>
        }//</editor-fold>
        //</editor-fold>//</editor-fold>//</editor-fold>//</editor-fold>
    }//</editor-fold>

    public void EjecutarAntesDeModificar(int[]... Píxeles) {
    }

    public void EjecutarDespuésDeModificar(int[]... Píxeles) {
    }

    //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
    /**
     * Calcula el porcentaje de brillo de un canal,
     * {@code 0 para 0% y 1 para 100%}.
     * <br><br>
     * <p align="center">
     * <img src="https://jeffaporta01.000webhostapp.com/Librerias Java/Porcentaje rgb.png" width="300" height="151">
     * </p>
     */
    //</editor-fold>
    public static float Porcentaje(float Canal) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return Canal / 255;
    }//</editor-fold>

    public abstract class OperaciónBinaria {//<editor-fold defaultstate="collapsed" desc="Código de la clase">

        public byte ALPHA_DISCRIMINADOR = OP_ALPHA_ADICIÓN;

        public abstract void Operar(int Píxel, int[]... Píxeles);

        public OperaciónBinaria NoHayRiesgoDeSalidaRGB() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            RiesgoSalida = false;
            return this;
        }//</editor-fold>

        public OperaciónBinaria ModificarOpacidad(double opacidad) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            Opacidad = opacidad;
            return this;
        }//</editor-fold>

        public void Calcular(int[]... Píxeles) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            int[] PíxelesPintar = Píxeles[0];
            int[] PíxelesPintados = Píxeles[1];
            int[] PíxelesSalida = Píxeles[2];
            int CantidadPíxeles = PíxelesPintar.length / RGBA;
            EjecutarAntesDeModificar(Píxeles);
            for (int Elemento = 0; Elemento < CantidadPíxeles; Elemento++) {
                int p = Elemento * 4;
                ModificarPixelesPintar(PíxelesPintar, p);
                EfectuarOperacionesFusión(PíxelesPintar, PíxelesPintados, PíxelesSalida, p);
                DiscriminarOperacionesSegúnAlpha(PíxelesPintar, PíxelesPintados, PíxelesSalida, Elemento);
                AplicaciónSegúnAlphas(
                        PíxelesPintar, PíxelesPintados, PíxelesSalida, p
                );
            }
            EjecutarDespuésDeModificar(Píxeles);
            AjustarAlEspacio(PíxelesSalida);
        }//</editor-fold>

        public void DiscriminarOperacionesSegúnAlpha(
                int[] PíxelesPintar, int[] PíxelesPintados, int[] PíxelesSalida, int Elemento
        ) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            int Pos_Alpha = A + Elemento * 4;
            int k = PíxelesSalida[Pos_Alpha];
            int Alpha_Pintado = PíxelesPintados[Pos_Alpha];

            int AlphaPintar = PíxelesPintar[Pos_Alpha];

            switch (ALPHA_DISCRIMINADOR) {//<editor-fold defaultstate="collapsed" desc="Selección de la operación">
                case OP_ALPHA_ADICIÓN://<editor-fold defaultstate="collapsed" desc="Operación">
                    k = Alpha_Pintado + AlphaPintar;
                    break;//</editor-fold>
                case OP_ALPHA_BINARIZAR://<editor-fold defaultstate="collapsed" desc="Operación">
                    if (Alpha_Pintado > 128 || AlphaPintar > 128) {
                        k = 0xff;
                    }
                    break;//</editor-fold>
                case OP_ALPHA_DENTRO://<editor-fold defaultstate="collapsed" desc="Operación">
                    k = Alpha_Pintado;
                    break;//</editor-fold>
                case OP_ALPHA_MASCARA://<editor-fold defaultstate="collapsed" desc="Operación">
                    k = AlphaPintar;
                    break;//</editor-fold>
                case OP_ALPHA_BORRADO://<editor-fold defaultstate="collapsed" desc="Operación">
                    k = Alpha_Pintado - AlphaPintar;
                    PíxelesPintar[Pos_Alpha] = 1;
                    break;//</editor-fold>
                case OP_ALPHA_BORRADO_Y_APLICADO://<editor-fold defaultstate="collapsed" desc="Operación">
                    k = Alpha_Pintado - AlphaPintar;
                    break;//</editor-fold>
                case OP_ALPHA_BORRADO_FUERTE://<editor-fold defaultstate="collapsed" desc="Operación">
                    if (AlphaPintar == 0) {
                        k = Alpha_Pintado;
                    }
                    break;//</editor-fold>
                case OP_ALPHA_BORRADO_RECTÁNGULAR://<editor-fold defaultstate="collapsed" desc="Operación">
                    if (Alpha_Pintado < 255 && AlphaPintar < 255) {
                        k = AlphaPintar;
                    }
                    break;//</editor-fold>
                case OP_ALPHA_DISTANCIA://<editor-fold defaultstate="collapsed" desc="Operación">
                    k = Math.abs(Alpha_Pintado - AlphaPintar);
                    break;//</editor-fold>
                case OP_ALPHA_INTERSECCIÓN://<editor-fold defaultstate="collapsed" desc="Operación">
                    k = (int) (255 * ((Alpha_Pintado / 255f) * (AlphaPintar / 255f)));
                    break;//</editor-fold>
                case OP_ALPHA_DISOLVER://<editor-fold defaultstate="collapsed" desc="Operación">
                    double t = (AlphaPintar | Alpha_Pintado) / 255f;
                    if (Math.random() < t) {
                        k = 255;
                    } else {
                        k = 0;
                    }
                    break;//</editor-fold>
                case OP_ALPHA_BITS_OP_INTERSECCIÓN://<editor-fold defaultstate="collapsed" desc="Operación">
                    k = Alpha_Pintado & AlphaPintar;
                    break;//</editor-fold>
                case OP_ALPHA_BITS_OP_UNIÓN://<editor-fold defaultstate="collapsed" desc="Operación">
                    k = Alpha_Pintado | AlphaPintar;
                    break;//</editor-fold>
                case OP_ALPHA_BITS_OP_XOR://<editor-fold defaultstate="collapsed" desc="Operación">
                    k = Alpha_Pintado ^ AlphaPintar;
                    break;//</editor-fold>
                case OP_ALPHA_SUSTRACCIÓN_8BITS://<editor-fold defaultstate="collapsed" desc="Operación">
                    k = (Alpha_Pintado - AlphaPintar) & 0xff;
                    break;//</editor-fold>
                case OP_ALPHA_SUSTRACCIÓN_8BITS_LAT://<editor-fold defaultstate="collapsed" desc="Operación">
                    k = (AlphaPintar - Alpha_Pintado) & 0xff;
                    break;//</editor-fold>
                case OP_ALPHA_CONST_OPACIDAD://<editor-fold defaultstate="collapsed" desc="Operación">
                    if (Alpha_Pintado > 0 || AlphaPintar > 0) {
                        k = (int) (255 * Opacidad);
                    }
                    break;//</editor-fold>
                case OP_ALPHA_CONST_OPACIDAD_MASCARA://<editor-fold defaultstate="collapsed" desc="Operación">
                    if (Alpha_Pintado > 0 && AlphaPintar > 0) {
                        k = (int) (255 * Opacidad);
                    } else {
                        PíxelesPintar[Pos_Alpha] = AlphaPintar;
                        k = Alpha_Pintado + AlphaPintar;
                    }
                    break;//</editor-fold>
                case OP_ALPHA_CONST_OPACIDAD_SALIDA://<editor-fold defaultstate="collapsed" desc="Operación">
                    if (Alpha_Pintado != 0 || AlphaPintar == 0) {
                        PíxelesPintar[Pos_Alpha] = AlphaPintar;
                        k = Alpha_Pintado + AlphaPintar;
                    } else {
                        k = (int) (255 * Opacidad);
                    }
                    break;//</editor-fold>
                case OP_ALPHA_CONST_OPACIDAD_INTERSECCIÓN: //<editor-fold defaultstate="collapsed" desc="Operación">
                    if (Alpha_Pintado > 0 && AlphaPintar > 0) {
                        k = (int) (255 * Opacidad);
                    } else {
                        PíxelesPintar[Pos_Alpha] = AlphaPintar;
                        k = Alpha_Pintado + AlphaPintar;
                    }
                    break; //</editor-fold>
                case OP_ALPHA_CONST_OPACIDAD_ESCALONAR://<editor-fold defaultstate="collapsed" desc="Operación">
                    if (Alpha_Pintado + AlphaPintar >= 255 * Opacidad) {
                        k = 0xff;
                    } else {
                        k = 0;
                    }
                    break;//</editor-fold>
                case OP_ALPHA_CONST_OPACIDAD_RECTIFICACIÓN_LINEAL://<editor-fold defaultstate="collapsed" desc="Operación">
                    if (Alpha_Pintado + AlphaPintar >= 255 * Opacidad) {
                        k = Alpha_Pintado + AlphaPintar;
                    } else {
                        k = 0;
                    }
                    break;//</editor-fold>
                default://<editor-fold defaultstate="collapsed" desc="Operación">
                    throw new RuntimeException(
                            "Alpha discriminador irreconocible " + ALPHA_DISCRIMINADOR
                    );//</editor-fold>
            }//</editor-fold>

            if (k <= 8) {
                //<editor-fold defaultstate="collapsed" desc="Los pixeles con alpha por debajo de 8 (3%) se eliminarán">
                PíxelesPintados[Pos_Alpha] = 0;
                PíxelesPintar[Pos_Alpha] = 0;
                PíxelesSalida[Pos_Alpha] = 0;
            }//</editor-fold>
            else {
                PíxelesSalida[Pos_Alpha] = (int) Rango_RGB.AjusteAExtremos(k);
            }
        }//</editor-fold>

        public boolean HayIntersección(
                int[] RGBA_Pintar, int[] RGBA_Pintado, int Píxel
        ) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            int PosAlpha = A + Píxel * RGBA;
            return RGBA_Pintado[PosAlpha] > 0 && RGBA_Pintar[PosAlpha] > 0;
        }//</editor-fold>

        void EfectuarOperacionesFusión(
                int[] PíxelesPintar, int[] PíxelesPintados, int[] PíxelesSalida, int PosPíxel
        ) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            int Alpha_PíxelPintado;
            int Alpha_PíxelPintar;
            {
                int Pos_Alpha = A + PosPíxel;
                Alpha_PíxelPintado = PíxelesPintados[Pos_Alpha];
                Alpha_PíxelPintar = PíxelesPintar[Pos_Alpha];
            }

            boolean HayColorPintado = Alpha_PíxelPintado > 0;
            boolean HayColorPintar = Alpha_PíxelPintar > 0;

            if (HayColorPintado && HayColorPintar) {//<editor-fold defaultstate="collapsed" desc="Hay Intersección, hay que efectuar operaciones">
                Operar(PosPíxel, PíxelesPintar, PíxelesPintados, PíxelesSalida);
                //</editor-fold>
            } else if (HayColorPintado) {//<editor-fold defaultstate="collapsed" desc="No hay intersección pero hay Color pintado">
                NoIntersección_HayColorPintado(
                        PíxelesPintar, PíxelesPintados, PíxelesSalida,
                        PosPíxel, Alpha_PíxelPintar, Alpha_PíxelPintado
                );
                //</editor-fold>
            } else if (HayColorPintar) {//<editor-fold defaultstate="collapsed" desc="No hay intersección pero hay Color a pintar">
                NoIntersección_HayColorPintar(
                        PíxelesPintar, PíxelesPintados, PíxelesSalida,
                        PosPíxel, Alpha_PíxelPintar, Alpha_PíxelPintado
                );
                //</editor-fold>
            }
        }//</editor-fold>

        public void NoIntersección_HayColorPintado(
                int[] PíxPintar, int[] PíxPintado, int[] Salida, int PosPíxel,
                int Alpha_PíxelPintar, int Alpha_PíxelPintado
        ) {
            System.arraycopy(PíxPintado, PosPíxel, Salida, PosPíxel, 3);
        }

        public void NoIntersección_HayColorPintar(
                int[] PíxPintar, int[] PíxPintado, int[] Salida, int PosPíxel,
                int Alpha_PíxelPintar, int Alpha_PíxelPintado
        ) {
            System.arraycopy(PíxPintar, PosPíxel, Salida, PosPíxel, 3);
        }

        public void AplicaciónSegúnAlphas(
                int[] PíxPintar, int[] PíxPintado, int[] Salida, int PosPíxel
        ) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            int Alpha_PíxelPintar = PíxPintar[PosPíxel + A];
            int Alpha_PíxelPintado = PíxPintado[PosPíxel + A];
            if (Alpha_PíxelPintar < 255 || Alpha_PíxelPintado < 255) {
                float t1 = Porcentaje(Alpha_PíxelPintado);
                float t2 = Porcentaje(Alpha_PíxelPintar);
                for (int caminante = R; caminante <= B; caminante++) {
                    int P = caminante + PosPíxel;
                    int p1 = PíxPintado[P];
                    int p2 = PíxPintar[P];
                    int s = Salida[P];
                    Salida[P] = (int) (((s - p2) * t1 + p2 - p1) * t2 + p1);
                }
            }
        }//</editor-fold>
    }
    //</editor-fold>
}
