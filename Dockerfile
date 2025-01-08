FROM openjdk:21
COPY out/artifacts/Kasper_jar/Kasper.jar /out/artifacts/Kasper_jar/
WORKDIR /out/artifacts/Kasper_jar/
RUN chmod +r Kasper.jar
EXPOSE 53182
CMD java -jar Kasper.jar
