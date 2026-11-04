# Diagrama de Casos de Uso

```mermaid
flowchart LR
    Cliente([👤 Cliente])
    Agente([🏢 Agente])
    Empresa([Empresa]) -->|é um| Agente
    Banco([Banco]) -->|é um| Agente

    subgraph Sistema["Sistema de Aluguel de Carros"]
        UC1(Cadastrar-se)
        UC2(Autenticar-se / login)
        UC3(Gerenciar cadastro do cliente<br/>consultar · modificar · excluir)
        UC4(Introduzir pedido de aluguel)
        UC5(Modificar pedido)
        UC6(Consultar pedidos e status)
        UC7(Cancelar pedido)
        UC8(Cadastrar automóvel)
        UC9(Avaliar pedido<br/>análise financeira)
        UC10(Executar contrato)
        UC11(Conceder contrato de crédito)
    end

    Cliente --- UC1 & UC2 & UC3 & UC4 & UC5 & UC6 & UC7 & UC8
    Agente --- UC1 & UC2 & UC5 & UC6 & UC8 & UC9 & UC10
    Banco --- UC11
    UC10 -.->|«extend» se houver crédito| UC11
    UC4 -.->|«include»| UC2
    UC9 -.->|«include»| UC2
```

## Descrição resumida

| Caso de uso | Ator | Regra principal |
|---|---|---|
| Cadastrar-se | Cliente, Agente | Usuário único; cliente informa RG, CPF, nome, endereço, profissão e até 3 rendimentos |
| Autenticar-se | Todos | Sistema só é usado após login (usuário e senha) |
| Gerenciar cadastro | Cliente | Consulta, altera e exclui o próprio cadastro |
| Introduzir pedido | Cliente | Pedido nasce **EM_ANALISE** |
| Modificar pedido | Cliente / Agente | Cliente: só em análise. Agente: enquanto não encerrado |
| Cancelar pedido | Cliente | Não cancela pedido com contrato executado |
| Avaliar pedido | Agente | Parecer financeiro positivo ou negativo |
| Executar contrato | Agente | Exige parecer positivo; define quem é dono do automóvel |
| Conceder crédito | Banco | Somente se o pedido solicitou crédito |
