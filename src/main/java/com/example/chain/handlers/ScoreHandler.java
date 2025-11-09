package com.example.chain.handlers;

import com.example.chain.core.AbstractApprovalHandler;
import com.example.chain.core.ApprovalContext;

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