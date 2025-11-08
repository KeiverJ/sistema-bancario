package com.example.repository.Cliente;

import com.example.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para ClienteRepository.
 */
class ClienteRepositoryIntegrationTest {

    @Test
    @DisplayName("findByNumeroDocumento y findByTipoCliente funcionan")
    void findByNumeroYTipoCliente() {
        TestAppContext ctx = TestAppContext.build();
        Cliente c1 = ctx.clienteService.crearCliente("IntRepo", "CC", "12345", Cliente.TipoCliente.PERSONA_NATURAL);
        Cliente c2 = ctx.clienteService.crearCliente("IntRepo2", "CC", "54321", Cliente.TipoCliente.PERSONA_JURIDICA);
        var encontrado = ctx.clienteRepository.findByNumeroDocumento("12345");
        assertTrue(encontrado.isPresent());
        var naturales = ctx.clienteRepository.findByTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL);
        assertTrue(naturales.stream().anyMatch(c -> c.getNombre().equals("IntRepo")));
    }
}
