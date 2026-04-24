#!/bin/bash
# ============================================================
# Commit da Sprint 3 — Lab03S03
# Execute este script no dia da entrega da Sprint 3
# ============================================================

set -e

echo "🚀 Preparando commit da Sprint 3..."

git add \
  README.md \
  src/main/java/com/pucminas/moedaestudantil/controller/ProfessorController.java \
  src/main/resources/templates/aluno/ \
  src/main/resources/templates/professor/ \
  src/main/resources/templates/empresa/

git commit -m "feat(sprint3): CRUDs finais e interface completa — Lab03S03

CRUDs finais de aluno e empresa parceira.
Interface Thymeleaf + Bootstrap 5 completa para todos os perfis.

Funcionalidades entregues:
- Aluno: dashboard, extrato, catálogo de vantagens, resgate com cupom
- Professor: dashboard, envio de moedas com mensagem, extrato
- Empresa: dashboard, CRUD de vantagens com upload de foto
- Notificações por e-mail (recebimento de moedas + cupom de resgate)
- Design responsivo com Bootstrap 5.3 e Font Awesome 6
- Máscaras de CPF/CNPJ e validação de formulários no front-end"

echo "✅ Sprint 3 commitada com sucesso!"
echo ""
echo "Próximo passo: git push origin main"
