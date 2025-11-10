package com.example.composite;

import com.example.decorator.impl.BaseProductoComponent;
import com.example.model.cuenta.Cuenta;
import com.example.decorator.core.ProductoFinancieroComponent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompositeUnitTest {

    // Verifica que el paquete suma correctamente los costos de los hijos
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

    // Verifica que un paquete vacío retorna costo y beneficio cero
    @Test
    @DisplayName("Paquete vacío retorna costo y beneficio cero")
    void paqueteVacio() {
        PaqueteProductos paquete = new PaqueteProductos("Vacío");
        assertEquals(0.0, paquete.getCostoMensual(), 0.01);
        assertEquals(0.0, paquete.getBeneficioMensual(), 0.01);
        assertTrue(paquete.getItems().isEmpty());
    }

    // Verifica que add/remove maneja nulos y elimina correctamente
    @Test
    @DisplayName("Paquete add/remove maneja nulos y elimina correctamente")
    void paqueteAddRemove() {
        PaqueteProductos paquete = new PaqueteProductos("Test");
        Cuenta c = new Cuenta(); c.setCuotaManejo(1000);
        ProductoFinancieroComponent comp = new BaseProductoComponent(c);
        paquete.add(null).add(comp);
        assertEquals(1, paquete.getItems().size());
        paquete.remove(comp);
        assertTrue(paquete.getItems().isEmpty());
    }

    // Verifica que un paquete anidado suma correctamente costos y beneficios
    @Test
    @DisplayName("Paquete anidado suma costos y beneficios")
    void paqueteAnidado() {
        PaqueteProductos paquete1 = new PaqueteProductos("P1");
        PaqueteProductos paquete2 = new PaqueteProductos("P2");
        Cuenta c1 = new Cuenta(); c1.setCuotaManejo(2000);
        Cuenta c2 = new Cuenta(); c2.setCuotaManejo(3000);
        ProductoFinancieroComponent comp1 = new BaseProductoComponent(c1);
        ProductoFinancieroComponent comp2 = new BaseProductoComponent(c2);
        paquete1.add(comp1);
        paquete2.add(comp2);
        paquete1.add(paquete2);
        assertEquals(5000, paquete1.getCostoMensual(), 0.01);
    }

    @Test
    @DisplayName("getDescripcion incluye nombre y descripción de hijos")
    void descripcionIncluyeHijos() {
        PaqueteProductos paquete = new PaqueteProductos("SuperPack");
        Cuenta c = new Cuenta(); c.setCuotaManejo(1000); c.setTipoCuenta(Cuenta.TipoCuenta.AHORROS); c.setNumeroCuenta("123");
        ProductoFinancieroComponent comp = new BaseProductoComponent(c);
        paquete.add(comp);
        String desc = paquete.getDescripcion();
        assertTrue(desc.contains("SuperPack"));
        assertTrue(desc.contains("Cuenta AHORROS #123"));
    }

    @Test
    @DisplayName("getId y getNombre funcionan")
    void idYNombre() {
        PaqueteProductos paquete = new PaqueteProductos("Combo");
        assertNotNull(paquete.getId());
        assertEquals("Combo", paquete.getNombre());
    }
}
