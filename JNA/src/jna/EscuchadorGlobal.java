package jna;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

public class EscuchadorGlobal implements NativeMouseInputListener, NativeMouseWheelListener, NativeKeyListener {

    public static void main(String[] args) throws Exception {
        try {
            GlobalScreen.registerNativeHook();
        } catch (Exception ex) {
        }

        EscuchadorGlobal escuchador = new EscuchadorGlobal();

//        GlobalScreen.addNativeMouseListener(escuchador);
//        GlobalScreen.addNativeMouseMotionListener(escuchador);
//        GlobalScreen.addNativeMouseWheelListener(escuchador);
        GlobalScreen.addNativeKeyListener(escuchador);

        while (true) {
            Thread.sleep(100);
        }
    }
    
    @Override
    public void nativeMouseClicked(NativeMouseEvent evt) {
//        System.out.println("Mouse Clicked: " + evt.getClickCount());
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent evt) {
//        System.out.println("Mouse Pressed: " + evt.getButton());
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent evt) {
//        System.out.println("Mouse Released: " + evt.getButton());
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent evt) {
//        System.out.println("Mouse Moved: " + evt.getX() + ", " + evt.getY());
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent evt) {
//        System.out.println("Mouse Dragged: " + evt.getX() + ", " + evt.getY());
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent evt) {
//        System.out.println("Se ha movido el scroll: " + evt.getWheelRotation());
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent evt) {
        
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent evt) {
        String keyText = NativeKeyEvent.getKeyText(evt.getKeyCode());
        System.out.println("tecla liberada: " + keyText);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent evt) {
    }

}
