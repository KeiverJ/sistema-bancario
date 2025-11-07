package unit;

import com.example.model.Cliente;
import com.example.repository.ClienteRepository;
import com.example.service.ClienteService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.TestDataFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService demonstrating advanced Mockito features
 * and comprehensive service layer testing patterns.
 */
@DisplayName("UserService Unit Tests - Advanced Mockito Demo")
@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

  @Mock
  private ClienteRepository mockRepository;

  @InjectMocks
  private ClienteService userService;

  @Nested
  @DisplayName("User Creation Tests")
  class UserCreationTests {

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
      // Arrange
      String username = "john_doe";
      String email = "john@example.com";
      Cliente expectedUser = TestDataFactory.createUserWithId(1L, username, email);

      when(mockRepository.existsByUsername(username)).thenReturn(false);
      when(mockRepository.existsByEmail(email)).thenReturn(false);
      when(mockRepository.save(any(Cliente.class))).thenReturn(expectedUser);

      // Act
      Cliente createdUser = userService.createUser(username, email);

      // Assert
      assertThat(createdUser).isNotNull();
      assertThat(createdUser.getId()).isEqualTo(1L);
      assertThat(createdUser.getUsername()).isEqualTo(username);
      assertThat(createdUser.getEmail()).isEqualTo(email);

      // Verify interactions
      verify(mockRepository).existsByUsername(username);
      verify(mockRepository).existsByEmail(email);
      verify(mockRepository)
          .save(argThat(user -> user.getUsername().equals(username) && user.getEmail().equals(email)));
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
      // Arrange
      String username = "existing_user";
      String email = "new@example.com";

      when(mockRepository.existsByUsername(username)).thenReturn(true);

      // Act & Assert
      assertThatThrownBy(() -> userService.createUser(username, email))
          .isInstanceOf(ClienteService.UserServiceException.class)
          .hasMessage("Username is already taken: " + username);

      // Verify that repository save was never called
      verify(mockRepository).existsByUsername(username);
      verify(mockRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
      // Arrange
      String username = "new_user";
      String email = "existing@example.com";

      when(mockRepository.existsByUsername(username)).thenReturn(false);
      when(mockRepository.existsByEmail(email)).thenReturn(true);

      // Act & Assert
      assertThatThrownBy(() -> userService.createUser(username, email))
          .isInstanceOf(ClienteService.UserServiceException.class)
          .hasMessage("Email is already registered: " + email);

      verify(mockRepository).existsByUsername(username);
      verify(mockRepository).existsByEmail(email);
      verify(mockRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Should validate username requirements")
    void shouldValidateUsernameRequirements() {
      assertThatThrownBy(() -> userService.createUser(null, "test@example.com"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Username cannot be null or empty");

      assertThatThrownBy(() -> userService.createUser("", "test@example.com"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Username cannot be null or empty");

      assertThatThrownBy(() -> userService.createUser("ab", "test@example.com"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Username must be at least 3 characters long");

      assertThatThrownBy(() -> userService.createUser("a".repeat(51), "test@example.com"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Username cannot be longer than 50 characters");

      assertThatThrownBy(() -> userService.createUser("user@invalid", "test@example.com"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Username can only contain letters, numbers, underscores, and hyphens");
    }

    @Test
    @DisplayName("Should validate email requirements")
    void shouldValidateEmailRequirements() {
      assertThatThrownBy(() -> userService.createUser("validuser", null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Email cannot be null or empty");

      assertThatThrownBy(() -> userService.createUser("validuser", ""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Email cannot be null or empty");

      assertThatThrownBy(() -> userService.createUser("validuser", "invalid-email"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Invalid email format");

      assertThatThrownBy(() -> userService.createUser("validuser", "a".repeat(250) + "@example.com"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Email cannot be longer than 255 characters");
    }
  }

  @Nested
  @DisplayName("User Retrieval Tests")
  class UserRetrievalTests {

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserByIdSuccessfully() {
      // Arrange
      Long userId = 1L;
      Cliente expectedUser = TestDataFactory.createUserWithId(userId, "john", "john@example.com");

      when(mockRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

      // Act
      Cliente foundUser = userService.getUserById(userId);

      // Assert
      assertThat(foundUser).isEqualTo(expectedUser);
      verify(mockRepository).findById(userId);
    }

    @Test
    @DisplayName("Should throw exception when user not found by ID")
    void shouldThrowExceptionWhenUserNotFoundById() {
      // Arrange
      Long userId = 999L;
      when(mockRepository.findById(userId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> userService.getUserById(userId))
          .isInstanceOf(ClienteService.UserServiceException.class)
          .hasMessage("User not found with ID: " + userId);

      verify(mockRepository).findById(userId);
    }

    @Test
    @DisplayName("Should find user by username")
    void shouldFindUserByUsername() {
      // Arrange
      String username = "john_doe";
      Cliente expectedUser = TestDataFactory.createUser(username, "john@example.com");

      when(mockRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

      // Act
      Optional<Cliente> foundUser = userService.findUserByUsername(username);

      // Assert
      assertThat(foundUser).isPresent();
      assertThat(foundUser.get()).isEqualTo(expectedUser);
      verify(mockRepository).findByUsername(username);
    }

    @Test
    @DisplayName("Should return empty when username not found")
    void shouldReturnEmptyWhenUsernameNotFound() {
      // Arrange
      String username = "nonexistent";
      when(mockRepository.findByUsername(username)).thenReturn(Optional.empty());

      // Act
      Optional<Cliente> foundUser = userService.findUserByUsername(username);

      // Assert
      assertThat(foundUser).isEmpty();
      verify(mockRepository).findByUsername(username);
    }

    @Test
    @DisplayName("Should handle null and empty username in find operations")
    void shouldHandleNullAndEmptyUsernameInFindOperations() {
      // Act & Assert
      assertThat(userService.findUserByUsername(null)).isEmpty();
      assertThat(userService.findUserByUsername("")).isEmpty();
      assertThat(userService.findUserByUsername("   ")).isEmpty();

      // Verify repository was never called
      verifyNoInteractions(mockRepository);
    }
  }

  @Nested
  @DisplayName("User Update Tests")
  class UserUpdateTests {

    @Test
    @DisplayName("Should update username successfully")
    void shouldUpdateUsernameSuccessfully() {
      // Arrange
      Long userId = 1L;
      String newUsername = "new_username";
      Cliente existingUser = TestDataFactory.createUserWithId(userId, "old_username", "user@example.com");
      Cliente updatedUser = TestDataFactory.createUserWithId(userId, newUsername, "user@example.com");

      when(mockRepository.findById(userId)).thenReturn(Optional.of(existingUser));
      when(mockRepository.findByUsername(newUsername)).thenReturn(Optional.empty());
      when(mockRepository.update(any(Cliente.class))).thenReturn(updatedUser);

      // Act
      Cliente result = userService.updateUser(userId, newUsername, null);

      // Assert
      assertThat(result.getUsername()).isEqualTo(newUsername);
      verify(mockRepository).findById(userId);
      verify(mockRepository).findByUsername(newUsername);
      verify(mockRepository).update(argThat(user -> user.getUsername().equals(newUsername)));
    }

    @Test
    @DisplayName("Should update email successfully")
    void shouldUpdateEmailSuccessfully() {
      // Arrange
      Long userId = 1L;
      String newEmail = "new@example.com";
      Cliente existingUser = TestDataFactory.createUserWithId(userId, "username", "old@example.com");

      when(mockRepository.findById(userId)).thenReturn(Optional.of(existingUser));
      when(mockRepository.findByEmail(newEmail)).thenReturn(Optional.empty());
      when(mockRepository.update(any(Cliente.class))).thenReturn(existingUser);

      // Act
      userService.updateUser(userId, null, newEmail);

      // Assert
      verify(mockRepository).findById(userId);
      verify(mockRepository).findByEmail(newEmail);
      verify(mockRepository).update(argThat(user -> user.getEmail().equals(newEmail)));
    }

    @Test
    @DisplayName("Should throw exception when updating to existing username")
    void shouldThrowExceptionWhenUpdatingToExistingUsername() {
      // Arrange
      Long userId = 1L;
      String existingUsername = "taken_username";
      Cliente currentUser = TestDataFactory.createUserWithId(userId, "current_username", "user@example.com");
      Cliente otherUser = TestDataFactory.createUserWithId(2L, existingUsername, "other@example.com");

      when(mockRepository.findById(userId)).thenReturn(Optional.of(currentUser));
      when(mockRepository.findByUsername(existingUsername)).thenReturn(Optional.of(otherUser));

      // Act & Assert
      assertThatThrownBy(() -> userService.updateUser(userId, existingUsername, null))
          .isInstanceOf(ClienteService.UserServiceException.class)
          .hasMessage("Username is already taken: " + existingUsername);

      verify(mockRepository).findById(userId);
      verify(mockRepository).findByUsername(existingUsername);
      verify(mockRepository, never()).update(any(Cliente.class));
    }
  }

  @Nested
  @DisplayName("User State Management Tests")
  class UserStateManagementTests {

    @Test
    @DisplayName("Should deactivate user successfully")
    void shouldDeactivateUserSuccessfully() {
      // Arrange
      Long userId = 1L;
      Cliente user = TestDataFactory.createUserWithId(userId, "username", "user@example.com");

      when(mockRepository.findById(userId)).thenReturn(Optional.of(user));
      when(mockRepository.update(any(Cliente.class))).thenReturn(user);

      // Act
      Cliente deactivatedUser = userService.deactivateUser(userId);

      // Assert
      assertThat(deactivatedUser.isActive()).isFalse();
      verify(mockRepository).findById(userId);
      verify(mockRepository).update(argThat(u -> !u.isActive()));
    }

    @Test
    @DisplayName("Should activate user successfully")
    void shouldActivateUserSuccessfully() {
      // Arrange
      Long userId = 1L;
      Cliente inactiveUser = TestDataFactory.createInactiveUser();
      inactiveUser.setId(userId);

      when(mockRepository.findById(userId)).thenReturn(Optional.of(inactiveUser));
      when(mockRepository.update(any(Cliente.class))).thenReturn(inactiveUser);

      // Act
      Cliente activatedUser = userService.activateUser(userId);

      // Assert
      assertThat(activatedUser.isActive()).isTrue();
      verify(mockRepository).findById(userId);
      verify(mockRepository).update(argThat(Cliente::isActive));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
      // Arrange
      Long userId = 1L;
      Cliente user = TestDataFactory.createUserWithId(userId, "username", "user@example.com");

      when(mockRepository.findById(userId)).thenReturn(Optional.of(user));
      when(mockRepository.deleteById(userId)).thenReturn(true);

      // Act
      userService.deleteUser(userId);

      // Assert
      verify(mockRepository).findById(userId);
      verify(mockRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw exception when delete fails")
    void shouldThrowExceptionWhenDeleteFails() {
      // Arrange
      Long userId = 1L;
      Cliente user = TestDataFactory.createUserWithId(userId, "username", "user@example.com");

      when(mockRepository.findById(userId)).thenReturn(Optional.of(user));
      when(mockRepository.deleteById(userId)).thenReturn(false);

      // Act & Assert
      assertThatThrownBy(() -> userService.deleteUser(userId))
          .isInstanceOf(ClienteService.UserServiceException.class)
          .hasMessage("Failed to delete user: Failed to delete user with ID: " + userId);

      verify(mockRepository).findById(userId);
      verify(mockRepository).deleteById(userId);
    }
  }

  @Nested
  @DisplayName("Bulk Operations Tests")
  class BulkOperationsTests {

    @Test
    @DisplayName("Should get all users")
    void shouldGetAllUsers() {
      // Arrange
      List<Cliente> expectedUsers = Arrays.asList(
          TestDataFactory.createUserWithId(1L, "user1", "user1@example.com"),
          TestDataFactory.createUserWithId(2L, "user2", "user2@example.com"));

      when(mockRepository.findAll()).thenReturn(expectedUsers);

      // Act
      List<Cliente> users = userService.getAllUsers();

      // Assert
      assertThat(users).hasSize(2);
      assertThat(users).containsExactlyElementsOf(expectedUsers);
      verify(mockRepository).findAll();
    }

    @Test
    @DisplayName("Should get all active users")
    void shouldGetAllActiveUsers() {
      // Arrange
      List<Cliente> activeUsers = Arrays.asList(
          TestDataFactory.createUserWithId(1L, "active1", "active1@example.com"),
          TestDataFactory.createUserWithId(2L, "active2", "active2@example.com"));

      when(mockRepository.findAllActive()).thenReturn(activeUsers);

      // Act
      List<Cliente> users = userService.getAllActiveUsers();

      // Assert
      assertThat(users).hasSize(2);
      assertThat(users).allMatch(Cliente::isActive);
      verify(mockRepository).findAllActive();
    }

    @Test
    @DisplayName("Should get user counts")
    void shouldGetUserCounts() {
      // Arrange
      when(mockRepository.count()).thenReturn(10L);
      when(mockRepository.countActive()).thenReturn(8L);

      // Act
      long totalCount = userService.getTotalUserCount();
      long activeCount = userService.getActiveUserCount();

      // Assert
      assertThat(totalCount).isEqualTo(10L);
      assertThat(activeCount).isEqualTo(8L);
      verify(mockRepository).count();
      verify(mockRepository).countActive();
    }
  }

  @Nested
  @DisplayName("Service Health Tests")
  class ServiceHealthTests {

    @Test
    @DisplayName("Should check service operational status")
    void shouldCheckServiceOperationalStatus() {
      // Arrange
      when(mockRepository.isConnectionValid()).thenReturn(true);

      // Act
      boolean isOperational = userService.isServiceOperational();

      // Assert
      assertThat(isOperational).isTrue();
      verify(mockRepository).isConnectionValid();
    }

    @Test
    @DisplayName("Should handle service health check exceptions")
    void shouldHandleServiceHealthCheckExceptions() {
      // Arrange
      when(mockRepository.isConnectionValid()).thenThrow(new RuntimeException("Connection failed"));

      // Act
      boolean isOperational = userService.isServiceOperational();

      // Assert
      assertThat(isOperational).isFalse();
      verify(mockRepository).isConnectionValid();
    }
  }

  @Nested
  @DisplayName("Exception Handling Tests")
  class ExceptionHandlingTests {

    @Test
    @DisplayName("Should wrap repository exceptions in service exceptions")
    void shouldWrapRepositoryExceptionsInServiceExceptions() {
      // Arrange
      String username = "testuser";
      String email = "test@example.com";

      when(mockRepository.existsByUsername(username)).thenReturn(false);
      when(mockRepository.existsByEmail(email)).thenReturn(false);
      when(mockRepository.save(any(Cliente.class))).thenThrow(new RuntimeException("Database error"));

      // Act & Assert
      assertThatThrownBy(() -> userService.createUser(username, email))
          .isInstanceOf(ClienteService.UserServiceException.class)
          .hasMessage("Failed to create user: Database error")
          .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should handle null ID validation")
    void shouldHandleNullIdValidation() {
      assertThatThrownBy(() -> userService.getUserById(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("User ID cannot be null");

      verifyNoInteractions(mockRepository);
    }
  }

  @Nested
  @DisplayName("Advanced Mockito Features")
  class AdvancedMockitoFeaturesTests {

    @Test
    @DisplayName("Should use argument matchers")
    void shouldUseArgumentMatchers() {
      // Arrange
      when(mockRepository.save(argThat(user -> user.getUsername().startsWith("test") &&
          user.getEmail().contains("@example.com"))))
          .thenReturn(TestDataFactory.createUserWithId(1L, "testuser", "test@example.com"));

      when(mockRepository.existsByUsername(startsWith("test"))).thenReturn(false);
      when(mockRepository.existsByEmail(contains("@example.com"))).thenReturn(false);

      // Act
      Cliente createdUser = userService.createUser("testuser", "test@example.com");

      // Assert
      assertThat(createdUser.getId()).isEqualTo(1L);
      verify(mockRepository).save(argThat(user -> user.getUsername().equals("testuser")));
    }

    @Test
    @DisplayName("Should verify method call order")
    void shouldVerifyMethodCallOrder() {
      // Arrange
      Long userId = 1L;
      Cliente user = TestDataFactory.createUserWithId(userId, "username", "user@example.com");

      when(mockRepository.findById(userId)).thenReturn(Optional.of(user));
      when(mockRepository.deleteById(userId)).thenReturn(true);

      // Act
      userService.deleteUser(userId);

      // Assert - verify order of method calls
      var inOrder = inOrder(mockRepository);
      inOrder.verify(mockRepository).findById(userId);
      inOrder.verify(mockRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Should verify exact number of interactions")
    void shouldVerifyExactNumberOfInteractions() {
      // Arrange
      List<Cliente> users = Arrays.asList(
          TestDataFactory.createUser("user1", "user1@example.com"),
          TestDataFactory.createUser("user2", "user2@example.com"));

      when(mockRepository.findAll()).thenReturn(users);

      // Act
      userService.getAllUsers();
      userService.getAllUsers();

      // Assert
      verify(mockRepository, times(2)).findAll();
      verifyNoMoreInteractions(mockRepository);
    }
  }
}