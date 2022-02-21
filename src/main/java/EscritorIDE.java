import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;

public class EscritorIDE extends JFrame {

    private JPanel principal;
    private JTextArea escritura;
    private JScrollPane lugarTerminal;
    private JTextArea terminalReal;

    private File archivo;
    private Clipboard clipboard;
    private UndoManager deshacerManager;

    // Declaracion de objetos del menu superior
    private JMenuBar menu;

    private JMenu mArchivo;
        private JMenuItem mNuevo;
        private JMenuItem mAbrir;
        private JMenuItem mGuardar;
        private JMenuItem mGuardarComo;
        private JMenuItem mImprimir;

    private JMenu mEdicion;
        private JMenuItem mDeshacer;
        private JMenuItem mRehacer;
        private JMenuItem mCopiar;
        private JMenuItem mCortar;
        private JMenuItem mPegar;
        private JMenuItem mEliminar;

    private JMenu mAyuda;
        private JMenuItem mAcercaDe;
        private JMenuItem mVerAyuda;

    private JButton compilar, ejecutar, salir;



    EscritorIDE(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initBarraSuperior();
        init();
        listeners();
    }


    private void init(){
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        initTerminal();
        principal.setPreferredSize(new Dimension(500,400));
        escritura.setFocusTraversalPolicyProvider(true);
        deshacerManager = new UndoManager();
        this.setTitle("IDE_REMAKE - " + "Sin Título");
        this.setJMenuBar(menu);
        this.add(principal);
        this.pack();

    }

    private void initTerminal(){
        terminalReal.setBackground(Color.DARK_GRAY);
        terminalReal.setForeground(Color.WHITE);
        terminalReal.setEditable(false);
        terminalReal.setBorder(new EmptyBorder(10,20,10,10));

    }

    private void initBarraSuperior(){

        menu = new JMenuBar();

        compilar = new JButton("Compilar");
        ejecutar = new JButton("Run");
        salir = new JButton("Salir");


        // MENU ARCHIVO
        mArchivo = new JMenu("Archivo");
        mNuevo = new JMenuItem("Nuevo");
        mAbrir = new JMenuItem("Abrir");
        mGuardar = new JMenuItem("Guardar");
        mGuardarComo = new JMenuItem("Guardar como");
        mImprimir = new JMenuItem("Imprimir");

        // Agregacion al menu Archivo
        mArchivo.add(mNuevo);
        mArchivo.add(mAbrir);
        mArchivo.add(mGuardar);
        mArchivo.add(mGuardarComo);
        mArchivo.add(mImprimir);

        // MENU EDICION
        mEdicion = new JMenu("Edicion");
        mDeshacer = new JMenuItem("Deshacer");
        mRehacer = new JMenuItem("Rehacer");
        mCopiar = new JMenuItem("Copiar");
        mCortar = new JMenuItem("Cortar");
        mPegar = new JMenuItem("Pegar");
        mEliminar = new JMenuItem("Eliminar");

        // Agregacion al menu Edicion
        mEdicion.add(mEdicion);
        mEdicion.add(mDeshacer);
        mEdicion.add(mRehacer);
        mEdicion.add(mCopiar);
        mEdicion.add(mCortar);
        mEdicion.add(mPegar);
        mEdicion.add(mEliminar);

        // MENU AYUDA
        mAyuda = new JMenu("Ayuda");
        mAcercaDe = new JMenuItem("Acerca de");
        mVerAyuda = new JMenuItem("Ver ayuda");

        // Agregacion al menu Ayuda
        mAyuda.add(mAcercaDe);
        mAyuda.add(mVerAyuda);

        //Agregacion al Menu Principal
        menu.add(mArchivo);
        menu.add(mEdicion);
        menu.add(mAyuda);
        menu.add(compilar);
        menu.add(ejecutar);
        menu.add(salir);
    }

