package be.beeles_place.jambiLight.modes.impl;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.modes.AbstractColorMode;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;
import be.beeles_place.jambiLight.utils.screenCapture.impl.ScreenCapper;
import be.beeles_place.jambiLight.utils.screenCapture.impl.ScreenCapperMock;

import java.awt.*;

public class AmbilightMode extends AbstractColorMode {

    private LOGGER logger;

    private AmbiLightCore core;
    private IScreenCapper capper;

    private SettingsModel settings;
    private ColorModel model;

    public AmbilightMode(SettingsModel settings, ColorModel model) {
        logger = LOGGER.getInstance();
        this.settings = settings;
        this.model = model;
    }

    public AmbilightMode(SettingsModel settings, ColorModel model, IScreenCapper capper) {
        logger = LOGGER.getInstance();

        this.settings = settings;
        this.model = model;
        this.capper = capper;
    }

    public void init() {
        logger.INFO("MODE => Starting AMBILIGHT-CORE");

        if(capper == null) {
            if(GraphicsEnvironment.isHeadless()){
                capper = new ScreenCapperMock(0,255,0);
            } else {
                capper = new ScreenCapper();
            }
        }
        core = new AmbiLightCore(settings, model, capper);
    }

    @Override
    public void start() {
        core.calculate();
    }
}