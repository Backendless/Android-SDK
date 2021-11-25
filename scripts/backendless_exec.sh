#!/bin/bash

docker exec -it $(docker ps -q -f name=bl-swarm_$1) /bin/bash