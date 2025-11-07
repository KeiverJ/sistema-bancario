package com.example.chain;

public class AprobacionFinalHandler extends AbstractApprovalHandler {
    @Override
    protected boolean precondicion(ApprovalContext ctx) {
        ctx.aprobar();
        return true;
    }

    @Override
    protected void procesar(ApprovalContext ctx) {
        ctx.getCredito().aprobar(); // usar State
    }
}