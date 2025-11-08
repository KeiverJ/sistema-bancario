package com.example.observer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObserverUnitTest {

    @Test
    @DisplayName("LoggingObserver soporta cualquier evento")
    void loggingObserverSoportaTodos() {
        LoggingObserver obs = new LoggingObserver();
        // LoggingObserver soporta cualquier tipo
        assertTrue(obs.soporta("CUALQUIER_EVENTO"));
    }
}
