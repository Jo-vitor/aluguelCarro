package br.pucminas.aluguel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "agentes")
@Getter @Setter @NoArgsConstructor
public class Agente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false) @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;
    @Column(nullable = false) private String nome;
    @Column(nullable = false, unique = true) private String cnpj;
}
