FROM openjdk:11 as build
MAINTAINER tihomir@temporal.io
COPY target/replaydemo-client-1.0.0.jar replaydemo-client-1.0.0.jar
ENTRYPOINT ["java","-jar","/replaydemo-client-1.0.0.jar"]