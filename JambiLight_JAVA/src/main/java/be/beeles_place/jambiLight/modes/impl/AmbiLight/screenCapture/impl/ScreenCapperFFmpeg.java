package be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.impl;

import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.IScreenCapper;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import com.sun.glass.ui.Screen;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ScreenCapperFFmpeg implements IScreenCapper {

    private LOGGER logger;
    private CanvasFrame frame;

    private Dimension size;
    private int[] pixels;
    private FFmpegFrameGrabber grabber;
    private Java2DFrameConverter frameConverter;

    private final String FORMAT_WINDOWS             = "dshow";
    private final String FORMAT_WINDOWS_FALLBACK    = "gdigrab";
    private final String FORMAT_LINUX               = "x11grab";
    private final String FORMAT_OSX                 = "avfoundation";

    private final String DEVICE_WINDOWS             = "video=screen-capture-recorder";
    private final String DEVICE_WINDOWS_FALLBACK    = "desktop";
    private final String DEVICE_LINUX               = ":0.0+0,0";
    private final String DEVICE_OSX                 = "1:";

    public ScreenCapperFFmpeg() {
        logger = LOGGER.getInstance();
        logger.INFO("SCREENCAPPER => Starting screen capture with FFmpeg");

        //frame = new CanvasFrame("FFmpeg debug view");

        size = new Dimension(Screen.getMainScreen().getWidth(), Screen.getMainScreen().getHeight());
        frameConverter = new Java2DFrameConverter();

        try {
            grabber = new FFmpegFrameGrabber(DEVICE_OSX);
            grabber.setFormat(FORMAT_OSX);
            grabber.setImageWidth((int)size.getWidth());
            grabber.setImageHeight((int)size.getHeight());
            grabber.setFrameRate(30);

            grabber.start();
        } catch (Exception e) {
            logger.ERROR("Cannot initialise capture method: " + e.getMessage());
        }
    }

    @Override
    public Dimension getScreenDimensions() {
        return size;
    }

    @Override
    public boolean capturesBytesInsteadOfPixelInts() {
        return true;
    }

    @Override
    public int[] capture() {
        try {
            long startTime = System.currentTimeMillis();
            org.bytedeco.javacv.Frame grabbedFrame = grabber.grab();

            /*if (frame.isVisible()) {
                frame.showImage(grabbedFrame);
            }*/

            BufferedImage img = frameConverter.getBufferedImage(grabbedFrame, 1.0, false, null);
            logger.DEBUG("Grabbing frame and making image took: " + (System.currentTimeMillis() - startTime) + "ms");

            startTime = System.currentTimeMillis();
            pixels = img.getRGB(0,0, 1920, 1080, pixels, 0, 1920);
            logger.DEBUG("Converting to int array took: " + (System.currentTimeMillis() - startTime) + "ms");

            img.flush();
            return pixels;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public byte[] captureBytes() {
        try {
            long startTime = System.currentTimeMillis();
            org.bytedeco.javacv.Frame grabbedFrame = grabber.grab();

            /*if (frame.isVisible()) {
                frame.showImage(grabbedFrame);
            }*/

            BufferedImage img = frameConverter.getBufferedImage(grabbedFrame, 1.0, false, null);
            logger.DEBUG("Grabbing frame and making image took: " + (System.currentTimeMillis() - startTime) + "ms");

            startTime = System.currentTimeMillis();
            byte[] pixelBytes = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
            logger.DEBUG("Converting to int array took: " + (System.currentTimeMillis() - startTime) + "ms");

            img.flush();
            return pixelBytes;

        } catch (Exception e) {
            logger.ERROR("A problem occurred during frame capture", e);
            dispose();
        }

        return null;
    }

    @Override
    public void dispose() {
        try {
            frame.dispose();
            grabber.stop();
        } catch (Exception e) {
            grabber = null;
        }
    }
}
