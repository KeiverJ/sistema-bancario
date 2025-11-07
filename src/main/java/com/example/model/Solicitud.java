package com.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Solicitud de producto (para Chain of Responsibility / State).
 */
public class Solicitud {

    public enum TipoSolicitud {
        CREDITO, TARJETA, CUENTA
    }

    public enum EstadoSolicitud {
        RECIBIDA, VALIDANDO, APROBADA, RECHAZADA
    }

    private String id;
    private String clienteId;
    private TipoSolicitud tipo;
    private EstadoSolicitud estado;
    private final List<String> comentarios = new ArrayList<>();
    private LocalDateTime fechaCreacion;

    public Solicitud() {
        this.estado = EstadoSolicitud.RECIBIDA;
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

    public TipoSolicitud getTipo() {
        return tipo;
    }

    public void setTipo(TipoSolicitud tipo) {
        this.tipo = tipo;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public List<String> getComentarios() {
        return List.copyOf(comentarios);
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void agregarComentario(String c) {
        if (c != null && !c.isBlank())
            comentarios.add(c);
    }

    public void marcarValidando() {
        if (estado == EstadoSolicitud.RECIBIDA)
            estado = EstadoSolicitud.VALIDANDO;
    }

    public void aprobar() {
        if (estado == EstadoSolicitud.VALIDANDO)
            estado = EstadoSolicitud.APROBADA;
    }

    public void rechazar(String motivo) {
        if (estado == EstadoSolicitud.VALIDANDO) {
            estado = EstadoSolicitud.RECHAZADA;
            agregarComentario("Rechazo: " + motivo);
        }
    }
}