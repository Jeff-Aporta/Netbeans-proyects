package HerramientaArchivos.ArrastrarArchivos;

import HerramientaArchivos.LectoEscrituraArchivos;
import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTargetListener;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class ArrastrarArchivos {

    private transient Border normalBorder;
    private transient DropTargetListener dropListener;

    private static Boolean supportsDnD;

    public static Color ColorBorde = new java.awt.Color(0f, 0f, 1f, 0.25f);

    public static void Añadir(Escuchador_ArchivosArrastrados escuchador, Component... componentes) {
        for (Component componente : componentes) {
            Añadir(componente, escuchador);
        }
    }

    public static void Añadir(Component componente, Escuchador_ArchivosArrastrados escuchador) {
        new ArrastrarArchivos(componente, escuchador);
    }

    protected ArrastrarArchivos(Component componente, Escuchador_ArchivosArrastrados escuchador) {
        this(
                null, componente,
                BorderFactory.createMatteBorder(2, 2, 2, 2, ColorBorde),
                true, escuchador
        );
    }

    protected ArrastrarArchivos(Component componente, boolean recursive, Escuchador_ArchivosArrastrados listener) {
        this(
                null, componente,
                BorderFactory.createMatteBorder(2, 2, 2, 2, ColorBorde), // Drag border
                recursive, listener
        );
    }

    protected ArrastrarArchivos(PrintStream out, Component c, Escuchador_ArchivosArrastrados listener) {
        this(out, // Logging stream
                c, // Drop target
                BorderFactory.createMatteBorder(2, 2, 2, 2, ColorBorde),
                false, // Recursive
                listener);
    }

    protected ArrastrarArchivos(
            PrintStream out,
            Component c,
            boolean recursive,
            Escuchador_ArchivosArrastrados listener) {
        this(out, // Logging stream
                c, // Drop target
                BorderFactory.createMatteBorder(2, 2, 2, 2, ColorBorde), // Drag border
                recursive, // Recursive
                listener);
    }

    protected ArrastrarArchivos(Component c, Border dragBorder, Escuchador_ArchivosArrastrados listener) {
        this(
                null, // Logging stream
                c, // Drop target
                dragBorder, // Drag border
                false, // Recursive
                listener
        );
    }

    protected ArrastrarArchivos(Component c, Border dragBorder, boolean recursive, Escuchador_ArchivosArrastrados listener) {
        this(null, c, dragBorder, recursive, listener);
    }

    protected ArrastrarArchivos(
            PrintStream out,
            Component c,
            Border dragBorder,
            Escuchador_ArchivosArrastrados listener) {
        this(
                out, // Logging stream
                c, // Drop target
                dragBorder, // Drag border
                false, // Recursive
                listener
        );
    }

    protected ArrastrarArchivos(PrintStream out, Component c, Border dragBorder, boolean recursive, Escuchador_ArchivosArrastrados listener) {
        if (supportsDnD()) {   // Make a drop listener
            dropListener = new java.awt.dnd.DropTargetListener() {
                public void dragEnter(java.awt.dnd.DropTargetDragEvent evt) {
                    log(out, "FileDrop: dragEnter event.");
                    // Is this an acceptable drag event?
                    if (isDragOk(out, evt)) {
                        // If it's a Swing component, set its border
                        if (c instanceof javax.swing.JComponent) {
                            javax.swing.JComponent jc = (javax.swing.JComponent) c;
                            normalBorder = jc.getBorder();
                            log(out, "FileDrop: normal border saved.");
                            jc.setBorder(dragBorder);
                            log(out, "FileDrop: drag border set.");
                        }
                        evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY);
                        log(out, "FileDrop: event accepted.");
                    } else {   // Reject the drag event
                        evt.rejectDrag();
                        log(out, "FileDrop: event rejected.");
                    }
                }

                public void dragOver(java.awt.dnd.DropTargetDragEvent evt) {   // This is called continually as long as the mouse is
                    // over the drag target.
                }

                public void drop(java.awt.dnd.DropTargetDropEvent evt) {
                    log(out, "FileDrop: drop event.");
                    try {   // Get whatever was dropped
                        java.awt.datatransfer.Transferable tr = evt.getTransferable();

                        if (tr.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.javaFileListFlavor)) {
                            evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
                            log(out, "FileDrop: file list accepted.");

                            java.util.List fileList = (java.util.List) tr.getTransferData(java.awt.datatransfer.DataFlavor.javaFileListFlavor);

                            File[] filesTemp = new File[fileList.size()];
                            fileList.toArray(filesTemp);
                            String[] cadenas = new String[fileList.size()];
                            for (int i = 0; i < cadenas.length; i++) {
                                cadenas[i] = filesTemp[i].getPath();
                                if (cadenas[i].endsWith(".url")) {
                                    try {
                                        cadenas[i] = LectoEscrituraArchivos.LeerArchivo_ASCII(cadenas[i])
                                                .replace("\n", "")
                                                .replace("[InternetShortcut]", "")
                                                .replace("URL=", "");
                                    } catch (Exception ex) {
                                    }
                                }
                            }
                            if (listener != null) {
                                listener.filesDropped(cadenas);
                            }

                            evt.getDropTargetContext().dropComplete(true);
                            log(out, "FileDrop: drop complete.");
                        } else // this section will check for a reader flavor.
                        {
                            // Thanks, Nathan!
                            // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
                            DataFlavor[] flavors = tr.getTransferDataFlavors();
                            boolean handled = false;
                            for (int zz = 0; zz < flavors.length; zz++) {
                                if (flavors[zz].isRepresentationClassReader()) {
                                    // Say we'll take it.
                                    //evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                                    evt.acceptDrop(java.awt.dnd.DnDConstants.ACTION_COPY);
                                    log(out, "FileDrop: reader accepted.");

                                    Reader reader = flavors[zz].getReaderForText(tr);

                                    BufferedReader br = new BufferedReader(reader);

                                    if (listener != null) {
                                        listener.filesDropped(createFileArray(br, out));
                                    }

                                    // Mark that drop is completed.
                                    evt.getDropTargetContext().dropComplete(true);
                                    log(out, "FileDrop: drop complete.");
                                    handled = true;
                                    break;
                                }
                            }
                            if (!handled) {
                                log(out, "FileDrop: not a file list or reader - abort.");
                                evt.rejectDrop();
                            }
                            // END 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
                        }
                    } catch (IOException io) {
                        log(out, "FileDrop: IOException - abort:");
                        io.printStackTrace(out);
                        evt.rejectDrop();
                    } catch (java.awt.datatransfer.UnsupportedFlavorException ufe) {
                        log(out, "FileDrop: UnsupportedFlavorException - abort:");
                        ufe.printStackTrace(out);
                        evt.rejectDrop();
                    } finally {
                        if (c instanceof javax.swing.JComponent) {
                            javax.swing.JComponent jc = (javax.swing.JComponent) c;
                            jc.setBorder(normalBorder);
                            log(out, "FileDrop: normal border restored.");
                        }
                    }
                }

                public void dragExit(java.awt.dnd.DropTargetEvent evt) {
                    log(out, "FileDrop: dragExit event.");

                    if (c instanceof javax.swing.JComponent) {
                        javax.swing.JComponent jc = (javax.swing.JComponent) c;
                        jc.setBorder(normalBorder);
                        log(out, "FileDrop: normal border restored.");
                    }
                }

                public void dropActionChanged(java.awt.dnd.DropTargetDragEvent evt) {
                    log(out, "FileDrop: dropActionChanged event.");
                    if (isDragOk(out, evt)) {   //evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                        evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY);
                        log(out, "FileDrop: event accepted.");
                    } else {
                        evt.rejectDrag();
                        log(out, "FileDrop: event rejected.");
                    }
                }
            };

            makeDropTarget(out, c, recursive);
        } else {
            log(out, "FileDrop: Drag and drop is not supported with this JVM");
        }
    }

    private static boolean supportsDnD() {   // Static Boolean
        if (supportsDnD == null) {
            supportsDnD = false;
            try {
                Class arbitraryDndClass = Class.forName("java.awt.dnd.DnDConstants");
                supportsDnD = true;
            } catch (Exception e) {
                supportsDnD = false;
            }
        }
        return supportsDnD.booleanValue();
    }

    private static String ZERO_CHAR_STRING = "" + (char) 0;

    private static String[] createFileArray(BufferedReader bReader, PrintStream out) {
        try {
            java.util.List list = new java.util.ArrayList();
            java.lang.String line = null;
            while ((line = bReader.readLine()) != null) {
                try {
                    if (ZERO_CHAR_STRING.equals(line)) {
                        continue;
                    }
                    if (new File(line).exists()) {
                        list.add(line);
                    }
                    String texto = LectoEscrituraArchivos.EliminarCódigoHTML_TextoPlano(line);
                    list.add(texto);
                } catch (Exception ex) {
                    log(out, "Error with " + line + ": " + ex.getMessage());
                }
            }
            return (String[]) list.toArray(new String[list.size()]);
        } catch (IOException ex) {
            log(out, "FileDrop: IOException");
        }
        return new String[0];
    }

    private void makeDropTarget(PrintStream out, Component c, boolean recursive) {
        // Make drop target
        java.awt.dnd.DropTarget dt = new java.awt.dnd.DropTarget();
        try {
            dt.addDropTargetListener(dropListener);
        } catch (java.util.TooManyListenersException e) {
            e.printStackTrace();
            log(out, "FileDrop: Drop will not work due to previous error. Do you have another listener attached?");
        }

        c.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                log(out, "FileDrop: Hierarchy changed.");
                Component parent = c.getParent();
                if (parent == null) {
                    c.setDropTarget(null);
                    log(out, "FileDrop: Drop target cleared from component.");
                } else {
                    new java.awt.dnd.DropTarget(c, dropListener);
                    log(out, "FileDrop: Drop target added to component.");
                }
            }
        });
        if (c.getParent() != null) {
            new java.awt.dnd.DropTarget(c, dropListener);
        }

        if (recursive && (c instanceof java.awt.Container)) {
            // Get the container
            java.awt.Container cont = (java.awt.Container) c;

            // Get it's components
            Component[] comps = cont.getComponents();

            // Set it's components as listeners also
            for (int i = 0; i < comps.length; i++) {
                makeDropTarget(out, comps[i], recursive);
            }
        }
    }

    private boolean isDragOk(PrintStream out, java.awt.dnd.DropTargetDragEvent evt) {
        boolean ok = false;

        // Get data flavors being dragged
        java.awt.datatransfer.DataFlavor[] flavors = evt.getCurrentDataFlavors();

        // See if any of the flavors are a file list
        int i = 0;
        while (!ok && i < flavors.length) {
            // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
            // Is the flavor a file list?
            DataFlavor curFlavor = flavors[i];
            if (curFlavor.equals(java.awt.datatransfer.DataFlavor.javaFileListFlavor)
                    || curFlavor.isRepresentationClassReader()) {
                ok = true;
            }
            // END 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
            i++;
        }

        // If logging is enabled, show data flavors
        if (out != null) {
            if (flavors.length == 0) {
                log(out, "FileDrop: no data flavors.");
            }
            for (i = 0; i < flavors.length; i++) {
                log(out, flavors[i].toString());
            }
        }

        return ok;
    }

    private static void log(PrintStream out, String message) {   // Log message if requested
        if (out != null) {
            out.println(message);
        }
    }

    public static boolean remove(Component c) {
        return remove(null, c, true);
    }

    /**
     * Removes the drag-and-drop hooks from the component and optionally from
     * the all children. You should call this if you add and remove components
     * after you've set up the drag-and-drop.
     *
     * @param out Optional {@link PrintStream} for logging drag and drop
     * messages
     * @param c The component to unregister
     * @param recursive Recursively unregister components within a container
     * @since 1.0
     */
    public static boolean remove(PrintStream out, Component c, boolean recursive) {   // Make sure we support dnd.
        if (supportsDnD()) {
            log(out, "FileDrop: Removing drag-and-drop hooks.");
            c.setDropTarget(null);
            if (recursive && (c instanceof java.awt.Container)) {
                Component[] comps = ((java.awt.Container) c).getComponents();
                for (int i = 0; i < comps.length; i++) {
                    remove(out, comps[i], recursive);
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static interface Escuchador_ArchivosArrastrados {

        public abstract void filesDropped(String[] rutas);

    }

    public static class Event extends java.util.EventObject {

        private File[] files;

        public Event(File[] files, Object source) {
            super(source);
            this.files = files;
        }

        public File[] getFiles() {
            return files;
        }

    }

    public static class TransferableObject implements java.awt.datatransfer.Transferable {

        public static String MIME_TYPE = "application/x-net.iharder.dnd.TransferableObject";

        public static java.awt.datatransfer.DataFlavor DATA_FLAVOR
                = new java.awt.datatransfer.DataFlavor(ArrastrarArchivos.TransferableObject.class, MIME_TYPE);

        private Fetcher fetcher;
        private Object data;

        private java.awt.datatransfer.DataFlavor customFlavor;

        public TransferableObject(Object data) {
            this.data = data;
            this.customFlavor = new java.awt.datatransfer.DataFlavor(data.getClass(), MIME_TYPE);
        }

        public TransferableObject(Fetcher fetcher) {
            this.fetcher = fetcher;
        }

        public TransferableObject(Class dataClass, Fetcher fetcher) {
            this.fetcher = fetcher;
            this.customFlavor = new java.awt.datatransfer.DataFlavor(dataClass, MIME_TYPE);
        }

        public java.awt.datatransfer.DataFlavor getCustomDataFlavor() {
            return customFlavor;
        }

        public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
            if (customFlavor != null) {
                return new java.awt.datatransfer.DataFlavor[]{customFlavor,
                    DATA_FLAVOR,
                    java.awt.datatransfer.DataFlavor.stringFlavor
                };
            } else {
                return new java.awt.datatransfer.DataFlavor[]{DATA_FLAVOR,
                    java.awt.datatransfer.DataFlavor.stringFlavor
                };
            }
        }

        public Object getTransferData(java.awt.datatransfer.DataFlavor flavor)
                throws java.awt.datatransfer.UnsupportedFlavorException, IOException {
            // Native object
            if (flavor.equals(DATA_FLAVOR)) {
                return fetcher == null ? data : fetcher.getObject();
            }

            // String
            if (flavor.equals(java.awt.datatransfer.DataFlavor.stringFlavor)) {
                return fetcher == null ? data.toString() : fetcher.getObject().toString();
            }

            // We can't do anything else
            throw new java.awt.datatransfer.UnsupportedFlavorException(flavor);
        }

        public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
            // Native object
            if (flavor.equals(DATA_FLAVOR)) {
                return true;
            }

            // String
            if (flavor.equals(java.awt.datatransfer.DataFlavor.stringFlavor)) {
                return true;
            }

            // We can't do anything else
            return false;
        }

        public static interface Fetcher {

            /**
             * Return the object being encapsulated in the
             * {@link TransferableObject}.
             *
             * @return The dropped object
             * @since 1.1
             */
            public abstract Object getObject();
        }

    }

}
