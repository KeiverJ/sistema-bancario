package integration;

import com.example.config.databaseConfig;
import com.example.model.Cliente;
import com.example.repository.ClienteRepository;
import com.example.service.ClienteService;
import org.junit.jupiter.api.*;
import util.TestDataFactory;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Integration tests that test the interaction between UserService and
 * UserRepository
 * without mocks. These tests verify that components work together correctly.
 * 
 * Integration tests:
 * - Use real implementations instead of mocks
 * - Test component interactions
 * - Verify data flow between layers
 * - Test transaction-like scenarios
 */
@DisplayName("User Service Integration Tests")
class UserServiceIntegrationTest {

  private ClienteService userService;
  private ClienteRepository userRepository;
  private databaseConfig databaseConfig;

  @BeforeEach
  void setUp() {
    // Use real implementations - no mocks
    databaseConfig = databaseConfig.getInstance();
    userRepository = new ClienteRepository(databaseConfig);
    userService = new ClienteService(userRepository);

    // Clean state for each test
    userRepository.deleteAll();
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
  }

  @Nested
  @DisplayName("Service-Repository Integration")
  class ServiceRepositoryIntegrationTests {

    @Test
    @DisplayName("Should create and retrieve user through service and repository layers")
    void shouldCreateAndRetrieveUserThroughServiceAndRepositoryLayers() {
      // Act - Use service layer which internally uses repository
      Cliente createdUser = userService.createUser("integration_user", "integration@example.com");
      Cliente retrievedUser = userService.getUserById(createdUser.getId());

      // Assert - Verify full integration
      assertThat(retrievedUser)
          .isNotNull()
          .satisfies(user -> {
            assertThat(user.getId()).isEqualTo(createdUser.getId());
            assertThat(user.getUsername()).isEqualTo("integration_user");
            assertThat(user.getEmail()).isEqualTo("integration@example.com");
            assertThat(user.isActive()).isTrue();
            assertThat(user.getCreatedAt()).isNotNull();
          });

      // Verify repository state directly
      Optional<Cliente> repositoryUser = userRepository.findById(createdUser.getId());
      assertThat(repositoryUser).isPresent();
      assertThat(repositoryUser.get()).isEqualTo(retrievedUser);
    }

    @Test
    @DisplayName("Should prevent duplicate usernames across service and repository")
    void shouldPreventDuplicateUsernamesAcrossServiceAndRepository() {
      // Arrange - Create first user
      userService.createUser("duplicate_test", "first@example.com");

      // Act & Assert - Attempt to create second user with same username
      assertThatThrownBy(() -> userService.createUser("duplicate_test", "second@example.com"))
          .isInstanceOf(ClienteService.UserServiceException.class)
          .hasMessage("Username is already taken: duplicate_test");

      // Verify repository state
      List<Cliente> allUsers = userRepository.findAll();
      assertThat(allUsers).hasSize(1);
      assertThat(allUsers.get(0).getEmail()).isEqualTo("first@example.com");
    }

    @Test
    @DisplayName("Should prevent duplicate emails across service and repository")
    void shouldPreventDuplicateEmailsAcrossServiceAndRepository() {
      // Arrange
      userService.createUser("user1", "duplicate@example.com");

      // Act & Assert
      assertThatThrownBy(() -> userService.createUser("user2", "duplicate@example.com"))
          .isInstanceOf(ClienteService.UserServiceException.class)
          .hasMessage("Email is already registered: duplicate@example.com");

      // Verify repository state
      assertThat(userRepository.count()).isEqualTo(1);
      assertThat(userRepository.existsByEmail("duplicate@example.com")).isTrue();
    }
  }

  @Nested
  @DisplayName("User Lifecycle Integration Tests")
  class UserLifecycleIntegrationTests {

