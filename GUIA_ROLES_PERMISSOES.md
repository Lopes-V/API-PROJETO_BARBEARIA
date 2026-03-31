# 🔐 GUIA DE ROLES E PERMISSÕES - AUTORIZAÇÃO

## 📋 O QUE FOI IMPLEMENTADO

Agora sua API possui um sistema de **Roles (Papéis)** que permite controlar quem pode acessar cada rota com precisão.

---

## 👥 ROLES DISPONÍVEIS

| Role | Código | Descrição |
|------|--------|-----------|
| **ADMIN** | `ROLE_ADMIN` | Administrador da barbearia - Acesso total |
| **BARBEIRO** | `ROLE_BARBEIRO` | Barbeiro - Pode gerenciar seus agendamentos |
| **CLIENTE** | `ROLE_CLIENTE` | Cliente - Pode agendar serviços |
| **RECEPCIONISTA** | `ROLE_RECEPCIONISTA` | Recepcionista - Gerencia agendamentos e clientes |

---

## 🛡️ COMO APLICAR AUTORIZAÇÃO NAS ROTAS

### Sintaxe Básica

```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> exemploAdmin() { ... }

@PreAuthorize("hasRole('CLIENTE') or hasRole('RECEPCIONISTA')")
public ResponseEntity<?> exemploMultiplasRoles() { ... }

@PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
public ResponseEntity<?> exemploAlternativo() { ... }
```

---

## 📝 EXEMPLO: PROTEGER ControllerBarbeiro

```java
package com.inicio.back_end.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/barbeiros")
public class ControllerBarbeiro {
    
    // Qualquer um autenticado pode VER os barbeiros
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getBarbeiros() {
        // ...
    }
    
    // Qualquer um autenticado pode VER detalhes de um barbeiro
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getBarbeiroById(@PathVariable Long id) {
        // ...
    }
    
    // Apenas ADMIN pode REGISTRAR novo barbeiro
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> cadastrarBarbeiro(@RequestBody DTOBarbeiro dto) {
        // ...
    }
    
    // Apenas ADMIN pode EDITAR barbeiro
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editarBarbeiro(@RequestBody DTOBarbeiro dto, @PathVariable Long id) {
        // ...
    }
    
    // Apenas ADMIN pode DELETAR barbeiro
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletarBarbeiro(@PathVariable Long id) {
        // ...
    }
}
```

---

## 🗓️ EXEMPLO: PROTEGER ControllerAgendamento

```java
@RestController
@RequestMapping("/agendamentos")
public class ControllerAgendamento {
    
    // Apenas ADMIN e RECEPCIONISTA veem todos
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<?> getAllAgendamentos() {
        // ...
    }
    
    // CLIENTE e RECEPCIONISTA podem agendar
    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE', 'RECEPCIONISTA')")
    public ResponseEntity<?> criarAgendamento(@RequestBody DTOAgendamento dto) {
        // ...
    }
    
    // BARBEIRO vê seus agendamentos
    @GetMapping("/meus")
    @PreAuthorize("hasRole('BARBEIRO')")
    public ResponseEntity<?> meus() {
        // ...
    }
    
    // CLIENTE cancela seus agendamentos
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        // ...
    }
}
```

---

## 💰 EXEMPLO: PROTEGER ControllerFinanceiro

```java
@RestController
@RequestMapping("/financeiro")
public class ControllerFinanceiro {
    
    // Apenas ADMIN vê financeiro
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getFinanceiro() {
        // ...
    }
    
    // Apenas ADMIN vê balanço
    @GetMapping("/balanco")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getBalanco() {
        // ...
    }
    
    // Apenas ADMIN registra lançamentos
    @PostMapping("/lancamento")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registrarLancamento(@RequestBody DTOFinanceiro dto) {
        // ...
    }
}
```

---

## 📦 EXEMPLO: PROTEGER ControllerEstoque

```java
@RestController
@RequestMapping("/estoque")
public class ControllerEstoque {
    
    // ADMIN e RECEPCIONISTA veem
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<?> getEstoque() {
        // ...
    }
    
    // Apenas ADMIN vê alerta
    @GetMapping("/alerta")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAlerta() {
        // ...
    }
    
    // ADMIN e RECEPCIONISTA gerenciam
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<?> cadastrar(@RequestBody DTOEstoque dto) {
        // ...
    }
    
    // ADMIN e RECEPCIONISTA fazem movimentação
    @PatchMapping("/{id}/movimentacao")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<?> movimentacao(@PathVariable Long id, @RequestBody DTOPatchEstoque dto) {
        // ...
    }
}
```

---

## 🧑‍💼 EXEMPLO: PROTEGER ControllerUsuario

```java
@RestController
@RequestMapping("/usuarios")
public class ControllerUsuario {
    
    // Registro é público
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody DTOUsuario dto) {
        // ...
    }
    
    // Qualquer autenticado vê seus dados
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUsuarioAutenticado() {
        // ...
    }
    
    // Apenas ADMIN gerencia usuários
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarTodos() {
        // ...
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody DTOUsuario dto) {
        // ...
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        // ...
    }
}
```

---

## 🧪 TESTE DE AUTORIZAÇÃO

### Passo 1: Registrar como CLIENTE (padrão)

```bash
POST http://localhost:8080/usuarios/registrar
{
  "login": "cliente_maria",
  "senha": "senha123"
}
```

### Passo 2: Fazer Login

```bash
POST http://localhost:8080/auth/login
{
  "login": "cliente_maria",
  "senha": "senha123"
}
# Retorna token
```

