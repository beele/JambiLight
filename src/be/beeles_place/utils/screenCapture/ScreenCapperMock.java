package be.beeles_place.utils.screenCapture;

import java.awt.*;

public class ScreenCapperMock implements IScreenCapper {

    private Dimension size;
    private Robot robot;

    private int r;
    private int g;
    private int b;

    /**
     * Creates a new ScreenCapper mock instance.
     */
    public ScreenCapperMock(int MockPixelColorR, int MockPixelColorG, int MockPixelColorB) {
        r = MockPixelColorR;
        g = MockPixelColorG;
        b = MockPixelColorB;
    }

    @Override
    public Dimension getScreenDimensions() {
        size = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            robot = new Robot();
        } catch (Exception e) {
            System.out.println("Cannot create robot!" + e.getMessage());
        }
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
