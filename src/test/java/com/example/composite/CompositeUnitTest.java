package com.example.composite;

import com.example.decorator.BaseProductoComponent;
import com.example.decorator.ProductoFinancieroComponent;
import com.example.model.Cuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompositeUnitTest {

    @Test
    @DisplayName("Paquete suma costos de hijos")
    void paqueteSumaCostos() {
        PaqueteProductos paquete = new PaqueteProductos("Paquete Test");
        Cuenta c1 = new Cuenta(); c1.setCuotaManejo(5000);
        Cuenta c2 = new Cuenta(); c2.setCuotaManejo(7000);
        ProductoFinancieroComponent comp1 = new BaseProductoComponent(c1);
        ProductoFinancieroComponent comp2 = new BaseProductoComponent(c2);
        paquete.add(comp1).add(comp2);
        assertEquals(12000, paquete.getCostoMensual(), 0.01);
    }
}
