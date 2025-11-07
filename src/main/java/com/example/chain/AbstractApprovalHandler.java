package com.example.chain;

public abstract class AbstractApprovalHandler implements ApprovalHandler {
    private ApprovalHandler next;

    @Override
    public void setNext(ApprovalHandler next) {
        this.next = next;
    }

    @Override
    public void handle(ApprovalContext ctx) {
        if (ctx.isAprobado())
            return; // ya aprobado final
        if (!precondicion(ctx))
            return; // rechaza
        procesar(ctx);
        if (next != null && !ctx.isAprobado() && ctx.getMotivoRechazo() == null) {
            next.handle(ctx);
        }
    }

    protected abstract boolean precondicion(ApprovalContext ctx); // valida, puede rechazar

    protected void procesar(ApprovalContext ctx) {
        /* opcional */ }
}