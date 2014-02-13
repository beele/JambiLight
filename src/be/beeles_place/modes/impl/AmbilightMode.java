package be.beeles_place.modes.impl;

import be.beeles_place.model.SettingsModel;
import be.beeles_place.modes.AbstractColorMode;
import be.beeles_place.view.ScreenGridView;

public class AmbilightMode extends AbstractColorMode{

    private SettingsModel settings;
    private AmbiLightCore core;
    private ScreenGridView frame;

    public AmbilightMode(SettingsModel settings, ScreenGridView frame) {
        this.settings = settings;
        this.frame = frame;
        core = new AmbiLightCore(settings);
    }

    @Override
    public boolean start() {
        core.calculate(frame);
        return true;
    }
}
