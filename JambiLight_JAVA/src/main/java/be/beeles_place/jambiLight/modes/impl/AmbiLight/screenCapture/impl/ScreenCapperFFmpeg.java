package be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.impl;

import be.beeles_place.jambiLight.modes.impl.AmbiLight.screenCapture.IScreenCapper;
import be.beeles_place.jambiLight.utils.OperatingSystemDetector;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import com.sun.glass.ui.Screen;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class ScreenCapperFFmpeg implements IScreenCapper {

    private LOGGER logger;

    private Dimension size;
    private int[] pixels;
    private FFmpegFrameGrabber grabber;
    private Java2DFrameConverter frameConverter;

    private int width;
    private int height;

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

        width = Screen.getMainScreen().getWidth() / 2;
        height = Screen.getMainScreen().getHeight() / 2;
        size = new Dimension(width, height);
        frameConverter = new Java2DFrameConverter();

        try {
            OperatingSystemDetector.OSType osType = OperatingSystemDetector.detectOperatingSystem();

            String device = null;
            String format = null;
            switch (osType) {
                case MacOS:
                    device = DEVICE_OSX;
                    format = FORMAT_OSX;
                    break;
                case Windows:
                    device = DEVICE_WINDOWS;
                    format = FORMAT_WINDOWS;
                    break;
                case Linux:
                    device = DEVICE_LINUX;
                    format = FORMAT_LINUX;
                    break;
                case Other:
                    throw new Exception("Could not determine the operating system, FFmpeg grabber cannot start!");
            }

            //TODO: Add fallback for windows!
            grabber = new FFmpegFrameGrabber(device);
            grabber.setFormat(format);
            grabber.setImageWidth((int)size.getWidth());
            grabber.setImageHeight((int)size.getHeight());
            grabber.setAudioBitrate(0);
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

            BufferedImage img = frameConverter.getBufferedImage(grabbedFrame, 1.0, false, null);
            logger.DEBUG("Grabbing frame and making image took: " + (System.currentTimeMillis() - startTime) + "ms");

            startTime = System.currentTimeMillis();
            pixels = img.getRGB(0,0, width, height, pixels, 0, width);
            logger.DEBUG("Converting to int array took: " + (System.currentTimeMillis() - startTime) + "ms");

            img.flush();
            return pixels;

        } catch (Exception e) {
            logger.ERROR("A problem occurred during frame capture", e);
        }

        return null;
    }

    @Override
    public byte[] captureBytes() {
        try {
            long startTime = System.currentTimeMillis();
            org.bytedeco.javacv.Frame grabbedFrame = grabber.grab();

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
            grabber.stop();
        } catch (Exception e) {
            grabber = null;
        }
    }
}
