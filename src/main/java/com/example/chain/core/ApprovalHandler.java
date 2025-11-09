package com.example.chain.core;

public interface ApprovalHandler {
    void setNext(ApprovalHandler next);

    void handle(ApprovalContext ctx);
}