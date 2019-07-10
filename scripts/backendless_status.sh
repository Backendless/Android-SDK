#!/bin/bash

echo "Usage: \"`basename "$0"` [<full>]\""

type=${1:-"short"}

if [[ "$type" = "full" ]]; then
  docker stack ps --no-trunc bl-swarm
  exit 0
fi

docker stack services bl-swarm

