package HerramientaDeImagen.ModoFusión;

//<editor-fold defaultstate="collapsed" desc="Importe de librerias ●">
import HerramientaArchivos.ArrastrarArchivos.ArrastrarArchivos;
import HerramientaArchivos.BibliotecaArchivos;
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientaDeImagen.Filtros_Lineales;
import HerramientaDeImagen.ModoFusión.ModoPintura.BaseModoPintura;
import HerramientasGUI.Maquillaje_SwingGUI;
import HerramientasGUI.Presentador;
import HerramientasGUI.Personalizador_JList;
import HerramientasMatemáticas.Dupla;
import HerramientasMatemáticas.Matemática;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
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

public class GUI_PruebaModoPintura extends GUI_Aux {

    BaseModoPintura ModoPintura;

    public static final byte MODOPINTURA_S_NORMAL = -128;
    public static final byte MODOPINTURA_S_INVERTIR = -127;
    public static final byte MODOPINTURA_S_RUIDO_GRIS = -99;
    public static final byte MODOPINTURA_S_M_TL_APAGAR_CANALES = -98;
    public static final byte MODOPINTURA_S_BINARIZAR = -44;

    public static final byte MODOPINTURA_S_G_PROMEDIO_RGB = -116;
    public static final byte MODOPINTURA_S_G_CANAL_ROJO = -115;
    public static final byte MODOPINTURA_S_G_CANAL_VERDE = -114;
    public static final byte MODOPINTURA_S_G_CANAL_AZUL = -113;
    public static final byte MODOPINTURA_S_G_CANAL_MAGENTA = -112;
    public static final byte MODOPINTURA_S_G_CANAL_AMARILLO = -111;
    public static final byte MODOPINTURA_S_G_CANAL_CYAN = -110;
    public static final byte MODOPINTURA_S_G_HSB_BRILLO = -109;
    public static final byte MODOPINTURA_S_G_HSL_LUMINOSIDAD = -108;
    public static final byte MODOPINTURA_S_G_HSB_SATURACIÓN = -107;
    public static final byte MODOPINTURA_S_G_HSL_SATURACIÓN = -106;
    public static final byte MODOPINTURA_S_G_TONO = -104;
    public static final byte MODOPINTURA_S_G_HSB_BRILLO_INVERTIDO = -103;
    public static final byte MODOPINTURA_S_G_HSL_LUMINOSIDAD_INVERTIDO = -102;
    public static final byte MODOPINTURA_S_G_HSB_BRILLO_BINARIZADO = -101;
    public static final byte MODOPINTURA_S_G_HSL_LUMINOSIDAD_BINARIZADO = -100;
    public static final byte MODOPINTURA_S_G_DISTANCIA = -97;
    public static final byte MODOPINTURA_S_G_DISTANCIA_INVERTIDO = -30;

    public static final byte MODOPINTURA_S_A_PROMEDIO_RGB = -60;
    public static final byte MODOPINTURA_S_A_CANAL_ROJO = -59;
    public static final byte MODOPINTURA_S_A_CANAL_VERDE = -58;
    public static final byte MODOPINTURA_S_A_CANAL_AZUL = -57;
    public static final byte MODOPINTURA_S_A_CANAL_MAGENTA = -56;
    public static final byte MODOPINTURA_S_A_CANAL_AMARILLO = -55;
    public static final byte MODOPINTURA_S_A_CANAL_CYAN = -54;
    public static final byte MODOPINTURA_S_A_HSB_BRILLO = -53;
    public static final byte MODOPINTURA_S_A_HSL_LUMINOSIDAD = -52;
    public static final byte MODOPINTURA_S_A_HSB_SATURACIÓN = -51;
    public static final byte MODOPINTURA_S_A_HSL_SATURACIÓN = -50;
    public static final byte MODOPINTURA_S_A_TONO = -49;
    public static final byte MODOPINTURA_S_A_HSB_BRILLO_INVERTIDO = -48;
    public static final byte MODOPINTURA_S_A_HSL_LUMINOSIDAD_INVERTIDO = -47;
    public static final byte MODOPINTURA_S_A_HSB_BRILLO_BINARIZADO = -46;
    public static final byte MODOPINTURA_S_A_HSL_LUMINOSIDAD_BINARIZADO = -45;
    public static final byte MODOPINTURA_S_A_DISTANCIA = -34;
    public static final byte MODOPINTURA_S_A_DISTANCIA_INVERTIDO = -31;

    public static final byte MODOPINTURA_S_M_TL_RGB_GNRL = -42;
    public static final byte MODOPINTURA_S_M_TL_HSB_GNRL = -41;
    public static final byte MODOPINTURA_S_M_TL_HSL_GNRL = -40;

    public static final byte MODOPINTURA_S_M_TL_R = -25;
    public static final byte MODOPINTURA_S_M_TL_G = -24;
    public static final byte MODOPINTURA_S_M_TL_B = -23;
    public static final byte MODOPINTURA_S_M_TL_RG = -22;
    public static final byte MODOPINTURA_S_M_TL_RB = -21;
    public static final byte MODOPINTURA_S_M_TL_GB = -20;
    public static final byte MODOPINTURA_S_M_TL_RBG = -19;
    public static final byte MODOPINTURA_S_M_TL_GRB = -18;
    public static final byte MODOPINTURA_S_M_TL_GBR = -17;
    public static final byte MODOPINTURA_S_M_TL_BGR = -16;
    public static final byte MODOPINTURA_S_M_TL_BRG = -15;

    public static final byte MODOPINTURA_S_M_CL_RGBHSB = -28;
    public static final byte MODOPINTURA_S_M_CL_RGBHSL = -27;
    public static final byte MODOPINTURA_S_M_CL_HSBRGB = -26;
    public static final byte MODOPINTURA_S_M_CL_HSBHSL = -39;
    public static final byte MODOPINTURA_S_M_CL_HSLRGB = -38;
    public static final byte MODOPINTURA_S_M_CL_HSLHSB = -37;
    public static final byte MODOPINTURA_S_M_CL_TEMPERATURA = -36;
    public static final byte MODOPINTURA_S_M_CL_INSERTAR_RGB_EN_HSB = -35;
    public static final byte MODOPINTURA_S_M_CL_INSERTAR_RGB_EN_HSL = -29;
    public static final byte MODOPINTURA_S_M_CL_INSERTAR_HSB_EN_HSL = -14;
    public static final byte MODOPINTURA_S_M_CL_INSERTAR_HSL_EN_HSB = -13;

    public static final byte MODOPINTURA_VA_U_DISCRETIZAR = -43;
    public static final byte MODOPINTURA_VA_U_MONOCROMATIZAR = -96;
    public static final byte MODOPINTURA_VA_U_MONOSATURAR_HSL = -95;
    public static final byte MODOPINTURA_VA_U_MONOSATURAR_HSB = -94;
    public static final byte MODOPINTURA_VA_U_MONOBRILLAR_HSL = -93;
    public static final byte MODOPINTURA_VA_U_MONOBRILLAR_HSB = -92;
    public static final byte MODOPINTURA_VA_U_MONOCANALIZAR_ROJO = -91;
    public static final byte MODOPINTURA_VA_U_MONOCANALIZAR_VERDE = -90;
    public static final byte MODOPINTURA_VA_U_MONOCANALIZAR_AZUL = -89;
    public static final byte MODOPINTURA_VA_U_MONOCANALIZAR_CYAN = -88;
    public static final byte MODOPINTURA_VA_U_MONOCANALIZAR_MAGENTA = -87;
    public static final byte MODOPINTURA_VA_U_MONOCANALIZAR_AMARILLO = -86;
    public static final byte MODOPINTURA_VA_U_MONOCANALIZAR_NEGRO = -85;

