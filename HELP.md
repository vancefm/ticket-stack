# Read Me First

# Description
An application to track and maintain tasks.

# Build
Be sure to use `mvn clean install` when modifying any database properties. This will regenerate the models in `com.vancefm.ticketstack.models`

# Docker:
This app uses docker compose.
### To build docker container ###
From the project directory, run the CleanDeploy.sh script. This will re-package the jar, clean the old docker image/container, and redeploy a new one.

### Quick Commands ###
1. Clear docker caches: `docker system prune -a`

# Documentation
API documentation by Swagger and can be found at http://localhost:8080/swagger-ui/index.html