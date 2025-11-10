package com.example.controller.menu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.controller.BankingMenu;
import com.example.controller.ClienteController;
import com.example.controller.CreditoController;
import com.example.controller.CuentaController;
import com.example.controller.TransaccionController;

import java.util.Scanner;
import static org.mockito.Mockito.*;

class BankingMenuIntegrationTest {
    @Test
    @DisplayName("Integración: Opción 3 llama a abrirCuenta")
    void testOpcion3AbrirCuenta() {
        ClienteController cliente = mock(ClienteController.class);
        CuentaController cuenta = mock(CuentaController.class);
        CreditoController credito = mock(CreditoController.class);
        TransaccionController transaccion = mock(TransaccionController.class);
        BankingMenu menu = new BankingMenu(cliente, cuenta, credito, transaccion);
        Scanner sc = mock(Scanner.class);
        when(sc.nextLine()).thenReturn("3", "", "0");
        menu.run(sc);
        verify(cuenta).abrirCuenta(any(Scanner.class));
    }
}