package com.example.decorator;

import com.example.model.Cuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorIntegrationTest {

    @Test
    @DisplayName("Cadena de decorators acumula costos y beneficios")
    void cadenaDecoratorsAcumula() {
        Cuenta cuenta = new Cuenta();
        cuenta.setCuotaManejo(8000);
        ProductoFinancieroComponent comp = new BaseProductoComponent(cuenta);
        comp = new SeguroVidaDecorator(comp);
        comp = new CashbackDecorator(comp);
        comp = new BeneficioVipDecorator(comp);
        double costo = comp.getCostoMensual();
        double beneficio = comp.getBeneficioMensual();
        assertTrue(costo > 8000);
        assertTrue(beneficio > 0);
    }
}
