package com.example.repository.Trasanccion;

import com.example.model.transacccion.Transaccion;
import com.example.repository.transaccion.TransaccionRepository;
import com.example.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Pruebas de integración para TransaccionRepository usando TestDataFactory.
 */
class TransaccionRepositoryIntegrationTest {
    private TransaccionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new TransaccionRepository();
    }

    @Test
    @DisplayName("guardar y buscar transacción")
    void guardarYBuscarTransaccion() {
        Transaccion t = TestDataFactory.transaccion("int-1");
        repository.save(t);
        assertThat(repository.findById("int-1")).isPresent();
    }

    @Test
    @DisplayName("eliminar transacción existente")
    void eliminarTransaccion() {
        Transaccion t = TestDataFactory.transaccion("int-2");
        repository.save(t);
        repository.delete("int-2");
        assertThat(repository.findById("int-2")).isEmpty();
    }
}
