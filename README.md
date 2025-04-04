A Spring Boot application that connects to the GitHub REST API to retrieve non-forked repositories and their branches for a given user.



## Technologies

- Java 21
- Spring Boot
- Maven
- JUnit 5
- WireMock (for mocking external APIs)
- Lombok
- GitHub REST API

---

## Endpoint to get data
http://localhost:8080/api/github/users/{username}/repos


### Prerequisites

- Java 21
- Maven

### Running the app

mvn spring-boot:run
