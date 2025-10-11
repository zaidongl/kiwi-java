FROM openjdk:17

# Set the working directory to /app
WORKDIR /app

# Copy the fat jar into the container at /app
COPY /config ./config
COPY /features ./features
COPY /data ./data
COPY /target/examples-1.0-SNAPSHOT-jar-with-dependencies.jar /app/examples-test-suite.jar

# Run jar file when the container launches
CMD ["java", "-jar", "examples-test-suite.jar"]