# FIAP Pan Login Service

Este projeto utiliza MongoDB para armazenar os dados dos clientes Pessoa física e júridica segregadas em duas collections:
```regularPerson``` e ```legalPerson``` respectivamente.

Também é utilizado Redis para armezar as intenções de login, onde as mesmas possuem o TTL de 1 minuto para que o usuário tente se autenticar, caso contrário ele não conseguirá performar o login sem antes chamar novamente o endpoint de intenção de login (pre-login).

Quando o usuário consegue se autenticar, a sessão dele fica armazenada dentro do redis e ele recebe um ```Phantom Token``` para trafegar suas requisições.


#### Obs: Para executar o projeto é necessário ter instalado o Java JDK 17.
#### Regras: 
- O Token expira em 1 hora, será removido do Redis após bater o seu TTL
- A intenção de login (X-Pan-Intent-Id) expira em 1 minuto, após isso você não irá conseguir autenticar com aquele ID de intenção, pois o registro terá sido elimitado do Redis por conta do TTL

```
C:\Users\dougl\IdeaProjects\fiap-pan-login>cd databases

C:\Users\dougl\IdeaProjects\fiap-pan-login\databases>docker-compose up -d
Docker Compose is now in the Docker CLI, try `docker compose up`

Creating databases_redis_1      ... done
Creating databases_mongodb_1 ... done
Creating databases_mongo_seed_1 ... done

C:\Users\dougl\IdeaProjects\fiap-pan-login\databases>
```

#### Endpoint para efetuar o pre-login, você deve enviar o seu CPF ou CNPJ, será gerado um token de intenção onde você deve utilizar no próximo request como Header: "X-Pan-Intent-Id"
```
curl --location --request POST 'http://localhost:8080/user/pre-login' \
--header 'Content-Type: application/json' \
--data-raw '{
"cpfOrCnpj": "30447844000146"
}'
```
#### Resposta da sua requisição contendo o valor do header X-Pan-Intent-Id e o seu teclado virtual, que será utilizado para montar a senha do cliente
```json
{
  "timestamp": 1655390993128,
  "data": {
    "intentId": "cef53c3a-ff38-4823-a417-e4da7d92d738",
    "keyboard": [
      {
        "first": 8,
        "second": 9
      },
      {
        "first": 4,
        "second": 5
      },
      {
        "first": 2,
        "second": 3
      },
      {
        "first": 0,
        "second": 1
      },
      {
        "first": 6,
        "second": 7
      }
    ]
  }
}
```
#### Endpoint para efetuar o login, o campo "password" contem os digitos do teclado virtual onde alguma combinação retornara a sua senha
#### Obs: todas senhas são 123456
```
curl --location --request POST 'http://localhost:8080/user/login' \
--header 'Content-Type: application/json' \
--header 'X-Pan-Intent-Id: 862dc0a9-24cb-477e-a1a5-9bfc70193e06' \
--data-raw '{
"cpfOrCnpj": "30447844000146",
"password": [0,1,2,3,2,3,4,5,4,5,6,7]
}'
```
#### Resposta contendo sua sessão e seu phantom token
```json
{
    "timestamp": 1655390995572,
    "data": {
        "username": "Empresa de Tecnologia XPTO LTDA",
        "token": {
            "tokenType": "Bearer",
            "accessToken": "0a0c113ab4c2bf5a23d5421c928331184bf70ca0",
            "tokenExpiry": 1655391595000
        }
    }
}
```
#### Endpoint apenas para recuperar o token gerado via sessão, demonstrando como o Phantom Token funciona:
```
curl --location --request GET 'http://localhost:8080/token/0a0c113ab4c2bf5a23d5421c928331184bf70ca0' \
--header 'Content-Type: application/json'
```
#### Resposta em formato RAW contendo o token
```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2MmFiNDJhY2FjZWU5MGNmMmZkOWUxNGIiLCJ1c2VyTmFtZSI6IkVtcHJlc2EgZGUgVGVjbm9sb2dpYSBYUFRPIExUREEiLCJwZXJzb25UeXBlIjoiTEVHQUwiLCJlbWFpbCI6ImVtcHJlc2EudGVjaEBlbWFpbC5jb20iLCJpYXQiOjE2NTUzOTA5OTUsImV4cCI6MTY1NTM5MTU5NX0.i0lNAtxyFhpd-mBIsrn5S8m6ZRzOmXlzECy-KnaYYutJzLie-rDuqoG7WiFVBsahSsGoTZkFQe_0QZYVKPtNWg
```
# jwt.io

![img.png](img.png)