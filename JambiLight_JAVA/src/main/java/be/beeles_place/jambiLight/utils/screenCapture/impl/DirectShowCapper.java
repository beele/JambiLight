package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

public class DirectShowCapper implements IScreenCapper {

    private final LOGGER logger;

    private SettingsModel settings;
    private Dimension dimensions;

    private VideoCapture vc;
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
        height = 576;

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

        //TODO: Get the device ID from the settings!
        vc = new VideoCapture(0);
        //vc.set(CV_CAP_PROP_FRAME_WIDTH,720);
        //vc.set(CV_CAP_PROP_FRAME_HEIGHT,480);
        vc.set(CV_CAP_PROP_FPS ,25);
        videoInputSurface = new Mat();

        initDone = true;
    }

    @Override
    public int[] capture() {
        //Do the init if required.
        if(initDone == false){
            init();
        }

        //Read the next frame, if true this means that it was read.
        if(vc.read(videoInputSurface)) {
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
            img = null;
            temp = null;
            return pixels;
        } else {
            logger.INFO("IScreenCapper => No data was captured, returning black frame!");
            return new int[width * height];
        }
    }

    @Override
    public void dispose() {
        try {
            if(vc != null && vc.isOpened()) {
                vc.release();
            }
            dimensions = null;
        } catch (Exception e) {
            logger.ERROR("IScreenCapper => Cannot dispose correctly!");
        }
    }
}
