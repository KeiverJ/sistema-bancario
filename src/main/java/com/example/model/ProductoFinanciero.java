package com.example.model;

import java.time.LocalDateTime;

/**
 * Clase base para productos financieros.
 * Se evita exponer getEstado()/setEstado() genéricos para no colisionar con
 * hijas
 * que usan enums u otros tipos. Usar estadoCodigo si se requiere un estado
 * textual común.
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

    // Estado genérico opcional (texto), para auditoría/reportes comunes
    public String getEstadoCodigo() {
        return estadoCodigo;
    }

    public void setEstadoCodigo(String estadoCodigo) {
        this.estadoCodigo = estadoCodigo;
    }
}