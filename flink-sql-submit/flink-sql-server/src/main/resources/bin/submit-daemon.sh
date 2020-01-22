#!/bin/sh
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

usage="Usage: submit-daemon.sh (start|stop) <command> "

# if no args specified, show usage
if [ $# -le 0 ]; then
  echo $usage
  exit 1
fi

startStop=$1
command=FlinkSqlSubmitApplicationServer

echo "Begin $startStop $command......"

BIN_DIR=`dirname $0`
BIN_DIR=`cd "$BIN_DIR"; pwd`
SUBMIT_HOME=$BIN_DIR/..

export JAVA_HOME=$JAVA_HOME
export HOSTNAME=`hostname`

export SUBMIT_PID_DIR=$SUBMIT_HOME/pid
export SUBMIT_LOG_DIR=$SUBMIT_HOME/logs
export SUBMIT_CONF_DIR=$SUBMIT_HOME/conf
export SUBMIT_LIB_JARS=$SUBMIT_HOME/lib/*

export SUBMIT_OPTS="-server -Xmx1g -Xms512m -Xss512k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70"

if [ ! -d "$SUBMIT_LOG_DIR" ]; then
  mkdir $SUBMIT_LOG_DIR
fi

log=$SUBMIT_LOG_DIR/sqlSubmitServer-$HOSTNAME.out
pid=$SUBMIT_PID_DIR/sqlSubmitServer.pid

LOG_FILE="-Dspring.profiles.active=sql"
CLASS=sql.submit.server.FlinkSqlSubmitApplicationServer

case $startStop in
  (start)
    [ -w "$SUBMIT_PID_DIR" ] ||  mkdir -p "$SUBMIT_PID_DIR"

    if [ -f $pid ]; then
      if kill -0 `cat $pid` > /dev/null 2>&1; then
        echo $command running as process `cat $pid`.  Stop it first.
        exit 1
      fi
    fi

    echo starting $command, logging to $log

    exec_command="$LOG_FILE $SUBMIT_OPTS -classpath $SUBMIT_CONF_DIR:$SUBMIT_LIB_JARS $CLASS"

    echo "nohup $JAVA_HOME/bin/java $exec_command > $log 2>&1 < /dev/null &"
#   nohup $JAVA_HOME/bin/java $exec_command > $log 2>&1 < /dev/null &
    nohup $JAVA_HOME/bin/java $exec_command > /dev/null 2>&1 &
    echo $! > $pid
    ;;

  (stop)

      if [ -f $pid ]; then
        TARGET_PID=`cat $pid`
        if kill -0 $TARGET_PID > /dev/null 2>&1; then
          echo stopping $command
          kill $TARGET_PID
          sleep $STOP_TIMEOUT
          if kill -0 $TARGET_PID > /dev/null 2>&1; then
            echo "$command did not stop gracefully after $STOP_TIMEOUT seconds: killing with kill -9"
            kill -9 $TARGET_PID
          fi
        else
          echo no $command to stop
        fi
        rm -f $pid
      else
        echo no $command to stop
      fi
      ;;

  (*)
    echo $usage
    exit 1
    ;;

esac

echo "End $startStop $command."