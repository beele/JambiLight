JambiLight
===========================================================

Java implementation of an Ambilight system (as seen in the Philips TV sets)
JambiLight can be used to supercharge your media viewing experience.

JambiLight supports a fully dynamical region system with optional margins and various enhancements for color detection.
Visit the website here => http://www.jambilight.tv
The Beta-1 binaries available here => http://www.beeles-place.be/wordpress/?p=726


How to build & run
-----------------------------------------------------------
- Have maven 3.x and the JDK/JRE 1.8 installed. (Make sure maven is using Java 8 ==> mvn --version)
- Clone the repository to a folder on your computer.
- Build the project using maven:
  * Windows users can build by double clicking the build.bat file.
  * OSX and Linux can build by executing the build.sh file.
- When the build has finished go to the /JambiLight_JAVA/target/ and/or the /JambiLight_XBMC/SocketDummy/target/ folder.
- Run the application by executing the run.bat or run.sh file. (This will set some Java environment variables, to keep the memory usage in check!)
  * You can also double click the jar file or manually start the program through the command prompt. This however could lead to high memory usage!
- KODI support is operational, read the KODI section below for full details!

  
Important notes:
-----------------------------------------------------------
RUNNING:
- Java Robot screen capture now works on both Windows and OSX (Linux is untested but should work).
- JNI implementation only supports Windows for now. The JNI code captures the screen but is as slow as the normal Java screen shot capture.
- When running in debug mode from an IDE the performance can be lower!
- To test the KODI logic without setting up KODI you can use the jar file in the /JambiLight_XBMC/SocketDummy/target/ folder.

JAVA:
- Make sure you use the 1.8 version of the JDK if developing or JRE 1.8 when running and not developing!
- To run in your IDE (without building with maven) make sure you copy the ScreenCapper.dll from the /lib folder to the main project folder (where the build scripts are located). 

KODI:
- Included in this project is a custom KODI plug-in that will allow the application to get the media directly from KODI instead of taking screen shots of the screen.
- Open the "JambiLight_XBMC" folder in the project root.
- You should now see a folder named: "plugin.service.jambilight".
- Compress this folder to a zip file and copy the zip file to an easy to remember location.
- In KODI install the add-on by zip file.
- Select the KODI screen capture method in the advanced tab.
- XBMC is also supported. (KODI is the newer version of XBMC)

DirectShow:
- JambiLight uses JavaCV (Java implementation of OpenCV) to allow for DirectShow device input.
- On Windows machines any DirectShow device can be used as input for JambiLight.
- This mode can still be buggy at times, so use with caution.
- This mode does not work on any other OSes (for the time being).

RXTX:
- RXTX is now deprecated, and will no longer be maintained!
- Please use the JSSC serial communication implementation! Using the RXTX way can be tricky to get working correctly!
- Place the required dll or jnilib files in the root folder of the project to run it.
  * When you have made a build with RXTX enabled use the following command: java -jar JambiLight.jar -Djava.library.path="PATH_TO_FOLDER_WITH_RXTX_SERIAL_DLL"
- The dll's and jnilib files can be found in the /lib/rxtx/ folder.
- OSX users must first run the following commands in the terminal application for the rxtx lib to work!
  * $ sudo mkdir /var/lock
  * $ sudo chmod 777 /var/lock

  
Uses the following libraries and resources:
-----------------------------------------------------------
Libraries:
- Guava (more information here => http://code.google.com/p/guava-libraries/)
- ControlsFX (more information here => http://fxexperience.com/controlsfx/)
- JavaCV (more information here => https://github.com/bytedeco/javacv)
- JSSC (more information here => https://code.google.com/p/java-simple-serial-connector/wiki/jSSC_examples)
- RXTX (more information here => http://rxtx.qbang.org/wiki/index.php/Main_Page)

	The lib folder included in the Java source code only contains the dependencies not found on a maven repository. The build scripts will place those libraries as dependencies in your local m2 storage!

	JSSC:  
	No special steps have to be taken to get the JSSC serial communication working.

	RXTX:  
	I have also included the .dll files for Windows and the jnilib for OSX (64bit compatible), This will allow you to run the project without much of a hiccup.  
	I do suggest you use the JSSC Serial communication implementation, as RXTX requires you to put .dll files in certain locations manually.

Resources:
- Images: Flickr (solarbotics), www.linhpham.me, www.danilodemarco.com and Google image search with copyright settings set to reuse.

License
-----------------------------------------------------------
JambiLight is available under the Creative Commons Attribution-NonCommercial 4.0 International Public License
Please consult LICENSE.MD for the full license text.
The read me and license can be changed at any time, without any notice and the license change will work with retrospective effect.

All Libraries can have their own license, all rights belong to their respective owners.
