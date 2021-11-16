#!/bin/bash
docker service scale bl-swarm_bl-hazelcast=0
docker service scale bl-swarm_bl-hazelcast=1

docker service scale bl-swarm_bl-server=0
docker service scale bl-swarm_bl-server=1

docker service scale bl-swarm_bl-web-console=0
docker service scale bl-swarm_bl-web-console=1

docker service scale bl-swarm_bl-rt-server=0
docker service scale bl-swarm_bl-rt-server=1

docker service scale bl-swarm_bl-coderunner-java=0
docker service scale bl-swarm_bl-coderunner-java=1

docker service scale bl-swarm_bl-coderunner-js=0
docker service scale bl-swarm_bl-coderunner-js=1

docker service scale bl-swarm_bl-taskman=0
docker service scale bl-swarm_bl-taskman=1