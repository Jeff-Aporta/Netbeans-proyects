package HerramientaArchivos;

import HerramientaDeImagen.Filtros_Lineales;
import HerramientaDeImagen.GeneradorDeTexto;
import HerramientaDeImagen.ModoFusión.FiltroModoPintura;
import HerramientasGUI.VentanaGráfica;
import HerramientasMatemáticas.Dupla;
import HerramientasMatemáticas.Dupla.Curva;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class BibliotecaArchivos {

    private static final int dias_actualizar = 15;

    public static void main(String[] args) {
        BufferedImage img = Imagenes.Texturas.HalftoneRadial();
        VentanaGráfica v = new VentanaGráfica(img);
        v.ColorFondo(null);
//        v.ConservarPíxelesFotograma(true);
        v.TipoAjusteFotograma(Filtros_Lineales.AJUSTE_AJUSTAR);
    }

    //<editor-fold defaultstate="collapsed" desc="Funciones para añadir a la biblioteca">
    public static BufferedImage añadir_biblioteca_appdata_img(String carpeta, String url, String nombre) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            CarpetaDeRecursos Carpeta = new CarpetaDeRecursos(
                    CarpetaDeRecursos.TIPO_APPDATA, carpeta
            ).diasActualizarArchivos(dias_actualizar);

            CarpetaDeRecursos.Recurso recurso = Carpeta.GenerarRecurso(url, nombre);
            BufferedImage Imagen = LectoEscrituraArchivos.cargar_imagen(recurso.DIRECCIÓN);
            return Imagen;
        } catch (Exception e) {
            return null;
        }
    }//</editor-fold>

    public static String añadir_biblioteca_appdata_txt(String carpeta, String url, String nombre) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {

            CarpetaDeRecursos Carpeta = new CarpetaDeRecursos(
                    CarpetaDeRecursos.TIPO_APPDATA, carpeta
            ).diasActualizarArchivos(dias_actualizar);

            CarpetaDeRecursos.Recurso recurso = Carpeta.GenerarRecurso(url, nombre);
            return LectoEscrituraArchivos.LeerArchivo_ASCII(recurso.DIRECCIÓN);
        } catch (Exception e) {
            return null;
        }
    }//</editor-fold>
    //</editor-fold>

    public static BufferedImage GenerarImagenConTexto_Derecha(BufferedImage icono, String texto, GeneradorDeTexto e) {//<editor-fold defaultstate="collapsed" desc="Implementación de código">
        if (e == null) {
            e = new GeneradorDeTexto().ColorFuente(Color.BLACK).ModificarTamañoFuente(100);
        }
        BufferedImage ImgTexto = Filtros_Lineales.Escalar(e.GenerarTexto(texto), -1, icono.getHeight());
        BufferedImage retorno = Dupla.convBufferedImage(
                icono.getWidth() + ImgTexto.getWidth(),
                icono.getHeight()
        );
        Filtros_Lineales.Ajuste_Centrado_Ajustar(icono, retorno, Dupla.IZQUIERDA, Dupla.MEDIO);
        Filtros_Lineales.Ajuste_Centrado_Ajustar(ImgTexto, retorno, Dupla.DERECHA, Dupla.MEDIO);
        return retorno;
    }//</editor-fold>

    public final static class tsv_google_spreadsheet {

        public static String Carpeta = "google spreadsheet";

        public static String buscar(String[] lista, String clave) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            for (String string : lista) {
                String[] dato = string.split("\t");
                if (clave.equals(dato[0])) {
                    return dato[1];
                }
            }
            return null;
        }//</editor-fold>

        public static String[] links() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga">
            String s = BibliotecaArchivos.añadir_biblioteca_appdata_txt(
                    Carpeta,
                    "https://docs.google.com/spreadsheets/d/e/2PACX-1vR2rpNCbZsQv9JY1HTKRGmvm_-2HyruwZTPKqPAGUJLZNzfkmB-DOA_8FqRgP2ttovP8MYYPGkNsisD/pub?output=tsv",
                    "links.tsv"
            );
            return s.split("\n");
        }//</editor-fold>
    }

    public final static class Imagenes {

        public final static class Iconos {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

            public static String Carpeta = "Iconos";

            public static BufferedImage Correcto(String texto) {
                return GenerarImagenConTexto_Derecha(Correcto(), texto, null);
            }

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vTdI2tly8tj2eibjrS3s4im069xNxREZ2mKdpoXjlNUyWwHdevGOipDJLdx33TVfrdO97fYjMTn0yTW/pub?w=500&h=500" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Juego() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return BibliotecaArchivos.añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vTdI2tly8tj2eibjrS3s4im069xNxREZ2mKdpoXjlNUyWwHdevGOipDJLdx33TVfrdO97fYjMTn0yTW/pub?w=500&h=500",
                        "Juego.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vSPYgo3-rqGv8tNK-R5oYkP5zpPFBxiI6edsXlrUMsWbw2U9ns6XrJbulsldKR207FykxbSVE6haobk/pub?w=600&h=600" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Correcto() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return BibliotecaArchivos.añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vSPYgo3-rqGv8tNK-R5oYkP5zpPFBxiI6edsXlrUMsWbw2U9ns6XrJbulsldKR207FykxbSVE6haobk/pub?w=600&h=600",
                        "Correcto.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRiNIvsSWrKNZWUGELni0Tx5C4Y2TcrqEBpe00URTl019CbXXy_ZaDKMMYSGzPqFCJe4miQSliFG3Xk/pub?w=500&h=500" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Correcto_estilo1() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return BibliotecaArchivos.añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vRiNIvsSWrKNZWUGELni0Tx5C4Y2TcrqEBpe00URTl019CbXXy_ZaDKMMYSGzPqFCJe4miQSliFG3Xk/pub?w=500&h=500",
                        "Correcto estilo 1.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vSRf-v3G76MJYnMOoW4HAJloc00wROFFgQA3v_4uC7uXQiAelmgXlxXW1nXjSZm1ymBPnmV7cCJ8zuy/pub?w=500&h=500" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Incorrecto_estilo1() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return BibliotecaArchivos.añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vSRf-v3G76MJYnMOoW4HAJloc00wROFFgQA3v_4uC7uXQiAelmgXlxXW1nXjSZm1ymBPnmV7cCJ8zuy/pub?w=500&h=500",
                        "Incorrecto estilo 1.png"
                );
            }//</editor-fold>

            public static BufferedImage Alerta(String texto) {
                return GenerarImagenConTexto_Derecha(Alerta(), texto, null);
            }

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQukrVesjgz51uyq58OBwTwhyR4J_lXNmbPSlw6HAIBCvRsT6S2gw9p_urVcBE2KFaE_jY6cFfdRaJq/pub?w=600&h=600" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Alerta() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return BibliotecaArchivos.añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vQukrVesjgz51uyq58OBwTwhyR4J_lXNmbPSlw6HAIBCvRsT6S2gw9p_urVcBE2KFaE_jY6cFfdRaJq/pub?w=600&h=600",
                        "Alerta.png"
                );
            }//</editor-fold>

        }//</editor-fold>

        public final static class Texturas {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

            public static String Carpeta = "Texturas";

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRI8wU5UkdC3LBsWjSqDmcvar5rMfdXWLOok6JCVjl3fB2yWYobzLRrKEaw95UmoVi9zVjTvx_VpBJr/pub?w=675&h=674" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage DegradadoRedNight() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vRI8wU5UkdC3LBsWjSqDmcvar5rMfdXWLOok6JCVjl3fB2yWYobzLRrKEaw95UmoVi9zVjTvx_VpBJr/pub?w=675&h=674",
                        "Degradado rednight.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen generada con código.
             */
            //</editor-fold>
            public static BufferedImage PatrónCuadricula(Paint c1, Paint c2, int lx, int ly) {//<editor-fold defaultstate="collapsed" desc=" ҉ Generación Algorítmica de la imagen">
                BufferedImage retorno = new BufferedImage(lx, ly, 2);
                Graphics2D g = retorno.createGraphics();
                g.setPaint(c1);
                int dx = lx / 2 + (lx % 2 == 0 ? 0 : 1);
                int dy = ly / 2 + (ly % 2 == 0 ? 0 : 1);
                g.fillRect(0, 0, dx, dy);
                g.fillRect(dx, dx, dx, dy);
                g.setPaint(c2);
                g.fillRect(dy, 0, dx, dy);
                g.fillRect(0, dy, dx, dy);
                g.dispose();
                return retorno;
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRA5ro1F8UbJjW0YYpUT5U3IDf1ZX5fblOw2RQQb_PF83xhp45Y7b0ul6kdHifcZWnLCNsw7cR0g19G/pub?w=652&h=652" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage HalftoneRadial() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vRA5ro1F8UbJjW0YYpUT5U3IDf1ZX5fblOw2RQQb_PF83xhp45Y7b0ul6kdHifcZWnLCNsw7cR0g19G/pub?w=652&h=652",
                        "Halftone Radial.png"
                );
            }//</editor-fold>

        }//</editor-fold>

        public final static class Logos {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

            public static String Carpeta = "Logo";

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQ4bebmHUan3es_6PxZWKJooQmno9L2aQeu-6ldc9_SnV8TOEBcqcjFIRsEmiXM9WQlVDkzglIg2Yaz/pub?w=336&h=341"/>
             */
            //</editor-fold>
            public static BufferedImage JeffAporta_Texto() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vQ4bebmHUan3es_6PxZWKJooQmno9L2aQeu-6ldc9_SnV8TOEBcqcjFIRsEmiXM9WQlVDkzglIg2Yaz/pub?w=672&h=683",
                        "Logo con texto.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRdqTC1_gN1YN2wAJlU4TFFgqKH1dhfUhEQx6HY0mbUvwO5VfDGgObSj2HBEuJiKNuTUcmQrfMbL5Nb/pub?w=509&h=509">
             */
            //</editor-fold>
            public static BufferedImage JeffAporta() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vRdqTC1_gN1YN2wAJlU4TFFgqKH1dhfUhEQx6HY0mbUvwO5VfDGgObSj2HBEuJiKNuTUcmQrfMbL5Nb/pub?w=509&h=509",
                        "Logo.png"
                );
            }//</editor-fold>

            public static BufferedImage DonacionesPaypal() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vTKBscs1Dq-nkZ-Xo87FgdZJRWuUZFq5EPGrQB-9jxcCfoOj9mpjwfDeraEvi4Z-YvO9Z_JUr4AS935/pub?w=440&h=188",
                        "Donación paypal.png"
                );
            }//</editor-fold>

            public static BufferedImage JeffDibuja() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vQToVkPYiL0sx-bxqHzKIUnVKXmthqkmZQ2YBN1EshZ9iQ8WdAhiTIPSllRvwqZuWh1AJ2oRLym-2X9/pub?w=509&h=509",
                        "Logo Jeff Dibuja.png"
                );
            }//</editor-fold>

            public static BufferedImage FBJeffrey() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vTAEP-JH2OZZcle-i6O3FS1fn8nfN6R5NdtLDNh3XW1L4fnz0u_a-6wojVTGMLSM1EWw__TmDO4mJQq/pub?w=509&h=509",
                        "Logo FBJeffrey.png"
                );
            }//</editor-fold>

            public static BufferedImage Java_MarcoYSombra() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "http://icons.iconarchive.com/icons/alecive/flatwoken/512/Apps-Java-icon.png",
                        "Logo Java.png"
                );
            }//</editor-fold>
        }//</editor-fold>

        public final static class Test {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

            public static String Carpeta = "De Prueba";

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet, de prueba #1.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQLMPF7x4U4wmuwKvS6z-hi4U5SPScv8h46l0cB3plDLI5Za3ca8U_X-8sNyBfElPsQKZ4afuyKXs25/pub?w=542&h=811" width=250 height=380>
             */
            //</editor-fold>
            public static BufferedImage IMG1() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vQLMPF7x4U4wmuwKvS6z-hi4U5SPScv8h46l0cB3plDLI5Za3ca8U_X-8sNyBfElPsQKZ4afuyKXs25/pub?w=542&h=811",
                        "Img 1.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet, de prueba #2.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vTwmo_PFQcaPDwfI6IK3LrmFJrEgH3x7dCwlJGFcC-QcW2zHb1sqILOXPpeRAKuaxt7HiYbpiXfL1pH/pub?w=506&h=337" width=340 height=235>
             */
            //</editor-fold>
            public static BufferedImage IMG2() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vTwmo_PFQcaPDwfI6IK3LrmFJrEgH3x7dCwlJGFcC-QcW2zHb1sqILOXPpeRAKuaxt7HiYbpiXfL1pH/pub?w=506&h=337",
                        "Img 2.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet, de prueba #3.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRZPxDPEzQS2WmjVftMcVQ0scQfPJuSecbqV9Y7Q0q7TFyJw-Gyvm_aKUHGDHvVRHkuf7anbfSmrRoV/pub?w=454&h=681" width=250 height=400>
             */
            //</editor-fold>
            public static BufferedImage IMG3() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vRZPxDPEzQS2WmjVftMcVQ0scQfPJuSecbqV9Y7Q0q7TFyJw-Gyvm_aKUHGDHvVRHkuf7anbfSmrRoV/pub?w=454&h=681",
                        "Img 3.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet, de prueba #4.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQ4LbwGyrdQuooDgqTIFnK62Xh2keVFJlJs8vgtSODqfrS9bM3VWSFF66uLW79YPayHegOLxvY890Y3/pub?w=918&h=690" width=300 height=220>
             */
            //</editor-fold>
            public static BufferedImage IMG4() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vQ4LbwGyrdQuooDgqTIFnK62Xh2keVFJlJs8vgtSODqfrS9bM3VWSFF66uLW79YPayHegOLxvY890Y3/pub?w=918&h=690",
                        "Img 4.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet, con transparencia png para prueba
             * número 1.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vSljrAD_V2vNl8rioKlVLhFGiHgfYiIJa7sNz9h_CgAc-9-ywzAuiJrphRAcH8rYsu3EJQtQKe74k_Y/pub?w=434&h=687" width=250 height=380>
             */
            //</editor-fold>
            public static BufferedImage PNG_1() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vSljrAD_V2vNl8rioKlVLhFGiHgfYiIJa7sNz9h_CgAc-9-ywzAuiJrphRAcH8rYsu3EJQtQKe74k_Y/pub?w=434&h=687",
                        "png 1.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet, con transparencia png para prueba
             * número 1.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vTepDriT6Pe_kp0IgcOh5l8DT8X8xIQS8jfmb73w-lU6iUQhkXLBHk6AEXst9l6yNLd1HBxMVhBBcIN/pub?w=1001&h=848" width=300 height=240>
             */
            //</editor-fold>
            public static BufferedImage PNG_2() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vTepDriT6Pe_kp0IgcOh5l8DT8X8xIQS8jfmb73w-lU6iUQhkXLBHk6AEXst9l6yNLd1HBxMVhBBcIN/pub?w=1001&h=848",
                        "png 2.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet, Wallpaper #1.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRYKGU7fea95v0BZnQZi3KsxouMOibfjPd59lBPx9RKnXdQM0ppYNbBSFFI2mBVluxaaAj6HZeFmgMT/pub?w=1280&h=720" width=320 height=180>
             */
            //</editor-fold>
            public static BufferedImage Wallpaper_1() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vRYKGU7fea95v0BZnQZi3KsxouMOibfjPd59lBPx9RKnXdQM0ppYNbBSFFI2mBVluxaaAj6HZeFmgMT/pub?w=1280&h=720",
                        "wallpaper test 1.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet, Wallpaper #2.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQ2_U6l3coM6BGdaHYdotTEFLN8EcqnbjDOpq44L9y5tL99nXEJEoq0SqLRZoxGDUqtbRVwMnnbZNNe/pub?w=1280&h=720" width=320 height=180>
             */
            //</editor-fold>
            public static BufferedImage Wallpaper_2() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vQ2_U6l3coM6BGdaHYdotTEFLN8EcqnbjDOpq44L9y5tL99nXEJEoq0SqLRZoxGDUqtbRVwMnnbZNNe/pub?w=1280&h=720",
                        "wallpaper test 2.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen cargada de internet, Wallpaper #3.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQOFbq0s6RQsbnr8Yz0Wyhbr_QWJ_2Fgfu-ro6ykg96m7sY2klJcq72qj6oPoGLMCN8FWmN4VdlTP1N/pub?w=1280&h=720" width=320 height=180>
             */
            //</editor-fold>
            public static BufferedImage Wallpaper_3() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vQOFbq0s6RQsbnr8Yz0Wyhbr_QWJ_2Fgfu-ro6ykg96m7sY2klJcq72qj6oPoGLMCN8FWmN4VdlTP1N/pub?w=1280&h=720",
                        "wallpaper test 3.png"
                );
            }//</editor-fold>
        }
