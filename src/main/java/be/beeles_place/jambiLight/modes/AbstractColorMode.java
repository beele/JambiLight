package be.beeles_place.jambiLight.modes;

import be.beeles_place.jambiLight.utils.logger.LOGGER;

public abstract class AbstractColorMode implements IColorMode, Runnable {

    private LOGGER logger;
    protected boolean forceQuit = false;

    protected AbstractColorMode() {
        logger = LOGGER.getInstance();
    }

    @Override
    public void run() {
        forceQuit = false;
        init();
        logger.INFO("MODE => Color mode running.");
        while (!forceQuit) {
            try {
                start();
            } catch (Exception e) {
                logger.ERROR("MODE => Unexpected error! => " + e.getMessage());
                stop();
            }
        }
    }

    @Override
    public abstract void init();

    @Override
    public abstract void start();

    @Override
    public void stop() {
        logger.INFO("MODE => Stopping current color mode.");
        forceQuit = true;
    }
}