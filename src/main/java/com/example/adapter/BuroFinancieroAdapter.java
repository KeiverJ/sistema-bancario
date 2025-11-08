package com.example.adapter;

import com.example.model.Cliente;
import com.example.model.Score;
import com.example.repository.ClienteRepository;
import org.springframework.stereotype.Component;

/**
 * Adaptador que obtiene el score del cliente desde la base de datos
 */
@Component
public class BuroFinancieroAdapter implements ScoreProvider {

    private final ClienteRepository clienteRepository;

    public BuroFinancieroAdapter(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Score obtenerScore(String clienteId) {
        // ✅ Obtener el cliente real de la base de datos
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        Score score = new Score();
        score.setClienteId(clienteId);

        // ✅ Usar el score REAL del cliente
        score.setValor(cliente.getScoreActual());
        score.setFuente("BURO");

        return score;
    }
}