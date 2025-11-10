# Java Testing Application

Una aplicación Java completa de terminal que demuestra el patrón Singleton, JUnit 5, Mockito y todos los tipos de Test Doubles, con pruebas unitarias, de integración y end-to-end.


# Sistema Bancario Java

Aplicación de terminal para gestión bancaria, con arquitectura en capas, patrones de diseño, pruebas unitarias, integración y E2E usando JUnit 5 y Mockito. Incluye cuentas, clientes, créditos, transacciones y lógica de negocio realista.
- [Arquitectura](#arquitectura)
- [Patrones de Diseño](#patrones-de-diseño)

### Funcionalidades de la Aplicación
- **Sistema de gestión de usuarios** completo (CRUD)
- **Interfaz de terminal** interactiva y fácil de usar
- **Validación de datos** robusta
- **Inyección de dependencias** manual
- **Arquitectura en capas** (Model, Repository, Service, App)

```
┌─────────────────┐
│   TerminalApp   │  ← Capa de Presentación
├─────────────────┤
│   UserService   │  ← Capa de Lógica de Negocio
├─────────────────┤
│ UserRepository  │  ← Capa de Acceso a Datos
├─────────────────┤
│ DatabaseConfig  │  ← Configuración (Singleton)
├─────────────────┤
│      User       │  ← Modelo de Dominio
└─────────────────┘
```

### Componentes Principales

1. **User (Modelo)**: Entidad que representa un usuario del sistema
2. **UserRepository (Acceso a Datos)**: Maneja la persistencia y consultas
3. **UserService (Lógica de Negocio)**: Implementa reglas de negocio y validaciones
4. **DatabaseConfig (Configuración)**: Singleton que maneja la configuración de BD
5. **TerminalApp (Aplicación)**: Interfaz de usuario de terminal

## 🎨 Patrones de Diseño

### Singleton Pattern
Implementado en `DatabaseConfig` con las siguientes características:
- **Thread-safe** con synchronization
- **Lazy initialization**
- **Double-checked locking** como alternativa
    private static DatabaseConfig instance;
    
    private DatabaseConfig() { /* Constructor privado */ }
    
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }
}
```

### Repository Pattern
Implementado en `UserRepository` para abstraer el acceso a datos:
- Encapsula la lógica de acceso a datos
- Proporciona una interfaz limpia para el servicio
- Simula operaciones de base de datos en memoria
- Valida reglas de negocio
- Coordina operaciones entre repositorios
- Maneja transacciones y errores
### 1. Pruebas Unitarias (Unit Tests)
Prueban componentes individuales de forma aislada usando mocks.

**Ubicación**: `src/test/java/unit/`

**Características**:
- Usan Mockito para aislar dependencias
- Prueban lógica de negocio específica
- Ejecución rápida y determinística
- `DatabaseConfigUnitTest`: Pruebas del patrón Singleton
- `UserServiceUnitTest`: Pruebas de la lógica de negocio

**Ubicación**: `src/test/java/integration/`

**Características**:
- Usan implementaciones reales (sin mocks)
- Prueban flujo de datos entre capas
- Verifican persistencia y consistencia

### 3. Pruebas End-to-End (E2E Tests)

**Ubicación**: `src/test/java/e2e/`

**Características**:
- Prueban flujos completos de usuario
- Verifican el sistema como un todo
- Incluyen inicialización y limpieza

## 🎭 Test Doubles
La aplicación demuestra todos los tipos de Test Doubles:

### 1. MOCKS 🎭
**Propósito**: Verificar comportamiento e interacciones

```java
@Mock
private UserRepository mockRepository;

