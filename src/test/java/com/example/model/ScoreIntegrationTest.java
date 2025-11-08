package com.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScoreIntegrationTest {
    @Test
    void integracionBasica() {
        Score score = new Score();
        assertNotNull(score);
    }
}
