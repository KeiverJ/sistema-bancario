package com.example.factory.common;

import com.example.model.credito.Credito;
import com.example.model.cuenta.Cuenta;
import com.example.factory.validacion.Validador;

public interface ProductoBancarioFactory {
    Cuenta crearCuenta(String clienteId, Cuenta.TipoCuenta tipoCuenta, double saldoInicial);

    Credito crearCredito(String clienteId, Credito.TipoCredito tipoCredito, double monto, int plazoMeses);

    Validador crearValidadorDocumento();
}