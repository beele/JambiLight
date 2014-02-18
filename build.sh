echo  "======================================"
echo  "======================================"
echo  "Building with maven:"
echo  "======================================"
echo  "======================================"
mvn install:install-file -Dfile=./lib/jssc.jar -DgroupId=com.google.code -DartifactId=jssc -Dversion=2.6 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/RXTXcomm.jar -DgroupId=com.google.code -DartifactId=RXTX -Dversion=2.2 -Dpackaging=jar
mvn clean install