package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;

import java.awt.*;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ScreenCapper implements IScreenCapper {

    private Dimension size;
    private Robot robot;
    private com.sun.glass.ui.Robot backupRobot;

    private boolean useBackupRobot = false;

    /**
     * Creates a new ScreenCapper instance.
     */
    public ScreenCapper() {
        useBackupRobot = GraphicsEnvironment.isHeadless();

        if(useBackupRobot) {
            int width = Screen.getMainScreen().getWidth();
            int height = Screen.getMainScreen().getHeight();
            size = new Dimension(width,height);
        } else {
            size = Toolkit.getDefaultToolkit().getScreenSize();
            try {
                robot = new Robot();
            } catch (Exception e) {
                System.out.println("Cannot create robot!" + e.getMessage());
            }
        }
    }

    public Dimension getScreenDimensions() {
        return size;
    }

    public int[] capture() {
        if(useBackupRobot){
            System.out.println(Thread.currentThread().getName());
            //TODO: crashes because of some threading problem ==> investigate!
            Pixels pixelsColl = backupRobot.getScreenCapture(0, 0, size.width, size.height);
            final int[] pixels = (int[]) pixelsColl.getPixels().array();
            return pixels;
        } else {
            BufferedImage img = robot.createScreenCapture(new Rectangle(size));
            return ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        }
    }
}