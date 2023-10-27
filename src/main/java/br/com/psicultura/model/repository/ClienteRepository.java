package br.com.psicultura.model.repository;

import br.com.psicultura.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long>  {
}