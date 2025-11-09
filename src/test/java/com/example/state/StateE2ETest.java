package com.example.state;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

class StateE2ETest {

    @Test
    @DisplayName("E2E: pagar varias cuotas reduce saldo y conserva id")
    void e2e_pagosSecuenciales() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("State Cliente", "CC", "555", Cliente.TipoCliente.PERSONA_NATURAL);
        cliente.setScoreActual(710);
        ctx.clienteRepository.save(cliente);
        Credito credito = ctx.creditoService.solicitarCredito(cliente.getId(), Credito.TipoCredito.CONSUMO, 5000, 12);
        // Desembolsar antes de pagar cuotas para habilitar acción pagar según state pattern
        credito.desembolsar();
        ctx.creditoService.guardar(credito);
        double saldoInicial = credito.getSaldo();
        boolean p1 = ctx.creditoService.pagarCuota(credito.getId(), 400);
        boolean p2 = ctx.creditoService.pagarCuota(credito.getId(), 300);
        assertTrue(p1 && p2);
        double saldoFinal = ctx.creditoService.consultarSaldo(credito.getId());
        assertTrue(saldoFinal < saldoInicial);
    }
}
