JambiLight
==========

Java implementation of an abmilight system (as seen in the philips tv sets)

-- MORE INFO TO COME --


Uses the following libraries:
---------------------------------
- Guava (can be found here => http://code.google.com/p/guava-libraries/)
- RXTX (more information here => http://rxtx.qbang.org/wiki/index.php/Main_Page)

I have included a lib folder in the git project. Link all the jars in this folder when setting up the project in your IDE.
I have also included the dll files for Windows and the jnilib for OSX (64bit compatible), This will allow you to run the project without much of a hiccup.


Important note:
---------------------------------
- Make sure you use the 1.7 version of the JDK!

- Place the required dll or jnilib files in the root folder of the project to run it.
the dll's and jnilib files can be found in the /lib/rxtx/ folder.

- OSX users must first run the following commands in the terminal application for the rxtx lib to work!
$ sudo mkdir /var/lock
$ sudo chmod 777 /var/lock
