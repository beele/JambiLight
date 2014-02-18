echo ======================================
echo ======================================
echo Building with maven:
echo ======================================
echo ======================================
call mvn install:install-file -Dfile=%cd%/lib/jssc.jar -DgroupId=com.google.code -DartifactId=jssc -Dversion=2.6 -Dpackaging=jar
call mvn install:install-file -Dfile=%cd%/lib/RXTXcomm.jar -DgroupId=com.google.code -DartifactId=RXTX -Dversion=2.2 -Dpackaging=jar
call mvn clean install