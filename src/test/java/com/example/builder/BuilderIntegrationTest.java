package com.example.builder;

import com.example.model.Credito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integración básica: registro + recuperación de builder y construcción de objeto.
 */
class BuilderIntegrationTest {

    @Test
    @DisplayName("Registro de builders y uso desde registry")
    void registry_registraYConstruye() {
    CreditoBuilderRegistry registry = new CreditoBuilderRegistry();
    CreditoBuilder builder = registry.get(Credito.TipoCredito.VEHICULO);
        assertNotNull(builder);

        Credito credito = builder
                .conGarantia("CARRO")
                .conSeguroVida(true)
                .build();

        assertEquals("CARRO", credito.getGarantia());
        assertTrue(credito.isSeguroVida());
    }
}