@Test
void shouldVerifyInteractions() {
    when(mockRepository.save(any(User.class))).thenReturn(user);
    
    userService.createUser("test", "test@example.com");
    
    verify(mockRepository).save(argThat(u -> 
        u.getUsername().equals("test")));
}
```

### 2. STUBS 📋
**Propósito**: Proporcionar respuestas predefinidas

```java
public class UserRepositoryStub extends UserRepository {
    private final Map<String, User> predefinedUsers;
    
    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(predefinedUsers.get(username));
    }
}
```

### 3. SPIES 🕵️
**Propósito**: Mocking parcial de objetos reales

```java
@Test
void shouldUseSpyForPartialMocking() {
    UserRepository repositorySpy = spy(new UserRepository());
    
    when(repositorySpy.existsByUsername(anyString())).thenReturn(true);
    
    // Los demás métodos usan implementación real
    User saved = repositorySpy.save(user);
    verify(repositorySpy).save(user);
}
```

### 4. FAKES 🎨
**Propósito**: Implementación funcional simplificada

```java
public class FakeUserRepository extends UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    
    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }
    
    // Implementación completa pero simplificada
}
```

### 5. DUMMIES 🤖
**Propósito**: Objetos pasados pero nunca usados

```java
public class DummyDatabaseConfig {
    public String getDatabaseUrl() {
        throw new UnsupportedOperationException("Dummy should not be used");
    }
}
```

## ⚙️ Configuración del Proyecto

### Requisitos
- **Java 21** o superior
- **Maven 3.6+**
- **IDE** con soporte para JUnit 5 (IntelliJ IDEA, Eclipse, VS Code)
```xml
<dependencies>
    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.11.2</version>
    </dependency>
    
    <!-- Mockito -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.14.2</version>
    </dependency>
    
    <!-- AssertJ -->
    <dependency>
        <groupId>org.assertj</groupId>
        <artifactId>assertj-core</artifactId>
        <version>3.26.3</version>
    </dependency>
</dependencies>
```

## 🚀 Instalación y Ejecución

### 1. Clonar y Compilar

```bash
# Compilar el proyecto
mvn clean compile

# Compilar incluyendo tests
mvn clean compile test-compile
```

### 2. Ejecutar la Aplicación

```bash
# Ejecutar la aplicación principal
mvn exec:java -Dexec.mainClass="com.example.TerminalApp"

# O usando el plugin exec
mvn exec:java
```

### 3. Ejecutar Pruebas

```bash
# Ejecutar todas las pruebas
mvn test

# Solo pruebas unitarias
mvn test -Dtest="**/*UnitTest"

# Solo pruebas de integración
mvn test -Dtest="**/*IntegrationTest"

# Solo pruebas E2E
mvn test -Dtest="**/*E2ETest"
```

## 📁 Estructura del Proyecto

```
src/
├── main/java/com/example/
│   ├── TerminalApp.java              # Aplicación principal
│   ├── config/
│   │   └── DatabaseConfig.java      # Singleton para configuración
│   ├── model/
│   │   └── User.java                # Modelo de dominio
│   ├── repository/
│   │   └── UserRepository.java      # Acceso a datos
│   └── service/
│       └── UserService.java         # Lógica de negocio
└── test/java/
    ├── unit/                         # Pruebas unitarias
    │   ├── UserUnitTest.java
    │   ├── DatabaseConfigUnitTest.java
    │   ├── UserRepositoryUnitTest.java
    │   └── UserServiceUnitTest.java
    ├── integration/                  # Pruebas de integración
    │   └── UserServiceIntegrationTest.java
    ├── e2e/                         # Pruebas end-to-end
    │   └── TerminalAppE2ETest.java
    └── util/                        # Utilidades de testing
        ├── TestDataFactory.java
        └── TestDoubles.java
```

## 📝 Comandos Maven

### Compilación y Empaquetado

```bash
# Limpiar y compilar
mvn clean compile

# Empaquetar en JAR
mvn package

# Instalar en repositorio local
mvn install
```

### Ejecución de Pruebas

```bash
# Todas las pruebas
mvn test

# Con perfiles específicos
mvn test -P unit-tests
mvn test -P integration-tests
mvn test -P e2e-tests

# Con reportes detallados
mvn test -Dmaven.test.verbose=true

# Con reporte de cobertura de código (coverage)
mvn clean test jacoco:report

# Pruebas específicas
mvn test -Dtest=UserUnitTest
mvn test -Dtest=UserServiceUnitTest#shouldCreateUserSuccessfully
```

### Ejecución de la Aplicación

```bash
# Ejecutar aplicación
mvn exec:java

# Con argumentos
mvn exec:java -Dexec.args="arg1 arg2"

