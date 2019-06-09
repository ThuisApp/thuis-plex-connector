# docker build -t thuisapp/thuis-plex-connector .
# kubectl run thuis-plex-connector --image=thuisapp/thuis-plex-connector --port=8080 --image-pull-policy=IfNotPresent
# kubectl expose deployment thuis-plex-connector --type=NodePort
# minikube service thuis-plex-connector --url

#
# Build stage
#
FROM maven:3.6.0-jdk-8-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM arm32v7/openjdk:8-alpine
COPY --from=build /home/app/target/thuis-plex-connector-runner.jar /usr/local/lib/thuis-plex-connector-runner.jar
COPY --from=build /home/app/target/lib /usr/local/lib/lib
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/thuis-plex-connector-runner.jar"]