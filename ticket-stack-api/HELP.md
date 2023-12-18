# Read Me First

# Description
An application to track and maintain tasks.

# Pre-Requirements
1. Java 17
2. MySQL-MariaDB (not included)

### Features
* Kafka messaging
* Hibernate
* JPA

# Deploy:
Run `docker-compose up -d` to deploy the app and supporting containers.

# Local run:
To run the application from your local environment (such as your IDE) it requires connection to Kafka. If you want to run the api from your IDE environment, it is recommended that you deploy everything, then stop the api container to avoid port conflicts.

### Quick clean deploy ###
From the project directory, run the CleanDeployDev.sh script. This will re-package the jar, clean out the old docker images/containers, and redeploy new ones.

### Quick Commands ###
1. Clear docker caches: `docker system prune -a`
2. List all Kafka topics (from inside the kafka container): `kafka-topics.sh --bootstrap-server 127.0.0.1:9093 --list`
4. Create a Kafka `test` topic (inside the kafka container): run command and press `Ctrl+C` when done.: `kafka-console-producer.sh --bootstrap-server 127.0.0.1:9093 --topic test`
5. List all messages for a Kafka `test` topic (inside container): run command and press `Ctrl+C` when done.: `/opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9093 --topic test --from-beginning`

# Documentation
### API 
Documentation by Swagger and can be found at http://localhost:8080/swagger-ui/index.html

### Kafka
* Topics are created in `com.vancefm.ticketstack.kafka.KafkaTopicConfig.java`

#### Troubleshooting
- If the application is losing connection to the database after 5 minutes, check the `vpnKitMaxPortIdleTime` value in the settings.json file (located: `%appdata%\Docker\settings.json` on Windows) and set to 0.