package br.com.psicultura.service;

import br.com.psicultura.exception.RegraNegocioException;
import br.com.psicultura.model.entity.Sensor;
import br.com.psicultura.model.repository.SensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SensorService {

    private SensorRepository repository;

    public SensorService(SensorRepository repository) {
        this.repository = repository;
    }

    public List<Sensor> getSensors() {
        return repository.findAll();
    }

    public Optional<Sensor> getSensorById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Sensor salvar(Sensor sensor) {
        validar(sensor);
        return repository.save(sensor);
    }

    @Transactional
    public void excluir(Sensor sensor) {
        Objects.requireNonNull(sensor.getId());
        repository.delete(sensor);
    }

    public void validar(Sensor sensor) {

        if (sensor.getManutencao() == null) {
            throw new RegraNegocioException("data de manutenção inválido");
        }
        if(sensor.getTanque() == null || sensor.getTanque().getId() == null || sensor.getTanque().getId() == 0 )
        {
            throw new NullPointerException("Cliente com Endereco inválido");
        }
    }
}