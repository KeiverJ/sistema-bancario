package util;

import com.example.model.Cliente;
import com.example.repository.ClienteRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Test Doubles Examples - This package demonstrates different types of test
 * doubles:
 * 
 * 1. MOCK: Verifies behavior and interactions (using Mockito)
 * 2. STUB: Provides predefined responses
 * 3. SPY: Partial mocking of real objects
 * 4. FAKE: Simple working implementation for testing
 */
public class TestDoubles {

  /**
   * STUB: UserRepository that returns predefined responses.
   * Stubs provide canned answers to calls made during tests.
   */
  public static class UserRepositoryStub extends ClienteRepository {
    private final Map<String, Cliente> predefinedUsers;
    private final boolean shouldThrowException;

    public UserRepositoryStub() {
      this.predefinedUsers = new HashMap<>();
      this.shouldThrowException = false;
      setupPredefinedData();
    }

    public UserRepositoryStub(boolean shouldThrowException) {
      this.predefinedUsers = new HashMap<>();
      this.shouldThrowException = shouldThrowException;
      if (!shouldThrowException) {
        setupPredefinedData();
      }
    }

    private void setupPredefinedData() {
      Cliente user1 = TestDataFactory.createUserWithId(1L, "stub_user", "stub@example.com");
      Cliente user2 = TestDataFactory.createUserWithId(2L, "another_user", "another@example.com");
      predefinedUsers.put("stub_user", user1);
      predefinedUsers.put("another_user", user2);
    }

    @Override
    public Optional<Cliente> findByUsername(String username) {
      if (shouldThrowException) {
        throw new RuntimeException("Stub exception");
      }
      return Optional.ofNullable(predefinedUsers.get(username));
    }

    @Override
    public Optional<Cliente> findById(Long id) {
      if (shouldThrowException) {
        throw new RuntimeException("Stub exception");
      }
      return predefinedUsers.values().stream()
          .filter(user -> Objects.equals(user.getId(), id))
          .findFirst();
    }

    @Override
    public boolean existsByUsername(String username) {
      if (shouldThrowException) {
        throw new RuntimeException("Stub exception");
      }
      return predefinedUsers.containsKey(username);
    }
  }

  /**
   * FAKE: In-memory UserRepository implementation.
   * Fakes have working implementations, but usually take shortcuts
   * which make them not suitable for production.
   */
  public static class FakeUserRepository extends ClienteRepository {
    private final Map<Long, Cliente> users;
    private final AtomicLong idGenerator;
    private boolean simulateFailure = false;

    public FakeUserRepository() {
      this.users = new HashMap<>();
      this.idGenerator = new AtomicLong(1);
    }

    // Method to simulate failures for testing error scenarios
    public void setSimulateFailure(boolean simulateFailure) {
      this.simulateFailure = simulateFailure;
    }

    @Override
    public Cliente save(Cliente user) {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }

      if (user == null) {
        throw new IllegalArgumentException("User cannot be null");
      }

      if (user.getId() == null) {
        user.setId(idGenerator.getAndIncrement());
      }

      users.put(user.getId(), user);
      return user;
    }

    @Override
    public Optional<Cliente> findById(Long id) {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }
      return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<Cliente> findByUsername(String username) {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }

      return users.values().stream()
          .filter(user -> Objects.equals(user.getUsername(), username))
          .findFirst();
    }

    @Override
    public Optional<Cliente> findByEmail(String email) {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }

      return users.values().stream()
          .filter(user -> Objects.equals(user.getEmail(), email))
          .findFirst();
    }

    @Override
    public List<Cliente> findAll() {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }
      return new ArrayList<>(users.values());
    }

    @Override
    public List<Cliente> findAllActive() {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }
      return users.values().stream()
          .filter(Cliente::isActive)
          .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    @Override
    public Cliente update(Cliente user) {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }

      if (user == null || user.getId() == null) {
        throw new IllegalArgumentException("User and ID cannot be null");
      }

      if (!users.containsKey(user.getId())) {
        throw new IllegalArgumentException("User not found");
      }

      users.put(user.getId(), user);
      return user;
    }

    @Override
    public boolean deleteById(Long id) {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }
      return users.remove(id) != null;
    }

    @Override
    public boolean existsByUsername(String username) {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }
      return users.values().stream()
          .anyMatch(user -> Objects.equals(user.getUsername(), username));
    }

    @Override
    public boolean existsByEmail(String email) {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }
      return users.values().stream()
          .anyMatch(user -> Objects.equals(user.getEmail(), email));
    }

    @Override
    public long count() {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }
      return users.size();
    }

    @Override
    public long countActive() {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }
      return users.values().stream()
          .filter(Cliente::isActive)
          .count();
    }

    @Override
    public void deleteAll() {
      if (simulateFailure) {
        throw new RuntimeException("Fake repository failure");
      }
      users.clear();
      idGenerator.set(1);
    }

    @Override
    public boolean isConnectionValid() {
      return !simulateFailure;
    }

    // Utility methods for testing
    public int size() {
      return users.size();
    }

    public boolean isEmpty() {
      return users.isEmpty();
    }
  }

  /**
   * DUMMY: Objects that are passed around but never actually used.
   * Usually just used to fill parameter lists.
   */
  public static class DummyDatabaseConfig {
    // This is just a placeholder - methods are not implemented
    // because they should never be called in the test scenarios where dummies are
    // used

    public String getDatabaseUrl() {
      throw new UnsupportedOperationException("Dummy should not be used");
    }

    public String getUsername() {
      throw new UnsupportedOperationException("Dummy should not be used");
    }

    public boolean isValidConfiguration() {
      throw new UnsupportedOperationException("Dummy should not be used");
    }
  }
}