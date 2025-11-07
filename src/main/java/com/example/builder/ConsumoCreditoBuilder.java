package com.example.builder;

import com.example.model.Credito;

public class ConsumoCreditoBuilder implements CreditoBuilder {

    private Credito credito;

    public ConsumoCreditoBuilder() {
        this.credito = new Credito();
    }

    @Override
    public CreditoBuilder desdeBase(Credito base) {
        this.credito = base;
        return this;
    }

    @Override
    public CreditoBuilder conSeguroVida(boolean valor) {
        credito.setSeguroVida(valor);
        return this;
    }

    @Override
    public CreditoBuilder conSeguroDesempleo(boolean valor) {
        credito.setSeguroDesempleo(valor);
        return this;
    }

    @Override
    public CreditoBuilder conGarantia(String garantia) {
        credito.setGarantia(garantia);
        return this;
    }

    @Override
    public CreditoBuilder conCostoApertura(double costo) {
        credito.setCostoApertura(costo);
        return this;
    }

    @Override
    public CreditoBuilder conCuotaAdministracion(double cuota) {
        credito.setCuotaAdministracion(cuota);
        return this;
    }

    @Override
    public Credito build() {
        // Configuración específica para crédito de consumo
        if (credito.getGarantia() == null) {
            credito.setGarantia("SIN_GARANTIA");
        }
        return credito;
    }
}