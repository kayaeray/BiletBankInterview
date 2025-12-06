FROM openjdk:17-jdk-alpine
MAINTAINER eray.kaya
COPY target/FlightProviderA-1.0-SNAPSHOT flightprovidera-1.0.jar
EXPOSE 9001
ENTRYPOINT ["java","-jar","/flightprovidera-1.0.jar"]