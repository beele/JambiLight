package be.beeles_place.jambiLight.modes.impl;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.modes.AbstractColorMode;
import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;
import be.beeles_place.jambiLight.utils.screenCapture.impl.ScreenCapper;

public class AmbilightMode extends AbstractColorMode {

    private SettingsModel settings;
    private AmbiLightCore core;

    public AmbilightMode(SettingsModel settings, ColorModel model) {
        this.settings = settings;
        core = new AmbiLightCore(settings, model, new ScreenCapper());
    }

    public AmbilightMode(SettingsModel settings, ColorModel model, IScreenCapper capper) {
        this.settings = settings;
        core = new AmbiLightCore(settings, model, capper);
    }

    @Override
    public boolean start() {
        core.calculate();
        return true;
    }
}
