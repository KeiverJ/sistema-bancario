package com.example.repository.transaccion;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.example.model.transacccion.Transaccion;

/**
 * Repository para gestión de Transacciones.
 */
public class TransaccionRepository {

    private final Map<String, Transaccion> transacciones;

    public TransaccionRepository() {
        this.transacciones = new ConcurrentHashMap<>();
    }

    public Transaccion save(Transaccion transaccion) {
        if (transaccion.getId() == null || transaccion.getId().isEmpty()) {
            transaccion.setId(UUID.randomUUID().toString());
        }
        transacciones.put(transaccion.getId(), transaccion);
        return transaccion;
    }

    public Optional<Transaccion> findById(String id) {
        return Optional.ofNullable(transacciones.get(id));
    }

    public List<Transaccion> findByCuentaId(String cuentaId) {
        return transacciones.values().stream()
                .filter(t -> cuentaId.equals(t.getCuentaOrigenId())
                        || cuentaId.equals(t.getCuentaDestinoId()))
                .toList();
    }

    public List<Transaccion> findByTipo(Transaccion.TipoTransaccion tipo) {
        return transacciones.values().stream()
                .filter(t -> t.getTipo() == tipo)
                .toList();
    }

    public List<Transaccion> findByEstado(Transaccion.EstadoTransaccion estado) {
        return transacciones.values().stream()
                .filter(t -> t.getEstado() == estado)
                .toList();
    }

    public List<Transaccion> findByFechaRange(LocalDateTime inicio, LocalDateTime fin) {
        return transacciones.values().stream()
                .filter(t -> !t.getFecha().isBefore(inicio) && !t.getFecha().isAfter(fin))
                .toList();
    }

    public List<Transaccion> findAll() {
        return new ArrayList<>(transacciones.values());
    }

    public void delete(String id) {
        transacciones.remove(id);
    }

    public void deleteAll() {
        transacciones.clear();
    }

    public long count() {
        return transacciones.size();
    }
}