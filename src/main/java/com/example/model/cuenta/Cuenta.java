package com.example.model.cuenta;

import com.example.model.producto.ProductoFinanciero;

public class Cuenta extends ProductoFinanciero {

    // Eliminar el campo id duplicado; usar el id heredado de ProductoFinanciero
    private String codigo;
    private String numeroCuenta;
    private TipoCuenta tipoCuenta;
    private double saldo;
    private double cuotaManejo;
    private EstadoCuenta estado;

    public enum TipoCuenta {
        AHORROS, CORRIENTE
    }

    public enum EstadoCuenta {
        ACTIVA, INACTIVA, BLOQUEADA
    }

    // Métodos getId/setId heredados directamente de ProductoFinanciero

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getCuotaManejo() {
        return cuotaManejo;
    }

    public void setCuotaManejo(double cuotaManejo) {
        this.cuotaManejo = cuotaManejo;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }

    public boolean depositar(double monto) {
        if (monto <= 0 || estado != EstadoCuenta.ACTIVA)
            return false;
        this.saldo += monto;
        return true;
    }

    public boolean retirar(double monto) {
        if (monto <= 0 || estado != EstadoCuenta.ACTIVA || saldo < monto)
            return false;
        this.saldo -= monto;
        return true;
    }
}