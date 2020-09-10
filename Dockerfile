FROM openjdk:11

ARG XMX=1024m
ARG PROFILE=production
ARG CLOUD_CONFIG

ENV XMX=$XMX
ENV PROFILE=$PROFILE
ENV CLOUD_CONFIG=$CLOUD_CONFIG

VOLUME /tmp

ADD ./target/st-microservice-tasks-0.0.1-SNAPSHOT.jar st-microservice-tasks.jar

EXPOSE 8080

ENTRYPOINT java -Xmx$XMX -jar /st-microservice-tasks.jar --spring.profiles.active=$PROFILE --spring.cloud.config.uri=$CLOUD_CONFIG