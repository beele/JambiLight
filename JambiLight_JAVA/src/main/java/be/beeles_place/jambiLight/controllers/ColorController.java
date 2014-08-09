package be.beeles_place.jambiLight.controllers;

import be.beeles_place.jambiLight.modes.AbstractColorMode;
import be.beeles_place.jambiLight.utils.logger.LOGGER;

public class ColorController {

    private LOGGER logger;

    private AbstractColorMode colorMode;
    private Thread colorThread;

    /**
     * Creates a ColorController instance.
     */
    public ColorController() {
        logger = LOGGER.getInstance();
    }

    /**
     * Stops the current color mode.
     */
    public void stopCurrentColorMode() {
        setColorMode(null);
    }

    //Getters & setters.
    public String getColorModeName() {
        return colorMode.getClass().getName();
    }

    public AbstractColorMode getColorMode() {
        return colorMode;
    }

    /**
     * Sets the new color mode.
     * If a color mode was previously running, it will be stopped before starting the new mode.
     * @param newColorMode A color mode that extends AbstractColorMode.
     */
    public void setColorMode(AbstractColorMode newColorMode) {
        logger.INFO("MODE => New color mode!");

        if (colorMode != null) {
            logger.INFO("MODE => Stopping previous color mode.");
            //Before setting the new color mode, stop the current one first!
            colorMode.stop();
        }

        //If no new color mode has been set.
        if (newColorMode == null) {
            logger.INFO("MODE => Cannot start a NULL color mode!");
            return;
        } else {
            colorMode = newColorMode;
            colorThread = new Thread(colorMode);
            colorThread.setPriority(Thread.MAX_PRIORITY);
            colorThread.setName(newColorMode.getClass().getName());
            colorThread.start();
            logger.INFO("MODE => Color mode " + newColorMode.getClass().getName() + " started!");
        }
    }
}