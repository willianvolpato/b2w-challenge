# Desafio B2W Digital

## Descrição
Projeto criado para o desafio proposto pela B2W Digital.

## Sumário
- [Desafio B2W](#desafio-b2w)
	- [Tecnologias utilizadas](#tecnologias-utilizadas)
    - [Requisitos:](#requisitos)
    - [Funcionalidades desejadas](#funcionalidades-desejadas)
    - [Pré-requisitos](#pré-requisitos)
        - [Banco de Dados](#banco-de-dados)
        - [Aplicação](#aplicação)
            - [Maven](#maven)
            - [Executável java (JAR)](#executável-java-jar)
    - [API](#api)
		- [Endpoints](#endpoints)
        - [Adicionar](#adicionar)
        - [Listar](#listar)
        - [Buscar por nome](#buscar-por-nome)
        - [Buscar por ID](#buscar-por-id)
        - [Remover](#remover)
        - [Buscar quantidade de filmes por nome](#buscar-quantidade-de-filmes-por-nome)
        - [Buscar quantidade de filmes de um planeta por id](#buscar-quantidade-de-filmes-de-um-planeta-por-id)

## Tecnologias utilizadas

- Java 1.8
- Mogodb
- Maven Warp
- SpringBoot
- Postman

## Requisitos:

- A API deve ser REST
- Para cada planeta, os seguintes dados devem ser obtidos do banco de dados da aplicação, sendo inserido manualmente:

    ```
    Nome
    Clima
    Terreno
    ```

- Para cada planeta também devemos ter a quantidade de aparições em filmes, que podem ser obtidas pela API pública do Star Wars: https://swapi.dev/about

## Funcionalidades desejadas

- Adicionar um planeta (com nome, clima e terreno)
- Listar planetas
- Buscar por nome
- Buscar por ID
- Remover planeta

## Pré-requisitos

- Java 1.8 e Git já instalados

### Banco de Dados

O projeto está configurado para usar uma base de dados local mongodb. Deve-se instalar, inicializar o mongodb e fazer as seguintes alterações no arquivo `/src/main/resources/application.properties`:

`spring.data.mongodb.uri=mongodb:mongodb://localhost:27017/b2wchallenge`

Com a configuração já definida, utilizará a porta padrão do mongodb (27017), caso necessário altrar, a mesma deve ser alterada nessa mesma configuração.

### Aplicação

Para a inicialização pode-se usar uma das duas maneiras:

#### Maven

Use o comando:

>`mvnw clean spring-boot:run`  

#### Executável java (JAR)

Use o comando:

`mvnw clean package`  

Esse comando executa os testes do sistema antes de gerar o executável.
Para pular os testes, use o comando:

`./mvnw clean package -Dmaven.test.skip=true`  

Com o executável gerado, use o comando:

`java -jar target/challenge-0.0.1-SNAPSHOT.jar`

O projeto estará disponível no endereço:  

`http://127.0.0.1:8080/api/planets`

## API

### Endpoints

|Ação|Caminho|Parâmetros do Request|Retorno|
|----|-------|---------------------|-------|
|Criar planeta| `POST /api/planets/`|`{ "name": "<NOME>",  "weather": <CLIMA>",  "terrain": "<TERRENO>"  }`|`{ "id": <ID>,  "name": "<NOME>",  "weather": <CLIMA>",  "terrain": "<TERRENO>",  "films" <QTD. FILMES> }`|
|Listar| GET /api/planets/?page=`<NUM. PÁGINA>`&size=`<REGISTROS POR PÁGINA>`||`{"content": [{ "id": <ID>,  "name": "<NOME>",  "weather": <CLIMA>",  "terrain": "<TERRENO>",  "films" <QTD. FILMES> }, ... ],"totalElements": <QTD. TOTAL DE PLANETAS>,  "last": <true SE FOR A ÚLTIMA PÁGINA, false CASO CONTRÁRIO>, "totalPages": <QTD. DE PÁGINAS>,  "first": <true SE FOR A PRIMEIRA PÁGINA, false CASO CONTRÁRIO>,  "numberOfElements": <QTD. DE PLANETAS NA PÁGINA>,  "size": <QTD. DE PLANETAS POR PÁGINA>,  "number": <NÚMERO DA PÁGINA>,  "empty": <true SE A PÁGINA NÃO TIVER PLANETAS, false CASO CONTRÁRIO>  }`|
|Buscarpor nome| GET /api/planets/name/`<NOME>`/?page=`<NUM. PÁGINA>`&size=`<ÍTENS POR PÁGINA>`||`{"content": [{ "id": <ID>,  "name": "<NOME>",  "weather": <CLIMA>",  "terrain": "<TERRENO>",  "films" <QTD. FILMES> }, ... ],"totalElements": <QTD. TOTAL DE PLANETAS>,  "last": <true SE FOR A ÚLTIMA PÁGINA, false CASO CONTRÁRIO>, "totalPages": <QTD. DE PÁGINAS>,  "first": <true SE FOR A PRIMEIRA PÁGINA, false CASO CONTRÁRIO>,  "numberOfElements": <QTD. DE PLANETAS NA PÁGINA>,  "size": <QTD. DE PLANETAS POR PÁGINA>,  "number": <NÚMERO DA PÁGINA>,  "empty": <true SE A PÁGINA NÃO TIVER PLANETAS, false CASO CONTRÁRIO>  }`|
|Buscarpor id| GET /api/planets/`<ID>`||`{ "id": <ID>,  "name": "<NOME>",  "weather": <CLIMA>",  "terrain": "<TERRENO>",  "films" <QTD. FILMES> }`|
|Remover| DELETE /api/planets/`<ID>`|||
|Buscar quantidade de filmes por nome| GET /api/planets/name/`<NOME>`/films||`<QTD. APARIÇÕES>`|
|Buscar quantidade de filmes de um planeta por id| GET /api/planets/`<ID>`/films||`<QTD. DE APARIÇÕES>`|

### Adicionar

Enviar `POST` para `/api/planets/`, com o body:

```json
{
    "nome": "<NOME>",
    "clima": "<CLIMA>",
    "terreno": "<TERRENO>"
}
```

**Todos os campos são obrigatórios** e devem ter **pelo menos 3 caracteres**, o **campo nome deve ser único** e também deve ser um nome de planeta pertencente a franquia *Star Wars*.

### Listar

Para listar os planetas cadastrados deve-se enviar `GET` para `/api/planets/`

É possível controlar a paginação da listagem informando `page` e `size` por parâmetros, onde `page` é o número da pagina e `size` é a quantidade de registros por página (ex. `/api/planets?page=3&size=3`).

### Buscar por nome

Para buscar planetas pelo nome deve-se enviar `GET` para `/api/planets/name/<NOME>`, `<NOME>` é o nome do planeta a ser buscado (case insensitive), não é necessário informar o nome do planeta por completo. Possui o controle de paginação, idem a listagem de planetas.

### Buscar por ID

Para buscar planetas pelo ID deve-se enviar `GET` para `/api/planets/<ID>`, `<ID>` é o ID do planeta a ser buscado.  

### Remover

Para remover um planeta, deve-se enviar `DELETE` para `/api/planets/<ID>`, `<ID>` é o ID do planeta a ser removido.

### Buscar quantidade de filmes por nome

Para buscar a quantidade de aparições de um planeta por nome, deve-se enviar `GET` para `/api/planets/name/<NOME>/films`, `<NOME>` é o nome exato do planeta (case insensitive).

### Buscar quantidade de filmes de um planeta por id

Para buscar a quantidade de aparições de um planeta por id, deve-se enviar `GET` para `/api/planets/<ID>/films`, `<ID>` é o id do planeta. 