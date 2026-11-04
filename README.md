# 🚗 Sistema de Aluguel de Carros

> Plataforma web para **efetuar, modificar, consultar e cancelar pedidos de aluguel de automóveis**,
> com análise financeira por agentes (empresas e bancos) e execução de contratos.
> Projeto do **Laboratório 02 — DIAW (Desenvolvimento e Integração de Aplicações Web)**, Engenharia de Software, PUC Minas.

![Java](https://img.shields.io/badge/Java-25-007ec6?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.x-007ec6?style=for-the-badge&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9+-007ec6?style=for-the-badge&logo=apachemaven&logoColor=white)
![React](https://img.shields.io/badge/React-19-007ec6?style=for-the-badge&logo=react&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-7-007ec6?style=for-the-badge&logo=vite&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-PostgreSQL-007ec6?style=for-the-badge&logo=supabase&logoColor=white)

## 📚 Índice
- [Sobre o Projeto](#-sobre-o-projeto) · [Funcionalidades](#-funcionalidades-principais) · [Tecnologias](#-tecnologias-utilizadas)
- [Arquitetura](#-arquitetura) · [Instalação e Execução](#-instalação-e-execução) · [API](#-api-rest)
- [Estrutura de Pastas](#-estrutura-de-pastas) · [Demonstração](#-demonstração) · [Testes](#-testes)
- [Documentações](#-documentações-utilizadas) · [Autores](#-autores) · [Licença](#-licença)

## 📝 Sobre o Projeto
O sistema apoia a gestão de aluguéis de automóveis pela Internet. O **cliente** cria e acompanha pedidos;
os **agentes** (empresas e bancos) fazem a análise financeira e, com parecer positivo, executam o contrato.
O contrato define de quem é a propriedade do automóvel (cliente, empresa ou banco) e pode estar associado
a um **contrato de crédito** concedido por um banco. O acesso exige cadastro prévio e login (usuário e senha).

## ✨ Funcionalidades Principais
- 🔐 **Autenticação:** cadastro e login (clientes, empresas e bancos) com JWT; rotas protegidas por papel.
- 👤 **CRUD de cliente:** RG, CPF, nome, endereço, profissão e até **3** rendimentos com entidade empregadora.
- 📄 **Pedidos de aluguel:** introduzir, modificar, consultar e cancelar; **status** visível em uma trilha (análise → contrato).
- 🏦 **Agentes:** avaliação financeira (parecer positivo/negativo), modificação de pedidos e execução de contrato.
- 💳 **Crédito:** contrato de crédito opcional, concedido por banco (valor, juros mensais, parcelas).
- 🚘 **Automóveis:** matrícula, ano, marca, modelo e placa; propriedade atualizada conforme o contrato.

## 🛠 Tecnologias Utilizadas
| Camada | Tecnologia |
|---|---|
| Linguagem | Java — JDK 25 |
| Back-end | Spring Boot 4.0.x (Web MVC, Data JPA, Validation, Security + OAuth2 Resource Server) |
| Build | Apache Maven |
| Front-end | React + Vite (React Router, Context API) |
| Banco de dados | Supabase (PostgreSQL) |
| Autenticação | Login obrigatório (usuário e senha), BCrypt + JWT (HS256) |

## 🏗 Arquitetura
Arquitetura **MVC** em camadas no back-end; o front-end React é a camada de **View**.

| MVC | Onde está |
|---|---|
| **Model** | `model` (entidades JPA), `repository` (acesso a dados), `service` (regras de negócio) |
| **View** | `frontend/src` (React + Vite): páginas e componentes |
| **Controller** | `controller` (REST) recebe HTTP, valida (`dto`) e delega aos serviços |

Diagramas UML (Mermaid, renderizados no GitHub) em [`docs/`](docs):
[Casos de Uso](docs/01-casos-de-uso.md) · [Histórias do Usuário](docs/02-historias-de-usuario.md) ·
[Classes](docs/03-diagrama-de-classes.md) · [Pacotes](docs/04-diagrama-de-pacotes.md).

**Fluxo do pedido:** `EM_ANALISE` → (agente) `PARECER_POSITIVO` / `PARECER_NEGATIVO` → (agente) `CONTRATO_EXECUTADO`;
o cliente pode `CANCELADO` enquanto não houver contrato executado.

## 🚀 Instalação e Execução
### Pré-requisitos
JDK 25, Maven 3.9+, Node.js 20+ e um projeto no [Supabase](https://supabase.com).

### Variáveis de ambiente
**Back-end** — copie `backend/.env.example` para `backend/.env` (lido automaticamente):

| Variável | Descrição |
|---|---|
| `SUPABASE_DB_URL` | JDBC do Supabase (*Project Settings → Database → Connection string*), ex.: `jdbc:postgresql://<host>:5432/postgres?sslmode=require` |
| `SUPABASE_DB_USER` / `SUPABASE_DB_PASSWORD` | Usuário e senha do banco |
| `JWT_SECRET` | Chave com 32+ caracteres |
| `CORS_ORIGINS` | Origem do front (padrão `http://localhost:5173`) |

**Front-end** — `frontend/.env`: `VITE_API_URL=http://localhost:8080`.

> Use a conexão **direta** ou o **Session pooler** (porta 5432). Se usar o Transaction pooler (6543), acrescente `&prepareThreshold=0` à URL.

### Banco de dados
Com `ddl-auto: update`, o Hibernate cria as tabelas (`usuarios`, `clientes`, `cliente_rendimentos`, `agentes`,
`automoveis`, `pedidos_aluguel`, `contratos`, `contratos_credito`) no Supabase na primeira execução.
Quatro automóveis de exemplo são inseridos automaticamente.

### Executar
```bash
# Terminal 1 — back-end (http://localhost:8080)
cd backend && mvn spring-boot:run

# Terminal 2 — front-end (http://localhost:5173)
cd frontend && npm install && npm run dev
```

## 🔌 API REST
| Método | Rota | Acesso |
|---|---|---|
| POST | `/api/auth/registro/cliente` · `/api/auth/registro/agente` · `/api/auth/login` | Público |
| GET · PUT · DELETE | `/api/clientes/me` | Cliente |
| GET | `/api/clientes` · `/api/clientes/{id}` | Agentes |
| GET · POST | `/api/automoveis` · GET `/api/automoveis/{id}` | Autenticado |
| POST | `/api/pedidos` | Cliente |
| GET | `/api/pedidos` · `/api/pedidos/{id}` | Cliente (só os seus) / Agente (todos) |
| PUT | `/api/pedidos/{id}` | Cliente (em análise) / Agente |
| POST | `/api/pedidos/{id}/cancelar` | Cliente |
| POST | `/api/pedidos/{id}/avaliar` | Agentes |
| POST | `/api/pedidos/{id}/contrato` | Agentes (crédito: somente Banco) |

## 📁 Estrutura de Pastas
```
aluguel-carros/
├── backend/                     # Spring Boot (Maven)
│   └── src/main/java/br/pucminas/aluguel/
│       ├── controller/  service/  repository/  model/
│       ├── dto/  security/  exception/  config/
├── frontend/                    # React + Vite
│   └── src/ pages/ components/ api.js auth.jsx
└── docs/                        # Modelagem UML e histórias do usuário
```

## 🎬 Demonstração
Roteiro sugerido para a apresentação:
1. Cadastrar um **cliente** e um **banco** (abas em `/registro`).
2. Como cliente: **Novo pedido** (marcando crédito) e acompanhar a trilha de status.
3. Como banco: abrir o pedido, registrar **parecer positivo** e **executar o contrato** com crédito.
4. Voltar ao cliente: status **Contrato executado**; em *Automóveis*, a propriedade mudou.
5. Comparar o que foi implementado com os diagramas de `docs/` e registrar as alterações.

## 🧪 Testes
Ainda não há testes automatizados. Validação manual pelo roteiro acima; o `npm run build` do front-end compila sem erros.

## 📖 Documentações utilizadas
[Spring Boot](https://spring.io/projects/spring-boot) · [Spring Security](https://spring.io/projects/spring-security) ·
[Supabase](https://supabase.com/docs) · [React](https://react.dev) · [Vite](https://vite.dev)

## 👨‍💻 Autores
João Vitor Soares— Engenharia de Software, PUC Minas.

## 📄 Licença
Projeto acadêmico, sem licença de redistribuição definida.
