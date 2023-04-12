FROM openjdk:11-jre-slim

WORKDIR /app

COPY ./target/Static-project-0.0.1-SNAPSHOT.jar ./ticker-stat.jar

ENV MONGODB_URI=mongodb+srv://vlad:1234.com@clusterjava2022.keunyxc.mongodb.net/first-project?retryWrites=true&w=majority

CMD ["java", "-jar", "/app/ticker-stat.jar"]