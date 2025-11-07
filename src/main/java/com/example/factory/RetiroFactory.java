package com.example.factory;

import com.example.model.Transaccion;

public class RetiroFactory extends TransaccionFactory {
    private final String cuentaOrigenId;
    private final double monto;
    private final String ubicacion;

    public RetiroFactory(String cuentaOrigenId, double monto, String ubicacion) {
        this.cuentaOrigenId = cuentaOrigenId;
        this.monto = monto;
        this.ubicacion = ubicacion;
    }

    @Override
    protected Transaccion crear() {
        Transaccion t = new Transaccion();
        t.setTipo(Transaccion.TipoTransaccion.RETIRO);
        t.setCuentaOrigenId(cuentaOrigenId);
        t.setMonto(monto);
        t.setDescripcion("Retiro en " + ubicacion);
        return t;
    }
}