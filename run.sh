#!/bin/bash

mvn clean compile exec:java -Dexec.mainClass="com.barbalho.rocha.TCPClient" -Dexec.cleanupDaemonThreads=false -Dexec.args="$*"