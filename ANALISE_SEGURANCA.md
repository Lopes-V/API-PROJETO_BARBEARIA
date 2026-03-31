# 🔒 Análise de Segurança - API Barbearia

**Data da Análise**: 31 de Março de 2026

---

## ❌ PROBLEMAS CRÍTICOS ENCONTRADOS

### 1. **FALTA DE FILTRO JWT (CRÍTICO)**

**Problema**: Não há implementação de um filtro para validar tokens JWT nas requisições.

**Impacto**: 
- Qualquer um pode acessar as rotas protegidas sem token válido
- Os endpoints devem estar protegidos por JWT, mas estão abertos para todos

**Localização**: 
- `config/SecurityConfig.java` - Não configura validação JWT
- Ausência de filtro `OncePerRequestFilter`

**Solução Necessária**:
```java
// Criar JwtAuthenticationFilter.java
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // Verificar header Authorization
    // Extrair token e validar com ServiceToken
    // Colocar autenticação no SecurityContext
}
```

---

### 2. **SEGURANÇA ABERTA DEMAIS (CRÍTICO)**

**Problema**: No `SecurityConfig.java`, linha 31-33:
```java
req.requestMatchers("/barbeiro/**", "/agendamentos/**", "/servicos/**","/estoque/**","/financeiro/**").permitAll();
```

**Impacto**:
- ❌ Qualquer pessoa pode:
  - Ver TODOS os agendamentos
  - Ver dados FINANCEIROS da barbearia
  - Editar ou deletar barbeiros
  - Ver estoque completo

**Deveria ser**: Apenas usuários autenticados via JWT

---

### 3. **ServiceAutenticacao NÃO ESTÁ IMPLEMENTADO CORRETAMENTE**

**Problema**: No `ServiceAutenticacao.java`:
- Falta `@Autowired` ou injeção de dependência para `repository`
- O método não implementa `UserDetailsService` corretamente
- Não há uma anotação `@Override`

**Código Atual**:
```java
@Service
@Primary
public class ServiceAutenticacao {
    private RepositoryUsuario repository;  // ❌ NÃO INJETADO!
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByLogin(username);  // ❌ VAI DAR NULL POINTER
    }
}
```

**Deveria Ser**:
```java
@Service
@Primary
public class ServiceAutenticacao implements UserDetailsService {
    private final RepositoryUsuario repository;
    
    public ServiceAutenticacao(RepositoryUsuario repository) {
        this.repository = repository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = repository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        return user;
    }
}
```

---

### 4. **RepositoryUsuario TEM TIPO DE ID INCORRETO**

**Problema**:
```java
public interface RepositoryUsuario extends JpaRepository<Usuario, UUID> {  // ❌ UUID!
    UserDetails findByLogin(String username);
}
```

**Impacto**:
- Entidade `Usuario` usa `@GeneratedValue(strategy = GenerationType.IDENTITY)` (Long)
- Mas o Repository espera UUID
- **Isso vai causar erro em tempo de execução!**

**Deveria ser**:
```java
public interface RepositoryUsuario extends JpaRepository<Usuario, Long> {  // ✅ Long
    UserDetails findByLogin(String username);
}
```

---

### 5. **ServiceToken USA SENHA HARDCODED**

**Problema** (ServiceToken.java):
```java
private final String senha = "senha";  // ❌ TERRÍVEL SEGURANÇA!
```

**Impacto**:
- Qualquer um que ver o código sabe a chave secreta
- Token JWT é previsível e quebrável
- NÃO é segredo de verdade

**Solução**:
```java
@Service
public class ServiceToken {
    @Value("${jwt.secret}")  // ✅ Do application.properties
    private String senha;
    // ...
}
```

E no `application.properties`:
```properties
jwt.secret=seu-super-secret-aleatorio-e-seguro-aqui
```

---

### 6. **ControllerUsuario INCOMPLETO**

**Problema**:
```java
@PostMapping("/registrar")
public ResponseEntity<?> registrarUsuario(DTOUsuario dtoUsuario){
    // ❌ VAZIO!
}
```

