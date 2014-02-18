package be.beeles_place.utils.screenCapture.impl;

import be.beeles_place.utils.screenCapture.IScreenCapper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ScreenCapper implements IScreenCapper {

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

    public Dimension getScreenDimensions() {
        return size;
    }

    public int[] capture() {
        BufferedImage img = robot.createScreenCapture(new Rectangle(size));
        return ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    }
}