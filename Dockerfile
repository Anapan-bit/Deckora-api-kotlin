# ---------- Etapa 1: Construcción ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos pom.xml y descargamos dependencias (cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Construimos el JAR sin tests
RUN mvn clean package -DskipTests

# ---------- Etapa 2: Runtime ----------
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiamos el jar generado en la etapa 1
COPY --from=build /app/target/*.jar app.jar

# Render usa un puerto asignado dinámicamente
ENV PORT=8080
EXPOSE 8080

# Activamos perfil "prod"
ENV SPRING_PROFILES_ACTIVE=prod

# Ejecutar el JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
