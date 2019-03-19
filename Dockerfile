FROM openjdk:jre-slim

RUN mkdir /app
COPY build/distributions/overlay.zip /app
WORKDIR /app
RUN unzip overlay.zip -d .
CMD ./overlay/bin/overlay $MODE