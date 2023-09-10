FROM openjdk:17

WORKDIR /app

COPY out/artifacts/bank_jar/bank.jar /app/

EXPOSE 8080

CMD ["java", "-jar", "bank.jar"]
