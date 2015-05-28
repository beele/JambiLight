#!/usr/bin/env bash
echo  "======================================"
echo  "======================================"
echo  "Building with maven:"
echo  "======================================"
echo  "======================================"
mvn install:install-file -Dfile=./JambiLight_JAVA/lib/RXTXcomm.jar -DgroupId=com.google.code -DartifactId=RXTX -Dversion=2.2 -Dpackaging=jar
mvn install:install-file -Dfile=./JambiLight_JAVA/lib/ScreenCapper.dll -DgroupId=be.beeles_place.code -DartifactId=JNIscreenCapMock -Dversion=1.0 -Dpackaging=dll
mvn clean install -Dplatform.dependencies=true
mv ./JambiLight_JAVA/target/dependency/JNIscreenCapMock-1.0.dll ./JambiLight_JAVA/target/ScreenCapper.dll
echo "java -Xmx256m -jar JambiLight.jar" > ./JambiLight_JAVA/target/run.sh
echo "java -Xmx128m -jar SocketDummy.jar" > ./JambiLight_XBMC/SocketDummy/target/run.sh
echo "Build completed!"