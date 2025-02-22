package HerramientaDeImagen.ModoFusión;

//<editor-fold defaultstate="collapsed" desc="Importe de librerias ●">
import HerramientaArchivos.ArrastrarArchivos.ArrastrarArchivos;
import HerramientaArchivos.BibliotecaArchivos;
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientaDeImagen.Filtros_Lineales;
import static HerramientaDeImagen.ModoFusión.GUI_PruebaModoPintura.ConsultarModoPintura;
import static HerramientaDeImagen.ModoFusión.GUI_PruebaModoPintura.*;
import HerramientasGUI.Maquillaje_SwingGUI;
import HerramientasGUI.Presentador;
import HerramientasGUI.Personalizador_JList;
import HerramientasMatemáticas.Dupla;
import HerramientasMatemáticas.Matemática;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
//</editor-fold>

public class GUI_PruebaModoFusión extends javax.swing.JFrame {

    //<editor-fold defaultstate="collapsed" desc="Variables ●">
    Presentador PresentadorEscenario;
    Presentador PresentadorPatrónFondo;
    Presentador PresentadorCapaSuperior;
    Presentador PresentadorCapaInferior;
    Fotograma Escenario;
    BaseModoFusión modoFusión = ObtenerModoFusión(MODOFUSIÓN_C_NORMAL);
    //</editor-fold>

    public static final byte MODOFUSIÓN_C_NORMAL = -128;
    public final static byte MODOFUSIÓN_C_MAX = -41;
    public final static byte MODOFUSIÓN_C_MIN = -40;
    public final static byte MODOFUSIÓN_C_E_DISOLVER = -39;
    public final static byte MODOFUSIÓN_C_DISOLVER_RUIDO = -37;

    //<editor-fold defaultstate="collapsed" desc="Modos de fusión por Combinación Lineal ●">
    public final static byte MODOFUSIÓN_CL_GENERAL = -95;

    //<editor-fold defaultstate="collapsed" desc="Modos de fusión por Combinación Lineal Aditivos ●">
    public final static byte MODOFUSIÓN_CL_ADITIVO = -127;
    public final static byte MODOFUSIÓN_CL_ADITIVO_LUZROJA = -73;
    public final static byte MODOFUSIÓN_CL_ADITIVO_LUZVERDE = -70;
    public final static byte MODOFUSIÓN_CL_ADITIVO_LUZAZUL = -65;
    public final static byte MODOFUSIÓN_CL_ADITIVO_LUZAMARILLA = -64;
    public final static byte MODOFUSIÓN_CL_ADITIVO_LUZMAGENTA = -63;
    public final static byte MODOFUSIÓN_CL_ADITIVO_LUZCYAN = -62;
    public final static byte MODOFUSIÓN_CL_ADITIVO_GRIS_LUZ = -61;
    public final static byte MODOFUSIÓN_CL_ADITIVO_GRIS_LUZROJO = -60;
    public final static byte MODOFUSIÓN_CL_ADITIVO_GRIS_LUZVERDE = -59;
    public final static byte MODOFUSIÓN_CL_ADITIVO_GRIS_LUZAZUL = -58;
    public final static byte MODOFUSIÓN_CL_ADITIVO_GRIS_LUZAMARILLO = -57;
    public final static byte MODOFUSIÓN_CL_ADITIVO_GRIS_LUZMAGENTA = -56;
    public final static byte MODOFUSIÓN_CL_ADITIVO_GRIS_LUZCYAN = -55;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Modos de fusión por Combinación Lineal para conmutación de canales ●">
    public final static byte MODOFUSIÓN_CL_CONMUTAR_ROJO = -126;
    public final static byte MODOFUSIÓN_CL_CONMUTAR_VERDE = -125;
    public final static byte MODOFUSIÓN_CL_CONMUTAR_AZUL = -124;
    public final static byte MODOFUSIÓN_CL_CONMUTAR_ROJOVERDE = -123;
    public final static byte MODOFUSIÓN_CL_CONMUTAR_ROJOAZUL = -122;
    public final static byte MODOFUSIÓN_CL_CONMUTAR_VERDEAZUL = -121;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Modos de fusión por Combinación Lineal de sustracción ●">
    public final static byte MODOFUSIÓN_CL_DIFERENCIA = -54;
    public final static byte MODOFUSIÓN_CL_DIFERENCIA_LAT = -53;
    public final static byte MODOFUSIÓN_CL_DIFERENCIA_TINTAROJA = -52;
    public final static byte MODOFUSIÓN_CL_DIFERENCIA_TINTAVERDE = -51;
    public final static byte MODOFUSIÓN_CL_DIFERENCIA_TINTAAZUL = -50;
    public final static byte MODOFUSIÓN_CL_DIFERENCIA_TINTAAMARILLA = -49;
    public final static byte MODOFUSIÓN_CL_DIFERENCIA_TINTAMAGENTA = -48;
    public final static byte MODOFUSIÓN_CL_DIFERENCIA_TINTACYAN = -47;
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Modos de fusión porcentuales ●">
    //<editor-fold defaultstate="collapsed" desc="Modos de fusión porcentuales basados en la suma ●">
    public final static byte MODOFUSIÓN_P_ADICIÓN_NULA = -120;
    public final static byte MODOFUSIÓN_P_ADICIÓN_SUP = -119;
    public final static byte MODOFUSIÓN_P_DECREMENTO_BASE = -118;
    public final static byte MODOFUSIÓN_P_DECREMENTO_SUP = -117;
    public final static byte MODOFUSIÓN_P_INCREMENTO_INVERTIDO_SUP = -116;
    public final static byte MODOFUSIÓN_P_INCREMENTO_INVERTIDO_BASE = -115;
    public final static byte MODOFUSIÓN_P_ADICIÓN_BASE = -114;
    public final static byte MODOFUSIÓN_P_INCREMENTO_SUP = -113;
    public final static byte MODOFUSIÓN_P_INCREMENTO_BASE = -112;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Modos de fusión porcentuales basados en el producto ●">
    public final static byte MODOFUSIÓN_P_PRODUCTO_DE_INVERSOS = -111;
    public final static byte MODOFUSIÓN_P_SOLAPAR = -110;
    public final static byte MODOFUSIÓN_P_PRODUCTO_BASE = -109;
    public final static byte MODOFUSIÓN_P_PRODUCTO_SUP = -108;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Modos de fusión porcentuales basados en la sustracción ●">
    public final static byte MODOFUSIÓN_P_SUSTRACCIÓN_BASE = -107;
    public final static byte MODOFUSIÓN_P_SUSTRACCIÓN_LAT_BASE = -106;
    public final static byte MODOFUSIÓN_P_SUSTRACCIÓN_ABS_BASE = -105;
    public final static byte MODOFUSIÓN_P_SUSTRACCIÓN_SUP = -104;
    public final static byte MODOFUSIÓN_P_SUSTRACCIÓN_LAT_SUP = -103;
    public final static byte MODOFUSIÓN_P_SUSTRACCIÓN_ABS_SUP = -102;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Modos de fusión porcentuales basados en la potencia ●">
    public final static byte MODOFUSIÓN_P_POTENCIA_BASE = -101;
    public final static byte MODOFUSIÓN_P_POTENCIA_SUP = -100;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Modos de fusión porcentuales basados en el cociente ●">
    public final static byte MODOFUSIÓN_P_COCIENTE_BASE = -99;
    public final static byte MODOFUSIÓN_P_COCIENTE_SUP = -98;
    public final static byte MODOFUSIÓN_P_COCIENTE_EXTREMO_BASE = -97;
    public final static byte MODOFUSIÓN_P_COCIENTE_EXTREMO_SUP = -96;
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Modos de fusión a nivel binario ●">
    public final static byte MODOFUSIÓN_NB_INTERSECCIÓN = -94;
    public final static byte MODOFUSIÓN_NB_UNIÓN = -93;
    public final static byte MODOFUSIÓN_NB_UNIÓN_EXCLUYENTE = -92;
    public final static byte MODOFUSIÓN_NB_DESPL_DERECHA = -91;
    public final static byte MODOFUSIÓN_NB_DESPL_IZQUIERDA = -90;
    public final static byte MODOFUSIÓN_NB_DESPL_DERECHA_LAT = -89;
    public final static byte MODOFUSIÓN_NB_DESPL_IZQUIERDA_LAT = -88;
    public final static byte MODOFUSIÓN_NB_DESPL_DERECHA_NEG = -87;
    public final static byte MODOFUSIÓN_NB_DESPL_IZQUIERDA_NEG = -86;
    public final static byte MODOFUSIÓN_NB_DESPL_DERECHA_LAT_NEG = -85;
    public final static byte MODOFUSIÓN_NB_DESPL_IZQUIERDA_LAT_NEG = -84;
    public final static byte MODOFUSIÓN_NB_DESPL_DERECHA_NEG_BASE = -83;
    public final static byte MODOFUSIÓN_NB_DESPL_IZQUIERDA_NEG_BASE = -82;
    public final static byte MODOFUSIÓN_NB_DESPL_DERECHA_LAT_NEG_SUP = -81;
    public final static byte MODOFUSIÓN_NB_DESPL_IZQUIERDA_LAT_NEG_SUP = -80;
    public final static byte MODOFUSIÓN_NB_DESPL_DERECHA_NEG_SUP = -79;
    public final static byte MODOFUSIÓN_NB_DESPL_IZQUIERDA_NEG_SUP = -78;
    public final static byte MODOFUSIÓN_NB_DESPL_DERECHA_LAT_NEG_BASE = -77;
    public final static byte MODOFUSIÓN_NB_DESPL_IZQUIERDA_LAT_NEG_BASE = -76;
    public final static byte MODOFUSIÓN_NB_INTERSECCIÓN_NEG = -75;
    public final static byte MODOFUSIÓN_NB_UNIÓN_NEG = -74;
    public final static byte MODOFUSIÓN_NB_INTERSECCIÓN_NEG_BASE = -72;
    public final static byte MODOFUSIÓN_NB_UNIÓN_NEG_BASE = -71;
    public final static byte MODOFUSIÓN_NB_UNIÓN_SUP = -69;
    public final static byte MODOFUSIÓN_NB_INTERSECCIÓN_NEG_SUP = -68;
    public final static byte MODOFUSIÓN_NB_UNIÓN_NEG_SUP = -67;
    public final static byte MODOFUSIÓN_NB_UNIÓN_EXCLUYENTE_NEG = -66;
    //</editor-fold>

