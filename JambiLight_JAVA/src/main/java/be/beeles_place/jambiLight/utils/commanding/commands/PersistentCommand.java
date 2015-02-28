package be.beeles_place.jambiLight.utils.commanding.commands;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public abstract class PersistentCommand <T> implements ICommand {

    protected boolean isRunning = false;

    /**
     * This method will start the command execution and it will continue to execute again until it is stopped!
     *
     * @param settings Contains an injected instance of the SettingsModel class as known by the CommandMapper.
     * @param model Contains an injected instance of the ColorModel class as known by the CommandMapper.
     * @param payload Contains an injected instance of type defined type on the implementation of the command.
     */
    @SuppressWarnings("unchecked")
    public void start(SettingsModel settings, ColorModel model, T payload) {
        isRunning = true;
        while (isRunning) {
            execute(settings, model, payload);
        }
    }

    @SuppressWarnings("unchecked")
    public void execute(SettingsModel settings, ColorModel model, Object payload) {
        //We use the extra persistentExecute() method for the children of PersistentCommand because the execute() method cannot keep the type <T> after erasure!
        persistentExecute(settings, model, (T) payload);
    }

    /**
     * This method will be executed when the corresponding event is dispatched to the CommandMapper instance.
     *
     * @param settings Contains an injected instance of the SettingsModel class as known by the CommandMapper.
     * @param model Contains an injected instance of the ColorModel class as known by the CommandMapper.
     * @param payload Contains an injected instance of type defined type on the implementation of the command.
     */
    public abstract void persistentExecute(SettingsModel settings, ColorModel model, T payload);

    /**
     * This method will stop execution of the command at the start of the next execute iteration.
     */
    public void stop() {
        isRunning = false;
    }
}