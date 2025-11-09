package com.example.model.cliente;

import com.example.model.Cliente;
import com.example.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas E2E para Cliente: flujo completo de registro, actualización y consulta.
 */
class ClienteE2ETest {

    private ClienteRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ClienteRepository();
    }

    @Test
    @DisplayName("Registrar, actualizar y consultar cliente")
    void flujoCompletoCliente() {
        // Registro
        Cliente cliente = new Cliente();
        cliente.setNombre("Carlos E2E");
        cliente.setNumeroDocumento("999999");
        Cliente guardado = repository.save(cliente);
        assertNotNull(guardado.getId());
        assertEquals("Carlos E2E", guardado.getNombre());

        // Consulta por documento
        Cliente encontrado = repository.findByNumeroDocumento("999999").orElse(null);
        assertNotNull(encontrado);
        assertEquals(guardado.getId(), encontrado.getId());

        // Actualización
        encontrado.setNombre("Carlos Actualizado");
        repository.save(encontrado);
        Cliente actualizado = repository.findById(encontrado.getId()).orElse(null);
        assertNotNull(actualizado);
        assertEquals("Carlos Actualizado", actualizado.getNombre());
    }

    @Test
    @DisplayName("Eliminar cliente y verificar no existencia")
    void eliminarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Eliminar E2E");
        cliente.setNumeroDocumento("888888");
        Cliente guardado = repository.save(cliente);
        assertNotNull(guardado.getId());

        repository.delete(guardado.getId());
        assertFalse(repository.findById(guardado.getId()).isPresent());
    }
}
