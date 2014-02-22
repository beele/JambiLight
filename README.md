JambiLight
==========

Java implementation of an abmilight system (as seen in the philips tv sets)

-- MORE INFO TO COME --


Uses the following libraries:
---------------------------------
- Guava (more information here => http://code.google.com/p/guava-libraries/)
- JSSP (more information here => https://code.google.com/p/java-simple-serial-connector/wiki/jSSC_examples)
- RXTX (more information here => http://rxtx.qbang.org/wiki/index.php/Main_Page)

I have included a lib folder in the git project. Link all the jars in this folder when setting up the project in your IDE.

JSSC:  
No special steps have to be taken to get the JSSC serial communication working.

RXTX:  
I have also included the .dll files for Windows and the jnilib for OSX (64bit compatible), This will allow you to run the project without much of a hiccup.  
I do suggest you use the JSSC Serial communication implementation, as RXTX requires you to put .dll files in certain locations manually.

How to build & run
---------------------------------
- Have maven installed.
- Clone the repo to a folder on your computer.
- Build the project using maven:
  * Windows users can build by double clicking the build.bat file.
  * OSX and linux can build by executing the build.sh file.
- When the build has finished go to the /target/ folder that has been created by maven.
- Open a command prompt and run the following command: java -jar JambiLight.jar
  * When you have made a build with RXTX enabled use the following command: java -jar JambiLight.jar -Djava.library.path="PATH_TO_FOLDER_WITH_RXTX_SERIAL_DLL"

Important notes:
---------------------------------
JAVA:
- Make sure you use the 1.7 version of the JDK!
- To run in your IDE (without building with maven) make sure you copy the ScreenCapper.dll from the /lib folder to the main project folder (where the build scripts are located). 

RXTX:
- Please use the JSSC serial communication implementation! Using the RXTX way can be tricky to get working correctly!
- Place the required dll or jnilib files in the root folder of the project to run it.
The dll's and jnilib files can be found in the /lib/rxtx/ folder.

- OSX users must first run the following commands in the terminal application for the rxtx lib to work!
  * $ sudo mkdir /var/lock
  * $ sudo chmod 777 /var/lock
