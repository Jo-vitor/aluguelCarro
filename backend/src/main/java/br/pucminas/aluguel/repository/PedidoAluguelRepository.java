package br.pucminas.aluguel.repository;

import br.pucminas.aluguel.model.PedidoAluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoAluguelRepository extends JpaRepository<PedidoAluguel, Long> {
    List<PedidoAluguel> findByClienteIdOrderByCriadoEmDesc(Long clienteId);
    List<PedidoAluguel> findAllByOrderByCriadoEmDesc();
}
