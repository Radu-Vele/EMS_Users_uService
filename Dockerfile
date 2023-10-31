FROM openjdk:17-jdk-alpine
COPY target/users-us-0.0.1-SNAPSHOT.jar energy-ms-users-us.jar

ENTRYPOINT ["java", "-jar", "energy-ms-users-us.jar"]
