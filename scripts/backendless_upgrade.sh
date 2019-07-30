#!/bin/bash

docker swarm init &> /dev/null

echo Usage:
echo "`basename "$0"`  <version>  <type>  <mode>"
echo "<version> -- version from the docker registry"
echo 

cd `dirname "$0"`;

mounts=$(pwd)"/mounts"

version=${1:-"latest"}
type=${2:-"backendless"} # local|private|backendless
mode=${3:-"pro"} # pro|cloud|managed

registry=""
if [[ "$type" == "private"  ]]; then
  registry="registry.backendless.com:5000"
fi

if [[ "$type" == "backendless"  ]]; then
  registry="backendless"
fi

./pull.sh ${version} ${registry}

env_file=`cat ./ports.env`
mounts=$(pwd)"/mounts"

env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./backendless-compose.yml bl-swarm

if [[ "$mode" == "pro" ]]; then
	env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./coderunner-js-compose.yml bl-swarm
else
	env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./coderunner-js-cloud-compose.yml bl-swarm
fi

env $env_file ./check_start.sh
