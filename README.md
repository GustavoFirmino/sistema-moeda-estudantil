# Sistema de Moeda Estudantil

> Laboratório de Desenvolvimento de Software — PUC Minas  
> Professor: João Paulo Carneiro Aramuni | 4º Período — Engenharia de Software

Sistema para estimular o reconhecimento do mérito estudantil por meio de uma moeda virtual. Professores distribuem moedas como reconhecimento e alunos as trocam por vantagens em empresas parceiras.

---

## Grupo

| Nome | GitHub |
|------|--------|
| (adicionar membros) | (adicionar links) |

---

## Tecnologias Utilizadas

| Camada | Tecnologia |
|--------|-----------|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.5 |
| MVC / Views | Spring MVC + Thymeleaf |
| Persistência | Spring Data JPA + Hibernate |
| Banco de Dados | PostgreSQL 16 |
| Segurança | Spring Security 6 |
| Email | Spring Mail (JavaMailSender) |
| Front-end | Bootstrap 5.3 + Font Awesome 6 |
| Build | Maven 3.9 |
| Container BD | Docker Compose |

---

## Arquitetura

O sistema segue o padrão **MVC (Model-View-Controller)**:

```
src/main/java/com/pucminas/moedaestudantil/
├── config/       → Configurações (Security, Web, DataInitializer)
├── controller/   → Camada MVC — recebe requisições HTTP
├── service/      → Regras de negócio e orquestração
├── repository/   → Acesso a dados via Spring Data JPA
└── model/        → Entidades JPA (mapeamento ORM)
    └── enums/    → Enumerações do domínio

src/main/resources/
├── templates/    → Views Thymeleaf (HTML)
│   ├── auth/     → Login e cadastro
│   ├── aluno/    → Páginas do aluno
│   ├── professor/→ Páginas do professor
│   ├── empresa/  → Páginas da empresa
│   └── fragments/→ Componentes reutilizáveis (navbar, head, footer)
└── static/       → CSS, JS e imagens estáticas
```

---

## Pré-requisitos

- Java 21+
- Maven 3.9+
- Docker Desktop (para o PostgreSQL)

---

## Como Executar

### 1. Subir o banco de dados

```bash
docker compose up -d
```

Isso cria o container PostgreSQL com o banco `moeda_estudantil` na porta `5432`.

### 2. Executar a aplicação

```bash
./mvnw spring-boot:run
```

Acesse: **http://localhost:8080**

### 3. Usuários padrão (seeds)

| Perfil | E-mail | Senha |
|--------|--------|-------|
| Professor | `professor@pucminas.br` | `professor123` |
| Aluno | Cadastre via `/auth/cadastro-aluno` | — |
| Empresa | Cadastre via `/auth/cadastro-empresa` | — |

---

## Funcionalidades

### Aluno
- [x] Cadastro com nome, email, CPF, RG, endereço, instituição e curso
- [x] Login com email e senha
- [x] Dashboard com saldo de moedas e recebimentos recentes
- [x] Extrato completo (recebimentos + resgates)
- [x] Listagem de vantagens disponíveis
- [x] Resgate de vantagem (cupom gerado + email enviado)
- [x] Edição de perfil

### Professor
- [x] Login (pré-cadastrado pela instituição)
- [x] Dashboard com saldo e últimos envios
- [x] Envio de moedas para aluno com mensagem obrigatória
- [x] Extrato completo de envios
- [x] Recebimento acumulativo de 1.000 moedas/semestre

### Empresa Parceira
- [x] Cadastro com nome fantasia e CNPJ
- [x] Dashboard com estatísticas de vantagens
- [x] CRUD completo de vantagens (com upload de foto)
- [x] Notificação por email a cada resgate

---

## Diagramas UML

| Diagrama | Localização |
|----------|------------|
| Casos de Uso | `docs/sprint1/casos-de-uso.puml` |
| Diagrama de Classes | `docs/sprint1/diagrama-classes.puml` |
| Diagrama de Componentes | `docs/sprint1/diagrama-componentes.puml` |
| Modelo ER | `docs/sprint2/modelo-er.puml` |
| User Stories | `docs/sprint1/user-stories.md` |

> Para visualizar os arquivos `.puml`, use o plugin **PlantUML** no VS Code ou acesse [plantuml.com/plantuml](https://www.plantuml.com/plantuml).

---

## Processo de Desenvolvimento

| Sprint | Entrega |
|--------|---------|
| **Lab03S01** | Modelagem: Casos de Uso, User Stories, Diagrama de Classes, Diagrama de Componentes |
| **Lab03S02** | Modelo ER, estratégia ORM (JPA/Hibernate), CRUDs de aluno e empresa (versão inicial) |
| **Lab03S03** | CRUDs finais, integração completa, apresentação da arquitetura |

---

## Configuração de Email

Por padrão, o envio de email está **desativado** (os emails são apenas logados no console).  
Para ativar, edite o `application.properties`:

```properties
app.email.enabled=true
spring.mail.username=seu-email@gmail.com
spring.mail.password=sua-senha-de-app-gmail
```

> Use uma **Senha de App** do Google (não a senha normal da conta).

---

## Links Úteis

- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Thymeleaf Docs](https://www.thymeleaf.org/documentation.html)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Bootstrap 5 Docs](https://getbootstrap.com/docs/5.3/)
