package com.example.factory;

import com.example.model.Transaccion;

public class PagoServicioFactory extends TransaccionFactory {
    private final String cuentaOrigenId;
    private final String codigoServicio;
    private final double monto;
    private final String referencia;

    public PagoServicioFactory(String cuentaOrigenId, String codigoServicio, double monto, String referencia) {
        this.cuentaOrigenId = cuentaOrigenId;
        this.codigoServicio = codigoServicio;
        this.monto = monto;
        this.referencia = referencia;
    }

    @Override
    protected Transaccion crear() {
        Transaccion t = new Transaccion();
        t.setTipo(Transaccion.TipoTransaccion.PAGO_SERVICIO);
        t.setCuentaOrigenId(cuentaOrigenId);
        t.setMonto(monto);
        t.setDescripcion("Pago " + codigoServicio + " Ref: " + referencia);
        return t;
    }
}