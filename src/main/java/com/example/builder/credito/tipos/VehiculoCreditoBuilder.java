package com.example.builder.credito.tipos;

import com.example.builder.credito.CreditoBuilder;
import com.example.model.credito.Credito;

public class VehiculoCreditoBuilder implements CreditoBuilder {

    private Credito credito;

    public VehiculoCreditoBuilder() {
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
        // Configuración específica para vehículo
        if (credito.getGarantia() == null) {
            credito.setGarantia("PRENDA");
        }
        // Los créditos de vehículo suelen tener seguro de vida
        credito.setSeguroVida(true);
        return credito;
    }
}