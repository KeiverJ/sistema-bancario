package com.example.builder;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * E2E simplificado: flujo completo de solicitud de crédito usando template y servicios reales.
 */
class BuilderE2ETest {

    @Test
    @DisplayName("Flujo completo solicitud crédito VEHICULO usando contexto real")
    void e2e_solicitudCreditoVehiculo() {
        TestAppContext ctx = TestAppContext.build();

        Cliente cliente = ctx.clienteService.crearCliente(
                "Juan Vehiculos", "CC", "123456", Cliente.TipoCliente.PERSONA_NATURAL);
        assertNotNull(cliente.getId());

    // Ajuste: asegurar score suficiente para aprobación (mínimo 650)
    cliente.setScoreActual(720);
    ctx.clienteRepository.save(cliente);

        Credito credito = ctx.creditoService.solicitarCredito(
                cliente.getId(), Credito.TipoCredito.VEHICULO, 20000, 48);

        assertNotNull(credito.getId());
        assertEquals(20000, credito.getMonto());
        assertEquals(Credito.TipoCredito.VEHICULO, credito.getTipoCredito());
        assertTrue(credito.isSeguroVida()); // default del builder de vehículo
    }
}
