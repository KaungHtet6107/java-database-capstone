# Schema Architecture

## Section 1: Architecture Summary

This Spring Boot application follows a layered architecture using both MVC and REST controllers. MVC controllers are responsible for rendering Thymeleaf web pages for modules such as the Admin and Doctor dashboards, while REST controllers provide JSON APIs for other application features. The application separates presentation, business logic, and data access into different layers to improve maintainability and scalability.

The application communicates with two databases. MySQL stores structured relational data such as patients, doctors, appointments, and administrators using JPA entities and Spring Data JPA. MongoDB stores prescription data as document models using Spring Data MongoDB. Controllers send requests to the validationService layer, which contains the application's business logic. The validationService layer then communicates with the appropriate repository to retrieve or update data before returning the result to the controller.

---

## Section 2: Numbered Flow of Data and Control

1. A user accesses the application through a web page or REST API endpoint.
2. The request is received by either an MVC controller (for Thymeleaf pages) or a REST controller (for JSON responses).
3. The controller validates the request and forwards it to the appropriate validationService class.
4. The validationService layer executes the business logic and determines which database should be accessed.
5. The validationService communicates with either the MySQL JPA repository or the MongoDB repository to perform database operations.
6. The repository retrieves, saves, updates, or deletes data and returns the result to the validationService layer.
7. The validationService returns the processed data to the controller, which sends either a rendered Thymeleaf page or a JSON response back to the user.