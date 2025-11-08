package com.example.chain;

public class AprobacionFinalHandler extends AbstractApprovalHandler {
    @Override
    protected boolean precondicion(ApprovalContext ctx) {
        ctx.aprobar();
        return true;
    }
}