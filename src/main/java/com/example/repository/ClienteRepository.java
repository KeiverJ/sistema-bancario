package com.example.repository;

import com.example.model.Cliente;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository para gestión de Clientes.
 * 
 * @Repository hace que Spring Boot cree UNA sola instancia (Singleton).
 */
public class ClienteRepository {

  private final Map<String, Cliente> clientes;

  public ClienteRepository() {
    this.clientes = new ConcurrentHashMap<>();
  }

  public Cliente save(Cliente cliente) {
    if (cliente.getId() == null || cliente.getId().isEmpty()) {
      cliente.setId(UUID.randomUUID().toString());
    }
    clientes.put(cliente.getId(), cliente);
    return cliente;
  }

  public Optional<Cliente> findById(String id) {
    return Optional.ofNullable(clientes.get(id));
  }

  public Optional<Cliente> findByNumeroDocumento(String numeroDocumento) {
    return clientes.values().stream()
        .filter(c -> c.getNumeroDocumento().equals(numeroDocumento))
        .findFirst();
  }

  public List<Cliente> findAll() {
    return new ArrayList<>(clientes.values());
  }

  public List<Cliente> findByTipoCliente(Cliente.TipoCliente tipoCliente) {
    return clientes.values().stream()
        .filter(c -> c.getTipoCliente() == tipoCliente)
        .toList();
  }

  public boolean existsByNumeroDocumento(String numeroDocumento) {
    return clientes.values().stream()
        .anyMatch(c -> c.getNumeroDocumento().equals(numeroDocumento));
  }

  public void delete(String id) {
    clientes.remove(id);
  }

  public void deleteAll() {
    clientes.clear();
  }

  public long count() {
    return clientes.size();
  }
}