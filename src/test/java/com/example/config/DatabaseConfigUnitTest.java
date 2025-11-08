package com.example.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConfigUnitTest {
    @Test
    @DisplayName("getters retornan valores por defecto")
    void gettersPorDefecto() {
        DatabaseConfig config = new DatabaseConfig();
        assertNotNull(config.getUrl());
        assertNotNull(config.getUsername());
        assertNotNull(config.getPassword());
    }
}
