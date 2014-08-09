package be.beeles_place.socketDummy;

import java.awt.*;
import java.io.DataOutputStream;
import java.net.Socket;

public class Main {

    private boolean initDone = false;

    private int port;
    private Dimension dimensions;

    private int totalPixels;
    private int width;
    private int height;
    private int totalBytes;

    private int r = 255;
    private int g = 0;
    private int b = 0;

    private float frequency = 0.3f;
    private int rbCount = 0;

    //TODO: Clean up this mess!

    public static void main(String[] args) {
        Main m = new Main();
        m.run();
    }

    public void run() {
        try {
            port = 1337;
            width = 720;
            height = 480;

            //XBMC sends the pixel data as BGRA (4 bytes per pixel).
            totalPixels = width * height;
            totalBytes = totalPixels * 4;
            dimensions = new Dimension(width, height);

            Socket clientSocket = new Socket("127.0.0.1",port);
            clientSocket.setReceiveBufferSize(totalBytes);
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            byte[] pixels = new byte[(int)(dimensions.getWidth() * dimensions.getHeight() * 4)];

            while(true) {

                //Rainbows!
                r = (int)(Math.sin(frequency * rbCount + 0) * 127 + 128);
                g = (int)(Math.sin(frequency * rbCount + 2) * 127 + 128);
                b = (int)(Math.sin(frequency * rbCount + 4) * 127 + 128);
                if (rbCount < 32) {
                    rbCount++;
                } else {
                    rbCount = 0;
                }

                for(int i = 0; i < totalBytes ; i+=4) {
                    pixels[i+2] = (byte)r;
                    pixels[i+1] = (byte)g;
                    pixels[i] = (byte)b;
                }
                out.write(pixels);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
