package br.com.psicultura.model.repository;
import br.com.psicultura.model.entity.Dado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DadoRepository extends JpaRepository<Dado, Long>  {
}