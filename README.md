# Microservicio Cliente

Microservicio maestro CRUD para gestión de clientes con arquitectura hexagonal y Spring WebFlux reactivo.

## Pre-requisitos

- Java 26+
- Maven 3.9+
- PostgreSQL 15+
- IDE (IntelliJ IDEA / Eclipse / VS Code)

## Estructura del Proyecto

```
src/main/java/pe/edu/vallegrande/karla_mv/client/
├── application/
│   ├── port/
│   │   ├── in/          # Puertos de entrada (interfaces de servicio)
│   │   └── out/         # Puertos de salida (interfaces de repositorio)
│   └── service/         # Implementaciones de servicio
├── domain/
│   └── model/           # Entidades de dominio
└── infrastructure/
    └── adapter/
        ├── in/
        │   └── rest/    # Controladores REST
        └── out/
            └── persistence/  # Repositorios y adaptadores
```

## Dependencias

### `pom.xml`

```xml
<dependencies>
    <!-- Spring Boot WebFlux -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    
    <!-- Spring Data R2DBC -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-r2dbc</artifactId>
    </dependency>
    
    <!-- DevTools -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
        <optional>true</optional>
    </dependency>
    
    <!-- PostgreSQL JDBC -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- PostgreSQL R2DBC Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>r2dbc-postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Reactor Test -->
    <dependency>
        <groupId>io.projectreactor</groupId>
        <artifactId>reactor-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Paso a Paso de Creación

### 1. Configurar la Base de Datos

1. Iniciar PostgreSQL
2. Crear la base de datos: `karla_mv`
3. Ejecutar el script `schema.sql` (se ejecuta automáticamente al iniciar la app)

### 2. Configurar Variables de Entorno

Editar `application.yml` con tus credenciales:

```yaml
server:
  port: 8080

spring:
  application:
    name: ms-clientes
  r2dbc:
    url: r2dbc:postgresql://${DB_HOST:localhost}/${DB_NAME:karla_mv}?sslmode=${DB_SSL_MODE:disable}
    username: ${DB_USER:postgres}
    password: ${DB_PASSWORD:tu_contraseña}
    pool:
      enabled: true
      initial-size: 1
      max-size: 5
      max-idle-time: 10m
      validation-query: SELECT 1
```

### 3. Orden de Implementación

**1. Domain Layer**
- Crear entidad: `Cliente`

**2. Application Layer**
- Crear puerto out: `IClienteRepositoryPort`
- Crear puerto in: `IClienteServicePort`
- Implementar servicio: `ClienteService`

**3. Infrastructure Layer**
- Crear repositorio R2DBC: `ClienteRepository`
- Crear adaptador: `ClienteRepositoryAdapter`
- Crear controlador REST: `ClienteRest`

### 4. Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

## Creación de Archivos

### 1. Domain Layer

#### `domain/model/Cliente.java`
```java
package pe.edu.vallegrande.karla_mv.client.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("clientes")
public class Cliente {
    @Id
    private Long id;

    @Column("dni")
    private String dni;

    @Column("nombres")
    private String nombres;

    @Column("apellidos")
    private String apellidos;

    @Column("celular")
    private String celular;

    @Column("correo")
    private String correo;

    @Column("licencia")
    private String licencia;

    @Column("estado")
    private String estado;
}
```

### 2. Application Layer

#### `application/port/out/IClienteRepositoryPort.java`
```java
package pe.edu.vallegrande.karla_mv.client.application.port.out;

import pe.edu.vallegrande.karla_mv.client.domain.model.Cliente;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClienteRepositoryPort {
    Mono<Cliente> save(Cliente cliente);
    Mono<Cliente> findById(Long id);
    Mono<Cliente> findByDni(String dni);
    Flux<Cliente> findAll();
    Mono<Cliente> update(Cliente cliente);
    Mono<Void> deleteById(Long id);
}
```

#### `application/port/in/IClienteServicePort.java`
```java
package pe.edu.vallegrande.karla_mv.client.application.port.in;

