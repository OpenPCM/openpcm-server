FROM maven:3.5.4-jdk-9-slim

ENV PORT = 8080

EXPOSE 8080

COPY . /code-repo
WORKDIR /code-repo
RUN mvn package spring-boot:repackage -DskipTests=true
RUN mv /code-repo/target/*.jar /openpcm.jar
RUN rm -rf /code-repo
RUN cd /
CMD ["java", "-jar", "/openpcm.jar"]
