package br.com.psicultura.service;

import br.com.psicultura.exception.RegraNegocioException;
import br.com.psicultura.model.entity.Tanque;
import br.com.psicultura.model.repository.TanqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TanqueService {

    private TanqueRepository repository;

    public TanqueService(TanqueRepository repository) {
        this.repository = repository;
    }

    public List<Tanque> getTanques() {
        return repository.findAll();
    }

    public Optional<Tanque> getTanqueById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Tanque salvar(Tanque tanque) {
        validar(tanque);
        return repository.save(tanque);
    }

    @Transactional
    public void excluir(Tanque tanque) {
        Objects.requireNonNull(tanque.getId());
        repository.delete(tanque);
    }

    public void validar(Tanque tanque) {

        if (tanque.getUltimaLavagem() == null) {
            throw new RegraNegocioException("data de lavagem inválido");
        }
        if( tanque.getCliente()== null || tanque.getCliente().getId() == null || tanque.getCliente().getId() == 0 ){
            throw new RegraNegocioException("cliente invalido");
        }
    }
}