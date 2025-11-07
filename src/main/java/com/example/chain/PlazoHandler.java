package com.example.chain;

public class PlazoHandler extends AbstractApprovalHandler {
    @Override
    protected boolean precondicion(ApprovalContext ctx) {
        int plazo = ctx.getCredito().getPlazoMeses();
        if (plazo <= 0 || plazo > ctx.getConfig().getPlazoMaximoCredito()) {
            ctx.rechazar("Plazo inválido");
            return false;
        }
        return true;
    }
}