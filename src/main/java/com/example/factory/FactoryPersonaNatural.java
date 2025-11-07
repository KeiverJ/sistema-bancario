package com.example.factory;

import com.example.model.Credito;
import com.example.model.Cuenta;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FactoryPersonaNatural implements ProductoBancarioFactory {
    private static final AtomicInteger contadorCuenta = new AtomicInteger(1);
    private static final AtomicInteger contadorCredito = new AtomicInteger(1);
    private static final AtomicLong contadorNumeroCuenta = new AtomicLong(1000000000L); // ← Esta línea

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
        return new ValidadorCedula();
    }
}