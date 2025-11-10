package com.example.model.credito;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreditoUnitTest {
    // Prueba que los getters y setters de Credito funcionan correctamente
    // Prueba que los getters y setters de Credito funcionan correctamente
    @Test
    void gettersYSettersBasicos() {
        Credito credito = new Credito();
        credito.setCodigo("CR-1");
        credito.setTipoCredito(Credito.TipoCredito.LIBRE_INVERSION);
        credito.setMonto(8000);
        credito.setSaldo(2000);
        credito.setPlazoMeses(24);
        credito.setTasaInteres(0.15);
        credito.setSeguroVida(true);
        credito.setSeguroDesempleo(false);
        credito.setGarantia("INMUEBLE");
        credito.setCostoApertura(100);
        credito.setCuotaAdministracion(20);
        credito.setEstadoActual(Credito.EstadoCredito.APROBADO);

        assertEquals("CR-1", credito.getCodigo());
        assertEquals(Credito.TipoCredito.LIBRE_INVERSION, credito.getTipoCredito());
        assertEquals(8000, credito.getMonto());
        assertEquals(2000, credito.getSaldo());
        assertEquals(24, credito.getPlazoMeses());
        assertEquals(0.15, credito.getTasaInteres());
        assertTrue(credito.isSeguroVida());
        assertFalse(credito.isSeguroDesempleo());
        assertEquals("INMUEBLE", credito.getGarantia());
        assertEquals(100, credito.getCostoApertura());
        assertEquals(20, credito.getCuotaAdministracion());
        assertEquals(Credito.EstadoCredito.APROBADO, credito.getEstadoActual());
    }
}
