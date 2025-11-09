package com.example.template;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

class TemplateE2ETest {

    @Test
    @DisplayName("E2E: Solicitud de crédito completa vía template")
    void e2e_solicitudCredito_template() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("Temp E2E", "CC", "222", Cliente.TipoCliente.PERSONA_NATURAL);
        cliente.setScoreActual(750);
        ctx.clienteRepository.save(cliente);
        Credito c = ctx.creditoService.solicitarCredito(cliente.getId(), Credito.TipoCredito.HIPOTECARIO, 120000, 120);
        assertNotNull(c.getId());
        assertEquals(Credito.TipoCredito.HIPOTECARIO, c.getTipoCredito());
    }
}
