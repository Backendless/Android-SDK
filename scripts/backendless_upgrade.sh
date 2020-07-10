#!/bin/bash

docker swarm init &> /dev/null

echo Usage:
echo "`basename "$0"`  <version>  <mode>"
echo "<version> -- version from the docker registry"
echo "<mode> -- [cloud|managed|pro] -- default=pro"
echo 

cd `dirname "$0"`;

mounts=$(pwd)"/mounts"

version=${1:-"latest"}
mode=${2:-"pro"} # pro|cloud|managed

echo $version > .bl-version

registry="backendless"

./pull.sh ${version}

env_file=`cat ./ports.env`
mounts=$(pwd)"/mounts"

env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./backendless-compose.yml bl-swarm

if [[ "$mode" == "pro" ]]; then
	env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./coderunner-js-compose.yml bl-swarm
else
	env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./coderunner-js-cloud-compose.yml bl-swarm
fi

env $env_file ./check_start.sh
