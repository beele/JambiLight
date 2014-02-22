package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;
import com.sun.glass.ui.Screen;

import java.awt.*;

public class ScreenCapperMock implements IScreenCapper {

    private Dimension size;

    private int r;
    private int g;
    private int b;

    /**
     * Creates a new ScreenCapper mock instance.
     * The JNI mock will return a full green image if the JNI call succeeds.
     * It will return a full red image if the JNI call fails.
     */
    public ScreenCapperMock(int MockPixelColorR, int MockPixelColorG, int MockPixelColorB) {
        r = MockPixelColorR;
        g = MockPixelColorG;
        b = MockPixelColorB;

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

        for(int i = 0; i < pixels.length; i++) {
            int pixel = r;
            pixel = (pixel << 8) + g;
            pixel = (pixel << 8) + b;
            pixels[i] = pixel;
        }

        return pixels;
    }
}