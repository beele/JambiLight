@echo off
echo ======================================
echo ======================================
echo Building with maven:
echo ======================================
echo ======================================
set DIR=%cd%
set DIR=^"%DIR%^"
call mvn install:install-file -Dfile=%DIR%/lib/jssc.jar -DgroupId=com.google.code -DartifactId=jssc -Dversion=2.6 -Dpackaging=jar
call mvn install:install-file -Dfile=%DIR%/lib/RXTXcomm.jar -DgroupId=com.google.code -DartifactId=RXTX -Dversion=2.2 -Dpackaging=jar
call mvn install:install-file -Dfile=%DIR%/lib/ScreenCapper.dll -DgroupId=be.beeles_place.code -DartifactId=JNIscreenCapMock -Dversion=1.0 -Dpackaging=dll
call mvn clean install
ren %DIR%\target\dependency\JNIscreenCapMock-1.0.dll ScreenCapper.dll
move %DIR%\target\dependency\ScreenCapper.dll %DIR%\target\