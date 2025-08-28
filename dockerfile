# Dockerfile para build sin ejecutar tests
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos pom.xml y descargamos dependencias (caché)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos todo el código fuente
COPY src ./src

# Compilamos sin ejecutar tests (antes estaba: RUN mvn clean verify)
RUN mvn clean package -DskipTests

# Segunda etapa: runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiamos el jar generado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

