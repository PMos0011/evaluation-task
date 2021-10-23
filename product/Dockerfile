FROM openjdk:11.0.4-jre-slim
VOLUME /tmp
COPY target/product-1.0.0.jar product.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java -jar /product.jar