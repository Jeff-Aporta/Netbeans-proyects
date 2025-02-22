package HerramientaDeImagen;

import Degradados.AjusteRango;
import Degradados.DegradadoRadial;
import Degradados.MapaDegradado;
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientasMatemáticas.Dupla;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class Filtro_Convolución {

    public static void main(String[] args) throws Exception {
        Prueba_Filtro_Convolución.main(args);
    }

    public static BufferedImage Desenfoque_Radial(BufferedImage img, int radio) {
        BufferedImage mascara = new BufferedImage(radio, radio, 2);
        {
            Graphics2D g = mascara.createGraphics();
            MapaDegradado mapaDegradado = new MapaDegradado(
                    Color.WHITE,
                    Color.BLACK
            );
            double c = (radio - 1) / 2f;
            Dupla p1 = new Dupla(c, c);
            Dupla p2 = new Dupla(0, c + 1.3);
            DegradadoRadial degradadoRadial = new DegradadoRadial(
                    p1, p2, mapaDegradado, AjusteRango.LINEAL
            );
            g.setPaint(degradadoRadial);
            g.fillRect(0, 0, radio, radio);
        }
        float[] matriz = new float[radio * radio];
        int den = 0;
        for (int i = 0; i < mascara.getWidth(); i++) {
            for (int j = 0; j < mascara.getHeight(); j++) {
                int v = (mascara.getRGB(i, j) << 8 >>> 24);
                matriz[i * radio + j] = v;
                den += v;
            }
        }
        for (int i = 0; i < matriz.length; i++) {
            matriz[i] /= den;
        }
        BufferedImage Bordeada = Bordear_Extruir(img, radio);
        ConvolveOp convolve = new ConvolveOp(new Kernel(radio, radio, matriz));
        return convolve.filter(Bordeada, null).getSubimage(radio, radio, img.getWidth(), img.getHeight());
    }

    public static BufferedImage Desenfoque_Horizontal(BufferedImage img, int radio) {
        if (radio <= 0) {
            throw new RuntimeException("El radio debe ser mayor a 0");
        }
        float[] matriz = new float[radio];
        for (int i = 0; i < matriz.length; i++) {
            matriz[i] = 1f / matriz.length;
        }
        BufferedImage Bordeada = Bordear_Extruir(img, radio, 0);
        ConvolveOp convolve = new ConvolveOp(new Kernel(radio, 1, matriz));
        return convolve.filter(Bordeada, null).getSubimage(radio, 0, img.getWidth(), img.getHeight());
    }

    public static BufferedImage Desenfoque_Vertical(BufferedImage img, int radio) {
        if (radio < 0) {
            throw new RuntimeException("El radio debe ser positivo");
        }
        if (radio == 0) {
            return new BufferedImage(img.getWidth(), img.getHeight(), 2) {
                {
                    createGraphics().drawImage(img, 0, 0, null);
                }
            };
        }
        float[] matriz = new float[radio];
        for (int i = 0; i < matriz.length; i++) {
            matriz[i] = 1f / matriz.length;
        }
        BufferedImage Bordeada = Bordear_Extruir(img, 0, radio);
        ConvolveOp convolve = new ConvolveOp(new Kernel(1, radio, matriz));
        return convolve.filter(Bordeada, null).getSubimage(0, radio, img.getWidth(), img.getHeight());
    }

    public static BufferedImage Desenfoque_aritmético(BufferedImage img, int radio) {
        if (radio <= 0) {
            throw new RuntimeException("El radio debe ser mayor a 0");
        }
        float[] matriz = new float[radio * radio];
        for (int i = 0; i < matriz.length; i++) {
            matriz[i] = 1f / matriz.length;
        }
        BufferedImage Bordeada = Bordear_Extruir(img, radio);
        ConvolveOp convolve = new ConvolveOp(new Kernel(radio, radio, matriz));
        return convolve.filter(Bordeada, null).getSubimage(radio, radio, img.getWidth(), img.getHeight());
    }

    public static BufferedImage EndurecerBordes(BufferedImage img, int radio) {
        float[] matriz = new float[(radio) * radio];
        for (int i = 0; i < matriz.length; i++) {
            matriz[i] = -1;
        }
        matriz[matriz.length / 2] = (matriz.length);
        return MatrizDeConvoluciónRGB(img, matriz, radio, radio);
    }

    public static BufferedImage Borde_Contraste(BufferedImage img, int radio) {
        float[] matriz = new float[(radio) * radio];
        for (int i = 0; i < matriz.length; i++) {
            matriz[i] = -1;
        }
        matriz[matriz.length / 2] = (matriz.length) - 4;
        return MatrizDeConvoluciónRGB(img, matriz, radio, radio);
    }

    public static BufferedImage PerfilarBordes(BufferedImage img, int radio) {
        float[][] matriz2d = new float[radio][radio];
        for (int i = 0; i < radio; i++) {
            matriz2d[i][radio / 2] = -1;
            matriz2d[radio / 2][i] = -1;
        }
        matriz2d[radio / 2][radio / 2] = 2 * (matriz2d.length) - 1;
        float[] matriz = new float[radio * radio];
        for (int i = 0; i < radio; i++) {
            for (int j = 0; j < radio; j++) {
                matriz[i * radio + j] = matriz2d[i][j];
            }
        }
        return MatrizDeConvoluciónRGB(img, matriz, radio, radio);
    }

    public static BufferedImage DetectarBordes_Neón(BufferedImage img, int radio) {
        float[][] matriz2d = new float[radio][radio];
        for (int i = 0; i < radio; i++) {
            matriz2d[i][radio / 2] = -1;
            matriz2d[radio / 2][i] = -1;
        }
        matriz2d[radio / 2][radio / 2] = 2 * (matriz2d.length) - 2;
        float[] matriz = new float[radio * radio];
        for (int i = 0; i < radio; i++) {
            for (int j = 0; j < radio; j++) {
                matriz[i * radio + j] = matriz2d[i][j];
            }
        }
        return MatrizDeConvoluciónRGB(img, matriz, radio, radio);
    }

    public static BufferedImage DetectarBordes_Radio(BufferedImage img, int radio) {
        float[][] matriz2d = new float[radio][radio];
        for (int i = 0; i < radio; i++) {
            matriz2d[i][radio / 2] = -1;
            matriz2d[radio / 2][i] = -1;
        }
        matriz2d[radio / 2][radio / 2] = 2 * (matriz2d.length) - 4;
        float[] matriz = new float[radio * radio];
        for (int i = 0; i < radio; i++) {
            for (int j = 0; j < radio; j++) {
                matriz[i * radio + j] = matriz2d[i][j];
            }
        }
        return MatrizDeConvoluciónRGB(img, matriz, radio, radio);
    }

    public static BufferedImage Sobel_0(BufferedImage img) {
        float[] matriz = {
            -1, 0, 1,
            -2, 0, 2,
            -1, 0, 1
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Sobel_45(BufferedImage img) {
        float[] matriz = {
            0, 1, 2,
            -1, 0, 1,
            -2, -1, 0
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Sobel_90(BufferedImage img) {
        float[] matriz = {
            1, 2, 1,
            0, 0, 0,
            -1, -2, -1
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Sobel_135(BufferedImage img) {
        float[] matriz = {
            2, 1, 0,
            1, 0, -1,
            0, -1, -2
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Sobel_180(BufferedImage img) {
        float[] matriz = {
            1, 0, -1,
            2, 0, -2,
            1, 0, -1
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Sobel_225(BufferedImage img) {
        float[] matriz = {
            0, -1, -2,
            1, 0, -1,
            2, 1, 0
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Sobel_270(BufferedImage img) {
        float[] matriz = {
            -1, -2, -1,
            0, 0, 0,
            1, 2, 1
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Sobel_315(BufferedImage img) {
        float[] matriz = {
            -2, -1, 0,
            -1, 0, 1,
            0, 1, 2
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Sobel2_0(BufferedImage img) {
        float[] matriz = {
            -2, -1, 0, 1, 2,
            -2, -1, 0, 1, 2,
            -4, -2, 0, 2, 4,
            -2, -1, 0, 1, 2,
            -2, -1, 0, 1, 2
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Sobel2_45(BufferedImage img) {
        float[] matriz = {
            0, 1, 2, 2, 4,
            -1, 0, 1, 2, 2,
            -2, -1, 0, 1, 2,
            -2, -2, -1, 0, 1,
            -4, -2, -2, -1, 0
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Sobel2_90(BufferedImage img) {
        float[] matriz = {
            2, 2, 4, 2, 2,
            1, 1, 2, 1, 1,
            0, 0, 0, 0, 0,
            -1, -1, -2, -1, -1,
            -2, -2, -4, -2, -2
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Sobel2_135(BufferedImage img) {
        float[] matriz = {
            4, 2, 2, 1, 0,
            2, 2, 1, 0, -1,
            2, 1, 0, -1, -2,
            1, 0, -1, -2, -2,
            0, -1, -2, -2, -4
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Sobel2_180(BufferedImage img) {
        float[] matriz = {
            2, 1, 0, -1, -2,
            2, 1, 0, -1, - 2,
            4, 2, 0, -2, -4,
            2, 1, 0, -1, -2,
            2, 1, 0, -1, -2
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Sobel2_225(BufferedImage img) {
        float[] matriz = {
            0, -1, -2, -2, -4,
            1, 0, -1, -2, -2,
            2, 1, 0, -1, -2,
            2, 2, 1, 0, -1,
            4, 2, 2, 1, 0
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Sobel2_270(BufferedImage img) {
        float[] matriz = {
            -2, -2, -4, -2, -2,
            -1, -1, -2, -1, -1,
            0, 0, 0, 0, 0,
            1, 1, 2, 1, 1,
            2, 2, 4, 2, 2
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Sobel2_315(BufferedImage img) {
        float[] matriz = {
            -4, -2, -2, -1, 0,
            -2, -2, -1, 0, 1,
            -2, -1, 0, 1, 2,
            -1, 0, 1, 2, 2,
            0, 1, 2, 2, 4
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Relieve_0(BufferedImage img) {
        float[] matriz = {
            -1, 0, 1,
            -2, 1, 2,
            -1, 0, 1
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Relieve_45(BufferedImage img) {
        float[] matriz = {
            0, 1, 2,
            -1, 1, 1,
            -2, -1, 0
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Relieve_90(BufferedImage img) {
        float[] matriz = {
            1, 2, 1,
            0, 1, 0,
            -1, -2, -1
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Relieve_135(BufferedImage img) {
        float[] matriz = {
            2, 1, 0,
            1, 1, -1,
            0, -1, -2
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Relieve_180(BufferedImage img) {
        float[] matriz = {
            1, 0, -1,
            2, 1, -2,
            1, 0, -1
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Relieve_225(BufferedImage img) {
        float[] matriz = {
            0, -1, -2,
            1, 1, -1,
            2, 1, 0
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Relieve_270(BufferedImage img) {
        float[] matriz = {
            -1, -2, -1,
            0, 1, 0,
            1, 2, 1
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Relieve_315(BufferedImage img) {
        float[] matriz = {
            -2, -1, 0,
            -1, 1, 1,
            0, 1, 2
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 3);
    }

    public static BufferedImage Relieve2_0(BufferedImage img) {
        float[] matriz = {
            -2, -1, 0, 1, 2,
            -2, -1, 0, 1, 2,
            -4, -2, 1, 2, 4,
            -2, -1, 0, 1, 2,
            -2, -1, 0, 1, 2
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Relieve2_45(BufferedImage img) {
        float[] matriz = {
            0, 1, 2, 2, 4,
            -1, 0, 1, 2, 2,
            -2, -1, 1, 1, 2,
            -2, -2, -1, 0, 1,
            -4, -2, -2, -1, 0
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Relieve2_90(BufferedImage img) {
        float[] matriz = {
            2, 2, 4, 2, 2,
            1, 1, 2, 1, 1,
            0, 0, 1, 0, 0,
            -1, -1, -2, -1, -1,
            -2, -2, -4, -2, -2
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Relieve2_135(BufferedImage img) {
        float[] matriz = {
            4, 2, 2, 1, 0,
            2, 2, 1, 0, -1,
            2, 1, 1, -1, -2,
            1, 0, -1, -2, -2,
            0, -1, -2, -2, -4
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Relieve2_180(BufferedImage img) {
        float[] matriz = {
            2, 1, 0, -1, -2,
            2, 1, 0, -1, - 2,
            4, 2, 1, -2, -4,
            2, 1, 0, -1, -2,
            2, 1, 0, -1, -2
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Relieve2_225(BufferedImage img) {
        float[] matriz = {
            0, -1, -2, -2, -4,
            1, 0, -1, -2, -2,
            2, 1, 1, -1, -2,
            2, 2, 1, 0, -1,
            4, 2, 2, 1, 0
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Relieve2_270(BufferedImage img) {
        float[] matriz = {
            -2, -2, -4, -2, -2,
            -1, -1, -2, -1, -1,
            0, 0, 1, 0, 0,
            1, 1, 2, 1, 1,
            2, 2, 4, 2, 2
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage Relieve2_315(BufferedImage img) {
        float[] matriz = {
            -4, -2, -2, -1, 0,
            -2, -2, -1, 0, 1,
            -2, -1, 1, 1, 2,
            -1, 0, 1, 2, 2,
            0, 1, 2, 2, 4
        };
        return MatrizDeConvoluciónRGB(img, matriz, 5, 5);
    }

    public static BufferedImage DetectarBorde_1(BufferedImage img) {
        float[] matriz = {
            -1, 1, 0
        };
        return MatrizDeConvoluciónRGB(img, matriz, 3, 1);
    }

    public static BufferedImage MatrizDeConvoluciónRGB(BufferedImage img, float[] matriz, int w, int h) {
        BufferedImage imgb = Bordear_Extruir(img, w, h);
        BufferedImage rgb = new BufferedImage(imgb.getWidth(), imgb.getHeight(), BufferedImage.TYPE_INT_RGB) {
            {
                Graphics2D g = createGraphics();
                g.drawImage(imgb, 0, 0, null);
            }
        };
        ConvolveOp convolve = new ConvolveOp(new Kernel(w, h, matriz));
        BufferedImage Aplicación;
        try {
            Aplicación = LectoEscrituraArchivos.convertir_tipo_ARGB(convolve.filter(rgb, null));
        } catch (Exception e) {
            return null;
        }
        BufferedImage retorno = new BufferedImage(img.getWidth(), img.getHeight(), 2) {
            {
                Graphics2D g = createGraphics();
                g.drawImage(img, 0, 0, null);
//                g.setComposite(new ModoFusión_Normal().Modificar_AlphaDiscriminador(Constantes_ModoFusión.OP_ALPHA_DENTRO));
                g.drawImage(Aplicación, -w, -h, null);
            }
        };
        return retorno;
    }

    protected static BufferedImage Bordear_Extruir(BufferedImage img, int radio) {
        return Bordear_Extruir(img, radio, radio);
    }

    protected static BufferedImage Bordear_Extruir(BufferedImage img, int radioX, int radioY) {
        BufferedImage Bordeada = new BufferedImage(
                img.getWidth() + 2 * radioX,
                img.getHeight() + 2 * radioY,
                2
        );
        {
            Graphics2D g = Bordeada.createGraphics();
            g.drawImage(img, radioX, radioY, null);
            if (radioX != 0 && radioY != 0) {
                {
                    BufferedImage esquina = img.getSubimage(0, 0, 1, 1);
                    esquina = Filtros_Lineales.Escalar(esquina, radioX, radioY);
                    g.drawImage(esquina, 0, 0, null);
                }
                {
                    BufferedImage esquina = img.getSubimage(img.getWidth() - 1, 0, 1, 1);
                    esquina = Filtros_Lineales.Escalar(esquina, radioX, radioY);
                    g.drawImage(esquina, img.getWidth() - 1 + radioX, 0, null);
                }
                {
                    BufferedImage esquina = img.getSubimage(0, img.getHeight() - 1, 1, 1);
                    esquina = Filtros_Lineales.Escalar(esquina, radioX, radioY);
                    g.drawImage(esquina, 0, img.getHeight() - 1 + radioY, null);
                }
                {
                    BufferedImage esquina = img.getSubimage(img.getWidth() - 1, img.getHeight() - 1, 1, 1);
                    esquina = Filtros_Lineales.Escalar(esquina, radioX, radioY);
                    g.drawImage(esquina, img.getWidth() - 1 + radioX, img.getHeight() - 1 + radioY, null);
                }
            }
            if (radioX != 0) {
                {
                    BufferedImage ladoIzquierda = img.getSubimage(0, 0, 1, img.getHeight());
                    ladoIzquierda = Filtros_Lineales.Escalar(ladoIzquierda, radioX, img.getHeight());
                    g.drawImage(ladoIzquierda, 0, radioY, null);
                }
                {
                    BufferedImage ladoDerecha = img.getSubimage(img.getWidth() - 1, 0, 1, img.getHeight());
                    ladoDerecha = Filtros_Lineales.Escalar(ladoDerecha, radioX, img.getHeight());
                    g.drawImage(ladoDerecha, img.getWidth() - 1 + radioX, radioY, null);
                }
            }
            if (radioY != 0) {
                {
                    BufferedImage ladoArriba = img.getSubimage(0, 0, img.getWidth(), 1);
                    ladoArriba = Filtros_Lineales.Escalar(ladoArriba, img.getWidth(), radioY);
                    g.drawImage(ladoArriba, radioX, 0, null);
                }
                {
                    BufferedImage ladoAbajo = img.getSubimage(0, img.getHeight() - 1, img.getWidth(), 1);
                    ladoAbajo = Filtros_Lineales.Escalar(ladoAbajo, img.getWidth(), radioY);
                    g.drawImage(ladoAbajo, radioX, img.getHeight() - 1 + radioY, null);
                }
            }
        }
        return Bordeada;
    }

}
