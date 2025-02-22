package _Laboratorio;

import HerramientasMatemáticas.Dupla;

public class SenoyCosenoPoligonal {

    public static void main(String[] args) {
        Contra(3 + "", "t", Deltoide());
    }

    public static String[] Deltoide() {
        double k = -3;
        String c = "("+(k + 1) + ")";
        return new String[]{
            "cos(" + c + " * t)" + "-" + c + "* cos(t)",
            "sen(" + c + "* t)" + "-" + c + "* sen(t)"
        };
    }

    public static void Contra(String v, String t, String... curva) {
        String k = "((2/" + v + ")π)";
        String CVR = "floor(|" + t + "|/" + k + ")";
        String Piθ = "sgn(" + t + ")" + CVR + k;
        String Pfθ = Piθ + "+ sgn(sgn(" + t + ") +" + ".5)*" + k;
        String T[] = {
            curva[0].replace(t, Piθ) + "+" + curva[0].replace(t, Pfθ),
            curva[1].replace(t, Piθ) + "+" + curva[1].replace(t, Pfθ)
        };
        String X = "-(" + curva[0] + ")+" + T[0];
        String Y = "-(" + curva[1] + ")+" + T[1];
        System.out.println("Curva(" + curva[0] + "," + curva[1] + "," + t + ", -10, 10)");
        System.out.println("Curva(" + X + "," + Y + "," + t + ", -4π, 4π)");
    }

    public static void Obra() {
        convPolar("0.02(t*(" + CosPoligono(3 + "", "(2.033951*t)") + "))");
    }

    public static void convPolar(String r) {
        System.out.println("Curva((" + r + ")*cos(t)" + ",(" + r + ")*sen(t)" + ", t, 0, 82π)");
    }

    public static String CosPoligono(String v, String t) {
        String k = "((2/" + v + ")π)";
        String CVR = "floor(|" + t + "|/" + k + ")";
        String Piθ = "sgn(" + t + ")" + CVR + k;
        String Pfθ = Piθ + "+ sgn(sgn(" + t + ") +" + ".5)*" + k;
        String ax = 0 + "", ay = 0 + "";
        String bx = "cos(" + t + ")", by = "sen(" + t + ")";
        String cx = "cos(" + Piθ + ")", cy = "sen(" + Piθ + ")";
        String dx = "cos(" + Pfθ + ")", dy = "sen(" + Pfθ + ")";
        String k1 = "(" + bx + "-" + ax + ")";
        String k2 = "(" + by + "-" + ay + ")";
        String w = "(-(" + k1 + "(" + cy + "-" + ay + ") -" + k2 + "(" + cx + "-" + ax + ")) / ("
                + k1 + "(" + dy + "-" + cy + ") -" + k2 + " (" + dx + " - " + cx + ")))";
        String Cos = "(" + dx + "-" + cx + ") * " + w + "+" + cx;
        return Cos;
    }

    public static void ParámetrizarPoligono(String v, String t) {
        String k = "((2/" + v + ")π)";
        String CVR = "floor(|" + t + "|/" + k + ")";
        String Piθ = "sgn(" + t + ")" + CVR + k;
        String Pfθ = Piθ + "+ sgn(sgn(" + t + ") +" + ".5)*" + k;
        String ax = 0 + "", ay = 0 + "";
        String bx = "cos(" + t + ")", by = "sen(" + t + ")";
        String cx = "cos(" + Piθ + ")", cy = "sen(" + Piθ + ")";
        String dx = "cos(" + Pfθ + ")", dy = "sen(" + Pfθ + ")";
        String k1 = "(" + bx + "-" + ax + ")";
        String k2 = "(" + by + "-" + ay + ")";
        String w = "(-(" + k1 + "(" + cy + "-" + ay + ") -" + k2 + "(" + cx + "-" + ax + ")) / ("
                + k1 + "(" + dy + "-" + cy + ") -" + k2 + " (" + dx + " - " + cx + ")))";
        String Cos = "(" + dx + "-" + cx + ") * " + w + "+" + cx;
        String Sen = "(" + dy + "-" + cy + ") * " + w + "+" + cy;
        System.out.println("Curva(" + Cos + "," + Sen + "," + t + ", -10, 10)");
    }

    public static void ConverciónGeogebra(String v, String t) {
        String k = "((2/" + v + ")π)";
        String CVR = "floor(|" + t + "|/" + k + ")";
        String Piθ = "sgn(" + t + ")" + CVR + k;
        String Pfθ = Piθ + "+ sgn(sgn(" + t + ") +" + ".5)*" + k;
        String ax = 0 + "", ay = 0 + "";
        String bx = "cos(" + t + ")", by = "sen(" + t + ")";
        String cx = "cos(" + Piθ + ")", cy = "sen(" + Piθ + ")";
        String dx = "cos(" + Pfθ + ")", dy = "sen(" + Pfθ + ")";
        String k1 = "(" + bx + "-" + ax + ")";
        String k2 = "(" + by + "-" + ay + ")";
        String w = "(-(" + k1 + "(" + cy + "-" + ay + ") -" + k2 + "(" + cx + "-" + ax + ")) / ("
                + k1 + "(" + dy + "-" + cy + ") -" + k2 + " (" + dx + " - " + cx + ")))";
        String Cos = "(" + dx + "-" + cx + ") * " + w + "+" + cx;
        String Sen = "(" + dy + "-" + cy + ") * " + w + "+" + cy;
        System.out.println("y = " + Cos);
        System.out.println("y = " + Sen);
    }

    public static void SenoYCosenoPoligonal(double t) {
        t = Math.toRadians(t);
        double v = 3;
        double k = 2 * Math.PI / v;
        double CVR = Math.floor(Math.abs(t) / k);
        double Piθ = Math.signum(t) * CVR * k;
        double Pfθ = Piθ + Math.signum(Math.signum(t) + .5) * k;
        double ax = 0, ay = 0;
        double bx = Math.cos(t), by = Math.sin(t);
        double cx = Math.cos(Piθ), cy = Math.sin(Piθ);
        double dx = Math.cos(Pfθ), dy = Math.sin(Pfθ);
        double k1 = (bx - ax);
        double k2 = (by - ay);
        double w = -(k1 * (cy - ay) - k2 * (cx - ax)) / (k1 * (dy - cy) - k2 * (dx - cx));
        double x = (dx - cx) * w + cx;
        double y = (dy - cy) * w + cy;
        System.out.println("\nCoseno y seno Poligonal con v = " + v);
        System.out.println("(" + String.format("%.4f", x) + "," + String.format("%.4f", y) + ")");
    }

}
