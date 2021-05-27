#!/usr/bin/env bash

cd `dirname "$0"`;

cd server

if [[ -z $JAVA_HOME ]]; then
    JAVA_HOME=`java -XshowSettings:properties -version 2>&1 | grep -E "java\.home" | awk '{print $3}'`;
fi

echo "Path to \"JAVA_HOME\": $JAVA_HOME"

JAVA_EXEC="$JAVA_HOME/bin/java"

JAVA_ARGS="$JAVA_ARGS
-XX:-OmitStackTraceInFastThrow
-server
-Xmn256m
-Xms256m
-Xmx512m
-Duser.timezone=UTC
-Dfile.encoding=UTF-8
-Djava.net.preferIPv4Stack=true"

if [ -d "target" ]; then
    cd target
fi


TIMERRUNNER_RUN_CMD="env JAVA_HOME=$JAVA_HOME $consulEnv START_MODE=timer_runner $JAVA_EXEC $JAVA_ARGS -cp \"*\" com.backendless.external.TimerRunner"

separator="----------------------------------------"
echo -e "${separator}\nStarting TimerRunner ..."
echo $TIMERRUNNER_RUN_CMD
echo -e "${separator}\n"
eval $TIMERRUNNER_RUN_CMD