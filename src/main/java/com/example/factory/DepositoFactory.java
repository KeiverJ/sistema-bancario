package com.example.factory;

import com.example.model.Transaccion;

public class DepositoFactory extends TransaccionFactory {
    private final String cuentaDestinoId;
    private final double monto;
    private final String descripcion;

    public DepositoFactory(String cuentaDestinoId, double monto, String descripcion) {
        this.cuentaDestinoId = cuentaDestinoId;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    @Override
    protected Transaccion crear() {
        Transaccion t = new Transaccion();
        t.setTipo(Transaccion.TipoTransaccion.DEPOSITO);
        t.setCuentaDestinoId(cuentaDestinoId);
        t.setMonto(monto);
        t.setDescripcion(descripcion);
        return t;
    }
}