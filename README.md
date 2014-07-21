JambiLight
==========

Java implementation of an abmilight system (as seen in the philips tv sets)
JambiLight can be used to supercharge your media viewing experience.

JambiLight supports a fully dynamical region system with optional margins and various enhancements for color detection.
A downloadable binary for Windows is available from http://www.beeles-place.be/wordpress/?p=687 Remember that the project it currently in beta and might not function as expected sometimes.

How to build & run
---------------------------------
- Have maven and the JRE 1.8 installed. (Make sure maven is using java 8 ==> mvn --version)
- Clone the repo to a folder on your computer.
- Build the project using maven:
  * Windows users can build by double clicking the build.bat file.
  * OSX and linux can build by executing the build.sh file.
- When the build has finished go to the /target/ folder that has been created by maven.
- Run the application by executing the run.bat or run.sh file. (This will set some java environment variables, to keep the memory usage in check!)
  * You can also double click the jar file or manually start the program through the command prompt. This however could lead to high memory usage!
  * When you have made a build with RXTX enabled use the following command: java -jar JambiLight.jar -Djava.library.path="PATH_TO_FOLDER_WITH_RXTX_SERIAL_DLL"
- XBMC support is forthcoming, read the XBMC section below for full details!

  
Important notes:
---------------------------------
RUNNING:
- Java Robot screen capture now works on both Windows and OSX (Linux is untested but should work).
- JNI Test classes and files included in the project are not used by default when building the checked out code. This is for testing purposes only and will later be used. JNI implementation only supports Windows for now.

JAVA:
- Make sure you use the 1.8 version of the JDK if developing or JRE 1.8 when running and not developing!
- To run in your IDE (without building with maven) make sure you copy the ScreenCapper.dll from the /lib folder to the main project folder (where the build scripts are located). 

XBMC:
- Included in this project is a custom XBMC plugin that will allow the application to get the media directly from XBMC instead of taking screenshots of the screen.
- Open the "JambiLight_XBMC" folder in the project root.
- You should now see a folder named: "plugin.service.jambilight".
- Compress this folder to a zip file and copy the zip file to an easy to remember location.
- In XBMC install the addon by zip file.
- Select the XBMC screencapture method in the advanced tab.

RXTX:
- RXTX is now deprecated, and will no longer be maintained!
- Please use the JSSC serial communication implementation! Using the RXTX way can be tricky to get working correctly!
- Place the required dll or jnilib files in the root folder of the project to run it.
- The dll's and jnilib files can be found in the /lib/rxtx/ folder.

- OSX users must first run the following commands in the terminal application for the rxtx lib to work!
  * $ sudo mkdir /var/lock
  * $ sudo chmod 777 /var/lock

  
Uses the following libraries:
---------------------------------
- Guava (more information here => http://code.google.com/p/guava-libraries/)
- JSSC (more information here => https://code.google.com/p/java-simple-serial-connector/wiki/jSSC_examples)
- RXTX (more information here => http://rxtx.qbang.org/wiki/index.php/Main_Page)

I have included a lib folder in the git project. Link all the jars in this folder when setting up the project in your IDE.

JSSC:  
No special steps have to be taken to get the JSSC serial communication working.

RXTX:  
I have also included the .dll files for Windows and the jnilib for OSX (64bit compatible), This will allow you to run the project without much of a hiccup.  
I do suggest you use the JSSC Serial communication implementation, as RXTX requires you to put .dll files in certain locations manually.
