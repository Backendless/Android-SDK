#!/bin/bash

ROOT=$(pwd)
BUILD_DIR=${ROOT}/build
SRC_DIR=${BUILD_DIR}/maven
VERSION="3.0.8"

echo "Creating Maven bundle for Backendless Android SDK ver.$VERSION"
echo "More info at http://tc.themidnightcoders.com:8081/display/DEV/Deploying+AndroidSDK+to+Maven+Central"
echo ""

#clear
rm -r ${SRC_DIR};
mkdir ${SRC_DIR};
cd ${SRC_DIR}

#rename files
cp ${BUILD_DIR}/libs/* ${SRC_DIR}/
cp ${BUILD_DIR}/poms/pom-default.xml ${SRC_DIR}/backendless-${VERSION}.pom
cp ${BUILD_DIR}/poms/pom-default.xml.asc ${SRC_DIR}/backendless-${VERSION}.pom.asc


#create Maven bundle
jar -cvf bundle.jar *

echo ""
echo "Bundle created."
echo "Next step: Deploy $SRC_DIR/bundle.jar to Maven Central"
cd ${ROOT}