    public final static byte MODOFUSIÓN_SG_DISTANCIA = -45;
    public final static byte MODOFUSIÓN_SG_LUZ_NIVEL = -44;
    public final static byte MODOFUSIÓN_SG_LUZ_NIVEL_FUERTE = -43;
    public final static byte MODOFUSIÓN_SG_ADITIVO_BINARIZADO = -42;
    public final static byte MODOFUSIÓN_SG_DIVISIÓN = -38;
    public final static byte MODOFUSIÓN_SG_DIVISIÓN_LATERALIZADA = -36;
    public final static byte MODOFUSIÓN_SG_MÓDULO = -35;
    public final static byte MODOFUSIÓN_SG_MÓDULO_LATERALIZADO = -34;
    public final static byte MODOFUSIÓN_SG_GRIS_DISTANCIA = -33;
    public final static byte MODOFUSIÓN_SG_COLOR_MÁS_OSCURO = -32;
    public final static byte MODOFUSIÓN_SG_COLOR_MÁS_CLARO = -31;
    public final static byte MODOFUSIÓN_SG_SUBEXPOSICIÓN_LINEAL = -30;
    public final static byte MODOFUSIÓN_SG_SOBREEXPONER = -29;
    public final static byte MODOFUSIÓN_SG_SUPERPONER = -28;
    public final static byte MODOFUSIÓN_SG_LUZ_FUERTE = -27;
    public final static byte MODOFUSIÓN_C_E_CUADRICULA = -25;
    public final static byte MODOFUSIÓN_C_E_CUADRICULA_AGUJEROS_CURCULARES = -24;
    public final static byte MODOFUSIÓN_C_E_CUADRICULA_PUNTOS = -23;
    public final static byte MODOFUSIÓN_C_E_AGUJEROS_PUNTOS_Y_CUADROS = -22;
    public final static byte MODOFUSIÓN_C_E_PUNTOS_Y_CUADROS = -21;
    public final static byte MODOFUSIÓN_C_E_COLUMNAS = -20;
    public final static byte MODOFUSIÓN_C_E_FILAS = -19;
    public final static byte MODOFUSIÓN_C_E_BLOQUES = -18;
    public final static byte MODOFUSIÓN_C_E_PASILLOS = -17;
    public final static byte MODOFUSIÓN_C_E_FUGA = -11;
    public final static byte MODOFUSIÓN_C_E_RADIAL = -14;
    public final static byte MODOFUSIÓN_C_E_ARCOS = -16;
    public final static byte MODOFUSIÓN_C_E_AROS = -15;
    public final static byte MODOFUSIÓN_C_E_ESPIRAL = -13;
    public final static byte MODOFUSIÓN_C_E_DIAFRAGMA = -12;
    public final static byte MODOFUSIÓN_C_E_CORAZÓN_RIZADO = -10;
    public final static byte MODOFUSIÓN_C_E_CORAZÓN = -9;
    public final static byte MODOFUSIÓN_C_E_CARDIOIDE = -8;
    public final static byte MODOFUSIÓN_C_E_FLOR_2_PETALOS = -7;
    public final static byte MODOFUSIÓN_C_E_FLOR_3_PETALOS = -6;
    public final static byte MODOFUSIÓN_C_E_FLOR_4_PETALOS = -5;
    public final static byte MODOFUSIÓN_C_E_FLOR_5_PETALOS = -4;
    public final static byte MODOFUSIÓN_C_E_FLOR_6_PETALOS = -3;
    public final static byte MODOFUSIÓN_1 = -2;
    public final static byte MODOFUSIÓN_2 = -1;
    public final static byte MODOFUSIÓN_h = 0;
    public final static byte MODOFUSIÓN_i = 1;
    public final static byte MODOFUSIÓN_l = 2;
    public final static byte MODOFUSIÓN_m = 3;
    public final static byte MODOFUSIÓN_n = 4;
    public final static byte MODOFUSIÓN_ñ = 5;

    public final static byte PHOTOSHOP_OSCURECER = MODOFUSIÓN_C_MIN,
            PHOTOSHOP_DARKEN = PHOTOSHOP_OSCURECER;

    public final static byte PHOTOSHOP_OSCURECER_MULTIPLICAR = MODOFUSIÓN_P_ADICIÓN_NULA,
            PHOTOSHOP_DARKEN_MULTIPLY = PHOTOSHOP_OSCURECER_MULTIPLICAR;

    public final static byte PHOTOSHOP_OSCURECER_SUBEXPOSICIÓN_LINEAL = MODOFUSIÓN_SG_SUBEXPOSICIÓN_LINEAL,
            PHOTOSHOP_DARKEN_LINEAR_BURN = PHOTOSHOP_OSCURECER_SUBEXPOSICIÓN_LINEAL;

    public final static byte PHOTOSHOP_OSCURECER_COLOR_MÁS_OSCURO = MODOFUSIÓN_SG_COLOR_MÁS_OSCURO,
            PHOTOSHOP_DARKEN_DARKER_COLOR = PHOTOSHOP_OSCURECER_COLOR_MÁS_OSCURO;

    public final static byte PHOTOSHOP_CONTRASTE_SUPERPONER = MODOFUSIÓN_SG_SUPERPONER,
            PHOTOSHOP_CONTRAST_OVERLAY = PHOTOSHOP_CONTRASTE_SUPERPONER;

    public final static byte PHOTOSHOP_CONTRASTE_LUZ_FUERTE = MODOFUSIÓN_SG_LUZ_FUERTE,
            PHOTOSHOP_CONTRAST_HARD_LIGHT = PHOTOSHOP_CONTRASTE_LUZ_FUERTE;

    public final static byte PHOTOSHOP_CONTRASTE_LUZ_SUAVE = MODOFUSIÓN_P_SOLAPAR,
            PHOTOSHOP_CONTRAST_SOFT_LIGHT = PHOTOSHOP_CONTRASTE_LUZ_SUAVE;

    public final static byte PHOTOSHOP_CONTRASTE_MEZCLA_DEFINIDA = MODOFUSIÓN_SG_ADITIVO_BINARIZADO,
            PHOTOSHOP_CONTRAST_HARD_MIX = PHOTOSHOP_CONTRASTE_MEZCLA_DEFINIDA;

    public final static byte PHOTOSHOP_ACLARAR = MODOFUSIÓN_C_MAX,
            PHOTOSHOP_LIGHTEN = PHOTOSHOP_ACLARAR;

    public final static byte PHOTOSHOP_ACLARAR_TRAMA = MODOFUSIÓN_P_PRODUCTO_DE_INVERSOS,
            PHOTOSHOP_ACLARAR_PANTALLA = PHOTOSHOP_ACLARAR_TRAMA,
            PHOTOSHOP_LIGHTEN_SCREEN = PHOTOSHOP_ACLARAR_TRAMA;

    public final static byte PHOTOSHOP_ACLARAR_COLOR_MÁS_CLARO = MODOFUSIÓN_SG_COLOR_MÁS_CLARO,
            PHOTOSHOP_LIGHTEN_LIGHTER_COLOR = PHOTOSHOP_ACLARAR_COLOR_MÁS_CLARO,
            PHOTOSHOP_LIGHTEN_LINEAR_COLOR = PHOTOSHOP_ACLARAR_COLOR_MÁS_CLARO;

    public final static byte PHOTOSHOP_COMPARAR_RESTAR = MODOFUSIÓN_CL_DIFERENCIA,
            PHOTOSHOP_COMPARE_SUBSTRACT = PHOTOSHOP_COMPARAR_RESTAR;

    public final static byte PHOTOSHOP_COMPARAR_DIFERENCIA = MODOFUSIÓN_SG_DISTANCIA,
            PHOTOSHOP_COMPARE_DIFFERENCE = PHOTOSHOP_COMPARAR_DIFERENCIA;

    JTextField[][] textfields;

