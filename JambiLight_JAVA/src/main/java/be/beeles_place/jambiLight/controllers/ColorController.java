package be.beeles_place.jambiLight.controllers;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.modes.AbstractColorStrategy;
import be.beeles_place.jambiLight.modes.ColorStrategy;
import be.beeles_place.jambiLight.modes.IColorMode;
import be.beeles_place.jambiLight.utils.logger.LOGGER;

public class ColorController {

    private LOGGER logger;

    private IColorMode colorStrategy;
    private Thread colorLogicThread;

    private SettingsModel settings;
    private ColorModel model;

    /**
     * Creates a ColorController instance.
     *
     * @param settings The object containing the settings.
     * @param model The object containing the model.
     */
    public ColorController(SettingsModel settings, ColorModel model) {
        logger = LOGGER.getInstance();

        this.settings = settings;
        this.model = model;
    }

    /**
     * Starts a new color strategy.
     *
     * If a color strategy was previously running, it will be stopped before starting the new strategy.
     * @param newStrategy A color strategy that extends AbstractColorMode.
     */
    public void startColorStrategy(ColorStrategy newStrategy) {
        logger.INFO("MODE => New color strategy!");

        if (colorStrategy != null) {
            logger.INFO("MODE => Stopping previous color strategy.");
            //Before setting the new color strategy, stop the current one first!
            colorStrategy.stop();
        }

        //If no new color strategy has been set.
        if (newStrategy == null) {
            logger.INFO("MODE => Current color strategy stopped, no strategy running!");
            return;
        } else {
            AbstractColorStrategy newStrategyInstance = (AbstractColorStrategy) newStrategy.getColorStrategy();
            newStrategyInstance.init(settings, model);

            colorLogicThread = new Thread(newStrategyInstance);
            colorLogicThread.setPriority(Thread.MAX_PRIORITY);
            colorLogicThread.setName(newStrategyInstance.getClass().getName());
            colorLogicThread.start();
            logger.INFO("MODE => Color strategy " + newStrategyInstance.getClass().getName() + " started!");
            colorStrategy = newStrategyInstance;
        }
    }

    /**
     * Stops the current color strategy.
     */
    public void stopCurrentColorStrategy() {
        startColorStrategy(null);
    }
}