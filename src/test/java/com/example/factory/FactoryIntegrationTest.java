package com.example.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.factory.common.FabricaProductosProvider;
import com.example.factory.common.ProductoBancarioFactory;
import com.example.factory.persona.FactoryExtranjero;
import com.example.factory.persona.FactoryPersonaJuridica;
import com.example.factory.persona.FactoryPersonaNatural;
import com.example.factory.validacion.Validador;
import com.example.factory.validacion.ValidadorCedula;
import com.example.factory.validacion.ValidadorNIT;

import static org.junit.jupiter.api.Assertions.*;

class FactoryIntegrationTest {

    @Test
    @DisplayName("Validadores de documento validan formatos esperados")
    void validadoresValidanFormato() {
        Validador vCed = new ValidadorCedula();
        Validador vNit = new ValidadorNIT();
        assertTrue(vCed.validar("1234567"));
        assertFalse(vCed.validar("ABC123"));
        assertTrue(vNit.validar("123456789-1"));
        assertFalse(vNit.validar("123456789"));
    }
    @Test
    @DisplayName("FactoryPersonaNatural y ValidadorCedula integran correctamente")
    void personaNatural_factoryYValidador() {
        FactoryPersonaNatural factory = new FactoryPersonaNatural();
        var cuenta = factory.crearCuenta("cli1", com.example.model.cuenta.Cuenta.TipoCuenta.AHORROS, 2000);
        assertNotNull(cuenta.getId());
        assertEquals("cli1", cuenta.getClienteId());
        Validador val = factory.crearValidadorDocumento();
        assertTrue(val.validar("1234567"));
        assertFalse(val.validar("ABC"));
    }

    @Test
    @DisplayName("FactoryPersonaJuridica y ValidadorNIT integran correctamente")
    void personaJuridica_factoryYValidador() {
        FactoryPersonaJuridica factory = new FactoryPersonaJuridica();
        var cuenta = factory.crearCuenta("cli2", com.example.model.cuenta.Cuenta.TipoCuenta.CORRIENTE, 5000);
        assertNotNull(cuenta.getId());
        assertEquals("cli2", cuenta.getClienteId());
        Validador val = factory.crearValidadorDocumento();
        assertTrue(val.validar("123456789-1"));
        assertFalse(val.validar("123456789"));
    }

    @Test
    @DisplayName("FactoryExtranjero y ValidadorPasaporte integran correctamente")
    void extranjero_factoryYValidador() {
        FactoryExtranjero factory = new FactoryExtranjero();
        var cuenta = factory.crearCuenta("cli3", com.example.model.cuenta.Cuenta.TipoCuenta.AHORROS, 3000);
        assertNotNull(cuenta.getId());
        assertEquals("cli3", cuenta.getClienteId());
        Validador val = factory.crearValidadorDocumento();
        assertTrue(val.validar("A12345B"));
        assertFalse(val.validar("123"));
    }

    @Test
    @DisplayName("FabricaProductosProvider integra con todas las factories")
    void provider_integracionTodas() {
        FabricaProductosProvider provider = new FabricaProductosProvider();
        for (com.example.model.cliente.Cliente.TipoCliente tipo : com.example.model.cliente.Cliente.TipoCliente.values()) {
            ProductoBancarioFactory factory = provider.getFactory(tipo);
            assertNotNull(factory);
            assertNotNull(factory.crearCuenta("cli", com.example.model.cuenta.Cuenta.TipoCuenta.AHORROS, 1000));
            assertNotNull(factory.crearCredito("cli", com.example.model.credito.Credito.TipoCredito.CONSUMO, 5000, 12));
        }
    }
}
