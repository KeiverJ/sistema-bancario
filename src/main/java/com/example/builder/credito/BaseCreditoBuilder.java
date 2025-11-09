package com.example.builder.credito;

import com.example.model.credito.Credito;

/**
 * Builder base con fluent API.
 */
public class BaseCreditoBuilder implements CreditoBuilder {

    protected Credito credito;

    @Override
    public CreditoBuilder desdeBase(Credito base) {
        this.credito = base;
        return this;
    }

    @Override
    public CreditoBuilder conSeguroVida(boolean valor) {
        ensure();
        credito.setSeguroVida(valor);
        return this;
    }

    @Override
    public CreditoBuilder conSeguroDesempleo(boolean valor) {
        ensure();
        credito.setSeguroDesempleo(valor);
        return this;
    }

    @Override
    public CreditoBuilder conGarantia(String garantia) {
        ensure();
        credito.setGarantia(garantia);
        return this;
    }

    @Override
    public CreditoBuilder conCostoApertura(double costo) {
        ensure();
        credito.setCostoApertura(costo);
        return this;
    }

    @Override
    public CreditoBuilder conCuotaAdministracion(double cuota) {
        ensure();
        credito.setCuotaAdministracion(cuota);
        return this;
    }

    @Override
    public Credito build() {
        ensure();
        return credito;
    }

    protected void ensure() {
        if (credito == null) {
            credito = new Credito();
        }
    }
}