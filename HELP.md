# Read Me First

# Description
An application to track and maintain tasks.

# Build
Be sure to use `mvn clean install` when modifying any database properties. This will regenerate the models in `com.vancefm.ticketstack.models`

# Docker:
This app uses docker compose.

### Quick Commands ### 
1. Build image, container and run: `docker compose up`
3. Rebuild image, container and run: `docker-compose up --force-recreate --build -d` and then `docker image prune -f` to cleanup

# Documentation
API documentation is provided by SpringDoc and can be found at http://localhost:8080/swagger-ui/index.html