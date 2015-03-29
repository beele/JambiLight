package be.beeles_place.jambiLight.modes;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.logger.LOGGER;

public abstract class AbstractColorStrategy implements IColorMode, Runnable {

    //Shared variables.
    protected LOGGER logger;
    protected ColorModel model;
    protected SettingsModel settings;

    protected boolean forceQuit = false;

    /**
     * Constructor.
     */
    protected AbstractColorStrategy() {
        logger = LOGGER.getInstance();
    }

    /**
     * Executed when the strategy is started as a thread.
     * Will loop the start() method (in a while loop) as long as the forceQuit variable is false.
     * If an exception occurs the logic will be aborted.
     */
    @Override
    public void run() {
        forceQuit = false;
        logger.INFO("MODE => Color strategy started.");

        while (!forceQuit) {
            try {
                start();
            } catch (Exception e) {
                logger.ERROR("MODE => Unexpected error! => " + e.toString());
                stop();
            }
        }
    }

    @Override
    public abstract void init(SettingsModel settings, ColorModel model);

    @Override
    public abstract void start();

    @Override
    public void stop() {
        logger.INFO("MODE => Stopping current color mode.");
        forceQuit = true;
    }
}