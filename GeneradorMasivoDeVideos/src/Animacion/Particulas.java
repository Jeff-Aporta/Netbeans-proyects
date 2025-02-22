package Animacion;

import Degradados.DegradadoRadial;
import Degradados.MapaDegradado;
import HerramientasGUI.VentanaGráfica;
import HerramientasMatemáticas.Dupla;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Particulas extends ObjetoDibujable {

    public static BufferedImage Particulabase;

    public ArrayList<Particula> particulas = new ArrayList<>();

    int anchoMínimoParticula = 10;
    int anchoMáximoParticula = 20;
    public double incrementador_VelocidadNacimientoParticulas = 2;
    double acumulador_NacimientoParticulas = .0;

    public static void main(String[] args) throws Exception {
        Particulas p = new Particulas(Color.PINK);
        p.posición = Escenario.Mitad();
        VentanaGráfica v = new VentanaGráfica("");
        while (true) {
            v.ActualizarFotograma(p.getSkin());
            Thread.sleep((long) (1000 / FPS));
        }
    }

    public Particulas(Color colorParticulas) {
        int lado = 20;
        Particulabase = Dupla.convBufferedImage(lado);
        Dupla centro = new Dupla(lado).Mitad().Proteger();
        DegradadoRadial radial = new DegradadoRadial(
                centro,
                centro.ReemplazarX(lado),
                new MapaDegradado(colorParticulas, new Color(colorParticulas.getRGB() & 0xffffff, true))
        );
        Graphics2D g = Particulabase.createGraphics();
        g.setPaint(radial);
        g.fillRect(0, 0, lado, lado);
    }

    @Override
    public BufferedImage getSkin() {
        BufferedImage fotograma = Escenario.convBufferedImage();
        Graphics2D g = fotograma.createGraphics();

        ArrayList<Particula> particulasMuertas = new ArrayList<>();

        acumulador_NacimientoParticulas += incrementador_VelocidadNacimientoParticulas;
        while (acumulador_NacimientoParticulas >= 1) {
            Particula p = new Particula(posición.Clon());
            p.aceleración.Multiplicar(incrementador_VelocidadNacimientoParticulas);
            particulas.add(p);
            acumulador_NacimientoParticulas--;
        }

        for (int i = 0; i < particulas.size(); i++) {
            Particula particula = particulas.get(i);
            particula.Dibujar(g);
            if (!particula.live) {
                particulasMuertas.add(particula);
            }
        }
        particulas.removeAll(particulasMuertas);
        return fotograma;
    }

    class Particula extends ObjetoDibujable {

        boolean live = true;
        long Ms_Nacimiento;
        float SegundosVida = 4;

        Dupla aceleración;

        public Particula(Dupla posi) {
            this.aceleración = new Dupla(Math.random() * 10, 0).girar(Math.random() * 2 * Math.PI);
            posición = posi;
            double lado = (anchoMáximoParticula - anchoMínimoParticula) * Math.random() + anchoMínimoParticula;
            Escala = new Dupla(lado).Dividir(Particulabase);
            Ms_Nacimiento = System.currentTimeMillis();
        }

        @Override
        public BufferedImage getSkin() {
            posición.Adicionar(aceleración);
            float SegundosTranscurridos = (System.currentTimeMillis() - Ms_Nacimiento) / 1000f;
            if (SegundosTranscurridos >= SegundosVida) {
                live = false;
                return null;
            }
            float porcentaje = SegundosTranscurridos / SegundosVida;
            BufferedImage skin = Dupla.convBufferedImage(Particulabase);
            Graphics2D g = skin.createGraphics();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - porcentaje));
            g.drawImage(Particulabase, 0, 0, null);
            return skin;
        }
    }
}
