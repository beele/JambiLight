echo  "======================================"
echo  "======================================"
echo  "Building with maven:"
echo  "======================================"
echo  "======================================"
mvn install:install-file -Dfile=./lib/RXTXcomm.jar -DgroupId=com.google.code -DartifactId=RXTX -Dversion=2.2 -Dpackaging=jar
mvn install:install-file -Dfile=./lib/ScreenCapper.dll -DgroupId=be.beeles_place.code -DartifactId=JNIscreenCapMock -Dversion=1.0 -Dpackaging=dll
mvn clean install
mv ./target/dependency/JNIscreenCapMock-1.0.dll ./target/ScreenCapper.dll
echo "java -Xmx256m -jar JambiLight.jar" > ./target/run.sh
read -p "Press any key to continue..."