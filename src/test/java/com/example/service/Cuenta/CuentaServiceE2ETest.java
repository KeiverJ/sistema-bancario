package com.example.service.Cuenta;

import com.example.model.Cliente;
import com.example.model.Cuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas E2E para CuentaService: flujo completo de cuenta.
 */
class CuentaServiceE2ETest {

    @Test
    @DisplayName("Flujo E2E: abrir, depositar, retirar y consultar cuenta")
    void flujoCompletoCuenta() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("E2ECuenta", "CC", "666", Cliente.TipoCliente.PERSONA_NATURAL);
        Cuenta cuenta = ctx.cuentaService.abrirCuenta(cliente.getId(), Cuenta.TipoCuenta.AHORROS, 60000);
        assertTrue(ctx.cuentaService.depositar(cuenta.getId(), 10000));
        assertTrue(ctx.cuentaService.retirar(cuenta.getId(), 5000));
        var encontrada = ctx.cuentaService.obtenerCuenta(cuenta.getId());
        assertTrue(encontrada.isPresent());
        assertTrue(encontrada.get().getSaldo() > 0);
    }
}
