package com.example.chain.handlers;

import com.example.chain.core.AbstractApprovalHandler;
import com.example.chain.core.ApprovalContext;

public class MontoHandler extends AbstractApprovalHandler {
    @Override
    protected boolean precondicion(ApprovalContext ctx) {
        double monto = ctx.getCredito().getMonto();
        if (!ctx.getConfig().isMontoValido(monto)) {
            ctx.rechazar("Monto excede límite o inválido");
            return false;
        }
        return true;
    }
}