package com.example;

import com.example.composite.PaqueteProductos;
import com.example.decorator.*;
import com.example.model.Cliente;
import com.example.model.Credito;
import com.example.model.Cuenta;
import com.example.model.Transaccion;
import com.example.service.ClienteService;
import com.example.service.CreditoService;
import com.example.service.CuentaService;
import com.example.service.TransaccionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@SpringBootApplication
public class BankingSystemApp {

  private static final String ABORTAR = "0";

  public static void main(String[] args) {
    SpringApplication.run(BankingSystemApp.class, args);
  }

  @Bean
  CommandLineRunner menu(ClienteService clienteService,
      CuentaService cuentaService,
      CreditoService creditoService,
      TransaccionService transaccionService) {
    return args -> {
      Scanner sc = new Scanner(System.in);
      System.out.println("SISTEMA BANCARIO - PATRONES DE DISEÑO");
      System.out.println();

      boolean running = true;
      while (running) {
        mostrarMenu();
        System.out.print("\nOpción: ");
        String opcion = sc.nextLine().trim();

        try {
          running = procesarOpcion(opcion, sc, clienteService, cuentaService,
              creditoService, transaccionService);
        } catch (Exception e) {
          System.err.println("\nError: " + e.getMessage());
        }

        if (running) {
          System.out.println("\nPresione ENTER para continuar...");
          sc.nextLine();
        }
      }

      System.out.println("\n¡Hasta pronto!");
      sc.close();
    };
  }

  private void mostrarMenu() {
    System.out.println("\n" + "=".repeat(60));
    System.out.println("MENÚ PRINCIPAL");
    System.out.println("=".repeat(60));
    System.out.println();
    System.out.println("CLIENTES (Abstract Factory + Singleton)");
    System.out.println("  1.  Crear cliente");
    System.out.println("  2.  Listar clientes");
    System.out.println();
    System.out.println("CUENTAS (Abstract Factory + Adapter)");
    System.out.println("  3.  Abrir cuenta");
    System.out.println("  4.  Listar cuentas de cliente");
    System.out.println("  5.  Depositar");
    System.out.println("  6.  Retirar");
    System.out.println("  7.  Transferir");
    System.out.println("  16. Consultar saldo");
    System.out.println();
    System.out.println("TRANSACCIONES (Factory Method)");
    System.out.println("  8.  Crear transacción");
    System.out.println("  9.  Procesar transacción pendiente");
    System.out.println("  15. Ver transacciones de cuenta");
    System.out.println();
    System.out.println("CRÉDITOS (Builder + Strategy + Chain + State)");
    System.out.println("  10. Solicitar crédito");
    System.out.println("  11. Pagar cuota de crédito (State)");
    System.out.println("  12. Listar créditos de cliente");
    System.out.println("  14. Cambiar estado de crédito (State)");
    System.out.println("  17. Consultar saldo de crédito");
    System.out.println();
    System.out.println("PRODUCTOS (Composite + Decorator)");
    System.out.println("  13. Demostración de paquetes y decoradores");
    System.out.println();
    System.out.println("  18. Actualizar score de cliente");
    System.out.println();
    System.out.println("  0.  Salir");
    System.out.println("=".repeat(60));
  }

  private boolean procesarOpcion(String opcion, Scanner sc, ClienteService clienteService,
      CuentaService cuentaService, CreditoService creditoService,
      TransaccionService transaccionService) {
    System.out.println();

    switch (opcion) {
      case "1" -> crearCliente(sc, clienteService);
      case "2" -> listarClientes(clienteService);
      case "3" -> abrirCuenta(sc, clienteService, cuentaService);
      case "4" -> listarCuentasCliente(sc, clienteService, cuentaService);
      case "5" -> depositar(sc, cuentaService);
      case "6" -> retirar(sc, cuentaService);
      case "7" -> transferir(sc, cuentaService);
      case "8" -> crearTransaccion(sc, transaccionService, cuentaService);
      case "9" -> procesarTransaccionPendiente(sc, transaccionService);
      case "10" -> solicitarCredito(sc, clienteService, creditoService);
      case "11" -> pagarCuotaCredito(sc, creditoService);
      case "12" -> listarCreditosCliente(sc, clienteService, creditoService);
      case "13" -> demoCompositeDecorator(sc, clienteService, cuentaService, creditoService);
      case "14" -> cambiarEstadoCredito(sc, creditoService);
      case "15" -> verTransaccionesCuenta(sc, transaccionService, cuentaService);
      case "16" -> consultarSaldoCuenta(sc, cuentaService);
      case "17" -> consultarSaldoCredito(sc, creditoService);
      case "18" -> actualizarScoreCliente(sc, clienteService);
      case "0" -> {
        return false;
      }
      default -> System.out.println("Opción inválida");
    }

    return true;
  }

  // CLIENTES

