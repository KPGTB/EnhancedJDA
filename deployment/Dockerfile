FROM azul/zulu-openjdk-alpine:17-latest

RUN mkdir /app
COPY *.jar /app/bot.jar
WORKDIR /app

ENTRYPOINT ["java", "-jar", "bot.jar"]
