package com.example.service.Transaccion;

import com.example.model.Cliente;
import com.example.model.Cuenta;
import com.example.model.Transaccion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas E2E para TransaccionService: flujo completo de transferencia.
 */
class TransaccionServiceE2ETest {

    @Test
    @DisplayName("Flujo E2E: crear cliente, cuenta, transferencia y consultar transacción")
    void flujoCompletoTransaccion() {
        TestAppContext ctx = TestAppContext.build();
        Cliente origen = ctx.clienteService.crearCliente("Origen", "CC", "101", Cliente.TipoCliente.PERSONA_NATURAL);
        Cliente destino = ctx.clienteService.crearCliente("Destino", "CC", "202", Cliente.TipoCliente.PERSONA_NATURAL);
        Cuenta cuentaOrigen = ctx.cuentaService.abrirCuenta(origen.getId(), Cuenta.TipoCuenta.AHORROS, 60000);
        Cuenta cuentaDestino = ctx.cuentaService.abrirCuenta(destino.getId(), Cuenta.TipoCuenta.AHORROS, 60000);
        Transaccion tx = ctx.transaccionService.crearTransferencia(cuentaOrigen.getId(), cuentaDestino.getId(), 5000, "Pago E2E");
        assertNotNull(tx.getId());
        var encontrada = ctx.transaccionService.obtenerTransaccion(tx.getId());
        assertTrue(encontrada.isPresent());
        assertEquals("Pago E2E", encontrada.get().getDescripcion());
    }
}
