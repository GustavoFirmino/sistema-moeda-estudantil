# 📘 Relatório de Análise Crítica do Projeto 👨‍💻

## 1. Informações do grupo
- **🎓 Curso:** Engenharia de Software
- **📘 Disciplina:** Laboratório de Desenvolvimento de Software
- **🗓 Período:** 4° Período
- **👨‍🏫 Professor(a):** Prof. Dr. João Paulo Carneiro Aramuni
- **👥 Membros do Grupo:** Gustavo Pessoa Firmino Duarte

---

## 📌 2. Identificação do Projeto
- **Nome do projeto:** Meritum — Sistema de Moeda Estudantil
- **Integrantes do outro grupo:** PHnsilva e colaboradores
- **Link do repositório:** https://github.com/PHnsilva/Meritum
- **Pull requests submetidos pelo seu grupo:**

  | 👤 Integrante | 🔧 Refatoração | 🔗 Link do PR |
  |--------------|---------------|----------------|
  | :octocat: <a href="https://github.com/GustavoFirmino">Gustavo Firmino</a> | Eliminação de duplicação no serviço de e-mail (Extract Method / DRY) | https://github.com/PHnsilva/Meritum/pull/18 |
  | :octocat: <a href="https://github.com/GustavoFirmino">Gustavo Firmino</a> | Centralização dos schemas das rotas (Extract Module) | https://github.com/PHnsilva/Meritum/pull/19 |
  | :octocat: <a href="https://github.com/GustavoFirmino">Gustavo Firmino</a> | Hook `useResourceList` nas páginas de listagem (Extract Function) | https://github.com/PHnsilva/Meritum/pull/20 |

