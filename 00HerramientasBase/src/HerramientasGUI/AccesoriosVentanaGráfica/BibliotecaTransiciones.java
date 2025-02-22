package HerramientasGUI.AccesoriosVentanaGráfica;

import HerramientaArchivos.CarpetaDeRecursos;
import HerramientaArchivos.LectoEscrituraArchivos;
import HerramientasSistema.Sistema;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Formatter;

public class BibliotecaTransiciones {

    public static File[] Añadir_Biblioteca_appdata(String NombreBiblioteca, String URLRecurso, int celdas) {
        return Añadir_Biblioteca_appdata(NombreBiblioteca, URLRecurso, celdas, false);
    }

    public static File[] Añadir_Biblioteca_appdata(String NombreBiblioteca, String URLRecurso, int celdas, boolean Vertical) {
        CarpetaDeRecursos Carpeta = new CarpetaDeRecursos(
                CarpetaDeRecursos.TIPO_APPDATA, "Animaciones por Sprites\\" + NombreBiblioteca
        );
        {
            File[] archivos = Carpeta.ListarArchivos(".png");
            if (archivos != null && archivos.length > 0) {
                System.out.println("Los Sprites existen");
                return archivos;
            }
        }
        System.out.println("Los Sprites no existen en el equipo, se procederá a crearlos");
        BufferedImage Imagen = LectoEscrituraArchivos.cargar_imagen(
                URLRecurso
        );
        if (Imagen == null) {
            if (!Sistema.VerificarConexiónWeb()) {
                System.out.println("No hay conexión a internet");
                return null;
            } else {
                System.out.println("Al parecer la ruta del sprite está mal formulada");
                return null;
            }
        }
        int lado = Vertical ? (Imagen.getHeight() - 1) / celdas : Imagen.getWidth() / celdas;
        if (lado < 1) {
            System.out.println("La imagen no tiene las dimensiones suficientes para "
                    + "la cantidad de imagenes que dice tener");
            return null;
        }
        for (int i = 0; i < celdas; i++) {
            System.out.println("img: " + i + " de " + celdas);
            BufferedImage celda = Imagen.getSubimage(
                    0, 0,
                    Vertical ? Imagen.getWidth() : lado, Vertical ? lado : Imagen.getHeight()
            );
            try {
                Imagen = Imagen.getSubimage(
                        Vertical ? 0 : lado, Vertical ? lado : 0,
                        Vertical ? Imagen.getWidth() : Imagen.getWidth() - lado,
                        Vertical ? Imagen.getHeight() - lado : Imagen.getHeight()
                );
            } catch (Exception e) {
            }
            String dir = Carpeta.DIRECCIÓN_CARPETA + new Formatter().format("%07d.png", i + 1);
            LectoEscrituraArchivos.exportar_imagen(celda, dir);
        }
        return Carpeta.ListarArchivos(".png");
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/mnhSUJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_1() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\1",
                "https://image.ibb.co/d4uwOd/Sprite.png",
                46
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/e4ZGpJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_2() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\2",
                "https://image.ibb.co/iFpcwy/Sprite.png",
                43
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/cmpGpJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_3() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\3",
                "https://image.ibb.co/cqkNUJ/Sprite.png",
                41
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/b7VE9J/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_4() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\4",
                "https://image.ibb.co/gWoO3d/Sprite.png",
                38
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/g5kQGy/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_5() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\5",
                "https://image.ibb.co/kHDZ9J/Sprite.png",
                44
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/nperpJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_6() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\6",
                "https://image.ibb.co/cjhY3d/Sprite.png",
                44
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/eG4GpJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_7() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\7",
                "https://image.ibb.co/ncpAGy/Sprite.png",
                44
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/b6jP9J/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_8() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\8",
                "https://image.ibb.co/gY3HUJ/Sprite.png",
                43
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/kjvjby/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_9() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\9",
                "https://image.ibb.co/bKNaid/Sprite.png",
                48
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/mtLW6y/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_10() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\10",
                "https://image.ibb.co/bFZr6y/Sprite.png",
                47
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/mfG2eJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_11() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\11",
                "https://image.ibb.co/c4OnDd/Sprite.png",
                45
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/gq5QYd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_12() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\12",
                "https://image.ibb.co/nQ98my/Sprite.png",
                35
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/fGvQYd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_13() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\13",
                "https://image.ibb.co/e3TCDd/Sprite.png",
                44
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/hsweRy/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_14() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\14",
                "https://image.ibb.co/hQ6Ptd/Sprite.png",
                46
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/mPc3my/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_15() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\15",
                "https://image.ibb.co/ihiheJ/Sprite.png",
                41
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/iVjAYd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_16() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\16",
                "https://image.ibb.co/jjh8KJ/Sprite.png",
                39
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/k734td/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_17() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\17",
                "https://image.ibb.co/c6Q66y/Sprite.png",
                41
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/gjnEtd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_18() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\18",
                "https://image.ibb.co/e2A0Yd/Sprite.png",
                48
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/gXU2Dd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_19() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\19",
                "https://image.ibb.co/gU1AzJ/Sprite.png",
                46
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/cGxuRy/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_20() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\20",
                "https://image.ibb.co/eZXiKJ/Sprite.png",
                46
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/nHkaYd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_21() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\21",
                "https://image.ibb.co/b5qneJ/Sprite.png",
                45
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/gvtUtd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_22() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\22",
                "https://image.ibb.co/iaHdmy/Sprite.png",
                44
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/hipxeJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_23() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\23",
                "https://image.ibb.co/n123my/Sprite.png",
                44
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/jRLZtd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_24() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\24",
                "https://image.ibb.co/jUyR6y/Sprite.png",
                33
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/ixAdKJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_25() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\25",
                "https://image.ibb.co/i6VHDd/Sprite.png",
                41
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/e9x3my/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_26() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\26",
                "https://image.ibb.co/fp0HDd/Sprite.png",
                43
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/bXsazJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_27() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\27",
                "https://image.ibb.co/jrSd3d/Sprite.png",
                39
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/jrcZby/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_28() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\28",
                "https://image.ibb.co/i7jmpJ/Sprite.png",
                39
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/d9ruby/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_29() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\29",
                "https://image.ibb.co/htrhUJ/Sprite.png",
                39
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/h2wkGy/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_30() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\30",
                "https://image.ibb.co/djS9by/Sprite.png",
                33
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/nQg8my/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_31() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\31",
                "https://image.ibb.co/ewRkYd/Sprite.png",
                36
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/nNGPRy/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_32() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\32",
                "https://image.ibb.co/i6Mr6y/Sprite.png",
                40
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/jqFheJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_33() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\33",
                "https://image.ibb.co/cVrcDd/Sprite.png",
                30
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/g3MoKJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_34() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\34",
                "https://image.ibb.co/dWem6y/Sprite.png",
                27
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/j9KfYd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_35() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\35",
                "https://image.ibb.co/dpvb6y/Sprite.png",
                30
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/mmODmy/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_36() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\36",
                "https://image.ibb.co/gGdqYd/Sprite.png",
                28
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/mLCOmy/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_37() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\37",
                "https://image.ibb.co/nDWSDd/Sprite.png",
                33
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/bR9NDd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_38() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\38",
                "https://image.ibb.co/kdf7eJ/Sprite.png",
                28
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/ihd4Ry/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_39() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\39",
                "https://image.ibb.co/iUh9td/Sprite.png",
                30
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/bPiceJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_40() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\40",
                "https://image.ibb.co/iHfOKJ/Sprite.png",
                28
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/jNq7eJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_41() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\41",
                "https://image.ibb.co/gy3omy/Sprite.png",
                25
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/iK2Ztd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_42() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\42",
                "https://image.ibb.co/iJMutd/Sprite.png",
                27
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/juRimy/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_43() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\43",
                "https://image.ibb.co/bCKseJ/Sprite.png",
                37
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/iqmYmy/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_44() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\44",
                "https://image.ibb.co/iVzazJ/Sprite.png",
                33
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/hiCdKJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_45() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\45",
                "https://image.ibb.co/jjZ3my/Sprite.png",
                30
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/eWtomy/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_46() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\46",
                "https://image.ibb.co/dxuuRy/Sprite.png",
                40
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/mkxERy/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_47() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\47",
                "https://image.ibb.co/djFetd/Sprite.png",
                25
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/fVcsDd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_48() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\48",
                "https://image.ibb.co/gnRKtd/Sprite.png",
                28
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/cU40zJ/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_49() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\49",
                "https://image.ibb.co/f86fzJ/Sprite.png",
                23
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/hsoM6y/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_50() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\50",
                "https://image.ibb.co/gNU5Yd/Sprite.png",
                34
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/hWssDd/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_51() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\51",
                "https://image.ibb.co/bNQOKJ/Sprite.png",
                33
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/gVEuRy/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_52() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\52",
                "https://image.ibb.co/jqtXDd/Sprite.png",
                28
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/nozB6y/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_53() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\53",
                "https://image.ibb.co/i5zYKJ/Sprite.png",
                29
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/iQfAid/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_54() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\54",
                "https://image.ibb.co/nEqAid/Sprite.png",
                32
        );
    }

    /**
     * <p align="center">
     * <img src="https://image.ibb.co/jMvAid/Animaci_n.gif">
     */
    public static File[] Transición_Cortina_55() {
        return Añadir_Biblioteca_appdata(
                "Transiciones Cortina\\55",
                "https://image.ibb.co/f9gNwy/Sprite.png",
                30
        );
    }

    public static void DescargarLasTransicionesCortina() {
        new Thread() {
            @Override
            public void run() {
                for (int i = 1; i <= 55; i++) {
                    long t = System.currentTimeMillis();
                    System.out.println("Descargando la transición " + i);
                    Transición_Cortina(i);
                    System.out.println("Transición " + i + " descargada");
                    System.out.println("Demoró " + ((System.currentTimeMillis() - t) / 1000f) + " segundos");
                }
            }
        }.start();
    }

    public static File[] Transición_Cortina(int n) {
        switch (n) {
            case 1:
                return Transición_Cortina_1();
            case 2:
                return Transición_Cortina_2();
            case 3:
                return Transición_Cortina_3();
            case 4:
                return Transición_Cortina_4();
            case 5:
                return Transición_Cortina_5();
            case 6:
                return Transición_Cortina_6();
            case 7:
                return Transición_Cortina_7();
            case 8:
                return Transición_Cortina_8();
            case 9:
                return Transición_Cortina_9();
            case 10:
                return Transición_Cortina_10();
            case 11:
                return Transición_Cortina_11();
            case 12:
                return Transición_Cortina_12();
            case 13:
                return Transición_Cortina_13();
            case 14:
                return Transición_Cortina_14();
            case 15:
                return Transición_Cortina_15();
            case 16:
                return Transición_Cortina_16();
            case 17:
                return Transición_Cortina_17();
            case 18:
                return Transición_Cortina_18();
            case 19:
                return Transición_Cortina_19();
            case 20:
                return Transición_Cortina_20();
            case 21:
                return Transición_Cortina_21();
            case 22:
                return Transición_Cortina_22();
            case 23:
                return Transición_Cortina_23();
            case 24:
                return Transición_Cortina_24();
            case 25:
                return Transición_Cortina_25();
            case 26:
                return Transición_Cortina_26();
            case 27:
                return Transición_Cortina_27();
            case 28:
                return Transición_Cortina_28();
            case 29:
                return Transición_Cortina_29();
            case 30:
                return Transición_Cortina_30();
            case 31:
                return Transición_Cortina_31();
            case 32:
                return Transición_Cortina_32();
            case 33:
                return Transición_Cortina_33();
            case 34:
                return Transición_Cortina_34();
            case 35:
                return Transición_Cortina_35();
            case 36:
                return Transición_Cortina_36();
            case 37:
                return Transición_Cortina_37();
            case 38:
                return Transición_Cortina_38();
            case 39:
                return Transición_Cortina_39();
            case 40:
                return Transición_Cortina_40();
            case 41:
                return Transición_Cortina_41();
            case 42:
                return Transición_Cortina_42();
            case 43:
                return Transición_Cortina_43();
            case 44:
                return Transición_Cortina_44();
            case 45:
                return Transición_Cortina_45();
            case 46:
                return Transición_Cortina_46();
            case 47:
                return Transición_Cortina_47();
            case 48:
                return Transición_Cortina_48();
            case 49:
                return Transición_Cortina_49();
            case 50:
                return Transición_Cortina_50();
            case 51:
                return Transición_Cortina_51();
            case 52:
                return Transición_Cortina_52();
            case 53:
                return Transición_Cortina_53();
            case 54:
                return Transición_Cortina_54();
            case 55:
                return Transición_Cortina_55();
            default:
                return null;
        }
    }

}
