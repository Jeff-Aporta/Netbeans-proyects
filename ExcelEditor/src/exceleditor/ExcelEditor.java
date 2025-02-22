package exceleditor;

import HerramientaArchivos.LectoEscrituraArchivos;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelEditor {
    
    public static void main(String[] args) {
        try {
            ActualizarPreciosMercadolibre();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    static void ActualizarPreciosMercadolibre() throws Exception {
        XSSFWorkbook mercadolibrePublicaciones;
        {
            FileInputStream fis = new FileInputStream(
                    System.getProperty("user.home") + "\\Publicaciones-2021_09_25-18_31.xlsx"
            );
            mercadolibrePublicaciones = new XSSFWorkbook(fis);
        }
        XSSFSheet hojaPublicacionesMercadolibre = mercadolibrePublicaciones.getSheet("Publicaciones");
        int rowshojaPublicacionesMercadolibre = hojaPublicacionesMercadolibre.getLastRowNum();
        String textoBD = LectoEscrituraArchivos.LeerArchivo_ASCII(
                "https://docs.google.com/spreadsheets/d/e/2PACX-1vTzSHP_HuiwCmpR6I56jACiS577zm3EEluoIIEna6mQOp05kYuo49CgzkFICGvwLfBsC4JIwWjXfEwj/pub?gid=0&single=true&output=tsv"
        );
        String[] renglones = textoBD.split("\n");
        for (int i = 0; i < renglones.length; i++) {
            if (i == 0) {
                continue;
            }
            String[] items = renglones[i].split("\t");
            for (int j = 0; j < rowshojaPublicacionesMercadolibre; j++) {
                XSSFRow fila = hojaPublicacionesMercadolibre.getRow(j);
                XSSFCell nombreMercadolibre = fila.getCell(2);
                XSSFCell tipoMercadolibre = fila.getCell(8);
                if (nombreMercadolibre != null) {
                    float porcentajeSimilitud = similarText(
                            items[0].trim(),
                            nombreMercadolibre.getStringCellValue()
                                    .replace("Caramelo Masticable Lokiño ×100", "Lokiño Caramelo Masticable ×100")
                                    .trim()
                    );
                    if (porcentajeSimilitud > 97) {
                        System.out.print(items[0].trim());
                        System.out.print("- - -");
                        System.out.print(nombreMercadolibre.getStringCellValue().trim());
                        System.out.print("- - -");
                        double porcentajeAlzaMercadolibre = Double.parseDouble(
                                tipoMercadolibre.getStringCellValue().replace("Clásica ", "").replace("Premium ", "").replace("%", "")
                        ) / 100;
                        System.out.print(porcentajeSimilitud);
                        int precioMercadoLibre = (int) Math.ceil(Double.parseDouble(items[7]) * (1 + porcentajeAlzaMercadolibre) * (1 + 0.414 / 100) * (1 + 1.5 / 100) + 500);
                        System.out.print("- - -");
                        System.out.print(porcentajeAlzaMercadolibre);
                        System.out.print("- - -");
                        System.out.print(precioMercadoLibre);
                        System.out.println();
                    }
                }
            }
        }
    }
    
    static void LeerExcel() throws Exception {
        FileInputStream fis = new FileInputStream(
                System.getProperty("user.home") + "\\Publicaciones-2021_09_25-18_31.xlsx"
        );
        XSSFWorkbook book = new XSSFWorkbook(fis);
        XSSFSheet hoja = book.getSheet("Publicaciones");
        int rows = hoja.getLastRowNum();
        for (int i = 0; i <= rows; i++) {
            XSSFRow fila = hoja.getRow(i);
            int columnas = fila.getLastCellNum();
            for (int j = 0; j < columnas; j++) {
                XSSFCell celda = fila.getCell(j);
                if (celda != null) {
                    switch (celda.getCellTypeEnum().toString()) {
                        case "STRING":
                        case "BLANK":
                            System.out.print(celda.getStringCellValue());
                            break;
                        case "NUMERIC":
                            System.out.print(celda.getNumericCellValue());
                            break;
                        default:
                            System.out.print(celda.getStringCellValue());
                    }
                    System.out.print("\t");
                }
            }
            System.out.println();
        }
    }
    
    static void crearExcel() throws Exception {
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("Jeff");
        FileOutputStream fos = new FileOutputStream(
                System.getProperty("user.home") + "\\Excel.xlsx"
        );
        book.write(fos);
        fos.close();
    }
    
    static float similarText(String first, String second) {
        first = first.toLowerCase();
        second = second.toLowerCase();
        return (float) (similar(first, second) * 200) / (first.length() + second.length());
    }
    
    static int similar(String first, String second) {
        int p, q, l, sum;
        int pos1 = 0;
        int pos2 = 0;
        int max = 0;
        char[] arr1 = first.toCharArray();
        char[] arr2 = second.toCharArray();
        int firstLength = arr1.length;
        int secondLength = arr2.length;
        
        for (p = 0; p < firstLength; p++) {
            for (q = 0; q < secondLength; q++) {
                for (l = 0; (p + l < firstLength) && (q + l < secondLength) && (arr1[p + l] == arr2[q + l]); l++);
                if (l > max) {
                    max = l;
                    pos1 = p;
                    pos2 = q;
                }
                
            }
        }
        sum = max;
        if (sum > 0) {
            if (pos1 > 0 && pos2 > 0) {
                sum += similar(first.substring(0, pos1 > firstLength ? firstLength : pos1), second.substring(0, pos2 > secondLength ? secondLength : pos2));
            }
            
            if ((pos1 + max < firstLength) && (pos2 + max < secondLength)) {
                sum += similar(first.substring(pos1 + max, firstLength), second.substring(pos2 + max, secondLength));
            }
        }
        return sum;
    }
    
}
