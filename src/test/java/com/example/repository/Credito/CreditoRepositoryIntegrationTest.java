package com.example.repository.Credito;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para CreditoRepository.
 */
class CreditoRepositoryIntegrationTest {

    @Test
    @DisplayName("findByClienteId retorna créditos del cliente")
    void findByClienteId_funciona() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("IntCredRepo", "CC", "888", Cliente.TipoCliente.PERSONA_NATURAL);
        Credito credito = new Credito();
        credito.setId("cred-1");
        credito.setClienteId(cliente.getId());
        ctx.creditoRepository.save(credito);
        var creditos = ctx.creditoRepository.findByClienteId(cliente.getId());
        assertTrue(creditos.stream().anyMatch(c -> c.getId().equals("cred-1")));
    }
}
