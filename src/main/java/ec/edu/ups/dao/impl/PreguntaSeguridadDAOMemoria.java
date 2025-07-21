package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.PreguntaSeguridadDAO;
import ec.edu.ups.modelo.PreguntaSeguridad;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PreguntaSeguridadDAOMemoria implements PreguntaSeguridadDAO {
    private final PreguntaSeguridad[] banco = new PreguntaSeguridad[10];

    public PreguntaSeguridadDAOMemoria(MensajeInternacionalizacionHandler mih) {
        banco[0] = new PreguntaSeguridad(mih.get("pS.1"));
        banco[1] = new PreguntaSeguridad(mih.get("pS.2"));
        banco[2] = new PreguntaSeguridad(mih.get("pS.3"));
        banco[3] = new PreguntaSeguridad(mih.get("pS.4"));
        banco[4] = new PreguntaSeguridad(mih.get("pS.5"));
        banco[5] = new PreguntaSeguridad(mih.get("pS.6"));
        banco[6] = new PreguntaSeguridad(mih.get("pS.7"));
        banco[7] = new PreguntaSeguridad(mih.get("pS.8"));
        banco[8] = new PreguntaSeguridad(mih.get("pS.9"));
        banco[9] = new PreguntaSeguridad(mih.get("pS.10"));
    }

    @Override
    public List<PreguntaSeguridad> listarTodas() {
        return Arrays.stream(banco).toList();
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
