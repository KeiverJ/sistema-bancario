package com.example.chain.handlers;

import com.example.chain.core.AbstractApprovalHandler;
import com.example.chain.core.ApprovalContext;

public class DocumentacionHandler extends AbstractApprovalHandler {
    @Override
    protected boolean precondicion(ApprovalContext ctx) {
        if (ctx.getCliente().getNumeroDocumento() == null) {
            ctx.rechazar("Documentación incompleta");
            return false;
        }
        return true;
    }
}