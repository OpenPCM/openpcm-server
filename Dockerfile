FROM openjdk:9

ENV PORT = 14606

EXPOSE 14606

COPY . /code-repo
WORKDIR /code-repo
RUN mvn install
COPY /code-repo/target/*jar /openpcm.jar
RUN rm -rf /code-repo

CMD["java", "-jar", "/openpcm.jar"]