package be.beeles_place.jambiLight.commanding.commands.impl;

import be.beeles_place.jambiLight.commanding.commands.ICommand;
import be.beeles_place.jambiLight.commanding.events.impl.TabOneUpdateEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public class TabOneUpdateCommand implements ICommand<TabOneUpdateEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, TabOneUpdateEvent payload) {
        payload.T1_TXT_VerticalLeds.setText((settings.getVerticalRegions() - 2) + "");
        payload.T1_TXT_HorizontalLeds.setText(settings.getHorizontalRegions() + "");

        payload.T1_TXT_TotalLeds.setText(((settings.getVerticalRegions() * 2 + settings.getHorizontalRegions() * 2) - 4) + "");

        payload.T1_SLD_VerticalMarg.setValue(settings.getVerticalMargin());
        payload.T1_SLD_HorizontalMarg.setValue(settings.getHorizontalMargin());
    }
}