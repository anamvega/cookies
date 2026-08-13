# Cookie Restaurant Service

Servicio REST para gestión de menú e ingredients en un restaurante de cookies. Desarrollado con Java 17, Spring Boot, Gradle, H2 y arquitectura MVC con patrón Gateway.

## Tecnologías

- **Java 17**
- **Spring Boot 3.2.3**
- **Spring Data JPA** (ORM con Hibernate)
- **H2 Database** (base de datos en memoria)
- **Lombok** (reducción de boilerplate)
- **Gradle 8.5**
- **JUnit 5** (testing)

## Descripción del Proyecto

Aplicación de gestión de menú para un restaurante especializado en cookies. Permite crear, actualizar y gestionar productos del menú junto con sus ingredients asociados. Utiliza una arquitectura en capas con el patrón Gateway para abstraer la capa de acceso a datos.

## Arquitectura

El proyecto sigue una arquitectura MVC estratificada con los siguientes componentes:

```
┌─────────────────────────────────────────────────────────┐
│                    Controller Layer                      │
│      MenuController | IngredienteController             │
├─────────────────────────────────────────────────────────┤
│                    Service Layer                         │
│      MenuService | IngredienteService                   │
├─────────────────────────────────────────────────────────┤
│                    Gateway Layer                         │
│      MenuGateway | IngredienteGateway                    │
├─────────────────────────────────────────────────────────┤
│                  Repository Layer                        │
│    MenuRepository | IngredienteRepository                │
├─────────────────────────────────────────────────────────┤
│                 Database Layer                           │
│                    H2 Database                           │
└─────────────────────────────────────────────────────────┘
```

### Capas

1. **Controller (MVC - View/Controller)**: Expone endpoints REST y maneja las peticiones HTTP
2. **Service**: Contiene la lógica de negocio y validaciones
3. **Gateway (Patrón Gateway)**: Actúa como intermediario entre la capa de servicio y el repositorio, abstrayendo el acceso a datos
4. **Repository (ORM)**: Interfaz Spring Data JPA para acceso directo a la base de datos
5. **Model (MVC - Model)**: Entidades JPA que mapean a tablas de la base de datos

## Estructura del Proyecto

```
src/main/java/com/restaurant/cookie/
├── CookieApplication.java          # Clase principal Spring Boot
├── config/                          # Configuración de la aplicación
├── controller/
│   ├── MenuController.java          # Endpoints para gestión de menú
│   └── IngredienteController.java   # Endpoints para gestión de ingredients
├── model/
│   ├── Menu.java                    # Entidad Menu con relación many-to-many
│   └── Ingrediente.java             # Entidad Ingrediente
├── repository/
│   ├── MenuRepository.java          # Repositorio Spring Data JPA para Menu
│   └── IngredienteRepository.java   # Repositorio Spring Data JPA para Ingrediente
├── gateway/
│   ├── MenuGateway.java             # Patrón Gateway para Menu
│   └── IngredienteGateway.java      # Patrón Gateway para Ingrediente
└── service/
    ├── MenuService.java             # Servicio de negocio para Menu
    ├── IngredienteService.java      # Servicio de negocio para Ingrediente
    └── ValidacionIngredienteService.java  # Validaciones específicas

src/main/resources/
└── application.properties           # Configuración de la aplicación

src/test/java/com/restaurant/cookie/
└── controller/
    └── MenuControllerIntegrationTest.java  # Tests de integración
```

## Modelo de Datos

### Entidades

**Menu**
- id (Long) - ID auto-generado
- descripcion (String) - Descripción del producto
- precio (BigDecimal) - Precio del producto
- estado (Integer) - 0 = disponible, 1 = no disponible
- ingredients (List<Ingrediente>) - Relación many-to-many

**Ingrediente**
- id (Long) - ID auto-generado
- nombre (String) - Nombre del ingredient
- descripcion (String) - Descripción del ingredient

**Tabla de Relación: menu_ingredientes**
- menu_id (Long) - FK a Menu
- ingrediente_id (Long) - FK a Ingrediente

## Endpoints

### Menú

#### Crear nuevo plato

```
POST /registros
Content-Type: application/json

{
  "descripcion": "Cookie de Chocolate",
  "precio": 29.99,
  "ingredientesIds": [1, 2, 3]
}
```

**Respuesta:**
```json
{
  "id": 1,
  "descripcion": "Cookie de Chocolate",
  "precio": 29.99,
  "estado": 0,
  "ingredients": [...]
}
```

#### Obtener todos los platos

```
GET /registros
```

#### Obtener plato por ID

```
GET /registros/{id}
```

### Ingredientes

#### Crear ingredient

```
POST /ingredients
Content-Type: application/json

{
  "nombre": "Chocolate",
  "descripcion": "Chocolate negro"
}
```

#### Obtener todos los ingredients

```
GET /ingredients
```

#### Obtener ingredient por ID

```
GET /ingredients/{id}
```

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

### Ejecutar Tests

```bash
./gradlew test
```

### Ejemplo con cURL

```bash
# Crear un ingredient
curl -X POST http://localhost:8080/ingredients \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Chocolate","descripcion":"Chocolate negro"}'

# Crear un plato con ingredients
curl -X POST http://localhost:8080/registros \
  -H "Content-Type: application/json" \
  -d '{"descripcion":"Cookie de Chocolate","precio":29.99,"ingredientesIds":[1,2]}'

# Obtener todos los platos
curl http://localhost:8080/registros

# Obtener todos los ingredients
curl http://localhost:8080/ingredients
```

## Configuración

La configuración se encuentra en `src/main/resources/application.properties`:

- **Base de datos:** H2 en memoria (`cookiedb`)
- **JPA:** Actualización automática de esquemas (`ddl-auto=update`)
- **Puerto:** 8080
- **H2 Console:** Habilitado en `/h2-console`

## Patrón Gateway

El patrón Gateway actúa como una capa de abstracción entre la capa de servicio y los repositorios:

- **Encapsula** la lógica de acceso a datos
- **Desacopla** la capa de servicio de la implementación del repositorio
- **Facilita** el testing y la mockeabilidad
- **Permite** cambiar la implementación de acceso a datos sin afectar la lógica de negocio

```java
// El servicio usa el Gateway, no el repositorio directamente
@Service
public class MenuService {
    private final MenuGateway menuGateway;
    // ...
}
```

## Licencia

Este proyecto es de uso educativo y de demostración.
