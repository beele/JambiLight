package be.beeles_place.jambiLight.utils.commanding.commands;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public abstract class PersistentCommand <T> implements ICommand {

    private boolean isRunning = false;

    @SuppressWarnings("unchecked")
    public void start(SettingsModel settings, ColorModel model, T payload) {
        isRunning = true;
        while (isRunning) {
            execute(settings, model, payload);
        }
    }

    @SuppressWarnings("unchecked")
    public void execute(SettingsModel settings, ColorModel model, Object payload) {
        persistentExecute(settings, model, (T) payload);
    }

    public abstract void persistentExecute(SettingsModel settings, ColorModel model, T payload);

    public void stop() {
        isRunning = false;
    }
}