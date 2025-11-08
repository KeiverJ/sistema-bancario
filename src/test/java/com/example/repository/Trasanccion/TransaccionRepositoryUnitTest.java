
package com.example.repository.Trasanccion;
import com.example.util.TestDataFactory;

import com.example.model.Transaccion;
import com.example.repository.TransaccionRepository;

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
    @DisplayName("delete elimina correctamente")
    void delete_funciona() {
        TransaccionRepository repo = new TransaccionRepository();
    Transaccion t = TestDataFactory.transaccion("t2");
    repo.save(t);
    repo.delete("t2");
    assertTrue(repo.findById("t2").isEmpty());
    }
}
