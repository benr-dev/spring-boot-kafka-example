# Spring Boot Kafka example
Example of configuring a Kafka producer using Spring Boot

# Build
~~~
./gradlew clean build
~~~

# Run
~~~ 
docker-compose up
~~~

# Notes
* Docker compose starts up:
    - Zookeeper
    - Kafka (single node with a single topic "test")
    - Zookeeper UI on localhost:9092
    - Kafka UI on localhost:9000
    - Example app (POST message text to localhost:8080/posts/{topic})

# Useful references
* https://docs.spring.io/spring-kafka/docs/current/reference/html/
* https://www.baeldung.com/spring-kafka
* https://success.docker.com/article/getting-started-with-kafka
