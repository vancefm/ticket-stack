#!/bin/bash
#On a Windows system, make sure the Windows Subsystem Linux feature is installed
#And the run "bash CleanDeploy.sh"

#1. Repackage
#2. Clean
#3. Deploy container

#Repackage
mvn clean package

#Get container ids for "ticket-stack-api"
repository_name="ticket-stack-api"
containerId_arr=( $(docker ps -aq --filter "name=$repository_name") )
imageId_arr=( $(docker images -aq $repository_name) )

#Stop and remove all containers
for i in "${containerId_arr[@]}"
    do
        echo "Stopping container " $i
        docker stop --time 5 $i
        echo "Removing container " $i
        docker rm -f $i
    done

#Remove image
for i in "${imageId_arr[@]}"
    do
        echo "Removing image " $i
        docker rmi -f $i
    done

docker-compose up --build --detach

echo "Done"