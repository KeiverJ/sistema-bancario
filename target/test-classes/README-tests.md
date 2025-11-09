# Convención de Pruebas

Este proyecto separa las pruebas por tipo y por patrón de diseño.

## Tipos
- Unit: `*UnitTest.java` (ejecutadas por Surefire)
- Integration: `*IntegrationTest.java` (Failsafe fase integration-test)
- E2E: `*E2ETest.java` (Failsafe fase verify)

## Ejecución rápida
```powershell
mvn test                # Solo unit
mvn verify              # Unit + Integration + E2E
mvn -Punit-tests test   # Fuerza solo unit
mvn -Pintegration-tests verify   # Solo integration
mvn -Pe2e-tests verify  # Solo E2E
```

## Patrones Cubiertos
Adapter, Builder, Chain, Composite, Decorator, Factory, Observer, State, Strategy, Template.

