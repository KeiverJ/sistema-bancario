package com.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScoreE2ETest {
    @Test
    void flujoCompleto() {
        Score score = new Score();
        score.setValor(800);
        assertEquals(800, score.getValor());
    }
}
