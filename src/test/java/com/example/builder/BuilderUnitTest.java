package com.example.builder;

import com.example.model.Credito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba unitaria del patrón Builder usando {@link VehiculoCreditoBuilder}.
 * Foco: lógica interna del builder sin repositorios ni estrategias externas.
 */
class BuilderUnitTest {

    @Test
    @DisplayName("VehiculoCreditoBuilder construye crédito con defaults y overrides")
    void vehiculoBuilder_creaCreditoConSeguroYGarantia() {
        Credito base = new Credito();
        base.setMonto(15000);
        base.setPlazoMeses(36);

        Credito credito = new VehiculoCreditoBuilder()
                .desdeBase(base)
                .conGarantia("AUTO")
                .conSeguroVida(true)
                .conCostoApertura(250.0)
                .conCuotaAdministracion(35.0)
                .build();

        assertNotNull(credito);
        assertEquals(15000, credito.getMonto());
        assertEquals(36, credito.getPlazoMeses());
        assertEquals("AUTO", credito.getGarantia());
        assertTrue(credito.isSeguroVida());
        assertEquals(250.0, credito.getCostoApertura());
        assertEquals(35.0, credito.getCuotaAdministracion());
    }
}
