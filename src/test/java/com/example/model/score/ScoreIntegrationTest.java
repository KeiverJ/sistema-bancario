package com.example.model.score;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreIntegrationTest {
    // Prueba integración básica: creación de un objeto Score
    @Test
    void integracionBasica() {
        Score score = new Score();
        assertNotNull(score);
    }
}
