#!/bin/bash

cd `dirname "$0"`;

silent=false
if [[ "$1" == "--silent" ]]; then
  silent=true
fi

docker_env=

check_port()
{
  message=$1
  env_key=$2
  default_port=$3
  silent_arg=$4
  port=

  if [ $silent_arg == false ]; then
    read -p "$message [$default_port]: " port;
  fi

  port=${port:-"$default_port"}

  nc -z localhost $port
  free=$?
  if [[ "$free" == 1 ]]; then
    docker_env="${docker_env}${env_key}=${port}\n";
  fi;
  if [[ "$free" == 0 ]]; then
    echo "Port $port is already busy! Choose another one.";
    check_port "$message" "$env_key" "$default_port" "false";
  fi;
}

echo "Choose ports for services:"
check_port "MySQL server:" "BL_MYSQL_PORT" "3306" "$silent"
check_port "MongoDB server:" "BL_MONGODB_PORT" "27017" "$silent"
check_port "Redis for debug server:" "BL_PROPERTY_config_redis_bl_debug_port" "6380" "$silent"
check_port "Backendless server:" "BL_PROPERTY_config_server_publicPort" "9000" "$silent"
check_port "Backendless web-console:" "BL_PROPERTY_config_console_port" "80" "$silent"
check_port "Backendless rt server:" "BL_PROPERTY_config_rt_server_socketServer_connection_port" "5000" "$silent"
echo "Port configuration completed."

echo -e $docker_env > ports.env

