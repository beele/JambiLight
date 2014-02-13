package be.beeles_place.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ScreenCapper {

    private Dimension size;
    private Robot robot;

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