    private void listeners(){

        // Listeners Archivo
        mNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoAccion();
            }
        });

        mAbrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    abrirAccion();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        mGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    guardarAccion();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        mGuardarComo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarComoAccion();
            }
        });

        mImprimir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    escritura.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Listeners Edicion
        escritura.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                deshacerManager.addEdit(e.getEdit());
            }
        });

        mCopiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { copiarAccion();

            }
        });

        mCortar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { cortarAccion();

            }
        });

        mDeshacer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { deshacerAccion();

            }
        });

        mPegar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { pegarAccion();

            }
        });

        mRehacer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { rehacerAccion();

            }

        });

        mEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { eliminarAccion();

            }
        });

        // Listeners Ayuda
        mAcercaDe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inf();
            }
        });

        mVerAyuda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    url();
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Listeners Botones

        compilar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    compilacion();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        ejecutar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    run();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }


    // METODOS ESPECIFICOS

        // METODOS ARCHIVO
    private void nuevoAccion(){
        archivo = null;
        escritura.setText("");
        this.setTitle("IDE_REMAKE - " + "Sin Título");
    }

    private void abrirAccion() throws IOException {
        if(!escritura.getText().equals("")){
            if(JOptionPane.showConfirmDialog(mAbrir, "Deberias guardar el archivo actual","Warning", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION);
            guardarAccion();
        }
        JFileChooser selector = new JFileChooser();

        int opcion = selector.showOpenDialog(this);
        archivo = selector.getSelectedFile();
        this.setTitle("IDE_REMAKE - " + archivo.getName());
        if(opcion == JFileChooser.APPROVE_OPTION){
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            escritura.setText("");
            String line = br.readLine();
            while(!(line == null)){
                escritura.append(line + "\n");
                line = br.readLine();
            }br.close();
        }
    }

    private void guardarComoAccion(){

        JFileChooser selector=new JFileChooser();
        int opcion = selector.showSaveDialog(this);
        archivo = selector.getSelectedFile();
        try (FileWriter escritor = new FileWriter(archivo)) {
            if (opcion == JFileChooser.APPROVE_OPTION)
                if(archivo !=null)
                    escritor.write(escritura.getText());
            JOptionPane.showMessageDialog(null, "El archivo se ha guardado satisfactoriamente");
        }
        catch(IOException e) {
            System.out.println("Error: "+ e.getMessage());
        }
    }

    private void guardarAccion() throws IOException {
        if (archivo == null){
            guardarComoAccion();
        }else {
            FileWriter escritor = new FileWriter(archivo);
            escritor.write(escritura.getText());
            JOptionPane.showMessageDialog(null, "El archivo se ha guardado satisfactoriamente");
            escritor.close();
        }
    }

        // METODOS EDICION
    private void copiarAccion(){
         if (escritura.getSelectedText() != null){
             StringSelection selector = new StringSelection("" + escritura.getSelectedText());
             clipboard.setContents(selector,selector);
         }
    }


    private void cortarAccion(){

        if (escritura.getSelectedText() != null){
            StringSelection selector = new StringSelection("" + escritura.getSelectedText());
            clipboard.setContents(selector,selector);
            escritura.replaceSelection("");
        }
    }


    private void pegarAccion(){

        Transferable datos = clipboard.getContents(null);
        try{
            if (datos != null && datos.isDataFlavorSupported(DataFlavor.stringFlavor))
                escritura.replaceSelection("" + datos.getTransferData(DataFlavor.stringFlavor));
        } catch (UnsupportedFlavorException | IOException ex){System.err.println(ex);}
    }

    private void deshacerAccion(){
        if (deshacerManager.canUndo()){
            deshacerManager.undo();
        }
    }

    private void rehacerAccion(){
        if (deshacerManager.canRedo()){
            deshacerManager.redo();
        }
    }

    private void eliminarAccion(){
        if (escritura.getSelectedText() != null){
            escritura.replaceSelection("");
        }
    }

    // METODOS AYUDA
    private void inf(){
        JOptionPane.showMessageDialog(this,"MGR999; hijo del Sol y soberano del infierno en la Tierra.");
    }

    private void url() throws URISyntaxException, IOException {
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                java.net.URI uri = new java.net.URI("https://youtu.be/0azsKCi3DmM");
                desktop.browse(uri);
            }
        }
    }

    // METODOS TERMINAL
    private void run() throws IOException {

        if (Objects.equals(escritura.getText(), "") || archivo == null) {
            JOptionPane.showMessageDialog(null, " Selecciona un archivo válido ", "Dialogo de alerta",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            guardarAccion();
            Runtime cmd = Runtime.getRuntime();
            String runJava = "java " + archivo.getPath();
            leerEnConsola(cmd, runJava);
            JOptionPane.showMessageDialog(null, "En funcionamiento", "Message Dialog", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void compilacion() throws IOException {
        if (Objects.equals(escritura.getText(), "") || archivo == null) {
            JOptionPane.showMessageDialog(null, " Selecciona un archivo valido", "Dialogo de alerta",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            guardarAccion();
            Runtime cmd = Runtime.getRuntime();
            String buildJava = "javac " + archivo.getPath();
            leerEnConsola(cmd, buildJava);
            JOptionPane.showMessageDialog(null, "Compilacion realizada correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void leerEnConsola(Runtime cmd, String command) throws IOException {

        Process proc = cmd.exec(command);
        InputStream inputStream = proc.getInputStream();
        InputStream errorStream = proc.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
        BufferedReader inputBufferedReader = new BufferedReader(inputStreamReader);
        BufferedReader errorBufferedReader = new BufferedReader(errorStreamReader);
        String inputline = "";
        String errorline = "";
        terminalReal.setText("");
        terminalReal.setForeground(Color.WHITE);
        while ((inputline = inputBufferedReader.readLine()) != null) {
            terminalReal.append(inputline + "\n");
        }
        while ((errorline = errorBufferedReader.readLine()) != null) {
            terminalReal.append(errorline + "\n");
            terminalReal.setForeground(Color.RED);
        }
    }

}
