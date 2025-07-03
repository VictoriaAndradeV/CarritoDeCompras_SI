package ec.edu.ups.vista;

import ec.edu.ups.modelo.PreguntaSeguridad;
import ec.edu.ups.modelo.RespuestaDeSeguridad;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PreguntasSeguridadView extends JDialog {
    private final List<PreguntaSeguridad> preguntas;
    private final List<JTextField> campos = new ArrayList<>();
    private boolean submitted;

    public PreguntasSeguridadView(List<PreguntaSeguridad> preguntas,MensajeInternacionalizacionHandler mih) {
        super();
        this.preguntas = preguntas;

        //config ventana dialog
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(mih.get("preguntaS.titulo"));

        buildUI(mih);
        pack();//ayuda a ajustar tama del contenid
        setLocationRelativeTo(null);
    }

    private void buildUI(MensajeInternacionalizacionHandler mih) {
        // Formulario: N filas, 2 columnas
        JPanel form = new JPanel(new GridLayout(preguntas.size(), 2, 5, 5));
        for (PreguntaSeguridad p : preguntas) {
            // etiqueta traducida
            JLabel lbl = new JLabel(mih.get(p.getClave()) + ":");
            JTextField tf = new JTextField(20);
            campos.add(tf);
            form.add(lbl);
            form.add(tf);
        }

        //usamos el bundle
        JButton btnCancelar = new JButton(mih.get("carrito.btnCancelar"));
        JButton btnAceptar  = new JButton(mih.get("agregar.btnAceptar"));
        btnCancelar.addActionListener(e -> dispose());
        btnAceptar.addActionListener(e -> onAccept(mih));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnCancelar);
        botones.add(btnAceptar);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
    }

    private void onAccept(MensajeInternacionalizacionHandler mih) {
        //validar que no existan campos vacios
        for (JTextField tf : campos) {
            if (tf.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        mih.get("preguntaS.mensajeError.vacio"),
                        mih.get("preguntaS.mensajeError.titulo"),
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        }
        submitted = true;
        dispose();
    }

    //me retorna true si el usuario coloco aceptar y completo todas las preguntas
    public boolean isSubmitted() {
        return submitted;
    }

    //me ayuda a listar las respuestas en el mismo orden que las pregunats
    public List<RespuestaDeSeguridad> getRespuestas() {
        List<RespuestaDeSeguridad> respuestas = new ArrayList<>();
        if (!submitted) return respuestas;
        for (int i = 0; i < preguntas.size(); i++) {
            String resp = campos.get(i).getText().trim();
            respuestas.add(new RespuestaDeSeguridad(preguntas.get(i), resp));
        }
        return respuestas;
    }
}
