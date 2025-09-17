# Etapa de build con Maven
FROM maven:3.9.3-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom.xml y descargamos dependencias (caché)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos todo el código fuente
COPY src ./src

# Compilamos sin ejecutar tests
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiamos el jar generado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Arrancamos la app (Render inyectará SPRING_PROFILES_ACTIVE=docker desde env vars)
ENTRYPOINT ["java", "-jar", "app.jar"]
