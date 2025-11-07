package util;

import com.example.model.Cliente;
import java.time.LocalDateTime;

/**
 * Test data factory for creating test objects.
 * This class demonstrates the Builder pattern and provides
 * convenient methods for creating test data in various scenarios.
 */
public class TestDataFactory {

  /**
   * Creates a basic user for testing.
   */
  public static Cliente createBasicUser() {
    return new Cliente("testuser", "test@example.com");
  }

  /**
   * Creates a user with specific username and email.
   */
  public static Cliente createUser(String username, String email) {
    return new Cliente(username, email);
  }

  /**
   * Creates a user with ID (simulating a saved user).
   */
  public static Cliente createUserWithId(Long id, String username, String email) {
    Cliente user = new Cliente(username, email);
    user.setId(id);
    return user;
  }

  /**
   * Creates an inactive user.
   */
  public static Cliente createInactiveUser() {
    Cliente user = createBasicUser();
    user.deactivate();
    return user;
  }

  /**
   * Creates a user with invalid email for testing validation.
   */
  public static Cliente createUserWithInvalidEmail() {
    Cliente user = new Cliente();
    user.setUsername("testuser");
    user.setEmail("invalid-email");
    return user;
  }

  /**
   * UserBuilder for fluent test data creation.
   */
  public static class UserBuilder {
    private Long id;
    private String username = "testuser";
    private String email = "test@example.com";
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean active = true;

    public UserBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public UserBuilder withUsername(String username) {
      this.username = username;
      return this;
    }

    public UserBuilder withEmail(String email) {
      this.email = email;
      return this;
    }

    public UserBuilder withCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public UserBuilder inactive() {
      this.active = false;
      return this;
    }

    public UserBuilder active() {
      this.active = true;
      return this;
    }

    public Cliente build() {
      Cliente user = new Cliente(username, email);
      user.setId(id);
      user.setCreatedAt(createdAt);
      user.setActive(active);
      return user;
    }
  }

  /**
   * Creates a UserBuilder for fluent test data creation.
   */
  public static UserBuilder aUser() {
    return new UserBuilder();
  }

  /**
   * Creates multiple users for testing.
   */
  public static Cliente[] createMultipleUsers(int count) {
    Cliente[] users = new Cliente[count];
    for (int i = 0; i < count; i++) {
      users[i] = createUser("user" + i, "user" + i + "@example.com");
      users[i].setId((long) (i + 1));
    }
    return users;
  }
}