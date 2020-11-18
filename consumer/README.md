This project reproduces issue https://github.com/spring-cloud/spring-cloud-sleuth/issues/1731

Run Jaeger and RabbitMQ via docker-compose

```
docker-compose up
```

Run consumer

```
cd consumer
./gradlew bootRun
```

Run producer

```
cd producer
./gradlew bootRun
```

Test using curl

curl -XPOST localhost:8080/


You should see the trace ids in the logs and in jeager
