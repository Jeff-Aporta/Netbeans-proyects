package Escenarios;

import GUI.GrupoDibujable;
import GUI.Botón;
import static HerramientaDeImagen.Filtros_Lineales.*;
import HerramientaDeImagen.GeneraciónDeTexto;

import HerramientasMatemáticas.Dupla;
import static GUI.Dibujable.*;
import static Escenarios.EscenarioInteractivo.DegradadoVertical;
import GUI.Imagen;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

public class AjusteJuego extends EscenarioInteractivo {
    
    Botón TableroPequeño;
    Botón TableroMediano;
    Botón TableroGrande;
    Imagen Columnas;
    Imagen Filas;
    AjusteNúmerico contadorColumnas = new AjusteNúmerico();
    AjusteNúmerico contadorFilas = new AjusteNúmerico();
    
    public AjusteJuego() {
        double altura = 0;
        Columnas = new Imagen(new GeneraciónDeTexto()
                .ModificarTamañoFuente(50)
                .ColorFuente(Color.WHITE)
                .GenerarTexto("Columnas")
        );
        Filas = new Imagen(new GeneraciónDeTexto()
                .ModificarTamañoFuente(50)
                .ColorFuente(Color.WHITE)
                .GenerarTexto("Filas")
        );
//        altura += Columnas.Dimensión.Alto();
//        contador.CambiarPosición(new Dupla().AdicionarY(altura));
//        altura += contador.Dimensión.Y;
//        contador2.CambiarPosición(new Dupla().AdicionarY(altura));
        ElementosDibujables.add(Columnas,
                contadorColumnas,
                Filas,
                contadorFilas
        ).Organizar();
    }
    
    @Override
    public void Cargar() {
    }
    
    @Override
    public void Deponer() {
    }
    
    @Override
    public void buscarAcciónBotones_Presión(MouseEvent me) {
        ElementosDibujables.EscuchadorMouse(me);
    }
    
    @Override
    public void buscarAcciónBotones_DeslizarScroll(MouseWheelEvent me) {
        ElementosDibujables.EscuchadorMouse(me);
    }
    
    @Override
    public Dupla DimensiónFotograma() {
        return ElementosDibujables.Dimensión();
    }
    
    static class AjusteNúmerico extends ObjetoDibujable {
        
        static final Dupla DimensiónPorDefecto = new Dupla(170, 50).Proteger();
        static final Dupla DimensiónBotones = DimensiónPorDefecto.DividirY(2).MultiplicarX(0.1);
        
        public AjusteNúmerico() {
            Dimensión = DimensiónPorDefecto;
            CambiarPosición(Dupla.ORIGEN);
        }
        
        BufferedImage CalcularImagenNúmero() {
            BufferedImage imagen = DimensiónPorDefecto.SustraerX(DimensiónBotones).convBufferedImage();
            Ajuste_Personalizado(
                    DegradadoVertical(imagen.getHeight(), new Color(0, 0, 0, 0), new Color(1, 1, 1, .07f)),
                    imagen, AJUSTE_EXPANDIR
            );
            Ajuste_Personalizado(
                    new GeneraciónDeTexto().ColorFuente(Color.WHITE).GenerarTexto(número + ""),
                    imagen, AJUSTE_CENTRADO_AJUSTAR
            );
            return imagen;
        }
        
        void ActualizarTableta() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
            Tableta.CambiarImagenes(CalcularImagenNúmero());
        }
        
        Botón Incrementar = new Botón(MapaBotónIcono_Sombra(Icono("+",DimensiónBotones))) {
            @Override
            protected void Acción_Interactivo(MouseEvent me) {
                número++;
                ActualizarTableta();
            }
            
            @Override
            protected void ScrollAcción_Interactivo(MouseWheelEvent me) {
                número -= me.getWheelRotation();
                ActualizarTableta();
            }
        };
        
        Botón decrementar = new Botón(MapaBotónIcono_Sombra(Icono("-",DimensiónBotones))) {
            @Override
            protected void Acción_Interactivo(MouseEvent me) {
                número--;
                ActualizarTableta();
            }
            
            @Override
            protected void ScrollAcción_Interactivo(MouseWheelEvent me) {
                número -= me.getWheelRotation();
                ActualizarTableta();
            }
        };
        
        Botón Tableta = new Botón(CalcularImagenNúmero()) {
            {
                this.INTERACTIVO = false;
            }
            
            @Override
            protected void Acción_Interactivo(MouseEvent me) {
            }
            
            @Override
            protected void ScrollAcción_No_Interactivo(MouseWheelEvent me) {
                número -= me.getWheelRotation();
                ActualizarTableta();
            }
        };
        
        GrupoDibujable Incrementadores = new GrupoDibujable(Incrementar, decrementar)
                .ModificarDistanciaEntreBotones(0);
        
        @Override
        public AjusteNúmerico CambiarPosición(Dupla Nuevaposición) {
            Posición = Nuevaposición.Proteger();
            Tableta.CambiarPosición(Posición.X, Posición.Y);
            Incrementadores.ReposicionarBotones(Posición.Adicionar(Dimensión.Sustraer(DimensiónBotones)).X, Posición.Y);
//            Incrementar.CambiarPosición(posición.Adicionar(dimensión.Sustraer(dimensiónBotón)).X, posición.Y);
//            decrementar.CambiarPosición(
//                    posición.Adicionar(dimensión.Sustraer(dimensiónBotón)).X, 
//                    posición.Adicionar(dimensión.Sustraer(dimensiónBotón)).Y
//            );
            return this;
        }
        
        int número = 0;
        
        @Override
        public void Dibujar(Graphics2D g) {
            Tableta.Dibujar(g);
            Incrementadores.Dibujar(g);
        }
        
        @Override
        public void EscuchadorDeDeslizamientoDeScroll(MouseWheelEvent me) {
            Incrementadores.EscuchadorMouse(me);
            Tableta.EscuchadorMouse(me);
        }
        
        @Override
        public void EscuchadorDePresiónDeBotones(MouseEvent me) {
            Incrementadores.EscuchadorMouse(me);
        }
    }
    
}
