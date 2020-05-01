#!/bin/bash

# exit when any command fails
set -e

echo "Usage: \"`basename "$0"` <container_version>\""

cd `dirname "$0"`;

VERSION=${1:?"You should provide artifact version."}

# bl-server
docker pull backendless/bl-server:$VERSION

# bl-upgrade
docker pull backendless/bl-upgrade:$VERSION

# bl-coderunner-java
docker pull backendless/bl-coderunner-java:$VERSION

# bl-coderunner-js
docker pull backendless/bl-coderunner-js:$VERSION

# bl-web-console
docker pull backendless/bl-web-console:$VERSION

# bl-config-loader
docker pull backendless/bl-config-loader:$VERSION

# bl-hazelcast
docker pull backendless/bl-hazelcast:$VERSION

# bl-rt-server
docker pull backendless/bl-rt-server:$VERSION

