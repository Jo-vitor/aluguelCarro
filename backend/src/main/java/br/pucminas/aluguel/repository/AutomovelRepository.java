package br.pucminas.aluguel.repository;

import br.pucminas.aluguel.model.Automovel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AutomovelRepository extends JpaRepository<Automovel, Long> {
    boolean existsByPlaca(String placa);
    boolean existsByMatricula(String matricula);
}
