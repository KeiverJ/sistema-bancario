package unit;

import com.example.config.databaseConfig;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DatabaseConfig class demonstrating Singleton pattern testing.
 * Shows how to test singleton instances and thread safety.
 */
@DisplayName("DatabaseConfig Singleton Unit Tests")
class DatabaseConfigUnitTest {

  @BeforeEach
  void setUp() {
    // Reset singleton before each test to ensure clean state
    databaseConfig.resetForTesting();
  }

  @AfterEach
  void tearDown() {
    // Reset singleton after each test
    databaseConfig.resetForTesting();
  }

  @Nested
  @DisplayName("Singleton Pattern Tests")
  class SingletonPatternTests {

    @Test
    @DisplayName("Should return same instance on multiple calls")
    void shouldReturnSameInstanceOnMultipleCalls() {
      databaseConfig instance1 = databaseConfig.getInstance();
      databaseConfig instance2 = databaseConfig.getInstance();
      databaseConfig instance3 = databaseConfig.getInstance();

      assertThat(instance1)
          .isSameAs(instance2)
          .isSameAs(instance3);
    }

    @Test
    @DisplayName("Should return same instance using double-checked locking")
    void shouldReturnSameInstanceUsingDoubleCheckedLocking() {
      databaseConfig instance1 = databaseConfig.getInstanceDoubleChecked();
      databaseConfig instance2 = databaseConfig.getInstanceDoubleChecked();

      assertThat(instance1).isSameAs(instance2);
    }

    @Test
    @DisplayName("Should be thread-safe")
    void shouldBeThreadSafe() throws InterruptedException {
      final int numberOfThreads = 10;
      final databaseConfig[] instances = new databaseConfig[numberOfThreads];
      final Thread[] threads = new Thread[numberOfThreads];

      // Create multiple threads that get singleton instance
      for (int i = 0; i < numberOfThreads; i++) {
        final int index = i;
        threads[i] = new Thread(() -> {
          instances[index] = databaseConfig.getInstance();
        });
      }

      // Start all threads
      for (Thread thread : threads) {
        thread.start();
      }

      // Wait for all threads to complete
      for (Thread thread : threads) {
        thread.join();
      }

      // Verify all instances are the same
      databaseConfig firstInstance = instances[0];
      for (int i = 1; i < numberOfThreads; i++) {
        assertThat(instances[i]).isSameAs(firstInstance);
      }
    }
  }

  @Nested
  @DisplayName("Configuration Properties Tests")
  class ConfigurationPropertiesTests {

    private databaseConfig config;

    @BeforeEach
    void setUp() {
      config = databaseConfig.getInstance();
    }

