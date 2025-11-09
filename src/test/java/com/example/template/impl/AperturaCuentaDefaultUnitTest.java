package com.example.template.impl;

import com.example.config.BankConfig;
import com.example.factory.FabricaProductosProvider;
import com.example.factory.ProductoBancarioFactory;
import com.example.model.Cliente;
import com.example.model.Cuenta;
import com.example.repository.ClienteRepository;
import com.example.repository.CuentaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AperturaCuentaDefaultUnitTest {
    @Test
    @DisplayName("AperturaCuentaDefault: flujo exitoso de apertura")
    void aperturaExitosa() {
        BankConfig config = mock(BankConfig.class);
        when(config.getSaldoMinimo(anyString())).thenReturn(500.0);
        when(config.getCuotaManejo(anyString())).thenReturn(100.0);
        FabricaProductosProvider fabrica = mock(FabricaProductosProvider.class);
        ProductoBancarioFactory factory = mock(ProductoBancarioFactory.class);
        when(fabrica.getFactory(any())).thenReturn(factory);
        Cuenta cuenta = new Cuenta();
        cuenta.setId("C1");
        when(factory.crearCuenta(any(), any(), anyDouble())).thenReturn(cuenta);
        CuentaRepository cuentaRepo = mock(CuentaRepository.class);
        when(cuentaRepo.save(any())).thenReturn(cuenta);
        ClienteRepository clienteRepo = mock(ClienteRepository.class);
        when(clienteRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Cliente cli = new Cliente();
        cli.setId("cli1");
        cli.setTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL);
        AperturaCuentaDefault tpl = new AperturaCuentaDefault(config, fabrica, cuentaRepo, clienteRepo);
        Cuenta result = tpl.abrirCuenta(cli, Cuenta.TipoCuenta.AHORROS, 1000);
        assertNotNull(result);
        assertEquals("C1", result.getId());
        assertEquals(100.0, result.getCuotaManejo());
        assertEquals(Cuenta.EstadoCuenta.ACTIVA, result.getEstado());
    }

    @Test
    @DisplayName("AperturaCuentaDefault: lanza excepción si saldo inicial es menor al mínimo")
    void aperturaSaldoMenorMinimo() {
        BankConfig config = mock(BankConfig.class);
        when(config.getSaldoMinimo(anyString())).thenReturn(500.0);
        FabricaProductosProvider fabrica = mock(FabricaProductosProvider.class);
        CuentaRepository cuentaRepo = mock(CuentaRepository.class);
        ClienteRepository clienteRepo = mock(ClienteRepository.class);
        Cliente cli = new Cliente();
        cli.setId("cli1");
        cli.setTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL);
        AperturaCuentaDefault tpl = new AperturaCuentaDefault(config, fabrica, cuentaRepo, clienteRepo);
        assertThrows(IllegalArgumentException.class, () -> tpl.abrirCuenta(cli, Cuenta.TipoCuenta.AHORROS, 100));
    }
}
