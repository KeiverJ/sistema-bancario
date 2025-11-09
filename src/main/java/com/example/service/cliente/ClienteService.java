package com.example.service.cliente;

import com.example.config.BankConfig;
import com.example.model.cliente.Cliente;
import com.example.repository.cliente.ClienteRepository;

import java.util.List;
import java.util.Optional;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service para operaciones de Cliente.
 * 
 * @Service hace que Spring Boot cree UNA sola instancia (Singleton).
 *          Usa Constructor Injection para inyectar dependencias.
 */
public class ClienteService {

  private final ClienteRepository clienteRepository;
  private final BankConfig bankConfig;
  private final AtomicInteger contadorCliente = new AtomicInteger(1);

  // Constructor Injection - Spring Boot inyecta automáticamente
  public ClienteService(ClienteRepository clienteRepository, BankConfig bankConfig) {
    this.clienteRepository = clienteRepository;
    this.bankConfig = bankConfig;
  }

  public Cliente crearCliente(String nombre, String tipoDocumento,
      String numeroDocumento, Cliente.TipoCliente tipoCliente) {
    Cliente cliente = new Cliente();
    cliente.setId(UUID.randomUUID().toString());
    cliente.setCodigo("CLI-" + String.format("%04d", contadorCliente.getAndIncrement()));
    cliente.setNombre(nombre);
    cliente.setTipoDocumento(tipoDocumento);
    cliente.setNumeroDocumento(numeroDocumento);
    cliente.setTipoCliente(tipoCliente);
    return clienteRepository.save(cliente);
  }

  public Optional<Cliente> obtenerClientePorDocumento(String numeroDocumento) {
    return clienteRepository.findByNumeroDocumento(numeroDocumento);
  }

  public List<Cliente> listarClientes() {
    return clienteRepository.findAll();
  }

  public Cliente actualizarCliente(String id, String email, String telefono) {
    Cliente cliente = clienteRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

    if (email != null) {
      cliente.setEmail(email);
    }
    if (telefono != null) {
      cliente.setTelefono(telefono);
    }

    return clienteRepository.save(cliente);
  }

  public boolean eliminarCliente(String id) {
    if (!clienteRepository.findById(id).isPresent()) {
      return false;
    }
    clienteRepository.delete(id);
    return true;
  }

  public boolean existeCliente(String numeroDocumento) {
    return clienteRepository.existsByNumeroDocumento(numeroDocumento);
  }

  public long contarClientes() {
    return clienteRepository.count();
  }

  public List<Cliente> listarClientesPorTipo(Cliente.TipoCliente tipoCliente) {
    return clienteRepository.findByTipoCliente(tipoCliente);
  }

  public List<Cliente> listarTodos() {
    return listarClientes();
  }

  public Optional<Cliente> obtenerCliente(String id) {
    return clienteRepository.findById(id);
  }

  public Optional<Cliente> obtenerClientePorCodigo(String codigo) {
    return clienteRepository.findAll().stream()
        .filter(c -> codigo.equals(c.getCodigo()))
        .findFirst();
  }

  public Cliente actualizarScore(String clienteId, int nuevoScore) {
    Cliente cliente = clienteRepository.findById(clienteId)
        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

    cliente.setScoreActual(nuevoScore);
    return clienteRepository.save(cliente);
  }

  public Cliente actualizarScorePorCodigo(String codigo, int nuevoScore) {
    Cliente cliente = clienteRepository.findAll().stream()
        .filter(c -> codigo.equals(c.getCodigo()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

    cliente.setScoreActual(nuevoScore);
    return clienteRepository.save(cliente);
  }
}