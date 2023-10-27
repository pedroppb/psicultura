package br.com.psicultura.service;

import br.com.psicultura.exception.RegraNegocioException;
import br.com.psicultura.model.entity.Dado;
import br.com.psicultura.model.repository.DadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

        import java.util.List;
        import java.util.Objects;
        import java.util.Optional;

@Service
public class DadoService {

    private DadoRepository repository;

    public DadoService(DadoRepository repository) {
        this.repository = repository;
    }

    public List<Dado> getDados() {
        return repository.findAll();
    }

    public Optional<Dado> getDadoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Dado salvar(Dado dado) {
        validar(dado);
        return repository.save(dado);
    }

    @Transactional
    public void excluir(Dado dado) {
        Objects.requireNonNull(dado.getId());
        repository.delete(dado);
    }

    public void validar(Dado dado) {

        if (dado.getValor() == null) {
            throw new RegraNegocioException("valor inválido");
        }
        if (dado.getData() == null) {
            throw new RegraNegocioException("data inválido");
        }
        if(dado.getSensor() == null || dado.getSensor().getId() == null || dado.getSensor().getId() == 0 )
        {
            throw new NullPointerException("sensor inválido");
        }
    }
}