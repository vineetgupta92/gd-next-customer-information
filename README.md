# GD Next Backend Project

## Overview

This is a backend project developed using Spring Boot. The application is designed to manage user data, including user details like birthdate, birthplace, sex, and address. It interacts with a database and provides endpoints for CRUD operations.

## Features

- User management with fields such as:
    - First Name
    - Last Name
    - Birthdate
    - Birthplace
    - Sex
    - Address
- Dynamic form handling for missing user fields
- Integration with external services
- Transactional management to ensure data consistency

## Technologies Used

- **Java**: Programming language
- **Spring Boot**: Framework for building the application
- **H2 Database**: In-memory database for development and testing
- **Lombok**: For reducing boilerplate code
- **JUnit & Mockito**: For unit testing
- **Maven**: Dependency management and build tool

## Installation

### Prerequisites

- Java 17 or higher
- Maven

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/vineetgupta92/gd-next-customer-information.git
2. Run the application:
   ```bash
   mvn spring-boot:run
3. Access the application at `http://localhost:8081/api/form/fetch-all-users`, you should see all the default users 
   which ran as part of script under `data.sql` file.
