package com.example.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConfigUnitTest {
    // Verifica que los getters devuelven valores por defecto no nulos
    @Test
    @DisplayName("getters retornan valores por defecto")
    void gettersPorDefecto() {
        DatabaseConfig config = new DatabaseConfig();
        assertNotNull(config.getUrl());
        assertNotNull(config.getUsername());
        assertNotNull(config.getPassword());
    }

    // Verifica que setters y getters funcionan correctamente y actualizan las propiedades
    @Test
    @DisplayName("Setters y getters funcionan y actualizan properties")
    void settersYGetters() {
        DatabaseConfig config = new DatabaseConfig();
        config.setUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("user");
        config.setPassword("pass");
        config.setMaxConnections(5);
        config.setPoolSize(10);
        config.setTimeout(60);
        assertEquals("jdbc:mysql://localhost:3306/test", config.getUrl());
        assertEquals("user", config.getUsername());
        assertEquals("pass", config.getPassword());
        assertEquals(5, config.getMaxConnections());
        assertEquals(10, config.getPoolSize());
        assertEquals(60, config.getTimeout());
        assertEquals("jdbc:mysql://localhost:3306/test", config.getProperty("database.url"));
    }

    // Verifica que setProperty y getAllProperties funcionan correctamente
    @Test
    @DisplayName("setProperty y getAllProperties funcionan")
    void setPropertyYGetAllProperties() {
        DatabaseConfig config = new DatabaseConfig();
        config.setProperty("database.custom", "valor");
        assertEquals("valor", config.getProperty("database.custom"));
        config.setProperty("database.custom", null);
        assertNull(config.getProperty("database.custom"));
        assertNotNull(config.getAllProperties());
    }

    // Verifica que isProductionEnvironment detecta correctamente el entorno de producción
    @Test
    @DisplayName("isProductionEnvironment detecta correctamente")
    void isProductionEnvironment_funciona() {
        DatabaseConfig config = new DatabaseConfig();
        assertFalse(config.isProductionEnvironment());
        config.setUrl("jdbc:mysql://localhost:3306/prod");
        assertTrue(config.isProductionEnvironment());
    }

    // Verifica que isValidConfiguration cubre casos válidos e inválidos
    @Test
    @DisplayName("isValidConfiguration cubre ramas válidas e inválidas")
    void isValidConfiguration_ramas() {
        DatabaseConfig config = new DatabaseConfig();
        assertTrue(config.isValidConfiguration());
        config.setUrl("");
        assertFalse(config.isValidConfiguration());
        config.setUrl("jdbc:h2:mem:bankdb");
        config.setUsername(null);
        assertFalse(config.isValidConfiguration());
        config.setUsername("sa");
        config.setMaxConnections(0);
        assertFalse(config.isValidConfiguration());
        config.setMaxConnections(2);
        assertTrue(config.isValidConfiguration());
    }

    // Verifica que getConnectionString retorna el formato esperado
    @Test
    @DisplayName("getConnectionString retorna formato esperado")
    void getConnectionString_funciona() {
        DatabaseConfig config = new DatabaseConfig();
        String str = config.getConnectionString();
        assertTrue(str.contains("USER="));
        assertTrue(str.contains("PASSWORD="));
    }

    // Verifica que toString retorna el formato esperado
    @Test
    @DisplayName("toString retorna formato esperado")
    void toString_funciona() {
        DatabaseConfig config = new DatabaseConfig();
        String str = config.toString();
        assertTrue(str.contains("DatabaseConfig"));
        assertTrue(str.contains(config.getUrl()));
    }
}
