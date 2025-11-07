package com.example.factory;

import com.example.model.Transaccion;

public class TransferenciaFactory extends TransaccionFactory {
    private final String cuentaOrigenId;
    private final String cuentaDestinoId;
    private final double monto;
    private final String descripcion;

    public TransferenciaFactory(String cuentaOrigenId, String cuentaDestinoId, double monto, String descripcion) {
        this.cuentaOrigenId = cuentaOrigenId;
        this.cuentaDestinoId = cuentaDestinoId;
        this.monto = monto;
        this.descripcion = descripcion;
    }

    @Override
    protected Transaccion crear() {
        Transaccion t = new Transaccion();
        t.setTipo(Transaccion.TipoTransaccion.TRANSFERENCIA);
        t.setCuentaOrigenId(cuentaOrigenId);
        t.setCuentaDestinoId(cuentaDestinoId);
        t.setMonto(monto);
        t.setDescripcion(descripcion);
        return t;
    }
}