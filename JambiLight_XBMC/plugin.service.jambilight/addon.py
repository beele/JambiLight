import sys
import socket

import xbmc
import xbmcgui
import xbmcplugin

HOST = "localhost"
PORT = 1337
SOCK = None
CAPT = None

LIVE = True
CONNECT = False
CONNECTED = False


def log(msg):
    xbmc.log("### [%s] - %s" % ("JambiLight", msg,), level=xbmc.LOGDEBUG)


def info(title, msg):
    xbmc.executebuiltin('Notification(%s, %s, %d)' % (title, msg, 2500))


class PlayerWatcher(xbmc.Player):
    def __init__(self, *args):
        pass

    def onPlayBackStarted(self):
        global CONNECT

        log("started playing a file!")
        info("Video playing", "Video playing")
        closeSocket()
        CONNECT = True

    def onPlayBackStopped(self):
        global CONNECT

        log("stopped playing a file!")
        info("Video stopped", "Video stopped")
        closeSocket()
        CONNECT = False


def init():
    global CAPT

    #### Show startup notification.
    info("Startup", "JambiLight plugin started!")

    #### Set up the xbmc rendercapture object.
    CAPT = xbmc.RenderCapture()
    CAPT.capture(720, 480, xbmc.CAPTURE_FLAG_CONTINUOUS)


def connectToSocket():
    global SOCK
    global CONNECTED

    try:
        SOCK = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        SOCK.connect((HOST, PORT))
        CONNECTED = True
        log("Connected to JambiLight host application!")
        info("Success!", "Connected to JambiLight host application!")
    except socket.error, e:
        log("Could not connect to JambiLight host application!")
        info("Warning!", "JambiLight host application not found!")


def sendData():
    global CAPT
    global SOCK

    CAPT.waitForCaptureStateChangeEvent(1000)
    # Only proceed if the capture has succeeded!
    if CAPT.getCaptureState() == xbmc.CAPTURE_STATE_DONE:
        # Get frame data and send it.
        pixels = CAPT.getImage()
        SOCK.send(pixels)


def closeSocket():
    global SOCK
    global CONNECTED

    CONNECTED = False
    if SOCK is not None:
        SOCK.close()
        SOCK = None


PLYR = PlayerWatcher()
init()
while LIVE:
    log("In main logic loop!")
    try:
        if CONNECT is True and CONNECTED is False:
            connectToSocket()
        elif CONNECT is True and CONNECTED is True:
            sendData()
        elif CONNECT is False and CONNECTED is True:
            closeSocket()

        if xbmc.abortRequested:
            closeSocket()
            LIVE = False

    except socket.error, e:
        log("Unknow error! Connection lost?")
        info("Error", "Unknow error! Connection lost?")
        closeSocket()

    xbmc.sleep(25)