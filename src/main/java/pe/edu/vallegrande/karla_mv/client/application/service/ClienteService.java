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
