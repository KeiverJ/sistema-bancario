package com.example.service.Transaccion;

import com.example.model.Transaccion;
import com.example.service.CuentaService;
import com.example.service.TransaccionService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para TransaccionService (con mocks).
 */
class TransaccionServiceUnitTest {
    @Test
    @DisplayName("registrarTransaccion lanza excepción si monto <= 0")
    void registrarTransaccion_montoInvalido() {
        var repo = mock(com.example.repository.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(config.getLimiteDiario()).thenReturn(1000.0);
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        Transaccion t = new Transaccion();
        t.setDescripcion("negativo");
        t.setMonto(0.0);
        assertThrows(IllegalArgumentException.class, () -> service.registrarTransaccion(t));
    }

    @Test
    @DisplayName("registrarTransaccion lanza excepción si monto excede límite diario")
    void registrarTransaccion_montoExcedeLimite() {
        var repo = mock(com.example.repository.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(config.getLimiteDiario()).thenReturn(100.0);
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        Transaccion t = new Transaccion();
        t.setDescripcion("excede");
        t.setMonto(200.0);
        assertThrows(IllegalArgumentException.class, () -> service.registrarTransaccion(t));
    }

    @Test
    @DisplayName("procesarTransaccion lanza excepción si no existe")
    void procesarTransaccion_noExiste() {
        var repo = mock(com.example.repository.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.findById(anyString())).thenReturn(java.util.Optional.empty());
        TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
        assertThrows(IllegalArgumentException.class, () -> service.procesarTransaccion("nope"));
    }

    @Test
    @DisplayName("registrarTransaccion llama a repo y retorna transacción")
    void registrarTransaccion_unit() {
        var repo = mock(com.example.repository.TransaccionRepository.class);
        var config = mock(com.example.config.BankConfig.class);
        var publisher = mock(com.example.observer.DomainEventPublisher.class);
        var cuentaService = mock(CuentaService.class);
        when(repo.save(any(Transaccion.class))).thenAnswer(i -> i.getArgument(0));
    when(config.getLimiteDiario()).thenReturn(1000.0); // Limite mayor al monto
    TransaccionService service = new TransaccionService(repo, config, publisher, cuentaService);
    Transaccion t = new Transaccion();
    t.setDescripcion("unit");
    t.setMonto(100.0); // monto válido
    Transaccion guardada = service.registrarTransaccion(t);
    assertEquals("unit", guardada.getDescripcion());
    }
}
