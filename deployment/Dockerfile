FROM amazoncorretto:21

RUN mkdir /app
COPY *.jar /app/bot.jar
WORKDIR /app

ENTRYPOINT ["java", "-jar", "bot.jar"]
