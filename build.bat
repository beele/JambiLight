@echo off
echo ======================================
echo ======================================
echo Building with maven:
echo ======================================
echo ======================================
set DIR=%cd%
set DIR=^"%DIR%^"
call mvn install:install-file -Dfile=%DIR%/JambiLight_JAVA/lib/RXTXcomm.jar -DgroupId=com.google.code -DartifactId=RXTX -Dversion=2.2 -Dpackaging=jar
call mvn install:install-file -Dfile=%DIR%/JambiLight_JAVA/lib/ScreenCapper.dll -DgroupId=be.beeles_place.code -DartifactId=JNIscreenCapMock -Dversion=1.0 -Dpackaging=dll
call mvn clean install
ren %DIR%\JambiLight_JAVA\target\dependency\JNIscreenCapMock-1.0.dll ScreenCapper.dll
move %DIR%\JambiLight_JAVA\target\dependency\ScreenCapper.dll %DIR%\JambiLight_JAVA\target\
echo java -Xmx256m -jar JambiLight.jar > %DIR%\JambiLight_JAVA\target\run.bat
echo java -Xmx128m -jar SocketDummy.jar > %DIR%\JambiLight_XBMC\SocketDummy\target\run.bat
echo ======================================
echo build complete, use the run.bat file in the target folder to start the application!
set /p DUMMY=Press return to continue...