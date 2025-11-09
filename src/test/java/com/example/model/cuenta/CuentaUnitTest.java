package com.example.model.cuenta;

import org.junit.jupiter.api.Test;

import com.example.model.Cuenta;
import com.example.model.Cuenta.EstadoCuenta;
import com.example.model.Cuenta.TipoCuenta;

import static org.junit.jupiter.api.Assertions.*;

class CuentaUnitTest {
    @Test
    void gettersYSettersBasicos() {
        Cuenta cuenta = new Cuenta();
        cuenta.setCodigo("C-1");
        cuenta.setNumeroCuenta("123456");
        cuenta.setTipoCuenta(Cuenta.TipoCuenta.AHORROS);
        cuenta.setSaldo(5000);
        cuenta.setCuotaManejo(15);
        cuenta.setEstado(Cuenta.EstadoCuenta.ACTIVA);

        assertEquals("C-1", cuenta.getCodigo());
        assertEquals("123456", cuenta.getNumeroCuenta());
        assertEquals(Cuenta.TipoCuenta.AHORROS, cuenta.getTipoCuenta());
        assertEquals(5000, cuenta.getSaldo());
        assertEquals(15, cuenta.getCuotaManejo());
        assertEquals(Cuenta.EstadoCuenta.ACTIVA, cuenta.getEstado());
    }

    @Test
    void depositarYRetirar() {
        Cuenta cuenta = new Cuenta();
        cuenta.setEstado(Cuenta.EstadoCuenta.ACTIVA);
        cuenta.setSaldo(1000);
        assertTrue(cuenta.depositar(500));
        assertEquals(1500, cuenta.getSaldo());
        assertTrue(cuenta.retirar(200));
        assertEquals(1300, cuenta.getSaldo());
        assertFalse(cuenta.retirar(2000)); // saldo insuficiente
    }
}
