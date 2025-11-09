package com.example.repository.Cuenta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.cuenta.Cuenta;

import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas E2E para CuentaRepository: ciclo completo CRUD.
 */
class CuentaRepositoryE2ETest {

    @Test
    @DisplayName("Ciclo E2E: guardar, buscar, eliminar cuenta")
    void cicloCompletoCRUD() {
        TestAppContext ctx = TestAppContext.build();
        Cuenta c = new Cuenta();
        c.setId("e2e-c1");
        c.setSaldo(5000);
        ctx.cuentaRepository.save(c);
        var encontrada = ctx.cuentaRepository.findById("e2e-c1");
        assertTrue(encontrada.isPresent());
        ctx.cuentaRepository.delete("e2e-c1");
        assertTrue(ctx.cuentaRepository.findById("e2e-c1").isEmpty());
    }
}
