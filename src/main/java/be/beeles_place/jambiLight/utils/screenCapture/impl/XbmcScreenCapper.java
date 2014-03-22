package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;

import java.awt.*;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class XbmcScreenCapper implements IScreenCapper {

    private InputStream in;
    //private PrintWriter out;µ

    private byte[] data;

    private Date start = null;
    private Date end = null;

    public XbmcScreenCapper() {
        data = new byte[1382400];

        try {
            ServerSocket server = new ServerSocket(8080);
            server.setReceiveBufferSize(1382400);
            System.out.println("waiting for connection on port 8080");

            Socket client = server.accept();
            System.out.println("got connection on port 8080");
            InputStream in = client.getInputStream();
            //PrintWriter out = new PrintWriter(client.getOutputStream(),true);
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

        try {
            int read = 0;
            boolean run = true;
            while(run) {

                inner: while(in.available() >= 1382400) {
                    start = new Date();
                    read += in.read(data, read, in.available());

                    while(read < 1382400) {
                        int toRead = 1382400 - read;
                        if(toRead > 0) {
                            read += in.read(data, read, toRead);
                        }
                    }
                    end = new Date();
                    break inner;
                }

                //Read out some stats!
                if(read > 1380000) {
                    long diff = end.getTime() - start.getTime();
                    System.out.println("bytes read: " + read + " in " + diff +  "ms");
                    System.out.println("color at 500k = " + data[500000]);
                    read = 0;
                    run = false;
                }
            }
        } catch (Exception e) {

        }

        int [] pixels = new int[1382400];
        int pixelCounter = 0;
        for(int i = 0; i < data.length ; i+=3) {
            pixels[pixelCounter++] = data[i];
            pixels[pixelCounter++] = data[i+1];
            pixels[pixelCounter++] = data[i+2];
        }

        return pixels;
    }
}