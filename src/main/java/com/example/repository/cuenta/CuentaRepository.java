package com.example.repository.cuenta;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.example.model.cuenta.Cuenta;

/**
 * Repository para gestión de Cuentas.
 * 
 * @Repository hace que Spring Boot cree UNA sola instancia (Singleton).
 */
public class CuentaRepository {

    private final Map<String, Cuenta> cuentas;

    public CuentaRepository() {
        this.cuentas = new ConcurrentHashMap<>();
    }

    public Cuenta save(Cuenta cuenta) {
        if (cuenta.getId() == null || cuenta.getId().isEmpty()) {
            cuenta.setId(UUID.randomUUID().toString());
        }
        if (cuenta.getNumeroCuenta() == null || cuenta.getNumeroCuenta().isEmpty()) {
            cuenta.setNumeroCuenta(generarNumeroCuenta());
        }
        cuentas.put(cuenta.getId(), cuenta);
        return cuenta;
    }

    public Optional<Cuenta> findById(String id) {
        return Optional.ofNullable(cuentas.get(id));
    }

    public Optional<Cuenta> findByNumeroCuenta(String numeroCuenta) {
        return cuentas.values().stream()
                .filter(c -> c.getNumeroCuenta().equals(numeroCuenta))
                .findFirst();
    }

    public List<Cuenta> findByClienteId(String clienteId) {
        return cuentas.values().stream()
                .filter(c -> c.getClienteId().equals(clienteId))
                .toList();
    }

    public List<Cuenta> findAll() {
        return new ArrayList<>(cuentas.values());
    }

    public List<Cuenta> findByEstado(Cuenta.EstadoCuenta estado) {
        return cuentas.values().stream()
                .filter(c -> c.getEstado() == estado)
                .toList();
    }

    public void delete(String id) {
        cuentas.remove(id);
    }

    public void deleteAll() {
        cuentas.clear();
    }

    public long count() {
        return cuentas.size();
    }

    private String generarNumeroCuenta() {
        return "CTA-" + System.currentTimeMillis() + "-" + new Random().nextInt(1000);
    }
}