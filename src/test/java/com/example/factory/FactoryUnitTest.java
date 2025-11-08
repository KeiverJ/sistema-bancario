package com.example.factory;

import com.example.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryUnitTest {
    @Test
    @DisplayName("FactoryPersonaNatural crearCuenta retorna cuenta válida")
    void personaNatural_crearCuenta_valida() {
        FactoryPersonaNatural factory = new FactoryPersonaNatural();
        var c = factory.crearCuenta("cli", com.example.model.Cuenta.TipoCuenta.AHORROS, 1000);
        assertNotNull(c.getId());
        assertEquals(com.example.model.Cuenta.TipoCuenta.AHORROS, c.getTipoCuenta());
        assertEquals("cli", c.getClienteId());
        assertTrue(c.getSaldo() >= 1000);
    }

    @Test
    @DisplayName("FactoryPersonaJuridica crearCuenta retorna cuenta válida")
    void personaJuridica_crearCuenta_valida() {
        FactoryPersonaJuridica factory = new FactoryPersonaJuridica();
        var c = factory.crearCuenta("cli", com.example.model.Cuenta.TipoCuenta.CORRIENTE, 5000);
        assertNotNull(c.getId());
        assertEquals(com.example.model.Cuenta.TipoCuenta.CORRIENTE, c.getTipoCuenta());
        assertEquals("cli", c.getClienteId());
        assertTrue(c.getSaldo() >= 5000);
    }

    @Test
    @DisplayName("FactoryExtranjero crearCuenta retorna cuenta válida")
    void extranjero_crearCuenta_valida() {
        FactoryExtranjero factory = new FactoryExtranjero();
        var c = factory.crearCuenta("cli", com.example.model.Cuenta.TipoCuenta.AHORROS, 1000);
        assertNotNull(c.getId());
        assertEquals(com.example.model.Cuenta.TipoCuenta.AHORROS, c.getTipoCuenta());
        assertEquals("cli", c.getClienteId());
        assertTrue(c.getSaldo() >= 1000);
    }

    @Test
    @DisplayName("RetiroFactory crear() retorna transacción de retiro válida")
    void retiroFactory_crear_retiroValido() {
        RetiroFactory factory = new RetiroFactory("cuenta1", 500.0, "Cajero Centro");
        var t = factory.crear();
        assertEquals(com.example.model.Transaccion.TipoTransaccion.RETIRO, t.getTipo());
        assertEquals("cuenta1", t.getCuentaOrigenId());
        assertEquals(500.0, t.getMonto());
        assertTrue(t.getDescripcion().contains("Cajero Centro"));
    }
    @Test
    @DisplayName("getFactory lanza excepción si tipo es nulo")
    void getFactory_tipoNulo() {
        FabricaProductosProvider provider = new FabricaProductosProvider();
        assertThrows(IllegalArgumentException.class, () -> provider.getFactory(null));
    }

    @Test
    @DisplayName("getFactory retorna todas las factories válidas")
    void getFactory_todosTipos() {
        FabricaProductosProvider provider = new FabricaProductosProvider();
        for (Cliente.TipoCliente tipo : Cliente.TipoCliente.values()) {
            assertNotNull(provider.getFactory(tipo));
        }
    }

    @Test
    @DisplayName("FabricaProductosProvider retorna fábrica según tipo de cliente")
    void providerRetornaFabrica() {
        FabricaProductosProvider provider = new FabricaProductosProvider();
        ProductoBancarioFactory factory = provider.getFactory(Cliente.TipoCliente.PERSONA_NATURAL);
        assertNotNull(factory);
    }
}
