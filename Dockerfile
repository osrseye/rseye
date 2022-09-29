FROM openjdk:19-alpine

WORKDIR /app

COPY build/libs/rseye-1.0.0.jar .
RUN chmod +x rseye-1.0.0.jar

EXPOSE 8443

ENTRYPOINT ["java", "-Xms1G", "-Xmx2G", "-jar", "rseye-1.0.0.jar"]