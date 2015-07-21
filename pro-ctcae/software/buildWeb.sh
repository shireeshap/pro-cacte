#!/usr/bin/env bash
#adjust for actual catalina home dir
export CATALINA_HOME=/Library/Tomcat
export JPDA_ADDRESS=9000
echo [refreshDEV]: $CATALINA_HOME
ant publish-all -Dskip.test=true -Doffline=true
cd web
ant -f ivy-build.xml deploy-proctcae -Doffline=true
echo [refreshDEV]:  'sleeping 5 seconds'
sleep 5
cd ..

