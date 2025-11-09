package com.example.factory;

import com.example.factory.deposito.DepositoFactory;
import com.example.factory.pagos.PagoServicioFactory;
import com.example.factory.transferencia.TransferenciaFactory;
import com.example.model.cliente.Cliente;
import com.example.model.cuenta.Cuenta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

class FactoryE2ETest {

    @Test
    @DisplayName("E2E: abrir cuenta vía provider de fábrica")
    void e2e_abrirCuenta_viaFabrica() {
        TestAppContext ctx = TestAppContext.build();
        Cliente c = ctx.clienteService.crearCliente("Fab Cliente", "CC", "333", Cliente.TipoCliente.PERSONA_NATURAL);
        Cuenta cuenta = ctx.cuentaService.abrirCuenta(c.getId(), Cuenta.TipoCuenta.AHORROS, 100_000);
        assertNotNull(cuenta.getId());
        assertEquals(Cuenta.TipoCuenta.AHORROS, cuenta.getTipoCuenta());
    }
    @Test
    @DisplayName("E2E: abrir crédito vía provider de fábrica")
    void e2e_abrirCredito_viaFabrica() {
        TestAppContext ctx = TestAppContext.build();
        Cliente c = ctx.clienteService.crearCliente("Fab Cliente", "CC", "444", Cliente.TipoCliente.PERSONA_JURIDICA);
        var credito = ctx.creditoService.solicitarCredito(c.getId(), com.example.model.credito.Credito.TipoCredito.CONSUMO, 50_000, 24);
        assertNotNull(credito.getId());
        assertEquals(com.example.model.credito.Credito.TipoCredito.CONSUMO, credito.getTipoCredito());
        assertEquals(50_000, credito.getMonto());
    }

    @Test
    @DisplayName("E2E: crear transacción de transferencia usando factory")
    void e2e_transferenciaFactory() {
        TransferenciaFactory factory = new TransferenciaFactory("cuenta1", "cuenta2", 2000.0, "Pago préstamo");
        var t = factory.nueva();
        assertEquals(com.example.model.transacccion.Transaccion.TipoTransaccion.TRANSFERENCIA, t.getTipo());
        assertEquals("cuenta1", t.getCuentaOrigenId());
        assertEquals("cuenta2", t.getCuentaDestinoId());
        assertEquals(2000.0, t.getMonto());
        assertTrue(t.getDescripcion().contains("Pago préstamo"));
    }

    @Test
    @DisplayName("E2E: crear depósito y pago de servicio usando factories")
    void e2e_depositoYPagoServicio() {
        DepositoFactory depFactory = new DepositoFactory("cuenta3", 5000.0, "Depósito inicial");
        var dep = depFactory.nueva();
        assertEquals(com.example.model.transacccion.Transaccion.TipoTransaccion.DEPOSITO, dep.getTipo());
        assertEquals("cuenta3", dep.getCuentaDestinoId());
        assertEquals(5000.0, dep.getMonto());

        PagoServicioFactory pagoFactory = new PagoServicioFactory("cuenta3", "AGUA", 120.0, "REF-AGUA-1");
        var pago = pagoFactory.nueva();
        assertEquals(com.example.model.transacccion.Transaccion.TipoTransaccion.PAGO_SERVICIO, pago.getTipo());
        assertEquals("cuenta3", pago.getCuentaOrigenId());
        assertEquals(120.0, pago.getMonto());
        assertTrue(pago.getDescripcion().contains("AGUA"));
        assertTrue(pago.getDescripcion().contains("REF-AGUA-1"));
    }
}
