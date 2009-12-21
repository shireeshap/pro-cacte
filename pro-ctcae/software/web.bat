cd web
set CATALINA_HOME=C:\Users\Harsh\proctcae\Tomcat_5_5_26
call ant -f ivy-build.xml deploy-proctcae -Doffline=true

cd C:\Users\Harsh\proctcae\Tomcat_5_5_26\bin

startup.bat
cd ..
