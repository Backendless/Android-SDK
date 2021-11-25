#!/bin/bash
echo "Usage: \"`basename "$0"` <service-name>\" - To get service name call backendless_status.sh and take name without bl-swarm_ prefix  "

VERSION=${1:?"You should provide name of the service for example: bl-server, or bl-consul"}

docker service logs bl-swarm_$1 -f