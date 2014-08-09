package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class ScreenCapper implements IScreenCapper {

    private Dimension size;
    private Robot robot;
    private com.sun.glass.ui.Robot backupRobot;

    private boolean useBackupRobot = false;

    private int[] pixels;
    private Rectangle captureField;

    /**
     * Creates a new ScreenCapper instance.
     */
    public ScreenCapper() {
        useBackupRobot = GraphicsEnvironment.isHeadless();

        if(useBackupRobot) {
            size = new Dimension(Screen.getMainScreen().getWidth(),Screen.getMainScreen().getHeight());
        } else {
            size = Toolkit.getDefaultToolkit().getScreenSize();
            captureField = new Rectangle(size);
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
            //System.out.println(Thread.currentThread().getName());
            //TODO: crashes because of some threading problem ==> investigate!
            Pixels pixelsColl = backupRobot.getScreenCapture(0, 0, size.width, size.height);
            pixels = (int[]) pixelsColl.getPixels().array();
            pixelsColl = null;
            return pixels;
        } else {
            BufferedImage img = robot.createScreenCapture(captureField);
            pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
            img.flush();
            img = null;
            return pixels;
        }
    }

    public void dispose() {
        captureField = null;
        robot = null;
        backupRobot = null;
        pixels = null;
        size = null;
    }
}