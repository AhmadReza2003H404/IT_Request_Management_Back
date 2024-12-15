# IT_Request_Management_Back

# Project Setup Guide

This guide helps you set up the MySQL database and prepare it for use with your Spring Boot project.

---

## Step 1: Install MySQL

2. During installation, configure:
1. Download and install MySQL from the [official website](https://dev.mysql.com/downloads/mysql/).
    - Set the **root password**.
    - Enable MySQL as a service (optional).

---

## Step 2: Configure the Database

### 2.1 Access the MySQL Command-Line Client

1. Open the MySQL Command-Line Client from the Start Menu or terminal.
2. Log in using the **root** user:
   ```bash
   mysql -u root -p
   ```
3. Enter your root password when prompted.

### 2.2 Run the SQL Commands

Execute the following SQL commands to create the database and user:

```sql
CREATE DATABASE mydb;
CREATE USER 'user'@'localhost' IDENTIFIED BY 'pass';
GRANT ALL PRIVILEGES ON mydb.* TO 'user'@'localhost';
FLUSH PRIVILEGES;
```

---

## Step 2: Run the Project

1. Start your MySQL server.
2. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Flyway will automatically apply the migration scripts during startup.

---

---

## Step 3: Configure Your Spring Profile


To run the project, ensure your **Spring profile** is set to `dev` for local development.

To set the Spring profile to `dev` in IntelliJ IDEA:

1. Open **IntelliJ IDEA** and navigate to **Run** > **Edit Configurations**.
2. Select the run configuration for your project (in your case, it appears to be `RequestManagementApplication`).
3. In the **Active profiles** field, enter `dev`. You can list multiple profiles by separating them with commas if needed.
4. Click **Apply** and then **OK**.

This will ensure that the `dev` profile is used when you run the Spring Boot application from within IntelliJ IDEA.


---


### Structure and Architecture

#### Controllers

In your Spring Boot application, **each controller** will be responsible for handling incoming HTTP requests. These controllers should **not directly interact with the database**. Instead, they delegate the logic to the **service layer**.

#### Services

The **service layer** will contain the business logic of your application. It is structured into two parts:
- **Service Interface**: This defines the contract for the operations that the service will provide. The interface ensures loose coupling, allowing for easier testing and flexibility.
- **Service Implementation**: This implements the service interface and contains the actual logic for interacting with the repositories and performing business operations.

#### Repositories

**Repositories** are responsible for interacting directly with the database using Spring Data JPA. They will execute the database commands triggered by the service layer.

#### DTOs (Data Transfer Objects)

The **service layer** should return **DTOs** instead of direct entities. This is important because:
- Not all fields in the entity may be needed or should be exposed via the API.
- DTOs allow you to tailor the data structure for the response, improving performance and security.

In the service layer, you should map your entity to a DTO using a **mapper** or **model mapper** utility. DTOs should be located in a separate **DTO package**.

After mapping the DTO, it will be sent to the **controller**, which then formats it into the appropriate **response object** for the client.



## Troubleshooting

- **Cannot connect to database**: Ensure MySQL is running and the `url`, `username`, and `password` in the properties file are correct.
- **Migration errors**: Check for syntax issues in your Flyway SQL files.

For additional support, refer to the [Spring Boot documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) or the [Flyway documentation](https://flywaydb.org/documentation/).
