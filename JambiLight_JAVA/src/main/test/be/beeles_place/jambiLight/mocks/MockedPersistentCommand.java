package be.beeles_place.jambiLight.mocks;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.commanding.commands.PersistentCommand;

public class MockedPersistentCommand extends PersistentCommand<MockedPayload> {

    @Override
    public void persistentExecute(SettingsModel settings, ColorModel model, MockedPayload payload) {
        payload.persistentCommandExecutionCounter++;
        super.stop();
    }
}