package be.beeles_place.jambiLight.modes;

import be.beeles_place.jambiLight.utils.logger.LOGGER;

public abstract class AbstractColorMode implements IColorMode, Runnable {

    private LOGGER logger;
    private boolean forceQuit = false;

    {
        logger = LOGGER.getInstance();
    }

    @Override
    public void run() {
        logger.INFO("MODE => Color mode running!");
        while (!forceQuit) {
            start();
        }
    }

    @Override
    public abstract void start();

    @Override
    public void stop() {
        logger.INFO("MODE => Stopping current color mode!");
        forceQuit = true;
    }
}