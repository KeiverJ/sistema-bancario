package com.example.repository.Cliente;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.cliente.Cliente;

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
        var encontrado = ctx.clienteRepository.findByNumeroDocumento("12345");
        assertTrue(encontrado.isPresent());
        var naturales = ctx.clienteRepository.findByTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL);
        assertTrue(naturales.stream().anyMatch(c -> c.getNombre().equals("IntRepo")));
    }
}
