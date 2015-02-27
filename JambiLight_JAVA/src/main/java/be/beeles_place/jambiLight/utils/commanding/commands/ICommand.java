package be.beeles_place.jambiLight.utils.commanding.commands;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public interface ICommand <T> {

    /**
     * This method will be executed when the corresponding event is dispatched to the CommandMapper instance.
     *
     * @param settings Contains an injected instance of the SettingsModel class as known by the CommandMapper.
     * @param model Contains an injected instance of the ColorModel class as known by the CommandMapper.
     * @param payload Contains an injected instance of type defined type on the implementation of the command.
     */
    public void execute(SettingsModel settings, ColorModel model, T payload);

}