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
    @DisplayName("save genera id si es nulo")
    void saveGeneraIdSiNulo() {
        CreditoRepository repo = new CreditoRepository();
        Credito c = new Credito();
        c.setMonto(100);
        Credito guardado = repo.save(c);
        assertNotNull(guardado.getId());
        assertEquals(100, guardado.getMonto(), 0.01);
    }

    @Test
    @DisplayName("findByClienteId filtra correctamente")
    void findByClienteId_funciona() {
        CreditoRepository repo = new CreditoRepository();
        Credito c1 = new Credito();
        c1.setId("a");
        c1.setClienteId("cli-1");
        Credito c2 = new Credito();
        c2.setId("b");
        c2.setClienteId("cli-2");
        repo.save(c1);
        repo.save(c2);
        var lista = repo.findByClienteId("cli-1");
        assertEquals(1, lista.size());
        assertEquals("a", lista.get(0).getId());
    }

    @Test
    @DisplayName("findAll retorna todos los créditos")
    void findAll_funciona() {
        CreditoRepository repo = new CreditoRepository();
        repo.save(new Credito());
        repo.save(new Credito());
        assertEquals(2, repo.findAll().size());
    }

    @Test
    @DisplayName("findByTipoCredito filtra correctamente")
    void findByTipoCredito_funciona() {
        CreditoRepository repo = new CreditoRepository();
        Credito c1 = new Credito();
        c1.setId("t1");
        c1.setTipoCredito(Credito.TipoCredito.CONSUMO);
        Credito c2 = new Credito();
        c2.setId("t2");
        c2.setTipoCredito(Credito.TipoCredito.HIPOTECARIO);
        repo.save(c1);
        repo.save(c2);
        var lista = repo.findByTipoCredito(Credito.TipoCredito.CONSUMO);
        assertEquals(1, lista.size());
        assertEquals("t1", lista.get(0).getId());
    }

    @Test
    @DisplayName("findByEstado filtra correctamente")
    void findByEstado_funciona() {
        CreditoRepository repo = new CreditoRepository();
        Credito c1 = new Credito();
        c1.setId("e1");
        c1.setEstadoActual(Credito.EstadoCredito.SOLICITADO);
        Credito c2 = new Credito();
        c2.setId("e2");
        c2.setEstadoActual(Credito.EstadoCredito.CANCELADO);
        repo.save(c1);
        repo.save(c2);
        var lista = repo.findByEstado("SOLICITADO");
        assertEquals(1, lista.size());
        assertEquals("e1", lista.get(0).getId());
    }

    @Test
    @DisplayName("getTotalSaldoByClienteId suma correctamente")
    void getTotalSaldoByClienteId_funciona() {
        CreditoRepository repo = new CreditoRepository();
        Credito c1 = new Credito();
        c1.setClienteId("cli-1");
        c1.setSaldo(100);
        Credito c2 = new Credito();
        c2.setClienteId("cli-1");
        c2.setSaldo(200);
        Credito c3 = new Credito();
        c3.setClienteId("cli-2");
        c3.setSaldo(50);
        repo.save(c1);
        repo.save(c2);
        repo.save(c3);
        assertEquals(300, repo.getTotalSaldoByClienteId("cli-1"), 0.01);
        assertEquals(50, repo.getTotalSaldoByClienteId("cli-2"), 0.01);
    }

    @Test
    @DisplayName("deleteAll elimina todos los créditos")
    void deleteAll_funciona() {
        CreditoRepository repo = new CreditoRepository();
        repo.save(new Credito());
        repo.save(new Credito());
        repo.deleteAll();
        assertEquals(0, repo.count());
        assertTrue(repo.findAll().isEmpty());
    }

    @Test
    @DisplayName("count retorna cantidad correcta")
    void count_funciona() {
        CreditoRepository repo = new CreditoRepository();
        assertEquals(0, repo.count());
        repo.save(new Credito());
        assertEquals(1, repo.count());
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
