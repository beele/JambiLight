package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;

import java.awt.*;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class XbmcScreenCapper implements IScreenCapper {

    private boolean initDone = false;

    private ServerSocket server;
    private Socket client;
    private InputStream in;

    private byte[] data;
    private int[] pixels;

    private final int width;
    private final int height;
    private final int totalBytes;
    private final int totalPixels;

    /**
     * Constructor for XbmcScreenCapper.
     * Sets and calculates initial values.
     */
    public XbmcScreenCapper() {
        width = 720;
        height = 480;

        //XBMC sends the pixel data as BGRA.
        totalPixels = width * height;
        totalBytes = totalPixels * 4;

        data = new byte[totalBytes];
        pixels = new int[totalPixels];
    }

    @Override
    public Dimension getScreenDimensions() {
        return new Dimension(width,height);
    }

    @Override
    public int[] capture() {

        try {
            if(initDone == false) {
                server = new ServerSocket(1337);
                server.setReceiveBufferSize(totalBytes);

                System.out.println("waiting for connection on port 8080");
                client = server.accept();
                System.out.println("got connection on port 8080");

                in = client.getInputStream();
                initDone = true;
            }
        } catch (Exception e) {
            System.out.println("XBMC init failed!: " + e.getMessage());
        }

        try {
            int read = 0;
            boolean run = true;
            while(run) {

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
                    read = 0;
                    run = false;
                } else {
                    Thread.sleep(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Socket error: " +  e.getMessage());
        }

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

        return pixels;
    }

    public void dispose() {
        try {
            if(client != null) {
                client.close();
            }
            if(server != null) {
                server.close();
            }
        } catch (Exception e) {
            System.out.println("Cannot dispose correctly!");
        }
    }
}