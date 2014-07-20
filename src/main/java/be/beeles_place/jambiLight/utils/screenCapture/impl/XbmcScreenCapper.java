package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;

import java.awt.*;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class XbmcScreenCapper implements IScreenCapper {

    private final LOGGER logger;

    private boolean initDone = false;

    private int port;
    private ServerSocket server;
    private Socket client;
    private InputStream in;

    private byte[] data;
    private int[] pixels;
    private Dimension dimensions;

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

        port = 1337;
        width = 720;
        height = 480;

        //XBMC sends the pixel data as BGRA (4 bytes per pixel).
        totalPixels = width * height;
        totalBytes = totalPixels * 4;

        data = new byte[totalBytes];
        pixels = new int[totalPixels];
        dimensions = new Dimension(width,height);
    }

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

        //Try to receive data.
        int read = 0;
        boolean run = true;
        while(run) {
            try {
                //If enough data is in the buffer, read it out.
                if(in.available() == totalBytes) {
                    read += in.read(data, read, in.available());

                    while(read < totalBytes) {
                        int toRead = totalBytes - read;
                        if(toRead > 0) {
                            read += in.read(data, read, toRead);
                        }
                    }
                }

                //Only continue when the correct amount of pixels has been read!
                if(read == totalBytes) {
                    //Process the data and return the pixels.
                    processData();
                    return pixels;
                } else {
                    //Saves CPU, waits for the next loop to see if all the data has been received.
                    Thread.sleep(1);
                }
            } catch (Exception e) {
                logger.ERROR("IScreenCapper => XBMC connection error: " +  e.getMessage());
                return null;
            }
        }

        //If you get here, something went wrong!
        return null;
    }

    /**
     * Sets up a new socket connection with both client and server.
     * @throws Exception When the socket cannot be started!
     */
    private void init() throws Exception{
        server = new ServerSocket(port);
        server.setReceiveBufferSize(totalBytes);

        logger.INFO("IScreenCapper => Waiting for XBMC connection on port " + port + "!");
        //This call is blocking and stops code execution until the connection has been made!
        client = server.accept();
        logger.INFO("IScreenCapper => XBMC client connected on port " + port + "!");

        in = client.getInputStream();
        initDone = true;
    }

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

    public void dispose() {
        try {
            if(in != null) {
                in.close();
            }
            if(client != null) {
                client.close();
            }
            if(server != null) {
                server.close();
            }
            in = null;
            client = null;
            server = null;
            
            data = null;
            pixels = null;
            dimensions = null;
        } catch (Exception e) {
            logger.ERROR("IScreenCapper => Cannot dispose correctly!");
        }
    }
}