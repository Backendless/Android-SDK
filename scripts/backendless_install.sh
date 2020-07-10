#!/bin/bash
  
echo "Usage: \"`basename "$0"` <version>"

cd `dirname "$0"`;

docker swarm init &> /dev/null

version=${1:-"latest"}

echo $version > .bl-version

./pull.sh ${version} backendless

./check_ports.sh
./check_hostname.sh

