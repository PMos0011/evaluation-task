FROM openjdk:11.0.4-jre-slim
VOLUME /tmp
COPY target/customer-1.0.0.jar customer.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java -jar /customer.jar