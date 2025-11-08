package com.example.decorator;

import com.example.model.Cuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecoratorUnitTest {

    @Test
    @DisplayName("SeguroVidaDecorator añade costo fijo")
    void seguroVidaAñadeCosto() {
        Cuenta cuenta = new Cuenta();
        cuenta.setCuotaManejo(10000);
        BaseProductoComponent base = new BaseProductoComponent(cuenta);
        ProductoFinancieroComponent decorado = new SeguroVidaDecorator(base);
        assertEquals(10000 + 15000.0, decorado.getCostoMensual(), 0.01);
    }
}
