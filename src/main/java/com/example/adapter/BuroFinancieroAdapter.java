package com.example.adapter;

import com.example.model.Cliente;
import com.example.model.Score;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Segunda fuente (ejemplo) para poder cambiar la implementación sin tocar
 * servicios.
 */
@Component // ⭐ CAMBIO 1: @Service → @Component (o quitar anotación)
public class BuroFinancieroAdapter implements ScoreProvider {

    @Override
    public Score obtenerScore(String clienteId) { // ⭐ CAMBIO 2: Cliente → String
        Score score = new Score();
        score.setClienteId(clienteId);

        // Score alto por defecto para testing
        score.setValor(750);
        score.setFuente("BURO");
        // ⭐ CAMBIO 3: Remover setFechaConsulta si no existe en Score

        return score;
    }
}