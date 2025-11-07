package com.example.state;

import com.example.model.Credito;

public class DesembolsadoState implements CreditoState {
    @Override
    public String nombre() {
        return "DESEMBOLSADO";
    }

    @Override
    public boolean pagar(Credito c, double monto) {
        if (monto <= 0)
            return false;
        double saldo = c.getSaldo() - monto;
        c.setSaldo(Math.max(0, saldo));
        if (c.getSaldo() <= 0) {
            c.setState(new CanceladoState());
        }
        return true;
    }

    @Override
    public void marcarMora(Credito c) {
        c.setState(new EnMoraState());
    }

    @Override
    public void cerrar(Credito c) {
        if (c.getSaldo() <= 0)
            c.setState(new CanceladoState());
        else
            throw new IllegalStateException("No se puede cerrar con saldo > 0");
    }
}