package be.beeles_place.modes.impl;

import be.beeles_place.model.ColorModel;
import be.beeles_place.model.SettingsModel;
import be.beeles_place.modes.AbstractColorMode;
import be.beeles_place.utils.screenCapture.ScreenCapper;

public class AmbilightMode extends AbstractColorMode {

    private SettingsModel settings;
    private AmbiLightCore core;

    public AmbilightMode(SettingsModel settings, ColorModel model) {
        this.settings = settings;
        core = new AmbiLightCore(settings, model, new ScreenCapper());
    }

    @Override
    public boolean start() {
        core.calculate();
        return true;
    }
}
