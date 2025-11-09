package com.example.chain.handlers;

import com.example.chain.core.AbstractApprovalHandler;
import com.example.chain.core.ApprovalContext;

public class AprobacionFinalHandler extends AbstractApprovalHandler {
    @Override
    protected boolean precondicion(ApprovalContext ctx) {
        ctx.aprobar();
        return true;
    }
}