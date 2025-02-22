package _Laboratorio;

import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientasMatemáticas.Matemática;
import java.util.Calendar;

public class hora_online extends javax.swing.JFrame {

    static String[] months = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};

    public static void main(String args[]) throws Exception {
        hora_online h = new hora_online();
        h.setVisible(true);
        while (true) {
            h.actualizar();
            Thread.sleep(333);
        }
    }

    public hora_online() {
        initComponents();
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    void actualizar() {
        Calendar c = fecha_online();
        String html = "<html>\n"
                + "<body align=\"center\">\n"
                + "<div style=\"font-size: 50px;\">"
                + Matemática.CerosIzquierda(c.get(Calendar.HOUR_OF_DAY), 2) + ":"
                + Matemática.CerosIzquierda(c.get(Calendar.MINUTE), 2) + ":"
                + Matemática.CerosIzquierda(c.get(Calendar.SECOND), 2) + "</div>\n"
                + "<div style=\"font-size: 20px;\">" 
                + Matemática.CerosIzquierda(c.get(Calendar.DAY_OF_MONTH), 2) + "/" 
                + Matemática.CerosIzquierda((c.get(Calendar.MONTH) + 1), 2) + "/" 
                + c.get(Calendar.YEAR) + "</div>\n"
                + "</body>\n"
                + "</html>";
        jLabel1.setText(html);
    }

    public static Calendar fecha_online() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            String[] s = LectoEscrituraArchivos.EliminarCódigoHTML_TextoPlano(
                    LectoEscrituraArchivos.LeerArchivo_ASCII("https://time.is/es/Colombia")
            ).split("\n");
            for (String string : s) {
                if (string.contains("La hora actual en Colombia") && !string.contains("Time.is")) {
                    String t = string
                            .replace("La hora actual en Colombia", "")
                            .replace("El reloj no está avanzando porque no tienes JavaScript o se encuentra deshabilitado por el navegador.", "\t")
                            .replace(", semana", "\t");
                    String[] u = t.split("\t");
                    String[] hhmmss = u[0].trim().split(":");
                    int hora = 0;
                    try {
                        hora = Integer.parseInt(hhmmss[0]);
                    } catch (Exception e) {
                        hora = Integer.parseInt(hhmmss[0].substring(1));
                    }
                    int minuto = 0;
                    try {
                        minuto = Integer.parseInt(hhmmss[1]);
                    } catch (Exception e) {
                        minuto = Integer.parseInt(hhmmss[1].substring(1));
                    }
                    int segundo = 0;
                    try {
                        segundo = Integer.parseInt(hhmmss[2]);
                    } catch (Exception e) {
                        segundo = Integer.parseInt(hhmmss[2].substring(1));
                    }
                    String[] fecha = u[1].split(",");
                    String diames = fecha[1];
                    int dia = 0;
                    int mes = 0;
                    for (String month : months) {
                        if (diames.contains(month)) {
                            dia = Integer.parseInt(diames.replace(month, "").trim());
                            break;
                        }
                        mes++;
                    }
                    int año = Integer.parseInt(fecha[2].trim());
                    Calendar c = Calendar.getInstance();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hora);
                    calendar.set(Calendar.MINUTE, minuto);
                    calendar.set(Calendar.SECOND, segundo);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, dia);
                    calendar.set(Calendar.MONTH, mes);
                    calendar.set(Calendar.YEAR, año);
                    return calendar;
                }
            }
        } catch (Exception ex) {
        }
        return Calendar.getInstance();
    }//</editor-fold>

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setFont(new java.awt.Font("sansserif", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("<html>\n<body align=\"center\">\n<div style=\"font-size: 50px;\">00:00:00</div>\n<div style=\"font-size: 20px;\">3/3/2020</div>\n</body>\n</html>");
        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
