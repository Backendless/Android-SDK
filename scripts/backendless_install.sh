#!/bin/bash

echo "Usage: \"`basename "$0"` <version>"

cd `dirname "$0"`;

docker swarm init &> /dev/null

version=${1:-"latest"}
registry=${2:-"backendless"}

if [[ "$registry" == "private"  ]]; then
  ./pull.sh ${version} registry.backendless.com:5000
fi

if [[ "$registry" == "backendless"  ]]; then
  ./pull.sh ${version} backendless
fi

./check_ports.sh