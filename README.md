# Email-sender-api

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen?style=flat-square&logo=spring-boot)
![Lombok](https://img.shields.io/badge/Lombok-1.18.26-red?style=flat-square&logo=lombok)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0.0-blue?style=flat-square&logo=openapi-initiative)
![JUnit](https://img.shields.io/badge/JUnit-5-purple?style=flat-square&logo=junit5)
![Maven](https://img.shields.io/badge/Maven-3.x-red?style=flat-square&logo=apache-maven)
![Mit License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

## 📋 Description

**Email-sender-api** is a RESTful application developed with **Spring Boot** that allows sending emails in a simple and efficient way. This API is designed to be integrated easily with other applications and services, giving a robust solution for managing email shipment.


### 🎯 Main Features

- ✅ **Email Shipment**: This app supports sending simple emails or with attachments.
- ✅ **API RESTful**: Well defined endpoints to interact with the sending emails service.
- ✅ **OpenAPI Documentation**: Swagger UI interface to explore and try endpoints.
- ✅ **Personalized Exceptions**: Clear and detailed answers to common errors.
- ✅ **Unit Tests**: Tests mades with JUnit for better quality.

## 📋 Main Endpoints

Here are the main endpoints of the API::

| Method | Endpoint               | Description                       |
|--------|------------------------|-----------------------------------|
| POST   | /api/v1/emails/send    | Sends an email                    |
| POST   | /api/v1/emails/sendWithAttachment | Sends an email with an attachment |

## 🏗️ Architecture

This project follows a well defined architecture:

```
┌─────────────────────────────────────┐
│         Controllers                 │
│      (REST API Endpoints)           │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│          Services                   │
│    (Business Logic)                 │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Email Sending Layer          │
│   (JavaMailSender Integration)      │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Configuration Layer          │
│   (Email Server Settings)           │
└─────────────────────────────────────┘
```
All layers have specific tasks, making sure there is a clear separation between each one. This allows easier maintenance and scalability of the project.

### How to Run the Application
1. Clone the repository:
   ```bash
    git clone <https://github.com/matepore/email-sender-api>
    cd email-sender-api
    ```
2. Configure the properties of the email credentials and service in `application.properties`.
3. Run the application using Maven:
   ```bash
    mvn spring-boot:run
    ```
4. Access the documentation of the API in `http://localhost:8080/swagger-ui.html`.
5. Use the endpoints to send emails.
6. Run the unit tests with:
   ```bash
    mvn test
    ```
   
## 🛠️ Used Technologies
- **Java 21**: Main programming language
- **Spring Boot 3.5.7**: Main framework for building the application
- **Spring Mail**: Integration for email shipment
- **SpringDoc OpenAPI**: Automatic documentation of the API
- **JUnit 5**: Unit testing framework
- **Maven**: Dependency and build management
- **Lombok 1.18.26**: Code simplification with annotations

## 📝 License

This project is licensed for educational and personal use under the MIT License.

## 👥 Author
**Mateo Calcagno**
- 📧 Email: calcagno.mateo@gmail.com
- 📱 Phone: +54 9 11 3119-1742
- 🌎 Nationality: Argentina
- 🎓 Education: Técnico Universitario en Desarrollo de Software
- 🏛️ Institution: Universidad de Ezeiza