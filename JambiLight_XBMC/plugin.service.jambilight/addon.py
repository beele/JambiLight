import sys
import socket

import xbmc
import xbmcgui
import xbmcplugin


def log(msg):
    xbmc.log("### [%s] - %s" % ("JambiLight", msg,), level=xbmc.LOGDEBUG)


def info(title, msg):
    xbmc.executebuiltin('Notification(%s, %s, %d)' % (title, msg, 5000))


#### Show startup notification.
info("Startup", "JambiLight plugin started!")

#### Start real code here!
ALIVE = True
CONNECTED = False

#### Set up the socket connection.
HOST = 'localhost'
PORT = 1337
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

#### Set up the xbmc rendercapture object.
capture = xbmc.RenderCapture()
capture.capture(720, 480, xbmc.CAPTURE_FLAG_CONTINUOUS)

#### Main loop => connects to socket and captures errors!
while ALIVE:
    try:
        sock.connect((HOST, PORT))
        CONNECTED = True
        log("Connected to socket!")
        info("Success!", "Connected to JambiLight host application!")
    except socket.error, e:
        log("Could not connect to JambiLight host application!")
        info("Error!", "Could not connect to JambiLight host application!")
        xbmc.sleep(10000)

    while CONNECTED:
        xbmc.sleep(30)
        try:
            #Check if there something playing.
            if xbmc.Player().isPlaying():
                capture.waitForCaptureStateChangeEvent(1000)
                #Only proceed if the capture has succeeded!
                if capture.getCaptureState() == xbmc.CAPTURE_STATE_DONE:
                    #Get frame data and send it.
                    pixels = capture.getImage()
                    sock.sendall(pixels)
                else:
                    log("No image captured!")
            else:
                log("No video playing!")
                #TODO: Send some static signal!
        except socket.error, e:
            log("Socket error occurred!")
            sock.close()
            CONNECTED = False

        # check to see if xbmc will shut down!
        if xbmc.abortRequested or ALIVE is False:
            sock.close()
            ALIVE = False
            CONNECTED = False
            log("Shutting down! ==> Goodbye!")

    # check to see if xbmc will shut down!
    if xbmc.abortRequested or ALIVE is False:
        ALIVE = False
        sock.close()
        log("Shutting down! ==> Goodbye!")
