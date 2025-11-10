package com.example.model.producto;

import java.time.LocalDateTime;

/**
 * Clase base para productos financieros.
 */
public abstract class ProductoFinanciero {
    protected String id;
    protected String clienteId;
    protected String estadoCodigo;
    protected LocalDateTime fechaCreacion;

    protected ProductoFinanciero() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public String getEstadoCodigo() {
        return estadoCodigo;
    }

    public void setEstadoCodigo(String estadoCodigo) {
        this.estadoCodigo = estadoCodigo;
    }
}