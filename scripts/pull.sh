#!/usr/bin/env bash
#!/bin/bash

echo "Usage: \"`basename "$0"` <container_version>\""

cd `dirname "$0"`;

VERSION=${1:?"You should provide artifact version."}

REGISTRY=${2:?"You should provide registry."}

# bl-server
docker pull $REGISTRY/bl-server:$VERSION

# bl-upgrade
docker pull $REGISTRY/bl-upgrade:$VERSION

# bl-billing
#docker pull $REGISTRY/bl-billing:$VERSION

# bl-coderunner-java
docker pull $REGISTRY/bl-coderunner-java:$VERSION

# bl-coderunner-js
docker pull $REGISTRY/bl-coderunner-js:$VERSION

# bl-limit
#docker pull $REGISTRY/bl-limit:$VERSION

# bl-web-console
docker pull $REGISTRY/bl-web-console:$VERSION

# bl-config-loader
docker pull $REGISTRY/bl-config-loader:$VERSION

# bl-hazelcast
docker pull $REGISTRY/bl-hazelcast:$VERSION

# bl-rt-server
docker pull $REGISTRY/bl-rt-server:$VERSION

