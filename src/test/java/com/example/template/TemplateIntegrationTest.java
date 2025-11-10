package com.example.template;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;
import com.example.template.impl.SolicitudCreditoDefault;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

class TemplateIntegrationTest {

    // Verifica que SolicitudCreditoDefault arma y persiste un crédito correctamente
    @Test
    @DisplayName("SolicitudCreditoDefault arma y persiste crédito")
    void solicitudCreditoDefault_integration() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("Temp Cliente", "CC", "111", Cliente.TipoCliente.PERSONA_NATURAL);
        cliente.setScoreActual(700);
        ctx.clienteRepository.save(cliente);
        SolicitudCreditoDefault template = ctx.solicitudCreditoTemplate;
        Credito c = template.solicitar(cliente, Credito.TipoCredito.LIBRE_INVERSION, 6000, 18);
        assertNotNull(c.getId());
        assertEquals(cliente.getId(), c.getClienteId());
    }
}
