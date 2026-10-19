# Reserva de Salas — API REST (Alura)

Projeto de aprendizado em Java 17 e Spring Boot com arquitetura `controller → service → repository`, JPA, H2, validacao, testes e Docker.

## Executar

**Requisitos:** Java 17 e Maven 3.9+.

```bash
mvn spring-boot:run
```

Ou com Docker:

```bash
docker compose up --build
```

API: `http://localhost:8080/api/v1`. O perfil `dev` utiliza H2 em arquivo (`./data`). Console do H2 em `/h2-console`, disponivel apenas no perfil dev. Para testes: `mvn clean verify`.

## Endpoints

| Recurso | Listar | Buscar | Criar | Atualizar | Excluir |
|---|---|---|---|---|---|
| Salas | GET /salas | GET /salas/{id} | POST /salas | PUT /salas/{id} | DELETE /salas/{id} |
| Usuarios | GET /usuarios | GET /usuarios/{id} | POST /usuarios | PUT /usuarios/{id} | DELETE /usuarios/{id} |
| Reservas | GET /reservas | GET /reservas/{id} | POST /reservas | PUT /reservas/{id} | DELETE /reservas/{id} |

Todas as rotas comecam com `/api/v1`. O DELETE em reserva **cancela**, sem apagar historico. Tambem existe `PATCH /reservas/{id}/cancelar`. Listagens usam `?page=0&size=20`; reservas aceitam `salaId`, `inicio` e `fim` para filtrar.

### Exemplo de criacao

```bash
curl -X POST http://localhost:8080/api/v1/salas -H "Content-Type: application/json" -d '{"nome":"Sala Azul","capacidade":8,"ativa":true}'
curl -X POST http://localhost:8080/api/v1/usuarios -H "Content-Type: application/json" -d '{"nome":"Ana","email":"ana@exemplo.com"}'
curl -X POST http://localhost:8080/api/v1/reservas -H "Content-Type: application/json" -d '{"salaId":1,"usuarioId":1,"inicio":"2026-10-20T09:00:00","fim":"2026-10-20T10:00:00"}'
```

## Regras de negocio

- Capacidade positiva, nomes obrigatorios, emails validos e sala ativa para reservar.
- Inicio **estritamente anterior** ao fim.
- Intervalos **semiabertos** `[inicio, fim)`: 09:00–10:00 e 10:00–11:00 sao compativeis; 09:30–10:30 conflita.
- Apenas reservas `ATIVA` bloqueiam horarios; canceladas nao podem ser reativadas nem editadas.
- A transacao trava a linha da sala ao criar/alterar uma reserva, para serializar verificacao de conflitos.
- Salas e usuarios com historico de reservas nao podem ser excluidos (HTTP 409).
- JPA `LAZY` impede carregamento automatico desnecessario; os DTOs leem referencias dentro de transacoes.

## Status HTTP

`201` criacao, `200` consulta/atualizacao, `204` exclusao/cancelamento, `400` entrada invalida, `404` nao encontrado e `409` conflito ou unicidade.

Projeto academico — desenvolvimento e evolucao incremental por pull requests.
