package com.example.decorator;

public class ExencionCuotaDecorator extends ProductoDecorator {

    public ExencionCuotaDecorator(ProductoFinancieroComponent wrappee) {
        super(wrappee);
    }

    @Override
    public double getCostoMensual() {
        // Remueve cuota manejo (asumimos wrappee incluye cuota)
        return Math.max(0, super.getCostoMensual() - super.getCostoMensual());
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + ExenciónCuotaManejo";
    }
}