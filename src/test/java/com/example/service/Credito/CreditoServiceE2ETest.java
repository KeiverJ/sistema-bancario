package com.example.service.Credito;

import com.example.model.Cliente;
import com.example.model.Credito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas E2E para CreditoService: flujo completo de crédito.
 */
class CreditoServiceE2ETest {

    @Test
    @DisplayName("Flujo E2E: solicitar, pagar cuota y consultar saldo")
    void flujoCompletoCredito() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("E2E", "CC", "333", Cliente.TipoCliente.PERSONA_NATURAL);
        cliente.setScoreActual(700);
        ctx.clienteRepository.save(cliente);
        Credito credito = ctx.creditoService.solicitarCredito(cliente.getId(), Credito.TipoCredito.CONSUMO, 8000, 12);
        credito.desembolsar();
        ctx.creditoService.guardar(credito);
        boolean pagado = ctx.creditoService.pagarCuota(credito.getId(), 1000);
        assertTrue(pagado);
        double saldo = ctx.creditoService.consultarSaldo(credito.getId());
        assertTrue(saldo < 8000);
    }
}
