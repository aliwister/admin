# Use a base image with Maven to build the project
FROM maven:3.8.4-openjdk-11 AS build

# Copy the Maven project and build it
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean package -D skipTests -P no-liquibase

# Use a base image with Java to run the application
FROM eclipse-temurin:11-jdk-alpine

# Copy the built jar file into the image from the previous stage
COPY --from=build /usr/src/app/target/*.jar /app.jar

# Set the entry point to run your application
ENTRYPOINT ["java","-jar","/app.jar"]
