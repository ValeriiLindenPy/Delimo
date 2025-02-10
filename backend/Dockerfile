
FROM openjdk:21-jdk AS builder
WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

COPY src src

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

RUN ls -la target

FROM openjdk:21-jdk
WORKDIR /app

COPY --from=builder /app/target/delimo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
