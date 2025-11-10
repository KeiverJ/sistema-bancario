package com.example.model.cliente;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteUnitTest {
    // Prueba que los setters y getters de Cliente funcionan correctamente
    @Test
    @DisplayName("Setters y getters funcionan")
    void settersYGetters() {
        Cliente c = new Cliente();
        c.setId("id");
        c.setNombre("Juan");
        c.setTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL);
        c.setNumeroDocumento("123");
        c.setTipoDocumento("CC");
        c.setEmail("a@b.com");
        c.setTelefono("555");
        c.setCodigo("C-1");
        assertEquals("id", c.getId());
        assertEquals("Juan", c.getNombre());
        assertEquals(Cliente.TipoCliente.PERSONA_NATURAL, c.getTipoCliente());
        assertEquals("123", c.getNumeroDocumento());
        assertEquals("CC", c.getTipoDocumento());
        assertEquals("a@b.com", c.getEmail());
        assertEquals("555", c.getTelefono());
        assertEquals("C-1", c.getCodigo());
    }

    // Prueba que mejorarScore y reducirScore modifican el score del cliente
    @Test
    @DisplayName("Mejorar y reducir score funcionan")
    void scoreTest() {
        Cliente c = new Cliente();
        int original = c.getScoreActual();
        c.mejorarScore(100);
        assertTrue(c.getScoreActual() > original);
        c.reducirScore(200);
        assertTrue(c.getScoreActual() >= 300);
    }

    // Prueba que se pueden agregar cuentas y créditos a un cliente
    @Test
    @DisplayName("Agregar cuentas y créditos")
    void agregarCuentasYCreditos() {
        Cliente c = new Cliente();
        c.agregarCuenta("CU-1");
        assertTrue(c.getCuentaIds().contains("CU-1"));
        c.agregarCredito("CR-1");
        assertTrue(c.getCreditoIds().contains("CR-1"));
    }
}
