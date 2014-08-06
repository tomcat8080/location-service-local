#!/bin/bash
if [ ! -d "./log" ];then
 sudo mkdir log;
fi
echo ==================kill Bootstrap================
jps | grep -w Bootstrap | awk '{system("kill "$1)}'
echo "java pid :" `jps| grep -w Bootstrap`
echo ==================start Bootstrap================

#-Xdebug -Xrunjdwp:transport=dt_socket,server=n,address=10000

nohup java -cp .:lib/* -Xmx1024m -Xms1024m -verbosegclog:log/jvm.log -XX:MaxTenuringThreshold=5 -XX:-UseAdaptiveSizePolicy -XX:MaxPermSize=128M -XX:SurvivorRatio=3  -XX:NewRatio=1 -XX:+PrintGCDateStamps -XX:-UseConcMarkSweepGC -XX:-UseParallelGC  -XX:+PrintGCDetails -XX:+UseParallelGC com.zank.search.Server >log/monitor.log 2>&1 &

tail -f log/monitor.log
