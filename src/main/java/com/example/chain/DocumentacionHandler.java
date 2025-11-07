package com.example.chain;

public class DocumentacionHandler extends AbstractApprovalHandler {
    @Override
    protected boolean precondicion(ApprovalContext ctx) {
        // Simulación mínima: documento cliente no nulo
        if (ctx.getCliente().getNumeroDocumento() == null) {
            ctx.rechazar("Documentación incompleta");
            return false;
        }
        return true;
    }
}