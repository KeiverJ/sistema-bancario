package com.example.decorator;

import com.example.decorator.impl.BaseProductoComponent;
import com.example.model.cuenta.Cuenta;
import com.example.decorator.core.ProductoFinancieroComponent;
import com.example.decorator.impl.SeguroVidaDecorator;
import com.example.decorator.impl.CashbackDecorator;
import com.example.decorator.impl.BeneficioVipDecorator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorIntegrationTest {

    // Verifica que la cadena de decorators acumula correctamente costos y beneficios
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
