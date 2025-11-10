package com.example.service.credito;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para CreditoService.
 */
class CreditoServiceIntegrationTest {

    @Test
    @DisplayName("solicitarCredito y consultarSaldo funcionan")
    void solicitarYConsultarSaldo() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("Int", "CC", "111", Cliente.TipoCliente.PERSONA_NATURAL);
        cliente.setScoreActual(700);
        ctx.clienteRepository.save(cliente);
        Credito credito = ctx.creditoService.solicitarCredito(cliente.getId(), Credito.TipoCredito.CONSUMO, 5000, 12);
        double saldo = ctx.creditoService.consultarSaldo(credito.getId());
        assertEquals(5000, saldo, 0.01);
    }

    @Test
    @DisplayName("listarTodosCreditos retorna lista")
    void listarTodosCreditos_funciona() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("Int2", "CC", "222", Cliente.TipoCliente.PERSONA_NATURAL);
        cliente.setScoreActual(700);
        ctx.clienteRepository.save(cliente);
        ctx.creditoService.solicitarCredito(cliente.getId(), Credito.TipoCredito.CONSUMO, 1000, 6);
        assertTrue(ctx.creditoService.listarTodosCreditos().size() > 0);
    }
}
