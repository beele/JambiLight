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

RXTX:  
I have also included the .dll files for Windows and the jnilib for OSX (64bit compatible), This will allow you to run the project without much of a hiccup.  
I do suggest you use the JSSC Serial communication implementation, as RXTX required you to put .dll files in certain locations manually.

How to build & run
---------------------------------
- Clone the repo to a folder on your computer.
- Windows users can simply double click on the build.bat file.
  * OSX and linux users will have to execute the three maven commands in the bat file separately.
- When the build has finished go to the /target/ folder that has been created by maven.
- Open a command prompt and run the following command: java -jar JambiLight.jar
  * When you have made a build with RXTX enabled use the following command: java -jar JambiLight.jar -Djava.library.path="PATH_TO_FOLDER_WITH_RXTX_SERIAL_DLL"

Important notes:
---------------------------------
JAVA:
- Make sure you use the 1.7 version of the JDK!

RXTX:
- Place the required dll or jnilib files in the root folder of the project to run it.
The dll's and jnilib files can be found in the /lib/rxtx/ folder.

- OSX users must first run the following commands in the terminal application for the rxtx lib to work!
  * $ sudo mkdir /var/lock
  * $ sudo chmod 777 /var/lock