    public static final byte MODOPINTURA_VA_D_MONOCOLORIZAR_HSB = -84;
    public static final byte MODOPINTURA_VA_D_MONOCOLORIZAR_HSL = -83;
    public static final byte MODOPINTURA_VA_D_CONSERVAR_SAT_COLOR_TONO = -82;
    public static final byte MODOPINTURA_VA_D_ELIMINAR_SAT_COLOR_TONO = -81;

    public static final byte MODOPINTURA_VA_T_EQUILIBRAR_HSB = -79;
    public static final byte MODOPINTURA_VA_T_EQUILIBRAR_HSL = -78;
    public static final byte MODOPINTURA_VA_T_EQUILIBRAR_BINARIZANDO_HSL = -77;
    public static final byte MODOPINTURA_VA_T_EQUILIBRAR_BINARIZANDO_HSB = -76;
    public static final byte MODOPINTURA_VA_T_EXTENDER_HSB = -75;
    public static final byte MODOPINTURA_VA_T_EXTENDER_HSL = -74;
    public static final byte MODOPINTURA_VA_T_EXTENDER_BINARIZANDO_HSB = -73;
    public static final byte MODOPINTURA_VA_T_EQUILIBRAR_RGB = -71;
    public static final byte MODOPINTURA_VA_T_EXTENDER_RGB = -70;
    public static final byte MODOPINTURA_VA_T_EQUILIBRAR_BINARIZANDO_RGB = -69;
    public static final byte MODOPINTURA_VA_T_EXTENDER_BINARIZANDO_RGB = -68;
    public static final byte MODOPINTURA_VA_T_CAMBIAR_TONO_COLOR_TONO_ESP = -80;
    public static final byte MODOPINTURA_VA_T_CAMBIAR_SAT_HSB_COLOR_TONO_ESP = -32;
    public static final byte MODOPINTURA_VA_T_CAMBIAR_BRILLO_HSB_COLOR_TONO_ESP = -63;
    public static final byte MODOPINTURA_VA_T_CAMBIAR_SAT_HSL_COLOR_TONO_ESP = -62;
    public static final byte MODOPINTURA_VA_T_CAMBIAR_BRILLO_HSL_COLOR_TONO_ESP = -61;

    public static final byte MODOPINTURA_VA_C_EQUILIBRAR_CMYK = -67;
    public static final byte MODOPINTURA_VA_C_EXTENDER_CMYK = -66;
    public static final byte MODOPINTURA_VA_C_EQUILIBRAR_BINARIZANDO_CMYK = -65;
    public static final byte MODOPINTURA_VA_C_EXTENDER_BINARIZANDO_CMYK = -64;

    public static final byte MODOPINTURA_j = -12;
    public static final byte MODOPINTURA_k = -11;
    public static final byte MODOPINTURA_l = -10;

    public static final byte MODOPINTURA_PUNTILLISMO = -126;
    public static final byte MODOPINTURA_PUNTILLISMO_FUERTE = -125;
    public static final byte MODOPINTURA_PUNTILLISMO_HORIZONTAL = -124;
    public static final byte MODOPINTURA_PUNTILLISMO_HORIZONTAL_FUERTE = -123;
    public static final byte MODOPINTURA_PUNTILLISMO_VERTICAL = -122;
    public static final byte MODOPINTURA_PUNTILLISMO_VERTICAL_FUERTE = -121;
    public static final byte MODOPINTURA_PUNTILLISMO_DIAGONAL_DERECHA = -120;
    public static final byte MODOPINTURA_PUNTILLISMO_DIAGONAL_DERECHA_FUERTE = -119;
    public static final byte MODOPINTURA_PUNTILLISMO_DIAGONAL_IZQUIERDA = -118;
    public static final byte MODOPINTURA_PUNTILLISMO_DIAGONAL_IZQUIERDA_FUERTE = -117;

    public static final byte MODOPINTURA_UMBRAL = MODOPINTURA_VA_T_EXTENDER_BINARIZANDO_RGB;
    public static final byte MODOPINTURA_EXAGERAR_BRILLOS = MODOPINTURA_S_M_CL_INSERTAR_HSB_EN_HSL;

    JTextField[][] textfields;

