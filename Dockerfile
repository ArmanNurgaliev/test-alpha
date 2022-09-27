FROM openjdk:17

COPY target/test-alpha-0.0.1-SNAPSHOT.jar test-alpha-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/test-alpha-0.0.1-SNAPSHOT.jar"]