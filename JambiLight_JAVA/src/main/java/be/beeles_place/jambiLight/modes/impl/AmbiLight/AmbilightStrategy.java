package be.beeles_place.jambiLight.modes.impl.AmbiLight;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.modes.AbstractColorStrategy;
import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;
import be.beeles_place.jambiLight.utils.screenCapture.ScreenCapperStrategy;

import java.awt.*;

public class AmbilightStrategy extends AbstractColorStrategy {

    private AmbiLightCore core;
    private IScreenCapper capper;

    public AmbilightStrategy() {
    }

    public void init(SettingsModel settings, ColorModel model) {
        logger.INFO("MODE => Init color strategy: AMBILIGHT");

        this.settings = settings;
        this.model = model;

        try {
            capper = settings.getCaptureMode().getCaptureStrategy();
            if(capper == null) {
                //Check to see if we are running headless, this sometimes occurs on OSX.
                if(GraphicsEnvironment.isHeadless()){
                    capper = ScreenCapperStrategy.MOCK_RAINBOW.getCaptureStrategy();
                } else {
                    capper = ScreenCapperStrategy.JAVA_SCREENSHOT.getCaptureStrategy();
                }
            }
            core = new AmbiLightCore(settings, model, capper);
        } catch (Exception e) {
            logger.ERROR("MODE => Error during init! Aborting!");
            stop();
        }
    }

    @Override
    public void start() {
        core.calculate();
    }

    @Override
    public void stop() {
        logger.INFO("MODE => Stopping current color mode.");

        if(core != null) {
            core.dispose();
        }
        forceQuit = true;
    }
}