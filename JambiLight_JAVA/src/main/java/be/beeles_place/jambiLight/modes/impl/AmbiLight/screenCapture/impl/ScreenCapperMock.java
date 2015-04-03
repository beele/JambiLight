package be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.impl;

import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.IScreenCapper;

import java.awt.*;

public class ScreenCapperMock implements IScreenCapper {

    private LOGGER logger;
    private Dimension size;

    private int r;
    private int g;
    private int b;
    
    private float frequency = 0.3f;
    private int rbCount = 0;

    private int delay = 0;
    private int[] pixels;

    /**
     * Creates a new ScreenCapper mock instance.
     * This will loop through the colors of the rainbow continuously.
     */
    public ScreenCapperMock() {
        this(20);
    }

    /**
     * Creates a new ScreenCapper mock instance.
     * This will loop through the colors of the rainbow continuously.
     *
     * @param delay Optional delay in milliseconds (default is 20)
     */
    public ScreenCapperMock(int delay) {
        logger = LOGGER.getInstance();
        logger.INFO("SCREENCAPPER => Starting mock screen capture. (RAINBOW)");

        this.delay = delay;
        //Static 480P size.
        int width = 720;
        int height = 480;
        logger.INFO("SCREENCAPPER => Mock screen resolution: width => " + width + " height => " + height);

        size = new Dimension(width ,height);
        pixels = new int[(int)(size.getWidth() * size.getHeight())];
    }

    @Override
    public Dimension getScreenDimensions() {
        return size;
    }

    @Override
    public int[] capture() {
        //Rainbows!
        r = (int)(Math.sin(frequency * rbCount + 0) * 127 + 128);
        g = (int)(Math.sin(frequency * rbCount + 2) * 127 + 128);
        b = (int)(Math.sin(frequency * rbCount + 4) * 127 + 128);
       if (rbCount < 32) {
           rbCount++;
       } else {
           rbCount = 0;
       }

        int totalPixels = pixels.length;
        for(int i = 0; i < totalPixels; i++) {
            int pixel = r;
            pixel = (pixel << 8) + g;
            pixel = (pixel << 8) + b;
            pixels[i] = pixel;
        }

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            logger.ERROR("IScreenCapper => RAINBOW-MOCK: Thread error in ScreenCapperMock!");
        }
        return pixels;
    }

    public void dispose() {
        size = null;
        logger = null; 
    }
}