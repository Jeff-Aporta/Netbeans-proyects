package jna;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import java.awt.Rectangle;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.Native;
import java.util.ArrayList;

public class JNA {

    public static void main(String[] args) throws Exception {
        for (String proceso : ObtenerProcesosActivos()) {
            System.out.println(proceso);
        }
    }

    public static ArrayList<String> ObtenerProcesosActivos() {
        Kernel32 kernel32 = (Kernel32) Native.loadLibrary(Kernel32.class, W32APIOptions.UNICODE_OPTIONS);
        Tlhelp32.PROCESSENTRY32.ByReference processEntry = new Tlhelp32.PROCESSENTRY32.ByReference();

        WinNT.HANDLE snapshot = kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));
        ArrayList<String> retorno = new ArrayList<>();
        try {
            while (kernel32.Process32Next(snapshot, processEntry)) {
                retorno.add(Native.toString(processEntry.szExeFile));
            }
        } finally {
            kernel32.CloseHandle(snapshot);
        }
        return retorno;
    }

    public static String ObtenerTituloVentanaActiva() {
        char[] buffer = new char[2048];
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.GetWindowText(hwnd, buffer, 1024);
        return Native.toString(buffer);
    }

    public static Rectangle ObtenerRectanguloVentanaActiva() {
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        RECT rect = new RECT();
        User32.INSTANCE.GetWindowRect(hwnd, rect);
        return rect.toRectangle();
    }

    public static void MostrarVentana(String titulo) {
        HWND hwnd = User32.INSTANCE.FindWindow(null, titulo); // window title
        if (hwnd == null) {
            System.out.println("Notepad window is not running");
        } else {
            User32.INSTANCE.ShowWindow(hwnd, User32.SW_RESTORE);
            User32.INSTANCE.SetForegroundWindow(hwnd);
        }
    }

    public static void MaximizarVentana(String titulo) {
        HWND hwnd = User32.INSTANCE.FindWindow(null, titulo); // window title
        if (hwnd == null) {
            System.out.println("Notepad window is not running");
        } else {
            User32.INSTANCE.ShowWindow(hwnd, User32.SW_MAXIMIZE);
            User32.INSTANCE.SetForegroundWindow(hwnd);
        }
    }

}
