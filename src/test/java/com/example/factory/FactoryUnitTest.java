package com.example.factory;

import com.example.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryUnitTest {

    @Test
    @DisplayName("FactoryPersonaJuridica: crearCredito retorna crédito válido")
    void personaJuridica_crearCredito_valido() {
        FactoryPersonaJuridica factory = new FactoryPersonaJuridica();
        var credito = factory.crearCredito("cliJ", com.example.model.Credito.TipoCredito.HIPOTECARIO, 100000, 36);
        assertNotNull(credito.getId());
        assertEquals("cliJ", credito.getClienteId());
        assertEquals(com.example.model.Credito.TipoCredito.HIPOTECARIO, credito.getTipoCredito());
        assertEquals(100000, credito.getMonto());
        assertEquals(100000, credito.getSaldo());
        assertEquals(36, credito.getPlazoMeses());
        assertNotNull(credito.getCodigo());
    }

    @Test
    @DisplayName("FactoryPersonaJuridica: crearCredito con monto cero y plazo negativo")
    void personaJuridica_crearCredito_casosLimite() {
        FactoryPersonaJuridica factory = new FactoryPersonaJuridica();
        var credito = factory.crearCredito("cliJ2", com.example.model.Credito.TipoCredito.LIBRE_INVERSION, 0, -12);
        assertEquals(0, credito.getMonto());
        assertEquals(-12, credito.getPlazoMeses());
        assertEquals(0, credito.getSaldo());
    }

    @Test
    @DisplayName("FactoryPersonaJuridica: crearValidadorDocumento retorna ValidadorNIT")
    void personaJuridica_crearValidadorDocumento() {
        FactoryPersonaJuridica factory = new FactoryPersonaJuridica();
        Validador validador = factory.crearValidadorDocumento();
        assertNotNull(validador);
        assertEquals("NIT", validador.tipo());
        assertTrue(validador.validar("123456789-1"));
        assertFalse(validador.validar("12345678-1"));
    }

    @Test
    @DisplayName("FactoryPersonaNatural: crearCuenta con saldo cero y tipo nulo")
    void personaNatural_crearCuenta_casosLimite() {
        FactoryPersonaNatural factory = new FactoryPersonaNatural();
        var cuenta = factory.crearCuenta("cliX", null, 0);
        assertNotNull(cuenta.getId());
        assertNull(cuenta.getTipoCuenta());
        assertEquals(0, cuenta.getSaldo());
        assertEquals("cliX", cuenta.getClienteId());
        assertNotNull(cuenta.getNumeroCuenta());
    }

    @Test
    @DisplayName("FactoryPersonaNatural: crearCredito con monto negativo")
    void personaNatural_crearCredito_montoNegativo() {
        FactoryPersonaNatural factory = new FactoryPersonaNatural();
        var credito = factory.crearCredito("cliY", com.example.model.Credito.TipoCredito.LIBRE_INVERSION, -1000, 12);
        assertNotNull(credito.getId());
        assertEquals(-1000, credito.getMonto());
        assertEquals(12, credito.getPlazoMeses());
    }

    @Test
    @DisplayName("FactoryPersonaJuridica: crearCuenta genera número único")
    void personaJuridica_crearCuenta_numeroUnico() {
        FactoryPersonaJuridica factory = new FactoryPersonaJuridica();
        var cuenta1 = factory.crearCuenta("cli1", com.example.model.Cuenta.TipoCuenta.CORRIENTE, 1000);
        var cuenta2 = factory.crearCuenta("cli2", com.example.model.Cuenta.TipoCuenta.CORRIENTE, 2000);
        assertNotEquals(cuenta1.getNumeroCuenta(), cuenta2.getNumeroCuenta());
    }

    @Test
    @DisplayName("FactoryExtranjero: crearCredito con plazo cero")
    void extranjero_crearCredito_plazoCero() {
        FactoryExtranjero factory = new FactoryExtranjero();
        var credito = factory.crearCredito("cliZ", com.example.model.Credito.TipoCredito.HIPOTECARIO, 50000, 0);
        assertNotNull(credito.getId());
        assertEquals(0, credito.getPlazoMeses());
        assertEquals(50000, credito.getMonto());
    }

    @Test
    @DisplayName("FactoryPersonaNatural: crearCuenta con clienteId nulo")
    void personaNatural_crearCuenta_clienteIdNulo() {
        FactoryPersonaNatural factory = new FactoryPersonaNatural();
        var cuenta = factory.crearCuenta(null, com.example.model.Cuenta.TipoCuenta.AHORROS, 100);
        assertNull(cuenta.getClienteId());
        assertEquals(100, cuenta.getSaldo());
    }
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
    @Test
    @DisplayName("TransferenciaFactory crea transacción de transferencia válida")
    void transferenciaFactory_crear_transferenciaValida() {
        TransferenciaFactory factory = new TransferenciaFactory("cuenta1", "cuenta2", 1500.0, "Pago amigo");
        var t = factory.nueva();
        assertEquals(com.example.model.Transaccion.TipoTransaccion.TRANSFERENCIA, t.getTipo());
        assertEquals("cuenta1", t.getCuentaOrigenId());
        assertEquals("cuenta2", t.getCuentaDestinoId());
        assertEquals(1500.0, t.getMonto());
        assertTrue(t.getDescripcion().contains("Pago amigo"));
    }

    @Test
    @DisplayName("PagoServicioFactory crea transacción de pago de servicio válida")
    void pagoServicioFactory_crear_pagoServicioValido() {
        PagoServicioFactory factory = new PagoServicioFactory("cuenta1", "ENERGIA", 200.0, "REF123");
        var t = factory.nueva();
        assertEquals(com.example.model.Transaccion.TipoTransaccion.PAGO_SERVICIO, t.getTipo());
        assertEquals("cuenta1", t.getCuentaOrigenId());
        assertEquals(200.0, t.getMonto());
        assertTrue(t.getDescripcion().contains("ENERGIA"));
        assertTrue(t.getDescripcion().contains("REF123"));
    }

    @Test
    @DisplayName("DepositoFactory crea transacción de depósito válida")
    void depositoFactory_crear_depositoValido() {
        DepositoFactory factory = new DepositoFactory("cuenta2", 300.0, "Depósito en ventanilla");
        var t = factory.nueva();
        assertEquals(com.example.model.Transaccion.TipoTransaccion.DEPOSITO, t.getTipo());
        assertEquals("cuenta2", t.getCuentaDestinoId());
        assertEquals(300.0, t.getMonto());
        assertTrue(t.getDescripcion().contains("ventanilla"));
    }

    @Test
    @DisplayName("ValidadorCedula valida correctamente cédulas")
    void validadorCedula_valida() {
        Validador v = new ValidadorCedula();
        assertTrue(v.validar("1234567"));
        assertFalse(v.validar("ABC123"));
        assertEquals("CC", v.tipo());
    }

    @Test
    @DisplayName("ValidadorNIT valida correctamente NITs")
    void validadorNIT_valida() {
        Validador v = new ValidadorNIT();
        assertTrue(v.validar("123456789-1"));
        assertFalse(v.validar("123456789"));
        assertEquals("NIT", v.tipo());
    }

    @Test
    @DisplayName("ValidadorPasaporte valida correctamente pasaportes")
    void validadorPasaporte_valida() {
        Validador v = new ValidadorPasaporte();
        assertTrue(v.validar("A12345B"));
        assertFalse(v.validar("123"));
        assertEquals("PASAPORTE", v.tipo());
    }
}
