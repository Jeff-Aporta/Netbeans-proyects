package Degradados;

import static Degradados.AjusteRango.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

public class MapaDegradado {

    private ArrayList<NodoColor> NodosColores = new ArrayList<>();

    public MapaDegradado(Color[] colores, double... porcentajesAparición) {
        this(colores, convFloat(porcentajesAparición));
    }

    private static float[] convFloat(double... porcentajesAparición) {
        float[] porcentajes = new float[porcentajesAparición.length];
        for (int i = 0; i < porcentajes.length; i++) {
            porcentajes[i] = (float) porcentajesAparición[i];
        }
        return porcentajes;
    }

    public MapaDegradado(Color... colores) {
        this(colores, EquidistarPorcentajes(colores.length));
    }

    private static float[] EquidistarPorcentajes(int cantidadColores) {
        float[] PorcentajesColor = new float[cantidadColores];
        for (int i = 0; i < cantidadColores; i++) {
            PorcentajesColor[i] = (float) i / (cantidadColores - 1);
        }
        return PorcentajesColor;
    }

    public MapaDegradado(Color[] colores, float... porcentajesAparición) {
        for (float porcentaje : porcentajesAparición) {
            if (porcentaje > 1 || porcentaje < 0) {
                throw new RuntimeException("Los porcentajes deben estar en un rango entre 0 y 1 (0% y 100%)");
            }
        }
        if (colores.length != porcentajesAparición.length) {
            throw new RuntimeException(
                    "La cantidad de colores y la cantidad de porcentajes no tienen la misma cantidad de elementos"
            );
        }
        if (colores == null || porcentajesAparición == null) {
            throw new RuntimeException("El vector de los colores o los porcentajes no pueden ser nulos");
        }
        for (int i = 0; i < colores.length; i++) {
            NodosColores.add(
                    new NodoColor(colores[i], porcentajesAparición[i])
            );
        }
        Collections.sort(NodosColores);
        {
            if (NodosColores.get(0).porcentaje != 0) {
                NodosColores.add(
                        0,
                        new NodoColor(NodosColores.get(0).color(), 0)
                );
            }
            int últimaPosición = NodosColores.size() - 1;
            if (NodosColores.get(últimaPosición).porcentaje != 1) {
                NodosColores.add(
                        últimaPosición,
                        new NodoColor(NodosColores.get(últimaPosición).color(), 1)
                );
            }
        }
    }

    public float[] CalcularComponentesColor(double porcentaje, AjusteRango ajuste) {
        return (float[]) CalcularCoordenadaColor(porcentaje, ajuste, false);
    }

    public Color CalcularColor(double porcentaje, AjusteRango ajuste) {
        return (Color) CalcularCoordenadaColor(porcentaje, ajuste, true);
    }

    public Object CalcularCoordenadaColor(double porcentaje, AjusteRango ajuste, boolean Color) {
        porcentaje = AjustarPorcentaje(porcentaje, ajuste);
        int últimaPosición = NodosColores.size() - 1;
        for (int i = 0; i < últimaPosición; i++) {
            int k = i + 1;
            NodoColor ColorPartida = NodosColores.get(i);
            NodoColor ColorLlegada = NodosColores.get(k);
            boolean DespuésDe_i = porcentaje >= ColorPartida.porcentaje;
            boolean AntesDe_k = porcentaje <= ColorLlegada.porcentaje;
            if (AntesDe_k && DespuésDe_i) {
                double V = porcentaje;
                double Vi = ColorPartida.porcentaje;
                double Vf = ColorLlegada.porcentaje;
                porcentaje = (V - Vi) / (Vf - Vi);
                float t = (float) porcentaje;
                if (Color) {
                    return new Color(
                            (ColorLlegada.Rojo - ColorPartida.Rojo) * t + ColorPartida.Rojo,
                            (ColorLlegada.Verde - ColorPartida.Verde) * t + ColorPartida.Verde,
                            (ColorLlegada.Azul - ColorPartida.Azul) * t + ColorPartida.Azul,
                            (ColorLlegada.Alpha - ColorPartida.Alpha) * t + ColorPartida.Alpha
                    );
                } else {
                    return new float[]{
                        (ColorLlegada.Rojo - ColorPartida.Rojo) * t + ColorPartida.Rojo,
                        (ColorLlegada.Verde - ColorPartida.Verde) * t + ColorPartida.Verde,
                        (ColorLlegada.Azul - ColorPartida.Azul) * t + ColorPartida.Azul,
                        (ColorLlegada.Alpha - ColorPartida.Alpha) * t + ColorPartida.Alpha
                    };
                }
            }
        }
        return CalcularCoordenadaColor(0, ajuste, Color);
    }

    public class NodoColor implements Comparable<NodoColor> {

        float Alpha, Rojo, Verde, Azul;
        double porcentaje;

        public Color color() {
            return new Color(Rojo, Verde, Azul, Alpha);
        }

        public NodoColor(Color color, double porcentaje) {
            Alpha = color.getAlpha()/255f;
            Rojo = color.getRed()/255f;
            Verde = color.getGreen()/255f;
            Azul = color.getBlue()/255f;
            this.porcentaje = porcentaje;
        }

        @Override
        public int compareTo(NodoColor t) {
            return new Double(porcentaje).compareTo(t.porcentaje);
        }
    }

}
