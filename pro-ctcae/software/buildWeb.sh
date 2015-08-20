#!/usr/bin/env bash
#adjust for actual catalina home dir
export CATALINA_HOME=/Library/Tomcat
export JPDA_ADDRESS=9000
echo [refreshDEV]: $CATALINA_HOME
echo "shutting down tomcat"
$CATALINA_HOME/bin/shutdown.sh
ant clean publish-all -Dskip.test=true -Doffline=true
ant -f web/ivy-build.xml deploy-proctcae -Doffline=true
echo "starting tomcat"
$CATALINA_HOME/bin/startup.sh


