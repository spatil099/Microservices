# Microservices Springboot 3 Project 
A simple shopping application with microservices architecture using Springboot 2, Java 21 and Maven build script.
It is yet a work in progress (Tests are pending)

# All the details of individual services are explained below
## ShoppingApp 
This folder contains bruno collections used for testing

## package structure
All of the services follow a similar package structure
1. controller : contains Controller of the respective service.
2. model : contains bean classes required for the respective service.
3. repository : appropriate repository for the respective service.
4. config : contains appropriate Configurations required for various purposes (openAPI ...) for the respective service.
5. dto : contains Data Transfer Object bean for the respective service so that model elements are not exposed to other services.
6. service : contains service class implementations for all the operations on repo.
7. event : All the events are in this package if Event based communication is established.

## Product Service
This is the service responsible for all CRUD actions related to products (model.Product class)
Database : mongodb (docker image : mongo:7.0.5)
server port : http://localhost:8080/
swagger doc : http://localhost:8080/swagger-ui.html

dependencies :
1. openApi for swagger documentation
2. io.rest-assured, junit, testcontainers for test cases
3. lombok for annotations to make code compact (beans, slf4j, constructors.....)
4. mongodb for repository
5. docker-compose-support to use docker file inbuilt

## Inventory Service
This is the service responsible to check if the product request by skuCode is available in the inventory 
Database : mysql (docker image : mysql:8.1)
Initialized with docker/mysql/init.sql (check volumes section in docker-compose.yml)
server port : http://localhost:8082/
swagger doc : http://localhost:8082/swagger-ui.html
migration scripts in resources/db/migration

dependencies :
1. openApi for swagger documentation
2. io.rest-assured, junit, testcontainers for test cases
3. lombok for annotations to make code compact (beans, slf4j, constructors.....)
4. mysql for repository
5. docker-compose-support to use docker file inbuilt
6. jpa for persistence using hibernate and Spring Data
7. mysql-connector-j for mysql driver
8. flyway for database migration

## Order Service
This is the service responsible to place order once it has verified that product is in the inventory
communication between order and inventory services is established via RestClient (Event based) for Sync communication,
since it has to wait for response from inventory service to actually place the order.
Circuit breaker is implemented (check InventoryClient.class for @CircuitBreaker) if it breaks then return false for inventory req.

Database : mysql (docker image : mysql:8.1)
Initialized with docker/mysql/init.sql (check volumes section in docker-compose.yml)
server port : http://localhost:8081/
swagger doc : http://localhost:8081/swagger-ui.html
migration scripts in resources/db/migration

dependencies :
1. openApi for swagger documentation
2. io.rest-assured, junit, testcontainers, wiremock for test cases
3. lombok for annotations to make code compact (beans, slf4j, constructors.....)
4. mysql for repository
5. docker-compose-support to use docker file inbuilt
6. jpa for persistence using hibernate and Spring Data
7. mysql-connector-j for mysql driver
8. flyway for database migration
9. avro for schema registration so that event is common across producer and consumer.
10. resilience4j for circuitbreaker

## api-gateway Service
This is the service which acts as gateway to outer world and provides appropriate service when requested

Initialized with docker/mysql/init.sql (check volumes section in docker-compose.yml)
server port : http://localhost:9000/
swagger doc : http://localhost:9000/swagger-ui.html (aggregated doc check application.properties)

keycloak authentication pending (issue with dependencies will check it later)

dependencies :
1. openApi for swagger documentation
2. io.rest-assured, junit, testcontainers for test cases
3. lombok for annotations to make code compact (beans, slf4j, constructors.....)
4. gateway for api gateway maintainence () check routes package with circuitbreaking impl
5. docker-compose-support to use docker file inbuilt
6. actuator, resilience4j for health monitoring & circuitbreaker
7. keycloak for authentication

## notification Service (In progress) 
This is the service responsible to send notifications

Trying to send out a mail to the email id provided my the user with ordernumber mentioned in hte body text.
Using mailtrap.io

Initialized with docker/mysql/init.sql (check volumes section in docker-compose.yml)
server port : http://localhost:8083/

dependencies :
1. io.rest-assured, junit, testcontainers for test cases
2. lombok for annotations to make code compact (beans, slf4j, constructors.....)
3. avro for schema registration so that event is common across producer and consumer.
4. Apache kafka for notifications


## TODO
1. Finish notification service
2. Implement authentication using keycloak
3. Write junit test cases for individual services
4. Containerize each of the services and deploy using k8s
5. Write contract tests and integration tests
