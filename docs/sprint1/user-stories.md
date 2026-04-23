# User Stories — Sistema de Moeda Estudantil

## Épico 1: Cadastro e Autenticação

| ID | Ator | Story | Critérios de Aceitação |
|----|------|-------|------------------------|
| US01 | Aluno | Como **aluno**, quero me cadastrar no sistema informando nome, email, CPF, RG, endereço, instituição e curso, para que eu possa participar do sistema de mérito. | - Campos obrigatórios validados; - CPF e email únicos; - Senha mínima 6 chars; - Redirect para login após cadastro |
| US02 | Empresa | Como **empresa parceira**, quero me cadastrar no sistema com nome fantasia, email e CNPJ, para que eu possa oferecer vantagens aos alunos. | - CNPJ e email únicos; - Confirmação de senha; - Redirect para login |
| US03 | Todos | Como **usuário**, quero fazer login com meu email e senha, para que eu possa acessar as funcionalidades do meu perfil. | - Autenticação via Spring Security; - Redirect para dashboard do perfil correto; - Mensagem de erro em credenciais inválidas |
| US04 | Todos | Como **usuário**, quero fazer logout com segurança, para que minha sessão seja encerrada. | - Sessão invalidada; - Cookie removido; - Redirect para login |

## Épico 2: Distribuição de Moedas (Professor)

| ID | Ator | Story | Critérios de Aceitação |
|----|------|-------|------------------------|
| US05 | Professor | Como **professor**, quero visualizar meu saldo de moedas no dashboard, para que eu saiba quantas moedas posso distribuir. | - Saldo atualizado em tempo real; - 1.000 moedas iniciais por semestre |
| US06 | Professor | Como **professor**, quero enviar moedas para um aluno com uma mensagem obrigatória, para que eu possa reconhecer seu mérito. | - Saldo suficiente verificado; - Mensagem obrigatória; - Email automático ao aluno; - Saldo deduzido imediatamente |
| US07 | Professor | Como **professor**, quero consultar o extrato de todos os meus envios de moedas, para que eu possa acompanhar minhas distribuições. | - Lista em ordem cronológica decrescente; - Mostra aluno, motivo, valor e data |

## Épico 3: Conta do Aluno

| ID | Ator | Story | Critérios de Aceitação |
|----|------|-------|------------------------|
| US08 | Aluno | Como **aluno**, quero receber uma notificação por email ao receber moedas, para que eu saiba quem me reconheceu e por quê. | - Email automático com nome do professor, valor e mensagem |
| US09 | Aluno | Como **aluno**, quero visualizar meu saldo e extrato de transações, para que eu possa acompanhar meus recebimentos e resgates. | - Dashboard com saldo em destaque; - Histórico separado por recebimentos e resgates |
| US10 | Aluno | Como **aluno**, quero ver todas as vantagens disponíveis com custo em moedas, para que eu possa escolher o que quero resgatar. | - Lista com foto, descrição e custo; - Botão desabilitado se saldo insuficiente |
| US11 | Aluno | Como **aluno**, quero resgatar uma vantagem, para que eu possa utilizá-la na empresa parceira. | - Saldo verificado antes do resgate; - Código único gerado; - Email com cupom enviado ao aluno; - Email de notificação enviado à empresa |
| US12 | Aluno | Como **aluno**, quero editar meus dados de perfil, para que minhas informações estejam sempre atualizadas. | - Pode editar nome, RG, endereço e curso; - Email e CPF não editáveis |

## Épico 4: Gestão de Vantagens (Empresa)

| ID | Ator | Story | Critérios de Aceitação |
|----|------|-------|------------------------|
| US13 | Empresa | Como **empresa parceira**, quero cadastrar vantagens com nome, descrição, foto e custo em moedas, para que os alunos possam resgatá-las. | - Upload de foto opcional; - Custo mínimo 1 moeda; - Vantagem ativa por padrão |
| US14 | Empresa | Como **empresa parceira**, quero editar as informações de uma vantagem, para que eu possa manter as ofertas atualizadas. | - Edição de nome, descrição, custo e foto; - Foto antiga mantida se nenhuma nova enviada |
| US15 | Empresa | Como **empresa parceira**, quero desativar uma vantagem, para que ela não apareça mais para os alunos. | - Vantagem marcada como inativa; - Desaparece da lista de vantagens disponíveis |
| US16 | Empresa | Como **empresa parceira**, quero receber um email quando um aluno resgatar uma vantagem, para que eu possa conferir o cupom presencialmente. | - Email com código do cupom, nome do aluno e vantagem |
