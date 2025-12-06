FROM openjdk:17-jdk-alpine
MAINTAINER eray.kaya
COPY target/FlightProviderB-1.0-SNAPSHOT flightproviderb-1.0.jar
EXPOSE 9002
ENTRYPOINT ["java","-jar","/flightproviderb-1.0.jar"]