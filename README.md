# 🧾 Gerenciador de Pedidos

Este projeto é um sistema de gerenciamento de pedidos desenvolvido em Spring Boot, com persistência de dados em MySQL executado via Docker Compose.

## 🚀 Passo a passo para subir o projeto
## 🧩 1. Instalar o Docker

Se você não possui o Docker instalado, acesse o site oficial e siga as instruções para o seu sistema operacional:

🔗 https://docs.docker.com/get-docker/

O Docker será necessário para subir a imagem do banco de dados MySQL utilizada pelo sistema.

## 🐳 2. Subir o container do banco de dados

Vá até a pasta docker-compose que está na raiz do projeto.

Abra o Prompt de Comando (CMD) ou terminal dentro dessa pasta.

Execute o comando:

     docker compose up -d 

Esse comando fará o download da imagem do MySQL e subirá o container em segundo plano.

## 🛠️ 3. Conectar ao banco de dados

Após o container estar rodando, conecte-se ao banco MySQL usando as credenciais abaixo (que também estão definidas no arquivo docker-compose.yml):

    Parâmetro	|   Valor
    Usuário	        |   myuser
    Senha	        |   mypassword
    Host	        |   localhost
    Porta	        |   3306

Você pode conectar usando qualquer cliente SQL, como:

    MySQL Workbench
    
    DBeaver
    
    HeidiSQL
    
    ou até via terminal (mysql -u myuser -pmypassword -h localhost)

## 🗃️ 4. Criar o banco de dados

Após conectar ao MySQL, crie o banco de dados utilizado pela aplicação:

    CREATE DATABASE gerenciador;

## 📦 5. Executar os scripts de schema e dados

Dentro da pasta banco (localizada na raiz do projeto), execute na seguinte ordem:

    1️⃣ 01-schema.sql → cria as tabelas e constraints
    2️⃣ 02-dados.sql → insere dados iniciais (categorias, produtos, etc.)

Exemplo (via terminal MySQL):

    mysql -u myuser -pmypassword -h localhost gerenciador < banco/01-schema.sql
    mysql -u myuser -pmypassword -h localhost gerenciador < banco/02-dados.sql

## ⚙️ 6. Subir o sistema

Após o banco estar criado e populado, volte para a raiz do projeto e execute:

via IDE (IntelliJ / Eclipse)

Localize a classe principal:
GerenciadorDePedidosApplication.java

Clique em Run ▶️

## ✅ 7. Acessar a aplicação

Assim que o sistema estiver rodando, ele estará acessível em:

👉 http://localhost:8080/gerenciador-pedidos

Se o Swagger estiver habilitado, acesse:
👉 http://localhost:8080/gerenciador-pedidos/swagger-ui/index.html

## 💡 Dica

Se quiser reiniciar o container do banco:

    docker compose down
    docker compose up -d

## 🧰 Resumo dos comandos principais
Etapa	Comando
Subir o banco	docker compose up -d
Criar banco manualmente	CREATE DATABASE gerenciador;
Executar scripts SQL	mysql -u myuser -pmypassword -h localhost gerenciador < banco/01-schema.sql
Rodar o sistema	./gradlew bootRun
Parar containers	docker compose down
## 🧑‍💻 Tecnologias utilizadas

    Java 21
    
    Spring Boot 3
    
    Spring Data JPA
    
    MySQL 8.0 (Docker)
    
    Swagger OpenAPI
    
    Lombok
    
    MapStruct