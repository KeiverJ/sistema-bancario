package com.example.composite;

import com.example.decorator.impl.CashbackDecorator;
import com.example.decorator.impl.SeguroVidaDecorator;
import com.example.decorator.impl.BaseProductoComponent;
import com.example.model.cliente.Cliente;
import com.example.model.cuenta.Cuenta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

class CompositeE2ETest {

    // Verifica el flujo E2E: paquete con cuenta decorada calcula el costo total
    @Test
    @DisplayName("E2E: paquete con cuenta decorada calcula costo total")
    void e2e_paqueteCuentaDecorada() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("Comp Cliente", "CC", "777", Cliente.TipoCliente.PERSONA_NATURAL);
    // Saldo mínimo ahorros según BankConfig: >= 50_000
    Cuenta cuenta = ctx.cuentaService.abrirCuenta(cliente.getId(), Cuenta.TipoCuenta.AHORROS, 60_000);
        var base = new BaseProductoComponent(cuenta);
        var decorada = new SeguroVidaDecorator(new CashbackDecorator(base));
        PaqueteProductos paquete = new PaqueteProductos("Combo Premium");
        paquete.add(decorada);
        assertTrue(paquete.getCostoMensual() > 0);
    }
}
