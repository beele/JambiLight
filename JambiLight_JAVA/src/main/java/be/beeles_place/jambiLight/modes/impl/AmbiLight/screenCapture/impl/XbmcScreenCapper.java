package be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.impl;

import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.IScreenCapper;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class XbmcScreenCapper implements IScreenCapper {

    private final LOGGER logger;
    private Dimension dimensions;

    private boolean initDone = false;

    private int port;
    private ServerSocket server;
    private Socket client;
    private BufferedInputStream in;
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
        logger.INFO("SCREENCAPPER => XBMC/KODI resolution: width => " + width + " height => " + height);

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

    @Override
    public Dimension getScreenDimensions() {
        return dimensions;
    }

    @Override
    public int[] capture() {
        //Perform startup, this should only be done once, hence the if statement!
        if (!initDone) {
            try {
                init();
            } catch (Exception e) {
                logger.ERROR("IScreenCapper => XBMC communication init failed!: " + e.getMessage());
                return null;
            }
        }
        //Try to receive data.
        int read = 0;

        try {
            //Collect all the bytes!
            while (read != totalBytes) {
                int av = in.available();
                av = av < totalBytes ? av : totalBytes;
                av = av - read < 0 ? 0 : av - read;

                int r = in.read(data, read, av);
                if (r > 0) {
                    read += r;
                }

                //Attempt to write to the socket, if this fails, the socket has most likely been closed from the python side!
                out.write(0);
                out.flush();
                Thread.sleep(1);
            }

            //Process and return the data we received.
            processData();
            return pixels;

        } catch (Exception e) {
            logger.ERROR("IScreenCapper => XBMC connection error: " + e.getMessage());
            logger.INFO("IScreenCapper => XBMC logic will be reset!");

            socketCleanup();
            initDone = false;
            return staticPixels;
        }
    }

    /**
     * Sets up a new socket connection with both client and server.
     *
     * @throws Exception When the socket cannot be started!
     */
    private void init() throws Exception {
        server = new ServerSocket(port);
        server.setReceiveBufferSize(totalBytes * 10);

        logger.INFO("IScreenCapper => Waiting for XBMC connection on port " + port + "!");
        //This call is blocking and stops code execution until the connection has been made!
        client = server.accept();
        logger.INFO("IScreenCapper => XBMC client connected on port " + port + "!");

        //Get the inputstream.
        InputStream is = client.getInputStream();
        in = new BufferedInputStream(is);
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
        for (int i = 0; i < data.length; i += 4) {
            int r = data[i + 2];
            int g = data[i + 1];
            int b = data[i];

            r = (r << 16) & 0x00FF0000;
            g = (g << 8) & 0x0000FF00;
            b = b & 0x000000FF;

            pixels[pixelCounter++] = (0xFF000000 | r | g | b);
        }
    }

    private void socketCleanup() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (client != null) {
                client.close();
            }
            if (server != null) {
                server.close();
            }
            in = null;
            out = null;
            client = null;
            server = null;
        } catch (Exception e) {
            logger.ERROR("IScreenCapper => Error during socket cleanup => " + e.getMessage());
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