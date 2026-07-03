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
