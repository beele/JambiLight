package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class XbmcScreenCapper implements IScreenCapper {

    private final LOGGER logger;
    private Dimension dimensions;

    private boolean initDone = false;

    private int port;
    private ServerSocket server;
    private Socket client;
    private InputStream in;
    private OutputStream out;

    private byte[] data;
    private int[] pixels;
    private int[] staticPixels;

    private final int totalPixels;
    private final int width;
    private final int height;
    private final int totalBytes;

    /**
     * Constructor for XbmcScreenCapper.
     * Sets and calculates initial values.
     */
    public XbmcScreenCapper() {
        logger = LOGGER.getInstance();
        logger.INFO("SCREENCAPPER => Starting screen capture with XBMC.");

        port = 1337;
        width = 720;
        height = 480;

        //XBMC sends the pixel data as BGRA (4 bytes per pixel).
        totalPixels = width * height;
        totalBytes = totalPixels * 4;

        data = new byte[totalBytes];
        pixels = new int[totalPixels];
        dimensions = new Dimension(width, height);

        staticPixels = new int[width * height];
        for (int i = 0; i < staticPixels.length; i++) {
            staticPixels[i] = 6729727;
        }
    }

    private int writeCounter = 0;

    @Override
    public Dimension getScreenDimensions() {
        return dimensions;
    }

    @Override
    public int[] capture() {
        //Perform startup, this should only be done once, hence the if statement!
        if(initDone == false) {
            try {
                init();
            } catch (Exception e) {
                logger.ERROR("IScreenCapper => XBMC communication init failed!: " + e.getMessage());
                return null;
            }
        }

        int previousMissingBytes = 0;

        //Try to receive data.
        int read = 0;
        boolean run = true;
        while(run) {
            try {
                Thread.sleep(20);
                //Check to see if the socket is still open!
                writeCounter = 0;
                try {
                    out.write(0);
                    out.flush();
                } catch (IOException ioe) {
                    //Reset init state.
                    logger.DEBUG("IScreenCapper => Killing inactive XBMC socket connection!");
                    socketCleanup();
                    initDone = false;
                    return staticPixels;
                }

                //If enough data is in the buffer, read it out.
                int size = in.available();
                if(size >= totalBytes) {
                    read = in.read(data, previousMissingBytes, totalBytes);

                    while(read < totalBytes) {
                        int toRead = totalBytes - read;
                        if(toRead > 0) {
                            read += in.read(data, read, toRead);
                        }
                    }

                    previousMissingBytes = 0;
                }

                //TODO: Temp fix for KODI issues on OSX => Causes horizontal scrolling of input source for now, but it keeps working!
                if(totalBytes - size < 100 && totalBytes - size > 0) {
                    int missingBytes = totalBytes - size;
                    logger.ERROR("- NON FATAL ERROR - Stall override: missing " + missingBytes + " bytes!");

                    read = in.read(data, previousMissingBytes, size);

                    while(read < totalBytes - missingBytes) {
                        int toRead = totalBytes - missingBytes - read;
                        if(toRead > 0) {
                            read += in.read(data, read, toRead);
                        }
                    }

                    for(int i = missingBytes; i > 0 ; i--) {
                        //Fill missing pixels data in with black!
                        data[data.length - i] = (byte) 0;
                    }

                    read += missingBytes;
                    previousMissingBytes = missingBytes;
                }
                //TODO: End of temp fix!

                //Only continue when the correct amount of pixels has been read!
                if(read == totalBytes) {
                    //Process the data and return the pixels.
                    processData();
                    return pixels;
                }
            } catch (Exception e) {
                logger.ERROR("IScreenCapper => XBMC connection error: " +  e.getMessage());
                e.printStackTrace();
                return null;
            }
        }

        //If you get here, something went wrong!
        return null;
    }

    /**
     * Sets up a new socket connection with both client and server.
     *
     * @throws Exception When the socket cannot be started!
     */
    private void init() throws Exception{
        server = new ServerSocket(port);
        server.setReceiveBufferSize(totalBytes);

        logger.INFO("IScreenCapper => Waiting for XBMC connection on port " + port + "!");
        //This call is blocking and stops code execution until the connection has been made!
        client = server.accept();
        logger.INFO("IScreenCapper => XBMC client connected on port " + port + "!");

        //Get the inputstream.
        in = client.getInputStream();
        out = client.getOutputStream();

        initDone = true;
    }

    /**
     * Processes the pixel data.
     * Splits up the R/G/B components.
     */
    private void processData() {
        //Convert bytes to integer values!
        int pixelCounter = 0;
        for(int i = 0; i < data.length ; i+=4) {
            int r = data[i+2];
            int g = data[i+1];
            int b = data[i];

            r = (r << 16) & 0x00FF0000;
            g = (g << 8) & 0x0000FF00;
            b = b & 0x000000FF;

            pixels[pixelCounter++] = (0xFF000000 | r | g | b);
        }
    }

    private void socketCleanup() {
        try {
            if(in != null) {
                in.close();
            }
            if(out != null) {
                out.close();
            }
            if(client != null) {
                client.close();
            }
            if(server != null) {
                server.close();
            }
            in = null;
            out = null;
            client = null;
            server = null;
        } catch (Exception e) {
            //TODO: Log
        }
    }

    public void dispose() {
        try {
            socketCleanup();
            
            data = null;
            pixels = null;
            dimensions = null;
        } catch (Exception e) {
            logger.ERROR("IScreenCapper => Cannot dispose correctly!");
        }
    }
}