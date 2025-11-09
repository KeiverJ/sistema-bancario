package com.example.model.score;

import java.time.LocalDateTime;

/**
 * Puntaje crediticio del cliente.
 */
public class Score {
    private String clienteId; 
    private int valor;
    private LocalDateTime fechaCalculo;
    private String fuente;

    public Score() {
        this.fechaCalculo = LocalDateTime.now();
    }

    public String getClienteId() { 
        return clienteId;
    }

    public void setClienteId(String clienteId) { 
        this.clienteId = clienteId;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public LocalDateTime getFechaCalculo() {
        return fechaCalculo;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public boolean esAceptable(int minimo) {
        return valor >= minimo;
    }
}