package com.example.chain.core;

public abstract class AbstractApprovalHandler implements ApprovalHandler {
    private ApprovalHandler next;

    @Override
    public void setNext(ApprovalHandler next) {
        this.next = next;
    }

    @Override
    public void handle(ApprovalContext ctx) {
        if (ctx.isAprobado())
            return;
        if (!precondicion(ctx))
            return;
        procesar(ctx);
        if (next != null && !ctx.isAprobado() && ctx.getMotivoRechazo() == null) {
            next.handle(ctx);
        }
    }

    protected abstract boolean precondicion(ApprovalContext ctx);

    protected void procesar(ApprovalContext ctx) {
    }
}