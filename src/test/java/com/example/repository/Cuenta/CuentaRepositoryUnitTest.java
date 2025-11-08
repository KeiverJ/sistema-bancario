package com.example.repository.Cuenta;

import com.example.model.Cuenta;
import com.example.repository.CuentaRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para CuentaRepository (en memoria).
 */
class CuentaRepositoryUnitTest {

    @Test
    @DisplayName("save y findById funcionan")
    void saveYFindById() {
        CuentaRepository repo = new CuentaRepository();
        Cuenta c = new Cuenta();
        c.setId("c1");
        c.setSaldo(1000);
        repo.save(c);
        var encontrada = repo.findById("c1");
        assertTrue(encontrada.isPresent());
        assertEquals(1000, encontrada.get().getSaldo(), 0.01);
    }

    @Test
    @DisplayName("delete elimina correctamente")
    void delete_funciona() {
        CuentaRepository repo = new CuentaRepository();
        Cuenta c = new Cuenta();
        c.setId("c2");
        repo.save(c);
        repo.delete("c2");
        assertTrue(repo.findById("c2").isEmpty());
    }
}
