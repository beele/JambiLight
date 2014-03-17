import sys
import xbmcgui
import xbmcplugin
 
title = "Hello World"
text = "This is some text"
time = 5000  # ms
 
xbmc.executebuiltin('Notification(%s, %s, %d)'%(title, text, time))