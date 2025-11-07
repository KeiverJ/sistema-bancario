package com.example.chain;

public interface ApprovalHandler {
    void setNext(ApprovalHandler next);

    void handle(ApprovalContext ctx);
}