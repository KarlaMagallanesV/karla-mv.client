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
