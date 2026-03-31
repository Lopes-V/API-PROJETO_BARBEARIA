# 📚 GUIA DE TESTE DA API - AUTENTICAÇÃO JWT

## 🚀 COMO USAR A API AGORA

Sua API agora está **100% segura** com autenticação JWT. Aqui está um guia completo para testar tudo.

---

## 🔧 PRÉ-REQUISITOS

- Java 17+
- Maven (ou `./mvnw`)
- Banco MySQL rodando (porta 3356)
- Cliente HTTP (Postman, Insomnia, cURL ou mesmo seu navegador)

---

## ▶️ INICIANDO O SERVIDOR

```bash
# Windows
cd C:\Users\vinicius_lopes150\Desktop\sistema-de-barbearia\API-PROJETO_BARBEARIA
.\mvnw spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

Servidor rodará em: **http://localhost:8080**

---

## 🧪 SEQUÊNCIA DE TESTES

### TESTE 1️⃣: Registrar um Novo Usuário

**Requisição**:
```http
POST http://localhost:8080/usuarios/registrar
Content-Type: application/json

{
  "login": "barbeiro_joao",
  "senha": "senha123456"
}
```

**Resposta Esperada (201 Created)**:
```json
{
  "success": true,
  "message": "Usuário registrado com sucesso",
  "data": {
    "id": 1,
    "login": "barbeiro_joao"
  }
}
```

✅ **Sucesso!** Agora você tem um usuário registrado.

---

### TESTE 2️⃣: Fazer Login (Obter Token JWT)

**Requisição**:
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "login": "barbeiro_joao",
  "senha": "senha123456"
}
```

**Resposta Esperada (200 OK)**:
```json
{
  "success": true,
  "message": "Login realizado com sucesso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJBUEktQkFSQkVBUklBIiwic3ViIjoiYmFyYmVpcm9fam9hbyIsImV4cCI6MTY0NTEyMzQ1Nn0.abc123xyz...",
    "usuario": {
      "id": 1,
      "login": "barbeiro_joao"
    }
  }
}
```

**⚠️ IMPORTANTE**: Copie o `token` (é um texto MUITO longo)

---

### TESTE 3️⃣: Acessar Rota Protegida (com Token)

**Requisição**:
```http
GET http://localhost:8080/usuarios/me
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJBUEktQkFSQkVBUklBIiwic3ViIjoiYmFyYmVpcm9fam9hbyIsImV4cCI6MTY0NTEyMzQ1Nn0.abc123xyz...
```

**Resposta Esperada (200 OK)**:
```json
{
  "success": true,
  "message": "Dados do usuário recuperados com sucesso",
  "data": {
    "id": 1,
    "login": "barbeiro_joao"
  }
}
```

✅ **Sucesso!** Token está válido e funcionando.

---

### TESTE 4️⃣: Tentar Acessar Sem Token (Deve Falhar)

**Requisição**:
```http
GET http://localhost:8080/usuarios/me
(sem header Authorization)
```

**Resposta Esperada (401 Unauthorized)**:
```json
{
  "timestamp": "2026-03-31T09:15:30.123Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required...",
  "path": "/usuarios/me"
}
```

✅ **Esperado!** Sem token, acesso negado.

---

### TESTE 5️⃣: Tentar Login com Senha Errada

**Requisição**:
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "login": "barbeiro_joao",
  "senha": "senha_errada"
}
```

**Resposta Esperada (401 Unauthorized)**:
```json
{
  "success": false,
  "message": "Usuário ou senha incorretos"
}
```

✅ **Esperado!** Senha incorreta, login falha.

---

### TESTE 6️⃣: Tentar Registrar Usuário Duplicado

**Requisição** (mesmo usuário duas vezes):
```http
POST http://localhost:8080/usuarios/registrar
Content-Type: application/json

{
  "login": "barbeiro_joao",
  "senha": "outra_senha"
}
```

**Resposta Esperada (400 Bad Request)**:
```json
{
  "success": false,
  "message": "Erro ao registrar usuário: Usuário com este login já existe!"
}
```

✅ **Esperado!** Usuários duplicados não são permitidos.

---

## 🧭 EXEMPLO COM CURL

```bash
# Registrar usuário
curl -X POST http://localhost:8080/usuarios/registrar \
  -H "Content-Type: application/json" \
  -d '{"login":"joao","senha":"123456"}'

