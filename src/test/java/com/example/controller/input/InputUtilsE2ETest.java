package com.example.controller.input;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.controller.InputUtils;

import java.io.ByteArrayInputStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class InputUtilsE2ETest {
    @Test
    @DisplayName("E2E: Flujo completo de solicitarMonto")
    void testE2EFlujoSolicitarMonto() {
        String input = "-1\nabc\n0\n100\n";
        Scanner sc = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Double res = InputUtils.solicitarMonto(sc, "Monto");
        // El flujo debe terminar en null (por cancelar con 0)
        assertNull(res);
    }
}