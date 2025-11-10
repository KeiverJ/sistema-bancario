package com.example.builder;

import com.example.builder.credito.BaseCreditoBuilder;
import com.example.builder.credito.CreditoBuilderRegistry;
import com.example.builder.credito.tipos.ConsumoCreditoBuilder;
import com.example.builder.credito.tipos.HipotecarioCreditoBuilder;
import com.example.builder.credito.tipos.LibreInversionCreditoBuilder;
import com.example.builder.credito.tipos.VehiculoCreditoBuilder;
import com.example.model.credito.Credito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba unitaria del patrón Builder usando {@link VehiculoCreditoBuilder}.
 * Foco: lógica interna del builder sin repositorios ni estrategias externas.
 */
class BuilderUnitTest {

    // Verifica que VehiculoCreditoBuilder construye crédito con valores por defecto y sobreescritos
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

    // Verifica que ConsumoCreditoBuilder aplica valores por defecto y sobreescritos
    @Test
    @DisplayName("ConsumoCreditoBuilder: defaults y overrides")
    void consumoBuilder_defaultsYOverrides() {
        Credito base = new Credito();
        base.setMonto(5000);
        base.setPlazoMeses(12);

        Credito credito = new ConsumoCreditoBuilder()
                .desdeBase(base)
                .conSeguroVida(true)
                .conSeguroDesempleo(true)
                .conCostoApertura(100.0)
                .conCuotaAdministracion(10.0)
                .build();

        assertNotNull(credito);
        assertEquals(5000, credito.getMonto());
        assertEquals(12, credito.getPlazoMeses());
        assertTrue(credito.isSeguroVida());
        assertTrue(credito.isSeguroDesempleo());
        assertEquals(100.0, credito.getCostoApertura());
        assertEquals(10.0, credito.getCuotaAdministracion());
        assertEquals("SIN_GARANTIA", credito.getGarantia());
    }

    // Verifica que LibreInversionCreditoBuilder aplica garantía por default y sobreescritos
    @Test
    @DisplayName("LibreInversionCreditoBuilder: garantía por default y overrides")
    void libreInversionBuilder_defaultsYOverrides() {
        Credito base = new Credito();
        base.setMonto(8000);
        base.setPlazoMeses(24);

        Credito credito = new LibreInversionCreditoBuilder()
                .desdeBase(base)
                .conSeguroVida(false)
                .conSeguroDesempleo(true)
                .conCostoApertura(120.0)
                .conCuotaAdministracion(12.0)
                .build();

        assertNotNull(credito);
        assertEquals(8000, credito.getMonto());
        assertEquals(24, credito.getPlazoMeses());
        assertFalse(credito.isSeguroVida());
        assertTrue(credito.isSeguroDesempleo());
        assertEquals(120.0, credito.getCostoApertura());
        assertEquals(12.0, credito.getCuotaAdministracion());
        assertEquals("SIN_GARANTIA", credito.getGarantia());
    }

    // Verifica que HipotecarioCreditoBuilder requiere garantía y seguro de vida obligatorios
    @Test
    @DisplayName("HipotecarioCreditoBuilder: garantía y seguro de vida obligatorios")
    void hipotecarioBuilder_defaultsYOverrides() {
        Credito base = new Credito();
        base.setMonto(200000);
        base.setPlazoMeses(240);

        Credito credito = new HipotecarioCreditoBuilder()
                .desdeBase(base)
                .conSeguroDesempleo(true)
                .conCostoApertura(500.0)
                .conCuotaAdministracion(50.0)
                .build();

        assertNotNull(credito);
        assertEquals(200000, credito.getMonto());
        assertEquals(240, credito.getPlazoMeses());
        assertTrue(credito.isSeguroVida()); // siempre true
        assertTrue(credito.isSeguroDesempleo());
        assertEquals(500.0, credito.getCostoApertura());
        assertEquals(50.0, credito.getCuotaAdministracion());
        assertEquals("HIPOTECA", credito.getGarantia());
    }

    // Verifica que VehiculoCreditoBuilder asigna valores por default
    @Test
    @DisplayName("VehiculoCreditoBuilder: garantía y seguro de vida por default")
    void vehiculoBuilder_defaults() {
        Credito credito = new VehiculoCreditoBuilder()
                .conSeguroDesempleo(false)
                .build();
        assertNotNull(credito);
        assertTrue(credito.isSeguroVida()); // siempre true
        assertFalse(credito.isSeguroDesempleo());
        assertEquals("PRENDA", credito.getGarantia());
    }

    // Verifica que BaseCreditoBuilder construye correctamente y asegura valores
    @Test
    @DisplayName("BaseCreditoBuilder: construcción básica y ensure")
    void baseBuilder_construccionBasica() {
        BaseCreditoBuilder builder = new BaseCreditoBuilder();
        Credito credito = builder
                .conSeguroVida(true)
                .conSeguroDesempleo(false)
                .conGarantia("TEST")
                .conCostoApertura(10.0)
                .conCuotaAdministracion(2.0)
                .build();
        assertNotNull(credito);
        assertTrue(credito.isSeguroVida());
        assertFalse(credito.isSeguroDesempleo());
        assertEquals("TEST", credito.getGarantia());
        assertEquals(10.0, credito.getCostoApertura());
        assertEquals(2.0, credito.getCuotaAdministracion());
    }

    // Verifica que CreditoBuilderRegistry retorna el builder correcto para cada tipo de crédito
    @Test
    @DisplayName("CreditoBuilderRegistry: retorna builder correcto para cada tipo")
    void builderRegistry_retornaBuilderCorrecto() {
        CreditoBuilderRegistry registry = new CreditoBuilderRegistry();
        assertTrue(registry.get(Credito.TipoCredito.CONSUMO) instanceof ConsumoCreditoBuilder);
        assertTrue(registry.get(Credito.TipoCredito.LIBRE_INVERSION) instanceof LibreInversionCreditoBuilder);
        assertTrue(registry.get(Credito.TipoCredito.HIPOTECARIO) instanceof HipotecarioCreditoBuilder);
        assertTrue(registry.get(Credito.TipoCredito.VEHICULO) instanceof VehiculoCreditoBuilder);
    }
}
