package com.example.repository.Cuenta;

import com.example.model.Cliente;
import com.example.model.Cuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.TestAppContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para CuentaRepository.
 */
class CuentaRepositoryIntegrationTest {

    @Test
    @DisplayName("findByClienteId retorna cuentas del cliente")
    void findByClienteId_funciona() {
        TestAppContext ctx = TestAppContext.build();
        Cliente cliente = ctx.clienteService.crearCliente("IntCuentaRepo", "CC", "777", Cliente.TipoCliente.PERSONA_NATURAL);
        Cuenta cuenta = new Cuenta();
        cuenta.setId("cuenta-1");
        cuenta.setClienteId(cliente.getId());
        ctx.cuentaRepository.save(cuenta);
        var cuentas = ctx.cuentaRepository.findByClienteId(cliente.getId());
        assertTrue(cuentas.stream().anyMatch(c -> c.getId().equals("cuenta-1")));
    }
}
