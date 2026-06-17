# kafka-producer

Example Spring Boot application that **produces** messages to an Apache Kafka topic.
Companion project: [kafka-consumer](https://github.com/mateusvpassos/kafka-consumer).

> Sample / learning project. Not production-grade.

## What it does

A `@Scheduled` job ([`KafkaProducer`](src/main/java/br/com/passos/kafkaproducer/service/producer/KafkaProducer.java))
serializes a payload to JSON and sends it to the configured topic every **60 seconds**.

```
Producer  --(JSON "Hello World!")-->  topic_test  -->  Consumer
```

## Stack

| | |
|---|---|
| Language | Java 24 |
| Framework | Spring Boot 3.5.0 |
| Messaging | Spring for Apache Kafka |
| Build | Gradle (wrapper, 8.14) |
| Extras | Lombok, Jackson (JSR-310) |

## Configuration

`kafka.topic` (default `topic_test`, override with env `KAFKA_TOPIC`) and the broker
address are profile-based:

| Profile | Bootstrap servers | Use |
|---------|-------------------|-----|
| `local` | `localhost:19092` | App on host, Kafka in Docker |
| `docker` | `${KAFKA_BOOTSTRAP_SERVERS:kafka:9092}` | App inside the Compose network |
| `desenv` / `homolog` / `prod` | env vars (SASL/SSL ready) | remote brokers |

HTTP port: **8090**.

## Run everything with Docker (easiest)

The included [`docker-compose.yml`](docker-compose.yml) brings up Kafka (+ Zookeeper),
Schema Registry, the Kowl UI, and **both** the producer and consumer (built straight
from GitHub):

```bash
docker compose up --build
```

- Kowl UI: http://localhost:9080 (Schema Registry on http://localhost:8081)
- Kafka external listener (from host): `localhost:19092`
- Watch the consumer logs to see the messages arrive:

```bash
docker compose logs -f consumer
```

Stop and clean up:

```bash
docker compose down
```

## Run the app on the host, Kafka in Docker

```bash
# 1. start just the broker (+ UI)
docker compose up -d zookeeper kafka-broker-1 kowl

# 2. run the producer against localhost:19092
./gradlew bootRun --args='--spring.profiles.active=local'
```

## Build

```bash
./gradlew build          # jar + tests
./gradlew bootJar        # runnable jar only
```
