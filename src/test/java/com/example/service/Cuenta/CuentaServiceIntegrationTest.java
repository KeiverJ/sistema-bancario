package com.example.service.Cuenta;

import com.example.model.Cliente;
import com.example.model.Cuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para CuentaService.
 */
class CuentaServiceIntegrationTest {

    @Test
    @DisplayName("abrirCuenta y obtenerCuenta funcionan")
    void abrirYObtenerCuenta() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("IntCuenta", "CC", "444", Cliente.TipoCliente.PERSONA_NATURAL);
        Cuenta cuenta = ctx.cuentaService.abrirCuenta(cliente.getId(), Cuenta.TipoCuenta.AHORROS, 60000);
        var encontrada = ctx.cuentaService.obtenerCuenta(cuenta.getId());
        assertTrue(encontrada.isPresent());
        assertEquals(60000, encontrada.get().getSaldo(), 0.01);
    }

    @Test
    @DisplayName("depositar y retirar actualizan saldo")
    void depositarYRetirar_funcionan() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("IntCuenta2", "CC", "555", Cliente.TipoCliente.PERSONA_NATURAL);
        Cuenta cuenta = ctx.cuentaService.abrirCuenta(cliente.getId(), Cuenta.TipoCuenta.AHORROS, 60000);
        assertTrue(ctx.cuentaService.depositar(cuenta.getId(), 5000));
        assertTrue(ctx.cuentaService.retirar(cuenta.getId(), 2000));
    }
}
