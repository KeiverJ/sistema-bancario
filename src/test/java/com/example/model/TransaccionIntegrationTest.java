package com.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransaccionIntegrationTest {
    @Test
    void integracionBasica() {
        Transaccion t = new Transaccion();
        assertNotNull(t);
    }
}
