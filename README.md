# Fintech-company
___
Project for processing loan applications. Developed as part of the Tinkoff Backend Academy.

Includes modules:

* Api - service for working with the client part.
* Origination - service of asynchronous scheduled processing of incoming applications.
* Scoring - service that checks the client and makes a decision on the application.
* Payment-Engine - service storing all information about payments and products.

All services contain their own documentation. (See 'doc' in module directory).

## Stack

* Spring
* PostgreSQL
* Hibernate
* REST
* gRPC
* Docker
* Gradle
* MapStruct
* Liquibase
* Testcontainers

## Application startup 
For application startup using:
```
./gradlew clean bootJar
docker-compose up
```

