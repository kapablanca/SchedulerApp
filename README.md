# SchedulerApp

SchedulerApp is a full-stack scheduling application built with Java, Spring Boot, Thymeleaf, and PostgreSQL. It allows administrators to manage People, Shifts, Users, and Schedules, while regular users can view People, Shifts, and Schedules. The application features role-based access control and a modern web interface.

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Building the Application](#building-the-application)
- [Running the Application Locally](#running-the-application-locally)
- [Docker Deployment](#docker-deployment)
- [Database Seeding](#database-seeding)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Features

- **People Management:** Create, edit, delete, and view people.
- **Shift Management:** Define shifts (e.g., Morning, Evening, Night, Day) with automatic end time calculation.
- **Schedule Management:** Assign people to shifts on specific dates.
- **User Management:** Admin-only interface for managing users.
- **Role-Based Access:**
  - **Admin:** Full CRUD operations on People, Shifts, Users, and Schedules.
  - **User:** View-only access to People, Shifts, and Schedules.
- **Responsive UI:** Built with Thymeleaf and CSS for a clean and modern look.

## Requirements

- Java 17 or higher
- Maven
- PostgreSQL
- (Optional) Docker and Docker Compose for containerized deployment

## Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/your-username/SchedulerApp.git
   cd SchedulerApp
   ```

2. **Set Up PostgreSQL:**

   Ensure PostgreSQL is installed and running. Create a database named `schedulerdb` and a user (e.g., username: `scheduler`, password: `scheduler`). You can adjust these settings in the configuration (see below).

## Configuration

The application configuration is located in `src/main/resources/application.properties`. Key settings include:

```properties
spring.application.name=SchedulerApp

# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/schedulerdb
spring.datasource.username=scheduler
spring.datasource.password=scheduler
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML5
spring.thymeleaf.encoding=UTF-8
spring.web.resources.static-locations=classpath:/static/,classpath:/META-INF/resources/,classpath:/META-INF/resources/webjars/

server.servlet.session.persistent=true
spring.session.timeout=30m

logging.level.org.springframework.security=DEBUG
```

**Note:** Adjust these values as necessary for your environment.

## Building the Application

To build the project, run:

```bash
mvn clean package
```

This will compile the source code and package the application into an executable JAR file located in the `target/` directory.

## Running the Application Locally

You can run the application in one of two ways:

1. **Using Maven:**

   ```bash
   mvn spring-boot:run
   ```

2. **Using the JAR File:**

   ```bash
   java -jar target/SchedulerApp.jar
   ```

Once running, access the application at `http://localhost:8080`.

## Docker Deployment

You can deploy SchedulerApp along with a PostgreSQL database using Docker Compose.

### Dockerfile

Ensure you have a `Dockerfile` in your project root:

```dockerfile
# Use OpenJDK 17 JRE slim as the base image
FROM openjdk:17-jre-slim

WORKDIR /app

# Copy the packaged jar file (adjust the name if necessary)
COPY target/SchedulerApp.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml

Also, create a `docker-compose.yml` file in your project root:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:13-alpine
    container_name: scheduler_postgres
    restart: always
    environment:
      POSTGRES_DB: schedulerdb
      POSTGRES_USER: scheduler
      POSTGRES_PASSWORD: scheduler
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: ../../Users/stath/Downloads
    container_name: scheduler_app
    restart: always
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/schedulerdb
      SPRING_DATASOURCE_USERNAME: scheduler
      SPRING_DATASOURCE_PASSWORD: scheduler

volumes:
  postgres_data:
```

### Running with Docker Compose

1. **Build the JAR:**

   ```bash
   mvn clean package
   ```

2. **Start Docker Compose:**

   ```bash
   docker-compose up --build
   ```

The application will be available at `http://localhost:8080`.

## Database Seeding

On startup, a database seeder automatically populates the database with sample data:

- **Users:**
  - **Admin:** Username `admin` with password `admin123`
  - **Test User:** Username `testuser` with password `test123`

- **People:** At least five sample entries (e.g., Alice Smith, Bob Johnson, Charlie Williams, Diana Brown, Ethan Davis).

- **Shifts:** Six sample shifts:
  - Two Morning Shifts (starting at 07:00)
  - Two Evening Shifts (starting at 15:00)
  - One Night Shift (starting at 23:00)
  - One Day Shift (starting at 10:00)

This seeded data allows you to test and explore the app immediately.

## Usage

- **Admin Users:** Log in as `admin` to manage People, Shifts, Users, and Schedules.
- **Regular Users:** Log in as `testuser` to view People, Shifts, and Schedules (the Users view is restricted to Admin only).

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests. For major changes, open an issue first to discuss your ideas.

## License

This project is licensed under the MIT License. See the LICENSE file for details.
