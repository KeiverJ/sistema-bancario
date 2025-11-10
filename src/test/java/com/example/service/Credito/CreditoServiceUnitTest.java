package com.example.service.credito;

import com.example.model.cliente.Cliente;
import com.example.model.credito.Credito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CreditoService (con mocks).
 */
class CreditoServiceUnitTest {
    @Test
    @DisplayName("listarCreditosClientePorCodigo: cliente no existe lanza excepción")
    void listarCreditosClientePorCodigo_clienteNoExiste() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        when(clienteRepo.findAll()).thenReturn(java.util.Collections.emptyList());
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        assertThrows(IllegalArgumentException.class, () -> service.listarCreditosClientePorCodigo("NOPE"));
    }

    @Test
    @DisplayName("listarCreditosClientePorCodigo: retorna créditos del cliente")
    void listarCreditosClientePorCodigo_ok() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        Cliente cli = new Cliente();
        cli.setCodigo("CLI-1");
        cli.setId("id-1");
        when(clienteRepo.findAll()).thenReturn(java.util.List.of(cli));
        Credito cr = new Credito();
        cr.setClienteId("id-1");
        when(repo.findAll()).thenReturn(java.util.List.of(cr));
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        java.util.List<Credito> lista = service.listarCreditosClientePorCodigo("CLI-1");
        assertEquals(1, lista.size());
        assertSame(cr, lista.get(0));
    }

    @Test
    @DisplayName("pagarCuota: crédito no existe lanza excepción")
    void pagarCuota_creditoNoExiste() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        when(repo.findById(anyString())).thenReturn(java.util.Optional.empty());
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        assertThrows(IllegalArgumentException.class, () -> service.pagarCuota("NOPE", 100));
    }

    @Test
    @DisplayName("pagarCuota: flujo feliz, publica evento")
    void pagarCuota_ok() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        Credito credito = mock(Credito.class);
        when(repo.findById("C-1")).thenReturn(java.util.Optional.of(credito));
        when(credito.pagarCuota(100)).thenReturn(true);
        when(repo.save(credito)).thenReturn(credito);
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        boolean ok = service.pagarCuota("C-1", 100);
        assertTrue(ok);
        verify(publisher).publish(any());
    }

    @Test
    @DisplayName("pagarCuota: pago fallido no publica evento")
    void pagarCuota_falla() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        Credito credito = mock(Credito.class);
        when(repo.findById("C-1")).thenReturn(java.util.Optional.of(credito));
        when(credito.pagarCuota(100)).thenReturn(false);
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        boolean ok = service.pagarCuota("C-1", 100);
        assertFalse(ok);
        verify(publisher, never()).publish(any());
    }

    @Test
    @DisplayName("calcularInteresMensual: crédito no existe lanza excepción")
    void calcularInteresMensual_creditoNoExiste() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        when(repo.findById(anyString())).thenReturn(java.util.Optional.empty());
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        assertThrows(IllegalArgumentException.class, () -> service.calcularInteresMensual("NOPE"));
    }

    @Test
    @DisplayName("calcularInteresMensual: flujo feliz")
    void calcularInteresMensual_ok() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        Credito credito = new Credito();
        credito.setSaldo(1200);
        credito.setTasaInteres(12);
        when(repo.findById("C-1")).thenReturn(java.util.Optional.of(credito));
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        double interesMensual = service.calcularInteresMensual("C-1");
        assertEquals(12.0, interesMensual, 0.0001);
    }

    @Test
    @DisplayName("guardar: guarda y publica evento")
    void guardar_ok() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        Credito credito = new Credito();
        when(repo.save(credito)).thenReturn(credito);
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        Credito result = service.guardar(credito);
        assertSame(credito, result);
        verify(publisher).publish(any());
    }

    @Test
    @DisplayName("getTotalEndeudamiento: delega en repo")
    void getTotalEndeudamiento_ok() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        when(repo.getTotalSaldoByClienteId("cli1")).thenReturn(123.45);
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        double total = service.getTotalEndeudamiento("cli1");
        assertEquals(123.45, total);
    }

    @Test
    @DisplayName("listarTodosCreditos: delega en repo")
    void listarTodosCreditos_ok() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        Credito cr = new Credito();
        when(repo.findAll()).thenReturn(java.util.List.of(cr));
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        java.util.List<Credito> lista = service.listarTodosCreditos();
        assertEquals(1, lista.size());
        assertSame(cr, lista.get(0));
    }

    private CreditoService buildService(
            com.example.repository.credito.CreditoRepository repo,
            com.example.repository.cliente.ClienteRepository clienteRepo,
            com.example.observer.core.DomainEventPublisher publisher,
            com.example.template.core.SolicitudCreditoTemplate template) {
        return new CreditoService(repo, clienteRepo, publisher, template);
    }

    @Test
    @DisplayName("solicitarCredito lanza excepción si cliente no existe")
    void solicitarCredito_clienteNoExiste() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        when(clienteRepo.findById(anyString())).thenReturn(java.util.Optional.empty());
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        assertThrows(IllegalArgumentException.class, () ->
            service.solicitarCredito("nope", Credito.TipoCredito.CONSUMO, 1000, 12)
        );
    }

    @Test
    @DisplayName("solicitarCredito llama a template y retorna crédito")
    void solicitarCredito_ok() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        Cliente cliente = new Cliente();
        Credito credito = new Credito();
        when(clienteRepo.findById("cli1")).thenReturn(java.util.Optional.of(cliente));
        when(template.solicitar(any(), any(), anyDouble(), anyInt())).thenReturn(credito);
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        Credito result = service.solicitarCredito("cli1", Credito.TipoCredito.CONSUMO, 1000, 12);
        assertSame(credito, result);
    }

    @Test
    @DisplayName("obtenerCredito y obtenerCreditoPorCodigo funcionan")
    void obtenerCredito_funciona() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        Credito c = new Credito();
        c.setCodigo("C-1");
        when(repo.findById("id1")).thenReturn(java.util.Optional.of(c));
        when(repo.findAll()).thenReturn(java.util.List.of(c));
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        assertTrue(service.obtenerCredito("id1").isPresent());
        assertTrue(service.obtenerCreditoPorCodigo("C-1").isPresent());
    }

    @Test
    @DisplayName("listarCreditosCliente funciona")
    void listarCreditosCliente_funciona() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        when(repo.findByClienteId("cli1")).thenReturn(java.util.List.of(new Credito()));
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        assertEquals(1, service.listarCreditosCliente("cli1").size());
    }

    @Test
    @DisplayName("consultarSaldo retorna saldo del repo")
    void consultarSaldo_unit() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        Credito credito = new Credito();
        credito.setSaldo(1234);
        when(repo.findById("id1")).thenReturn(java.util.Optional.of(credito));
        double saldo = service.consultarSaldo("id1");
        assertEquals(1234, saldo);
    }

    @Test
    @DisplayName("consultarSaldo retorna 0.0 si no existe")
    void consultarSaldo_retornaCeroSiNoExiste() {
        var repo = mock(com.example.repository.credito.CreditoRepository.class);
        var clienteRepo = mock(com.example.repository.cliente.ClienteRepository.class);
        var publisher = mock(com.example.observer.core.DomainEventPublisher.class);
        var template = mock(com.example.template.core.SolicitudCreditoTemplate.class);
        when(repo.findById("nope")).thenReturn(java.util.Optional.empty());
    CreditoService service = buildService(repo, clienteRepo, publisher, template);
        double saldo = service.consultarSaldo("nope");
        assertEquals(0.0, saldo);
    }
}
