#!/bin/bash
# ============================================================
# Commit da Sprint 2 — Lab03S02
# Execute este script no dia da entrega da Sprint 2
# ============================================================

set -e

echo "🚀 Preparando commit da Sprint 2..."

git add \
  docs/sprint2/ \
  src/main/resources/application.properties \
  src/main/java/com/pucminas/moedaestudantil/model/ \
  src/main/java/com/pucminas/moedaestudantil/repository/ \
  src/main/java/com/pucminas/moedaestudantil/config/ \
  src/main/java/com/pucminas/moedaestudantil/service/ \
  src/main/java/com/pucminas/moedaestudantil/security/ \
  src/main/java/com/pucminas/moedaestudantil/dto/ \
  src/main/java/com/pucminas/moedaestudantil/exception/ \
  src/main/java/com/pucminas/moedaestudantil/handler/ \
  src/main/java/com/pucminas/moedaestudantil/mapper/ \
  src/main/java/com/pucminas/moedaestudantil/event/ \
  src/main/java/com/pucminas/moedaestudantil/util/ \
  src/main/java/com/pucminas/moedaestudantil/constant/ \
  src/main/java/com/pucminas/moedaestudantil/MoedaEstudantilApplication.java \
  src/main/java/com/pucminas/moedaestudantil/controller/AuthController.java \
  src/main/java/com/pucminas/moedaestudantil/controller/AlunoController.java \
  src/main/java/com/pucminas/moedaestudantil/controller/EmpresaParceiraController.java \
  src/main/resources/templates/auth/ \
  src/main/resources/templates/fragments/ \
  src/main/resources/templates/error/ \
  src/main/resources/static/ \
  src/test/

git commit -m "feat(sprint2): modelo ER, ORM JPA/Hibernate, camadas e CRUDs iniciais — Lab03S02

Modelo ER documentado em docs/sprint2/modelo-er/v1/.
Estratégia de persistência: Spring Data JPA com Hibernate.
Herança JOINED para Usuario; SINGLE_TABLE para Transacao.

Implementações:
- Entidades JPA (Aluno, Professor, EmpresaParceira, Vantagem, Transacao)
- Repositórios Spring Data JPA com query methods derivados
- Serviços com lógica de negócio e validações (@Transactional)
- Spring Security 6 com autenticação por e-mail e redirect por role
- DTOs de cadastro, envio de moedas, vantagens e dashboards
- Exceções customizadas (SaldoInsuficiente, UsuarioNaoEncontrado, etc.)
- GlobalExceptionHandler com @ControllerAdvice
- Mappers AlunoMapper e VantagemMapper
- Eventos Spring (MoedasEnviadas, VantagemResgatada)
- Utilitários CpfCnpjUtils, MoedaUtils e SecurityUtils
- CRUD inicial de aluno e empresa parceira (front-end + back-end)
- DataInitializer: seed de instituições e professor padrão"

echo "✅ Sprint 2 commitada com sucesso!"
echo ""
echo "Próximo passo: git push origin main"
