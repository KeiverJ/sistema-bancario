package com.example.repository.Trasanccion;

import com.example.model.transacccion.Transaccion;
import com.example.repository.transaccion.TransaccionRepository;

import util.TestAppContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Pruebas E2E para TransaccionRepository usando TestAppContext.
 */
class TransaccionRepositoryE2ETest {
    private TransaccionRepository repository;

    @BeforeEach
    void setUp() {
    repository = TestAppContext.build().transaccionRepository;
    }

    @Test
    @DisplayName("guardar y buscar transacción E2E")
    void guardarYBuscarTransaccionE2E() {
        Transaccion t = new Transaccion();
        t.setId("e2e-1");
        t.setDescripcion("depósito E2E");
        repository.save(t);
        assertThat(repository.findById("e2e-1")).isPresent();
        assertThat(repository.findById("e2e-1").get().getDescripcion()).isEqualTo("depósito E2E");
    }

    @Test
    @DisplayName("eliminar transacción E2E")
    void eliminarTransaccionE2E() {
        Transaccion t = new Transaccion();
        t.setId("e2e-2");
        repository.save(t);
        repository.delete("e2e-2");
        assertThat(repository.findById("e2e-2")).isEmpty();
    }
}
