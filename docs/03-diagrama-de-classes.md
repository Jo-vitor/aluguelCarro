# Diagrama de Classes (modelo de domínio)

```mermaid
classDiagram
    class Usuario {
        +Long id
        +String username
        +String senhaHash
        +Papel papel
    }
    class Cliente {
        +Long id
        +String nome
        +String rg
        +String cpf
        +String endereco
        +String profissao
    }
    class Rendimento {
        +String entidadeEmpregadora
        +BigDecimal valor
    }
    class Agente {
        +Long id
        +String nome
        +String cnpj
    }
    class Automovel {
        +Long id
        +String matricula
        +Integer ano
        +String marca
        +String modelo
        +String placa
        +TipoProprietario tipoProprietario
        +Long proprietarioId
    }
    class PedidoAluguel {
        +Long id
        +LocalDate dataInicio
        +LocalDate dataFim
        +BigDecimal valorMensal
        +boolean solicitaCredito
        +StatusPedido status
        +String parecer
        +Instant criadoEm
    }
    class Contrato {
        +Long id
        +TipoProprietario tipo
        +LocalDate dataAssinatura
    }
    class ContratoCredito {
        +Long id
        +BigDecimal valor
        +BigDecimal taxaJurosMensal
        +Integer parcelas
    }
    class Papel { <<enumeration>> CLIENTE EMPRESA BANCO }
    class StatusPedido { <<enumeration>> EM_ANALISE PARECER_POSITIVO PARECER_NEGATIVO CONTRATO_EXECUTADO CANCELADO }
    class TipoProprietario { <<enumeration>> CLIENTE EMPRESA BANCO }

    Usuario "1" -- "0..1" Cliente
    Usuario "1" -- "0..1" Agente
    Usuario --> Papel
    Cliente "1" *-- "0..3" Rendimento
    Cliente "1" -- "0..*" PedidoAluguel : introduz
    PedidoAluguel "0..*" --> "1" Automovel : aluga
    PedidoAluguel "0..*" --> "0..1" Agente : avaliadoPor
    PedidoAluguel --> StatusPedido
    PedidoAluguel "1" *-- "0..1" Contrato
    Contrato "0..*" --> "1" Agente : executadoPor
    Contrato --> TipoProprietario
    Contrato "1" *-- "0..1" ContratoCredito
    ContratoCredito "0..*" --> "1" Agente : bancoConcedente
    Automovel --> TipoProprietario
```

**Observação:** `Automovel.proprietarioId` é uma referência polimórfica (id de `Cliente` ou `Agente`,
conforme `tipoProprietario`). Ela é atualizada quando um contrato é executado.
