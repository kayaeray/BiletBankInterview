#  Fligh Provider Consumer

This project is a Spring Boot REST API application that retrieves flight data from two different SOAP services simulated as ProviderA and ProviderB, combines this data and offers the cheapest flights to the user.

## ✨ Features

* REST-based integration with external SOAP flight providers
* Fetching and transforming flight information
* Centralized flight data accessible via REST API
* Docker-ready structure for easy deployment
* High readability with Lombok annotations


## 🛠️ Tech Stack

| Technology                  | Purpose                              |
| --------------------------- | ------------------------------------ |
| **Java 17**                 | Programming Language                 |
| **Spring Boot 3.x**         | Backend Framework                    |
| **PostgreSQL**              | Database                             |
| **Maven**                   | Dependency Management & Build Tool   |
| **SOAP**                    | External Flight Provider Integration |
| **Lombok**                  | Boilerplate Code Reduction           |
| **Docker & Docker Compose** | Containerization & Deployment        |
| **IntelliJ IDEA**           | Main Development IDE                 |

---

## 🏗️ System Architecture Overview

This project is part of a 3-service flight management ecosystem:

| Project                                       | Description                                                                                   | Technology                |
| --------------------------------------------- | --------------------------------------------------------------------------------------------- | ------------------------- |
| **Flight Provider A**                         | A mock flight provider service exposing flight data via SOAP API                              | Java + Spring (SOAP)      |
| **Flight Provider B**                         | Another SOAP-based flight provider with different data structure/rules                        | Java + Spring (SOAP)      |
| **Flight Provider Consumer** *(this project)* | Aggregates and consumes flight data from Provider A & B, processes and stores into PostgreSQL | Java 17 + Spring Boot 3.x |


### 🧱 High-Level Architecture Diagram

```text
         ┌──────────────────────┐
         │  Flight Provider A   │
         │      (SOAP API)      │
         └───────────▲──────────┘
                     │
                     │ SOAP
 Client ───▶ Flight Provider Consumer ───▶ PostgreSQL
                     │
                     │ SOAP
         ┌───────────▼──────────┐
         │  Flight Provider B   │
         │      (SOAP API)      │
         └──────────────────────┘
```

---

### 🗄 Database Schema

This project logs all incoming and outgoing requests using a PostgreSQL table called `request_response_log`.

```sql
CREATE TABLE request_response_log
(
    id             SERIAL PRIMARY KEY,
    status         INTEGER,
    method         VARCHAR(50),
    uri            VARCHAR(50),
    remote_address VARCHAR(50),
    request        VARCHAR(500),
    response       VARCHAR(500),
    timestamp      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

📌 **Purpose**
This table stores request and response details for monitoring and debugging purposes.

📝 **Fields Explanation**

| Column         | Type         | Description                      |
| -------------- | ------------ | -------------------------------- |
| id             | SERIAL       | Unique identifier for each log   |
| status         | INTEGER      | HTTP status code of response     |
| method         | VARCHAR(50)  | HTTP request method (GET/POST/…) |
| uri            | VARCHAR(50)  | Request endpoint                 |
| remote_address | VARCHAR(50)  | Client IP address                |
| request        | VARCHAR(500) | Request payload                  |
| response       | VARCHAR(500) | Response payload                 |
| timestamp      | TIMESTAMP    | Time of the transaction          |

---

## 🛡 Exception Handling Strategy

The application implements a robust exception handling mechanism to ensure reliability when consuming multiple SOAP flight providers.
If a provider fails or returns a SOAP fault, the system continues responding with the remaining valid results.

### 🔐 Custom Exception Hierarchy

| Exception                            | Trigger                         | Responsibility              |
| ------------------------------------ | ------------------------------- | --------------------------- |
| `FlightProviderUnavailableException` | Provider is down / timeout      | Failover to other provider  |
| `FlightProviderBusinessException`    | Provider returns business fault | Inform caller clearly       |
| `FlightException`                    | No provider available           | Client-facing error message |

---

### 🌍 Global Exception Handling (REST Layer)

All consumer-facing errors are normalized using `@RestControllerAdvice`:

```java
@ExceptionHandler(FlightException.class)
public ResponseEntity<ErrorResponse> handleFlightException(FlightException ex) {
    return ResponseEntity.badRequest().body(
        new ErrorResponse(ex.getMessage(), LocalDateTime.now(), null)
    );
}
```

✔ No stack traces leaked to the client
✔ Standardized API error payload

## 🧠 Summary of Benefits

| Benefit                             | Meaning                         |
| ----------------------------------- | ------------------------------- |
| Resilient against provider downtime | System still returns results    |
| Clear business error messages       | Faster debugging for users      |
| Unified API error format            | Better frontend/API integration |
| Full request/response logging       | Enhances monitoring & auditing  |

---

### 🔥 Logging Design

The logging system uses **OncePerRequestFilter** to capture all REST API traffic **without modifying controllers**, ensuring single execution per request:

🧱 `RequestResponseLoggingFilter.java`

### 🗄 Database Table Structure

```sql
CREATE TABLE request_response_log
(
    id SERIAL PRIMARY KEY,
    service_name VARCHAR(50),
    endpoint VARCHAR(200),
    http_method VARCHAR(10),
    request_payload TEXT,
    response_payload TEXT,
    status_code INTEGER,
    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🧪 Unit Testing

The project includes a comprehensive set of automated unit tests designed to ensure full correctness of:

✔ Business rules
✔ Flight aggregation logic
✔ Price optimization (cheapest flight grouping)
✔ Error handling safety
✔ Provider call resilience

Testing is performed using **JUnit 5 + Mockito** with Spring Boot test support.

---

### 🔍 Scope of the Tests

| Test Area                 | Description                                      |
| ------------------------- | ------------------------------------------------ |
| Flight Aggregation        | Combines results from both providers             |
| Cheapest Flight Selection | Groups flights and picks lowest priced per route |
| Null Handling             | Provider failures return partial results safely  |
| Exception Flow            | API remains stable on external service failure   |
| Mapping Validation        | Provider-specific responses correctly unified    |

---

### 📌 Example Test Code

Below is a real test taken from the project:

```java
@Test
void shouldPickCheapestFlightPerGroup() {
    UnifiedFlight flight1 = UnifiedFlight.builder()
            .flightNumber("TK123")
            .origin("IST")
            .destination("AMS")
            .departure(LocalDateTime.now())
            .arrival(LocalDateTime.now().plusHours(3))
            .price(300.0)
            .build();

    UnifiedFlight flight2 = UnifiedFlight.builder()
            .flightNumber("TK123")
            .origin("IST")
            .destination("AMS")
            .departure(flight1.getDeparture())
            .arrival(flight1.getArrival())
            .price(180.0)
            .build();

    when(flightIntegrationService.searchFlightsFromProviderA(searchRequest))
            .thenReturn(List.of(FlightMapper.mapDTOToProviderA(flight1)));

    when(flightIntegrationService.searchFlightsFromProviderB(searchRequest))
            .thenReturn(List.of(FlightMapper.mapDTOToProviderB(flight2)));

    AllFlightsResponse result =
            flightSearchService.getCheapestFlights(searchRequest);

    assertTrue(result.isSuccess());
    assertEquals(1, result.getCount());
    assertEquals(180.0, result.getFlights().get(0).getPrice());
}
```
