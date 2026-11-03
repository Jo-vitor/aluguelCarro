package br.pucminas.aluguel.repository;

import br.pucminas.aluguel.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AgenteRepository extends JpaRepository<Agente, Long> {
    Optional<Agente> findByUsuarioId(Long usuarioId);
    boolean existsByCnpj(String cnpj);
}
