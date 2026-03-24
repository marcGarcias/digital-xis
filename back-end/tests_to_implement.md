# Lista de Testes Automatizados (Plano de Ação)

Este documento detalha os testes automatizados necessários para garantir a mitigação contínua das vulnerabilidades de segurança corrigidas, conforme os padrões existentes da aplicação.

## 🔴 Prioridade ALTA

### 1. SSRF e Path Traversal no ProxyController
A correção de validação de caminho implementada no `ProxyController` impede o acesso arbitrário ao CMS (Server-Side Request Forgery) e a injeção local de diretórios.

- **Tipo:** Teste de Integração (WebMvcTest).
- **Alvo:** `ProxyController.java`
- **Cenários:**
  - **[Path Traversal Múltiplo]:** Enviar requisições como `/uploads/../../api/users` e afirmar que o status HTTP retornado é `400 Bad Request`.
  - **[Escape Simples]:** Enviar `/uploads/../admin` e verificar status `400`.
  - **[URL Encoding Path Traversal]:** Enviar `%2e%2e%2f` ou instâncias semelhantes que tentem ofuscar o payload. Status esperado: `400`.
  - **[Prefixo Isolado]:** Tentar acessar `/api/interno` sem o prefixo `/uploads/`. Status esperado: `400`.
  - **[Positivo]:** Tentar acessar um arquivo válido `/uploads/logo.png` (com o *mock* do WebClient) e garantir que retorna `200 OK` e roteia a requisição de forma correta.

## 🟡 Prioridade MÉDIA

### 2. Rate Limiting Abrangente no RateLimitFilter
A implementação do Bucket4j foi expandida, adicionando um *bucket* geral de limite tolerante (60rpm) a todas as APIs de conteúdo e proxy, mantendo o limite estrito (5rpm) no contato.

- **Tipo:** Teste de Integração / E2E com MockMvc.
- **Alvo:** `RateLimitFilter.java` e Controllers
- **Cenários:**
  - **[Esgotamento do Bucket de Contato]:** Realizar 5 chamadas `POST /api/contact` sucessivas recebendo HTTP `200 OK`. A 6ª requisição no mesmo minuto deve retornar obrigatoriamente HTTP `429 Too Many Requests`.
  - **[Esgotamento do Bucket Global]:** Fazer 60 requisições simultâneas/repetitivas para `/api/content/about` e `/uploads/image.jpg`. Da 61ª em diante, afirmar que retornam HTTP `429`.
  - **[Isolamento de IP]:** Simular requisições HTTP 429 de um "IP Atacante" (mock de RemoteAddr), e garantir que uma requisição partindo de outro IP ("IP Usuário Válido") no mesmo segundo recebe HTTP `200`.

## 🟢 Prioridade BAIXA

### 3. Sanitização de E-mail (CRLF) no EmailService / ContactService
Já garantimos que caracteres de quebra de linha estão sendo formatados, mas testes unitários são essenciais para evitar regressão se o método `sanitizeSubject` for alterado futuramente.

- **Tipo:** Teste Unitário (Mockito / Extensão de `ContactServiceTest.java`).
- **Alvo:** `EmailService.java`
- **Cenários:**
  - **[Remoção de CRLF da String]:** Enviar um `ContactRequest` com subject = `Inquiry\r\nBcc: attacker@email.com` e garantir via argumento interceptado (`ArgumentCaptor` do Mockito) na chamada `mailSender.send` que a quebra de linha foi efetivamente ignorada, mitigando Header Injection.
  - **[Boundary Excedido]:** Mandar uma string de assunto de mais de 150 caracteres e afirmar que o valor efetivamente enviado é truncado corretamente em 150.
