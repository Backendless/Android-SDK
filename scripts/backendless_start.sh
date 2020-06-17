#!/bin/bash

echo Usage:
echo "`basename "$0"`  <version>  <type>  <mode>"
echo "<version> -- version from the docker registry"
echo "<type> -- [local|private|backendless] -- default=backendless"
echo "<mode> -- [cloud|managed|pro] -- default=pro"
echo 


cd `dirname "$0"`;

mounts=$(pwd)"/mounts"

version=${1:-"latest"}
type=${2:-"backendless"} # local|private|backendless
mode=${3:-"pro"} # pro|cloud|managed

if [[ "$type" != "local" && "$type" != "private" && "$type" != "backendless" ]]; then
  echo "Wrong repository type. Should be 'local', 'private' or 'backendless'."
  exit 1
fi

registry=""
if [[ "$type" == "private"  ]]; then
  registry="registry.backendless.com:5000"
fi

if [[ "$type" == "backendless"  ]]; then
  registry="backendless"
fi

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

