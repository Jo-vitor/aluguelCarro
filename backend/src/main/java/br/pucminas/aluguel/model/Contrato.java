package br.pucminas.aluguel.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "contratos")
@Getter @Setter @NoArgsConstructor
public class Contrato {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false) @JoinColumn(name = "pedido_id", unique = true)
    private PedidoAluguel pedido;
    @ManyToOne(optional = false) private Agente agente;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private TipoProprietario tipo;
    @Column(nullable = false) private LocalDate dataAssinatura = LocalDate.now();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "credito_id")
    private ContratoCredito credito;
}
