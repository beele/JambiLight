package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;
import com.sun.glass.ui.Screen;

import java.awt.*;
import java.util.Date;

public class ScreenCapperJNI implements IScreenCapper {

    private LOGGER logger;
    private Dimension size;

    private static boolean linkError = false;

    /**
     * Loads the native library.
     * Will set linkError to true if the library cannot be loaded!
     */
    static {
        try {
            //ScreenCapper.dll (Windows) or ScreenCapper.so (osx/linux)
            System.loadLibrary("ScreenCapper");
        } catch (UnsatisfiedLinkError e) {
            linkError = true;
        }
    }

    /**
     * Constructor.
     */
    public ScreenCapperJNI() {
        logger = LOGGER.getInstance();
        logger.INFO("SCREENCAPPER => Starting screen capture with JNI.");

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
            logger.ERROR("SCREENCAPPER => JNI Link not satisfied! Please correct DLL location!");
        } else {
            //Do JNI call!
            long startTime = new Date().getTime();
            temp = captureViaJNI();
            long endTime = new Date().getTime();
            long difference = endTime - startTime;
            logger.DEBUG("SCREENCAPPER => JNI call took : " + difference + "ms");
        }

        return temp;
    }

    /**
     * Native method, this is the name of the C++ method that is executed.
     *
     * @return An array of int, the captured pixels.
     */
    private native int[] captureViaJNI();

    public void dispose() {
        size = null;
        logger = null;
    }
}