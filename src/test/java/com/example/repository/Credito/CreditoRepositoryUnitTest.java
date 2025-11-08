package com.example.repository.Credito;

import com.example.model.Credito;
import com.example.repository.CreditoRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para CreditoRepository (en memoria).
 */
class CreditoRepositoryUnitTest {

    @Test
    @DisplayName("save y findById funcionan")
    void saveYFindById() {
        CreditoRepository repo = new CreditoRepository();
        Credito c = new Credito();
        c.setId("cr1");
        c.setMonto(2000);
        repo.save(c);
        var encontrada = repo.findById("cr1");
        assertTrue(encontrada.isPresent());
        assertEquals(2000, encontrada.get().getMonto(), 0.01);
    }

    @Test
    @DisplayName("delete elimina correctamente")
    void delete_funciona() {
        CreditoRepository repo = new CreditoRepository();
        Credito c = new Credito();
        c.setId("cr2");
        repo.save(c);
        repo.delete("cr2");
        assertTrue(repo.findById("cr2").isEmpty());
    }
}
