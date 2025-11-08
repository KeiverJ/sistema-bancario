package com.example.model;

import java.time.LocalDateTime;


/**
 * Transacción bancaria.
 */
public class Transaccion {

    public enum TipoTransaccion {
        TRANSFERENCIA, PAGO_SERVICIO, RETIRO, DEPOSITO
    }

    public enum EstadoTransaccion {
        PENDIENTE, EXITOSA, FALLIDA
    }

    private TipoTransaccion tipo;
    private String cuentaOrigenId;
    private String cuentaDestinoId;
    private double monto;
    private String descripcion;
    private LocalDateTime fecha;
    private EstadoTransaccion estado;

    private String id;
    private String codigo;

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransaccion tipo) {
        this.tipo = tipo;
    }

    public String getCuentaOrigenId() {
        return cuentaOrigenId;
    }

    public void setCuentaOrigenId(String cuentaOrigenId) {
        this.cuentaOrigenId = cuentaOrigenId;
    }

    public String getCuentaDestinoId() {
        return cuentaDestinoId;
    }

    public void setCuentaDestinoId(String cuentaDestinoId) {
        this.cuentaDestinoId = cuentaDestinoId;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoTransaccion getEstado() {
        return estado;
    }

    public void setEstado(EstadoTransaccion estado) {
        this.estado = estado;
    }

    public boolean validar() {
        return monto > 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void ejecutar() {
        if (!validar()) {
            estado = EstadoTransaccion.FALLIDA;
            return;
        }
        estado = EstadoTransaccion.EXITOSA;
        fecha = LocalDateTime.now();
    }
}