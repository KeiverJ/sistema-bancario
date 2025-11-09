package com.example.service.Cliente;

import com.example.model.cliente.Cliente;
import com.example.service.cliente.ClienteService;

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
        var repo = mock(com.example.repository.cliente.ClienteRepository.class);
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
    @DisplayName("eliminarCliente retorna true si existe y false si no")
    void eliminarCliente_trueFalse() {
        var repo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        when(repo.findById("1")).thenReturn(java.util.Optional.of(new Cliente()));
        ClienteService service = new ClienteService(repo, config);
        assertTrue(service.eliminarCliente("1"));
        when(repo.findById("2")).thenReturn(java.util.Optional.empty());
        assertFalse(service.eliminarCliente("2"));
    }

    @Test
    @DisplayName("obtenerClientePorDocumento y existeCliente funcionan")
    void obtenerYExisteCliente() {
        var repo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        Cliente c = new Cliente();
        when(repo.findByNumeroDocumento("123")).thenReturn(java.util.Optional.of(c));
        when(repo.existsByNumeroDocumento("123")).thenReturn(true);
        ClienteService service = new ClienteService(repo, config);
        assertTrue(service.obtenerClientePorDocumento("123").isPresent());
        assertTrue(service.existeCliente("123"));
    }

    @Test
    @DisplayName("listarClientes, listarClientesPorTipo y contarClientes funcionan")
    void listarYContarClientes() {
        var repo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        when(repo.findAll()).thenReturn(java.util.List.of(new Cliente(), new Cliente()));
        when(repo.findByTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL)).thenReturn(java.util.List.of(new Cliente()));
        when(repo.count()).thenReturn(2L);
        ClienteService service = new ClienteService(repo, config);
        assertEquals(2, service.listarClientes().size());
        assertEquals(1, service.listarClientesPorTipo(Cliente.TipoCliente.PERSONA_NATURAL).size());
        assertEquals(2, service.contarClientes());
    }

    @Test
    @DisplayName("obtenerCliente y obtenerClientePorCodigo funcionan")
    void obtenerClientePorIdYCodigo() {
        var repo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        Cliente c = new Cliente();
        c.setCodigo("C-1");
        when(repo.findById("1")).thenReturn(java.util.Optional.of(c));
        when(repo.findAll()).thenReturn(java.util.List.of(c));
        ClienteService service = new ClienteService(repo, config);
        assertTrue(service.obtenerCliente("1").isPresent());
        assertTrue(service.obtenerClientePorCodigo("C-1").isPresent());
    }

    @Test
    @DisplayName("actualizarScore y actualizarScorePorCodigo funcionan")
    void actualizarScore_funciona() {
        var repo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        Cliente c = new Cliente();
        c.setId("1");
        c.setCodigo("C-1");
        when(repo.findById("1")).thenReturn(java.util.Optional.of(c));
        when(repo.findAll()).thenReturn(java.util.List.of(c));
        when(repo.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));
        ClienteService service = new ClienteService(repo, config);
        Cliente actualizado = service.actualizarScore("1", 800);
        assertEquals(800, actualizado.getScoreActual());
        Cliente actualizado2 = service.actualizarScorePorCodigo("C-1", 700);
        assertEquals(700, actualizado2.getScoreActual());
    }

    @Test
    @DisplayName("actualizarCliente lanza excepción si no existe")
    void actualizarCliente_lanzaExcepcion() {
        var repo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        when(repo.findById("nope")).thenReturn(java.util.Optional.empty());
        ClienteService service = new ClienteService(repo, config);
        assertThrows(IllegalArgumentException.class, () -> service.actualizarCliente("nope", "a", "b"));
    }

    @Test
    @DisplayName("actualizarScore lanza excepción si no existe")
    void actualizarScore_lanzaExcepcion() {
        var repo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        when(repo.findById("nope")).thenReturn(java.util.Optional.empty());
        ClienteService service = new ClienteService(repo, config);
        assertThrows(IllegalArgumentException.class, () -> service.actualizarScore("nope", 1));
    }

    @Test
    @DisplayName("actualizarScorePorCodigo lanza excepción si no existe")
    void actualizarScorePorCodigo_lanzaExcepcion() {
        var repo = mock(com.example.repository.cliente.ClienteRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        when(repo.findAll()).thenReturn(java.util.List.of());
        ClienteService service = new ClienteService(repo, config);
        assertThrows(IllegalArgumentException.class, () -> service.actualizarScorePorCodigo("NOPE", 1));
    }

    @Test
    @DisplayName("actualizarCliente actualiza email y teléfono")
    void actualizarCliente_unit() {
        var repo = mock(com.example.repository.cliente.ClienteRepository.class);
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
