package com.example.chain;

public class ScoreHandler extends AbstractApprovalHandler {
    @Override
    protected boolean precondicion(ApprovalContext ctx) {
        if (!ctx.getScore().esAceptable(ctx.getConfig().getScoreMinimo())) {
            ctx.rechazar("Score insuficiente: " + ctx.getScore().getValor());
            return false;
        }
        return true;
    }
}