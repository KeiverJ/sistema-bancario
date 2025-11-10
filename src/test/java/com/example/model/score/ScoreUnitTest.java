package com.example.model.score;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreUnitTest {
    // Prueba getters, setters y lógica de Score (valor, fuente y aceptación)
    @Test
    void gettersYSettersYLogica() {
        Score score = new Score();
        score.setValor(700);
        score.setFuente("EXTERNA");
        assertEquals(700, score.getValor());
        assertEquals("EXTERNA", score.getFuente());
        assertTrue(score.esAceptable(650));
        assertFalse(score.esAceptable(750));
    }
}
