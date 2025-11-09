package com.example.model.transaccion;

import org.junit.jupiter.api.Test;

import com.example.model.transacccion.Transaccion;
import com.example.model.transacccion.Transaccion.EstadoTransaccion;

import static org.junit.jupiter.api.Assertions.*;

class TransaccionUnitTest {
    @Test
    void gettersYSettersBasicos() {
        Transaccion t = new Transaccion();
        t.setMonto(100);
        t.setDescripcion("Pago");
        t.setEstado(Transaccion.EstadoTransaccion.PENDIENTE);
        assertEquals(100, t.getMonto());
        assertEquals("Pago", t.getDescripcion());
        assertEquals(Transaccion.EstadoTransaccion.PENDIENTE, t.getEstado());
    }

    @Test
    void ejecutarYValidar() {
        Transaccion t = new Transaccion();
        t.setMonto(0);
        t.ejecutar();
        assertEquals(Transaccion.EstadoTransaccion.FALLIDA, t.getEstado());
        t.setMonto(200);
        t.ejecutar();
        assertEquals(Transaccion.EstadoTransaccion.EXITOSA, t.getEstado());
        assertNotNull(t.getFecha());
    }
}
