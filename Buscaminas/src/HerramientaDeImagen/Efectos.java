package HerramientaDeImagen;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;

public class Efectos {

    public final static byte //
            BORDE_FUERA = 0,
            BORDE_DENTRO = 1,
            BORDE_CENTRO = 2,
            BORDE_DEBORADOR = 3,
            BORDE_LIMPIADOR = 4;

    public static void DibujarBorde(Graphics2D g, Shape shape, Color color, float Grosor, byte Estilo_Borde, boolean SuavizarDibujado) {
        g.setStroke(new BasicStroke(Grosor));
        g.setColor(color);
        switch (Estilo_Borde) {
            case BORDE_FUERA:
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP));
                break;
            case BORDE_CENTRO:
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                break;
            case BORDE_DENTRO:
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP));
                break;
            case BORDE_DEBORADOR:
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OUT));
                break;
            case BORDE_LIMPIADOR:
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
                break;
        }
        if (SuavizarDibujado) {
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
        }
        g.draw(shape);
    }

    public final static ExtractorShape_Imágen MétodoDeCasillas = new ExtractorShape_Imágen() {
        @Override
        public Area Extraer(BufferedImage imagen, Rectangle2D r2d, Shape shapeExclusor, int ToleranciaMínα, int ToleranciaMáxα) {
            Area area = new Area();
            int PosX = (int) r2d.getX(), PosY = (int) r2d.getY();
            double anchoÁrea = r2d.getX() + r2d.getWidth(), altoÁrea = r2d.getY() + r2d.getHeight();
            boolean[][] MapaDeTolerancia = GenerarMapaTolerancia(
                    imagen, r2d, shapeExclusor, ToleranciaMínα, ToleranciaMáxα
            );
            for (int c = PosX, f = PosY; c < anchoÁrea; c += f == altoÁrea - 1 ? 1 : 0, f = (int) (++f % altoÁrea)) {
                if (MapaDeTolerancia[c][f]) {
                    area.add(new Area(new Rectangle(c, f, 1, 1)));
                }
            }
            return area;
        }
    };

    public static ExtractorShape_Imágen MétodoDeColumnas = new ExtractorShape_Imágen() {
        @Override
        public Area Extraer(BufferedImage imagen, Rectangle2D r2d, Shape shapeExclusor, int ToleranciaMínα, int ToleranciaMáxα) {
            Area area = new Area();
            int h = 0, y = 0;
            int ancho = imagen.getWidth(), alto = imagen.getHeight(), LimF = alto - 1;
            boolean[][] MapaDeTolerancia = GenerarMapaTolerancia(
                    imagen, r2d, shapeExclusor, ToleranciaMínα, ToleranciaMáxα
            );
            for (int c = 0, f = 0; c < ancho; c += f == LimF ? 1 : 0, f = ++f % alto) {
                if (MapaDeTolerancia[c][f] && f < LimF) {
                    h++;
                } else {
                    if (h > 0) {
                        area.add(new Area(new Rectangle(c, y, 1, h)));
                        h = 0;
                    }
                    y = f;
                }
            }
            return area;
        }
    };
    public static ExtractorShape_Imágen MétodoDeFilas = new ExtractorShape_Imágen() {
        @Override
        public Area Extraer(BufferedImage imagen, Rectangle2D r2d, Shape shapeExclusor, int ToleranciaMínα, int ToleranciaMáxα) {
            Area Área = new Area();
            int ancho = imagen.getWidth(), alto = imagen.getHeight();
            boolean[][] MapaDeTolerancia = GenerarMapaTolerancia(
                    imagen, r2d, shapeExclusor, ToleranciaMínα, ToleranciaMáxα
            );
            for (int f = 0; f < alto; f++) {
                int c = 0;
                while (c < ancho) {
                    int w = 0;
                    final int x = c;
                    while (MapaDeTolerancia[c++][f] && c < ancho) {
                        w++;
                    }
                    if (w > 0) {
                        Área.add(new Area(new Rectangle(x, f, w, 1)));
                    }
                }
            }
            return Área;
        }
    };
    public static ExtractorShape_Imágen MétodoDeDiscriminación_CF = new ExtractorShape_Imágen() {
        @Override
        public Area Extraer(BufferedImage imagen, Rectangle2D r2d, Shape shapeExclusor, int ToleranciaMínα, int ToleranciaMáxα) {
            if (imagen.getWidth() > imagen.getHeight()) {
                return MétodoDeFilas.Extraer(imagen, r2d, shapeExclusor, ToleranciaMínα, ToleranciaMáxα);
            } else {
                return MétodoDeColumnas.Extraer(imagen, r2d, shapeExclusor, ToleranciaMínα, ToleranciaMáxα);
            }
        }
    };
    public static ExtractorShape_Imágen MétodoDeBloquesHorizontales = new ExtractorShape_Imágen() {
        @Override
        public Area Extraer(BufferedImage imagen, Rectangle2D r2d, Shape shapeExclusor, int ToleranciaMínα, int ToleranciaMáxα) {
            Area Área = new Area();

            int anchoImagen = imagen.getWidth(), altoImagen = imagen.getHeight();
            int PosXiÁrea = (int) r2d.getX(), PosYiÁrea = (int) r2d.getY();
            int AnchoÁrea = (int) r2d.getWidth(), altoÁrea = (int) r2d.getHeight();

            boolean[][] MapaDeTolerancia = GenerarMapaTolerancia(
                    imagen, r2d, shapeExclusor, ToleranciaMínα, ToleranciaMáxα
            );
            for (int c = 0; c < AnchoÁrea && c + PosXiÁrea < anchoImagen; c++) {
                for (int f = 0; f < altoÁrea && f + PosYiÁrea < altoImagen; f++) {
                    int y = f;
                    int w = Integer.MAX_VALUE, h = 0;
                    if (MapaDeTolerancia[c][f]) {
                        while (MapaDeTolerancia[c][f++] && f < altoÁrea) {
                            h++;
                        }
                        if (h > 0) {
                            for (int f_b = y; f_b < y + h; f_b++) {
                                int wTemp = 0;
                                {
                                    int xTemp = c;
                                    while (MapaDeTolerancia[xTemp++][f_b] && xTemp < AnchoÁrea && wTemp < w) {
                                        wTemp++;
                                    }
                                }
                                w = w < wTemp ? w : wTemp;
                                if (w == 1) {
                                    break;
                                }
                            }
                            LimpiarÁreaMapaTolerancia(MapaDeTolerancia, c, y, w, h);
                            Área.add(new Area(new Rectangle(c + PosXiÁrea, y + PosYiÁrea, w, h)));
                        }
                    }
                }
            }
            return Área;
        }
    };

    public interface ExtractorShape_Imágen {

        Area Extraer(
                BufferedImage imagen,
                Rectangle2D áreaEvaluación,
                Shape shapeExclusor,
                int Toleranciaα,
                int ToleranciaMáxα
        );

        default Area Extraer(BufferedImage imagen, Shape shapeExclusor) {
            return Extraer(
                    imagen,
                    new Rectangle.Double(0, 0, imagen.getWidth(), imagen.getHeight()),
                    shapeExclusor, 0, 256
            );
        }

        default Area Extraer(BufferedImage imagen, Rectangle2D r2d, Shape shapeExclusor) {
            return Extraer(imagen, r2d, shapeExclusor, 0, 256);
        }

        default Area Extraer(BufferedImage imagen, Rectangle2D r2d, int ToleranciaMínα, int ToleranciaMáxα) {
            return Extraer(imagen, r2d, null, ToleranciaMínα, ToleranciaMáxα);
        }

        default Area Extraer(BufferedImage imagen, Rectangle2D r2d, int ToleranciaMínα) {
            return Extraer(imagen, r2d, ToleranciaMínα, 256);
        }

        default Area Extraer(BufferedImage imagen, Rectangle2D r2d) {
            return Extraer(imagen, r2d, 0);
        }

        default Area Extraer(BufferedImage imagen) {
            return Extraer(
                    imagen,
                    new Rectangle2D.Double(0, 0, imagen.getWidth(), imagen.getHeight()),
                    0
            );
        }

        default Area Extraer(BufferedImage imagen, int ToleranciaMínα, int ToleranciaMáxα) {
            return Extraer(
                    imagen,
                    new Rectangle2D.Double(0, 0, imagen.getWidth(), imagen.getHeight()),
                    ToleranciaMínα,
                    ToleranciaMáxα
            );
        }

        default Area Extraer(BufferedImage imagen, int ToleranciaMínα) {
            return Extraer(
                    imagen,
                    new Rectangle2D.Double(0, 0, imagen.getWidth(), imagen.getHeight()),
                    ToleranciaMínα,
                    256
            );
        }

        default boolean DeterminarPertenencia(BufferedImage imagen, int i, int j, Rectangle2D r, Shape s, int ToleranciaMínα, int ToleranciaMáxα) {
            int PosXiÁrea = (int) r.getX(), PosYiÁrea = (int) r.getY();
            int α = imagen.getRGB(i + PosXiÁrea, j + PosYiÁrea) >>> 24;
            boolean Pertenencia = α > ToleranciaMínα;
            if (Pertenencia) {
                Pertenencia &= α < ToleranciaMáxα;
                if (Pertenencia) {
                    if (s != null) {
                        Pertenencia &= !s.contains(i + PosXiÁrea, j + PosYiÁrea);
                    }
                }
            }
            return Pertenencia;
        }

        default boolean[][] GenerarMapaTolerancia(BufferedImage imagen, Rectangle2D r, Shape shapeExclusor, int ToleranciaMínα, int ToleranciaMáxα) {
            int AnchoÁrea = (int) r.getWidth(), altoÁrea = (int) r.getHeight();
            boolean[][] MapaDeTolerancia = new boolean[AnchoÁrea][altoÁrea];
            for (int i = 0, j = 0; i < AnchoÁrea; i += j == altoÁrea - 1 ? 1 : 0, j = ++j % altoÁrea) {
                try {
                    MapaDeTolerancia[i][j] = DeterminarPertenencia(
                            imagen, i, j, r, shapeExclusor, ToleranciaMínα, ToleranciaMáxα
                    );
                } catch (Exception e) {
                }
            }
            return MapaDeTolerancia;
        }

        default void LimpiarÁreaMapaTolerancia(boolean[][] MapaDeTolerancia, int c, int f, int w, int h) {
            for (int i = c; i < c + w; i++) {
                for (int j = f; j < f + h; j++) {
                    MapaDeTolerancia[i][j] = false;
                }
            }
        }

        default Area ExtraerConHilos(BufferedImage imagen, int Columnas, int Filas) {
            return ExtraerConHilos(imagen, Columnas, Filas, 0, 256);
        }

        default Area ExtraerConHilos_Columnas(BufferedImage imagen, int Columnas) {
            return ExtraerConHilos(imagen, Columnas, 1, 0, 256);
        }

        default Area ExtraerConHilos_Fila(BufferedImage imagen, int Filas) {
            return ExtraerConHilos(imagen, 1, Filas, 0, 256);
        }

        default Area ExtraerConHilos_Columnas(BufferedImage imagen, int Columnas, int ToleranciaMínα, int ToleranciaMáxα) {
            return ExtraerConHilos(imagen, Columnas, 1, ToleranciaMínα, ToleranciaMáxα);
        }

        default Area ExtraerConHilos_Columnas(BufferedImage imagen, int Columnas, int Tolerancia) {
            return ExtraerConHilos(imagen, Columnas, 1, Tolerancia, 256);
        }

        default Area ExtraerConHilos_Fila(BufferedImage imagen, int Filas, int ToleranciaMínα, int ToleranciaMáxα) {
            return ExtraerConHilos(imagen, 1, Filas, ToleranciaMínα, ToleranciaMáxα);
        }

        default Area ExtraerConHilos_Fila(BufferedImage imagen, int Filas, int ToleranciaMínα) {
            return ExtraerConHilos(imagen, 1, Filas, ToleranciaMínα, 256);
        }

        default Area ExtraerConHilos(BufferedImage imagen, int Columnas, int Filas, int ToleranciaMínα, int ToleranciaMáxα) {

            final CeldaDeProcesamiento[][] celdas = new CeldaDeProcesamiento[Columnas][Filas];
            final int ancho = imagen.getWidth() / Columnas;
            final int alto = imagen.getHeight() / Filas;

            for (int i = 0; i < Columnas; i++) {
                for (int j = 0; j < Filas; j++) {
                    celdas[i][j] = new CeldaDeProcesamiento(
                            imagen, new Rectangle(i * ancho, j * alto, ancho + 1, alto + 1), ToleranciaMínα, ToleranciaMáxα, this
                    );
                }
            }

            while (true) {
                boolean Esperar = false;
                for (CeldaDeProcesamiento[] columna : celdas) {
                    for (CeldaDeProcesamiento celda : columna) {
                        Esperar = Esperar || celda.procesando;
                    }
                }
                if (!Esperar) {
                    break;
                }
            }

            Area área = new Area();
            for (CeldaDeProcesamiento[] columna : celdas) {
                for (CeldaDeProcesamiento celda : columna) {
                    área.add(celda.retorno);
                }
            }
            return área;
        }

        class CeldaDeProcesamiento {

            final BufferedImage imagen;
            final Rectangle Área;
            final int ToleranciaMínα;
            final int ToleranciaMáxα;
            final ExtractorShape_Imágen Extractor;

            Area retorno;
            boolean procesando = true;

            public CeldaDeProcesamiento(BufferedImage imagen, Rectangle área, int ToleranciaMínα, int ToleranciaMáxα, ExtractorShape_Imágen extractor) {
                this.imagen = imagen;
                this.Área = área;
                this.ToleranciaMínα = ToleranciaMínα;
                this.ToleranciaMáxα = ToleranciaMínα;
                Extractor = extractor;
                new Thread(
                        () -> {
                            retorno = Extractor.Extraer(imagen, Área, ToleranciaMínα, ToleranciaMáxα);
                            procesando = false;
                        }
                ).start();
            }

        }
    }

}
