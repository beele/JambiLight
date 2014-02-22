echo  "======================================"
echo  "======================================"
echo  "Building with maven:"
echo  "======================================"
echo  "======================================"
mvn install:install-file -Dfile=./lib/jssc.jar -DgroupId=com.google.code -DartifactId=jssc -Dversion=2.6 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/RXTXcomm.jar -DgroupId=com.google.code -DartifactId=RXTX -Dversion=2.2 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/ScreenCapper.dll -DgroupId=be.beeles_place.code -DartifactId=JNIscreenCapMock -Dversion=1.0 -Dpackaging=dll
mvn clean install
mv ./target/dependency/JNIscreenCapMock-1.0.dll ./target/ScreenCapper.dll