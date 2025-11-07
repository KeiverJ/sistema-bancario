package com.example.factory;

import com.example.model.Cuenta;
import com.example.model.Credito;

public interface ProductoBancarioFactory {
    Cuenta crearCuenta(String clienteId, Cuenta.TipoCuenta tipoCuenta, double saldoInicial);

    Credito crearCredito(String clienteId, Credito.TipoCredito tipoCredito, double monto, int plazoMeses);

    Validador crearValidadorDocumento();
}