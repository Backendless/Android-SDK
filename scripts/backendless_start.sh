#!/bin/bash

echo Usage:
echo "`basename "$0"`  <version>  <mode>"
echo "<version> -- version from the docker registry"
echo "<mode> -- [cloud|managed|pro] -- default=pro"
echo 


cd `dirname "$0"`;

mounts=$(pwd)"/mounts"

version_from_file=`cat ./.bl-version`

version=${1:-$version_from_file}
mode=${2:-"pro"} # pro|cloud|managed

echo "starting backendless $version ..."

registry="backendless"

env_file=`cat ./ports.env`

set -e

echo "starting dbs..."
env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./dbs-compose.yml bl-swarm
echo "starting consul..."
env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./consul-compose.yml bl-swarm
echo "starting configuration..."
env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./pre-configuration-compose.yml bl-swarm
echo "starting services..."
env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./backendless-compose.yml bl-swarm

if [[ "$mode" == "pro" ]]; then
	env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./coderunner-js-compose.yml bl-swarm
else
	env $env_file REGISTRY="${registry}" VERSION="${version}" MOUNTS="${mounts}" docker stack deploy -c ./coderunner-js-cloud-compose.yml bl-swarm
fi

env $env_file ./check_start.sh

