package br.pucminas.aluguel.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity @Table(name = "pedidos_aluguel")
@Getter @Setter @NoArgsConstructor
public class PedidoAluguel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) private Cliente cliente;
    @ManyToOne(optional = false) private Automovel automovel;
    @Column(nullable = false) private LocalDate dataInicio;
    @Column(nullable = false) private LocalDate dataFim;
    @Column(nullable = false, precision = 14, scale = 2) private BigDecimal valorMensal;
    @Column(nullable = false) private boolean solicitaCredito;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private StatusPedido status = StatusPedido.EM_ANALISE;
    @Column(length = 1000) private String parecer;
    @ManyToOne private Agente agenteAvaliador;
    @Column(nullable = false) private Instant criadoEm = Instant.now();

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Contrato contrato;
}
