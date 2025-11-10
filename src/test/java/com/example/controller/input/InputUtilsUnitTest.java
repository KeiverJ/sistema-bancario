package com.example.controller.input;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.controller.InputUtils;

import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InputUtilsUnitTest {
    @Test
    @DisplayName("solicitarTexto obligatorio: éxito")
    void testSolicitarTextoObligatorioExito() {
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("valor");
        String res = InputUtils.solicitarTexto(sc, "Mensaje", true);
        assertEquals("valor", res);
    }

    @Test
    @DisplayName("solicitarTexto obligatorio: cancelar")
    void testSolicitarTextoObligatorioCancelar() {
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("0");
        String res = InputUtils.solicitarTexto(sc, "Mensaje", true);
        assertNull(res);
    }

    @Test
    @DisplayName("solicitarTexto obligatorio: vacío, luego válido")
    void testSolicitarTextoObligatorioVacioLuegoValido() {
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("", "abc");
        String res = InputUtils.solicitarTexto(sc, "Mensaje", true);
        assertEquals("abc", res);
    }

    @Test
    @DisplayName("solicitarTexto opcional: vacío")
    void testSolicitarTextoOpcionalVacio() {
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("");
        String res = InputUtils.solicitarTexto(sc, "Mensaje", false);
        assertEquals("", res);
    }

    @Test
    @DisplayName("solicitarMonto: éxito")
    void testSolicitarMontoExito() {
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("100");
        Double res = InputUtils.solicitarMonto(sc, "Monto");
        assertEquals(100.0, res);
    }

    @Test
    @DisplayName("solicitarMonto: cancelar")
    void testSolicitarMontoCancelar() {
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("0");
        Double res = InputUtils.solicitarMonto(sc, "Monto");
        assertNull(res);
    }

    @Test
    @DisplayName("solicitarMonto: valor negativo y no numérico")
    void testSolicitarMontoNegativoYNoNumerico() {
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("-5", "abc", "50");
        Double res = InputUtils.solicitarMonto(sc, "Monto");
        assertEquals(50.0, res);
    }

    @Test
    @DisplayName("solicitarEnteroPositivo: éxito")
    void testSolicitarEnteroPositivoExito() {
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("3");
        Integer res = InputUtils.solicitarEnteroPositivo(sc, "Plazo");
        assertEquals(3, res);
    }

    @Test
    @DisplayName("solicitarEnteroPositivo: cancelar")
    void testSolicitarEnteroPositivoCancelar() {
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("0");
        Integer res = InputUtils.solicitarEnteroPositivo(sc, "Plazo");
        assertNull(res);
    }

    @Test
    @DisplayName("solicitarEnteroPositivo: negativo y no numérico")
    void testSolicitarEnteroPositivoNegativoYNoNumerico() {
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("-1", "abc", "7");
        Integer res = InputUtils.solicitarEnteroPositivo(sc, "Plazo");
        assertEquals(7, res);
    }

    enum DummyEnum { UNO, DOS }

    @Test
    @DisplayName("solicitarEnum: éxito")
    void testSolicitarEnum() {
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("2");
        DummyEnum res = InputUtils.solicitarEnum(sc, "Elija", DummyEnum.class);
        assertEquals(DummyEnum.DOS, res);
    }

    @Test
    @DisplayName("solicitarEnum: cancelar")
    void testSolicitarEnumCancelar() {
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("0");
        DummyEnum res = InputUtils.solicitarEnum(sc, "Elija", DummyEnum.class);
        assertNull(res);
    }
}