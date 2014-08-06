#!/bin/bash
mvn -Dmaven.test.skip=true clean -e package
mv target/location-service-0.0.1-SNAPSHOT-make-assembly.tar.gz    ./location-service.tar.gz

echo '=======================backup============================'
sudo mv  /home/server/location-service/    /home/server/location-service_`date  +%y%m%d%H%M`/

if [ ! -d "/home/server/location-service/" ];then
        sudo mkdir /home/server/location-service
fi;

echo '==================move============'
mv ./location.tar.gz /home/server/location-service/
cd /home/server/location-service/
echo '=============解压===================='
tar -zxvf location-service.tar.gz
echo '=============启动============================='
chmod 755 start.sh
./start.sh