package be.beeles_place.jambiLight.mocks;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.commanding.commands.ICommand;

public class MockedCommand implements ICommand<BaseEvent> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, BaseEvent payload) {

    }

}