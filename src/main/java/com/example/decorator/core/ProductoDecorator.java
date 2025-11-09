package com.example.decorator.core;

public abstract class ProductoDecorator implements ProductoFinancieroComponent {
    protected final ProductoFinancieroComponent wrappee;

    protected ProductoDecorator(ProductoFinancieroComponent wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public String getId() {
        return wrappee.getId();
    }

    @Override
    public double getCostoMensual() {
        return wrappee.getCostoMensual();
    }

    @Override
    public double getBeneficioMensual() {
        return wrappee.getBeneficioMensual();
    }

    @Override
    public String getDescripcion() {
        return wrappee.getDescripcion();
    }
}