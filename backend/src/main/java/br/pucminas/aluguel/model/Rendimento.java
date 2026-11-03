package br.pucminas.aluguel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.math.BigDecimal;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Rendimento {
    @Column(nullable = false)
    private String entidadeEmpregadora;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;
}
