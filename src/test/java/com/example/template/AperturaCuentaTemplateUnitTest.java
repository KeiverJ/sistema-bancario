package com.example.template;

import com.example.model.cliente.Cliente;
import com.example.model.cuenta.Cuenta;
import com.example.template.core.AperturaCuentaTemplate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AperturaCuentaTemplateUnitTest {

    static class DummyTemplate extends AperturaCuentaTemplate {
        boolean reglas, construir, persist, pre, post, notificar;
        @Override
        protected void validarReglas(Cliente cliente, Cuenta.TipoCuenta tipoCuenta, double saldoInicial) { reglas = true; }
        @Override
        protected Cuenta construirCuenta(Cliente cliente, Cuenta.TipoCuenta tipoCuenta, double saldoInicial) { construir = true; return new Cuenta(); }
        @Override
        protected void persist(Cuenta cuenta, Cliente cliente) { persist = true; }
        @Override
        protected void prePersist(Cuenta cuenta, Cliente cliente) { pre = true; }
        @Override
        protected void postPersist(Cuenta cuenta, Cliente cliente) { post = true; }
        @Override
        protected void notificar(Cuenta cuenta, Cliente cliente) { notificar = true; }
    }

    @Test
    @DisplayName("abrirCuenta ejecuta el flujo completo y hooks")
    void abrirCuentaFlujoCompleto() {
        DummyTemplate tpl = new DummyTemplate();
        Cliente cli = new Cliente();
        cli.setId("cli1");
        Cuenta c = tpl.abrirCuenta(cli, Cuenta.TipoCuenta.AHORROS, 1000);
        assertNotNull(c);
        assertTrue(tpl.reglas);
        assertTrue(tpl.construir);
        assertTrue(tpl.persist);
        assertTrue(tpl.pre);
        assertTrue(tpl.post);
        assertTrue(tpl.notificar);
    }

    @Test
    @DisplayName("abrirCuenta lanza excepción si cliente es null")
    void abrirCuentaClienteNull() {
        DummyTemplate tpl = new DummyTemplate();
        assertThrows(IllegalArgumentException.class, () -> tpl.abrirCuenta(null, Cuenta.TipoCuenta.AHORROS, 1000));
    }
}
