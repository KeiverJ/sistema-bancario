package com.example.adapter;

import com.example.model.Cliente;
import com.example.model.Score;
import com.example.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba unitaria del adapter que obtiene score del repositorio de cliente.
 */
class AdapterUnitTest {

    @Test
    @DisplayName("Adapter obtiene score desde ClienteRepository")
    void adapter_obtieneScore() {
        ClienteRepository repo = new ClienteRepository();
        Cliente c = new Cliente();
        c.setNombre("Ana");
        c.setScoreActual(730);
        repo.save(c);

    BuroFinancieroAdapter adapter = new BuroFinancieroAdapter(repo);
    Score score = adapter.obtenerScore(c.getId());
        assertNotNull(score);
        assertEquals(730, score.getValor());
        assertEquals(c.getId(), score.getClienteId());
    }
}
