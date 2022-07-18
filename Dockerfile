FROM openjdk:18-alpine

WORKDIR /app

COPY build/libs/rseye-0.0.1-SNAPSHOT.jar .
RUN chmod +x rseye-0.0.1-SNAPSHOT.jar

EXPOSE 8443

ENTRYPOINT ["java", "-Xms1G", "-Xmx2G", "-jar", "rseye-0.0.1-SNAPSHOT.jar"]