  private void crearCliente(Scanner sc, ClienteService clienteService) {
    System.out.println("\nCREAR NUEVO CLIENTE\n");

    String nombre = solicitarTexto(sc, "Nombre completo", true);
    if (nombre == null)
      return;

    String tipoDoc = solicitarOpcion(sc, "Tipo de documento",
        new String[] { "CC", "NIT", "PASAPORTE" });
    if (tipoDoc == null)
      return;

    String numDoc = solicitarTexto(sc, "Número de documento", true);
    if (numDoc == null)
      return;

    Cliente.TipoCliente tipoCliente = solicitarEnum(sc, "Tipo de cliente",
        Cliente.TipoCliente.class);
    if (tipoCliente == null)
      return;

    try {
      Cliente cliente = clienteService.crearCliente(nombre, tipoDoc, numDoc, tipoCliente);
      System.out.println("\nCliente creado exitosamente");
      System.out.println("Código: " + cliente.getCodigo());
      System.out.println("Tipo: " + cliente.getTipoCliente());
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private void listarClientes(ClienteService clienteService) {
    System.out.println("\nLISTADO DE CLIENTES\n");

    List<Cliente> clientes = clienteService.listarTodos();

    if (clientes.isEmpty()) {
      System.out.println("No hay clientes registrados");
      return;
    }

    System.out.println(String.format("%-12s %-30s %-20s %-15s",
        "CÓDIGO", "NOMBRE", "TIPO", "DOCUMENTO"));
    System.out.println("-".repeat(80));

    clientes.forEach(c -> System.out.println(
        String.format("%-12s %-30s %-20s %-15s",
            c.getCodigo(),
            truncar(c.getNombre(), 30),
            c.getTipoCliente(),
            c.getNumeroDocumento())));

    System.out.println("-".repeat(80));
    System.out.println("Total: " + clientes.size() + " cliente(s)");
  }

  // CUENTAS

  private void abrirCuenta(Scanner sc, ClienteService clienteService, CuentaService cuentaService) {
    System.out.println("\nAPERTURA DE CUENTA\n");

    String codigoCliente = seleccionarCliente(sc, clienteService);
    if (codigoCliente == null)
      return;

    Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigoCliente);
    if (clienteOpt.isEmpty()) {
      System.err.println("Cliente no encontrado");
      return;
    }

    Cuenta.TipoCuenta tipo = solicitarEnum(sc, "Tipo de cuenta", Cuenta.TipoCuenta.class);
    if (tipo == null)
      return;

    Double saldoInicial = solicitarMonto(sc, "Saldo inicial");
    if (saldoInicial == null)
      return;

    try {
      Cuenta cuenta = cuentaService.abrirCuenta(clienteOpt.get().getId(), tipo, saldoInicial);
      System.out.println("\nCuenta creada exitosamente");
      System.out.println("Código: " + cuenta.getCodigo());
      System.out.println("Número: " + cuenta.getNumeroCuenta());
      System.out.println("Saldo: $" + String.format("%,.2f", cuenta.getSaldo()));
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private void listarCuentasCliente(Scanner sc, ClienteService clienteService, CuentaService cuentaService) {
    System.out.println("\nCUENTAS DEL CLIENTE\n");

    String codigoCliente = seleccionarCliente(sc, clienteService);
    if (codigoCliente == null)
      return;

    Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigoCliente);
    if (clienteOpt.isEmpty()) {
      System.err.println("Cliente no encontrado");
      return;
    }

    Cliente cliente = clienteOpt.get();

    if (cliente.getCuentaIds().isEmpty()) {
      System.out.println("El cliente no tiene cuentas");
      return;
    }

    System.out.println("Cliente: " + cliente.getNombre() + "\n");
    System.out.println(String.format("%-12s %-20s %-12s %15s %-12s",
        "CÓDIGO", "NÚMERO", "TIPO", "SALDO", "ESTADO"));
    System.out.println("-".repeat(75));

    for (String cuentaId : cliente.getCuentaIds()) {
      cuentaService.obtenerCuenta(cuentaId)
          .ifPresent(cuenta -> System.out.println(String.format("%-12s %-20s %-12s $%,14.2f %-12s",
              cuenta.getCodigo(),
              cuenta.getNumeroCuenta(),
              cuenta.getTipoCuenta(),
              cuenta.getSaldo(),
              cuenta.getEstado())));
    }

    System.out.println("-".repeat(75));
  }

  private void depositar(Scanner sc, CuentaService cuentaService) {
    System.out.println("\nDEPÓSITO\n");

    String codigoCuenta = solicitarTexto(sc, "Código de cuenta", true);
    if (codigoCuenta == null)
      return;

    Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuentaPorCodigo(codigoCuenta);
    if (cuentaOpt.isEmpty()) {
      System.err.println("Cuenta no encontrada");
      return;
    }

    Cuenta cuenta = cuentaOpt.get();
    System.out.println("Saldo actual: $" + String.format("%,.2f", cuenta.getSaldo()));

    Double monto = solicitarMonto(sc, "Monto a depositar");
    if (monto == null)
      return;

    try {
      boolean exito = cuentaService.depositar(cuenta.getId(), monto);

      if (exito) {
        Cuenta actualizada = cuentaService.obtenerCuenta(cuenta.getId()).orElse(cuenta);
        System.out.println("\nDepósito realizado");
        System.out.println("Nuevo saldo: $" + String.format("%,.2f", actualizada.getSaldo()));
      } else {
        System.err.println("No se pudo realizar el depósito");
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private void retirar(Scanner sc, CuentaService cuentaService) {
    System.out.println("\nRETIRO\n");

    String codigoCuenta = solicitarTexto(sc, "Código de cuenta", true);
    if (codigoCuenta == null)
      return;

    Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuentaPorCodigo(codigoCuenta);
    if (cuentaOpt.isEmpty()) {
      System.err.println("Cuenta no encontrada");
      return;
    }

    Cuenta cuenta = cuentaOpt.get();
    System.out.println("Saldo disponible: $" + String.format("%,.2f", cuenta.getSaldo()));

    Double monto = solicitarMonto(sc, "Monto a retirar");
    if (monto == null)
      return;

    if (monto > cuenta.getSaldo()) {
      System.err.println("Saldo insuficiente");
      return;
    }

    try {
      boolean exito = cuentaService.retirar(cuenta.getId(), monto);

      if (exito) {
        Cuenta actualizada = cuentaService.obtenerCuenta(cuenta.getId()).orElse(cuenta);
        System.out.println("\nRetiro realizado");
        System.out.println("Nuevo saldo: $" + String.format("%,.2f", actualizada.getSaldo()));
      } else {
        System.err.println("No se pudo realizar el retiro");
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private void transferir(Scanner sc, CuentaService cuentaService) {
    System.out.println("\nTRANSFERENCIA\n");

    String codigoOrigen = solicitarTexto(sc, "Código cuenta origen", true);
    if (codigoOrigen == null)
      return;

    Optional<Cuenta> cuentaOrigenOpt = cuentaService.obtenerCuentaPorCodigo(codigoOrigen);
    if (cuentaOrigenOpt.isEmpty()) {
      System.err.println("Cuenta origen no encontrada");
      return;
    }

    Cuenta cuentaOrigen = cuentaOrigenOpt.get();
    System.out.println("Saldo disponible: $" + String.format("%,.2f", cuentaOrigen.getSaldo()));

    String codigoDestino = solicitarTexto(sc, "Código cuenta destino", true);
    if (codigoDestino == null)
      return;

    if (codigoOrigen.equals(codigoDestino)) {
      System.err.println("Las cuentas deben ser diferentes");
      return;
    }

    Optional<Cuenta> cuentaDestinoOpt = cuentaService.obtenerCuentaPorCodigo(codigoDestino);
    if (cuentaDestinoOpt.isEmpty()) {
      System.err.println("Cuenta destino no encontrada");
      return;
    }

    Double monto = solicitarMonto(sc, "Monto a transferir");
    if (monto == null)
      return;

    if (monto > cuentaOrigen.getSaldo()) {
      System.err.println("Saldo insuficiente");
      return;
    }

    try {
      boolean exito = cuentaService.transferir(cuentaOrigen.getId(),
          cuentaDestinoOpt.get().getId(), monto);

      if (exito) {
        System.out.println("\nTransferencia realizada");
        System.out.println("Monto: $" + String.format("%,.2f", monto));
      } else {
        System.err.println("No se pudo realizar la transferencia");
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private void consultarSaldoCuenta(Scanner sc, CuentaService cuentaService) {
    System.out.println("\nCONSULTA DE SALDO - CUENTA\n");

    String codigoCuenta = solicitarTexto(sc, "Código de cuenta", true);
    if (codigoCuenta == null)
      return;

    Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuentaPorCodigo(codigoCuenta);

    if (cuentaOpt.isPresent()) {
      Cuenta cuenta = cuentaOpt.get();
      System.out.println("\nInformación de la cuenta");
      System.out.println("Número: " + cuenta.getNumeroCuenta());
      System.out.println("Tipo: " + cuenta.getTipoCuenta());
      System.out.println("Estado: " + cuenta.getEstado());
      System.out.println("Saldo: $" + String.format("%,.2f", cuenta.getSaldo()));
    } else {
      System.err.println("Cuenta no encontrada");
    }
  }

  // TRANSACCIONES

  private void crearTransaccion(Scanner sc, TransaccionService transaccionService,
      CuentaService cuentaService) {
    System.out.println("\nCREAR TRANSACCIÓN\n");

    Transaccion.TipoTransaccion tipo = solicitarEnum(sc, "Tipo de transacción",
        Transaccion.TipoTransaccion.class);
    if (tipo == null)
      return;

    Transaccion transaccion = null;

    try {
      switch (tipo) {
        case TRANSFERENCIA -> transaccion = crearTransferencia(sc, transaccionService, cuentaService);
        case PAGO_SERVICIO -> transaccion = crearPagoServicio(sc, transaccionService, cuentaService);
        case RETIRO -> transaccion = crearRetiroTx(sc, transaccionService, cuentaService);
        case DEPOSITO -> transaccion = crearDepositoTx(sc, transaccionService, cuentaService);
      }

      if (transaccion != null) {
        System.out.println("\nTransacción creada");
        System.out.println("Código: " + transaccion.getCodigo());
        System.out.println("Estado: " + transaccion.getEstado());
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  // ...existing code... (métodos crearTransferencia, crearPagoServicio, etc.
  // permanecen igual)

  private Transaccion crearTransferencia(Scanner sc, TransaccionService ts, CuentaService cs) {
    String origen = solicitarTexto(sc, "Código cuenta origen", true);
    if (origen == null)
      return null;

    Optional<Cuenta> origenOpt = cs.obtenerCuentaPorCodigo(origen);
    if (origenOpt.isEmpty()) {
      System.err.println("Cuenta origen no encontrada");
      return null;
    }

    String destino = solicitarTexto(sc, "Código cuenta destino", true);
    if (destino == null)
      return null;

    Optional<Cuenta> destinoOpt = cs.obtenerCuentaPorCodigo(destino);
    if (destinoOpt.isEmpty()) {
      System.err.println("Cuenta destino no encontrada");
      return null;
    }

    Double monto = solicitarMonto(sc, "Monto");
    if (monto == null)
      return null;

    String desc = solicitarTexto(sc, "Descripción", false);

    return ts.crearTransferencia(origenOpt.get().getId(),
        destinoOpt.get().getId(), monto, desc);
  }

  private Transaccion crearPagoServicio(Scanner sc, TransaccionService ts, CuentaService cs) {
    String origen = solicitarTexto(sc, "Código cuenta", true);
    if (origen == null)
      return null;

    Optional<Cuenta> origenOpt = cs.obtenerCuentaPorCodigo(origen);
    if (origenOpt.isEmpty()) {
      System.err.println("Cuenta no encontrada");
      return null;
    }

    String codServicio = solicitarTexto(sc, "Código del servicio", true);
    if (codServicio == null)
      return null;

    Double monto = solicitarMonto(sc, "Monto");
    if (monto == null)
      return null;

    String ref = solicitarTexto(sc, "Referencia", true);
    if (ref == null)
      return null;

    return ts.crearPagoServicio(origenOpt.get().getId(), codServicio, monto, ref);
  }

  private Transaccion crearRetiroTx(Scanner sc, TransaccionService ts, CuentaService cs) {
    String origen = solicitarTexto(sc, "Código cuenta", true);
    if (origen == null)
      return null;

    Optional<Cuenta> origenOpt = cs.obtenerCuentaPorCodigo(origen);
    if (origenOpt.isEmpty()) {
      System.err.println("Cuenta no encontrada");
      return null;
    }

    Double monto = solicitarMonto(sc, "Monto");
    if (monto == null)
      return null;

    String ubicacion = solicitarTexto(sc, "Ubicación", true);
    if (ubicacion == null)
      return null;

    return ts.crearRetiro(origenOpt.get().getId(), monto, ubicacion);
  }

  private Transaccion crearDepositoTx(Scanner sc, TransaccionService ts, CuentaService cs) {
    String destino = solicitarTexto(sc, "Código cuenta destino", true);
    if (destino == null)
      return null;

    Optional<Cuenta> destinoOpt = cs.obtenerCuentaPorCodigo(destino);
    if (destinoOpt.isEmpty()) {
      System.err.println("Cuenta no encontrada");
      return null;
    }

    Double monto = solicitarMonto(sc, "Monto");
    if (monto == null)
      return null;

    String desc = solicitarTexto(sc, "Descripción", false);

    return ts.crearDeposito(destinoOpt.get().getId(), monto, desc);
  }

  private void procesarTransaccionPendiente(Scanner sc, TransaccionService transaccionService) {
    System.out.println("\nPROCESAR TRANSACCIÓN PENDIENTE\n");

    String codigo = solicitarTexto(sc, "Código de transacción", true);
    if (codigo == null)
      return;

    Optional<Transaccion> txOpt = transaccionService.obtenerPorCodigo(codigo);

    if (txOpt.isEmpty()) {
      System.err.println("Transacción no encontrada");
      return;
    }

    Transaccion tx = txOpt.get();

    if (tx.getEstado() != Transaccion.EstadoTransaccion.PENDIENTE) {
      System.err.println("Transacción ya procesada");
      System.out.println("Estado: " + tx.getEstado());
      return;
    }

    try {
      Transaccion procesada = transaccionService.procesarTransaccion(tx.getId());
      System.out.println("\nTransacción procesada");
      System.out.println("Estado: " + procesada.getEstado());
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private void verTransaccionesCuenta(Scanner sc, TransaccionService transaccionService,
      CuentaService cuentaService) {
    System.out.println("\nTRANSACCIONES DE LA CUENTA\n");

    String codigo = solicitarTexto(sc, "Código de cuenta", true);
    if (codigo == null)
      return;

    Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuentaPorCodigo(codigo);
    if (cuentaOpt.isEmpty()) {
      System.err.println("Cuenta no encontrada");
      return;
    }

    List<Transaccion> txs = transaccionService.listarTransaccionesCuenta(cuentaOpt.get().getId());

    if (txs.isEmpty()) {
      System.out.println("No hay transacciones");
      return;
    }

    System.out.println(String.format("%-12s %-18s %15s %-12s",
        "CÓDIGO", "TIPO", "MONTO", "ESTADO"));
    System.out.println("-".repeat(60));

    txs.forEach(tx -> System.out.println(String.format("%-12s %-18s $%,14.2f %-12s",
        tx.getCodigo(),
        tx.getTipo(),
        tx.getMonto(),
        tx.getEstado())));

    System.out.println("-".repeat(60));
  }

  // CRÉDITOS

  private void solicitarCredito(Scanner sc, ClienteService clienteService,
      CreditoService creditoService) {
    System.out.println("\nSOLICITUD DE CRÉDITO\n");

    String codigo = seleccionarCliente(sc, clienteService);
    if (codigo == null)
      return;

    Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigo);
    if (clienteOpt.isEmpty()) {
      System.err.println("Cliente no encontrado");
      return;
    }

    Credito.TipoCredito tipo = solicitarEnum(sc, "Tipo de crédito", Credito.TipoCredito.class);
    if (tipo == null)
      return;

    Double monto = solicitarMonto(sc, "Monto solicitado");
    if (monto == null)
      return;

    Integer plazo = solicitarEnteroPositivo(sc, "Plazo en meses");
    if (plazo == null)
      return;

    try {
      Credito cr = creditoService.solicitarCredito(clienteOpt.get().getId(), tipo, monto, plazo);
      System.out.println("\nCrédito solicitado");
      System.out.println("Código: " + cr.getCodigo());
      System.out.println("Monto: $" + String.format("%,.2f", cr.getMonto()));
      System.out.println("Tasa: " + cr.getTasaInteres() + "%");
      System.out.println("Estado: " + cr.getEstadoActual());
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private void pagarCuotaCredito(Scanner sc, CreditoService creditoService) {
    System.out.println("\nPAGO DE CUOTA DE CRÉDITO\n");

    String codigo = solicitarTexto(sc, "Código de crédito", true);
    if (codigo == null)
      return;

    Optional<Credito> crOpt = creditoService.obtenerCreditoPorCodigo(codigo);
    if (crOpt.isEmpty()) {
      System.err.println("Crédito no encontrado");
      return;
    }

    Credito cr = crOpt.get();
    System.out.println("Estado: " + cr.getEstadoActual());
    System.out.println("Saldo: $" + String.format("%,.2f", cr.getSaldo()));

    Double monto = solicitarMonto(sc, "Monto del pago");
    if (monto == null)
      return;

    try {
      boolean exito = creditoService.pagarCuota(cr.getId(), monto);

      if (exito) {
        Credito actualizado = creditoService.obtenerCredito(cr.getId()).orElse(cr);
        System.out.println("\nPago registrado");
        System.out.println("Nuevo saldo: $" + String.format("%,.2f", actualizado.getSaldo()));

        if (actualizado.getSaldo() == 0) {
          System.out.println("\nCrédito pagado totalmente");
        }
      } else {
        System.err.println("No se pudo registrar el pago");
      }
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private void listarCreditosCliente(Scanner sc, ClienteService clienteService,
      CreditoService creditoService) {
    System.out.println("\nCRÉDITOS DEL CLIENTE\n");

    String codigo = seleccionarCliente(sc, clienteService);
    if (codigo == null)
      return;

    try {
      List<Credito> creditos = creditoService.listarCreditosClientePorCodigo(codigo);

      if (creditos.isEmpty()) {
        System.out.println("El cliente no tiene créditos");
        return;
      }

      System.out.println(String.format("%-12s %-18s %15s %15s %-15s",
          "CÓDIGO", "TIPO", "MONTO", "SALDO", "ESTADO"));
      System.out.println("-".repeat(75));

      creditos.forEach(cr -> System.out.println(String.format("%-12s %-18s $%,14.2f $%,14.2f %-15s",
          cr.getCodigo(),
          cr.getTipoCredito(),
          cr.getMonto(),
          cr.getSaldo(),
          cr.getEstadoActual())));

      System.out.println("-".repeat(75));

      // ✅ CORRECCIÓN: Solo sumar créditos que tengan saldo real (APROBADO,
      // DESEMBOLSADO, ACTIVO, EN_MORA)
      double totalSaldo = creditos.stream()
          .filter(cr -> cr.getEstadoActual() != Credito.EstadoCredito.RECHAZADO
              && cr.getEstadoActual() != Credito.EstadoCredito.CANCELADO)
          .mapToDouble(Credito::getSaldo)
          .sum();

      System.out.println("Total adeudado: $" + String.format("%,.2f", totalSaldo));
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private void cambiarEstadoCredito(Scanner sc, CreditoService creditoService) {
    System.out.println("\nCAMBIAR ESTADO DE CRÉDITO\n");

    String codigo = solicitarTexto(sc, "Código de crédito", true);
    if (codigo == null)
      return;

    Optional<Credito> crOpt = creditoService.obtenerCreditoPorCodigo(codigo);
    if (crOpt.isEmpty()) {
      System.err.println("Crédito no encontrado");
      return;
    }

    Credito cr = crOpt.get();
    System.out.println("Estado actual: " + cr.getEstadoActual());

    String op = solicitarOpcion(sc, "Operación (D=Desembolsar, M=Mora, C=Cerrar)",
        new String[] { "D", "M", "C" });
    if (op == null)
      return;

    try {
      switch (op) {
        case "D" -> {
          cr.desembolsar();
          System.out.println("\nCrédito desembolsado");
        }
        case "M" -> {
          cr.marcarMora();
          System.out.println("\nCrédito marcado en mora");
        }
        case "C" -> {
          cr.cerrar();
          System.out.println("\nCrédito cerrado");
        }
      }

      creditoService.guardar(cr);
      System.out.println("Nuevo estado: " + cr.getEstadoActual());

    } catch (IllegalStateException e) {
      System.err.println("Operación no válida: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private void consultarSaldoCredito(Scanner sc, CreditoService creditoService) {
    System.out.println("\nCONSULTA DE SALDO - CRÉDITO\n");

    String codigo = solicitarTexto(sc, "Código de crédito", true);
    if (codigo == null)
      return;

    Optional<Credito> crOpt = creditoService.obtenerCreditoPorCodigo(codigo);

    if (crOpt.isPresent()) {
      Credito cr = crOpt.get();
      System.out.println("\nInformación del crédito");
      System.out.println("Tipo: " + cr.getTipoCredito());
      System.out.println("Estado: " + cr.getEstadoActual());
      System.out.println("Monto: $" + String.format("%,.2f", cr.getMonto()));
      System.out.println("Saldo: $" + String.format("%,.2f", cr.getSaldo()));
      System.out.println("Tasa: " + cr.getTasaInteres() + "%");

      double progreso = ((cr.getMonto() - cr.getSaldo()) / cr.getMonto()) * 100;
      System.out.println("Pagado: " + String.format("%.1f", progreso) + "%");
    } else {
      System.err.println("Crédito no encontrado");
    }
  }

  // COMPOSITE + DECORATOR (permanece igual por su complejidad funcional)

  private void demoCompositeDecorator(Scanner sc, ClienteService clienteService,
      CuentaService cuentaService, CreditoService creditoService) {
    System.out.println("\nDEMO: PAQUETES Y DECORADORES\n");

    String codigo = seleccionarCliente(sc, clienteService);
    if (codigo == null)
      return;

    Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigo);
    if (clienteOpt.isEmpty()) {
      System.err.println("Cliente no encontrado");
      return;
    }

    Cliente cliente = clienteOpt.get();

    List<Cuenta> cuentas = new ArrayList<>();
    for (String cuentaId : cliente.getCuentaIds()) {
      cuentaService.obtenerCuenta(cuentaId).ifPresent(cuentas::add);
    }

    List<Credito> creditos = creditoService.listarCreditosCliente(cliente.getId());

    if (cuentas.isEmpty() && creditos.isEmpty()) {
      System.err.println("El cliente no tiene productos para armar paquete");
      return;
    }

    System.out.print("Nombre del paquete (ej: Premium, Básico, VIP): ");
    String nombrePaquete = sc.nextLine().trim();
    if (nombrePaquete.isEmpty()) {
      nombrePaquete = "Mi Paquete";
    }

    PaqueteProductos paquete = new PaqueteProductos(nombrePaquete);

    if (!cuentas.isEmpty()) {
      System.out.println("\nCUENTAS DISPONIBLES");
      for (int i = 0; i < cuentas.size(); i++) {
        Cuenta c = cuentas.get(i);
        System.out.println((i + 1) + ". " + c.getCodigo() + " - " + c.getTipoCuenta() +
            " (Saldo: $" + String.format("%,.2f", c.getSaldo()) + ")");
      }

      System.out.print("\n¿Agregar cuenta al paquete? (número, 0=omitir): ");
      try {
        int idx = Integer.parseInt(sc.nextLine().trim());

        if (idx > 0 && idx <= cuentas.size()) {
          Cuenta cuentaSeleccionada = cuentas.get(idx - 1);
          ProductoFinancieroComponent componente = new BaseProductoComponent(cuentaSeleccionada);

          System.out.println("\nDecorar cuenta con:");
          System.out.println("1. Cashback (2% compras - Costo: $5,000/mes - Beneficio: $20,000/mes)");
          System.out.println("2. Exención cuota manejo (Ahorro: $8,000/mes)");
          System.out.println("3. Beneficio VIP (Costo: $10,000/mes - Beneficio: $30,000/mes)");
          System.out.println("4. Seguro de vida (Costo: $15,000/mes)");
          System.out.println("5. Varios decoradores (combinados)");
          System.out.println("0. Sin decorar");
          System.out.print("Opción: ");

          int deco = Integer.parseInt(sc.nextLine().trim());

          switch (deco) {
            case 1 -> componente = new CashbackDecorator(componente);
            case 2 -> componente = new ExencionCuotaDecorator(componente);
            case 3 -> componente = new BeneficioVipDecorator(componente);
            case 4 -> componente = new SeguroVidaDecorator(componente);
            case 5 -> {
              componente = new CashbackDecorator(componente);
              componente = new BeneficioVipDecorator(componente);
              componente = new SeguroVidaDecorator(componente);
              System.out.println("Cuenta decorada con: Cashback + VIP + Seguro");
            }
          }

          paquete.add(componente);
          System.out.println("Cuenta agregada al paquete");
        }
      } catch (NumberFormatException e) {
        System.err.println("Entrada inválida, cuenta omitida");
      }
    }

    if (!creditos.isEmpty()) {
      System.out.println("\nCRÉDITOS DISPONIBLES");
      for (int i = 0; i < creditos.size(); i++) {
        Credito cr = creditos.get(i);
        System.out.println((i + 1) + ". " + cr.getCodigo() + " - " + cr.getTipoCredito() +
            " (Saldo: $" + String.format("%,.2f", cr.getSaldo()) +
            " - Estado: " + cr.getEstadoActual() + ")");
      }

      System.out.print("\n¿Agregar crédito al paquete? (número, 0=omitir): ");
      try {
        int idx = Integer.parseInt(sc.nextLine().trim());

        if (idx > 0 && idx <= creditos.size()) {
          Credito creditoSeleccionado = creditos.get(idx - 1);
          ProductoFinancieroComponent componente = new BaseProductoComponent(creditoSeleccionado);

          System.out.println("\nDecorar crédito con:");
          System.out.println("1. Seguro de vida (Costo: $15,000/mes)");
          System.out.println("2. Exención cuota administración (Ahorro: $8,000/mes)");
          System.out.println("3. Ambos decoradores");
          System.out.println("0. Sin decorar");
          System.out.print("Opción: ");

          int deco = Integer.parseInt(sc.nextLine().trim());

          switch (deco) {
            case 1 -> componente = new SeguroVidaDecorator(componente);
            case 2 -> componente = new ExencionCuotaDecorator(componente);
            case 3 -> {
              componente = new SeguroVidaDecorator(componente);
              componente = new ExencionCuotaDecorator(componente);
              System.out.println("Crédito decorado con: Seguro + Exención cuota");
            }
          }

          paquete.add(componente);
          System.out.println("Crédito agregado al paquete");
        }
      } catch (NumberFormatException e) {
        System.err.println("Entrada inválida, crédito omitido");
      }
    }

    if (paquete.getItems().isEmpty()) {
      System.out.println("\nNo se agregó ningún producto al paquete");
      return;
    }

    System.out.println("\n" + "=".repeat(60));
    System.out.println("RESUMEN DEL PAQUETE");
    System.out.println("=".repeat(60));
    System.out.println(paquete.getDescripcion());
    System.out.println("\nCosto mensual total: $" + String.format("%,.2f", paquete.getCostoMensual()));
    System.out.println("Beneficio mensual total: $" + String.format("%,.2f", paquete.getBeneficioMensual()));

    double balance = paquete.getBeneficioMensual() - paquete.getCostoMensual();
    if (balance > 0) {
      System.out.println("Balance neto: +$" + String.format("%,.2f", balance) + " (Beneficioso)");
    } else if (balance < 0) {
      System.out.println("Balance neto: -$" + String.format("%,.2f", Math.abs(balance)) + " (Costo adicional)");
    } else {
      System.out.println("Balance neto: $0 (Neutro)");
    }

    System.out.println("=".repeat(60));
  }

  // ACTUALIZAR SCORE

  private void actualizarScoreCliente(Scanner sc, ClienteService clienteService) {
    System.out.println("\nACTUALIZAR SCORE DE CLIENTE\n");

    String codigo = seleccionarCliente(sc, clienteService);
    if (codigo == null)
      return;

    Optional<Cliente> clienteOpt = clienteService.obtenerClientePorCodigo(codigo);
    if (clienteOpt.isEmpty()) {
      System.err.println("Cliente no encontrado");
      return;
    }

    Cliente cliente = clienteOpt.get();
    System.out.println("Cliente: " + cliente.getNombre());
    System.out.println("Score actual: " + cliente.getScoreActual());

    Integer nuevoScore = solicitarEnteroPositivo(sc, "Nuevo score (300-850)");
    if (nuevoScore == null)
      return;

    if (nuevoScore < 300 || nuevoScore > 850) {
      System.err.println("El score debe estar entre 300 y 850");
      return;
    }

    try {
      Cliente actualizado = clienteService.actualizarScorePorCodigo(codigo, nuevoScore);
      System.out.println("\nScore actualizado exitosamente");
      System.out.println("Cliente: " + actualizado.getNombre());
      System.out.println("Score anterior: " + cliente.getScoreActual());
      System.out.println("Score nuevo: " + actualizado.getScoreActual());

      String categoria = categorizarScore(nuevoScore);
      System.out.println("Categoría: " + categoria);

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

  private String categorizarScore(int score) {
    if (score >= 750)
      return "EXCELENTE (tasas preferenciales)";
    if (score >= 650)
      return "BUENO (tasas competitivas)";
    if (score >= 550)
      return "REGULAR (tasas estándar)";
    if (score >= 450)
      return "BAJO (tasas altas)";
    return "MUY BAJO (difícil aprobación)";
  }

  // MÉTODOS AUXILIARES

  private String solicitarTexto(Scanner sc, String mensaje, boolean obligatorio) {
    while (true) {
      System.out.print(mensaje + (obligatorio ? "" : " (opcional)") + " [0=cancelar]: ");
      String valor = sc.nextLine().trim();

      if (valor.equals(ABORTAR)) {
        System.out.println("Operación cancelada");
        return null;
      }

      if (!valor.isEmpty() || !obligatorio) {
        return valor;
      }

      System.err.println("Campo obligatorio");
    }
  }

  private Double solicitarMonto(Scanner sc, String mensaje) {
    while (true) {
      System.out.print(mensaje + " [0=cancelar]: $");
      String entrada = sc.nextLine().trim();

      if (entrada.equals(ABORTAR)) {
        System.out.println("Operación cancelada");
        return null;
      }

      try {
        double monto = Double.parseDouble(entrada);
        if (monto > 0) {
          return monto;
        }
        System.err.println("Debe ser mayor a cero");
      } catch (NumberFormatException e) {
        System.err.println("Monto inválido");
      }
    }
  }

  private Integer solicitarEnteroPositivo(Scanner sc, String mensaje) {
    while (true) {
      System.out.print(mensaje + " [0=cancelar]: ");
      String entrada = sc.nextLine().trim();

      if (entrada.equals(ABORTAR)) {
        System.out.println("Operación cancelada");
        return null;
      }

      try {
        int valor = Integer.parseInt(entrada);
        if (valor > 0) {
          return valor;
        }
        System.err.println("Debe ser mayor a cero");
      } catch (NumberFormatException e) {
        System.err.println("Valor inválido");
      }
    }
  }

  private <E extends Enum<E>> E solicitarEnum(Scanner sc, String mensaje, Class<E> enumClass) {
    E[] valores = enumClass.getEnumConstants();

    System.out.println("\n" + mensaje + ":");
    for (int i = 0; i < valores.length; i++) {
      System.out.println("  " + (i + 1) + ". " + valores[i]);
    }
    System.out.println("  0. Cancelar");

    while (true) {
      System.out.print("\nOpción: ");
      String entrada = sc.nextLine().trim();

      if (entrada.equals(ABORTAR)) {
        System.out.println("Operación cancelada");
        return null;
      }

      try {
        int opcion = Integer.parseInt(entrada);
        if (opcion > 0 && opcion <= valores.length) {
          return valores[opcion - 1];
        }
        System.err.println("Opción inválida");
      } catch (NumberFormatException e) {
        System.err.println("Ingrese un número");
      }
    }
  }

  private String solicitarOpcion(Scanner sc, String mensaje, String[] opciones) {
    System.out.println("\n" + mensaje);

    while (true) {
      System.out.print("→ ");
      String entrada = sc.nextLine().trim().toUpperCase();

      if (entrada.equals(ABORTAR)) {
        System.out.println("Operación cancelada");
        return null;
      }

      for (String opcion : opciones) {
        if (opcion.equals(entrada)) {
          return entrada;
        }
      }

      System.err.println("Opción inválida");
    }
  }

  private String seleccionarCliente(Scanner sc, ClienteService clienteService) {
    List<Cliente> clientes = clienteService.listarTodos();

    if (clientes.isEmpty()) {
      System.err.println("No hay clientes. Cree un cliente primero");
      return null;
    }

    System.out.println("\nClientes:");
    System.out.println(String.format("%-12s %-30s", "CÓDIGO", "NOMBRE"));
    System.out.println("-".repeat(45));

    clientes.forEach(c -> System.out.println(
        String.format("%-12s %-30s", c.getCodigo(), truncar(c.getNombre(), 30))));

    System.out.println("-".repeat(45));

    String codigo = solicitarTexto(sc, "\nCódigo del cliente", true);

    if (codigo != null && clienteService.obtenerClientePorCodigo(codigo).isEmpty()) {
      System.err.println("Cliente no encontrado");
      return null;
    }

    return codigo;
  }

  private String truncar(String texto, int longitud) {
    if (texto == null)
      return "";
    return texto.length() > longitud ? texto.substring(0, longitud - 3) + "..." : texto;
  }
}