    @Test
    @DisplayName("Should have default database configuration")
    void shouldHaveDefaultDatabaseConfiguration() {
      assertThat(config.getDatabaseUrl()).isEqualTo("jdbc:h2:mem:testdb");
      assertThat(config.getUsername()).isEqualTo("sa");
      assertThat(config.getPassword()).isEmpty();
      assertThat(config.getMaxConnections()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should provide database properties")
    void shouldProvideDatabaseProperties() {
      assertThat(config.getProperty("database.driver")).isEqualTo("org.h2.Driver");
      assertThat(config.getProperty("database.url")).isEqualTo("jdbc:h2:mem:testdb");
      assertThat(config.getProperty("database.username")).isEqualTo("sa");
      assertThat(config.getProperty("database.password")).isEmpty();
      assertThat(config.getProperty("database.maxConnections")).isEqualTo("10");
      assertThat(config.getProperty("database.autoCommit")).isEqualTo("true");
      assertThat(config.getProperty("database.timeout")).isEqualTo("30");
    }

    @Test
    @DisplayName("Should return null for non-existent property")
    void shouldReturnNullForNonExistentProperty() {
      assertThat(config.getProperty("nonexistent.property")).isNull();
    }

    @Test
    @DisplayName("Should allow setting custom properties")
    void shouldAllowSettingCustomProperties() {
      String key = "custom.property";
      String value = "custom.value";

      config.setProperty(key, value);

      assertThat(config.getProperty(key)).isEqualTo(value);
    }

    @Test
    @DisplayName("Should return all properties")
    void shouldReturnAllProperties() {
      var properties = config.getAllProperties();

      assertThat(properties).isNotNull();
      assertThat(properties.getProperty("database.driver")).isEqualTo("org.h2.Driver");
      assertThat(properties.getProperty("database.url")).isEqualTo("jdbc:h2:mem:testdb");
    }
  }

  @Nested
  @DisplayName("Business Logic Tests")
  class BusinessLogicTests {

    private databaseConfig config;

    @BeforeEach
    void setUp() {
      config = databaseConfig.getInstance();
    }

    @Test
    @DisplayName("Should detect non-production environment")
    void shouldDetectNonProductionEnvironment() {
      assertThat(config.isProductionEnvironment()).isFalse();
    }

    @Test
    @DisplayName("Should have valid configuration")
    void shouldHaveValidConfiguration() {
      assertThat(config.isValidConfiguration()).isTrue();
    }

    @Test
    @DisplayName("Should return default connection timeout")
    void shouldReturnDefaultConnectionTimeout() {
      assertThat(config.getConnectionTimeout()).isEqualTo(30);
    }

    @Test
    @DisplayName("Should handle custom timeout value")
    void shouldHandleCustomTimeoutValue() {
      String customTimeout = "60";
      config.setProperty("database.timeout", customTimeout);

      assertThat(config.getConnectionTimeout()).isEqualTo(60);
    }

    @Test
    @DisplayName("Should use default timeout for invalid value")
    void shouldUseDefaultTimeoutForInvalidValue() {
      config.setProperty("database.timeout", "invalid");

      // Should return default value instead of throwing exception
      assertThat(config.getConnectionTimeout()).isEqualTo(30);
    }
  }

  @Nested
  @DisplayName("toString Method Tests")
  class ToStringTests {

    @Test
    @DisplayName("Should provide meaningful string representation")
    void shouldProvideMeaningfulStringRepresentation() {
      databaseConfig config = databaseConfig.getInstance();
      String toString = config.toString();

      assertThat(toString)
          .contains("DatabaseConfig{")
          .contains("url='jdbc:h2:mem:testdb'")
          .contains("username='sa'")
          .contains("maxConnections=10");
    }
  }

  @Nested
  @DisplayName("Edge Cases Tests")
  class EdgeCasesTests {

    private databaseConfig config;

    @BeforeEach
    void setUp() {
      config = databaseConfig.getInstance();
    }

    @Test
    @DisplayName("Should handle null property key")
    void shouldHandleNullPropertyKey() {
      assertThat(config.getProperty(null)).isNull();
    }

    @Test
    @DisplayName("Should handle empty property key")
    void shouldHandleEmptyPropertyKey() {
      assertThat(config.getProperty("")).isNull();
    }

    @Test
    @DisplayName("Should handle null property value")
    void shouldHandleNullPropertyValue() {
      assertDoesNotThrow(() -> {
        config.setProperty("test.key", null);
      });
    }

    @Test
    @DisplayName("Should handle empty property value")
    void shouldHandleEmptyPropertyValue() {
      assertDoesNotThrow(() -> {
        config.setProperty("test.key", "");
        assertThat(config.getProperty("test.key")).isEmpty();
      });
    }
  }

  @Test
  @DisplayName("Should demonstrate singleton behavior across test methods")
  void shouldDemonstrateSingletonBehaviorAcrossTestMethods() {
    // This test demonstrates that singleton state persists across test methods
    databaseConfig config = databaseConfig.getInstance();

    // Set a custom property
    String testKey = "test.singleton.behavior";
    String testValue = "singleton_works";
    config.setProperty(testKey, testValue);

    // Get a new reference to the singleton
    databaseConfig anotherReference = databaseConfig.getInstance();

    // Verify it's the same instance and has the property we set
    assertThat(anotherReference).isSameAs(config);
    assertThat(anotherReference.getProperty(testKey)).isEqualTo(testValue);
  }
}