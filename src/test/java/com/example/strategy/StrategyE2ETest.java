package com.example.strategy;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

class StrategyE2ETest {

    @Test
    @DisplayName("E2E solicitud crédito aplica estrategia de interés")
    void e2e_aplicaEstrategiaInteres() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("Estrat Cliente", "CC", "999", Cliente.TipoCliente.PERSONA_NATURAL);
        // score mínimo para pasar cadena de aprobación
        cliente.setScoreActual(700);
        ctx.clienteRepository.save(cliente);
        Credito credito = ctx.creditoService.solicitarCredito(cliente.getId(), Credito.TipoCredito.CONSUMO, 8000, 24);
        assertTrue(credito.getTasaInteres() > 0);
    }
}
