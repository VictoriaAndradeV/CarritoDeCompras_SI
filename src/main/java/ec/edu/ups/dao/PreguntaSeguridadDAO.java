package ec.edu.ups.dao;

import ec.edu.ups.modelo.PreguntaSeguridad;

import java.util.List;

public interface PreguntaSeguridadDAO {

    List<PreguntaSeguridad> listarTodas(); //lista de preguntas

    //permite seleccionar preguntas aleatorias del banco
    List<PreguntaSeguridad> seleccionarAleatorias(int n);
}
