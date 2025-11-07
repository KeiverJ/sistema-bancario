package com.example.chain;

import com.example.model.Cliente;
import com.example.model.Credito;
import com.example.model.Score;
import com.example.config.BankConfig;

public class ApprovalContext {
    private final Cliente cliente;
    private final Credito credito;
    private final Score score;
    private final BankConfig config;
    private boolean aprobado;
    private String motivoRechazo;

    public ApprovalContext(Cliente cliente, Credito credito, Score score, BankConfig config) {
        this.cliente = cliente;
        this.credito = credito;
        this.score = score;
        this.config = config;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Credito getCredito() {
        return credito;
    }

    public Score getScore() {
        return score;
    }

    public BankConfig getConfig() {
        return config;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void aprobar() {
        this.aprobado = true;
    }

    public void rechazar(String motivo) {
        this.aprobado = false;
        this.motivoRechazo = motivo;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }
}