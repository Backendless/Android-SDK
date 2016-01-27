#!/bin/bash

ROOT=$(pwd)
BUILD_DIR=${ROOT}/out
SRC_DIR=${BUILD_DIR}/maven
VERSION="3.0.3"

echo "Creating Maven bundle for Backendless Android SDK ver.$VERSION"
echo "More info at http://tc.themidnightcoders.com:8081/display/DEV/Deploying+AndroidSDK+to+Maven+Central"
echo ""

#clear
rm -r ${SRC_DIR};
mkdir ${SRC_DIR};
cd ${SRC_DIR}

#rename files
cp ${BUILD_DIR}/backendless.jar ${SRC_DIR}/android-${VERSION}.jar
cp ${BUILD_DIR}/backendless-javadoc.jar ${SRC_DIR}/android-${VERSION}-javadoc.jar
cp ${BUILD_DIR}/backendless-sources.jar ${SRC_DIR}/android-${VERSION}-sources.jar

#get old pom file and replace versions inside
wget -O ${SRC_DIR}/android-${VERSION}.pom http://central.maven.org/maven2/com/backendless/android/3.0.0/android-3.0.0.pom
sed -i 's/3.0.0/'${VERSION}'/g' ${SRC_DIR}/android-${VERSION}.pom

#sign files
gpg -ab ${SRC_DIR}/android-${VERSION}.pom
gpg -ab ${SRC_DIR}/android-${VERSION}.jar
gpg -ab ${SRC_DIR}/android-${VERSION}-javadoc.jar
gpg -ab ${SRC_DIR}/android-${VERSION}-sources.jar

#create Maven bundle
jar -cvf bundle.jar android-${VERSION}.pom android-${VERSION}.jar android-${VERSION}-javadoc.jar android-${VERSION}-sources.jar android-${VERSION}.pom.asc android-${VERSION}.jar.asc android-${VERSION}-javadoc.jar.asc android-${VERSION}-sources.jar.asc

echo ""
echo "Bundle created."
echo "Next step: Deploy $SRC_DIR/bundle.jar to Maven Central"
cd ${ROOT}