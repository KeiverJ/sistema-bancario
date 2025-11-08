package com.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductoFinancieroUnitTest {
    @Test
    void gettersYSettersBasicos() {
        Cuenta cuenta = new Cuenta();
        cuenta.setId("ID-1");
        cuenta.setClienteId("CL-1");
        cuenta.setEstadoCodigo("ACTIVA");
        assertEquals("ID-1", cuenta.getId());
        assertEquals("CL-1", cuenta.getClienteId());
        assertEquals("ACTIVA", cuenta.getEstadoCodigo());
        assertNotNull(cuenta.getFechaCreacion());
    }
}
