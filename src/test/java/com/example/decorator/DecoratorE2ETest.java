package com.example.decorator;

import com.example.decorator.impl.BaseProductoComponent;
import com.example.decorator.core.ProductoFinancieroComponent;
import com.example.decorator.impl.SeguroVidaDecorator;
import com.example.decorator.impl.CashbackDecorator;
import com.example.model.cliente.Cliente;
import com.example.model.cuenta.Cuenta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorE2ETest {

    @Test
    @DisplayName("E2E: Decorar cuenta en paquete de productos")
    void e2e_decorarCuentaEnPaquete() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("Deco Cliente", "CC", "666", Cliente.TipoCliente.PERSONA_NATURAL);
    // Saldo mínimo cuenta corriente según BankConfig: >= 100_000
    Cuenta cuenta = ctx.cuentaService.abrirCuenta(cliente.getId(), Cuenta.TipoCuenta.CORRIENTE, 120_000);
        ProductoFinancieroComponent comp = new BaseProductoComponent(cuenta);
        comp = new SeguroVidaDecorator(comp);
        comp = new CashbackDecorator(comp);
        assertTrue(comp.getCostoMensual() > 0);
    }
}
