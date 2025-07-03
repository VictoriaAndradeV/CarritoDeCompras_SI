package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.PreguntaSeguridadDAO;
import ec.edu.ups.modelo.PreguntaSeguridad;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PreguntaSeguridadDAOMemoria implements PreguntaSeguridadDAO {
    private final List<PreguntaSeguridad> banco;

    public PreguntaSeguridadDAOMemoria() {
        banco = List.of( //lista de preguntas
                new PreguntaSeguridad("pS.1"),
                new PreguntaSeguridad("pS.2"),
                new PreguntaSeguridad("pS.3"),
                new PreguntaSeguridad("pS.4"),
                new PreguntaSeguridad("pS.5"),
                new PreguntaSeguridad("pS.6"),
                new PreguntaSeguridad("pS.7"),
                new PreguntaSeguridad("pS.8"),
                new PreguntaSeguridad("pS.9"),
                new PreguntaSeguridad("pS.10" )
        );
    }

    @Override
    public List<PreguntaSeguridad> listarTodas() {
        return new ArrayList<>(banco);
    }

    @Override
    public List<PreguntaSeguridad> seleccionarAleatorias(int n) {
        //se realiza una copia
        List<PreguntaSeguridad> temp = new ArrayList<>(banco);
        List<PreguntaSeguridad> elegidas = new ArrayList<>();

        //se generan numeros aleatorios
        Random rnd = new Random();

        for (int i = 0; i < n && !temp.isEmpty(); i++) {
            //se elige un indice al azar
            int idx = rnd.nextInt(temp.size());
            // removemos de la lista temporal y lo añadimos al resultado
            elegidas.add(temp.remove(idx));
        }
        return elegidas;
    }
}
