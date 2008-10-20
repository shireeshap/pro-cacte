export CATALINA_HOME=/Users/saurabhagrawal/tomcats/ae-tomcat-5.5.26
export JPDA_ADDRESS=9000
echo [refreshDEV]: $CATALINA_HOME
cd core
mvn clean
mvn install -Dmaven.test.skip=true
cd ..
cd web

mvn compile war:inplace catman:go -Dmanager.username=admin -Dmanager.password=admin -Dmanager.url=http://localhost:8040/manager/
echo [refreshDEV]:  'sleeping 5 seconds'
sleep 5
cd ..

