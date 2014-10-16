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
sock = None

#### Static color!
staticPixels = ""
i = 0
while i < (720 * 480):
    #### BGRA colors!
    staticPixels += str(unichr(127))
    staticPixels += str(unichr(89))
    staticPixels += str(unichr(49))
    staticPixels += str(unichr(0))
    i = i + 4

#### Set up the xbmc rendercapture object.
capture = xbmc.RenderCapture()
capture.capture(720, 480, xbmc.CAPTURE_FLAG_CONTINUOUS)

#### Main loop => connects to socket and captures errors!
while ALIVE:
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((HOST, PORT))
        CONNECTED = True
        log("Connected to JambiLight host application!")
        info("Success!", "Connected to JambiLight host application!")
    except socket.error, e:
        log("Could not connect to JambiLight host application!")
        info("Warning!", "JambiLight host application not found!")
        xbmc.sleep(10000)

    while CONNECTED:
        xbmc.sleep(10)
        try:
            # Check if there something playing.
            if xbmc.Player().isPlaying():
                capture.waitForCaptureStateChangeEvent(30)
                #Only proceed if the capture has succeeded!
                if capture.getCaptureState() == xbmc.CAPTURE_STATE_DONE:
                    #Get frame data and send it.
                    pixels = capture.getImage()
                    log("Image data captured (" + str(len(pixels)) + ")!")
                    sock.send(pixels)
                    pixels = None
                else:
                    log("No image captured!")
            else:
                log("No video playing, sending static signal!")
                sock.send(staticPixels)
                xbmc.sleep(40)
        except socket.error, e:
            log("Disconnected from the JambiLight Host!")
            info("Disconnected", "Disconnected from the JambiLight Host!")
            sock.close()
            sock = None
            CONNECTED = False

        # check to see if xbmc will shut down!
        if xbmc.abortRequested or ALIVE is False:
            sock.close()
            sock = None
            ALIVE = False
            CONNECTED = False
            log("Shutting down! ==> Goodbye!")
            info("shutdown", "shutdown")

    # check to see if xbmc will shut down!
    if xbmc.abortRequested or ALIVE is False:
        ALIVE = False
        sock.close()
        sock = None
        log("Shutting down! ==> Goodbye!")
        info("shutdown", "shutdown")

log("Exit!")