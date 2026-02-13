# transaction-challenge
API for managing transaction data.

## Endpoints
PUT /transactions/{id}
GET /transactions/types/{type}
GET /transactions/sum/{id}

## Run with Docker
docker build -t transaction-challenge .
docker run -p 8080:8080 transaction-challenge
