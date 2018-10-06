FROM airdock/oracle-jdk:1.8

ENV DOCKERIZE_VERSION v0.2.0
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

RUN mkdir /app
WORKDIR /app
COPY build/libs/hd-cloud-thirdparty.jar /app
ENTRYPOINT ["dockerize", "-timeout", "5m", "-wait", "http://hd-config-server:8100/health", "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/hd-cloud-thirdparty.jar", "--spring.profiles.active=docker"]
EXPOSE 8080