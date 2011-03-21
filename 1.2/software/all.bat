set CATALINA_HOME=C:\Users\Harsh\proctcae\Tomcat_5_5_26
call ant publish-all  -Dskip.test=true 
pause
cd web
call ant -f ivy-build.xml deploy-proctcae 
pause
cd ..
cd C:\Users\Harsh\proctcae\Tomcat_5_5_26\bin
startup.bat

