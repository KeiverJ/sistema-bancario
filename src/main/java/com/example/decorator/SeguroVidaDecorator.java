package com.example.decorator;

public class SeguroVidaDecorator extends ProductoDecorator {
    
    private static final double COSTO_SEGURO = 15000.0;
    
    public SeguroVidaDecorator(ProductoFinancieroComponent componente) {
        super(componente);
    }
    
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + "\n  + Seguro de vida (cobertura $50M)";
    }
    
    @Override
    public double getCostoMensual() {
        return super.getCostoMensual() + COSTO_SEGURO;
    }
}