# Cookie Service

Servicio REST desarrollado con Java 17, Spring Boot, Gradle, H2 y arquitectura MVC con patrón Gateway.

## Tecnologías

- **Java 17**
- **Spring Boot 3.2.3**
- **Spring Data JPA** (ORM con Hibernate)
- **H2 Database** (base de datos en memoria)
- **Lombok** (reducción de boilerplate)
- **Gradle 8.5**

## Arquitectura

El proyecto sigue una arquitectura MVC estratificada con los siguientes componentes:

```
┌─────────────────────────────────────────────────────────┐
│                    Controller Layer                      │
│              RegistroController.java                     │
├─────────────────────────────────────────────────────────┤
│                    Service Layer                         │
│                RegistroService.java                      │
├─────────────────────────────────────────────────────────┤
│                    Gateway Layer                         │
│              RegistroGateway.java                        │
├─────────────────────────────────────────────────────────┤
│                  Repository Layer                        │
│           RegistroRepository.java                        │
├─────────────────────────────────────────────────────────┤
│                 Database Layer                           │
│                    H2 Database                           │
└─────────────────────────────────────────────────────────┘
```

### Capas

1. **Controller (MVC - View/Controller)**: Expone endpoints REST y maneja las peticiones HTTP
2. **Service**: Contiene la lógica de negocio
3. **Gateway (Patrón Gateway)**: Actúa como intermediario entre la capa de servicio y el repositorio, abstrayendo el acceso a datos
4. **Repository (ORM)**: Interfaz Spring Data JPA para acceso directo a la base de datos
5. **Model (MVC - Model)**: Entidades JPA que mapean a tablas de la base de datos

## Estructura del Proyecto

```
src/main/java/com/example/cookie/
├── CookieApplication.java          # Clase principal Spring Boot
├── controller/
│   └── RegistroController.java     # Endpoint REST
├── model/
│   └── Registro.java               # Entidad JPA
├── repository/
│   └── RegistroRepository.java     # Repositorio Spring Data JPA
├── gateway/
│   └── RegistroGateway.java        # Patrón Gateway
└── service/
    └── RegistroService.java        # Servicio de negocio

src/main/resources/
└── application.properties          # Configuración de la aplicación
```

## Endpoints

### Crear Registro

```
POST /registros
Content-Type: application/json

{
  "descripcion": "Descripción del producto",
  "precio": 29.99
}
```

**Respuesta:**
```json
{
  "id": 1,
  "descripcion": "Descripción del producto",
  "precio": 29.99
}
```

### Obtener Todos los Registros

```
GET /registros
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "descripcion": "Descripción del producto",
    "precio": 29.99
  }
]
```

## Tabla en la Base de Datos

La base de datos H2 contiene la tabla `registros`:

| Columna     | Tipo       | Descripción          |
|-------------|------------|----------------------|
| id          | BIGINT     | ID auto-generado     |
| descripcion | VARCHAR    | Descripción del registro |
| precio      | DECIMAL    | Precio del registro  |

## Cómo Ejecutar

### Requisitos previos

- Java 17 o superior
- Gradle 8.5 (incluido wrapper)

### Pasos

1. **Compilar el proyecto:**
   ```bash
   ./gradlew build
   ```

2. **Ejecutar la aplicación:**
   ```bash
   ./gradlew bootRun
   ```

3. **Acceder a la aplicación:**
   - API: `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`

### Ejemplo con cURL

```bash
# Crear un registro
curl -X POST http://localhost:8080/registros \
  -H "Content-Type: application/json" \
  -d '{"descripcion":"Producto de ejemplo","precio":49.99}'

# Obtener todos los registros
curl http://localhost:8080/registros
```

## Configuración

La configuración se encuentra en `src/main/resources/application.properties`:

- **Base de datos:** H2 en memoria (`cookiedb`)
- **JPA:** Actualización automática de esquemas (`ddl-auto=update`)
- **Puerto:** 8080
- **H2 Console:** Habilitado en `/h2-console`

## Patrón Gateway

El patrón Gateway en `RegistroGateway` actúa como una capa de abstracción entre el servicio y el repositorio:

- **Encapsula** la lógica de acceso a datos
- **Desacopla** la capa de servicio de la implementación del repositorio
- **Facilita** el testing y la mockeabilidad
- **Permite** cambiar la implementación de acceso a datos sin afectar la lógica de negocio

```java
// El servicio usa el Gateway, no el repositorio directamente
@Service
public class RegistroService {
    private final RegistroGateway registroGateway;
    // ...
}
```

## Licencia

Este proyecto es de uso educativo y de demostración.
