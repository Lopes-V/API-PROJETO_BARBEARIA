# 🔧 BarberSim - API

Backend do sistema de gestão de barbearia. Responsável pela lógica de negócio, autenticação, agendamentos e controle de barbeiros.

## ⚙️ Stack Técnico

- **Linguagem**: Java
- **Framework**: Spring Boot
- **Build**: Maven
- **Containerização**: Docker & Docker Compose
- **Banco de Dados**: PostgreSQL (via docker-compose)

## 🎯 Funcionalidades Principais

✅ Gerenciamento de agendamentos  
✅ Controle de 3 barbeiros distintos  
✅ Autenticação de administrador  
✅ Simulação de operações em tempo real  
✅ API RESTful  

## 🚀 Como Executar

### Requisitos
- Java 17+
- Maven (ou usar `./mvnw`)
- Docker & Docker Compose (opcional)

### Desenvolvimento Local

```bash
# Clonar repositório
git clone https://github.com/Lopes-V/API-PROJETO_BARBEARIA.git
cd API-PROJETO_BARBEARIA

# Com Docker Compose (recomendado)
docker-compose up

# Sem Docker (configurar banco de dados manualmente)
./mvnw spring-boot:run
```

### Build para Produção

```bash
./mvnw clean package
java -jar target/api-barbearia.jar
```

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/barbearia/
│   │       ├── controller/      # Endpoints da API
│   │       ├── service/         # Lógica de negócio
│   │       ├── repository/      # Acesso ao banco
│   │       └── model/           # Entidades JPA
│   └── resources/
│       └── application.yml      # Configurações
```

## 📡 Endpoints Principais

- `POST /api/auth/login` - Autenticação
- `GET/POST /api/agendamentos` - Gerenciar agendamentos
- `GET/POST /api/barbeiros` - Gerenciar barbeiros
- `GET /api/agenda/:barbeiro` - Agenda do barbeiro

## 🔐 Configuração

Edite `application.yml` para:
- Conectar ao banco de dados
- Configurar porta (padrão: 8080)
- Definir variáveis de ambiente

## 📝 Notas

- Projeto utiliza Maven Wrapper para facilitar build
- Docker Compose já configura banco de dados automaticamente
- Dependências gerenciadas via `pom.xml`

---

**Repositório**: [API-PROJETO_BARBEARIA](https://github.com/Lopes-V/API-PROJETO_BARBEARIA)
