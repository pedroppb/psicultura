package br.com.psicultura.model.repository;

import br.com.psicultura.model.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
}