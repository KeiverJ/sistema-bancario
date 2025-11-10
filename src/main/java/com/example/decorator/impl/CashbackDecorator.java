package com.example.decorator.impl;

import com.example.decorator.core.ProductoDecorator;
import com.example.decorator.core.ProductoFinancieroComponent;

public class CashbackDecorator extends ProductoDecorator {
    
    private static final double COSTO_ADICIONAL = 5000.0;
    private static final double BENEFICIO_CASHBACK = 20000.0; 
    public CashbackDecorator(ProductoFinancieroComponent componente) {
        super(componente);
    }
    
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + "\n  + Cashback 2% en compras (hasta $50K/mes)";
    }
    
    @Override
    public double getCostoMensual() {
        return super.getCostoMensual() + COSTO_ADICIONAL;
    }
    
    @Override
    public double getBeneficioMensual() {
        return super.getBeneficioMensual() + BENEFICIO_CASHBACK;
    }
}