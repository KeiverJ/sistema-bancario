package com.example.observer;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

class ObserverE2ETest {

    @Test
    @DisplayName("E2E: pagar cuota publica evento de cambio estado")
    void e2e_pagoCreditoPublicaEvento() {
        TestAppContext ctx = TestAppContext.build();
        Cliente c = ctx.clienteService.crearCliente("Obs Cliente", "CC", "444", Cliente.TipoCliente.PERSONA_NATURAL);
        c.setScoreActual(730);
        ctx.clienteRepository.save(c);
        Credito cr = ctx.creditoService.solicitarCredito(c.getId(), Credito.TipoCredito.CONSUMO, 4000, 12);
        // Poner el crédito en estado desembolsado antes de pagar
        cr.desembolsar();
        ctx.creditoService.guardar(cr);
        boolean ok = ctx.creditoService.pagarCuota(cr.getId(), 500);
        assertTrue(ok);
    }
}
