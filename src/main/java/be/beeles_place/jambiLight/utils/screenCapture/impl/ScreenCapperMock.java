package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;
import com.sun.glass.ui.Screen;

import java.awt.*;

public class ScreenCapperMock implements IScreenCapper {

    private Dimension size;

    private int r;
    private int g;
    private int b;

    private float frequency = 0.3f;
    private int rbCount = 0;

    private int delay = 0;

    /**
     * Creates a new ScreenCapper mock instance.
     * The JNI mock will return a full green image if the JNI call succeeds.
     * It will return a full red image if the JNI call fails.
     */
    public ScreenCapperMock(int delay) {
        this.delay = delay;

        int width = Screen.getMainScreen().getWidth();
        int height = Screen.getMainScreen().getHeight();
        size = new Dimension(width,height);
    }

    @Override
    public Dimension getScreenDimensions() {
        return size;
    }

    @Override
    public int[] capture() {
        int[] pixels = new int[(int)(size.getWidth() * size.getHeight())];

        //Rainbows!
        r = (int)(Math.sin(frequency * rbCount + 0) * 127 + 128);
        g = (int)(Math.sin(frequency * rbCount + 2) * 127 + 128);
        b = (int)(Math.sin(frequency * rbCount + 4) * 127 + 128);
       if (rbCount < 32) {
           rbCount++;
       } else {
           rbCount = 0;
       }

        for(int i = 0; i < pixels.length; i++) {
            int pixel = r;
            pixel = (pixel << 8) + g;
            pixel = (pixel << 8) + b;
            pixels[i] = pixel;
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            System.out.println("Thread error in ScreenCapperMock!");
        }
        return pixels;
    }

    public void dispose() {
        size = null;
    }
}