package unit;

import com.example.config.databaseConfig;
import com.example.model.Cliente;
import com.example.repository.ClienteRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import util.TestDataFactory;
import util.TestDoubles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserRepository demonstrating all types of Test Doubles:
 * - MOCKS: Verify behavior and interactions (using Mockito)
 * - STUBS: Provide predefined responses
 * - SPIES: Partial mocking of real objects
 * - FAKES: Simple working implementation for testing
 * - DUMMIES: Objects passed around but never used
 */
@DisplayName("UserRepository Unit Tests - Test Doubles Demo")
@ExtendWith(MockitoExtension.class)
class UserRepositoryUnitTest {

  @Mock
  private databaseConfig mockDatabaseConfig;

  private ClienteRepository repository;

  @BeforeEach
  void setUp() {
    repository = new ClienteRepository();
  }

  @AfterEach
  void tearDown() {
    repository.deleteAll();
  }

  @Nested
  @DisplayName("Basic Repository Operations")
  class BasicRepositoryOperationsTests {

    @Test
    @DisplayName("Should save user successfully")
    void shouldSaveUserSuccessfully() {
      Cliente user = TestDataFactory.createBasicUser();

      Cliente savedUser = repository.save(user);

      assertThat(savedUser.getId()).isNotNull();
      assertThat(savedUser.getUsername()).isEqualTo("testuser");
      assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should find user by ID")
    void shouldFindUserById() {
      Cliente user = TestDataFactory.createBasicUser();
      Cliente savedUser = repository.save(user);

      Optional<Cliente> foundUser = repository.findById(savedUser.getId());

      assertThat(foundUser).isPresent();
      assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    @DisplayName("Should return empty when user not found by ID")
    void shouldReturnEmptyWhenUserNotFoundById() {
      Optional<Cliente> foundUser = repository.findById(999L);

      assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Should find user by username")
    void shouldFindUserByUsername() {
      Cliente user = TestDataFactory.createUser("john_doe", "john@example.com");
      repository.save(user);

      Optional<Cliente> foundUser = repository.findByUsername("john_doe");

      assertThat(foundUser).isPresent();
      assertThat(foundUser.get().getUsername()).isEqualTo("john_doe");
    }

    @Test
    @DisplayName("Should delete user by ID")
    void shouldDeleteUserById() {
      Cliente user = TestDataFactory.createBasicUser();
      Cliente savedUser = repository.save(user);

      boolean deleted = repository.deleteById(savedUser.getId());

      assertThat(deleted).isTrue();
      assertThat(repository.findById(savedUser.getId())).isEmpty();
    }
  }

  @Nested
  @DisplayName("Test Doubles - MOCKS Demo")
  class MocksTests {

    @Test
    @DisplayName("Should use mocked DatabaseConfig with Mockito")
    void shouldUseMockedDatabaseConfigWithMockito() {
      // Arrange
      when(mockDatabaseConfig.isValidConfiguration()).thenReturn(true);
      when(mockDatabaseConfig.getDatabaseUrl()).thenReturn("jdbc:mock:test");

      ClienteRepository mockedRepository = new ClienteRepository(mockDatabaseConfig);

      // Act
      boolean isValid = mockedRepository.isConnectionValid();
      databaseConfig config = mockedRepository.getDatabaseConfig();

      // Assert
      assertThat(isValid).isTrue();
      assertThat(config.getDatabaseUrl()).isEqualTo("jdbc:mock:test");

      // Verify interactions (this is what makes it a MOCK)
      verify(mockDatabaseConfig).isValidConfiguration();
      verify(mockDatabaseConfig).getDatabaseUrl();
    }

    @Test
    @DisplayName("Should verify no interactions when not expected")
    void shouldVerifyNoInteractionsWhenNotExpected() {
      // Arrange
      ClienteRepository mockedRepository = new ClienteRepository(mockDatabaseConfig);
      Cliente user = TestDataFactory.createBasicUser();

      // Act
      mockedRepository.save(user);

      // Assert - verify that DatabaseConfig was not used during save
      verifyNoInteractions(mockDatabaseConfig);
    }

    @Test
    @DisplayName("Should mock static method calls")
    void shouldMockStaticMethodCalls() {
      // Demonstrate mocking static singleton method
      try (MockedStatic<databaseConfig> mockedStatic = mockStatic(databaseConfig.class)) {
        databaseConfig mockConfig = mock(databaseConfig.class);
        when(mockConfig.isValidConfiguration()).thenReturn(false);
        mockedStatic.when(databaseConfig::getInstance).thenReturn(mockConfig);

        ClienteRepository repo = new ClienteRepository();
        boolean isValid = repo.isConnectionValid();

        assertThat(isValid).isFalse();
        mockedStatic.verify(databaseConfig::getInstance);
      }
    }
  }

  @Nested
  @DisplayName("Test Doubles - STUBS Demo")
  class StubsTests {

    @Test
    @DisplayName("Should use repository stub with predefined responses")
    void shouldUseRepositoryStubWithPredefinedResponses() {
      // Arrange
      TestDoubles.UserRepositoryStub stub = new TestDoubles.UserRepositoryStub();

      // Act & Assert - STUB provides predefined responses
      Optional<Cliente> user1 = stub.findByUsername("stub_user");
      Optional<Cliente> user2 = stub.findByUsername("another_user");
      Optional<Cliente> user3 = stub.findByUsername("nonexistent");

      assertThat(user1).isPresent();
      assertThat(user1.get().getUsername()).isEqualTo("stub_user");

      assertThat(user2).isPresent();
      assertThat(user2.get().getUsername()).isEqualTo("another_user");

      assertThat(user3).isEmpty();

      // STUBs always return the same response
      assertThat(stub.existsByUsername("stub_user")).isTrue();
      assertThat(stub.existsByUsername("nonexistent")).isFalse();
    }

    @Test
    @DisplayName("Should handle stub exceptions")
    void shouldHandleStubExceptions() {
      // Arrange
      TestDoubles.UserRepositoryStub stubWithException = new TestDoubles.UserRepositoryStub(true);

      // Act & Assert
      assertThatThrownBy(() -> stubWithException.findByUsername("any"))
          .isInstanceOf(RuntimeException.class)
          .hasMessage("Stub exception");

      assertThatThrownBy(() -> stubWithException.findById(1L))
          .isInstanceOf(RuntimeException.class)
          .hasMessage("Stub exception");
    }
  }

  @Nested
  @DisplayName("Test Doubles - SPIES Demo")
  class SpiesTests {

    @Test
    @DisplayName("Should use Mockito spy for partial mocking")
    void shouldUseMockitoSpyForPartialMocking() {
      // Arrange - Create a spy of real repository
      ClienteRepository repositorySpy = spy(new ClienteRepository());
      Cliente user = TestDataFactory.createBasicUser();

      // Act
      Cliente savedUser = repositorySpy.save(user);
      Optional<Cliente> foundUser = repositorySpy.findById(savedUser.getId());

      // Assert - SPY allows both real method calls and stubbing
      assertThat(foundUser).isPresent();

      // Verify real methods were called
      verify(repositorySpy).save(user);
      verify(repositorySpy).findById(savedUser.getId());
    }

    @Test
    @DisplayName("Should stub specific methods on spy while keeping others real")
    void shouldStubSpecificMethodsOnSpyWhileKeepingOthersReal() {
      // Arrange
      ClienteRepository repositorySpy = spy(new ClienteRepository());
      Cliente user = TestDataFactory.createBasicUser();

      // Stub only the existsByUsername method to return false for save to work
      when(repositorySpy.existsByUsername(anyString())).thenReturn(false);

      // Act - save uses real implementation, existsByUsername uses stub
      Cliente savedUser = repositorySpy.save(user);
      boolean exists = repositorySpy.existsByUsername("any_username");

      // Assert
      assertThat(savedUser.getId()).isNotNull(); // Real save worked
      assertThat(exists).isFalse(); // Stubbed method returned false

      verify(repositorySpy).save(user);
      verify(repositorySpy).existsByUsername("any_username");
    }

    @Test
    @DisplayName("Should spy on void methods")
    void shouldSpyOnVoidMethods() {
      // Arrange
      ClienteRepository repositorySpy = spy(new ClienteRepository());

      // Act
      repositorySpy.deleteAll();

      // Assert
      verify(repositorySpy).deleteAll();
      assertThat(repositorySpy.count()).isZero();
    }
  }

  @Nested
  @DisplayName("Test Doubles - FAKES Demo")
  class FakesTests {

    @Test
    @DisplayName("Should use fake repository implementation")
    void shouldUseFakeRepositoryImplementation() {
      // Arrange
      TestDoubles.FakeUserRepository fakeRepository = new TestDoubles.FakeUserRepository();
      Cliente user1 = TestDataFactory.createUser("user1", "user1@example.com");
      Cliente user2 = TestDataFactory.createUser("user2", "user2@example.com");

      // Act - FAKE has working implementation
      Cliente savedUser1 = fakeRepository.save(user1);
      Cliente savedUser2 = fakeRepository.save(user2);

      List<Cliente> allUsers = fakeRepository.findAll();
      Optional<Cliente> foundUser = fakeRepository.findByUsername("user1");

      // Assert - FAKE behaves like real implementation but is simpler
      assertThat(savedUser1.getId()).isNotNull();
      assertThat(savedUser2.getId()).isNotNull();
      assertThat(allUsers).hasSize(2);
      assertThat(foundUser).isPresent();
      assertThat(foundUser.get().getUsername()).isEqualTo("user1");
    }

    @Test
    @DisplayName("Should simulate failures in fake repository")
    void shouldSimulateFailuresInFakeRepository() {
      // Arrange
      TestDoubles.FakeUserRepository fakeRepository = new TestDoubles.FakeUserRepository();
      fakeRepository.setSimulateFailure(true);
      Cliente user = TestDataFactory.createBasicUser();

      // Act & Assert
      assertThatThrownBy(() -> fakeRepository.save(user))
          .isInstanceOf(RuntimeException.class)
          .hasMessage("Fake repository failure");

      assertThatThrownBy(() -> fakeRepository.findAll())
          .isInstanceOf(RuntimeException.class)
          .hasMessage("Fake repository failure");
    }

    @Test
    @DisplayName("Should demonstrate fake repository state management")
    void shouldDemonstrateFakeRepositoryStateManagement() {
      // Arrange
      TestDoubles.FakeUserRepository fakeRepository = new TestDoubles.FakeUserRepository();

      // Act
      Cliente user1 = fakeRepository.save(TestDataFactory.createUser("user1", "user1@example.com"));
      Cliente user2 = fakeRepository.save(TestDataFactory.createUser("user2", "user2@example.com"));

      assertThat(fakeRepository.size()).isEqualTo(2);

      fakeRepository.deleteById(user1.getId());

      // Assert
      assertThat(fakeRepository.size()).isEqualTo(1);
      assertThat(fakeRepository.findById(user1.getId())).isEmpty();
      assertThat(fakeRepository.findById(user2.getId())).isPresent();
    }
  }

  @Nested
  @DisplayName("Test Doubles - DUMMIES Demo")
  class DummiesTests {

    @Test
    @DisplayName("Should use dummy objects that are never called")
    void shouldUseDummyObjectsThatAreNeverCalled() {
      // Arrange - DUMMY is passed but never used
      TestDoubles.DummyDatabaseConfig dummyConfig = new TestDoubles.DummyDatabaseConfig();

      // This test simulates a scenario where we need to pass an object
      // but we know it won't be used in the specific test scenario

      // Act - Create repository but don't call methods that would use the config
      ClienteRepository repositoryWithDummy = new ClienteRepository();
      Cliente user = TestDataFactory.createBasicUser();

      // Methods that don't use DatabaseConfig should work fine
      Cliente savedUser = repositoryWithDummy.save(user);
      Optional<Cliente> foundUser = repositoryWithDummy.findById(savedUser.getId());

      // Assert
      assertThat(savedUser.getId()).isNotNull();
      assertThat(foundUser).isPresent();

      // DUMMY should never be called - if it is, it will throw
      // UnsupportedOperationException
      // This demonstrates that the dummy is truly not used
    }

    @Test
    @DisplayName("Should throw exception if dummy is accidentally used")
    void shouldThrowExceptionIfDummyIsAccidentallyUsed() {
      // Arrange
      TestDoubles.DummyDatabaseConfig dummyConfig = new TestDoubles.DummyDatabaseConfig();

      // Act & Assert - DUMMY throws exception when used
      assertThatThrownBy(() -> dummyConfig.getDatabaseUrl())
          .isInstanceOf(UnsupportedOperationException.class)
          .hasMessage("Dummy should not be used");

      assertThatThrownBy(() -> dummyConfig.getUsername())
          .isInstanceOf(UnsupportedOperationException.class)
          .hasMessage("Dummy should not be used");
    }
  }

  @Nested
  @DisplayName("Validation and Error Handling")
  class ValidationAndErrorHandlingTests {

    @Test
    @DisplayName("Should throw exception when saving null user")
    void shouldThrowExceptionWhenSavingNullUser() {
      assertThatThrownBy(() -> repository.save(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("User cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when saving user with null username")
    void shouldThrowExceptionWhenSavingUserWithNullUsername() {
      Cliente user = new Cliente();
      user.setEmail("test@example.com");

      assertThatThrownBy(() -> repository.save(user))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Username cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception when saving user with invalid email")
    void shouldThrowExceptionWhenSavingUserWithInvalidEmail() {
      Cliente user = TestDataFactory.createUserWithInvalidEmail();

      assertThatThrownBy(() -> repository.save(user))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Valid email is required");
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
      Cliente user1 = TestDataFactory.createUser("duplicate", "user1@example.com");
      Cliente user2 = TestDataFactory.createUser("duplicate", "user2@example.com");

      repository.save(user1);

      assertThatThrownBy(() -> repository.save(user2))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Username already exists: duplicate");
    }
  }

  @Nested
  @DisplayName("Complex Scenarios")
  class ComplexScenariosTests {

    @Test
    @DisplayName("Should handle multiple users and queries")
    void shouldHandleMultipleUsersAndQueries() {
      // Arrange
      Cliente[] users = TestDataFactory.createMultipleUsers(5);
      for (Cliente user : users) {
        repository.save(user);
      }

      // Act
      List<Cliente> allUsers = repository.findAll();
      List<Cliente> activeUsers = repository.findAllActive();
      long totalCount = repository.count();
      long activeCount = repository.countActive();

      // Assert
      assertThat(allUsers).hasSize(5);
      assertThat(activeUsers).hasSize(5);
      assertThat(totalCount).isEqualTo(5);
      assertThat(activeCount).isEqualTo(5);
    }

    @Test
    @DisplayName("Should handle user activation and deactivation")
    void shouldHandleUserActivationAndDeactivation() {
      // Arrange
      Cliente user = TestDataFactory.createBasicUser();
      Cliente savedUser = repository.save(user);

      // Act
      savedUser.deactivate();
      repository.update(savedUser);

      // Assert
      List<Cliente> activeUsers = repository.findAllActive();
      long activeCount = repository.countActive();

      assertThat(activeUsers).isEmpty();
      assertThat(activeCount).isZero();
      assertThat(repository.count()).isEqualTo(1);
    }
  }
}