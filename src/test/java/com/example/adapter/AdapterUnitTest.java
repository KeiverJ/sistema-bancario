package com.example.adapter;

import com.example.adapter.score.ScoreProviderRegistry;
import com.example.adapter.buro.BuroFinancieroAdapter;
import com.example.adapter.legacy.LegacyRiskApiAdapter;
import com.example.model.cliente.Cliente;
import com.example.model.score.Score;
import com.example.repository.cliente.ClienteRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba unitaria del adapter que obtiene score del repositorio de cliente.
 */
class AdapterUnitTest {

    @Test
    @DisplayName("ScoreProviderRegistry: constructor y obtenerScoreParaCliente funcionan")
    void scoreProviderRegistry_funciona() {
        ClienteRepository repo = new ClienteRepository();
        Cliente cliente = new Cliente();
        cliente.setNombre("Pedro");
        cliente.setScoreActual(700);
        repo.save(cliente);

        BuroFinancieroAdapter buro = new BuroFinancieroAdapter(repo);
        LegacyRiskApiAdapter legacy = new LegacyRiskApiAdapter();
        ScoreProviderRegistry registry = new ScoreProviderRegistry(buro, legacy);

        // Debe usar buro
        Score score = registry.obtenerScoreParaCliente(cliente);
        assertNotNull(score);
        assertEquals(700, score.getValor());
        assertEquals(cliente.getId(), score.getClienteId());
        assertEquals("BURO", score.getFuente());

        // Si cambiamos el score en repo, debe reflejarse
        cliente.setScoreActual(650);
        Score score2 = registry.obtenerScoreParaCliente(cliente);
        assertEquals(650, score2.getValor());
    }

    // Verifica que LegacyRiskApiAdapter retorna score por defecto si el cliente es
    // null
    @Test
    @DisplayName("LegacyRiskApiAdapter retorna score por defecto si cliente es null")
    void legacy_score_clienteNull() {
        LegacyRiskApiAdapter adapter = new LegacyRiskApiAdapter();
        Score score = adapter.obtenerScore(null);
        assertNotNull(score);
        assertEquals(600, score.getValor());
    }

    // Verifica que LegacyRiskApiAdapter retorna score para cliente con id '2'
    @Test
    @DisplayName("LegacyRiskApiAdapter retorna score para cliente con id '2'")
    void legacy_score_clienteId2() {
        LegacyRiskApiAdapter adapter = new LegacyRiskApiAdapter();
        Score score = adapter.obtenerScore("2");
        assertTrue(score.getValor() == 800 || score.getValor() == 700);
    }

    // Verifica que LegacyRiskApiAdapter retorna score para cliente con id '3'
    @Test
    @DisplayName("LegacyRiskApiAdapter retorna score para cliente con id '3'")
    void legacy_score_clienteId3() {
        LegacyRiskApiAdapter adapter = new LegacyRiskApiAdapter();
        Score score = adapter.obtenerScore("3");
        assertTrue(score.getValor() == 800 || score.getValor() == 700);
    }

    // Verifica que BuroFinancieroAdapter obtiene el score desde ClienteRepository
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
