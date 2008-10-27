export CATALINA_HOME=/Users/saurabhagrawal/tomcats/ae-tomcat-5.5.26
export JPDA_ADDRESS=9000
echo [refreshDEV]: $CATALINA_HOME
ant publish-all -Dskip.test=true -Doffline=true
cd web
ant -f ivy-build.xml deploy-ctcae -Doffline=true
echo [refreshDEV]:  'sleeping 5 seconds'
sleep 5
cd ..

