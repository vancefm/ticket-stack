# Read Me First

When refactoring pojos, it may be beneficial to generate templates from JOOQ.
Do this by running ***mvn clean install*** from console. This will generate the models located in com.vancefm.ticketstack.models.

# Docker:
This app uses docker compose.

### Quick Commands ### 
1. Build image, container and run: `docker compose up`
3. Rebuild image, container and run: `docker-compose up --force-recreate --build -d` and then `docker image prune -f` to cleanup

# Documentation
API documentation is provided by SpringDoc and can be found at http://localhost:8080/swagger-ui/index.html