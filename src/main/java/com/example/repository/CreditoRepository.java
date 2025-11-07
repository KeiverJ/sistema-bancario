package com.example.repository;

import com.example.model.Credito;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository para gestión de Créditos.
 * 
 * @Repository hace que Spring Boot cree UNA sola instancia (Singleton).
 */
@Repository
public class CreditoRepository {

    private final Map<String, Credito> creditos;

    public CreditoRepository() {
        this.creditos = new ConcurrentHashMap<>();
    }

    public Credito save(Credito credito) {
        if (credito.getId() == null || credito.getId().isEmpty()) {
            credito.setId(UUID.randomUUID().toString());
        }
        creditos.put(credito.getId(), credito);
        return credito;
    }

    public Optional<Credito> findById(String id) {
        return Optional.ofNullable(creditos.get(id));
    }

    public List<Credito> findByClienteId(String clienteId) {
        return creditos.values().stream()
                .filter(c -> c.getClienteId().equals(clienteId))
                .toList();
    }

    public List<Credito> findAll() {
        return new ArrayList<>(creditos.values());
    }

    public List<Credito> findByTipoCredito(Credito.TipoCredito tipoCredito) {
        return creditos.values().stream()
                .filter(c -> c.getTipoCredito() == tipoCredito)
                .toList();
    }

    public List<Credito> findByEstado(String estado) {
        return creditos.values().stream()
                .filter(c -> estado.equals(c.getEstadoActual()))
                .toList();
    }

    public double getTotalSaldoByClienteId(String clienteId) {
        return creditos.values().stream()
                .filter(c -> c.getClienteId().equals(clienteId))
                .mapToDouble(Credito::getSaldo)
                .sum();
    }

    public void delete(String id) {
        creditos.remove(id);
    }

    public void deleteAll() {
        creditos.clear();
    }

    public long count() {
        return creditos.size();
    }
}