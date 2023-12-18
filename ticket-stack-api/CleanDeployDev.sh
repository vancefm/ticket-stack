#!/bin/bash
#On a Windows system, make sure the Windows Subsystem Linux feature is installed
#And the run "bash CleanDeployDev.sh"
#Use this script on your local dev environment to ensure a clean deployment to docker.

#1. Stop containers
#2. Remove containers
#3. Deploy Kafka container
#4. Repackage API
#5. Deploy API container

echo "This script removes all ticket-stack-api and kafka containers. And removes the ticket-stack-api and kafka images"
read -p "Press Enter to continue..."

#1.Stop and remove all containers

#Get container ids for "ticket-stack-"
repository_name="ticket-stack-"
containerId_arr=( $(docker ps -aq --filter "name=$repository_name") )

#Stop containers
for i in "${containerId_arr[@]}"
    do
        echo "Stopping container " $i
        docker stop --time 5 $i
        echo "Removing container " $i
        docker rm -f $i
    done

#2.Remove images
echo "Removing image ticket-stack-api:latest"
docker rmi -f "ticket-stack-api:latest"
echo "Removing image bitnami/kafka:latest"
docker rmi -f "bitnami/kafka:latest"

#Note: data volumes need to be removed manually

#3.Deploy kafka container (without it, the api build will fail)
docker-compose up kafka-broker0 -d

sleep 3

isKafkaRunning=( $(docker ps -aq --filter "name=kafka-broker0" --filter "status=running"))

echo $isKafkaRunning
while [ -z $isKafkaRunning ]; do
  read -p "Kafka is not running, press Enter to try starting it again..."
  docker-compose up kafka-broker0 -d
  isKafkaRunning=( $(docker ps -aq --filter "name=kafka-broker0" --filter "status=running"))
done

echo "Kafka is running."

#4. Repackage
echo "Repackaging ticket-stack-api..."
mvn clean package
if [[ "$?" -ne 0 ]]; then
    echo 'mvn clean package failed'
    echo 'Resolve the issue with <mvn clean package>, then run <docker-compose up api -d> to start the api container. Or re-run this script.'
    read -p "Press Enter to exit ..."
    exit
fi

#5. Deploy API container
docker-compose up api -d

exit