FROM openjdk:21
COPY . /usr/src/myapp
WORKDIR /usr/src/myapp
RUN mvn package
CMD ["java", "-jar", "./target/task-0.0.1-SNAPSHOT.jar"] 