//</editor-fold>

        public final static class Botones {//<editor-fold defaultstate="collapsed" desc="Cuerpo de la biblioteca »">

            public static String Carpeta = "Botones";

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQNr-dLWN5VL5C_PSsripDkzmQ3IZ45oEhUA7cZO8Eo56cq-nF7W24P4LfDTitBuOx7ynZIQppsLZHY/pub?w=300&h=110">
             */
            //</editor-fold>
            public static BufferedImage DonaciónPaypal() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vQNr-dLWN5VL5C_PSsripDkzmQ3IZ45oEhUA7cZO8Eo56cq-nF7W24P4LfDTitBuOx7ynZIQppsLZHY/pub?w=482&h=136",
                        "Donar Paypal.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQ90enN_Lh3MTjEn78DLYqROi_7sL3pfDRAF49pb4T3nLYqx47kXCfQt7-SkrEj4S8sziVCY8druyM6/pub?w=300&h=110">
             */
            //</editor-fold>
            public static BufferedImage VerEnYouTube() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vQ90enN_Lh3MTjEn78DLYqROi_7sL3pfDRAF49pb4T3nLYqx47kXCfQt7-SkrEj4S8sziVCY8druyM6/pub?w=482&h=136",
                        "Ver en YouTube.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRTPvlRslepAnHadAi3rLtWcjoydiHFc68m3y87oNRBbHIod6HxzRR2tl2RU7XyVM3wv1Ora1E5kMax/pub?w=500&h=500" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Añadir() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vRTPvlRslepAnHadAi3rLtWcjoydiHFc68m3y87oNRBbHIod6HxzRR2tl2RU7XyVM3wv1Ora1E5kMax/pub?w=500&h=500",
                        "Añadir.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vTESlUjzMMKlzyi0fiRMknP4Xm-jjhgkRQDBmPy6sQ5ZI8d6TeYGbGxHz5-3OWm16tflHZ3A-QVALLr/pub?w=500&h=500" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Reiniciar() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vTESlUjzMMKlzyi0fiRMknP4Xm-jjhgkRQDBmPy6sQ5ZI8d6TeYGbGxHz5-3OWm16tflHZ3A-QVALLr/pub?w=500&h=500",
                        "Reiniciar.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQUjqKvUUmDtmgJZegFKV964vQ4mQUeRBFac6ziaPQSs3WUP3NPWbZJOLvXOWWIG9Mt4_Fx97wvVKKW/pub?w=500&h=500" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage X() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vQUjqKvUUmDtmgJZegFKV964vQ4mQUeRBFac6ziaPQSs3WUP3NPWbZJOLvXOWWIG9Mt4_Fx97wvVKKW/pub?w=500&h=500",
                        "X.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vR71f7ErtBvikwL6OOQJcUNfjWicmNHHlrsSJ6m3LB5hFoOHzhrAvNko1avYCeTqg9F2my98lhruKZu/pub?w=500&h=500" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Basura() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vR71f7ErtBvikwL6OOQJcUNfjWicmNHHlrsSJ6m3LB5hFoOHzhrAvNko1avYCeTqg9F2my98lhruKZu/pub?w=500&h=500",
                        "Basura.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vSVFlphRb3GddOXO1RZ3WdnPOMPZDqw3j8oaKn3mA_MJqmrBp3i5LMkx1kEuY4tvpQkjSz_0AxNGekK/pub?w=500&h=500" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Adelante() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vSVFlphRb3GddOXO1RZ3WdnPOMPZDqw3j8oaKn3mA_MJqmrBp3i5LMkx1kEuY4tvpQkjSz_0AxNGekK/pub?w=500&h=500",
                        "Adelante.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón. Es la
             * {@linkplain #Adelante Imagen del botón "Adelante"} con reflejo
             * horizontal horizontal
             */
            //</editor-fold>
            public static BufferedImage Atras() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return Filtros_Lineales.ReflejarHorizontal(Adelante());
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vSu24f5pvmIrzB-ua3hzRwhZEABV96zoFwMCYv6gtkoQrY1UZOMNKcNNqiGAB5u-SCe83DCSamjUghU/pub?w=500&h=500" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Pausa() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vSu24f5pvmIrzB-ua3hzRwhZEABV96zoFwMCYv6gtkoQrY1UZOMNKcNNqiGAB5u-SCe83DCSamjUghU/pub?w=500&h=500",
                        "Pausa.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQ3FYpgcxvaq_kP-KDzk-h672X1dzgJcT8rcwJYWHFwOTLdojUVf21pAWvwjNwMTC7DGVVjPIZ91xtn/pub?w=500&h=500" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Inicio() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                BufferedImage retorno = añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vQ3FYpgcxvaq_kP-KDzk-h672X1dzgJcT8rcwJYWHFwOTLdojUVf21pAWvwjNwMTC7DGVVjPIZ91xtn/pub?w=500&h=500",
                        "Inicio.png"
                );
                if (retorno == null) {
                    return Algorítmicos.BaseIniciar();
                }
                return retorno;
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vT2kPVkxL5__fWrpzjv24PutE8cGVeItLwml3aAtZ3hVXRuDaJtahN7vfGuTMDa_J1BYFSA-qQ2t9q7/pub?w=500&h=500" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Estrella() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vT2kPVkxL5__fWrpzjv24PutE8cGVeItLwml3aAtZ3hVXRuDaJtahN7vfGuTMDa_J1BYFSA-qQ2t9q7/pub?w=500&h=500",
                        "Estrella.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vQNQ1pSK8hZaSl8dqxFZPQ8HwB9Fk3fskgCPP4J2U_qrcOxy6cS-oIv7rluB5Ybvdl44LENr-tBRoF4/pub?w=500&h=500" width=200 height=200>
             */
            //</editor-fold>
            public static BufferedImage Apagar() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vQNQ1pSK8hZaSl8dqxFZPQ8HwB9Fk3fskgCPP4J2U_qrcOxy6cS-oIv7rluB5Ybvdl44LENr-tBRoF4/pub?w=500&h=500",
                        "Apagar.png"
                );
            }//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
            /**
             * Imagen para texturizar Botón.
             * <p align="center">
             * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRYVth6ubCBsvViTBRHh3Z2k76zB7nMvYpm3uoeH5cNiztq_3M6iBtPRIXD-rZ5nEF-JjnS78YIoe9v/pub?w=200&h=200">
             */
            //</editor-fold>
            public static BufferedImage Creditos() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                return añadir_biblioteca_appdata_img(
                        Carpeta,
                        "https://docs.google.com/drawings/d/e/2PACX-1vRYVth6ubCBsvViTBRHh3Z2k76zB7nMvYpm3uoeH5cNiztq_3M6iBtPRIXD-rZ5nEF-JjnS78YIoe9v/pub?w=500&h=500",
                        "Creditos.png"
                );
            }//</editor-fold>

            public static class Algorítmicos {

                //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
                /**
                 * Imagen para texturizar Botón. dimensión 500x500
                 * <p align="center">
                 * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRX4OJu0a241Z_OOJ0_TY11cxPjuQ0d0X3_x74lb_N4pjsz15CzrxvAZD2sB8U6QrwScPBnVP7ieN8_/pub?w=500&h=500" width=200 height=200>
                 */
                //</editor-fold>
                public static BufferedImage BaseCircular() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                    return Algorítmicos.BaseCircular(Color.BLACK);
                }//</editor-fold>

                //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
                /**
                 * Imagen para texturizar Botón. dimensión 500x500
                 * <p align="center">
                 * <img src="https://docs.google.com/drawings/d/e/2PACX-1vRX4OJu0a241Z_OOJ0_TY11cxPjuQ0d0X3_x74lb_N4pjsz15CzrxvAZD2sB8U6QrwScPBnVP7ieN8_/pub?w=500&h=500" width=200 height=200>
                 */
                //</editor-fold>
                public static BufferedImage BaseCircular(Color c) {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen">
                    BufferedImage retorno = añadir_biblioteca_appdata_img(
                            Carpeta,
                            "https://docs.google.com/drawings/d/e/2PACX-1vRX4OJu0a241Z_OOJ0_TY11cxPjuQ0d0X3_x74lb_N4pjsz15CzrxvAZD2sB8U6QrwScPBnVP7ieN8_/pub?w=500&h=500",
                            "Base.png"
                    );
                    if (retorno == null) {
                        retorno = new BufferedImage(500, 500, 2) {
                            {
                                Graphics2D g = createGraphics();
                                g.setRenderingHint(
                                        RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON
                                );
                                g.setPaint(new GradientPaint(0, 0, new Color(0x00A9EA), 0, 500, new Color(0x0080B1)));
                                g.fillOval(0, 0, getWidth(), getHeight());
                            }
                        };
                    }
                    if (c.getRGB() != Color.BLACK.getRGB()) {
                        retorno = FiltroModoPintura.Aplicar(retorno, FiltroModoPintura.Monocromo(c));
                    }
                    return retorno;
                }//</editor-fold>

                //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
                /**
                 * Pone una imagen sobre el {@link #BaseCircular() botón base}.
                 */
                //</editor-fold>
                public static BufferedImage BaseCircular(BufferedImage img, Color c) {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen y ҉ Generación algorítmica">
                    BufferedImage base = BaseCircular(c);
                    img = Filtros_Lineales.Escalar(img, (int) (base.getWidth() * .5), -1);
                    Filtros_Lineales.Ajuste_Centrado_Ajustar(img, base);
                    return base;
                }//</editor-fold>

                //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
                /**
                 * Pone una imagen sobre el {@link #BaseCircular() botón base}.
                 */
                //</editor-fold>
                public static BufferedImage BaseCircular(BufferedImage img) {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen y ҉ Generación algorítmica">
                    return BaseCircular(img, Color.BLACK);
                }//</editor-fold>

                //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
                /**
                 * Pone un icono cuadrado blanco
                 * {@link #BaseCircular() botón base}.
                 */
                //</editor-fold>
                public static BufferedImage DetenerCircular() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen y ҉ Generación algorítmica">
                    return BaseDetener(Color.BLACK);
                }//</editor-fold>

                //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
                /**
                 * Pone un icono cuadrado blanco
                 * {@link #BaseCircular() botón base}.
                 */
                //</editor-fold>
                public static BufferedImage BaseDetener(Color c) {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen y ҉ Generación algorítmica">
                    return BaseCircular(new BufferedImage(400, 400, 2) {
                        {
                            Graphics g = createGraphics();
                            g.setColor(Color.WHITE);
                            g.fillRoundRect(0, 0, getWidth(), getHeight(), 100, 100);
                        }
                    }, c);
                }//</editor-fold>

                //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
                /**
                 * Pone un icono cuadrado blanco
                 * {@link #BaseCircular() botón base}.
                 */
                //</editor-fold>
                public static BufferedImage BaseIniciar() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen y ҉ Generación algorítmica">
                    return BaseIniciar(Color.BLACK);
                }//</editor-fold>

                //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
                /**
                 * Pone un icono cuadrado blanco
                 * {@link #BaseCircular() botón base}.
                 */
                //</editor-fold>
                public static BufferedImage BaseIniciar(Color c) {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen y ҉ Generación algorítmica">
                    return BaseCircular(new BufferedImage(400, 400, 2) {
                        {
                            Graphics g = createGraphics();
                            g.setColor(Color.WHITE);
                            Curva c = Curva.Circulo(new Dupla(200), 200, 300);
                            g.fillPolygon(
                                    new int[]{c.XY(0).intX(), c.XY(Math.toRadians(140)).intX(), c.XY(Math.toRadians(220)).intX()},
                                    new int[]{c.XY(0).intY(), c.XY(Math.toRadians(140)).intY(), c.XY(Math.toRadians(220)).intY()},
                                    3
                            );
                        }
                    }, c);
                }//</editor-fold>

                //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
                /**
                 * Pone un icono cuadrado blanco
                 * {@link #BaseCircular() botón base}.
                 */
                //</editor-fold>
                public static BufferedImage BasePausa() {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen y ҉ Generación algorítmica">
                    return BaseIniciar(Color.BLACK);
                }//</editor-fold>

                //<editor-fold defaultstate="collapsed" desc=" JavaDoc [≡] ">
                /**
                 * Pone un icono cuadrado blanco
                 * {@link #BaseCircular() botón base}.
                 */
                //</editor-fold>
                public static BufferedImage BasePausa(Color c) {//<editor-fold defaultstate="collapsed" desc=" [↓] Descarga de la imagen y ҉ Generación algorítmica">
                    return BaseCircular(new BufferedImage(400, 400, 2) {
                        {
                            Graphics g = createGraphics();
                            int grosor = 100;
                            g.setColor(Color.WHITE);
                            g.fillRoundRect(0, 0, grosor, getWidth(), 30, 30);
                            g.fillRoundRect(getWidth() - grosor, 0, grosor, getWidth(), 30, 30);
                        }
                    }, c);
                }//</editor-fold>

            }

        }//</editor-fold>
    }

}
