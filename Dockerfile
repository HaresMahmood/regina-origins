
# Use an official Java runtime as a parent image
FROM eclipse-temurin:17-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Compile the Java application
RUN javac Game.java

# Set the default command to run the Java application
CMD ["java", "Game"]