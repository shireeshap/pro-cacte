call ant recreate-test-db
cd core
call ant -f ivy-build.xml test -Dtest=ImporterIntegrationTest -Doffline=true -Ddb=test-db
pause