**Impacto**: Não é possível registrar novos usuários

---

### 7. **FALTA VALIDAÇÃO DE SENHA CRIPTOGRAFADA**

**Problema**: Na hora do login, a senha vem em texto plano pelo DTO, mas nunca é criptografada antes de salvar no banco.

**Impacto**: Senhas armazenadas em texto plano (GRAVÍSSIMO!)

---

### 8. **CORS ABERTO DEMAIS**

**Problema**:
```java
corsConfiguration.setAllowedMethods(List.of("GET","POST","DELETE","PUT"));
// ❌ Falta PATCH e permite todos os métodos
```

**Deveria adicionar**:
```java
corsConfiguration.setAllowedMethods(List.of("GET","POST","DELETE","PUT","PATCH"));
corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
```

---

### 9. **USUARIO NÃO IMPLEMENTA UserDetails COMPLETAMENTE**

**Problema**:
```java
@Override
public String getPassword() { return senha; }
@Override
public String getUsername() { return login; }
```

Faltam implementar:
- `isAccountNonExpired()`
- `isAccountNonLocked()`
- `isCredentialsNonExpired()`
- `isEnabled()`

---

## 📋 ROTAS QUE DEVEM SER PROTEGIDAS

| Rota | Método | Requer Autenticação? | Quem Acessa? |
|------|--------|----------------------|--------------|
| `/auth/login` | POST | ❌ NÃO | Qualquer um |
| `/usuarios/registrar` | POST | ✅ SIM | Apenas ADMIN |
| `/usuarios/me` | GET | ✅ SIM | Qualquer autenticado |
| `/barbeiros` | GET/POST/PUT/DELETE | ✅ SIM | Apenas ADMIN |
| `/agendamentos` | GET/POST/PATCH/DELETE | ✅ SIM | ADMIN + CLIENTE |
| `/servicos` | GET/POST/PUT/DELETE | ✅ SIM | Apenas ADMIN |
| `/estoque` | GET/POST/PUT/PATCH | ✅ SIM | Apenas ADMIN |
| `/financeiro` | GET/POST/PATCH | ✅ SIM | Apenas ADMIN |

---

## 🚀 CHECKLIST DE CORREÇÕES NECESSÁRIAS

- [ ] Criar `JwtAuthenticationFilter` para validar tokens
- [ ] Remover `permitAll()` de rotas protegidas no `SecurityConfig`
- [ ] Implementar `UserDetailsService` corretamente em `ServiceAutenticacao`
- [ ] Corrigir tipo de ID em `RepositoryUsuario` de `UUID` para `Long`
- [ ] Mover chave JWT para `application.properties`
- [ ] Implementar `ControllerUsuario.registrarUsuario()`
- [ ] Criptografar senhas com BCrypt antes de salvar
- [ ] Implementar todos os métodos de `UserDetails` em `Usuario`
- [ ] Adicionar PATCH em CORS
- [ ] Adicionar validação de token expirado
- [ ] Implementar diferenciação de roles (ADMIN vs CLIENT)
- [ ] Adicionar headers de segurança (CORS, Cache-Control, etc)

---

## 🔐 RECOMENDAÇÕES GERAIS

1. **Nunca exponha secrets em código** - Use variáveis de ambiente
2. **Sempre criptografe senhas** - BCryptPasswordEncoder é o padrão
3. **Valide tokens JWT** - Cada requisição deve ser verificada
4. **Use diferentes roles** - Nem todo autenticado deve acessar tudo
5. **Implemente logging de segurança** - Registre tentativas falhadas
6. **Adicione rate limiting** - Previna força bruta
7. **Use HTTPS em produção** - Tokens JWT viajam no header
8. **Teste tudo** - Segurança precisa de testes

---

## 📚 REFERÊNCIAS

- Spring Security: https://spring.io/projects/spring-security
- JWT Best Practices: https://tools.ietf.org/html/rfc7519
- OWASP Top 10: https://owasp.org/www-project-top-ten/

