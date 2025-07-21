package ec.edu.ups.vista;

import ec.edu.ups.modelo.PreguntaSeguridad;
import ec.edu.ups.modelo.RespuestaDeSeguridad;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
/**
 * Diálogo modal para mostrar un conjunto de preguntas de seguridad
 * y recopilar las respuestas del usuario.
 * <p>
 * Construye dinámicamente un formulario con tantas filas como preguntas,
 * valida que se hayan respondido al menos un mínimo de ellas,
 * y devuelve las respuestas emparejadas con sus preguntas.
 * </p>
 */
public class PreguntasSeguridadView extends JDialog {
    private final List<PreguntaSeguridad> preguntas;
    private final List<JTextField> campos = new ArrayList<>(); //textFild donde se digitan las respuestas del usuario
    private boolean comprobarRespuestasCompletadas = false;
    private final int minimoRespuestasRequeridas;
    private MensajeInternacionalizacionHandler mih;
    /**
     * Constructor que inicializa el diálogo con preguntas y mínimo de respuestas.
     *
     * @param preguntas             Lista de preguntas de seguridad.
     * @param minimoRespuestas      Número mínimo de respuestas obligatorias.
     * @param mih                   Manejador de mensajes internacionalizados.
     */
    public PreguntasSeguridadView(List<PreguntaSeguridad> preguntas, int minimoRespuestas, MensajeInternacionalizacionHandler mih) {
        super();
        this.preguntas = preguntas;
        this.minimoRespuestasRequeridas = minimoRespuestas;
        this.mih = mih;

        setTitle(mih.get("preguntaS.titulo"));
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buildUI();
        pack(); //tamaño de los componentes
        setLocationRelativeTo(null);
    }
    /**
     * Construye la interfaz: etiquetas, campos de texto y botones.
     */
    private void buildUI() {
        JPanel form = new JPanel(new GridLayout(preguntas.size(), 2, 5, 5));
        for (PreguntaSeguridad p : preguntas) {
            JLabel lbl = new JLabel(mih.get(p.getClave()) + ":");
            JTextField tf = new JTextField(20);// ancho del textField
            campos.add(tf);
            form.add(lbl);
            form.add(tf);
        }

        JButton btnCancelar = new JButton(mih.get("carrito.btnCancelar"));
        JButton btnAceptar  = new JButton(mih.get("agregar.btnAceptar"));

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAccept();
            }
        });

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnCancelar);
        botones.add(btnAceptar);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(botones, BorderLayout.SOUTH);
    }
    /**
     * Maneja la acción de Aceptar: valida respuestas y cierra si es válido.
     */
    private void onAccept() {

        int cont = 0;
        for(JTextField respuestas: campos) {
            if(!respuestas.getText().isEmpty()) { //contamos campos no vacios
                cont++;
            }
        }

        if (cont < minimoRespuestasRequeridas) {
            JOptionPane.showMessageDialog(
                    this,
                    mih.get("preguntaS.error.Responder"),
                    mih.get("preguntaS.tituloCompleto"),
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        comprobarRespuestasCompletadas = true;
        dispose();
    }

    /**
     * Indica si el usuario aceptó tras ingresar suficientes respuestas.
     *
     * @return {@code true} si aceptó y completó el mínimo de respuestas.
     */
    public boolean isSubmitted() {
        return comprobarRespuestasCompletadas;
    }

    /**
     * Recopila y devuelve las respuestas ingresadas, en el mismo orden que las preguntas.
     * Retorna lista vacía si no se completó el diálogo.
     *
     * @return Lista de objetos RespuestaDeSeguridad.
     */
    public List<RespuestaDeSeguridad> getRespuestas() {
        List<RespuestaDeSeguridad> respuestas = new ArrayList<>();
        if (!comprobarRespuestasCompletadas) return respuestas;
        for (int i = 0; i < preguntas.size(); i++) {
            String resp = campos.get(i).getText().trim();
            respuestas.add(new RespuestaDeSeguridad(preguntas.get(i), resp));
        }
        return respuestas;
    }
}
