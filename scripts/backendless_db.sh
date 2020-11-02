#!/bin/bash
cd `dirname "$0"`;

mounts=$(pwd)"/mounts"

env_file=`cat ./ports.env`

echo "starting dbs..."
env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./dbs-compose.yml bl-swarm