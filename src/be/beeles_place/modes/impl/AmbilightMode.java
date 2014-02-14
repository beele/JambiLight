package be.beeles_place.modes.impl;

import be.beeles_place.model.ColorModel;
import be.beeles_place.model.SettingsModel;
import be.beeles_place.modes.AbstractColorMode;
import be.beeles_place.view.ScreenGridView;

public class AmbilightMode extends AbstractColorMode{

    private SettingsModel settings;
    private AmbiLightCore core;

    public AmbilightMode(SettingsModel settings, ColorModel model) {
        this.settings = settings;
        core = new AmbiLightCore(settings, model);
    }

    @Override
    public boolean start() {
        core.calculate();
        return true;
    }
}
