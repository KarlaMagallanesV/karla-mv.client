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
