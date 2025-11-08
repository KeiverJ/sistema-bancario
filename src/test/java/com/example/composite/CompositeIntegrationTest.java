package com.example.composite;

import com.example.decorator.BaseProductoComponent;
import com.example.model.Cuenta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompositeIntegrationTest {

    @Test
    @DisplayName("Paquete permite agregar y remover componentes")
    void agregarYRemover() {
        PaqueteProductos paquete = new PaqueteProductos("Pack");
        Cuenta c = new Cuenta(); c.setCuotaManejo(4000);
        var comp = new BaseProductoComponent(c);
        paquete.add(comp);
    assertEquals(1, paquete.getItems().size());
        paquete.remove(comp);
    assertTrue(paquete.getItems().isEmpty());
    }
}
