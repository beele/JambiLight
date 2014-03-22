package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;
import com.sun.glass.ui.Screen;

import java.awt.*;
import java.util.Date;

public class ScreenCapperJNIMock implements IScreenCapper {

    private Dimension size;

    private static boolean linkError = false;

    static {
        try {
            System.loadLibrary("ScreenCapper");
            //ScreenCapper.dll (Windows) or ScreenCapper.so (osx/linux)
        } catch (UnsatisfiedLinkError e) {
            linkError = true;
        }
    }

    /**
     * Creates a new ScreenCapperJNIMock instance.
     */
    public ScreenCapperJNIMock() {
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


        int[] temp;
        if(linkError) {
            temp = new int[size.width * size.height];
            for(int i = 0; i < temp.length; i++) {
                int pixel = 255;
                pixel = (pixel << 8);
                pixel = (pixel << 8);
                temp[i] = pixel;
            }
            LOGGER.getInstance().ERROR("JNI Link not satisfied! Please correct DLL location!");
        } else {
            //Do JNI call!
            long startTime = new Date().getTime();
            temp = captureViaJNI();
            long endTime = new Date().getTime();
            long difference = endTime - startTime;
            LOGGER.getInstance().INFO("SCREENCAPPER => JNI call took : " + difference + "ms");
        }

        return temp;
    }

    private native int[] captureViaJNI();

    public void dispose() {

    }
}