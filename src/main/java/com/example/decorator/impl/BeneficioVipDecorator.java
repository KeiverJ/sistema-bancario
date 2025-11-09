package com.example.decorator.impl;

import com.example.decorator.core.ProductoDecorator;
import com.example.decorator.core.ProductoFinancieroComponent;

public class BeneficioVipDecorator extends ProductoDecorator {

    public BeneficioVipDecorator(ProductoFinancieroComponent wrappee) {
        super(wrappee);
    }

    @Override
    public double getCostoMensual() {
        return super.getCostoMensual() + 20000;
    }

    @Override
    public double getBeneficioMensual() {
        return super.getBeneficioMensual() + 10000;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + VIP";
    }
}