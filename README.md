# dbc-pauta

Esse sistema trata-se de uma API para criação de pautas e um sistema de votação para essas pautas. Para seu
desenvolvimento foi utilizado Java 11, juntamente com o spring-boot e como banco de dados foi utilizado H2, por ser de
mais fácil teste.

A inicialização do sistema irá criar todas as tabelas do banco de dados, essas podem ser vistas em
```localhost:8080/h2```, todos os caminhos podem ser modificados nas configurações da aplicação, presente em
``src/main/resources/application.yml``. Ao iniciar a aplicação um script sql irá povoar uma base de dados de usuários,
como não era descrito nenhum serviço para criar os usuários então esse não foi realizado. O usuário e senha do banco de
dados pode ser encontrado no ``application.yml``.

Todos os endpoints podem ser vistos pelo swagger na página ``http://localhost:8080/swagger-ui.html``. Vale resaltar que
não é necessário passar o id da pauta quando for criar uma nova pauta, ela utiliza um ide generator.

Todos os endpoints pode ser encontrados em ``/http`` e possuem final WS de (Web Service).

Para o versionamento da aplicação foi utilizado o versionamento semântico, por sua facilidade visto o tamanho da
aplicação.

Testes de carga utilizando o Jmeter podem ser encontrados na pasta ``Jmeter`` na raiz do projeto. Após os primeiros
testes foi comprovado que o Dozer (framework de mapeamento de classes) prejudicava o desempenho da aplicação, por isso
ele foi removido.

A API faz uma integração com o serviço `https://user-info.herokuapp.com/users/{cpf}` para validar se o usuário possui
permissão para votar na pauta.
