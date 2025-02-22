package _Laboratorio;

import HerramientaArchivos.LectoEscrituraArchivos;

public class Contar_coronavirus {

    public static void main(String[] args) throws Exception {
        tabla();
    }

    static String[][] tabla() throws Exception {
        String ruta = "https://www.datos.gov.co/api/views/gt2j-8ykr/rows.csv?accessType=DOWNLOAD";
        String txt = LectoEscrituraArchivos.LeerArchivo_ASCII(ruta);
        String[] renglones = txt.split("\n");
        Ciudad[] ciudades = {
            new Ciudad("Pereira"),
            new Ciudad("Dosquebradas"),
            new Ciudad("Risaralda"),
            new Ciudad("Tuluá"),
            new Ciudad("Cali"),
            new Ciudad("Armenia"),
            new Ciudad("Quindio"),
            new Ciudad("Bogotá")
        };
        for (String renglon : renglones) {
            for (Ciudad ciudad : ciudades) {
                if (renglon.toLowerCase().contains(ciudad.nombre.toLowerCase())) {
                    ciudad.casos++;
                }
            }
        }
        for (Ciudad ciudad : ciudades) {
            System.out.println(ciudad);
        }
        return null;
    }

    static class Ciudad {

        String nombre;
        int casos;

        public Ciudad(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre + ": " + casos;
        }
    }
}
