package be.beeles_place.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ScreenCapper {

    private Dimension size;
    private Robot robot;

    /**
     * Creates a new ScreenCapper instance.
     */
    public ScreenCapper() {
        size = Toolkit.getDefaultToolkit().getScreenSize();
        try {
            robot = new Robot();
        } catch (Exception e) {
            System.out.println("Cannot create robot!" + e.getMessage());
        }
    }

    /**
     * Get the screen dimensions.
     *
     * @return The dimensions of the screen.
     */
    public Dimension getScreenDimensions() {
        return size;
    }

    /**
     * Captures the current screen (excluding mouse cursor).
     *
     * @return An array of int containing all the pixels. One int per pixel. From Top-Left to Bottom-Right per row.
     */
    public int[] capture() {
        BufferedImage img = robot.createScreenCapture(new Rectangle(size));
        return ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    }
}