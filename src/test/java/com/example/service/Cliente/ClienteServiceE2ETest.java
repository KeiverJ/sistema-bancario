package com.example.service.Cliente;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.cliente.Cliente;

import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas E2E para ClienteService: ciclo completo de vida de un cliente.
 */
class ClienteServiceE2ETest {

    @Test
    @DisplayName("Ciclo completo: crear, actualizar, eliminar cliente")
    void cicloCompletoCliente() {
        TestAppContext ctx = TestAppContext.build();
        // Crear
        Cliente cliente = ctx.clienteService.crearCliente("E2E", "CC", "555", Cliente.TipoCliente.PERSONA_NATURAL);
        assertNotNull(cliente.getId());
        // Actualizar
        Cliente actualizado = ctx.clienteService.actualizarCliente(cliente.getId(), "e2e@mail.com", "123-456");
        assertEquals("e2e@mail.com", actualizado.getEmail());
        // Eliminar
        boolean eliminado = ctx.clienteService.eliminarCliente(cliente.getId());
        assertTrue(eliminado);
        assertTrue(ctx.clienteService.obtenerCliente(cliente.getId()).isEmpty());
    }
}
