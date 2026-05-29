# isobar.fm — Backend API

A Spring Boot REST API built for the DWS Backend Developer test. It consumes the upstream
bands catalogue (`https://bands-api.vercel.app/api`) and exposes a clean, cached, documented
API for listing bands, searching/sorting them, and retrieving band and album details.

## Tech stack

| Concern | Choice |
|---------|--------|
| Language / runtime | Java 21 |
| Framework | Spring Boot 3.2 (Spring Web MVC) |
| HTTP client | `RestClient` (with connect/read timeouts) |
| Caching | Spring Cache + Caffeine |
| API docs | springdoc-openapi (Swagger UI) |
| Observability | Spring Boot Actuator |
| Build | Maven |
| Tests | JUnit 5 + AssertJ |

## Prerequisites

- **JDK 21+**
- **Maven 3.9+** (or use the bundled wrapper if present)
- Internet access (the app calls the upstream bands API on a cache miss)

Check your versions:

```bash
java -version
mvn -version
```

## Run it

```bash
# from the project root
mvn spring-boot:run
```

or build a jar and run it:

```bash
mvn clean package
java -jar target/isobar-fm-0.0.1-SNAPSHOT.jar
```

The service starts on **http://localhost:8080**.

## Explore the API with Swagger

Once the app is running, open the interactive docs in a browser:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI spec (JSON):** http://localhost:8080/v3/api-docs

In Swagger UI you can read every endpoint's contract and click **"Try it out"** to send live
requests. The `sort` parameter renders as a dropdown of the allowed values.

## Endpoints

| Method | Endpoint | Query params | Description |
|--------|----------|--------------|-------------|
| `GET` | `/api/bands` | `sort`, `search` | List bands |
| `GET` | `/api/bands/{id}` | — | Band detail + its albums |
| `GET` | `/api/albums` | — | All albums + tracklists |
| `GET` | `/api/albums/{id}` | — | Single album + tracks |

**`GET /api/bands` parameters**

| Param | Values | Default | Notes |
|-------|--------|---------|-------|
| `sort` | `popularity`, `alphabetical` | `popularity` | Case-insensitive. Invalid value → `400`. |
| `search` | any text | — | Case-insensitive substring match on band name. |

**Error responses** share a uniform body: `{ "error": "..." }`

| Status | Meaning |
|--------|---------|
| `400` | Invalid `sort` value |
| `404` | Band / album id not found |
| `503` | Upstream API unavailable or timed out |

## Try it with curl

```bash
# Bands sorted by popularity (default)
curl "http://localhost:8080/api/bands"

# Bands sorted alphabetically (case-insensitive param)
curl "http://localhost:8080/api/bands?sort=alphabetical"

# Search by name
curl "http://localhost:8080/api/bands?search=nickel"

# Band detail (includes the band's albums)
curl "http://localhost:8080/api/bands/bc710bcf-8815-42cf-bad2-3f1d12246aeb"

# Album detail (includes tracklist + band summary)
curl "http://localhost:8080/api/albums/3c5794a0-d913-390d-ab24-6762af38c112"

# Invalid sort → 400 with a helpful message
curl -i "http://localhost:8080/api/bands?sort=foo"

# Unknown id → 404
curl -i "http://localhost:8080/api/bands/does-not-exist"
```

## Run the tests

```bash
mvn test
```

13 unit tests cover the index/sort/search logic and not-found handling. They use in-memory
stubs and do **not** hit the network, so they run in milliseconds.

## Caching

The app caches a single immutable snapshot of the catalogue (Caffeine, 5-minute TTL by
default). On a cache miss it fetches the upstream once, builds an indexed snapshot, and serves
all subsequent requests from memory until the TTL expires. You can observe it:

```bash
# Registered caches
curl http://localhost:8080/actuator/caches

# Cache hit/miss counters
curl "http://localhost:8080/actuator/metrics/cache.gets?tag=cache:catalog"

# Health
curl http://localhost:8080/actuator/health
```

## Configuration

Defaults live in `src/main/resources/application.properties` and can be overridden via
environment variables or command-line args:

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | `8080` | HTTP port |
| `bands.api.base-url` | `https://bands-api.vercel.app/api` | Upstream API base URL |
| `bands.api.connect-timeout` | `2s` | Upstream connect timeout |
| `bands.api.read-timeout` | `5s` | Upstream read timeout |
| `cache.ttl-minutes` | `5` | Catalogue cache TTL |

Example override:

```bash
java -jar target/isobar-fm-0.0.1-SNAPSHOT.jar --cache.ttl-minutes=1
```
