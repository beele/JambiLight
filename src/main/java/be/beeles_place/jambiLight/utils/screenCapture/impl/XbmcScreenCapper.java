package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;

import java.awt.*;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class XbmcScreenCapper implements IScreenCapper {

    private InputStream in;

    private byte[] data;

    private Date start = null;
    private Date end = null;

    private ServerSocket server;
    private Socket client;

    public XbmcScreenCapper() {
        data = new byte[1382400];

        try {
            server = new ServerSocket(8080);
            server.setReceiveBufferSize(1382400);
        } catch (Exception e) {
            System.out.println("CRITICAL ERROR!");
        }
    }

    @Override
    public Dimension getScreenDimensions() {
        return new Dimension(720,480);
    }

    @Override
    public int[] capture() {

        if(client ==null || in == null) {
            try {
                if(client == null) {
                    System.out.println("waiting for connection on port 8080");
                    client = server.accept();
                    System.out.println("got connection on port 8080");
                }
                in = client.getInputStream();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        try {
            int read = 0;
            boolean run = true;
            while(run) {

                inner: while(in.available() == 1382400) {
                    read += in.read(data, read, in.available());

                    while(read < 1382400) {
                        int toRead = 1382400 - read;
                        if(toRead > 0) {
                            read += in.read(data, read, toRead);
                        }
                    }
                    break inner;
                }

                //Only continue when the correct amount of pixels has been read!
                if(read == 1382400) {
                    read = 0;
                    run = false;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //Convert pixels!
        int [] pixels = new int[345600];
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