    @Test
    @DisplayName("Should handle complete user lifecycle")
    void shouldHandleCompleteUserLifecycle() {
      // 1. Create user
      Cliente createdUser = userService.createUser("lifecycle_user", "lifecycle@example.com");
      assertThat(createdUser.getId()).isNotNull();
      assertThat(createdUser.isActive()).isTrue();

      // 2. Update user
      Cliente updatedUser = userService.updateUser(
          createdUser.getId(),
          "updated_username",
          "updated@example.com");
      assertThat(updatedUser.getUsername()).isEqualTo("updated_username");
      assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");

      // 3. Deactivate user
      Cliente deactivatedUser = userService.deactivateUser(createdUser.getId());
      assertThat(deactivatedUser.isActive()).isFalse();

      // 4. Reactivate user
      Cliente reactivatedUser = userService.activateUser(createdUser.getId());
      assertThat(reactivatedUser.isActive()).isTrue();

      // 5. Verify repository state throughout lifecycle
      Optional<Cliente> finalUser = userRepository.findById(createdUser.getId());
      assertThat(finalUser).isPresent();
      assertThat(finalUser.get().getUsername()).isEqualTo("updated_username");
      assertThat(finalUser.get().getEmail()).isEqualTo("updated@example.com");
      assertThat(finalUser.get().isActive()).isTrue();

      // 6. Delete user
      userService.deleteUser(createdUser.getId());
      assertThat(userRepository.findById(createdUser.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should maintain data consistency during concurrent operations")
    void shouldMaintainDataConsistencyDuringConcurrentOperations() {
      // Create multiple users
      Cliente user1 = userService.createUser("user1", "user1@example.com");
      Cliente user2 = userService.createUser("user2", "user2@example.com");
      Cliente user3 = userService.createUser("user3", "user3@example.com");

      // Perform various operations
      userService.deactivateUser(user2.getId());
      userService.updateUser(user1.getId(), "updated_user1", null);
      userService.deleteUser(user3.getId());

      // Verify final state
      List<Cliente> allUsers = userService.getAllUsers();
      List<Cliente> activeUsers = userService.getAllActiveUsers();

      assertThat(allUsers).hasSize(2);
      assertThat(activeUsers).hasSize(1);

      Optional<Cliente> updatedUser1 = userRepository.findByUsername("updated_user1");
      assertThat(updatedUser1).isPresent();
      assertThat(updatedUser1.get().isActive()).isTrue();

      Optional<Cliente> deactivatedUser2 = userRepository.findById(user2.getId());
      assertThat(deactivatedUser2).isPresent();
      assertThat(deactivatedUser2.get().isActive()).isFalse();

      assertThat(userRepository.findById(user3.getId())).isEmpty();
    }
  }

  @Nested
  @DisplayName("Bulk Operations Integration Tests")
  class BulkOperationsIntegrationTests {

    @Test
    @DisplayName("Should handle bulk user operations")
    void shouldHandleBulkUserOperations() {
      // Create multiple users
      Cliente[] users = TestDataFactory.createMultipleUsers(5);
      for (Cliente user : users) {
        userService.createUser(user.getUsername(), user.getEmail());
      }

      // Verify bulk retrieval
      List<Cliente> allUsers = userService.getAllUsers();
      List<Cliente> activeUsers = userService.getAllActiveUsers();

      assertThat(allUsers).hasSize(5);
      assertThat(activeUsers).hasSize(5);
      assertThat(userService.getTotalUserCount()).isEqualTo(5);
      assertThat(userService.getActiveUserCount()).isEqualTo(5);

      // Deactivate some users
      List<Cliente> savedUsers = userRepository.findAll();
      userService.deactivateUser(savedUsers.get(0).getId());
      userService.deactivateUser(savedUsers.get(1).getId());

      // Verify counts after deactivation
      assertThat(userService.getTotalUserCount()).isEqualTo(5);
      assertThat(userService.getActiveUserCount()).isEqualTo(3);

      List<Cliente> activeAfterDeactivation = userService.getAllActiveUsers();
      assertThat(activeAfterDeactivation).hasSize(3);
    }

    @Test
    @DisplayName("Should maintain referential integrity during bulk operations")
    void shouldMaintainReferentialIntegrityDuringBulkOperations() {
      // Create users with various states
      Cliente activeUser = userService.createUser("active_user", "active@example.com");
      Cliente inactiveUser = userService.createUser("inactive_user", "inactive@example.com");
      userService.deactivateUser(inactiveUser.getId());

      // Verify initial state
      assertThat(userRepository.count()).isEqualTo(2);
      assertThat(userRepository.countActive()).isEqualTo(1);

      // Perform search operations
      Optional<Cliente> foundActive = userService.findUserByUsername("active_user");
      Optional<Cliente> foundInactive = userService.findUserByUsername("inactive_user");
      Optional<Cliente> foundByEmail = userService.findUserByEmail("active@example.com");

      assertThat(foundActive).isPresent();
      assertThat(foundInactive).isPresent();
      assertThat(foundByEmail).isPresent();

      // Verify states are maintained
      assertThat(foundActive.get().isActive()).isTrue();
      assertThat(foundInactive.get().isActive()).isFalse();
      assertThat(foundByEmail.get().getUsername()).isEqualTo("active_user");
    }
  }

  @Nested
  @DisplayName("Error Handling Integration Tests")
  class ErrorHandlingIntegrationTests {

    @Test
    @DisplayName("Should handle repository exceptions in service layer")
    void shouldHandleRepositoryExceptionsInServiceLayer() {
      // Create a user first
      Cliente user = userService.createUser("test_user", "test@example.com");

      // Attempt operations that should fail due to business rules
      assertThatThrownBy(() -> userService.updateUser(user.getId(), "test_user", null))
          .isInstanceOf(ClienteService.UserServiceException.class);

      assertThatThrownBy(() -> userService.updateUser(user.getId(), null, "test@example.com"))
          .isInstanceOf(ClienteService.UserServiceException.class);

      // Verify user state remains unchanged
      Cliente unchangedUser = userService.getUserById(user.getId());
      assertThat(unchangedUser.getUsername()).isEqualTo("test_user");
      assertThat(unchangedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should propagate validation errors from repository through service")
    void shouldPropagateValidationErrorsFromRepositoryThroughService() {
      // Test validation errors propagation
      assertThatThrownBy(() -> userService.createUser("ab", "valid@example.com"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Username must be at least 3 characters long");

      assertThatThrownBy(() -> userService.createUser("valid_user", "invalid-email"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Invalid email format");

      // Verify no data was saved
      assertThat(userRepository.count()).isZero();
    }
  }

  @Nested
  @DisplayName("Singleton Integration Tests")
  class SingletonIntegrationTests {

    @Test
    @DisplayName("Should use singleton DatabaseConfig across all components")
    void shouldUseSingletonDatabaseConfigAcrossAllComponents() {
      // Get DatabaseConfig instances from different sources
      databaseConfig serviceConfig = databaseConfig.getInstance();
      databaseConfig repositoryConfig = userRepository.getDatabaseConfig();
      databaseConfig directConfig = databaseConfig.getInstance();

      // Verify all references point to the same singleton instance
      assertThat(serviceConfig)
          .isSameAs(repositoryConfig)
          .isSameAs(directConfig);

      // Verify singleton state is shared
      String testProperty = "integration.test.property";
      String testValue = "shared_value";

      serviceConfig.setProperty(testProperty, testValue);

      assertThat(repositoryConfig.getProperty(testProperty)).isEqualTo(testValue);
      assertThat(directConfig.getProperty(testProperty)).isEqualTo(testValue);
    }

    @Test
    @DisplayName("Should verify service operational status using singleton config")
    void shouldVerifyServiceOperationalStatusUsingSingletonConfig() {
      // Test service health check that depends on singleton configuration
      boolean isOperational = userService.isServiceOperational();
      assertThat(isOperational).isTrue();

      // Verify the check uses the same configuration as repository
      boolean repositoryConnectionValid = userRepository.isConnectionValid();
      assertThat(repositoryConnectionValid).isTrue();

      // Both should be consistent since they use the same singleton
      assertThat(isOperational).isEqualTo(repositoryConnectionValid);
    }
  }

  @Nested
  @DisplayName("Data Persistence Integration Tests")
  class DataPersistenceIntegrationTests {

    @Test
    @DisplayName("Should persist data across service method calls")
    void shouldPersistDataAcrossServiceMethodCalls() {
      // Create user through service
      Cliente createdUser = userService.createUser("persistent_user", "persistent@example.com");
      Long userId = createdUser.getId();

      // Perform operations through different service method calls
      userService.updateUser(userId, "updated_name", null);
      userService.deactivateUser(userId);
      userService.activateUser(userId);

      // Verify data persistence
      Cliente finalUser = userService.getUserById(userId);
      assertThat(finalUser.getUsername()).isEqualTo("updated_name");
      assertThat(finalUser.getEmail()).isEqualTo("persistent@example.com");
      assertThat(finalUser.isActive()).isTrue();

      // Verify through direct repository access
      Optional<Cliente> repositoryUser = userRepository.findById(userId);
      assertThat(repositoryUser).isPresent();
      assertThat(repositoryUser.get()).isEqualTo(finalUser);
    }

    @Test
    @DisplayName("Should maintain data integrity across multiple sessions")
    void shouldMaintainDataIntegrityAcrossMultipleSessions() {
      // Session 1: Create and modify users
      Cliente user1 = userService.createUser("session_user1", "session1@example.com");
      Cliente user2 = userService.createUser("session_user2", "session2@example.com");
      userService.deactivateUser(user2.getId());

      // Simulate new session with new service instance but same repository
      ClienteService newServiceSession = new ClienteService(userRepository);

      // Session 2: Verify data from previous session
      Cliente retrievedUser1 = newServiceSession.getUserById(user1.getId());
      Cliente retrievedUser2 = newServiceSession.getUserById(user2.getId());

      assertThat(retrievedUser1.getUsername()).isEqualTo("session_user1");
      assertThat(retrievedUser1.isActive()).isTrue();

      assertThat(retrievedUser2.getUsername()).isEqualTo("session_user2");
      assertThat(retrievedUser2.isActive()).isFalse();

      // Session 2: Perform additional operations
      newServiceSession.updateUser(user1.getId(), "session_updated", null);
      newServiceSession.activateUser(user2.getId());

      // Verify changes are reflected in original service
      Cliente updatedUser1 = userService.getUserById(user1.getId());
      Cliente reactivatedUser2 = userService.getUserById(user2.getId());

      assertThat(updatedUser1.getUsername()).isEqualTo("session_updated");
      assertThat(reactivatedUser2.isActive()).isTrue();
    }
  }
}