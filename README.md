# Car Rental System

A backend system for managing car rentals, including vehicle inventory, customer management, and rental processing. Built using the Spring Framework with RESTful API architecture.

---

## Features

- **Car Management**: Add, update, delete, and view car details.
- **Customer Management**: Register customers, view customer details, and search by name.
- **Rental Management**: Process car rentals and returns with detailed tracking.
- **Global Exception Handling**: Unified error response format.
- **Swagger Integration**: Automatically generated API documentation for easy exploration and testing.

---

## API Documentation

The API follows RESTful principles and returns ISO 8601 formatted datetime strings, e.g., `2024-12-05T23:34:00.000Z`.

### Swagger UI
Swagger provides a detailed interface for exploring and testing the API. Once the application is running, you can access the Swagger UI at:
http://localhost:8080/swagger-ui/index.html



---

## Endpoints Overview

### Car Management Endpoints

- **GET /api/cars**: Retrieve all cars.
- **POST /api/cars**: Add a new car. Requires:
  ```json
  {
    "brand": "Toyota",
    "model": "Camry",
    "yearOfManufacture": 2021,
    "color": "Blue",
    "mileage": 50000,
    "available": true
  }
DELETE /api/cars/{id}: Delete a car. Returns:

"Car deleted successfully!"

Customer Management Endpoints

GET /api/customers: Retrieve all customers.

POST /api/customers: Register a new customer. Requires:

{
  "firstname": "John",
  "surname": "Doe",
  "emailAddress": "john.doe@example.com",
  "phoneNumber": "123456789",
  "address": "123 Main St"
}

Rental Management Endpoints

POST /api/rentals: Create a rental. Requires:

{
  "car": { "id": 1 },
  "customer": { "id": 1 },
  "rentalDate": "2024-12-05T23:34:00.000Z",
  "plannedReturnDate": "2024-12-10T23:34:00.000Z"
}

PUT /api/rentals/{id}/return: Complete a rental. Requires:

"conditionOnReturn": "GOOD"

Running the Application

The application runs on:

http://localhost:8080


Setup Instructions

Clone the repository:

git clone https://github.com/your-repo/car-rental-system.git

Navigate to the project directory:

cd car-rental-system

Build the application:

mvn clean install

Run the application:

mvn spring-boot:run

Known Issues & Future Enhancements

Pagination

For scalability, pagination can be implemented on list endpoints such as GET /api/cars and GET /api/customers.

Searching

Future updates could include search functionality for customers by name.

Database Warning

Tests use the same database as the application, and running tests will erase all data. Consider isolating the test database for production setups.

Known Vulnerabilities

The project depends on spring-beans:6.1.13 and spring-core:6.1.13, which have known vulnerabilities:

These issues are not exploitable in this project due to the following:

No case-sensitive comparisons for authorization are performed.

This application is a demo/non-production system.

For production, upgrading to 6.1.14 or higher is recommended.

Notes on pom.xml

Spring Boot Version: 3.3.4 was chosen to resolve compatibility with Swagger.

Global Exception Handler

All errors return a consistent JSON format:

{
  "error": "Message describing the error"
}
Examples:

404 Not Found:

{
  "error": "Car not found"
}

400 Bad Request:

{
  "error": "Invalid input"
}
