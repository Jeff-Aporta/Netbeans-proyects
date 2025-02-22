package HerramientasSistema;

import _Laboratorio.hora_online;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Fecha {

    public static void main(String[] args) {
        System.out.println(online_time_is());
//        Calendar c1 = Calendar.getInstance();
//        Calendar c2 = Calendar.getInstance();
//        c2.add(Calendar.HOUR_OF_DAY, 601);//horas antes
//        System.out.println(Dias_EntreFechas(c1, c2));
    }

    public static double DiasDeModificaciónDeUnArchivo(File f) {
        try {
            if (!f.exists()) {
                return -1;
            }
            return Fecha.Dias_EntreFechas(System.currentTimeMillis(), f.lastModified());
        } catch (Exception e) {
            return -1;
        }
    }

    public static String[] months = {"enero", "febrero", "marzo", "abril", "mayo", "junio", "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"};

    //<editor-fold defaultstate="collapsed" desc="Descripción JavaDoc">
    /**
     * Retorna la conversión del mes en texto en español
     */
    //</editor-fold>
    public static String ObtenerNombreMes(Calendar c) {//<editor-fold defaultstate="collapsed" desc="Implementación del código">
        int m = c.get(Calendar.MONTH);
        return months[m];
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tiempo entre fechas »">
    public static double Años_EntreFechas(Object c1, Object c2) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return Fecha.Años_EntreFechas(c1, c2, true);
    }//</editor-fold>

    public static double Años_EntreFechas(Object c1, Object c2, boolean Bisiesto) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return Dias_EntreFechas(c1, c2) / (365 + (Bisiesto ? .25 : 0));
    }//</editor-fold>

    public static double Meses_EntreFechas(Object c1, Object c2) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return Dias_EntreFechas(c1, c2) / 30;
    }//</editor-fold>

    public static double Semanas_EntreFechas(Object c1, Object c2) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return Dias_EntreFechas(c1, c2) / 7;
    }//</editor-fold>

    public static double Dias_EntreFechas(Object c1, Object c2) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return Horas_EntreFechas(c1, c2) / 24;
    }//</editor-fold>

    public static double Horas_EntreFechas(Object c1, Object c2) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return Minutos_EntreFechas(c1, c2) / 60;
    }//</editor-fold>

    public static double Minutos_EntreFechas(Object c1, Object c2) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return Segundos_EntreFechas(c1, c2) / 60;
    }//</editor-fold>

    public static double Segundos_EntreFechas(Object c1, Object c2) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return Milisegundos_EntreFechas(c1, c2) / 1000;
    }//</editor-fold>

    public static long Milisegundos_EntreFechas(Object c1, Object c2) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Date d1 = object2date(c1);
        Date d2 = object2date(c2);
        long Ms_transcurridos = d1.getTime() - d2.getTime();
        return Ms_transcurridos;
    }//</editor-fold>
    //</editor-fold>

    public static Calendar online_time_is() {
        return hora_online.fecha_online();
    }

    public static String object2ddmmaaaa(Object o, String separador) {
        Calendar c = object2calendar(o);
        return c.get(Calendar.DAY_OF_MONTH) + separador + (c.get(Calendar.MONTH) + 1) + separador + c.get(Calendar.YEAR);
    }
    
    public static long now2long(){
        return Calendar.getInstance().getTimeInMillis();
    }

    public static Calendar object2calendar(Object o) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Date d = object2date(o);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c;
    }//</editor-fold>

    public static long object2long(Object o) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        return object2date(o).getTime();
    }//</editor-fold>

    public static Date object2date(Object o) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        if (o instanceof Date) {
            return (Date) o;
        }
        if (o instanceof Number) {
            return new Date(((Number) o).longValue());
        }
        if (o instanceof Calendar) {
            return ((Calendar) o).getTime();
        }
        if (o instanceof String) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                return dateFormat.parse(((String) o));
            } catch (Exception ex) {
            }
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                return dateFormat.parse(((String) o));
            } catch (Exception ex) {
            }
        }
        return null;
    }//</editor-fold>

    public static Calendar date2calendar(int dia, int mes, int año) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, dia);
        c.set(Calendar.MONTH, mes - 1);
        c.set(Calendar.YEAR, año);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }//</editor-fold>

    public static long date2long(int dia, int mes, int año) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, dia);
        c.set(Calendar.MONTH, mes - 1);
        c.set(Calendar.YEAR, año);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }//</editor-fold>

    public static Calendar long2calendar(long l) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Calendar c = Calendar.getInstance();
        c.setTime(object2date(l));
        return c;
    }//</editor-fold>

    public static Calendar date2calendar(Date d) {//<editor-fold defaultstate="collapsed" desc="Implementación de código »">
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c;
    }//</editor-fold>

}
