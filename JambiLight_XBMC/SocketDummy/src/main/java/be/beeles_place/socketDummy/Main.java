package be.beeles_place.socketDummy;

import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {

    private int port;
    private Dimension dimensions;

    private int totalPixels;
    private int width;
    private int height;
    private int totalBytes;
    private byte[] pixels;

    private int r = 255;
    private int g = 0;
    private int b = 0;

    private float frequency = 0.3f;
    private int rbCount = 0;
    
    private DataOutputStream out;
    private Socket clientSocket;

    public static void main(String[] args) {
        Main mummy = new Main();
        mummy.run();
    }

    public void run() {
        try {
        	init();
            sendLoop();
        } catch (Exception e) {
        	System.out.println("Fatal Error => " + e.getMessage());
        } finally {
        	dispose();
        }
    }
    
    private void init() throws InterruptedException {
    	System.out.println("Info => Init...");
    	
    	port = 1337;
        width = 720;
        height = 480;

        //XBMC sends the pixel data as BGRA (4 bytes per pixel).
        totalPixels = width * height;
        totalBytes = totalPixels * 4;
        dimensions = new Dimension(width, height);
        pixels = new byte[(int)(dimensions.getWidth() * dimensions.getHeight() * 4)];

        boolean connected = false;
        while(connected == false) {
        	try {
           	  	connectToSocket();
           	  	connected = true;
            } catch (Exception e) {
            	System.out.println("Error => " + e.getMessage());
            	Thread.sleep(1000);
            }
        }
        System.out.println("Info => Connected!");
    }
    
    private void connectToSocket() throws Exception {
    	System.out.println("Info => Connecting to socket...");
    	
    	clientSocket = new Socket("127.0.0.1",port);
        clientSocket.setReceiveBufferSize(totalBytes);
        out = new DataOutputStream(clientSocket.getOutputStream());
    }
    
    private void sendLoop() {
    	boolean run = true;
    	try {
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
                System.out.println("Info => Sending data: R: " + r + " G: " + g + " B: " + b);
                out.write(pixels);
                //Set to 30 FPS => 1000/30
                Thread.sleep(33);
            }
    	} catch (Exception e) {
    		System.out.println("Error => " + e.getMessage());
    		run = false;
    	}
    }
    
    private void dispose() {
    	System.out.println("Info => Disposing...");
    	
    	if(out != null) {
			try {
				out.close();
			} catch (Exception e) {
				System.out.println("Error => Cannot close output stream!");
			}
    	}
    	
    	if(clientSocket != null) {
    		try {
				clientSocket.close();
			} catch (IOException e) {
				System.out.println("Error => Cannot close socket!");
			}
    	}
    }
}
