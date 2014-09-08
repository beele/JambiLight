package be.beeles_place.jambiLight.modes;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;

public interface IColorMode {

    /**
     * This method should be called before calling any other methods or using it in a thread.
     *
     * @param settings The object containing the application settings.
     * @param model The object containing the application model.
     */
    public void init(SettingsModel settings, ColorModel model);

    /**
     * This method will be called multiple times and should execute a single unit of work.
     */
    public void start();

    /**
     * This method will stop the mode after the current execution of the start method!
     */
    public void stop();
}
