echo ======================================
echo ======================================
echo Building with maven:
echo ======================================
echo ======================================
set DIR=%cd%
set DIR=^"%DIR%^"
call mvn install:install-file -Dfile=%DIR%/lib/jssc.jar -DgroupId=com.google.code -DartifactId=jssc -Dversion=2.6 -Dpackaging=jar
call mvn install:install-file -Dfile=%DIR%/lib/RXTXcomm.jar -DgroupId=com.google.code -DartifactId=RXTX -Dversion=2.2 -Dpackaging=jar
call mvn clean install