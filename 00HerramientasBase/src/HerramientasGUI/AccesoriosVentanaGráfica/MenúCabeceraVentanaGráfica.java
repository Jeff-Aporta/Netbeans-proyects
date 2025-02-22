package HerramientasGUI.AccesoriosVentanaGráfica;

import HerramientasGUI.VentanaGráfica;
import static HerramientasGUI.VentanaGráfica.*;
import HerramientasMatemáticas.Dupla;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;

public class MenúCabeceraVentanaGráfica extends JPopupMenu {

    @Override
    public JMenuItem add(JMenuItem jmi) {
        for (String string : EvitarGeneraciónDeOpcionesEnELMenúEspontaneo) {
            if (jmi.getText().equals(string)) {
                return null;
            }
        }
        return super.add(jmi);
    }

    public MenúCabeceraVentanaGráfica(VentanaGráfica ventana) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        {
            MenúItems booleanos = new MenúItems("Booleanos");
            booleanos.AgregarOpcionesBooleanas(
                    new String[]{
                        "Ventana siempre visible",
                    },
                    new boolean[]{
                        ventana.ObtenerSi_EsSiempreVisible(),
                    },
                    new ActionListener[]{
                        (ActionEvent ae) -> {
                            this.setVisible(false);
                            ventana.Alternar_SiempreVisible();
                        }
                    }
            );
            booleanos.addSeparator();
            booleanos.AgregarOpcionesBooleanas(
                    new String[]{"áreas interactivas, redimensión y reposición", "Reposicionar", "Redimensionar"},
                    new boolean[]{ventana.ÁreasInteractivas_ReposiciónYRedimensión, ventana.esReposicionable, ventana.isResizable()},
                    new ActionListener[]{
                        ae -> {
                            setVisible(false);
                            ventana.ÁreasInteractivas_ReposiciónYRedimensión = !ventana.ÁreasInteractivas_ReposiciónYRedimensión;
                        }, ae -> {
                            setVisible(false);
                            ventana.esReposicionable = !ventana.esReposicionable;
                        }, ae -> {
                            setVisible(false);
                            ventana.setResizable(!ventana.isResizable());
                        }
                    }
            );
            booleanos.addSeparator();
            booleanos.AgregarOpcionesBooleanas(
                    new String[]{"Conservar píxeles al escalar fotograma", "Esquinas redondeadas"},
                    new boolean[]{ventana.ConservarPíxelesFotograma(), ventana.RedondearEsquinas},
                    new ActionListener[]{
                        ae -> {
                            setVisible(false);
                            ventana.ConservarPíxelesFotograma(ventana.ConservarPíxelesFotograma());
                            ventana.ActualizarFotograma();
                        }, ae -> {
                            setVisible(false);
                            ventana.RedondearEsquinas = !ventana.RedondearEsquinas;
                            ventana.setSize(ventana.getSize());
                        }
                    }
            );
            add(booleanos);
        }
        {
            MenúItems OpcionesMultiples = new MenúItems("Cambiar Dimension",
                    new String[]{"Muy Pequeña", "Pequeña", "Mediana", "Grande", "Pantalla Completa", "Fotograma"},
                    new ActionListener[]{
                        (ActionEvent ae) -> {
                            ventana.CambiarTamaño(DIMENSIÓN_MUY_PEQUEÑA, ventana.ObtenerSi_EstáCentradaEnPantalla());
                        }, (ActionEvent ae) -> {
                            ventana.CambiarTamaño(DIMENSIÓN_PEQUEÑA, ventana.ObtenerSi_EstáCentradaEnPantalla());
                        }, (ActionEvent ae) -> {
                            ventana.CambiarTamaño(DIMENSIÓN_MEDIANA, ventana.ObtenerSi_EstáCentradaEnPantalla());
                        }, (ActionEvent ae) -> {
                            ventana.CambiarTamaño(DIMENSIÓN_GRANDE, ventana.ObtenerSi_EstáCentradaEnPantalla());
                        }, (ActionEvent ae) -> {
                            ventana.CambiarTamaño(DIMENSIÓN_PANTALLA_COMPLETA, ventana.ObtenerSi_EstáCentradaEnPantalla());
                        }, (ActionEvent ae) -> {
                            ventana.CambiarTamaño(DIMENSIÓN_FOTOGRAMA, ventana.ObtenerSi_EstáCentradaEnPantalla());
                        }
                    }
            );
            OpcionesMultiples.addSeparator();
            OpcionesMultiples.AgregarOpciones(new String[]{"Centrar"},
                    new ActionListener[]{
                        (ActionEvent ae) -> {
                            ventana.CentrarEnPantalla();
                        }
                    });
            add(OpcionesMultiples);
        }
        {
            MenúItems OpcionesMultiples = new MenúItems("Modo Fondo",
                    new String[]{"Normal", "Transparente suave", "Transparente", "Invisible"},
                    new ActionListener[]{
                        (ActionEvent ae) -> {
                            ventana.CambiarModoFondo(FONDO_NORMAL);
                        }, (ActionEvent ae) -> {
                            ventana.CambiarModoFondo(FONDO_TRANSPARENTE_SUAVE);
                        }, (ActionEvent ae) -> {
                            ventana.CambiarModoFondo(FONDO_TRANSPARENTE);
                        }, (ActionEvent ae) -> {
                            ventana.CambiarModoFondo(FONDO_INVISIBLE);
                        }
                    }
            );
            add(OpcionesMultiples);
        }
        {
            ActionListener a = ae -> {
            };
            MenúItems OpcionesMultiples = new MenúItems("Información",
                    new String[]{
                        "Posición: (" + ventana.getX() + "," + ventana.getY() + ")",
                        "Dimensión: (" + ventana.getWidth() + "," + ventana.getHeight() + ")"
                    }, new ActionListener[]{a, a}
            );
            add(OpcionesMultiples);
        }
        addSeparator();
        {
            Opción opción = new Opción("No mostrar este menú");
            opción.addActionListener((ActionEvent ae) -> {
                ventana.MostrarPopMenúEncabezado = false;
            });
            add(opción);
        }
        {
            Opción opción = new Opción("Cerrar aplicación");
            opción.addActionListener((ActionEvent ae) -> {
                ventana.SalirAplicación();
            });
            add(opción);
        }
    }

    class Opción extends JMenuItem {

        public Opción(String texto) {
            setText(texto);
        }
    }

    public Component add(OpciónBoleana bool) {
        for (String string : EvitarGeneraciónDeOpcionesEnELMenúEspontaneo) {
            if (bool.getText().equals(string)) {
                return null;
            }
        }
        return super.add(bool);
    }

    class OpciónBoleana extends JRadioButton {

        public OpciónBoleana(String texto) {
            setText(texto);
        }
    }

    final class MenúItems extends JMenu {

        public MenúItems(String texto) {
            super(texto);
        }

        public MenúItems(String Título, String[] opciones, ActionListener[] acciones) {
            this(Título);
            AgregarOpciones(opciones, acciones);
        }

        public void AgregarOpciones(String[] opciones, ActionListener[] acciones) {
            for (int i = 0; i < acciones.length; i++) {
                Opción opción = new Opción(opciones[i]) {
                    {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                };
                opción.addActionListener(acciones[i]);
                add(opción);
            }
        }

        public void AgregarOpcionesBooleanas(String[] opciones, boolean[] val, ActionListener[] acciones) {
            for (int i = 0; i < acciones.length; i++) {
                OpciónBoleana opción = new OpciónBoleana(opciones[i]) {
                    {
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    }
                };
                opción.setSelected(val[i]);
                opción.addActionListener(acciones[i]);
                add(opción);
            }
        }

        public Component add(OpciónBoleana bool) {
            for (String string : EvitarGeneraciónDeOpcionesEnELMenúEspontaneo) {
                if (bool.getText().equals(string)) {
                    return null;
                }
            }
            return super.add(bool);
        }

        @Override
        public JMenuItem add(JMenuItem jmi) {
            for (String string : EvitarGeneraciónDeOpcionesEnELMenúEspontaneo) {
                if (jmi.getText().equals(string)) {
                    return null;
                }
            }
            return super.add(jmi);
        }
    }
}
