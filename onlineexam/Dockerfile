# ---------- BUILD STAGE ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests


# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# 👉 Copy Firebase service account key
COPY serviceAccountKey.json /app/serviceAccountKey.json

# 👉 Set Firebase credentials environment variable
ENV GOOGLE_APPLICATION_CREDENTIALS=/app/serviceAccountKey.json

# Copy application JAR
COPY --from=build /app/target/*.jar app.jar

# Fix permissions so spring user can read the JSON
RUN chown spring:spring /app/serviceAccountKey.json

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
