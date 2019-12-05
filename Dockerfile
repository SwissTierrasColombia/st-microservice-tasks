FROM openjdk:12

VOLUME /tmp

ADD ./target/st-microservice-tasks-0.0.1-SNAPSHOT.jar st-microservice-tasks.jar

EXPOSE 8080

ENTRYPOINT java -jar /st-microservice-tasks.jar