# Read Me First

# Description
An application to track and maintain tasks.

# Build
Be sure to use `mvn clean install` when modifying any database properties. This will regenerate the models in `com.vancefm.ticketstack.models`

# Docker:
This app uses docker compose.
### To build docker container ###
From the project directory, run:
1. `mvn package`
2. `docker-compose build`
3. `docker-compose up`

### Quick Commands ###
2. Clear docker caches: `docker system prune -a`

# Documentation
API documentation by Swagger and can be found at http://localhost:8080/swagger-ui/index.html

# Noted Features
* Exceptions include description/reason message.