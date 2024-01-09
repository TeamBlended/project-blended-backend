FROM openjdk:17-jdk
EXPOSE 8089
ARG JAR_FILE=/build/libs/blended-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","-Daws.region=ap-northeast-2","/app.jar"]