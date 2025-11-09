package com.example.model.cliente;

import com.example.model.Cliente;
import com.example.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas de integración para Cliente: interacción con repositorio simulado.
 */
class ClienteIntegrationTest {

    private ClienteRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ClienteRepository.class);
    }

    @Test
    @DisplayName("Guardar y buscar cliente por ID")
    void guardarYBuscarCliente() {
        Cliente cliente = new Cliente();
        cliente.setId("123");
        cliente.setNombre("Juan Perez");
        when(repository.save(cliente)).thenReturn(cliente);
        when(repository.findById("123")).thenReturn(java.util.Optional.of(cliente));

        Cliente guardado = repository.save(cliente);
        assertNotNull(guardado);
        assertEquals("Juan Perez", guardado.getNombre());

        java.util.Optional<Cliente> encontradoOpt = repository.findById("123");
        assertTrue(encontradoOpt.isPresent());
        Cliente encontrado = encontradoOpt.get();
        assertEquals("123", encontrado.getId());
    }

    @Test
    @DisplayName("Actualizar datos de cliente existente")
    void actualizarCliente() {
        Cliente cliente = new Cliente();
        cliente.setId("456");
        cliente.setNombre("Ana Torres");
        when(repository.findById("456")).thenReturn(java.util.Optional.of(cliente));

        java.util.Optional<Cliente> encontradoOpt = repository.findById("456");
        assertTrue(encontradoOpt.isPresent());
        Cliente encontrado = encontradoOpt.get();
        encontrado.setNombre("Ana T. Actualizada");
        when(repository.save(encontrado)).thenReturn(encontrado);

        Cliente actualizado = repository.save(encontrado);
        assertEquals("Ana T. Actualizada", actualizado.getNombre());
    }
}
