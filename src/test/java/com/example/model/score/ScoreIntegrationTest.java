package com.example.model.score;

import org.junit.jupiter.api.Test;

import com.example.model.Score;

import static org.junit.jupiter.api.Assertions.*;

class ScoreIntegrationTest {
    @Test
    void integracionBasica() {
        Score score = new Score();
        assertNotNull(score);
    }
}