import pe.edu.vallegrande.karla_mv.client.domain.model.Cliente;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClienteServicePort {
    Mono<Cliente> create(Cliente cliente);
    Mono<Cliente> findById(Long id);
    Mono<Cliente> findByDni(String dni);
    Flux<Cliente> findAll();
    Mono<Cliente> update(Long id, Cliente cliente);
    Mono<Void> deleteById(Long id);
}
```

#### `application/service/ClienteService.java`
```java
package pe.edu.vallegrande.karla_mv.client.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.karla_mv.client.application.port.in.IClienteServicePort;
import pe.edu.vallegrande.karla_mv.client.application.port.out.IClienteRepositoryPort;
import pe.edu.vallegrande.karla_mv.client.domain.model.Cliente;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClienteService implements IClienteServicePort {

    private final IClienteRepositoryPort clienteRepositoryPort;

    @Override
    public Mono<Cliente> create(Cliente cliente) {
        return clienteRepositoryPort.save(cliente);
    }

    @Override
    public Mono<Cliente> findById(Long id) {
        return clienteRepositoryPort.findById(id);
    }

    @Override
    public Mono<Cliente> findByDni(String dni) {
        return clienteRepositoryPort.findByDni(dni);
    }

    @Override
    public Flux<Cliente> findAll() {
        return clienteRepositoryPort.findAll();
    }

    @Override
    public Mono<Cliente> update(Long id, Cliente cliente) {
        return clienteRepositoryPort.findById(id)
                .flatMap(existingCliente -> {
                    existingCliente.setDni(cliente.getDni());
                    existingCliente.setNombres(cliente.getNombres());
                    existingCliente.setApellidos(cliente.getApellidos());
                    existingCliente.setCelular(cliente.getCelular());
                    existingCliente.setCorreo(cliente.getCorreo());
                    existingCliente.setLicencia(cliente.getLicencia());
                    existingCliente.setEstado(cliente.getEstado());
                    return clienteRepositoryPort.update(existingCliente);
                });
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return clienteRepositoryPort.deleteById(id);
    }
}
```

### 3. Infrastructure Layer

#### `infrastructure/adapter/out/persistence/ClienteRepository.java`
```java
package pe.edu.vallegrande.karla_mv.client.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.karla_mv.client.domain.model.Cliente;
import reactor.core.publisher.Mono;

@Repository
public interface ClienteRepository extends ReactiveCrudRepository<Cliente, Long> {
    Mono<Cliente> findByDni(String dni);
}
```

#### `infrastructure/adapter/out/persistence/ClienteRepositoryAdapter.java`
```java
package pe.edu.vallegrande.karla_mv.client.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.vallegrande.karla_mv.client.application.port.out.IClienteRepositoryPort;
import pe.edu.vallegrande.karla_mv.client.domain.model.Cliente;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ClienteRepositoryAdapter implements IClienteRepositoryPort {

    private final ClienteRepository clienteRepository;

    @Override
    public Mono<Cliente> save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Mono<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Mono<Cliente> findByDni(String dni) {
        return clienteRepository.findByDni(dni);
    }

    @Override
    public Flux<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    @Override
    public Mono<Cliente> update(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return clienteRepository.deleteById(id);
    }
}
```

#### `infrastructure/adapter/in/rest/ClienteRest.java`
```java
package pe.edu.vallegrande.karla_mv.client.infrastructure.adapter.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.karla_mv.client.application.port.in.IClienteServicePort;
import pe.edu.vallegrande.karla_mv.client.domain.model.Cliente;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteRest {

    private final IClienteServicePort clienteServicePort;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cliente> create(@RequestBody Cliente cliente) {
        return clienteServicePort.create(cliente);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Cliente>> findById(@PathVariable Long id) {
        return clienteServicePort.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/dni/{dni}")
    public Mono<ResponseEntity<Cliente>> findByDni(@PathVariable String dni) {
        return clienteServicePort.findByDni(dni)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Cliente> findAll() {
        return clienteServicePort.findAll();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Cliente>> update(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteServicePort.update(id, cliente)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable Long id) {
        return clienteServicePort.deleteById(id);
    }
}
```

## Endpoints API

### Clientes (`/api/clientes`)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/` | Crear cliente |
| GET | `/{id}` | Obtener cliente por ID |
| GET | `/dni/{dni}` | Obtener cliente por DNI |
| GET | `/` | Listar todos los clientes |
| PUT | `/{id}` | Actualizar cliente |
| DELETE | `/{id}` | Eliminar cliente |

## Ejemplos de Uso

### Crear Cliente

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "dni": "12345678",
    "nombres": "Juan",
    "apellidos": "Pérez",
    "celular": "987654321",
    "correo": "juan@example.com",
    "licencia": "A123456",
    "estado": "ACTIVO"
  }'
```
