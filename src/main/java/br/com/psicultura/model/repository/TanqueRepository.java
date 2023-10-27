package br.com.psicultura.model.repository;

import br.com.psicultura.model.entity.Tanque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  TanqueRepository extends JpaRepository<Tanque, Long>  {
}