import sys
import socket

import xbmc
import xbmcgui
import xbmcplugin


def log(msg):
    xbmc.log("### [%s] - %s" % ("JambiLight", msg,), level=xbmc.LOGDEBUG)


#### Show startup notification.
title = 'Startup'
text = 'JambiLight plugin started!'
time = 5000
xbmc.executebuiltin('Notification(%s, %s, %d)' % (title, text, time))

#### Start real code here!
ALIVE = True
HOST = 'localhost'
PORT = 8080

# Connect to the remote socket.
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.connect((HOST, PORT))

capture = xbmc.RenderCapture()
capture.capture(720, 480, xbmc.CAPTURE_FLAG_CONTINUOUS)

# Enter a loop for sending frames to the server application.
while ALIVE:
    xbmc.sleep(50)

    try:
        if xbmc.Player().isPlaying():
            log("video playing!")

            capture.waitForCaptureStateChangeEvent(100)
            if capture.getCaptureState() == xbmc.CAPTURE_STATE_DONE:
                #Get frame data, process it and send it!
                sock.sendall(capture.getImage())
                log("sending data")
            else:
                log("no image captured!")
        else:
            log("no video playing")

        #data = sock.recv(1024)
    except socket.error, e:
        log("Socket error")
        sock.close()

# check to see if xbmc will shut down!
if xbmc.abortRequested:
    ALIVE = False
    sock.close()
    log("Socket closed")

