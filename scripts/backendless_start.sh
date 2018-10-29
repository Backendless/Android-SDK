#!/bin/bash

echo "Usage: \"`basename "$0"` <version> [<type> local|backendless|private - default backendless]\""

cd `dirname "$0"`;

version=${1:-"latest"}
type=${2:-"backendless"}

mounts=$(pwd)"/mounts"


if [[ "$type" != "local" && "$type" != "private" && "$type" != "backendless" ]]; then
  echo "Wrong repository type. Should be 'local', 'private' or 'backendless'."
  exit 1
fi

registry=""
if [[ "$type" == "private"  ]]; then
  registry="registry.backendless.com:5000/"
fi

if [[ "$type" == "backendless"  ]]; then
  registry="backendless/"
fi

env_file=`cat ./ports.env`

echo "starting dbs..."
env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./dbs-compose.yml bl-swarm
echo "starting consul..."
env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./consul-compose.yml bl-swarm
echo "starting configuration..."
env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./pre-configuration-compose.yml bl-swarm
echo "starting services..."
env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./backendless-compose.yml bl-swarm

env $env_file ./check_start.sh

