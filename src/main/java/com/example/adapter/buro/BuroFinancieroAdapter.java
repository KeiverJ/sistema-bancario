package com.example.adapter.buro;

import com.example.adapter.score.ScoreProvider;
import com.example.model.cliente.Cliente;
import com.example.model.score.Score;
import com.example.repository.cliente.ClienteRepository;

/**
 * Adaptador que obtiene el score del cliente 
 */
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