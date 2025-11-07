package e2e;

import com.example.BankingSystemApp;
import com.example.config.databaseConfig;
import com.example.model.Cliente;
import com.example.repository.ClienteRepository;
import com.example.service.ClienteService;
import org.junit.jupiter.api.*;
import util.TestDataFactory;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.*;

/**
 * End-to-End tests that test the complete application flow.
 * These tests verify the entire system working together from
 * user input to data persistence.
 * 
 * E2E tests:
 * - Test complete user workflows
 * - Use real components throughout the stack
 * - Simulate actual user interactions
 * - Verify system behavior as a whole
 */
@DisplayName("Terminal Application End-to-End Tests")
class TerminalAppE2ETest {

  private ClienteService userService;
  private ClienteRepository userRepository;
  private databaseConfig databaseConfig;

  @BeforeEach
  void setUp() {
    // Initialize the complete application stack
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
  @DisplayName("Application Initialization E2E Tests")
  class ApplicationInitializationE2ETests {

    @Test
    @DisplayName("Should initialize application with singleton pattern")
    void shouldInitializeApplicationWithSingletonPattern() {
      // Test complete application initialization
      databaseConfig config1 = databaseConfig.getInstance();
      ClienteRepository repo = new ClienteRepository();
      ClienteService service = new ClienteService(repo);

      // Verify singleton behavior throughout the stack
      databaseConfig config2 = repo.getDatabaseConfig();
      databaseConfig config3 = databaseConfig.getInstance();

      assertThat(config1)
          .isSameAs(config2)
          .isSameAs(config3);

      // Verify application is operational
      assertThat(service.isServiceOperational()).isTrue();
      assertThat(repo.isConnectionValid()).isTrue();
    }

    @Test
    @DisplayName("Should create TerminalApp with all dependencies")
    void shouldCreateTerminalAppWithAllDependencies() {
      // Test that TerminalApp can be instantiated with all real dependencies
      assertThatCode(() -> {
        BankingSystemApp app = new BankingSystemApp();
        // App should initialize without throwing exceptions
      }).doesNotThrowAnyException();

      // Verify sample data was created during initialization
      List<Cliente> users = userRepository.findAll();
      assertThat(users).isNotEmpty();

      // Verify specific sample users
      assertThat(userRepository.existsByUsername("john_doe")).isTrue();
      assertThat(userRepository.existsByUsername("jane_smith")).isTrue();
      assertThat(userRepository.existsByUsername("admin")).isTrue();
    }
  }

  @Nested
  @DisplayName("Complete User Management Workflows E2E")
  class CompleteUserManagementWorkflowsE2ETests {

    @Test
    @DisplayName("Should complete full user creation workflow")
    void shouldCompleteFullUserCreationWorkflow() {
      // Simulate complete user creation process
      String username = "e2e_user";
      String email = "e2e@example.com";

      // Step 1: Validate input (business logic)
      assertThatCode(() -> {
        if (username.length() < 3) {
          throw new IllegalArgumentException("Username too short");
        }
        if (!email.contains("@")) {
          throw new IllegalArgumentException("Invalid email");
        }
      }).doesNotThrowAnyException();

      // Step 2: Check for duplicates
      boolean usernameExists = userService.findUserByUsername(username).isPresent();
      boolean emailExists = userService.findUserByEmail(email).isPresent();

      assertThat(usernameExists).isFalse();
      assertThat(emailExists).isFalse();

      // Step 3: Create user
      Cliente createdUser = userService.createUser(username, email);

      // Step 4: Verify creation at all levels
      assertThat(createdUser.getId()).isNotNull();
      assertThat(createdUser.getUsername()).isEqualTo(username);
      assertThat(createdUser.getEmail()).isEqualTo(email);
      assertThat(createdUser.isActive()).isTrue();

      // Step 5: Verify persistence
      Cliente retrievedUser = userService.getUserById(createdUser.getId());
      assertThat(retrievedUser).isEqualTo(createdUser);

      // Step 6: Verify in repository
      assertThat(userRepository.existsByUsername(username)).isTrue();
      assertThat(userRepository.existsByEmail(email)).isTrue();
    }

    @Test
    @DisplayName("Should complete full user update workflow")
    void shouldCompleteFullUserUpdateWorkflow() {
      // Setup: Create initial user
      Cliente originalUser = userService.createUser("original_user", "original@example.com");
      Long userId = originalUser.getId();

      // Complete update workflow
      String newUsername = "updated_user";
      String newEmail = "updated@example.com";

      // Step 1: Validate new data
      assertThat(newUsername).hasSize(12); // Valid length
      assertThat(newEmail).contains("@").contains(".");

      // Step 2: Check for conflicts
      assertThat(userService.findUserByUsername(newUsername)).isEmpty();
      assertThat(userService.findUserByEmail(newEmail)).isEmpty();

      // Step 3: Perform update
      Cliente updatedUser = userService.updateUser(userId, newUsername, newEmail);

      // Step 4: Verify update at all levels
      assertThat(updatedUser.getId()).isEqualTo(userId);
      assertThat(updatedUser.getUsername()).isEqualTo(newUsername);
      assertThat(updatedUser.getEmail()).isEqualTo(newEmail);

      // Step 5: Verify old data is replaced
      assertThat(userService.findUserByUsername("original_user")).isEmpty();
      assertThat(userService.findUserByEmail("original@example.com")).isEmpty();

      // Step 6: Verify new data is findable
      assertThat(userService.findUserByUsername(newUsername)).isPresent();
      assertThat(userService.findUserByEmail(newEmail)).isPresent();
    }

    @Test
    @DisplayName("Should complete full user lifecycle workflow")
    void shouldCompleteFullUserLifecycleWorkflow() {
      // Complete lifecycle: Create -> Update -> Deactivate -> Reactivate -> Delete

      // Phase 1: Creation
      Cliente user = userService.createUser("lifecycle_user", "lifecycle@example.com");
      Long userId = user.getId();
      assertThat(userService.getTotalUserCount()).isGreaterThan(0);

      // Phase 2: Update
      Cliente updatedUser = userService.updateUser(userId, "updated_lifecycle", null);
      assertThat(updatedUser.getUsername()).isEqualTo("updated_lifecycle");

      // Phase 3: Deactivation
      Cliente deactivatedUser = userService.deactivateUser(userId);
      assertThat(deactivatedUser.isActive()).isFalse();
      assertThat(userService.getAllActiveUsers()).doesNotContain(deactivatedUser);

      // Phase 4: Reactivation
      Cliente reactivatedUser = userService.activateUser(userId);
      assertThat(reactivatedUser.isActive()).isTrue();
      assertThat(userService.getAllActiveUsers()).contains(reactivatedUser);

      // Phase 5: Deletion
      long countBeforeDelete = userService.getTotalUserCount();
      userService.deleteUser(userId);

      assertThat(userService.getTotalUserCount()).isEqualTo(countBeforeDelete - 1);
      assertThatThrownBy(() -> userService.getUserById(userId))
          .isInstanceOf(ClienteService.UserServiceException.class);
    }
  }

  @Nested
  @DisplayName("Multi-User Scenarios E2E")
  class MultiUserScenariosE2ETests {

    @Test
    @DisplayName("Should handle multiple users with complex interactions")
    void shouldHandleMultipleUsersWithComplexInteractions() {
      // Create multiple users with different characteristics
      Cliente admin = userService.createUser("admin_user", "admin@company.com");
      Cliente regularUser = userService.createUser("regular_user", "user@company.com");
      Cliente tempUser = userService.createUser("temp_user", "temp@company.com");

      // Verify initial state
      assertThat(userService.getTotalUserCount()).isEqualTo(3);
      assertThat(userService.getActiveUserCount()).isEqualTo(3);

      // Perform various operations
      userService.deactivateUser(tempUser.getId());
      userService.updateUser(regularUser.getId(), "updated_regular", null);

      // Verify intermediate state
      assertThat(userService.getTotalUserCount()).isEqualTo(3);
      assertThat(userService.getActiveUserCount()).isEqualTo(2);

      // Perform search operations
      assertThat(userService.findUserByUsername("admin_user")).isPresent();
      assertThat(userService.findUserByUsername("updated_regular")).isPresent();
      assertThat(userService.findUserByUsername("regular_user")).isEmpty();

      // Verify temp user is inactive but still exists
      Cliente foundTempUser = userService.getUserById(tempUser.getId());
      assertThat(foundTempUser.isActive()).isFalse();

      // Final cleanup and verification
      userService.deleteUser(tempUser.getId());
      assertThat(userService.getTotalUserCount()).isEqualTo(2);

      List<Cliente> finalUsers = userService.getAllUsers();
      assertThat(finalUsers)
          .hasSize(2)
          .allMatch(Cliente::isActive);
    }

    @Test
    @DisplayName("Should handle bulk operations with validation")
    void shouldHandleBulkOperationsWithValidation() {
      // Create multiple users for bulk testing
      Cliente[] testUsers = TestDataFactory.createMultipleUsers(10);

      // Bulk creation with validation
      for (Cliente user : testUsers) {
        assertThat(user.getUsername()).hasSizeGreaterThanOrEqualTo(3);
        assertThat(user.getEmail()).contains("@");
        userService.createUser(user.getUsername(), user.getEmail());
      }

      // Verify bulk creation
      assertThat(userService.getTotalUserCount()).isEqualTo(10);
      assertThat(userService.getActiveUserCount()).isEqualTo(10);

      // Bulk deactivation of even-numbered users
      List<Cliente> allUsers = userService.getAllUsers();
      for (int i = 0; i < allUsers.size(); i += 2) {
        userService.deactivateUser(allUsers.get(i).getId());
      }

      // Verify bulk deactivation
      assertThat(userService.getTotalUserCount()).isEqualTo(10);
      assertThat(userService.getActiveUserCount()).isEqualTo(5);

      // Bulk deletion of inactive users
      List<Cliente> inactiveUsers = userService.getAllUsers().stream()
          .filter(user -> !user.isActive())
          .toList();

      for (Cliente inactiveUser : inactiveUsers) {
        userService.deleteUser(inactiveUser.getId());
      }

      // Final verification
      assertThat(userService.getTotalUserCount()).isEqualTo(5);
      assertThat(userService.getActiveUserCount()).isEqualTo(5);
      assertThat(userService.getAllUsers()).allMatch(Cliente::isActive);
    }
  }

  @Nested
  @DisplayName("Error Handling E2E Scenarios")
  class ErrorHandlingE2EScenariosTests {

    @Test
    @DisplayName("Should handle complete error scenarios gracefully")
    void shouldHandleCompleteErrorScenariosGracefully() {
      // Setup valid user
      Cliente validUser = userService.createUser("valid_user", "valid@example.com");

      // Test duplicate username error flow
      assertThatThrownBy(() -> userService.createUser("valid_user", "different@example.com"))
          .isInstanceOf(ClienteService.UserServiceException.class)
          .hasMessage("Username is already taken: valid_user");

      // Test duplicate email error flow
      assertThatThrownBy(() -> userService.createUser("different_user", "valid@example.com"))
          .isInstanceOf(ClienteService.UserServiceException.class)
          .hasMessage("Email is already registered: valid@example.com");

      // Test non-existent user operations
      assertThatThrownBy(() -> userService.getUserById(999L))
          .isInstanceOf(ClienteService.UserServiceException.class)
          .hasMessage("User not found with ID: 999");

      assertThatThrownBy(() -> userService.updateUser(999L, "new_name", null))
          .isInstanceOf(ClienteService.UserServiceException.class)
          .hasMessage("User not found with ID: 999");

      // Verify original user is unaffected by error scenarios
      Cliente unchangedUser = userService.getUserById(validUser.getId());
      assertThat(unchangedUser.getUsername()).isEqualTo("valid_user");
      assertThat(unchangedUser.getEmail()).isEqualTo("valid@example.com");
      assertThat(unchangedUser.isActive()).isTrue();
    }

    @Test
    @DisplayName("Should maintain data consistency during error conditions")
    void shouldMaintainDataConsistencyDuringErrorConditions() {
      // Create baseline data
      userService.createUser("user1", "user1@example.com");
      userService.createUser("user2", "user2@example.com");
      long initialCount = userService.getTotalUserCount();

      // Attempt invalid operations
      try {
        userService.createUser("", "invalid@example.com");
      } catch (IllegalArgumentException e) {
        // Expected
      }

      try {
        userService.createUser("user1", "duplicate@example.com");
      } catch (ClienteService.UserServiceException e) {
        // Expected
      }

      try {
        userService.updateUser(999L, "nonexistent", null);
      } catch (ClienteService.UserServiceException e) {
        // Expected
      }

      // Verify data consistency maintained
      assertThat(userService.getTotalUserCount()).isEqualTo(initialCount);
      assertThat(userService.getActiveUserCount()).isEqualTo(initialCount);

      // Verify original users are intact
      assertThat(userService.findUserByUsername("user1")).isPresent();
      assertThat(userService.findUserByUsername("user2")).isPresent();
      assertThat(userService.findUserByEmail("user1@example.com")).isPresent();
      assertThat(userService.findUserByEmail("user2@example.com")).isPresent();
    }
  }

  @Nested
  @DisplayName("System Integration E2E Tests")
  class SystemIntegrationE2ETests {

    @Test
    @DisplayName("Should verify complete system health and configuration")
    void shouldVerifyCompleteSystemHealthAndConfiguration() {
      // Test singleton configuration across entire system
      databaseConfig appConfig = databaseConfig.getInstance();

      // Verify configuration properties
      assertThat(appConfig.getDatabaseUrl()).isEqualTo("jdbc:h2:mem:testdb");
      assertThat(appConfig.getUsername()).isEqualTo("sa");
      assertThat(appConfig.getMaxConnections()).isEqualTo(10);
      assertThat(appConfig.isValidConfiguration()).isTrue();
      assertThat(appConfig.isProductionEnvironment()).isFalse();

      // Test system operational status
      assertThat(userService.isServiceOperational()).isTrue();
      assertThat(userRepository.isConnectionValid()).isTrue();

      // Test configuration consistency across components
      assertThat(userRepository.getDatabaseConfig()).isSameAs(appConfig);

      // Test system can handle load
      for (int i = 0; i < 50; i++) {
        userService.createUser("load_test_" + i, "load" + i + "@example.com");
      }

      assertThat(userService.getTotalUserCount()).isEqualTo(50);
      assertThat(userService.isServiceOperational()).isTrue();
    }

    @Test
    @DisplayName("Should demonstrate complete application workflow")
    void shouldDemonstrateCompleteApplicationWorkflow() {
      // Simulate a complete business workflow

      // 1. System startup - verify initialization
      assertThat(userService.isServiceOperational()).isTrue();
      long initialUsers = userService.getTotalUserCount();

      // 2. User registration phase
      Cliente newUser = userService.createUser("business_user", "business@company.com");
      assertThat(userService.getTotalUserCount()).isEqualTo(initialUsers + 1);

      // 3. User profile update phase
      Cliente updatedUser = userService.updateUser(
          newUser.getId(),
          "business_user_updated",
          "updated.business@company.com");
      assertThat(updatedUser.getUsername()).isEqualTo("business_user_updated");

      // 4. User status management phase
      userService.deactivateUser(newUser.getId());
      assertThat(userService.getActiveUserCount()).isEqualTo(initialUsers);

      // 5. User reactivation phase
      userService.activateUser(newUser.getId());
      assertThat(userService.getActiveUserCount()).isEqualTo(initialUsers + 1);

      // 6. Reporting phase
      List<Cliente> allUsers = userService.getAllUsers();
      List<Cliente> activeUsers = userService.getAllActiveUsers();

      assertThat(allUsers).hasSize((int) (initialUsers + 1));
      assertThat(activeUsers).hasSize((int) (initialUsers + 1));

      // 7. Data verification phase
      Cliente finalUser = userService.getUserById(newUser.getId());
      assertThat(finalUser.getUsername()).isEqualTo("business_user_updated");
      assertThat(finalUser.getEmail()).isEqualTo("updated.business@company.com");
      assertThat(finalUser.isActive()).isTrue();

      // 8. Cleanup phase
      userService.deleteUser(newUser.getId());
      assertThat(userService.getTotalUserCount()).isEqualTo(initialUsers);
    }
  }

  @Nested
  @DisplayName("Application State Management E2E")
  class ApplicationStateManagementE2ETests {

    @Test
    @DisplayName("Should maintain state across multiple service instances")
    void shouldMaintainStateAcrossMultipleServiceInstances() {
      // Create data with first service instance
      Cliente user1 = userService.createUser("state_user1", "state1@example.com");
      Cliente user2 = userService.createUser("state_user2", "state2@example.com");

      // Create new service instance (simulating app restart)
      ClienteRepository newRepository = new ClienteRepository(databaseConfig.getInstance());
      ClienteService newService = new ClienteService(newRepository);

      // Verify state persistence
      assertThat(newService.getTotalUserCount()).isEqualTo(2);

      Cliente retrievedUser1 = newService.getUserById(user1.getId());
      Cliente retrievedUser2 = newService.getUserById(user2.getId());

      assertThat(retrievedUser1.getUsername()).isEqualTo("state_user1");
      assertThat(retrievedUser2.getUsername()).isEqualTo("state_user2");

      // Perform operations with new service
      newService.updateUser(user1.getId(), "state_updated", null);
      newService.deactivateUser(user2.getId());

      // Verify changes in original service
      Cliente updatedUser1 = userService.getUserById(user1.getId());
      Cliente deactivatedUser2 = userService.getUserById(user2.getId());

      assertThat(updatedUser1.getUsername()).isEqualTo("state_updated");
      assertThat(deactivatedUser2.isActive()).isFalse();
    }
  }
}