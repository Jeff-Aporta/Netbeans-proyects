package Degradados;

public enum AjusteRango {

    LINEAL, LINEAL_ABS, CÍCLICO, REFLEJO;

    public static double AjustarPorcentaje(double porcentaje, AjusteRango ajuste) {
        switch (ajuste) {
            case LINEAL:
                if (porcentaje > 1) {
                    return 1;
                }
                if (porcentaje < 0) {
                    return 0;
                }
                break;
            case CÍCLICO:
                porcentaje %= 1;
                if (porcentaje < 0) {
                    porcentaje = 1 + porcentaje;
                }
                return porcentaje;
            case REFLEJO:
                porcentaje %= 1;
                if (porcentaje < 0) {
                    porcentaje = 1 + porcentaje;
                }
                if (Math.floor(porcentaje) % 2 == 0) {
                    return porcentaje;
                } else {
                    return 1 - porcentaje;
                }
            case LINEAL_ABS:
                return AjustarPorcentaje(Math.abs(porcentaje), LINEAL);
        }
        if (porcentaje < 0 || porcentaje > 1) {
            throw new RuntimeException("Hay un error en la programación de los ajustes");
        }
        return porcentaje;
    }
}