# Fazer login e capturar token (Linux/Mac)
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"login":"joao","senha":"123456"}' | grep -o '"token":"[^"]*' | cut -d'"' -f4)

# Usar token para acessar rota protegida
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/usuarios/me
```

---

## 🧭 EXEMPLO COM POSTMAN

1. **Criar Collection** chamada "Barbearia API"

2. **Request 1 - Registrar**:
   - URL: `POST http://localhost:8080/usuarios/registrar`
   - Body (raw, JSON):
   ```json
   {
     "login": "teste",
     "senha": "123456"
   }
   ```

3. **Request 2 - Login**:
   - URL: `POST http://localhost:8080/auth/login`
   - Body (raw, JSON):
   ```json
   {
     "login": "teste",
     "senha": "123456"
   }
   ```
   - Copie o `token` da resposta

4. **Request 3 - Dados do Usuário** (com Token):
   - URL: `GET http://localhost:8080/usuarios/me`
   - Headers:
     ```
     Authorization: Bearer eyJhbGci...
     Content-Type: application/json
     ```
   - Note: Em Postman você pode usar variáveis para não copiar/colar manualmente

---

## 🧭 EXEMPLO COM INSOMNIA

1. Mesma abordagem que Postman
2. Use a aba "Auth" em vez de adicionar header manualmente
3. Selecione "Bearer Token" e cole o token

---

## ⏰ INFORMAÇÕES IMPORTANTES

### Token Expira em...
- **2 horas** após geração
- Depois disso, você precisa fazer login novamente

### Formatos Esperados

**Header Authorization**:
```
Authorization: Bearer {token}
```
(note o espaço entre "Bearer" e o token)

**Data e Hora**:
```
2026-03-31T09:15:30
```
(formato ISO 8601)

---

## 🚨 ERROS COMUNS

### Erro: "Token inválido ou expirado"
**Solução**: Fazer login novamente para obter um novo token

### Erro: "Full authentication is required"
**Solução**: Você esqueceu de enviar o header `Authorization`

### Erro: "Usuário não autenticado"
**Solução**: Token inválido, expirado ou não existe este usuário

### Erro: "Usuário ou senha incorretos"
**Solução**: Verificar login/senha digitados corretamente

---

## 🔍 VERIFICAR BANCO DE DADOS

Para verificar se os usuários foram salvos com sucesso:

```sql
USE `barbearia-database`;
SELECT id, login FROM usuario;
```

---

## 🔐 SEGURANÇA

- ❌ NUNCA exponha seu token em URLs ou logs
- ❌ NUNCA compartilhe seu token com outras pessoas
- ❌ NUNCA commite tokens em Git
- ✅ Use HTTPS em produção (não HTTP)
- ✅ Regenere tokens periodicamente
- ✅ Use tokens de curta duração (2 horas é bom)

---

## 📊 RESPOSTA PADRÃO DA API

Todas as respostas seguem este padrão:

```json
{
  "success": true/false,
  "message": "Descrição do resultado",
  "data": {
    "..." : "..."
  }
}
```

---

## 🎯 CHECKLIST DE TESTE

- [ ] Registrar novo usuário ✅
- [ ] Fazer login e obter token ✅
- [ ] Acessar rota protegida com token ✅
- [ ] Tentar acessar sem token (deve falhar) ✅
- [ ] Tentar login com senha errada (deve falhar) ✅
- [ ] Tentar registrar usuário duplicado (deve falhar) ✅
- [ ] Verificar banco de dados ✅

---

## 🆘 AJUDA

Se algo não está funcionando:

1. Verifique se o servidor está rodando: http://localhost:8080/swagger-ui/index.html
2. Verifique os logs do Maven
3. Verifique se o MySQL está rodando
4. Leia as respostas de erro com atenção

---

**Pronto para testar!** 🚀


