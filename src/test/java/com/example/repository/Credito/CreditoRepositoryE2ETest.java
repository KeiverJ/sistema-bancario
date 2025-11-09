package com.example.repository.Credito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.credito.Credito;

import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas E2E para CreditoRepository: ciclo completo CRUD.
 */
class CreditoRepositoryE2ETest {

    @Test
    @DisplayName("Ciclo E2E: guardar, buscar, eliminar crédito")
    void cicloCompletoCRUD() {
        TestAppContext ctx = TestAppContext.build();
        Credito c = new Credito();
        c.setId("e2e-cr1");
        c.setMonto(9000);
        ctx.creditoRepository.save(c);
        var encontrada = ctx.creditoRepository.findById("e2e-cr1");
        assertTrue(encontrada.isPresent());
        ctx.creditoRepository.delete("e2e-cr1");
        assertTrue(ctx.creditoRepository.findById("e2e-cr1").isEmpty());
    }
}
