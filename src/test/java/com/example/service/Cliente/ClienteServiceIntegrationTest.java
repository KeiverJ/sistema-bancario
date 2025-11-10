package com.example.service.cliente;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.cliente.Cliente;

import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para ClienteService usando wiring real.
 */
class ClienteServiceIntegrationTest {

    @Test
    @DisplayName("crear y obtener cliente por documento")
    void crearYObtenerClientePorDocumento() {
        TestAppContext ctx = TestAppContext.build();
        Cliente creado = ctx.clienteService.crearCliente("Integracion", "CC", "999", Cliente.TipoCliente.PERSONA_NATURAL);
        assertNotNull(creado.getId());
        var opt = ctx.clienteService.obtenerClientePorDocumento("999");
        assertTrue(opt.isPresent());
        assertEquals("Integracion", opt.get().getNombre());
    }

    @Test
    @DisplayName("listarClientesPorTipo retorna solo los del tipo")
    void listarClientesPorTipo_funciona() {
        TestAppContext ctx = TestAppContext.build();
        ctx.clienteService.crearCliente("A", "CC", "1", Cliente.TipoCliente.PERSONA_NATURAL);
        ctx.clienteService.crearCliente("B", "CC", "2", Cliente.TipoCliente.PERSONA_JURIDICA);
        var naturales = ctx.clienteService.listarClientesPorTipo(Cliente.TipoCliente.PERSONA_NATURAL);
        assertTrue(naturales.stream().anyMatch(c -> c.getNombre().equals("A")));
        assertTrue(naturales.stream().noneMatch(c -> c.getNombre().equals("B")));
    }
}
