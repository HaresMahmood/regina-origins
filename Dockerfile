# Build stage
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn package

# Runtime stage
FROM openjdk:17-slim
COPY --from=build /app/target/regina-origins-1.0.jar /app/target/lib/* /app/
CMD ["java", "-cp", "/app/regina-origins-1.0.jar:/app/lib/*", "com.lbg.Main"]