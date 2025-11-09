package com.example.factory.persona;

import com.example.model.credito.Credito;
import com.example.model.cuenta.Cuenta;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.example.factory.common.ProductoBancarioFactory;
import com.example.factory.validacion.Validador;
import com.example.factory.validacion.ValidadorNIT;

public class FactoryPersonaJuridica implements ProductoBancarioFactory {
    private static final AtomicInteger contadorCuenta = new AtomicInteger(2001);
    private static final AtomicInteger contadorCredito = new AtomicInteger(2001);
    private static final AtomicLong contadorNumeroCuenta = new AtomicLong(2000000000L);

    @Override
    public Cuenta crearCuenta(String clienteId, Cuenta.TipoCuenta tipoCuenta, double saldoInicial) {
        Cuenta c = new Cuenta();
        c.setId(UUID.randomUUID().toString());
        c.setCodigo("CTA-" + String.format("%04d", contadorCuenta.getAndIncrement()));
        c.setClienteId(clienteId);
        c.setTipoCuenta(tipoCuenta);
        c.setSaldo(saldoInicial);
        c.setNumeroCuenta(generarNumeroCuenta());
        return c;
    }

    @Override
    public Credito crearCredito(String clienteId, Credito.TipoCredito tipoCredito, double monto, int plazoMeses) {
        Credito cr = new Credito();
        cr.setId(UUID.randomUUID().toString());
        cr.setCodigo("CRE-" + String.format("%04d", contadorCredito.getAndIncrement()));
        cr.setClienteId(clienteId);
        cr.setTipoCredito(tipoCredito);
        cr.setMonto(monto);
        cr.setSaldo(monto);
        cr.setPlazoMeses(plazoMeses);
        return cr;
    }

    private String generarNumeroCuenta() {
        return String.valueOf(contadorNumeroCuenta.getAndIncrement());
    }

    @Override
    public Validador crearValidadorDocumento() {
        return new ValidadorNIT();
    }
}