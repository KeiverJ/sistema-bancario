package com.example.state;

import com.example.model.Credito;

public class EnMoraState implements CreditoState {
    @Override
    public String nombre() {
        return "EN_MORA";
    }

    @Override
    public boolean pagar(Credito c, double monto) {
        if (monto <= 0)
            return false;
        double saldo = c.getSaldo() - monto;
        c.setSaldo(Math.max(0, saldo));
        if (c.getSaldo() <= 0) {
            c.setState(new CanceladoState());
        } else {
            c.setState(new DesembolsadoState()); // normaliza el estado
        }
        return true;
    }
}