### Passo 3: Tentar Acessar Rota de ADMIN (Deve Falhar)

```bash
GET http://localhost:8080/financeiro
Authorization: Bearer {token}

Response: 403 Forbidden
{
  "timestamp": "2026-03-31T09:30:00.000Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/financeiro"
}
```

### Passo 4: Acessar Rota de CLIENTE (Deve Funcionar)

```bash
GET http://localhost:8080/agendamentos
Authorization: Bearer {token}

Response: 200 OK
```

---

## 🛠️ COMO ALTERAR A ROLE DE UM USUÁRIO

Como você AINDA NÃO tem um endpoint para alterar role (dica para implementar!), você pode fazer via banco de dados:

```sql
-- Mudar um usuário para ADMIN
UPDATE usuario SET role = 'ADMIN' WHERE id = 1;

-- Mudar para BARBEIRO
UPDATE usuario SET role = 'BARBEIRO' WHERE id = 2;

-- Mudar para RECEPCIONISTA
UPDATE usuario SET role = 'RECEPCIONISTA' WHERE id = 3;

-- Voltar para CLIENTE (padrão)
UPDATE usuario SET role = 'CLIENTE' WHERE id = 4;
```

---

## 🔍 VERIFICAR ROLE DE UM USUÁRIO

```bash
GET http://localhost:8080/usuarios/me
Authorization: Bearer {token}

Response:
{
  "success": true,
  "message": "Dados do usuário recuperados com sucesso",
  "data": {
    "id": 1,
    "login": "cliente_maria"
    // Role não aparece na resposta por segurança,
    // mas está sendo usado para autorização
  }
}
```

---

## 📊 MATRIZ DE PERMISSÕES RECOMENDADA

| Endpoint | GET | POST | PUT | DELETE | PATCH | Role Requerida |
|----------|-----|------|-----|--------|-------|----------------|
| `/barbeiros` | ✅ | 🔒 | 🔒 | 🔒 | - | ADMIN |
| `/agendamentos` | 📋 | 👤 | - | 👤 | ✅ | ADMIN/RECEP/CLIENTE |
| `/servicos` | ✅ | 🔒 | 🔒 | 🔒 | - | ADMIN |
| `/estoque` | 📋 | 🔒 | 🔒 | - | 🔒 | ADMIN/RECEP |
| `/financeiro` | 🔒 | 🔒 | - | - | 🔒 | ADMIN |
| `/usuarios/me` | ✅ | - | - | - | - | Todos |
| `/usuarios` | 🔒 | 🔒 | 🔒 | 🔒 | - | ADMIN |

**Legenda**:
- ✅ Público/Todos autenticados
- 🔒 Apenas ADMIN
- 👤 CLIENTE/BARBEIRO/RECEP
- 📋 ADMIN/RECEPCIONISTA

---

## ⚠️ EXPRESSÕES @PreAuthorize ÚTEIS

```java
// Qualquer um autenticado
@PreAuthorize("isAuthenticated()")

// Uma role específica
@PreAuthorize("hasRole('ADMIN')")

// Múltiplas roles (OU)
@PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")

// Múltiplas roles (versão alternativa)
@PreAuthorize("hasRole('ADMIN') or hasRole('RECEPCIONISTA')")

// Verificar se tem ALGUMA role específica
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_BARBEIRO')")

// Verificar autoridade específica
@PreAuthorize("hasAuthority('ROLE_ADMIN')")

// Verificar parâmetro da URL
@PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")

// Combinações complexas
@PreAuthorize("(hasRole('CLIENTE') and #id == authentication.principal.id) or hasRole('ADMIN')")
```

---

## 🚀 PRÓXIMO PASSO

Agora você deve ir em cada **Controller** e adicionar `@PreAuthorize` nas rotas apropriadas. 

Exemplo para completar:

```java
// ControllerBarbeiro
@PreAuthorize("isAuthenticated()")
@GetMapping
public ResponseEntity<?> getBarbeiros() { ... }

@PreAuthorize("isAuthenticated()")
@GetMapping("/{id}")
public ResponseEntity<?> getBarbeiroById(@PathVariable Long id) { ... }

@PreAuthorize("hasRole('ADMIN')")
@PostMapping
public ResponseEntity<?> cadastrarBarbeiro(@RequestBody DTOBarbeiro dto) { ... }

// E assim por diante...
```

---

## 🎯 CHECKLIST

- [x] Enum RoleUsuario criado com 4 roles
- [x] Usuario.role adicionado
- [x] SecurityConfig com @EnableMethodSecurity
- [ ] @PreAuthorize em ControllerBarbeiro
- [ ] @PreAuthorize em ControllerAgendamento
- [ ] @PreAuthorize em ControllerServico
- [ ] @PreAuthorize em ControllerEstoque
- [ ] @PreAuthorize em ControllerFinanceiro
- [ ] @PreAuthorize em ControllerUsuario
- [ ] Testes de autorização

---

## 🆘 ERROS COMUNS

### Erro: "Access Denied"
**Solução**: Sua role não permite acessar essa rota. Altere via banco de dados ou use um usuário com a role correta.

### Erro: "@PreAuthorize não funciona"
**Solução**: Certificar que `@EnableMethodSecurity(prePostEnabled = true)` está no SecurityConfig (já adicionado).

### Erro: "ROLE_ duplicado"
**Solução**: Não adicione "ROLE_" manualmente. O Spring adiciona automaticamente. Apenas use `hasRole('ADMIN')` e não `hasRole('ROLE_ADMIN')`.

---

**Documentação de Autorização Completa!** 🔐


