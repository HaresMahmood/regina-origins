# Use an official Maven image as a parent image
FROM maven:3.8.5-openjdk-17-slim

# Set the working directory to /app
WORKDIR /app

# Copy the pom.xml file to the container at /app
COPY pom.xml .

# Extract the application name from the pom.xml file and set it as an environment variable
RUN export APP_NAME=$(xmlstarlet sel -t -v '//artifactId' pom.xml)

# Download the dependencies specified in the pom.xml file
RUN mvn dependency:go-offline

# Copy the rest of the project files to the container at /app
COPY . .

# Build the Maven project
RUN mvn package

# Set the default command to run the built jar file
CMD ["java", "-jar", "target/${APP_NAME}.jar"]
