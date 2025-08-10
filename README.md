# Challenge LiterAlura

## Descrição

Este projeto é uma solução para o desafio de Back-End do programa Alura ONE (Oracle Next Education). A aplicação consiste em um catálogo de livros que consome a API pública [Gutendex](https://gutendex.com/) para buscar e armazenar informações sobre livros e autores em um banco de dados PostgreSQL.

A aplicação é executada via linha de comando e oferece um menu interativo para o usuário.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**

## Pré-requisitos

Antes de começar, você precisará ter as seguintes ferramentas instaladas em sua máquina:
- [Java Development Kit (JDK) 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Apache Maven](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)

## Como Executar

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/rsprogrammerbr/ChallengeLiterAlura.git
    cd ChallengeLiterAlura
    ```

2.  **Configure o Banco de Dados:**
    - Certifique-se de que seu servidor PostgreSQL está rodando.
    - Crie um banco de dados chamado `literalura`.
    - Abra o arquivo `src/main/resources/application.properties` e atualize as propriedades `spring.datasource.username` e `spring.datasource.password` com suas credenciais do PostgreSQL.

3.  **Execute a aplicação:**
    - Pelo terminal, na raiz do projeto, execute o seguinte comando Maven:
    ```bash
    mvn spring-boot:run
    ```

A aplicação iniciará e exibirá o menu de opções no console.

## Funcionalidades

O menu principal oferece as seguintes opções:

1.  **Buscar livro pelo título:** Busca um livro na API Gutendex e o salva no banco de dados.
2.  **Listar livros registrados:** Exibe todos os livros salvos no banco de dados.
3.  **Listar autores registrados:** Exibe todos os autores salvos no banco de dados.
4.  **Listar autores vivos em um determinado ano:** Exibe os autores que estavam vivos no ano especificado.
5.  **Listar livros em um determinado idioma:** Exibe os livros registrados em um idioma específico (ex: `en`, `pt`, `es`).
6.  **Sair:** Encerra a aplicação.

---

## Autor

**Rodrigo Silva**
