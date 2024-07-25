Test Account
Este projeto é uma aplicação de gerenciamento de contas construída usando Spring Boot. Ele fornece funcionalidades básicas para criar, atualizar, visualizar e deletar contas, bem como autenticação de usuários e importação de dados de contas a partir de arquivos CSV.

Tecnologias Utilizadas
Java 17
Spring Boot 3.3.2
Spring Security
Spring Data JPA
PostgreSQL 12.0
H2 Database
Flyway
JWT (JSON Web Token)
Apache Commons CSV
Docker
Configuração do Projeto
Pré-requisitos
JDK 17
Docker (opcional para uso com docker-compose)
Maven
Como Executar
Clone o repositório:

bash
Copiar código
git clone https://github.com/PettersonSantos/test-account.git
Navegue até o diretório do projeto:

bash
Copiar código
cd test-account
Configure o banco de dados H2 e as migrações Flyway:

O projeto está configurado para usar o banco de dados H2 em memória para testes. As migrações do Flyway serão executadas automaticamente ao iniciar a aplicação.
Execute a aplicação:

bash
Copiar código
mvn spring-boot:run
Para executar os testes:

bash
Copiar código
mvn clean test -Dspring.profiles.active=test
Usando Docker
Para executar o projeto usando Docker:

Certifique-se de que o Docker está instalado e em execução.

Construa a imagem Docker:

bash
Copiar código
docker build -t test-account .
Execute o contêiner:

bash
Copiar código
docker run -p 8080:8080 test-account
Documentação da API
A documentação da API está disponível no Swagger UI, que pode ser acessado em Swagger UI.

Endpoints da API
Autenticação
POST /auth/login: Autentica um usuário e retorna um token JWT.
Contas
POST /api/accounts: Cria uma nova conta.
PUT /api/accounts/{id}: Atualiza uma conta existente.
PATCH /api/accounts/{id}/situacao: Altera o status de uma conta existente.
GET /api/accounts: Retorna uma lista de contas filtradas por data de vencimento e descrição.
GET /api/accounts/{id}: Retorna uma conta pelo ID.
GET /api/accounts/total-paid: Retorna o valor total pago em um período específico.
POST /api/accounts/import: Importa contas a partir de um arquivo CSV.