> Os PRs foram abertos via **fork** ([GustavoFirmino/Meritum](https://github.com/GustavoFirmino/Meritum)), com uma branch por refatoração, propondo merge para a `main` do repositório original. Conforme o enunciado, o merge é opcional para os proprietários.

---

## 🧱 3. Arquitetura e Tecnologias Utilizadas

O Meritum é dividido em duas aplicações independentes dentro de `Codigo/`: **Backend** (API REST) e **Frontend** (SPA), integradas por HTTP/JSON.

### 🏗️ Backend — Node.js + TypeScript + Fastify
O backend **não** usa Spring Boot: é uma API em **Node.js/TypeScript** com **Fastify 5**, **Prisma 6** como ORM e **PostgreSQL**. A organização é **modular por domínio** (`aluno`, `professor`, `instituicao`, `parceiro`, `moeda`, `vantagem`, `news`, `auth`), e cada módulo segue uma separação interna inspirada em Clean Architecture / Ports & Adapters:

- **action/** — rotas Fastify com schemas JSON (validação + documentação Swagger);
- **application/** — services com as regras de negócio (fábricas `createXxxService`);
- **domain/** — entidades, value objects (ex.: `CoinBalance`) e ports (interfaces de repositório);
- **infra/** — implementações Prisma dos repositórios.

Pontos técnicos que merecem destaque positivo:
- **Event bus interno** (`shared/domain/events`) desacopla os fluxos de negócio das notificações por e-mail — o envio de moedas publica `MoedasEnviadasEvent` e o handler dispara os e-mails;
- **Unit of Work** (`shared/infra/unit-of-work.ts`) garante atomicidade: o débito do professor usa **decremento condicional atômico** dentro da transação, com verificação eager antes apenas para mensagem de erro amigável (excelente tratamento de concorrência);
- **Autenticação JWT + RBAC** (`admin`, `professor`, `student`, `partner`, `institution`) com `preHandler` por rota, **rate limit** no endpoint público de cadastro e **Swagger UI** para documentação;
- Senhas com **scrypt** (nativo do Node, com salt por usuário) em `shared/security/password-hasher.ts`;
- **Cupom de resgate com QR Code** (lib `qrcode`) embutido nos e-mails de aluno e parceiro;
- E-mail com **fallback em camadas**: Resend (HTTP 443, compatível com Render) → SMTP → Ethereal (preview de teste).

Tecnologias: Fastify 5, Prisma 6, PostgreSQL, @fastify/jwt, @fastify/rate-limit, @fastify/swagger, nodemailer, resend, qrcode, amqplib (RabbitMQ), Vitest, ESLint, Docker.

### 🎨 Frontend — React + Vite (não usa Thymeleaf/Next.js)
SPA em **React 19 + TypeScript + Vite**, com módulos espelhando o backend, rotas protegidas por perfil, dashboards por papel e componentes compartilhados (`Alert`, `Button`, `ConfirmModal`, `PageHeader`, `SearchInput`). Deploy preparado para **Vercel** (`vercel.json`).

### 🔄 Integração entre Camadas
O backend expõe endpoints REST consumidos pela SPA. A separação em dois deploys (Render para API + Vercel para front) é limpa e reflete bem a arquitetura. Não há dualidade de camadas de view (sem server-side rendering), o que simplifica a manutenção — o custo é depender de CORS e de duas pipelines de deploy.

---

## 🗂️ 4. Organização do GitHub e Fluxo de Trabalho Colaborativo

### 4.1. Estrutura do Repositório e Documentação
- **Estrutura de pastas:** muito clara — `Codigo/Backend`, `Codigo/Frontend` e `Artefatos/` (modelagem UML completa: casos de uso, classes, ER, componentes, comunicação, implantação, 14 diagramas de sequência + geral, e capturas de tela).
- **README:** um dos pontos mais fortes do projeto — índice navegável, badges, descrição das funcionalidades, tabela de variáveis de ambiente, instruções com e sem Docker, tabela de scripts npm, endpoints da API, seção de segurança, telas e **troubleshooting**. O `.env.example` é comentado e explica inclusive o comportamento em produção (a aplicação se recusa a subir sem `JWT_SECRET` — decisão consciente e documentada).

### 4.2. Gerenciamento de Tarefas (Issues)
O grupo utilizou **15 issues** para rastrear funcionalidades e tarefas, o que demonstra uso real do quadro de trabalho. Nem todas as mudanças, porém, chegam à `main` vinculadas a uma issue/PR.

### 4.3. Fluxo de Trabalho (Pull Requests e Branches)
- Existem **5 PRs** no histórico (ex.: [#17 — feat/Vantagens](https://github.com/PHnsilva/Meritum/pull/17)), mas a maior parte dos **49 commits** foi feita **diretamente na `main`**;
- No momento da análise só existe a branch `main` — as feature branches não são mantidas, e não há branch `develop` nem *branch protection* aparente;
- Consequência: pouca revisão de código registrada; o histórico não permite rastrear quem revisou o quê.

### 4.4. Padrões de Commits e Versionamento
- **Padrão misto:** há bons commits em Conventional Commits (`feat: adciona qrcode ao resgate`, `fix: resolve TypeScript compilation errors...`, `docs: adicionar diagramas restantes`), mas também commits sem significado (`as`, `muda email`, `fix docker`, `fix codigo`, `seed`, `render`). A adoção integral de [Conventional Commits](https://www.conventionalcommits.org/pt-br/v1.0.0/) melhoraria a rastreabilidade;
- **Sem tags ou releases** — não é possível identificar o corte de cada sprint/release no histórico.

---

## 🖥️ 5. Dificuldade para Configuração do Ambiente

### 5.1. Requisitos de Linguagem e Ferramentas de Build
- Requisitos: **Node.js 20+**, npm e Docker. Não há Java/Maven — a stack é 100% JavaScript/TypeScript;
- `npm ci` no Backend e no Frontend baixou todas as dependências sem conflitos;
- O TypeScript de ambos compila **sem erros** (`npx tsc --noEmit`).

### 5.2. Configuração de Persistência e Variáveis de Ambiente
- O banco PostgreSQL sobe via `docker compose up --build` (compose na raiz orquestra banco + API + front), e o `.env.example` documenta todas as variáveis com comentários;
- **Único atrito real:** rodar `npx tsc --noEmit` logo após `npm ci` falha com dezenas de erros `TS7006 (implicitly any)` nos repositórios Prisma — os tipos do Prisma Client ainda não existem. A solução é executar **`npm run prisma:generate` antes de qualquer verificação de tipos**. O script está documentado na tabela do README, mas a *ordem* (generate antes de build/typecheck) não é mencionada — vale uma linha no README;
- As migrations e o seed (`npm run prisma:migrate`, `npm run prisma:seed`) estão prontos e documentados.

### 5.3. Aspectos a Analisar e Soluções Aplicadas
Passos executados pelo grupo na análise:
1. `git clone https://github.com/PHnsilva/Meritum.git`
2. `cd Codigo/Backend && npm ci` → OK
3. `npx tsc --noEmit` → **falhou** (tipos do Prisma ausentes)
4. `npx prisma generate` → OK — **typecheck passou a rodar 100% limpo**
5. `cd Codigo/Frontend && npm ci && npx tsc --noEmit` → OK sem nenhum ajuste

No geral, a configuração é **fácil** em comparação com a média: README completo, `.env.example` comentado, Docker Compose funcional e seeds prontos.

---

## 🔎 6. Análise de Qualidade do Código e Testes

### 6.1. Design e Princípios SOLID
- **Coesão:** os services de negócio são pequenos e coesos (ex.: `coin-service.ts` com 80 linhas fazendo apenas envio/extrato) — sem God Classes na camada de negócio;
- **Acoplamento:** o uso de ports (interfaces) + injeção nas fábricas mantém a camada de aplicação independente do Prisma — bom exemplo de **D**ependency Inversion;
- **Code smells encontrados:**
  - `shared/email/email-service.ts` (472 linhas): **Duplicated Code** massivo — 10 templates repetindo o mesmo boilerplate HTML (endereçado no PR [#18](https://github.com/PHnsilva/Meritum/pull/18));
  - Schemas `errorSchema`/`messageSchema`/paginação **redeclarados em 8 arquivos** de rotas (endereçado no PR [#19](https://github.com/PHnsilva/Meritum/pull/19));
  - Páginas de listagem do frontend com estado/fluxo duplicado (endereçado no PR [#20](https://github.com/PHnsilva/Meritum/pull/20));
  - `app.ts` (219 linhas) concentra toda a composição de dependências — funcional, mas tende a crescer a cada módulo novo.

### 6.2. Testabilidade e Cobertura
- Há **testes com Vitest** em `Backend/src/tests/` (`auth.test.ts`, `coin.test.ts`, `institution.test.ts`, `student.test.ts`) com helpers/factories próprios — o teste de moedas cobre a regra de negócio central (saldo insuficiente, instituições diferentes);
- Existe script `npm run test:coverage`, mas a cobertura não é publicada;
- **Lacunas:** os módulos `vantagem` (resgate/cupom) e `parceiro` (aprovação) não têm testes — justamente fluxos críticos da Release 3;
- O frontend não possui testes.

### 6.3. Segurança e Tratamento de Erros (OWASP Top 10)
- **Validação de entrada:** os schemas JSON do Fastify validam formato/limites de todos os bodies e querystrings — bom controle de *input* na borda;
- **Credenciais:** senhas com **scrypt + salt** (adequado); `JWT_SECRET` obrigatório em produção (a aplicação não sobe sem ele — ótima decisão); nenhuma credencial hardcoded encontrada (tudo via `.env`);
- **Rate limit** no cadastro público mitiga abuso;
- **Tratamento de exceções:** centralizado em `shared/errors/domain-errors.ts` + `shared/responder/error-responder.ts`, retornando mensagens controladas sem vazar stack traces;
- **Ponto de atenção:** o Prisma parametriza as queries (sem SQL injection), mas os e-mails HTML interpolam nome do aluno/motivo sem sanitização — um motivo contendo HTML seria renderizado no cliente de e-mail do destinatário (risco baixo, mas fácil de sanitizar).

---

## 🚀 7. Sugestões de Melhorias

1. **Padronizar 100% dos commits** em Conventional Commits e ativar **branch protection** na `main`, exigindo PR + revisão (hoje a maioria dos commits vai direto para a `main`, incluindo mensagens como `as` e `fix codigo`).
2. **Criar tags/releases por sprint** (`v1.0-release1`, `v2.0-release2`, `v3.0-release3`) para que o professor e o próprio grupo naveguem o histórico por marcos.
3. **CI com GitHub Actions**: pipeline rodando `prisma generate` + `tsc --noEmit` + `vitest` + `eslint` em cada PR — o projeto já tem todos os scripts prontos, falta só o workflow.
4. **Documentar a ordem `prisma:generate` → build/typecheck** no README (único atrito real do setup) e considerar um script `postinstall` que gere o client automaticamente.
5. **Ampliar testes para os módulos `vantagem` e `parceiro`** (resgate com débito atômico, geração de cupom e aprovação de parceiro) e publicar a cobertura do `test:coverage` no README.
6. **Sanitizar/escapar interpolações nos templates de e-mail** (nome, motivo) para eliminar o risco de HTML injetado no e-mail do destinatário.
7. **Modularizar o composition root** (`app.ts`): extrair o registro de cada módulo (repositórios + service + rotas) para um arquivo `<modulo>/register.ts`, mantendo `app.ts` apenas como orquestrador.

---

## 🔧 8. Refatorações Propostas (3 partes do código)

### 1️⃣ Refatoração 1 – Eliminação de duplicação no serviço de e-mail

**Arquivo:** `Codigo/Backend/src/shared/email/email-service.ts`
**Pull Request:** https://github.com/PHnsilva/Meritum/pull/18

#### 🔴 Antes
Cada um dos 10 e-mails repetia o boilerplate completo (e os pares parceiro/instituição eram cópias um do outro):

```ts
function buildPartnerRegistrationHtml(name: string): string {
  return `
<!DOCTYPE html>
<html lang="pt-BR">
<head><meta charset="UTF-8"><title>Meritum - Cadastro Recebido</title></head>
<body style="font-family:Arial,sans-serif;background:#f4f4f4;padding:24px">
  <div style="max-width:560px;margin:0 auto;background:#fff;border-radius:8px;padding:32px">
    <h1 style="color:#2563eb;margin-top:0">Meritum</h1>
    <h2 style="color:#1e293b">Solicitacao recebida!</h2>
    <p>Ola, <strong>${name}</strong>!</p>
    <p>Recebemos a solicitacao de cadastro da sua empresa como parceira no sistema Meritum.</p>
    ...
    <hr style="border:none;border-top:1px solid #e2e8f0;margin:24px 0">
    <p style="color:#94a3b8;font-size:12px">Sistema de Moeda Estudantil - Meritum</p>
  </div>
</body>
</html>`;
}

// ... e mais 9 funções praticamente idênticas, além de QR Code e try/catch duplicados:
export async function sendStudentCouponEmail(...): Promise<void> {
  try {
    const qrCodeBuffer = await QRCode.toBuffer(code, { width: 200, margin: 1, color: {...} });
    await sendEmail({ ... });
  } catch (err) {
    console.error('[email] Falha ao enviar cupom ao aluno:', err);
  }
}
```

#### 🟢 Depois
Layout e blocos extraídos; cada e-mail declara apenas o conteúdo específico:

```ts
function renderEmailLayout(tabTitle: string, heading: string, bodyHtml: string): string {
  return `<!DOCTYPE html> ... <h1>Meritum</h1><h2>${heading}</h2>${bodyHtml} ...`;
}

function buildRegistrationReceivedHtml(name: string, entityDescription: string): string {
  return renderEmailLayout('Cadastro Recebido', 'Solicitacao recebida!', `
    <p>Ola, <strong>${name}</strong>!</p>
    <p>Recebemos a solicitacao de cadastro ${entityDescription} no sistema Meritum.</p>
    ...`);
}

async function buildQrCodeAttachment(code: string, contentId: string): Promise<EmailAttachment> {
  const content = await QRCode.toBuffer(code, { width: 200, margin: 1, ... });
  return { filename: 'qrcode.png', content, contentId };
}

async function sendEmailSafely(errorLabel: string, opts: SendEmailOptions): Promise<void> {
  try { await sendEmail(opts); }
  catch (err) { console.error(`[email] ${errorLabel}:`, err); }
}

export async function sendPartnerRegistrationEmail(partnerEmail: string, partnerName: string) {
  await sendEmailSafely('Falha ao confirmar registro de parceiro', {
    to: partnerEmail,
    subject: 'Meritum: solicitacao de cadastro recebida',
    html: buildRegistrationReceivedHtml(partnerName, 'da sua empresa como parceira')
  });
}
```

#### ✔ Tipo de refatoração aplicada
- **Extract Method** / **Replace Duplicated Code with Method** (DRY)

#### 📝 Justificativa
O arquivo cai de 472 para ~340 linhas (−269/+140) sem nenhuma mudança de comportamento. Alterações visuais (logo, rodapé, cores) passam a ser feitas em **um** lugar; novos e-mails custam só o corpo específico; o tratamento de falha de envio fica consistente por construção. Verificado com `tsc --noEmit` limpo.

---

### 2️⃣ Refatoração 2 – Centralização dos schemas das rotas

**Arquivos:** 8 arquivos `*.routes.ts` + novo `Codigo/Backend/src/shared/http/route-schemas.ts`
**Pull Request:** https://github.com/PHnsilva/Meritum/pull/19

#### 🔴 Antes
O mesmo schema era redeclarado em 6 arquivos (e o envelope de paginação em 4):

```ts
// student.routes.ts, institution.routes.ts, coin.routes.ts,
// partner-company.routes.ts, professor.routes.ts, advantage.routes.ts:
const errorSchema = { type: 'object', properties: { message: { type: 'string' } } } as const;

const paginatedStudentSchema = {
  type: 'object',
  properties: {
    data: { type: 'array', items: studentResponseSchema },
    total: { type: 'integer' },
    page: { type: 'integer' },
    limit: { type: 'integer' },
    totalPages: { type: 'integer' }
  }
} as const;
```

#### 🟢 Depois
```ts
// shared/http/route-schemas.ts
export const errorSchema = { type: 'object', properties: { message: { type: 'string' } } } as const;
export const messageSchema = { type: 'object', properties: { message: { type: 'string' } } } as const;

export function paginatedSchema<TItems>(itemsSchema: TItems) {
  return {
    type: 'object',
    properties: {
      data: { type: 'array', items: itemsSchema },
      total: { type: 'integer' },
      page: { type: 'integer' },
      limit: { type: 'integer' },
      totalPages: { type: 'integer' }
    }
  } as const;
}

// student.routes.ts
import { errorSchema, paginatedSchema } from '../../../shared/http/route-schemas.js';
const paginatedStudentSchema = paginatedSchema(studentResponseSchema);
```

#### ✔ Tipo de refatoração aplicada
- **Extract Module** / **Remove Duplicated Code**

#### 📝 Justificativa
Remove 61 linhas duplicadas (+17) e garante que o contrato de resposta da API (erros e paginação documentados no Swagger) seja consistente por construção — uma mudança de formato passa a ser feita num único ponto, espelhando o `PaginatedResult<T>` que já existia em `shared/pagination`. Verificado com `tsc --noEmit` limpo.

---

### 3️⃣ Refatoração 3 – Hook `useResourceList` nas páginas de listagem

**Arquivos:** `Codigo/Frontend/src/modules/aluno/pages/AlunoListPage.tsx`, `Codigo/Frontend/src/modules/professor/pages/ProfessorListPage.tsx` + novo `Codigo/Frontend/src/shared/hooks/useResourceList.ts`
**Pull Request:** https://github.com/PHnsilva/Meritum/pull/20

#### 🔴 Antes
Cada página de listagem repetia o mesmo estado e fluxo (~40 linhas):

```tsx
const [alunos, setAlunos] = useState<Aluno[]>([]);
const [loading, setLoading] = useState(true);
const [error, setError] = useState('');
const [confirmDeleteId, setConfirmDeleteId] = useState<string | null>(null);

async function loadAlunos() {
  setLoading(true);
  setError('');
  try {
    setAlunos(await listAlunos(...));
  } catch (loadError) {
    setError(loadError instanceof Error ? loadError.message : 'Nao foi possivel carregar alunos');
  } finally {
    setLoading(false);
  }
}

async function handleDeleteConfirmed() {
  if (!confirmDeleteId) return;
  const id = confirmDeleteId;
  setConfirmDeleteId(null);
  try {
    await deleteAluno(id);
    await loadAlunos();
  } catch (deleteError) { setError(...); }
}

useEffect(() => { void loadAlunos(); }, []);
```

#### 🟢 Depois
```tsx
const {
  items: alunos, loading, error, load: loadAlunos,
  confirmDeleteId, setConfirmDeleteId, handleDeleteConfirmed
} = useResourceList<Aluno>({
  fetcher: () => listAlunos(isInstitution ? (user?.id ?? undefined) : undefined),
  remover: deleteAluno,
  loadErrorMessage: 'Nao foi possivel carregar alunos',
  deleteErrorMessage: 'Nao foi possivel remover o aluno'
});
```

#### ✔ Tipo de refatoração aplicada
- **Extract Function (custom hook)** / **Remove Duplicated Code**

#### 📝 Justificativa
Sem mudança de comportamento ou de UI: o JSX das páginas permanece idêntico. O boilerplate de carregamento/erro/exclusão passa a viver num único hook tipado, e `ParceiroListPage`/`InstituicaoListPage` — que repetem o mesmo padrão — podem adotá-lo em seguida (deixadas fora do PR para mantê-lo pequeno e fácil de revisar). Verificado com `tsc --noEmit` limpo.

---

## 9. 📄 Conclusão

A análise crítica do Meritum revelou um projeto **acima da média** em arquitetura: modularização por domínio com ports & adapters, event bus para notificações, Unit of Work com débito atômico contra condições de corrida, autenticação JWT/RBAC bem aplicada e um README exemplar. As fragilidades estão menos no código e mais no **processo**: commits direto na `main` com mensagens inconsistentes, ausência de tags/releases e de CI, e lacunas de teste justamente nos fluxos da Release 3 (vantagens/resgate).

As três refatorações entregues atacaram o principal *code smell* encontrado — **duplicação** — em três camadas diferentes do sistema (serviço de e-mail, contrato das rotas e páginas do frontend), todas verificadas com typecheck limpo e sem mudança de comportamento, priorizando compreensão e manutenção do sistema.

O processo reforçou a importância da **refatoração contínua**, da **revisão estruturada de código** via pull requests e de **boas práticas de engenharia colaborativa** para manter um software sustentável ao longo do ciclo de vida.

---

## 10. 📚 Referências
- Revisando alterações em Pull Requests:
  https://docs.github.com/pt/pull-requests/collaborating-with-pull-requests/reviewing-changes-in-pull-requests/commenting-on-a-pull-request
- Guia oficial de **Conventional Commits**:
  https://www.conventionalcommits.org/pt-br/v1.0.0/
- Documentação do Fastify (validação e serialização):
  https://fastify.dev/docs/latest/Reference/Validation-and-Serialization/
- Documentação do Prisma ORM:
  https://www.prisma.io/docs
- Refactoring (Martin Fowler) — catálogo de refatorações:
  https://refactoring.com/catalog/
- OWASP Cheat Sheets (segurança em aplicações web):
  https://cheatsheetseries.owasp.org/

---
