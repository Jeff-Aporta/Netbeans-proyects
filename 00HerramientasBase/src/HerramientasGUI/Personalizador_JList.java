package HerramientasGUI;

import HerramientasMatemáticas.Dupla;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

public class Personalizador_JList extends JPanel implements ListCellRenderer {

    protected Presentador label;
    boolean separador = false;

    public static void main(String args[]) {
        new JFrame() {
            {
                //<editor-fold defaultstate="collapsed" desc="Inicialización de la ventana »">
                String[] strs = {
                    "/-/Separador 1", "home", "basic", "metal", "JList",
                    "/-/", "home", "basic", "metal", "JList",
                    "/-/", "home", "basic", "metal", "JList"
                };

                final JList list = new JList(strs);

                list.setCellRenderer(new Personalizador_JList());

                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                JScrollPane sp = new JScrollPane(list);

                final JTextArea textArea = new JTextArea(4, 10);
                JScrollPane textPanel = new JScrollPane(textArea);

                JButton printButton = new JButton("Imprimir");
                printButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String item = list.getSelectedValue() + "";
                        textArea.setText(item.toString());
                    }
                });

                JPanel panel = new JPanel(new GridLayout(2, 1));
                panel.add(printButton);

                getContentPane().add(sp, BorderLayout.CENTER);
                getContentPane().add(panel, BorderLayout.EAST);
                getContentPane().add(textPanel, BorderLayout.SOUTH);

                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setSize(900, 600);
                setVisible(true);
                setLocationRelativeTo(null);
                //</editor-fold>
            }
        };
    }

    public Personalizador_JList() {
        setLayout(null);
        label = new Presentador();
        add(label);
    }

    @Override
    public void paint(Graphics grphcs) {
        label.ActualizarFotograma();
        super.paint(grphcs);
    }

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index, boolean isSelected, boolean hasFocus
    ) {
        //<editor-fold defaultstate="collapsed" desc="Código del interpretador »">
        label.setFont(list.getFont());
        String s = value.toString().replace("/t/", "    ");
        if (s.contains("/-/")) {
            if (s.contains("/cb/")) {
                label.setForeground(new Color(0x99cc));
            } else if (s.contains("/cg/")) {
                label.setForeground(new Color(0xbb00));
            } else {
                label.setForeground(Color.LIGHT_GRAY);
            }
            String k = s
                    .replace("/-/", "")
                    .replace("/l/", "")
                    .replace("/t/", "")
                    .replace("/cg/", "")
                    .replace("/cb/", "");
            if (k.isEmpty()) {
                k += " ";
            }
            label.setText(k);
            if (s.contains("/l/")) {
                label.setHorizontalAlignment(JLabel.LEFT);
            } else {
                label.setHorizontalAlignment(JLabel.CENTER);
            }
        } else {
            if (isSelected) {
                label.setForeground(Selección_ColorFuente);
            } else {
                label.setForeground(NoSelección_ColorFuente);
            }
            label.setText(s);
            label.setHorizontalAlignment(JLabel.LEFT);
        }
        setEnabled(list.isEnabled());
        if (isSelected) {
            if (s.contains("/-/")) {
                setBackground(NoSelección_ColorFondo);
            } else {
                setBackground(Selección_ColorFondo);
            }
        } else {
            setBackground(NoSelección_ColorFondo);
        }
        //</editor-fold>
        return this;
    }

    public Color Selección_ColorFondo = new Color(0xccffff);
    public Color NoSelección_ColorFondo = Color.WHITE;

    public Color Selección_ColorFuente = Color.BLACK;
    public Color NoSelección_ColorFuente = Color.BLACK;

    @Override
    public Dimension getPreferredSize() {
        return new Dupla(label.getPreferredSize()).convDimension();
    }

    @Override
    public void doLayout() {
        label.setSize(getWidth(), getHeight());
    }
}
