package ec.edu.ups.vista;

import ec.edu.ups.modelo.Producto;

import javax.swing.*;
import java.util.List;

public class ProductoAnadirView extends JInternalFrame {

    private JPanel panelPrincipal;
    private JTextField campoPrecio;
    private JTextField campoNombre;
    private JTextField campoCodigo;
    private JButton btnAceptar;
    private JButton btnLimpiar;
    private JLabel textoPrecio;
    private JLabel textoNombre;
    private JLabel textoCodigo;

    public ProductoAnadirView() {

        setContentPane(panelPrincipal);
        setTitle("Datos del Producto");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(400, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        //setVisible(true);

    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void limpiarCampos() {
        campoCodigo.setText("");
        campoNombre.setText("");
        campoPrecio.setText("");
    }

    public void mostrarProductos(List<Producto> productos) {
        for (Producto producto : productos) {
            System.out.println(producto);
        }
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTextField getCampoPrecio() {
        return campoPrecio;
    }

    public void setCampoPrecio(JTextField campoPrecio) {
        this.campoPrecio = campoPrecio;
    }

    public JTextField getCampoNombre() {
        return campoNombre;
    }

    public void setCampoNombre(JTextField campoNombre) {
        this.campoNombre = campoNombre;
    }

    public JTextField getCampoCodigo() {
        return campoCodigo;
    }

    public void setCampoCodigo(JTextField campoCodigo) {
        this.campoCodigo = campoCodigo;
    }

    public JButton getBtnAceptar() {
        return btnAceptar;
    }

    public void setBtnAceptar(JButton btnAceptar) {
        this.btnAceptar = btnAceptar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public void setBtnLimpiar(JButton btnLimpiar) {
        this.btnLimpiar = btnLimpiar;
    }

    public JLabel getTextoPrecio() {
        return textoPrecio;
    }

    public void setTextoPrecio(JLabel textoPrecio) {
        this.textoPrecio = textoPrecio;
    }

    public JLabel getTextoNombre() {
        return textoNombre;
    }

    public void setTextoNombre(JLabel textoNombre) {
        this.textoNombre = textoNombre;
    }

    public JLabel getTextoCodigo() {
        return textoCodigo;
    }

    public void setTextoCodigo(JLabel textoCodigo) {
        this.textoCodigo = textoCodigo;
    }

}
