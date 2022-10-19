FROM openjdk:19-alpine

WORKDIR /app

RUN apk add --no-cache wget && \
    apk add --no-cache unzip && \
    wget https://github.com/osrseye/rseye/archive/refs/heads/main.zip && \
    unzip main.zip && \
    rm main.zip && \
    rm rseye-main/data/token.txt && \
    cp -r rseye-main/data/ . && \
    rm -r rseye-main &&

COPY build/libs/rseye-1.0.0.jar .
RUN chmod +x rseye-1.0.0.jar

EXPOSE 8443

ENTRYPOINT ["java", "-Xms1G", "-Xmx2G", "-jar", "rseye-1.0.0.jar"]