package com.example.adapter;

import com.example.adapter.buro.BuroFinancieroAdapter;
import com.example.adapter.legacy.LegacyRiskApiAdapter;
import com.example.adapter.score.ScoreProviderRegistry;
import com.example.model.cliente.Cliente;
import com.example.model.score.Score;
import com.example.repository.cliente.ClienteRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integración: Registry con dos proveedores y selección de fuente.
 */
class AdapterIntegrationTest {

    // Verifica que el registry entrega el adapter de buro y retorna un score válido
    @Test
    @DisplayName("Registry entrega adapter de buro y retorna score válido")
    void registry_scoreProvider() {
        ClienteRepository repo = new ClienteRepository();
        Cliente c = new Cliente();
        c.setNombre("Carlos");
        c.setScoreActual(690);
        repo.save(c);

    ScoreProviderRegistry registry = new ScoreProviderRegistry(
        new BuroFinancieroAdapter(repo),
        new LegacyRiskApiAdapter());

    Score score = registry.obtenerScoreParaCliente(c);
        assertEquals(690, score.getValor());
    }
}
