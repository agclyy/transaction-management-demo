FROM openjdk:21-jdk-slim

RUN mkdir /opt/app
# copy the jar pakcage form mvn package result
ADD target/transaction-manage-service-1.0.0.jar /opt/app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/opt/app/app.jar"]
