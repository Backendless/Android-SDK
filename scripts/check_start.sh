#!/bin/bash
cd `dirname "$0"`;

docker service logs bl-swarm_bl-server -f &
pid=$!

trap on_exit INT

function on_exit() {
        exitCode=$?
        kill $pid
        exit $exitCode;
}

for i in {1..300}
do
   status_code=$(curl -o /dev/null -s -w "%{http_code}\n" http://localhost:${BL_PROPERTY_config_server_publicPort})
   if [[ "$status_code" == "200" ]]; then
      echo "*************************************************************"
      echo "******************** Backendless started ********************"
      echo "*************************************************************"
      echo "console is available on 'http://localhost:${BL_PROPERTY_config_console_port}'"
      on_exit
   fi
   sleep 2
done

on_exit
