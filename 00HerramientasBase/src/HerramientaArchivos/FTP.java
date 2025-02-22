package HerramientaArchivos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class FTP {

    public static final char ASCII = 'A', BIN = 'I';

    public static void main(String[] args) {
        Subir_Archivo(
                "files.000webhost.com",
                "jeffaporta01",
                "05101896",
                "/Fotos",
                new File("D:\\1 año 1 mes.png"),
                BIN
        );
    }

    public static void Subir_Archivo(String srvdr, String usr, String pw, String location, File archvo, char TYPE) {
        try (
                Socket socket = new Socket(srvdr, 21);
                BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            if (archvo.exists()) {
                escritor.write("USER " + usr + "\r\n");
                escritor.flush();
                escritor.write("PASS " + pw + "\r\n");
                escritor.flush();
                escritor.write("CWD " + location + "\r\n");
                escritor.flush();
                escritor.write("TYPE " + TYPE + "\r\n");
                escritor.flush();
                escritor.write("PASV\r\n");
                escritor.flush();
                String respuesta = null;
                while ((respuesta = lector.readLine()) != null) {
                    if (respuesta.startsWith("530")) {
                        System.err.println("Login aunthentication failed");
                        break;
                    }
                    if (respuesta.startsWith("227")) {
                        String address = null;
                        int port = -1;
                        int opening = respuesta.indexOf('(');
                        int closing = respuesta.indexOf(')', opening + 1);
                        if (closing > 0) {
                            String dataLink = respuesta.substring(opening + 1, closing);
                            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
                            try {
                                address = tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken() + "." + tokenizer.nextToken();
                                port = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Socket transfer = new Socket(address, port);
                                escritor.write("STOR " + archvo.getName() + "\r\n");
                                escritor.flush();
                                respuesta = lector.readLine();
                                if (respuesta.startsWith("150")) {

                                    try (
                                            FileInputStream in = new FileInputStream(archvo);
                                            FileOutputStream out = (FileOutputStream) transfer.getOutputStream();) {
                                        int c;
                                        while ((c = in.read()) != -1) {
                                            out.write(c);
                                        }
                                    } catch (Exception e) {
                                    }
                                    escritor.write("QUIT\r\n");
                                    escritor.flush();
                                    try {
                                        escritor.close();
                                        lector.close();
                                        socket.close();
                                    } catch (Exception e) {

                                    }
                                    System.out.println("File sucessfully transferred!");
                                    break;
                                }
                            } catch (Exception e) {
                                System.err.println(e);
                            }
                        }
                    }
                }
            } else {
                System.err.println(archvo + "no exist!");
            }
        } catch (Exception e) {
        }
    }
}
