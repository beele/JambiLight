package be.beeles_place.jambiLight;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.commanding.commands.ICommand;

public class MockedCommand implements ICommand<MockedPayload> {

    @Override
    public void execute(SettingsModel settings, ColorModel model, MockedPayload payload) {
        payload.commandExecutionCounter++;
    }

}