    ListaID ListaID = new ListaID();

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc="Selección del Look and feel">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
        }
        //</editor-fold>
        new GUI_PruebaModoFusión();
    }

    public GUI_PruebaModoFusión() {//<editor-fold defaultstate="collapsed" desc="Implementación de código del constructor »">

        //<editor-fold defaultstate="collapsed" desc="inicialización de la GUI">
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        //</editor-fold>

        jLabel5.setText("");

        textfields = ObtenerMatrizGUI_CL();

        jList2.setCellRenderer(new Personalizador_JList());
        jList2.setBorder(new EmptyBorder(0, 10, 0, 0));

        EdiciónJtext(false);

        ListenersJText();
        ListenersArrastreDeArchivos();

        InicializaciónDeLosPresentadores();

        Escenario = new Fotograma();
        Escenario.centrarCapas();

        LlenarTablaDeModosDeFusión();

        Maquillaje_SwingGUI.AsignarLista_JList(jList2, ObtenerKeysTabla());

        SincronizarGUIConModoFusión();
    }//</editor-fold>

    void LlenarTablaDeModosDeFusión() {
        ListaID.add("/-//cb/Modos de fusión en Adobe Photoshop");
        ListaID.add("/-//l//cb/Photoshop Normal");
        ListaID.add("Normal", MODOFUSIÓN_C_NORMAL);
        ListaID.add("Disolver (Dissolve)", MODOFUSIÓN_C_E_DISOLVER);
        ListaID.add("/-//l//cb/Photoshop Oscurecer (Darken)");
        ListaID.add("Oscurecer (Darken)", MODOFUSIÓN_C_MIN);
        ListaID.add("Subexposición lineal (Linear Burn)", MODOFUSIÓN_SG_SUBEXPOSICIÓN_LINEAL);
        ListaID.add("Color más oscuro (Darker Color)", MODOFUSIÓN_SG_COLOR_MÁS_OSCURO);
        ListaID.add("/-//l//cb/Photoshop Aclarar (Lighten)");
        ListaID.add("Aclarar (Lighten)", MODOFUSIÓN_C_MAX);
        ListaID.add("Color más claro (Linear/Lighter Color)", MODOFUSIÓN_SG_COLOR_MÁS_CLARO);
        ListaID.add("Sobreexposición Lineal / Añadir (Linear Dodge / Add)", MODOFUSIÓN_CL_ADITIVO);
        ListaID.add("/-//l//cb/Photoshop Contraste (Contrast)");
        ListaID.add("Superponer (Overlay)", MODOFUSIÓN_SG_SUPERPONER);
        ListaID.add("Mezcla definida (Hard Mix)", MODOFUSIÓN_SG_ADITIVO_BINARIZADO);
        ListaID.add("Luz fuerte (Hard Light)", MODOFUSIÓN_SG_LUZ_FUERTE);
        ListaID.add("/-//l//cb/Photoshop Comparar (Compare)");
        ListaID.add("Diferencia", MODOFUSIÓN_SG_DISTANCIA);
        ListaID.add("Dividir (Divide)", MODOFUSIÓN_SG_DIVISIÓN);
        ListaID.add("Restar (Substract)", MODOFUSIÓN_CL_DIFERENCIA);

        ListaID.add("/-//cg/Modos de fusión Jeff Aporta");

        ListaID.add("/-//l//cg/Modos de conmutación");
        ListaID.add("C: Normal", MODOFUSIÓN_C_NORMAL);
        ListaID.add("C: Disolver Ruido", MODOFUSIÓN_C_DISOLVER_RUIDO);
        ListaID.add("C: Máximo", MODOFUSIÓN_C_MAX);
        ListaID.add("C: Mínimo", MODOFUSIÓN_C_MIN);
        ListaID.add("/-//l//cg/Modos de conmutación enmallados");
        ListaID.add("C E: Disolver", MODOFUSIÓN_C_E_DISOLVER);
        ListaID.add("/-//l//cg/Modos de conmutación enmallados (Matriz)");
        ListaID.add("C E: Cuadricula", MODOFUSIÓN_C_E_CUADRICULA);
        ListaID.add("C E: Columnas", MODOFUSIÓN_C_E_COLUMNAS);
        ListaID.add("C E: Filas", MODOFUSIÓN_C_E_FILAS);
        ListaID.add("C E: Bloques", MODOFUSIÓN_C_E_BLOQUES);
        ListaID.add("C E: Pasillos", MODOFUSIÓN_C_E_PASILLOS);
        ListaID.add("C E: Cuadricula agujeros circulares", MODOFUSIÓN_C_E_CUADRICULA_AGUJEROS_CURCULARES);
        ListaID.add("C E: Cuadricula puntos", MODOFUSIÓN_C_E_CUADRICULA_PUNTOS);
        ListaID.add("C E: Agujeros puntos y cuadros", MODOFUSIÓN_C_E_AGUJEROS_PUNTOS_Y_CUADROS);
        ListaID.add("C E: Puntos y cuadros", MODOFUSIÓN_C_E_PUNTOS_Y_CUADROS);
        ListaID.add("/-//l//cg/Modos de conmutación enmallados (Polares)");
        ListaID.add("C E: Fuga", MODOFUSIÓN_C_E_FUGA);
        ListaID.add("C E: Radial", MODOFUSIÓN_C_E_RADIAL);
        ListaID.add("C E: Arcos", MODOFUSIÓN_C_E_ARCOS);
        ListaID.add("C E: Aros", MODOFUSIÓN_C_E_AROS);
        ListaID.add("C E: Espiral", MODOFUSIÓN_C_E_ESPIRAL);
        ListaID.add("C E: Diafragma", MODOFUSIÓN_C_E_DIAFRAGMA);
        ListaID.add("C E: Corazón rizado", MODOFUSIÓN_C_E_CORAZÓN_RIZADO);
        ListaID.add("C E: Corazón", MODOFUSIÓN_C_E_CORAZÓN);
        ListaID.add("C E: Cordioide", MODOFUSIÓN_C_E_CARDIOIDE);
        ListaID.add("C E: Flor de 2 petalos", MODOFUSIÓN_C_E_FLOR_2_PETALOS);
        ListaID.add("C E: Flor de 3 petalos", MODOFUSIÓN_C_E_FLOR_3_PETALOS);
        ListaID.add("C E: Flor de 4 petalos", MODOFUSIÓN_C_E_FLOR_4_PETALOS);
        ListaID.add("C E: Flor de 5 petalos", MODOFUSIÓN_C_E_FLOR_5_PETALOS);
        ListaID.add("C E: Flor de 6 petalos", MODOFUSIÓN_C_E_FLOR_6_PETALOS);

        //<editor-fold defaultstate="collapsed" desc="Modos de Combinación Lineal">
        ListaID.add("/-//l//cg/Modos de Combinación Lineal");
        ListaID.add("CL: General", MODOFUSIÓN_CL_GENERAL);
        ListaID.add("/-//l//cg/Modos de Combinación Lineal (Aditivos)");
        ListaID.add("CL: Adición, Aditivo, Luz, Sobreexposición Lineal", MODOFUSIÓN_CL_ADITIVO);
        ListaID.add("CL: Aditivo Luz Roja", MODOFUSIÓN_CL_ADITIVO_LUZROJA);
        ListaID.add("CL: Aditivo Luz Verde", MODOFUSIÓN_CL_ADITIVO_LUZVERDE);
        ListaID.add("CL: Aditivo Luz Azúl", MODOFUSIÓN_CL_ADITIVO_LUZAZUL);
        ListaID.add("CL: Aditivo Luz Amarilla", MODOFUSIÓN_CL_ADITIVO_LUZAMARILLA);
        ListaID.add("CL: Aditivo Luz Magenta", MODOFUSIÓN_CL_ADITIVO_LUZMAGENTA);
        ListaID.add("CL: Aditivo Luz Cyan", MODOFUSIÓN_CL_ADITIVO_LUZCYAN);
        ListaID.add("CL: Aditivo Gris Luz", MODOFUSIÓN_CL_ADITIVO_GRIS_LUZ);
        ListaID.add("CL: Aditivo Gris Luz Rojo", MODOFUSIÓN_CL_ADITIVO_GRIS_LUZROJO);
        ListaID.add("CL: Aditivo Gris Luz Verde", MODOFUSIÓN_CL_ADITIVO_GRIS_LUZVERDE);
        ListaID.add("CL: Aditivo Gris Luz Azul", MODOFUSIÓN_CL_ADITIVO_GRIS_LUZAZUL);
        ListaID.add("CL: Aditivo Gris Luz Amarillo", MODOFUSIÓN_CL_ADITIVO_GRIS_LUZAMARILLO);
        ListaID.add("CL: Aditivo Gris Luz Magenta", MODOFUSIÓN_CL_ADITIVO_GRIS_LUZMAGENTA);
        ListaID.add("CL: Aditivo Gris Luz Cyan", MODOFUSIÓN_CL_ADITIVO_GRIS_LUZCYAN);
        ListaID.add("/-//l//cg/Modos de Combinación Lineal (Diferencia)");
        ListaID.add("CL: Diferencia", MODOFUSIÓN_CL_DIFERENCIA);
        ListaID.add("CL: Diferencia Lat.", MODOFUSIÓN_CL_DIFERENCIA_LAT);
        ListaID.add("CL: Diferencia Tinta Roja", MODOFUSIÓN_CL_DIFERENCIA_TINTAROJA);
        ListaID.add("CL: Diferencia Tinta Verde", MODOFUSIÓN_CL_DIFERENCIA_TINTAVERDE);
        ListaID.add("CL: Diferencia Tinta Azul", MODOFUSIÓN_CL_DIFERENCIA_TINTAAZUL);
        ListaID.add("CL: Diferencia Tinta Amarilla", MODOFUSIÓN_CL_DIFERENCIA_TINTAAMARILLA);
        ListaID.add("CL: Diferencia Tinta Magenta", MODOFUSIÓN_CL_DIFERENCIA_TINTAMAGENTA);
        ListaID.add("CL: Diferencia Tinta Cyan", MODOFUSIÓN_CL_DIFERENCIA_TINTACYAN);
        ListaID.add("/-//l//cg/Modos de Combinación Lineal (Conmutación)");
        ListaID.add("CL: Conmutar Rojo", MODOFUSIÓN_CL_CONMUTAR_ROJO);
        ListaID.add("CL: Conmutar Verde", MODOFUSIÓN_CL_CONMUTAR_VERDE);
        ListaID.add("CL: Conmutar Azul", MODOFUSIÓN_CL_CONMUTAR_AZUL);
        ListaID.add("CL: Conmutar Rojo-Verde", MODOFUSIÓN_CL_CONMUTAR_ROJOVERDE);
        ListaID.add("CL: Conmutar Rojo-Azul", MODOFUSIÓN_CL_CONMUTAR_ROJOAZUL);
        ListaID.add("CL: Conmutar Verde-Azul", MODOFUSIÓN_CL_CONMUTAR_VERDEAZUL);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Modos Porcentuales">
        ListaID.add("/-//l//cg/Modos de operaciones Porcentuales");
        ListaID.add("/-//l//cg/Modos de operaciones Porcentuales (Adición)");
        ListaID.add("P: Adición nula, Multiplicar", MODOFUSIÓN_P_ADICIÓN_NULA);
        ListaID.add("P: Adición base", MODOFUSIÓN_P_ADICIÓN_BASE);
        ListaID.add("P: Adición superior", MODOFUSIÓN_P_ADICIÓN_SUP);
        ListaID.add("/-//l//cg/Modos de operaciones Porcentuales (Decremento)");
        ListaID.add("P: Decremento base", MODOFUSIÓN_P_DECREMENTO_BASE);
        ListaID.add("P: Decremento superior", MODOFUSIÓN_P_DECREMENTO_SUP);
        ListaID.add("/-//l//cg/Modos de operaciones Porcentuales (Incremento)");
        ListaID.add("P: Incremento invertido Superior", MODOFUSIÓN_P_INCREMENTO_INVERTIDO_SUP);
        ListaID.add("P: Incremento invertido Base", MODOFUSIÓN_P_INCREMENTO_INVERTIDO_BASE);
        ListaID.add("P: Incremento superior", MODOFUSIÓN_P_INCREMENTO_SUP);
        ListaID.add("P: Incremento base", MODOFUSIÓN_P_INCREMENTO_BASE);
        ListaID.add("/-//l//cg/Modos de operaciones Porcentuales (Especiales)");
        ListaID.add("P: Solapar, Luz Suave", MODOFUSIÓN_P_SOLAPAR);
        ListaID.add("/-//l//cg/Modos de operaciones Porcentuales (Producto)");
        ListaID.add("P: Productos de inversos", MODOFUSIÓN_P_PRODUCTO_DE_INVERSOS);
        ListaID.add("P: Producto base", MODOFUSIÓN_P_PRODUCTO_BASE);
        ListaID.add("P: Producto superior", MODOFUSIÓN_P_PRODUCTO_SUP);
        ListaID.add("P: Producto Inversos, Trama, Pantalla", MODOFUSIÓN_P_PRODUCTO_DE_INVERSOS);
        ListaID.add("/-//l//cg/Modos de operaciones Porcentuales (Sustracción)");
        ListaID.add("P: Sustracción base", MODOFUSIÓN_P_SUSTRACCIÓN_BASE);
        ListaID.add("P: Sustracción lat. base", MODOFUSIÓN_P_SUSTRACCIÓN_LAT_BASE);
        ListaID.add("P: Sustracción abs. base", MODOFUSIÓN_P_SUSTRACCIÓN_ABS_BASE);
        ListaID.add("P: Sustracción superior", MODOFUSIÓN_P_SUSTRACCIÓN_SUP);
        ListaID.add("P: Sustracción lat. superior", MODOFUSIÓN_P_SUSTRACCIÓN_LAT_SUP);
        ListaID.add("P: Sustracción abs. superior", MODOFUSIÓN_P_SUSTRACCIÓN_ABS_SUP);
        ListaID.add("/-//l//cg/Modos de operaciones Porcentuales (Potencia)");
        ListaID.add("P: Potencia base", MODOFUSIÓN_P_POTENCIA_BASE);
        ListaID.add("P: Potencia superior", MODOFUSIÓN_P_POTENCIA_SUP);
        ListaID.add("/-//l//cg/Modos de operaciones Porcentuales (Cociente)");
        ListaID.add("P: Cociente base", MODOFUSIÓN_P_COCIENTE_BASE);
        ListaID.add("P: Cociente superior", MODOFUSIÓN_P_COCIENTE_SUP);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Modos a nivel binario">
        ListaID.add("/-//l//cg/Modos a Nivel Binario");
        ListaID.add("/-//l//cg/Modos a Nivel Binario (Intersección)");
        ListaID.add("NB: Intersección", MODOFUSIÓN_NB_INTERSECCIÓN);
        ListaID.add("NB: Intersección neg.", MODOFUSIÓN_NB_INTERSECCIÓN_NEG);
        ListaID.add("NB: Intersección neg. base", MODOFUSIÓN_NB_INTERSECCIÓN_NEG_BASE);
        ListaID.add("NB: Intersección neg. sup.", MODOFUSIÓN_NB_INTERSECCIÓN_NEG_SUP);
        ListaID.add("/-//l//cg/Modos a Nivel Binario (Unión)");
        ListaID.add("NB: Unión", MODOFUSIÓN_NB_UNIÓN);
        ListaID.add("NB: Unión neg.", MODOFUSIÓN_NB_UNIÓN_NEG);
        ListaID.add("NB: Unión neg. base", MODOFUSIÓN_NB_UNIÓN_NEG_BASE);
        ListaID.add("NB: Unión neg. sup.", MODOFUSIÓN_NB_UNIÓN_NEG_SUP);
        ListaID.add("/-//l//cg/Modos a Nivel Binario (Unión Excluyente)");
        ListaID.add("NB: Unión excluyente", MODOFUSIÓN_NB_UNIÓN_EXCLUYENTE);
        ListaID.add("NB: Unión excluyente neg.", MODOFUSIÓN_NB_UNIÓN_EXCLUYENTE_NEG);
        ListaID.add("/-//l//cg/Modos a Nivel Binario (Desplazamiento a la derecha)");
        ListaID.add("NB: despl. derecha", MODOFUSIÓN_NB_DESPL_DERECHA);
        ListaID.add("NB: despl. derecha neg.", MODOFUSIÓN_NB_DESPL_DERECHA_NEG);
        ListaID.add("NB: despl. derecha neg. base", MODOFUSIÓN_NB_DESPL_DERECHA_NEG_BASE);
        ListaID.add("NB: despl. derecha neg. sup.", MODOFUSIÓN_NB_DESPL_DERECHA_NEG_SUP);
        ListaID.add("NB: despl. derecha lat", MODOFUSIÓN_NB_DESPL_DERECHA_LAT);
        ListaID.add("NB: despl. derecha lat neg.", MODOFUSIÓN_NB_DESPL_DERECHA_LAT_NEG);
        ListaID.add("NB: despl. derecha lat neg. sup.", MODOFUSIÓN_NB_DESPL_DERECHA_LAT_NEG_SUP);
        ListaID.add("NB: despl. derecha lat neg. base", MODOFUSIÓN_NB_DESPL_DERECHA_LAT_NEG_BASE);
        ListaID.add("/-//l//cg/Modos a Nivel Binario (Desplazamiento a la izquierda)");
        ListaID.add("NB: despl. izquierda", MODOFUSIÓN_NB_DESPL_IZQUIERDA);
        ListaID.add("NB: despl. izquierda neg.", MODOFUSIÓN_NB_DESPL_IZQUIERDA_NEG);
        ListaID.add("NB: despl. izquierda neg. base", MODOFUSIÓN_NB_DESPL_IZQUIERDA_NEG_BASE);
        ListaID.add("NB: despl. izquierda neg. sup.", MODOFUSIÓN_NB_DESPL_IZQUIERDA_NEG_SUP);
        ListaID.add("NB: despl. izquierda lat.", MODOFUSIÓN_NB_DESPL_IZQUIERDA_LAT);
        ListaID.add("NB: despl. izquierda lat neg..", MODOFUSIÓN_NB_DESPL_IZQUIERDA_LAT_NEG);
        ListaID.add("NB: despl. izquierda lat. neg. sup.", MODOFUSIÓN_NB_DESPL_IZQUIERDA_LAT_NEG_SUP);
        ListaID.add("NB: despl. izquierda lat. neg. base", MODOFUSIÓN_NB_DESPL_IZQUIERDA_LAT_NEG_BASE);
        //</editor-fold>

        ListaID.add("/-//l//cg/Modos sin grupo");
        ListaID.add("SG: Luz Nivel", MODOFUSIÓN_SG_LUZ_NIVEL);
        ListaID.add("SG: Luz Nivel Fuerte", MODOFUSIÓN_SG_LUZ_NIVEL_FUERTE);
        ListaID.add("SG: Aditivo Binarizado", MODOFUSIÓN_SG_ADITIVO_BINARIZADO);
        ListaID.add("SG: Distancia", MODOFUSIÓN_SG_DISTANCIA);
        ListaID.add("SG: División", MODOFUSIÓN_SG_DIVISIÓN);
        ListaID.add("SG: División Lateralizada", MODOFUSIÓN_SG_DIVISIÓN_LATERALIZADA);
        ListaID.add("SG: Módulo", MODOFUSIÓN_SG_MÓDULO);
        ListaID.add("SG: Módulo Lateralizado", MODOFUSIÓN_SG_MÓDULO_LATERALIZADO);
        ListaID.add("SG: Gris distancia", MODOFUSIÓN_SG_GRIS_DISTANCIA);
        ListaID.add("SG: Sobreexponer", MODOFUSIÓN_SG_SOBREEXPONER);
        ListaID.add("SG: Subexposición lineal", MODOFUSIÓN_SG_SUBEXPOSICIÓN_LINEAL);
        ListaID.add("SG: Color más oscuro", MODOFUSIÓN_SG_COLOR_MÁS_OSCURO);
        ListaID.add("SG: Color más claro", MODOFUSIÓN_SG_COLOR_MÁS_CLARO);
        ListaID.add("SG: Superponer", MODOFUSIÓN_SG_SUPERPONER);
        ListaID.add("SG: Luz fuerte", MODOFUSIÓN_SG_LUZ_FUERTE);
    }

    BaseModoFusión ObtenerModoFusión(byte MODOFUSIÓN, float[][] M) {
        switch (MODOFUSIÓN) {
            case MODOFUSIÓN_C_NORMAL:
                return new ModoFusión.Conmutar.Normal();
            case MODOFUSIÓN_C_E_DIAFRAGMA:
                return new ModoFusión.Conmutar.Enmallado.Diafragma();
            case MODOFUSIÓN_C_E_FLOR_6_PETALOS:
                return new ModoFusión.Conmutar.Enmallado.Flor6Petalos();
            case MODOFUSIÓN_C_E_FLOR_5_PETALOS:
                return new ModoFusión.Conmutar.Enmallado.Flor5Petalos();
            case MODOFUSIÓN_C_E_FLOR_4_PETALOS:
                return new ModoFusión.Conmutar.Enmallado.Flor4Petalos();
            case MODOFUSIÓN_C_E_FLOR_3_PETALOS:
                return new ModoFusión.Conmutar.Enmallado.Flor3Petalos();
            case MODOFUSIÓN_C_E_FLOR_2_PETALOS:
                return new ModoFusión.Conmutar.Enmallado.Flor2Petalos();
            case MODOFUSIÓN_C_E_CARDIOIDE:
                return new ModoFusión.Conmutar.Enmallado.Cardioide();
            case MODOFUSIÓN_C_E_CORAZÓN:
                return new ModoFusión.Conmutar.Enmallado.Corazón();
            case MODOFUSIÓN_C_E_CORAZÓN_RIZADO:
                return new ModoFusión.Conmutar.Enmallado.CorazónRizado();
            case MODOFUSIÓN_C_MAX:
                return new ModoFusión.Conmutar.Máximo();
            case MODOFUSIÓN_C_MIN:
                return new ModoFusión.Conmutar.Mínimo();
            case MODOFUSIÓN_C_E_DISOLVER:
                return new ModoFusión.Conmutar.Enmallado.Disolver();
            case MODOFUSIÓN_C_E_CUADRICULA:
                return new ModoFusión.Conmutar.Enmallado.Cuadricula();
            case MODOFUSIÓN_C_E_CUADRICULA_AGUJEROS_CURCULARES:
                return new ModoFusión.Conmutar.Enmallado.CuadriculaAgujerosCirculares();
            case MODOFUSIÓN_C_E_CUADRICULA_PUNTOS:
                return new ModoFusión.Conmutar.Enmallado.CuadriculaPuntos();
            case MODOFUSIÓN_C_E_AGUJEROS_PUNTOS_Y_CUADROS:
                return new ModoFusión.Conmutar.Enmallado.AgujerosPuntosYCuadros();
            case MODOFUSIÓN_C_E_PUNTOS_Y_CUADROS:
                return new ModoFusión.Conmutar.Enmallado.PuntosYCuadros();
            case MODOFUSIÓN_C_E_COLUMNAS:
                return new ModoFusión.Conmutar.Enmallado.Columnas();
            case MODOFUSIÓN_C_E_FILAS:
                return new ModoFusión.Conmutar.Enmallado.Filas();
            case MODOFUSIÓN_C_E_BLOQUES:
                return new ModoFusión.Conmutar.Enmallado.Bloques();
            case MODOFUSIÓN_C_E_PASILLOS:
                return new ModoFusión.Conmutar.Enmallado.Pasillos();
            case MODOFUSIÓN_C_E_FUGA:
                return new ModoFusión.Conmutar.Enmallado.Fuga();
            case MODOFUSIÓN_C_E_RADIAL:
                return new ModoFusión.Conmutar.Enmallado.Radial();
            case MODOFUSIÓN_C_E_ARCOS:
                return new ModoFusión.Conmutar.Enmallado.Arcos();
            case MODOFUSIÓN_C_E_AROS:
                return new ModoFusión.Conmutar.Enmallado.Aros();
            case MODOFUSIÓN_C_E_ESPIRAL:
                return new ModoFusión.Conmutar.Enmallado.Espiral();
            case MODOFUSIÓN_C_DISOLVER_RUIDO:
                return new ModoFusión.Conmutar.DisolverRuido();
            case MODOFUSIÓN_SG_LUZ_FUERTE:
                return new ModoFusión.SinGrupo.LuzFuerte();
            case MODOFUSIÓN_SG_SUPERPONER:
                return new ModoFusión.SinGrupo.Superponer();
            case MODOFUSIÓN_SG_SUBEXPOSICIÓN_LINEAL:
                return new ModoFusión.SinGrupo.SubexposicionLineal();
            case MODOFUSIÓN_SG_SOBREEXPONER:
                return new ModoFusión.SinGrupo.Sobreexponer();
            case MODOFUSIÓN_SG_COLOR_MÁS_OSCURO:
                return new ModoFusión.SinGrupo.ColorMasOscuro();
            case MODOFUSIÓN_SG_COLOR_MÁS_CLARO:
                return new ModoFusión.SinGrupo.ColorMasClaro();
            case MODOFUSIÓN_SG_DISTANCIA:
                return new ModoFusión.SinGrupo.Distancia();
            case MODOFUSIÓN_SG_DIVISIÓN:
                return new ModoFusión.SinGrupo.División();
            case MODOFUSIÓN_SG_DIVISIÓN_LATERALIZADA:
                return new ModoFusión.SinGrupo.DivisiónLateralizada();
            case MODOFUSIÓN_SG_MÓDULO:
                return new ModoFusión.SinGrupo.Módulo();
            case MODOFUSIÓN_SG_MÓDULO_LATERALIZADO:
                return new ModoFusión.SinGrupo.MóduloLateralizado();
            case MODOFUSIÓN_SG_GRIS_DISTANCIA:
                return new ModoFusión.SinGrupo.Gris_Distancia();
            case MODOFUSIÓN_SG_LUZ_NIVEL:
                return new ModoFusión.SinGrupo.LuzNivel();
            case MODOFUSIÓN_SG_LUZ_NIVEL_FUERTE:
                return new ModoFusión.SinGrupo.LuzNivelFuerte();
            case MODOFUSIÓN_SG_ADITIVO_BINARIZADO:
                return new ModoFusión.SinGrupo.AditivoBinarizado();

            //<editor-fold defaultstate="collapsed" desc="Modos de combinación lineal">
            case MODOFUSIÓN_CL_GENERAL:
                return new ModoFusión.CombinaciónLineal.General(ObtenerMatrizCombinaciónLineal());

            case MODOFUSIÓN_CL_ADITIVO:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo();
            case MODOFUSIÓN_CL_ADITIVO_LUZROJA:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzRoja();
            case MODOFUSIÓN_CL_ADITIVO_LUZVERDE:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzVerde();
            case MODOFUSIÓN_CL_ADITIVO_LUZAZUL:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzAzul();
            case MODOFUSIÓN_CL_ADITIVO_LUZAMARILLA:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzAmarilla();
            case MODOFUSIÓN_CL_ADITIVO_LUZMAGENTA:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzMagenta();
            case MODOFUSIÓN_CL_ADITIVO_LUZCYAN:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzCyan();
            case MODOFUSIÓN_CL_ADITIVO_GRIS_LUZ:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzGris();
            case MODOFUSIÓN_CL_ADITIVO_GRIS_LUZROJO:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzGrisRojo();
            case MODOFUSIÓN_CL_ADITIVO_GRIS_LUZVERDE:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzGrisVerde();
            case MODOFUSIÓN_CL_ADITIVO_GRIS_LUZAZUL:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzGrisAzul();
            case MODOFUSIÓN_CL_ADITIVO_GRIS_LUZAMARILLO:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzGrisAmarillo();
            case MODOFUSIÓN_CL_ADITIVO_GRIS_LUZMAGENTA:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzGrisMagenta();
            case MODOFUSIÓN_CL_ADITIVO_GRIS_LUZCYAN:
                return new ModoFusión.CombinaciónLineal.RGB_Aditivo_LuzGrisCyan();

            case MODOFUSIÓN_CL_DIFERENCIA:
                return new ModoFusión.CombinaciónLineal.RGB_Diferencia();
            case MODOFUSIÓN_CL_DIFERENCIA_LAT:
                return new ModoFusión.CombinaciónLineal.RGB_Diferencia_Lat();
            case MODOFUSIÓN_CL_DIFERENCIA_TINTAROJA:
                return new ModoFusión.CombinaciónLineal.RGB_Diferencia_TintaRoja();
            case MODOFUSIÓN_CL_DIFERENCIA_TINTAVERDE:
                return new ModoFusión.CombinaciónLineal.RGB_Diferencia_TintaVerde();
            case MODOFUSIÓN_CL_DIFERENCIA_TINTAAZUL:
                return new ModoFusión.CombinaciónLineal.RGB_Diferencia_TintaAzul();
            case MODOFUSIÓN_CL_DIFERENCIA_TINTAAMARILLA:
                return new ModoFusión.CombinaciónLineal.RGB_Diferencia_TintaAmarilla();
            case MODOFUSIÓN_CL_DIFERENCIA_TINTAMAGENTA:
                return new ModoFusión.CombinaciónLineal.RGB_Diferencia_TintaMagenta();
            case MODOFUSIÓN_CL_DIFERENCIA_TINTACYAN:
                return new ModoFusión.CombinaciónLineal.RGB_Diferencia_TintaCyan();

            case MODOFUSIÓN_CL_CONMUTAR_ROJO:
                return new ModoFusión.CombinaciónLineal.Conmutar_Rojo();
            case MODOFUSIÓN_CL_CONMUTAR_VERDE:
                return new ModoFusión.CombinaciónLineal.Conmutar_Verde();
            case MODOFUSIÓN_CL_CONMUTAR_AZUL:
                return new ModoFusión.CombinaciónLineal.Conmutar_Azul();
            case MODOFUSIÓN_CL_CONMUTAR_ROJOVERDE:
                return new ModoFusión.CombinaciónLineal.Conmutar_RojoVerde();
            case MODOFUSIÓN_CL_CONMUTAR_ROJOAZUL:
                return new ModoFusión.CombinaciónLineal.Conmutar_RojoAzul();
            case MODOFUSIÓN_CL_CONMUTAR_VERDEAZUL:
                return new ModoFusión.CombinaciónLineal.Conmutar_VerdeAzul();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Modos Porcentuales">
            case MODOFUSIÓN_P_ADICIÓN_NULA:
                return new ModoFusión.Porcentual.Adición_Nula();
            case MODOFUSIÓN_P_ADICIÓN_SUP:
                return new ModoFusión.Porcentual.Adición_Superior();
            case MODOFUSIÓN_P_DECREMENTO_BASE:
                return new ModoFusión.Porcentual.Adición_Base();
            case MODOFUSIÓN_P_DECREMENTO_SUP:
                return new ModoFusión.Porcentual.Decremento_Superior();
            case MODOFUSIÓN_P_INCREMENTO_INVERTIDO_SUP:
                return new ModoFusión.Porcentual.IncrementoInvertido_Superior();
            case MODOFUSIÓN_P_INCREMENTO_INVERTIDO_BASE:
                return new ModoFusión.Porcentual.IncrementoInvertido_Base();
            case MODOFUSIÓN_P_ADICIÓN_BASE:
                return new ModoFusión.Porcentual.Adición_Base();
            case MODOFUSIÓN_P_INCREMENTO_SUP:
                return new ModoFusión.Porcentual.Incremento_Superior();
            case MODOFUSIÓN_P_INCREMENTO_BASE:
                return new ModoFusión.Porcentual.Incremento_Base();
            case MODOFUSIÓN_P_PRODUCTO_DE_INVERSOS:
                return new ModoFusión.Porcentual.Producto_Inversos();
            case MODOFUSIÓN_P_SOLAPAR:
                return new ModoFusión.Porcentual.LuzSuave();
            case MODOFUSIÓN_P_PRODUCTO_BASE:
                return new ModoFusión.Porcentual.Producto_Base();
            case MODOFUSIÓN_P_PRODUCTO_SUP:
                return new ModoFusión.Porcentual.Producto_Superior();
            case MODOFUSIÓN_P_SUSTRACCIÓN_BASE:
                return new ModoFusión.Porcentual.Sustracción_Base();
            case MODOFUSIÓN_P_SUSTRACCIÓN_LAT_BASE:
                return new ModoFusión.Porcentual.SustracciónLateralizada_Base();
            case MODOFUSIÓN_P_SUSTRACCIÓN_ABS_BASE:
                return new ModoFusión.Porcentual.SustracciónAbsoluta_Base();
            case MODOFUSIÓN_P_SUSTRACCIÓN_SUP:
                return new ModoFusión.Porcentual.Sustracción_Superior();
            case MODOFUSIÓN_P_SUSTRACCIÓN_LAT_SUP:
                return new ModoFusión.Porcentual.SustracciónLateralizada_Superior();
            case MODOFUSIÓN_P_SUSTRACCIÓN_ABS_SUP:
                return new ModoFusión.Porcentual.SustracciónAbsoluta_Superior();
            case MODOFUSIÓN_P_POTENCIA_BASE:
                return new ModoFusión.Porcentual.Potencia_Base();
            case MODOFUSIÓN_P_POTENCIA_SUP:
                return new ModoFusión.Porcentual.Potencia_Superior();
            case MODOFUSIÓN_P_COCIENTE_BASE:
                return new ModoFusión.Porcentual.Cociente_Base();
            case MODOFUSIÓN_P_COCIENTE_SUP:
                return new ModoFusión.Porcentual.Cociente_Superior();
            case MODOFUSIÓN_P_COCIENTE_EXTREMO_BASE:
                return new ModoFusión.Porcentual.Cociente_Extremo_Base();
            case MODOFUSIÓN_P_COCIENTE_EXTREMO_SUP:
                return new ModoFusión.Porcentual.Cociente_Extremo_Superior();
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Modos a nivel binario">
            case MODOFUSIÓN_NB_INTERSECCIÓN:
                return new ModoFusión.NivelBinario.Intersección();
            case MODOFUSIÓN_NB_UNIÓN:
                return new ModoFusión.NivelBinario.Unión();
            case MODOFUSIÓN_NB_UNIÓN_EXCLUYENTE:
                return new ModoFusión.NivelBinario.UniónExcluyente();
            case MODOFUSIÓN_NB_INTERSECCIÓN_NEG:
                return new ModoFusión.NivelBinario.Intersección_Negación();
            case MODOFUSIÓN_NB_UNIÓN_NEG:
                return new ModoFusión.NivelBinario.Unión_Negación();
            case MODOFUSIÓN_NB_UNIÓN_EXCLUYENTE_NEG:
                return new ModoFusión.NivelBinario.UniónExcluyente_Negación();
            case MODOFUSIÓN_NB_INTERSECCIÓN_NEG_BASE:
                return new ModoFusión.NivelBinario.Intersección_NegaciónBase();
            case MODOFUSIÓN_NB_UNIÓN_NEG_BASE:
                return new ModoFusión.NivelBinario.Unión_NegaciónBase();
            case MODOFUSIÓN_NB_INTERSECCIÓN_NEG_SUP:
                return new ModoFusión.NivelBinario.Intersección_NegaciónSuperior();
            case MODOFUSIÓN_NB_UNIÓN_NEG_SUP:
                return new ModoFusión.NivelBinario.Unión_NegaciónSuperior();
            case MODOFUSIÓN_NB_DESPL_DERECHA:
                return new ModoFusión.NivelBinario.DesplazarDerecha();
            case MODOFUSIÓN_NB_DESPL_IZQUIERDA:
                return new ModoFusión.NivelBinario.DesplazarIzquierda();
            case MODOFUSIÓN_NB_DESPL_DERECHA_LAT:
                return new ModoFusión.NivelBinario.DesplazarDerechaLat();
            case MODOFUSIÓN_NB_DESPL_IZQUIERDA_LAT:
                return new ModoFusión.NivelBinario.DesplazarIzquierdaLat();
            case MODOFUSIÓN_NB_DESPL_DERECHA_NEG:
                return new ModoFusión.NivelBinario.DesplazarDerecha_Negación();
            case MODOFUSIÓN_NB_DESPL_IZQUIERDA_NEG:
                return new ModoFusión.NivelBinario.DesplazarIzquierdaLat_Negación();
            case MODOFUSIÓN_NB_DESPL_DERECHA_LAT_NEG:
                return new ModoFusión.NivelBinario.DesplazarDerechaLat_Negación();
            case MODOFUSIÓN_NB_DESPL_IZQUIERDA_LAT_NEG:
                return new ModoFusión.NivelBinario.DesplazarIzquierdaLat_Negación();
            case MODOFUSIÓN_NB_DESPL_DERECHA_NEG_BASE:
                return new ModoFusión.NivelBinario.DesplazarDerecha_NegaciónBase();
            case MODOFUSIÓN_NB_DESPL_IZQUIERDA_NEG_BASE:
                return new ModoFusión.NivelBinario.DesplazarIzquierda_NegaciónBase();
            case MODOFUSIÓN_NB_DESPL_DERECHA_LAT_NEG_SUP:
                return new ModoFusión.NivelBinario.DesplazarDerechaLat_NegaciónSuperior();
            case MODOFUSIÓN_NB_DESPL_IZQUIERDA_LAT_NEG_SUP:
                return new ModoFusión.NivelBinario.DesplazarIzquierdaLat_NegaciónSuperior();
            case MODOFUSIÓN_NB_DESPL_DERECHA_NEG_SUP:
                return new ModoFusión.NivelBinario.DesplazarDerecha_NegaciónSuperior();
            case MODOFUSIÓN_NB_DESPL_IZQUIERDA_NEG_SUP:
                return new ModoFusión.NivelBinario.DesplazarIzquierda_NegaciónSuperior();
            case MODOFUSIÓN_NB_DESPL_DERECHA_LAT_NEG_BASE:
                return new ModoFusión.NivelBinario.DesplazarDerechaLat_NegaciónBase();
            case MODOFUSIÓN_NB_DESPL_IZQUIERDA_LAT_NEG_BASE:
                return new ModoFusión.NivelBinario.DesplazarIzquierdaLat_NegaciónBase();
//</editor-fold>

            default:
                throw new RuntimeException("El Modo de fusión no se reconoce " + MODOFUSIÓN);
        }
    }

    BaseModoFusión ObtenerModoFusión(byte MODOFUSIÓN) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return ObtenerModoFusión(MODOFUSIÓN, null);
    }//</editor-fold>

    void SincronizarGUIConModoFusión() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            //<editor-fold defaultstate="collapsed" desc="Se avisará si el modo de fusión genera salidas RGB">
            Presentador p = (Presentador) jLabel5;
            if (modoFusión.RiesgoSalida) {
                p.ActualizarFotograma(BibliotecaArchivos.Imagenes.Iconos.Alerta("  Hay riesgo\n  de salida\n  RGB")
                );
            } else {
                p.ActualizarFotograma(BibliotecaArchivos.Imagenes.Iconos.Correcto("  No Hay\n  riesgo de\n  salida RGB")
                );
            }
            //</editor-fold>
            if (jList2.getSelectedValue() != null) {
                PresentadorEscenario.setText("  " + jList2.getSelectedValue());
            } else {
                PresentadorEscenario.setText("  Normal");
            }
        } catch (Exception e) {
        }
    }//</editor-fold>

    void SincronizarModoFusiónConGUI() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            modoFusión.Opacidad = (double) jSlider1.getValue() / jSlider1.getMaximum();
            modoFusión.Modificar_AlphaDiscriminador((byte) jComboBox1.getSelectedIndex());
            switch (jComboBox2.getSelectedItem().toString()) {
                case "Extremos":
                    modoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_A_EXTREMOS);
                    break;
                case "Extremos abs":
                    modoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_A_EXTREMOS_ABS);
                    break;
                case "Circular":
                    modoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_CIRCULAR);
                    break;
                case "Circular abs":
                    modoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_CIRCULAR_ABS);
                    break;
                case "Reflejo":
                    modoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_REFLEJO);
                    break;
                case "Reflejo Inv":
                    modoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_REFLEJO_INV);
                    break;
                case "Mascara de bits":
                    modoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_MASCARA_BITS);
                    break;
                case "Nulidad por salida":
                    modoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_NULIDAD_POR_SALIDA);
                    break;
                case "Inserción a extremos por salida":
                    modoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_INSERCIÓN_EXTREMOS_POR_SALIDA);
                    break;
                case "Inserción circular por salida":
                    modoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_INSERCIÓN_CIRCULAR_POR_SALIDA);
                    break;
                case "Inserción reflejo por salida":
                    modoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_INSERCIÓN_REFLEJO_POR_SALIDA);
                    break;
                case "Sin definir":
                    modoFusión.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_SIN_DEFINIR);
                    break;
            }
        } catch (Exception e) {
        }
        ActualizarEscenario();
    }
    //</editor-fold>

    void InicializaciónDeLosPresentadores() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        PresentadorEscenario = (Presentador) jLabel1;
        PresentadorEscenario.setText("");
        PresentadorEscenario.TIPO_AJUSTE_FONDO = Filtros_Lineales.AJUSTE_MOSAICO;

        PresentadorPatrónFondo = (Presentador) jLabel4;
        PresentadorPatrónFondo.TIPO_AJUSTE_FONDO = Filtros_Lineales.AJUSTE_MOSAICO;

        PresentadorCapaSuperior = (Presentador) jLabel2;
        PresentadorCapaSuperior.Borde_Fotograma(Color.GRAY, 2).setText("");
        PresentadorCapaSuperior.ColorFondo(new Color(0x13000000, true));

        PresentadorCapaInferior = (Presentador) jLabel3;
        PresentadorCapaInferior.Borde_Fotograma(Color.LIGHT_GRAY, 2).setText("");

        jLabel4MouseReleased(null);
    }//</editor-fold>

    String[] ObtenerKeysTabla() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return ListaID.obtenerListaNombres();
    }//</editor-fold>

    void ActualizarEscenario() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            PresentadorEscenario.ActualizarFotograma(Escenario.fotograma());
        } catch (Exception e) {
        }
    }//</editor-fold>

    void EdiciónJtext(boolean b) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (JTextField[] textfield : textfields) {
            for (JTextField jTextField : textfield) {
                if (b) {
                    jTextField.setBackground(Color.WHITE);
                } else {
                    jTextField.setBackground(new Color(0xcccccc));
                }
                jTextField.setEditable(b);
            }
        }
    }//</editor-fold>

    void ListenersArrastreDeArchivos() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        ArrastrarArchivos.Añadir((rutas) -> {
            BufferedImage NuevaImagen = LectoEscrituraArchivos.cargar_imagen(rutas[0]);
            if (NuevaImagen == null) {
                return;
            }
            Escenario.ModificarCapas(
                    NuevaImagen, null
            );
            Escenario.centrarCapas();
        },
                jLabel1, jLabel2
        );
        ArrastrarArchivos.Añadir(jLabel3, (rutas) -> {
            BufferedImage NuevaImagen = LectoEscrituraArchivos.cargar_imagen(rutas[0]);
            if (NuevaImagen == null) {
                return;
            }
            Escenario.ModificarCapas(
                    null, NuevaImagen
            );
            Escenario.centrarCapas();
        });
    }//</editor-fold>

    void ListenersJText() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (JTextField[] textfield : textfields) {
            for (JTextField jTextField : textfield) {
                jTextField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent evt) {
                        if (!jTextField.isEditable()) {
                            return;
                        }
                        try {
                            if (!jTextField.getText().equals("")) {
                                double a = Double.parseDouble(jTextField.getText());
                            }
                            jTextField.setBackground(Color.WHITE);
                            ActualizarModoFusiónCombinaciónLineal();
                            ActualizarEscenario();
                        } catch (Exception e) {
                            jTextField.setBackground(Color.PINK);
                        }
                    }
                });
            }
        }
    }//</editor-fold>

    void ActualizarModoFusiónCombinaciónLineal() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (modoFusión instanceof ModoFusión.CombinaciónLineal.General) {
            ModoFusión.CombinaciónLineal.General CL = (ModoFusión.CombinaciónLineal.General) modoFusión;
            CL.ActualizarMatriz(ObtenerMatrizCombinaciónLineal());
            ActualizarEscenario();
        }
    }//</editor-fold>

    float[][] ObtenerMatrizCombinaciónLineal() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        float[][] retorno = new float[6][3];
        for (int c = 0; c < 3; c++) {
            for (int f = 0; f < 6; f++) {
                try {
                    retorno[f][c] = Float.parseFloat(textfields[f][c].getText());
                } catch (Exception e) {
                    retorno[f][c] = 0;
                }
            }
        }
        return retorno;
    }//</editor-fold>

    JTextField[][] ObtenerMatrizGUI_CL() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        JTextField[][] retorno = new JTextField[6][3];
        int f = 0;
        int c = 0;
        for (Component component : jPanel2.getComponents()) {
            retorno[f][c] = (JTextField) component;
            c++;
            if (c == 3) {
                c = 0;
                f++;
            }
        }
        return retorno;
    }//</editor-fold>

    public class ListaID extends ArrayList<ID> {//<editor-fold defaultstate="collapsed" desc="Código de la clase">

        public void add(String nombre, byte ID) {
            add(new ID(nombre, ID));
        }

        public void add(String nombre) {
            add(new ID(nombre));
        }

        String[] obtenerListaNombres() {
            String[] retorno = new String[size()];
            for (int j = 0; j < this.size(); j++) {
                retorno[j] = get(j).nombre;
            }
            return retorno;
        }

        byte ObtenerID(String n) {
            for (ID id : this) {
                if (id.nombre.equals(n)) {
                    return id.ID;
                }
            }
            throw new RuntimeException("No se encontró el identificador con nombre: " + n);
        }
    }//</editor-fold>

    public class ID {//<editor-fold defaultstate="collapsed" desc="Código de la clase">

        String nombre;
        byte ID;

        public ID(String nombre) {
            this.nombre = nombre;
        }

        public ID(String nombre, byte ID) {
            this.nombre = nombre;
            this.ID = ID;
        }
    }//</editor-fold>

    class Fotograma {//<editor-fold defaultstate="collapsed" desc="Código de la clase">

        //<editor-fold defaultstate="collapsed" desc="Variables ●">
        Capa capaSuperior;
        Capa capaInferior;
        //</editor-fold>

        public Fotograma() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            PresentadorEscenario.setText("Cargando imagenes");
            PresentadorCapaSuperior.setText("Cargando imagenes");
            PresentadorCapaInferior.setText("Cargando imagenes");
            ModificarCapas(BibliotecaArchivos.Imagenes.Test.IMG1(),
                    BibliotecaArchivos.Imagenes.Test.IMG2()
            );
            PresentadorEscenario.setText("");
            PresentadorCapaSuperior.setText("");
            PresentadorCapaInferior.setText("");
        }//</editor-fold>

        void ModificarCapas(BufferedImage Superior, BufferedImage Inferior) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            if (Superior != null) {
                capaSuperior = new Capa(Superior);
                PresentadorCapaSuperior.ActualizarFotograma(Superior);
            }
            if (Inferior != null) {
                capaInferior = new Capa(Inferior);
                PresentadorCapaInferior.ActualizarFotograma(Inferior);
            }
        }//</editor-fold>

        void IntercambiarCapas() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            ModificarCapas(capaInferior.imagenDibujar, capaSuperior.imagenDibujar);
            centrarCapas();
            ActualizarEscenario();
        }//</editor-fold>

        void centrarCapas() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            capaSuperior.Centrar();
            capaInferior.Centrar();
            ActualizarEscenario();
        }//</editor-fold>

        void AjustarCapasAlEscenario() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            capaSuperior.AjustarAlEscenario();
            capaInferior.AjustarAlEscenario();
            centrarCapas();
        }//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Herramientas para el movimiento con el mouse »">
        void BuscarActivación() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            capaSuperior.BuscarActivación();
            if (capaSuperior.mover) {
                return;
            }
            capaInferior.BuscarActivación();
        }//</editor-fold>

        void Liberar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            capaSuperior.mover = false;
            capaInferior.mover = false;
        }//</editor-fold>

        void Arrastrar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            capaSuperior.Arrastrar();
            capaInferior.Arrastrar();
            ActualizarEscenario();
        }//</editor-fold>
        //</editor-fold>

        BufferedImage fotograma() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
            BufferedImage retorno = Dupla.convBufferedImage(PresentadorEscenario);
            Graphics2D g = retorno.createGraphics();
            //<editor-fold defaultstate="collapsed" desc="Instrucciones de dibujado">
            capaInferior.Dibujar(g);
            //<editor-fold defaultstate="collapsed" desc="Modificación del composite">
            if (modoFusión != null) {
                g.setComposite(modoFusión);
            }
            //</editor-fold>
            capaSuperior.Dibujar(g);
            //</editor-fold>
            return retorno;
        }//</editor-fold>

        class Capa {//<editor-fold defaultstate="collapsed" desc="Código de la clase  »">

            //<editor-fold defaultstate="collapsed" desc="Variables »">
            //<editor-fold defaultstate="collapsed" desc="Para la interacción con el Mouse ●">
            boolean mover = false;
            Dupla XY;
            Rectangle2D zonaActividad;
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Para el dibujado ●">
            BufferedImage imagenEntrada;
            BufferedImage imagenDibujar;
            AffineTransform at;
            //</editor-fold>
            //</editor-fold>

            public Capa(BufferedImage imagen) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
                imagenEntrada = imagen;
                AjustarAlEscenario();
            }//</editor-fold>

            void AjustarAlEscenario() {
                {//<editor-fold defaultstate="collapsed" desc="Ajuste de redimensión para el escenarios">
                    int Lado = Math.min(PresentadorEscenario.getWidth(), PresentadorEscenario.getHeight()) - 20;
                    if (Lado < 1) {
                        Lado = 1;
                    }
                    imagenDibujar = Filtros_Lineales.Escalar(imagenEntrada, Lado, -1);
                    if (imagenDibujar.getHeight() > Lado) {
                        imagenDibujar = Filtros_Lineales.Escalar(imagenEntrada, -1, Lado);
                    }
                }//</editor-fold>
                zonaActividad = new Rectangle.Double(0, 0, imagenDibujar.getWidth(), imagenDibujar.getHeight());
                at = AffineTransform.getTranslateInstance(zonaActividad.getX(), zonaActividad.getY());
            }

            public void Centrar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
                Dupla p = Dupla.Alinear(PresentadorEscenario, imagenDibujar, Dupla.MEDIO, Dupla.MEDIO);
                ModificarPosición(p.X, p.Y);
            }//</editor-fold>

            public void Dibujar(Graphics2D g) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
                g.drawImage(imagenDibujar, at, null);
                g.setComposite(AlphaComposite.SrcOver);
                g.setColor(Color.WHITE);
                g.draw(zonaActividad);
            }//</editor-fold>

            public void BuscarActivación() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
                if (zonaActividad.contains(PresentadorEscenario.PosiciónCursorEnFotograma().convPoint2D())) {
                    mover = true;
                }
                XY = PresentadorEscenario.PosiciónCursorEnFotograma().Sustraer(zonaActividad);
            }//</editor-fold>

            public void Arrastrar() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
                if (!mover) {
                    return;
                }
                Dupla pos = PresentadorEscenario.PosiciónCursorEnFotograma().Sustraer(XY);
                ModificarPosición(pos.X, pos.Y);
            }//</editor-fold>

            public void ModificarPosición(double x, double y) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
                zonaActividad.setRect(x, y, zonaActividad.getWidth(), zonaActividad.getHeight());
                at = AffineTransform.getTranslateInstance(zonaActividad.getX(), zonaActividad.getY());
            }//</editor-fold>
        }//</editor-fold>
    }//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Código generado para la GUI con ayuda de Netbeans">
    //<editor-fold defaultstate="collapsed" desc="Inicialización de las variables de la GUI">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new Presentador();
        jLabel3 = new Presentador();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new Presentador();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel5 = new Presentador();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new Presentador();
        jSlider1 = new javax.swing.JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Matriz de combinación"));
        jPanel2.setLayout(new java.awt.GridLayout(6, 3));

        jTextField1.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("1");
        jTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField1);

        jTextField2.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField2);

        jTextField3.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField3);

        jTextField4.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField4);

        jTextField5.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setText("1");
        jTextField5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField5);

        jTextField6.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField6);

        jTextField7.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField7);

        jTextField8.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField8);

        jTextField9.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField9.setText("1");
        jTextField9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField9);

        jTextField10.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField10.setText("1");
        jTextField10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField10);

        jTextField11.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField11);

        jTextField12.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField12);

        jTextField13.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField13);

        jTextField14.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField14.setText("1");
        jTextField14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField14);

        jTextField15.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField15);

        jTextField16.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField16.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField16);

        jTextField17.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField17);

        jTextField18.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jTextField18.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField18.setText("1");
        jTextField18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        jPanel2.add(jTextField18);

        jPanel3.setLayout(new java.awt.GridLayout(2, 1));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Capa superior");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setBorder(null);
        jPanel3.add(jLabel2);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Capa Inferior");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel3.setBorder(null);
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel3MouseReleased(evt);
            }
        });
        jPanel3.add(jLabel3);

        jButton1.setBackground(new java.awt.Color(151, 176, 177));
        jButton1.setText("Cambiar imagenes");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(151, 176, 177));
        jButton2.setText("Randomizar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(151, 176, 177));
        jButton3.setText("C");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(151, 176, 177));
        jButton4.setText("Limpiar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)))))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 102, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Presentador Imagenes");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jLabel1MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jLabel1MouseMoved(evt);
            }
        });
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel1MouseReleased(evt);
            }
        });
        jLabel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jLabel1ComponentResized(evt);
            }
        });

        jPanel6.setLayout(new java.awt.GridLayout(3, 1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Adición", "Binarizar", "Dentro", "Mascara", "Borrado", "Borrado y aplicado", "Borrado fuerte", "Borrado rectángular", "Distancia", "Intersección", "Disolver", "Unión por bits", "Intersección por bits", "XOR por bits", "Sustracción & masc. 16-bits", "Sustracción & masc. 16-bits lat.", "const. opacidad", "const. opacidad mascara", "const. opacidad salida", "const. opacidad intersección", "const. opacidad escalonar", "const. opacidad rectificación lineal" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createTitledBorder("Discriminación Alpha"));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel6.add(jComboBox1);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Extremos", "Extremos abs", "Circular", "Circular abs", "Reflejo", "Reflejo Inv", "Mascara de bits", "Inserción a extremos por salida", "Inserción circular por salida", "Inserción reflejo por salida", "Nulidad por salida", "Sin definir" }));
        jComboBox2.setBorder(javax.swing.BorderFactory.createTitledBorder("Ajuste RGB"));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jPanel6.add(jComboBox2);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Riesgo de salida");
        jPanel6.add(jLabel5);

        jList2.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Lista de Modos de fusión" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

        jPanel7.setLayout(new java.awt.GridLayout(2, 0));

        jLabel4.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Patrón de fondo");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel4MouseReleased(evt);
            }
        });
        jPanel7.add(jLabel4);

        jSlider1.setMaximum(300);
        jSlider1.setValue(jSlider1.getMaximum());
        jSlider1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Opacidad 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        jPanel7.add(jSlider1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //</editor-fold>

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        String s = JOptionPane.showInputDialog(
                null,
                "Inserte una ruta para la carga de la imagen 1",
                "Lectura de la ruta",
                JOptionPane.PLAIN_MESSAGE
        );
        String s2 = JOptionPane.showInputDialog(
                null,
                "Inserte una ruta para la carga de la imagen 2",
                "Lectura de la ruta",
                JOptionPane.PLAIN_MESSAGE
        );
        BufferedImage Imagen1 = LectoEscrituraArchivos.cargar_imagen(s);
        BufferedImage Imagen2 = LectoEscrituraArchivos.cargar_imagen(s2);
        Escenario.ModificarCapas(Imagen1, Imagen2);
        Escenario.centrarCapas();
        //</editor-fold>
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        SincronizarModoFusiónConGUI();
        jSlider1.setBorder(
                BorderFactory.createTitledBorder(null,
                        "Opacidad " + Matemática.Truncar(modoFusión.Opacidad * 100, 2) + "%",
                        TitledBorder.LEFT, TitledBorder.ABOVE_TOP
                )
        );
        //</editor-fold>
    }//GEN-LAST:event_jSlider1StateChanged

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        Escenario.BuscarActivación();
    }//GEN-LAST:event_jLabel1MousePressed

    private void jLabel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseMoved

    }//GEN-LAST:event_jLabel1MouseMoved

    private void jLabel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseDragged
        Escenario.Arrastrar();
    }//GEN-LAST:event_jLabel1MouseDragged

    private void jLabel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseReleased
        Escenario.Liberar();
    }//GEN-LAST:event_jLabel1MouseReleased

    private void jLabel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jLabel1ComponentResized
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            Escenario.AjustarCapasAlEscenario();
        } catch (Exception e) {
        }
        //</editor-fold>
    }//GEN-LAST:event_jLabel1ComponentResized

    private void jList2ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList2ValueChanged
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        String s = jList2.getSelectedValue();
        if (s.startsWith("/-/")) {
            return;
        }
        byte Modo = (byte) ListaID.ObtenerID(s);
        if (Modo == MODOFUSIÓN_CL_GENERAL) {
            modoFusión = ObtenerModoFusión(Modo, ObtenerMatrizCombinaciónLineal());
            jButton2.doClick();
            EdiciónJtext(true);
        } else {
            modoFusión = ObtenerModoFusión(Modo);
            EdiciónJtext(false);
        }
        SincronizarModoFusiónConGUI();
        SincronizarGUIConModoFusión();
        //</editor-fold>
    }//GEN-LAST:event_jList2ValueChanged

    private void jLabel3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseReleased
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            Escenario.IntercambiarCapas();
        } catch (Exception e) {
        }
        //</editor-fold>
    }//GEN-LAST:event_jLabel3MouseReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (JTextField[] textfield : textfields) {
            for (JTextField jtxt : textfield) {
                if (!jtxt.isEditable()) {
                    continue;
                }
                int a = -1;
                int b = 1;
                float f = (float) Matemática.Truncar(((b - a) * Math.random() + a), 4, false);
                jtxt.setText(f + "");
            }
        }
        ActualizarModoFusiónCombinaciónLineal();
        //</editor-fold>
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        SincronizarModoFusiónConGUI();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        SincronizarModoFusiónConGUI();
    }//GEN-LAST:event_jComboBox2ActionPerformed

    int Swiche1 = 0;

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        switch (Swiche1 = (Swiche1 + 1) % 4) {
            case 0:
                Escenario.ModificarCapas(BibliotecaArchivos.Imagenes.Test.IMG1(),
                        BibliotecaArchivos.Imagenes.Test.IMG2()
                );
                break;
            case 1:
                Escenario.ModificarCapas(BibliotecaArchivos.Imagenes.Test.PNG_2(),
                        BibliotecaArchivos.Imagenes.Test.Wallpaper_3()
                );
                break;
            case 2:
                Escenario.ModificarCapas(BibliotecaArchivos.Imagenes.Test.IMG3(),
                        BibliotecaArchivos.Imagenes.Test.IMG4()
                );
                break;
            case 3:
                Escenario.ModificarCapas(BibliotecaArchivos.Imagenes.Test.Wallpaper_1(),
                        BibliotecaArchivos.Imagenes.Test.Wallpaper_2()
                );
                break;
        }
        Escenario.centrarCapas();
        //</editor-fold>
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        for (JTextField[] textfield : textfields) {
            for (JTextField jTextField : textfield) {
                jTextField.setText("");
            }
        }
        ActualizarModoFusiónCombinaciónLineal();
        //</editor-fold>
    }//GEN-LAST:event_jButton4ActionPerformed

    int Swiche2 = 0;

    private void jLabel4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseReleased
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Paint c = null;
        BufferedImage img = null;
        switch ((Swiche1++) % 5) {
            case 0:
                img = BibliotecaArchivos.Imagenes.Texturas.PatrónCuadricula(
                        Color.LIGHT_GRAY, Color.WHITE, 26, 26
                );
                break;
            case 1:
                img = BibliotecaArchivos.Imagenes.Texturas.PatrónCuadricula(
                        Color.DARK_GRAY, Color.BLACK, 26, 26
                );
                break;
            case 3:
                c = Color.WHITE;
                break;
            case 4:
                c = Color.BLACK;
                break;
        }
        PresentadorEscenario.ImagenFondo(img);
        PresentadorEscenario.ColorFondo(c);
        PresentadorEscenario.ActualizarFotograma();

        PresentadorPatrónFondo.ImagenFondo(img);
        PresentadorPatrónFondo.ColorFondo(c);
        PresentadorPatrónFondo.ActualizarFotograma();
        //</editor-fold>
    }//GEN-LAST:event_jLabel4MouseReleased

    //<editor-fold defaultstate="collapsed" desc="Declaración de variables de la GUI">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
    //</editor-fold>

}
