package com.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScoreUnitTest {
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
