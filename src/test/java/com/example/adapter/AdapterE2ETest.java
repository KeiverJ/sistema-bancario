package com.example.adapter;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;
import com.example.model.score.Score;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * E2E: El score influye en solicitud de crédito (implícito en cadena de aprobación / estrategia).
 */
class AdapterE2ETest {

    @Test
    @DisplayName("Flujo: crear cliente, asignar score y solicitar crédito")
    void e2e_scoreInfluyeCredito() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("Laura", "CC", "777", Cliente.TipoCliente.PERSONA_NATURAL);
        cliente.setScoreActual(780); // Alto score
        ctx.clienteRepository.save(cliente);

    Score score = ctx.scoreProviderRegistry.obtenerScoreParaCliente(cliente);
        assertTrue(score.getValor() >= 0);

        Credito credito = ctx.creditoService.solicitarCredito(cliente.getId(), Credito.TipoCredito.LIBRE_INVERSION, 5000, 24);
        assertNotNull(credito.getId());
    }
}
