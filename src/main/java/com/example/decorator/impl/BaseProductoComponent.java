package com.example.decorator.impl;

import com.example.model.credito.Credito;
import com.example.model.cuenta.Cuenta;
import com.example.decorator.core.ProductoFinancieroComponent;

public class BaseProductoComponent implements ProductoFinancieroComponent {

    private final Cuenta cuenta;
    private final Credito credito;

    public BaseProductoComponent(Cuenta cuenta) {
        this.cuenta = cuenta;
        this.credito = null;
    }

    public BaseProductoComponent(Credito credito) {
        this.credito = credito;
        this.cuenta = null;
    }

    @Override
    public String getId() {
        return cuenta != null ? cuenta.getId() : (credito != null ? credito.getId() : null);
    }

    @Override
    public String getDescripcion() {
        if (cuenta != null) {
            return "Cuenta " + cuenta.getTipoCuenta() + " #" + cuenta.getNumeroCuenta();
        }
        if (credito != null) {
            return "Crédito " + credito.getTipoCredito() + " Monto=" + credito.getMonto();
        }
        return "Producto";
    }

    @Override
    public double getCostoMensual() {
        // Base: cuota manejo cuenta o 0 para crédito
        if (cuenta != null)
            return cuenta.getCuotaManejo();
        return 0.0;
    }

    @Override
    public double getBeneficioMensual() {
        return 0.0;
    }
}