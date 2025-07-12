FROM eclipse-temurin:24-alpine

USER root

WORKDIR /app

RUN apk add --no-cache wget && \
    apk add --no-cache unzip && \
    wget https://github.com/osrseye/rseye/archive/refs/heads/main.zip && \
    unzip main.zip && \
    rm main.zip && \
    rm rseye-main/data/token.txt && \
    mv rseye-main/data/* . && \
    rm -r rseye-main

COPY build/libs/rseye-1.0.1.jar .
RUN chmod +x rseye-1.0.1.jar

EXPOSE 8443

ENTRYPOINT ["java", "-Xms256M", "-Xmx512M", "-jar", "rseye-1.0.1.jar"]