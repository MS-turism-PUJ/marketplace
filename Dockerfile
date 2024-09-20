FROM openjdk:17-jdk-alpine
COPY target/marketplace-*.jar java-app.jar
ENTRYPOINT [ "java", "-jar", "java-app.jar"]
