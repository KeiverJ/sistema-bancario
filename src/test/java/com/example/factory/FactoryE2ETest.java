package com.example.factory;

import com.example.model.Cliente;
import com.example.model.Cuenta;
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
}
