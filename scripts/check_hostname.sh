#!/bin/bash
  
SERVER_HOST_NAME=backendless.local

check_host_cmd="ping -c1 backendless.local 2>&1 | grep -E '(Name or service not known|Temporary failure in name resolution)' > /dev/null"
host_exists=`eval $check_host_cmd; echo $?`

if [ "${host_exists}" == 0 ]
then
  echo "Host ${SERVER_HOST_NAME} cannot be found."
  echo "To add the host to '/etc/hosts' superuser rights are needed, so system will ask password for it "
  sudo bash -c "echo -e '\n127.0.0.1\t\t${SERVER_HOST_NAME}\n' >> /etc/hosts"
  sleep 1
  host_exists=`eval $check_host_cmd; echo $?`
  if [ "${host_exists}" == 0 ]
  then
    echo "Host wasn't added properly. Please check '/etc/hosts' file and add the record for ${SERVER_HOST_NAME} manually."
    echo "In case of use other local DNS subsysems (systemd, dnsmasq, etc.) you may create mapping for ${SERVER_HOST_NAME} in their configs."
    exit 1
  else
    echo "Host was added successfully."
  fi
fi

