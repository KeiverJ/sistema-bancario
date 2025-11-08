package com.example.service.Cliente;

import com.example.model.Cliente;
import com.example.service.ClienteService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias puras para ClienteService (sin wiring real, usando mocks).
 */
class ClienteServiceUnitTest {

    @Test
    @DisplayName("crearCliente asigna datos correctamente")
    void crearCliente_asignaDatos() {
        var repo = mock(com.example.repository.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        when(repo.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));
        ClienteService service = new ClienteService(repo, config);
        Cliente c = service.crearCliente("Unitario", "CC", "123", Cliente.TipoCliente.PERSONA_NATURAL);
        assertEquals("Unitario", c.getNombre());
        assertEquals("CC", c.getTipoDocumento());
        assertEquals("123", c.getNumeroDocumento());
        assertEquals(Cliente.TipoCliente.PERSONA_NATURAL, c.getTipoCliente());
    }

    @Test
    @DisplayName("actualizarCliente actualiza email y teléfono")
    void actualizarCliente_unit() {
        var repo = mock(com.example.repository.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        Cliente original = new Cliente();
        original.setId("1");
        when(repo.findById("1")).thenReturn(java.util.Optional.of(original));
        when(repo.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));
        ClienteService service = new ClienteService(repo, config);
        Cliente actualizado = service.actualizarCliente("1", "mail@unit.com", "555");
        assertEquals("mail@unit.com", actualizado.getEmail());
        assertEquals("555", actualizado.getTelefono());
    }
}
