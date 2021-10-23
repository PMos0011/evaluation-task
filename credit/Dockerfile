FROM openjdk:11.0.4-jre-slim
VOLUME /tmp
COPY target/credit-1.0.0.jar credit.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java -jar /credit.jar