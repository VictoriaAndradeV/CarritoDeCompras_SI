package ec.edu.ups.vista;

import ec.edu.ups.modelo.Producto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductoAnadirView extends JFrame {

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        //setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        //pack();

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTextField getTxtPrecio() {
        return campoPrecio;
    }

    public void setTxtPrecio(JTextField txtPrecio) {
        this.campoPrecio = txtPrecio;
    }

    public JTextField getTxtNombre() {
        return campoNombre;
    }

    public void setTxtNombre(JTextField txtNombre) {
        this.campoNombre = txtNombre;
    }

    public JTextField getTxtCodigo() {
        return campoCodigo;
    }

    public void setTxtCodigo(JTextField txtCodigo) {
        this.campoCodigo = txtCodigo;
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
}
