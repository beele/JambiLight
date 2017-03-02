package be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.impl;

import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.DirectShowEnumerator;
import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.IScreenCapper;
import be.beeles_place.jambiLight.utils.logger.LOGGER;

import java.awt.*;

import static org.bytedeco.javacpp.opencv_core.Mat;

public class DirectShowCapper implements IScreenCapper {

    private final LOGGER logger;

    private SettingsModel settings;
    private Dimension dimensions;

    //private VideoCapture vc;
    private Mat videoInputSurface;
    private int[] pixels;

    private int width;
    private int height;

    private boolean initDone;

    /**
     * Constructor for DirectShowCapper.
     * Sets and calculates initial values.
     */
    public DirectShowCapper() {
        logger = LOGGER.getInstance();
        logger.INFO("IScreenCapper => Starting screen capture with through DirectShow device.");

        //Widescreen PAL format.
        width = 720;
        height = 480;
        logger.INFO("SCREENCAPPER => DirectShow source resolution: width => " + width + " height => " + height);

        pixels = new int[width * height];
        dimensions = new Dimension(width,height);
    }

    @Override
    public Dimension getScreenDimensions() {
        return dimensions;
    }

    @Override
    public void setSettings(SettingsModel settings) {
        this.settings = settings;
    }

    private void init() {
        logger.INFO("IScreenCapper => Running INIT for DirectShow capture");

        try {
            int deviceId = DirectShowEnumerator.findDeviceIdForName(settings.getDirectShowDeviceName());
            if(deviceId > -1) {
                /*vc = new VideoCapture(deviceId);
                vc.set(CV_CAP_PROP_FRAME_WIDTH,720);
                vc.set(CV_CAP_PROP_FRAME_HEIGHT,480);
                vc.set(CV_CAP_PROP_FPS ,25);*/
                videoInputSurface = new Mat();

                initDone = true;
            } else {
                logger.ERROR("IScreenCapper => No device id to connect to!");
            }
        } catch (Exception e) {
            logger.ERROR("IScreenCapper => Cannot initialize DirectShow capture: " + e.getMessage());
        }
    }

    @Override
    public int[] capture() {
        try {
            //Do the init if required.
            if(!initDone){
                init();
            }

            //Read the next frame, if true this means that it was read.
            /*if(vc.read(videoInputSurface)) {
                BufferedImage img = videoInputSurface.getBufferedImage();

                //Adjust capture dimensions if required.
                if(img.getWidth() != width || img.getHeight() != height) {
                    width = img.getWidth();
                    height = img.getHeight();
                    dimensions = new Dimension(width,height);

                    pixels = new int[width * height];
                }

                //Convert BGR component to a single pixel integer value.
                byte[] temp = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
                for (int i = 0 ; i < pixels.length; i++) {
                    //Convert from RGB bytes to pixel integer!
                    pixels[i] = (( temp[i*3+2] &0x0ff)<<16)|(( temp[i*3+1] &0x0ff)<<8)|( temp[i*3] &0x0ff);
                }

                //Clean up.
                img.flush();
                return pixels;
            } else {
                logger.INFO("IScreenCapper => No data was captured, returning black frame!");
                return new int[width * height];
            }*/
            return null;
        } catch (Exception e) {
            logger.ERROR("IScreenCapper => Error during capture: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void dispose() {
        try {
            /*if(vc != null && vc.isOpened()) {
                vc.release();
            }*/
            dimensions = null;
        } catch (Exception e) {
            logger.ERROR("IScreenCapper => Cannot dispose correctly!");
        }
    }
}
