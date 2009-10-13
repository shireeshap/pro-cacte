set CATALINA_HOME=C:\Users\Harsh\proctcae\Tomcat_5_5_26
call ant publish-all  -Dskip.test=true -Doffline=true

cd web
call ant -f ivy-build.xml deploy-proctcae  -Doffline=true
cd ..
cd C:\Users\Harsh\proctcae\Tomcat_5_5_26\bin
startup.bat

