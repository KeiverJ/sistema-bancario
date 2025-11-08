package com.example.factory;

import com.example.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FactoryUnitTest {

    @Test
    @DisplayName("FabricaProductosProvider retorna fábrica según tipo de cliente")
    void providerRetornaFabrica() {
        FabricaProductosProvider provider = new FabricaProductosProvider();
        ProductoBancarioFactory factory = provider.getFactory(Cliente.TipoCliente.PERSONA_NATURAL);
        assertNotNull(factory);
    }
}
