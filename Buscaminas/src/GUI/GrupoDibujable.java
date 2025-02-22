package GUI;

import HerramientasMatemáticas.Dupla;
import static HerramientasMatemáticas.Matemática.Máx;
import GUI.Dibujable.*;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

public class GrupoDibujable extends ArrayList<ObjetoDibujable> implements Dibujable, EscuchadorMouse {

    private int DistanciaEntreBotones = 10;
    Dupla Posición = new Dupla();
    boolean Vertical = true;

    ArrayList<GrupoDibujable> gruposDibujables;

    public GrupoDibujable() {
    }

    public GrupoDibujable(GrupoDibujable... Grupos) {
        gruposDibujables = new ArrayList<>();
        for (GrupoDibujable ObjetosDibujables : Grupos) {
            gruposDibujables.add(ObjetosDibujables);
        }
    }

    public GrupoDibujable(ObjetoDibujable... ObjetosDibujables) {
        add(ObjetosDibujables);
    }

    public int DistanciaEntreBotones() {
        return DistanciaEntreBotones;
    }

    public GrupoDibujable ModificarDistanciaEntreBotones(int distancia) {
        DistanciaEntreBotones = distancia;
        Organizar();
        return this;
    }

    public GrupoDibujable Organizar() {
        ReposicionarObjetosDibujables(Posición);
        return this;
    }

    public GrupoDibujable OrientaciónVertical(boolean OrientaciónVertical) {
        Vertical = OrientaciónVertical;
        return this;
    }

    public Dupla Dimensión() {
        if (Vertical) {
            double ancho = 0;
            double alto = 0;
            for (ObjetoDibujable botón : this) {
                ancho = Máx(ancho, botón.Dimensión.X);
                alto += botón.Dimensión.Y + DistanciaEntreBotones;
            }
            return new Dupla(ancho, alto);
        } else {
            double ancho = 0;
            double alto = 0;
            for (ObjetoDibujable botón : this) {
                ancho += botón.Dimensión.X + DistanciaEntreBotones;
                alto = Máx(botón.Dimensión.Y, alto);
            }
            return new Dupla(ancho, alto);
        }
    }

    public void ReposicionarBotones(double X, double Y) {
        ReposicionarObjetosDibujables(new Dupla(X, Y));
    }

    public void ReposicionarObjetosDibujables(Dupla posición) {
        Posición = posición;
        Dupla Dimensión = Dimensión();
        if (Vertical) {
            int alto = 0;
            for (ObjetoDibujable botón : this) {
                Dupla pos = Dupla.Alinear(
                        Dimensión, botón.Dimensión, Dupla.MEDIO, Dupla.ARRIBA
                );
                botón.CambiarPosición(pos.intX() + posición.intX(), alto + posición.intY());
                alto += botón.Dimensión.Alto() + DistanciaEntreBotones;
            }
        } else {
            int ancho = 0;
            for (ObjetoDibujable botón : this) {
                Dupla pos = Dupla.Alinear(
                        Dimensión, botón.Dimensión, Dupla.IZQUIERDA, Dupla.MEDIO
                );
                botón.CambiarPosición(ancho + posición.intX(), pos.intY() + posición.intY());
                ancho += botón.Dimensión.Ancho() + DistanciaEntreBotones;
            }
        }
    }

    @Override
    public void Dibujar(Graphics2D g) {
        if (gruposDibujables != null) {
            for (GrupoDibujable grupoDibujable : gruposDibujables) {
                grupoDibujable.Dibujar(g);
            }
        }
        for (ObjetoDibujable ObjetoDibujable : this) {
            ObjetoDibujable.Dibujar(g);
        }
    }

    public GrupoDibujable add(GrupoDibujable... grupos) {
        if (gruposDibujables == null) {
            gruposDibujables = new ArrayList<>();
        }
        for (GrupoDibujable grupo : grupos) {
            gruposDibujables.add(grupo);
        }
        return this;
    }

    public GrupoDibujable add(ObjetoDibujable... ObjetosDibujables) {
        for (ObjetoDibujable ObjetoDibujable : ObjetosDibujables) {
            if (ObjetoDibujable != null) {
                add(ObjetoDibujable);
            }
        }
        return this;
    }

    public ObjetoDibujable[] convVector() {
        ObjetoDibujable[] objetos = new ObjetoDibujable[size()];
        for (int i = 0; i < size(); i++) {
            objetos[i] = get(i);
        }
        return objetos;
    }

    @Override
    public void EscuchadorDeDeslizamientoDeScroll(MouseWheelEvent me) {
        if (gruposDibujables != null) {
            for (GrupoDibujable grupoDibujable : gruposDibujables) {
                grupoDibujable.EscuchadorMouse(me);
            }
        }
        for (ObjetoDibujable ObjetoDibujable : this) {
            ObjetoDibujable.EscuchadorMouse(me);
        }
    }

    @Override
    public void EscuchadorDePresiónDeBotones(MouseEvent me) {
        if (gruposDibujables != null) {
            for (GrupoDibujable gruposDibujable : gruposDibujables) {
                gruposDibujable.EscuchadorMouse(me);
            }
        }
        for (ObjetoDibujable Botón : this) {
            Botón.EscuchadorMouse(me);
        }
    }
}
