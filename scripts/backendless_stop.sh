#!/bin/bash

echo "Stopping of containers ..."
docker stack rm bl-swarm
sleep 2


service_count_cmd="docker stack services bl-swarm 2>/dev/null | wc -l"
ok_result="Nothing found in stack: bl-swarm"

service_count=`eval ${service_count_cmd}`
trial=0

until [ "${service_count}" -lt 1 -o ${trial} -gt 15 ]
do
  let "trial+=1"
  echo "Waiting for bl-swarm to stop. Left ${service_count} services.";
  sleep 2
  stop_result=`eval ${service_count_cmd}`
done


bkndls_net_cmd="docker network ls 2>/dev/null | grep bl-swarm | awk '{print \$2}'"
trial=0

until [ ${trial} -gt 15 ]
do
  let "trial+=1"
  bkndls_net=`eval "${bkndls_net_cmd}"`

  if [ ${bkndls_net} ]
  then
    echo "Waiting for network ${bkndls_net} to stop.";
    sleep 2
  else
    break
  fi
done

if [ $bkndls_net ]
then
  echo "Trying to delete network ${bkndls_net} forcibly."
  docker network rm ${bkndls_net}  2>/dev/null
  sleep 3
fi


bkndls_net=`eval "${bkndls_net_cmd}"`
if [ $bkndls_net ]
then
  echo "Some problem happend with the docker itslef. Cannot remove stack 'bl-swarm' properly."
else
  echo "Backendless stack 'bl-swarm' has been stopped."
fi

echo

