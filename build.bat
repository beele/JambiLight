@echo off
echo ======================================
echo ======================================
echo Building with maven:
echo ======================================
echo ======================================
set DIR=%cd%
set DIR=^"%DIR%^"
call mvn install:install-file -Dfile=%DIR%/lib/RXTXcomm.jar -DgroupId=com.google.code -DartifactId=RXTX -Dversion=2.2 -Dpackaging=jar
call mvn install:install-file -Dfile=%DIR%/lib/ScreenCapper.dll -DgroupId=be.beeles_place.code -DartifactId=JNIscreenCapMock -Dversion=1.0 -Dpackaging=dll
call mvn clean install
ren %DIR%\target\dependency\JNIscreenCapMock-1.0.dll ScreenCapper.dll
move %DIR%\target\dependency\ScreenCapper.dll %DIR%\target\
echo java -Xmx256m -jar JambiLight.jar > %DIR%\target\run.bat
echo ======================================
echo build complete, use the run.bat file in the target folder to start the application!
set /p DUMMY=Press any key to continue...