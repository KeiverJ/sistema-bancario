package com.example.repository.Cliente;

import com.example.model.cliente.Cliente;
import com.example.repository.cliente.ClienteRepository;

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
    @DisplayName("save genera id si es nulo")
    void saveGeneraIdSiNulo() {
        ClienteRepository repo = new ClienteRepository();
        Cliente c = new Cliente();
        c.setNombre("SinId");
        Cliente guardado = repo.save(c);
        assertNotNull(guardado.getId());
        assertEquals("SinId", guardado.getNombre());
    }

    @Test
    @DisplayName("findByNumeroDocumento retorna cliente correcto")
    void findByNumeroDocumento_funciona() {
        ClienteRepository repo = new ClienteRepository();
        Cliente c = new Cliente();
        c.setId("doc-1");
        c.setNumeroDocumento("ABC123");
        repo.save(c);
        var encontrado = repo.findByNumeroDocumento("ABC123");
        assertTrue(encontrado.isPresent());
        assertEquals("doc-1", encontrado.get().getId());
    }

    @Test
    @DisplayName("findAll retorna todos los clientes")
    void findAll_funciona() {
        ClienteRepository repo = new ClienteRepository();
        repo.save(new Cliente());
        repo.save(new Cliente());
        assertEquals(2, repo.findAll().size());
    }

    @Test
    @DisplayName("findByTipoCliente filtra correctamente")
    void findByTipoCliente_funciona() {
        ClienteRepository repo = new ClienteRepository();
        Cliente c1 = new Cliente();
        c1.setId("t1");
        c1.setTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL);
        Cliente c2 = new Cliente();
        c2.setId("t2");
        c2.setTipoCliente(Cliente.TipoCliente.PERSONA_JURIDICA);
        repo.save(c1);
        repo.save(c2);
        var naturales = repo.findByTipoCliente(Cliente.TipoCliente.PERSONA_NATURAL);
        assertEquals(1, naturales.size());
        assertEquals("t1", naturales.get(0).getId());
    }

    @Test
    @DisplayName("existsByNumeroDocumento funciona")
    void existsByNumeroDocumento_funciona() {
        ClienteRepository repo = new ClienteRepository();
        Cliente c = new Cliente();
        c.setNumeroDocumento("XD123");
        repo.save(c);
        assertTrue(repo.existsByNumeroDocumento("XD123"));
        assertFalse(repo.existsByNumeroDocumento("NOPE"));
    }

    @Test
    @DisplayName("deleteAll elimina todos los clientes")
    void deleteAll_funciona() {
        ClienteRepository repo = new ClienteRepository();
        repo.save(new Cliente());
        repo.save(new Cliente());
        repo.deleteAll();
        assertEquals(0, repo.count());
        assertTrue(repo.findAll().isEmpty());
    }

    @Test
    @DisplayName("count retorna cantidad correcta")
    void count_funciona() {
        ClienteRepository repo = new ClienteRepository();
        assertEquals(0, repo.count());
        repo.save(new Cliente());
        assertEquals(1, repo.count());
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
