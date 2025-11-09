package com.example.chain;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * E2E: Solicitud de crédito pasa por la cadena completa mediante el template.
 */
class ChainE2ETest {

    @Test
    @DisplayName("Flujo completo con aprobación de crédito VEHICULO")
    void e2e_aprobacionCreditoVehiculo() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("Cadena Cliente", "CC", "888", Cliente.TipoCliente.PERSONA_NATURAL);
        cliente.setScoreActual(780);
        ctx.clienteRepository.save(cliente);

        Credito credito = ctx.creditoService.solicitarCredito(cliente.getId(), Credito.TipoCredito.VEHICULO, 30000, 36);
        assertNotNull(credito.getId());
        // Tras pasar la cadena y aprobar, el crédito debe quedar en estado APROBADO
        assertEquals(Credito.EstadoCredito.APROBADO, credito.getEstadoActual());
        assertNotEquals(Credito.EstadoCredito.SOLICITADO, credito.getEstadoActual());
    }
}
