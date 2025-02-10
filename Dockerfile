# Use an OpenJDK 17 slim image as the base
FROM openjdk:17-jdk-slim

# Create a working directory
WORKDIR /app

# Copy the packaged jar file from the target directory into the container
COPY target/scheduler-app-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
