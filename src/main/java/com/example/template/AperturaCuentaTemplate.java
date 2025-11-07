package com.example.template;

import com.example.model.Cliente;
import com.example.model.Cuenta;

public abstract class AperturaCuentaTemplate {

    public final Cuenta abrirCuenta(Cliente cliente, Cuenta.TipoCuenta tipoCuenta, double saldoInicial) {
        validarCliente(cliente);
        validarReglas(cliente, tipoCuenta, saldoInicial);
        Cuenta cuenta = construirCuenta(cliente, tipoCuenta, saldoInicial);
        prePersist(cuenta, cliente);
        persist(cuenta, cliente);
        postPersist(cuenta, cliente);
        notificar(cuenta, cliente);
        return cuenta;
    }

    protected void validarCliente(Cliente cliente) {
        if (cliente == null)
            throw new IllegalArgumentException("Cliente no encontrado");
    }

    protected abstract void validarReglas(Cliente cliente, Cuenta.TipoCuenta tipoCuenta, double saldoInicial);

    protected abstract Cuenta construirCuenta(Cliente cliente, Cuenta.TipoCuenta tipoCuenta, double saldoInicial);

    protected void prePersist(Cuenta cuenta, Cliente cliente) {
    }

    protected abstract void persist(Cuenta cuenta, Cliente cliente);

    protected void postPersist(Cuenta cuenta, Cliente cliente) {
    }

    protected void notificar(Cuenta cuenta, Cliente cliente) {
    }
}