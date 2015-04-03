package be.beeles_place.jambiLight.mocks;

import be.beeles_place.jambiLight.commanding.events.BaseEvent;
import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.commanding.commands.PersistentCommand;

public class MockedPersistentCommand extends PersistentCommand<BaseEvent> {

    @Override
    public void persistentExecute(SettingsModel settings, ColorModel model, BaseEvent payload) {
        super.stop();
    }
}