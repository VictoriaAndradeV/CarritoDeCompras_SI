package ec.edu.ups.vista;

import ec.edu.ups.util.IdiomaUsado;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Locale;

public class CuentaUsuarioView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField textNombre;
    private JButton cambiarButton;
    private JButton btnEliminarCuenta;
    private JButton btnCerrarSesion;
    private JButton btnActualizar;
    private JLabel lblTitulo;
    private JLabel lblUsuario;
    private JLabel lblContrasenia;
    private JTextField textApe;
    private JTextField textTelefo;
    private JTextField textFechaN;
    private JTextField textEmail;
    private JTextField textUsua;
    private JTextField textContra;
    private JComboBox<IdiomaUsado> comboBoxIdioma;
    private JLabel lblIdioma;
    private JLabel lblNombre;
    private JLabel lblApellido;
    private JLabel lblTelefono;
    private JLabel lblFechaN;
    private JLabel lblEmail;

    private MensajeInternacionalizacionHandler mih;

    public CuentaUsuarioView() {
        super("Cuenta de Usuario", true, true, false, true);
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        setSize(750, 600);

        idiomaComboBox();

        comboBoxIdioma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                IdiomaUsado sel = (IdiomaUsado) comboBoxIdioma.getSelectedItem();
                mih.setLenguaje(sel.getLocale().getLanguage(), sel.getLocale().getCountry());
                actualizarTextos();
            }
        });


        setIconoEscalado(cambiarButton, "imagenes/modificarDatos.png", 25, 25);
        setIconoEscalado(btnEliminarCuenta, "imagenes/icono_eliminar.png", 25, 25);
        setIconoEscalado(btnCerrarSesion, "imagenes/cerrarSesion.png", 25, 25);
        setIconoEscalado(btnActualizar, "imagenes/icono_actualizar.png", 25, 25);
    }

    private void setIconoEscalado(JButton boton, String ruta, int ancho, int alto) {
        try {
            java.net.URL url = getClass().getClassLoader().getResource(ruta);
            if (url != null) {
                Image imagen = new ImageIcon(url).getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                boton.setIcon(new ImageIcon(imagen));
                boton.setHorizontalTextPosition(SwingConstants.RIGHT);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen" + ruta + " → " + e.getMessage());
        }
    }

    public void setMensajeHandler(MensajeInternacionalizacionHandler mih) {
        this.mih = mih;
        actualizarTextos();
    }

    private void actualizarTextos() {
        setTitle(mih.get("sesionU.titulo"));
        lblTitulo.setText(mih.get("sesionU.titulo"));
        lblIdioma.setText(mih.get("login.txtIdioma"));
        lblUsuario.setText(mih.get("login.txtUsuario"));
        lblContrasenia.setText(mih.get("login.txtContrasenia"));
        lblNombre.setText(mih.get("registrar.txtNombre"));
        lblApellido.setText(mih.get("registrar.txtApellido"));
        lblTelefono.setText(mih.get("registrar.txtTelefono"));
        lblEmail.setText(mih.get("registrar.txtEmail"));
        lblFechaN.setText(mih.get("registrar.txtFechaN"));

        cambiarButton.setText(mih.get("sesionU.txtCambiar"));
        btnEliminarCuenta.setText(mih.get("sesionU.btnEliminar"));
        btnActualizar.setText(mih.get("sesionU.btnActualizar"));
        btnCerrarSesion.setText(mih.get("menu.salir.login"));

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    private void idiomaComboBox() {
        comboBoxIdioma.removeAllItems();
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("es", "EC"), "Español"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("en", "US"), "English"));
        comboBoxIdioma.addItem(new IdiomaUsado(new Locale("it", "IT"), "Italiano"));
        comboBoxIdioma.setSelectedIndex(0);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    //getters y setters
    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JTextField getTextContra() {
        return textContra;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTextField getTextNombre() {
        return textNombre;
    }

    public JButton getCambiarButton() {
        return cambiarButton;
    }

    public JButton getBtnEliminarCuenta() {
        return btnEliminarCuenta;
    }

    public JButton getBtnCerrarSesion() {
        return btnCerrarSesion;
    }

    public JTextField getTextApe() {
        return textApe;
    }

    public void setTextApe(JTextField textApe) {
        this.textApe = textApe;
    }

    public JTextField getTextTelefo() {
        return textTelefo;
    }

    public JTextField getTextFechaN() {
        return textFechaN;
    }

    public JTextField getTextEmail() {
        return textEmail;
    }

    public JTextField getTextUsua() {
        return textUsua;
    }

    public JLabel getLblNombre() {
        return lblNombre;
    }

    public void setLblNombre(JLabel lblNombre) {
        this.lblNombre = lblNombre;
    }
}