    boolean cambiandoModo = true;

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
        new GUI_PruebaModoPintura();
    }

    public GUI_PruebaModoPintura() {//<editor-fold defaultstate="collapsed" desc="Implementación de código del constructor »">

        //<editor-fold defaultstate="collapsed" desc="inicialización de la GUI">
        initComponents();
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        //</editor-fold>

        jLabel5.setText("");

        textfields = ObtenerMatrizGUI_CL();

        ActualizarJtext();

        ListenersJText();
        ListenersArrastreDeArchivos();

        InicializaciónDeLosPresentadores();

        Escenario = new Fotograma();
        Escenario.centrarCapas();

        LlenarTablaDeModeloPintura(ListaID);

        jList2.setCellRenderer(new Personalizador_JList());
        jList2.setBorder(new EmptyBorder(0, 10, 0, 0));
        Maquillaje_SwingGUI.AsignarLista_JList(jList2, ObtenerKeysTabla());

        ModoFusión = GUI_PruebaModoPintura.this.ConsultarModoPintura(
                MODOPINTURA_S_NORMAL,
                ObtenerMatrizCombinaciónLineal()
        );
        ModoPintura = (BaseModoPintura) ModoFusión;

        SincronizarGUIConModeloPintura();
        setVisible(true);
        cambiandoModo = false;
    }//</editor-fold>

    public static void LlenarTablaDeModeloPintura(ListaID ListaID) {
        ListaID.add("/-//l//cg/Simple");
        ListaID.add("S: Normal", MODOPINTURA_S_NORMAL);
        ListaID.add("S: Invertir", MODOPINTURA_S_INVERTIR);
        ListaID.add("S: Binarizar", MODOPINTURA_S_BINARIZAR);
        ListaID.add("S: Ruido gris", MODOPINTURA_S_RUIDO_GRIS);

        ListaID.add("/-//l//cg//t/Simple (Grises por canal)");
        ListaID.add("/t/S-G: Promedio RGB", MODOPINTURA_S_G_PROMEDIO_RGB);
        ListaID.add("/t/S-G: Distancia", MODOPINTURA_S_G_DISTANCIA);
        ListaID.add("/t/S-G: Distancia Invertido", MODOPINTURA_S_G_DISTANCIA_INVERTIDO);
        ListaID.add("/t/S-G: Canal rojo", MODOPINTURA_S_G_CANAL_ROJO);
        ListaID.add("/t/S-G: Canal verde", MODOPINTURA_S_G_CANAL_VERDE);
        ListaID.add("/t/S-G: Canal azul", MODOPINTURA_S_G_CANAL_AZUL);
        ListaID.add("/t/S-G: Canal magenta", MODOPINTURA_S_G_CANAL_MAGENTA);
        ListaID.add("/t/S-G: Canal amarillo", MODOPINTURA_S_G_CANAL_AMARILLO);
        ListaID.add("/t/S-G: Canal cyan", MODOPINTURA_S_G_CANAL_CYAN);
        ListaID.add("/t/S-G: Tono", MODOPINTURA_S_G_TONO);
        ListaID.add("/-//l//cg//t//t/Simple (Grises HSB y HSL)");
        ListaID.add("/t//t/S-G-HSB: brillo", MODOPINTURA_S_G_HSB_BRILLO);
        ListaID.add("/t//t/S-G-HSB: brillo Binarizado", MODOPINTURA_S_G_HSB_BRILLO_BINARIZADO);
        ListaID.add("/t//t/S-G-HSB: brillo Invertido", MODOPINTURA_S_G_HSB_BRILLO_INVERTIDO);
        ListaID.add("/t//t/S-G-HSB: saturación", MODOPINTURA_S_G_HSB_SATURACIÓN);
        ListaID.add("/t//t/S-G-HSL: brillo", MODOPINTURA_S_G_HSL_LUMINOSIDAD);
        ListaID.add("/t//t/S-G-HSL: brillo Binarizado", MODOPINTURA_S_G_HSL_LUMINOSIDAD_BINARIZADO);
        ListaID.add("/t//t/S-G-HSL: brillo Invertido", MODOPINTURA_S_G_HSL_LUMINOSIDAD_INVERTIDO);
        ListaID.add("/t//t/S-G-HSL: saturación", MODOPINTURA_S_G_HSL_SATURACIÓN);

        ListaID.add("/-//l//cg//t/Simple (Alpha por canal)");
        ListaID.add("/t/S-A: Promedio RGB", MODOPINTURA_S_A_PROMEDIO_RGB);
        ListaID.add("/t/S-A: Distancia", MODOPINTURA_S_A_DISTANCIA);
        ListaID.add("/t/S-A: Distancia Invertido", MODOPINTURA_S_A_DISTANCIA_INVERTIDO);
        ListaID.add("/t/S-A: Canal rojo", MODOPINTURA_S_A_CANAL_ROJO);
        ListaID.add("/t/S-A: Canal verde", MODOPINTURA_S_A_CANAL_VERDE);
        ListaID.add("/t/S-A: Canal azul", MODOPINTURA_S_A_CANAL_AZUL);
        ListaID.add("/t/S-A: Canal magenta", MODOPINTURA_S_A_CANAL_MAGENTA);
        ListaID.add("/t/S-A: Canal amarillo", MODOPINTURA_S_A_CANAL_AMARILLO);
        ListaID.add("/t/S-A: Canal cyan", MODOPINTURA_S_A_CANAL_CYAN);
        ListaID.add("/t/S-A: Tono", MODOPINTURA_S_A_TONO);
        ListaID.add("/-//l//cg//t//t/Simple (Alphas HSB y HSL)");
        ListaID.add("/t//t/S-A-HSB: brillo", MODOPINTURA_S_A_HSB_BRILLO);
        ListaID.add("/t//t/S-A-HSB: brillo Binarizado", MODOPINTURA_S_A_HSB_BRILLO_BINARIZADO);
        ListaID.add("/t//t/S-A-HSB: brillo Invertido", MODOPINTURA_S_A_HSB_BRILLO_INVERTIDO);
        ListaID.add("/t//t/S-A-HSB: saturación", MODOPINTURA_S_A_HSB_SATURACIÓN);
        ListaID.add("/t//t/S-A-HSL: brillo", MODOPINTURA_S_A_HSL_LUMINOSIDAD);
        ListaID.add("/t//t/S-A-HSL: brillo Binarizado", MODOPINTURA_S_A_HSL_LUMINOSIDAD_BINARIZADO);
        ListaID.add("/t//t/S-A-HSL: brillo Invertido", MODOPINTURA_S_A_HSL_LUMINOSIDAD_INVERTIDO);
        ListaID.add("/t//t/S-A-HSL: saturación", MODOPINTURA_S_A_HSL_SATURACIÓN);

        ListaID.add("/-//l//cg//t/Simple (Matriz de Transformación Lineal (TL))");
        ListaID.add("/-//l//cb//t//t/TL Generales");
        ListaID.add("/t//t/S-M-TL: RGB", MODOPINTURA_S_M_TL_RGB_GNRL);
        ListaID.add("/t//t/S-M-TL: HSB", MODOPINTURA_S_M_TL_HSB_GNRL);
        ListaID.add("/t//t/S-M-TL: HSL", MODOPINTURA_S_M_TL_HSL_GNRL);
        ListaID.add("/-//l//cg//t//t//t/TL Particulares");
        ListaID.add("/t//t//t/S-M-TL: Apagar canales", MODOPINTURA_S_M_TL_APAGAR_CANALES);
        ListaID.add("/t//t//t/S-M-TL: R", MODOPINTURA_S_M_TL_R);
        ListaID.add("/t//t//t/S-M-TL: G", MODOPINTURA_S_M_TL_G);
        ListaID.add("/t//t//t/S-M-TL: B", MODOPINTURA_S_M_TL_B);
        ListaID.add("/t//t//t/S-M-TL: RG", MODOPINTURA_S_M_TL_RG);
        ListaID.add("/t//t//t/S-M-TL: RB", MODOPINTURA_S_M_TL_RB);
        ListaID.add("/t//t//t/S-M-TL: GB", MODOPINTURA_S_M_TL_GB);
        ListaID.add("/t//t//t/S-M-TL: RBG", MODOPINTURA_S_M_TL_RBG);
        ListaID.add("/t//t//t/S-M-TL: GRB", MODOPINTURA_S_M_TL_GRB);
        ListaID.add("/t//t//t/S-M-TL: GBR", MODOPINTURA_S_M_TL_GBR);
        ListaID.add("/t//t//t/S-M-TL: BRG", MODOPINTURA_S_M_TL_BRG);
        ListaID.add("/t//t//t/S-M-TL: BGR", MODOPINTURA_S_M_TL_BGR);

        ListaID.add("/-//l//cg//t/Simple (Matriz de combinación Lineal (CL))");
        ListaID.add("/-//l//cb//t//t//t/CL Generales");
        ListaID.add("/t//t//t/S-M-CL: RGB-HSB", MODOPINTURA_S_M_CL_RGBHSB);
        ListaID.add("/t//t//t/S-M-CL: RGB-HSL", MODOPINTURA_S_M_CL_RGBHSL);
        ListaID.add("/t//t//t/S-M-CL: HSB-RGB", MODOPINTURA_S_M_CL_HSBRGB);
        ListaID.add("/t//t//t/S-M-CL: HSB-HSL", MODOPINTURA_S_M_CL_HSBHSL);
        ListaID.add("/t//t//t/S-M-CL: HSL-RGB", MODOPINTURA_S_M_CL_HSLRGB);
        ListaID.add("/t//t//t/S-M-CL: HSL-HSB", MODOPINTURA_S_M_CL_HSLHSB);
        ListaID.add("/-//l//cg//t//t//t/CL Particulares");
        ListaID.add("/t//t//t/S-M-CL: Temperatura", MODOPINTURA_S_M_CL_TEMPERATURA);
        ListaID.add("/t//t//t/S-M-CL: Insertar RGB en HSB", MODOPINTURA_S_M_CL_INSERTAR_RGB_EN_HSB);
        ListaID.add("/t//t//t/S-M-CL: Insertar RGB en HSL", MODOPINTURA_S_M_CL_INSERTAR_RGB_EN_HSL);
        ListaID.add("/t//t//t/S-M-CL: Insertar HSB en HSL (Exagerar brillos)", MODOPINTURA_S_M_CL_INSERTAR_HSB_EN_HSL);
        ListaID.add("/t//t//t/S-M-CL: Insertar HSL en HSB", MODOPINTURA_S_M_CL_INSERTAR_HSL_EN_HSB);

        ListaID.add("/-//l//cg/Con variable de aplicación");
        ListaID.add("VA-U: Discretizar", MODOPINTURA_VA_U_DISCRETIZAR);
        ListaID.add("VA-U: Monocromatizar", MODOPINTURA_VA_U_MONOCROMATIZAR);
        ListaID.add("VA-U: Monosaturar HSL", MODOPINTURA_VA_U_MONOSATURAR_HSL);
        ListaID.add("VA-U: Monosaturar HSB", MODOPINTURA_VA_U_MONOSATURAR_HSB);
        ListaID.add("VA-U: Monobrillar HSL", MODOPINTURA_VA_U_MONOBRILLAR_HSL);
        ListaID.add("VA-U: Monobrillar HSB", MODOPINTURA_VA_U_MONOBRILLAR_HSB);
        ListaID.add("VA-U: Monocanalizar rojo", MODOPINTURA_VA_U_MONOCANALIZAR_ROJO);
        ListaID.add("VA-U: Monocanalizar verde", MODOPINTURA_VA_U_MONOCANALIZAR_VERDE);
        ListaID.add("VA-U: Monocanalizar azul", MODOPINTURA_VA_U_MONOCANALIZAR_AZUL);
        ListaID.add("VA-U: Monocanalizar cyan", MODOPINTURA_VA_U_MONOCANALIZAR_CYAN);
        ListaID.add("VA-U: Monocanalizar magenta", MODOPINTURA_VA_U_MONOCANALIZAR_MAGENTA);
        ListaID.add("VA-U: Monocanalizar amarillo", MODOPINTURA_VA_U_MONOCANALIZAR_AMARILLO);
        ListaID.add("VA-U: Monocanalizar negro", MODOPINTURA_VA_U_MONOCANALIZAR_NEGRO);
        ListaID.add("/-//l//cg//t/Con doble variable de aplicación");
        ListaID.add("/t/VA-D: Monocolorizar HSB", MODOPINTURA_VA_D_MONOCOLORIZAR_HSB);
        ListaID.add("/t/VA-D: Monocolorizar HSL", MODOPINTURA_VA_D_MONOCOLORIZAR_HSL);
        ListaID.add("/t/VA-D: Conservar saturación en color con tono especifico", MODOPINTURA_VA_D_CONSERVAR_SAT_COLOR_TONO);
        ListaID.add("/t/VA-D: Eliminar saturación en color con tono especifico", MODOPINTURA_VA_D_ELIMINAR_SAT_COLOR_TONO);
        ListaID.add("/-//l//cg//t//t/Con triple variable de aplicación");
        ListaID.add("/t//t/VA-T: Cambiar tono de color en tono especifico", MODOPINTURA_VA_T_CAMBIAR_TONO_COLOR_TONO_ESP);
        ListaID.add("/t//t/VA-T: Cambiar saturación HSB de color en tono especifico", MODOPINTURA_VA_T_CAMBIAR_SAT_HSB_COLOR_TONO_ESP);
        ListaID.add("/t//t/VA-T: Cambiar brillo HSB de color en tono especifico", MODOPINTURA_VA_T_CAMBIAR_BRILLO_HSB_COLOR_TONO_ESP);
        ListaID.add("/t//t/VA-T: Cambiar saturación HSL de color en tono especifico", MODOPINTURA_VA_T_CAMBIAR_SAT_HSL_COLOR_TONO_ESP);
        ListaID.add("/t//t/VA-T: Cambiar brillo HSL de color en tono especifico", MODOPINTURA_VA_T_CAMBIAR_BRILLO_HSL_COLOR_TONO_ESP);
        ListaID.add("/t//t/VA-T: Equilibrar RGB", MODOPINTURA_VA_T_EQUILIBRAR_RGB);
        ListaID.add("/t//t/VA-T: Equilibrar HSB", MODOPINTURA_VA_T_EQUILIBRAR_HSB);
        ListaID.add("/t//t/VA-T: Equilibrar HSL", MODOPINTURA_VA_T_EQUILIBRAR_HSL);
        ListaID.add("/t//t/VA-T: Equilibrar binarizando RGB", MODOPINTURA_VA_T_EQUILIBRAR_BINARIZANDO_RGB);
        ListaID.add("/t//t/VA-T: Equilibrar binarizando HSB", MODOPINTURA_VA_T_EQUILIBRAR_BINARIZANDO_HSB);
        ListaID.add("/t//t/VA-T: Equilibrar binarizando HSL  (Umbral)", MODOPINTURA_VA_T_EQUILIBRAR_BINARIZANDO_HSL);
        ListaID.add("/t//t/VA-T: Extender RGB", MODOPINTURA_VA_T_EXTENDER_RGB);
        ListaID.add("/t//t/VA-T: Extender HSB", MODOPINTURA_VA_T_EXTENDER_HSB);
        ListaID.add("/t//t/VA-T: Extender HSL", MODOPINTURA_VA_T_EXTENDER_HSL);
        ListaID.add("/t//t/VA-T: Extender binarizando RGB", MODOPINTURA_VA_T_EXTENDER_BINARIZANDO_RGB);
        ListaID.add("/t//t/VA-T: Extender binarizando HSB", MODOPINTURA_VA_T_EXTENDER_BINARIZANDO_HSB);
        ListaID.add("/-//l//cg//t//t//t/Con cuadruple variable de aplicación");
        ListaID.add("/t//t//t/VA-C: Equilibrar CMYK", MODOPINTURA_VA_C_EQUILIBRAR_CMYK);
        ListaID.add("/t//t//t/VA-C: Extender CMYK", MODOPINTURA_VA_C_EXTENDER_CMYK);
        ListaID.add("/t//t//t/VA-C: Equilibrar binarizando CMYK", MODOPINTURA_VA_C_EQUILIBRAR_BINARIZANDO_CMYK);
        ListaID.add("/t//t//t/VA-C: Extender binarizando CMYK", MODOPINTURA_VA_C_EQUILIBRAR_BINARIZANDO_CMYK);
        ListaID.add("Puntillismo", MODOPINTURA_PUNTILLISMO);
        ListaID.add("Puntillismo fuerte", MODOPINTURA_PUNTILLISMO_FUERTE);
        ListaID.add("Puntillismo horizontal", MODOPINTURA_PUNTILLISMO_HORIZONTAL);
        ListaID.add("Puntillismo horizontal fuerte", MODOPINTURA_PUNTILLISMO_HORIZONTAL_FUERTE);
        ListaID.add("Puntillismo vertical", MODOPINTURA_PUNTILLISMO_VERTICAL);
        ListaID.add("Puntillismo vertical fuerte", MODOPINTURA_PUNTILLISMO_VERTICAL_FUERTE);
        ListaID.add("Puntillismo diagonal derecha", MODOPINTURA_PUNTILLISMO_DIAGONAL_DERECHA);
        ListaID.add("Puntillismo diagonal derecha fuerte", MODOPINTURA_PUNTILLISMO_DIAGONAL_DERECHA_FUERTE);
        ListaID.add("Puntillismo diagonal izquierda", MODOPINTURA_PUNTILLISMO_DIAGONAL_IZQUIERDA);
        ListaID.add("Puntillismo diagonal izquierda fuerte", MODOPINTURA_PUNTILLISMO_DIAGONAL_IZQUIERDA_FUERTE);
    }

    public static BaseModoPintura ConsultarModoPintura(byte MODOPINTURA, float[][] M) {
        switch (MODOPINTURA) {
            case MODOPINTURA_S_NORMAL:
                return new ModoPintura.Simple.Matriz.CL.InsertarRGBenHSL();
            case MODOPINTURA_S_BINARIZAR:
                return new ModoPintura.Simple.Binarizar();
            case MODOPINTURA_S_INVERTIR:
                return new ModoPintura.Simple.Invertir();
            case MODOPINTURA_S_M_TL_APAGAR_CANALES:
                return new ModoPintura.Simple.Matriz.TL.ApagarCanales_RGB();
            case MODOPINTURA_S_RUIDO_GRIS:
                return new ModoPintura.Simple.RuidoGris();
            case MODOPINTURA_S_M_CL_INSERTAR_RGB_EN_HSB:
                return new ModoPintura.Simple.Matriz.CL.InsertarRGBenHSB();
            case MODOPINTURA_S_M_CL_INSERTAR_RGB_EN_HSL:
                return new ModoPintura.Simple.Matriz.CL.InsertarRGBenHSL();
            case MODOPINTURA_S_M_CL_INSERTAR_HSB_EN_HSL:
                return new ModoPintura.Simple.Matriz.CL.InsertarHSBenHSL();
            case MODOPINTURA_S_M_CL_INSERTAR_HSL_EN_HSB:
                return new ModoPintura.Simple.Matriz.CL.InsertarHSLenHSB();
            case MODOPINTURA_S_M_CL_TEMPERATURA:
                return new ModoPintura.Simple.Matriz.CL.Temperatura();
            case MODOPINTURA_S_M_CL_RGBHSB:
                return new ModoPintura.Simple.Matriz.CL.RGBHSB(M);
            case MODOPINTURA_S_M_CL_RGBHSL:
                return new ModoPintura.Simple.Matriz.CL.RGBHSL(M);
            case MODOPINTURA_S_M_CL_HSBRGB:
                return new ModoPintura.Simple.Matriz.CL.HSBRGB(M);
            case MODOPINTURA_S_M_CL_HSBHSL:
                return new ModoPintura.Simple.Matriz.CL.HSBHSL(M);
            case MODOPINTURA_S_M_CL_HSLRGB:
                return new ModoPintura.Simple.Matriz.CL.HSLRGB(M);
            case MODOPINTURA_S_M_CL_HSLHSB:
                return new ModoPintura.Simple.Matriz.CL.HSLHSB(M);
            case MODOPINTURA_S_M_TL_R:
                return new ModoPintura.Simple.Matriz.TL.R();
            case MODOPINTURA_S_M_TL_G:
                return new ModoPintura.Simple.Matriz.TL.G();
            case MODOPINTURA_S_M_TL_B:
                return new ModoPintura.Simple.Matriz.TL.B();
            case MODOPINTURA_S_M_TL_RG:
                return new ModoPintura.Simple.Matriz.TL.RG();
            case MODOPINTURA_S_M_TL_RB:
                return new ModoPintura.Simple.Matriz.TL.RB();
            case MODOPINTURA_S_M_TL_GB:
                return new ModoPintura.Simple.Matriz.TL.GB();
            case MODOPINTURA_S_M_TL_RBG:
                return new ModoPintura.Simple.Matriz.TL.RBG();
            case MODOPINTURA_S_M_TL_GRB:
                return new ModoPintura.Simple.Matriz.TL.GRB();
            case MODOPINTURA_S_M_TL_GBR:
                return new ModoPintura.Simple.Matriz.TL.GBR();
            case MODOPINTURA_S_M_TL_BGR:
                return new ModoPintura.Simple.Matriz.TL.BGR();
            case MODOPINTURA_S_M_TL_BRG:
                return new ModoPintura.Simple.Matriz.TL.BRG();
            case MODOPINTURA_S_M_TL_RGB_GNRL:
                return new ModoPintura.Simple.Matriz.TL.RGB(M);
            case MODOPINTURA_S_M_TL_HSB_GNRL:
                return new ModoPintura.Simple.Matriz.TL.HSB(M);
            case MODOPINTURA_S_M_TL_HSL_GNRL:
                return new ModoPintura.Simple.Matriz.TL.HSL(M);
            case MODOPINTURA_S_G_PROMEDIO_RGB:
                return new ModoPintura.Simple.Gris.PromedioRGB();
            case MODOPINTURA_S_G_DISTANCIA:
                return new ModoPintura.Simple.Gris.Distancia();
            case MODOPINTURA_S_G_DISTANCIA_INVERTIDO:
                return new ModoPintura.Simple.Gris.Distancia_Invertido();
            case MODOPINTURA_S_G_CANAL_ROJO:
                return new ModoPintura.Simple.Gris.CanalRojo();
            case MODOPINTURA_S_G_CANAL_VERDE:
                return new ModoPintura.Simple.Gris.CanalVerde();
            case MODOPINTURA_S_G_CANAL_AZUL:
                return new ModoPintura.Simple.Gris.CanalAzul();
            case MODOPINTURA_S_G_CANAL_MAGENTA:
                return new ModoPintura.Simple.Gris.CanalMagenta();
            case MODOPINTURA_S_G_CANAL_AMARILLO:
                return new ModoPintura.Simple.Gris.CanalAmarillo();
            case MODOPINTURA_S_G_CANAL_CYAN:
                return new ModoPintura.Simple.Gris.CanalCyan();
            case MODOPINTURA_S_G_TONO:
                return new ModoPintura.Simple.Gris.Tono();
            case MODOPINTURA_S_G_HSB_BRILLO:
                return new ModoPintura.Simple.Gris.Brillo_HSB();
            case MODOPINTURA_S_G_HSB_BRILLO_INVERTIDO:
                return new ModoPintura.Simple.Gris.Brillo_HSB_Invertido();
            case MODOPINTURA_S_G_HSB_BRILLO_BINARIZADO:
                return new ModoPintura.Simple.Gris.Brillo_HSB_Binarizado();
            case MODOPINTURA_S_G_HSL_LUMINOSIDAD:
                return new ModoPintura.Simple.Gris.Brillo_HSL();
            case MODOPINTURA_S_G_HSL_LUMINOSIDAD_INVERTIDO:
                return new ModoPintura.Simple.Gris.Brillo_HSL_Invertido();
            case MODOPINTURA_S_G_HSL_LUMINOSIDAD_BINARIZADO:
                return new ModoPintura.Simple.Gris.Brillo_HSL_Binarizado();
            case MODOPINTURA_S_G_HSB_SATURACIÓN:
                return new ModoPintura.Simple.Gris.Saturación_HSB();
            case MODOPINTURA_S_G_HSL_SATURACIÓN:
                return new ModoPintura.Simple.Gris.Saturación_HSL();
            case MODOPINTURA_S_A_PROMEDIO_RGB:
                return new ModoPintura.Simple.Alpha.PromedioRGB();
            case MODOPINTURA_S_A_DISTANCIA:
                return new ModoPintura.Simple.Alpha.Distancia();
            case MODOPINTURA_S_A_DISTANCIA_INVERTIDO:
                return new ModoPintura.Simple.Alpha.Distancia_Invertido();
            case MODOPINTURA_S_A_CANAL_ROJO:
                return new ModoPintura.Simple.Alpha.CanalRojo();
            case MODOPINTURA_S_A_CANAL_VERDE:
                return new ModoPintura.Simple.Alpha.CanalVerde();
            case MODOPINTURA_S_A_CANAL_AZUL:
                return new ModoPintura.Simple.Alpha.CanalAzul();
            case MODOPINTURA_S_A_CANAL_MAGENTA:
                return new ModoPintura.Simple.Alpha.CanalMagenta();
            case MODOPINTURA_S_A_CANAL_AMARILLO:
                return new ModoPintura.Simple.Alpha.CanalAmarillo();
            case MODOPINTURA_S_A_CANAL_CYAN:
                return new ModoPintura.Simple.Alpha.CanalCyan();
            case MODOPINTURA_S_A_TONO:
                return new ModoPintura.Simple.Alpha.Tono();
            case MODOPINTURA_S_A_HSB_BRILLO:
                return new ModoPintura.Simple.Alpha.Brillo_HSB();
            case MODOPINTURA_S_A_HSB_BRILLO_INVERTIDO:
                return new ModoPintura.Simple.Alpha.Brillo_HSB_Invertido();
            case MODOPINTURA_S_A_HSB_BRILLO_BINARIZADO:
                return new ModoPintura.Simple.Alpha.Brillo_HSB_Binarizado();
            case MODOPINTURA_S_A_HSL_LUMINOSIDAD:
                return new ModoPintura.Simple.Alpha.Brillo_HSL();
            case MODOPINTURA_S_A_HSL_LUMINOSIDAD_INVERTIDO:
                return new ModoPintura.Simple.Alpha.Brillo_HSL_Invertido();
            case MODOPINTURA_S_A_HSL_LUMINOSIDAD_BINARIZADO:
                return new ModoPintura.Simple.Alpha.Brillo_HSL_Binarizado();
            case MODOPINTURA_S_A_HSB_SATURACIÓN:
                return new ModoPintura.Simple.Alpha.Saturación_HSB();
            case MODOPINTURA_S_A_HSL_SATURACIÓN:
                return new ModoPintura.Simple.Alpha.Saturación_HSL();
            case MODOPINTURA_VA_U_DISCRETIZAR:
                return new ModoPintura.ConVariableDeAplicación.Discretizar();
            case MODOPINTURA_VA_U_MONOCROMATIZAR:
                return new ModoPintura.ConVariableDeAplicación.Monocromatizar();
            case MODOPINTURA_VA_U_MONOSATURAR_HSB:
                return new ModoPintura.ConVariableDeAplicación.Monosaturar_HSB();
            case MODOPINTURA_VA_U_MONOSATURAR_HSL:
                return new ModoPintura.ConVariableDeAplicación.Monosaturar_HSL();
            case MODOPINTURA_VA_U_MONOBRILLAR_HSB:
                return new ModoPintura.ConVariableDeAplicación.Monobrillar_HSB();
            case MODOPINTURA_VA_U_MONOBRILLAR_HSL:
                return new ModoPintura.ConVariableDeAplicación.Monobrillar_HSL();
            case MODOPINTURA_VA_U_MONOCANALIZAR_ROJO:
                return new ModoPintura.ConVariableDeAplicación.Monocanalizar_rojo();
            case MODOPINTURA_VA_U_MONOCANALIZAR_VERDE:
                return new ModoPintura.ConVariableDeAplicación.Monocanalizar_verde();
            case MODOPINTURA_VA_U_MONOCANALIZAR_AZUL:
                return new ModoPintura.ConVariableDeAplicación.Monocanalizar_azul();
            case MODOPINTURA_VA_U_MONOCANALIZAR_CYAN:
                return new ModoPintura.ConVariableDeAplicación.Monocanalizar_cyan();
            case MODOPINTURA_VA_U_MONOCANALIZAR_MAGENTA:
                return new ModoPintura.ConVariableDeAplicación.Monocanalizar_magenta();
            case MODOPINTURA_VA_U_MONOCANALIZAR_AMARILLO:
                return new ModoPintura.ConVariableDeAplicación.Monocanalizar_amarillo();
            case MODOPINTURA_VA_U_MONOCANALIZAR_NEGRO:
                return new ModoPintura.ConVariableDeAplicación.Monocanalizar_negro();
            case MODOPINTURA_VA_D_MONOCOLORIZAR_HSB:
                return new ModoPintura.ConVariableDeAplicación.Monocolorizar_HSB();
            case MODOPINTURA_VA_D_MONOCOLORIZAR_HSL:
                return new ModoPintura.ConVariableDeAplicación.Monocolorizar_HSL();
            case MODOPINTURA_VA_D_CONSERVAR_SAT_COLOR_TONO:
                return new ModoPintura.ConVariableDeAplicación.ConservarSaturaciónDeColorEnTonoEspecifico();
            case MODOPINTURA_VA_D_ELIMINAR_SAT_COLOR_TONO:
                return new ModoPintura.ConVariableDeAplicación.EliminarSaturaciónDeColorEnTonoEspecifico();
            case MODOPINTURA_VA_T_CAMBIAR_TONO_COLOR_TONO_ESP:
                return new ModoPintura.ConVariableDeAplicación.CambiarTonoDeColorEnTonoEspecifico();
            case MODOPINTURA_VA_T_CAMBIAR_SAT_HSB_COLOR_TONO_ESP:
                return new ModoPintura.ConVariableDeAplicación.CambiarSaturaciónHSBColorEnTonoEspecifico();
            case MODOPINTURA_VA_T_CAMBIAR_BRILLO_HSB_COLOR_TONO_ESP:
                return new ModoPintura.ConVariableDeAplicación.CambiarBrilloHSBDeColorEnTonoEspecifico();
            case MODOPINTURA_VA_T_CAMBIAR_SAT_HSL_COLOR_TONO_ESP:
                return new ModoPintura.ConVariableDeAplicación.CambiarSaturaciónHSLColorEnTonoEspecifico();
            case MODOPINTURA_VA_T_CAMBIAR_BRILLO_HSL_COLOR_TONO_ESP:
                return new ModoPintura.ConVariableDeAplicación.CambiarBrilloHSLDeColorEnTonoEspecifico();
            case MODOPINTURA_VA_T_EQUILIBRAR_RGB:
                return new ModoPintura.ConVariableDeAplicación.EquilibrarRGB();
            case MODOPINTURA_VA_T_EQUILIBRAR_HSB:
                return new ModoPintura.ConVariableDeAplicación.EquilibrarHSB();
            case MODOPINTURA_VA_T_EQUILIBRAR_HSL:
                return new ModoPintura.ConVariableDeAplicación.EquilibrarHSL();
            case MODOPINTURA_VA_T_EQUILIBRAR_BINARIZANDO_RGB:
                return new ModoPintura.ConVariableDeAplicación.EquilibrarBinarizandoRGB();
            case MODOPINTURA_VA_T_EQUILIBRAR_BINARIZANDO_HSB:
                return new ModoPintura.ConVariableDeAplicación.EquilibrarBinarizandoHSB();
            case MODOPINTURA_VA_T_EQUILIBRAR_BINARIZANDO_HSL:
                return new ModoPintura.ConVariableDeAplicación.EquilibrarBinarizandoHSL();
            case MODOPINTURA_VA_T_EXTENDER_RGB:
                return new ModoPintura.ConVariableDeAplicación.ExtenderRGB();
            case MODOPINTURA_VA_T_EXTENDER_HSB:
                return new ModoPintura.ConVariableDeAplicación.ExtenderHSB();
            case MODOPINTURA_VA_T_EXTENDER_HSL:
                return new ModoPintura.ConVariableDeAplicación.ExtenderHSL();
            case MODOPINTURA_VA_T_EXTENDER_BINARIZANDO_RGB:
                return new ModoPintura.ConVariableDeAplicación.ExtenderBinarizandoRGB();
            case MODOPINTURA_VA_T_EXTENDER_BINARIZANDO_HSB:
                return new ModoPintura.ConVariableDeAplicación.ExtenderBinarizandoHSB();
            case MODOPINTURA_VA_C_EQUILIBRAR_CMYK:
                return new ModoPintura.ConVariableDeAplicación.EquilibrarCMYK();
            case MODOPINTURA_VA_C_EXTENDER_CMYK:
                return new ModoPintura.ConVariableDeAplicación.ExtenderCMYK();
            case MODOPINTURA_VA_C_EXTENDER_BINARIZANDO_CMYK:
                return new ModoPintura.ConVariableDeAplicación.ExtenderBinarizandoCMYK();
            case MODOPINTURA_VA_C_EQUILIBRAR_BINARIZANDO_CMYK:
                return new ModoPintura.ConVariableDeAplicación.EquilibrarBinarizandoCMYK();
            case MODOPINTURA_PUNTILLISMO:
                return new ModoPintura.Puntillismo();
            case MODOPINTURA_PUNTILLISMO_FUERTE:
                return new ModoPintura.Puntillismo.Fuerte();
            case MODOPINTURA_PUNTILLISMO_HORIZONTAL:
                return new ModoPintura.PuntillismoHorizontal();
            case MODOPINTURA_PUNTILLISMO_HORIZONTAL_FUERTE:
                return new ModoPintura.PuntillismoHorizontal.Fuerte();
            case MODOPINTURA_PUNTILLISMO_VERTICAL:
                return new ModoPintura.PuntillismoVertical();
            case MODOPINTURA_PUNTILLISMO_VERTICAL_FUERTE:
                return new ModoPintura.PuntillismoVertical.Fuerte();
            case MODOPINTURA_PUNTILLISMO_DIAGONAL_DERECHA:
                return new ModoPintura.PuntillismoDiagonalDerecha();
            case MODOPINTURA_PUNTILLISMO_DIAGONAL_DERECHA_FUERTE:
                return new ModoPintura.PuntillismoDiagonalDerecha.Fuerte();
            case MODOPINTURA_PUNTILLISMO_DIAGONAL_IZQUIERDA:
                return new ModoPintura.PuntillismoDiagonalIzquierda();
            case MODOPINTURA_PUNTILLISMO_DIAGONAL_IZQUIERDA_FUERTE:
                return new ModoPintura.PuntillismoDiagonalIzquierda.Fuerte();
            default:
                throw new RuntimeException("El Modo de pintura no se reconoce " + MODOPINTURA);
        }
    }

    public static BaseModoPintura ConsultarModoPintura(byte MODOFUSIÓN) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return ConsultarModoPintura(MODOFUSIÓN, null);
    }//</editor-fold>

    void SincronizarGUIConModeloPintura() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            //<editor-fold defaultstate="collapsed" desc="Se avisará si el modo de fusión genera salidas RGB">
            Presentador p = (Presentador) jLabel5;
            if (ModoPintura.RiesgoSalida) {
                p.ActualizarFotograma(BibliotecaArchivos.Imagenes.Iconos.Alerta("  Hay riesgo\n  de salida\n  RGB")
                );
            } else {
                p.ActualizarFotograma(BibliotecaArchivos.Imagenes.Iconos.Correcto("  No Hay\n  riesgo de\n  salida RGB")
                );
            }
            //</editor-fold>
            if (jList2.getSelectedValue() != null) {
                PresentadorEscenario.setText("  " + jList2.getSelectedValue().replace("/t/", ""));
            } else {
                PresentadorEscenario.setText("  Normal");
            }
            jSlider8.setEnabled(false);
            jSlider7.setEnabled(false);
            jSlider6.setEnabled(false);
            jSlider2.setEnabled(false);
            switch (ModoPintura.AplicacionesUsadas()) {
                case 4:
                    jSlider8.setEnabled(true);
                    jSlider8.setValue((int) (ModoPintura.Aplicación4 * jSlider8.getMaximum()));
                case 3:
                    jSlider7.setEnabled(true);
                    jSlider7.setValue((int) (ModoPintura.Aplicación3 * jSlider7.getMaximum()));
                case 2:
                    jSlider6.setEnabled(true);
                    jSlider6.setValue((int) (ModoPintura.Aplicación2 * jSlider6.getMaximum()));
                case 1:
                    jSlider2.setEnabled(true);
                    jSlider2.setValue((int) (ModoPintura.Aplicación * jSlider2.getMaximum()));
            }
        } catch (Exception e) {
        }
    }//</editor-fold>

    void SincronizarModoPinturaConGUI() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        try {
            if (!cambiandoModo) {
                ModoPintura.Opacidad = 1d * jSlider1.getValue() / jSlider1.getMaximum();
                ModoPintura.AplicaciónRojo = 1d * jSlider3.getValue() / jSlider3.getMaximum();
                ModoPintura.AplicaciónVerde = 1d * jSlider4.getValue() / jSlider4.getMaximum();
                ModoPintura.AplicaciónAzul = 1d * jSlider5.getValue() / jSlider5.getMaximum();
                switch (ModoPintura.AplicacionesUsadas()) {
                    case 4:
                        ModoPintura.Aplicación4 = 1d * jSlider8.getValue() / jSlider8.getMaximum();
                    case 3:
                        ModoPintura.Aplicación3 = 1d * jSlider7.getValue() / jSlider7.getMaximum();
                    case 2:
                        ModoPintura.Aplicación2 = 1d * jSlider6.getValue() / jSlider6.getMaximum();
                    case 1:
                        ModoPintura.Aplicación = 1d * jSlider2.getValue() / jSlider2.getMaximum();
                }
            }
            ModoPintura.Modificar_AlphaDiscriminador((byte) jComboBox1.getSelectedIndex());
            switch (jComboBox2.getSelectedItem().toString()) {
                case "Extremos":
                    ModoPintura.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_A_EXTREMOS);
                    break;
                case "Extremos abs":
                    ModoPintura.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_A_EXTREMOS_ABS);
                    break;
                case "Circular":
                    ModoPintura.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_CIRCULAR);
                    break;
                case "Circular abs":
                    ModoPintura.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_CIRCULAR_ABS);
                    break;
                case "Reflejo":
                    ModoPintura.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_REFLEJO);
                    break;
                case "Reflejo Inv":
                    ModoPintura.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_REFLEJO_INV);
                    break;
                case "Mascara de bits":
                   ModoPintura.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_MASCARA_BITS);
                    break;
                case "Nulidad por salida":
                   ModoPintura.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_NULIDAD_POR_SALIDA);
                    break;
                case "Sin definir":
                   ModoPintura.Modificar_AjusteRGB(BaseModoFusión.AJUSTE_SIN_DEFINIR);
                    break;
            }
        } catch (Exception e) {
        }
        ActualizarEscenario();
    }//</editor-fold>

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

    void ActualizarJtext() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        boolean b = true;
        int n = 0;
        if (ModoFusión instanceof ModoPintura.Simple.Matriz.TL.BaseMatrizTL) {
            n = 9;
        }
        if (ModoFusión instanceof ModoPintura.Simple.Matriz.CL.BaseMatrizCL) {
            n = 18;
        }
        for (JTextField[] textfield : textfields) {
            for (JTextField jTextField : textfield) {
                b = n-- > 0;
                if (b) {
                    jTextField.setBackground(Color.WHITE);
                } else {
                    jTextField.setBackground(new Color(0xcccccc));
                }
                jTextField.setEditable(b);
            }
        }
        ActualizarModeloBasePintura_Matriz();
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
                            ActualizarModeloBasePintura_Matriz();
                        } catch (Exception e) {
                            jTextField.setBackground(Color.PINK);
                        }
                    }
                });
            }
        }
    }//</editor-fold>

    void ActualizarModeloBasePintura_Matriz() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (ModoPintura instanceof ModoPintura.Simple.Matriz.BaseMatriz) {
            ModoPintura.Simple.Matriz.BaseMatriz CL = (ModoPintura.Simple.Matriz.BaseMatriz) ModoPintura;
            CL.actualizarMatriz(ObtenerMatrizCombinaciónLineal());
            ActualizarEscenario();
        }
    }//</editor-fold>

    float[][] ObtenerMatrizCombinaciónLineal() {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        float[][] retorno = new float[6][3];
        for (int c = 0; c < retorno[0].length; c++) {
            for (int f = 0; f < retorno.length; f++) {
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
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new Presentador();
        jSlider1 = new javax.swing.JSlider();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jPanel7 = new javax.swing.JPanel();
        jSlider3 = new javax.swing.JSlider();
        jSlider4 = new javax.swing.JSlider();
        jSlider5 = new javax.swing.JSlider();
        jLabel5 = new Presentador();
        jPanel6 = new javax.swing.JPanel();
        jSlider2 = new javax.swing.JSlider();
        jSlider6 = new javax.swing.JSlider();
        jSlider7 = new javax.swing.JSlider();
        jSlider8 = new javax.swing.JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Matriz de transformación"));
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

        jPanel5.setLayout(new java.awt.GridLayout(4, 1));

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
        jPanel5.add(jLabel4);

        jSlider1.setMaximum(300);
        jSlider1.setValue(jSlider1.getMaximum());
        jSlider1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Opacidad 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        jPanel5.add(jSlider1);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Adición", "Binarizar", "Dentro", "Mascara", "Borrado", "Borrado y aplicado", "Borrado fuerte", "Borrado rectángular", "Distancia", "Intersección", "Disolver", "Unión por bits", "Intersección por bits", "XOR por bits", "Sustracción & masc. 16-bits", "Sustracción & masc. 16-bits lat.", "const. opacidad", "const. opacidad mascara", "const. opacidad salida", "const. opacidad intersección", "const. opacidad escalonar", "const. opacidad rectificación lineal" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createTitledBorder("Discriminación Alpha"));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel5.add(jComboBox1);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Extremos", "Extremos abs", "Circular", "Circular abs", "Reflejo", "Reflejo Inv", "Mascara de bits", "Nulidad por salida", "Sin definir" }));
        jComboBox2.setBorder(javax.swing.BorderFactory.createTitledBorder("Ajuste RGB"));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });
        jPanel5.add(jComboBox2);

        jList2.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Lista de Modos de pintura" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList2.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList2ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

        jPanel7.setLayout(new java.awt.GridLayout(4, 1));

        jSlider3.setMaximum(300);
        jSlider3.setValue(jSlider3.getMaximum());
        jSlider3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación Rojo 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(153, 0, 0))); // NOI18N
        jSlider3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider3StateChanged(evt);
            }
        });
        jPanel7.add(jSlider3);

        jSlider4.setMaximum(300);
        jSlider4.setValue(jSlider4.getMaximum());
        jSlider4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación Verde 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(0, 153, 0))); // NOI18N
        jSlider4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider4StateChanged(evt);
            }
        });
        jPanel7.add(jSlider4);

        jSlider5.setMaximum(300);
        jSlider5.setValue(jSlider5.getMaximum());
        jSlider5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación Azul 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("sansserif", 1, 12), new java.awt.Color(0, 0, 204))); // NOI18N
        jSlider5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider5StateChanged(evt);
            }
        });
        jPanel7.add(jSlider5);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Riesgo de salida");
        jPanel7.add(jLabel5);

        jPanel6.setLayout(new java.awt.GridLayout(4, 0));

        jSlider2.setMaximum(300);
        jSlider2.setValue(jSlider2.getMaximum());
        jSlider2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación 1) 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });
        jPanel6.add(jSlider2);

        jSlider6.setMaximum(400);
        jSlider6.setValue(jSlider6.getMaximum());
        jSlider6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación 2) 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));
        jSlider6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider6StateChanged(evt);
            }
        });
        jPanel6.add(jSlider6);

        jSlider7.setMaximum(400);
        jSlider7.setValue(jSlider7.getMaximum());
        jSlider7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación 3) 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));
        jSlider7.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider7StateChanged(evt);
            }
        });
        jPanel6.add(jSlider7);

        jSlider8.setMaximum(400);
        jSlider8.setValue(jSlider7.getMaximum());
        jSlider8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Aplicación 4) 100%", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP));
        jSlider8.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider8StateChanged(evt);
            }
        });
        jPanel6.add(jSlider8);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
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
                .addContainerGap())
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
        SincronizarModoPinturaConGUI();
        jSlider1.setBorder(BorderFactory.createTitledBorder(null,
                "Opacidad " + Matemática.Truncar(ModoPintura.Opacidad * 100, 2) + "%",
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
        cambiandoModo = true;
        String s = jList2.getSelectedValue();
        if (s.startsWith("/-/")) {
            cambiandoModo = false;
            return;
        }
        byte Modo = (byte) ListaID.ObtenerID(s);
        ModoFusión = ConsultarModoPintura(Modo, ObtenerMatrizCombinaciónLineal());
        ModoPintura = (BaseModoPintura) ModoFusión;
        ActualizarJtext();
        SincronizarGUIConModeloPintura();
        SincronizarModoPinturaConGUI();
        cambiandoModo = false;
        ActualizarEscenario();
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
                int a = -2;
                int b = 2;
                float f = (float) Matemática.Truncar(((b - a) * Math.random() + a), 4, true);
                jtxt.setText(f + "");
            }
        }
        ActualizarModeloBasePintura_Matriz();
        //</editor-fold>
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        SincronizarModoPinturaConGUI();
    }//GEN-LAST:event_jComboBox1ActionPerformed

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
        ActualizarModeloBasePintura_Matriz();
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

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        SincronizarModoPinturaConGUI();

        jSlider2.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación 1) " + Matemática.Truncar(ModoPintura.Aplicación * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP
        )
        );
        //</editor-fold>
    }//GEN-LAST:event_jSlider2StateChanged

    private void jSlider3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider3StateChanged
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        SincronizarModoPinturaConGUI();
        jSlider3.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación Rojo " + Matemática.Truncar(ModoPintura.AplicaciónRojo * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP,
                new Font("sansserif", Font.BOLD, 12),
                new Color(153, 0, 0))
        );
        //</editor-fold>
    }//GEN-LAST:event_jSlider3StateChanged

    private void jSlider4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider4StateChanged
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        SincronizarModoPinturaConGUI();
        jSlider4.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación Verde " + Matemática.Truncar(ModoPintura.AplicaciónVerde * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP,
                new Font("sansserif", Font.BOLD, 12),
                new Color(0, 153, 0))
        );
        //</editor-fold>
    }//GEN-LAST:event_jSlider4StateChanged

    private void jSlider5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider5StateChanged
        //<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        SincronizarModoPinturaConGUI();
        jSlider5.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación Azul " + Matemática.Truncar(ModoPintura.AplicaciónAzul * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP,
                new Font("sansserif", Font.BOLD, 12),
                new Color(0, 0, 153))
        );
        //</editor-fold>
    }//GEN-LAST:event_jSlider5StateChanged

    private void jSlider6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider6StateChanged
        SincronizarModoPinturaConGUI();
        float p = 0;
        if (ModoPintura.AplicacionesUsadas() == 2) {
            p = (float) ModoPintura.Aplicación2;
        } else {
            p = 1f * jSlider6.getValue() / jSlider6.getMaximum();
        }
        jSlider6.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación 2) " + Matemática.Truncar(p * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP)
        );
    }//GEN-LAST:event_jSlider6StateChanged

    private void jSlider7StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider7StateChanged
        SincronizarModoPinturaConGUI();
        float p = 0;
        if (ModoPintura.AplicacionesUsadas() == 3) {
            p = (float) ModoPintura.Aplicación3;
        } else {
            p = 1f * jSlider7.getValue() / jSlider7.getMaximum();
        }
        jSlider7.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación 3) " + Matemática.Truncar(p * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP)
        );
    }//GEN-LAST:event_jSlider7StateChanged

    private void jSlider8StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider8StateChanged
        SincronizarModoPinturaConGUI();
        float p = 0;
        if (ModoPintura.AplicacionesUsadas() == 4) {
            p = (float) ModoPintura.Aplicación4;
        } else {
            p = 1f * jSlider8.getValue() / jSlider8.getMaximum();
        }
        jSlider8.setBorder(BorderFactory.createTitledBorder(null,
                "Aplicación 4) " + Matemática.Truncar(p * 100, 2) + "%",
                TitledBorder.LEFT, TitledBorder.ABOVE_TOP)
        );
    }//GEN-LAST:event_jSlider8StateChanged

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        SincronizarModoPinturaConGUI();
    }//GEN-LAST:event_jComboBox2ActionPerformed

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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JSlider jSlider4;
    private javax.swing.JSlider jSlider5;
    private javax.swing.JSlider jSlider6;
    private javax.swing.JSlider jSlider7;
    private javax.swing.JSlider jSlider8;
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
