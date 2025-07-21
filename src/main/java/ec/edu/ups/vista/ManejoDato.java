package ec.edu.ups.vista;

import ec.edu.ups.modelo.TipoManejoMemoria;
import ec.edu.ups.util.IdiomaUsado;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
/**
 * Diálogo modal para configurar el manejo de datos del sistema.
 * <p>
 * Extiende {@link JDialog}. Permite al usuario elegir el modo de persistencia
 * (memoria, archivo de texto o archivo binario), seleccionar la ruta de archivos
 * y ajustar el idioma de la interfaz.</p>
 */
public class ManejoDato extends JDialog {
    private JComboBox comboBoxMemoria;
    private JLabel txtUsoMemoria;
    private JTextField textFieldRuta;
    private JButton btnElegirRuta;
    private JButton btnAceptar;
    private JLabel txtRuta;
    private JComboBox comboBoxIdioma;
    private JPanel panelPrincipal;
    private JLabel txtIdioma;

    private MensajeInternacionalizacionHandler mih;
    /**
     * Construye el diálogo para seleccionar el modo de manejo de datos.
     * Inicializa idioma por defecto, componentes y listeners.
     */
    public ManejoDato() {
        super((Dialog) null, "Manejo de Memoria", true); // true = modal
        setTitle("Manejo de Datos");
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(450, 400);

        //idioma por defecto
        mih = new MensajeInternacionalizacionHandler("es", "EC");

        setIconoEscalado(btnAceptar, "imagenes/aceptar.png");
        setIconoEscalado(btnElegirRuta, "imagenes/icono_actualizar.png");

        idiomaComboBox();
        manejoArchivos();
        textFieldRuta.setEditable(false);
        btnElegirRuta.setEnabled(false);

        comboBoxIdioma.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IdiomaUsado sel = (IdiomaUsado) comboBoxIdioma.getSelectedItem();
                mih.setLenguaje(sel.getLocale().getLanguage(), sel.getLocale().getCountry());
                actualizarTextos();
            }
        });

        comboBoxMemoria.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean modoArchivo = getEscogerManejoArchivos() != TipoManejoMemoria.MANEJO_MEMORIA;

                if (!modoArchivo) {
                    // Limpia y bloquea si vuelven a elegir "Memoria"
                    textFieldRuta.setText("");
                    textFieldRuta.setEditable(false);
                    btnElegirRuta.setEnabled(false);
                }else {
                    textFieldRuta.setEditable(true);
                    btnElegirRuta.setEnabled(true);
                }
            }
        });

        btnElegirRuta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnSeleccionarRutaActionPerformed(e);
            }
        });
    }

    private void setIconoEscalado(JButton boton, String ruta) {
        int ancho = 25;
        int alto = 25;
        try {
            java.net.URL url = getClass().getClassLoader().getResource(ruta);
            if (url != null) {
                Image imagen = new ImageIcon(url).getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen " + ruta + " → " + e.getMessage());
        }
    }

    private void actualizarTextos() {
        setTitle(mih.get("manejoD.titulo"));
        txtIdioma.setText(mih.get("login.txtIdioma"));
        txtUsoMemoria.setText(mih.get("manejoD.uso.memoria"));
        txtRuta.setText(mih.get("manejoD.ruta"));
        btnElegirRuta.setText(mih.get("manejoD.btn.seleccionar"));
        btnAceptar.setText(mih.get("manejoD.btn.aceptar"));
    }

    public TipoManejoMemoria getEscogerManejoArchivos() {
        int sel = comboBoxMemoria.getSelectedIndex();
        if (sel==1) {
            return TipoManejoMemoria.MANEJO_ARCHIVO_TEXTO;
        }
        if (sel == 2) {
            return TipoManejoMemoria.MANEJO_ARCHIVO_BINARIO;
        }
        return TipoManejoMemoria.MANEJO_MEMORIA;
    }
    /**
     * Abre un JFileChooser para seleccionar la carpeta de archivos y bloquea
     * el campo de texto tras la selección.
     */
    private void btnSeleccionarRutaActionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(mih.get("login.seleccionar.carpeta"));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File carpeta = fc.getSelectedFile();
            textFieldRuta.setText(carpeta.getAbsolutePath());
            // Bloquea para que ni el usuario ni un cambio de idioma borren esta ruta
            textFieldRuta.setEditable(false);
            btnElegirRuta.setEnabled(false);
        }
    }

    private void manejoArchivos() {
        comboBoxMemoria.removeAllItems();

        comboBoxMemoria.addItem(mih.get("uso.memoria"));           // Memoria
        comboBoxMemoria.addItem(mih.get("uso.archivoTexto"));     // Archivo de texto
        comboBoxMemoria.addItem(mih.get("uso.archivoBinario"));   // Archivo binario

        comboBoxMemoria.setSelectedIndex(0);
    }

    private void idiomaComboBox() {
        comboBoxIdioma.removeAllItems();
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("es","EC"), "Español"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("en","US"), "English"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("it","IT"), "Italiano"));
        comboBoxIdioma.setSelectedIndex(0);
    }



    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public JComboBox getComboBoxMemoria() {
        return comboBoxMemoria;
    }

    public void setComboBoxMemoria(JComboBox comboBoxMemoria) {
        this.comboBoxMemoria = comboBoxMemoria;
    }

    public JLabel getTxtUsoMemoria() {
        return txtUsoMemoria;
    }

    public void setTxtUsoMemoria(JLabel txtUsoMemoria) {
        this.txtUsoMemoria = txtUsoMemoria;
    }

    public JTextField getTextFieldRuta() {
        return textFieldRuta;
    }

    public void setTextFieldRuta(JTextField textFieldRuta) {
        this.textFieldRuta = textFieldRuta;
    }

    public JButton getBtnElegirRuta() {
        return btnElegirRuta;
    }

    public void setBtnElegirRuta(JButton btnElegirRuta) {
        this.btnElegirRuta = btnElegirRuta;
    }

    public JButton getBtnAceptar() {
        return btnAceptar;
    }

    public void setBtnAceptar(JButton btnAceptar) {
        this.btnAceptar = btnAceptar;
    }

    public JLabel getTxtRuta() {
        return txtRuta;
    }

    public void setTxtRuta(JLabel txtRuta) {
        this.txtRuta = txtRuta;
    }

    public JComboBox getComboBoxIdioma() {
        return comboBoxIdioma;
    }

    public void setComboBoxIdioma(JComboBox comboBoxIdioma) {
        this.comboBoxIdioma = comboBoxIdioma;
    }
}
