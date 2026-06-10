FROM selenium/standalone-chrome:latest

USER root

WORKDIR /app

RUN apt-get update && apt-get install -y maven openjdk-21-jdk

COPY . .

RUN mvn clean install -DskipTests

CMD ["mvn", "test",]