# Con perfil específico
mvn exec:java -P development
```

### Reportes de Cobertura de Código

```bash
# Generar reporte de coverage después de ejecutar las pruebas
mvn clean test jacoco:report

# El reporte HTML se genera en: target/site/jacoco/index.html
open target/site/jacoco/index.html  # macOS
start target/site/jacoco/index.html # Windows
xdg-open target/site/jacoco/index.html # Linux

# Verificar que la cobertura cumple con los umbrales (opcional)
# mvn clean test jacoco:report jacoco:check
```

**Interpretando el Reporte de Coverage:**
- **Líneas verdes**: Código cubierto por las pruebas
- **Líneas rojas**: Código no cubierto por las pruebas  
- **Líneas amarillas**: Código parcialmente cubierto
### Uso de la Aplicación

1. **Ejecutar la aplicación**:
   ```bash
   mvn exec:java
   ```

2. **Navegar por el menú**:
   ```
   === User Management Menu ===
   1. Create User
   2. View User by ID
   3. Find User by Username
   ...
   Choose an option: 1
   ```

3. **Crear un usuario**:
   ```
   Enter username: john_doe
   Enter email: john@example.com
   User created successfully!
   ```

### Ejemplos de Testing

#### Prueba Unitaria con Mock
```java
@Test
void shouldCreateUserWithMock() {
    when(mockRepository.existsByUsername("test")).thenReturn(false);
    when(mockRepository.save(any(User.class))).thenReturn(savedUser);
    
    User result = userService.createUser("test", "test@example.com");
    
    assertThat(result).isNotNull();
    verify(mockRepository).save(any(User.class));
}
```

#### Prueba de Integración
```java
@Test
void shouldIntegrateServiceAndRepository() {
    User created = userService.createUser("integration", "test@example.com");
    User retrieved = userService.getUserById(created.getId());
    
    assertThat(retrieved).isEqualTo(created);
}
```

#### Prueba End-to-End
```java
@Test
void shouldCompleteUserLifecycle() {
    // Create -> Update -> Deactivate -> Delete
    User user = userService.createUser("e2e", "e2e@example.com");
    userService.updateUser(user.getId(), "updated", null);
    userService.deactivateUser(user.getId());
    userService.deleteUser(user.getId());
    
    assertThatThrownBy(() -> userService.getUserById(user.getId()))
        .isInstanceOf(UserService.UserServiceException.class);
}
```

## 🛠️ Tecnologías Utilizadas

### Framework y Bibliotecas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 21 | Lenguaje de programación |
| **Maven** | 3.6+ | Gestión de dependencias y build |
| **JUnit 5** | 5.11.2 | Framework de testing |
| **Mockito** | 5.14.2 | Biblioteca de mocking |
| **AssertJ** | 3.26.3 | Aserciones fluidas |

### Características de Java 21 Utilizadas

- **Text Blocks** para strings multilínea
- **Switch Expressions** para lógica condicional
- **Records** en las utilidades de testing
- **maven-compiler-plugin**: Compilación con Java 21
- **maven-surefire-plugin**: Ejecución de pruebas unitarias
- **maven-failsafe-plugin**: Ejecución de pruebas de integración
- **Clases principales**: 5
- **Clases de test**: 7
- **Total de pruebas**: 100+
Este proyecto demuestra:

1. **Patrón Singleton** thread-safe
2. **JUnit 5** con features modernas
3. **Mockito** para mocking avanzado
4. **Test Doubles** completos
5. **Arquitectura en capas**
6. **Testing estratificado** (Unit/Integration/E2E)
7. **Inyección de dependencias** manual
8. **Manejo de errores** robusto
9. **Validación de datos** comprehensiva
10. **Documentación** profesional

## 🤝 Contribuciones

Para contribuir al proyecto:

1. Fork el repositorio
2. Crea una rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crea un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 📞 Contacto

- **Proyecto**: Java Testing Application
- **Versión**: 1.0.0
- **Última actualización**: Octubre 2025
*Este proyecto fue creado como una demostración completa de testing en Java, incluyendo todos los tipos de Test Doubles y estrategias de testing modernas.*