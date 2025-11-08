package com.example.repository.Cliente;

import com.example.model.Cliente;
import com.example.repository.ClienteRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para ClienteRepository (en memoria).
 */
class ClienteRepositoryUnitTest {
    @Test
    @DisplayName("findById retorna vacío si no existe")
    void findById_noExiste() {
        ClienteRepository repo = new ClienteRepository();
        assertTrue(repo.findById("nope").isEmpty());
    }

    @Test
    @DisplayName("save y findById funcionan")
    void saveYFindById() {
        ClienteRepository repo = new ClienteRepository();
        Cliente c = new Cliente();
        c.setId("1");
        c.setNombre("UnitRepo");
        repo.save(c);
        var encontrado = repo.findById("1");
        assertTrue(encontrado.isPresent());
        assertEquals("UnitRepo", encontrado.get().getNombre());
    }

    @Test
    @DisplayName("delete elimina correctamente")
    void delete_funciona() {
        ClienteRepository repo = new ClienteRepository();
        Cliente c = new Cliente();
        c.setId("2");
        repo.save(c);
        repo.delete("2");
        assertTrue(repo.findById("2").isEmpty());
    }
}
