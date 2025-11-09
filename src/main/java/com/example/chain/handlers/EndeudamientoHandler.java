package com.example.chain.handlers;

import com.example.chain.core.AbstractApprovalHandler;
import com.example.chain.core.ApprovalContext;

public class EndeudamientoHandler extends AbstractApprovalHandler {
    private final double ratioMax;

    public EndeudamientoHandler(double ratioMax) {
        this.ratioMax = ratioMax;
    }

    @Override
    protected boolean precondicion(ApprovalContext ctx) {
        // Simulación endeudamiento (monto / montoMax)
        double ratio = ctx.getCredito().getMonto() / ctx.getConfig().getMontoMaximoCredito();
        if (ratio > ratioMax) {
            ctx.rechazar(String.format("Ratio endeudamiento %.2f > %.2f", ratio, ratioMax));
            return false;
        }
        return true;
    }
}