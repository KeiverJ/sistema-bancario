package com.example.service.transaccion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.model.transacccion.Transaccion;

import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para TransaccionService.
 */
class TransaccionServiceIntegrationTest {

    @Test
    @DisplayName("registrarTransaccion y obtenerTransaccion funcionan")
    void registrarYObtenerTransaccion() {
        TestAppContext ctx = TestAppContext.build();
        Transaccion t = new Transaccion();
        t.setDescripcion("integracion");
        Transaccion guardada = ctx.transaccionService.registrarTransaccion(t);
        var encontrada = ctx.transaccionService.obtenerTransaccion(guardada.getId());
        assertTrue(encontrada.isPresent());
        assertEquals("integracion", encontrada.get().getDescripcion());
    }
}
