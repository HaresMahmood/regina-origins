# Use an official Maven image as a parent image
FROM maven:3.8.5-openjdk-17-slim

# Set the working directory to /app
WORKDIR /app

# Copy the pom.xml file to the container at /app
COPY pom.xml .

# Download the dependencies specified in the pom.xml file
RUN mvn dependency:go-offline

# Copy the rest of the project files to the container at /app
COPY . .

# Build the Maven project
RUN mvn package

# Set the default command to run the built jar file
CMD ["java", "-jar", "target/regina-origins-1.0.jar"]
