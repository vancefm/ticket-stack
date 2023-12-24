docker build -t ticket-stack-ui .
docker run -d -it --rm -v "%cd%":/app -v /app/node_modules -p 3000:3000 ticket-stack-ui