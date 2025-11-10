package com.example.controller.input;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.controller.InputUtils;

import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

class InputUtilsIntegrationTest {
    // Aquí se pueden agregar pruebas que combinen InputUtils con otros módulos si fuera necesario
    // Por ejemplo, integración con lógica de validación externa o servicios
    @Test
    @DisplayName("Integración: solicitarTexto con validación externa")
    void testSolicitarTextoConValidacionExterna() {
        Scanner sc = new Scanner("valor\n");
        // Simula integración: el texto debe ser validado por otro módulo (simulado aquí)
        String res = InputUtils.solicitarTexto(sc, "Mensaje", true);
        assertTrue(res.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"));
    }
}