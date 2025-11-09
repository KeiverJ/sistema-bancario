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
    @DisplayName("save genera id y número de cuenta si son nulos")
    void saveGeneraIdYNumeroCuentaSiNulo() {
        CuentaRepository repo = new CuentaRepository();
        Cuenta c = new Cuenta();
        Cuenta guardada = repo.save(c);
        assertNotNull(guardada.getId());
        assertNotNull(guardada.getNumeroCuenta());
    }

    @Test
    @DisplayName("findByNumeroCuenta retorna cuenta correcta")
    void findByNumeroCuenta_funciona() {
        CuentaRepository repo = new CuentaRepository();
        Cuenta c = new Cuenta();
        c.setId("n1");
        c.setNumeroCuenta("NC-123");
        repo.save(c);
        var encontrada = repo.findByNumeroCuenta("NC-123");
        assertTrue(encontrada.isPresent());
        assertEquals("n1", encontrada.get().getId());
    }

    @Test
    @DisplayName("findByClienteId filtra correctamente")
    void findByClienteId_funciona() {
        CuentaRepository repo = new CuentaRepository();
        Cuenta c1 = new Cuenta();
        c1.setId("a");
        c1.setClienteId("cli-1");
        Cuenta c2 = new Cuenta();
        c2.setId("b");
        c2.setClienteId("cli-2");
        repo.save(c1);
        repo.save(c2);
        var lista = repo.findByClienteId("cli-1");
        assertEquals(1, lista.size());
        assertEquals("a", lista.get(0).getId());
    }

    @Test
    @DisplayName("findAll retorna todas las cuentas")
    void findAll_funciona() {
        CuentaRepository repo = new CuentaRepository();
        repo.save(new Cuenta());
        repo.save(new Cuenta());
        assertEquals(2, repo.findAll().size());
    }

    @Test
    @DisplayName("findByEstado filtra correctamente")
    void findByEstado_funciona() {
        CuentaRepository repo = new CuentaRepository();
        Cuenta c1 = new Cuenta();
        c1.setId("e1");
        c1.setEstado(Cuenta.EstadoCuenta.ACTIVA);
        Cuenta c2 = new Cuenta();
        c2.setId("e2");
        c2.setEstado(Cuenta.EstadoCuenta.BLOQUEADA);
        repo.save(c1);
        repo.save(c2);
        var lista = repo.findByEstado(Cuenta.EstadoCuenta.ACTIVA);
        assertEquals(1, lista.size());
        assertEquals("e1", lista.get(0).getId());
    }

    @Test
    @DisplayName("deleteAll elimina todas las cuentas")
    void deleteAll_funciona() {
        CuentaRepository repo = new CuentaRepository();
        repo.save(new Cuenta());
        repo.save(new Cuenta());
        repo.deleteAll();
        assertEquals(0, repo.count());
        assertTrue(repo.findAll().isEmpty());
    }

    @Test
    @DisplayName("count retorna cantidad correcta")
    void count_funciona() {
        CuentaRepository repo = new CuentaRepository();
        assertEquals(0, repo.count());
        repo.save(new Cuenta());
        assertEquals(1, repo.count());
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
