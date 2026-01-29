# ---------- BUILD ----------
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Certificados SSL (CRÍTICO)
RUN apt-get update && apt-get install -y ca-certificates curl && update-ca-certificates

# Copiamos wrapper y config primero (mejor cache)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# Descarga dependencias (cache)
RUN ./gradlew dependencies --no-daemon

# Copiamos el código
COPY src src

# Build
RUN ./gradlew clean build -x test --no-daemon


# ---------- RUNTIME ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]