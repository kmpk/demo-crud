FROM eclipse-temurin:21-jre-alpine
RUN mkdir /app
COPY /target/demo-crud-0.0.1-SNAPSHOT.jar /app/demo.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]