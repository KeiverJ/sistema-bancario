
package com.example.repository.Trasanccion;
import com.example.util.TestDataFactory;
import com.example.model.transacccion.Transaccion;
import com.example.repository.transaccion.TransaccionRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para TransaccionRepository (en memoria).
 */
class TransaccionRepositoryUnitTest {

    @Test
    @DisplayName("save y findById funcionan")
    void saveYFindById() {
        TransaccionRepository repo = new TransaccionRepository();
    Transaccion t = TestDataFactory.transaccion("t1");
    repo.save(t);
    var encontrada = repo.findById("t1");
    assertTrue(encontrada.isPresent());
    assertTrue(encontrada.get().getDescripcion().contains("t1"));
    }

    @Test
    @DisplayName("save genera id si es nulo")
    void saveGeneraIdSiNulo() {
        TransaccionRepository repo = new TransaccionRepository();
        Transaccion t = new Transaccion();
        Transaccion guardada = repo.save(t);
        assertNotNull(guardada.getId());
    }

    @Test
    @DisplayName("findByCuentaId filtra correctamente")
    void findByCuentaId_funciona() {
        TransaccionRepository repo = new TransaccionRepository();
        Transaccion t1 = new Transaccion();
        t1.setId("a");
        t1.setCuentaOrigenId("c1");
        Transaccion t2 = new Transaccion();
        t2.setId("b");
        t2.setCuentaDestinoId("c1");
        Transaccion t3 = new Transaccion();
        t3.setId("c");
        t3.setCuentaOrigenId("c2");
        repo.save(t1);
        repo.save(t2);
        repo.save(t3);
        var lista = repo.findByCuentaId("c1");
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("findByTipo filtra correctamente")
    void findByTipo_funciona() {
        TransaccionRepository repo = new TransaccionRepository();
        Transaccion t1 = new Transaccion();
        t1.setId("t1");
        t1.setTipo(Transaccion.TipoTransaccion.DEPOSITO);
        Transaccion t2 = new Transaccion();
        t2.setId("t2");
        t2.setTipo(Transaccion.TipoTransaccion.RETIRO);
        repo.save(t1);
        repo.save(t2);
        var lista = repo.findByTipo(Transaccion.TipoTransaccion.DEPOSITO);
        assertEquals(1, lista.size());
        assertEquals("t1", lista.get(0).getId());
    }

    @Test
    @DisplayName("findByEstado filtra correctamente")
    void findByEstado_funciona() {
        TransaccionRepository repo = new TransaccionRepository();
        Transaccion t1 = new Transaccion();
        t1.setId("e1");
        t1.setEstado(Transaccion.EstadoTransaccion.PENDIENTE);
        Transaccion t2 = new Transaccion();
        t2.setId("e2");
    t2.setEstado(Transaccion.EstadoTransaccion.EXITOSA);
        repo.save(t1);
        repo.save(t2);
        var lista = repo.findByEstado(Transaccion.EstadoTransaccion.PENDIENTE);
        assertEquals(1, lista.size());
        assertEquals("e1", lista.get(0).getId());
    }

    @Test
    @DisplayName("findByFechaRange filtra correctamente")
    void findByFechaRange_funciona() {
        TransaccionRepository repo = new TransaccionRepository();
        var now = java.time.LocalDateTime.now();
        Transaccion t1 = new Transaccion();
        t1.setId("f1");
        t1.setFecha(now.minusDays(1));
        Transaccion t2 = new Transaccion();
        t2.setId("f2");
        t2.setFecha(now.plusDays(1));
        repo.save(t1);
        repo.save(t2);
        var lista = repo.findByFechaRange(now.minusDays(2), now);
        assertEquals(1, lista.size());
        assertEquals("f1", lista.get(0).getId());
    }

    @Test
    @DisplayName("findAll retorna todas las transacciones")
    void findAll_funciona() {
        TransaccionRepository repo = new TransaccionRepository();
        repo.save(new Transaccion());
        repo.save(new Transaccion());
        assertEquals(2, repo.findAll().size());
    }

    @Test
    @DisplayName("deleteAll elimina todas las transacciones")
    void deleteAll_funciona() {
        TransaccionRepository repo = new TransaccionRepository();
        repo.save(new Transaccion());
        repo.save(new Transaccion());
        repo.deleteAll();
        assertEquals(0, repo.count());
        assertTrue(repo.findAll().isEmpty());
    }

    @Test
    @DisplayName("count retorna cantidad correcta")
    void count_funciona() {
        TransaccionRepository repo = new TransaccionRepository();
        assertEquals(0, repo.count());
        repo.save(new Transaccion());
        assertEquals(1, repo.count());
    }

    @Test
    @DisplayName("delete elimina correctamente")
    void delete_funciona() {
        TransaccionRepository repo = new TransaccionRepository();
    Transaccion t = TestDataFactory.transaccion("t2");
    repo.save(t);
    repo.delete("t2");
    assertTrue(repo.findById("t2").isEmpty());
    }
}
