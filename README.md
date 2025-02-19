# **Test Account**

Este projeto é uma aplicação de gerenciamento de contas construída usando Spring Boot. Ele fornece funcionalidades básicas para criar, atualizar, visualizar e deletar contas, bem como autenticação de usuários e importação de dados de contas a partir de arquivos CSV.

## **Tecnologias Utilizadas**

- **Java 17**
- **Spring Boot 3.3.2**
- **Spring Security**
- **Spring Data JPA**
- **PostgreSQL 12.0**
- **H2 Database**
- **Flyway**
- **JWT (JSON Web Token)**
- **Apache Commons CSV**
- **Docker**

## **Configuração do Projeto**

### **Pré-requisitos**

- **JDK 17**
- **Docker (opcional para uso com `docker-compose`)**
- **Maven**

### **Como Executar**

1. **Clone o repositório:**
    ```bash
    git clone https://github.com/PettersonSantos/test-account.git
    ```

2. **Navegue até o diretório do projeto:**
    ```bash
    cd test-account
    ```

3. **Configure o banco de dados H2 e as migrações Flyway:**

    - O projeto está configurado para usar o banco de dados H2 em memória para testes. As migrações do Flyway serão executadas automaticamente ao iniciar a aplicação.


4. **Para executar os testes:**
    ```bash
    mvn clean test -Dspring.profiles.active=test
    ```
5. **Para gerar um novo package:**
    ```bash
    mvn clean package
    ```

### **Usando Docker**

Para executar o projeto usando Docker:

1. **Certifique-se de que o Docker está instalado e em execução.**

2. **Execute o comando Docker Compose:**
    ```bash
    docker-compose up --build .
    ```

## **Documentação da API**

A documentação da API está disponível no Swagger UI, que pode ser acessado em [Swagger UI](http://localhost:8080/swagger-ui/index.html).

## **Endpoints da API**

### **Autenticação**

- **POST /auth/login**: Autentica um usuário e retorna um token JWT.

### **Contas**

- **POST /api/accounts**: Cria uma nova conta.
- **PUT /api/accounts/{id}**: Atualiza uma conta existente.
- **PATCH /api/accounts/{id}/situacao**: Altera o status de uma conta existente.
- **GET /api/accounts**: Retorna uma lista de contas filtradas por data de vencimento e descrição, com suporte a paginação.
   - Parâmetros de consulta:
      - `startDate`: Data de início para o filtro.
      - `endDate`: Data de término para o filtro.
      - `descricao`: Descrição para o filtro (opcional).
      - `page`: Número da página (opcional, padrão é 0).
      - `size`: Tamanho da página (opcional, padrão é 10).- **GET /api/accounts/{id}**: Retorna uma conta pelo ID.
- **GET /api/accounts/total-paid**: Retorna o valor total pago em um período específico.
- **POST /api/accounts/import**: Importa contas a partir de um arquivo CSV.

## **Licença**

Este projeto está licenciado sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
