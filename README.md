# Qcentro_IOT_Device_Apis
Overview
A Spring Boot application that provides REST APIs for managing IoT devices, with MySQL storage and Apache Kafka integration for event-driven communication.

Features
----

Device CRUD operations

Event-driven architecture using Kafka

MySQL data persistence

JWT Role-authentication

Flyway for database migrations

Dockerized deployment


API Endpoints
---------------


Authentication
--------
POST /auth/authenticate/userregister - User Register

POST /auth/authenticate/login - Get JWT token

POST /auth/admin/addRole - Add Role

GET  /auth/admin/role-List - Get List

POST /auth/admin/assign-role - Assign Role

Device Management
----------
POST /api/devices - Register new device

GET /api/devices/{id} - Get All device details With Pagination & Filter

GET /api/devices/{id} - Get device details By Id

PUT /api/devices/{id} - Update device

DELETE /api/devices/{id} - Delete device

POST /api/devices/{id}/send-command - Send command to device


   
Prerequisites
--------

Docker and Docker Compose( Current version: 4.27.1 (136059))

JDK 17+

Maven

MySql 9.0

SpringBoot -  Model Version: 4  ,  parent Version:3.3.4

Setup Instructions
----------

Setup Instructions

Clone the repository:

git clone https://github.com/johnblessjbk/Qcentro_IOT_Device_Apis.git

Start services using Docker Compose:

docker-compose up -d

Access the API documentation:

Swagger UI: http://localhost:8080/swagger-ui.html

This will start:

MySQL database

Kafka broker

Zookeeper

Kafka


![image](https://github.com/user-attachments/assets/16818329-a16d-486c-8b72-b85f6b2568af)


mvn clean package

java -jar target/Authentication_Service-0.0.1-SNAPSHOT.jar


Configuration
-------

Environment variables can be set in application.yml or via Docker:

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/iot_devices
    username: root 
    password: Jenisha@123 ( your password)
    driver-class-name: com.mysql.cj.jdbc.Driver
  kafka:
    bootstrap-servers: kafka:9092


API Documentation
-----
http://localhost:8084/swagger-ui/index.html

spring Acuator included in Api document-http://localhost:8084/actuator

Kafka Topics
------
thingwire.devices.events - Device registration events

thingwire.devices.commands - Device commands

thingwire.devices.responses - Device responses

Database Schema
----

The MySQL database will be automatically initialized with:

CREATE TABLE devices (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status ENUM('ONLINE', 'OFFLINE', 'ERROR') NOT NULL,
    last_seen TIMESTAMP,
    metadata JSON
);

Testing

Run unit tests:
mvn test

----------------------------------------------------------------------
docker-compose up -d - to dockerize
docker ps- check all running images

For - Kafka Test Commands
1. Produce a Test Message

docker exec -it authentication_service-kafka-1 kafka-console-producer --topic test --bootstrap-server localhost:9092
(enter msg)
2. Consume the Message

docker exec -it authentication_service-kafka-1 kafka-console-consumer --topic test --bootstrap-server localhost:9092 --from-beginning
( receiving msg)

-------------------API call------------------
Response:

{
  "status": "success",
  "message": "Device registered successfully",
  "data": {
    "id": "33145432-8c47-49f5-9760-ba42100b6bd0",
    "name": "sw",
    "status": "ONLINE",
    "lastSeen": "2025-04-06T11:03:32.3982047",
    "metadata": "{\"Memory\": \"2GB\"}"
  },
  "timestamp": "2025-04-06 11:03:32"
}
--------
send command:
---

{
  "commandType": "RESTART",
  "payload": "{\"delay\": 5}"
}
------------

{
  "status": "success",
  "message": "Command sent to device",
  "data": null,
  "timestamp": "2025-04-06 11:21:08"
}


-------to check the consumer-----


docker exec -it authentication_service-kafka-1 kafka-console-consumer --bootstrap-server localhost:9092 --topic thingwire.devices.commands --from-beginning


{"deviceId":"8bed4003-5e20-4e5a-98e6-16207944430f","eventType":"device_command","timestamp":[2025,4,6,7,11,36,815768200],"payload":{"command":"RESTART","payload":"{\"delay\": 5}"}}
