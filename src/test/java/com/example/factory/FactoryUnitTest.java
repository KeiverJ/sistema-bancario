package com.example.factory;

import com.example.factory.common.FabricaProductosProvider;
import com.example.factory.common.ProductoBancarioFactory;
import com.example.factory.deposito.DepositoFactory;
import com.example.factory.pagos.PagoServicioFactory;
import com.example.factory.persona.FactoryExtranjero;
import com.example.factory.persona.FactoryPersonaJuridica;
import com.example.factory.persona.FactoryPersonaNatural;
import com.example.factory.retiro.RetiroFactory;
import com.example.factory.transferencia.TransferenciaFactory;
import com.example.factory.validacion.Validador;
import com.example.factory.validacion.ValidadorCedula;
import com.example.factory.validacion.ValidadorNIT;
import com.example.factory.validacion.ValidadorPasaporte;
import com.example.model.cliente.Cliente;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryUnitTest {

    // Verifica que FactoryPersonaJuridica crea un crédito válido
    @Test
    @DisplayName("FactoryPersonaJuridica: crearCredito retorna crédito válido")
    void personaJuridica_crearCredito_valido() {
        FactoryPersonaJuridica factory = new FactoryPersonaJuridica();
        var credito = factory.crearCredito("cliJ", com.example.model.credito.Credito.TipoCredito.HIPOTECARIO, 100000, 36);
        assertNotNull(credito.getId());
        assertEquals("cliJ", credito.getClienteId());
        assertEquals(com.example.model.credito.Credito.TipoCredito.HIPOTECARIO, credito.getTipoCredito());
        assertEquals(100000, credito.getMonto());
        assertEquals(100000, credito.getSaldo());
        assertEquals(36, credito.getPlazoMeses());
        assertNotNull(credito.getCodigo());
    }

    // Verifica que FactoryPersonaJuridica maneja casos límite de monto cero y plazo negativo
    @Test
    @DisplayName("FactoryPersonaJuridica: crearCredito con monto cero y plazo negativo")
    void personaJuridica_crearCredito_casosLimite() {
        FactoryPersonaJuridica factory = new FactoryPersonaJuridica();
        var credito = factory.crearCredito("cliJ2", com.example.model.credito.Credito.TipoCredito.LIBRE_INVERSION, 0, -12);
        assertEquals(0, credito.getMonto());
        assertEquals(-12, credito.getPlazoMeses());
        assertEquals(0, credito.getSaldo());
    }

    // Verifica que FactoryPersonaJuridica retorna ValidadorNIT correctamente
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

    // Verifica que FactoryPersonaNatural maneja saldo cero y tipo nulo
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

    // Verifica que FactoryPersonaNatural crea crédito con monto negativo
    @Test
    @DisplayName("FactoryPersonaNatural: crearCredito con monto negativo")
    void personaNatural_crearCredito_montoNegativo() {
        FactoryPersonaNatural factory = new FactoryPersonaNatural();
        var credito = factory.crearCredito("cliY", com.example.model.credito.Credito.TipoCredito.LIBRE_INVERSION, -1000, 12);
        assertNotNull(credito.getId());
        assertEquals(-1000, credito.getMonto());
        assertEquals(12, credito.getPlazoMeses());
    }

    // Verifica que FactoryPersonaJuridica genera número de cuenta único
    @Test
    @DisplayName("FactoryPersonaJuridica: crearCuenta genera número único")
    void personaJuridica_crearCuenta_numeroUnico() {
        FactoryPersonaJuridica factory = new FactoryPersonaJuridica();
        var cuenta1 = factory.crearCuenta("cli1", com.example.model.cuenta.Cuenta.TipoCuenta.CORRIENTE, 1000);
        var cuenta2 = factory.crearCuenta("cli2", com.example.model.cuenta.Cuenta.TipoCuenta.CORRIENTE, 2000);
        assertNotEquals(cuenta1.getNumeroCuenta(), cuenta2.getNumeroCuenta());
    }

    // Verifica que FactoryExtranjero crea crédito con plazo cero
    @Test
    @DisplayName("FactoryExtranjero: crearCredito con plazo cero")
    void extranjero_crearCredito_plazoCero() {
        FactoryExtranjero factory = new FactoryExtranjero();
        var credito = factory.crearCredito("cliZ", com.example.model.credito.Credito.TipoCredito.HIPOTECARIO, 50000, 0);
        assertNotNull(credito.getId());
        assertEquals(0, credito.getPlazoMeses());
        assertEquals(50000, credito.getMonto());
    }

    // Verifica que FactoryPersonaNatural maneja clienteId nulo al crear cuenta
    @Test
    @DisplayName("FactoryPersonaNatural: crearCuenta con clienteId nulo")
    void personaNatural_crearCuenta_clienteIdNulo() {
        FactoryPersonaNatural factory = new FactoryPersonaNatural();
        var cuenta = factory.crearCuenta(null, com.example.model.cuenta.Cuenta.TipoCuenta.AHORROS, 100);
        assertNull(cuenta.getClienteId());
        assertEquals(100, cuenta.getSaldo());
    }
    // Verifica que FactoryPersonaNatural crea cuenta válida
    @Test
    @DisplayName("FactoryPersonaNatural crearCuenta retorna cuenta válida")
    void personaNatural_crearCuenta_valida() {
        FactoryPersonaNatural factory = new FactoryPersonaNatural();
        var c = factory.crearCuenta("cli", com.example.model.cuenta.Cuenta.TipoCuenta.AHORROS, 1000);
        assertNotNull(c.getId());
        assertEquals(com.example.model.cuenta.Cuenta.TipoCuenta.AHORROS, c.getTipoCuenta());
        assertEquals("cli", c.getClienteId());
        assertTrue(c.getSaldo() >= 1000);
    }

    // Verifica que FactoryPersonaJuridica crea cuenta válida
    @Test
    @DisplayName("FactoryPersonaJuridica crearCuenta retorna cuenta válida")
    void personaJuridica_crearCuenta_valida() {
        FactoryPersonaJuridica factory = new FactoryPersonaJuridica();
        var c = factory.crearCuenta("cli", com.example.model.cuenta.Cuenta.TipoCuenta.CORRIENTE, 5000);
        assertNotNull(c.getId());
        assertEquals(com.example.model.cuenta.Cuenta.TipoCuenta.CORRIENTE, c.getTipoCuenta());
        assertEquals("cli", c.getClienteId());
        assertTrue(c.getSaldo() >= 5000);
    }

    // Verifica que FactoryExtranjero crea cuenta válida
    @Test
    @DisplayName("FactoryExtranjero crearCuenta retorna cuenta válida")
    void extranjero_crearCuenta_valida() {
        FactoryExtranjero factory = new FactoryExtranjero();
        var c = factory.crearCuenta("cli", com.example.model.cuenta.Cuenta.TipoCuenta.AHORROS, 1000);
        assertNotNull(c.getId());
        assertEquals(com.example.model.cuenta.Cuenta.TipoCuenta.AHORROS, c.getTipoCuenta());
        assertEquals("cli", c.getClienteId());
        assertTrue(c.getSaldo() >= 1000);
    }

    // Verifica que RetiroFactory crea transacción de retiro válida
    @Test
    @DisplayName("RetiroFactory crear() retorna transacción de retiro válida")
    void retiroFactory_crear_retiroValido() {
        RetiroFactory factory = new RetiroFactory("cuenta1", 500.0, "Cajero Centro");
        var t = factory.nueva();
        assertEquals(com.example.model.transacccion.Transaccion.TipoTransaccion.RETIRO, t.getTipo());
        assertEquals("cuenta1", t.getCuentaOrigenId());
        assertEquals(500.0, t.getMonto());
        assertTrue(t.getDescripcion().contains("Cajero Centro"));
    }
    // Verifica que getFactory lanza excepción si el tipo es nulo
    @Test
    @DisplayName("getFactory lanza excepción si tipo es nulo")
    void getFactory_tipoNulo() {
        FabricaProductosProvider provider = new FabricaProductosProvider();
        assertThrows(IllegalArgumentException.class, () -> provider.getFactory(null));
    }

    // Verifica que getFactory retorna todas las factories válidas para cada tipo de cliente
    @Test
    @DisplayName("getFactory retorna todas las factories válidas")
    void getFactory_todosTipos() {
        FabricaProductosProvider provider = new FabricaProductosProvider();
        for (Cliente.TipoCliente tipo : Cliente.TipoCliente.values()) {
            assertNotNull(provider.getFactory(tipo));
        }
    }

    // Verifica que FabricaProductosProvider retorna la fábrica según el tipo de cliente
    @Test
    @DisplayName("FabricaProductosProvider retorna fábrica según tipo de cliente")
    void providerRetornaFabrica() {
        FabricaProductosProvider provider = new FabricaProductosProvider();
        ProductoBancarioFactory factory = provider.getFactory(Cliente.TipoCliente.PERSONA_NATURAL);
        assertNotNull(factory);
    }
    // Verifica que TransferenciaFactory crea transacción de transferencia válida
    @Test
    @DisplayName("TransferenciaFactory crea transacción de transferencia válida")
    void transferenciaFactory_crear_transferenciaValida() {
        TransferenciaFactory factory = new TransferenciaFactory("cuenta1", "cuenta2", 1500.0, "Pago amigo");
        var t = factory.nueva();
        assertEquals(com.example.model.transacccion.Transaccion.TipoTransaccion.TRANSFERENCIA, t.getTipo());
        assertEquals("cuenta1", t.getCuentaOrigenId());
        assertEquals("cuenta2", t.getCuentaDestinoId());
        assertEquals(1500.0, t.getMonto());
        assertTrue(t.getDescripcion().contains("Pago amigo"));
    }

    // Verifica que PagoServicioFactory crea transacción de pago de servicio válida
    @Test
    @DisplayName("PagoServicioFactory crea transacción de pago de servicio válida")
    void pagoServicioFactory_crear_pagoServicioValido() {
        PagoServicioFactory factory = new PagoServicioFactory("cuenta1", "ENERGIA", 200.0, "REF123");
        var t = factory.nueva();
        assertEquals(com.example.model.transacccion.Transaccion.TipoTransaccion.PAGO_SERVICIO, t.getTipo());
        assertEquals("cuenta1", t.getCuentaOrigenId());
        assertEquals(200.0, t.getMonto());
        assertTrue(t.getDescripcion().contains("ENERGIA"));
        assertTrue(t.getDescripcion().contains("REF123"));
    }

    // Verifica que DepositoFactory crea transacción de depósito válida
    @Test
    @DisplayName("DepositoFactory crea transacción de depósito válida")
    void depositoFactory_crear_depositoValido() {
        DepositoFactory factory = new DepositoFactory("cuenta2", 300.0, "Depósito en ventanilla");
        var t = factory.nueva();
        assertEquals(com.example.model.transacccion.Transaccion.TipoTransaccion.DEPOSITO, t.getTipo());
        assertEquals("cuenta2", t.getCuentaDestinoId());
        assertEquals(300.0, t.getMonto());
        assertTrue(t.getDescripcion().contains("ventanilla"));
    }

    // Verifica que ValidadorCedula valida correctamente cédulas
    @Test
    @DisplayName("ValidadorCedula valida correctamente cédulas")
    void validadorCedula_valida() {
        Validador v = new ValidadorCedula();
        assertTrue(v.validar("1234567"));
        assertFalse(v.validar("ABC123"));
        assertEquals("CC", v.tipo());
    }

    // Verifica que ValidadorNIT valida correctamente NITs
    @Test
    @DisplayName("ValidadorNIT valida correctamente NITs")
    void validadorNIT_valida() {
        Validador v = new ValidadorNIT();
        assertTrue(v.validar("123456789-1"));
        assertFalse(v.validar("123456789"));
        assertEquals("NIT", v.tipo());
    }

    // Verifica que ValidadorPasaporte valida correctamente pasaportes
    @Test
    @DisplayName("ValidadorPasaporte valida correctamente pasaportes")
    void validadorPasaporte_valida() {
        Validador v = new ValidadorPasaporte();
        assertTrue(v.validar("A12345B"));
        assertFalse(v.validar("123"));
        assertEquals("PASAPORTE", v.tipo());
    }
}
