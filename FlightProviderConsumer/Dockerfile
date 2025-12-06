FROM openjdk:17-jdk-alpine
MAINTAINER eray.kaya
COPY target/FlightProviderConsumer-0.0.1-SNAPSHOT.jar flighproviderconsumer-0.0.1.jar
EXPOSE 9000
ENTRYPOINT ["java","-jar","/flighproviderconsumer-0.0.1.jar"]