package com.example.builder;

import com.example.builder.credito.CreditoBuilder;
import com.example.builder.credito.CreditoBuilderRegistry;
import com.example.model.credito.Credito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integración básica: registro + recuperación de builder y construcción de objeto.
 */
class BuilderIntegrationTest {

    // Verifica que el registro de builders permite registrar y construir correctamente
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
