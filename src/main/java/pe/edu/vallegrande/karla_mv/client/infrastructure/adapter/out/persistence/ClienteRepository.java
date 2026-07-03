package pe.edu.vallegrande.karla_mv.client.infrastructure.adapter.out.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import pe.edu.vallegrande.karla_mv.client.domain.model.Cliente;
import reactor.core.publisher.Mono;

@Repository
public interface ClienteRepository extends ReactiveCrudRepository<Cliente, Long> {
    Mono<Cliente> findByDni(String dni);
}
