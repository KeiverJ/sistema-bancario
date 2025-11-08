package com.example.repository.Cliente;

import com.example.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas E2E para ClienteRepository: ciclo completo CRUD.
 */
class ClienteRepositoryE2ETest {

    @Test
    @DisplayName("Ciclo E2E: guardar, buscar, eliminar cliente")
    void cicloCompletoCRUD() {
        TestAppContext ctx = TestAppContext.build();
        Cliente c = new Cliente();
        c.setId("e2e-1");
        c.setNombre("E2ERepo");
        ctx.clienteRepository.save(c);
        var encontrado = ctx.clienteRepository.findById("e2e-1");
        assertTrue(encontrado.isPresent());
        ctx.clienteRepository.delete("e2e-1");
        assertTrue(ctx.clienteRepository.findById("e2e-1").isEmpty());
    }
}
