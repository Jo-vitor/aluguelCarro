# Diagrama de Pacotes (Visão Lógica)

```mermaid
flowchart TB
    subgraph FE["Front-end — React + Vite (View)"]
        pages["pages"]
        components["components"]
        apiauth["api.js · auth.jsx"]
        pages --> components
        pages --> apiauth
    end

    subgraph BE["Back-end — Spring Boot (MVC)"]
        direction TB
        controller["controller (C)"]
        dto["dto"]
        service["service"]
        repository["repository"]
        model["model (M)"]
        security["security"]
        exception["exception"]
        config["config"]
        controller --> dto
        controller --> service
        service --> repository
        service --> model
        service --> exception
        repository --> model
        controller -.-> security
        config --> repository
    end

    DB[("Supabase<br/>PostgreSQL")]

    apiauth -- "HTTP/JSON + JWT" --> controller
    repository -- "JPA / JDBC" --> DB
```

## Subsistemas citados no enunciado

| Subsistema | Pacotes |
|---|---|
| Gestão de pedidos e contratos | `service`, `repository`, `model`, `controller` |
| Construção dinâmica das páginas web | Front-end React (`pages`, `components`) |
