FROM openjdk:18

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/app.jar"]