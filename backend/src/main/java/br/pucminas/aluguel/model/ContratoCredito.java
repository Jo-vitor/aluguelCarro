package br.pucminas.aluguel.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity @Table(name = "contratos_credito")
@Getter @Setter @NoArgsConstructor
public class ContratoCredito {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) private Agente banco;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal valor;
    @Column(nullable = false, precision = 6, scale = 3) private BigDecimal taxaJurosMensal;
    @Column(nullable = false) private Integer parcelas;
}
