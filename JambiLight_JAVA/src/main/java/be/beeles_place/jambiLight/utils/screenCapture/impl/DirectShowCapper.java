package be.beeles_place.jambiLight.utils.screenCapture.impl;

import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;
import org.bytedeco.javacpp.opencv_core;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import static org.bytedeco.javacpp.opencv_highgui.*;

public class DirectShowCapper implements IScreenCapper {

    private final LOGGER logger;
    private Dimension dimensions;

    private VideoCapture vc;

    private byte[] data;
    private int[] pixels;

    private final int width;
    private final int height;

    private boolean initDone;

    /**
     * Constructor for DirectShowCapper.
     * Sets and calculates initial values.
     */
    public DirectShowCapper() {
        logger = LOGGER.getInstance();
        logger.INFO("SCREENCAPPER => Starting screen capture with through DirectShow device.");

        width = 720;
        height = 576;

        pixels = new int[width * height];
        dimensions = new Dimension(width,height);
    }

    @Override
    public Dimension getScreenDimensions() {
        return dimensions;
    }

    private void init() {
        //TODO: Set up the correct device!
        vc = new VideoCapture(0);
        //vc.set(CV_CAP_PROP_FRAME_WIDTH,720);
        //vc.set(CV_CAP_PROP_FRAME_HEIGHT,480);

        initDone = true;
    }

    @Override
    public int[] capture() {
        if(initDone == false){
            init();
        }

        opencv_core.Mat m = new opencv_core.Mat();
        if(vc.read(m)) {
            BufferedImage img = m.getBufferedImage();
            byte[] temp = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
            for (int i = 0 ; i < pixels.length; i++) {
                //Convert from RGB bytes to pixel integer!
                pixels[i] = (( temp[i*3+2] &0x0ff)<<16)|(( temp[i*3+1] &0x0ff)<<8)|( temp[i*3] &0x0ff);
            }

            img.flush();
            img = null;
            temp = null;
            return pixels;
        } else {
            //TODO: Error!
            return new int[width * height];
        }
    }

    @Override
    public void dispose() {
        if(vc != null && vc.isOpened()) {
            vc.release();
        }

        dimensions = null;
    }
}
