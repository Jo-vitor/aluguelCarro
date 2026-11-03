package br.pucminas.aluguel.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "usuarios")
@Getter @Setter @NoArgsConstructor
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String senhaHash;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Papel papel;
}
