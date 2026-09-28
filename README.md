# FlashStock ⚡

Distributed flash-sale inventory and reservation engine built with Spring Boot.

## Engineering highlights
- PostgreSQL pessimistic row locking for inventory correctness
- Redis short-lived distributed lock per SKU
- Idempotency keys to make retries safe
- Kafka inventory events: RESERVED / CONFIRMED / RELEASED
- Automatic reservation expiry and stock release
- Docker Compose for PostgreSQL, Redis and Kafka
- Actuator health/metrics endpoints
- GitHub Actions CI

## Run
```bash
docker compose up --build
```

## API
Reserve:
```bash
curl -X POST http://localhost:8080/api/reservations \
  -H 'Content-Type: application/json' \
  -d '{"sku":"SHOE-001","quantity":1,"idempotencyKey":"checkout-1001"}'
```
Confirm:
```bash
curl -X POST http://localhost:8080/api/reservations/checkout-1001/confirm
```

## Interview talking points
Concurrency control, race conditions, idempotency, transactional boundaries, distributed locking, asynchronous events, retry/failure handling and load testing.

## Load test
With the stack running and a `SHOE-001` product seeded, run:
```bash
k6 run load-test/k6.js
```
See `docs/architecture.md` for the design and correctness strategy.
